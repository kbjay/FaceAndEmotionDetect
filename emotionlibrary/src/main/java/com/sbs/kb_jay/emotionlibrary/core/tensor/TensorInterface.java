package com.sbs.kb_jay.emotionlibrary.core.tensor;

import android.graphics.Bitmap;

import com.sbs.kb_jay.emotionlibrary.admin.EmotionDetectResult;

public interface TensorInterface {
    EmotionDetectResult recognizeImage(Bitmap bitmap);
}
