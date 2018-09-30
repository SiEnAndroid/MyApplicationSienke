package com.example.administrator.thinker_soft.meter_code.sk.widget;

import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/4/26.
 */

public class UIHandler<T> extends Handler {

    protected WeakReference<T> ref;

    public UIHandler(T cls){
        ref = new WeakReference<T>(cls);
    }

    public T getRef(){
        return ref != null ? ref.get() : null;
    }
}