package com.example.administrator.thinker_soft.meter_code.sk.bean;

public class NatureNameBean {


    /**
     * N_PROPERTIES_ID : 101.0
     * C_PROPERTIES_NAME : 原水
     */

    private String N_PROPERTIES_ID;
    private String C_PROPERTIES_NAME;

    public String getN_PROPERTIES_ID() {
        return N_PROPERTIES_ID;
    }

    public void setN_PROPERTIES_ID(String N_PROPERTIES_ID) {
        this.N_PROPERTIES_ID = N_PROPERTIES_ID;
    }

    public String getC_PROPERTIES_NAME() {
        return C_PROPERTIES_NAME;
    }

    public void setC_PROPERTIES_NAME(String C_PROPERTIES_NAME) {
        this.C_PROPERTIES_NAME = C_PROPERTIES_NAME;
    }

    @Override
    public String toString() {
        return "NatureNameBean{" +
                "N_PROPERTIES_ID=" + N_PROPERTIES_ID +
                ", C_PROPERTIES_NAME='" + C_PROPERTIES_NAME + '\'' +
                '}';
    }
}
