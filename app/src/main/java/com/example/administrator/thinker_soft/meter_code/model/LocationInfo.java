package com.example.administrator.thinker_soft.meter_code.model;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/28 0028.
 */
public class LocationInfo implements Serializable {
    private LatLng latLng;
    private String time;
    private String name;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
