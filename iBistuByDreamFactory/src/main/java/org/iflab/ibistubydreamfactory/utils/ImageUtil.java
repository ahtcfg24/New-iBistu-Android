package org.iflab.ibistubydreamfactory.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 *
 */
public class ImageUtil {
    /**
     * 将图片压缩后转换成Base64字符串
     *
     * @param imgPath 图片路径
     */
    public static String ImageToBase64Content(String imgPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//设为true时，options.outHeight返回的是原图的尺寸
        BitmapFactory.decodeFile(imgPath, options);
        int size = options.outWidth * options.outHeight;
        if (size <= 1000000) {//小于一百万像素
            options.inSampleSize = 1;//压缩尺寸，压缩原来尺寸的1/inSampleSize
        } else if (size > 1000000 && size <= 3000000) {//一百万像素-三百万像素
            options.inSampleSize = 2;
        } else if (size > 3000000 && size <= 6000000) {
            options.inSampleSize = 3;
        } else {
            options.inSampleSize = 4;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);//压缩质量，100表示不压缩
        byte[] bytes = outputStream.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


}
