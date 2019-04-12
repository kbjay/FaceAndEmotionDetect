package com.xiaoice.example.emotionlibrary_lite.core;

import android.content.Context;
import android.graphics.Bitmap;

import com.xiaoice.example.emotionlibrary_lite.admin.EmotionDetectInterface;
import com.xiaoice.example.emotionlibrary_lite.admin.EmotionDetectResult;
import com.xiaoice.example.emotionlibrary_lite.core.tensor.TensorImpl;
import com.xiaoice.example.emotionlibrary_lite.core.tensor.TensorInterface;

public class EmotionDetect implements EmotionDetectInterface {

    TensorInterface tensorInterface;

    private EmotionDetect(Context context) {
        initTensor(context);
    }

    private static EmotionDetect instance;

    public static EmotionDetect getInstance(Context context) {
        if (instance == null) {
            synchronized (EmotionDetect.class) {
                if (instance == null) {
                    instance = new EmotionDetect(context);
                }
            }
        }
        return instance;
    }

    private void initTensor(Context context) {
        tensorInterface = TensorImpl.create(context.getApplicationContext(),
                EmotionModelConfig.modelName,
                EmotionModelConfig.labels,
                EmotionModelConfig.inputSize
        );
    }

    @Override
    public EmotionDetectResult detect(Bitmap bitmap) {
        if (bitmap == null) {
            throw new RuntimeException("args->bitmap can not be null");
        }
        if (tensorInterface == null) {
            throw new RuntimeException("tensor init failed");
        }
        //handle image
        Bitmap grayBitmap = ImageUtils.convertGreyImg(bitmap);
        Bitmap scaleBitmap = ImageUtils.getScaleBitmap(grayBitmap, EmotionModelConfig.inputSize);
        //tensor
        return tensorInterface.recognizeImage(scaleBitmap);
    }
}
