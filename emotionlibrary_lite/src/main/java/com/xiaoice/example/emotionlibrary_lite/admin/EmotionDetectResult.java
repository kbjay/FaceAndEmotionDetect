package com.xiaoice.example.emotionlibrary_lite.admin;

import java.util.ArrayList;

public class EmotionDetectResult {

    public String EmotionType;

    public ArrayList<EmotionItem> Emotion;

    public static class EmotionItem{
        public String Key;
        public float Value;

        @Override
        public String toString() {
            return "EmotionItem{" +
                    "Key='" + Key + '\'' +
                    ", Value=" + Value +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "EmotionDetectResult{" +
                "EmotionType='" + EmotionType + '\'' +
                ", Emotion=" + Emotion +
                '}';
    }
}
