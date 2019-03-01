package com.sbs.kb_jay.facelibrary.admin;

import org.opencv.core.Rect;

import java.util.Arrays;

public class FaceDetectResult {
    boolean hasFace;
    Rect[] faceArray;

    public boolean isHasFace() {
        return hasFace;
    }

    public void setHasFace(boolean hasFace) {
        this.hasFace = hasFace;
    }

    public Rect[] getFaceArray() {
        return faceArray;
    }

    public void setFaceArray(Rect[] faceArray) {
        this.faceArray = faceArray;
    }

    @Override
    public String toString() {
        return "FaceDetectResult{" +
                "hasFace=" + hasFace +
                ", faceArray=" + Arrays.toString(faceArray) +
                '}';
    }
}
