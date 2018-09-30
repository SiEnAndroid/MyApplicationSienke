package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/14.
 */
public class BusinessPopPublishActivity extends Activity {
    private ImageView back;
    private TextView popText,zhiliao,shenghuo,jianyi,jianwen;
    private PopupWindow window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pup_publish);//发布文章

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView(){
        back = (ImageView) findViewById(R.id.back);
        popText = (TextView) findViewById(R.id.pop_text);
    }

    public void setOnClickListener(){
        back.setOnClickListener(clickListener);
        popText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = BusinessPopPublishActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_pup_publish,null);
                window = new PopupWindow(popupView,600,400);
                window.setAnimationStyle(R.style.Popupwindow);
                window.setFocusable(true);
                backgroundAlpha(0.6F);   //背景变暗
                window.setOutsideTouchable(true);
                window.update();
                window.showAsDropDown(popText, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1.0F);
                        popText.setClickable(true);
                    }
                });
                zhiliao = (TextView) popupView.findViewById(R.id.zhiliao);
                shenghuo = (TextView) popupView.findViewById(R.id.shenghuo);
                jianyi = (TextView) popupView.findViewById(R.id.jianyi);
                jianwen = (TextView) popupView.findViewById(R.id.jianwen);

                zhiliao.setOnClickListener(clickListener);
                shenghuo.setOnClickListener(clickListener);
                jianyi.setOnClickListener(clickListener);
                jianwen.setOnClickListener(clickListener);
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.zhiliao:
                    popText.setText("常备资料");
                    window.dismiss();
                    break;
                case R.id.shenghuo:
                    popText.setText("文化生活");
                    window.dismiss();
                    break;
                case R.id.jianyi:
                    popText.setText("管理建议");
                    window.dismiss();
                    break;
                case R.id.jianwen:
                    popText.setText("趣事见闻");
                    window.dismiss();
                    break;
            }
        }
    };

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = BusinessPopPublishActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            BusinessPopPublishActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            BusinessPopPublishActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        BusinessPopPublishActivity.this.getWindow().setAttributes(lp);
    }
}
