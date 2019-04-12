package com.xiaoice.example.emotionlibrary_lite.core.tensor;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.xiaoice.example.emotionlibrary_lite.admin.EmotionDetectResult;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class TensorImpl implements TensorInterface {

    private Interpreter tensorFlowInferenceInterface;
    private int inputSize;
    private String[] labels;
    private float[][] tfOutputs;

    private TensorImpl() {
    }

    public static TensorInterface create(
            Context context,
            String modelName,
            String[] labels,
            int size
    ) {
        TensorImpl tensor = new TensorImpl();
        tensor.tensorFlowInferenceInterface = loadModel(context.getApplicationContext().getAssets(), modelName);
        tensor.inputSize = size;
        tensor.labels = labels;
        tensor.tfOutputs = new float[1][labels.length];
        return tensor;
    }

    private static Interpreter loadModel(AssetManager assetManager, String model) {
        Interpreter tflite = new Interpreter(loadModelFile(assetManager, model));
        tflite.setNumThreads(4);
        return tflite;
    }

    private static MappedByteBuffer loadModelFile(AssetManager assets, String modelName) {
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = assets.openFd(modelName);
        } catch (IOException e) {
            throw new RuntimeException("model not find!!");
        }
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        try {
            MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
            return map;
        } catch (IOException e) {
            throw new RuntimeException("model not find!!");
        }
    }


    @Override
    public EmotionDetectResult recognizeImage(Bitmap bitmap) {
        tensorFlowInferenceInterface.run(getScaledMatrix(bitmap, inputSize), tfOutputs);

        float[] outputs = tfOutputs[0];
        probs(outputs);

        EmotionDetectResult emotionDetectResult = new EmotionDetectResult();

        ArrayList<EmotionDetectResult.EmotionItem> emotionItems = new ArrayList<>();

        String emotionType = null;

        float max = Float.MIN_VALUE;

        for (int i = 0; i < outputs.length; i++) {
            EmotionDetectResult.EmotionItem emotionItem = new EmotionDetectResult.EmotionItem();
            emotionItem.Key = labels[i];
            emotionItem.Value = outputs[i];
            emotionItems.add(emotionItem);

            if (max < outputs[i]) {
                emotionType = labels[i];
                max = outputs[i];
            }
        }

        emotionDetectResult.Emotion = emotionItems;
        emotionDetectResult.EmotionType = emotionType;

        return emotionDetectResult;
    }

    public static ByteBuffer getScaledMatrix(Bitmap bitmap, int inputSize) {
        ByteBuffer imgData = ByteBuffer.allocateDirect(inputSize * inputSize * 4);
        imgData.order(ByteOrder.nativeOrder());
        int[] pixels = new int[inputSize * inputSize];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, inputSize, inputSize);
        int pixel = 0;
        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                final int val = pixels[pixel++];
                int red = Color.red(val);
                imgData.putFloat((float) (red / 255.0));
            }
        }

        if (bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return imgData;
    }

    private float[] probs(float[] outputs) {
        //计算和，调整outputs
        float sum = 0f;
        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = (float) Math.pow(Math.E, outputs[i]);
            sum += outputs[i];
        }
        //计算输出
        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = outputs[i] / sum;
        }
        return outputs;
    }
}
