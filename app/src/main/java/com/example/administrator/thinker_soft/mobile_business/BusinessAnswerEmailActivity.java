package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/15.
 */
public class BusinessAnswerEmailActivity extends Activity {

    private ImageView back;
    private RelativeLayout send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_answer_email);//回复

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView(){
        back = (ImageView) findViewById(R.id.back);
        send = (RelativeLayout) findViewById(R.id.send);
    }
    public void setOnClickListener(){
        back.setOnClickListener(clickListener);
        send.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
            }
        }
    };
}
