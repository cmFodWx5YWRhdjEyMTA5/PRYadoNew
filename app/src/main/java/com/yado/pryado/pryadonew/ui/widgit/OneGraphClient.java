package com.yado.pryado.pryadonew.ui.widgit;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.net.EadoUrl;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import java.io.IOException;

/**
 * Created by eado on 2017/1/9.
 */

public class OneGraphClient extends WebViewClient {
    private Activity activity;
    private int pid;
    public static int PLAN_INFO = 1;//平面图；
    public static int ONE_GRAPH = 2;//一次图；

    String NetErrorurl = "file:///android_asset/NetError.html";

    public OneGraphClient(Activity activity, int pid) {
        this.activity = activity;
        this.pid = pid;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        toNetError(view);
    }

    private void toNetError(WebView view) {
        view.stopLoading();
        view.loadUrl(NetErrorurl);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.contains("Login")) {
            view.loadUrl(getWebUrl(pid, ONE_GRAPH));
            return true;
        }
        if (url.contains("DeviceInfo") || url.contains("DeviceManage")) {//http://113.106.90.51:8004/Monitor/DeviceInfo?sid=1&pid=6&did=82，一次图的；
            //http://113.106.90.51:8006/Monitor/DeviceInfo?sid=0&did=144&pid=8，平面图的；
            //http://113.106.90.51:8008/DeviceManage/Index?sid=0&did=471&pid=null
            //http://113.106.90.51:8008/DeviceManage/Index?sid=0&did=1020,1021&pid=1，特殊的一多展示用柜；
            String did;
            if (url.contains("DeviceInfo")) {
                did = url.substring(url.lastIndexOf("did=") + 4, url.length());
            } else {
                did = url.substring(url.lastIndexOf("did=") + 4, url.lastIndexOf("&pid"));
            }

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
        view.loadUrl(url);
        return true;
    }

    private String getWebUrl(int pid, int type) {
        if (type == PLAN_INFO) {//平面图;
//            return SharedPrefUtil.getInstance(activity).getString(Constant.BASE_url, EadoUrl.BASE_URL_WEB) + "/AppPlan/PlanInfo" + pid;
            return SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/AppPlan/PlanInfo?id=" + pid;  //mod by cww 20180604
        } else {
//            return SharedPrefUtil.getInstance(activity).getString(Constant.BASE_url, EadoUrl.BASE_URL_WEB) + "/AppPlan/OneGraph" + pid;
            return SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/AppPlan/OneGraph?id=" + pid;  //mod by cww 20180604
        }
    }


}