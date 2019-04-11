package com.yado.pryado.pryadonew.bean;

import java.util.List;

/**
 * Created by Ulez on 2017/5/10.
 * Email：lcy1532110757@gmail.com
 */


public class AlertBean {


    /**
     * rows : [{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 8:20:22","AlarmID":"669447","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"08:20:22.635","AlarmValue":"40.1","Company":"一多监测","ConfirmDate":"2017/7/5 10:03:00","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"曾庆利"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 8:14:20","AlarmID":"669446","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"08:14:20.736","AlarmValue":"44.5","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 8:12:21","AlarmID":"669445","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"08:12:21.738","AlarmValue":"42.3","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 7:59:27","AlarmID":"669444","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"07:59:27.247","AlarmValue":"41.2","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 7:36:57","AlarmID":"669443","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"07:36:57.631","AlarmValue":"41.5","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 7:33:55","AlarmID":"669442","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"07:33:55.074","AlarmValue":"41.6","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 7:25:59","AlarmID":"669441","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"07:25:59.453","AlarmValue":"42.6","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 7:21:26","AlarmID":"669440","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"07:21:26.707","AlarmValue":"40.5","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 7:10:59","AlarmID":"669439","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"07:10:59.355","AlarmValue":"41.3","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 7:03:37","AlarmID":"669438","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"07:03:37.213","AlarmValue":"40.4","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 6:41:24","AlarmID":"669437","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"06:41:24.632","AlarmValue":"40.7","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 6:29:19","AlarmID":"669436","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"06:29:19.984","AlarmValue":"40.3","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 3:42:26","AlarmID":"669435","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"03:42:26.065","AlarmValue":"40","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 2:48:27","AlarmID":"669434","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"02:48:27.943","AlarmValue":"40.6","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 1:16:34","AlarmID":"669433","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"01:16:34.799","AlarmValue":"40.2","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 0:27:23","AlarmID":"669432","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"00:27:23.730","AlarmValue":"41.1","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 0:04:46","AlarmID":"669431","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"00:04:46.128","AlarmValue":"41.6","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"一般","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/31","AlarmDateTime":"2017/5/31 0:03:00","AlarmID":"669430","AlarmMaxValue":"30","AlarmState":"0","AlarmTime":"00:03:00.265","AlarmValue":"41.7","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/30","AlarmDateTime":"2017/5/30 22:51:38","AlarmID":"669428","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"22:51:38.119","AlarmValue":"41.4","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"},{"ALarmType":"严重","AlarmAddress":"下触头","AlarmArea":"P05联络柜","AlarmCate":"温度","AlarmConfirm":"已确认","AlarmDate":"2017/05/30","AlarmDateTime":"2017/5/30 22:18:34","AlarmID":"669427","AlarmMaxValue":"40","AlarmState":"0","AlarmTime":"22:18:34.527","AlarmValue":"41.7","Company":"一多监测","ConfirmDate":"2017/6/1 9:07:33","DID":"8","PID":"1","TagID":"11","Units":"℃","UserName":"admin"}]
     * total : 1152
     */

    private int total;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * ALarmType : 严重
         * AlarmAddress : 下触头
         * AlarmArea : P05联络柜
         * AlarmCate : 温度
         * AlarmConfirm : 已确认
         * AlarmDate : 2017/05/31
         * AlarmDateTime : 2017/5/31 8:20:22
         * AlarmID : 669447
         * AlarmMaxValue : 40
         * AlarmState : 0
         * AlarmTime : 08:20:22.635
         * AlarmValue : 40.1
         * Company : 一多监测
         * ConfirmDate : 2017/7/5 10:03:00
         * DID : 8
         * PID : 1
         * TagID : 11
         * Units : ℃
         * UserName : 曾庆利
         */

        private String ALarmType;
        private String AlarmAddress;
        private String AlarmArea;
        private String AlarmCate;
        private String AlarmConfirm;
        private String AlarmDate;
        private String AlarmDateTime;
        private String AlarmID;
        private String AlarmMaxValue;
        private String AlarmState;
        private String AlarmTime;
        private String AlarmValue;
        private String Company;
        private String ConfirmDate;
        private String DID;
        private String PID;
        private String TagID;
        private String Units;
        private String UserName;

        public String getALarmType() {
            return ALarmType;
        }

        public void setALarmType(String ALarmType) {
            this.ALarmType = ALarmType;
        }

        public String getAlarmAddress() {
            return AlarmAddress;
        }

        public void setAlarmAddress(String AlarmAddress) {
            this.AlarmAddress = AlarmAddress;
        }

        public String getAlarmArea() {
            return AlarmArea;
        }

        public void setAlarmArea(String AlarmArea) {
            this.AlarmArea = AlarmArea;
        }

        public String getAlarmCate() {
            return AlarmCate;
        }

        public void setAlarmCate(String AlarmCate) {
            this.AlarmCate = AlarmCate;
        }

        public String getAlarmConfirm() {
            return AlarmConfirm;
        }

        public void setAlarmConfirm(String AlarmConfirm) {
            this.AlarmConfirm = AlarmConfirm;
        }

        public String getAlarmDate() {
            return AlarmDate;
        }

        public void setAlarmDate(String AlarmDate) {
            this.AlarmDate = AlarmDate;
        }

        public String getAlarmDateTime() {
            return AlarmDateTime;
        }

        public void setAlarmDateTime(String AlarmDateTime) {
            this.AlarmDateTime = AlarmDateTime;
        }

        public String getAlarmID() {
            return AlarmID;
        }

        public void setAlarmID(String AlarmID) {
            this.AlarmID = AlarmID;
        }

        public String getAlarmMaxValue() {
            return AlarmMaxValue;
        }

        public void setAlarmMaxValue(String AlarmMaxValue) {
            this.AlarmMaxValue = AlarmMaxValue;
        }

        public String getAlarmState() {
            return AlarmState;
        }

        public void setAlarmState(String AlarmState) {
            this.AlarmState = AlarmState;
        }

        public String getAlarmTime() {
            return AlarmTime;
        }

        public void setAlarmTime(String AlarmTime) {
            this.AlarmTime = AlarmTime;
        }

        public String getAlarmValue() {
            return AlarmValue;
        }

        public void setAlarmValue(String AlarmValue) {
            this.AlarmValue = AlarmValue;
        }

        public String getCompany() {
            return Company;
        }

        public void setCompany(String Company) {
            this.Company = Company;
        }

        public String getConfirmDate() {
            return ConfirmDate;
        }

        public void setConfirmDate(String ConfirmDate) {
            this.ConfirmDate = ConfirmDate;
        }

        public String getDID() {
            return DID;
        }

        public void setDID(String DID) {
            this.DID = DID;
        }

        public String getPID() {
            return PID;
        }

        public void setPID(String PID) {
            this.PID = PID;
        }

        public String getTagID() {
            return TagID;
        }

        public void setTagID(String TagID) {
            this.TagID = TagID;
        }

        public String getUnits() {
            return Units;
        }

        public void setUnits(String Units) {
            this.Units = Units;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }
    }
}
