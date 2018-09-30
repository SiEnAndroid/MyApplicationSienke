package com.example.administrator.thinker_soft.Security_check.model;

/**
 * Created by Administrator on 2017/3/16.
 */
public class DownloadListvieItem {
    private String taskName;   //任务名称
    private int taskNumber;  //任务编号
    private String checkType;   //安检类型
    private int totalUserNumber;  //总用户数
    private String endTime;   //结束时间

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public int getTotalUserNumber() {
        return totalUserNumber;
    }

    public void setTotalUserNumber(int totalUserNumber) {
        this.totalUserNumber = totalUserNumber;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
