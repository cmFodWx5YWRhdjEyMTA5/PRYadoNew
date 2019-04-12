package com.yado.pryado.pryadonew.ui.temp_monitor;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yado.pryado.pryadonew.ChangeOrientationHandler;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.OrientationSensorListener;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.base.BaseFragment;
import com.yado.pryado.pryadonew.bean.DeviceInfoListBean;
import com.yado.pryado.pryadonew.bean.NonIntrusiveEvent;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.ui.adapter.DeviceInfoListAdapter;
import com.yado.pryado.pryadonew.ui.adapter.MyPagerAdapter;
import com.yado.pryado.pryadonew.ui.adapter.RoomsSpinnerAdapter;
import com.yado.pryado.pryadonew.ui.widgit.DragFloatActionButton;
import com.yado.pryado.pryadonew.ui.widgit.DragView;
import com.yado.pryado.pryadonew.ui.widgit.PopupWindow.CommonPopupWindow;
import com.yado.pryado.pryadonew.ui.widgit.ZoomOutPageTransformer;
import com.yado.pryado.pryadonew.util.ScreenUtil;
import com.yado.pryado.pryadonew.util.Util;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/temp_monitor/TempMonitorActivity")
public class TempMonitorActivity extends BaseActivity<TempMonitorPresent> implements TempMonitorContract.View {

    @BindView(R.id.name)
    TextView name;
//    @BindView(R.id.pd_name_spinner)
//    NiceSpinner pdNameSpinner;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.title)
    ViewGroup title;
    @BindView(R.id.vs_spinner)
    ViewStub vsSpinner;



    private List<RoomListBean.RowsEntity> rooms;

    private List<BaseFragment> fragments;
    private List<String> titles;
    private TempMonitorPlanFragment fragmentPlan;
    private TempMonitorAssessFragment fragmentAssess;
    private MyPagerAdapter mAdapter;

    private int pid = -1;
    private Configuration mConfiguration;

    private boolean isFullScreen;
    private ImageView circleImageView;
    private DragView dragView;

    private LinearLayout llRooms;
    private TextView tvRoomName;
    private ImageView ivRooms;
    private CommonPopupWindow popupWindow;
    private View popView;
    private RecyclerView rv_rooms;
    private TextView room_name;
    @Inject
    RoomsSpinnerAdapter adapter;

    @Override
    public int inflateContentView() {
        return R.layout.activity_temp_monitor;
    }

    @Override
    protected void initData() {
        llRooms = vsSpinner.inflate().findViewById(R.id.ll_rooms);
        tvRoomName = llRooms.findViewById(R.id.tv_room_name);
        ivRooms = llRooms.findViewById(R.id.iv_rooms);
        name.setText("非介入式测温");
        initDragView();
        initPopView();
        assert mPresenter != null;
        mPresenter.getRoomList();
        initListener();
    }

    private void initDragView() {
        circleImageView = new ImageView(mContext);
        circleImageView.setScaleType(ImageView.ScaleType.CENTER);
        circleImageView.setBackground(getResources().getDrawable(R.drawable.circle_image));
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

        dragView = new DragView.Builder()
                .setActivity(this)
                .setView(circleImageView)
                .setNeedNearEdge(true)
                .build();
        dragView.setUpListener(new DragView.OnUpListener() {
            @Override
            public void gestureUp() {
                circleImageView.setBackground(getResources().getDrawable(R.drawable.circle_image));
            }

            @Override
            public void gestureDown() {
                circleImageView.setBackground(getResources().getDrawable(R.drawable.circle_image2));
            }
        });
    }

