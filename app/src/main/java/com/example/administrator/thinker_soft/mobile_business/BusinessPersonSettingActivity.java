package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/12.
 */
public class BusinessPersonSettingActivity extends Activity {
    private ImageView back;
    private RelativeLayout id,news,general;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_person_setting);//个人设置

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView(){
        back = (ImageView) findViewById(R.id.back);
        id = (RelativeLayout) findViewById(R.id.id);
        news = (RelativeLayout) findViewById(R.id.news);
        general = (RelativeLayout) findViewById(R.id.general);
    }

    public void setOnClickListener(){
        back.setOnClickListener(clickListener);
        id.setOnClickListener(clickListener);
        news.setOnClickListener(clickListener);
        general.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.id:
                    Intent intent = new Intent(BusinessPersonSettingActivity.this,BusinessSettingIdInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.news:
                    Intent intent1 = new Intent(BusinessPersonSettingActivity.this,BusinessSettingNewsActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.general:
                    Intent intent2 = new Intent(BusinessPersonSettingActivity.this,BusinessSettingGeneralActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    };
}
