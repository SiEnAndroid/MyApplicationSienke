package com.example.administrator.thinker_soft.Security_check.model;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
public class UploadFailedItem {
    private String taskId;               //任务编号
    private String userName;             //姓名
    private String userId;               //用户老编号
    private String userNewId;            //用户新编号

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNewId() {
        return userNewId;
    }

    public void setUserNewId(String userNewId) {
        this.userNewId = userNewId;
    }
}
