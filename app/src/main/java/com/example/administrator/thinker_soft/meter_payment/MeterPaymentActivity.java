package com.example.administrator.thinker_soft.meter_payment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 缴费
 */
public class MeterPaymentActivity extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.more)
    ImageView more;
    @BindView(R.id.rb_user_id)
    RadioButton rbUserId;
    @BindView(R.id.et_user_id)
    EditText etUserId;
    @BindView(R.id.radio_root1)
    LinearLayout radioRoot1;
    @BindView(R.id.rb_meter_number)
    RadioButton rbMeterNumber;
    @BindView(R.id.et_meter_number)
    EditText etMeterNumber;
    @BindView(R.id.radio_root2)
    LinearLayout radioRoot2;
    @BindView(R.id.btn_query)
    Button btnQuery;
    @BindView(R.id.root_linearlayout)
    LinearLayout rootLinearlayout;

    private SharedPreferences public_sharedPreferences, sharedPreferences_login;
    private SharedPreferences.Editor editor;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private PopupWindow popupWindow;
    public int responseCode = 0;
    private int readerCompanyId;
    private String result; //网络请求结果
    //private String ip, port;  //接口ip地址   端口


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_payment);
        ButterKnife.bind(this);
        defaultSetting();
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        public_sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = public_sharedPreferences.edit();
        readerCompanyId = sharedPreferences_login.getInt("company_id", 0);
        rbUserId.setChecked(true);
        etUserId.setEnabled(true);
        etUserId.setFocusable(true);
        etMeterNumber.setFocusableInTouchMode(true);
        etMeterNumber.setEnabled(false);
    }


    @OnClick({R.id.back, R.id.more, R.id.btn_query, R.id.rb_user_id, R.id.rb_meter_number})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.more:
                break;
            case R.id.btn_query:
                queryInfo();
                break;
            case R.id.rb_user_id:
                etUserId.setEnabled(true);
                etMeterNumber.setEnabled(false);
                etUserId.setFocusable(true);
                etUserId.setFocusableInTouchMode(true);
                etMeterNumber.setFocusable(false);
                etMeterNumber.setFocusableInTouchMode(false);
                etUserId.setText("");
                etMeterNumber.setText("");
                break;
            case R.id.rb_meter_number:
                etUserId.setEnabled(false);
                etMeterNumber.setEnabled(true);
                etMeterNumber.setFocusable(true);
                etMeterNumber.setFocusableInTouchMode(true);
                etUserId.setFocusable(false);
                etUserId.setFocusableInTouchMode(false);
                etUserId.setText("");
                etMeterNumber.setText("");
                break;
        }
    }

    //查询用户信息
    public void queryInfo() {
        if (etUserId.getText().toString().equals("") && rbUserId.isChecked()) {
            Toast.makeText(MeterPaymentActivity.this, "请输入用户编号", Toast.LENGTH_SHORT).show();
        } else if (etMeterNumber.getText().toString().equals("") && rbMeterNumber.isChecked()) {
            Toast.makeText(MeterPaymentActivity.this, "请输入表编号", Toast.LENGTH_SHORT).show();
        }
        if (!etUserId.getText().toString().equals("")) {
            showPopupwindow();
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        requireMyWorks("getAllUser.do", "userid=" + etUserId.getText().toString() + "&companyid=" + readerCompanyId);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else if (!etMeterNumber.getText().toString().equals("")) {
            showPopupwindow();
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        requireMyWorks("getAllUser.do", "meterNumber=" + etMeterNumber.getText().toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    //请求网络数据
    private void requireMyWorks(final String method, final String keyAndValue) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url;
                    HttpURLConnection httpURLConnection;
//                    String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
                    String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterPaymentActivity.this)).append(method).toString();
                    //有参数传递
                    if (!keyAndValue.equals("")) {
                        url = new URL(httpUrl + "?" + keyAndValue);
                        //没有参数传递
                    } else {
                        url = new URL(httpUrl);
                    }
                    Log.i("url=============>", url + "");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setReadTimeout(10000);
                    httpURLConnection.connect();
                    //传回的数据解析成String
                    Log.i("responseCode====>", httpURLConnection.getResponseCode() + "");
                    if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        result = stringBuilder.toString();
                        Log.i("result_query==========>", result);
                        JSONObject jsonObject = new JSONObject(result);
                        if (!jsonObject.optString("isExist", "").equals("0")) {
                            handler.sendEmptyMessage(0);
                        } else {
                            try {
                                Thread.sleep(500);
                                handler.sendEmptyMessage(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            Thread.sleep(500);
                            handler.sendEmptyMessage(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException==========>", "网络请求超时!");
                    handler.sendEmptyMessage(3);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    popupWindow.dismiss();

                    Intent intent = new Intent(MeterPaymentActivity.this, MeterReadingActivity.class);
                    if (rbUserId.isChecked()) {
                        intent.putExtra("userid", etUserId.getText().toString());
                    } else if (rbMeterNumber.isChecked()) {
                        intent.putExtra("meterNumber", etMeterNumber.getText().toString());
                    }
                    startActivityForResult(intent, 100);
                    break;
                case 1:    //没有数据
                    popupWindow.dismiss();
                    Toast.makeText(MeterPaymentActivity.this, "没有相应用户数据！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:   //返回码不是200，网络异常
                    popupWindow.dismiss();
                    Toast.makeText(MeterPaymentActivity.this, "编号不正确，请重新输入！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:    //请求超时
                    popupWindow.dismiss();
                    Toast.makeText(MeterPaymentActivity.this, "网络请求超时！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    //show弹出框
    public void showPopupwindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(MeterPaymentActivity.this);
        View view = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) view.findViewById(R.id.frame_animation);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.update();
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        startFrameAnimation();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }


}
