package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.adapter.PopupwindowListAdapter;
import com.example.administrator.thinker_soft.Security_check.model.NewTaskListviewItem;
import com.example.administrator.thinker_soft.Security_check.model.PopupwindowListItem;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.mode.ChangeDatePopwindow;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/3/15.
 */
public class NewTaskActivity extends Activity {
    private CardView taskName, securityType, startDate, endDate;// 安检类型
    private TextView securityTypeTv, startDateTv, endDateTv, tips, confirm;
    private EditText taskNameEt;//安检名称
   // private String ip, port;  //接口ip地址   端口
    private String result; //网络请求结果
    private SharedPreferences sharedPreferences, sharedPreferences_login, public_sharedPreferences;
    private SharedPreferences.Editor editor;
    private SQLiteDatabase db;  //数据库
    public int responseCode = 0;
    private ListView listView;
    private LayoutInflater inflater;  //转换器
    private ProgressBar progressBar;  //下载进度条
    private TextView progressName, progressPercent;
    private View typeView;
    private LinearLayout linearlayoutDown,confirm_layout;
    private PopupWindow popupWindow;
    private ImageView newTaskBack;
    private Button newPlanAddBtn;
    private Button save_btn, finishBtn;
    private View view;
    private LinearLayout rootLinearlayout;
    private ArrayList<NewTaskListviewItem> parclebleList = new ArrayList<>();
    private String resultTaskId;   //新增任务点保存时从服务器返回的任务编号
    private String userResult; //网络请求结果
    private JSONObject object;
    private int currentProgress = 0;
    private int currentPercent = 0;
    private Calendar c; //日历
    private List<PopupwindowListItem> popupwindowListItemList = new ArrayList<>();
    private PopupwindowListAdapter adapter;
    private int res, current_res;
    private String typeID;
    private String security_Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //输入法打开后布局上移
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_new_task);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setViewClickListener();//点击事件
    }

    //绑定控件
    private void bindView() {
        taskNameEt = (EditText) findViewById(R.id.task_name_et);
        newTaskBack = (ImageView) findViewById(R.id.newtask_back);
        newPlanAddBtn = (Button) findViewById(R.id.newplan_add_btn);
        taskName = (CardView) findViewById(R.id.task_name);
        securityType = (CardView) findViewById(R.id.security_type);
        securityTypeTv = (TextView) findViewById(R.id.security_type_tv);
        startDate = (CardView) findViewById(R.id.start_date);
        endDate = (CardView) findViewById(R.id.end_date);
        startDateTv = (TextView) findViewById(R.id.start_date_tv);
        endDateTv = (TextView) findViewById(R.id.end_date_tv);
        save_btn = (Button) findViewById(R.id.save_btn);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = NewTaskActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        public_sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        MySqliteHelper helper = new MySqliteHelper(NewTaskActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        startDateTv.setText(dateFormat.format(new Date()));
        endDateTv.setText(dateFormat.format(new Date()));
        new Thread() {
            @Override
            public void run() {
                getSecurityState();
            }
        }.start();
    }

    //点击事件
    private void setViewClickListener() {
        newTaskBack.setOnClickListener(onClickListener);
        newPlanAddBtn.setOnClickListener(onClickListener);
        taskName.setOnClickListener(onClickListener);
        securityType.setOnClickListener(onClickListener);
        startDate.setOnClickListener(onClickListener);
        endDate.setOnClickListener(onClickListener);
        save_btn.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.newtask_back:
                    NewTaskActivity.this.finish();
                    break;
                case R.id.newplan_add_btn:
                    if ("复检".equals(securityTypeTv.getText().toString())) {
                        Intent intent1 = new Intent(NewTaskActivity.this, NewTaskDetailActivity.class);
                        intent1.putExtra("security_type", "复检");
                        startActivityForResult(intent1, 100);
                    } else if ("暂无".equals(securityTypeTv.getText().toString())) {
                        Toast.makeText(NewTaskActivity.this, "安检类型不能为空！", Toast.LENGTH_SHORT).show();
                    } else if("点击选择".equals(securityTypeTv.getText().toString())){
                        Toast.makeText(NewTaskActivity.this, "请选择安检类型！", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent1 = new Intent(NewTaskActivity.this, NewTaskDetailActivity.class);
                        intent1.putExtra("security_type", "不是复检");
                        startActivityForResult(intent1, 100);
                    }
                    break;
                case R.id.save_btn:
                    save_btn.setEnabled(false);
                    if (parclebleList.size() != 0) {
                        if (!taskNameEt.getText().toString().equals("")) {
                            String str1 = startDateTv.getText().toString();
                            String str2 = endDateTv.getText().toString();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String currentTime = formatter.format(new Date());
                            current_res = str2.compareTo(currentTime);
                            if (current_res >= 0) {
                                res = str1.compareTo(str2);
                                Log.i("NewTaskActivity", "比较结果:" + res);
                                if (res <= 0) {
                                    showPopupwindow();
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            postMyTask();
                                        }
                                    }.start();
                                } else {
                                    Toast.makeText(NewTaskActivity.this, "开始时间不能大于结束时间哦！", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(NewTaskActivity.this, "结束时间不能小于当天时间哦！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(NewTaskActivity.this, "任务名称不能为空哦！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(NewTaskActivity.this, "请添加用户数据哦！", Toast.LENGTH_SHORT).show();
                    }
                    save_btn.setEnabled(true);
                    break;
                case R.id.security_type:
                    if (!"暂无".equals(securityTypeTv.getText().toString())) {
                        createSecurityTypePopupwindow();
                    } else {
                        Toast.makeText(NewTaskActivity.this, "安检类型列表为空！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.start_date:
//                    startDate.setClickable(false);
                    //开始时间选择器
                  //  selectDate(startDateTv);
                    DatePickerDialog startDateDialog = new DatePickerDialog(NewTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            startDateTv.setText(new StringBuilder().append(year).append("-").append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "")
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
//                    endDate.setClickable(false);
                    //结束时间选择器
                  //  selectDate(endDateTv);
                    DatePickerDialog endDateDialog = new DatePickerDialog(NewTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            endDateTv.setText(new StringBuilder().append(year).append("-").append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "")
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
                default:
                    break;
            }
        }
    };
    private String[] selectDate(final TextView textView) {
        final String[] str = new String[10];
        ChangeDatePopwindow mChangeBirthDialog = new ChangeDatePopwindow(this);

        mChangeBirthDialog.setDate( mChangeBirthDialog.getYear(), "1", "1");
        mChangeBirthDialog.showAtLocation(textView, Gravity.CENTER, 0, 0);
        mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow.OnBirthListener() {

            @Override
            public void onClick(String year, String month, String day) {
                // TODO Auto-generated method stub
//                Toast.makeText(MainActivity.this,year + "-" + month + "-" + day,Toast.LENGTH_LONG).show();
                StringBuilder sb = new StringBuilder();
                sb.append(year.substring(0, year.length() - 1)).append("-").append(month.substring(0, day.length() - 1)).append("-").append(day);
                str[0] = year + "-" + month + "-" + day;
                str[1] = sb.toString();
                int m = Integer.valueOf(month);
                int d = Integer.valueOf(day);
                String month1;
                String day1;
                if (m <10 ){
                    month1 = "0"+month;
                }else {
                    month1 =  month;
                }
                if (d <10 ){
                    day1 = "0"+day;
                }else {
                    day1 =  day;
                }
                textView.setText(year + "-" + month1 + "-" + day1);

            }
        });
        return str;
    }

    //弹出安检类型popupwindow
    public void createSecurityTypePopupwindow() {
        inflater = LayoutInflater.from(NewTaskActivity.this);
        typeView = inflater.inflate(R.layout.popupwindow_security_hidden_type_or_reason, null);
        popupWindow = new PopupWindow(typeView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        tips = (TextView) typeView.findViewById(R.id.tips);
        listView = (ListView) typeView.findViewById(R.id.list_view);
        confirm = (TextView) typeView.findViewById(R.id.confirm);
        confirm_layout = (LinearLayout) typeView.findViewById(R.id.confirm_layout);
        tips.setText("请选择安检类型");
        confirm.setText("取消");
        adapter = new PopupwindowListAdapter(NewTaskActivity.this, popupwindowListItemList, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupwindowListItem item = (PopupwindowListItem) adapter.getItem(position);
                typeID = item.getItemId();
                securityTypeTv.setText(item.getItemName());
                popupWindow.dismiss();
            }
        });
        confirm_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.Popupwindow);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }


    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = NewTaskActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            NewTaskActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            NewTaskActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        NewTaskActivity.this.getWindow().setAttributes(lp);
    }

    //请求网络数据
    private void postMyTask() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //请求的地址
                    //String httpUrl = "http://" + ip + port + "/SMDemo/addSafePlan.do";
                    String httpUrl=new StringBuffer().append(SkUrl.SkHttp(NewTaskActivity.this)).append("addSafePlan.do").toString();
                    Log.i("postMyTask_url====>", "" + httpUrl);
                    // 根据地址创建URL对象
                    URL url = new URL(httpUrl);
                    // 根据URL对象打开链接
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    // 发送POST请求必须设置允许输出
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setUseCaches(false);//不使用缓存
                    // 设置请求的方式
                    urlConnection.setRequestMethod("POST");
                    // 设置请求的超时时间
                    urlConnection.setReadTimeout(8000);
                    urlConnection.setConnectTimeout(8000);
                    // 传递的数据
                    String data = dataToJson();
                    Log.i("postMyTask_data=>", "data=" + data);
                    // 设置请求的头
                    urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    urlConnection.setRequestProperty("Content-Type", "applicaton/json;charset=UTF-8");
                    //urlConnection.setRequestProperty("Origin", "http://"+ ip + port);
                    //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                    //获取输出流
                    OutputStream os = urlConnection.getOutputStream();
                    os.write(data.getBytes("UTF-8"));
                    os.flush();
                    os.close();
                    Log.i("getResponseCode====>", "" + urlConnection.getResponseCode());
                    if ((responseCode = urlConnection.getResponseCode()) == 200) {
                        InputStream inputStream = urlConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        // 释放资源
                        inputStream.close();
                        // 返回字符串
                        result = stringBuilder.toString();
                        Log.i("postMyTask_result====>", result);
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.optString("message", "").equals("1")) {
                            handler.sendEmptyMessage(1);
                        } else if (jsonObject.optString("message", "").equals("2")) {
                            handler.sendEmptyMessage(10);
                        } else if (jsonObject.optString("message", "").equals("3")) {
                            handler.sendEmptyMessage(11);
                        } else {
                            handler.sendEmptyMessage(2);
                        }
                    } else {
                        handler.sendEmptyMessage(3);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException==========>", "网络请求异常!");
                    handler.sendEmptyMessage(2);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    //将数据转换成Json格式
    public String dataToJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject object = new JSONObject();
            object.put("c_safety_plan_name", taskNameEt.getText().toString());      //安检任务名称
            object.put("c_safety_plan_member", sharedPreferences_login.getString("user_name", ""));    //操作员
            object.put("d_safety_start", startDateTv.getText().toString());       //开始时间
            object.put("d_safety_end", endDateTv.getText().toString());    //结束时间
            object.put("n_company_id", sharedPreferences_login.getInt("company_id", 0));       //公司id
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < parclebleList.size(); i++) {
                JSONObject object1 = new JSONObject();
                object1.put("c_user_id", parclebleList.get(i).getUserId());
                object1.put("n_data_state", 1);
                object1.put("n_safety_state", 0);
                object1.put("n_safety_date_type", 0);
                object1.put("c_safety_type", Integer.parseInt(typeID));       //安检类型
                jsonArray.put(i, object1);
            }
            jsonObject.put("safetyInspection", jsonArray);
            jsonObject.put("safetyPlan", object);
            Log.i("dataToJson==========>", "封装的json数据为：" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //将新增的任务数据存到本地数据库任务表
    private void insertTaskDataBase() {
        ContentValues values = new ContentValues();
        values.put("taskName", taskNameEt.getText().toString());
        values.put("taskId", resultTaskId);
        values.put("securityType", securityTypeTv.getText().toString());
        values.put("totalCount", parclebleList.size() + "");
        values.put("endTime", endDateTv.getText().toString());
        values.put("loginUserId", sharedPreferences_login.getString("userId", ""));
        values.put("restCount", parclebleList.size() + "");
        db.insert("Task", null, values);
    }

    //将添加的用户信息数据存到本地数据库用户表
    private void insertUserDataBase(String securityId) {
        ContentValues values = new ContentValues();
        values.put("securityNumber", securityId);
        if(!"null".equals(object.optString("userName", ""))){
            values.put("userName", object.optString("userName", ""));
        }else {
            values.put("userName", "暂无");
        }
        if(!"null".equals(object.optString("meterNumber", ""))){
            values.put("meterNumber", object.optString("meterNumber", ""));
        }else {
            values.put("meterNumber", "暂无");
        }
        if(!"null".equals(object.optString("userPhone", ""))){
            values.put("userPhone", object.optString("userPhone", ""));
        }else {
            values.put("userPhone", "暂无");
        }
        if(!"null".equals(object.optString("securityName", ""))){
            values.put("securityType", object.optString("securityName", ""));
        }else {
            values.put("securityType", "暂无");
        }
        if(!"null".equals(object.optString("oldUserId", ""))){
            values.put("oldUserId", object.optString("oldUserId", ""));
        }else {
            values.put("oldUserId", "暂无");
        }
        if(!"null".equals(object.optString("userId", ""))){
            values.put("newUserId", object.optString("userId", ""));
        }else {
            values.put("newUserId", "暂无");
        }
        if(!"null".equals(object.optString("userAdress", ""))){
            values.put("userAddress", object.optString("userAdress", ""));
        }else {
            values.put("userAddress", "暂无");
        }
        values.put("taskId", resultTaskId);
        values.put("ifChecked", "false");
        values.put("security_case", "");
        values.put("remarks", "");
        values.put("security_hidden", "");
        values.put("security_hidden_reason", "");
        values.put("security_case_id","");
        values.put("security_hidden_id","");
        values.put("security_hidden_reason_id","");
        values.put("photoNumber", "0");
        values.put("ifUpload", "false");
        values.put("currentTime", "");
        values.put("ifPass", "");
        values.put("loginName", sharedPreferences_login.getString("login_name", ""));
        values.put("loginUserId", sharedPreferences_login.getString("userId", ""));
        values.put("security_state", "0");
        if(!"null".equals(object.optString("c_properties_name", ""))){
            values.put("userProperty", object.optString("c_properties_name", ""));    //用户性质
        }else {
            values.put("userProperty", "暂无");
        }
        db.insert("User", null, values);
    }

    //根据数据库返回回来的任务编号去查询相应用户的安检编号
    private void requireSecurityId(final String method, final String keyAndValue) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url;
                    HttpURLConnection httpURLConnection;
                    Log.i("sharedPreferences====>", sharedPreferences.getString("IP", ""));
                   // String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
                    String httpUrl=new StringBuffer().append(SkUrl.SkHttp(NewTaskActivity.this)).append(method).toString();
                    //有参数传递
                    if (!keyAndValue.equals("")) {
                        url = new URL(httpUrl + "?" + keyAndValue);
                        //没有参数传递
                    } else {
                        url = new URL(httpUrl);
                    }
                    Log.i("url=============>", url + "");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(6000);
                    httpURLConnection.setReadTimeout(6000);
                    httpURLConnection.connect();
                    //传回的数据解析成String
                    Log.i("responseCode====>", httpURLConnection.getResponseCode() + "");
                    if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        userResult = stringBuilder.toString();
                        Log.i("userResult=====>", userResult);
                        JSONObject jsonObject = new JSONObject(userResult);
                        if (!jsonObject.optString("total", "").equals("0")) {
                            handler.sendEmptyMessage(4);
                        } else {
                            handler.sendEmptyMessage(5);
                        }
                    } else {
                        try {
                            Thread.sleep(3000);
                            handler.sendEmptyMessage(6);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException==========>", "网络请求异常!");
                    handler.sendEmptyMessage(6);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //show弹出框
    public void showPopupwindow() {
        inflater = LayoutInflater.from(NewTaskActivity.this);
        view = inflater.inflate(R.layout.popupwindow_download_progressbar, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearlayoutDown = (LinearLayout) view.findViewById(R.id.linearlayout_down);
        finishBtn = (Button) view.findViewById(R.id.finish_btn);
        progressBar = (ProgressBar) view.findViewById(R.id.download_progress);
        progressName = (TextView) view.findViewById(R.id.progress_name);
        progressPercent = (TextView) view.findViewById(R.id.progress_percent);
        progressBar.setMax(50);
        progressName.setText("任务正在保存，请稍后...");
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                popupWindow.dismiss();
                save_btn.setClickable(true);
                NewTaskActivity.this.finish();
            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
                save_btn.setClickable(true);
            }
        });
    }


    public void setProgress() {
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 5; i++) {
                        Thread.sleep(300);
                        currentProgress += 10 * 5 / 5;
                        currentPercent = (1000 * (i + 1)) / (10 * 5);
                        Message msg = new Message();
                        msg.what = 7;
                        msg.arg1 = currentProgress;
                        msg.arg2 = currentPercent;
                        handler.sendMessage(msg);
                        Log.i("upload_task_progress=>", " 更新进度条" + currentProgress);
                        Log.i("upload_task_progress=>", " 下载进度: " + currentPercent);
                    }
                    handler.sendEmptyMessage(8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject taskObject = new JSONObject(result);
                        resultTaskId = taskObject.optInt("safetyPlanId", 0) + "";
                        requireSecurityId("getUserCheck.do", "safetyPlan=" + resultTaskId + "&safetyState=0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    popupWindow.dismiss();
                    Toast.makeText(NewTaskActivity.this, "操作异常！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    popupWindow.dismiss();
                    Toast.makeText(NewTaskActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    try {
                        setProgress();
                        JSONObject userObject = new JSONObject(userResult);
                        JSONArray array = userObject.getJSONArray("rows");
                        for (int i = 0; i < array.length(); i++) {
                            object = array.getJSONObject(i);
                            insertUserDataBase(String.valueOf(object.optInt("safetyInspectionId", 0)));  //将添加的用户信息数据存到本地数据库用户表
                        }
                        insertTaskDataBase();  //将新增的任务数据存到本地数据库任务表
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    Toast.makeText(NewTaskActivity.this, "任务编号请求数据为空！", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(NewTaskActivity.this, "任务编号请求数据错误！", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    progressPercent.setText(String.valueOf(msg.arg2));
                    progressBar.setProgress(msg.arg1);
                    Log.i("down_progress=>", " 任务进度为：" + progressBar.getProgress());
                    break;
                case 8:
                    progressName.setText("任务新增成功！");
                    linearlayoutDown.setVisibility(View.GONE);
                    finishBtn.setVisibility(View.VISIBLE);
                    currentProgress = 0;
                    currentPercent = 0;
                    break;
                case 9:
                    securityTypeTv.setText("暂无");
                    break;
                case 10:
                    //获取计划id失败
                    popupWindow.dismiss();
                    Toast.makeText(NewTaskActivity.this, "获取计划id失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 11:
                    popupWindow.dismiss();
                    Toast.makeText(NewTaskActivity.this, "获取安检用户id失败！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //读取安检状态信息
    public void getSecurityState() {
        popupwindowListItemList.clear();
        //cursor = db.rawQuery("select * from SecurityState where loginName=?", new String[]{sharedPreferences_login.getString("login_name", "")});//查询并获得游标
        Cursor cursor = db.query("SecurityState", null, null, null, null, null, null);//查询并获得游标
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(9);
            return;
        }
        while (cursor.moveToNext()) {
            PopupwindowListItem item = new PopupwindowListItem();
            item.setItemId(cursor.getString(cursor.getColumnIndex("securityId")));
            item.setItemName(cursor.getString(cursor.getColumnIndex("securityName")));
            popupwindowListItemList.add(item);
        }
        Log.i("getSecurityState=>", " 安检状态个数为：" + popupwindowListItemList.size());
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                if (data != null) {
                    parclebleList = data.getParcelableArrayListExtra("parclebleList");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭数据库
        db.close();
    }
}
