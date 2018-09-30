package com.example.administrator.thinker_soft.meter_code.sk.bean;

public class NewUsersPrame {
    private String c_user_name;
    private String c_user_address;
    private String c_user_id;
    private String c_meter_number;
    private String n_company_id;//公司编号
    private String starttime;//开始时间
    private String endtime;//结束时间


    public String getN_company_id() {
        return n_company_id;
    }

    public void setN_company_id(String n_company_id) {
        this.n_company_id = n_company_id;
    }

    public String getC_user_name() {
        return c_user_name;
    }

    public void setC_user_name(String c_user_name) {
        this.c_user_name = c_user_name;
    }

    public String getC_user_address() {
        return c_user_address;
    }

    public void setC_user_address(String c_user_address) {
        this.c_user_address = c_user_address;
    }

    public String getC_user_id() {
        return c_user_id;
    }

    public void setC_user_id(String c_user_id) {
        this.c_user_id = c_user_id;
    }

    public String getC_meter_number() {
        return c_meter_number;
    }

    public void setC_meter_number(String c_meter_number) {
        this.c_meter_number = c_meter_number;
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
