package com.yado.pryado.pryadonew.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.yado.pryado.pryadonew.InfraredInfo;
import com.yado.pryado.pryadonew.MyApplication;
import com.yado.pryado.pryadonew.MyConstants;
import com.yado.pryado.pryadonew.base.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;

public class AddPhotoUtil {

    public  String TAG = "AddPhotoUtil";
    @SuppressLint("StaticFieldLeak")
    public static AddPhotoUtil instance;
    private WeakReference<Context> mContext;
//    private Context mContext;
    public boolean fromInfred;

    public static AddPhotoUtil getInstance(Context context) {
        if (instance == null) {
            instance = new AddPhotoUtil(context);
        }
        return instance;
    }

    private AddPhotoUtil(Context context){
        mContext = new WeakReference<>(context);
    }

    /**
     * 添加常规图片
     */
    public void addPic(ArrayList<String> photos_path) {
        PhotoPicker.builder()
                .setPhotoCount(9)
                .setShowCamera(false)
                .setPreviewEnabled(false)
                .setSelected(photos_path)
                .setFrom(MyConstants.ADD_NORMAL_PHOTO)
                .start((Activity) mContext.get());
    }

    /**
     * 跳转到红外APP拍照
     */
    public void goToInfredApp() {
        //缺少返回的结果
        if (MyApplication.isAppInstalled(MyConstants.INFRARED_PACKAGE_NAME)) {
            Intent mLaunchIntent = mContext.get().getPackageManager().getLaunchIntentForPackage(MyConstants.INFRARED_PACKAGE_NAME);
            mLaunchIntent.putExtra(InfraredInfo.JUMP_KEY, InfraredInfo.PREADO_KEY);
            ((BaseActivity)mContext.get()).startActivityForResult(mLaunchIntent, 0x2000);
            fromInfred = true;
        } else {
            ToastUtils.showShort("没有安装红外双视...");
        }
    }

    public void getInfrared(String path, HashMap<String, String> temps, float minTemp, float maxTemp, List<String>mInfraredPathList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (path != null && path.length() > 0) {
                mInfraredPathList.add(path);
                if ((maxTemp != 0 && minTemp != 0)) {
                    temps.put(path, maxTemp + "_" + minTemp);
                }
                fromInfred = false;
            }
        } else {
            ContentResolver mResolver = mContext.get().getContentResolver();
            Cursor mCursor = mResolver.query(InfraredInfo.CONTENT_URI, null, null, null, null);

            if (mCursor != null) {
                //会执行完毕后清空数据，故游标只有一条记录
                while (mCursor.moveToNext()) {

                    String infraredPath = mCursor.getString(mCursor.getColumnIndex(InfraredInfo.COLUMN_PATH));
                    Log.e(TAG, "onResume: " + path);

                    if (!mInfraredPathList.contains(path)) {
                        mInfraredPathList.add(path);
                        String infrareMaxTemp = mCursor.getString(mCursor.getColumnIndex(InfraredInfo.COLUMN_MAX_TEMP));
                        String infrareMinTemp = mCursor.getString(mCursor.getColumnIndex(InfraredInfo.COLUMN_MIN_TEMP));
                        temps.put(infraredPath, infrareMaxTemp + "_" + infrareMinTemp);

                    }

                }
                //清空数据
                mResolver.delete(MyConstants.CONTENT_URI, InfraredInfo.COLUMN_PATH + "!=?", new String[]{" "});
                mCursor.close();
            }
        }
    }

    public void reset() {
        if (instance != null) {
            instance = null;
        }
    }

}
