package com.yado.pryado.pryadonew.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Ulez on 2017/6/2.
 * Email：lcy1532110757@gmail.com
 */


public class DeviceDetailBean2 implements Parcelable {

    /**
     * BuyTime : 2016/6/20 0:00:00
     * Company : 天津泰达
     * DID : 469
     * DeviceCode : H7
     * DeviceModel : TDDH-10
     * DeviceName : H7高压柜
     * DeviceState : 使用中
     * InstallAddr : 天保生活区B08 10KV变电站
     * LastMtcDate : 2016/6/20 0:00:00
     * LastMtcPerson : 1
     * MFactory : 天津泰达
     * UseDate : 2016/6/21 0:00:00
     * devicePhoto :
     * elec : 233
     * instruction :
     * realTimeHumi : 61
     * realTimeParams : [{"PName":"上触头(℃)","StatusA":"正常 正常","StatusB":"正常 正常","StatusC":"正常 正常","TagIDA":"12261","TagIDB":"12262","TagIDC":"12263","ValueA":"29","ValueB":"29","ValueC":"29"},{"PName":"下触头(℃)","StatusA":"正常 正常","StatusB":"正常 正常","StatusC":"正常 正常","TagIDA":"12264","TagIDB":"12265","TagIDC":"12266","ValueA":"29","ValueB":"28","ValueC":"29"},{"PName":"电缆头(℃)","StatusA":"正常 正常","StatusB":"正常 正常","StatusC":"正常 正常","TagIDA":"12267","TagIDB":"12268","TagIDC":"12269","ValueA":"28","ValueB":"28","ValueC":"28"},{"PName":"电缆室环境(℃)","Status":"正常 正常","TagID":"12284","Value":"27"},{"PName":"电缆室环境(%rh)","Status":"正常 正常","TagID":"12285","Value":"61"}]
     * realTimeTemp : 27
     */

    private String BuyTime;
    private String Company;
    private int DID;
    private String DeviceCode;
    private String DeviceModel;
    private String DeviceName;
    private String DeviceState;
    private String InstallAddr;
    private String LastMtcDate;
    private String LastMtcPerson;
    private String MFactory;
    private String UseDate;
    private String devicePhoto;
    private String elec;
    private String instruction;
    private String realTimeHumi;
    private String realTimeTemp;
    private List<RealTimeParamsBean> realTimeParams;

    public String getBuyTime() {
        return BuyTime;
    }

