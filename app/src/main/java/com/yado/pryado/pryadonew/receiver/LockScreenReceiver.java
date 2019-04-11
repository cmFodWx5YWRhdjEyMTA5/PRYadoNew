package com.yado.pryado.pryadonew.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.yado.pryado.pryadonew.ui.LockActivity;

public class LockScreenReceiver extends BroadcastReceiver {
    private static final String TAG = "LockScreenMsgReceiver";
    private static final String NOTICE_CANCEL = "notice_cancel";
    public static int type;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive:收到了锁屏消息 ");
        String action = intent.getAction();
        if (TAG.equals(action)) {
            //管理锁屏的一个服务
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            if (!isScreenOn) {
                Log.i(TAG, "onReceive:锁屏了 ");
                //判断是否锁屏
                Intent alarmIntent = new Intent(context, LockActivity.class);
                //在广播中启动Activity的context可能不是Activity对象，所以需要添加NEW_TASK的标志，否则启动时可能会报错。
                alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(alarmIntent); //启动显示锁屏消息的activity
            }
        } else if (NOTICE_CANCEL.equals(action) && type > 0) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.cancel(type);
        }
    }

}
