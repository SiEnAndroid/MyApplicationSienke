package com.example.administrator.thinker_soft.meter_code.activity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.mode.Tools;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.util.ArrayList;

public class MeterNumberQueryActivity extends Activity {
    private EditText editUserMeter;
    private TextView clear, query;
    private ImageView back;
    private SQLiteDatabase db;  //数据库
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    private ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_number_query);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        editUserMeter = (EditText) findViewById(R.id.edit_user_meter);
        clear = (TextView) findViewById(R.id.clear);
        query = (TextView) findViewById(R.id.query);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterNumberQueryActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = MeterNumberQueryActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        clear.setOnClickListener(onClickListener);
        query.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    MeterNumberQueryActivity.this.finish();
                    break;
                case R.id.clear:
                    editUserMeter.setText("");
                    break;
                case R.id.query:
                    if (!editUserMeter.getText().toString().equals("")) {
                        Tools.hideSoftInput(MeterNumberQueryActivity.this, editUserMeter);
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                queryMeterUserInfo(editUserMeter.getText().toString());  //根据表号查询用户信息
                            }
                        }.start();
                    } else {
                        Toast.makeText(MeterNumberQueryActivity.this, "请输入用户编号！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 查询抄表用户信息
     */
    public void queryMeterUserInfo(String meterNumb) {
        userLists.clear();
//        Cursor cursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meter_number=?", new String[]{sharedPreferences_login.getString("userId", ""),sharedPreferences.getString("currentFileName",""),meterNumb});//查询并获得游标
        Cursor cursor = db.rawQuery("select * from MeterUser where login_user_id=? and meter_number=?", new String[]{sharedPreferences_login.getString("userId", ""), meterNumb});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(0);
            return;
        }
        while (cursor.moveToNext()) {
            MeterUserListviewItem item = new MeterUserListviewItem();
            item.setMeterID(cursor.getString(cursor.getColumnIndex("meter_order_number")));
            item.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
            item.setUserID(cursor.getString(cursor.getColumnIndex("user_id")));
            if (!cursor.getString(cursor.getColumnIndex("old_user_id")).equals("null")) {
                item.setOldUserID(cursor.getString(cursor.getColumnIndex("old_user_id")));
            } else {
                item.setOldUserID("无");
            }
            if (!cursor.getString(cursor.getColumnIndex("meter_number")).equals("null")) {
                item.setMeterNumber(cursor.getString(cursor.getColumnIndex("meter_number")));
            } else {
                item.setMeterNumber("无");
            }
            item.setLastMonthDegree(cursor.getString(cursor.getColumnIndex("meter_degrees")));
            item.setLastMonthDosage(cursor.getString(cursor.getColumnIndex("last_month_dosage")));
            item.setAddress(cursor.getString(cursor.getColumnIndex("user_address")));
            if (cursor.getString(cursor.getColumnIndex("uploadState")).equals("false")) {
                item.setUploadState("");
            } else {
                item.setUploadState("已上传");
            }
            if (cursor.getString(cursor.getColumnIndex("meterState")).equals("false")) {
                item.setMeterState("未抄");
                item.setIfEdit(R.mipmap.meter_false);
                item.setThisMonthDegree("无");
                item.setThisMonthDosage("无");
            } else {
                item.setMeterState("已抄");
                item.setIfEdit(R.mipmap.meter_true);
                item.setThisMonthDegree(cursor.getString(cursor.getColumnIndex("this_month_end_degree")));
                item.setThisMonthDosage(cursor.getString(cursor.getColumnIndex("this_month_dosage")));
            }
            item.setRemark(cursor.getString(cursor.getColumnIndex("opened_remark")));
            userLists.add(item);
        }
        handler.sendEmptyMessage(1);
        cursor.close();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(MeterNumberQueryActivity.this, "未查到用户信息，请您核对表号是否正确！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Intent intent = new Intent(MeterNumberQueryActivity.this, MeterUserQueryResultActivity.class);
                    intent.putParcelableArrayListExtra("meter_user_info", userLists);
                    startActivity(intent);
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
