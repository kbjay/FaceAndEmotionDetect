package com.xiaoice.example.emotionlibrary_lite.core;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.IOException;

public class ImageUtils {
    /**
     * 图片转成灰度图
     *
     * @param img
     * @return
     */
    public static Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();			//获取位图的宽
        int height = img.getHeight();		//获取位图的高

        int []pixels = new int[width * height];	//通过位图的大小创建像素点数组

        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for(int i = 0; i < height; i++)	{
            for(int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey  & 0x00FF0000 ) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int)((float) red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }
    /**
     * 对图片进行缩放
     *
     * @param bitmap
     * @param size
     * @return
     * @throws IOException
     */
    public static Bitmap getScaleBitmap(Bitmap bitmap, int size) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int startx = width > height ? (width - height) / 2 : 0;
        int starty = width > height ? 0 : (height - width) / 2;
        int cropSize = width > height ? height : width;

        float scaleSize = ((float) size) / cropSize;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleSize, scaleSize);
        return Bitmap.createBitmap(bitmap, startx, starty, cropSize, cropSize, matrix, true);
    }
}
