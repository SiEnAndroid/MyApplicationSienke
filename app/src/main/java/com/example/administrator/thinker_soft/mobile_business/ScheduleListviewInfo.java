package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

/**
 * Created by Administrator on 2017/6/28.
 */
public class ScheduleListviewInfo extends Activity {

    private ImageView back;
    private CheckBox slip;
    private TextView title, address, detail, customerName, startDate, endDate;
    private String time;
    private SQLiteDatabase db;  //数据库
    private Cursor cursorCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_listview_info);

        bindView();
        defaultSetting();//初始化设置
        setOnClickListener();
        queryOaCalendar();
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        slip = (CheckBox) findViewById(R.id.slip);
        title = (TextView) findViewById(R.id.title);
        address = (TextView) findViewById(R.id.address);
        detail = (TextView) findViewById(R.id.detail);
        customerName = (TextView) findViewById(R.id.customer_name);
        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
    }

    private void setOnClickListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleListviewInfo.this.finish();
            }
        });
    }

    private void defaultSetting() {
        final MySqliteHelper helper = new MySqliteHelper(ScheduleListviewInfo.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        Intent intent = getIntent();
        time = intent.getStringExtra("time");
        startDate.setText(time);
    }

    /**
     * 根据用户ID查询用户外勤信息并显示item数据
     */
    private void queryOaCalendar() {
        cursorCalendar = db.rawQuery("select * from OaCalendar where beginTime=?", new String[]{time});
        if (cursorCalendar.getCount() == 0) {
            return;
        }
        while (cursorCalendar.moveToNext()) {
            if (cursorCalendar.getString(cursorCalendar.getColumnIndex("isAllDay")).equals("true")) {
                slip.setChecked(true);
            } else {
                slip.setChecked(false);
            }
            title.setText(cursorCalendar.getString(cursorCalendar.getColumnIndex("title")));
            address.setText(cursorCalendar.getString(cursorCalendar.getColumnIndex("address")));
            detail.setText(cursorCalendar.getString(cursorCalendar.getColumnIndex("details")));
            customerName.setText(cursorCalendar.getString(cursorCalendar.getColumnIndex("participant")));
            startDate.setText(cursorCalendar.getString(cursorCalendar.getColumnIndex("beginTime")));
            endDate.setText(cursorCalendar.getString(cursorCalendar.getColumnIndex("endTime")));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursorCalendar != null) {
            cursorCalendar.close();
        }
        db.close();
    }
}
