package com.yado.pryado.pryadonew.bean;

import java.util.LinkedHashMap;

/**
 * Created by Ulez on 2017/8/1.
 * Email：lcy1532110757@gmail.com
 */


public class RoomDetail {

    private HiPointHisDataEntity HiPointHisData;

    public HiPointHisDataEntity getHiPointHisData() {
        return HiPointHisData;
    }

    public void setHiPointHisData(HiPointHisDataEntity hiPointHisData) {
        HiPointHisData = hiPointHisData;
    }

    public class HiPointHisDataEntity {
        private LinkedHashMap<String, String> AllHiPoint;
        private LinkedHashMap<String, String> AllEvPoint;

        private LinkedHashMap<String, String> MonHiPoint;
        private LinkedHashMap<String, String> MonEvPoint;

        private LinkedHashMap<String, String> WeeHiPoint;
        private LinkedHashMap<String, String> WeeEvPoint;

        private LinkedHashMap<String, String> DayHiPoint;
        private LinkedHashMap<String, String> DayEvPoint;

        private String AlarmStatus;
        private String AlarmVal;
        private String TypeName;
        private String max_name;

        public String getMax_name() {
            return max_name;
        }

        public void setMax_name(String max_name) {
            this.max_name = max_name;
        }

        private String Position;
        private String TagName;
        private String TagID;

        public LinkedHashMap<String, String> getAllHiPoint() {
            return AllHiPoint;
        }

        public void setAllHiPoint(LinkedHashMap<String, String> allHiPoint) {
            AllHiPoint = allHiPoint;
        }

        public LinkedHashMap<String, String> getAllEvPoint() {
            return AllEvPoint;
        }

        public void setAllEvPoint(LinkedHashMap<String, String> allEvPoint) {
            AllEvPoint = allEvPoint;
        }

        public LinkedHashMap<String, String> getMonHiPoint() {
            return MonHiPoint;
        }

        public void setMonHiPoint(LinkedHashMap<String, String> monHiPoint) {
            MonHiPoint = monHiPoint;
        }

        public LinkedHashMap<String, String> getMonEvPoint() {
            return MonEvPoint;
        }

        public void setMonEvPoint(LinkedHashMap<String, String> monEvPoint) {
            MonEvPoint = monEvPoint;
        }

        public LinkedHashMap<String, String> getWeeHiPoint() {
            return WeeHiPoint;
        }

        public void setWeeHiPoint(LinkedHashMap<String, String> weeHiPoint) {
            WeeHiPoint = weeHiPoint;
        }

        public LinkedHashMap<String, String> getWeeEvPoint() {
            return WeeEvPoint;
        }

        public void setWeeEvPoint(LinkedHashMap<String, String> weeEvPoint) {
            WeeEvPoint = weeEvPoint;
        }

        public LinkedHashMap<String, String> getDayHiPoint() {
            return DayHiPoint;
        }

        public void setDayHiPoint(LinkedHashMap<String, String> dayHiPoint) {
            DayHiPoint = dayHiPoint;
        }

        public LinkedHashMap<String, String> getDayEvPoint() {
            return DayEvPoint;
        }

        public void setDayEvPoint(LinkedHashMap<String, String> dayEvPoint) {
            DayEvPoint = dayEvPoint;
        }

        public String getAlarmStatus() {
            return AlarmStatus;
        }

        public void setAlarmStatus(String alarmStatus) {
            AlarmStatus = alarmStatus;
        }

        public String getAlarmVal() {
            return AlarmVal;
        }

        public void setAlarmVal(String alarmVal) {
            AlarmVal = alarmVal;
        }

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String typeName) {
            TypeName = typeName;
        }

        public String getPosition() {
            return Position;
        }

        public void setPosition(String position) {
            Position = position;
        }

        public String getTagName() {
            return TagName;
        }

        public void setTagName(String tagName) {
            TagName = tagName;
        }

        public String getTagID() {
            return TagID;
        }

        public void setTagID(String tagID) {
            TagID = tagID;
        }
    }

    /**
     * image_url : http://113.106.90.51:8004/Content/Image/p1.png
     * pdname : 一多监测
     * pid : 1
     * status : 0
     * web_url : http://113.106.90.51:8004/Monitor/Index?pid=1
     */

    private String image_url;
    private String pdname;
    private String pid;
    private String status;
    private String web_url;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPdname() {
        return pdname;
    }

    public void setPdname(String pdname) {
        this.pdname = pdname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }
}
