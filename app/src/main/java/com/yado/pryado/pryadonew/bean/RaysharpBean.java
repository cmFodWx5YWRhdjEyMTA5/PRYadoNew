package com.yado.pryado.pryadonew.bean;

/**
 * Created by Andrew on 2016/5/9.
 */
public class RaysharpBean {

    private String User;
    private String Password;
    private String Mediaport;
    private int StreamType;
    private String IP;

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getMediaport() {
        return Mediaport;
    }

    public void setMediaport(String mediaport) {
        Mediaport = mediaport;
    }

    public int getStreamType() {
        return StreamType;
    }

    public void setStreamType(int streamType) {
        StreamType = streamType;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}
