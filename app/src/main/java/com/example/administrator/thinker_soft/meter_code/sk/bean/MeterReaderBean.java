package com.example.administrator.thinker_soft.meter_code.sk.bean;

/**
 * 分区
 * Created by Administrator on 2018/4/19.
 */

public class MeterReaderBean {

    /**
     * areaId : 0
     * areaName : 默认分区
     * companyId : 1
     * stationId : 1
     * areaSuperintendent : 1
     * areaRemark : 1
     * cAreaId : 1
     * companyName : null
     * stationName : null
     */

    private int areaId;
    private String areaName;
    private int companyId;
    private int stationId;
    private String areaSuperintendent;
    private String areaRemark;
    private String cAreaId;
    private Object companyName;
    private Object stationName;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getAreaSuperintendent() {
        return areaSuperintendent;
    }

    public void setAreaSuperintendent(String areaSuperintendent) {
        this.areaSuperintendent = areaSuperintendent;
    }

    public String getAreaRemark() {
        return areaRemark;
    }

    public void setAreaRemark(String areaRemark) {
        this.areaRemark = areaRemark;
    }

    public String getCAreaId() {
        return cAreaId;
    }

    public void setCAreaId(String cAreaId) {
        this.cAreaId = cAreaId;
    }

    public Object getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Object companyName) {
        this.companyName = companyName;
    }

    public Object getStationName() {
        return stationName;
    }

    public void setStationName(Object stationName) {
        this.stationName = stationName;
    }
}
