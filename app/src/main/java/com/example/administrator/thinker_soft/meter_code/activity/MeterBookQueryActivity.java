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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterBookSingleQueryAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterBookSingleQueryLvItem;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.mode.Tools;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class MeterBookQueryActivity extends Activity {
    private ImageView back;
    private ListView listView;
    private TextView selectMeterBook;
    private EditText editMeterNum;
    private TextView clear, query;
    private List<MeterBookSingleQueryLvItem> bookLists = new ArrayList<>();
    private ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();
    private MeterBookSingleQueryAdapter adapter;
    private MeterBookSingleQueryLvItem item;
    private SQLiteDatabase db;  //数据库
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    private String bookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_book_query);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.list_view);
        selectMeterBook = (TextView) findViewById(R.id.select_meter_book);
        editMeterNum = (EditText) findViewById(R.id.edit_meter_num);
        clear = (TextView) findViewById(R.id.clear);
        query = (TextView) findViewById(R.id.query);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterBookQueryActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = MeterBookQueryActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        selectMeterBook.setOnClickListener(onClickListener);
        clear.setOnClickListener(onClickListener);
        query.setOnClickListener(onClickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = (MeterBookSingleQueryLvItem) adapter.getItem(position);
                bookID = item.getBookId();
                selectMeterBook.setText(item.getBookName());
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    MeterBookQueryActivity.this.finish();
                    finish();
                    break;
                case R.id.select_meter_book:
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            getBookInfo();
                        }
                    }.start();
                    break;
                case R.id.clear:
                    editMeterNum.setText("");
                    break;
                case R.id.query:
                    if (!selectMeterBook.getText().toString().equals("单击选择")) {
                        if (!editMeterNum.getText().toString().equals("")) {
                            Tools.hideSoftInput(MeterBookQueryActivity.this, editMeterNum);
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    queryMeterUserInfo(bookID, editMeterNum.getText().toString());  //根据抄表序号查询用户信息
                                }
                            }.start();
                        } else {
                            Toast.makeText(MeterBookQueryActivity.this, "请输入抄表序号！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MeterBookQueryActivity.this, "请先选择抄表本！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //查询所有表册信息
    public void getBookInfo() {
        bookLists.clear();
        Cursor cursor = db.rawQuery("select * from MeterBook where login_user_id=? and fileName=?", new String[]{sharedPreferences_login.getString("userId", ""), sharedPreferences.getString("currentFileName", "")});//查询并获得游标
        Log.i("MeterBookQueryActivity", "所有表册ID个数为：" + cursor.getCount());
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            MeterBookSingleQueryLvItem item = new MeterBookSingleQueryLvItem();
            item.setBookName(cursor.getString(cursor.getColumnIndex("bookName")));
            item.setBookId(cursor.getString(cursor.getColumnIndex("bookId")));
            bookLists.add(item);
        }
        handler.sendEmptyMessage(0);
        cursor.close();
    }

    /**
     * 查询抄表用户信息
     */
    public void queryMeterUserInfo(String booID, String orderNumb) {
        userLists.clear();
        Cursor cursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? and meter_order_number=?", new String[]{sharedPreferences_login.getString("userId", ""), sharedPreferences.getString("currentFileName", ""), booID, orderNumb});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(1);
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
        handler.sendEmptyMessage(2);
        cursor.close();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter = new MeterBookSingleQueryAdapter(MeterBookQueryActivity.this, bookLists);
                    listView.setAdapter(adapter);
                    MyAnimationUtils.viewGroupOutAnimation(MeterBookQueryActivity.this, listView, 0.1F);
                    break;
                case 1:
                    Toast.makeText(MeterBookQueryActivity.this, "未查到用户信息，请您核对序号是否正确！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Intent intent = new Intent(MeterBookQueryActivity.this, MeterUserQueryResultActivity.class);
                    intent.putParcelableArrayListExtra("meter_user_info", userLists);
                    startActivity(intent);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MeterBookQueryActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterBookQueryActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterBookQueryActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterBookQueryActivity.this.getWindow().setAttributes(lp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
