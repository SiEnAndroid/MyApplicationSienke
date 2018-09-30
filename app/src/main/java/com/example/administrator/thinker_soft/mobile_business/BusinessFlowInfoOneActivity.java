package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/7/6.
 */
public class BusinessFlowInfoOneActivity extends Activity {
    private ImageView back;
    private RadioButton leave, overtime, evection, worker, dimission;
    private TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_flowinfo_one);

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        leave = (RadioButton) findViewById(R.id.leave);
        overtime = (RadioButton) findViewById(R.id.over_time);
        evection = (RadioButton) findViewById(R.id.evection);
        worker = (RadioButton) findViewById(R.id.worker);
        dimission = (RadioButton) findViewById(R.id.dimission);
        save = (TextView) findViewById(R.id.save);
    }

    private void setOnClickListener() {
        back.setOnClickListener(clickListener);
        leave.setOnClickListener(clickListener);
        overtime.setOnClickListener(clickListener);
        evection.setOnClickListener(clickListener);
        worker.setOnClickListener(clickListener);
        dimission.setOnClickListener(clickListener);
        save.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.save:
                    if (leave.isChecked()) {
                        Intent intent = new Intent(BusinessFlowInfoOneActivity.this, BusinessFlowLeaveActivity.class);//请假
                        startActivity(intent);
                        finish();
                    } else if (overtime.isChecked()) {
                        Intent intent1 = new Intent(BusinessFlowInfoOneActivity.this, BusinessFlowOvertimeActivity.class);//加班
                        startActivity(intent1);
                        finish();
                    } else if (evection.isChecked()) {
                        Intent intent2 = new Intent(BusinessFlowInfoOneActivity.this, BusinessFlowEvectionActivity.class);//出差
                        startActivity(intent2);
                        finish();
                    } else if (worker.isChecked()) {
                        Intent intent3 = new Intent(BusinessFlowInfoOneActivity.this, BusinessFlowWorkerActivity.class);//转正
                        startActivity(intent3);
                        finish();
                    } else if (dimission.isChecked()) {
                        Intent intent4 = new Intent(BusinessFlowInfoOneActivity.this, BusinessFlowDismissionActivity.class);
                        startActivity(intent4);
                        finish();
                    }
                    break;
            }
        }
    };
}
