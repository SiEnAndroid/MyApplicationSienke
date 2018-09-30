package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/28.
 */
public class BusinessSettingIdinfoEmailActivity extends Activity {

    private ImageView back;
    private TextView emailNumber;
    private Button genghuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_idinfo_email);

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    private void bindView() {
        emailNumber = (TextView) findViewById(R.id.email_number);
        back = (ImageView) findViewById(R.id.back);
        genghuan = (Button) findViewById(R.id.genghuan);
    }

    private void setOnClickListener() {
        back.setOnClickListener(clickListener);
        genghuan.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.genghuan:
                    finish();
                    break;
            }
        }
    };
}
