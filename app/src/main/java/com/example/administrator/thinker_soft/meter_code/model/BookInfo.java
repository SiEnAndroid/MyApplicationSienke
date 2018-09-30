package com.example.administrator.thinker_soft.meter_code.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BookInfo implements Parcelable {
    private String ID;
    private String NUMBER;
    private String BOOK;
    private String BOOKMEN;
    private String BOOKREMARK;
    private String READERNAME;
    private boolean isChecked;


    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public void setNUMBER(String nUMBER) {
        NUMBER = nUMBER;
    }

    public String getBOOK() {
        return BOOK;
    }

    public void setBOOK(String BOOK) {
        this.BOOK = BOOK;
    }

    public String getBOOKMEN() {
        return BOOKMEN;
    }

    public void setBOOKMEN(String BOOKMEN) {
        this.BOOKMEN = BOOKMEN;
    }

    public String getBOOKREMARK() {
        return BOOKREMARK;
    }

    public void setBOOKREMARK(String BOOKREMARK) {
        this.BOOKREMARK = BOOKREMARK;
    }

    public String getREADERNAME() {
        return READERNAME;
    }

    public void setREADERNAME(String READERNAME) {
        this.READERNAME = READERNAME;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public BookInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ID);
        dest.writeString(this.NUMBER);
        dest.writeString(this.BOOK);
        dest.writeString(this.BOOKMEN);
        dest.writeString(this.BOOKREMARK);
        dest.writeString(this.READERNAME);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

//    protected BookInfo(Parcel in) {
//        this.ID = in.readString();
//        this.NUMBER = in.readString();
//        this.BOOK = in.readString();
//        this.BOOKMEN = in.readString();
//        this.BOOKREMARK = in.readString();
//        this.READERNAME = in.readString();
//        this.isChecked = in.readByte() != 0;
//    }

    public static final Creator<BookInfo> CREATOR = new Creator<BookInfo>() {
        @Override
        public BookInfo createFromParcel(Parcel source) {
            BookInfo bookInfo=new BookInfo();
            bookInfo.ID = source.readString();
            bookInfo.NUMBER = source.readString();
            bookInfo.BOOK = source.readString();
            bookInfo.BOOKMEN = source.readString();
            bookInfo.BOOKREMARK = source.readString();
            bookInfo.READERNAME = source.readString();
            bookInfo.isChecked = source.readByte() != 0;

            return bookInfo;
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };
}
