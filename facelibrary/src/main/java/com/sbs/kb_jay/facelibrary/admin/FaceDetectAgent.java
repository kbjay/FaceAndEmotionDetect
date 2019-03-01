package com.sbs.kb_jay.facelibrary.admin;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;

import com.sbs.kb_jay.facelibrary.core.FaceDetect;

public class FaceDetectAgent implements FaceDetectInterface {

    Context context;

    public FaceDetectAgent(Context context){
        this.context=context;
    }

    @Override
    public FaceDetectResult detect(Bitmap bitmap) {
       return FaceDetect.getInstance(context).detect(bitmap);
    }

    @Override
    public FaceDetectResult detect(byte[] cameraData, Camera camera) {
        return FaceDetect.getInstance(context).detect(cameraData,camera);
    }
}
