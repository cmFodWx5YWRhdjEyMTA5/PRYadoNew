package com.yado.pryado.pryadonew.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by Eric on 2016/5/9.
 */
public class NetworkChecker {

    public static final int TYPE_NONE = 0;
    public static final int TYPE_WIFI = 1;//WiFi
    public static final int TYPE_MOBILE = 2;//移动蜂窝数据，禁止数据漫游
    public static final int TYPE_ROAMING = 3;//数据漫游

    private Context mContext;
    private ConnectivityManager mConnectivity;
    private TelephonyManager mTelephony;
    private NetworkInfo info;


    public NetworkChecker(Context mContext) {
        this.mContext = mContext;
        mConnectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mTelephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        info = mConnectivity.getActiveNetworkInfo();

    }

    /**
     * 判断有无网络连接
     *
     * @return true 当连接网络时，否则返回false
     */
    public boolean isConnected() {
        //检查网络连接
        return !(info == null || !mConnectivity.getBackgroundDataSetting());
    }

    /**
     * 检查网络类型
     *
     * @return int类型的对应返回值
     */
    public int getNetType() {
        int netType = info.getType();
        int netSubtype = info.getSubtype();//TelephonyManager.NETWORK_TYPE

        if (netType == ConnectivityManager.TYPE_WIFI) {
            return TYPE_WIFI;
        } else if ((netType == ConnectivityManager.TYPE_MOBILE || netType == ConnectivityManager.TYPE_MOBILE_DUN) && !mTelephony.isNetworkRoaming()) {
            return TYPE_MOBILE;
        } else if ((netType == ConnectivityManager.TYPE_MOBILE || netType == ConnectivityManager.TYPE_MOBILE_DUN) && mTelephony.isNetworkRoaming()) {
            return TYPE_ROAMING;
        } else {
            return TYPE_NONE;
        }
    }

    /**
     * 判断WiFi和移动蜂窝数据是否已连接
     *
     * @return true 当已连接WiFi或移动蜂窝数据时
     */
    public boolean checkNetworkConnection() {
//        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = mConnectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo mobile = mConnectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        //getState()方法是查询是否连接了数据网络
        return wifi.isAvailable() || mobile.isAvailable();
    }

    /**
     * 判断WiFi是否已连接
     *
     * @return true 当已连接WiFi时
     */
    public boolean isWiFiConnected() {
//        ConnectivityManager mConnectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo mActiveNetInfo = mConnectivity.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断移动网络连接是否正常
     *
     * @return true 当已连接移动蜂窝数据时
     */
    public boolean isMobileConnected() {
//            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobileNetworkInfo = mConnectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);   //获取移动网络信息
        //getState()方法是查询是否连接了数据网络
        return mMobileNetworkInfo != null && mMobileNetworkInfo.isAvailable();
    }

}
