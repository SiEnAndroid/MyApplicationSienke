package com.example.administrator.thinker_soft.meter_code.sk.bean;

public class ProjectTypeBean {

    /**
     * N_PROJECT_TYPE_ID : 1.0
     * C_PROJECT_TYPE_NAME : 零星定额
     */

    private String N_PROJECT_TYPE_ID;
    private String C_PROJECT_TYPE_NAME;

    public String getN_PROJECT_TYPE_ID() {
        return N_PROJECT_TYPE_ID;
    }

    public void setN_PROJECT_TYPE_ID(String N_PROJECT_TYPE_ID) {
        this.N_PROJECT_TYPE_ID = N_PROJECT_TYPE_ID;
    }

    public String getC_PROJECT_TYPE_NAME() {
        return C_PROJECT_TYPE_NAME;
    }

    public void setC_PROJECT_TYPE_NAME(String C_PROJECT_TYPE_NAME) {
        this.C_PROJECT_TYPE_NAME = C_PROJECT_TYPE_NAME;
    }

    @Override
    public String toString() {
        return "ProjectTypeBean{" +
                "N_PROJECT_TYPE_ID='" + N_PROJECT_TYPE_ID + '\'' +
                ", C_PROJECT_TYPE_NAME='" + C_PROJECT_TYPE_NAME + '\'' +
                '}';
    }
}
