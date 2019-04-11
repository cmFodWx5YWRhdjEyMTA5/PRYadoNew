package com.yado.pryado.pryadonew.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.TypeBean;
import com.yado.pryado.pryadonew.net.EadoUrl;
import com.yado.pryado.pryadonew.ui.roomDetail.RoomDetailActivity;
import com.yado.pryado.pryadonew.ui.widgit.MyWebView;
import com.yado.pryado.pryadonew.ui.widgit.OneGraphClient;
import com.yado.pryado.pryadonew.ui.widgit.PlanInfoClient;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import org.angmarch.views.NiceSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/WebViewActivity")
public class WebViewActivity extends BaseActivity {

    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
//    @BindView(R.id.webview)
    MyWebView webview;

    @Autowired(name = "pid")
    public int pid;
    @Autowired(name = "type")
    public int type;
    @Autowired(name = "video_url")
    public String videoUrl;
    @Autowired(name = "graph")
    public TypeBean graph;
    private String url;
    private WebViewClient client;

    @Override
    public int inflateContentView() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initData() {
        initWebView();
        tvPre.setText(getResources().getString(R.string.back));
        webview.setOnFlingListner(new MyWebView.OnFlingListner() {
            @Override
            public void onFling() {
                changeView(++type);
            }
        });
        changeView(type);
    }

    private void changeView(int type) {
        if (graph != null && Integer.parseInt(graph.getHymannew().get(0).getTotal2()) > 0) { //非介入式平面图
            name.setText(getResources().getString(R.string.planinfo));
            url = SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/NonIntrusive/PlanInfo/" + pid;
            client = new OneGraphClient((Activity) mContext, pid);
        } else {
            if (SharedPrefUtil.getInstance(MyApplication.getInstance()).getT("Compatibility", false)) {
                if (type % 2 == 0) {
                    name.setText(getResources().getString(R.string.onegraph));
                    url = SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/AppPlan/OneGraph?id=" + pid;
                    client = new OneGraphClient((Activity) mContext, pid);
                } else {
                    name.setText(getResources().getString(R.string.planinfo));
                    url = SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/AppPlan/PlanInfo?id=" + pid;  //mod by cww 20180604
                    client = new PlanInfoClient(pid, "");

                }
            } else {
                if (type % 2 == 0) {
                    name.setText(getResources().getString(R.string.onegraph));
                    url = SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/PDRInfo/OneGraph/" + pid;
                    client = new OneGraphClient((Activity) mContext, pid);
                } else {
                    name.setText(getResources().getString(R.string.planinfo));
                    url = SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/PDRInfo/PlanInfo/" + pid;  //mod by cww 20180604
                    client = new PlanInfoClient(pid, "");
                }
            }
        }
        webview.loadUrl(url);
        webview.setWebViewClient(client);

    }

    @SuppressLint({"SetJavaScriptEnabled", "WrongConstant"})
    private void initWebView() {
        if (webview == null) {
            webview = new MyWebView(mContext);
            webview = new MyWebView(getApplicationContext());
            llContent.addView(webview);
        }
        // 设置可以支持缩放
        webview.getSettings().setSupportZoom(false);
        // 设置出现缩放工具
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);

        //设置滚动条不可见
        webview.setVerticalScrollBarEnabled(false);
        webview.setHorizontalScrollBarEnabled(false);

        //扩大比例的缩放
        webview.getSettings().setUseWideViewPort(true);

        //自适应屏幕
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        //设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new JavaScriptInterface(), "android");

        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT | WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.setOnFlingListner(new MyWebView.OnFlingListner() {
            @Override
            public void onFling() {
                changeView(++type);
            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected boolean isNeedInject() {
        return true;
    }


    @OnClick(R.id.tv_shouye)
    public void onViewClicked() {
        finish();
    }

    public class JavaScriptInterface {
        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        public void getDID(String did) {
            Log.e("did", did);

            ARouter.getInstance().build(MyConstants.DEVICE_DETAIL)
                    .withString("did", did)
                    .withInt("pid", pid)
                    .navigation();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webview != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            webview.clearCache(true);
            ViewParent parent = webview.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webview);
            }
            webview.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webview.getSettings().setJavaScriptEnabled(false);
            webview.clearHistory();
            webview.clearView();
            webview.removeAllViews();
            webview.destroy();
        }
    }
}
