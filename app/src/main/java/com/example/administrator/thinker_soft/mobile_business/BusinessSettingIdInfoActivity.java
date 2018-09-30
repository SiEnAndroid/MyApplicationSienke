package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/28.
 */
public class BusinessSettingIdInfoActivity extends Activity {
    private ImageView back;
    private TextView name;
    private RelativeLayout phoneNumber, email;
    private SharedPreferences sharedPreferences_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_setting_idinfo);

        bindView();//绑定控件
        defaultSetting();
        setOnClickListener();//点击事件
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        phoneNumber = (RelativeLayout) findViewById(R.id.phone_number);
        email = (RelativeLayout) findViewById(R.id.email);
    }
    private void defaultSetting(){
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        name.setText(sharedPreferences_login.getString("user_name", ""));
    }

    private void setOnClickListener(){
        back.setOnClickListener(clickListener);
        phoneNumber.setOnClickListener(clickListener);
        email.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.phone_number:
                    Intent intent = new Intent(BusinessSettingIdInfoActivity.this,BusinessSettingIdinfoPhoneActivity.class);
                    startActivity(intent);
                    break;
                case R.id.email:
                    Intent intent1 = new Intent(BusinessSettingIdInfoActivity.this,BusinessSettingIdinfoEmailActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    };
}
