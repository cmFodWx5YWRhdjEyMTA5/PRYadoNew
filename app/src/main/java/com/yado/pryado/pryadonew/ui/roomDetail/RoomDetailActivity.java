package com.yado.pryado.pryadonew.ui.roomDetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.DeviceInfoListBean;
import com.yado.pryado.pryadonew.bean.RoomDetail;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.bean.StatusBean;
import com.yado.pryado.pryadonew.bean.TypeBean;
import com.yado.pryado.pryadonew.ui.adapter.DeviceInfoListAdapter;
import com.yado.pryado.pryadonew.ui.widgit.CustomMarkerView;
import com.yado.pryado.pryadonew.ui.widgit.DragView;
import com.yado.pryado.pryadonew.ui.widgit.EmptyLayout;
import com.yado.pryado.pryadonew.ui.widgit.MyWebChromeClient;
import com.yado.pryado.pryadonew.ui.widgit.MyWebView;
import com.yado.pryado.pryadonew.ui.widgit.OneGraphClient;
import com.yado.pryado.pryadonew.ui.widgit.PlanInfoClient;
import com.yado.pryado.pryadonew.ui.widgit.PopupWindow.CommonPopupWindow;
import com.yado.pryado.pryadonew.util.ScreenUtil;
import com.yado.pryado.pryadonew.util.Util;

import org.greenrobot.eventbus.Subscribe;

