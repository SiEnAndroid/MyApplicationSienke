package com.example.administrator.thinker_soft.meter_code.sk.uitls;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 布局工具类
 * Created by Administrator on 2018/4/9.
 */

public class LayoutUtil {
    //切图宽高比例值
    private float ratio;
    //手机屏幕高比
    private float mRatio;
    //屏幕的高
    private static float ScreenWidth = 0;
    //屏幕的宽
    private static float ScreenHeight = 0;
    //切图的宽
    private static float designWidth = 0;
    //切图的高
    private static float designHeight = 0;
    static LayoutUtil instance;

    public LayoutUtil() {
        ratio = designWidth / designHeight;
        mRatio = ScreenWidth / ScreenHeight;
    }


    /**
     * 单利获取LayoutUtil
     * @return
     */
    public static LayoutUtil getInstance() {

        if (instance == null) {
            synchronized (LayoutUtil.class) {
                if (instance == null) {
                    instance = new LayoutUtil();
                }
            }
        }

        return instance;
    }


    /**
     * @param deviceWidth 手机屏幕宽
     * @param deviceHeight 手机屏幕高
     * @param cutWidth 切图宽
     * @param cutHeight 切图高
     */
    public static void initConfig(float deviceWidth, float deviceHeight, float cutWidth,
                                  float cutHeight) {
        ScreenWidth = deviceWidth;
        ScreenHeight = deviceHeight;
        designWidth = cutWidth;
        designHeight = cutHeight;
    }


    /**
     * 注意此方法适用于当前控件的父布局是LinearLayout
     *
     * @param view 当前绘制的View
     * @param width 切图的宽
     * @param height 切图的高
     * @param marginLeft 切图标注距离左边的值
     * @param marginTop 切图标注距离顶部的值
     * @param marginRight 切图标注距离顶右边距离
     * @param marginBottom 切图标注距离底部的距离
     */
    public void drawViewLinearRBLayout(View view, float width, float height, float marginLeft,
                                       float marginRight,
                                       float marginTop, float marginBottom) {
        float ScreenHeight = ScreenWidth / ratio;

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();

        if (width == 0.0f) {

        } else {
            params.width = (int) (ScreenWidth * (width / designWidth));
        }
        if (height == 0.0f) {

        } else {
            params.height = (int) (ScreenHeight * (height / designHeight));
        }

        if (marginLeft == 0.0f) {

        } else {
            params.leftMargin = (int) (ScreenWidth * (marginLeft / designWidth));
        }

        if (marginTop == 0.0f) {

        } else {
            params.topMargin = (int) (ScreenHeight * (marginTop / designHeight));
        }
        if (marginRight == 0.0f) {

        } else {
            params.rightMargin = (int) (ScreenWidth * (marginRight / designWidth));
        }
        if (marginBottom == 0.0f) {

        } else {
            params.bottomMargin = (int) (ScreenHeight * (marginBottom / designWidth));
        }

        view.setLayoutParams(params);
    }


    /**
     * 注意此方法适用于当前控件的父布局是LinearLayout
     *
     * @param view 当前绘制的View
     * @param width 切图的宽
     * @param height 切图的高
     * @param marginLeft 切图标注距离左边的值
     * @param marginTop 切图标注距离顶部的值
     */
    public void drawViewLinearLayout(View view, float width, float height, float marginLeft,
                                     float marginTop) {

        float ScreenHeight = ScreenWidth / ratio;

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();

        if (width == 0.0f) {

        } else {
            params.width = (int) (ScreenWidth * (width / designWidth));
        }
        if (height == 0.0f) {

        } else {
            params.height = (int) (ScreenHeight * (height / designHeight));
        }

        if (marginLeft == 0.0f) {

        } else {
            params.leftMargin = (int) (ScreenWidth * (marginLeft / designWidth));
        }

        if (marginTop == 0.0f) {

        } else {
            params.topMargin = (int) (ScreenHeight * (marginTop / designHeight));
        }

        view.setLayoutParams(params);
    }

