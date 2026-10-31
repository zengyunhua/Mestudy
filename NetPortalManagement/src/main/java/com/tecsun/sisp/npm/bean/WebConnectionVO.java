package com.tecsun.sisp.npm.bean;


import java.sql.Timestamp;


/**
 * created by fuweifeng on 2015-6-1.
 */
public class WebConnectionVO {

    public static final String ID = "id";

    private long id;


    public static final String IP = "ip";
    private String ip;


    public static final String MAC = "mac";
    private String mac;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public static final String CREATE_TIME = "create_time";
    private String create_time;


}
