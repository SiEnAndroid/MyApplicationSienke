package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

/**
 * Created by Administrator on 2017/6/29.
 */
public class ReportInfoInfoActivity extends Activity {
    private ImageView back, userPhoto;
    private TextView name, content, summarize, plant;
    private String contentString, summarizeString, plantString;
    private String time;
    private SQLiteDatabase db;  //数据库
    private Cursor cursor;
    private SharedPreferences sharedPreferences_login;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    content.setText(contentString);
                    summarize.setText(summarizeString);
                    plant.setText(plantString);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportinfo_info);

        bindView();//绑定控件
        defaultSetting();
        setOnClickListener();//点击事件
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        userPhoto = (ImageView) findViewById(R.id.user_photo);
        name = (TextView) findViewById(R.id.name);
        content = (TextView) findViewById(R.id.content);
        summarize = (TextView) findViewById(R.id.summarize);
        plant = (TextView) findViewById(R.id.plant);
    }

    private void setOnClickListener() {
        back.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
            }
        }
    };

    private void defaultSetting() {
        final MySqliteHelper helper = new MySqliteHelper(ReportInfoInfoActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        name.setText(sharedPreferences_login.getString("user_name", ""));
        Intent intent = getIntent();
        if (intent != null) {
            time = intent.getStringExtra("time");
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                queryoaReport();
                if (cursor.getCount() != 0) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    private void queryoaReport() {
        cursor = db.rawQuery("select * from oaReport where time=?", new String[]{time});
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            contentString = cursor.getString(cursor.getColumnIndex("createReport"));
            summarizeString = cursor.getString(cursor.getColumnIndex("summarizeReport"));
            plantString = cursor.getString(cursor.getColumnIndex("nextReport"));
            handler.sendEmptyMessage(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }
}
