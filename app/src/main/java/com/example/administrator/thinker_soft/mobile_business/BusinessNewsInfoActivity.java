package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.BusinessNewsAdapter;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */
public class BusinessNewsInfoActivity extends Activity {

    private ImageView back;
    private ListView listView;
    private TextView time, content, type;
    private String timeString;
    private SQLiteDatabase db;  //数据库
    private List<BusinessNewsItem> businessNewsItemList = new ArrayList<>();
    private BusinessNewsAdapter adapter;
    private Cursor cursor;
    private BusinessNewsItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_news);//通知公告

        bindView();
        defaultSetting();//初始化设置
        setViewClickListener();
        queryOaAnnounce();
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listview);
        time = (TextView) findViewById(R.id.time);
        content = (TextView) findViewById(R.id.content);
        type = (TextView) findViewById(R.id.type);
    }

    //假数据
    public void getData() {
        for (int i = 0; i < 10; i++) {
            item = new BusinessNewsItem();
            item.setName("张黎明");
            businessNewsItemList.add(item);
        }
    }

    public void setViewClickListener() {
        getData();
        adapter = new BusinessNewsAdapter(BusinessNewsInfoActivity.this, businessNewsItemList);
        listView.setAdapter(adapter);
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
        final MySqliteHelper helper = new MySqliteHelper(BusinessNewsInfoActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        Intent intent = getIntent();
        timeString = intent.getStringExtra("time");
        time.setText(timeString);
    }

    /**
     * 根据用户ID查询用户外勤信息并显示item数据
     */
    private void queryOaAnnounce() {
        cursor = db.rawQuery("select * from OaAnnounce where time=?", new String[]{timeString});
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            time.setText(cursor.getString(cursor.getColumnIndex("time")));
            content.setText(cursor.getString(cursor.getColumnIndex("content")));
            type.setText(cursor.getString(cursor.getColumnIndex("type")));
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
