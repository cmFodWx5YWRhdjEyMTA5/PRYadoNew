package com.yado.pryado.pryadonew.ui.widgit;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URI;

public class PlanInfoClient extends WebViewClient {

    private int pid;
    private String videourl;

    String NetErrorurl = "file:///android_asset/NetError.html";

    public PlanInfoClient(int pid, String videourl) {
        this.pid = pid;
        this.videourl = videourl;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        view.loadUrl("javascript:window.android.getSource('<head>'+" +
                "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        super.onPageFinished(view, url);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        // Handle the error
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return;
        }
        if (errorCode == -2) {
            toNetError(view);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
        // Redirect to deprecated method, so you can use it in all SDK versions
        super.onReceivedError(view, req, rerr);
        onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
    }

    private void toNetError(WebView view) {
        view.stopLoading();
        view.loadUrl(NetErrorurl);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.contains("DeviceInfo?") || url.contains("DeviceManage")) {
            //http://113.106.90.51:8006/Monitor/DeviceInfo?sid=0&did=144&pid=8
            String did = url.substring(url.lastIndexOf("did=") + 4, url.lastIndexOf("&"));
            ARouter.getInstance().build(MyConstants.DEVICE_DETAIL)
                    .withString("did", did)
                    .withInt("pid", pid)
                    .navigation();
//            Intent intent = new Intent(activity, DeviceDetailsActivity.class);
//            intent.putExtra("did", did);
//            intent.putExtra("pid", pid);
//            activity.startActivity(intent);
            return true;
        }
        if (url.contains("InfraredInfo?")) {//弹出红外照片的窗口； //http://113.106.90.51:8004/Monitor/InfraredInfo?pid=8
            String pid = url.substring(url.lastIndexOf("pid=") + 4, url.length());
            //isBackToUrl = true;
            return true;
        }

        if (url.contains("RealTimeVideo")) {
//            ARouter.getInstance().build(MyConstants.VIDEO)
//                    .withString("web_url", url)
//                    .withString("video_url", videourl)
//                    .navigation();
            return true;
        }
        view.loadUrl(url);
        return true;
    }

//    @Nullable
//    @Override
//    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//        if(url.contains("cewen-plan.js")){//加载指定.js时 引导服务端加载本地Assets/www文件夹下的cordova.js
//            try {
//                return new WebResourceResponse("application/x-javascript","utf-8", MyApplication.getContext().getAssets().open("js/cewen-plan.js"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return super.shouldInterceptRequest(view, url);
//    }
//
//
//    @Override
//    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Uri url = request.getUrl();
//            if(url.toString().contains("cewen-plan.js")){//加载指定.js时 引导服务端加载本地Assets/www文件夹下的cordova.js
//                try {
//                    return new WebResourceResponse("application/x-javascript","utf-8",MyApplication.getContext().getAssets().open("js/cewen-plan.js"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return super.shouldInterceptRequest(view, request);
//    }

}
