package com.yado.pryado.pryadonew.bean;

import java.util.List;

/**
 * Created by eado on 2017/2/10.
 */

public class BugInfo {

    /**
     * AlarmID :
     * BugDesc : 描述。。。。。fragment
     * BugID : 197
     * BugLevel :
     * BugLocation : 缺陷部位。。。。。
     * DID : 471
     * DeviceModel : GCK
     * DeviceName : 1BB4低压馈线柜
     * EntityKey : System.Data.EntityKey
     * EntityState : Unchanged
     * HandeSituation : 未审核
     * HandleDate :
     * InfUrl : []
     * InstallAddr : 天保生活区B08 10KV变电站
     * PID : 105
     * PName : 天保生活区B08站
     * PhotoUrl : [{"flieName":"magazine-unlock-01-2.3.508-bigpicture_01_32.jpg","fliePath":"~/UploadFiles/bug/636222541167280015.jpg"}]
     * ReportDate : 2017/2/9 16:21:50
     * ReportWay : app
     * UserName :
     * VoiceUrl : [{"flieName":"Record_20170209_162118.3gp","fliePath":"~/UploadFiles/bug/636222541167180009.3gp"}]
     * VoltageLevel : 380V
     */

    private String AlarmID;
    private String BugDesc;
    private int BugID;
    private String BugLevel;
    private String BugLocation;
    private String DID;
    private String DeviceModel;
    private String DeviceName;
    private String EntityKey;
    private String EntityState;
    private String HandeSituation;
    private String CheckDate;
    private String HandleDate;
    private String InstallAddr;
    private String PID;
    private String PName;
    private String ReportDate;
    private String ReportWay;
    private String UserName;
    private String VoltageLevel;
    private List<InfUrlBean> InfUrl;
    private List<PhotoUrlBean> PhotoUrl;
    private List<VoiceUrlBean> VoiceUrl;

    public String getCheckDate() {
        return CheckDate;
    }

    public void setCheckDate(String checkDate) {
        CheckDate = checkDate;
    }

    public String getAlarmID() {
        return AlarmID;
    }

    public void setAlarmID(String AlarmID) {
        this.AlarmID = AlarmID;
    }

    public String getBugDesc() {
        return BugDesc;
    }

    public void setBugDesc(String BugDesc) {
        this.BugDesc = BugDesc;
    }

    public int getBugID() {
        return BugID;
    }

    public void setBugID(int BugID) {
        this.BugID = BugID;
    }

    public String getBugLevel() {
        return BugLevel;
    }

    public void setBugLevel(String BugLevel) {
        this.BugLevel = BugLevel;
    }

    public String getBugLocation() {
        return BugLocation;
    }

    public void setBugLocation(String BugLocation) {
        this.BugLocation = BugLocation;
    }

    public String getDID() {
        return DID;
    }

    public void setDID(String DID) {
        this.DID = DID;
    }

    public String getDeviceModel() {
        return DeviceModel;
    }

    public void setDeviceModel(String DeviceModel) {
        this.DeviceModel = DeviceModel;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String DeviceName) {
        this.DeviceName = DeviceName;
    }

    public String getEntityKey() {
        return EntityKey;
    }

    public void setEntityKey(String EntityKey) {
        this.EntityKey = EntityKey;
    }

    public String getEntityState() {
        return EntityState;
    }

    public void setEntityState(String EntityState) {
        this.EntityState = EntityState;
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

    public String getInstallAddr() {
        return InstallAddr;
    }

    public void setInstallAddr(String InstallAddr) {
        this.InstallAddr = InstallAddr;
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

    public String getReportDate() {
        return ReportDate;
    }

    public void setReportDate(String ReportDate) {
        this.ReportDate = ReportDate;
    }

    public String getReportWay() {
        return ReportWay;
    }

    public void setReportWay(String ReportWay) {
        this.ReportWay = ReportWay;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getVoltageLevel() {
        return VoltageLevel;
    }

    public void setVoltageLevel(String VoltageLevel) {
        this.VoltageLevel = VoltageLevel;
    }

    public List<InfUrlBean> getInfUrl() {
        return InfUrl;
    }

    public void setInfUrl(List<InfUrlBean> InfUrl) {
        this.InfUrl = InfUrl;
    }

    public List<PhotoUrlBean> getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(List<PhotoUrlBean> PhotoUrl) {
        this.PhotoUrl = PhotoUrl;
    }

    public List<VoiceUrlBean> getVoiceUrl() {
        return VoiceUrl;
    }

    public void setVoiceUrl(List<VoiceUrlBean> VoiceUrl) {
        this.VoiceUrl = VoiceUrl;
    }

    public static class InfUrlBean {
        /**
         * flieName : Record_20170209_163435.3gp
         * fliePath : ~/UploadFiles/bug/636222549127555317.3gp
         */

        private String flieName;
        private String fliePath;

        public String getFlieName() {
            return flieName;
        }

        public void setFlieName(String flieName) {
            this.flieName = flieName;
        }

        public String getFliePath() {
            return fliePath;
        }

        public void setFliePath(String fliePath) {
            this.fliePath = fliePath;
        }
    }

    public static class PhotoUrlBean {
        /**
         * flieName : magazine-unlock-01-2.3.508-bigpicture_01_32.jpg
         * fliePath : ~/UploadFiles/bug/636222541167280015.jpg
         */

        private String flieName;
        private String fliePath;

        public String getFlieName() {
            return flieName;
        }

        public void setFlieName(String flieName) {
            this.flieName = flieName;
        }

        public String getFliePath() {
            return fliePath;
        }

        public void setFliePath(String fliePath) {
            this.fliePath = fliePath;
        }
    }

    public static class VoiceUrlBean {
        /**
         * flieName : Record_20170209_162118.3gp
         * fliePath : ~/UploadFiles/bug/636222541167180009.3gp
         */

        private String flieName;
        private String fliePath;

        public String getFlieName() {
            return flieName;
        }

        public void setFlieName(String flieName) {
            this.flieName = flieName;
        }

        public String getFliePath() {
            return fliePath;
        }

        public void setFliePath(String fliePath) {
            this.fliePath = fliePath;
        }
    }
}
