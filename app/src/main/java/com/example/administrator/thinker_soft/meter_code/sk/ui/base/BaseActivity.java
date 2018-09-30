package com.example.administrator.thinker_soft.meter_code.sk.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.administrator.thinker_soft.meter_code.sk.uitls.ActivityManagerUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.NetUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/5/4.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private Context mContxt;//上下文
    private Unbinder mUnbinder;//绑定
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //输入法打开后布局上移
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
//                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            //设置Activity的SoftInputMode属性值为adjustResize
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ActivityManagerUtil.getActivityManager().addActivity(this);
        if (getContentViewID() != 0) {
            setContentView(getContentViewID());
            //注入绑定id
            mUnbinder = ButterKnife.bind(this);
            mContxt = this;
            initView();
        }
    }

    /**
     * Activity
     *
     * @param newClass
     */
    // (1)页面跳转重载
    protected void GoActivity(Class<?> newClass) {
        Intent intent = new Intent(this, newClass);
        //退出多个Activity的程序
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void GoActivity(Class<?> newClass, Bundle extras) {
        Intent intent = new Intent(this, newClass);
        if (null != extras) {
            intent.putExtras(extras);
        }
        //退出多个Activity的程序
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //带返回值跳转重载
    protected void GoStartForResult(Class<?> newClass,final int flag){
        Intent intent = new Intent(this,newClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent,flag);
    }
    protected void GostartForResult(Class<?> newClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, newClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, requestCode);
    }
    /**
     * 布局
     * @return
     */
    protected abstract int getContentViewID();
    /**
     * 布局
     * @return
     */
    protected abstract void initView();

    protected boolean checkNet() {
        return NetUtils.isNetworkAvailable(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        ActivityManagerUtil.getActivityManager().removeActivity(this);
    }
}
