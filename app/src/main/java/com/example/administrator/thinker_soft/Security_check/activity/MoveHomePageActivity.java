package com.example.administrator.thinker_soft.Security_check.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.adapter.GridviewHomePageAdapter;
import com.example.administrator.thinker_soft.Security_check.model.GridHomePageItem;
import com.example.administrator.thinker_soft.meter_code.activity.MeterHomePageActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn.ReportFlowActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.LayoutUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;
import com.example.administrator.thinker_soft.meter_payment.MeterPaymentActivity;
import com.example.administrator.thinker_soft.meter_payment.Utils.MyDialog;
import com.example.administrator.thinker_soft.mobile_business.MobileBusinessActivity;
import com.example.administrator.thinker_soft.patrol_inspection.activity.PatrolHomePageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */
public class MoveHomePageActivity extends AppCompatActivity {
    private ImageView set;
    private GridView gridView;
    private GridviewHomePageAdapter adapter;
    private List<GridHomePageItem> gridHomePageItems = new ArrayList<>();
    private long exitTime = 0;//退出程序
    private LayoutInflater inflater; //转换器
    private View popupwindowView, quiteView;
    private Button cancelRb, saveRb;
    private PopupWindow popupWindow, quitePopup;
    private RadioButton settings, quite, feedback, versionInfo; //系统设置 退出应用 问题反馈 版本信息
    private TextView tips;
    private RelativeLayout rootRelative;
    private LinearLayout rootLinearlayout;
    private SharedPreferences sharedPreferences_login;
    protected static final int PERMISSION_REQUEST_CODE = 1;  //6.0之后需要动态申请权限，   请求码
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION}; //权限数组
    private List<String> permissionList = new ArrayList<>();  //权限集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_homepage);
        bindView();//绑定控件
        defaultSetting();
        requestPermissions();      //检测权限
        setViewClickListener();//设置点击事件
    }

    //绑定控件
    private void bindView() {
        set = (ImageView) findViewById(R.id.set);
        gridView = (GridView) findViewById(R.id.gridview);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
        rootRelative = (RelativeLayout) findViewById(R.id.root_relative);
        ImageView imageView= (ImageView) findViewById(R.id.image_home);

        LayoutUtil mLayoutUtil = LayoutUtil.getInstance();
        mLayoutUtil.drawViewLinearRBLayout(imageView, 0f, 360f, 0f, 0f, 0f,0f);
    }

    /**
     * 动态申请权限，如果6.0以上则弹出需要的权限选择框，以下则直接运行
     */
    private void requestPermissions() {
        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MoveHomePageActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                /*if(ActivityCompat.shouldShowRequestPermissionRationale(UserDetailInfoActivity.this,Manifest.permission.CAMERA)){
                    //已经禁止提示了
                    Toast.makeText(UserDetailInfoActivity.this, "您已禁止该权限，需要重新开启！", Toast.LENGTH_SHORT).show();
                }else {
                    ActivityCompat.requestPermissions(UserDetailInfoActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                }*/
                permissionList.add(permissions[0]);
            }
            if (ContextCompat.checkSelfPermission(MoveHomePageActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[1]);
            }
            if (ContextCompat.checkSelfPermission(MoveHomePageActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[2]);
            }

            Log.i("requestPermissions", "权限集合的长度为：" + permissionList.size());
            if (!permissionList.isEmpty()) {  //判断权限集合是否为空
                String[] permissionArray = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(MoveHomePageActivity.this, permissionArray, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MoveHomePageActivity.this, "必须同意所有权限才能操作哦！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    //用户同意授权
                } else {
                    //用户拒绝授权
                    Toast.makeText(MoveHomePageActivity.this, "您拒绝了授权！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void getData() {
        int number;
        int index=0;
        //苍溪 移动抄表、抄表缴费
        if (SharedPreferencesHelper.getFirm(MoveHomePageActivity.this).equals("苍溪")){
            number=2;
        }else if (SharedPreferencesHelper.getFirm(MoveHomePageActivity.this).equals("绵竹")){
            //绵竹 移动抄表
            number=1;
        }else if (SharedPreferencesHelper.getFirm(MoveHomePageActivity.this).equals("渝川安检")||SharedPreferencesHelper.getFirm(MoveHomePageActivity.this).equals("方根安检")){
            //移动安检
           // number=3;
            number=4;
            //index=2;
            index=0;
        }else {
            number=2;
        }
        for (int i = index; i < number; i++) {
            GridHomePageItem item = new GridHomePageItem();
            if (i == 0) {
                item.setImageZsbg(R.mipmap.ydcb);
                item.setImageName("移动抄表");
            } else if (i == 1) {
//                item.setImageZsbg(R.mipmap.cbjf);
//                item.setImageName("抄表缴费");
                item.setImageZsbg(R.mipmap.gzl);
                item.setImageName("移动报装");
            }else if (i==2){
                item.setImageZsbg(R.mipmap.gzl);
                item.setImageName("移动报装");
            }else if(i==3){
                item.setImageZsbg(R.mipmap.ydaj);
                item.setImageName("移动安检");
            }

//            if (i == 0) {
//                item.setImageZsbg(R.mipmap.zsbg);
//                item.setImageName("掌上办公");
//            } else if (i == 1) {
//                item.setImageZsbg(R.mipmap.gzl);
//                item.setImageName("工作流");
//            } else if (i == 2) {
//                item.setImageZsbg(R.mipmap.ydcb);
//                item.setImageName("移动抄表");
//            } else if (i == 3) {
//                item.setImageZsbg(R.mipmap.ydaj);
//                item.setImageName("移动安检");
//            } else if (i == 4) {
//                item.setImageZsbg(R.mipmap.ydcx);
//                item.setImageName("移动查询");
////            } else if (i == 5) {
////                item.setImageZsbg(R.mipmap.ydcx);
////                item.setImageName("燃气巡检");
//            } else if (i == 5) {
//                item.setImageZsbg(R.mipmap.cbjf);
//                item.setImageName("抄表缴费");
//            }
            gridHomePageItems.add(item);
        }


    }
    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);  //退出登录以后需要这个备份记录是否更换账号
        getData();
        adapter = new GridviewHomePageAdapter(MoveHomePageActivity.this, gridHomePageItems);
        gridView.setAdapter(adapter);
    }

    //点击事件
    private void setViewClickListener() {
        set.setOnClickListener(clickListener);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridHomePageItem item = gridHomePageItems.get((int) parent.getAdapter().getItemId(position));
                if (item.getImageName().equals("移动安检")) {
                    Intent intent = new Intent(MoveHomePageActivity.this, SecurityChooseActivity.class);
                    startActivity(intent);
                } else if (item.getImageName().equals("移动查询")) {
                    Intent intent = new Intent(MoveHomePageActivity.this, QueryActivity.class);
                    startActivity(intent);
                } else if (item.getImageName().equals("掌上办公")) {
                    Intent intent = new Intent(MoveHomePageActivity.this, MobileBusinessActivity.class);
                    startActivity(intent);
                } else if (item.getImageName().equals("工作流")) {
                    Intent intent = new Intent(MoveHomePageActivity.this, BusinessWebviewActivity.class);
                    startActivity(intent);
                } else if (item.getImageName().equals("移动抄表")) {
                    /**移动抄表 */
                    Intent intent = new Intent(MoveHomePageActivity.this, MeterHomePageActivity.class);
                    startActivity(intent);
                } else if (item.getImageName().equals("燃气巡检")) {
                    Intent intent = new Intent(MoveHomePageActivity.this, PatrolHomePageActivity.class);
                    startActivity(intent);
                } else if (item.getImageName().equals("抄表缴费")) {
                    Intent intent = new Intent(MoveHomePageActivity.this, MeterPaymentActivity.class);
                    startActivity(intent);
                }else if (item.getImageName().equals("移动报装")){
                    /**移动报装 */
//                    Intent intent = new Intent(MoveHomePageActivity.this, LotActivity.class);
//                    startActivity(intent);
                    Intent intent = new Intent(MoveHomePageActivity.this, ReportFlowActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.set:
                    createPopupwindow();
                    break;
            }
        }
    };

    //系统设置popupwindow
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void createPopupwindow() {
        inflater = LayoutInflater.from(MoveHomePageActivity.this);

        popupwindowView = inflater.inflate(R.layout.popup_window_security, null);
        //获取PopupWindow中View的宽高
        popupwindowView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow = new PopupWindow(popupwindowView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        settings = (RadioButton) popupwindowView.findViewById(R.id.settings);//系统设置
        quite = (RadioButton) popupwindowView.findViewById(R.id.quite);//安全退出
        feedback = (RadioButton) popupwindowView.findViewById(R.id.feedback);
        versionInfo = (RadioButton) popupwindowView.findViewById(R.id.version_info);
        //设置点击事件
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.settings:
                        Intent intent = new Intent(MoveHomePageActivity.this, SystemSettingActivity.class);
                        startActivity(intent);
                        popupWindow.dismiss();
                        break;
                }
            }
        });
        quite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showQuitePopup();
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoveHomePageActivity.this, FeedbackActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        versionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                final MyDialog myDialog = new MyDialog();
                myDialog.show("版本信息", "版本号:" + getLocalVersionName(MoveHomePageActivity.this), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDialog.dismiss();
                    }
                }, getSupportFragmentManager());
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        //PopWindowUtil.showLocation(popupWindow,set);
        popupWindow.showAsDropDown(set, -PopWindowUtil.dip2px(MoveHomePageActivity.this,68), 0);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //弹出退出登录前提示popupwindow
    public void showQuitePopup() {
        inflater = LayoutInflater.from(MoveHomePageActivity.this);
        quiteView = inflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        quitePopup = new PopupWindow(quiteView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        tips = (TextView) quiteView.findViewById(R.id.tips);
        cancelRb = (RadioButton) quiteView.findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) quiteView.findViewById(R.id.save_rb);
        //设置点击事件
        tips.setText("退出后不会删除历史数据，下次登录依然可以使用本账号！");
        saveRb.setTextColor(getResources().getColor(R.color.red));
        saveRb.setText("退出登录");
        cancelRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitePopup.dismiss();
            }
        });
        saveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitePopup.dismiss();
                Intent intent = new Intent(MoveHomePageActivity.this, MoveLoginActivity.class);
                startActivity(intent);
                sharedPreferences_login.edit().putBoolean("have_logined", false).apply();
                finish();
            }
        });
        quitePopup.update();
        quitePopup.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        quitePopup.setAnimationStyle(R.style.camera);
        quitePopup.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        quitePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MoveHomePageActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MoveHomePageActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MoveHomePageActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MoveHomePageActivity.this.getWindow().setAttributes(lp);
    }

    /**
     * 获取软件版本号
     */
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 捕捉返回事件按钮
     * 因为此 Activity继承 TabActivity,用 onKeyDown无响应，
     * 所以改用 dispatchKeyEvent
     * <p>
     * 一般的 Activity 用 onKeyDown就可以了
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Log.i("exitTime==========>", System.currentTimeMillis() - exitTime + "");
            //-------------Activity.this的context 返回当前activity的上下文，属于activity，activity 摧毁他就摧毁
            //-------------getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
