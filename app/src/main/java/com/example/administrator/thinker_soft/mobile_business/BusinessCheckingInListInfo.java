package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

/**
 * Created by Administrator on 2017/6/24.
 */
public class BusinessCheckingInListInfo extends Activity {

    private ImageView back, photo;
    private TextView time1, dizhi, leixin, kehu, lxr;
    private String time;
    private SQLiteDatabase db;  //数据库
    private Cursor cursorOutWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_info);


        bindView();
        defaultSetting();//初始化设置
        setOnClickListener();
        queryOaUserOutWorkInfo();
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        time1 = (TextView) findViewById(R.id.time1);
        dizhi = (TextView) findViewById(R.id.dizhi);
        leixin = (TextView) findViewById(R.id.leixin);
        kehu = (TextView) findViewById(R.id.kehu);
        lxr = (TextView) findViewById(R.id.lxr);
        photo = (ImageView) findViewById(R.id.photo);
    }

    private void setOnClickListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusinessCheckingInListInfo.this.finish();
            }
        });
    }

    private void defaultSetting() {
        final MySqliteHelper helper = new MySqliteHelper(BusinessCheckingInListInfo.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        Intent intent = getIntent();
        time = intent.getStringExtra("time");
        time1.setText(time);

    }

    /**
     * 根据用户ID查询用户外勤信息并显示item数据
     */
    private void queryOaUserOutWorkInfo() {
        cursorOutWork = db.rawQuery("select * from OaUserOutWork where checkTime=?", new String[]{time});
        if (cursorOutWork.getCount() == 0) {
            return;
        }
        while (cursorOutWork.moveToNext()) {
            dizhi.setText(cursorOutWork.getString(cursorOutWork.getColumnIndex("checkAddress")));
            leixin.setText(cursorOutWork.getString(cursorOutWork.getColumnIndex("contactType")));
            kehu.setText(cursorOutWork.getString(cursorOutWork.getColumnIndex("customerName")));
            lxr.setText(cursorOutWork.getString(cursorOutWork.getColumnIndex("customerPhoneNumber")));
            photo.setImageResource(cursorOutWork.getInt(cursorOutWork.getColumnIndex("photo")));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursorOutWork != null) {
            cursorOutWork.close();
        }
        db.close();
    }
}
