package com.example.administrator.thinker_soft.meter_code.sk.uitls;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class ActivityManagerUtil {

    private static ActivityManagerUtil instance;
    private List<Activity> listAct = new ArrayList<Activity>();//退出app使用
    private List<Activity> listCurrAct = new ArrayList<Activity>();//关闭多个使用

    private ActivityManagerUtil() {
    }
    //单列模式
    public static ActivityManagerUtil getActivityManager() {

        if (null == instance) {
            synchronized (ActivityManagerUtil.class) {
                if (null == instance) {
                    instance = new ActivityManagerUtil();
                }
            }
        }
        return instance;
    }
    //application管理所有activity,暂不用广播
    public void addActivity(Activity activity) {
        listAct.add(activity);
        Log.d("SJY", "Current Acitvity Size :" + getCurrentActivitySize());
    }

    public void removeActivity(Activity activity) {
        listAct.remove(activity);
        activity.finish();
        Log.d("SJY", "Current Acitvity Size :" + getCurrentActivitySize());
    }

    public void exit() {
        for (Activity activity : listAct) {
            if (activity != null){
                activity.finish();
        }
    }
    }

    public int getCurrentActivitySize() {
        return listAct.size();
    }

    //管理多个界面使用,不同于 管理所有界面
    public void addACT(Activity activity){
        listCurrAct.add(activity);
    }

    public void closeACT(){
        for (Activity activity : listCurrAct) {
            activity.finish();
        }
        //清空数据
        listCurrAct.clear();
    }
}
