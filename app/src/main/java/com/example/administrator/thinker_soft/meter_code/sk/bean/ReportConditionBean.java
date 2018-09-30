package com.example.administrator.thinker_soft.meter_code.sk.bean;

/**
 * @author g
 * @FileName ReportConditionBean
 * @date 2018/8/31 12:43
 */
public class ReportConditionBean {


    /**
     * N_TRANSACTION_ID : 15336.0
     * N_AUDIT_TYPE : 1.0
     * N_QUEUE_ID : 1.0
     * C_NODE_NAME : 前台申请    节点
     * TRANSACTION_STATE : 流转
     * N_TRANSACTION_COMMENT : 报装申请 办理批示
     * C_USER_NAME : SUPER
     * TRANSACTION_TIME_END : 2018-03-02 14:35:45
     * C_TRANSACTOR_SIGNATURE : null
     *
     * state_id : 0
     * state_text : 未找数据
     */

     private double N_TRANSACTION_ID;
    private double N_AUDIT_TYPE;
    private double N_QUEUE_ID;
    private String C_NODE_NAME;
    private String TRANSACTION_STATE;
    private String N_TRANSACTION_COMMENT;
    private String C_USER_NAME;
    private String TRANSACTION_TIME_END;
    private String C_TRANSACTOR_SIGNATURE;
    private int state_id;
    private String state_text;


    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public String getState_text() {
        return state_text;
    }

    public void setState_text(String state_text) {
        this.state_text = state_text;
    }
    public double getN_TRANSACTION_ID() {
        return N_TRANSACTION_ID;
    }

    public void setN_TRANSACTION_ID(double N_TRANSACTION_ID) {
        this.N_TRANSACTION_ID = N_TRANSACTION_ID;
    }

    public double getN_AUDIT_TYPE() {
        return N_AUDIT_TYPE;
    }

    public void setN_AUDIT_TYPE(double N_AUDIT_TYPE) {
        this.N_AUDIT_TYPE = N_AUDIT_TYPE;
    }

    public double getN_QUEUE_ID() {
        return N_QUEUE_ID;
    }

    public void setN_QUEUE_ID(double N_QUEUE_ID) {
        this.N_QUEUE_ID = N_QUEUE_ID;
    }

    public String getC_NODE_NAME() {
        return C_NODE_NAME;
    }

    public void setC_NODE_NAME(String C_NODE_NAME) {
        this.C_NODE_NAME = C_NODE_NAME;
    }

    public String getTRANSACTION_STATE() {
        return TRANSACTION_STATE;
    }

    public void setTRANSACTION_STATE(String TRANSACTION_STATE) {
        this.TRANSACTION_STATE = TRANSACTION_STATE;
    }

    public String getN_TRANSACTION_COMMENT() {
        return N_TRANSACTION_COMMENT;
    }

    public void setN_TRANSACTION_COMMENT(String N_TRANSACTION_COMMENT) {
        this.N_TRANSACTION_COMMENT = N_TRANSACTION_COMMENT;
    }

    public String getC_USER_NAME() {
        return C_USER_NAME;
    }

    public void setC_USER_NAME(String C_USER_NAME) {
        this.C_USER_NAME = C_USER_NAME;
    }

    public String getTRANSACTION_TIME_END() {
        return TRANSACTION_TIME_END;
    }

    public void setTRANSACTION_TIME_END(String TRANSACTION_TIME_END) {
        this.TRANSACTION_TIME_END = TRANSACTION_TIME_END;
    }

    public String getC_TRANSACTOR_SIGNATURE() {
        return C_TRANSACTOR_SIGNATURE;
    }

    public void setC_TRANSACTOR_SIGNATURE(String C_TRANSACTOR_SIGNATURE) {
        this.C_TRANSACTOR_SIGNATURE = C_TRANSACTOR_SIGNATURE;
    }
}
