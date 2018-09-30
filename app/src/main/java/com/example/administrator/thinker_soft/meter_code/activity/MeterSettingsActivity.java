package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.db.MySQLiteOpenHelpers;
import com.example.administrator.thinker_soft.meter_code.sk.mode.DatabaseContext;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolManager;
import com.example.administrator.thinker_soft.meter_code.sk.widget.UIHandler;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

/**
 * 设置
 *  MeterSettingsActivity class
 * @author g
 * @date 2018/8/2.
 */

public class MeterSettingsActivity extends Activity {
    private ImageView back;
    private CardView bluetooth, fileDelete, setPageCount/*,printNote*/, pattern, downloadMode,dataWrite;
    /**数据库*/
    private SQLiteDatabase db;
    private SQLiteDatabase add_db;
    /**存储*/
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    /**更新Ui*/
    private UIMyHandler myHandler = new UIMyHandler(this);
    /**总数据*/
    private static final int MESSAGE_DATA = 1;
    private static final int MESSAGE_DATA_NO = 2;
    ProgressDialog progressDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_settings);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            MeterSettingsActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        bluetooth = (CardView) findViewById(R.id.bluetooth);
        fileDelete = (CardView) findViewById(R.id.file_delete);
        setPageCount = (CardView) findViewById(R.id.set_page_count);
        /*printNote = (CardView) findViewById(R.id.print_note);*/
        pattern = (CardView) findViewById(R.id.pattern);
        downloadMode = (CardView) findViewById(R.id.cd_dataDownloadMode);
        dataWrite = (CardView) findViewById(R.id.cd_dataWrite);

        if (getIntent().getStringExtra("anJian")!=null){
            pattern.setVisibility(View.GONE);
            downloadMode.setVisibility(View.GONE);
            dataWrite.setVisibility(View.GONE);
        }

    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterSettingsActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();

        DatabaseContext dbContext=new DatabaseContext(MeterSettingsActivity.this);
         MySQLiteOpenHelpers helpers = new MySQLiteOpenHelpers(dbContext,1);
        add_db=helpers.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);

    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        bluetooth.setOnClickListener(onClickListener);
        fileDelete.setOnClickListener(onClickListener);
        setPageCount.setOnClickListener(onClickListener);
        /*printNote.setOnClickListener(onClickListener);*/
        pattern.setOnClickListener(onClickListener);
        downloadMode.setOnClickListener(onClickListener);
        dataWrite.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.back:
                    MeterSettingsActivity.this.finish();
                    break;
                case R.id.bluetooth:
                    intent = new Intent(MeterSettingsActivity.this, BluetoothActivity.class);
                    startActivity(intent);
                    break;
                case R.id.file_delete:
                    intent = new Intent(MeterSettingsActivity.this, MeterDeleteFileActivity.class);
                    startActivity(intent);
                    break;
                case R.id.set_page_count:
                    intent = new Intent(MeterSettingsActivity.this, MeterPageCountSettingsActivity.class);
                    startActivity(intent);
                    break;
                /*case R.id.print_note:
                    intent = new Intent(MeterSettingsActivity.this, MeterPrintNoteActivity.class);
                    startActivity(intent);
                    break;*/
                case R.id.pattern:
                    intent = new Intent(MeterSettingsActivity.this, MeterPatternActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cd_dataDownloadMode:
                    intent = new Intent(MeterSettingsActivity.this, MeterDownloadModeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cd_dataWrite:
                    progressDialog = ProgressDialog.show(MeterSettingsActivity.this, "","加载中...");
                    //写入SD卡
                    ThreadPoolManager.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                        getTotalUserAll();
                        }
                    });
                default:
                    break;
            }
        }
    };

        /**
     * 查询抄表用户总数
     */
    public void getTotalUserAll() {
        int i=1;
        add_db.delete("MeterUsers", null, null);  //删除User表中当前用户的所有数据（官方推荐方法）
        //设置id从1开始（sqlite默认id从1开始），若没有这一句，id将会延续删除之前的id
        add_db.execSQL("update sqlite_sequence set seq=0 where name='MeterUsers'");
        Cursor userLimitCursor;
        if (sharedPreferences.getBoolean("show_temp_data", false)) {
            //未登录
            userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=?", new String[]{"0"});//查询并获得游标
        } else {
            userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=?", new String[]{sharedPreferences_login.getString("userId", "")});//查询并获得游标
        }
        //如果游标为空，则显示没有数据
        Log.i("MeterUserLVActivity", "总的查询到" + userLimitCursor.getCount() + "条数据！");
        if (userLimitCursor.getCount() == 0) {
            myHandler.sendEmptyMessage(MESSAGE_DATA_NO);
            userLimitCursor.close();
            return;
        }
        while (userLimitCursor.moveToNext()) {
            //插入数据
            insertMeterUserData(userLimitCursor,i);
            i++;
        }
        userLimitCursor.close();
        myHandler.sendEmptyMessage(MESSAGE_DATA);

    }
    private void insertMeterUserData(Cursor userLimitCursor,int i) {
        ContentValues values = new ContentValues();
        values.put("login_user_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("login_user_id")));          //登录人ID
        values.put("meter_reader_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_reader_id")));    //抄表员ID
        values.put("meter_reader_name",  userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_reader_name")));        //抄表员名称
        values.put("meter_date", userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_date")));                                                      //抄表时间
        values.put("user_phone", userLimitCursor.getString(userLimitCursor.getColumnIndex("user_phone")));                    //用户电话
        values.put("user_amount", userLimitCursor.getString(userLimitCursor.getColumnIndex("user_amount")));                      //用户余额
        values.put("meter_degrees", userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_degrees")));             //上月读数
        values.put("meter_number", userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_number")));                //表编号
        values.put("arrearage_months", userLimitCursor.getString(userLimitCursor.getColumnIndex("arrearage_months")));                      //欠费月数
        values.put("mix_state", userLimitCursor.getString(userLimitCursor.getColumnIndex("mix_state")));                     //混合使用状态（0正常  1混合）
        values.put("meter_order_number",userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_order_number")));           //抄表序号
        values.put("arrearage_amount", userLimitCursor.getString(userLimitCursor.getColumnIndex("arrearage_amount")));          //欠费金额
        values.put("area_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("area_id")));                         //抄表本分区ID
        values.put("area_name",userLimitCursor.getString(userLimitCursor.getColumnIndex("area_name")));                      //抄表本分区名称
        values.put("user_name", userLimitCursor.getString(userLimitCursor.getColumnIndex("user_name")));                      //用户名
        values.put("last_month_dosage", userLimitCursor.getString(userLimitCursor.getColumnIndex("last_month_dosage")));                //上月用量
        values.put("property_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("property_id")));               //性质ID
        values.put("property_name", userLimitCursor.getString(userLimitCursor.getColumnIndex("property_name")));            //性质名称
        values.put("user_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("user_id")));                          //用户ID
        values.put("book_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("book_id")));                         //抄表本ID
        values.put("float_range", userLimitCursor.getString(userLimitCursor.getColumnIndex("float_range")));                      //浮动范围
        values.put("meterState", userLimitCursor.getString(userLimitCursor.getColumnIndex("meterState")));                                                     //抄表状态
        values.put("dosage_change", userLimitCursor.getString(userLimitCursor.getColumnIndex("dosage_change")));              //更换量
        values.put("user_address", userLimitCursor.getString(userLimitCursor.getColumnIndex("user_address")));                //用户地址
        values.put("start_dosage", userLimitCursor.getString(userLimitCursor.getColumnIndex("start_dosage")));                    //启用量
        values.put("old_user_id",userLimitCursor.getString(userLimitCursor.getColumnIndex("old_user_id")));                  //用户老编号
        values.put("book_name", userLimitCursor.getString(userLimitCursor.getColumnIndex("book_name")));                      //抄表本名称
        values.put("meter_model", userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_model")));                   //表型号
        values.put("rubbish_cost", userLimitCursor.getString(userLimitCursor.getColumnIndex("rubbish_cost")));                  //垃圾费
        values.put("remission", userLimitCursor.getString(userLimitCursor.getColumnIndex("remission")));                     //加减量
        values.put("locationAddress", userLimitCursor.getString(userLimitCursor.getColumnIndex("locationAddress")));                                                //定位地址
        values.put("file_name",userLimitCursor.getString(userLimitCursor.getColumnIndex("file_name")));                            //本地文件名
        values.put("uploadState", userLimitCursor.getString(userLimitCursor.getColumnIndex("uploadState")));                                                     //上传状态
        values.put("n_state_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("n_state_id")));                                                     //估录
        values.put("opened_remark", userLimitCursor.getString(userLimitCursor.getColumnIndex("opened_remark")));
        values.put("user_remark", userLimitCursor.getString(userLimitCursor.getColumnIndex("user_remark")));
        values.put("n_user_state",userLimitCursor.getString(userLimitCursor.getColumnIndex("n_user_state")));
        //下面这些个字段抄表完成后需上传
        values.put("this_month_dosage", userLimitCursor.getString(userLimitCursor.getColumnIndex("this_month_dosage")));                                               //本月用量
        values.put("this_month_end_degree", userLimitCursor.getString(userLimitCursor.getColumnIndex("this_month_end_degree")));                                               //本月止度
        values.put("n_jw_x", userLimitCursor.getString(userLimitCursor.getColumnIndex("n_jw_x")));                                                        //纬度
        values.put("n_jw_y", userLimitCursor.getString(userLimitCursor.getColumnIndex("n_jw_y")));                                                        //经度
        values.put("d_jw_time", userLimitCursor.getString(userLimitCursor.getColumnIndex("d_jw_time")));                                                     //抄表时间
        values.put("n_state_remark",userLimitCursor.getString(userLimitCursor.getColumnIndex("n_state_remark")));                                                     //抄表时间
        add_db.insert("MeterUsers", null, values);
        Log.e("insertMeterUserData", "用户数据插入成功"+i);
    }
    /**
     * 弱引用hander
     */
    private static class UIMyHandler extends UIHandler<MeterSettingsActivity> {

        public UIMyHandler(MeterSettingsActivity cls) {
            super(cls);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MeterSettingsActivity activity = ref.get();
            if (activity != null) {
                if (activity.isFinishing()) {
                    return;
                }
                switch (msg.what) {
                    case MESSAGE_DATA: {
                        activity.dissDialog();
                        Toast.makeText(activity,"写入成功",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case MESSAGE_DATA_NO: {
                        activity.dissDialog();
                       Toast.makeText(activity,"写入失败",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:
                        break;
                }

            }
        }
    }



    /**
     * 关闭弹出框
     */
    private void dissDialog(){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dissDialog();
        db.close();
        add_db.close();
    }
}
