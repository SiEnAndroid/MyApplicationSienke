package com.example.administrator.thinker_soft.meter_code.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserQueryResultActivity;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12 0012.
 */
public class ScanCodeMeterFragment extends Fragment {
    private View view;
    private TextView scanCode;
    private TextView resultTv;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private String[] mPermissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}; //权限数组
    protected static final int PERMISSION_REQUEST_CODE = 1;  //6.0之后需要动态申请权限，请求码
    private List<String> permissionList = new ArrayList<>();  //权限集合
    private List<String> haveDeniedPermissionList = new ArrayList<>();  //已经拒绝过的权限集合
    private boolean mShowRequestPermission = true;//用户是否禁止权限
    private AlertDialog mPermissionDialog;
    private SQLiteDatabase db;  //数据库
    private SharedPreferences sharedPreferences_login,sharedPreferences;
    private ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_scan_code, null);
        bindView();
        defaultSetting();
        setViewClickListener();
        return view;
    }

    public void bindView() {
        scanCode = (TextView) view.findViewById(R.id.scan_code);
        resultTv = (TextView) view.findViewById(R.id.resultTv);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(getActivity(), MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences(sharedPreferences_login.getString("login_name","")+"data", Context.MODE_PRIVATE);
       /* IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.setPrompt("请扫描"); //底部的提示文字，设为""可以置空
        integrator.setCameraId(0); //前置或者后置摄像头
        integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();*/
    }

    //点击事件
    private void setViewClickListener() {
        scanCode.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.scan_code:
                    checkPermissions();
                    break;
            }
        }
    };

    /**
     * 动态申请权限，如果6.0以上则弹出需要的权限选择框，以下则直接运行
     */
    private void checkPermissions() {
        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < mPermissions.length; i++) {           //检测是否授予权限
                if (ContextCompat.checkSelfPermission(getActivity(), mPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {
                        // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                        openAppDetails();
                    } else {
                        // 申请授权。
                        requestPermissions(mPermissions, PERMISSION_REQUEST_CODE);
                    }
                }
            }
            if (checkPermissionAllGranted(mPermissions)) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            } else {
                requestPermissions(mPermissions, PERMISSION_REQUEST_CODE);    //申请权限
            }
            permissionList.clear();
            haveDeniedPermissionList.clear();
            for (int i = 0; i < mPermissions.length; i++) {           //检测是否授予权限
                if (ContextCompat.checkSelfPermission(getActivity(), mPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(mPermissions[i]);      //未授予的权限放进集合
                }
            }
            Log.i("requestPermissions", "权限集合的长度为：" + permissionList.size());
            if (permissionList.isEmpty()) {  //未授予的权限为空，表示都授予了
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            } else {
                String[] permissionArray = permissionList.toArray(new String[permissionList.size()]);   //集合转成数组
                ActivityCompat.requestPermissions(getActivity(), permissionArray, PERMISSION_REQUEST_CODE);    //申请权限
            }
        } else {
            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     * @param permissions
     * @return
     */

    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("ScanCodeMeterFragment","权限授权回调进来了");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i]);
                    if (showRequestPermission) {
                        // 已经拒绝过
                        haveDeniedPermissionList.add(permissions[i]);
                    } else {
                        // 第一次拒绝
                        if(i == (grantResults.length-1)){

                        }
                    }
                }
            }
            if (haveDeniedPermissionList.size() == 0) {
                // 如果所有的权限都授予了, 则执行备份代码
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
    }

    /**
     * 不再提示权限 时的展示对话框
     */
    private void openAppDetails() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(getActivity())
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("给你吧", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mPermissionDialog != null) {
                                mPermissionDialog.dismiss();
                            }
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mPermissionDialog != null) {
                                mPermissionDialog.dismiss();
                            }
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    /**
     * 查询抄表用户信息
     * @param userID
     */
    public void queryMeterUserInfo(String userID) {
        userLists.clear();
        Cursor cursor;
//        if (userID!=null){
            cursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and user_id=?", new String[]{sharedPreferences_login.getString("userId", ""),sharedPreferences.getString("currentFileName",""),userID});//查询并获得游标
//        }else {
//            cursor=db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meter_number=?", new String[]{sharedPreferences_login.getString("userId", ""),sharedPreferences.getString("currentFileName",""),meterNumber});//查询并获得游标
//        }
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(0);
            return;
        }
        while (cursor.moveToNext()) {
            MeterUserListviewItem item = new MeterUserListviewItem();
            item.setMeterID(cursor.getString(cursor.getColumnIndex("meter_order_number")));
            item.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
            item.setUserID(cursor.getString(cursor.getColumnIndex("user_id")));
            if(!cursor.getString(cursor.getColumnIndex("meter_number")).equals("null")){
                item.setMeterNumber(cursor.getString(cursor.getColumnIndex("meter_number")));
            }else {
                item.setMeterNumber("无");
            }
            item.setLastMonthDegree(cursor.getString(cursor.getColumnIndex("meter_degrees")));
            item.setLastMonthDosage(cursor.getString(cursor.getColumnIndex("last_month_dosage")));
            item.setAddress(cursor.getString(cursor.getColumnIndex("user_address")));
            if (cursor.getString(cursor.getColumnIndex("uploadState")).equals("false")) {
                item.setUploadState("");
            } else {
                item.setUploadState("已上传");
            }
            if (cursor.getString(cursor.getColumnIndex("meterState")).equals("false")) {
                item.setMeterState("未抄");
                item.setIfEdit(R.mipmap.meter_false);
                item.setThisMonthDegree("无");
                item.setThisMonthDosage("无");
            } else {
                item.setMeterState("已抄");
                item.setIfEdit(R.mipmap.meter_true);
                item.setThisMonthDegree(cursor.getString(cursor.getColumnIndex("this_month_end_degree")));
                item.setThisMonthDosage(cursor.getString(cursor.getColumnIndex("this_month_dosage")));
            }
            userLists.add(item);
        }
        handler.sendEmptyMessage(1);
        cursor.close();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(getActivity(),"未查到用户信息，请您核对编号是否正确！",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Intent intent = new Intent(getActivity(),MeterUserQueryResultActivity.class);
                    intent.putParcelableArrayListExtra("meter_user_info",userLists);
                    startActivity(intent);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity(), "扫码取消！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "扫描成功，条码值: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String result1 = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                resultTv.setText("扫码结果为：" + result1);
                queryMeterUserInfo(result1);
            }
        }
    }
}
