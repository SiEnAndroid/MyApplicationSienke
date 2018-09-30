package com.example.administrator.thinker_soft.Security_check.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.SyEastHomeActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.SyEastLoginActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 111
 */
public class MoveGuideActivity extends Activity {

    private ImageView imageViewGif;
    private SharedPreferences sharedpreferencesLogin;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION}; //权限数组
    private List<String> permissionList = new ArrayList<>();  //权限集合
    protected static final int PERMISSION_REQUEST_CODE = 1;  //6.0之后需要动态申请权限，请求码

    /**
     * 各个公司名称
     */
    private String[] firmName = new String[]{"川发展", "渝山", "兴文", "江安", "荥经", "苍溪", "绵竹", "南部", "荣昌", "云阳", "渝川安检", "方根安检", "东渝安检"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_guide);

        SharedPreferencesHelper sharedPreferencesfirm = new SharedPreferencesHelper(MoveGuideActivity.this, SharedPreferencesHelper.SP_FIRM);
        sharedPreferencesfirm.putSharedPreference("firmName", firmName[12]);

        //权限
        requestPermissions();
        imageViewGif = (ImageView) findViewById(R.id.image_gif);
        Glide.with(MoveGuideActivity.this).load(R.mipmap.welcome1).asGif().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewGif);
        sharedpreferencesLogin = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {

//                    Intent intent = new Intent(MoveGuideActivity.this, MoveHomePageActivity.class);
                //对比时间，判断是否到了试用期。
                // TODO: 2017/10/11  
//                    if (sharedpreferencesLogin.getLong("creat_date", 0) != 0) {
//                        long start_time = sharedpreferencesLogin.getLong("creat_date", 0);
//                        long end_time = System.currentTimeMillis();
////                        int s = (int) ((end_time - start_time) / (ONE_DAY_MS));
//                        int s = TimeUtil.betweenDays(start_time, end_time);
//                        Log.i("delta-T", " creat_date!=0,時間差=" + s);
//                        if (s > 30) {
//                            Log.i("delta-T", " creat_date!=0,時間差=" + s);
//                            Toast.makeText(MoveGuideActivity.this, "试用期时间已过！联系思恩科公司继续使用！", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(MoveGuideActivity.this, MoveLoginActivity.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            //直接倒转到移动抄表页面
//                            Log.i("delta-T", " creat_date!=0,時間差=" + s);
//                            Intent intent = new Intent(MoveGuideActivity.this, MeterHomePageActivity.class);
//                            Intent intent = new Intent(MoveGuideActivity.this, MoveHomePageActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    } else {
//                        //直接倒转到移动抄表页面
//                        Log.i("delta-T", " creat_date=0");
//                      Intent intent = new Intent(MoveGuideActivity.this, MeterHomePageActivity.class);
//                        Intent intent = new Intent(MoveGuideActivity.this, MoveHomePageActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
                //直接倒转到移动抄表页面
                goActivity();
       
                super.handleMessage(msg);
            }
        }.sendEmptyMessageDelayed(0, 4000);
    }


    /**
     * 跳转
     */
    private void goActivity() {

        if (sharedpreferencesLogin.getBoolean("have_logined", false)) {
            Intent intent;
            if (SharedPreferencesHelper.getFirm(MoveGuideActivity.this).equals("渝川安检") || SharedPreferencesHelper.getFirm(MoveGuideActivity.this).equals("方根安检")) {

                intent = new Intent(MoveGuideActivity.this, SecurityChooseActivity.class);
            } else if (SharedPreferencesHelper.getFirm(MoveGuideActivity.this).equals("东渝安检")){
                intent = new Intent(MoveGuideActivity.this, SyEastHomeActivity.class);
            }else{
                intent = new Intent(MoveGuideActivity.this, MoveHomePageActivity.class);
            }
            startActivity(intent);
            finish();
        } else {
            Intent intents;
            if (SharedPreferencesHelper.getFirm(MoveGuideActivity.this).equals("渝川安检") || SharedPreferencesHelper.getFirm(MoveGuideActivity.this).equals("方根安检")) {
                intents = new Intent(MoveGuideActivity.this, MobileSecurityLoginActivity.class);
            } else if (SharedPreferencesHelper.getFirm(MoveGuideActivity.this).equals("东渝安检")){
                intents = new Intent(MoveGuideActivity.this, SyEastLoginActivity.class);
            }else {
                intents = new Intent(MoveGuideActivity.this, MoveLoginActivity.class);
            }
            startActivity(intents);
            finish();
        }
    }


    /**
     * 动态申请权限，如果6.0以上则弹出需要的权限选择框，以下则直接运行
     */
    private void requestPermissions() {
        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MoveGuideActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                /*if(ActivityCompat.shouldShowRequestPermissionRationale(UserDetailInfoActivity.this,Manifest.permission.CAMERA)){
                    //已经禁止提示了
                 Toast.makeText(UserDetailInfoActivity.this, "您已禁止该权限，需要重新开启！", Toast.LENGTH_SHORT).show();
                }else {
                    ActivityCompat.requestPermissions(UserDetailInfoActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                }*/
                permissionList.add(permissions[0]);
            }
            if (ContextCompat.checkSelfPermission(MoveGuideActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[1]);
            }
            if (ContextCompat.checkSelfPermission(MoveGuideActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[2]);
            }
            Log.i("requestPermissions", "权限集合的长度为：" + permissionList.size());
            if (!permissionList.isEmpty()) {
                //判断权限集合是否为空
                String[] permissionArray = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(MoveGuideActivity.this, permissionArray, PERMISSION_REQUEST_CODE);
            }
        }
    }

}
