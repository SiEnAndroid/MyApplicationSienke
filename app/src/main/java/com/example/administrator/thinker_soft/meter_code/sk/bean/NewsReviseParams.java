package com.example.administrator.thinker_soft.meter_code.sk.bean;

/**
 * Created by Administrator on 2018/8/17.
 */

public class NewsReviseParams {
    //用户id
    private String c_user_id;
    //气表编号
    private String c_meter_number;
    //用户名称
    private String c_user_name;
    //用户电话
    private String c_user_phone;
    //进气方向
    private String n_meter_direction;
    //操作员id
    private String n_log_operator_id;
    //日志内容
    private String c_log;


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

    public String getC_user_name() {
        return c_user_name;
    }

    public void setC_user_name(String c_user_name) {
        this.c_user_name = c_user_name;
    }

    public String getC_user_phone() {
        return c_user_phone;
    }

    public void setC_user_phone(String c_user_phone) {
        this.c_user_phone = c_user_phone;
    }

    public String getN_meter_direction() {
        return n_meter_direction;
    }

    public void setN_meter_direction(String n_meter_direction) {
        this.n_meter_direction = n_meter_direction;
    }

    public String getN_log_operator_id() {
        return n_log_operator_id;
    }

    public void setN_log_operator_id(String n_log_operator_id) {
        this.n_log_operator_id = n_log_operator_id;
    }

    public String getC_log() {
        return c_log;
    }

    public void setC_log(String c_log) {
        this.c_log = c_log;
    }
}
