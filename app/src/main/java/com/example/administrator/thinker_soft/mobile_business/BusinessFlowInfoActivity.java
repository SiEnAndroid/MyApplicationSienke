package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/15.
 */
public class BusinessFlowInfoActivity extends Activity {
    private ImageView back;
    private TextView time;
    private Calendar c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_flow_info);//流程审批详细

        bindView();
        setOnClickListener();
        defaultSetting();
    }

    private void defaultSetting() {
        c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        time.setText(dateFormat.format(new Date()));
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        time = (TextView) findViewById(R.id.time);
    }

    public void setOnClickListener() {
        back.setOnClickListener(clickListener);
        time.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.time:
                    time.setClickable(false);
                    //开始时间选择器
                    DatePickerDialog startDateDialog = new DatePickerDialog(BusinessFlowInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            time.setText(new StringBuilder().append(year).append("-").append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "")
                                    .append("-")
                                    .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth + ""));
                            time.setClickable(true);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    startDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            time.setClickable(true);
                        }
                    });
                    startDateDialog.show();
                    break;
            }
        }
    };
}
