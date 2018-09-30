package com.example.administrator.thinker_soft.Security_check.model;

/**
 * Created by Administrator on 2017/3/16.
 */
public class NoCheckUserItem {
    private String user_name;             //姓名
    private int number;                   //表编号
    private int phone_number;             //电话号码
    private String security_type;         //安检类型
    private int time;                     //时间
    private String adress;                //地址

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public String getSecurity_type() {
        return security_type;
    }

    public void setSecurity_type(String security_type) {
        this.security_type = security_type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
