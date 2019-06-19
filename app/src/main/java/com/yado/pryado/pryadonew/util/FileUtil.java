package com.yado.pryado.pryadonew.util;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by Administrator on 2018/2/27.
 */

public class FileUtil {

    public final static String FILE_EXTENSION_SEPARATOR = ".";

    public static void write(Context context, String fileName, String text, boolean type){
        File file = new File(context.getApplicationContext().getFilesDir().getAbsolutePath(), fileName);
        if (!file.exists()){
            try {
                file.createNewFile();
                String path = file.getPath();
                FileOutputStream fos = new FileOutputStream(path, type);  //true：追加
                fos.write(text.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                FileOutputStream fos = new FileOutputStream(file.getPath(), type);
                fos.write(text.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除指定目录中特定的文件
     *
     * @param dir
     * @param filter
     */
    public static void delete(String dir, FilenameFilter filter) {
        if (TextUtils.isEmpty(dir))
            return;
        File file = new File(dir);
        if (!file.exists())
            return;
        if (file.isFile())
            file.delete();
        if (!file.isDirectory())
            return;

        File[] lists = null;
        if (filter != null)
            lists = file.listFiles(filter);
        else
            lists = file.listFiles();

        if (lists == null)
            return;
        for (File f : lists) {
            if (f.isFile()) {
                f.delete();
            }
        }
    }

    /**
     * 获得不带扩展名的文件名称
     *
     * @param filePath 文件路径
     * @return
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);

        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }
}
