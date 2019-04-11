package com.yado.pryado.pryadonew.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eado on 2016/4/12.
 */
public class RoomListBean {

    /**
     * rows : [{"Humidity":[{"postion":"室内环境湿度","pv":0},{"postion":"室内环境湿度","pv":0},{"postion":"室内环境湿度","pv":0}],"Name":"沃尔玛","PID":3,"Temperature":[{"postion":"室内环境温度","pv":0},{"postion":"室内环境温度","pv":0},{"postion":"室内环境温度","pv":0}],"latitude":"113.562038","longtitude":"22.397108","status":"0"}]
     * total : 95
     */

    private int total;
    /**
     * Humidity : [{"postion":"室内环境湿度","pv":0},{"postion":"室内环境湿度","pv":0},{"postion":"室内环境湿度","pv":0}]
     * Name : 沃尔玛
     * PID : 3
     * Temperature : [{"postion":"室内环境温度","pv":0},{"postion":"室内环境温度","pv":0},{"postion":"室内环境温度","pv":0}]
     * latitude : 113.562038
     * longtitude : 22.397108
     * status : 0
     */

    private ArrayList<RowsEntity> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<RowsEntity> getRows() {
        return rows;
    }

    public void setRows(ArrayList<RowsEntity> rows) {
        this.rows = rows;
    }

    public static class RowsEntity implements Parcelable {

        private String Name;
        private int PID;
        private String videourl;

        public String getVideourl() {
            return videourl;
        }

        public void setVideourl(String videourl) {
            this.videourl = videourl;
        }

        @Override
        public String toString() {
            return "RowsEntity{" +
                    "Name='" + Name + '\'' +
                    ", PID=" + PID +
                    ", latitude='" + latitude + '\'' +
                    ", longtitude='" + longtitude + '\'' +
                    ", status='" + status + '\'' +
                    ", Humidity=" + Humidity +
                    ", Temperature=" + Temperature +
                    '}';
        }

        private String latitude;
        private String longtitude;
        private String status;
        /**
         * postion : 室内环境湿度
         * pv : 0
         */

        private List<HumidityEntity> Humidity;
        /**
         * postion : 室内环境温度
         * pv : 0
         */

        private List<TemperatureEntity> Temperature;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getPID() {
            return PID;
        }

        public void setPID(int PID) {
            this.PID = PID;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongtitude() {
            return longtitude;
        }

        public void setLongtitude(String longtitude) {
            this.longtitude = longtitude;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<HumidityEntity> getHumidity() {
            return Humidity;
        }

        public void setHumidity(List<HumidityEntity> Humidity) {
            this.Humidity = Humidity;
        }

        public List<TemperatureEntity> getTemperature() {
            return Temperature;
        }

        public void setTemperature(List<TemperatureEntity> Temperature) {
            this.Temperature = Temperature;
        }

        public static class HumidityEntity implements Parcelable {

            private String postion;
            private float pv;

            public String getPostion() {
                return postion;
            }

            public void setPostion(String postion) {
                this.postion = postion;
            }

            public float getPv() {
                return pv;
            }

            public void setPv(float pv) {
                this.pv = pv;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.postion);
                dest.writeFloat(this.pv);
            }

            public HumidityEntity() {
            }

            protected HumidityEntity(Parcel in) {
                this.postion = in.readString();
                this.pv = in.readFloat();
            }

            public static final Creator<HumidityEntity> CREATOR = new Creator<HumidityEntity>() {
                @Override
                public HumidityEntity createFromParcel(Parcel source) {
                    return new HumidityEntity(source);
                }

                @Override
                public HumidityEntity[] newArray(int size) {
                    return new HumidityEntity[size];
                }
            };
        }

        public static class TemperatureEntity implements Parcelable {

            private String postion;
            private float pv;

            public String getPostion() {
                return postion;
            }

            public void setPostion(String postion) {
                this.postion = postion;
            }

            public float getPv() {
                return pv;
            }

            public void setPv(float pv) {
                this.pv = pv;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.postion);
                dest.writeFloat(this.pv);
            }

            public TemperatureEntity() {
            }

            protected TemperatureEntity(Parcel in) {
                this.postion = in.readString();
                this.pv = in.readFloat();
            }

            public static final Creator<TemperatureEntity> CREATOR = new Creator<TemperatureEntity>() {
                @Override
                public TemperatureEntity createFromParcel(Parcel source) {
                    return new TemperatureEntity(source);
                }

                @Override
                public TemperatureEntity[] newArray(int size) {
                    return new TemperatureEntity[size];
                }
            };
        }


        public RowsEntity() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.Name);
            dest.writeInt(this.PID);
            dest.writeString(this.videourl);
            dest.writeString(this.latitude);
            dest.writeString(this.longtitude);
            dest.writeString(this.status);
            dest.writeTypedList(Humidity);
            dest.writeTypedList(Temperature);
        }

        protected RowsEntity(Parcel in) {
            this.Name = in.readString();
            this.PID = in.readInt();
            this.videourl = in.readString();
            this.latitude = in.readString();
            this.longtitude = in.readString();
            this.status = in.readString();
            this.Humidity = in.createTypedArrayList(HumidityEntity.CREATOR);
            this.Temperature = in.createTypedArrayList(TemperatureEntity.CREATOR);
        }

        public static final Parcelable.Creator<RowsEntity> CREATOR = new Parcelable.Creator<RowsEntity>() {
            @Override
            public RowsEntity createFromParcel(Parcel source) {
                return new RowsEntity(source);
            }

            @Override
            public RowsEntity[] newArray(int size) {
                return new RowsEntity[size];
            }
        };
    }
}
