package com.example.administrator.thinker_soft.meter_code.sk.bean;

/**
 * Created by Administrator on 2018/8/6.
 */

public class AuditPrame {
    private  String c_safety_inspection_member;//安检员
    private String starttime;//开始时间
    private String  endtime;//结束时间


    public String getC_safety_inspection_member() {
        return c_safety_inspection_member;
    }

    public void setC_safety_inspection_member(String c_safety_inspection_member) {
        this.c_safety_inspection_member = c_safety_inspection_member;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}