    private void initListener() {
        llRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow == null) {
                    initPopWindow();
                }
                popupWindow.showAsDropDown(view);
                Animation rotate = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                LinearInterpolator lin = new LinearInterpolator();
                rotate.setInterpolator(lin); //设置插值器
                rotate.setDuration(200);//设置动画持续周期
                rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
                ivRooms.startAnimation(rotate);
            }
        });

    }

    private void getByPid(int pid) {
        reGet(fragmentPlan, pid);
        reGet(fragmentAssess, pid);
    }

    private void reGet(BaseFragment fragment, int pid) {
        if (fragment != null && fragment instanceof TempMonitorPlanFragment) {
            ((TempMonitorPlanFragment) fragment).refreshWebView(pid);
        } else {
            ((TempMonitorAssessFragment) fragment).setPid(pid);
        }
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        fragmentPlan = TempMonitorPlanFragment.newInstance(rooms.get(0).getPID());
        fragmentAssess = TempMonitorAssessFragment.newInstance(rooms.get(0).getPID());
        fragments.add(fragmentAssess);
        fragments.add(fragmentPlan);
        titles = new ArrayList<>();
        titles.add("诊断/分析");
        titles.add("平面图");
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(mAdapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setRooms(List<RoomListBean.RowsEntity> rooms) {
        if (rooms.size() > 0) {
            this.rooms = rooms;
            llRooms.setVisibility(View.VISIBLE);
            tvRoomName.setText(rooms.get(0).getName());
            adapter.setNewData(rooms);
            pid = rooms.get(0).getPID();
        } else {
            llRooms.setVisibility(View.GONE);
        }
        if (fragmentPlan == null) {
            initFragments();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(NonIntrusiveEvent event) {
        String did =  event.getDid();
        int isClick = event.getIsClick();//是否点击
        String station = event.getStation();//站室名
        if (isClick == 1) {
            viewPager.setCurrentItem(0);
        }
        fragmentAssess.onRefresh(did, station);
    }


    //切换到横屏调用
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            title.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);


            if (fragmentPlan != null) {
                fragmentPlan.isHide(true);
            }
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            title.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            if (fragmentPlan != null) {
                fragmentPlan.isHide(false);
            }
        }
        dragView.getRootLayout().removeView(dragView.getDragView());
        dragView.getmBuilder().setView(null);
        dragView.getmBuilder().setActivity(null);
        dragView = null;
        dragView = new DragView.Builder()
                .setActivity(this)
                .setNeedNearEdge(true)
                .setView(circleImageView)
                .build();
        dragView.setUpListener(new DragView.OnUpListener() {
            @Override
            public void gestureUp() {
                circleImageView.setBackground(getResources().getDrawable(R.drawable.circle_image));
            }

            @Override
            public void gestureDown() {
                circleImageView.setBackground(getResources().getDrawable(R.drawable.circle_image2));
            }
        });
    }

    private void initPopWindow() {
        if (popupWindow == null) {
            popupWindow = new CommonPopupWindow.Builder(this)
                    .setView(popView)
                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenHeight(mContext) * 2 / 3)
                    .setAnimationStyle(R.style.AnimDown)
                    .setOutsideTouchable(true)
                    .create();
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Animation rotate = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    LinearInterpolator lin = new LinearInterpolator();
                    rotate.setInterpolator(lin); //设置插值器
                    rotate.setDuration(200);//设置动画持续周期
                    rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
                    ivRooms.startAnimation(rotate);
                }
            });
        }

    }

    private void initPopView() {
        popView = LayoutInflater.from(mContext).inflate(R.layout.popup_item2, null);
        rv_rooms = popView.findViewById(R.id.rv_devices);
        room_name = popView.findViewById(R.id.room_name);
        room_name.setText("站室列表");
        room_name.setBackgroundColor(Color.parseColor("#C8484848"));
        rv_rooms.setLayoutManager(new LinearLayoutManager(mContext));
        //添加Android自带的分割线
        rv_rooms.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv_rooms.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RoomListBean.RowsEntity rowsEntity = (RoomListBean.RowsEntity) adapter.getData().get(position);
                if (pid != rowsEntity.getPID()) {
                    pid = rowsEntity.getPID();
                    getByPid(pid);
                    tvRoomName.setText(rowsEntity.getName());
                }
                popupWindow.dismiss();

            }
        });
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }

    @OnClick(R.id.tv_shouye)
    public void onViewClicked(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rooms != null && rooms.size() > 0) {
            rooms.clear();
        }
        if (dragView != null) {
            dragView.getmBuilder().setView(null);
            dragView.getmBuilder().setActivity(null);
            dragView = null;
        }
        if (fragments != null && fragments.size() > 0) {
            fragments.clear();
            fragments = null;
        }
        if (titles != null && titles.size() > 0) {
            titles.clear();
            titles = null;
        }
    }
}
