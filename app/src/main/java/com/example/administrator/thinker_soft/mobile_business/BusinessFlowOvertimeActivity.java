package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/7.
 */
public class BusinessFlowOvertimeActivity extends Activity {

    private TextView name, section, data, startDate, endDate, save,id;
    private EditText content;
    private ImageView back;
    private SimpleDateFormat dateFormat, dateFormat1;
    private SharedPreferences sharedPreferences_login;
    private SQLiteDatabase db;  //数据库
    private Calendar c; //日历
    private int res, current_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_flow_overtime);//加班申请

        bindView();//绑定控件
        defaultSetting();
        setOnClickListener();//点击事件
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        section = (TextView) findViewById(R.id.section);
        data = (TextView) findViewById(R.id.data);
        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
        section = (TextView) findViewById(R.id.section);
        save = (TextView) findViewById(R.id.save);
        name  = (TextView) findViewById(R.id.name);
        id = (TextView) findViewById(R.id.id);
        content  = (EditText) findViewById(R.id.content);
    }

    private void defaultSetting() {
        c = Calendar.getInstance();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        MySqliteHelper helper = new MySqliteHelper(BusinessFlowOvertimeActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        name.setText(sharedPreferences_login.getString("user_name", ""));
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        data.setText(dateFormat.format(new Date()));
        startDate.setText(dateFormat1.format(new Date()));
        endDate.setText(dateFormat1.format(new Date()));
    }

    private void setOnClickListener() {
        back.setOnClickListener(clickListener);
        startDate.setOnClickListener(clickListener);
        endDate.setOnClickListener(clickListener);
        save.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.start_date:
                    //开始时间选择器
                    DatePickerDialog startDateDialog = new DatePickerDialog(BusinessFlowOvertimeActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            startDate.setText(new StringBuilder().append(year).append("-").append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "")
                                    .append("-")
                                    .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth + ""));
                            startDate.setClickable(true);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    startDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            startDate.setClickable(true);
                        }
                    });
                    startDateDialog.show();
                    break;
                case R.id.end_date:
                    DatePickerDialog endDateDialog = new DatePickerDialog(BusinessFlowOvertimeActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            endDate.setText(new StringBuilder().append(year).append("-").append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "")
                                    .append("-")
                                    .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth + ""));
                            endDate.setClickable(true);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    endDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            endDate.setClickable(true);
                        }
                    });
                    endDateDialog.show();
                    break;
                case R.id.save:
                    String str1 = startDate.getText().toString();
                    String str2 = endDate.getText().toString();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String currentTime = formatter.format(new Date());
                    current_res = str2.compareTo(currentTime);
                    if (current_res >= 0) {
                        res = str1.compareTo(str2);
                        if (res <= 0) {
                            finish();
                        } else {
                            save.setClickable(true);
                            Toast.makeText(BusinessFlowOvertimeActivity.this, "开始时间不能大于结束时间哦！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        save.setClickable(true);
                        Toast.makeText(BusinessFlowOvertimeActivity.this, "结束时间不能小于当天时间哦！", Toast.LENGTH_SHORT).show();
                    }
                    insertFlow();
                    break;
            }
        }
    };
    /**
     * 将信息保存到本地数据库OA工作汇报表
     */
    private void insertFlow() {
        ContentValues values = new ContentValues();
        values.put("userId", sharedPreferences_login.getString("userId", ""));
        values.put("userName", sharedPreferences_login.getString("user_name", ""));
        values.put("type", id.getText().toString().trim());
        values.put("section", section.getText().toString().trim());
        values.put("date", data.getText().toString().trim());
        values.put("content", content.getText().toString().trim());
        values.put("startDate", startDate.getText().toString().trim());
        values.put("endDate", endDate.getText().toString().trim());
        db.insert("Flow", null, values);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
