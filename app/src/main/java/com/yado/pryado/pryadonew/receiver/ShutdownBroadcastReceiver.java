package com.yado.pryado.pryadonew.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yado.pryado.pryadonew.util.UserLogoutUtil;

public class ShutdownBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "ShutdownBroadcastReceiver";
    private static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
    private static final String ACTION_REBOOT = "android.intent.action.REBOOT";

    @SuppressLint({"LongLogTag", "CheckResult"})
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_SHUTDOWN) || intent.getAction().equals(ACTION_REBOOT)) {
//            UserLogoutUtil.logout(true);
            Log.e(TAG, "关机了或重启了");
        }
    }
}
