package com.example.administrator.thinker_soft.Security_check.model;

/**
 * Created by Administrator on 2017/5/10.
 */
public class GridviewTypeItem  {
    private String typeName;
    private String typeId;
    private boolean isChecked;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
