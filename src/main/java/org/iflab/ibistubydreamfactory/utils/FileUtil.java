package org.iflab.ibistubydreamfactory.utils;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class FileUtil {
    public static String ImageToBase64Content(String imgPath) {
        String base64Content = "";
        File imageFile = new File(imgPath);
        try {
            InputStream inputStream = new FileInputStream(imageFile);
            byte[] buffer = new byte[(int) imageFile.length()];
            inputStream.read(buffer);
            inputStream.close();
            base64Content = Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Content;
    }
}
