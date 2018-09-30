package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/14.
 */
public class BusinessSendEmailActivity extends Activity {
    private ImageView back;
    private RelativeLayout send, copyTo;
    private EditText sendName, recipients, type, content;
    private TextView time;
    private SharedPreferences sharedPreferences_login;
    private SQLiteDatabase db;  //数据库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_send_email);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        send = (RelativeLayout) findViewById(R.id.send);
        sendName = (EditText) findViewById(R.id.send_name);
        recipients = (EditText) findViewById(R.id.recipients);
        type = (EditText) findViewById(R.id.type);
        content = (EditText) findViewById(R.id.content);
        copyTo = (RelativeLayout) findViewById(R.id.copy_to);
        time = (TextView) findViewById(R.id.time);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(BusinessSendEmailActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        time.setText(dateFormat.format(new Date()));
    }

    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        send.setOnClickListener(clickListener);
        copyTo.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.send:
                    insertOaEmail();
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    };

    /**
     * 将信息保存到本地数据库OA日程表
     */
    private void insertOaEmail() {
        ContentValues values = new ContentValues();
        values.put("userId", sharedPreferences_login.getString("userId", ""));
        values.put("sendName", sendName.getText().toString().trim());
        values.put("recipients", recipients.getText().toString().trim());
        values.put("type", type.getText().toString().trim());
        values.put("content", content.getText().toString().trim());
        values.put("time", time.getText().toString().trim());
        db.insert("OaEmail", null, values);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
