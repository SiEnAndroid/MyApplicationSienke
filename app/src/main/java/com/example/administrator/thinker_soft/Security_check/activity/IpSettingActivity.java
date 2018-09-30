package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;
import com.example.administrator.thinker_soft.mode.Tools;

/**
 * Created by Administrator on 2017/3/17.
 */
public class IpSettingActivity extends Activity {
    private ImageView back;
    private EditText ipEdit, portEdit;
    private Button confirmBtn, edit;
    private SharedPreferencesHelper sharedPreferences;
    //private SharedPreferences public_sharedPreferences;
//    private SharedPreferences.Editor editor;
    private String ip, port;
    private LinearLayout rootLinearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_settings);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setViewClickListener();//点击事件
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        ipEdit = (EditText) findViewById(R.id.ip_edit);
        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        edit = (Button) findViewById(R.id.edit);
        portEdit = (EditText) findViewById(R.id.port_edit);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //点击事件
    private void setViewClickListener() {
        back.setOnClickListener(clickListener);
        edit.setOnClickListener(clickListener);
        confirmBtn.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    IpSettingActivity.this.finish();
                    break;
                case R.id.edit:
                    //编辑
                    if (!Tools.isInputMethodOpened(IpSettingActivity.this)) {
                        Tools.showInputMethod(IpSettingActivity.this, ipEdit);
                    }
                    ipEdit.setTextColor(getResources().getColor(R.color.text_black));
                    portEdit.setTextColor(getResources().getColor(R.color.text_black));
                    portEdit.setFocusable(true);
                    portEdit.setFocusableInTouchMode(true);
                    portEdit.requestFocus();
                    ipEdit.setFocusable(true);
                    ipEdit.setFocusableInTouchMode(true);
                    ipEdit.requestFocus();
                    ipEdit.setCursorVisible(true);
                    portEdit.setCursorVisible(true);
                    if (!sharedPreferences.getSharedPreference("security_ip", "").equals("")) {
                        ipEdit.setSelection(((String) sharedPreferences.getSharedPreference("security_ip", "")).length());//将光标移至文字末尾
                    } else {
                        // ipEdit.setSelection(SkUrl.SKIP.length());//将光标移至文字末尾
                        //苍溪
                        if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("苍溪")) {
                            ipEdit.setSelection(SkUrl.SKSY.length());
                        } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("南部")) {
                            //南部
                            ipEdit.setSelection(SkUrl.SKNB.length());
                        } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("渝川安检")) {
                            // 渝川安检
                            ipEdit.setSelection(SkUrl.SYNB.length());
                        } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("方根安检")) {
                            //方根安检
                            ipEdit.setSelection(SkUrl.SYFG.length());
                        } else  if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("东渝安检")) {
                            //东渝安检
                            ipEdit.setSelection(SkUrl.SYDY.length());
                        } else  if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("渝山")) {
                            //渝山
                            ipEdit.setSelection(SkUrl.SYYS.length());
                        } else {
                            ipEdit.setSelection(SkUrl.SKIP.length());
                        }
          
                    }
                    if (!sharedPreferences.getSharedPreference("security_port", "").equals("")) {
                        //将光标移至文字末尾
                        portEdit.setSelection(((String) sharedPreferences.getSharedPreference("security_port", "")).length());
                    } else {
                        //    portEdit.setSelection(SkUrl.SKPORT.length());

                        //苍溪
                        if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("苍溪")) {
                            portEdit.setSelection(SkUrl.SKSYPORT.length());
                        } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("南部")) {
                            //南部
                            portEdit.setSelection(SkUrl.SKNBPORT.length());
                        } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("渝川安检")) {
                            portEdit.setSelection(SkUrl.SYNBPORT.length());
                        } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("方根安检")) {
                            //方根安检
                            portEdit.setSelection(SkUrl.SYFGPORT.length());
                        }else  if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("东渝安检")) {
                            //东渝安检
                            portEdit.setSelection(SkUrl.SYDYPORT.length());
                        }else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("渝山")) {
                            //渝山
                            portEdit.setSelection(SkUrl.SYYSPORT.length());
                        }else {
                            portEdit.setSelection(SkUrl.SKPORT.length());
                        }
                    }
                    edit.setVisibility(View.GONE);
                    confirmBtn.setVisibility(View.VISIBLE);
                    break;
                case R.id.confirm_btn:
                    //提交
                    if (Tools.isInputMethodOpened(IpSettingActivity.this)) {
                        Tools.hideSoftInput(IpSettingActivity.this, ipEdit);
                    }
                    if (Tools.isInputMethodOpened(IpSettingActivity.this)) {
                        Tools.hideSoftInput(IpSettingActivity.this, portEdit);
                    }
                    edit.setVisibility(View.VISIBLE);
                    confirmBtn.setVisibility(View.GONE);
                    ipEdit.setTextColor(getResources().getColor(R.color.text_gray));
                    portEdit.setTextColor(getResources().getColor(R.color.text_gray));
                    ipEdit.setCursorVisible(false);
                    portEdit.setCursorVisible(false);
                    ipEdit.setFocusable(false);
                    ipEdit.setFocusableInTouchMode(false);
                    portEdit.setFocusable(false);
                    portEdit.setFocusableInTouchMode(false);
                    ip = ipEdit.getText().toString().trim();
                    port = portEdit.getText().toString().trim();
