package com.yado.pryado.pryadonew.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bob on 2016/4/18.
 */
public class CatchInitBean {

    @JSONField(name = "PID")
    private int PID;
    @JSONField(name = "PDRName")
    private String PDRName;

    @JSONField(name = "devices")
    private List<DeviceEntity> devices;

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public String getPDRName() {
        return PDRName;
    }

    public void setPDRName(String PDRName) {
        this.PDRName = PDRName;
    }

    public List<DeviceEntity> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceEntity> devices) {
        this.devices = devices;
    }

    public static class DeviceEntity implements Serializable {

        @JSONField(name = "DID")
        private int DID;
        @JSONField(name = "DeviceName")
        private String DeviceName;
        @JSONField(name = "positions")
        private List<PositionEntity> positions;

        public int getDID() {
            return DID;
        }

        public void setDID(int DID) {
            this.DID = DID;
        }

        public String getDeviceName() {
            return DeviceName;
        }

        public void setDeviceName(String deviceName) {
            DeviceName = deviceName;
        }

        public List<PositionEntity> getPositions() {
            return positions;
        }

        public void setPositions(List<PositionEntity> positions) {
            this.positions = positions;
        }

        public static class PositionEntity implements Serializable {

            @JSONField(name = "TagID")
            private int TagID;
            @JSONField(name = "TagName")
            private String TagName;

            public int getTagID() {
                return TagID;
            }

            public void setTagID(int tagID) {
                TagID = tagID;
            }

            public String getTagName() {
                return TagName;
            }

            public void setTagName(String tagName) {
                TagName = tagName;
            }
        }

    }


}
