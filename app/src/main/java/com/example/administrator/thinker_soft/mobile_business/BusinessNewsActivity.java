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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.NewsAdapter;
import com.example.administrator.thinker_soft.mobile_business.model.NewsItem;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */
public class BusinessNewsActivity extends Activity {

    private ImageView back;
    private ListView listView;
    private NewsAdapter adapter;
    private List<NewsItem> itemList = new ArrayList<>();
    private SharedPreferences sharedPreferences_login;
    private SQLiteDatabase db;  //数据库
    private NewsItem item;
    private Cursor cursor;
    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    adapter = new NewsAdapter(BusinessNewsActivity.this, itemList);
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
        setContentView(R.layout.activity_news);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    private void defaultSetting(){
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        final MySqliteHelper helper = new MySqliteHelper(BusinessNewsActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        new Thread(){
            @Override
            public void run(){
                super.run();
                queryOaAnnounce();
                if (cursor.getCount() != 0) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    private void bindView(){
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listview);
    }

    private void setViewClickListener() {
        adapter = new NewsAdapter(BusinessNewsActivity.this, itemList);
        listView.setAdapter(adapter);
        back.setOnClickListener(clickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = (NewsItem) adapter.getItem(position);
                Intent intent = new Intent(BusinessNewsActivity.this, BusinessNewsInfoActivity.class);
                intent.putExtra("time", item.getTime());
                startActivity(intent);
            }
        });

    }

    /**
     * 根据用户ID查询日程安排并显示listview数据*/
    private void queryOaAnnounce() {
        itemList.clear();
        cursor = db.rawQuery("select * from OaAnnounce where userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            NewsItem item = new NewsItem();
            item.setTime(cursor.getString(cursor.getColumnIndex("time")));
            item.setType(cursor.getString(cursor.getColumnIndex("type")));
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
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==RESULT_OK){
            if (requestCode ==100){
                queryOaAnnounce();
                adapter = new NewsAdapter(BusinessNewsActivity.this, itemList);
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
