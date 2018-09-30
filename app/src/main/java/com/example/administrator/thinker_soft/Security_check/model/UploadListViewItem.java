package com.example.administrator.thinker_soft.Security_check.model;

/**
 * Created by Administrator on 2017/3/16.
 */
public class UploadListViewItem {
    private String taskName;   //任务名称
    private String taskNumber;  //任务编号
    private String checkType;   //安检类型
    private String totalUserNumber;  //总用户数
    private String restCount;  //剩余户数
    private String endTime;   //结束时间
    private boolean isChecked;  //是否是选中状态

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getTotalUserNumber() {
        return totalUserNumber;
    }

    public void setTotalUserNumber(String totalUserNumber) {
        this.totalUserNumber = totalUserNumber;
    }

    public String getRestCount() {
        return restCount;
    }

    public void setRestCount(String restCount) {
        this.restCount = restCount;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}