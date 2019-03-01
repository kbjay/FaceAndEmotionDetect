package com.sbs.kb_jay.facelibrary.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.YuvImage;
import android.hardware.Camera;

import com.sbs.kb_jay.facelibrary.R;
import com.sbs.kb_jay.facelibrary.admin.FaceDetectInterface;
import com.sbs.kb_jay.facelibrary.admin.FaceDetectResult;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.opencv.objdetect.Objdetect.CASCADE_SCALE_IMAGE;

public class FaceDetect implements FaceDetectInterface {
    private int mAbsoluteFaceSize = 0;
    private CascadeClassifier classifier;
    private static final String modelDir = "cascade";
    private static final String modelName = "haarcascade_frontalface.xml";
    private static final float detectScaleFactor = 1.3f;
    private static final int detectMinNeighbors = 2;
    private static final float detectRelativeFaceSize = 0.2f;

    static {
        System.loadLibrary("opencv_java3");
    }

    private Bitmap bmp;
    private Bitmap resizedBitmap;

    private FaceDetect(Context context) {
        init(context);
    }

    private static FaceDetect instance;

    public static FaceDetect getInstance(Context context) {
        if (instance == null) {
            synchronized (FaceDetect.class) {
                if (instance == null) {
                    instance = new FaceDetect(context.getApplicationContext());
                }
            }
        }
        return instance;
    }


    void init(Context context) {
        context = context.getApplicationContext();

        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = context.getResources()
                    .openRawResource(R.raw.haarcascade_frontalface);
            File cascadeDir = context.getDir(modelDir, Context.MODE_PRIVATE);
            File cascadeFile = new File(cascadeDir, modelName);
            os = new FileOutputStream(cascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            classifier = new CascadeClassifier(cascadeFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public FaceDetectResult detect(Bitmap bitmap) {
        if (bitmap == null) {
            throw new RuntimeException("args->bitmap can not be null");
        }
        if (classifier == null) {
            throw new RuntimeException("init classifier failed");
        }
        Mat image = new Mat();
        Utils.bitmapToMat(bitmap, image);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        MatOfRect facesMat = new MatOfRect();
        float mRelativeFaceSize = detectRelativeFaceSize;
        if (mAbsoluteFaceSize == 0) {
            int height = image.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }

        classifier.detectMultiScale(image, facesMat, detectScaleFactor, detectMinNeighbors, CASCADE_SCALE_IMAGE, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());

        Rect[] rects = facesMat.toArray();

        FaceDetectResult faceDetectResult = new FaceDetectResult();
        if (rects.length < 0) {
            faceDetectResult.setHasFace(false);
            faceDetectResult.setFaceArray(null);
        } else {
            faceDetectResult.setHasFace(true);
            faceDetectResult.setFaceArray(rects);
        }
        return faceDetectResult;
    }

    @Override
    public FaceDetectResult detect(byte[] cameraData, Camera camera) {
        if (cameraData == null) {
            throw new RuntimeException("args->cameraData can not be null");
        }
        if (camera == null) {
            throw new RuntimeException("args->camera can not be null");
        }
        Camera.Size size = camera.getParameters().getPreviewSize();
        YuvImage image = new YuvImage(cameraData, ImageFormat.NV21, size.width, size.height, null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compressToJpeg(new android.graphics.Rect(0, 0, size.width, size.height), 100, stream);
        bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        resizedBitmap = Bitmap.createBitmap(bmp, 0, 0,
                bmp.getWidth(), bmp.getHeight(), matrix, true);
        return detect(resizedBitmap);
    }
}
