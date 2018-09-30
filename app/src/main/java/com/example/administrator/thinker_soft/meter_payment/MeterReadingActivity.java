package com.example.administrator.thinker_soft.meter_payment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallbackStringListener;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.EditTextUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.HttpUtils;
import com.example.administrator.thinker_soft.meter_payment.Utils.MyDialog;
import com.example.administrator.thinker_soft.mode.Tools;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 在线缴费
 */
public class MeterReadingActivity extends AppCompatActivity {
    private  String monthDegree="0";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.userlist_tv)
    TextView tv_userlistTv;
    @BindView(R.id.save)
    ImageView save;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.longitude)
    TextView tv_longitude;
    @BindView(R.id.latitude)
    TextView tv_latitude;
    @BindView(R.id.address)
    TextView tv_address;
    @BindView(R.id.location)
    ImageView iv_location;
    @BindView(R.id.location_cardview)
    CardView cv_locationCardview;
    @BindView(R.id.this_month_end_degree)
    EditText et_thisMonthEndDegree;//本月读数
    @BindView(R.id.this_month_dosage)
    TextView tv_thisMonthDosage;//本月用量
    @BindView(R.id.last_month_degree)
    TextView tv_lastMonthDegree;//本月起度
    @BindView(R.id.last_month_dosage)
    TextView tv_lastMonthDosage;//上月用量
    @BindView(R.id.meter_date)
    TextView tv_meterDate;
    @BindView(R.id.meter_flag)
    TextView tv_meterFlag;
    @BindView(R.id.user_name)
    TextView tv_userName;
    @BindView(R.id.zhanghao_button)
    LinearLayout zhanghaoButton;
    @BindView(R.id.user_id)
    TextView tv_userId;
    @BindView(R.id.liulan_button)
    LinearLayout liulanButton;
    @BindView(R.id.user_old_id)
    TextView tv_userOldId;
    @BindView(R.id.meter_number)
    TextView tv_meterNumber;
    @BindView(R.id.area_name)
    TextView tv_areaName;
    @BindView(R.id.user_addres)
    TextView tv_userAddres;
    @BindView(R.id.user_phone)
    TextView tv_userPhone;
    @BindView(R.id.user_property)
    TextView tv_userProperty;
    @BindView(R.id.meter_reader)
    TextView tv_meterReader;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorlayout;
    @BindView(R.id.tv_charge)
    TextView tvCharge;
    @BindView(R.id.meter_type)
    TextView meterType;
    @BindView(R.id.tv_state_id)
    TextView tvStateId;
    @BindView(R.id.cb_state_id)
    CheckBox cbStateId;
    @BindView(R.id.tv_warning)
    TextView tvWarning;
    @BindView(R.id.btn_modify_data)
    Button btnModifyData;
    @BindView(R.id.user_use)
    TextView tv_userUse;
    @BindView(R.id.et_state_remark)
    EditText etStateRemark;
    @BindView(R.id.layout_remark)
    LinearLayout layoutRemark;

    @BindView(R.id.et_meter_read)
    EditText etEstimate;//估录输入

    private ImageView frameAnimation;
    private TextView tv_tips;
    private View view;
    private TextView tv_ensure;
    private String userID, meterNumber, longitude, latitude, locationAddress, meterDate, idCard;
    private String stateID = "0";//估录：1，正常录入：0
    private int readerCompanyId;//抄表员公司ID
    private int userCompanyId;//用户所属水表公司ID
    private String meterDegrees;//计量器读数
    private String initDegree;//初始读数
    private String openInitDegree;//启用初始读数
    private String remission;//加减量
    private int changeMeter;//更换量
    private Double adjust;//更换量
    private String minimum;//启用量
    private String accounting;//会计期间
    private int userState;//用户状态，0停表1正常2拆表3迁表保户4t停气6未通气7未开户8正在办理业务9销户
    private SharedPreferences public_sharedPreferences, sharedPreferences, sharedPreferences_login;
   // private String ip, port;  //接口ip地址   端口
    public int responseCode = 0;  //返回码
    private String result; //网络请求结果
    private String uploadResult;
    private SimpleDateFormat dateFormat, stateFormat;
    private Intent intent;
    private boolean isShowWindow = true;
    private double actualDosage;//实际用量
    // 定位相关
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_reading);
        ButterKnife.bind(this);
        initData();
        setViewListener();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isShowWindow) {
            isShowWindow = false;
            showPopupwindow();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        public_sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("login_name", "") + "data", Context.MODE_PRIVATE);
        readerCompanyId = sharedPreferences_login.getInt("company_id", 0);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        stateFormat = new SimpleDateFormat("yyyyMM", Locale.getDefault());
        tv_thisMonthDosage.setText("");
        tv_meterDate.setText("");
        tv_meterFlag.setText("");
        /**
         * 设置 下拉刷新 Header风格样式
         */
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setPrimaryColorsId(R.color.theme_colors, android.R.color.white);
        refreshLayout.setEnableLoadmore(false);  //关闭上拉加载功能
        refreshLayout.setDisableContentWhenRefresh(true);  //是否在刷新的时候禁止内容的一切手势操作（默认false）
        new Thread() {
            @Override
            public void run() {
                super.run();
                intent = getIntent();
                Log.i("CostListview_intent==>", intent + "");
                if (intent != null) {
                    userID = intent.getStringExtra("userid");
                    if (userID != null) {
                        getMeterUserInfo("getAllUser.do", "userid=" + userID + "&companyid=" + readerCompanyId);
                        //Log.i("CostListview_userid===>",userid);
                    }
                    meterNumber = intent.getStringExtra("meterNumber");
//                     Log.i("meterNumber===========>",meterNumber);
                    if (meterNumber != null) {
                        getMeterUserInfo("getAllUser.do", "meterNumber=" + meterNumber);
                        // Log.i("CostListview_meterNumb=",meterNumber);
                    }
                }
            }
        }.start();
    }

    /**
     * 获取用户详细信息
     * @param method
     * @param keyAndValue
     */
    private void getMeterUserInfo(String method, String keyAndValue) {
        try {
            Thread.sleep(500);
            URL url;
            HttpURLConnection httpURLConnection;

            //String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
            String httpUrl=new StringBuffer().append(SkUrl.SkHttp(MeterReadingActivity.this)).append(method).toString();
            //有参数传递
            if (!keyAndValue.equals("")) {
                url = new URL(httpUrl + "?" + keyAndValue);
                //没有参数传递
            } else {
                url = new URL(httpUrl);
            }
            Log.i("url=============>", url + "");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.connect();
            //传回的数据解析成String
            if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    stringBuilder.append(str);
                }
                result = stringBuilder.toString();
                Log.i("CostDetailresult===>", result);
                JSONObject object = new JSONObject(result);
                if (!object.optString("isExist").equals("0")) {
                    handler.sendEmptyMessage(0);
                } else {
                    Thread.sleep(3000);
                    handler.sendEmptyMessage(4);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置监听
     */
    private void setViewListener() {

        cbStateId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stateID = "1";
                    if (tv_meterFlag.getText().toString().equals("未抄")) {
                        layoutRemark.setVisibility(View.VISIBLE);
                        //估录
                        etEstimate.setVisibility(View.VISIBLE);
                        //etEstimate.setText(thisMonthDosage);
                        EditTextUtils.setEnabled(etEstimate,true);
                     EditTextUtils.setEnabled(et_thisMonthEndDegree,false);
                        et_thisMonthEndDegree.setText(monthDegree);
                    }
                } else {
                    stateID = "0";
                    if (tv_meterFlag.getText().toString().equals("未抄")) {
                        EditTextUtils.setEnabled(etEstimate, false);
                        layoutRemark.setVisibility(View.GONE);
                        etEstimate.setVisibility(View.GONE);
                        EditTextUtils.setEnabled(et_thisMonthEndDegree, true);
                        et_thisMonthEndDegree.setText("");
                    }

                }
            }
        });
        /**
         * 下拉刷新监听
         */
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        intent = getIntent();
                        Log.i("CostListview_intent==>", intent + "");
                        if (intent != null) {
                            userID = intent.getStringExtra("userid");
                            if (userID != null) {
                                getMeterUserInfo("getAllUser.do", "userid=" + userID + "&companyid=" + readerCompanyId);
                                //Log.i("CostListview_userid===>",userid);
                            }
                            meterNumber = intent.getStringExtra("meterNumber");
                            if (meterNumber != null) {
                                getMeterUserInfo("getAllUser.do", "meterNumber=" + meterNumber);
                                // Log.i("CostListview_meterNumb=",meterNumber);
                            }
                        }
                        refreshLayout.finishRefresh();
                        refreshlayout.finishRefresh(3000);
                    }
                }.start();
            }
        });
    }

    @OnClick({R.id.back, R.id.save, R.id.tv_charge, R.id.btn_modify_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_state_id:
                if (cbStateId.isChecked()) {
                    cbStateId.setChecked(false);
                } else {
                    cbStateId.setChecked(true);
                }
                break;
            case R.id.tv_charge:
                //缴费
                if (userID == null) {
                    Toast.makeText(MeterReadingActivity.this, "未获取用户信息，不能进行缴费！", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (readerCompanyId != userCompanyId) {
//                    Toast.makeText(MeterReadingActivity.this, "非本公司用户，不能进行抄表缴费相关操作！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                //缴费页面
                Intent intent = new Intent(MeterReadingActivity.this, ChargeActivity.class);
                intent.putExtra("userid", userID);
                startActivity(intent);
                break;
            case R.id.save:
                //保存
//                Intent intent = new Intent(MeterReadingActivity.this, ChargeActivity.class);
//                startActivity(intent);
                if (!tv_meterFlag.getText().toString().equals("未抄")) {
                    Toast.makeText(MeterReadingActivity.this, "抄过表了！", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (readerCompanyId != userCompanyId) {
//                    Toast.makeText(MeterReadingActivity.this, "非本公司用户，不能进行抄表缴费相关操作！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (Tools.isInputMethodOpened(MeterReadingActivity.this)) {
                    Tools.hideSoftInput(MeterReadingActivity.this, et_thisMonthEndDegree);
                }

                if (!"".equals(et_thisMonthEndDegree.getText().toString())) {
                    int i = Integer.parseInt(et_thisMonthEndDegree.getText().toString()) - Integer.parseInt(tv_lastMonthDegree.getText().toString()) + changeMeter;
                    if (Double.parseDouble(remission) != 0 && (i + Double.parseDouble(remission)) < 0) {
                        tv_thisMonthDosage.setText("0");
                    } else {
                        tv_thisMonthDosage.setText((i + Double.parseDouble(remission)) + "");
                    }
                    if (stateID.equals("1") &&etEstimate.getText().toString().equals("")) {
                        Toast.makeText(MeterReadingActivity.this, "请输入估录用量！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showPopupwindow();
                    if (stateID.equals("1") && etStateRemark.getText().toString().equals("")) {
                        popupWindow.dismiss();
                        Toast.makeText(MeterReadingActivity.this, "请说明估录原因！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Double thisMonthDosage = Double.parseDouble(tv_thisMonthDosage.getText().toString());
                    Double lastMonthDosage = Double.parseDouble(tv_lastMonthDosage.getText().toString());
                    if (thisMonthDosage > lastMonthDosage + lastMonthDosage * 0.5 && lastMonthDosage != 0) {
                        tvWarning.setVisibility(View.VISIBLE);
                        popupWindow.dismiss();
                        final MyDialog warningDialog = new MyDialog();
                        warningDialog.show("警告：", "本月用量超过上月用量50%！点击确定继续抄表。", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showPopupwindow();
                                if (Double.parseDouble(tv_thisMonthDosage.getText().toString()) >= 0) {
                                    if (meterType.getText().toString().equals("普通表")) {
                                        meterDate = dateFormat.format(new Date());
                                        tv_meterDate.setText(meterDate);
                                        new Thread() {
                                            @Override
                                            public void run() {
                                               // dataToJson();
                                                dataToJsons();
                                            }
                                        }.start();
                                    } else {
                                        popupWindow.dismiss();
                                        Toast.makeText(MeterReadingActivity.this, "非普通表不能进行移动抄表！", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    popupWindow.dismiss();
                                    Toast.makeText(MeterReadingActivity.this, "本月读数不能小于上月读数哦！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                warningDialog.dismiss();
                            }
                        }, getSupportFragmentManager());
                    } else {
                        if (Double.parseDouble(tv_thisMonthDosage.getText().toString()) >= 0) {
                            if (meterType.getText().toString().equals("普通表")) {
                                meterDate = dateFormat.format(new Date());
                                tv_meterDate.setText(meterDate);
                                new Thread() {
                                    @Override
                                    public void run() {
                                        //dataToJson();
                                        dataToJsons();
                                    }
                                }.start();
                            } else {
                                popupWindow.dismiss();
                                Toast.makeText(MeterReadingActivity.this, "非普通表不能进行移动抄表！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            popupWindow.dismiss();
                            Toast.makeText(MeterReadingActivity.this, "本月读数不能小于上月读数哦！", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    popupWindow.dismiss();
                    Toast.makeText(MeterReadingActivity.this, "请您输入本月读数！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_modify_data:
                //修改信息
                Intent intent1 = new Intent(MeterReadingActivity.this, ModifyDataActivity.class);
                intent1.putExtra("userID", tv_userId.getText().toString());
                intent1.putExtra("userName", tv_userName.getText().toString());
                intent1.putExtra("userPhone", tv_userPhone.getText().toString());
                intent1.putExtra("userUse", tv_userUse.getText().toString());
                intent1.putExtra("idCard", idCard);
                startActivity(intent1);
                break;
        }
    }

    /**
     * 封装为json数据
     */
    private void dataToJson() {
        try {
            JSONObject object = new JSONObject();
            object.put("n_jw_x", tv_longitude.getText().toString());      //纬度
            object.put("n_jw_y", tv_latitude.getText().toString());      //经度
            object.put("n_meter_degrees", et_thisMonthEndDegree.getText().toString());       //本月止度
            if (stateID.equals("1")) {
                object.put("nDosage", etEstimate.getText().toString());          //本月用量
            }else {
                object.put("nDosage", tv_thisMonthDosage.getText().toString());          //本月用量
            }

            object.put("n_situation_operatorId", sharedPreferences_login.getString("userId", ""));       //操作员ID
            object.put("c_user_id", userID);       //抄表用户ID
            object.put("n_stateId", stateID);       //是否估录
            if (stateID.equals("1")) {
                object.put("c_logRemark", etStateRemark.getText().toString());       //估录原因
            } else {
                object.put("c_logRemark", "抄表录入");       //估录原因
            }
            object.put("d_jw_time", tv_meterDate.getText().toString());       //抄表时间
            Log.i("dataToJson==========>", "封装的json数据为：" + object.toString() + ",userid" + userID);
            //抄表
            uploadMeterData(object.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 封装为json数据
     */
    private void dataToJsons() {

            Map<String, Object> map = new HashMap<String, Object>();
        map.put("n_jw_x", tv_longitude.getText().toString());      //纬度
        map.put("n_jw_y", tv_latitude.getText().toString());      //经度
        map.put("n_meter_degrees", et_thisMonthEndDegree.getText().toString());       //本月止度
            if (stateID.equals("1")) {
                map.put("nDosage", etEstimate.getText().toString());          //本月用量
            }else {
                map.put("nDosage", tv_thisMonthDosage.getText().toString());          //本月用量
            }

        map.put("n_situation_operatorId", sharedPreferences_login.getString("userId", ""));       //操作员ID
        map.put("c_user_id", userID);       //抄表用户ID
        map.put("n_stateId", stateID);       //是否估录
            if (stateID.equals("1")) {
                map.put("c_logRemark", etStateRemark.getText().toString());       //估录原因
            } else {
                map.put("c_logRemark", "抄表录入");       //估录原因
            }
        map.put("d_jw_time", tv_meterDate.getText().toString());       //抄表时间
            Log.i("dataToJso==========>", "封装的json数据为：" +new Gson().toJson(map) + ",userid" + userID);
            //抄表
        setUploadMeterData(map);



    }
    /**
     * 包含图片上传
     */
    private void setUploadMeterData( final Map<String, Object> map) {
        String httpUrl=new StringBuffer().append(SkUrl.SkHttp(MeterReadingActivity.this)).append("meterReadingAdd.do").toString();
        HttpUtils.sendFilePosts(MeterReadingActivity.this, httpUrl, map,null , new HttpCallbackStringListener() {
            @Override
            public void onFinish(String response) {
                //成功
                Log.i("login_result=========>", response);

                uploadResult =response;
                Log.i("login_result=========>", uploadResult);
                if ("1".equals(response)) {//"1" 代表上传成功
                    handler.sendEmptyMessage(1);
                } else {
                    //失败
                    handler.sendEmptyMessage(2);
                }
            }

            @Override
            public void onError(Exception e) {
                handler.sendEmptyMessage(3);

            }
        });

    }


    /**
     * 抄表
      * @param JsonData
     */
    private void uploadMeterData(String JsonData) {
        Log.i("dataToJson==========>", "封装的json数据为：" + JsonData);
        try {
            //请求的地址
            Thread.sleep(500);
        // String httpUrl = "http://" + ip + port + "/SMDemo/meterReadingAdd.do";
        String httpUrl=new StringBuffer().append(SkUrl.SkHttp(MeterReadingActivity.this)).append("meterReadingAdd.do").toString();
        // String httpUrl = "http://" + ip + "9459/AppService/GetJsonByAjax.aspx?ajax=meterReadingAdd";
          // String httpUrl = SkUrl.SKURL;
            Log.i("httpUrl==========>", "" + httpUrl);
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
            urlConnection.setReadTimeout(6000);
            urlConnection.setConnectTimeout(6000);
            // 传递的数据
            // 设置请求的头
            //urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            //urlConnection.setRequestProperty("Origin", "http://"+ ip + port);
            urlConnection.setRequestProperty("Content-Length", String.valueOf(JsonData.getBytes().length));
            //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            //获取输出流
            OutputStream os = urlConnection.getOutputStream();
            os.write(JsonData.getBytes("UTF-8"));
            os.flush();
            os.close();
            Log.i("getResponseCode====>", "" + urlConnection.getResponseCode());
            if (urlConnection.getResponseCode() == 200) {
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
                uploadResult = stringBuilder.toString();
                Log.i("login_result=========>", uploadResult);
                if ("1".equals(uploadResult)) {//"1" 代表上传成功
                    handler.sendEmptyMessage(1);
                } else {
                    //失败
                    handler.sendEmptyMessage(2);
                }
            } else {
                //返回码不是200
                Log.i("responsecode==========>", "code: " + urlConnection.getResponseCode());
//                isfaild = true;
                handler.sendEmptyMessage(3);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求异常!");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private AnimationDrawable animationDrawable;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;

    private void showPopupwindow() {
        layoutInflater = LayoutInflater.from(MeterReadingActivity.this);
        view = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) view.findViewById(R.id.frame_animation);
        tv_tips = (TextView) view.findViewById(R.id.tips);
        tv_tips.setText("正在处理,请稍候...");
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.update();
        popupWindow.showAtLocation(refreshLayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        startFrameAnimation();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    popupWindow.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.optString("row").equals("null")) {
                            Toast.makeText(MeterReadingActivity.this, "没有相应的数据!", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        JSONObject object = jsonObject.getJSONObject("row");
//                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
//                        JSONObject object = jsonArray.getJSONObject(0);
                        String dataMonth = object.optString("c_situation_use_month", "");
//                        String t = object.optString("meterReadingDate", "");
//                        Log.i("meterReadingDate", "handleMessage: t:" + t);
//                        String time = t.substring(0, t.lastIndexOf("-"));
//                        Log.i("meterReadingDate", "handleMessage: time:" + time);
                        userState = object.optInt("n_user_state", -1);
                        if (userState == 0) {
                            userStateDialog("该用户已经停表，不能进行查询和抄表。");
                            return;
                        } else if (userState == 2) {
                            userStateDialog("该用户已经拆表，不能进行查询和抄表。");
                            return;
                        } else if (userState == 9) {
                            userStateDialog("该用户已经销户，不能进行查询和抄表。");
                            return;
                        }
                        meterDegrees = String.valueOf(object.optInt("n_meter_degrees", 0));
                        minimum = String.valueOf(object.optInt("n_minimum", 0));
                        changeMeter = object.optInt("n_change_meter", 0);
                        remission = String.valueOf(object.optDouble("n_remission", 0.0));//加减量
                        openInitDegree = String.valueOf(object.optInt("n_opened_init_degrees", 0));
                        initDegree = String.valueOf(object.optInt("n_install_init_degrees", 0));
                        userCompanyId = object.optInt("n_company_id");
                        adjust = object.optDouble("adjustDosage", 0.0);
                        accounting = object.optString("c_accounting", "");
                        if (dataMonth.equals(accounting) && adjust == 0) {
                            if (object.optInt("n_stateId") == 0) {
                                tv_meterFlag.setText("已抄");
                            } else {
                                tv_meterFlag.setText("已抄(估录)");
                            }
                            et_thisMonthEndDegree.setFocusable(false);
                            et_thisMonthEndDegree.setFocusableInTouchMode(false);
                            cbStateId.setChecked(false);
                            cbStateId.setVisibility(View.GONE);
                            tvStateId.setVisibility(View.GONE);
                            et_thisMonthEndDegree.setText(object.optString("maxEnd", ""));
                            tv_lastMonthDegree.setText(object.optString("minStart", ""));
                            //本月起度
                            monthDegree=object.optString("minStart", "");
                            int i = Integer.parseInt(et_thisMonthEndDegree.getText().toString()) - Integer.parseInt(tv_lastMonthDegree.getText().toString()) + changeMeter;
                            actualDosage = i + Double.parseDouble(remission);
                            tv_thisMonthDosage.setText(object.optString("realDosage") + "");
                            tv_meterDate.setText(object.optString("meterReadingDate", ""));
                            if (object.optInt("lastMonthDosage", 0) == -1) {
                                tv_lastMonthDosage.setText("0");
                            } else {
                                tv_lastMonthDosage.setText(object.optString("lastMonthDosage", "0"));
                            }
                            Double thisMonthDosage = Double.parseDouble(tv_thisMonthDosage.getText().toString());
                            Double lastMonthDosage = Double.parseDouble(tv_lastMonthDosage.getText().toString());
                            if (thisMonthDosage > lastMonthDosage + lastMonthDosage * 0.5 && lastMonthDosage != 0) {
                                tvWarning.setVisibility(View.VISIBLE);
                            } else {
                                tvWarning.setVisibility(View.GONE);
                            }
                        } else {
                            tv_meterFlag.setText("未抄");
                            tv_thisMonthDosage.setText("");
                            tv_meterDate.setText("");
                            et_thisMonthEndDegree.setText("");
                            et_thisMonthEndDegree.setFocusableInTouchMode(true);
                            et_thisMonthEndDegree.setFocusable(true);
                            et_thisMonthEndDegree.requestFocus();
                            tv_lastMonthDegree.setText(object.optInt("n_meter_degrees", 0) + "");
                            //本月起度
                            monthDegree=object.optInt("n_meter_degrees", 0) + "";
                            tv_lastMonthDosage.setText(object.optString("realDosage", "0"));
                            tvWarning.setVisibility(View.GONE);
                        }
                        if (!object.optString("type", "").equals("普通表")) {
                            et_thisMonthEndDegree.setFocusable(false);
                            et_thisMonthEndDegree.setFocusableInTouchMode(false);
                            Toast.makeText(MeterReadingActivity.this, "非普通表用户不能进行移动抄表!", Toast.LENGTH_SHORT).show();
                        }
//                        if (readerCompanyId != userCompanyId) {
//                            et_thisMonthEndDegree.setFocusable(false);
//                            et_thisMonthEndDegree.setFocusableInTouchMode(false);
//                            Toast.makeText(MeterReadingActivity.this, "非本公司用户，不能进行抄表缴费相关操作！", Toast.LENGTH_SHORT).show();
//                        }
                        idCard = object.optString("c_identity_card", "");
                        userID = object.optString("c_user_id", "");
                        meterType.setText(object.optString("type", ""));
                        tv_userId.setText(object.optString("c_user_id", ""));
                        tv_userName.setText(object.optString("c_user_name", ""));
                        tv_userOldId.setText((object.optString("c_old_user_id", "")));
                        if (object.optString("c_meter_number", "") != null && !object.optString("c_meter_number", "").equals("null")) {
                            tv_meterNumber.setText(object.optString("c_meter_number", ""));
                        } else {
                            tv_meterNumber.setText("暂无");
                        }
                        tv_userUse.setText(object.optString("c_user_remark", "暂无"));
                        tv_userAddres.setText(object.optString("c_user_address", ""));
                        if (object.getString("c_user_phone") != null && !object.getString("c_user_phone").equals("null")) {
                            tv_userPhone.setText(object.optString("c_user_phone", ""));
                        } else {
                            tv_userPhone.setText("暂无");
                        }
                        tv_userProperty.setText(object.optString("c_properties_name", ""));
                        if (object.optString("c_meter_reader_name", "").equals("null")) {
                            tv_meterReader.setText("暂无");
                        } else {
                            tv_meterReader.setText(object.optString("c_meter_reader_name", ""));
                        }
                        tv_areaName.setText(object.optString("c_community_name", ""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    popupWindow.dismiss();
                    final MyDialog myDialog = new MyDialog();
                    myDialog.setCancelable(false);
                    myDialog.show("缴费", "是否跳转至缴费页面？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myDialog.dismiss();
                            Intent intent = new Intent(MeterReadingActivity.this, ChargeActivity.class);
                            intent.putExtra("userid", userID);
                            startActivity(intent);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myDialog.dismiss();
                            showPopupwindow();
                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    intent = getIntent();
                                    Log.i("CostListview_intent==>", intent + "");
                                    userID = intent.getStringExtra("userid");
                                    if (intent != null) {
                                        userID = intent.getStringExtra("userid");
                                        if (userID != null) {
                                            getMeterUserInfo("getAllUser.do", "userid=" + userID);
                                            //Log.i("CostListview_userid===>",userid);
                                        }
                                        meterNumber = intent.getStringExtra("meterNumber");
                                        if (meterNumber != null) {
                                            getMeterUserInfo("getAllUser.do", "meterNumber=" + meterNumber);
                                            // Log.i("CostListview_meterNumb=",meterNumber);
                                        }
                                    }
                                }
                            }.start();
                        }
                    }, getSupportFragmentManager());

                    break;
                case 2:
                    popupWindow.dismiss();
                    Toast.makeText(MeterReadingActivity.this, "上传失败!" + uploadResult + "!", Toast.LENGTH_SHORT).show();
                    showPopupwindow();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            intent = getIntent();
                            Log.i("CostListview_intent==>", intent + "");
                            userID = intent.getStringExtra("userid");
                            if (intent != null) {
                                userID = intent.getStringExtra("userid");
                                if (userID != null) {
                                    getMeterUserInfo("getAllUser.do", "userid=" + userID);
                                    //Log.i("CostListview_userid===>",userid);
                                }
                                meterNumber = intent.getStringExtra("meterNumber");
                                if (meterNumber != null) {
                                    getMeterUserInfo("getAllUser.do", "meterNumber=" + meterNumber);
                                    // Log.i("CostListview_meterNumb=",meterNumber);
                                }
                            }
                        }
                    }.start();
                    break;
                case 3:
                    popupWindow.dismiss();
                    Toast.makeText(MeterReadingActivity.this, "网络请求异常!", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    popupWindow.dismiss();
                    Toast.makeText(MeterReadingActivity.this, "没有相应用户数据！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void userStateDialog(String s) {
        final MyDialog myDialog = new MyDialog();
        myDialog.setCancelable(false);
        myDialog.show("提示", s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDialog.dismiss();
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDialog.dismiss();
                finish();
            }
        }, getSupportFragmentManager());
    }

    /**
     * 定位初始化
     */
    private void initLocation() {
        // 定位初始化
        mLocClient = new LocationClient(getApplicationContext()); //声明LocationClient类
        mLocClient.registerLocationListener(myListener);     //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll"); // 设置坐标类型
        //option.setScanSpan(1000);
        option.setOpenGps(true); // 打开gps
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setNeedDeviceDirect(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
        tv_longitude.setText(longitude);
        tv_latitude.setText(latitude);
        tv_address.setText(locationAddress);
    }




    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map_meter_icon view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            int code = location.getLocType();
            Log.i("MyLocationListenner", "code是：" + code);
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());

            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            locationAddress = location.getAddrStr();
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            Log.i("currentAddress", locationAddress);
            Log.i("MyLocationListenner", sb.toString());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

}
