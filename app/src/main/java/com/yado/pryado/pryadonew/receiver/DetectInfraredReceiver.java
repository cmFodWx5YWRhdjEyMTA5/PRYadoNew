package com.yado.pryado.pryadonew.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yado.pryado.pryadonew.MyConstants;

import org.greenrobot.eventbus.EventBus;

/** 接收红外app广播(>=android7.0)
 *
 * @author Administrator
 * @date 2018/2/1
 */

public class DetectInfraredReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String s = intent.getAction();
        assert s != null;
        if (s.equals(MyConstants.INFRARED_ACTION)){
            String infraredPath = intent.getStringExtra("msg");
            float maxTemp = intent.getFloatExtra("maxTemp", 0);
            float minTemp = intent.getFloatExtra("minTemp", 0);
            EventBus.getDefault().post(new InfraredEvent(infraredPath, maxTemp, minTemp));
        }
    }

    public class InfraredEvent{
        private String infraredPath;
        private float maxTemp;
        private float minTemp;

        public InfraredEvent(String infraredPath, float maxTemp, float minTemp) {
            this.infraredPath = infraredPath;
            this.maxTemp = maxTemp;
            this.minTemp = minTemp;
        }

        public String getInfraredPath() {
            return infraredPath;
        }

        public float getMaxTemp() {
            return maxTemp;
        }

        public float getMinTemp() {
            return minTemp;
        }
    }
}
