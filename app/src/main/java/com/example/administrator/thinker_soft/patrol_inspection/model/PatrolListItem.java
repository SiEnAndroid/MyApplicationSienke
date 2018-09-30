package com.example.administrator.thinker_soft.patrol_inspection.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PatrolListItem implements Parcelable {
    private String title;
    private String date;
    private String state;

    public PatrolListItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static final Creator<PatrolListItem> CREATOR = new Creator<PatrolListItem>() {
        @Override
        public PatrolListItem createFromParcel(Parcel in) {
            PatrolListItem item = new PatrolListItem();
            item.title = in.readString();
            item.date = in.readString();
            item.state = in.readString();
            return item;
        }

        @Override
        public PatrolListItem[] newArray(int size) {
            return new PatrolListItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(state);
    }
}