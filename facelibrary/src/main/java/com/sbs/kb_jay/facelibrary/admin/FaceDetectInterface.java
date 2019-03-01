package com.sbs.kb_jay.facelibrary.admin;

import android.graphics.Bitmap;
import android.hardware.Camera;

public interface FaceDetectInterface {
    /**
     * detect from bitmap
     *
     * @param bitmap src
     * @return
     */
    FaceDetectResult detect(Bitmap bitmap);

    /**
     * detect from camera
     *
     * @param cameraData src
     * @param camera
     * @return
     */
    FaceDetectResult detect(byte[] cameraData, Camera camera);
}
