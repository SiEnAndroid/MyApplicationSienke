package com.example.administrator.thinker_soft.Security_check.activity;

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
import com.example.administrator.thinker_soft.Security_check.adapter.TaskChooseAdapter;
import com.example.administrator.thinker_soft.Security_check.model.TaskChoose;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
public class SecurityStatisticsActivity extends Activity {
    private ImageView securityStatisticsBack;
    private TextView taskName,checkedNumber, totalNumber, notCheckedNumber, finishRate, problemCheckedNumber;
    private SharedPreferences sharedPreferences,sharedPreferences_login;
    private CardView userStatistics,taskStatistics;
    private int notChecked = 0;      //未安检的户数
    private SQLiteDatabase db;       //数据库
    private MySqliteHelper helper;   //数据库帮助类
    private int totalUserNumber;     //总户数
    private int checkedUserNumber;   //已检户数
    private int problemNumber;       //存在问题户数
    private LayoutInflater inflater;  //转换器
    private View taskView;
    private PopupWindow popupWindow;
    private ListView listView;
    private LinearLayout rootLinearlayout,noData;
    private List<TaskChoose> taskChooseList = new ArrayList<>();
    private TaskChooseAdapter adapter;   //适配器
    private TaskChoose item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_statistics);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setViewClickListener();//点击事件
    }

    //绑定控件
    private void bindView() {
        securityStatisticsBack = (ImageView) findViewById(R.id.security_statistics_back);
        taskName = (TextView) findViewById(R.id.task_name);
        checkedNumber = (TextView) findViewById(R.id.checked_number);
        totalNumber = (TextView) findViewById(R.id.total_number);
        notCheckedNumber = (TextView) findViewById(R.id.not_checked_number);
        finishRate = (TextView) findViewById(R.id.finish_rate);
        problemCheckedNumber = (TextView) findViewById(R.id.problem_checked_number);
        userStatistics= (CardView) findViewById(R.id.user_statistics);
        taskStatistics= (CardView) findViewById(R.id.task_statistics);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        taskName.setText("所有用户");
        helper = new MySqliteHelper(SecurityStatisticsActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = SecurityStatisticsActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId","")+"data", Context.MODE_PRIVATE);
        new Thread(){
            @Override
            public void run() {
                getTotalData(sharedPreferences_login.getString("userId",""));   //获取所有户数统计
            }
        }.start();
    }

    //点击事件
    private void setViewClickListener() {
        securityStatisticsBack.setOnClickListener(onClickListener);
        userStatistics.setOnClickListener(onClickListener);
        taskStatistics.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.security_statistics_back:
                    SecurityStatisticsActivity.this.finish();
                    break;
                case R.id.user_statistics:
                    taskName.setText("所有用户");
                    new Thread(){
                        @Override
                        public void run() {
                            getTotalData(sharedPreferences_login.getString("userId",""));   //获取所有户数统计
                        }
                    }.start();
                    break;
                case R.id.task_statistics:
                    taskChooseWindow();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            getTaskData(sharedPreferences_login.getString("userId", ""));//读取下载到本地的任务数据
                        }
                    }.start();
                    break;
            }
        }
    };

    //读取下载到本地的任务数据
    public void getTaskData(String loginUserId) {
        taskChooseList.clear();
        Cursor cursor = db.rawQuery("select * from Task where loginUserId=?", new String[]{loginUserId});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(1);
            return;
        }
        while (cursor.moveToNext()) {
            TaskChoose taskChoose = new TaskChoose();
            taskChoose.setTaskName(cursor.getString(cursor.getColumnIndex("taskName")));
            taskChoose.setTaskNumber(cursor.getString(cursor.getColumnIndex("taskId")));
            taskChoose.setCheckType(cursor.getString(cursor.getColumnIndex("securityType")));
            taskChoose.setTotalUserNumber(cursor.getString(cursor.getColumnIndex("totalCount")));
            taskChoose.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
            taskChoose.setRestCount("(" + cursor.getString(cursor.getColumnIndex("restCount")) + ")");
            taskChooseList.add(taskChoose);
        }
        //cursor游标操作完成以后,一定要关闭
        cursor.close();
        handler.sendEmptyMessage(2);
    }

    /**
     * 选择任务窗口
     */
    public void taskChooseWindow() {
        inflater = LayoutInflater.from(SecurityStatisticsActivity.this);
        taskView = inflater.inflate(R.layout.poupwindow_task_select, null);
        popupWindow = new PopupWindow(taskView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        listView = (ListView) taskView.findViewById(R.id.list_view);
        noData = (LinearLayout) taskView.findViewById(R.id.no_data);
        LinearLayout confirmLayout = (LinearLayout) taskView.findViewById(R.id.confirm_layout);
        noData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {           //listview点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                item = (TaskChoose) adapter.getItem(position);
                taskName.setText(item.getTaskName());
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        getPartData(item.getTaskNumber(),sharedPreferences_login.getString("userId","")); //获取单个任务统计数据
                    }
                }.start();
            }
        });
        confirmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = SecurityStatisticsActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            SecurityStatisticsActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            SecurityStatisticsActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        SecurityStatisticsActivity.this.getWindow().setAttributes(lp);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(SecurityStatisticsActivity.this,"您还没下载数据，请您下载后统计！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    noData.setVisibility(View.VISIBLE);
                    Toast.makeText(SecurityStatisticsActivity.this, "您还没有任务哦，快去下载吧！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    adapter = new TaskChooseAdapter(SecurityStatisticsActivity.this, taskChooseList);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    break;
                case 3:
                    Toast.makeText(SecurityStatisticsActivity.this,"当前任务统计失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    showUserData();
                    break;
                case 5:
                    Toast.makeText(SecurityStatisticsActivity.this,"本地还没有数据哦！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    //获取所有存在问题户数
    public void getTotalData(String loginUserId){
        totalUserNumber = 0;
        checkedUserNumber = 0;
        problemNumber = 0;
        Cursor  cursor = db.rawQuery("select * from User where loginUserId=?", new String[]{loginUserId});//查询并获得游标
        totalUserNumber = cursor.getCount(); //获取所有任务总户数
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(5);
            return;
        }
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("true")) {
                checkedUserNumber++;
            }
            if (cursor.getString(cursor.getColumnIndex("ifPass")).equals("false")) {
                problemNumber++;
            }
        }
        Log.i("getCheckedNumber===>", "总的存在问题" + problemNumber + "户");
        cursor.close(); //游标关闭
        handler.sendEmptyMessage(4);
    }

    //获取已安检户数
    public void getPartData(String taskId,String loginUserId){
        totalUserNumber = 0;
        checkedUserNumber = 0;
        problemNumber = 0;
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginUserId=?", new String[]{taskId,loginUserId});//查询并获得游标
        totalUserNumber = cursor.getCount(); //获取所有任务总户数
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(3);
            return;
        }
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("true")) {
                checkedUserNumber++;
            }
            if (cursor.getString(cursor.getColumnIndex("ifPass")).equals("false")) {
                problemNumber++;
            }
        }
        Log.i("getCheckedNumber===>", "已安检" + checkedUserNumber + "户");
        Log.i("getCheckedNumber===>", "部分存在问题" + problemNumber + "户");
        cursor.close(); //游标关闭
        handler.sendEmptyMessage(4);
    }

    //总用户统计并显示数据
    public void showUserData (){
        Log.i("getTaskUserNumber===>", "所有任务总户数=" + totalUserNumber + "户");
        totalNumber.setText(String.valueOf(totalUserNumber));
        checkedNumber.setText(String.valueOf(checkedUserNumber));
        notChecked = totalUserNumber - checkedUserNumber;
        notCheckedNumber.setText(String.valueOf(notChecked));
        if (totalUserNumber != 0) {
            double checkedNumber = (double) checkedUserNumber* 100;
            double totalCount = (double) totalUserNumber;
            double finishingRate = checkedNumber/totalCount;  //完成率
            Log.i("getTotalUserNumber===>", "完成率=" + finishingRate + "%");
            DecimalFormat df = new DecimalFormat("0.0");
            String result = df.format(finishingRate);
            Log.i("getTotalUserNumber===>", "完成率=" + result + "%");
            finishRate.setText(result);
        } else {
            finishRate.setText("0.0");
        }
        problemCheckedNumber.setText(String.valueOf(problemNumber));
    }
}
