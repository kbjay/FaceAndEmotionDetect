package com.sbs.kb_jay.emotionlibrary.core.tensor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.sbs.kb_jay.emotionlibrary.admin.EmotionDetectResult;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.ArrayList;

public class TensorImpl implements TensorInterface {

    private TensorFlowInferenceInterface tensorFlowInferenceInterface;
    private int inputSize;
    private String inputName;
    private int dims;
    private int channal;
    private String[] labels;
    private String[] outputNames;
    private float[] outputs;
    private String outputName;

    private TensorImpl() {
    }

    public static TensorInterface create(
            Context context,
            String modelName,
            String inputName,
            String outputName,
            String[] labels,
            int dims,
            int size,
            int channal
    ) {
        TensorImpl tensor = new TensorImpl();
        tensor.tensorFlowInferenceInterface = new TensorFlowInferenceInterface(context.getApplicationContext().getAssets(), modelName);
        tensor.inputSize = size;
        tensor.inputName = inputName;
        tensor.dims = dims;
        tensor.channal = channal;
        tensor.labels = labels;
        tensor.outputNames = new String[]{outputName};
        tensor.outputs = new float[labels.length];
        tensor.outputName = outputName;
        return tensor;
    }

    @Override
    public EmotionDetectResult recognizeImage(Bitmap bitmap) {
        float[] input = new float[inputSize * inputSize];
        int[] ints = new int[inputSize * inputSize];
        bitmap.getPixels(ints, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < ints.length; i++) {
            input[i] = (float) (Color.red(ints[i]) / 255.0);
        }
        tensorFlowInferenceInterface.feed(inputName, input, dims, inputSize, inputSize, channal);
        tensorFlowInferenceInterface.run(outputNames, false);
        tensorFlowInferenceInterface.fetch(outputName, outputs);

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
