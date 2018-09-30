package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.io.Serializable;
import java.util.List;

public class NewUsersBean implements Serializable {

    /**
     * msg : 查询成功
     * list : [{"用户编号":"0650100000041","用户地址":"1","性质名称":"居民用户","气表编号":null,"联系电话":"1","开户时间":"2018-09-27 11:09:23","分区名称":"默认","上次安检时间":null,"用户名称":"1"},{"用户编号":"0650100000028","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户31"},{"用户编号":"0650100000037","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户406"},{"用户编号":"0650100000033","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户36"},{"用户编号":"0650100000035","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户38"},{"用户编号":"0650100000025","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户2999"},{"用户编号":"0650100000032","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":"898","联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户35"},{"用户编号":"0650100000030","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户33"},{"用户编号":"0650300000001","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":"256598","联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试号1"},{"用户编号":"0650100000034","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户37"},{"用户编号":"0650100000024","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户20"},{"用户编号":"0650100000031","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户34"},{"用户编号":"0650100000027","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":"15353","联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户30"},{"用户编号":"0650100000036","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户39"},{"用户编号":"0650100000029","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户32"},{"用户编号":"0650100000026","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":null,"用户名称":"测试户263"},{"用户编号":"0650100000020","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":"541","联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":"2018-09-28 15:02:59","用户名称":"测试户26"},{"用户编号":"0650100000014","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":"2018-09-27 15:01:07","用户名称":"测试户9"},{"用户编号":"0650100000007","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":"0","联系电话":"13500000000","开户时间":"2018-09-27 00:00:00","分区名称":"泽普分区","上次安检时间":"2018-09-27 09:48:03","用户名称":"测试户2"},{"用户编号":"0650100000011","用户地址":"重庆市江北区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:43:34","分区名称":"泽普分区","上次安检时间":"2018-09-27 09:55:10","用户名称":"测试户6"},{"用户编号":"0650400000001","用户地址":"泽西岛","性质名称":"居民用户","气表编号":null,"联系电话":"182232323235","开户时间":"2018-09-29 00:00:00","分区名称":"英吉沙分区","上次安检时间":null,"用户名称":"测试1"},{"用户编号":"0650400000002","用户地址":"泽西岛","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-27 08:47:04","分区名称":"英吉沙分区","上次安检时间":null,"用户名称":"测试2"},{"用户编号":"0650100000006","用户地址":"垫江英吉某一个小区","性质名称":"居民用户","气表编号":null,"联系电话":"0","开户时间":"2018-09-12 15:21:09","分区名称":"英吉沙分区","上次安检时间":"2018-09-21 12:52:14","用户名称":"黄泽"},{"用户编号":"0650100000042","用户地址":"325322655494654","性质名称":"居民用户","气表编号":"3659659","联系电话":"21143","开户时间":"2018-09-28 10:55:53","分区名称":"测试分区","上次安检时间":null,"用户名称":"测试用户报装"}]
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
         * userNumber         用户编号 : 0650100000041
         * userAddress         用户地址 : 1
         * propertyName          性质名称 : 居民用户
         * gasMeterNumber          气表编号 : null
         * contactNumber         联系电话 : 1
         * accountOpeningTime         开户时间 : 2018-09-27 11:09:23
         * partitionName         分区名称 : 默认
         * lastSecurityCheckTime         上次安检时间 : null
         * userName         用户名称 : 1
         */

        private String userNumber;
        private String userAddress;
        private String propertyName;
        private String gasMeterNumber;
        private String contactNumber;
        private String accountOpeningTime;
        private String partitionName;
        private String lastSecurityCheckTime;
        private String userName;

        public String getUserNumber() {
            return userNumber;
        }

        public void setUserNumber(String userNumber) {
            this.userNumber = userNumber;
        }

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getGasMeterNumber() {
            return gasMeterNumber;
        }

        public void setGasMeterNumber(String gasMeterNumber) {
            this.gasMeterNumber = gasMeterNumber;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getAccountOpeningTime() {
            return accountOpeningTime;
        }

        public void setAccountOpeningTime(String accountOpeningTime) {
            this.accountOpeningTime = accountOpeningTime;
        }

        public String getPartitionName() {
            return partitionName;
        }

        public void setPartitionName(String partitionName) {
            this.partitionName = partitionName;
        }

        public String getLastSecurityCheckTime() {
            return lastSecurityCheckTime;
        }

        public void setLastSecurityCheckTime(String lastSecurityCheckTime) {
            this.lastSecurityCheckTime = lastSecurityCheckTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
