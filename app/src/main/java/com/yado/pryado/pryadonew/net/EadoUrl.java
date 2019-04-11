package com.yado.pryado.pryadonew.net;

import android.os.Environment;

import java.io.File;

/**
 * Created by eado on 2016/4/12.
 */
public interface EadoUrl {
    //老的：http://113.106.90.51:8004/
    //新的：http://113.106.90.51:8008/
    //测试：http://192.168.1.15:80/
    //      http:192.168.1.15:80
    String BASE_URL_WEB = "http://120.227.0.10:8081";
    // 默认存放文件下载的路径
    String DEFAULT_SAVE_FILE_PATH = Environment.getExternalStorageDirectory()
            + File.separator
            + "preado"
            + File.separator + "download" + File.separator;
}
