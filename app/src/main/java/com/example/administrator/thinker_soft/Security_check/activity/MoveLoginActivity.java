package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.LayoutUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;
import com.example.administrator.thinker_soft.mode.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by Administrator on 2017/5/25.
 */
public class MoveLoginActivity extends Activity {
    private Button loginBtn, temp_login;
    private ImageView logo;
    private long exitTime = 0;//退出程序
    private CheckBox remindMe;  //记住账号密码
    private EditText editUser, editPsw;
    private SharedPreferences sharedPreferences_login, public_sharedPreferences, sharedPreferences;
    private SharedPreferences.Editor editor;
    //private String ip, port;  //接口ip地址   端口
    private TextView ipTextView;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private LinearLayout rootLinearlayout;
    private View view;
    private TextView tips;  //加载进度的提示
    long[] mHits = new long[3];  //三击事件
    private static final long ONE_DAY_MS = 24 * 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_login);
        bindView();//绑定控件
        defaultSetting();
        setViewClickListener();//点击事件
    }

    //绑定控件
    public void bindView() {
        logo = (ImageView) findViewById(R.id.logo);
        editUser = (EditText) findViewById(R.id.edit_user);
        editPsw = (EditText) findViewById(R.id.edit_psw);
        remindMe = (CheckBox) findViewById(R.id.remind_me);
        ipTextView = (TextView) findViewById(R.id.ip);
        loginBtn = (Button) findViewById(R.id.login_btn);
        temp_login = (Button) findViewById(R.id.temp_login);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);


        LayoutUtil mLayoutUtil = LayoutUtil.getInstance();
        mLayoutUtil.drawViewLinearRBLayout(logo, 0f, 0f, 0f, 0f, 80f, 0f);
        mLayoutUtil.drawViewLinearRBLayout(editUser, 0f, 0f, 0f, 0f, 80f, 0f);
        mLayoutUtil.drawViewLinearRBLayout(editPsw, 0f, 0f, 0f, 0f, 40f, 0f);
        mLayoutUtil.drawViewLinearRBLayout(loginBtn, 0f, 0f, 0f, 0f, 40f, 0f);
        mLayoutUtil.drawViewLinearRBLayout(temp_login, 0f, 0f, 0f, 0f, 20f, 40f);
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        public_sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        editor = sharedPreferences_login.edit();
        Log.i("MobileSecurityLogin", "默认设置进来了！是否记住账号：" + sharedPreferences_login.getBoolean("remind_me", false));
        if (sharedPreferences_login.getBoolean("remind_me", false)) {
            Log.i("MobileSecurityLogin", "记住账号默认设置进来了！");
            remindMe.setChecked(true);
            editUser.setText(sharedPreferences_login.getString("login_name", ""));//设置EditText控件的内容
            editUser.setSelection(sharedPreferences_login.getString("login_name", "").length());//将光标移至文字末尾
            editPsw.setText(sharedPreferences_login.getString("login_psw", ""));
            editPsw.setSelection(sharedPreferences_login.getString("login_psw", "").length());//将光标移至文字末尾
        } else {
            editUser.setText(sharedPreferences_login.getString("login_name", ""));//设置EditText控件的内容
            editUser.setSelection(sharedPreferences_login.getString("login_name", "").length());//将光标移至文字末尾
            editUser.requestFocus();
            Log.i("MobileSecurityLogin", "没记住账号默认设置进来了！");
        }


    }

    //点击事件
    public void setViewClickListener() {
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    temp_login.setVisibility(View.VISIBLE);
                }
            }
        });
        loginBtn.setOnClickListener(onclickListener);
        temp_login.setOnClickListener(onclickListener);
        ipTextView.setOnClickListener(onclickListener);
        remindMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i("MobileSecurityLogin", "记住账号");
                    editor.putBoolean("remind_me", true);
                    editor.apply();
                } else {
                    Log.i("MobileSecurityLogin", "没记住账号");
                    editor.putBoolean("remind_me", false);
                    editor.apply();
                }
            }
        });
    }

    View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ip:
                    Intent intent = new Intent(MoveLoginActivity.this, IpSettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.login_btn:
                    loginBtn.setClickable(false);
                    if (editUser.getText().toString().equals("") && editPsw.getText().toString().equals("")) {
                        Toast.makeText(MoveLoginActivity.this, "请您输入用户名和密码！", Toast.LENGTH_LONG).show();
                        loginBtn.setClickable(true);
                    } else if (editUser.getText().toString().equals("")) {
                        Toast.makeText(MoveLoginActivity.this, "请您输入用户名！", Toast.LENGTH_LONG).show();
                        loginBtn.setClickable(true);
                    } else if (editPsw.getText().toString().equals("")) {
                        Toast.makeText(MoveLoginActivity.this, "请您输入密码！", Toast.LENGTH_LONG).show();
                        loginBtn.setClickable(true);
                    }
                    if (!editUser.getText().toString().equals("") && !editPsw.getText().toString().equals("")) {
                        Tools.hideSoftInput(MoveLoginActivity.this, editPsw);
                        showPopupwindow();

//                        Request.Builder build = new Request.Builder()
//                                .url("http://88.88.88.251:8080/SMDemo/login.do")
//                                .method(HttpMethod.POST)
//                                .encode("UTF-8")
//                                .tag("login")
//                                .headers("User-Agent", "sk-1.0")
//                                .headers("Content-Type", "application/x-www-form-urlencoded")
//                                .params("username", editUser.getText().toString().trim().toUpperCase())
//                                .params("password", editPsw.getText().toString().trim())
//                                .string("");
//                        Request.newRequest(build, new HttpCallback() {
//                            @Override
//                            public void onComplete(Response response) {
//                                Log.e("Login", "onComplete/response:" + response.getBody());
//                                Log.e("Login", "onComplete/response: content type=" + response.getContentType());
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.e("Login", "onError:" + e.getMessage());
//                            }
//                        }).executeAsync();


                        //开启子线程
                        new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(1500);
                                    if (editUser.getText().toString().trim().equals("super")) {
                                        loginByPost(editUser.getText().toString().trim().toUpperCase(), editPsw.getText().toString().trim());
                                    } else {
                                        loginByPost(editUser.getText().toString().trim(), editPsw.getText().toString().trim());
                                    }

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();


                    }
                    break;
                case R.id.temp_login:
                    editor.putBoolean("have_logined", false).apply();
                    sharedPreferences.edit().putBoolean("show_temp_data", true).apply();
                    Intent intent1 = new Intent(MoveLoginActivity.this, /*SecurityChooseActivity*/MoveHomePageActivity.class);
                    //跳转到移动抄表页面
//                  Intent intent1 = new Intent(MoveLoginActivity.this, MeterHomePageActivity.class);
                    startActivity(intent1);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    //show加载动画
    public void showPopupwindow() {
        layoutInflater = LayoutInflater.from(MoveLoginActivity.this);
        view = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) view.findViewById(R.id.frame_animation);
        tips = (TextView) view.findViewById(R.id.tips);
        tips.setText("登录中...请稍后");
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        //popupWindow.update();
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

    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MoveLoginActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MoveLoginActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MoveLoginActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MoveLoginActivity.this.getWindow().setAttributes(lp);
    }

    //post请求
    public void loginByPost(final String userName, final String userPass) {
        new Thread() {
            @Override
            public void run() {
                try {

                    //  String httpUrl = "http://" + ip + port + "/SMDemo/login.do";
                    String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MoveLoginActivity.this)).append("login.do").toString();
                    Log.i("httpUrl==========>", "" + httpUrl + "---" + SkUrl.encode(httpUrl));
                    // 根据地址创建URL对象
                    URL url = new URL(httpUrl);
                    // 根据URL对象打开链接2017-09-08
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    // 发送POST请求必须设置允许输出
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setUseCaches(false);//不使用缓存
                    // 设置请求的方式
                    urlConnection.setRequestMethod("POST");
                    // 设置请求的超时时间
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setConnectTimeout(5000);
                    // 传递的数据
                    String data = "username=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + URLEncoder.encode(userPass, "UTF-8");
                    Log.i("data==========>", "data=" + data);
                    // 设置请求的头
                    //urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    //urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    //urlConnection.setRequestProperty("Origin", "http://"+ ip + port);
                    Log.i("data==========>", "data=" + data);
                    // 设置请求的头
                    //urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //urlConnection.setRequestProperty("Origin", "http://"+ ip + port);
                    urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                    //获取输出流
                    OutputStream os = urlConnection.getOutputStream();
                    os.write(data.getBytes("UTF-8"));
                    os.flush();
                    os.close();
                    Log.i("getResponseCode====>", "" + urlConnection.getResponseCode());
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream inputStream = urlConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        // 释放资源
                        inputStream.close();
                        // 返回字符串
                        String result = stringBuilder.toString();
                        Log.i("login_result=========>", result);
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.optInt("messg", 0) == 0) {
                            handler.sendEmptyMessage(2);
                            return;
                        }
                        if (jsonObject.optInt("jobState") == 0) {
                            handler.sendEmptyMessage(5);
                            return;
                        }
                        if (jsonObject.optInt("useState") == 0) {
                            handler.sendEmptyMessage(6);
                            return;
                        }
                        if (jsonObject.optInt("messg", 0) == 1) {
                            editor.putInt("company_id", jsonObject.optInt("companyid", 0));
                            editor.putString("user_name", jsonObject.optString("userName", ""));
                            editor.putString("userId", jsonObject.optInt("systemuserId", 0) + "");
                            // TODO: 2017/10/11
//                            if (sharedPreferences_login.getLong("creat_date", 0) != 0) {
//                                long start_time = sharedPreferences_login.getLong("creat_date", 0);
//                                Log.i("delta-T", " start_time=" + sharedPreferences_login.getLong("creat_date", 0));
//                                long end_time = System.currentTimeMillis();
//                                Log.i("delta-T", " end_time=" + System.currentTimeMillis());
////                                int s = (int) ((end_time - start_time) / (ONE_DAY_MS));
//                                int s = TimeUtil.betweenDays(start_time, end_time);
//                                Log.i("delta-T", " creat_date!=0,時間差=" + s);
//                                if (s > 30) {
//                                    handler.sendEmptyMessage(4);
//                                    return;
//                                }
//                            } else {
//                                //直接倒转到移动抄表页面
//                                Log.i("delta-T", " creat_date=0,時間=" + System.currentTimeMillis());
//                                editor.putLong("creat_date", System.currentTimeMillis());
//                            }
                            editor.apply();
                            Log.i("MobileSecurityLogin", "当前用户公司ID是：" + sharedPreferences_login.getInt("company_id", 0));
                            Log.i("MobileSecurityLogin", "当前用户是：" + sharedPreferences_login.getString("user_name", ""));
                            Log.i("MobileSecurityLogin", "userId：" + sharedPreferences_login.getString("userId", ""));
                            handler.sendEmptyMessage(1);
                        }

                    } else {
                        handler.sendEmptyMessage(3);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException==========>", "网络请求异常!");
                    handler.sendEmptyMessage(3);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (editUser.getText().toString().equals(sharedPreferences_login.getString("login_name", "")) && editPsw.getText().toString().equals(sharedPreferences_login.getString("login_psw", ""))) {
                        Log.i("MobileSecurityLogin", "用户未改变");
                        sharedPreferences_login.edit().putBoolean("user_exchanged", false).apply();
                    } else {
                        Log.i("MobileSecurityLogin", "用户改变");
                        sharedPreferences_login.edit().putBoolean("user_exchanged", true).apply();
                    }
                    Log.i("MobileSecurityLogin", "是否记住账号" + sharedPreferences_login.getBoolean("remind_me", false));
                    if (sharedPreferences_login.getBoolean("remind_me", false)) {
                        editor.putString("login_name", editUser.getText().toString());
                        editor.putString("login_psw", editPsw.getText().toString());
                        editor.apply();
                    } else {
                        editor.putString("login_name", editUser.getText().toString()).apply();
                        Log.i("MobileSecurityLogin", "未记住密码！账号为" + editUser.getText().toString());
                    }
                    popupWindow.dismiss();
                    Toast.makeText(MoveLoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MoveLoginActivity.this, MoveHomePageActivity.class);
                    //TODO 跳转到移动抄表页面
//                    Intent intent = new Intent(MoveLoginActivity.this, MeterHomePageActivity.class);
                    startActivity(intent);
                    loginBtn.setClickable(true);
                    sharedPreferences.edit().putBoolean("show_temp_data", false).apply();
                    editor.putBoolean("have_logined", true);
                    editor.apply();
                    //登录成功
                    SharedPreferences  loginDataShared = getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
                    //苍溪
                    if (SharedPreferencesHelper.getFirm(MoveLoginActivity.this).equals("苍溪")) {
                        loginDataShared.edit().putInt("all_downloads", 0).apply();

                    } else if (SharedPreferencesHelper.getFirm(MoveLoginActivity.this).equals("南部")) {
                        loginDataShared.edit().putInt("all_downloads", 0).apply();
                    } else {
                        loginDataShared.edit().putInt("all_downloads", 1).apply();
                    }

                    finish();
                    break;
                case 2:
                    popupWindow.dismiss();
                    loginBtn.setClickable(true);
                    Toast.makeText(MoveLoginActivity.this, "账号或密码错误!", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    popupWindow.dismiss();
                    loginBtn.setClickable(true);
                    Toast.makeText(MoveLoginActivity.this, "网络请求异常!", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    popupWindow.dismiss();
                    loginBtn.setClickable(true);
                    Toast.makeText(MoveLoginActivity.this, "试用期时间已过！联系思恩科公司继续使用！", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    popupWindow.dismiss();
                    loginBtn.setClickable(true);
                    Toast.makeText(MoveLoginActivity.this, "该账号员工已经离职，不能登录！", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    popupWindow.dismiss();
                    loginBtn.setClickable(true);
                    Toast.makeText(MoveLoginActivity.this, "该用户不允许登录！", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };
//    /**
//     * 点击空白区域隐藏键盘.
//     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideKeyboard(v, ev)) {
//                hideKeyboard(v.getWindowToken());
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
