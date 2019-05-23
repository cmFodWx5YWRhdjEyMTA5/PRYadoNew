package com.yado.pryado.pryadonew.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eado on 2016/7/8.
 */
public class Util {
    // 取得版本号
    private static PackageInfo GetVersion(Context context) {
        PackageManager manager;
        PackageInfo info = null;
        manager = context.getPackageManager();
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info;
        } catch (PackageManager.NameNotFoundException e) {// TODO Auto-generated catch block
            return null;
        }
    }

    public static String getVersionName(Context context){
        try {
            return GetVersion(context).versionName;
        }catch (Exception e){
            return "未知";
        }

    }
    public static int getVersionCode(Context context){
        try {
            return GetVersion(context).versionCode;
        }catch (Exception e){
            return 0;
        }
    }

    public static String matchChanel(String webUrl) {
        Pattern pattern = Pattern.compile("drv=\\d*");
        Matcher matcher = pattern.matcher(webUrl);
        matcher.find();
        String sss = matcher.group(0);
        return sss.substring(sss.indexOf("drv=") + 4, sss.length());
    }

    private static long lastClickTime;
    private final static int SPACE_TIME = 500;
    /**
     * 防止多次点击创建多个页面
     */
    public synchronized static boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClick2;
        if (currentTime - lastClickTime >
                SPACE_TIME) {
            isClick2 = false;
        } else {
            isClick2 = true;
        }
        lastClickTime = currentTime;
        return isClick2;
    }


}
