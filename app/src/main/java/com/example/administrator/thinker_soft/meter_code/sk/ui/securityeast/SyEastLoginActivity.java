package com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.activity.IpSettingActivity;
import com.example.administrator.thinker_soft.Security_check.activity.MobileSecurityLoginActivity;
import com.example.administrator.thinker_soft.Security_check.activity.SecurityChooseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastLoginBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpService;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ActivityManagerUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.NetUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtils;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.HttpRetrofit;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * 东渝安检登录
 *
 * @author g
 * @FileName SyEastLoginActivity
 * @date 2018/9/28 9:47
 */
public class SyEastLoginActivity extends BaseActivity {
    /**
     * 数据存储
     */
    private SharedPreferences sharedPreferences_login;
    private SharedPreferences.Editor editor;
    /**
     * 进度条
     */
    private LoadingView loadingView;

    @BindView(R.id.edit_mobile_user)
    EditText editMobileUser;
    @BindView(R.id.edit_mobile_psw)
    EditText editMobilePsw;
    @BindView(R.id.remind_me)
    CheckBox remindMe;
    @BindView(R.id.logon_btn)
    Button btnLogon;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_east_login;
    }

    @Override
    protected void initView() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        editor = sharedPreferences_login.edit();
        if (sharedPreferences_login.getBoolean("remind_me", false)) {
            Log.i("MobileSecurityLogin", "记住账号默认设置进来了！");
            remindMe.setChecked(true);
            //设置EditText控件的内容
            editMobileUser.setText(sharedPreferences_login.getString("login_name", ""));
            //将光标移至文字末尾
            editMobileUser.setSelection(sharedPreferences_login.getString("login_name", "").length());
            editMobilePsw.setText(sharedPreferences_login.getString("login_psw", ""));
            editMobilePsw.setSelection(sharedPreferences_login.getString("login_psw", "").length());
        } else {
            editMobileUser.setText(sharedPreferences_login.getString("login_name", ""));
            editMobileUser.setSelection(sharedPreferences_login.getString("login_name", "").length());
            editMobileUser.requestFocus();
            Log.i("MobileSecurityLogin", "没记住账号默认设置进来了！");
        }
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

    /**
     * 取消
     *
     * @param view
     */
    @OnClick({R.id.cancel_btn, R.id.logon_btn, R.id.ip})
    public void OnclickLogin(View view) {
        switch (view.getId()) {
            case R.id.ip:
                //设置ip
                GoActivity(IpSettingActivity.class);
                break;
            case R.id.cancel_btn:
                //取消
                finish();
                ActivityManagerUtil.getActivityManager().exit();
                break;
            case R.id.logon_btn:
                //登录
                btnLogon.setClickable(false);
                String userName = editMobileUser.getText().toString().trim();
                String userPsw = editMobilePsw.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    ToastUtil.showShort(SyEastLoginActivity.this, "请输入用户名");
                    break;
                }
                if (TextUtils.isEmpty(userPsw)) {
                    ToastUtil.showShort(SyEastLoginActivity.this, "请输入密码");
                    break;
                }
                //网络是否可用
                if (NetUtils.isNetworkAvailable(SyEastLoginActivity.this)) {
                    showLoadView();
                    if (userName.equals("super")) {
                        userName = editMobileUser.getText().toString().trim().toUpperCase();
                    }
                    setRequest(userName, userPsw);
                } else {
                    ToastUtils.showShort(SyEastLoginActivity.this, "网络不可用,请检查网络");
                }
                btnLogon.setClickable(true);
                break;
            default:
                break;

        }
    }


    /**
     * 加载进度
     */
    private void showLoadView() {
        //加载
        loadingView = new LoadingView(SyEastLoginActivity.this, R.style.LoadingDialog, "登录中...请稍后");
        loadingView.show();
    }

    /**
     * 关闭进度
     */
    private void dissLoadView() {
        if (loadingView != null) {
            loadingView.dismiss();
        }
    }

    /**
     * 登录
     *
     * @param userName
     * @param userPws
     */
    private void setRequest(final String userName, final String userPws) {

        HttpRetrofit.setrRequest(this, HttpRetrofit.getInstance(null).getApiServise(HttpService.class).getDyLogin(userName, userPws),
                new HttpRetrofit.OnRequestCall<SyEastLoginBean>() {

                    @Override
                    public void onSuccess(SyEastLoginBean bean) {
                        dissLoadView();
                        if (bean.getMessg() == 1) {
                            editor.putInt("company_id", bean.getCompanyid());
                            editor.putString("user_name", bean.getUserName());
                            editor.putString("userId", bean.getSystemuserId() + "");
                            editor.apply();
                            if (editMobileUser.getText().toString().trim().equals(sharedPreferences_login.getString("login_name", "")) && editMobilePsw.getText().toString().trim().equals(sharedPreferences_login.getString("login_psw", ""))) {
                                Log.i("MobileSecurityLogin", "用户未改变");
                                sharedPreferences_login.edit().putBoolean("user_exchanged", false).apply();
                            } else {
                                Log.i("MobileSecurityLogin", "用户改变");
                                sharedPreferences_login.edit().putBoolean("user_exchanged", true).apply();
                            }
                            Log.i("MobileSecurityLogin", "是否记住账号" + sharedPreferences_login.getBoolean("remind_me", false));
                            if (sharedPreferences_login.getBoolean("remind_me", false)) {
                                editor.putString("login_name", userName);
                                editor.putString("login_psw", userPws);
                                editor.apply();
                            } else {
                                editor.putString("login_name", editMobileUser.getText().toString()).apply();
                                Log.i("MobileSecurityLogin", "未记住密码！账号为" + editMobileUser.getText().toString());
                            }
                            ToastUtil.showShort(SyEastLoginActivity.this, "登录成功!");

                            GoActivity(SyEastHomeActivity.class);
                            editor.putBoolean("have_logined", true);
                            editor.apply();
                            finish();
                        } else {
                            ToastUtil.showShort(SyEastLoginActivity.this, "账号或密码错误!");
                        }

                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("error", msg);
                        dissLoadView();
                        ToastUtil.showShort(SyEastLoginActivity.this, "网络请求异常!");
                    }
                });
    }


}
