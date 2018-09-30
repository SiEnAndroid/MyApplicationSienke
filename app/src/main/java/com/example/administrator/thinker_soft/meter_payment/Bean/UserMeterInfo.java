package com.example.administrator.thinker_soft.meter_payment.Bean;

public class UserMeterInfo {
    private String pay_state;   //是否缴费
    private int start_degree;  //起度
    private int end_degree;   //止度
    private double remission;   //加减量
    private int changeMeter;   //更换量
    private double adjust_dosage;   //调整量
    private double unit_cost;   //单价
    private double real_dosage;   //实际用量
    private double integrated_water;  //综合水费
    private String date;
    /**抄表员*/
    private String  reader_name;
    /**抄表时间*/
    private String  reader_time;
    /**生产水费*/
    private  String weter_amount;
    /**生产污水处理费*/
    private  String weter_wamount;
    /**表号*/
    private String meter_number;

    public double getReal_dosage() {
        return real_dosage;
    }

    public void setReal_dosage(double real_dosage) {
        this.real_dosage = real_dosage;
    }

    public String getPay_state() {
        return pay_state;
    }

    public void setPay_state(String pay_state) {
        this.pay_state = pay_state;
    }

    public int getStart_degree() {
        return start_degree;
    }

    public void setStart_degree(int start_degree) {
        this.start_degree = start_degree;
    }

    public int getEnd_degree() {
        return end_degree;
    }

    public void setEnd_degree(int end_degree) {
        this.end_degree = end_degree;
    }

    public double getRemission() {
        return remission;
    }

    public void setRemission(double remission) {
        this.remission = remission;
    }

    public int getChangeMeter() {
        return changeMeter;
    }

    public void setChangeMeter(int changeMeter) {
        this.changeMeter = changeMeter;
    }

    public double getAdjust_dosage() {
        return adjust_dosage;
    }

    public void setAdjust_dosage(double adjust_dosage) {
        this.adjust_dosage = adjust_dosage;
    }

    public double getUnit_cost() {
        return unit_cost;
    }

    public void setUnit_cost(double unit_cost) {
        this.unit_cost = unit_cost;
    }

    public double getIntegrated_water() {
        return integrated_water;
    }

    public void setIntegrated_water(double integrated_water) {
        this.integrated_water = integrated_water;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getMeter_number() {
        return meter_number;
    }

    public void setMeter_number(String meter_number) {
        this.meter_number = meter_number;
    }
}