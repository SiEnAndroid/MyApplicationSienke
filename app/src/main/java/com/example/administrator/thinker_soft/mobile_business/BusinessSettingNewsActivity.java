package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

/**
 * Created by Administrator on 2017/6/28.
 */
public class BusinessSettingNewsActivity extends Activity {

    private ImageView back;
    private CheckBox slip, slip1, slip2;
    private Vibrator vibrator;
    private SQLiteDatabase db;  //数据库
    private SharedPreferences sharedPreferences_login;
    private Cursor cursor;
    private int totalCount;
    private int changed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_setting_news);

        bindView();//绑定控件
        defaultSetting();
        setOnClickListener();//点击事件
        queryOaUser();
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        slip = (CheckBox) findViewById(R.id.slip);
        slip1 = (CheckBox) findViewById(R.id.slip1);
        slip2 = (CheckBox) findViewById(R.id.slip2);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(BusinessSettingNewsActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
    }

    private void setOnClickListener() {
        back.setOnClickListener(clickListener);
        slip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        slip1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (slip1.isChecked()) {
                    MediaPlayer mp = new MediaPlayer();
                    try {
                        mp.setDataSource(BusinessSettingNewsActivity.this, RingtoneManager
                                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        mp.prepare();
                        mp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        slip2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (slip2.isChecked()) {
                    if (changed != 0) {
                        Log.i("onCheckedChanged", "震动开始");
                        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                        long[] pattern = {0, 500};
                        vibrator.vibrate(pattern, -1);
                    } else {
                        changed++;
                    }
                } else {
                    if (vibrator != null) {
                        vibrator.cancel();
                    }
                }
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    if (totalCount != 0) {
                        upDateOaUser();
                    } else {
                        insertOaUser();
                    }
                    finish();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        vibrator.cancel();
    }


    /**
     * 更新用户基础信息表
     */
    private void upDateOaUser() {
        Log.i("insertOaUser", "信息已更新");
        ContentValues values = new ContentValues();
        values.put("isNewMessage", slip.isChecked() + "");
        values.put("isRing", slip1.isChecked() + "");
        values.put("isShake", slip2.isChecked() + "");
        db.update("OaUser", values, "userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
    }

    /**
     * 将信息保存到用户基础信息表
     */
    private void insertOaUser() {
        Log.i("insertOaUser", "信息已保存");
        ContentValues values = new ContentValues();
        values.put("userId", sharedPreferences_login.getString("userId", ""));
        values.put("userName", sharedPreferences_login.getString("user_name", ""));
        values.put("isNewMessage", slip.isChecked() + "");
        values.put("isRing", slip1.isChecked() + "");
        values.put("isShake", slip2.isChecked() + "");
        db.insert("OaUser", null, values);
    }

    /**
     * 根据用户ID查询用户基础信息表并显示数据
     */
    private void queryOaUser() {
        cursor = db.rawQuery("select * from OaUser where userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
        totalCount = cursor.getCount();
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            Log.i("insertOaUser", "查询到：" + totalCount);
            slip.setChecked(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isNewMessage"))));
            slip1.setChecked(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isRing"))));
            slip2.setChecked(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isShake"))));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        vibrator.cancel();
    }
}
