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
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.CheckingInAdapter;
import com.example.administrator.thinker_soft.mobile_business.model.BusinessCheckinginItem;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/13.
 */
public class BusinessCheckingInActivity extends Activity {
    private ImageView back, outSignIn;
    private ListView listView;
    private CheckingInAdapter adapter;
    private List<BusinessCheckinginItem> itemList = new ArrayList<>();
    private SharedPreferences sharedPreferences_login;
    private SQLiteDatabase db;  //数据库
    private String outWorkCount;
    private BusinessCheckinginItem item;
    private Cursor cursorOutWork, cursorOutWorkCount;
    private TextView outWork;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter = new CheckingInAdapter(BusinessCheckingInActivity.this, itemList);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    break;
                case 1:
                    outWork.setText(outWorkCount);
                    Log.i("queryOaUserOutWorkTime", "outWorkCount:" + outWorkCount);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_checking_in);//考勤

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setOnClickListener();//点击事件
    }

    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        MySqliteHelper helper = new MySqliteHelper(BusinessCheckingInActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        new Thread() {
            @Override
            public void run() {
                super.run();
                queryOaUserOutWorkInfo();
                if (cursorOutWork.getCount() != 0) {
                    handler.sendEmptyMessage(0);
                }
                queryOaUserOutWorkTime();
                if (cursorOutWorkCount.getCount() != 0) {
                    handler.sendEmptyMessage(1);
                    Log.i("queryOaUserOutWorkTime", "发送成功");
                }
            }
        }.start();

    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        outSignIn = (ImageView) findViewById(R.id.out_sign_in);
        listView = (ListView) findViewById(R.id.listview);
        outWork = (TextView) findViewById(R.id.outWork);
    }


    private void setOnClickListener() {
        adapter = new CheckingInAdapter(BusinessCheckingInActivity.this, itemList);
        listView.setAdapter(adapter);
        back.setOnClickListener(clickListener);
        outSignIn.setOnClickListener(clickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = (BusinessCheckinginItem) adapter.getItem(position);
                Intent intent = new Intent(BusinessCheckingInActivity.this, BusinessCheckingInListInfo.class);
                intent.putExtra("time", item.getTime());
                startActivity(intent);
            }
        });
    }

    /**
     * 根据用户ID查询用户外勤信息并显示listview数据
     */
    private void queryOaUserOutWorkInfo() {
        itemList.clear();
        cursorOutWork = db.rawQuery("select * from OaUserOutWork where userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
        Log.i("queryOaUserInfo", "集合长度为：" + cursorOutWork.getCount());
        if (cursorOutWork.getCount() == 0) {
            return;
        }
        while (cursorOutWork.moveToNext()) {
            BusinessCheckinginItem item = new BusinessCheckinginItem();
            item.setAddress(cursorOutWork.getString(cursorOutWork.getColumnIndex("checkAddress")));
            item.setTime(cursorOutWork.getString(cursorOutWork.getColumnIndex("checkTime")));
            itemList.add(item);
        }
    }

    /**
     * 根据用户ID查询用户外勤次数
     */
    private void queryOaUserOutWorkTime() {
        cursorOutWorkCount = db.rawQuery("select * from OaUser where userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
        Log.i("queryOaUserInfo", "集合长度为：" + cursorOutWorkCount.getCount());
        if (cursorOutWorkCount.getCount() == 0) {
            return;
        }
        while (cursorOutWorkCount.moveToNext()) {
            outWorkCount = cursorOutWorkCount.getString(cursorOutWorkCount.getColumnIndex("outWork"));
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.out_sign_in:
                    Intent intent = new Intent(BusinessCheckingInActivity.this, BusinessCheckingInInfoActivity.class);
                    startActivityForResult(intent, 100);
                    break;
                case R.id.sign_in:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                queryOaUserOutWorkTime();
                outWork.setText(outWorkCount);
                queryOaUserOutWorkInfo();
                Log.i("CheckingInActivity", "返回码为100 进来了 集合长度为：" + itemList.size());
                adapter = new CheckingInAdapter(BusinessCheckingInActivity.this, itemList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
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
