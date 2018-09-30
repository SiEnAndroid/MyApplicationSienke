package com.example.administrator.thinker_soft.meter_code.sk.bean;

/**
 * Created by Administrator on 2018/8/3.
 */

public class AetatePrame {
    private String c_user_id;//用户编号
    private String n_company_id;//公司编号
    private String c_old_user_id;//旧编号
    private String c_user_name;//名称
    private String c_user_address;//地址
    private String starttime;//开始时间
    private String endtime;//结束时间
    private int pageIndex;//当前页
    private int perPageCount;//当前页的数量
    private String n_check_statu;//不合格
    private String userName;//登录名



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getC_user_id() {
        return c_user_id;
    }

    public void setC_user_id(String c_user_id) {
        this.c_user_id = c_user_id;
    }

    public String getN_company_id() {
        return n_company_id;
    }

    public void setN_company_id(String n_company_id) {
        this.n_company_id = n_company_id;
    }

    public String getC_old_user_id() {
        return c_old_user_id;
    }

    public void setC_old_user_id(String c_old_user_id) {
        this.c_old_user_id = c_old_user_id;
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

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPerPageCount() {
        return perPageCount;
    }

    public void setPerPageCount(int perPageCount) {
        this.perPageCount = perPageCount;
    }

    public String getN_check_statu() {
        return n_check_statu;
    }

    public void setN_check_statu(String n_check_statu) {
        this.n_check_statu = n_check_statu;
    }


}
