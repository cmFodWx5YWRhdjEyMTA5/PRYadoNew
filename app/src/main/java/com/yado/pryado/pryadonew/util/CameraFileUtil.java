package com.yado.pryado.pryadonew.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Eric on 2016/3/14.
 */
public class CameraFileUtil {

    private static final String TAG = "CameraFileUtil";
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = "";
    private static final String DST_FOLDER_NAME = "/PReado/";

    /**
     * @return storagePath
     */
    public static String initPath() {
        if ("".equals(storagePath)) {
            storagePath = parentPath.getAbsolutePath() + "/" + Environment.DIRECTORY_DCIM +  DST_FOLDER_NAME;
            File file = new File(storagePath);
            if (!file.exists()) {
                boolean isMkdir = file.mkdir();
                Log.i(TAG, "isMkdir: " + isMkdir);
            }
        }
        return storagePath;
    }

    /**
     * Bitmap sdcard
     * @param bmp bitmap to save
     */
    public static void saveBitmap(Bitmap bmp) {

        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake + ".jpg";
        Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
        try {
            FileOutputStream fos = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i(TAG, "saveBitmap:success");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "saveBitmap:failure");
            e.printStackTrace();
        }

    }

}
