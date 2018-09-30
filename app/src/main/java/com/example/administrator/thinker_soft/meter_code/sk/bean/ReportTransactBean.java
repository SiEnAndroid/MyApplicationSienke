package com.example.administrator.thinker_soft.meter_code.sk.bean;

/**
 * @author g
 * @FileName ReportTransactBean
 * @date 2018/9/3 16:18
 */
public class ReportTransactBean {

    /**
     * transnode : 领料计划复核（政府）
     * transactor : SUPER,李军
     * transatp : 1
     */

    private String transnode;
    private String transactor;
    private String transatp;

    public String getTransnode() {
        return transnode;
    }

    public void setTransnode(String transnode) {
        this.transnode = transnode;
    }

    public String getTransactor() {
        return transactor;
    }

    public void setTransactor(String transactor) {
        this.transactor = transactor;
    }

    public String getTransatp() {
        return transatp;
    }

    public void setTransatp(String transatp) {
        this.transatp = transatp;
    }
}
