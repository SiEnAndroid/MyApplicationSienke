package com.example.administrator.thinker_soft.meter_code.sk.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class PhotoViewPager extends ViewPager {

    //
//    public PhotoViewPager(Context context) {
//        super(context);
//    }
//
//    public PhotoViewPager(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        try {
//            return super.dispatchTouchEvent(ev);
//        } catch (IllegalArgumentException ignored) {
//        } catch (ArrayIndexOutOfBoundsException e) {
//        }
//        return false;
//
//    }
    public PhotoViewPager(Context context) {
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

}
