package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/7.
 */

public class PhotoPathBean implements Serializable {
    /**图片路径*/
    private String cropPath;
    /**图片类型*/
    private String type;
    /**图片id*/
    private  String typeId;

    public PhotoPathBean() {
    }

    public PhotoPathBean(String cropPath, String type, String typeId) {
        this.cropPath = cropPath;
        this.type = type;
        this.typeId = typeId;
    }

    public String getCropPath() {
        return cropPath;
    }

    public void setCropPath(String cropPath) {
        this.cropPath = cropPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
