package com.example.administrator.thinker_soft.myapplicaction;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.LayoutUtil;

public class MyApplication extends MultiDexApplication {
    /**上下文*/
    private static Context mContext;
    public static Boolean isCreatData = true;
    public static final int DATA_BASE_VERSION = 6;
    /**屏幕的宽*/
    public static int screenWidth;
    /**屏幕的高*/
    public static int screenHeight;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 获取屏幕尺寸大小，使程序能在不同大小的手机上有更好的兼容性
        mContext = getApplicationContext();
        screenHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        screenWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;

        //   LayoutUtil.initConfig(screenWidth, screenHeight, 1080f, 1920f);
        LayoutUtil.initConfig(screenWidth, screenHeight, 720f, 1280f);
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        Log.e("路径=", getApplicationContext().getFilesDir().getAbsolutePath());


//        DatabaseContext dbContext=new DatabaseContext(mContext);
        // MySqliteHelper helpers = new MySqliteHelper(dbContext, MyApplication.DATA_BASE_VERSION);
//        helpers.getWritableDatabase();
        //当前应用的代码执行目录
        //  upgradeRootPermission(getPackageCodePath());
        /**
         * 下拉刷新
         */
        /*//设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });*/
    }

    public static Context getContext() {
        return mContext;
    }


}
