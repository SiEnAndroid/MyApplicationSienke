package com.example.administrator.thinker_soft.Security_check.model;

/**
 * Created by Administrator on 2017/3/16.
 */
public class UserListviewItem {
    private String userName;             //姓名
    private String taskId;               //任务编号
    private String number;               //表编号
    private String phoneNumber;          //电话号码
    private String securityType;         //安检类型
    private String userId;               //用户老编号
    private String userNewId;            //用户新编号
    private String userProperty;         //用气性质
    private String adress;               //地址
    private String securityNumber;       //安检编号
    private int ifEdit;                  //是否编辑(图片)
    private String ifChecked;            //文字
    private String ifUpload;             //是否上传
    private String lastCheckTime;         //上次安检时间
    private String sumDosage;           //总购气量
    /**当前安检时间*/
    private String thisTime;

    public String getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(String lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNewId() {
        return userNewId;
    }

    public void setUserNewId(String userNewId) {
        this.userNewId = userNewId;
    }

    public String getUserProperty() {
        return userProperty;
    }

    public void setUserProperty(String userProperty) {
        this.userProperty = userProperty;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getSecurityNumber() {
        return securityNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }

    public int getIfEdit() {
        return ifEdit;
    }

    public void setIfEdit(int ifEdit) {
        this.ifEdit = ifEdit;
    }

    public String getIfChecked() {
        return ifChecked;
    }

    public void setIfChecked(String ifChecked) {
        this.ifChecked = ifChecked;
    }

    public String getIfUpload() {
        return ifUpload;
    }

    public void setIfUpload(String ifUpload) {
        this.ifUpload = ifUpload;
    }

    public String getThisTime() {
        return thisTime;
    }

    public void setThisTime(String thisTime) {
        this.thisTime = thisTime;
    }

    public String getSumDosage() {
        return sumDosage;
    }

    public void setSumDosage(String sumDosage) {
        this.sumDosage = sumDosage;
    }
}
