package com.yado.pryado.pryadonew.ui.temp_monitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.blankj.utilcode.util.SizeUtils;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseFragment;
import com.yado.pryado.pryadonew.bean.NonIntrusiveEvent;
import com.yado.pryado.pryadonew.net.EadoUrl;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.ui.widgit.MyWebChromeClient;
import com.yado.pryado.pryadonew.ui.widgit.PlanInfoClient;
import com.yado.pryado.pryadonew.util.SharedPrefUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TempMonitorPlanFragment extends BaseFragment<TempMonitorPresent> implements TempMonitorContract.View {
    public static int PLAN_INFO = 1;//平面图；
    public static int ONE_GRAPH = 2;//一次图；
    //    @BindView(R.id.webview)
//    WebView webview;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.sv_content)
    ScrollView svContent;
    @BindView(R.id.ll_web)
    LinearLayout llWeb;
//    @BindView(R.id.webview2)
//    WebView webview2;

    private WebView webview, webview2;

    private int pid;
    private WebViewClient client;
    private List<String> mDids;
    private List<String> mStations;
    private NonIntrusiveEvent event;
    private WebChromeClient webChromeClient;
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    private String cacheDirPath;
    private static final String TAG = TempMonitorPlanFragment.class.getSimpleName();

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_temp_monitor_plan;
    }

    public static TempMonitorPlanFragment newInstance(int pid) {
        Bundle bundle = new Bundle();
        bundle.putInt("pid", pid);
        TempMonitorPlanFragment fragment = new TempMonitorPlanFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private String getWebUrl(int pid, int type) {
//        return "http://113.106.90.51:8008/Monitor/AppPlanInfo?pid=211";
        String url;
        if (type == PLAN_INFO) {//平面图;
//            return SharedPrefUtil.getInstance(this).getString(Constant.BASE_url, EadoUrl.BASE_URL_WEB) + "/AppPlan/PlanInfo" + pid;
            url = SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/NonIntrusive/PlanInfo/" + pid;  //mod by cww 20180604
        } else {
//            return SharedPrefUtil.getInstance(this).getString(Constant.BASE_url, EadoUrl.BASE_URL_WEB) + "/AppPlan/OneGraph" + pid;
            url = SharedPrefUtil.getInstance(MyApplication.getInstance()).getString(MyConstants.BASE_URL, EadoUrl.BASE_URL_WEB) + "/AppPlan/OneGraph?id=" + pid;  //mod by cww 20180604
        }
        return url;
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void initView() {

    }

    public void isHide(boolean isShow) {
        webview2.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void loadData() {
        cacheDirPath = context.getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        assert getArguments() != null;
        pid = getArguments().getInt("pid");
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        initWebView();
        setView();
        assert mPresenter != null;
        mPresenter.getGraphType(pid);
        mPresenter.getStatusData(pid, 0, 0);
    }

    @SuppressLint({"WrongConstant", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {

        if (webview == null) {
            webview = new WebView(context);
            llWeb.addView(webview);
        }
        if (webview2 == null) {
            webview2 = new WebView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(llWeb.getLayoutParams());
            lp.setMargins(SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10));
            webview2.setLayoutParams(lp);
            llWeb.addView(webview2);
        }
        // 设置可以支持缩放
        webview.getSettings().setSupportZoom(true);
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
        //设置数据库缓存路径
//        webview.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webview.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webview.getSettings().setAppCacheEnabled(true);
        Log.e("databasepath",  webview.getSettings().getDatabasePath());



        // 设置可以支持缩放
        webview2.getSettings().setSupportZoom(false);

        //设置滚动条不可见
        webview2.setVerticalScrollBarEnabled(false);
        webview2.setHorizontalScrollBarEnabled(false);

        //扩大比例的缩放
        webview2.getSettings().setUseWideViewPort(true);

        //自适应屏幕
        webview2.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview2.getSettings().setLoadWithOverviewMode(true);
    }

    private void setView() {
        webview.loadUrl(getWebUrl(pid, PLAN_INFO));
        //设置Web视图
        client = new PlanInfoClient(pid, "");
        webview.setWebViewClient(client);
        webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title) && (title.contains("resource cannot be found")
                        || title.contains("无法") || title.contains("未找到")) || title.contains("运行时")) {
                    svContent.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                    emptyLayout.setErrorType(EmptyLayout.NODATA);
                }
            }
        };
        webview.setWebChromeClient(webChromeClient);
        webview2.setWebChromeClient(new MyWebChromeClient());
    }

    public void refreshWebView(final int pid) {
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        svContent.setVisibility(View.GONE);
        client = new PlanInfoClient(pid, "");
        webview.loadUrl(getWebUrl(pid, PLAN_INFO));
        webview.setWebViewClient(client);
        webview.setWebChromeClient(webChromeClient);
        assert mPresenter != null;
        mPresenter.getGraphType(pid);
        mPresenter.getStatusData(pid, 0, 0);
    }

    public void setStatusData(String string) {
        String[] split = string.split("\\^\\^\\^\\^");
        // 加载并显示HTML代码
        String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
                "<title></title>" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/planpub.css\"/>" +
                "</head>" +
                "<body class=\"body\">" +
                "<div class=\"station_room_monitor2\" id=\"contentmain111\" >"
                + split[0] + "</div>" + "</body>" +
                "</html>";

        webview2.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);


    }

    public void setShowOrHide(boolean b) {
        svContent.setVisibility(b ? View.VISIBLE : View.GONE);
        emptyLayout.setVisibility(b ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.tv_rooms)
    public void onViewClicked() {
        if (webview != null) {
            webview.reload();
        }
    }

    public class JavaScriptInterface {
        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        public void getDID(String did) {
            Log.e("did", did);
            if (event == null) {
                event = new NonIntrusiveEvent();
            }
            event.setDid(did);
            event.setIsClick(1);
            if (mDids != null && mDids.size() > 0) {
                int index = mDids.indexOf(did);
                if (index >= 0) {
                    if (mStations != null && mStations.size() > 0) {
                        event.setStation(mStations.get(index));//站室名
                    }
                }

            }
            EventBus.getDefault().post(event);
        }

        /**
         * 获取网页内容
         *
         * @param html
         */
        @JavascriptInterface
        public void getSource(String html) {
            Log.e("html=", html);
            searchAllIndex(html);
            if (mDids != null && mDids.size() > 0) {
                if (event == null) {
                    event = new NonIntrusiveEvent();
                }
                event.setDid(mDids.get(0)); //did
                event.setIsClick(0);
                if (mStations != null && mStations.size() > 0) {
                    event.setStation(mStations.get(0));//站室名
                }
                EventBus.getDefault().post(event);
            }

        }

        private void searchAllIndex(String str) {
            int a = str.indexOf("deviceinfo");//*第一个出现的索引位置
            if (mDids != null) {
                mDids.clear();
            }
            if (mStations != null) {
                mStations.clear();
            }
            while (a != -1) {
                if (mDids == null) {
                    mDids = new ArrayList<>();
                }
                if (mStations == null) {
                    mStations = new ArrayList<>();
                }
                mDids.add(str.substring(a + 11, a + 15));
                mStations.add(str.substring(a - 25, a - 22));
                Log.e("deviceinfo", str.substring(a + 11, a + 15) + "   " + a + "\t");

                a = str.indexOf("deviceinfo", a + 1);//*从这个索引往后开始第一个出现的位置
            }
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDids != null && mDids.size() > 0) {
            mDids.clear();
            mDids = null;
        }
        if (mStations != null && mStations.size() > 0) {
            mStations.clear();
            mStations = null;
        }
        if (client != null) {
            client = null;
        }
        releaseWebView();
    }

    private void releaseWebView() {
        if (webview != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            webview.clearCache(true);
            webview2.clearCache(true);
            webview.setWebChromeClient(null);
            webview.setWebViewClient(null);
            webview2.setWebChromeClient(null);
            webview2.setWebViewClient(null);
            ViewParent parent = webview.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webview);
                ((ViewGroup) parent).removeView(webview2);
            }

            webview.stopLoading();
            webview2.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webview.getSettings().setJavaScriptEnabled(false);
            webview.clearHistory();
            webview.clearView();
            webview.removeAllViews();
            webview2.getSettings().setJavaScriptEnabled(false);
            webview2.clearHistory();
            webview2.clearView();
            webview2.removeAllViews();

            try {
                webview.destroy();
                webview2.destroy();
            } catch (Throwable ex) {

            }
        }
    }
}
