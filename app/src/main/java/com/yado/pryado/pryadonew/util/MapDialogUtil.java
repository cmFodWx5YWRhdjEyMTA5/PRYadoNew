package com.yado.pryado.pryadonew.util;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.ToastUtils;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.yado.pryado.pryadonew.R;

import java.lang.ref.WeakReference;

public class MapDialogUtil {
    private static MapDialogUtil instance;
    private NiftyDialogBuilder dialogBuilder;
    private WeakReference<Context> mContext;
//    private  Context mContext;
    private final static String TAG = "MapDialogUtil";

    private View map;
    private TextureMapView mapView;

    private double mLatitude;
    private double longtitude;
    private String currentplace;
    private LatLng mLatLng;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private OnMapClickListener mapClickListener;
    private MyLocationListener locationListener;

    public static MapDialogUtil getInstance(Context context) {
        if (instance == null) {
            instance = new MapDialogUtil(context);
            instance.initMapView();
            instance.initLocationClient();
        }
        return instance;
    }

    private MapDialogUtil(Context context) {
        mContext = new WeakReference<>(context);
    }

    public void setMapClickListener(OnMapClickListener mapClickListener) {
        this.mapClickListener = mapClickListener;
    }

    private void initMapView() {
        map = LayoutInflater.from(mContext.get()).inflate(R.layout.map_view, null);
        mapView = map.findViewById(R.id.map_view);
        mapView.onResume();
    }

    private void initLocationClient() {
        mLocationClient = new LocationClient(mContext.get());
        locationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(locationListener);
        InitLocation();
        mLocationClient.start();
    }

