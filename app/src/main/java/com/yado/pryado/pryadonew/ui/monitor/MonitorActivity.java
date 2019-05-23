package com.yado.pryado.pryadonew.ui.monitor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.R;
import com.yado.pryado.pryadonew.base.BaseActivity;
import com.yado.pryado.pryadonew.bean.Entity;
import com.yado.pryado.pryadonew.bean.RoomListBean;
import com.yado.pryado.pryadonew.bean.StatusBean;
import com.yado.pryado.pryadonew.bean.TypeBean;
import com.yado.pryado.pryadonew.ui.adapter.RoomsAdapter;

import com.yado.pryado.pryadonew.ui.widgit.MarkerView;
import com.yado.pryado.pryadonew.ui.widgit.PopupWindow.CommonPopupWindow;
import com.yado.pryado.pryadonew.util.ScreenUtil;
import com.yado.pryado.pryadonew.util.Util;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/ui/monitor/MonitorActivity")
public class MonitorActivity extends BaseActivity<MonitorPresent> implements MonitorContract.View, BaiduMap.OnMapLoadedCallback {
    private static String TAG = "MonitorActivity";
    private static final int SUCCESS_GET_DATA = 1;//成功获取到配电房列表；
    private static final int FAIL_TRANGE_DATA = 2;//解析json错误；
    private static final int NO_LOGIN = 33;
    private static final int ERROR_GET_JSON = 4;
    private static final int ERROR_NET = 21;
    @BindView(R.id.name)
    TextView name;
    //    @BindView(R.id.displaytype)
//    CheckBox displaytype;
    @BindView(R.id.check_type)
    ViewStub checkType;
    @BindView(R.id.mapView)
    MapView mapView;
//    @BindView(R.id.pd_name_spinner)
//    NiceSpinner pdNameSpinner;
    private CommonPopupWindow popupWindow;
    private View popView;
    private RecyclerView rv_rooms;
    @Inject
    RoomsAdapter adapter;
    private LinearLayout ll_displayType;
    private CheckBox displaytype;

    private BaiduMap mBaiduMap;
    private MarkerView normal_view;
    private MarkerView alert_view;
    private MarkerView notice_view;
    private List<RoomListBean.RowsEntity> rooms;
    private List<OverlayOptions> optionses;
    private boolean showPoint = true;//默认显示地图小点；
    private List<Overlay> mOverlayList = new ArrayList<>();
    private BitmapDescriptor bitmapDescriptor;
    private MyHandler handler = new MyHandler(MonitorActivity.this);



    private static class MyHandler extends Handler {

        WeakReference<MonitorActivity> weakReference;

        MyHandler(MonitorActivity monitorActivity) {
            this.weakReference = new WeakReference<>(monitorActivity);

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS_GET_DATA:
                    break;
                case FAIL_TRANGE_DATA:
                    ToastUtils.showShort("json解析错误...");
                    break;
                case NO_LOGIN:
                    ToastUtils.showShort("没有登录，请登录...");
                    ARouter.getInstance().build(MyConstants.LOGIN).navigation();
//                    startActivity(new Intent(MonitorActivity.this, LoginActivity.class));
                    weakReference.get().finish();
                    break;
                case ERROR_GET_JSON:
                case ERROR_NET:
                    Log.e(TAG, "---网络错误----");
                    ToastUtils.showShort("网络错误!");
                default:
                    break;
            }
        }
    }


    @Override
    public int inflateContentView() {
        return R.layout.activity_monitor;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void initData() {
//        pdNameSpinner.setVisibility(View.GONE);
        name.setText("监测");
        ll_displayType = checkType.inflate().findViewById(R.id.ll_displayType);
        displaytype = ll_displayType.findViewById(R.id.displaytype);
        ll_displayType.setVisibility(View.VISIBLE);
        displaytype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (displaytype.isChecked()) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        return;
                    }
                    initPopWindow();
                    if (popupWindow != null) {
                        popupWindow.showAsDropDown(view);
                    }
                } else {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            }
        });
