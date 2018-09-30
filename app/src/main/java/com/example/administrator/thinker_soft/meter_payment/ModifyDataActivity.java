package com.example.administrator.thinker_soft.meter_payment;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;

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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyDataActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.et_ID_number)
    EditText etIDNumber;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.et_use)
    EditText etUse;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.layout)
    LinearLayout layout;

    private String userName, userID, userPhone, userUse, userIDCard, modifyTime;
    private SharedPreferences public_sharedPreferences, sharedPreferences, sharedPreferences_login;
   // private String ip, port;  //接口ip地址   端口
    public int responseCode = 0;  //返回码
    private String result; //网络请求结果

    private AnimationDrawable animationDrawable;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private ImageView frameAnimation;
    private TextView tv_tips;
    private View view;
    private SimpleDateFormat dateFormat;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    popupWindow.dismiss();
                    Toast.makeText(ModifyDataActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 1:
                    popupWindow.dismiss();
                    Toast.makeText(ModifyDataActivity.this, "修改失败!", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    popupWindow.dismiss();
                    Toast.makeText(ModifyDataActivity.this, "网络请求异常!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_data);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        public_sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("login_name", "") + "data", Context.MODE_PRIVATE);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");
        userPhone = intent.getStringExtra("userPhone");
        userUse = intent.getStringExtra("userUse");
        userIDCard = intent.getStringExtra("idCard");
        tvUserId.setText(userID);
        tvUserName.setText(userName);
        etUse.setText(userUse);
        etIDNumber.setText(userIDCard);
        etPhoneNumber.setText(userPhone);

    }

    @OnClick({R.id.iv_back, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                modifyTime = dateFormat.format(new Date());
                showPopupwindow();
                new Thread() {
                    @Override
                    public void run() {
                        dataToJson();
                    }
                }.start();
                break;
        }
    }

    /**
     * 封装json数据
     */
    private void dataToJson() {
        try {
            JSONObject object = new JSONObject();
            object.put("c_user_id", tvUserId.getText().toString());      //用户编号
            object.put("c_identity_card", etIDNumber.getText().toString());      //身份证号
            object.put("c_user_phone", etPhoneNumber.getText().toString());       //电话
            object.put("c_user_remark", etUse.getText().toString());       //用水用途
            object.put("logOperationTime", modifyTime);       //修改时间
            object.put("n_user_edit_operator_id", sharedPreferences_login.getString("userId", ""));       //操作员ID
            Log.i("dataToJson==========>", "封装的json数据为：" + object.toString() + ",userid" + userID);
            modifyData(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 修改用户信息
     * @param JsonData
     */
    private void modifyData(String JsonData) {
        Log.i("dataToJson==========>", "封装的json数据为：" + JsonData);
        try {
            //请求的地址
            Thread.sleep(500);
          //  String httpUrl = "http://" + ip + port + "/SMDemo/updateUserPhone.do";
            String httpUrl=new StringBuffer().append(SkUrl.SkHttp(ModifyDataActivity.this)).append("updateUserPhone.do").toString();
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
                result = stringBuilder.toString();
                Log.i("login_result=========>", result);
                JSONObject jsonObj = new JSONObject(result);
                String msg = jsonObj.optString("messg", "");
                int m = jsonObj.optInt("msg", 0);
                if (m == 1) {//"1" 代表上传成功
                    handler.sendEmptyMessage(0);
                } else {
                    //失败
                    handler.sendEmptyMessage(1);
                }
            } else {
                //返回码不是200
                Log.i("responsecode==========>", "code: " + urlConnection.getResponseCode());
//                isfaild = true;
                handler.sendEmptyMessage(2);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求异常!");
            handler.sendEmptyMessage(2);
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showPopupwindow() {
        layoutInflater = LayoutInflater.from(ModifyDataActivity.this);
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
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
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

}
