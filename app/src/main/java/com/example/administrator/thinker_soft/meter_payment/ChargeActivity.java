package com.example.administrator.thinker_soft.meter_payment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PrintUtils;
import com.example.administrator.thinker_soft.meter_payment.Bean.UserMeterInfo;
import com.example.administrator.thinker_soft.meter_payment.Utils.MyDialog;
import com.example.administrator.thinker_soft.mode.CommonAdapter;
import com.example.administrator.thinker_soft.mode.ViewHolder;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 缴费
 */
public class ChargeActivity extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.iv_payment_success)
    ImageView ivPaymentSuccess;
    @BindView(R.id.layout_payment_success)
    LinearLayout layoutPaymentSuccess;
    @BindView(R.id.user_number)
    TextView userNumber;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_addres)
    TextView userAddres;
    @BindView(R.id.layout_pay)
    LinearLayout layoutPay;
    @BindView(R.id.lv_not_pay)
    ListView lvNotPay;
    @BindView(R.id.layout_charge)
    LinearLayout layoutCharge;
    @BindView(R.id.tv_pay_ps)
    TextView tvPayPs;
    @BindView(R.id.old_user_number)
    TextView oldUserNumber;
    @BindView(R.id.user_balance)
    TextView userBalance;//用户余额

    private String userMoney="0.00";//计算后的余额
    private String m_inAmount;

    //private String ip, port;  //接口ip地址   端口
    private SharedPreferences public_sharedPreferences, sharedPreferences_login;
    private Intent intent;
    private String userid;
    private Double balance;
    private String meterReder, currentBalance;
    public int responseCode = 0;
    private String result; //网络请求结果
    private String chargeResult; //网络请求结果
    private String faildMsg;
    private CommonAdapter<UserMeterInfo> adapter;
    private ArrayList<UserMeterInfo> notPayList = new ArrayList<>();
    private UserMeterInfo paymentInfo;
    private boolean isShowWindow = true;
    private LinearLayout rootLinearlayout;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private View view;
    private String time;
    private Date date;
    private String chargeDate;
    private SimpleDateFormat dateFormat;
    private String paid_inAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        ButterKnife.bind(this);
        defaultSetting();
        new Thread() {//开起一个支线程进行网络请求
            @Override
            public void run() {
                intent = getIntent();
                Log.i("CostListview_intent==>", intent + "");
                if (intent != null) {
                    userid = intent.getStringExtra("userid");
                    if (userid != null) {
                        requireNotPayList("getUser.do", "userid=" + userid);
                        //Log.i("CostListview_userid===>",userid);
                    }
                }
            }
        }.start();

//0361112802434
   Log.e("====", PrintUtils.setLength("锦竹市供排水总公司(玉妃路)政务中心D区(景观大道)回澜大道(德阳银行侧)",14)+"\n"+PrintUtils.setLengths("    1.1元/吨    6吨    5.1元"));

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isShowWindow) {
            isShowWindow = false;
            showPopupwindow();
        }
    }

    //请求网络数据listview
    private void requireNotPayList(final String method, final String keyAndValue) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    URL url;
                    HttpURLConnection httpURLConnection;

                 //   String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
                    String httpUrl=new StringBuffer().append(SkUrl.SkHttp(ChargeActivity.this)).append(method).toString();
                    //有参数传递
                    if (!keyAndValue.equals("")) {
                        url = new URL(httpUrl + "?" + keyAndValue);
                        //没有参数传递
                    } else {
                        url = new URL(httpUrl);
                    }
                    Log.i("url_listview=========>", url + "");
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
                        Log.i("result==========>", result);
