package com.example.administrator.thinker_soft.meter_code.sk.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2018/5/18.
 */

public class LoadingView extends ProgressDialog {

    private TextView loadingText;//提示文字
    private String title;
    private Activity mActivity;

    public LoadingView(Activity activity, String loadText) {
        super(activity);
        this.title=loadText;
        this.mActivity=activity;
    }
    public LoadingView(Activity activity, int theme, String loadText) {
        super(activity, theme);
        this.title=loadText;
        this.mActivity=activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }
    private void init(Context context) {
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.view_loading);//loading的xml文件
        Log.i("LoadingView=>", "加载中：" );
        loadingText= (TextView) findViewById(R.id.tv_load_dialog);
        loadingText.setText(title);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);


    }
    @Override
    public void show() {//开启
        super.show();
    }


    @Override
    public void dismiss() {//关闭
        super.dismiss();
    }
}
