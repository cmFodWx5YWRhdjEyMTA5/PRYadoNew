package com.yado.pryado.pryadonew.bean;

import java.util.LinkedHashMap;

/**
 * Created by eado on 2016/4/20.
 */
public class RoomStatusBean {

    /**
     * AllHiPoint : {"2014-12-24 14:50:16":86.556,"2015-1-14 14:08:57":0,"2015-2-4 10:18:00":21,"2015-2-17 0:00:00":21,"2015-3-18 0:00:01":26,"2015-4-8 0:00:00":28,"2015-4-24 0:00:00":27,"2015-5-19 11:45:01":38,"2015-6-9 15:00:00":47,"2015-6-30 14:51:00":47,"2015-7-21 2:03:00":43,"2015-8-5 9:57:00":0,"2015-9-1 16:21:00":41,"2015-9-22 16:27:00":41,"2015-10-13 9:39:00":28,"2015-11-2 0:00:00":39,"2015-11-24 16:31:00":30,"2015-12-15 10:01:05":24,"2016-1-5 15:01:28":25,"2016-1-26 10:41:03":15,"2016-2-16 16:21:06":19,"2016-3-8 16:32:30":25,"2016-3-29 16:10:44":23,"2016-4-19 0:01:34":27}
     * AllEvPoint : {"2014-12-24 14:50:16":86.556,"2015-1-14 14:08:57":0,"2015-2-4 10:18:00":21,"2015-2-17 0:00:00":21,"2015-3-18 0:00:01":26,"2015-4-8 0:00:00":28,"2015-4-24 0:00:00":27,"2015-5-19 11:45:01":38,"2015-6-9 15:00:00":47,"2015-6-30 14:51:00":47,"2015-7-21 2:03:00":43,"2015-8-5 9:57:00":0,"2015-9-1 16:21:00":41,"2015-9-22 16:27:00":41,"2015-10-13 9:39:00":28,"2015-11-2 0:00:00":39,"2015-11-24 16:31:00":30,"2015-12-15 10:01:05":24,"2016-1-5 15:01:28":25,"2016-1-26 10:41:03":15,"2016-2-16 16:21:06":19,"2016-3-8 16:32:30":25,"2016-3-29 16:10:44":23,"2016-4-19 0:01:34":27}
     * MonHiPoint : {"2016-4-1 12:00:00":0,"2016-4-2 12:00:00":0,"2016-4-3 12:00:00":0,"2016-4-4 12:00:00":0,"2016-4-5 15:10:49":26,"2016-4-6 14:20:47":27,"2016-4-7 15:40:50":28,"2016-4-8 16:30:50":29,"2016-4-9 13:10:47":28,"2016-4-10 0:00:48":27,"2016-4-11 16:20:49":29,"2016-4-12 10:30:49":27,"2016-4-13 12:21:00":27,"2016-4-14 10:01:03":27,"2016-4-15 9:11:02":29,"2016-4-16 0:01:01":27,"2016-4-17 0:01:02":27,"2016-4-18 11:51:03":27,"2016-4-19 0:01:34":27,"2016-4-20 13:01:01":27,"2016-4-21 12:00:00":0,"2016-4-22 12:00:00":0,"2016-4-23 12:00:00":0,"2016-4-24 12:00:00":0,"2016-4-25 12:00:00":0,"2016-4-26 12:00:00":0,"2016-4-27 12:00:00":0,"2016-4-28 12:00:00":0,"2016-4-29 12:00:00":0,"2016-4-30 12:00:00":0}
     * MonEvPoint : {"2016-4-1 12:00:00":0,"2016-4-2 12:00:00":0,"2016-4-3 12:00:00":0,"2016-4-4 12:00:00":0,"2016-4-5 15:10:49":26,"2016-4-6 14:20:47":27,"2016-4-7 15:40:50":28,"2016-4-8 16:30:50":29,"2016-4-9 13:10:47":28,"2016-4-10 0:00:48":27,"2016-4-11 16:20:49":29,"2016-4-12 10:30:49":27,"2016-4-13 12:21:00":27,"2016-4-14 10:01:03":27,"2016-4-15 9:11:02":29,"2016-4-16 0:01:01":27,"2016-4-17 0:01:02":27,"2016-4-18 11:51:03":27,"2016-4-19 0:01:34":27,"2016-4-20 13:01:01":27,"2016-4-21 12:00:00":0,"2016-4-22 12:00:00":0,"2016-4-23 12:00:00":0,"2016-4-24 12:00:00":0,"2016-4-25 12:00:00":0,"2016-4-26 12:00:00":0,"2016-4-27 12:00:00":0,"2016-4-28 12:00:00":0,"2016-4-29 12:00:00":0,"2016-4-30 12:00:00":0}
     * WeeHiPoint : {"2016-4-18 11:51:03":27,"2016-4-19 0:01:34":27,"2016-4-20 13:01:01":27,"2016-4-21 12:00:00":0,"2016-4-22 12:00:00":0,"2016-4-23 12:00:00":0,"2016-4-24 12:00:00":0}
     * WeeEvPoint : {"2016-4-18 11:51:03":27,"2016-4-19 0:01:34":27,"2016-4-20 13:01:01":27,"2016-4-21 12:00:00":0,"2016-4-22 12:00:00":0,"2016-4-23 12:00:00":0,"2016-4-24 12:00:00":0}
     * DayHiPoint : {"2016-4-20 0:51:35":27,"2016-4-20 1:51:35":27,"2016-4-20 2:51:35":27,"2016-4-20 3:51:35":27,"2016-4-20 4:51:35":27,"2016-4-20 5:51:35":27,"2016-4-20 6:51:35":27,"2016-4-20 7:51:36":27,"2016-4-20 8:51:35":27,"2016-4-20 9:51:35":27,"2016-4-20 10:51:01":27,"2016-4-20 11:51:00":27,"2016-4-20 12:51:00":27,"2016-4-20 13:01:01":27,"2016-4-21 2:00:00":0,"2016-4-21 3:00:00":0,"2016-4-21 4:00:00":0,"2016-4-21 5:00:00":0,"2016-4-21 6:00:00":0,"2016-4-21 7:00:00":0,"2016-4-21 8:00:00":0,"2016-4-21 9:00:00":0,"2016-4-21 10:00:00":0,"2016-4-21 11:00:00":0}
     * DayEvPoint : {"2016-4-20 0:51:35":27,"2016-4-20 1:51:35":27,"2016-4-20 2:51:35":27,"2016-4-20 3:51:35":27,"2016-4-20 4:51:35":27,"2016-4-20 5:51:35":27,"2016-4-20 6:51:35":27,"2016-4-20 7:51:36":27,"2016-4-20 8:51:35":27,"2016-4-20 9:51:35":27,"2016-4-20 10:51:01":27,"2016-4-20 11:51:00":27,"2016-4-20 12:51:00":27,"2016-4-20 13:01:01":27,"2016-4-21 2:00:00":0,"2016-4-21 3:00:00":0,"2016-4-21 4:00:00":0,"2016-4-21 5:00:00":0,"2016-4-21 6:00:00":0,"2016-4-21 7:00:00":0,"2016-4-21 8:00:00":0,"2016-4-21 9:00:00":0,"2016-4-21 10:00:00":0,"2016-4-21 11:00:00":0}
     * AlarmStatus : 正常
     * AlarmVal :
     * TypeName : 测点温度
     * Position : 上触头
     * TagName : P02_T1C
     * TagID : 2
     */

