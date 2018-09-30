package com.example.administrator.thinker_soft.meter_code.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.MapMeterActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterDeleteFileActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterMapDownloadActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterSettingsActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterTrackActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserQueryResultActivity;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;
import com.example.administrator.thinker_soft.meter_code.zxing.android.CaptureActivity;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/21 0021.
 *
 * 我的（查询）
 */
public class MyInfoFragment extends Fragment {
    private View view;
    private CardView scanCode, mapMeter, fileManage, mapManage, meterTrack, meterSettings;
    private TextView meterReaderName, meterReaderNumber, companyId;
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private SQLiteDatabase db;  //数据库
    private ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_info, null);

        bindView();
        defaultSetting();
        setViewClickListener();
        return view;
    }

    //绑定控件
    private void bindView() {
        scanCode = (CardView) view.findViewById(R.id.scan_code);
        mapMeter = (CardView) view.findViewById(R.id.map_meter_cardview);
        fileManage = (CardView) view.findViewById(R.id.file_manage);
        mapManage = (CardView) view.findViewById(R.id.map_manage);
        meterTrack = (CardView) view.findViewById(R.id.meter_track);
        meterSettings = (CardView) view.findViewById(R.id.meter_settings);
        meterReaderName = (TextView) view.findViewById(R.id.meter_reader_name);
        meterReaderNumber = (TextView) view.findViewById(R.id.meter_reader_number);
        companyId = (TextView) view.findViewById(R.id.company_id);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(getActivity(), MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
//        sharedPreferences.edit().putString("meter_reader_id", fileItem.getName()).apply();
        companyId.setText(sharedPreferences_login.getInt("company_id",0)+"");
        meterReaderName.setText(sharedPreferences_login.getString("user_name",""));
        meterReaderNumber.setText(sharedPreferences_login.getString("userId",""));
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

    //点击事件
    public void setViewClickListener() {
        scanCode.setOnClickListener(clickListener);
        mapMeter.setOnClickListener(clickListener);
        fileManage.setOnClickListener(clickListener);
        mapManage.setOnClickListener(clickListener);
        meterTrack.setOnClickListener(clickListener);
        meterSettings.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.scan_code:
                    /**扫一扫*/
                    intent = new Intent(getActivity(), CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                    break;
                case R.id.map_meter_cardview:
                    /**地图抄表*/
                    intent = new Intent(getActivity(), MapMeterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.file_manage:
                    /**文件管理*/
                    intent = new Intent(getActivity(), MeterDeleteFileActivity.class);
                    startActivity(intent);
                    break;
                case R.id.map_manage:
                    /**离线地图*/
                    intent = new Intent(getActivity(), MeterMapDownloadActivity.class);
                    startActivity(intent);
                    break;
                case R.id.meter_track:
                    /**抄表轨迹*/
                    intent = new Intent(getActivity(), MeterTrackActivity.class);
                    startActivity(intent);
                    break;
                case R.id.meter_settings:
                    /**设置*/
                    intent = new Intent(getActivity(), MeterSettingsActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
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
//                resultTv.setText("扫码结果为：" + result1);
                queryMeterUserInfo(result1);
            }
        }
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
//        cursor=db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meter_number=?", new String[]{sharedPreferences_login.getString("userId", ""),sharedPreferences.getString("currentFileName",""),meterNumber});//查询并获得游标
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
            if (!cursor.getString(cursor.getColumnIndex("old_user_id")).equals("null")) {
                item.setOldUserID(cursor.getString(cursor.getColumnIndex("old_user_id")));
            } else {
                item.setOldUserID("无");
            }
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
}
