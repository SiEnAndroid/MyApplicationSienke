package com.example.administrator.thinker_soft.Security_check.model;

/**
 * Created by Administrator on 2017/2/24 0024.
 */
public class QueryListviewItem {
    private String pay_state;   //是否缴费
    private int start_degree;  //起度
    private int end_degree;   //止度
    private double unit_cost;   //单价
    private double integrated_water;  //综合水费
    private String date;

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
}