import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/ui/roomDetail/RoomDetailActivity")
public class RoomDetailActivity extends BaseActivity<RoomDetailPresent> implements RadioGroup.OnCheckedChangeListener, RoomDetailContract.View, View.OnTouchListener {

    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_rooms)
    TextView tvRooms;
    //    @BindView(R.id.webview)
    MyWebView webview;
    @BindView(R.id.textView_maxname)
    TextView textViewMaxname;
    @BindView(R.id.linechart)
    LineChart linechart;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.rb_day)
    RadioButton rbDay;
    @BindView(R.id.rb_week)
    RadioButton rbWeek;
    @BindView(R.id.rb_month)
    RadioButton rbMonth;
    @BindView(R.id.rb_all)
    RadioButton rbAll;
    @BindView(R.id.rg_max)
    RadioGroup rgMax;
    @BindView(R.id.rl_max)
    RelativeLayout rlMax;
    @BindView(R.id.linechart2)
    LineChart linechart2;
    @BindView(R.id.rb_day1)
    RadioButton rbDay1;
    @BindView(R.id.rb_week1)
    RadioButton rbWeek1;
    @BindView(R.id.rb_month1)
    RadioButton rbMonth1;
    @BindView(R.id.rb_all1)
    RadioButton rbAll1;
    @BindView(R.id.rg_load)
    RadioGroup rgLoad;
    @BindView(R.id.rl_load)
    RelativeLayout rlLoad;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.title)
    ViewGroup title;
    WebView webview2;
    @BindView(R.id.emptyLayout)
    EmptyLayout emptyLayout;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    public static int PLAN_INFO = 1;//平面图；
    public static int ONE_GRAPH = 2;//一次图；

    @Autowired(name = "room")
    public RoomListBean.RowsEntity room;
    //    @Autowired(name = "room_list")
    public List<RoomListBean.RowsEntity> room_list;
    @Autowired
    public int position;
    @Autowired
    public String video_url;
    @Autowired(name = "TypeBean")
    TypeBean gragh;

    private WebViewClient client;
    private int pid;
    private int type = 1;
    private boolean webView2Enable = true;
    private int times;

    private RoomDetail roomStatusBean;
    private RoomDetail.HiPointHisDataEntity hiPointHisData;
    private LinkedHashMap<String, String> dayEvPoint;
    private LinkedHashMap<String, String> dayHiPoint;
    private LinkedHashMap<String, String> weeEvPoint;
    private LinkedHashMap<String, String> weeHiPoint;
    private LinkedHashMap<String, String> monEvPoint;
    private LinkedHashMap<String, String> monHiPoint;
    private LinkedHashMap<String, String> allEvPoint;
    private LinkedHashMap<String, String> allHiPoint;
    private int widthScreen;

    private float lastX;
    private float lastY;
    private float firstX;
    private float firstY;
    private boolean hasDown = false;
    private CustomMarkerView markerView;

    private CommonPopupWindow popupWindow;
    private View popView;
    private RecyclerView rv_devices;
    @Inject
    DeviceInfoListAdapter adapter;

    private ImageView circleImageView;
    private DragView dragView;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            assert mPresenter != null;
            switch (msg.what) {
                case 1://成功获取配电房运行状态；
                    name.setText(roomStatusBean.getPdname());
                    setStatus(roomStatusBean.getStatus());
                    hiPointHisData = roomStatusBean.getHiPointHisData();
                    if (hiPointHisData == null) {
                        return;
                    }
                    dayEvPoint = hiPointHisData.getDayEvPoint();
                    dayHiPoint = hiPointHisData.getDayHiPoint();
                    weeHiPoint = hiPointHisData.getWeeHiPoint();
                    weeEvPoint = hiPointHisData.getWeeEvPoint();
                    monEvPoint = hiPointHisData.getMonEvPoint();
                    monHiPoint = hiPointHisData.getMonHiPoint();
                    allEvPoint = hiPointHisData.getAllEvPoint();
                    allHiPoint = hiPointHisData.getAllHiPoint();
//                 ... Glide.with(RoomDetailActivity.this).load(roomStatusBean.getImage_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivYici);
                    if (linechart.getLineData() != null) {
                        linechart.clearValues();
                    }
                    linechart.setData(mPresenter.transform_data(roomStatusBean.getHiPointHisData().getTypeName(), dayHiPoint, dayEvPoint, 1, markerView));

                    linechart.notifyDataSetChanged();
                    if (linechart2.getLineData() != null) {
                        linechart2.clearValues();
                    }
                    linechart2.setData(mPresenter.transform_data(roomStatusBean.getHiPointHisData().getTypeName(), allHiPoint));

                    linechart2.notifyDataSetChanged();
                    textViewMaxname.setText(roomStatusBean.getHiPointHisData().getPosition());
                    rgMax.check(R.id.rb_day);
                    rgLoad.check(R.id.rb_day1);
                    pbLoading.setVisibility(View.GONE);
                    break;
                case 2://服务器返回错误
                    try {
                        name.setText(room_list.get(position).getName());
                        hiPointHisData = null;
                        dayEvPoint = null;
                        weeEvPoint = null;
                        monEvPoint = null;
                        allEvPoint = null;
                        dayHiPoint = null;
                        weeHiPoint = null;
                        monHiPoint = null;
                        allHiPoint = null;

                        linechart.clearValues();
                        linechart2.clearValues();
                        linechart.notifyDataSetChanged();
                        linechart2.notifyDataSetChanged();
//                        ivYici.setImageResource(R.drawable.ic_affaires);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ToastUtils.showShort("服务器返回错误");
                    pbLoading.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };
    private Configuration mConfiguration;
    private boolean isFullScreen;//是否全屏

    /**
     * 加载布局
     * @return
     */
    @Override
    public int inflateContentView() {
        return R.layout.activity_room_detail;
    }

    /**
     * 初始化数据
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {
        tvPre.setText(getString(R.string.back));
        assert mPresenter != null;
        widthScreen = mPresenter.getScreenWidth(mContext);
        tvDate.setVisibility(View.VISIBLE);
        room_list = getIntent().getParcelableArrayListExtra("room_list");
//        scrollView.setOnTouchListener(this);
        initWebView();
        if (room_list == null && pid <= 0) {
            ToastUtils.showShort("缺少配电房信息...");
            finish();
        }
        initView();
        initPopView();
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        setView(position);
        assert mPresenter != null;
        mPresenter.setLineType(linechart, 7);
        mPresenter.setLineType(linechart2, 7);
        mPresenter.getRoomDetail(room.getPID(), 1);
        mPresenter.getDeviceInfoList(room.getPID(), 100, 1);
//        mPresenter.getGraphType(room.getPID());
    }

    /**
     * 设置数据到View中
     * @param position
     */
    private void setView(int position) {
        if (linechart.getData() != null) {
            linechart.clearValues();
        }
        if (room_list != null && room_list.get(position) != null) {
            if (room_list.get(position).getName().length() > 13 && room_list.get(position).getName().length() < 16) {
                name.setTextSize(16);
            } else if (room_list.get(position).getName().length() > 16) {
                name.setTextSize(14);
            }
            name.setText(room_list.get(position).getName());
        } else {
            if (room.getName().length() > 13 && room.getName().length() < 16) {
                name.setTextSize(16);
            } else if (room.getName().length() > 16) {
                name.setTextSize(14);
            }
            name.setText(room.getName());
        }
        tvRooms.setText("配电房平面图(点我切换)");
        type = 1;
        room = room_list.get(position);
        video_url = room.getVideourl();
        pid = room.getPID();
        //http://113.106.90.51:8004/Monitor/AppPlanInfo?pid=4
        assert mPresenter != null;
        webview.loadUrl(mPresenter.getWebUrl(gragh, pid, PLAN_INFO));

        //设置Web视图
        client = new PlanInfoClient(pid, video_url);
        webview.setWebViewClient(client);
        webview.setWebChromeClient(new MyWebChromeClient());
//        ivYici.setWebViewClient(client2);
        webview.setOnDoubleClickListener(new MyWebView.OnDoubleClickListener() {
            @Override
            public void onDoubleClick() {
                int type;
                if (client instanceof PlanInfoClient) {
                    type = 1;
                } else {
                    type = 2;
                }
                ARouter.getInstance().build(MyConstants.WEB_VIEW)
                        .withInt("pid", pid)
                        .withInt("type", type)
                        .withString("video_url", video_url)
                        .withSerializable("graph", gragh)
                        .navigation();
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        firstX = event.getRawX();
//        firstY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstX = event.getRawX();
                firstY = event.getRawY();
                hasDown = true;
                break;
            case MotionEvent.ACTION_UP:
                lastX = event.getRawX();
                lastY = event.getRawY();
                if (Math.abs(lastX - firstX) < Math.abs(lastY - firstY) || Math.abs(lastX - firstX) < widthScreen / 3 || !hasDown) {
                    return false;
                } else {
                    if (lastX - firstX > 0) {
                        if (position >= 1 && position <= room_list.size()) {
                            position--;
                            setView(position);
                            hasDown = false;
                        }
//                        TUtil.t("right");
                    } else {
                        if (position >= 0 && position <= room_list.size() - 2) {
                            position++;
                            setView(position);
                            hasDown = false;
                        }
//                        TUtil.t("left");
                    }
                    firstX = lastX;
                    firstY = lastY;
                    return true;
                }
            default:
                break;
        }
        return super.onTouchEvent(event);
//        return gd.onTouchEvent(event);
    }

    /**
     * 初始化View
     */
    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    private void initView() {
        setStatus(room.getStatus());
        tvDate.setText("设备列表");
        rgMax.setOnCheckedChangeListener(this);
        rgLoad.setOnCheckedChangeListener(this);
        markerView = new CustomMarkerView(mActivityComponent.getActivityContext(), R.layout.marker_view);
        linechart.setMarkerView(markerView);
        markerView.setChartView(linechart);
        circleImageView = new ImageView(mContext);
        circleImageView.setScaleType(ImageView.ScaleType.CENTER);
        circleImageView.setBackground(getDrawable(R.drawable.circle_image));
        circleImageView.setClickable(true);
        circleImageView.setFocusable(true);
        circleImageView.setImageResource(R.drawable.ic_full_screen);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFullScreen = !isFullScreen;
                circleImageView.setImageResource(isFullScreen ? R.drawable.ic_vertical_screen : R.drawable.ic_full_screen);
                if (isFullScreen) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
                }
            }
        });

        //悬浮按钮
        dragView = new DragView.Builder()
                .setActivity(this)
                .setSize(150)
                .setView(circleImageView)
                .setNeedNearEdge(true)
                .build();
        dragView.setUpListener(new DragView.OnUpListener() {
            @Override
            public void gestureUp() {//手势抬起
                circleImageView.setBackground(getDrawable(R.drawable.circle_image));
            }

            @Override
            public void gestureDown() {//手势按下
                circleImageView.setBackground(getDrawable(R.drawable.circle_image2));
            }
        });
    }

    /**
     * 设置配电房状态
     * @param status
     */
    private void setStatus(String status) {
        String mStatus;
        int color;
        if ("0".equals(status)) {
            mStatus = "正常";
            color = Color.GREEN;
        } else if ("1".equals(status)) {
            mStatus = "关注";
            color = Color.parseColor("#FFFF00");
        } else if ("2".equals(status)) {
            mStatus = "预警";
            color = Color.parseColor("#FFA500");
        } else {
            mStatus = "报警";
            color = Color.RED;
        }
        tvStatus.setText(mStatus);
        tvStatus.setTextColor(color);
    }

    /**
     * 初始化WebView
     */
    @SuppressLint({"WrongConstant", "SetJavaScriptEnabled"})
    private void initWebView() {
        if (webview == null) {
            webview = new MyWebView(mActivityComponent.getActivityContext());
            llContent.addView(webview, 2);
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
//        webview.setOnFlingListner(new MyWebView.OnFlingListner() {
//            @Override
//            public void onFling() {
//                changeView(++type);
//            }
//        });
        if (gragh == null && webview2 == null) {

            webview2 = new WebView(mActivityComponent.getActivityContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(llContent.getLayoutParams());
            lp.setMargins(SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10), SizeUtils.dp2px(10));
            webview2.setLayoutParams(lp);
            llContent.addView(webview2, 3);
            webview2.setVisibility(View.GONE);
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
            webview2.setWebChromeClient(new MyWebChromeClient());
        }
    }

    /**
     * 一次图平面图切换
     * @param i
     */
    private void changeView(int i) {
        if (i % 2 == 0) {
            client = new OneGraphClient((Activity) mContext, pid);
            assert mPresenter != null;
            webview.loadUrl(mPresenter.getWebUrl(gragh, pid, ONE_GRAPH));
            webview.setWebViewClient(client);
            if (gragh == null) {
                tvRooms.setText(getString(R.string.onegraph) + "(点我切换)");
            }
        } else {
            client = new PlanInfoClient(pid, video_url);
            assert mPresenter != null;
            webview.loadUrl(mPresenter.getWebUrl(gragh, pid, PLAN_INFO));
            webview.setWebViewClient(client);
            tvRooms.setText(getString(R.string.planinfo) + "(点我切换)");
        }
    }

    /**
     * 注入View
     */
    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    /**
     * 是否需要注册 EventBus
     * @return
     */
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe
    public void onMessageEvent(StatusBean change) {
        if (getString(R.string.has_new_alert).equals(change.getStatus())) {
            setView(position);
        }
    }

    //获取温度HTML
    @Subscribe
    public void onMaxDivEvent(Message message) {
        if (message.what == 1) {
            if (!webview.getUrl().contains("NonIntrusive")) {    //常规平面图
                if (times < 1) {
                    times++;
                    assert mPresenter != null;
                    mPresenter.getMaxDiv(pid);
                }
            }
        } else {
            webView2Enable = false;
        }

    }

    /**
     * 是否需要注入Arouter
     * @return
     */
    @Override
    protected boolean isNeedInject() {
        return true;
    }

    @OnClick({R.id.tv_shouye, R.id.tv_date, R.id.tv_rooms})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shouye:
                finish();
                break;
            case R.id.tv_date:
                if (popupWindow == null) {
                    initPopWindow();
                }
                tvDate.setText("收起");
                popupWindow.showAsDropDown(view);
                break;
            case R.id.tv_rooms:
                changeView(++type);
                break;
            default:
                break;
        }
    }

    /**
     * 舒适化设备列表弹窗
     */
    private void initPopWindow() {
        if (popupWindow == null) {
            popupWindow = new CommonPopupWindow.Builder(this)
                    .setView(popView)
                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenHeight(mContext) * 3 / 4)
                    .setAnimationStyle(R.style.AnimDown)
                    .setOutsideTouchable(true)
                    .create();
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    tvDate.setText("设备列表");
                }
            });
        }

    }

    /**
     * 初始化弹窗View
     */
    private void initPopView() {
        popView = LayoutInflater.from(mContext).inflate(R.layout.popup_item2, null);
        rv_devices = popView.findViewById(R.id.rv_devices);
        rv_devices.setLayoutManager(new GridLayoutManager(mContext, 2));
        //添加Android自带的分割线
        rv_devices.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv_devices.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DeviceInfoListBean.RowsBean rowsBean = (DeviceInfoListBean.RowsBean) adapter.getData().get(position);
                if (!Util.isDoubleClick()) {
                    ARouter.getInstance().build(MyConstants.DEVICE_DETAIL)
                            .withString("did", rowsBean.getDID())
                            .withInt("pid", pid)
                            .navigation();
                }
            }
        });
    }

    /**
     * 切换曲线
     * @param checkedId
     */
    private void trySetData(int checkedId) {
        assert mPresenter != null;
        switch (checkedId) {
            case R.id.rb_day:
                if (linechart.getData() != null) {
                    linechart.clearValues();
                }
                if (roomStatusBean == null) {
                    linechart.setData(mPresenter.transform_data("", null, null, 5, markerView));
                } else {
                    linechart.setData(mPresenter.transform_data(roomStatusBean.getHiPointHisData().getTypeName(), dayHiPoint, dayEvPoint, 1, markerView));
                }
                linechart.notifyDataSetChanged();
                rbDay.setTextColor(Color.GREEN);
                rbWeek.setTextColor(Color.BLACK);
                rbMonth.setTextColor(Color.BLACK);
                rbAll.setTextColor(Color.BLACK);
                break;
            case R.id.rb_week:
                if (linechart.getData() != null) {
                    linechart.clearValues();
                }
                if (roomStatusBean == null) {
                    linechart.setData(mPresenter.transform_data("", null, null, 5, markerView));
                } else {
                    linechart.setData(mPresenter.transform_data(roomStatusBean.getHiPointHisData().getTypeName(), weeHiPoint, weeEvPoint, 2, markerView));
                }
                linechart.notifyDataSetChanged();
                rbDay.setTextColor(Color.BLACK);
                rbWeek.setTextColor(Color.GREEN);
                rbMonth.setTextColor(Color.BLACK);
                rbAll.setTextColor(Color.BLACK);
                break;
            case R.id.rb_month:
                if (linechart.getData() != null) {
                    linechart.clearValues();
                }
                linechart.setData(mPresenter.transform_data(roomStatusBean.getHiPointHisData().getTypeName(), monHiPoint, monEvPoint, 3, markerView));
                linechart.notifyDataSetChanged();
                rbDay.setTextColor(Color.BLACK);
                rbWeek.setTextColor(Color.BLACK);
                rbMonth.setTextColor(Color.GREEN);
                rbAll.setTextColor(Color.BLACK);
                break;
            case R.id.rb_all:
                if (linechart.getData() != null) {
                    linechart.clearValues();
                }
                linechart.setData(mPresenter.transform_data(roomStatusBean.getHiPointHisData().getTypeName(), allHiPoint, allEvPoint, 4, markerView));
                linechart.notifyDataSetChanged();
                rbDay.setTextColor(Color.BLACK);
                rbWeek.setTextColor(Color.BLACK);
                rbMonth.setTextColor(Color.BLACK);
                rbAll.setTextColor(Color.GREEN);
                break;
            case R.id.rb_day1:
                if (linechart2.getData() != null) {
                    linechart2.clearValues();
                }

                if (roomStatusBean == null) {
                    return;
                }
                linechart2.setData(mPresenter.transform_data(roomStatusBean.getHiPointHisData().getTypeName(), dayEvPoint));
                linechart2.notifyDataSetChanged();
                textViewMaxname.setText(roomStatusBean.getHiPointHisData().getMax_name());
                rbDay1.setTextColor(Color.GREEN);
                rbWeek1.setTextColor(Color.BLACK);
                rbMonth1.setTextColor(Color.BLACK);
                rbAll1.setTextColor(Color.BLACK);
                break;
            case R.id.rb_week1:
                if (linechart2.getData() != null) {
                    linechart2.clearValues();
                }

                linechart2.setData(mPresenter.transform_data(roomStatusBean.getHiPointHisData().getTypeName(), weeEvPoint));
                linechart2.notifyDataSetChanged();
                linechart2.notifyDataSetChanged();
                textViewMaxname.setText(roomStatusBean.getHiPointHisData().getPosition());
                rbDay1.setTextColor(Color.BLACK);
                rbWeek1.setTextColor(Color.GREEN);
                rbMonth1.setTextColor(Color.BLACK);
                rbAll1.setTextColor(Color.BLACK);
                break;
            case R.id.rb_month1:
                if (linechart2.getData() != null) {
                    linechart2.clearValues();
                }

                linechart2.setData(mPresenter.transform_data(roomStatusBean.getHiPointHisData().getTypeName(), monEvPoint));
                linechart2.notifyDataSetChanged();
                textViewMaxname.setText(roomStatusBean.getHiPointHisData().getPosition());
                rbDay1.setTextColor(Color.BLACK);
                rbWeek1.setTextColor(Color.BLACK);
                rbMonth1.setTextColor(Color.GREEN);
                rbAll1.setTextColor(Color.BLACK);
                break;
            case R.id.rb_all1:
                if (linechart2.getData() != null) {
                    linechart2.clearValues();
                }
                linechart2.setData(mPresenter.transform_data(roomStatusBean.getHiPointHisData().getTypeName(), allEvPoint));
                linechart2.notifyDataSetChanged();
                textViewMaxname.setText(roomStatusBean.getHiPointHisData().getPosition());
                rbDay1.setTextColor(Color.BLACK);
                rbWeek1.setTextColor(Color.BLACK);
                rbMonth1.setTextColor(Color.BLACK);
                rbAll1.setTextColor(Color.GREEN);
                break;
            default:
                break;
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        try {
            if (roomStatusBean == null) {
                ToastUtils.showShort(R.string.no_line_data);
                return;
            }
            trySetData(checkedId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置站室详情信息
     * @param roomDetail
     */
    @Override
    public void setRoomDetail(RoomDetail roomDetail) {
        this.roomStatusBean = roomDetail;
        emptyLayout.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(1);
        pbLoading.setVisibility(View.GONE);
    }

    //设置错误信息
    @Override
    public void setError(Throwable error) {
        handler.sendEmptyMessage(2);
    }

    /**
     * 拼接 并显示站室运行状况 webView2
     * @param string
     */
    @Override
    public void setMaxDivResource(String string) {
        webView2Enable = true;
        if (webview2 != null) {
            webview2.setVisibility(webView2Enable ? View.VISIBLE : View.GONE);
            // 加载并显示HTML代码
            final String html = "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                    "<head>" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
                    "<title></title>" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/planpub.css\"/>" +
                    "</head>" +
                    "<body class=\"body\">" +
                    "<div class=\"station_room_monitor2\" id=\"contentmain111\" >"
                    + string + "</div>" + "</body>" +
                    "</html>";
            webview2.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        }

    }

    /**
     * 设置设备列表
     * @param listBean
     */
    @Override
    public void setDeviceInfoListBean(DeviceInfoListBean listBean) {
        if (listBean.getRows() != null && listBean.getRows().size() > 0) {
            adapter.setNewData(listBean.getRows());
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            title.setVisibility(View.GONE);
            if (webView2Enable && webview2 != null) {
                webview2.setVisibility(View.GONE);
            }
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            title.setVisibility(View.VISIBLE);
            if (webView2Enable && webview2 != null) {
                webview2.setVisibility(View.VISIBLE);
            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
        }
        dragView.getRootLayout().removeView(dragView.getDragView());
        dragView.getmBuilder().setView(null);
        dragView.getmBuilder().setActivity(null);
        dragView = null;
        dragView = new DragView.Builder()
                .setActivity(this)
                .setNeedNearEdge(true)
                .setSize(150)
                .setView(circleImageView)
                .build();
        dragView.setUpListener(new DragView.OnUpListener() {
            @Override
            public void gestureUp() {
                circleImageView.setBackground(getDrawable(R.drawable.circle_image));
            }

            @Override
            public void gestureDown() {
                circleImageView.setBackground(getDrawable(R.drawable.circle_image2));
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
        setNull();

    }

    private void setNull() {
        if (dragView != null) {
            dragView.getmBuilder().setView(null);
            dragView.getmBuilder().setActivity(null);
            dragView = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (room_list != null && room_list.size() > 0) {
            room_list.clear();
            room_list = null;
        }
        if (dayEvPoint != null && dayEvPoint.size() > 0) {
            dayEvPoint.clear();
            dayEvPoint = null;
        }
        if (dayHiPoint != null && dayHiPoint.size() > 0) {
            dayHiPoint.clear();
            dayHiPoint = null;
        }
        if (weeEvPoint != null && weeEvPoint.size() > 0) {
            weeEvPoint.clear();
            weeEvPoint = null;
        }
        if (weeHiPoint != null && weeHiPoint.size() > 0) {
            weeHiPoint.clear();
            weeHiPoint = null;
        }
        if (monEvPoint != null && monEvPoint.size() > 0) {
            monEvPoint.clear();
            monEvPoint = null;
        }
        if (monHiPoint != null && monHiPoint.size() > 0) {
            monHiPoint.clear();
            monHiPoint = null;
        }
        if (allEvPoint != null && allEvPoint.size() > 0) {
            allEvPoint.clear();
            allEvPoint = null;
        }
        if (allHiPoint != null && allHiPoint.size() > 0) {
            allHiPoint.clear();
            allHiPoint = null;
        }
    }

    /**
     * 销毁WebView
     */
    private void destroyWebView() {
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
        if (webview2 != null) {
            webview2.clearCache(true);
            ViewParent parent = webview2.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webview2);
            }
            webview2.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webview2.getSettings().setJavaScriptEnabled(false);
            webview2.clearHistory();
            webview2.clearView();
            webview2.removeAllViews();
            webview2.destroy();
        }
    }

    /**
     * js 调用Java接口
     */
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

        @JavascriptInterface
        public void getDID(String did, String sid) {
            Log.e("sid", sid);

            ARouter.getInstance().build(MyConstants.DEVICE_DETAIL)
                    .withString("did", sid)
                    .withInt("pid", pid)
                    .navigation();
        }
    }

}
