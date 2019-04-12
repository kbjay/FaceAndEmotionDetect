package com.sbs.kb_jay.facedetectsdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbs.kb_jay.facelibrary.admin.FaceDetectAgent;
import com.sbs.kb_jay.facelibrary.admin.FaceDetectResult;
import com.xiaoice.example.emotionlibrary_lite.admin.EmotionDetectAgent;
import com.xiaoice.example.emotionlibrary_lite.admin.EmotionDetectResult;

import org.opencv.core.Rect;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView mTv = findViewById(R.id.tv_test);
        final TextView mTvEmotion = findViewById(R.id.tv_test1);
        final ImageView mIvtest1 = findViewById(R.id.iv_test);
        final ImageView mIvTest2 = findViewById(R.id.iv_test1);

        this.findViewById(R.id.bt_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.test, options);

                mIvtest1.setImageBitmap(bitmap);
                FaceDetectResult detect = new FaceDetectAgent(MainActivity.this).detect(bitmap);

                if (detect.isHasFace()) {
                    Rect rect = detect.getFaceArray()[0];
                    Bitmap faceBitmap = Bitmap.createBitmap(bitmap, rect.x, rect.y, rect.width, rect.height);
                    EmotionDetectResult emotionDetect = new EmotionDetectAgent(MainActivity.this).detect(faceBitmap);
                    mTvEmotion.setText(emotionDetect.toString());
                }
            }
        });
    }
}
