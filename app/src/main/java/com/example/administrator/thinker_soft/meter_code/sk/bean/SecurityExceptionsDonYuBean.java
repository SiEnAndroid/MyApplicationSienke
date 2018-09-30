package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.util.List;

public class SecurityExceptionsDonYuBean {


    /**
     * msg : 查询成功
     * list : [{"用户地址":"北滨二路","序号":1,"安检员":"SUPER","表编号":"124467","安检时间":"2018-09-30 09:34:55","安检备注":"大股东发生股份的","上次安检时间":"2018-09-30 09:34:55","安全隐患原因":"原因1,原因2","安检编号":139,"计划编号":93,"用户名称":"app测试13","安检状态":"安检不合格","用户编号":"0650100000050","老编号":"01010009","安检计划名称":"9月安检数据测试","联系电话":null,"安检情况":"不合格","开户日期":"2018-09-29 16:33:59","安全隐患类型":"隐患类型1,隐患类型2"},{"用户地址":"北滨二路","序号":2,"安检员":"SUPER","表编号":"124483","安检时间":"2018-09-30 09:34:14","安检备注":"大范甘迪三方合同","上次安检时间":"2018-09-30 09:34:14","安全隐患原因":"原因1","安检编号":141,"计划编号":93,"用户名称":"app测试29","安检状态":"安检不合格","用户编号":"0650100000066","老编号":"01010025","安检计划名称":"9月安检数据测试","联系电话":null,"安检情况":"不合格","开户日期":"2018-09-29 16:34:02","安全隐患类型":"隐患类型1"},{"用户地址":"垫江英吉某一个小区","序号":3,"安检员":"SUPER","表编号":null,"安检时间":"2018-09-21 12:52:14","安检备注":"是豆腐干","上次安检时间":"2018-09-21 12:52:14","安全隐患原因":"原因3333,原因1","安检编号":82,"计划编号":71,"用户名称":"黄泽","安检状态":"安检不合格","用户编号":"0650100000006","老编号":"01000004","安检计划名称":"9月计划","联系电话":"0","安检情况":"不合格","开户日期":"2018-09-12 15:21:09","安全隐患类型":"隐患类型3,隐患类型1"}]
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

    public static class ListBean {
        /**
         * userAddress 用户地址 : 北滨二路
         * serialNumber 序号 : 1
         * subject 安检员 : SUPER
         * tableNumber 表编号 : 124467
         * securityTime 安检时间 : 2018-09-30 09:34:55
         * securityNotes 安检备注 : 大股东发生股份的
         * lastSecurityCheckTime 上次安检时间 : 2018-09-30 09:34:55
         * causesOfPotentialSafetyHazards 安全隐患原因 : 原因1,原因2
         * securityNumber 安检编号 : 139
         * planNumber 计划编号 : 93
         * userName 用户名称 : app测试13
         * securityStatus 安检状态 : 安检不合格
         * userNumber 用户编号 : 0650100000050
         * oldNumber 老编号 : 01010009
         * nameOfSecurityCheckPlan 安检计划名称 : 9月安检数据测试
         * contactNumber 联系电话 : null
         * securityScreening 安检情况 : 不合格
         * accountOpeningDate 开户日期 : 2018-09-29 16:33:59
         * typesOfSafetyHazards 安全隐患类型 : 隐患类型1,隐患类型2
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
        private String planNumber;
        private String userName;
        private String securityStatus;
        private String userNumber;
        private String oldNumber;
        private String nameOfSecurityCheckPlan;
        private String contactNumber;
        private String securityScreening;
        private String accountOpeningDate;
        private String typesOfSafetyHazards;

        private String 用户地址;
        private String 序号;
        private String 安检员;
        private String 表编号;
        private String 安检时间;
        private String 安检备注;
        private String 上次安检时间;
        private String 安全隐患原因;
        private String 安检编号;
        private String 计划编号;
        private String 用户名称;
        private String 安检状态;
        private String 用户编号;
        private String 老编号;
        private String 安检计划名称;
        private String 联系电话;
        private String 安检情况;
        private String 开户日期;
        private String 安全隐患类型;

        public String get用户地址() {
            return 用户地址;
        }

        public void set用户地址(String 用户地址) {
            this.用户地址 = 用户地址;
        }

        public String get序号() {
            return 序号;
        }

        public void set序号(String 序号) {
            this.序号 = 序号;
        }

        public String get安检员() {
            return 安检员;
        }

        public void set安检员(String 安检员) {
            this.安检员 = 安检员;
        }

        public String get表编号() {
            return 表编号;
        }

        public void set表编号(String 表编号) {
            this.表编号 = 表编号;
        }

        public String get安检时间() {
            return 安检时间;
        }

        public void set安检时间(String 安检时间) {
            this.安检时间 = 安检时间;
        }

        public String get安检备注() {
            return 安检备注;
        }

        public void set安检备注(String 安检备注) {
            this.安检备注 = 安检备注;
        }

        public String get上次安检时间() {
            return 上次安检时间;
        }

        public void set上次安检时间(String 上次安检时间) {
            this.上次安检时间 = 上次安检时间;
        }

        public String get安全隐患原因() {
            return 安全隐患原因;
        }

        public void set安全隐患原因(String 安全隐患原因) {
            this.安全隐患原因 = 安全隐患原因;
        }

        public String get安检编号() {
            return 安检编号;
        }

        public void set安检编号(String 安检编号) {
            this.安检编号 = 安检编号;
        }

        public String get计划编号() {
            return 计划编号;
        }

        public void set计划编号(String 计划编号) {
            this.计划编号 = 计划编号;
        }

        public String get用户名称() {
            return 用户名称;
        }

        public void set用户名称(String 用户名称) {
            this.用户名称 = 用户名称;
        }

        public String get安检状态() {
            return 安检状态;
        }

        public void set安检状态(String 安检状态) {
            this.安检状态 = 安检状态;
        }

        public String get用户编号() {
            return 用户编号;
        }

        public void set用户编号(String 用户编号) {
            this.用户编号 = 用户编号;
        }

        public String get老编号() {
            return 老编号;
        }

        public void set老编号(String 老编号) {
            this.老编号 = 老编号;
        }

        public String get安检计划名称() {
            return 安检计划名称;
        }

        public void set安检计划名称(String 安检计划名称) {
            this.安检计划名称 = 安检计划名称;
        }

        public String get联系电话() {
            return 联系电话;
        }

        public void set联系电话(String 联系电话) {
            this.联系电话 = 联系电话;
        }

        public String get安检情况() {
            return 安检情况;
        }

        public void set安检情况(String 安检情况) {
            this.安检情况 = 安检情况;
        }

        public String get开户日期() {
            return 开户日期;
        }

        public void set开户日期(String 开户日期) {
            this.开户日期 = 开户日期;
        }

        public String get安全隐患类型() {
            return 安全隐患类型;
        }

        public void set安全隐患类型(String 安全隐患类型) {
            this.安全隐患类型 = 安全隐患类型;
        }
    }
}
