package com.example.administrator.thinker_soft.mode;

import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/7/13 0013.
 */
public class MyAnimationUtils {
    /**
     * viewGroup出来动画
     */
    public static void viewGroupOutAnimation(Context context, ViewGroup viewGroup,float delay) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.list_out_anim);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setInterpolator(new AccelerateInterpolator());
        controller.setDelay(delay);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        viewGroup.setLayoutAnimation(controller);
    }

    /**
     * 透明度从无到有动画
     */
    public static void viewGroupOutAlphaAnimation(Context context, ViewGroup viewGroup,float delay) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.user_list_out_anim);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setInterpolator(new AccelerateInterpolator());
        controller.setDelay(delay);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        viewGroup.setLayoutAnimation(controller);
    }

    /**
     * 从底部弹出动画
     */
    public static void viewGroupBottomInAnimation(Context context, ViewGroup viewGroup,float delay) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.camera_enter);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setInterpolator(new AccelerateInterpolator());
        controller.setDelay(delay);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        viewGroup.setLayoutAnimation(controller);
    }

    /**
     * 从底部消失动画
     */
    public static void viewGroupBottomOutAnimation(Context context, ViewGroup viewGroup,float delay) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.camera_out);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setInterpolator(new AccelerateInterpolator());
        controller.setDelay(delay);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        viewGroup.setLayoutAnimation(controller);
    }

    /**
     * viewGroup返回动画
     */
    public static void viewGroupBackAnimation(Context context,ViewGroup viewGroup,float delay) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.out_anim);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setInterpolator(new AccelerateInterpolator());
        controller.setDelay(delay);
        controller.setOrder(LayoutAnimationController.ORDER_REVERSE);
        viewGroup.setLayoutAnimation(controller);
    }
}
