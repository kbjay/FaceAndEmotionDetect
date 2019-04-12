package com.xiaoice.example.emotionlibrary_lite.admin;

import android.graphics.Bitmap;

public interface EmotionDetectInterface {
    /**
     * @param bitmap 最好是只包含人脸区域的bitmap
     * @return
     */
    EmotionDetectResult detect(Bitmap bitmap);
}
