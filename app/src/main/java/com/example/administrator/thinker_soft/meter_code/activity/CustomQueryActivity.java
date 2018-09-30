package com.example.administrator.thinker_soft.meter_code.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/12 0012.
 */
public class CustomQueryActivity extends AppCompatActivity {
    private ImageView back;
    private CardView bookCardView, userNameCardview, userIdCardview, meterNumberCardview;
    private SharedPreferences sharedPreferences_login, sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_query);

        bindView();
        defaultSetting();
        setViewClickListener();
    }


    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        bookCardView = (CardView) findViewById(R.id.book_cardview);
        userNameCardview = (CardView) findViewById(R.id.user_name_cardview);
        userIdCardview = (CardView) findViewById(R.id.user_id_cardview);
        meterNumberCardview = (CardView) findViewById(R.id.meter_number_cardview);
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = CustomQueryActivity.this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = CustomQueryActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        bookCardView.setOnClickListener(onClickListener);
        userNameCardview.setOnClickListener(onClickListener);
        userIdCardview.setOnClickListener(onClickListener);
        meterNumberCardview.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.back:
                    CustomQueryActivity.this.finish();
                    break;
                case R.id.book_cardview:
                    if (!"".equals(sharedPreferences.getString("currentFileName", ""))) {
                        intent = new Intent(CustomQueryActivity.this, MeterBookQueryActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CustomQueryActivity.this, "请先完成文件选择！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.user_name_cardview:
                    if (!"".equals(sharedPreferences.getString("currentFileName", ""))) {
                        intent = new Intent(CustomQueryActivity.this, MeterUserNameQueryActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CustomQueryActivity.this, "请先完成文件选择！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.user_id_cardview:
                    if (!"".equals(sharedPreferences.getString("currentFileName", ""))) {
                        intent = new Intent(CustomQueryActivity.this, MeterUserIDQueryActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CustomQueryActivity.this, "请先完成文件选择！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.meter_number_cardview:
                    if (!"".equals(sharedPreferences.getString("currentFileName", ""))) {
                        intent = new Intent(CustomQueryActivity.this, MeterNumberQueryActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CustomQueryActivity.this, "请先完成文件选择！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
}