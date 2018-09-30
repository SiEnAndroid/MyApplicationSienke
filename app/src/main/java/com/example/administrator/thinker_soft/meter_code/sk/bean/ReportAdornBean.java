package com.example.administrator.thinker_soft.meter_code.sk.bean;

/**
 * File:ReportAdornBean.class
 *
 * @author Administrator
 * @date 2018/8/29 16:23
 */

public class ReportAdornBean {
    /**
     * TRANID : 26723.0
     * OPID : 1.0
     * BID : 1115.0
     * N_AUDIT_TYPE : 1.0
     * NODENAME : 库房出料（定额）
     * C_COMPANY_REMARK : 重庆渝山
     * N_COMPANY_ID : 1.0
     * BUSINESS : 思恩科夏登栖测试
     * C_USER_NAME : SUPER
     * PROCESS : 报装流程
     * TRAN_STATE : 未办理
     * DUE_STATE : 未超期
     * D_TRANSACTION_TIME_BEGIN : 2018-08-29T08:54:29
     * N_QUEUE_ID : 354.0
     * N_PROCESS_ID : 4.0
     */

    private double TRANID;
    private double OPID;
    private double BID;
    private double N_AUDIT_TYPE;
    private String NODENAME;
    private String C_COMPANY_REMARK;
    private double N_COMPANY_ID;
    private String BUSINESS;
    private String C_USER_NAME;
    private String PROCESS;
    private String TRAN_STATE;
    private String DUE_STATE;
    private String D_TRANSACTION_TIME_BEGIN;
    private String D_TRANSACTION_TIME_DUE;
    private double N_QUEUE_ID;
    private double N_PROCESS_ID;

    public String getD_TRANSACTION_TIME_DUE() {
        return D_TRANSACTION_TIME_DUE;
    }

    public void setD_TRANSACTION_TIME_DUE(String d_TRANSACTION_TIME_DUE) {
        D_TRANSACTION_TIME_DUE = d_TRANSACTION_TIME_DUE;
    }

    private String state_text;
    public double getTRANID() {
        return TRANID;
    }

    public void setTRANID(double TRANID) {
        this.TRANID = TRANID;
    }

    public double getOPID() {
        return OPID;
    }

    public void setOPID(double OPID) {
        this.OPID = OPID;
    }

    public double getBID() {
        return BID;
    }

    public void setBID(double BID) {
        this.BID = BID;
    }

    public double getN_AUDIT_TYPE() {
        return N_AUDIT_TYPE;
    }

    public void setN_AUDIT_TYPE(double N_AUDIT_TYPE) {
        this.N_AUDIT_TYPE = N_AUDIT_TYPE;
    }

    public String getNODENAME() {
        return NODENAME;
    }

    public void setNODENAME(String NODENAME) {
        this.NODENAME = NODENAME;
    }

    public String getC_COMPANY_REMARK() {
        return C_COMPANY_REMARK;
    }

    public void setC_COMPANY_REMARK(String C_COMPANY_REMARK) {
        this.C_COMPANY_REMARK = C_COMPANY_REMARK;
    }

    public double getN_COMPANY_ID() {
        return N_COMPANY_ID;
    }

    public void setN_COMPANY_ID(double N_COMPANY_ID) {
        this.N_COMPANY_ID = N_COMPANY_ID;
    }

    public String getBUSINESS() {
        return BUSINESS;
    }

    public void setBUSINESS(String BUSINESS) {
        this.BUSINESS = BUSINESS;
    }

    public String getC_USER_NAME() {
        return C_USER_NAME;
    }

    public void setC_USER_NAME(String C_USER_NAME) {
        this.C_USER_NAME = C_USER_NAME;
    }

    public String getPROCESS() {
        return PROCESS;
    }

    public void setPROCESS(String PROCESS) {
        this.PROCESS = PROCESS;
    }

    public String getTRAN_STATE() {
        return TRAN_STATE;
    }

    public void setTRAN_STATE(String TRAN_STATE) {
        this.TRAN_STATE = TRAN_STATE;
    }

    public String getDUE_STATE() {
        return DUE_STATE;
    }

    public void setDUE_STATE(String DUE_STATE) {
        this.DUE_STATE = DUE_STATE;
    }

    public String getD_TRANSACTION_TIME_BEGIN() {
        return D_TRANSACTION_TIME_BEGIN;
    }

    public void setD_TRANSACTION_TIME_BEGIN(String D_TRANSACTION_TIME_BEGIN) {
        this.D_TRANSACTION_TIME_BEGIN = D_TRANSACTION_TIME_BEGIN;
    }

    public double getN_QUEUE_ID() {
        return N_QUEUE_ID;
    }

    public void setN_QUEUE_ID(double N_QUEUE_ID) {
        this.N_QUEUE_ID = N_QUEUE_ID;
    }

    public double getN_PROCESS_ID() {
        return N_PROCESS_ID;
    }

    public void setN_PROCESS_ID(double N_PROCESS_ID) {
        this.N_PROCESS_ID = N_PROCESS_ID;
    }

    public String getState_text() {
        return state_text;
    }

    public void setState_text(String state_text) {
        this.state_text = state_text;
    }
}
