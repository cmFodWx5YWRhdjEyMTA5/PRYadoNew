package com.yado.pryado.pryadonew.bean;

/**
 * Created by eado on 2016/7/6.
 */
public class PullBean {

    /**
     * AlarmCount : 2
     * lastAlarmTime : 2016/07/06 04:48:32.959
     * message : 有2条新的报警信息！
     * title : 有新报警信息
     */

    private int AlarmCount;
    private String lastAlarmTime;
    private String message;
    private String title;

    public int getAlarmCount() {
        return AlarmCount;
    }

    public void setAlarmCount(int AlarmCount) {
        this.AlarmCount = AlarmCount;
    }

    public String getLastAlarmTime() {
        return lastAlarmTime;
    }

    public void setLastAlarmTime(String lastAlarmTime) {
        this.lastAlarmTime = lastAlarmTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "PullBean{" +
                "AlarmCount=" + AlarmCount +
                ", lastAlarmTime='" + lastAlarmTime + '\'' +
                ", message='" + message + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
