package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 费用详情
 * Created by Administrator on 2017/2/27 0027.
 */
public class CostDetailActivity extends Activity {
    private ImageView back;
    private Intent intent;
    private TextView userNumber;  //用户编号
    private TextView userName;   //用户名
    private TextView startDegree; //起度
    private TextView endDegree;  //止度
    private TextView totalDosage;  //综合用量
    private TextView waterType;  //性质
    private TextView unitCost;  //单价
    private TextView derateLimit;  //减免额度
    private TextView integratedWater;  //综合费用
    private TextView meterTime;   //抄表时间
    private TextView chargeTime;   //收费时间
    private TextView servicePhoneNumber;   //服务电话
    private String servicePhoneNumb;   //服务电话
    private String userid;    //用户编号
    private String month;    //月份
    public int responseCode = 0;  //返回码
    private String result; //网络请求结果
    private SharedPreferences public_sharedPreferences,sharedPreferences,sharedPreferences_login;
    private SharedPreferences.Editor editor;
  //  private String ip,port;  //接口ip地址   端口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_detail);

        bindView();//绑定控件ID
        defaultSetting();//初始化设置

        new Thread(){
            @Override
            public void run() {  //开起一个支线程进行网络请求
                intent = getIntent();
                Log.i("intent===========>",intent+"");
                if(intent != null){
                    userid = intent.getStringExtra("userid");
                    Log.i("CostDetailActivity=>",userid);
                    month = intent.getStringExtra("useMonth");
                    Log.i("CostDetailActivity===>",month);
                    if(!userid.equals("") && !month.equals("")){
                        requireMyWorks("getUserMonthMessage.do", "userid=" + userid+"&"+"useMonth="+month);
                    }
                }
            }
        }.start();
        setViewClickListener();//点击事件
    }

    //绑定控件ID
    private void bindView(){
        back = (ImageView) findViewById(R.id.back);
        userNumber = (TextView) findViewById(R.id.user_number);
        userName = (TextView) findViewById(R.id.user_name);
        startDegree = (TextView) findViewById(R.id.start_degree);
        endDegree = (TextView) findViewById(R.id.end_degree);
        totalDosage = (TextView) findViewById(R.id.total_dosage);
        waterType = (TextView) findViewById(R.id.water_type);
        unitCost = (TextView) findViewById(R.id.unit_cost);
        derateLimit = (TextView) findViewById(R.id.derate_limit);
        integratedWater = (TextView) findViewById(R.id.integrated_water);
        meterTime = (TextView) findViewById(R.id.meter_time);
        chargeTime = (TextView) findViewById(R.id.charge_time);
        servicePhoneNumber = (TextView) findViewById(R.id.service_phone_number);
    }

    //点击事件
    private void setViewClickListener(){
        back.setOnClickListener(clickListener);
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

    private void defaultSetting() {
        public_sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("login_name","")+"data", Context.MODE_PRIVATE);
    }


    //请求网络数据
    private void requireMyWorks(final String method, final String keyAndValue) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url;
                    HttpURLConnection httpURLConnection;

                   // String httpUrl = "http://"+ ip + port +"/SMDemo/" + method;
                    String httpUrl=new StringBuffer().append(SkUrl.SkHttp(CostDetailActivity.this)).append(method).toString();
                    //有参数传递
                    if (!keyAndValue.equals("") ) {
                        url = new URL(httpUrl + "?" + keyAndValue);
                        //没有参数传递
                    } else {
                        url = new URL(httpUrl);
                    }
                    Log.i("url=============>",url+"");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(8000);
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
                        Log.i("CostDetailresult===>",result);
                        JSONObject object = new JSONObject(result);
                        if(!object.optString("total").equals("0")){
                            handler.sendEmptyMessage(0);
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
                }
            }
        }.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
                    JSONObject object = jsonArray.getJSONObject(0);
                    userNumber.setText(object.optString("userId",""));
                    userName.setText(object.optString("userName",""));
                    startDegree.setText(object.optInt("degreesStart",0)+"");
                    endDegree.setText(object.optInt("degreesEnd",0)+"");
                    totalDosage.setText(object.optInt("totalDosage",0)+"");
                    waterType.setText(object.optString("propertiesName",""));
                    unitCost.setText(object.optDouble("detailPrice",0.0)+"");
                    derateLimit.setText(object.optInt("remissionAmount",0)+"");
                    integratedWater.setText(object.optDouble("totalAmount",0.0)+"");
                    meterTime.setText(object.optString("meterReadingDate",""));
                    chargeTime.setText(object.optString("chargeDate",""));
                    servicePhoneNumb = sharedPreferences.getString("servePhoneNumber","");
                    servicePhoneNumber.setText(servicePhoneNumb);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.handleMessage(msg);
        }
    };
}
