package com.yado.pryado.pryadonew.util;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;

import java.lang.ref.WeakReference;

public class LocationUtil {

    private LocationClient mLocationClient;
    private WeakReference<Context> context;
//    private Context context;
    public static LocationUtil instance;
    private MyLocationListener myListener;

    private LocationUtil(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void initLocation() {

        myListener = new MyLocationListener();
        // 开启定位图层
        mLocationClient = new LocationClient(context.get());
        // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        // 注册监听函数
        setLocationOption();
        mLocationClient.start();
    }

    public static LocationUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (LocationUtil.class) {
                if (instance == null) {
                    instance = new LocationUtil(context);
                }
            }
        }
        return instance;
    }

    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPS
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向

        mLocationClient.setLocOption(option);
    }

    private LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void reset(){
        if (myListener != null && mLocationClient != null){
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient = null;
            myListener = null;
        }
        instance = null;

    }
    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            Log.e("location", "location=" + location.toString());
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("error code : ");
            sb.append(location.getLocType());
            sb.append("latitude : ");
            sb.append(location.getLatitude());
            sb.append("lontitude : ");
            sb.append(location.getLongitude());
            sb.append("radius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("speed : ");
                sb.append(location.getSpeed());
                sb.append("satellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("direction : ");
                sb.append("addr : ");
                sb.append(location.getAddrStr());
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("addr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("operationers : ");
                sb.append(location.getOperators());
            }
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            setLatLng(latLng);
//            latitude = location.getLatitude();
//            longtitude = location.getLongitude();
//            currentplace = location.getAddrStr();
//            KLog.e("LCY", "纬度=" + latitude + "，经度=" + longtitude + "," + currentplace);
//
//            setLocation(latitude, longtitude);
//            Log.i("dwtedx", sb.toString());
            mLocationClient.stop();
        }

    }

//    private void setLocation(double latitude, double longtitude) {
//
////        4.9E-324
//        if (longtitude <= 0.01) {
//            latitude = 22.394200906676026;
//            longtitude = 113.55899456862457;
//            TUtil.t(context.getString(R.string.no_location));
//        } else {
//            TUtil.t(currentplace);
//        }
//        //设定初始化坐标
//        //39.851564   116.432799;
//        //onMapPoiClick: 一多监测 latitude: 22.394200906676026 longitude: 113.55899456862457
//        LatLng mInitLatLng = new LatLng(latitude, longtitude);//一多地理位置
//        mLatLng = mInitLatLng;
//        //定义地图状态
//        MapStatus mMapStatus = new MapStatus.Builder()
//                .target(mInitLatLng)
//                .zoom(14)
//                .build();
//        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
//        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//        mBaiduMap = mTextureMapView.getMap();
//
//        BitmapDescriptor initBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
//        MarkerOptions initOptions = new MarkerOptions().position(mInitLatLng).icon(initBitmap);
//        mBaiduMap.addOverlay(initOptions);
//
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//        mBaiduMap.setBuildingsEnabled(true);
//        mBaiduMap.setMapStatus(mMapStatusUpdate);
//
//        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//                mBaiduMap.clear();
//
//                mLatLng = latLng;
//
//                Log.i(TAG, "onMapClick: latitude: " + latLng.latitude + " longitude: " + latLng.longitude);
//
//                //更改标记点
//                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
//                MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmap).title("当前位置");
//
//                mBaiduMap.showInfoWindow(new InfoWindow(getView(latLng, null), latLng, -100));
//
//                mBaiduMap.addOverlay(options);
//
//            }
//
//            @Override
//            public boolean onMapPoiClick(MapPoi mapPoi) {
//
//                LatLng latLng = mapPoi.getPosition();
//                mBaiduMap.clear();
//
//                mLatLng = latLng;
//
//                //更改标记点
//                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
//                MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmap).title(mapPoi.getName());
//
//                mBaiduMap.showInfoWindow(new InfoWindow(getView(latLng, mapPoi.getName()), latLng, -100));
//
//                mBaiduMap.addOverlay(options);
//
//                Log.i(TAG, "onMapPoiClick: " + mapPoi.getName() + " latitude: " + latLng.latitude + " longitude: " + latLng.longitude);
//
//                return true;
//            }
//        });
//    }
}