//        checkType.setVisibility(View.VISIBLE);

        initPopView();
        initMapView();
        assert mPresenter != null;
        mPresenter.getRoomList();
    }

    private void initMap() {
        assert mPresenter != null;
        mPresenter.handle(rooms);
        addIcon(R.drawable.point_green, R.drawable.point_yellow_light, R.drawable.point_yellow, R.drawable.point_red);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (!Util.isDoubleClick()) {
                    Bundle x = marker.getExtraInfo();
                    final int position = x.getInt("position");
                    final RoomListBean.RowsEntity room = x.getParcelable("room");
//                Intent intent=new Intent(MonitorActivity.this,RoomDetailActivity.class);
//                intent.putExtra("room",room);
                    LatLng pt = new LatLng(Double.valueOf(room.getLongtitude()), Double.valueOf(room.getLatitude()));
//创建InfoWindow , 传入 imageView， 地理坐标， y 轴偏移量
//                InfoWindow mInfoWindow = new InfoWindow(textView, pt, -47);
//显示InfoWindow
//                mBaiduMap.showInfoWindow(mInfoWindow);
                    assert mPresenter != null;
                    mPresenter.getGraphType(room.getPID(), room, rooms, position);
//                    ARouter.getInstance().build(MyConstants.ROOM_DETAIL)
//                            .withParcelable("room", room)
//                            .withParcelableArrayList("room_list", (ArrayList<? extends Parcelable>) rooms)
//                            .withInt("position", position)
//                            .withString("video_url", room.getLatitude())
//                            .navigation();
                }
                return true;
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                zoomToSpan();
            }
        }, 100);


        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                deliver(mapStatus);
            }
        });
    }

    private void initMapView() {
        mBaiduMap = mapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
//        设定中心点坐标
        //onMapPoiClick: 一多监测 latitude:  longitude:
        LatLng cenpt = new LatLng(22.394200906676026, 113.55899456862457);
//        定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(11)
                .build();
//        定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//        改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);//禁止旋转；
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);//禁止俯视仰视；
    }

    private void deliver(MapStatus mapStatus) {
        Log.e(TAG, "deliver");
        if (mapStatus.zoom >= 8) {
            if (showPoint) {
                showPoint = false;
                addIcon(R.drawable.normal, R.drawable.icon_yellow, R.drawable.notice, R.drawable.alert);
            }
        } else {
            if (!showPoint) {
                showPoint = true;
                addIcon(R.drawable.point_green, R.drawable.point_yellow_light, R.drawable.point_yellow, R.drawable.point_red);
            }
        }
    }


    public void zoomToSpan() {
        if (mBaiduMap == null) {
            return;
        }
        if (optionses.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Overlay overlay : mOverlayList) {
                // polyline 中的点可能太多，只按marker 缩放
                if (overlay instanceof Marker) {
                    builder.include(((Marker) overlay).getPosition());
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build()));
//            deliver(mBaiduMap.getMapStatus());
        }
    }

    private void initPopView() {
        popView = LayoutInflater.from(mContext).inflate(R.layout.popup_item, null);
        rv_rooms = popView.findViewById(R.id.rv_rooms);
        rv_rooms.setLayoutManager(new LinearLayoutManager(mContext));
        //添加Android自带的分割线
        rv_rooms.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv_rooms.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RoomListBean.RowsEntity room = rooms.get(position);


                if (!Util.isDoubleClick()) {
                    assert mPresenter != null;
                    mPresenter.getGraphType(room.getPID(), room, rooms, position);
//                    ARouter.getInstance().build(MyConstants.ROOM_DETAIL)
//                            .withParcelable("room", room)
//                            .withParcelableArrayList("room_list", (ArrayList<? extends Parcelable>) rooms)
//                            .withInt("position", position)
//                            .navigation();
                }
            }
        });
    }

    private void addIcon(int normal, int notice, int pre_alert, int alert) {
        Log.e(TAG, "addIcon");
        if (normal_view == null) {
            normal_view = new MarkerView(mContext);
            alert_view = new MarkerView(mContext);
            notice_view = new MarkerView(mContext);
        }
        if (rooms == null || rooms.size() <= 0) {
            return;
        }
        mBaiduMap.clear();
        optionses = new ArrayList<>();
        mOverlayList = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            RoomListBean.RowsEntity room = rooms.get(i);
            String x = showPoint ? null : room.getName();
            if ("0".equals(room.getStatus())) {
//                bitmapDescriptor = bd_normal;
                bitmapDescriptor = BitmapDescriptorFactory.fromView(normal_view.setTitle(normal, x));
            } else if ("1".equals(room.getStatus())) {
//                bitmapDescriptor = bd_alert;
                bitmapDescriptor = BitmapDescriptorFactory.fromView(notice_view.setTitle(notice, x));
            } else if ("2".equals(room.getStatus())) {
                bitmapDescriptor = BitmapDescriptorFactory.fromView(notice_view.setTitle(pre_alert, x));
//                bitmapDescriptor = bd_notice;
            } else {//没有匹配的；
                bitmapDescriptor = BitmapDescriptorFactory.fromView(alert_view.setTitle(alert, x));
//                bitmapDescriptor = bd_notice;
            }
            if (TextUtils.isEmpty(room.getLongtitude()) || TextUtils.isEmpty(room.getLatitude())) {
                return;
            } else {
                LatLng point = new LatLng(Double.valueOf(room.getLongtitude()), Double.valueOf(room.getLatitude()));
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmapDescriptor);
                optionses.add(option);
                //在地图上添加Marker，并显示
                Marker marker = (Marker) mBaiduMap.addOverlay(option);
                mOverlayList.add(marker);
                Bundle bundle = new Bundle();
                bundle.putParcelable("room", room);
                bundle.putInt("position", i);
                marker.setExtraInfo(bundle);
            }
        }
    }

    @Subscribe
    public void onMessageEvent(StatusBean change) {
        if (getString(R.string.has_new_alert).equals(change.getStatus())) {
            Log.e(TAG, "有新的报警信息，请求配电房刷新列表");
            assert mPresenter != null;
            mPresenter.getRoomList();
        }
    }


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected boolean isNeedInject() {
        return false;
    }

    public void setRooms(ArrayList<RoomListBean.RowsEntity> rows) {
        this.rooms = rows;
        adapter.setNewData(this.rooms);
        initMap();
    }

    @OnClick({R.id.tv_shouye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shouye:
                finish();
                break;
            case R.id.displaytype:
                if (displaytype.isChecked()) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        return;
                    }
                    initPopWindow();
                    if (popupWindow != null) {
                        popupWindow.showAsDropDown(view);
                    }
                } else {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBaiduMap != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBaiduMap != null) {
            mapView.onPause();
        }
    }


    private void initPopWindow() {
        if (popupWindow == null) {
            popupWindow = new CommonPopupWindow.Builder(this)
                    .setView(popView)
                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenHeight(mContext) * 5 / 6)
                    .setAnimationStyle(R.style.AnimDown)
                    .setOutsideTouchable(true)
                    .create();
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    displaytype.setChecked(false);
                }
            });
        }

    }

    @Override
    public void onMapLoaded() {

    }


    @Override
    protected void onDestroy() {
        if (mBaiduMap != null) {
            MapView.setMapCustomEnable(false);
            mBaiduMap.clear();
            mBaiduMap = null;
            mapView.onDestroy();
        }
        if (mOverlayList != null) {
            mOverlayList.clear();
            mOverlayList = null;
        }
        if (rooms != null) {
            rooms.clear();
            rooms = null;
        }
        if (optionses != null) {
            optionses.clear();
            optionses = null;
        }
        handler.removeCallbacksAndMessages(null);
        handler = null;
        super.onDestroy();
    }

}