    private HiPointHisDataEntity HiPointHisData;

    public HiPointHisDataEntity getHiPointHisData() {
        return HiPointHisData;
    }

    public void setHiPointHisData(HiPointHisDataEntity hiPointHisData) {
        HiPointHisData = hiPointHisData;
    }

   public class HiPointHisDataEntity {
        private LinkedHashMap<String,Float> AllHiPoint;
        private LinkedHashMap<String,Float> AllEvPoint;

        private LinkedHashMap<String,Float> MonHiPoint;
        private LinkedHashMap<String,Float> MonEvPoint;

        private LinkedHashMap<String,Float> WeeHiPoint;
        private LinkedHashMap<String,Float> WeeEvPoint;

        private LinkedHashMap<String,Float> DayHiPoint;
        private LinkedHashMap<String,Float> DayEvPoint;

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

        public LinkedHashMap<String,Float> getAllHiPoint() {
            return AllHiPoint;
        }

        public void setAllHiPoint(LinkedHashMap<String,Float> allHiPoint) {
            AllHiPoint = allHiPoint;
        }

        public LinkedHashMap<String,Float> getAllEvPoint() {
            return AllEvPoint;
        }

        public void setAllEvPoint(LinkedHashMap<String,Float> allEvPoint) {
            AllEvPoint = allEvPoint;
        }

        public LinkedHashMap<String,Float> getMonHiPoint() {
            return MonHiPoint;
        }

        public void setMonHiPoint(LinkedHashMap<String,Float> monHiPoint) {
            MonHiPoint = monHiPoint;
        }

        public LinkedHashMap<String,Float> getMonEvPoint() {
            return MonEvPoint;
        }

        public void setMonEvPoint(LinkedHashMap<String,Float> monEvPoint) {
            MonEvPoint = monEvPoint;
        }

        public LinkedHashMap<String,Float> getWeeHiPoint() {
            return WeeHiPoint;
        }

        public void setWeeHiPoint(LinkedHashMap<String,Float> weeHiPoint) {
            WeeHiPoint = weeHiPoint;
        }

        public LinkedHashMap<String,Float> getWeeEvPoint() {
            return WeeEvPoint;
        }

        public void setWeeEvPoint(LinkedHashMap<String,Float> weeEvPoint) {
            WeeEvPoint = weeEvPoint;
        }

        public LinkedHashMap<String,Float> getDayHiPoint() {
            return DayHiPoint;
        }

        public void setDayHiPoint(LinkedHashMap<String,Float> dayHiPoint) {
            DayHiPoint = dayHiPoint;
        }

        public LinkedHashMap<String,Float> getDayEvPoint() {
            return DayEvPoint;
        }

        public void setDayEvPoint(LinkedHashMap<String,Float> dayEvPoint) {
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
