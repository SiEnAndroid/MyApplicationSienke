package com.example.administrator.thinker_soft.meter_code.model;

import android.os.Parcel;
import android.os.Parcelable;

public class  AreaInfo implements Parcelable{
	private String ID;
	private String Area;
	private String AreaRemark;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getArea() {
		return Area;
	}

	public void setArea(String Area) {
		this.Area = Area;
	}

	public String getAreaRemark() {
		return AreaRemark;
	}

	public void setAreaRemark(String AreaRemark) {
		this.AreaRemark = AreaRemark;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ID);
		dest.writeString(Area);
		dest.writeString(AreaRemark);
	}

	public static final Parcelable.Creator<AreaInfo>CREATOR = new Parcelable.Creator<AreaInfo>(){
		@Override
		public AreaInfo createFromParcel(Parcel parcel) {
			AreaInfo item = new AreaInfo();
			item.ID = parcel.readString();
			item.Area = parcel.readString();
			item.AreaRemark = parcel.readString();
			return item;
		}

		@Override
		public AreaInfo[] newArray(int i) {
			return new AreaInfo[i];
		}
	};
}
