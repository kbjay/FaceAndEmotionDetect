package com.sbs.kb_jay.emotionlibrary.core;

import android.content.Context;
import android.graphics.Bitmap;

import com.sbs.kb_jay.emotionlibrary.admin.EmotionDetectInterface;
import com.sbs.kb_jay.emotionlibrary.admin.EmotionDetectResult;
import com.sbs.kb_jay.emotionlibrary.core.tensor.TensorImpl;
import com.sbs.kb_jay.emotionlibrary.core.tensor.TensorInterface;

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
                EmotionModelConfig.inputName,
                EmotionModelConfig.outputName,
                EmotionModelConfig.labels,
                EmotionModelConfig.dims,
                EmotionModelConfig.inputSize,
                EmotionModelConfig.channal);
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
