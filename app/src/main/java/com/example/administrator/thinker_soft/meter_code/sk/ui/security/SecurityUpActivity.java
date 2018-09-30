package com.example.administrator.thinker_soft.meter_code.sk.ui.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;



/**
 * 上传模式
 * Created by Administrator on 2018/7/31.
 */

public class SecurityUpActivity extends BaseActivity {
    private SharedPreferences sharedPreferences_login, sharedPreferences;

    @BindView(R.id.name)
    TextView title;//标题名
    @BindView(R.id.tv_up_title)
    TextView upNoceTitle;//自动上传
    @BindView(R.id.text)
    TextView upTitle;//手动上传
    @BindView(R.id.detail_tb)
    RadioButton detailTb;//0
    @BindView(R.id.shortcut)
    RadioButton shortcut;//100
    @Override
    protected int getContentViewID() {
        return R.layout.activity_pattern;
    }

    @Override
    protected void initView() {
        title.setText("上传模式");
        upNoceTitle.setText("自动上传");
        upTitle.setText("手动上传");
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("security_up", 100)==0) {
            detailTb.setChecked(true);
        } else {
            shortcut.setChecked(true);
        }

    }

    @OnClick({R.id.back,R.id.detail_tb, R.id.shortcut})
    public void OnclikUp(View view){
        if(view.getId()==R.id.back){
            if (detailTb.isChecked()) {
                sharedPreferences.edit().putInt("security_up", 0).apply();
            } else {
                sharedPreferences.edit().putInt("security_up", 100).apply();
            }
            finish();

        }else if (view.getId()==R.id.detail_tb){
            shortcut.setChecked(false);
        }else {
            detailTb.setChecked(false);
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==event.KEYCODE_BACK){
            if (detailTb.isChecked()) {
                sharedPreferences.edit().putInt("security_up", 0).apply();
            } else {
                sharedPreferences.edit().putInt("security_up", 100).apply();
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
