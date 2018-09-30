package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.io.Serializable;
import java.util.List;

public class AuditDonYuBean  implements Serializable{


    /**
     * msg : 查询成功
     * list : [{"用户地址":"垫江英吉某一个小区","序号":1,"安检员":"SUPER","表编号":null,"安检时间":"2018-09-21 12:52:14","安检备注":"是豆腐干","上次安检时间":null,"安全隐患原因":"原因3333,原因1","安检编号":82,"用户名称":"黄泽","安检状态":"安检不合格","用户编号":"0650100000006","老编号":"01000004","安检计划名称":"9月计划","联系电话":"0","安检情况":"不合格","安全隐患类型":"隐患类型3,隐患类型1"}]
     * status : success
     */

    private String msg;
    private String status;
    private List<ListBean> list;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * userAddress     用户地址 : 垫江英吉某一个小区
         * serialNumber    序号 : 1
         * subject    安检员 : SUPER
         * tableNumber     表编号 : null
         * securityTime      安检时间 : 2018-09-21 12:52:14
         * securityNotes     安检备注 : 是豆腐干
         * lastSecurityCheckTime     上次安检时间 : null
         * causesOfPotentialSafetyHazards     安全隐患原因 : 原因3333,原因1
         * securityNumber      安检编号 : 82
         * userName     用户名称 : 黄泽
         * securityStatus     安检状态 : 安检不合格
         * userNumber     用户编号 : 0650100000006
         * theOldNumber     老编号 : 01000004
         * nameOfSecurityCheckPlan     安检计划名称 : 9月计划
         * contactNumber     联系电话 : 0
         * securityScreening     安检情况 : 不合格
         * typesOfSafetyHazards     安全隐患类型 : 隐患类型3,隐患类型1
         */

        private String userAddress;
        private String serialNumber;
        private String subject;
        private String tableNumber;
        private String securityTime;
        private String securityNotes;
        private String lastSecurityCheckTime;
        private String causesOfPotentialSafetyHazards;
        private String securityNumber;
        private String userName;
        private String securityStatus;
        private String userNumber;
        private String theOldNumber;
        private String nameOfSecurityCheckPlan;
        private String contactNumber;
        private String securityScreening;
        private String typesOfSafetyHazards;
        private String ifthrough;

        public String getIfthrough() {
            return ifthrough;
        }

        public void setIfthrough(String ifthrough) {
            this.ifthrough = ifthrough;
        }

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getTableNumber() {
            return tableNumber;
        }

        public void setTableNumber(String tableNumber) {
            this.tableNumber = tableNumber;
        }

        public String getSecurityTime() {
            return securityTime;
        }

        public void setSecurityTime(String securityTime) {
            this.securityTime = securityTime;
        }

        public String getSecurityNotes() {
            return securityNotes;
        }

        public void setSecurityNotes(String securityNotes) {
            this.securityNotes = securityNotes;
        }

        public String getLastSecurityCheckTime() {
            return lastSecurityCheckTime;
        }

        public void setLastSecurityCheckTime(String lastSecurityCheckTime) {
            this.lastSecurityCheckTime = lastSecurityCheckTime;
        }

        public String getCausesOfPotentialSafetyHazards() {
            return causesOfPotentialSafetyHazards;
        }

        public void setCausesOfPotentialSafetyHazards(String causesOfPotentialSafetyHazards) {
            this.causesOfPotentialSafetyHazards = causesOfPotentialSafetyHazards;
        }

        public String getSecurityNumber() {
            return securityNumber;
        }

        public void setSecurityNumber(String securityNumber) {
            this.securityNumber = securityNumber;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getSecurityStatus() {
            return securityStatus;
        }

        public void setSecurityStatus(String securityStatus) {
            this.securityStatus = securityStatus;
        }

        public String getUserNumber() {
            return userNumber;
        }

        public void setUserNumber(String userNumber) {
            this.userNumber = userNumber;
        }

        public String getTheOldNumber() {
            return theOldNumber;
        }

        public void setTheOldNumber(String theOldNumber) {
            this.theOldNumber = theOldNumber;
        }

        public String getNameOfSecurityCheckPlan() {
            return nameOfSecurityCheckPlan;
        }

        public void setNameOfSecurityCheckPlan(String nameOfSecurityCheckPlan) {
            this.nameOfSecurityCheckPlan = nameOfSecurityCheckPlan;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getSecurityScreening() {
            return securityScreening;
        }

        public void setSecurityScreening(String securityScreening) {
            this.securityScreening = securityScreening;
        }

        public String getTypesOfSafetyHazards() {
            return typesOfSafetyHazards;
        }

        public void setTypesOfSafetyHazards(String typesOfSafetyHazards) {
            this.typesOfSafetyHazards = typesOfSafetyHazards;
        }
    }
}
