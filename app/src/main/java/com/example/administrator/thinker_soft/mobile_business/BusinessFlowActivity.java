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
import android.widget.RelativeLayout;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.BusinessFlowAdapter;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */
public class BusinessFlowActivity extends Activity {

    private ImageView back;
    private SharedPreferences sharedPreferences_login;
    private ListView listView;
    private RelativeLayout add;
    private BusinessFlowListviewItem item;
    private SQLiteDatabase db;  //数据库
    private Cursor cursor;
    private BusinessFlowAdapter adapter;
    private List<BusinessFlowListviewItem> businessFlowListviewItemList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.i("WorkReportInfo", "设置适配器成功");
                    adapter = new BusinessFlowAdapter(BusinessFlowActivity.this, businessFlowListviewItemList);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_flow);//审批流程

        bindView();//绑定控件
        defaultSetting();
        setOnClickListener();//点击事件
    }

    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        final MySqliteHelper helper = new MySqliteHelper(BusinessFlowActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        new Thread() {
            @Override
            public void run() {
                super.run();
                queryFlow();
                if (cursor.getCount() != 0) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listview);
        add = (RelativeLayout) findViewById(R.id.add);
    }


    public void setOnClickListener() {
        adapter = new BusinessFlowAdapter(BusinessFlowActivity.this, businessFlowListviewItemList);
        listView.setAdapter(adapter);
        back.setOnClickListener(clickListener);
        add.setOnClickListener(clickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = (BusinessFlowListviewItem) adapter.getItem(position);
                Intent intent = new Intent(BusinessFlowActivity.this, BusinessFlowInfoActivity.class);
                intent.putExtra("time", item.getTime());
                startActivity(intent);
            }
        });
    }

    /**
     * 根据时间查询工作汇报并显示listview数据
     */
    private void queryFlow() {
        businessFlowListviewItemList.clear();
        cursor = db.rawQuery("select * from Flow where userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
        Log.i("queryOaUserInfo", "集合长度为：" + cursor.getCount());
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            BusinessFlowListviewItem item = new BusinessFlowListviewItem();
            item.setName(cursor.getString(cursor.getColumnIndex("userName")));
            item.setTitle(cursor.getString(cursor.getColumnIndex("id")));
            item.setTime(cursor.getString(cursor.getColumnIndex("date")));
            businessFlowListviewItemList.add(item);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.add:
                    Intent intent = new Intent(BusinessFlowActivity.this, BusinessFlowInfoOneActivity.class);
                    startActivityForResult(intent, 100);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                queryFlow();
                adapter = new BusinessFlowAdapter(BusinessFlowActivity.this, businessFlowListviewItemList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
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