    /**
     * 注意此方法适用于当前控件的父布局是FrameLayout
     *
     * @param view 当前绘制的View
     * @param width 切图的宽
     * @param height 切图的高
     * @param marginLeft 切图标注距离左边的值
     * @param marginTop 切图标注距离顶部的值
     */
    public void drawViewFramLayout(View view, float width, float height, float marginLeft,
                                   float marginTop) {
        float ScreenHeight = ScreenWidth / ratio;

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();

        if (width == 0.0f) {

        } else {
            params.width = (int) (ScreenWidth * (width / designWidth));
        }
        if (height == 0.0f) {

        } else {
            params.height = (int) (ScreenHeight * (height / designHeight));
        }

        if (marginLeft == 0.0f) {

        } else {
            params.leftMargin = (int) (ScreenWidth * (marginLeft / designWidth));
        }

        if (marginTop == 0.0f) {

        } else {
            params.topMargin = (int) (ScreenHeight * (marginTop / designHeight));
        }

        view.setLayoutParams(params);
    }
    /**
     * 注意此方法适用于当前控件的父布局是FrameLayout
     *
     * @param view 当前绘制的View
     * @param width 切图的宽
     * @param height 切图的高
     * @param marginLeft 切图标注距离左边的值
     * @param marginTop 切图标注距离顶部的值
     */
    public void drawViewCardView(View view, float width, float height, float marginLeft,
                                   float marginTop) {
        float ScreenHeight = ScreenWidth / ratio;

        CardView.LayoutParams params = (CardView.LayoutParams) view.getLayoutParams();

        if (width == 0.0f) {

        } else {
            params.width = (int) (ScreenWidth * (width / designWidth));
        }
        if (height == 0.0f) {

        } else {
            params.height = (int) (ScreenHeight * (height / designHeight));
        }

        if (marginLeft == 0.0f) {

        } else {
            params.leftMargin = (int) (ScreenWidth * (marginLeft / designWidth));
        }

        if (marginTop == 0.0f) {

        } else {
            params.topMargin = (int) (ScreenHeight * (marginTop / designHeight));
        }

        view.setLayoutParams(params);
    }


    /**
     * 注意此方法适用于当前控件的父布局是RelativeLayout
     *
     * @param view 当前绘制的View
     * @param width 切图的宽
     * @param height 切图的高
     * @param marginLeft 切图标注距离左边的值
     * @param marginTop 切图标注距离顶部的值
     * @param marginRight 切图标注距离顶右边距离
     */
    public void drawViewlLayout(View view, float width, float height, float marginLeft,
                                float marginRight,
                                float marginTop) {
        float ScreenHeight = ScreenWidth / ratio;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

        if (width == 0.0f) {

        } else {
            params.width = (int) (ScreenWidth * (width / designWidth));
        }
        if (height == 0.0f) {

        } else {
            params.height = (int) (ScreenHeight * (height / designHeight));
        }

        if (marginLeft == 0.0f) {

        } else {
            params.leftMargin = (int) (ScreenWidth * (marginLeft / designWidth));
        }

        if (marginTop == 0.0f) {

        } else {
            params.topMargin = (int) (ScreenHeight * (marginTop / designHeight));
        }
        if (marginRight == 0.0f) {

        } else {
            params.rightMargin = (int) (ScreenWidth * (marginRight / designWidth));
        }
        view.setLayoutParams(params);
    }

    /**
     * 注意此方法适用于当前控件的父布局是RelativeLayout
     *
     * @param view 当前绘制的View
     * @param width 切图的宽
     * @param height 切图的高
     * @param marginLeft 切图标注距离左边的值
     * @param marginTop 切图标注距离顶部的值
     * @param marginRight 切图标注距离顶右边距离
     * @param marginBottom 切图标注距离底部的距离
     */
    public void drawViewRBLayout(View view, float width, float height, float marginLeft,
                                 float marginRight, float marginTop, float marginBottom) {
        float ScreenHeight = ScreenWidth / ratio;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

        if (width == 0.0f) {

        } else {
            params.width = (int) (ScreenWidth * (width / designWidth));
        }
        if (height == 0.0f) {

        } else {
            params.height = (int) (ScreenHeight * (height / designHeight));
        }

        if (marginLeft == 0.0f) {

        } else {
            params.leftMargin = (int) (ScreenWidth * (marginLeft / designWidth));
        }

        if (marginTop == 0.0f) {

        } else {
            params.topMargin = (int) (ScreenHeight * (marginTop / designHeight));
        }
        if (marginRight == 0.0f) {

        } else {
            params.rightMargin = (int) (ScreenWidth * (marginRight / designWidth));
        }
        if (marginBottom == 0.0f) {

        } else {
            params.bottomMargin = (int) (ScreenHeight * (marginBottom / designWidth));
        }
        view.setLayoutParams(params);
    }

}
