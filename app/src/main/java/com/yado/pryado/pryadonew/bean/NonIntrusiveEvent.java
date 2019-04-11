package com.yado.pryado.pryadonew.bean;

public class NonIntrusiveEvent {
    private String did;

    private int isClick;

    private String station;

    public void setDid(String did) {
        this.did = did;
    }

    public void setIsClick(int isClick) {
        this.isClick = isClick;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDid() {
        return did;
    }

    public int getIsClick() {
        return isClick;
    }

    public String getStation() {
        return station;
    }
}
