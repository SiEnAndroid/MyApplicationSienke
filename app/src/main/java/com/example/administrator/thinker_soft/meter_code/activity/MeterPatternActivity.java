package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/8/23.
 */
public class MeterPatternActivity extends Activity {
    private RadioButton shortcut, detailTb;
    private ImageView back;
    private SharedPreferences sharedPreferences_login, sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        detailTb = (RadioButton) findViewById(R.id.detail_tb);
        shortcut = (RadioButton) findViewById(R.id.shortcut);
        back = (ImageView) findViewById(R.id.back);
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = MeterPatternActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("detail_meter", true)) {
            detailTb.setChecked(true);
        } else {
            shortcut.setChecked(true);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==event.KEYCODE_BACK){
            if (detailTb.isChecked()) {
                sharedPreferences.edit().putBoolean("detail_meter", true).apply();
            } else {
                sharedPreferences.edit().putBoolean("detail_meter", false).apply();
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //点击事件
    public void setViewClickListener() {
        detailTb.setOnClickListener(clickListener);
        shortcut.setOnClickListener(clickListener);
        back.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.detail_tb:
                    shortcut.setChecked(false);
                    break;
                case R.id.shortcut:
                    detailTb.setChecked(false);
                    break;
                case R.id.back:
                    if (detailTb.isChecked()) {
                        sharedPreferences.edit().putBoolean("detail_meter", true).apply();
                    } else {
                        sharedPreferences.edit().putBoolean("detail_meter", false).apply();
                    }
                    finish();
                    break;
            }
        }
    };
}
