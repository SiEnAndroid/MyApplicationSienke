package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/5/9 0009.
 */
public class SecurityGuideActivity extends Activity {
    private SharedPreferences sharedPreferences_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_guide);

        defaultSetting();
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(sharedPreferences_login.getBoolean("have_logined",false)){
                    Intent intent = new Intent(SecurityGuideActivity.this,SecurityChooseActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SecurityGuideActivity.this,MobileSecurityLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                super.handleMessage(msg);
            }
        }.sendEmptyMessageDelayed(0,2000);
    }
}
