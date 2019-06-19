package com.yado.pryado.pryadonew.net;

import android.os.Environment;

import java.io.File;

/**
 * Created by eado on 2016/4/12.
 */
public interface EadoUrl {
    String BASE_URL_WEB = "http://121.201.108.27:8008";
    // 默认存放文件下载的路径
    String DEFAULT_SAVE_FILE_PATH = Environment.getExternalStorageDirectory()
            + File.separator
            + "preado"
            + File.separator + "download" + File.separator;
}
