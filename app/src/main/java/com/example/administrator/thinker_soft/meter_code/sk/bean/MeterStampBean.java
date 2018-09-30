package com.example.administrator.thinker_soft.meter_code.sk.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 小票数据
 * Created by Administrator on 2018/4/28.
 */

public class MeterStampBean implements Parcelable {
    /**表号*/
    private String meter_number;
    /**缴费日期*/
    private String meter_data;
    /**户名*/
    private String user_name;
    /**用户地址*/
    private String user_address;
    /**起度*/
    private String min_start;
    /**止度*/
    private String max_end;
    /**合计吨数*/
    private String all_max;
    /**缴费金额*/
    private String user_amount;
    /**----------------------------------*/
    /**生产水费*/
    private  String weter_amount;
    /**生产污水处理费*/
    private  String weter_wamount;
    /**收款单位*/
    private String  meter_payee;
    /**开户行*/
    private String  bank_name;
    /**账号*/
    private String  meter_account;
    /**交款地址*/
    private String  payment_address;
    /**收费电话*/
    private String  payment_phone;
    /**抄表、维修电话*/
    private String  maintain_phone;
    /**抄表员*/
    private String  reader_name;
    /**抄表时间*/
    private String  reader_time;
    /**备注*/
    private String  reader_remark;



    public String getMeter_number() {
        return meter_number;
    }

    public void setMeter_number(String meter_number) {
        this.meter_number = meter_number;
    }

    public String getMeter_data() {
        return meter_data;
    }

    public void setMeter_data(String meter_data) {
        this.meter_data = meter_data;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getMin_start() {
        return min_start;
    }

    public void setMin_start(String min_start) {
        this.min_start = min_start;
    }

    public String getMax_end() {
        return max_end;
    }

    public void setMax_end(String max_end) {
        this.max_end = max_end;
    }

    public String getAll_max() {
        return all_max;
    }

    public void setAll_max(String all_max) {
        this.all_max = all_max;
    }

    public String getUser_amount() {
        return user_amount;
    }

    public void setUser_amount(String user_amount) {
        this.user_amount = user_amount;
    }

    public String getWeter_amount() {
        return weter_amount;
    }

    public void setWeter_amount(String weter_amount) {
        this.weter_amount = weter_amount;
    }

    public String getWeter_wamount() {
        return weter_wamount;
    }

    public void setWeter_wamount(String weter_wamount) {
        this.weter_wamount = weter_wamount;
    }

    public String getMeter_payee() {
        return meter_payee;
    }

    public void setMeter_payee(String meter_payee) {
        this.meter_payee = meter_payee;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getMeter_account() {
        return meter_account;
    }

    public void setMeter_account(String meter_account) {
        this.meter_account = meter_account;
    }

    public String getPayment_address() {
        return payment_address;
    }

    public void setPayment_address(String payment_address) {
        this.payment_address = payment_address;
    }

    public String getPayment_phone() {
        return payment_phone;
    }

    public void setPayment_phone(String payment_phone) {
        this.payment_phone = payment_phone;
    }

    public String getMaintain_phone() {
        return maintain_phone;
    }

    public void setMaintain_phone(String maintain_phone) {
        this.maintain_phone = maintain_phone;
    }

    public String getReader_name() {
        return reader_name;
    }

    public void setReader_name(String reader_name) {
        this.reader_name = reader_name;
    }

    public String getReader_time() {
        return reader_time;
    }

    public void setReader_time(String reader_time) {
        this.reader_time = reader_time;
    }

    public String getReader_remark() {
        return reader_remark;
    }

    public void setReader_remark(String reader_remark) {
        this.reader_remark = reader_remark;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.meter_number);
        dest.writeString(this.meter_data);
        dest.writeString(this.user_name);
        dest.writeString(this.user_address);
        dest.writeString(this.min_start);
        dest.writeString(this.max_end);
        dest.writeString(this.all_max);
        dest.writeString(this.user_amount);
        dest.writeString(this.weter_amount);
        dest.writeString(this.weter_wamount);
        dest.writeString(this.meter_payee);
        dest.writeString(this.bank_name);
        dest.writeString(this.meter_account);
        dest.writeString(this.payment_address);
        dest.writeString(this.payment_phone);
        dest.writeString(this.maintain_phone);
        dest.writeString(this.reader_name);
        dest.writeString(this.reader_time);
        dest.writeString(this.reader_remark);

    }

    public MeterStampBean() {
    }

    protected MeterStampBean(Parcel in) {
        this.meter_number = in.readString();
        this.meter_data = in.readString();
        this.user_name = in.readString();
        this.user_address = in.readString();
        this.min_start = in.readString();
        this.max_end = in.readString();
        this.all_max = in.readString();
        this.user_amount = in.readString();
        this.weter_amount = in.readString();
        this.weter_wamount = in.readString();
        this.meter_payee = in.readString();
        this.bank_name = in.readString();
        this.meter_account = in.readString();
        this.payment_address = in.readString();
        this.payment_phone = in.readString();
        this.maintain_phone = in.readString();
        this.reader_name = in.readString();
        this.reader_time = in.readString();
        this.reader_remark = in.readString();

    }

    public static final Creator<MeterStampBean> CREATOR = new Creator<MeterStampBean>() {
        @Override
        public MeterStampBean createFromParcel(Parcel source) {
            return new MeterStampBean(source);
        }

        @Override
        public MeterStampBean[] newArray(int size) {
            return new MeterStampBean[size];
        }
    };
}