//                        handler.sendEmptyMessage(1);
                        handler.sendEmptyMessage(0);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 封装json数据
     * @param total
     * @param detailMonth
     * @param chargeDate
     */
    private void dataToJson(Double total, String detailMonth, String chargeDate) {
        try {
            JSONObject object = new JSONObject();
            object.put("userid", userid);
            object.put("c_detail_use_month", detailMonth);
            object.put("cashierId", sharedPreferences_login.getString("userId", ""));
            object.put("d_charge_date", chargeDate);
            object.put("totalAumen", total.toString());
            if (balance >= total && total != 0) {
//                object.put("userid", userid);
//                object.put("cashierId", sharedPreferences_login.getString("userId", ""));
                Log.i("dataToJson==========>", "余额付款json数据为：" + object.toString());
                balancePay(object.toString());
            } else {
//                object.put("userid", userid);
//                object.put("c_detail_use_month", detailMonth);
//                object.put("cashierId", sharedPreferences_login.getString("userId", ""));
//                object.put("d_charge_date", chargeDate);
////                BigDecimal due = new BigDecimal(total);
////                BigDecimal bg = due.setScale(0, BigDecimal.ROUND_HALF_UP);
//                object.put("totalAumen", total.toString());
                Log.i("dataToJson==========>", "现金付款json数据为：" + object.toString());
                charge(object.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //缴费
    private void charge(String JsonData) {
        Log.i("dataToJson==========>", "封装的json数据为：" + JsonData);
        try {
            Thread.sleep(500);
            //请求的地址

       //     String httpUrl = "http://" + ip + port + "/SMDemo/userAndroidCharge.do";
            String httpUrl=new StringBuffer().append(SkUrl.SkHttp(ChargeActivity.this)).append("userAndroidCharge.do").toString();
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
            urlConnection.setRequestProperty("Content-Type", "applicaton/json;charset=UTF-8");
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
                chargeResult = stringBuilder.toString();
                Log.i("charge_result=========>", chargeResult);
                JSONObject jsonObject = new JSONObject(chargeResult);

                if (jsonObject.optInt("msg") == 0) {//0 代表成功
                    handler.sendEmptyMessage(2);

                } else {
                    //失败
                    String msg = jsonObject.optString("chargeinfo", "");
                    int cashier = jsonObject.optInt("cashier", 0);
                    if (cashier == 1) {
                        handler.sendEmptyMessage(6);
                    } else {
                        faildMsg = msg.substring(msg.indexOf(":") + 1, msg.length());
                        handler.sendEmptyMessage(3);
                    }
                }
            } else {
                //返回码不是200
                Log.i("responsecode==========>", "code: " + urlConnection.getResponseCode());
//                isfaild = true;
                handler.sendEmptyMessage(4);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求异常!");
            handler.sendEmptyMessage(5);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //余额缴费
    private void balancePay(String JsonData) {
        Log.i("dataToJson==========>", "封装的json数据为：" + JsonData);
        try {
            Thread.sleep(500);
            //请求的地址

//            String httpUrl = "http://" + ip + port + "/SMDemo/prestoreDeduct.do";
            String httpUrl=new StringBuffer().append(SkUrl.SkHttp(ChargeActivity.this)).append("prestoreDeduct.do").toString();
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
            urlConnection.setRequestProperty("Content-Type", "applicaton/json;charset=UTF-8");
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
                chargeResult = stringBuilder.toString();
                Log.i("charge_result=========>", chargeResult);
                JSONObject jsonObject = new JSONObject(chargeResult);

                if (jsonObject.optInt("msg") == 0) {//0 代表成功
                    handler.sendEmptyMessage(2);

                } else {
                    //失败
                    String msg = jsonObject.optString("chargeinfo", "");
                    int cashier = jsonObject.optInt("cashier", 0);
                    if (cashier == 1) {
                        handler.sendEmptyMessage(6);
                    } else {
                        faildMsg = msg.substring(msg.indexOf(":") + 1, msg.indexOf("<"));
                        handler.sendEmptyMessage(3);
                    }
                }
            } else {
                //返回码不是200
                Log.i("responsecode==========>", "code: " + urlConnection.getResponseCode());
//                isfaild = true;
                handler.sendEmptyMessage(4);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求异常!");
            handler.sendEmptyMessage(5);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取值
     * @param strs
     * @return
     */
    private List<String> stringToList(String strs){
        String str[] = strs.split("|");
        return Arrays.asList(str);
    }

    /**
     * 根据逗号取值
     * @param strs
     * @return
     */
    private List<String> stringToLists(String strs){
        String str[] = strs.split(",");
        return Arrays.asList(str);
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //解析listview的数据
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        Log.i("jsonArray_listview====", "" + jsonArray.length());
                        JSONObject object = jsonArray.getJSONObject(0);
                        userNumber.setText(object.optString("c_user_id", ""));
                        userName.setText(object.optString("c_user_name", ""));
                        oldUserNumber.setText(object.optString("c_old_user_id", ""));
                        userAddres.setText(object.optString("c_user_address", ""));
                        balance = object.optDouble("n_amount", 0.00);
                        userBalance.setText(balance + "");//余额

                        for (int t = 0; t < jsonArray.length(); t++) {
                            object = jsonArray.getJSONObject(t);
                            if (object.optString("n_charge_state", "").equals("欠费")) {
                                UserMeterInfo meterInfo = new UserMeterInfo();
                                meterInfo.setDate(object.optString("c_situation_use_month", ""));
                                meterInfo.setPay_state(object.optString("n_charge_state", ""));
                                meterInfo.setStart_degree(object.optInt("minStart", 0));
                                meterInfo.setEnd_degree(object.optInt("maxEnd", 0));
                                meterInfo.setUnit_cost(object.optDouble("detailPrice", 0.0));
                                meterInfo.setIntegrated_water(object.optDouble("amount", 0.0));
                                meterInfo.setAdjust_dosage(object.optDouble("adjustDosage", 0.0));
                                meterInfo.setReal_dosage(object.optDouble("realDosage", 0.0));
                                meterInfo.setMeter_number(object.optString("c_meter_number", "").equals("null")?"无":object.optString("c_meter_number", ""));//表号
                                meterInfo.setReader_name(object.optString("c_meter_reader_name", ""));//抄表员
                                meterInfo.setReader_time(object.optString("d_situation_operation_time", ""));//抄表时间
                                String price=object.optString("price_detail","");//单价处理
                                List<String> priceList=stringToLists(price);//取值
                                Log.e("ok=",new Gson().toJson(priceList).toString());
                               //遍历取值
                                Iterator it1 = priceList.iterator();
                                int i=0;
                                while(it1.hasNext()){
                                    StringBuffer bufferStr=new StringBuffer();
                                    String repText= (String) it1.next();//获取值
                                    String repStr = repText.substring(repText.indexOf("|"));//截取元素
                                    bufferStr.append(repStr.replace("|","    "));//替换元素
                                    if (i==0) {
                                        meterInfo.setWeter_amount(bufferStr.toString());
                                    }else if (i==1){
                                        meterInfo.setWeter_wamount(bufferStr.toString());
                                    }
                                    Log.e("bbbb",bufferStr.toString());
                                    i++;
                               }
                          //  Log.e("成功",new Gson().toJson(meterInfo).toString());
                                notPayList.add(meterInfo);
                            }
                        }
                        if (notPayList.size() != 0) {
                            tvPayPs.setVisibility(View.GONE);
                            lvNotPay.setVisibility(View.VISIBLE);
                            ListSort(notPayList);
                            adapter = new CommonAdapter<UserMeterInfo>(ChargeActivity.this, R.layout.payment_month_item) {
                                @Override
                                public void convertView(final int i, ViewHolder viewHolder, final UserMeterInfo item) {
                                    Button pay = viewHolder.getView(R.id.btn_pay);

                                    try {
                                        date = new SimpleDateFormat("yyyyMM").parse(item.getDate());
                                        time = new SimpleDateFormat("yyyy年MM月").format(date);
                                        viewHolder.setText(R.id.tv_payment_item_date, time + "");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    viewHolder.setText(R.id.tv_payment_item_start_degree, item.getStart_degree() + "/" + item.getEnd_degree());
                                    viewHolder.setText(R.id.tv_payment_item_actual_dosage, item.getReal_dosage() + "");
                                    viewHolder.setText(R.id.tv_payment_item_unit_cost, item.getUnit_cost() + "");
                                    viewHolder.setText(R.id.tv_payment_item_integrated_water, item.getIntegrated_water() + "");
                                    pay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (balance >= item.getIntegrated_water()) {
                                                paid_inAmount = "用户余额扣款" + item.getIntegrated_water() + "元";
                                              //  m_inAmount=Double.toString(item.getIntegrated_water());//实缴金额
                                                m_inAmount=Double.toString(0.0);//实缴金额
                                            } else {
                                                BigDecimal due = new BigDecimal(item.getIntegrated_water());
                                               // BigDecimal bg = (due.subtract(new BigDecimal(balance))).setScale(0, BigDecimal.ROUND_HALF_UP);//四舍五入
                                                BigDecimal bg = (due.subtract(new BigDecimal(balance))).setScale(0, BigDecimal.ROUND_UP);//进位处理
                                                Log.i("amount", "convertView: " + bg);
                                                paid_inAmount = bg.toString();
                                                m_inAmount=bg.toString();////实缴金额
                                            }
                                            if (item.getIntegrated_water() < 0) {
                                                final MyDialog dia = new MyDialog();
                                                dia.show("提示", "请到营业厅进行退款操作！", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dia.dismiss();
                                                    }
                                                }, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dia.dismiss();
                                                    }
                                                }, getSupportFragmentManager());
                                            } else {
                                                MyDialog dialog = new MyDialog();
                                                dialog.show("请确认缴费信息:", "月份:" + time + "\n应缴:" + item.getIntegrated_water()
                                                        + "\n实缴:" + paid_inAmount, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        showPopupwindow();
                                                        new Thread() {
                                                            @Override
                                                            public void run() {
                                                                chargeDate = dateFormat.format(new Date());
                                                                paymentInfo = item;
                                                               dataToJson(item.getIntegrated_water(), item.getDate(), chargeDate);
                                                                //余额
                                                                BigDecimal bg =(new BigDecimal(m_inAmount).subtract(new BigDecimal(Double.toString(item.getIntegrated_water()))));
                                                                bg = bg.add(new BigDecimal(Double.toString(balance)));
                                                                userMoney=bg.setScale(2,BigDecimal.ROUND_HALF_UP).toString();//四舍五入
                                                                Log.e("==余额=",userMoney);

                                                            }
                                                        }.start();
                                                    }
                                                }, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }, getSupportFragmentManager());
                                            }

                                        }
                                    });
                                }
                            };
                            adapter.addDatas(notPayList);
                            lvNotPay.setAdapter(adapter);
                        } else {
                            lvNotPay.setVisibility(View.GONE);
                            tvPayPs.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {

                    }
                    break;
                case 1:
//                    try {
//                        if (popupWindow != null && popupWindow.isShowing()) {
//                            popupWindow.dismiss();
//                        }
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
//                        JSONObject object = jsonArray.getJSONObject(0);
//                        userNumber.setText(object.optString("c_user_id", ""));
//                        userName.setText(object.optString("c_user_name", ""));
//                        userAddres.setText(object.optString("c_user_address", ""));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                    break;
                case 2:
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(chargeResult);
                        meterReder = jsonObject.optString("C_METER_READER_NAME");
                        //余额
                        currentBalance = jsonObject.optString("N_NOW_BALANCE");
                        Log.i("charge", "handleMessage: balance:" + currentBalance);
                        Log.i("charge", "handleMessage: meterReder:" + meterReder);
                        final MyDialog myDialog = new MyDialog();
                        myDialog.setCancelable(false);
                        myDialog.show("打印小票", "缴费成功！是否打印小票？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                //抄表员打印
                               Intent intent = new Intent(ChargeActivity.this, ChargeBluetoothActivity.class);
                                intent.putExtra("userNum", oldUserNumber.getText().toString() + "");
                                intent.putExtra("userName", userName.getText().toString());
                                intent.putExtra("userAddress", userAddres.getText().toString());
                                intent.putExtra("price", paymentInfo.getUnit_cost() + "");
                                intent.putExtra("total", paymentInfo.getIntegrated_water() + "");//应缴
                                intent.putExtra("month", paymentInfo.getDate());
                                intent.putExtra("start_degree", paymentInfo.getStart_degree() + "");
                                intent.putExtra("end_degree", paymentInfo.getEnd_degree() + "");
                                intent.putExtra("real_dosage", paymentInfo.getReal_dosage() + "");
                                intent.putExtra("d_charge_date", chargeDate);
                                intent.putExtra("meter_reder", meterReder);
                               // intent.putExtra("current_balance", currentBalance + "");//余额
                                intent.putExtra("current_balance", userMoney);//余额
                                intent.putExtra("adjust_dosage", paymentInfo.getAdjust_dosage() + "");
                                intent.putExtra("in_money",m_inAmount);//实缴金额
                               // balance = object.optDouble("n_amount", 0.00);
                                startActivity(intent);
//                                //抄表员打印
//                                Intent intent2 = new Intent(ChargeActivity.this, BluetoothMeteActivity.class);
//                                Bundle bundle = new Bundle();
//                                MeterStampBean stampBean=new MeterStampBean();
//                                stampBean.setMeter_number(new StringBuffer().append(paymentInfo.getMeter_number()).toString());//表号
//                                stampBean.setMeter_data("本月12日至26日");//缴费日期
//                                stampBean.setUser_name(new StringBuffer().append(userName.getText().toString()).toString());//户名
//                                stampBean.setUser_address(new StringBuffer().append(userAddres.getText().toString()).toString());//用户地址
//                                stampBean.setMin_start(new StringBuffer().append(paymentInfo.getStart_degree()).toString());//起度
//                                stampBean.setMax_end(new StringBuffer().append(paymentInfo.getEnd_degree()).toString());//止度
//                                stampBean.setAll_max(new StringBuffer().append(paymentInfo.getEnd_degree()-paymentInfo.getStart_degree()).append("吨").toString());//合计吨数
//                                stampBean.setUser_amount(new StringBuffer().append(m_inAmount).append("元").toString());//缴费金额
//                                stampBean.setWeter_amount(new StringBuffer().append(paymentInfo.getWeter_amount()).toString());//水费明细
//                                stampBean.setWeter_wamount(new StringBuffer().append(paymentInfo.getWeter_wamount()).toString());//污水明细
//                                stampBean.setMeter_payee("锦竹国润供水有限公司");//收款单位
//                                stampBean.setBank_name("中国建设银行股份有限公司");
//                                stampBean.setMeter_account("5105016472260000105");
//                                stampBean.setPayment_address("锦竹市供排水总公司(玉妃路)政务中心D区(景观大道)回澜大道(德阳银行侧)");
//                                stampBean.setPayment_phone("62173366");
//                                stampBean.setMaintain_phone("6202781");
//                                stampBean.setReader_name(new StringBuffer().append(paymentInfo.getReader_name()).toString());//抄表员
//                                stampBean.setReader_time(new StringBuffer().append(paymentInfo.getReader_time()).toString());//抄表时间
//                                stampBean.setReader_remark("请在指定时间内交款,逾期每天加收5%违约金");
//                                bundle.putParcelable(BluetoothMeteActivity.STAMP_KEY, stampBean);
//                                intent2.putExtras(bundle);
//                                startActivity(intent2);
//                                String[] contStr = new String[]{paymentInfo.getMeter_number(),
//                                        "本月12日至26日", userName.getText().toString(),
//                                        userAddres.getText().toString(),
//                                        new StringBuffer().append(paymentInfo.getStart_degree()).toString(),
//                                        new StringBuffer().append(paymentInfo.getEnd_degree()).toString(),
//                                        new StringBuffer().append(paymentInfo.getEnd_degree()-paymentInfo.getStart_degree()).toString()+"吨",
//                                        m_inAmount,
//                                        paymentInfo.getWeter_amount(),
//                                        paymentInfo.getWeter_wamount(),
//                                        "锦竹国润供水有限公司",
//                                        "中国建设银行股份有限公司",
//                                        "5105016472260000105",
//                                        "锦竹市供排水总公司(玉妃路)政务中心D区(景观大道)回澜大道(德阳银行侧)",
//                                        "62173366",
//                                        "6202781",
//                                        new StringBuffer().append(paymentInfo.getReader_name()).toString(),
//                                        new StringBuffer().append(paymentInfo.getReader_time()).toString(),
//                                        "请在指定时间内交款,逾期每天加收5%违约金"     };

                                myDialog.dismiss();
                                finish();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                adapter.clearData();
                                notPayList.clear();
                                showPopupwindow();
                                requireNotPayList("getUser.do", "userid=" + userid);
                            }
                        }, getSupportFragmentManager());
                        Toast.makeText(ChargeActivity.this, "缴费成功!", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    Toast.makeText(ChargeActivity.this, faildMsg, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    Toast.makeText(ChargeActivity.this, "错误的响应码!", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    Toast.makeText(ChargeActivity.this, "网络请求异常!", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    MyDialog dialog = new MyDialog();
                    dialog.show("缴费失败", "您不是收费员，没有收费权限。", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, getSupportFragmentManager());
                    break;

            }
        }
    };

    /**
     * 按时间排序list集合
     *
     * @param list
     */
    private void ListSort(List<UserMeterInfo> list) {
        Collections.sort(list, new Comparator<UserMeterInfo>() {
            @Override
            public int compare(UserMeterInfo o1, UserMeterInfo o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
                try {
                    Date dt1 = format.parse(o1.getDate());
                    Date dt2 = format.parse(o2.getDate());
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }


    //初始化设置
    private void defaultSetting() {
        public_sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    //show弹出框
    public void showPopupwindow() {
        layoutInflater = LayoutInflater.from(ChargeActivity.this);
        view = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) view.findViewById(R.id.frame_animation);
        TextView tips = (TextView) view.findViewById(R.id.tips);
        tips.setText("正在处理，请稍后...");
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.update();
        popupWindow.showAtLocation(layoutCharge, Gravity.CENTER, 0, 0);
       PopWindowUtil.backgroundAlpha(ChargeActivity.this,0.6F);   //背景变暗
        startFrameAnimation();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(ChargeActivity.this,1.0F);
            }
        });
    }

    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }


}