//                    editor.putString("security_ip",ip);
//                    editor.putString("security_port",port);
//                    editor.putBoolean("ip_port_changed",true);
//                    editor.apply();
                    sharedPreferences.putSharedPreference("security_ip", ip.replace(":", ""));
                    sharedPreferences.putSharedPreference("security_port", port);
                    sharedPreferences.putSharedPreference("ip_port_changed", true);
                    Toast.makeText(IpSettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    //初始化设置
    private void defaultSetting() {
        sharedPreferences = new SharedPreferencesHelper(IpSettingActivity.this, SharedPreferencesHelper.DATA_IP);
        ipEdit.setTextColor(getResources().getColor(R.color.text_gray));
        portEdit.setTextColor(getResources().getColor(R.color.text_gray));
        ipEdit.setFocusable(false);
        ipEdit.setFocusableInTouchMode(false);
        portEdit.setFocusable(false);
        portEdit.setFocusableInTouchMode(false);

        if (!sharedPreferences.getSharedPreference("security_ip", "").equals("")) {
            ipEdit.setText((String) sharedPreferences.getSharedPreference("security_ip", ""));
        } else {
            //ipEdit.setText(SkUrl.SKIP);
            //苍溪
            if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("苍溪")) {
                ipEdit.setText(SkUrl.SKSY);
            } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("南部")) {
                //南部
                ipEdit.setText(SkUrl.SKNB);
            } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("渝川安检")) {
                // 渝川安检
                ipEdit.setText(SkUrl.SYNB);
            } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("方根安检")) {
                //方根安检
                ipEdit.setText(SkUrl.SYFG);
            } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("东渝安检")) {
                //东渝安检
                ipEdit.setText(SkUrl.SYDY);
            } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("渝山")) {
                //渝山
                ipEdit.setText(SkUrl.SYYS);
            } else {
                ipEdit.setText(SkUrl.SKIP);
            }

        }
        if (!sharedPreferences.getSharedPreference("security_port", "").equals("")) {
            portEdit.setText((String) sharedPreferences.getSharedPreference("security_port", ""));
        } else {
            // portEdit.setText(SkUrl.SKPORT);
            //苍溪
            if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("苍溪")) {
                portEdit.setText(SkUrl.SKSYPORT);
            } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("南部")) {
                //南部
                portEdit.setText(SkUrl.SKNBPORT);
            } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("渝川安检")) {
                //渝川安检
                portEdit.setText(SkUrl.SYNBPORT);
            } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("方根安检")) {
                //方根安检
                portEdit.setText(SkUrl.SYFGPORT);
            } else  if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("东渝安检")) {
                //东渝安检
                portEdit.setText(SkUrl.SYDYPORT);
            } else if (SharedPreferencesHelper.getFirm(IpSettingActivity.this).equals("渝山")) {
                //渝山
                portEdit.setText(SkUrl.SYYSPORT);
            } else {
                portEdit.setText(SkUrl.SKPORT);
            }


        }


    }
}

