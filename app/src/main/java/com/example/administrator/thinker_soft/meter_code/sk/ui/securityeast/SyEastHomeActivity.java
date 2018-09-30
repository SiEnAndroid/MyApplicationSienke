package com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.activity.MobileSecurityLoginActivity;
import com.example.administrator.thinker_soft.Security_check.activity.SecurityChooseActivity;
import com.example.administrator.thinker_soft.Security_check.activity.SystemSettingActivity;
import com.example.administrator.thinker_soft.Security_check.adapter.SecurityCheckViewPagerAdapter;
import com.example.administrator.thinker_soft.Security_check.fragment.MyInfoFragment;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastContentBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastHiddenBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastLoginBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastReasonBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastStateBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpService;
import com.example.administrator.thinker_soft.meter_code.sk.ui.MeterUserListsActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.fragment.SyEastHomePagerFragment;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.DeviceUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.NetUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtils;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.HttpRetrofit;
import com.example.administrator.thinker_soft.meter_payment.Utils.MyDialog;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;


/**
 * @author g
 * @FileName SyEastHomeActivity
 * @date 2018/9/28 16:01
 */
public class SyEastHomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private List<Fragment> fragmentList;
    /**
     * 适配器
     */
    private SecurityCheckViewPagerAdapter adapter;
    /**
     * 弹出框
     */
    private PopupWindow popupwindow, quitePopup;
    /**
     * 存储
     */
    private SharedPreferences sharedPreferences_login;
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * 退出程序
     */
    private long exitTime = 0;
    @BindView(R.id.back)
    ImageView brack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.clear)
    TextView clear;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_mime)
    RadioButton rbMime;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_dy_home;
    }

    /**
     *
     */
    @Override
    protected void initView() {
        tvTitle.setText("首页");
        brack.setVisibility(View.GONE);
        setddrawable();
        //退出登录以后需要这个备份记录是否更换账号
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        db = MySqliteHelper.getInstance(this).getWritableDatabase();

        fragmentList = new ArrayList<>();
        //添加fragment到list
        fragmentList.add(SyEastHomePagerFragment.newInstance());
        fragmentList.add(MyInfoFragment.newInstance());
        //避免报空指针
        if (fragmentList != null) {
            adapter = new SecurityCheckViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        }
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        //是否有网络
        if (NetUtils.isNetworkAvailable(SyEastHomeActivity.this)) {
            setRequest();
        } else {
            ToastUtils.showShort(SyEastHomeActivity.this, "网络不可用,请检查网络");
        }

    }


    /**
     * 监听
     *
     * @param view
     */
    @OnClick({R.id.back, R.id.rb_home, R.id.rb_mime, R.id.clear})
    public void OnclickHome(View view) {

        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.rb_home:
                //首页
                viewPager.setCurrentItem(0);
                tvTitle.setText("首页");
                break;
            case R.id.rb_mime:
                //我的
                viewPager.setCurrentItem(1);
                tvTitle.setText("我的");
                break;

            case R.id.clear:
                //更多
                showCreatePopupwindow(view);
                break;
            default:
                break;

        }
    }

    /**
     * 设置添加按钮
     */
    private void setddrawable() {
        Drawable drawableRight = getResources().getDrawable(
                R.drawable.more_settings_selector);
        clear.setCompoundDrawablesWithIntrinsicBounds(null,
                null, drawableRight, null);
        clear.setCompoundDrawablePadding(5);


    }

    /**
     * 显示更多
     */
    public void showCreatePopupwindow(View vs) {
        View view = LayoutInflater.from(SyEastHomeActivity.this).inflate(R.layout.popup_window_security, null);
        popupwindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        //file = (RadioButton) popupwindowView.findViewById(R.id.file);//文件管理
        //系统设置
        RadioButton settings =view.findViewById(R.id.settings);
        ;//安全退出
        RadioButton quite =view.findViewById(R.id.quite);
        //版本信息
        RadioButton versionInfo =view.findViewById(R.id.version_info);
        versionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupwindow.dismiss();
                final MyDialog myDialog = new MyDialog();
                // 当前的版本号
                double mVersion_code = Double.parseDouble(DeviceUtils.getVersionName(SyEastHomeActivity.this));
                myDialog.show("版本信息", "版本号:" + mVersion_code, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDialog.dismiss();
                    }
                }, getSupportFragmentManager());
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoActivity(SystemSettingActivity.class);
                popupwindow.dismiss();

            }
        });

        quite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupwindow.dismiss();
                showQuite();
            }
        });
        popupwindow.setFocusable(true);
        popupwindow.setOutsideTouchable(true);
        popupwindow.update();
        popupwindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupwindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupwindow.showAsDropDown(vs, -PopWindowUtil.dip2px(SyEastHomeActivity.this, 73), 0);
        PopWindowUtil.backgroundAlpha(SyEastHomeActivity.this, 0.6F);
        popupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SyEastHomeActivity.this, 1.0F);
            }
        });
    }

    /**
     * 弹出退出登录前提示popupwindow
     */
    public void showQuite() {
        View quiteView = LayoutInflater.from(SyEastHomeActivity.this).inflate(R.layout.popupwindow_user_detail_info_save, null);
        quitePopup = new PopupWindow(quiteView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        TextView tips = quiteView.findViewById(R.id.tips);
        RadioButton cancelRb = quiteView.findViewById(R.id.cancel_rb);
        RadioButton saveRb = quiteView.findViewById(R.id.save_rb);
        //设置点击事件
        tips.setText("退出后不会删除历史数据，下次登录依然可以使用本账号！");
        saveRb.setTextColor(getResources().getColor(R.color.red));
        saveRb.setText("退出登录");
        cancelRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitePopup.dismiss();
            }
        });
        saveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitePopup.dismiss();
                GoActivity(SyEastLoginActivity.class);
                sharedPreferences_login.edit().putBoolean("have_logined", false).apply();
                finish();
            }
        });
        quitePopup.update();
        quitePopup.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        quitePopup.setAnimationStyle(R.style.camera);
        quitePopup.showAtLocation(quiteView, Gravity.CENTER, 0, 0);
        PopWindowUtil.backgroundAlpha(SyEastHomeActivity.this, 0.6F);
        quitePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SyEastHomeActivity.this, 1.0F);
            }
        });
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                rbHome.setChecked(true);
                rbMime.setChecked(false);
                tvTitle.setText("首页");
                break;
            case 1:
                rbHome.setChecked(false);
                rbMime.setChecked(true);
                tvTitle.setText("我的");
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * 设置请求
     */
    private void setRequest() {
        //安检状态
        HttpRetrofit.setrRequest(this, HttpRetrofit.getInstance(null).getApiServise(HttpService.class).getSecurityState(),
                new HttpRetrofit.OnRequestCall<SyEastStateBean>() {

                    @Override
                    public void onSuccess(SyEastStateBean bean) {
                        if (bean.getTotal() != 0) {
                            //删除SecurityState表中的所有数据
                            db.delete("SecurityState", null, null);
                            db.execSQL("update sqlite_sequence set seq=0 where name='SecurityState'");
                            //成功
                            for (SyEastStateBean.RowsBean rowsBean : bean.getRows()) {
                                insertSecurityState(rowsBean);
                            }
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("error", msg);
                    }
                });
        //安检内容
        HttpRetrofit.setrRequest(this, HttpRetrofit.getInstance(null).getApiServise(HttpService.class).getSecurityContent(),
                new HttpRetrofit.OnRequestCall<SyEastContentBean>() {

                    @Override
                    public void onSuccess(SyEastContentBean bean) {
                        if (bean.getTotal() != 0) {
                            //删除security_content表中的所有数据
                            db.delete("security_content", null, null);
                            db.execSQL("update sqlite_sequence set seq=0 where name='security_content'");
                            //成功
                            for (SyEastContentBean.RowsBean rowsBean : bean.getRows()) {
                                insertSecurityContent(rowsBean);
                            }
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("error", msg);
                    }
                });
        //安检原因类型
        HttpRetrofit.setrRequest(this, HttpRetrofit.getInstance(null).getApiServise(HttpService.class).getSecurityHidden(),
                new HttpRetrofit.OnRequestCall<SyEastHiddenBean>() {

                    @Override
                    public void onSuccess(SyEastHiddenBean bean) {
                        if (bean.getTotal() != 0) {
                            //删除security_hidden表中的所有数据
                            db.delete("security_hidden", null, null);
                            db.execSQL("update sqlite_sequence set seq=0 where name='security_hidden'");
                            //成功
                            for (SyEastHiddenBean.RowsBean rowsBean : bean.getRows()) {
                                insertSecurityHidden(rowsBean);
                            }
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("error", msg);
                    }
                });
        //安检原因
        HttpRetrofit.setrRequest(this, HttpRetrofit.getInstance(null).getApiServise(HttpService.class).getSecurityReason(),
                new HttpRetrofit.OnRequestCall<SyEastReasonBean>() {

                    @Override
                    public void onSuccess(SyEastReasonBean bean) {

                        if (bean.getTotal() != 0) {
                            deleteDb();
                            //成功
                            for (SyEastReasonBean.RowsBean rowsBean : bean.getRows()) {
                                insertSecurityHiddenReason(rowsBean);
                            }
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("error", msg);
                    }
                });

    }

    /**
     * 安检状态数据存到本地数据库安检状态表
     */
    private void insertSecurityState(SyEastStateBean.RowsBean bean) {
        ContentValues values = new ContentValues();
        values.put("securityId", bean.getSecurityId() + "");
        values.put("securityName", bean.getSecurityName());
        db.insert("SecurityState", null, values);
    }

    /**
     * 安检内容数据存到本地数据库安检内容表
     */

    private void insertSecurityContent(SyEastContentBean.RowsBean bean) {
        ContentValues values = new ContentValues();
        values.put("securityId", bean.getSecurityId() + "");
        values.put("securityName", bean.getSecurityName());
        db.insert("security_content", null, values);
    }

    /**
     * 安检隐患类型数据存到本地数据库安检内容表
     */
    private void insertSecurityHidden(SyEastHiddenBean.RowsBean bean) {
        ContentValues values = new ContentValues();
        values.put("n_safety_hidden_id", bean.getN_safety_hidden_id() + "");
        values.put("n_safety_hidden_name", bean.getN_safety_hidden_name());
        db.insert("security_hidden", null, values);
    }

    /**
     * 安检隐患原因数据存到本地数据库安检内容表
     */
    private void insertSecurityHiddenReason(SyEastReasonBean.RowsBean bean) {
        ContentValues values = new ContentValues();
        values.put("n_safety_hidden_reason_id", bean.getN_safety_hidden_reason_id() + "");
        values.put("n_safety_hidden_id", bean.getN_safety_hidden_id() + "");
        values.put("n_safety_hidden_reason_name", bean.getN_safety_hidden_reason_name());
        db.insert("security_hidden_reason", null, values);
    }

    /**
     * 清除默认数据
     */
    private void deleteDb() {
        //删除security_hidden_reason表中的所有数据
        db.delete("security_hidden_reason", null, null);
        //设置id从1开始（sqlite默认id从1开始），若没有这一句，id将会延续删除之前的id
        db.execSQL("update sqlite_sequence set seq=0 where name='security_hidden_reason'");
        Log.i("清除数据","---");
    }

    /**
     * 捕捉返回事件按钮
     * 因为此 Activity继承 TabActivity,用 onKeyDown无响应，
     * 所以改用 dispatchKeyEvent
     * <p>
     * 一般的 Activity 用 onKeyDown就可以了
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Log.i("exitTime==========>", System.currentTimeMillis() - exitTime + "");
            ToastUtil.showLong(getApplicationContext(), "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
