package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.io.Serializable;

/**
 * @author g
 * @FileName ReportTsParame
 * @date 2018/9/3 10:12
 */
public class ReportTsParame  implements Serializable {

    /**
     * 当前队列id
     */
    private String queId;
    /**
     * 当前流程id
     */
    private String proId;
    /**
     * 当前业务id
     */
    private String busId;

    /**
     * 办理人
     */
    private String userName;
    /**
     * 节点名称
     */
    private String process;


    private String tranId;

    public String getQueId() {
        return queId;
    }

    public void setQueId(String queId) {
        this.queId = queId;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