    public void setBuyTime(String BuyTime) {
        this.BuyTime = BuyTime;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public int getDID() {
        return DID;
    }

    public void setDID(int DID) {
        this.DID = DID;
    }

    public String getDeviceCode() {
        return DeviceCode;
    }

    public void setDeviceCode(String DeviceCode) {
        this.DeviceCode = DeviceCode;
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

    public String getDeviceState() {
        return DeviceState;
    }

    public void setDeviceState(String DeviceState) {
        this.DeviceState = DeviceState;
    }

    public String getInstallAddr() {
        return InstallAddr;
    }

    public void setInstallAddr(String InstallAddr) {
        this.InstallAddr = InstallAddr;
    }

    public String getLastMtcDate() {
        return LastMtcDate;
    }

    public void setLastMtcDate(String LastMtcDate) {
        this.LastMtcDate = LastMtcDate;
    }

    public String getLastMtcPerson() {
        return LastMtcPerson;
    }

    public void setLastMtcPerson(String LastMtcPerson) {
        this.LastMtcPerson = LastMtcPerson;
    }

    public String getMFactory() {
        return MFactory;
    }

    public void setMFactory(String MFactory) {
        this.MFactory = MFactory;
    }

    public String getUseDate() {
        return UseDate;
    }

    public void setUseDate(String UseDate) {
        this.UseDate = UseDate;
    }

    public String getDevicePhoto() {
        return devicePhoto;
    }

    public void setDevicePhoto(String devicePhoto) {
        this.devicePhoto = devicePhoto;
    }

    public String getElec() {
        return elec;
    }

    public void setElec(String elec) {
        this.elec = elec;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getRealTimeHumi() {
        return realTimeHumi;
    }

    public void setRealTimeHumi(String realTimeHumi) {
        this.realTimeHumi = realTimeHumi;
    }

    public String getRealTimeTemp() {
        return realTimeTemp;
    }

    public void setRealTimeTemp(String realTimeTemp) {
        this.realTimeTemp = realTimeTemp;
    }

    public List<RealTimeParamsBean> getRealTimeParams() {
        return realTimeParams;
    }

    public void setRealTimeParams(List<RealTimeParamsBean> realTimeParams) {
        this.realTimeParams = realTimeParams;
    }

    public static class RealTimeParamsBean implements Parcelable {
        /**
         * DataTypeID   :   1
         * PName : 上触头(℃)
         * StatusA : 正常 正常
         * StatusB : 正常 正常
         * StatusC : 正常 正常
         * TagIDA : 12261
         * TagIDB : 12262
         * TagIDC : 12263
         * ValueA : 29
         * ValueB : 29
         * ValueC : 29
         * Status : 正常 正常
         * TagID : 12284
         * Value : 27
         */
        private String DataTypeID;
        private String PName;
        private String StatusA;
        private String StatusB;
        private String StatusC;
        private String TagIDA;
        private String TagIDB;
        private String TagIDC;
        private String ValueA;
        private String ValueB;
        private String ValueC;
        private String Status;
        private String TagID;
        private String Value;

        public String getDataTypeID() {
            return DataTypeID;
        }

        public void setDataTypeID(String dataTypeID) {
            DataTypeID = dataTypeID;
        }

        public String getPName() {
            return PName;
        }

        public void setPName(String PName) {
            this.PName = PName;
        }

        public String getStatusA() {
            return StatusA;
        }

        public void setStatusA(String StatusA) {
            this.StatusA = StatusA;
        }

        public String getStatusB() {
            return StatusB;
        }

        public void setStatusB(String StatusB) {
            this.StatusB = StatusB;
        }

        public String getStatusC() {
            return StatusC;
        }

        public void setStatusC(String StatusC) {
            this.StatusC = StatusC;
        }

        public String getTagIDA() {
            return TagIDA;
        }

        public void setTagIDA(String TagIDA) {
            this.TagIDA = TagIDA;
        }

        public String getTagIDB() {
            return TagIDB;
        }

        public void setTagIDB(String TagIDB) {
            this.TagIDB = TagIDB;
        }

        public String getTagIDC() {
            return TagIDC;
        }

        public void setTagIDC(String TagIDC) {
            this.TagIDC = TagIDC;
        }

        public String getValueA() {
            return ValueA;
        }

        public void setValueA(String ValueA) {
            this.ValueA = ValueA;
        }

        public String getValueB() {
            return ValueB;
        }

        public void setValueB(String ValueB) {
            this.ValueB = ValueB;
        }

        public String getValueC() {
            return ValueC;
        }

        public void setValueC(String ValueC) {
            this.ValueC = ValueC;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getTagID() {
            return TagID;
        }

        public void setTagID(String TagID) {
            this.TagID = TagID;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String Value) {
            this.Value = Value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.DataTypeID);
            dest.writeString(this.PName);
            dest.writeString(this.StatusA);
            dest.writeString(this.StatusB);
            dest.writeString(this.StatusC);
            dest.writeString(this.TagIDA);
            dest.writeString(this.TagIDB);
            dest.writeString(this.TagIDC);
            dest.writeString(this.ValueA);
            dest.writeString(this.ValueB);
            dest.writeString(this.ValueC);
            dest.writeString(this.Status);
            dest.writeString(this.TagID);
            dest.writeString(this.Value);
        }

        public RealTimeParamsBean() {
        }

        protected RealTimeParamsBean(Parcel in) {
            this.DataTypeID = in.readString();
            this.PName = in.readString();
            this.StatusA = in.readString();
            this.StatusB = in.readString();
            this.StatusC = in.readString();
            this.TagIDA = in.readString();
            this.TagIDB = in.readString();
            this.TagIDC = in.readString();
            this.ValueA = in.readString();
            this.ValueB = in.readString();
            this.ValueC = in.readString();
            this.Status = in.readString();
            this.TagID = in.readString();
            this.Value = in.readString();
        }

        public static final Parcelable.Creator<RealTimeParamsBean> CREATOR = new Parcelable.Creator<RealTimeParamsBean>() {
            @Override
            public RealTimeParamsBean createFromParcel(Parcel source) {
                return new RealTimeParamsBean(source);
            }

            @Override
            public RealTimeParamsBean[] newArray(int size) {
                return new RealTimeParamsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.BuyTime);
        dest.writeString(this.Company);
        dest.writeInt(this.DID);
        dest.writeString(this.DeviceCode);
        dest.writeString(this.DeviceModel);
        dest.writeString(this.DeviceName);
        dest.writeString(this.DeviceState);
        dest.writeString(this.InstallAddr);
        dest.writeString(this.LastMtcDate);
        dest.writeString(this.LastMtcPerson);
        dest.writeString(this.MFactory);
        dest.writeString(this.UseDate);
        dest.writeString(this.devicePhoto);
        dest.writeString(this.elec);
        dest.writeString(this.instruction);
        dest.writeString(this.realTimeHumi);
        dest.writeString(this.realTimeTemp);
        dest.writeTypedList(this.realTimeParams);
    }

    public DeviceDetailBean2() {
    }

    protected DeviceDetailBean2(Parcel in) {
        this.BuyTime = in.readString();
        this.Company = in.readString();
        this.DID = in.readInt();
        this.DeviceCode = in.readString();
        this.DeviceModel = in.readString();
        this.DeviceName = in.readString();
        this.DeviceState = in.readString();
        this.InstallAddr = in.readString();
        this.LastMtcDate = in.readString();
        this.LastMtcPerson = in.readString();
        this.MFactory = in.readString();
        this.UseDate = in.readString();
        this.devicePhoto = in.readString();
        this.elec = in.readString();
        this.instruction = in.readString();
        this.realTimeHumi = in.readString();
        this.realTimeTemp = in.readString();
        this.realTimeParams = in.createTypedArrayList(RealTimeParamsBean.CREATOR);
    }

    public static final Parcelable.Creator<DeviceDetailBean2> CREATOR = new Parcelable.Creator<DeviceDetailBean2>() {
        @Override
        public DeviceDetailBean2 createFromParcel(Parcel source) {
            return new DeviceDetailBean2(source);
        }

        @Override
        public DeviceDetailBean2[] newArray(int size) {
            return new DeviceDetailBean2[size];
        }
    };
}
