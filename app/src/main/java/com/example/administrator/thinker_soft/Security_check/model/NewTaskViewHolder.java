package com.example.administrator.thinker_soft.Security_check.model;

import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/4/19 0019.
 */
public class NewTaskViewHolder {
    public TextView user_name;  //姓名
    public TextView securityState; //安检状态
    public LinearLayout securityStateRoot;
    public TextView remarks; //备注
    public LinearLayout remarksRoot;
    public TextView number;  //表编号
    public TextView phone_number;  //电话号码
    public TextView user_id;  //用户新编号
    public TextView old_user_id;  //用户老编号
    public TextView userProperty;  //用气性质
    public TextView address;   //地址
    public CheckBox checkBox;  //选择框
}
