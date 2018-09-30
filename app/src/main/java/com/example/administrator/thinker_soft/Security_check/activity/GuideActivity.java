package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/2/24 0024.
 */
public class GuideActivity extends Activity {
    private TextView companyName;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        bindView();//绑定控件
        setCompanyNameAnimation();//设置动画

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                /*boolean b = sharedPreferences.getBoolean("is_first",true);
                if(b){
                    sharedPreferences.edit().putBoolean("is_first",false).commit();
                }*/
                Intent intent = new Intent(GuideActivity.this,QueryActivity.class);
                startActivity(intent);
                finish();
            }
        }.sendEmptyMessageDelayed(0,3500);

    }

    //绑定控件ID
    private void bindView(){
        companyName = (TextView) findViewById(R.id.company_name);
    }

    //设置动画
    private void setCompanyNameAnimation(){
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/classic_empty_fold_round.TTF");
        companyName.setTypeface(typeface);
        companyName.setAnimation(AnimationUtils.loadAnimation(GuideActivity.this,R.anim.text_scale_alpha));
    }
}
