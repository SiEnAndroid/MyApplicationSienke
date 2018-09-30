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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.BusinessDataInfoAdapter;
import com.example.administrator.thinker_soft.mobile_business.model.DataInfoItem;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12.
 */
public class BusinessScheduleActivity extends Activity {
    private ImageView back, tj;
    private ListView listview;
    private BusinessDataInfoAdapter adapter;
    private List<DataInfoItem> itemList = new ArrayList<>();
    private SharedPreferences sharedPreferences_login;
    private SQLiteDatabase db;  //数据库
    private DataInfoItem item;
    private Cursor cursorCalendar;
    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    adapter = new BusinessDataInfoAdapter(BusinessScheduleActivity.this, itemList);
                    adapter.notifyDataSetChanged();
                    listview.setAdapter(adapter);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_data);//日程安排

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        final MySqliteHelper helper = new MySqliteHelper(BusinessScheduleActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        new Thread(){
            @Override
            public void run(){
                super.run();
                queryOaCalendar();
                if (cursorCalendar.getCount() != 0) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        tj = (ImageView) findViewById(R.id.tj);
        listview = (ListView) findViewById(R.id.listview);
    }

    private void setViewClickListener() {
        adapter = new BusinessDataInfoAdapter(BusinessScheduleActivity.this, itemList);
        listview.setAdapter(adapter);
        back.setOnClickListener(clickListener);
        tj.setOnClickListener(clickListener);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = (DataInfoItem) adapter.getItem(position);
                Intent intent = new Intent(BusinessScheduleActivity.this, ScheduleListviewInfo.class);
                intent.putExtra("time", item.getTime());
                startActivity(intent);
            }
        });

    }

    /**
     * 根据用户ID查询日程安排并显示listview数据*/
    private void queryOaCalendar() {
        itemList.clear();
        cursorCalendar = db.rawQuery("select * from OaCalendar where userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
        Log.i("queryOaUserInfo", "集合长度为：" + cursorCalendar.getCount());
        if (cursorCalendar.getCount() == 0) {
            return;
        }
        while (cursorCalendar.moveToNext()) {
            DataInfoItem item = new DataInfoItem();
            item.setTitle(cursorCalendar.getString(cursorCalendar.getColumnIndex("title")));
            item.setTime(cursorCalendar.getString(cursorCalendar.getColumnIndex("beginTime")));
            itemList.add(item);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.tj:
                    Intent intent = new Intent(BusinessScheduleActivity.this, BusinessDataInfoActivity.class);
                    startActivityForResult(intent,100);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==RESULT_OK){
            if (requestCode ==100){
                queryOaCalendar();
                adapter = new BusinessDataInfoAdapter(BusinessScheduleActivity.this, itemList);
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);
            }
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
