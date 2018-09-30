package com.example.administrator.thinker_soft.meter_code.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/6/28 0028.
 */
public class MeterUserListviewItem implements Parcelable, Comparable<MeterUserListviewItem> {
    private String meterID;//编号
    private String userID;
    private String userName;
    private String meterNumber;
    private String oldUserID;
    private String lastMonthDegree;
    private String lastMonthDosage;
    private String thisMonthDegree;
    private String thisMonthDosage;
    private String address;
    private String remission;//加减量
    private String changeDosage;//更换量
    private String uploadState;   //上传状态
    private String meterState;  //抄表状态（文字）
    private int ifEdit;    //图片
    private String remark;    //图片
    private int redColor;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemission() {
        return remission;
    }

    public void setRemission(String remission) {
        this.remission = remission;
    }

    public String getChangeDosage() {
        return changeDosage;
    }

    public void setChangeDosage(String changeDosage) {
        this.changeDosage = changeDosage;
    }

    public String getOldUserID() {
        return oldUserID;
    }

    public void setOldUserID(String oldUserID) {
        this.oldUserID = oldUserID;
    }

    public String getMeterID() {
        return meterID;
    }

    public void setMeterID(String meterID) {
        this.meterID = meterID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getLastMonthDegree() {
        return lastMonthDegree;
    }

    public void setLastMonthDegree(String lastMonthDegree) {
        this.lastMonthDegree = lastMonthDegree;
    }

    public String getLastMonthDosage() {
        return lastMonthDosage;
    }

    public void setLastMonthDosage(String lastMonthDosage) {
        this.lastMonthDosage = lastMonthDosage;
    }

    public String getThisMonthDegree() {
        return thisMonthDegree;
    }

    public void setThisMonthDegree(String thisMonthDegree) {
        this.thisMonthDegree = thisMonthDegree;
    }

    public String getThisMonthDosage() {
        return thisMonthDosage;
    }

    public void setThisMonthDosage(String thisMonthDosage) {
        this.thisMonthDosage = thisMonthDosage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUploadState() {
        return uploadState;
    }

    public void setUploadState(String uploadState) {
        this.uploadState = uploadState;
    }

    public String getMeterState() {
        return meterState;
    }

    public void setMeterState(String meterState) {
        this.meterState = meterState;
    }

    public int getIfEdit() {
        return ifEdit;
    }

    public void setIfEdit(int ifEdit) {
        this.ifEdit = ifEdit;
    }

    public int getRedColor() {
        return redColor;
    }

    public void setRedColor(int redColor) {
        this.redColor = redColor;
    }

    @Override
    public String toString() {
        return "MeterUserListviewItem{" +
                "meterID='" + meterID + '\'' +
                ", userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                ", meterNumber='" + meterNumber + '\'' +
                ", oldUserID='" + oldUserID + '\'' +
                ", lastMonthDegree='" + lastMonthDegree + '\'' +
                ", lastMonthDosage='" + lastMonthDosage + '\'' +
                ", thisMonthDegree='" + thisMonthDegree + '\'' +
                ", thisMonthDosage='" + thisMonthDosage + '\'' +
                ", address='" + address + '\'' +
                ", uploadState='" + uploadState + '\'' +
                ", meterState='" + meterState + '\'' +
                ", ifEdit=" + ifEdit +'\''+
                ", redColor=" + redColor +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(meterID);
        parcel.writeString(userID);
        parcel.writeString(userName);
        parcel.writeString(meterNumber);
        parcel.writeString(oldUserID);
        parcel.writeString(lastMonthDegree);
        parcel.writeString(lastMonthDosage);
        parcel.writeString(thisMonthDegree);
        parcel.writeString(thisMonthDosage);
        parcel.writeString(address);
        parcel.writeString(uploadState);
        parcel.writeString(meterState);
        parcel.writeInt(ifEdit);
        parcel.writeString(remark);
        parcel.writeInt(redColor);
    }

    public static final Parcelable.Creator<MeterUserListviewItem> CREATOR = new Parcelable.Creator<MeterUserListviewItem>() {
        @Override
        public MeterUserListviewItem createFromParcel(Parcel parcel) {
            MeterUserListviewItem item = new MeterUserListviewItem();
            item.meterID = parcel.readString();
            item.userID = parcel.readString();
            item.userName = parcel.readString();
            item.meterNumber = parcel.readString();
            item.oldUserID = parcel.readString();
            item.lastMonthDegree = parcel.readString();
            item.lastMonthDosage = parcel.readString();
            item.thisMonthDegree = parcel.readString();
            item.thisMonthDosage = parcel.readString();
            item.address = parcel.readString();
            item.uploadState = parcel.readString();
            item.meterState = parcel.readString();
            item.ifEdit = parcel.readInt();
            item.remark = parcel.readString();
            item.redColor=parcel.readInt();
            return item;
        }

        @Override
        public MeterUserListviewItem[] newArray(int i) {
            return new MeterUserListviewItem[i];
        }
    };

    @Override
    public int compareTo(MeterUserListviewItem item) {
        int i = Integer.parseInt(this.getMeterID()) - Integer.parseInt(item.getMeterID());
        return i;
    }
}
