package com.yado.pryado.pryadonew.ui.widgit;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by eado on 2017/1/11.
 */

public class MyWebChromeClient extends WebChromeClient {

    String PermissionErrorurl = "file:///android_asset/PermissionError.html";

    @Override
    public void onReceivedTitle(WebView view, String title) {
        Message message = Message.obtain();
        if (!TextUtils.isEmpty(title) && (title.contains("resource cannot be found") || title.contains("无法") || title.contains("未找到") || title.contains("运行时")))  //mod by cww 20180604
        {
            message.what = 0;
            toNoPermmition(view);
            ToastUtils.showLong("如果加载出错，可以试着返回首页调试一下兼容模式,或点击切换刷新一下平面图！");
        } else {
            message.what = 1;
        }
        if (!view.getTitle().contains("无权访问")) {
            EventBus.getDefault().post(message);
        }
        super.onReceivedTitle(view, title);
    }

    private void toNoPermmition(WebView view) {
        view.stopLoading();
        view.loadUrl(PermissionErrorurl);
    }

}
