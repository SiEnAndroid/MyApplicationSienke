package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/3/21.
 */
public class SystemSettingActivity extends Activity {
    private ImageView back;
    private CardView ip, clearData,tempdata_cardview;
    private SharedPreferences sharedPreferences,sharedPreferences_login;
    private CheckBox show_tempdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_settings);
        //绑定控件
        bindView();
        defaultSetting();//初始化设置
        //点击事件
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        ip = (CardView) findViewById(R.id.ip);
        tempdata_cardview = (CardView) findViewById(R.id.tempdata_cardview);
        show_tempdata = (CheckBox) findViewById(R.id.show_tempdata);
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("userId","")+"data", Context.MODE_PRIVATE);
        if(sharedPreferences_login.getBoolean("have_logined",false)){
            tempdata_cardview.setVisibility(View.GONE);
            show_tempdata.setChecked(false);
        }else {
            tempdata_cardview.setVisibility(View.VISIBLE);
            if (sharedPreferences.getBoolean("show_temp_data", false)) {
                show_tempdata.setChecked(true);
            }else {
                show_tempdata.setChecked(false);
            }
        }
    }

    //点击事件
    private void setViewClickListener() {
        back.setOnClickListener(clickListener);
        ip.setOnClickListener(clickListener);
        show_tempdata.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sharedPreferences.edit().putBoolean("show_temp_data",true).apply();
                }else {
                    sharedPreferences.edit().putBoolean("show_temp_data",false).apply();
                }
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.ip:
                    intent = new Intent(SystemSettingActivity.this, IpSettingActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

}
