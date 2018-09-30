package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/3/9 0009.
 */
public class MoreSettingsActivity extends Activity {
    private TextView back;   //返回
    private Button modify;   //修改
    private Button save;    //保存
    private EditText ipEdit;   //IP地址编辑
    private EditText servePhoneEdit;  //服务电话编辑
    private String ip,servePhoneNumber;
    private SharedPreferences sharedPreferences,sharedPreferences_login;
    private SharedPreferences.Editor editor;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_settings);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setViewClickListener();//设置点击事件
    }

    //绑定控件ID
    private void bindView(){
        back = (TextView) findViewById(R.id.back);
        modify = (Button) findViewById(R.id.modify);
        save = (Button) findViewById(R.id.save);
        ipEdit = (EditText) findViewById(R.id.ip_edit);
        servePhoneEdit = (EditText) findViewById(R.id.serve_phone_edit);
    }

    //设置点击事件
    private void setViewClickListener(){
        back.setOnClickListener(clickListener);
        modify.setOnClickListener(clickListener);
        save.setOnClickListener(clickListener);
        ipEdit.setOnClickListener(clickListener);
        servePhoneEdit.setOnClickListener(clickListener);
        ipEdit.setEnabled(false);
        servePhoneEdit.setEnabled(false);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    intent = getIntent();
                    MoreSettingsActivity.this.setResult(200,intent);
                    MoreSettingsActivity.this.finish();
                    break;
                case R.id.ip_edit:
                    if(ipEdit.getText().equals("")){
                        ipEdit.setHint("请输入接口地址");
                    }
                    break;
                case R.id.serve_phone_edit:
                    if(servePhoneEdit.getText().equals("")){
                        servePhoneEdit.setHint("请输入电话号码");
                    }
                    break;
                case R.id.modify:
                    ipEdit.setTextColor(getResources().getColor(R.color.text_black));
                    servePhoneEdit.setTextColor(getResources().getColor(R.color.text_black));
                    ipEdit.setEnabled(true);
                    servePhoneEdit.setEnabled(true);
                    break;
                case R.id.save:
                    ipEdit.setTextColor(getResources().getColor(R.color.text_gray));
                    servePhoneEdit.setTextColor(getResources().getColor(R.color.text_gray));
                    ipEdit.setEnabled(false);
                    servePhoneEdit.setEnabled(false);
                    ip = ipEdit.getText().toString().trim();
                    servePhoneNumber = servePhoneEdit.getText().toString().trim();
                    ipEdit.setText(ip);
                    servePhoneEdit.setText(servePhoneNumber);
                    editor.putString("IP",ip);
                    editor.putString("servePhoneNumber",servePhoneNumber);
                    editor.commit();
                    Toast.makeText(MoreSettingsActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void defaultSetting() {
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("login_name","")+"data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //Log.i("sharedPreferences====>",sharedPreferences.getString("IP",""));
        if(!sharedPreferences.getString("IP","").equals("")){
            ipEdit.setText(sharedPreferences.getString("IP",""));
        }else {
            ipEdit.setText("192.168.2.200:8080");
        }
        if(!sharedPreferences.getString("servePhoneNumber","").equals("")){
            servePhoneEdit.setText(sharedPreferences.getString("servePhoneNumber",""));
        }else {
            servePhoneEdit.setText("023-12345678");
        }
    }
}
