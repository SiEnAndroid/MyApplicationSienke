package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/13.
 */
public class BusinessDataInfoActivity extends Activity {
    private ImageView back;
    private CheckBox slip;
    private TextView startDate, endDate;
    private EditText title, customerName, address, detail;
    private Calendar c;//日历
    private Cursor cursor;
    private Button saveBtn;
    private SimpleDateFormat dateFormat, dateFormat1;
    private int res, current_res;
    private SharedPreferences sharedPreferences_login;
    private SQLiteDatabase db;  //数据库
    private LayoutInflater inflater;  //转换器
    private View view;
    private PopupWindow popupWindow;
    private LinearLayout dateLayout,timeLayout,rootLinearlayout;
    private RadioButton cancelRb,saveRb;
    private DatePicker date;
    private TimePicker time;
    private boolean dateChanged = false;
    private boolean timeChanged = false;
    private String dataString, timeString;
    private int year, month, day, hour, minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_data_info);//日程详细

        bindView();//绑定控件
        defaultSetting();
        setOnClickListener();//点击事件
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        title = (EditText) findViewById(R.id.title);
        customerName = (EditText) findViewById(R.id.customer_name);
        address = (EditText) findViewById(R.id.address);
        detail = (EditText) findViewById(R.id.detail);
        slip = (CheckBox) findViewById(R.id.slip);
        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
        saveBtn = (Button) findViewById(R.id.save_btn);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(BusinessDataInfoActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        c = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        startDate.setText(dateFormat1.format(new Date()));
        endDate.setText(dateFormat1.format(new Date()));
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.AM_PM);
        minutes = c.get(Calendar.MINUTE);
    }

    //点击事件
    private void setOnClickListener() {
        back.setOnClickListener(clickListener);
        slip.setOnClickListener(clickListener);
        startDate.setOnClickListener(clickListener);
        endDate.setOnClickListener(clickListener);
        saveBtn.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.slip:
                    if (slip.isChecked()) {
                        startDate.setText(dateFormat.format(new Date()));
                        endDate.setText(dateFormat.format(new Date()));
                    } else {
                        startDate.setText(dateFormat1.format(new Date()));
                        endDate.setText(dateFormat1.format(new Date()));
                    }
                    break;
                case R.id.start_date:
                    showdateAndTimePopup();
                    break;
                case R.id.end_date:
                    showdateAndTimePopup();
                    break;
                case R.id.save_btn:
                    insertOaCalendar();
                    saveBtn.setClickable(false);
                    String str = endDate.getText().toString();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm");
                    String currentTime = formatter.format(new Date());
                    current_res = str.compareTo(currentTime);
                    if (current_res <= 0) {
                        saveBtn.setClickable(true);
                        Toast.makeText(BusinessDataInfoActivity.this, "开始时间不能大于结束时间哦！", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent2 = new Intent();
                        setResult(Activity.RESULT_OK, intent2);
                        finish();
                    }
                    break;
            }
        }
    };


    //弹出选择日期时间选择框
    public void showdateAndTimePopup() {
        inflater = LayoutInflater.from(BusinessDataInfoActivity.this);
        view = inflater.inflate(R.layout.popupwindow_business_datatimepicker, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        date = (DatePicker) view.findViewById(R.id.date);
        time = (TimePicker) view.findViewById(R.id.time);
        dateLayout = (LinearLayout) view.findViewById(R.id.date_layout);
        timeLayout = (LinearLayout) view.findViewById(R.id.time_layout);
        cancelRb = (RadioButton) view.findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) view.findViewById(R.id.save_rb);
        //设置点击事件
        date.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateChanged = true;
                BusinessDataInfoActivity.this.year = year;
                BusinessDataInfoActivity.this.month = monthOfYear;
                BusinessDataInfoActivity.this.day = dayOfMonth;
                dataString = year + "-" + (month+1) + "-" + day;
            }
        });

        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeChanged = true;
                BusinessDataInfoActivity.this.hour = hourOfDay;
                BusinessDataInfoActivity.this.minutes = minute;
                timeString = hour + ":" + minutes;
            }
        });
        cancelRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        saveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateLayout.getVisibility() == View.VISIBLE){
                    dateLayout.setVisibility(View.GONE);
                    timeLayout.setVisibility(View.VISIBLE);
                }else {
                    if(dateChanged){   //日期改变了
                        dateChanged = false;
                        if(timeChanged){  //时间改变了
                            try {
                                timeChanged = false;
                                startDate.setText(dateFormat.format(dateFormat1.parse(dataString+timeString)));
                                Log.i("showdateAndTimePopup","日期改变，时间改变了"+startDate.getText());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else {
                            try {
                                StringBuilder sb = new StringBuilder(dateFormat1.format(new Date()));
                                sb.replace(0,10,dataString);
                                startDate.setText(dateFormat1.format(dateFormat1.parse(sb.toString())));
                                Log.i("showdateAndTimePopup","日期改变，时间未改变进来了"+startDate.getText());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {    //日期不改变
                        if(timeChanged){  //时间改变了
                            try {
                                timeChanged = false;
                                StringBuilder sb = new StringBuilder(dateFormat1.format(new Date()));
                                sb.replace(11,16,timeString);
                                startDate.setText(dateFormat1.format(dateFormat1.parse(sb.toString())));
                                Log.i("showdateAndTimePopup","日期未改变，时间改变了"+sb.toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else {
                            startDate.setText(dateFormat1.format(new Date()));
                        }
                    }
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
                saveBtn.setClickable(true);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = BusinessDataInfoActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            BusinessDataInfoActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            BusinessDataInfoActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        BusinessDataInfoActivity.this.getWindow().setAttributes(lp);
    }

    /**
     * 将信息保存到本地数据库OA日程表
     */
    private void insertOaCalendar() {
        ContentValues values = new ContentValues();
        values.put("userId", sharedPreferences_login.getString("userId", ""));
        values.put("title", title.getText().toString().trim());
        values.put("isAllDay", slip.isChecked() + "");
        values.put("beginTime", startDate.getText().toString().trim());
        values.put("endTime", endDate.getText().toString().trim());
        values.put("participant", customerName.getText().toString().trim());
        values.put("address", address.getText().toString().trim());
        values.put("details", detail.getText().toString().trim());
        db.insert("OaCalendar", null, values);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }
}

