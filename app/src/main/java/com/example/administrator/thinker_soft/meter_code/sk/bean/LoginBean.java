package com.example.administrator.thinker_soft.meter_code.sk.bean;

/**
 * Created by Administrator on 2018/3/22.
 */

public class LoginBean {


    /**
     * messg : 1 0账号或密码错误
     * companyid : -2
     * jobState : 1
     * systemuserId : 1
     * check : 0
     * userName : SUPER
     * useState : 1
     */

    private int messg;
    private int companyid;
    private int jobState;
    private int systemuserId;
    private int check;
    private String userName;
    private int useState;

    public int getMessg() {
        return messg;
    }

    public void setMessg(int messg) {
        this.messg = messg;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public int getJobState() {
        return jobState;
    }

    public void setJobState(int jobState) {
        this.jobState = jobState;
    }

    public int getSystemuserId() {
        return systemuserId;
    }

    public void setSystemuserId(int systemuserId) {
        this.systemuserId = systemuserId;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUseState() {
        return useState;
    }

    public void setUseState(int useState) {
        this.useState = useState;
    }
}
