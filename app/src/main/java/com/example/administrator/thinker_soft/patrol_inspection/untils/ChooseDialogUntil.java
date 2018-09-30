package com.example.administrator.thinker_soft.patrol_inspection.untils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

public class ChooseDialogUntil {
    private static Dialog dialog;

    //Dialog提示框消失方法
    public static void dialogDismiss() {
        if (isDialogShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    //Dialog提示框是否正在运行
    public static boolean isDialogShowing() {
        return dialog != null && dialog.isShowing();
    }

    //创建Dialog提示框
    private static void createDialog(Activity activity) {
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.patrol_choose_dialog);
        // 点击弹窗外区域，弹窗不消失
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();   //实例化Window
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); //实例化Window操作者
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        dialogWindow.setAttributes(lp);//放置属性
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);  //size.x就/是宽度，size.y就是高度
//        dialog.getWindow().setLayout(size.x - 80, size.y / 3);//设置宽高
        setHeight(activity);
        dialog.show();
    }

    /**
     * 显示提示框
     *
     * @param activity 当前Activity
     */
    public static void showSecurityCodeInputDialog(final Activity activity, String title) {
        if (dialog == null) {
            createDialog(activity);
        } else {
            dialog = null;
            createDialog(activity);
        }

        Window dialogWindow = dialog.getWindow();
        Button cancel = (Button) dialogWindow.findViewById(R.id.choose_dialog_cancle);
        TextView tv_hint = (TextView) dialogWindow.findViewById(R.id.choose_dialog_title);
        tv_hint.setText(title);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDismiss();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    dialogDismiss();
                    return true;
                }
                return false;
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialogDismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialogDismiss();
            }
        });

    }

    public static View getView(int viewId) {
        return dialog.getWindow().findViewById(viewId);
    }

    private static void setHeight(Context context) {
        Window window = dialog.getWindow();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (window.getDecorView().getHeight() >= (int) (displayMetrics.heightPixels * 0.6)) {
            attributes.height = (int) (displayMetrics.heightPixels * 0.6);
        }
        window.setAttributes(attributes);
    }
}