package com.xiaoice.example.emotionlibrary_lite.admin;

import android.content.Context;
import android.graphics.Bitmap;

import com.xiaoice.example.emotionlibrary_lite.core.EmotionDetect;


public class EmotionDetectAgent implements EmotionDetectInterface {

    private Context context;

    public EmotionDetectAgent(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public EmotionDetectResult detect(Bitmap bitmap) {
        return EmotionDetect.getInstance(context).detect(bitmap);
    }
}
