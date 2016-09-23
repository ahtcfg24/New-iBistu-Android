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
        Bitmap bitmap = getSmallBitmap(imgPath);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);//质量压缩，参数100表示不压缩
        byte[] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    /**
     * 根据路径获得图片并压缩返回bitmap
     */
    private static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    private static int calculateInSampleSize(BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int size = height * width;
        int inSampleSize;//表示压缩为原图像素的1/inSampleSize
        if (size >= 921600) {
            inSampleSize = 4;
        } else if (size < 921600 && size >= 320000) {
            inSampleSize = 3;
        } else if (size < 320000 && size > 153600) {
            inSampleSize = 2;
        } else {
            inSampleSize = 1;
        }
        return inSampleSize;
    }

}
