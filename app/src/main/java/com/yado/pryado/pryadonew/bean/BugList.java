package com.yado.pryado.pryadonew.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by eado on 2017/1/18.
 */

public class BugList {


    private List<ListmapBean> listmap;

    public List<ListmapBean> getListmap() {
        return listmap;
    }

    public void setListmap(List<ListmapBean> listmap) {
        this.listmap = listmap;
    }

    public static class ListmapBean implements Parcelable {
        /**
         * BugID : 14
         * PID : 103
         * PName : 山姆购物中心
         * DID : 451
         * DeviceName : E02电缆地沟02
         * AlarmID :
         * BugLocation : E02电缆地沟02电缆头
         * BugDesc : E02电缆地沟02电缆头
         * BugLevel : 一般
         * ReportWay : 在线监测
         * ReportDate : 2017/1/17 15:43:10
         * UserName :
         * HandeSituation : 未审核
         * HandleDate :
         * EntityState : Unchanged
         * EntityKey : System.Data.EntityKey
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

        public int getBugID() {
            return BugID;
        }

        public void setBugID(int BugID) {
            this.BugID = BugID;
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

        public void setDeviceName(String DeviceName) {
            this.DeviceName = DeviceName;
        }

        public String getAlarmID() {
            return AlarmID;
        }

        public void setAlarmID(String AlarmID) {
            this.AlarmID = AlarmID;
        }

        public String getBugLocation() {
            return BugLocation;
        }

        public void setBugLocation(String BugLocation) {
            this.BugLocation = BugLocation;
        }

        public String getBugDesc() {
            return BugDesc;
        }

        public void setBugDesc(String BugDesc) {
            this.BugDesc = BugDesc;
        }

        public String getBugLevel() {
            return BugLevel;
        }

        public void setBugLevel(String BugLevel) {
            this.BugLevel = BugLevel;
        }

        public String getReportWay() {
            return ReportWay;
        }

        public void setReportWay(String ReportWay) {
            this.ReportWay = ReportWay;
        }

        public String getReportDate() {
            return ReportDate;
        }

        public void setReportDate(String ReportDate) {
            this.ReportDate = ReportDate;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getHandeSituation() {
            return HandeSituation;
        }

        public void setHandeSituation(String HandeSituation) {
            this.HandeSituation = HandeSituation;
        }

        public String getHandleDate() {
            return HandleDate;
        }

        public void setHandleDate(String HandleDate) {
            this.HandleDate = HandleDate;
        }

        public String getEntityState() {
            return EntityState;
        }

        public void setEntityState(String EntityState) {
            this.EntityState = EntityState;
        }

        public String getEntityKey() {
            return EntityKey;
        }

        public void setEntityKey(String EntityKey) {
            this.EntityKey = EntityKey;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.BugID);
            dest.writeString(this.PID);
            dest.writeString(this.PName);
            dest.writeString(this.DID);
            dest.writeString(this.DeviceName);
            dest.writeString(this.AlarmID);
            dest.writeString(this.BugLocation);
            dest.writeString(this.BugDesc);
            dest.writeString(this.BugLevel);
            dest.writeString(this.ReportWay);
            dest.writeString(this.ReportDate);
            dest.writeString(this.UserName);
            dest.writeString(this.HandeSituation);
            dest.writeString(this.HandleDate);
            dest.writeString(this.EntityState);
            dest.writeString(this.EntityKey);
        }

        public ListmapBean() {
        }

        protected ListmapBean(Parcel in) {
            this.BugID = in.readInt();
            this.PID = in.readString();
            this.PName = in.readString();
            this.DID = in.readString();
            this.DeviceName = in.readString();
            this.AlarmID = in.readString();
            this.BugLocation = in.readString();
            this.BugDesc = in.readString();
            this.BugLevel = in.readString();
            this.ReportWay = in.readString();
            this.ReportDate = in.readString();
            this.UserName = in.readString();
            this.HandeSituation = in.readString();
            this.HandleDate = in.readString();
            this.EntityState = in.readString();
            this.EntityKey = in.readString();
        }

        public static final Parcelable.Creator<ListmapBean> CREATOR = new Parcelable.Creator<ListmapBean>() {
            @Override
            public ListmapBean createFromParcel(Parcel source) {
                return new ListmapBean(source);
            }

            @Override
            public ListmapBean[] newArray(int size) {
                return new ListmapBean[size];
            }
        };
    }
}
