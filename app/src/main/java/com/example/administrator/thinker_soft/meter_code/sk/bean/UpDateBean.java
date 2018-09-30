package com.example.administrator.thinker_soft.meter_code.sk.bean;

/**
 * class
 *
 * @author g
 * @date 2018/8/22:13:51]
 */
public class UpDateBean {

    /**
     * msg : 查询成功
     * date : 2018/8/22
     * success : true
     * version : 3.0
     * url : http://gdown.baidu.com/data/wisegame/f5eae17c4f4bb40b/baidushoujizhushou_16795794.apk
     * updateEmotion : 1、代码优化
     */

    private String msg;
    private String date;
    private boolean success;
    private String version;
    private String url;
    private String updateEmotion;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateEmotion() {
        return updateEmotion;
    }

    public void setUpdateEmotion(String updateEmotion) {
        this.updateEmotion = updateEmotion;
    }
}