    private void InitLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");
        int span = 1000;
        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    public void showMapDialog() {
        if (dialogBuilder == null) {
            dialogBuilder = NiftyDialogBuilder.getInstance(mContext.get());
            dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    onPause();
                }
            });
            dialogBuilder.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    if (mLocationClient != null) {
                        mLocationClient.restart();
                    }
                    onResume();
                }
            });
        }
        dialogBuilder
                .withTitle("位置选择")                               //标题.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                               //def  标题颜色
                .withDividerColor("#11000000")                           //def
                .withMessage(null)                 //.withMessage(null)  no Msg   内容
                .withMessageColor("#FFCFCFCF")                          //def  | withMessageColor(int resid)   内容颜色
                .withDialogColor("#AF444D50")                            //def  | withDialogColor(int resid)   dialog框颜色
                .withIcon(mContext.get().getResources().getDrawable(R.drawable.ic_logo2))  //标题栏图标
                .withDuration(700)                                      //def      动画持续时间
                .withEffect(Effectstype.SlideBottom)                                     //def Effectstype.Slidetop  动画模式
                .withButton1Text("取消")                                  //def gone     按钮文字
                .withButton2Text("确定")                              //def gone
                .isCancelableOnTouchOutside(true)                       //def    | isCancelable(true)是否支持点击dialog框外关闭dialog
                .setCustomView(map, mContext.get())     //.setCustomView(View or ResId,context)    自定义布局
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        mLocationClient.stop();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        mLocationClient.stop();
                        mapClickListener.onMapClick(mLatLng.latitude, mLatLng.longitude);
                    }
                })
                .show();    //展示

    }

    private void onResume() {
        if (mBaiduMap != null) {
            mapView.onResume();
        }
    }

    private void onPause() {
        if (mBaiduMap != null) {
            mapView.onPause();
        }
    }

    public void reset() {
        if (instance != null) {
            instance = null;
            mLocationClient.stop();
            if (locationListener != null){
                mLocationClient.unRegisterLocationListener(locationListener);
                mLocationClient = null;
                locationListener = null;
            }
            dialogBuilder = null;
            if (mBaiduMap != null) {
                MapView.setMapCustomEnable(false);
                mapView.onPause();
                mBaiduMap.clear();
                mBaiduMap = null;
                mapView.onDestroy();
            }
        }
    }

    private void setLocation(double latitude, double longtitude) {

//        4.9E-324
        if (longtitude <= 0.01) {
            latitude = 22.394200906676026;
            longtitude = 113.55899456862457;
            ToastUtils.showShort(mContext.get().getString(R.string.no_location));
        }
//        else {
//            ToastUtils.showShort(currentplace);
//        }
        //设定初始化坐标
        //39.851564   116.432799;
        //onMapPoiClick: 一多监测 latitude: 22.394200906676026 longitude: 113.55899456862457
        LatLng mInitLatLng = new LatLng(latitude, longtitude);//一多地理位置
        mLatLng = mInitLatLng;
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(mInitLatLng)
                .zoom(14)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap = mapView.getMap();

        BitmapDescriptor initBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
        MarkerOptions initOptions = new MarkerOptions().position(mInitLatLng).icon(initBitmap);
        mBaiduMap.addOverlay(initOptions);

        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setBuildingsEnabled(true);
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mBaiduMap.clear();

                mLatLng = latLng;

                Log.i(TAG, "onMapClick: latitude: " + latLng.latitude + " longitude: " + latLng.longitude);

                //更改标记点
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
                MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmap).title("当前位置");

                mBaiduMap.showInfoWindow(new InfoWindow(getView(latLng, null), latLng, -100));

                mBaiduMap.addOverlay(options);

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {

                LatLng latLng = mapPoi.getPosition();
                mBaiduMap.clear();

                mLatLng = latLng;

                //更改标记点
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
                MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmap).title(mapPoi.getName());

                mBaiduMap.showInfoWindow(new InfoWindow(getView(latLng, mapPoi.getName()), latLng, -100));

                mBaiduMap.addOverlay(options);

                Log.i(TAG, "onMapPoiClick: " + mapPoi.getName() + " latitude: " + latLng.latitude + " longitude: " + latLng.longitude);

                return true;
            }
        });
    }

    public View getView(LatLng latLng, String name) {

        View view = LayoutInflater.from(mContext.get()).inflate(R.layout.view_mark_info, null, false);

        TextView mName = (TextView) view.findViewById(R.id.name);
        TextView latitude = (TextView) view.findViewById(R.id.latitude);
        TextView longitude = (TextView) view.findViewById(R.id.longitude);

        if (name == null) {
            mName.setText("当前选中位置");
        } else {
            mName.setText(name);
        }

        latitude.setText(String.format("纬度 ：%.4f", latLng.latitude));
        longitude.setText(String.format("经度 ：%.4f", latLng.longitude));

        view.setBackgroundResource(R.drawable.bg_map_text);
        view.setClickable(false);

        return view;
    }

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
//            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("error code : ");
//            sb.append(location.getLocType());
//            sb.append("latitude : ");
//            sb.append(location.getLatitude());
//            sb.append("lontitude : ");
//            sb.append(location.getLongitude());
//            sb.append("radius : ");
//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                sb.append("speed : ");
//                sb.append(location.getSpeed());
//                sb.append("satellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("direction : ");
//                sb.append("addr : ");
//                sb.append(location.getAddrStr());
//                sb.append(location.getDirection());
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                sb.append("addr : ");
//                sb.append(location.getAddrStr());
//                //运营商信息
//                sb.append("operationers : ");
//                sb.append(location.getOperators());
//            }
            mLatitude = location.getLatitude();
            longtitude = location.getLongitude();
            currentplace = location.getAddrStr();
            Log.e("LCY", "纬度=" + mLatitude + "，经度=" + longtitude + "," + currentplace);

            setLocation(mLatitude, longtitude);
//            Log.i("dwtedx", sb.toString());
            if (mLocationClient != null) {
                mLocationClient.stop();
            }
        }
    }

    public interface OnMapClickListener {
        void onMapClick(double latitude, double longtitude);
    }

}
