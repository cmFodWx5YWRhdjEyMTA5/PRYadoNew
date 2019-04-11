package com.yado.pryado.pryadonew.util;

public interface DownloadListener {
    void startDownload();

    void stopDownload();

    void finishDownload();

    void downloadProgress(long progress);
}

