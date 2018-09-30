package com.example.administrator.thinker_soft.meter_code.sk.bean;

/**
 * @author g
 * @FileName ReportFlowBean
 * @date 2018/8/31 12:12
 */
public class ReportFlowBean {

    /**
     * N_PROCESS_ID : 35.0
     * C_PROCESS_NAME : 维修流程
     * C_PROCESS_REQUESTION_COM : WX_APPLICATION
     *
     */

    private double N_PROCESS_ID;
    private String C_PROCESS_NAME;
    private String C_PROCESS_REQUESTION_COM;

    private String state_text;

    public String getState_text() {
        return state_text;
    }

    public void setState_text(String state_text) {
        this.state_text = state_text;
    }

    public double getN_PROCESS_ID() {
        return N_PROCESS_ID;
    }

    public void setN_PROCESS_ID(double N_PROCESS_ID) {
        this.N_PROCESS_ID = N_PROCESS_ID;
    }

    public String getC_PROCESS_NAME() {
        return C_PROCESS_NAME;
    }

    public void setC_PROCESS_NAME(String C_PROCESS_NAME) {
        this.C_PROCESS_NAME = C_PROCESS_NAME;
    }

    public String getC_PROCESS_REQUESTION_COM() {
        return C_PROCESS_REQUESTION_COM;
    }

    public void setC_PROCESS_REQUESTION_COM(String C_PROCESS_REQUESTION_COM) {
        this.C_PROCESS_REQUESTION_COM = C_PROCESS_REQUESTION_COM;
    }
}
