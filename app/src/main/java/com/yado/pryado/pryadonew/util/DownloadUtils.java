package com.yado.pryado.pryadonew.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DownloadUtils {
    private static volatile DownloadUtils instance;
    private OkHttpClient client;
    private DownloadListener mlistener;
    private File file;
    private String fileAbsolutePath;
    public File downloadFile;
    private long startPosition;
    private Call call;

    private DownloadUtils() {
        client = new OkHttpClient();
    }

    public void setListener(DownloadListener listener) {
        this.mlistener = listener;
    }

    /**
     * 初始化下载父路径
     *
     * @return
     */
    public void initDownload(String path) {
        file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        if (!file.exists()) {
            file.mkdir();
        }
//        if(!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        fileAbsolutePath = file.getAbsolutePath();
        Log.d("DownloadUtils", fileAbsolutePath.toString());
    }

    public void reset() {
        if (call != null) {
            call.cancel();
            call = null;
            client = null;
        }
        instance = null;
    }

    public static DownloadUtils getInstance() {
        if (instance == null) {
            synchronized (DownloadUtils.class) {
                if (instance == null) {
                    instance = new DownloadUtils();
                }
            }
        }
        return instance;
    }

    public void startDownload(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (url.contains(".")) {
            String typename = url.substring(url.lastIndexOf(".") + 1);
            if (url.contains("/")) {
                String filename = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
                String fn = filename + "." + typename;
                downloadFile = new File(this.file, fn);
                if (downloadFile.exists() && downloadFile.isFile()) {
                    if (downloadFile.length() > (1024 * 1024 * 10.66)) {
                        mlistener.finishDownload();
                        return;
                    }
                    Log.e("yuong", downloadFile.length() + "........");
                }
                Log.d("DownloadUtils", "downloadFile" + downloadFile.toString());
            }
        }
        startPosition = 0;
        if (downloadFile.exists()) {
            startPosition = downloadFile.length();
        }
        final Request request = new Request.Builder()
                .addHeader("RANGE", "bytes=" + startPosition + "-")
                .url(url)
                .build();
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mlistener.startDownload();
                ResponseBody body = response.body();
//                startPosition
                long totalLength = body.contentLength();
                Log.d("DownloadUtils", "totalLength: " + totalLength + "----");
                InputStream is = body.byteStream();
                byte[] bytes = new byte[2048];
                int len = 0;
                long totalNum = startPosition;
                RandomAccessFile raf = new RandomAccessFile(downloadFile, "rw");
                while ((len = is.read(bytes, 0, bytes.length)) != -1) {
                    raf.seek(totalNum);
                    raf.write(bytes, 0, len);
                    totalNum += len;
                    mlistener.downloadProgress(totalNum * 100 / totalLength);

                }
                mlistener.finishDownload();
                body.close();
            }
        });
    }

    public void stopDownload() {
        mlistener.startDownload();
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
    }
}
