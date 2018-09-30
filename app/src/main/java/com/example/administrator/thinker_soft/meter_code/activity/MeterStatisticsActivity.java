package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterFileSelectListAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterSingleSelectItem;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MeterStatisticsActivity extends Activity {
    private ImageView back;
    private TextView allUserNumberTv, doneNumberTv, undoneNumberTv, meterCountTv, finishRateTv, fileName, bookName;
    private int allUserNumber = 0, doneNumber = 0, undoneNumber = 0;
    private Double meterCount = 0.0;
    private String finishRate;
    private CardView allUserStatistics, singleStatistics;
    private SQLiteDatabase db;  //数据库
    private Cursor bookAllCursor, bookSingleCursor;
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    private List<MeterSingleSelectItem> bookList = new ArrayList<>();  //存放表册ID的集合
    private LayoutInflater layoutInflater;
    private PopupWindow bookWindow;
    private View bookView;
    private ListView bookListview;
    private LinearLayout rootLinearlayout;
    private MeterSingleSelectItem bookItem;
    private MeterFileSelectListAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_statistics);
        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        fileName = (TextView) findViewById(R.id.file_name);
        bookName = (TextView) findViewById(R.id.book_name);
        allUserNumberTv = (TextView) findViewById(R.id.all_user_number);
        doneNumberTv = (TextView) findViewById(R.id.done_number);
        undoneNumberTv = (TextView) findViewById(R.id.undone_number);
        meterCountTv = (TextView) findViewById(R.id.meter_number);
        finishRateTv = (TextView) findViewById(R.id.finish_rate);
        allUserStatistics = (CardView) findViewById(R.id.all_user_statistics);
        singleStatistics = (CardView) findViewById(R.id.single_statistics);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterStatisticsActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = MeterStatisticsActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        if (!"".equals(sharedPreferences.getString("currentFileName", ""))) {
            fileName.setText(sharedPreferences.getString("currentFileName", ""));
            bookName.setText("所有抄表本");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    getAllUserData(sharedPreferences.getString("currentFileName", ""));  //获取所有统计数据
                }
            }.start();
        } else {
            fileName.setText("无");
            bookName.setText("无");
            Toast.makeText(MeterStatisticsActivity.this, "请先完成文件选择！", Toast.LENGTH_SHORT).show();
        }
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        allUserStatistics.setOnClickListener(clickListener);
        singleStatistics.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.all_user_statistics:
                    if (!"".equals(sharedPreferences.getString("currentFileName", ""))) {
                        bookName.setText("所有抄表本");
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                getAllUserData(sharedPreferences.getString("currentFileName", ""));  //获取所有统计数据
                            }
                        }.start();
                    } else {
                        Toast.makeText(MeterStatisticsActivity.this, "请先完成文件选择！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.single_statistics:
                    if (!"".equals(sharedPreferences.getString("currentFileName", ""))) {
                        showBookSelectWindow();
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                getBookInfo();
                            }
                        }.start();
                    } else {
                        Toast.makeText(MeterStatisticsActivity.this, "请先完成文件选择！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 抄表本选择窗口
     */
    public void showBookSelectWindow() {
        layoutInflater = LayoutInflater.from(MeterStatisticsActivity.this);
        bookView = layoutInflater.inflate(R.layout.popupwindow_meter_single_select, null);
        bookWindow = new PopupWindow(bookView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        TextView back = (TextView) bookView.findViewById(R.id.back);
        bookListview = (ListView) bookView.findViewById(R.id.list_view);
        TextView tips = (TextView) bookView.findViewById(R.id.tips);
        //设置点击事件
        tips.setText("请选择抄表本");
        bookListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                bookItem = (MeterSingleSelectItem) bookAdapter.getItem(position);
                Log.i("meterHomePage", "当前点击的item为：" + bookItem.getName());
                bookWindow.dismiss();
                bookName.setText(bookItem.getName());
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        getSingleBookUserData(sharedPreferences.getString("currentFileName", ""), bookItem.getID());
                    }
                }.start();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookWindow.dismiss();
            }
        });
        bookWindow.update();
        bookWindow.setFocusable(true);
        bookWindow.setTouchable(true);
        bookWindow.setOutsideTouchable(true);
        bookWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        bookWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        bookWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        bookWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MeterStatisticsActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterStatisticsActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterStatisticsActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterStatisticsActivity.this.getWindow().setAttributes(lp);
    }

    /**
     * 查询表册所有用户数据信息
     *
     * @param fileName
     */
    public void getAllUserData(String fileName) {
        doneNumber = 0;
        meterCount = 0.0;
        allUserNumber = 0;
        bookAllCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName});//查询并获得游标
        allUserNumber = bookAllCursor.getCount();
        if (bookAllCursor.getCount() == 0) {
            return;
        }
        while (bookAllCursor.moveToNext()) {
            if (bookAllCursor.getString(bookAllCursor.getColumnIndex("meterState")).equals("true")) {
                doneNumber++;
                String s = bookAllCursor.getString(bookAllCursor.getColumnIndex("this_month_dosage"));
//                if (s.contains(".")){
//                    s=s.substring(0,s.indexOf("."));
//                }
                meterCount += Double.valueOf(s);
            }
        }
        getStatisticsData(); //获取统计数据
        handler.sendEmptyMessage(0);
    }

    //查询抄表本信息
    public void getBookInfo() {
        bookList.clear();
        Cursor cursor;
        if (sharedPreferences.getBoolean("show_temp_data", false)) {   //显示演示数据
            cursor = db.rawQuery("select * from MeterBook where login_user_id=? and fileName=?", new String[]{"0", sharedPreferences.getString("currentFileName", "")});//查询并获得游标
        } else {
            cursor = db.rawQuery("select * from MeterBook where login_user_id=? and fileName=?", new String[]{sharedPreferences_login.getString("userId", ""), sharedPreferences.getString("currentFileName", "")});//查询并获得游标
        }
        Log.i("meterHomePage", "抄表本个数为：" + cursor.getCount());
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            MeterSingleSelectItem item = new MeterSingleSelectItem();
            item.setName(cursor.getString(cursor.getColumnIndex("bookName")));
            item.setID(cursor.getString(cursor.getColumnIndex("bookId")));
            bookList.add(item);
        }
        handler.sendEmptyMessage(1);
        cursor.close();
    }

    /**
     * 获取其他统计数据
     */
    public void getStatisticsData() {
        undoneNumber = allUserNumber - doneNumber;
        if (allUserNumber != 0) {
            double haveDone = (double) doneNumber * 100;
            double totalCount = (double) allUserNumber;
            double finishingRate = haveDone / totalCount;  //完成率
            Log.i("getTotalUserNumber===>", "完成率=" + finishingRate + "%");
            DecimalFormat df = new DecimalFormat("0.0");
            finishRate = df.format(finishingRate);
            Log.i("getTotalUserNumber===>", "完成率=" + finishRate + "%");
        } else {
            finishRate = "0.0";
        }
    }

    /**
     * 查询单个表册用户数据信息(传入表册ID)
     *
     * @param bookID
     */
    public void getSingleBookUserData(String fileName, String bookID) {
        doneNumber = 0;
        meterCount = 0.0;
        allUserNumber = 0;
        bookSingleCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游标
        allUserNumber = bookSingleCursor.getCount();
        if (bookSingleCursor.getCount() == 0) {
            return;
        }
        while (bookSingleCursor.moveToNext()) {
            if (bookSingleCursor.getString(bookSingleCursor.getColumnIndex("meterState")).equals("true")) {
                doneNumber++;
                meterCount += Double.valueOf(bookSingleCursor.getString(bookSingleCursor.getColumnIndex("this_month_dosage")));
            }
        }
        getStatisticsData(); //获取其他统计数据
        handler.sendEmptyMessage(0);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setStatisticsData();  //设置统计数据
                    break;
                case 1:
                    bookAdapter = new MeterFileSelectListAdapter(MeterStatisticsActivity.this, bookList, 0);
                    bookListview.setAdapter(bookAdapter);
                    MyAnimationUtils.viewGroupOutAnimation(MeterStatisticsActivity.this, bookListview, 0.1F);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 设置统计数据
     */
    public void setStatisticsData() {
        allUserNumberTv.setText(String.valueOf(allUserNumber));
        doneNumberTv.setText(String.valueOf(doneNumber));
        undoneNumberTv.setText(String.valueOf(undoneNumber));
        meterCountTv.setText(String.valueOf(meterCount));
        finishRateTv.setText(String.valueOf(finishRate));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //cursor游标操作完成以后,一定要关闭
        if (bookAllCursor != null) {
            bookAllCursor.close();
        }
        if (bookSingleCursor != null) {
            bookSingleCursor.close();
        }
        db.close();
    }
}
