package com.example.administrator.thinker_soft.Security_check.model;

/**
 * Created by Administrator on 2017-05-17.
 */
public class SelectTaskItem {
    private String taskId;     //任务ID
    private String totalNumber;  //总户数
    private String restNumber;  //剩余户数
    private String taskName;   //任务名称
    private String startDate;  //开始时间
    private String endDate;    //结束时间
    private boolean isChecked;  //是否是选中状态

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getRestNumber() {
        return restNumber;
    }

    public void setRestNumber(String restNumber) {
        this.restNumber = restNumber;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
