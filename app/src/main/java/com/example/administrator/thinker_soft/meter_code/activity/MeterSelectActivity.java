package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.MeterAreaViewHolder;
import com.example.administrator.thinker_soft.meter_code.model.MeterBookViewHolder;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.meter_code.model.AreaInfo;
import com.example.administrator.thinker_soft.meter_code.model.BookInfo;
import com.example.administrator.thinker_soft.meter_code.adapter.AreaDataAdapter;
import com.example.administrator.thinker_soft.meter_code.adapter.BookDataAdapter;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeterSelectActivity extends Activity {
    private ImageView back;
    private ListView booklistView;
    private ListView arealistView;
    private BookDataAdapter bookAdapter;
    private AreaDataAdapter areaAdapter;
    private TextView selectBookNumber, selectAreaNumber,save,noAreaData,noBookData;
    private TextView bookSelectAll, bookReverse, bookSelectCancel, areaSelectAll, areaReverse, areaSelectCancel;
    private RelativeLayout areaSelectRelative,bookSelectRelative;
    private List<BookInfo> bookInfoList = new ArrayList<>();      //抄表本集合
    private List<AreaInfo> areaInfoList = new ArrayList<>();   //抄表分区集合
    private int bookNum = 0; // 记录抄表本选中的条目数量
    private int areaNum = 0; // 记录抄表分区选中的条目数量
    private HashMap<String, Integer> bookIDMap = new HashMap<String, Integer>();  //用于保存选中的抄表本ID
    private HashMap<String, Integer> areaIDMap = new HashMap<String, Integer>();  //用于保存选中的抄表分区ID
    private ArrayList<Integer> bookIDList = new ArrayList<>();  //用于保存抄表本ID值
    private ArrayList<Integer> areaIDLsit = new ArrayList<>();  //用于保存抄表分区ID值
    private LinearLayout rootLinearlayout;
    private SQLiteDatabase db;  //数据库
    private Cursor areaCursor,bookCursor;
    private SharedPreferences sharedPreferences_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_select);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        save = (TextView) findViewById(R.id.save);
        noAreaData = (TextView) findViewById(R.id.no_area_data);
        noBookData = (TextView) findViewById(R.id.no_book_data);
        selectBookNumber = (TextView) findViewById(R.id.select_book_number);
        selectAreaNumber = (TextView) findViewById(R.id.select_area_number);
        arealistView = (ListView) findViewById(R.id.meter_area_lv);
        booklistView = (ListView) findViewById(R.id.meter_book_lv);
        bookSelectAll = (TextView) findViewById(R.id.book_select_all);
        bookReverse = (TextView) findViewById(R.id.book_reverse);
        bookSelectCancel = (TextView) findViewById(R.id.book_select_cancel);
        areaSelectAll = (TextView) findViewById(R.id.area_select_all);
        areaReverse = (TextView) findViewById(R.id.area_reverse);
        areaSelectCancel = (TextView) findViewById(R.id.area_select_cancel);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
        areaSelectRelative = (RelativeLayout) findViewById(R.id.area_select_relative);
        bookSelectRelative = (RelativeLayout) findViewById(R.id.book_select_relative);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterSelectActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        new Thread(){
            @Override
            public void run() {
                super.run();
                //获取分区和抄表本的数据并显示
                getAreaData();
                handler.sendEmptyMessage(0);
                getbookData();
                handler.sendEmptyMessage(1);
            }
        }.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    areaAdapter = new AreaDataAdapter(MeterSelectActivity.this,areaInfoList);
                    areaAdapter.notifyDataSetChanged();
                    arealistView.setAdapter(areaAdapter);
                    break;
                case 1:
                    bookAdapter = new BookDataAdapter(MeterSelectActivity.this,bookInfoList);
                    bookAdapter.notifyDataSetChanged();
                    booklistView.setAdapter(bookAdapter);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        save.setOnClickListener(clickListener);
        arealistView.setOnItemClickListener(new OnItemClickListener() {   //抄表分区item点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeterAreaViewHolder holder = (MeterAreaViewHolder) view.getTag();
                holder.checkedState.toggle();
                AreaDataAdapter.getIsCheck().put(position, holder.checkedState.isChecked());
                if (holder.checkedState.isChecked()) {
                    areaNum++;
                } else {
                    areaNum--;
                }
                //TextView显示
                selectAreaNumber.setText("(" + areaNum + ")");
            }
        });
        booklistView.setOnItemClickListener(new OnItemClickListener() {   //抄表本item点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeterBookViewHolder holder = (MeterBookViewHolder) view.getTag();
                holder.checkedState.toggle();
                BookDataAdapter.getIsCheck().put(position, holder.checkedState.isChecked());
                // 调整选定条目
                if (holder.checkedState.isChecked()) {
                    bookNum++;
                } else {
                    bookNum--;
                }
                //TextView显示
                selectBookNumber.setText("(" + bookNum + ")");
            }
        });
        bookSelectAll.setOnClickListener(clickListener);
        bookReverse.setOnClickListener(clickListener);
        bookSelectCancel.setOnClickListener(clickListener);
        areaSelectAll.setOnClickListener(clickListener);
        areaReverse.setOnClickListener(clickListener);
        areaSelectCancel.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    MeterSelectActivity.this.finish();
                    break;
                case R.id.save:
                    saveBookInfo();  //保存选中的抄表本ID信息
                    saveAreaInfo();  //保存选中的抄表分区ID信息
                    break;
                case R.id.book_select_all:
                    bookSelectAll();
                    break;
                case R.id.book_reverse:
                    bookReverse();
                    break;
                case R.id.book_select_cancel:
                    bookSelectCancle();
                    break;
                case R.id.area_select_all:
                    areaSelectAll();
                    break;
                case R.id.area_reverse:
                    areaReverse();
                    break;
                case R.id.area_select_cancel:
                    areaSelectCancle();
                    break;
                default:
                    break;
            }
        }
    };

    // 刷新抄表本listview和TextView的显示
    private void bookDataChanged() {
        // 通知listView刷新
        bookAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        selectBookNumber.setText("(" + bookNum + ")");
    }

    // 刷新抄表分区listview和TextView的显示
    private void areaDataChanged() {
        // 通知listView刷新
        areaAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        selectAreaNumber.setText("(" + areaNum + ")");
    }

    //抄表本全选
    public void bookSelectAll() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            BookDataAdapter.getIsCheck().put(i, true);
        }
        bookNum = bookInfoList.size();
        bookDataChanged();
    }

    //抄表本反选
    public void bookReverse() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookDataAdapter.getIsCheck().put(i, false);
                bookNum--;//数量减一
            } else {
                BookDataAdapter.getIsCheck().put(i, true);
                bookNum++;//数量加一
            }
        }
        bookDataChanged();
    }

    //抄表本取消选择
    public void bookSelectCancle() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookDataAdapter.getIsCheck().put(i, false);
                bookNum--; //数量减一
            }
        }
        bookDataChanged();
    }

    //抄表分区全选
    public void areaSelectAll() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            AreaDataAdapter.getIsCheck().put(i, true);
        }
        areaNum = areaInfoList.size();
        areaDataChanged();
    }

    //抄表分区反选
    public void areaReverse() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            if (AreaDataAdapter.getIsCheck().get(i)) {
                AreaDataAdapter.getIsCheck().put(i, false);
                areaNum--;
            } else {
                AreaDataAdapter.getIsCheck().put(i, true);
                areaNum++;
            }
        }
        areaDataChanged();
    }

    //抄表分区取消选择
    public void areaSelectCancle() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            if (AreaDataAdapter.getIsCheck().get(i)) {
                AreaDataAdapter.getIsCheck().put(i, false);
                areaNum--;
            }
        }
        areaDataChanged();
    }

    //保存选中的抄表本ID信息
    public void saveBookInfo() {
        bookIDMap.clear();
        bookIDList.clear();
        for (int i = 0; i < bookInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookInfo bookInfo = bookInfoList.get((int) bookAdapter.getItemId(i));
                bookIDMap.put("bookID" + i, Integer.parseInt(bookInfo.getID()));
                Log.i("bookID=========>", "这次被勾选第" + i + "个，抄表本ID为：" + bookInfo.getID());
                bookIDList.add(i);
            }
        }
    }

    //保存选中的抄表分区ID信息
    public void saveAreaInfo() {
        areaIDMap.clear();
        areaIDLsit.clear();
        for (int i = 0; i < areaInfoList.size(); i++) {
            if (AreaDataAdapter.getIsCheck().get(i)) {
                AreaInfo areaInfo = areaInfoList.get((int) areaAdapter.getItemId(i));
                areaIDMap.put("areaID" + i, Integer.parseInt(areaInfo.getID()));
                Log.i("areaID=========>", "这次被勾选第" + i + "个，抄表分区ID为：" + areaInfo.getID());
                areaIDLsit.add(i);
            }
        }
    }

    //读取本地的抄表分区数据
    public void getAreaData() {
        areaInfoList.clear();
        areaCursor = db.rawQuery("select * from MeterArea where login_user_id=? order by areaId", new String[]{sharedPreferences_login.getString("userId","")});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        if (areaCursor.getCount() == 0) {
            if (noAreaData.getVisibility() == View.GONE) {
                noAreaData.setVisibility(View.VISIBLE);
            }
            areaSelectRelative.setVisibility(View.GONE);
            return;
        }
        if (noAreaData.getVisibility() == View.VISIBLE) {
            noAreaData.setVisibility(View.GONE);
        }
        areaSelectRelative.setVisibility(View.VISIBLE);
        while (areaCursor.moveToNext()) {
            AreaInfo areaInfo = new AreaInfo();
            areaInfo.setArea(areaCursor.getString(areaCursor.getColumnIndex("areaName")));
            areaInfo.setID(areaCursor.getString(areaCursor.getColumnIndex("areaId")));
            areaInfoList.add(areaInfo);
        }
    }

    //读取本地的抄表本数据
    public void getbookData() {
        bookInfoList.clear();
        bookCursor = db.rawQuery("select * from MeterBook where login_user_id=? order by bookId", new String[]{sharedPreferences_login.getString("userId","")});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        if (bookCursor.getCount() == 0) {
            if (noBookData.getVisibility() == View.GONE) {
                noBookData.setVisibility(View.VISIBLE);
            }
            bookSelectRelative.setVisibility(View.GONE);
            return;
        }
        if (noBookData.getVisibility() == View.VISIBLE) {
            noBookData.setVisibility(View.GONE);
        }
        bookSelectRelative.setVisibility(View.VISIBLE);
        while (bookCursor.moveToNext()) {
            BookInfo bookInfo = new BookInfo();
            bookInfo.setBOOK(bookCursor.getString(bookCursor.getColumnIndex("bookName")));
            bookInfo.setID(bookCursor.getString(bookCursor.getColumnIndex("bookId")));
            bookInfoList.add(bookInfo);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //cursor游标操作完成以后,一定要关闭
        if(areaCursor != null){
            areaCursor.close();
        }
        if(bookCursor != null){
            bookCursor.close();
        }
        db.close();
    }
}
