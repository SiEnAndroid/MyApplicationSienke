package com.example.administrator.thinker_soft.Security_check.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/5.
 */
public class NewTaskListviewItem implements Parcelable {
    private String userName;              //姓名
    private String userproperty;          //用气性质
    private String securityState;         //安检状态
    private String remarks;               //备注
    private String number;                //表编号
    private String phoneNumber;           //电话号码
    private String userId;                //用户新编号
    private String oldUserId;             //用户老编号
    private String adress;                //地址
    private int flag;                     //标志

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserproperty() {
        return userproperty;
    }

    public void setUserproperty(String userproperty) {
        this.userproperty = userproperty;
    }

    public String getSecurityState() {
        return securityState;
    }

    public void setSecurityState(String securityState) {
        this.securityState = securityState;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldUserId() {
        return oldUserId;
    }

    public void setOldUserId(String oldUserId) {
        this.oldUserId = oldUserId;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(userproperty);
        parcel.writeString(number);
        parcel.writeString(phoneNumber);
        parcel.writeString(userId);
        parcel.writeString(oldUserId);
        parcel.writeString(adress);
    }
    public static final Parcelable.Creator<NewTaskListviewItem>CREATOR = new Parcelable.Creator<NewTaskListviewItem>(){
        @Override
        public NewTaskListviewItem createFromParcel(Parcel parcel) {
            NewTaskListviewItem item = new NewTaskListviewItem();
            item.userName = parcel.readString();
            item.userproperty = parcel.readString();
            item.number = parcel.readString();
            item.phoneNumber = parcel.readString();
            item.userId = parcel.readString();
            item.oldUserId = parcel.readString();
            item.adress = parcel.readString();
            return item;
        }

        @Override
        public NewTaskListviewItem[] newArray(int i) {
            return new NewTaskListviewItem[i];
        }
    };
}

