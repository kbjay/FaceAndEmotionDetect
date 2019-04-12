package com.xiaoice.example.emotionlibrary_lite.core.tensor;

import android.graphics.Bitmap;

import com.xiaoice.example.emotionlibrary_lite.admin.EmotionDetectResult;

public interface TensorInterface {
    EmotionDetectResult recognizeImage(Bitmap bitmap);
}
