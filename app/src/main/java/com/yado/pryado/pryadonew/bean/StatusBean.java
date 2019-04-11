package com.yado.pryado.pryadonew.bean;

/**
 * Created by eado on 2016/6/8.
 * 视频监控的状态类
 */
public class StatusBean {
    private String status;

    private int alarmCount;

    public StatusBean(String status) {
        this.status = status;
    }

    public void setAlarmCount(int alarmCount) {
        this.alarmCount = alarmCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAlarmCount() {
        return alarmCount;
    }
}
