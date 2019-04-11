package com.yado.pryado.pryadonew.bean;

import java.util.LinkedHashMap;

/**
 * Created by eado on 2017/1/18.
 */

public class BugDetail {

    /**
     * BugID : 6
     * PID : 1
     * PName : 一多监测
     * DID : 5
     * DeviceName : P02进线柜
     * AlarmID : 667447
     * BugLocation : P02进线柜电缆头
     * BugDesc : P02进线柜电缆头
     * BugLevel : 一般
     * ReportWay : app
     * ReportDate : 2017/1/13 10:20:11
     * UserName :
     * HandeSituation : 未审核
     * HandleDate :
     * EntityState : Unchanged
     * EntityKey : System.Data.EntityKey
     * AlarmMaxValue : 7
     * ALarmValue : 107.86
     * ALarmType : 预警
     * DayHiPoint : {"2016/9/17 0:00:00":25.5,"2016/9/17 1:00:00":25.5,"2016/9/17 2:00:00":25.5,"2016/9/17 3:00:00":25.5,"2016/9/17 4:00:00":25.5,"2016/9/17 5:00:00":25.5,"2016/9/17 6:00:00":25.5,"2016/9/17 7:00:00":25.5,"2016/9/17 8:00:00":25.5,"2016/9/17 9:00:00":25.5,"2016/9/17 10:00:00":25.5,"2016/9/17 11:00:00":25.5,"2016/9/17 12:00:00":25.5,"2016/9/17 13:00:00":25.5,"2016/9/17 14:00:00":25.5,"2016/9/17 15:00:00":25.5,"2016/9/17 16:00:00":25.5,"2016/9/17 17:00:00":25.5,"2016/9/17 18:00:00":25.5,"2016/9/17 19:00:00":25.5,"2016/9/17 20:00:00":25.5,"2016/9/17 21:00:00":25.5,"2016/9/17 22:00:00":25.5,"2016/9/17 23:00:00":25.5}
     */

    private int BugID;
    private String PID;
    private String PName;
    private String DID;
    private String DeviceName;
    private String AlarmID;
    private String BugLocation;
    private String BugDesc;
    private String BugLevel;
    private String ReportWay;
    private String ReportDate;
    private String UserName;
    private String HandeSituation;
    private String HandleDate;
    private String EntityState;
    private String EntityKey;
    private String AlarmMaxValue;
    private String ALarmValue;
    private String ALarmType;
    private LinkedHashMap<String,Float> DayHiPoint;

    public String getBugLocation() {
        return BugLocation;
    }

    public void setBugLocation(String bugLocation) {
        BugLocation = bugLocation;
    }

    public int getBugID() {
        return BugID;
    }

    public void setBugID(int bugID) {
        BugID = bugID;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getPName() {
        return PName;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    public String getDID() {
        return DID;
    }

    public void setDID(String DID) {
        this.DID = DID;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getAlarmID() {
        return AlarmID;
    }

    public void setAlarmID(String alarmID) {
        AlarmID = alarmID;
    }

    public String getBugDesc() {
        return BugDesc;
    }

    public void setBugDesc(String bugDesc) {
        BugDesc = bugDesc;
    }

    public String getBugLevel() {
        return BugLevel;
    }

    public void setBugLevel(String bugLevel) {
        BugLevel = bugLevel;
    }

    public String getReportWay() {
        return ReportWay;
    }

    public void setReportWay(String reportWay) {
        ReportWay = reportWay;
    }

    public String getReportDate() {
        return ReportDate;
    }

    public void setReportDate(String reportDate) {
        ReportDate = reportDate;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getHandeSituation() {
        return HandeSituation;
    }

    public void setHandeSituation(String handeSituation) {
        HandeSituation = handeSituation;
    }

    public String getHandleDate() {
        return HandleDate;
    }

    public void setHandleDate(String handleDate) {
        HandleDate = handleDate;
    }

    public String getEntityState() {
        return EntityState;
    }

    public void setEntityState(String entityState) {
        EntityState = entityState;
    }

    public String getEntityKey() {
        return EntityKey;
    }

    public void setEntityKey(String entityKey) {
        EntityKey = entityKey;
    }

    public String getAlarmMaxValue() {
        return AlarmMaxValue;
    }

    public void setAlarmMaxValue(String alarmMaxValue) {
        AlarmMaxValue = alarmMaxValue;
    }

    public String getALarmValue() {
        return ALarmValue;
    }

    public void setALarmValue(String ALarmValue) {
        this.ALarmValue = ALarmValue;
    }

    public String getALarmType() {
        return ALarmType;
    }

    public void setALarmType(String ALarmType) {
        this.ALarmType = ALarmType;
    }

    public LinkedHashMap<String, Float> getDayHiPoint() {
        return DayHiPoint;
    }

    public void setDayHiPoint(LinkedHashMap<String, Float> dayHiPoint) {
        DayHiPoint = dayHiPoint;
    }
}
