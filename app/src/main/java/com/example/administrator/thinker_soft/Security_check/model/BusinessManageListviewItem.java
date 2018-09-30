package com.example.administrator.thinker_soft.Security_check.model;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public class BusinessManageListviewItem {
    private String businessNumber;  //业务编号
    private String manageNumber;  //业务编号
    private String businessName;  //业务名称
    private String nodeName;  //节点名称
    private String endTime;  //结束时间
    private String proposer; //申请人
    private String overtimeState; //是否超期

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getManageNumber() {
        return manageNumber;
    }

    public void setManageNumber(String manageNumber) {
        this.manageNumber = manageNumber;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getOvertimeState() {
        return overtimeState;
    }

    public void setOvertimeState(String overtimeState) {
        this.overtimeState = overtimeState;
    }
}
