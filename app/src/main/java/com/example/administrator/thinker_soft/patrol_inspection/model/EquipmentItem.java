package com.example.administrator.thinker_soft.patrol_inspection.model;

import android.os.Parcel;
import android.os.Parcelable;

public class EquipmentItem implements Parcelable {
    private String equipmentName;
    private String equipmentInfo;
    private String distance;
    private String state;

    public EquipmentItem() {
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentInfo() {
        return equipmentInfo;
    }

    public void setEquipmentInfo(String equipmentInfo) {
        this.equipmentInfo = equipmentInfo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static final Creator<EquipmentItem> CREATOR = new Creator<EquipmentItem>() {
        @Override
        public EquipmentItem createFromParcel(Parcel in) {
            EquipmentItem item = new EquipmentItem();
            item.setEquipmentName(in.readString());
            item.setEquipmentInfo(in.readString());
            item.setDistance(in.readString());
            item.setState(in.readString());
            return item;
        }

        @Override
        public EquipmentItem[] newArray(int size) {
            return new EquipmentItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(equipmentName);
        parcel.writeString(equipmentInfo);
        parcel.writeString(distance);
        parcel.writeString(state);
    }
}