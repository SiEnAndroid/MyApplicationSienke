package com.example.administrator.thinker_soft.mobile_business;

/**
 * Created by Administrator on 2017/6/9.
 */
public class BusinessEmailListviewItem {
    private boolean check;//打钩
    private String emailAdress;//
    private boolean startCheck;
    private String time;
    private String title;
    private String content;

    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public boolean getStartCheck() {
        return startCheck;
    }

    public void setStartCheck(boolean startCheck) {
        this.startCheck = startCheck;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
