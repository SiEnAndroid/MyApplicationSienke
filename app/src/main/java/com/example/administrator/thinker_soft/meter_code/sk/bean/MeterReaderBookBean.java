package com.example.administrator.thinker_soft.meter_code.sk.bean;

/**
 * Created by Administrator on 2018/4/19.
 * 抄表本
 */

public class MeterReaderBookBean {

    /**
     * n_book_id : 0
     * c_book_name : 默认抄表本
     * n_book_meter_reader_id : 1
     * n_book_state : 1
     * c_book_remark : null
     * n_company_id : 1
     * c_book_number : 0
     * n_book_reminder_id : 1
     * bookReminderIdName : null
     * c_meter_reader_name : SUPER
     */

    private int n_book_id;
    private String c_book_name;
    private int n_book_meter_reader_id;
    private int n_book_state;
    private Object c_book_remark;
    private int n_company_id;
    private String c_book_number;
    private int n_book_reminder_id;
    private Object bookReminderIdName;
    private String c_meter_reader_name;

    public int getN_book_id() {
        return n_book_id;
    }

    public void setN_book_id(int n_book_id) {
        this.n_book_id = n_book_id;
    }

    public String getC_book_name() {
        return c_book_name;
    }

    public void setC_book_name(String c_book_name) {
        this.c_book_name = c_book_name;
    }

    public int getN_book_meter_reader_id() {
        return n_book_meter_reader_id;
    }

    public void setN_book_meter_reader_id(int n_book_meter_reader_id) {
        this.n_book_meter_reader_id = n_book_meter_reader_id;
    }

    public int getN_book_state() {
        return n_book_state;
    }

    public void setN_book_state(int n_book_state) {
        this.n_book_state = n_book_state;
    }

    public Object getC_book_remark() {
        return c_book_remark;
    }

    public void setC_book_remark(Object c_book_remark) {
        this.c_book_remark = c_book_remark;
    }

    public int getN_company_id() {
        return n_company_id;
    }

    public void setN_company_id(int n_company_id) {
        this.n_company_id = n_company_id;
    }

    public String getC_book_number() {
        return c_book_number;
    }

    public void setC_book_number(String c_book_number) {
        this.c_book_number = c_book_number;
    }

    public int getN_book_reminder_id() {
        return n_book_reminder_id;
    }

    public void setN_book_reminder_id(int n_book_reminder_id) {
        this.n_book_reminder_id = n_book_reminder_id;
    }

    public Object getBookReminderIdName() {
        return bookReminderIdName;
    }

    public void setBookReminderIdName(Object bookReminderIdName) {
        this.bookReminderIdName = bookReminderIdName;
    }

    public String getC_meter_reader_name() {
        return c_meter_reader_name;
    }

    public void setC_meter_reader_name(String c_meter_reader_name) {
        this.c_meter_reader_name = c_meter_reader_name;
    }
}
