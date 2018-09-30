package com.example.administrator.thinker_soft.meter_code.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.activity.MyPhotoGalleryActivity;
import com.example.administrator.thinker_soft.Security_check.adapter.GridviewImageAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallbackStringListener;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.BluetoothMeteActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.MeterRemarkActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.CommonUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.HttpUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PermissionUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PhotoUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PictureUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.meter_code.sk.widget.MyPopupWind;
import com.example.administrator.thinker_soft.meter_payment.Utils.MyDialog;
import com.example.administrator.thinker_soft.mode.MyGridview;
import com.example.administrator.thinker_soft.mode.MyPhotoUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.mode.Tools;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Double.parseDouble;

/**
 * 抄表详情
 */
public class MeterUserDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ImageView back, save;
    private TextView stamp;
    private View surePopup;
    private SQLiteDatabase db;  //数据库
    private Cursor cursor;
    private PopupWindow popupWindow;
    private RadioButton cancelRb, saveRb;
    private TextView tips;
    private CoordinatorLayout coordinatorlayout;
    private SharedPreferences sharedPreferences_login, sharedPreferences, mySharedPreferences;
    private SimpleDateFormat dateFormat;
    private LayoutInflater layoutInflater;
    private CheckBox cbStateID;
    private LinearLayout layoutRemark;
    private EditText thisMonthEndDegreeEdit;   //本月读数
    private EditText etStateRemark;   //估录备注
    //private EditText etOpenedRemark;//抄表备注
    private TextView etOpenedRemark;//抄表备注
    private String user_remark;
    private String user_state;//用户状态
    private TextView tvUserZt;//用户状态
    private String userID, uploadState;  //当前抄表用户的ID  上传状态
    private String stateID = "0";//正常录入：0 || 估录：1
    private EditText etEstimate;   //估录用量
    private String longitude, latitude, locationAddress, thisMonthEndDegree, thisMonthDosage, lastMonthDegree, lastMonthDosage, meterDate,
            meterFlag, meterUserName, meterUserID, meterUserOldID, meterNumber, meterBelongArea, meterUserAddress,
            meterUserPhone, meterUserBalance, meterOweAcount, meterOweMonths, meterUserProperty, meterBook, stateRemark,
            meterSerialNumb, meterModel, meterReader, meterChangeDosage, meterStartdosage, meterRemission, meterRubbish;
    private TextView longitudeTv, latitudeTv, addressTv, thisMonthDosageTv, lastMonthDegreeTv, lastMonthDosageTv, meterDateTv,
            meterFlagTv, meterUserNameTv, meterUserIDTv, meterUserOldIDTv, meterNumberTv, meterBelongAreaTv, meterUserAddressTv,
            meterUserPhoneTv, meterUserBalanceTv, meterOweAcountTv, meterOweMonthsTv, meterUserPropertyTv, meterBookTv,
            meterSeriaNumbTv, meterModelTv, meterReaderTv, meterChangeDosageTv, meterStartdosageTv, meterRemissionTv, meterRubbishTv;
    private Double monthDosage;
    private CardView locationCardview;
    private static final int LOCATION_REQUEST_CODE = 100;
    // 定位相关
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    /**
     * 下拉刷新，上拉加载
     */
    private RefreshLayout mRefreshLayout;

    /**
     * 图片拍照上传
     */
    private MyGridview myGridview;
    //图片适配器
    private GridviewImageAdapter iamgeAdapter;
    //裁剪的图片路径集合
    private ArrayList<String> cropPathLists = new ArrayList<>();
    //本地图片路径集合
    private ArrayList<String> pathList = new ArrayList<>();
    //自定义弹出框
    private MyPopupWind myPopupWind;
    //裁剪成小图片
    protected static final int CROP_SMALL_PICTURE = 166;
    //拍照
    private static final int TAKE_PHOTO = 188;
    //查看
    protected static final int CHECK_PICTURE = 199;
    //拍照图片路径地址
    private String cropPhotoPath, allPhotoPath;
    //抄表备注请求码
    private static final int REQUEST_REMARK = 88;

    private MyPhotoUtils photoUtils;//路径
    //默认值
    private int currentPosition = 0;

    private TextView tv_pop_number;
    // 拍照布局
    private LinearLayout layout_pop;
    /**
     * 进度条
     */
    private LoadingView loadingView;
    private String errorString;
    private String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_user_detail_info);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        stamp = (TextView) findViewById(R.id.stamp);
        coordinatorlayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        //本月读数
        thisMonthEndDegreeEdit = (EditText) findViewById(R.id.this_month_end_degree);
        save = (ImageView) findViewById(R.id.save);
        longitudeTv = (TextView) findViewById(R.id.longitude);
        latitudeTv = (TextView) findViewById(R.id.latitude);
        //地址
        addressTv = (TextView) findViewById(R.id.address);
        locationCardview = (CardView) findViewById(R.id.location_cardview);
        //本月用量
        thisMonthDosageTv = (TextView) findViewById(R.id.this_month_dosage);
        //上月读数
        lastMonthDegreeTv = (TextView) findViewById(R.id.last_month_degree);

        lastMonthDosageTv = (TextView) findViewById(R.id.last_month_dosage);
        meterDateTv = (TextView) findViewById(R.id.meter_date);
        meterFlagTv = (TextView) findViewById(R.id.meter_flag);
        meterUserNameTv = (TextView) findViewById(R.id.user_name);
        meterUserIDTv = (TextView) findViewById(R.id.user_id);
        meterUserOldIDTv = (TextView) findViewById(R.id.user_old_id);
        meterNumberTv = (TextView) findViewById(R.id.meter_number);
        meterBelongAreaTv = (TextView) findViewById(R.id.area_name);
        meterUserAddressTv = (TextView) findViewById(R.id.user_addres);
        meterUserPhoneTv = (TextView) findViewById(R.id.user_phone);
        meterUserBalanceTv = (TextView) findViewById(R.id.user_balance);
        //欠费金额
        meterOweAcountTv = (TextView) findViewById(R.id.owe_acount);
        //欠费月数
        meterOweMonthsTv = (TextView) findViewById(R.id.owe_months);

        meterUserPropertyTv = (TextView) findViewById(R.id.user_property);
        meterBookTv = (TextView) findViewById(R.id.meter_book);
        meterSeriaNumbTv = (TextView) findViewById(R.id.meter_serial_numb);
        meterModelTv = (TextView) findViewById(R.id.meter_model);
        meterReaderTv = (TextView) findViewById(R.id.meter_reader);
        meterChangeDosageTv = (TextView) findViewById(R.id.change_dosage);
        meterStartdosageTv = (TextView) findViewById(R.id.start_dosage);
        meterRemissionTv = (TextView) findViewById(R.id.remission);
        meterRubbishTv = (TextView) findViewById(R.id.rubbish_cost);
        cbStateID = (CheckBox) findViewById(R.id.cb_state_id);
        etStateRemark = (EditText) findViewById(R.id.et_state_remark);
        layoutRemark = (LinearLayout) findViewById(R.id.layout_remark);
        //抄表备注
        // etOpenedRemark= (EditText) findViewById(R.id.et_opened_remark);
        etOpenedRemark = (TextView) findViewById(R.id.tv_opened_remark);
        //用户状态
        tvUserZt = (TextView) findViewById(R.id.user_zt);

        //估录用量
        etEstimate = (EditText) findViewById(R.id.et_estimate);
        //图片拍照显示
        myGridview = (MyGridview) findViewById(R.id.gridView_detail);
        //拍照布局
        tv_pop_number = (TextView) findViewById(R.id.tv_pop_number);
        layout_pop = (LinearLayout) findViewById(R.id.layout_pop);

    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterUserDetailActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = MeterUserDetailActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        mySharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        /**
         * 设置 下拉刷新 Header风格样式
         */
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        mRefreshLayout.setPrimaryColorsId(R.color.theme_colors, android.R.color.white);
        mRefreshLayout.setEnableLoadmore(false);  //关闭上拉加载功能
        mRefreshLayout.setDisableContentWhenRefresh(true);  //是否在刷新的时候禁止内容的一切手势操作（默认false）
        /**
         * 获取上个页面传过来的参数
         */
        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("user_id");
            uploadState = intent.getStringExtra("upload_state");
            fileName = intent.getStringExtra("fileName");
            if ("已上传".equals(uploadState)) {
                save.setVisibility(View.GONE);
                setEnabled(thisMonthEndDegreeEdit, false);
                setEnabled(etEstimate, false);
                stamp.setText("打印");

            }
            Log.i("MeterUserDetailActivity", "用户ID为：" + userID);
            //初始化
            iamgeAdapter = new GridviewImageAdapter(MeterUserDetailActivity.this, cropPathLists);
            //gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            //设置adapter
            myGridview.setAdapter(iamgeAdapter);
            //监听
            myGridview.setOnItemClickListener(this);
            //  initLocation();  //初始化定位
            ThreadPoolManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    //抄表详情
                    getMeterUserDetailInfo(userID);
                    //图片
                    getPhoto(userID);
                }
            });
        }
        //南部
        if (SharedPreferencesHelper.getFirm(MeterUserDetailActivity.this).equals("南部")) {
            setVBLayout();
        } else if (SharedPreferencesHelper.getFirm(MeterUserDetailActivity.this).equals("荣昌")) {
            setVBLayout();
        } else if (SharedPreferencesHelper.getFirm(MeterUserDetailActivity.this).equals("渝山")) {
            setVBLayout();
        }

        if ((SharedPreferencesHelper.getFirm(MeterUserDetailActivity.this).equals("渝山"))) {
            if (!"已上传".equals(uploadState)) {
                stamp.setText("上传");
            }
        }
    }

    /**
     * 显示
     */
    private void setVBLayout() {
        layout_pop.setVisibility(View.VISIBLE);
        tv_pop_number.setVisibility(View.VISIBLE);

    }

    /**
     * 获取图片显示
     */
    private void getPhoto(String newUserId) {
        if (tabbleIsExist(db, "MeterPhoto")) {
            cropPathLists.clear();
            Cursor cursor = db.rawQuery("select * from MeterPhoto where newUserId=? and loginUserId=? and fileName=?", new String[]{newUserId, sharedPreferences_login.getString("userId", ""),fileName});//查询并获得游标
            while (cursor.moveToNext()) {
                cropPathLists.add(cursor.getString(cursor.getColumnIndex("photoPath")));
            }

            handler.sendEmptyMessage(1);
            Log.i("querySecurityPhoto", "上次照片数量为：" + cropPathLists.size());
        }
    }

    /**
     * 表是否存在
     *
     * @param db
     * @param tableName
     * @return
     */
    public boolean tabbleIsExist(SQLiteDatabase db, String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='" + tableName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        save.setOnClickListener(clickListener);
        stamp.setOnClickListener(clickListener);
        etOpenedRemark.setOnClickListener(clickListener);
        locationCardview.setOnClickListener(clickListener);
        /**
         * 下拉刷新监听
         */
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(400);
                initLocation();  //初始化定位
            }
        });
        cbStateID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stateID = "1";
                    //估录
//                    etEstimate.setVisibility(View.VISIBLE);
//                    // etEstimate.setText(thisMonthDosage);
//                    setEnabled(thisMonthEndDegreeEdit,false);
//                    thisMonthEndDegreeEdit.setText(lastMonthDegree);
                    if (meterFlagTv.getText().toString().equals("未抄")) {
                        layoutRemark.setVisibility(View.VISIBLE);
                        //估录
                        etEstimate.setVisibility(View.VISIBLE);
                        // etEstimate.setText(thisMonthDosage);
                        // setEnabled(thisMonthEndDegreeEdit,false);
                        // thisMonthEndDegreeEdit.setText(lastMonthDegree);
                    }

                } else {
                    stateID = "0";
                    if (meterFlagTv.getText().toString().equals("未抄")) {
                        etEstimate.setVisibility(View.GONE);
                        layoutRemark.setVisibility(View.GONE);
                        thisMonthEndDegreeEdit.setText("");
                        setEnabled(thisMonthEndDegreeEdit, true);
                    }

                }
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    MeterUserDetailActivity.this.finish();
                    break;
                case R.id.save:
                    //保存
                    if (Tools.isInputMethodOpened(MeterUserDetailActivity.this)) {
                        Tools.hideSoftInput(MeterUserDetailActivity.this, thisMonthEndDegreeEdit);
                        // Tools.hideSoftInput(MeterUserDetailActivity.this,etOpenedRemark);
                    }
                    if (!"".equals(thisMonthEndDegreeEdit.getText().toString())) {
                        if (Integer.parseInt(thisMonthEndDegreeEdit.getText().toString()) >= Integer.parseInt(lastMonthDegreeTv.getText().toString())) {
//                            if (!longitudeTv.getText().toString().equals("未获取")) {
                            //保存图片
                            savephoto();
                            meterDate = dateFormat.format(new Date());
                            //抄表日期
                            meterDateTv.setText(meterDate);
                            //估录用量
                            if (stateID.equals("1")) {
                                if (etEstimate.getText().toString().equals("")) {
                                    Toast.makeText(MeterUserDetailActivity.this, "请输入估录用量！", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    if (parseDouble(etEstimate.getText().toString()) < 0) {
                                        Toast.makeText(MeterUserDetailActivity.this, "估录用量不能小于0！", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    // monthDosage = Integer.valueOf(etEstimate.getText().toString().trim());
                                    monthDosage = parseDouble(etEstimate.getText().toString().trim());
                                }
                            } else {
                                //    monthDosage = (Integer.parseInt(thisMonthEndDegreeEdit.getText().toString()) - Integer.parseInt(lastMonthDegree)+Integer.parseInt(meterChangeDosage) + parseDouble(meterRemission));
                                //  Log.e("1用量",monthDosage+"");
                                //用量
                                int degree = Integer.parseInt(thisMonthEndDegreeEdit.getText().toString()) - Integer.parseInt(lastMonthDegree);
                                //换表用量
                                double replaceDegree = aDouble((double) Integer.parseInt(meterChangeDosage), Double.parseDouble(meterRemission));
                                monthDosage = aDouble((double) degree, replaceDegree);
                                Log.e("2用量", monthDosage + "");

                            }
                            if (monthDosage < 0) {
                                monthDosage = 0.0;
                            }
                            if (stateID.equals("1") && etStateRemark.getText().toString().equals("")) {
                                Toast.makeText(MeterUserDetailActivity.this, "请说明估录原因！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            int lastDosage = Integer.valueOf(lastMonthDosage);
                            //超出用量
                            double lasts = sub(monthDosage, Double.parseDouble(lastMonthDosage));

                            if (monthDosage > lastDosage + lastDosage / 2) {
                                final MyDialog myDialog = new MyDialog();
                                myDialog.show("用量异常提示", "本次录入水量为" + thisMonthEndDegreeEdit.getText().toString().trim() + ",用量较上月超出" + (int) lasts + "吨,请确认数据，若无误请点击确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        myDialog.dismiss();
                                        updateMeterUserInfo();
                                        Intent intent = new Intent();
                                        intent.putExtra("this_month_end_degree", thisMonthEndDegreeEdit.getText().toString().trim());
                                        intent.putExtra("this_month_dosage", String.valueOf(monthDosage));
                                        intent.putExtra("this_user_remark", etOpenedRemark.getText().toString());
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        myDialog.dismiss();
                                    }
                                }, getSupportFragmentManager());
                            } else {
                                updateMeterUserInfo();
                                Intent intent = new Intent();
                                intent.putExtra("this_month_end_degree", thisMonthEndDegreeEdit.getText().toString().trim());
                                intent.putExtra("this_month_dosage", String.valueOf(monthDosage));
                                intent.putExtra("this_user_remark", etOpenedRemark.getText().toString());
                                setResult(RESULT_OK, intent);
                                finish();
                            }
//                            } else {
//                                Toast.makeText(MeterUserDetailActivity.this, "请您获取当前经纬度坐标！", Toast.LENGTH_SHORT).show();
//                            }
                        } else {
                            Toast.makeText(MeterUserDetailActivity.this, "本月读数不能小于上月读数哦！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MeterUserDetailActivity.this, "请您输入本月读数！", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.location_cardview:
                    if (!"未获取".equals(longitudeTv.getText().toString())) {
                        Intent intent = new Intent(MeterUserDetailActivity.this, ObtainMeterCoodinateActivity.class);
                        if (!longitudeTv.getText().toString().equals("未获取")) {
                            intent.putExtra("longitude", longitudeTv.getText().toString());
                            intent.putExtra("latitude", latitudeTv.getText().toString());
                        }
                        startActivityForResult(intent, LOCATION_REQUEST_CODE);
                    } else {
                        Snackbar.make(coordinatorlayout, "定位失败！未获取到当前位置，不能查看", Snackbar.LENGTH_SHORT);
                    }
                    break;
                case R.id.stamp:
                    //打印
                    if ("已上传".equals(uploadState)) {
//                        Intent intent1 = new Intent(MeterUserDetailActivity.this, BluetoothActivity.class);
//                        startActivity(intent1);
                        Intent intent2 = new Intent(MeterUserDetailActivity.this, BluetoothMeteActivity.class);
                        startActivity(intent2);
                        finish();
                    } else {
                        showPrintWindow();
                    }
                    break;

                case R.id.tv_opened_remark:
                    //修改备注
                    Intent intent = new Intent(MeterUserDetailActivity.this, MeterRemarkActivity.class);
                    intent.putExtra("remark", etOpenedRemark.getText().toString());
                    intent.putExtra("user_id", String.valueOf(userID));
                    startActivityForResult(intent, REQUEST_REMARK);
                    break;
                default:
                    break;
            }
        }
    };

    //打印窗口
    private void showPrintWindow() {
        layoutInflater = LayoutInflater.from(MeterUserDetailActivity.this);
        surePopup = layoutInflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        popupWindow = new PopupWindow(surePopup, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        cancelRb = (RadioButton) surePopup.findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) surePopup.findViewById(R.id.save_rb);
        tips = (TextView) surePopup.findViewById(R.id.tips);
        tips.setText("确定保存当前数据");
        saveRb.setText("确定");
        //设置点击事件
        cancelRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        saveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.isInputMethodOpened(MeterUserDetailActivity.this)) {
                    Tools.hideSoftInput(MeterUserDetailActivity.this, thisMonthEndDegreeEdit);

                    popupWindow.dismiss();
                }
                if (!"".equals(thisMonthEndDegreeEdit.getText().toString())) {
                    if (Integer.parseInt(thisMonthEndDegreeEdit.getText().toString()) >= Integer.parseInt(lastMonthDegreeTv.getText().toString())) {
//                        if (!longitudeTv.getText().toString().equals("未获取")) {
                        meterDate = dateFormat.format(new Date());
                        meterDateTv.setText(meterDate);

                        //估录用量
                        if (stateID.equals("1")) {
                            if (etEstimate.getText().toString().equals("")) {
                                Toast.makeText(MeterUserDetailActivity.this, "请输入估录用量！", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
//                                if (!thisMonthEndDegreeEdit.getText().toString().equals(lastMonthDegree)) {
//                                    Toast.makeText(MeterUserDetailActivity.this, "估录时，本月止度应和起度一样！", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
                                monthDosage = parseDouble(etEstimate.getText().toString().trim());
                                // monthDosage = Integer.valueOf(etEstimate.getText().toString().trim());
                            }
                        } else {
                            //   monthDosage = (Integer.parseInt(thisMonthEndDegreeEdit.getText().toString()) - Integer.parseInt(lastMonthDegree)+ Integer.parseInt(meterChangeDosage)+ parseDouble(meterRemission));
                            //   Log.e("1用量",monthDosage+"");
                            //用量
                            int degree = Integer.parseInt(thisMonthEndDegreeEdit.getText().toString()) - Integer.parseInt(lastMonthDegree);
                            //换表用量
                            double replaceDegree = aDouble((double) Integer.parseInt(meterChangeDosage), Double.parseDouble(meterRemission));
                            monthDosage = aDouble((double) degree, replaceDegree);
                            Log.e("2用量", monthDosage + "");
                        }
                        // monthDosage = Integer.parseInt(thisMonthEndDegreeEdit.getText().toString()) - Integer.parseInt(lastMonthDegree) + Integer.parseInt(meterChangeDosage) + Double.parseDouble(meterRemission);
                        if (monthDosage < 0) {
                            monthDosage = 0.0;
                        }
                        if (stateID.equals("1") && etStateRemark.getText().toString().equals("")) {
                            Toast.makeText(MeterUserDetailActivity.this, "请说明估录原因！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int lastDosage = Integer.valueOf(lastMonthDosage);
                        //超出用量
                        double lasts = sub(monthDosage, Double.parseDouble(lastMonthDosage));

                        if (monthDosage > lastDosage + lastDosage / 2) {
                            popupWindow.dismiss();
                            final MyDialog myDialog = new MyDialog();
                            myDialog.show("用量异常提示", "本次录入水量为" + thisMonthEndDegreeEdit.getText().toString().trim() + ",用量较上月超出" + (int) lasts + "吨,请确认数据，若无误请点击确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    updateMeterUserInfo();
                                    //上传抄表
                                    setIntent();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myDialog.dismiss();
                                }
                            }, getSupportFragmentManager());
                        } else {
                            popupWindow.dismiss();
                            updateMeterUserInfo();
                            //上传抄表
                            setIntent();
                        }

//                        } else {
//                            Toast.makeText(MeterUserDetailActivity.this, "请您获取当前经纬度坐标！", Toast.LENGTH_SHORT).show();
//                        }
                    } else {
                        Toast.makeText(MeterUserDetailActivity.this, "本月读数不能小于上月读数哦！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MeterUserDetailActivity.this, "请您输入本月读数！", Toast.LENGTH_SHORT).show();
                }
            }

        });
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.showAtLocation(coordinatorlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MeterUserDetailActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterUserDetailActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterUserDetailActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterUserDetailActivity.this.getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case LOCATION_REQUEST_CODE:
                    if (data != null) {

                    }
                    break;
                case TAKE_PHOTO:
                    //拍照
                    //  startCropPhoto(data);
//                    File file = new MyPhotoUtils(MeterUserDetailActivity.this, userID).createCropFile();
//                    Uri tempUri;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        tempUri = FileProvider.getUriForFile(MeterUserDetailActivity.this, "com.example.administrator.thinker_soft.fileprovider", file);//通过FileProvider创建一个content类型的Uri
//                    } else {
//                        // 指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
//                        tempUri = Uri.fromFile(file);
//                    }
//                    if (tempUri != null) {
//                        File file1 = new File(tempUri.getPath());
//                        if (file1.exists()) {
//                            Log.i("MeterUserDetailActivity", "删除原图！ ");
//                            file1.delete();
//                        }
//                    }
                    //  Log.i("startCropPhoto===>", "大小="+FileSizeUtil.getFileOrFilesSize(cropPhotoPath));

                    try {
                        photoUtils.saveFile(PictureUtil.compressSizeImage(cropPhotoPath), cropPhotoPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cropPathLists.add(cropPhotoPath);
                    //    Log.i("startCropPhoto===>", "图片集合长度为：" + cropPathLists.size() + "路径为" + cropPhotoPath+"大小="+FileSizeUtil.getFileOrFilesSize(cropPhotoPath));
                    //  refreshAdapter(cropPathLists);

//                    File temFile = photoUtils.outputIamge(PictureUtil.compressSizeImage(cropPhotoPath));
//                    Uri outputUri = Uri.fromFile(temFile);
//                    allPhotoPath=outputUri.getPath();
//                    cropPathLists.add(outputUri.getPath());
                    //  Log.i("startCropPhoto===>", "图片集合长度为：" + cropPathLists.size() + "路径为" + allPhotoPath+"大小="+FileSizeUtil.getFileOrFilesSize(allPhotoPath));
                    refreshAdapter(cropPathLists);
                    break;

                case CROP_SMALL_PICTURE:
                    //截取
                    Log.i("MeterUserDetailActivity", "图片裁剪回调进来了！ ");
//                    File file = new MyPhotoUtils(MeterUserDetailActivity.this, userID).createTempFile();
//                    Uri tempUri;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        tempUri = FileProvider.getUriForFile(MeterUserDetailActivity.this, "com.example.administrator.thinker_soft.fileprovider", file);//通过FileProvider创建一个content类型的Uri
//                    } else {
//                        // 指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
//                        tempUri = Uri.fromFile(file);
//                    }
//                    if (tempUri != null) {
//                        File file1 = new File(tempUri.getPath());
//                        if (file1.exists()) {
//                            Log.i("MeterUserDetailActivity", "删除原图！ ");
//                            file1.delete();
//                        }
//                    }
//                    cropPathLists.add(cropPhotoPath);
//                    Log.i("startCropPhoto===>", "图片集合长度为：" + cropPathLists.size() + "路径为" + cropPhotoPath);
//                    refreshAdapter(cropPathLists);
                    break;

                case CHECK_PICTURE:
                    //查看
                    if (data != null) {
                        cropPathLists.clear();
                        cropPathLists = data.getStringArrayListExtra("cropPathLists_back");
                        refreshAdapter(cropPathLists);
                    }
                    break;

                case REQUEST_REMARK:
                    //备注
                    if (data != null) {
                        String remark = data.getStringExtra("remarks");
                        String status = data.getStringExtra("status");
                        if (!TextUtils.isEmpty(remark)) {
                            etOpenedRemark.setText(remark);
                            if (status.equals("true")) {
                                updateMeterRemark(remark);//更新数据库
                            }
                        }
                        Log.e("备注", remark);
                        //  updateMeterRemark(remark);//更新
                    }
                    break;
            }

        }
    }

    //初始化定位
    private void initLocation() {
        // 定位初始化
        if (mLocClient == null) {
            mLocClient = new LocationClient(getApplicationContext()); //声明LocationClient类
        }
        mLocClient.registerLocationListener(myListener);     //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
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
            if (TextUtils.isEmpty(addressTv.getText().toString().trim()) || addressTv.getText().toString().trim().equals("未定位")) {
                handler.sendEmptyMessage(6);
            }

//            longitudeTv.setText(longitude);
//            latitudeTv.setText(latitude);
//            addressTv.setText(locationAddress);

            Log.i("currentAddress", locationAddress);
            Log.i("MyLocationListenner", sb.toString());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }


    /**
     * 根据用户ID查询本地数据库抄表详情
     */
    private void getMeterUserDetailInfo(String userID) {
        if (sharedPreferences.getBoolean("show_temp_data", false)) {
            cursor = db.rawQuery("select * from MeterUser where login_user_id=?", new String[]{"0"});//查询并获得游标
        } else {
            cursor = db.rawQuery("select * from MeterUser where login_user_id=? and user_id=? and file_name=?", new String[]{sharedPreferences_login.getString("userId", ""), userID,fileName});//查询并获得游标
        }
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            if ("".equals(cursor.getString(cursor.getColumnIndex("n_state_id")))) {
                stateID = "0";
            } else {
                stateID = cursor.getString(cursor.getColumnIndex("n_state_id"));

            }
            latitude = cursor.getString(cursor.getColumnIndex("n_jw_x"));
            longitude = cursor.getString(cursor.getColumnIndex("n_jw_y"));
            locationAddress = cursor.getString(cursor.getColumnIndex("locationAddress"));
            thisMonthEndDegree = cursor.getString(cursor.getColumnIndex("this_month_end_degree"));
            thisMonthDosage = cursor.getString(cursor.getColumnIndex("this_month_dosage"));
            if ("".equals(cursor.getString(cursor.getColumnIndex("meter_date")))) {
                meterDate = "暂无";
            } else {
                meterDate = cursor.getString(cursor.getColumnIndex("meter_date"));
            }
            lastMonthDegree = cursor.getString(cursor.getColumnIndex("meter_degrees"));
            lastMonthDosage = cursor.getString(cursor.getColumnIndex("last_month_dosage"));
            if (cursor.getString(cursor.getColumnIndex("meterState")).equals("false")) {
                meterFlag = "未抄";
                stateID = "0";
            } else {
                meterFlag = "已抄";
            }
            if (!cursor.getString(cursor.getColumnIndex("user_name")).equals("null")) {
                meterUserName = cursor.getString(cursor.getColumnIndex("user_name"));
            } else {
                meterUserName = "无";
            }
            meterUserID = cursor.getString(cursor.getColumnIndex("user_id"));
            meterUserOldID = cursor.getString(cursor.getColumnIndex("old_user_id"));
            if (!cursor.getString(cursor.getColumnIndex("meter_number")).equals("null")) {
                meterNumber = cursor.getString(cursor.getColumnIndex("meter_number"));
            } else {
                meterNumber = "暂无";
            }
            meterBelongArea = cursor.getString(cursor.getColumnIndex("area_name"));
            meterUserAddress = cursor.getString(cursor.getColumnIndex("user_address"));
            if (!cursor.getString(cursor.getColumnIndex("user_phone")).equals("null")) {
                meterUserPhone = cursor.getString(cursor.getColumnIndex("user_phone"));
            } else {
                meterUserPhone = "暂无";
            }
            meterUserBalance = cursor.getString(cursor.getColumnIndex("user_amount"));
            //欠费金额
            meterOweAcount = cursor.getString(cursor.getColumnIndex("arrearage_amount"));

            meterOweMonths = cursor.getString(cursor.getColumnIndex("arrearage_months"));
            meterUserProperty = cursor.getString(cursor.getColumnIndex("property_name"));
            meterBook = cursor.getString(cursor.getColumnIndex("book_name"));
            meterSerialNumb = cursor.getString(cursor.getColumnIndex("meter_order_number"));
            meterModel = cursor.getString(cursor.getColumnIndex("meter_model"));
            meterReader = cursor.getString(cursor.getColumnIndex("meter_reader_name"));

            meterChangeDosage = cursor.getString(cursor.getColumnIndex("dosage_change"));//

            meterStartdosage = cursor.getString(cursor.getColumnIndex("start_dosage"));
            meterRemission = cursor.getString(cursor.getColumnIndex("remission"));
            meterRubbish = cursor.getString(cursor.getColumnIndex("rubbish_cost"));
            stateRemark = cursor.getString(cursor.getColumnIndex("n_state_remark"));
            user_remark = cursor.getString(cursor.getColumnIndex("user_remark"));//抄表备注
            user_state = cursor.getString(cursor.getColumnIndex("n_user_state"));//用户状态


        }
        handler.sendEmptyMessage(0);
    }

    /**
     * 保存抄表信息到本地数据库
     */
    private void updateMeterUserInfo() {
        ContentValues values = new ContentValues();
        if (latitudeTv.getText().toString().equals("未获取")) {
            values.put("n_jw_x", "0");
            values.put("n_jw_y", "0");
        } else {
            values.put("n_jw_x", latitudeTv.getText().toString());
            values.put("n_jw_y", longitudeTv.getText().toString());
        }
        //定位地址
        values.put("locationAddress", addressTv.getText().toString());
        values.put("this_month_end_degree", thisMonthEndDegreeEdit.getText().toString().trim());//本月读数
        values.put("this_month_dosage", "" + monthDosage);//本月用量
        //  if (meterFlag.equals("未抄")) {
        values.put("meter_date", meterDate);//时间
        //  }
        values.put("meterState", "true");
        values.put("n_state_id", stateID);
        values.put("n_state_remark", etStateRemark.getText().toString());
        values.put("user_remark", etOpenedRemark.getText().toString());//抄表备注
        if (sharedPreferences.getBoolean("show_temp_data", false)) {
            db.update("MeterUser", values, "login_user_id=? and user_id=?", new String[]{"0", userID});
        } else {
            db.update("MeterUser", values, "login_user_id=? and user_id=? and file_name=?", new String[]{sharedPreferences_login.getString("userId", ""), userID,fileName});
        }
        /*db.update("MeterUser", values, "login_user_id=? and user_id=?", new String[]{sharedPreferences_login.getString("userId", ""), userID});*/
        //db.update("MeterUser", values, "login_user_id=?", new String[]{"0"});
    }

    /**
     * 设置请求
     */
    private void setIntent() {
        if ((SharedPreferencesHelper.getFirm(MeterUserDetailActivity.this).equals("渝山"))) {
            //上传抄表
            if (!sharedPreferences.getBoolean("show_temp_data", false)) {
                showLoadView();
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        dataToJsons();
                    }
                });
            } else {
                Log.e("未登录", "----");
                Intent intent = new Intent();
                intent.putExtra("this_month_end_degree", thisMonthEndDegreeEdit.getText().toString().trim());
                intent.putExtra("this_month_dosage", String.valueOf(monthDosage));
                intent.putExtra("this_user_remark", etOpenedRemark.getText().toString());
                intent.putExtra("user_id", String.valueOf(userID));
                setResult(RESULT_OK, intent);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("user_id", String.valueOf(userID));
                editor.apply();
                Intent intent2 = new Intent(MeterUserDetailActivity.this, BluetoothMeteActivity.class);
                startActivity(intent2);
                finish();

            }
        } else {
            Intent intent = new Intent();
            intent.putExtra("this_month_end_degree", thisMonthEndDegreeEdit.getText().toString().trim());
            intent.putExtra("this_month_dosage", String.valueOf(monthDosage));
            intent.putExtra("this_user_remark", etOpenedRemark.getText().toString());
            intent.putExtra("user_id", String.valueOf(userID));
            setResult(RESULT_OK, intent);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("user_id", String.valueOf(userID));
            editor.apply();
            Intent intent2 = new Intent(MeterUserDetailActivity.this, BluetoothMeteActivity.class);
            startActivity(intent2);
            finish();
        }

    }

    /**
     * 加载进度
     */
    private void showLoadView() {
        //加载
        loadingView = new LoadingView(MeterUserDetailActivity.this, R.style.LoadingDialog, "上传中...请稍后");
        loadingView.show();
    }

    /**
     * 关闭进度
     */
    private void dissLoadView() {
        if (loadingView != null) {
            loadingView.dismiss();
        }
    }

    /**
     * 封装为json数据
     */
    private void dataToJsons() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("n_jw_x", latitudeTv.getText().toString());      //纬度
        map.put("n_jw_y", longitudeTv.getText().toString());      //经度
        map.put("n_meter_degrees", thisMonthEndDegreeEdit.getText().toString().trim());       //本月止度
        if (stateID.equals("1")) {
            map.put("nDosage", "" + monthDosage);          //本月用量
        } else {
            map.put("nDosage", "" + monthDosage);          //本月用量
        }
        map.put("n_situation_operatorId", sharedPreferences_login.getString("userId", ""));       //操作员ID
        map.put("c_user_id", userID);       //抄表用户ID
        map.put("n_stateId", stateID);       //是否估录

        // map.put("")//抄表状态1录入;2本月修改
        //  map.put("n_add_state",addState);

        if (stateID.equals("1")) {
            map.put("c_logRemark", etStateRemark.getText().toString());       //估录原因
        } else {
            map.put("c_logRemark", "抄表录入");       //估录原因
        }
        map.put("d_jw_time", meterDate);       //抄表时间
        map.put("c_user_remark", etOpenedRemark.getText().toString());//抄表备注

        Log.i("dataToJso==========>", "封装的json数据为：" + new Gson().toJson(map) + ",userid" + userID);
        //抄表
        setUploadMeterData(map);


    }

    /**
     * 包含图片上传
     */
    private void setUploadMeterData(final Map<String, Object> map) {

        String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterUserDetailActivity.this)).append("meterReadingAdd.do").toString();
        final Map<String, File> file = getPhotoData(userID);
//        if (file!=null){
//            map.put("n_type",popType.toString());
//        }

        HttpUtils.sendFilePosts(MeterUserDetailActivity.this, httpUrl, map, file, new HttpCallbackStringListener() {
            @Override
            public void onFinish(String response) {
                //成功
                Log.i("login_result=========>", response);

                if ("1".equals(response)) {
                    //"1" 代表上传成功
                    handler.sendEmptyMessage(2);
                    updateMeterUserUploadState();
                    //更新本地数据

                } else {
                    //失败
                    handler.sendEmptyMessage(3);
                    errorString = response;

                }
            }

            @Override
            public void onError(Exception e) {
                handler.sendEmptyMessage(3);
            }
        });

    }

    /**
     * 读取本地的图片数据，并上传服务器
     *
     * @param newUserId
     * @return
     */
    public Map<String, File> getPhotoData(String newUserId) {
        //   popType = new StringBuilder();
        Cursor cursor = db.rawQuery("select * from MeterPhoto where newUserId=? and loginUserId=? and file_name=?", new String[]{newUserId, sharedPreferences_login.getString("userId", ""),fileName});//查询并获得游标
        Map<String, File> fileMap = new HashMap<String, File>();
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return null;
        }
        File file = null;
        while (cursor.moveToNext()) {
            file = new File(cursor.getString(1));
            fileMap.put("file" + cursor.getPosition(), file);

        }
        Log.i("getUserData=>", "上传的照片流为：" + fileMap.size());
        cursor.close(); //游标关闭

        return fileMap;

    }

    /**
     * 更新抄表用户上传状态
     */
    private void updateMeterUserUploadState() {

        ContentValues values = new ContentValues();
        values.put("uploadState", "true");
        db.update("MeterUser", values, "login_user_id=? and user_id=? and file_name=?", new String[]{sharedPreferences_login.getString("userId", ""), userID,fileName});
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (meterFlag.equals("未抄")) {
                        mRefreshLayout.autoRefresh();
                        initLocation();  //初始化定位
                    } else {
                        mRefreshLayout.setEnableRefresh(false);
                        longitudeTv.setText(longitude);
                        latitudeTv.setText(latitude);
                        addressTv.setText(locationAddress);
                        if (stateID.equals("1")) {
                            layoutRemark.setVisibility(View.VISIBLE);
                            //估录
                            etEstimate.setVisibility(View.VISIBLE);
                            etStateRemark.setText(stateRemark);
                            thisMonthDosageTv.setVisibility(View.GONE);
                            setEnabled(thisMonthEndDegreeEdit, true);
                            setEnabled(etEstimate, true);
                            etEstimate.setText(thisMonthDosage);
                        } else {
                            layoutRemark.setVisibility(View.GONE);
                            //估录
                            etEstimate.setVisibility(View.GONE);
                            thisMonthDosageTv.setVisibility(View.VISIBLE);
                        }
                    }

                    thisMonthEndDegreeEdit.setText(thisMonthEndDegree);
                    thisMonthEndDegreeEdit.setSelection(thisMonthEndDegree.length());//将光标移至文字末尾


                    thisMonthDosageTv.setText(thisMonthDosage);//本月用量
                    lastMonthDegreeTv.setText(lastMonthDegree);//上月读数
                    lastMonthDosageTv.setText(lastMonthDosage);
                    meterDateTv.setText(meterDate);
                    meterFlagTv.setText(meterFlag);
                    meterUserNameTv.setText(meterUserName);
                    meterUserIDTv.setText(meterUserID);
                    meterUserOldIDTv.setText(meterUserOldID);
                    meterNumberTv.setText(meterNumber);
                    meterBelongAreaTv.setText(meterBelongArea);
                    meterUserAddressTv.setText(meterUserAddress);
                    meterUserPhoneTv.setText(meterUserPhone);
                    meterUserBalanceTv.setText(meterUserBalance);
                    //欠费金额
                    meterOweAcountTv.setText(meterOweAcount);
                    meterOweMonthsTv.setText(meterOweMonths);
                    meterUserPropertyTv.setText(meterUserProperty);
                    meterBookTv.setText(meterBook);
                    meterSeriaNumbTv.setText(meterSerialNumb);
                    meterModelTv.setText(meterModel);
                    meterReaderTv.setText(meterReader);
                    meterChangeDosageTv.setText(meterChangeDosage);
                    meterStartdosageTv.setText(meterStartdosage);
                    meterRemissionTv.setText(meterRemission);
                    meterRubbishTv.setText(meterRubbish);
                    cbStateID.setChecked("0".equals(stateID) ? false : true);
                    //抄表备注
                    etOpenedRemark.setText(user_remark);
                    //用户状态
                    tvUserZt.setText(user_state);

                    break;
                case 1:
                    //图片赋值
                    pathList.clear();
                    pathList.addAll(cropPathLists);
                    //刷新数据
                    refreshAdapter(pathList);
                    break;

                case 2:
                    stamp.setText("打印");
                    //抄表上传
                    dissLoadView();
                    Intent intent = new Intent();
                    intent.putExtra("this_month_end_degree", thisMonthEndDegreeEdit.getText().toString().trim());
                    intent.putExtra("this_month_dosage", String.valueOf(monthDosage));
                    intent.putExtra("this_user_remark", etOpenedRemark.getText().toString());
                    intent.putExtra("user_id", String.valueOf(userID));
                    intent.putExtra("uploadState", "1");
                    setResult(RESULT_OK, intent);
                    Intent intent2 = new Intent(MeterUserDetailActivity.this, BluetoothMeteActivity.class);
                    startActivity(intent2);
                    finish();
                    break;

                case 3:
                    //上传失败
                    dissLoadView();
                    Toast.makeText(MeterUserDetailActivity.this, "上传失败:" + errorString, Toast.LENGTH_LONG).show();
                    Intent intentUp = new Intent();
                    intentUp.putExtra("this_month_end_degree", thisMonthEndDegreeEdit.getText().toString().trim());
                    intentUp.putExtra("this_month_dosage", String.valueOf(monthDosage));
                    intentUp.putExtra("this_user_remark", etOpenedRemark.getText().toString());
                    intentUp.putExtra("user_id", String.valueOf(userID));
                    intentUp.putExtra("uploadState", "0");
                    setResult(RESULT_OK, intentUp);
                    Intent intent3 = new Intent(MeterUserDetailActivity.this, BluetoothMeteActivity.class);
                    startActivity(intent3);
                    finish();
                    break;
                case 6:
                    if (longitude.indexOf("E-") != -1) {
                        longitudeTv.setText("0");
                        latitudeTv.setText("0");
                        addressTv.setText("未获取");
                    }else {
                        longitudeTv.setText(longitude);
                        latitudeTv.setText(latitude);
                        addressTv.setText(locationAddress); 
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 图片监听
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        myGridview.setClickable(false);
        currentPosition = position;
        if (!iamgeAdapter.getDeleteShow() && (iamgeAdapter.getCount() - 1 != position)) {
            Intent intent = new Intent(MeterUserDetailActivity.this, MyPhotoGalleryActivity.class);
            intent.putExtra("currentPosition", currentPosition);
            intent.putExtra("newUserId", userID);
            intent.putStringArrayListExtra("cropPathLists", cropPathLists);
            Log.i("UserDetailInfoActivity", "点击图片跳转进来到预览详情页面的图片数量为：" + cropPathLists.size());
            startActivityForResult(intent, CHECK_PICTURE);
            myGridview.setClickable(true);

        }
        // 如果单击时删除按钮处在显示状态，则隐藏它
        if (iamgeAdapter.getDeleteShow()) {
            iamgeAdapter.setDeleteShow(false);
            iamgeAdapter.notifyDataSetChanged();
        } else {
            if (iamgeAdapter.getCount() - 1 == position) {
                // 判断是否达到了可添加图片最大数
                if (cropPathLists.size() != 9) {
                    //检查权限
                    if (PermissionUtil.requestPermissions(MeterUserDetailActivity.this)) {
                        showAll(view);
                    }
                }
            }
        }
    }

    /**
     * 全屏弹出
     *
     * @param view
     */

    public void showAll(View view) {
        if (myPopupWind != null && myPopupWind.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.view_popup_window, null);
        //测量View的宽高
        CommonUtil.measureWidthAndHeight(upView);
        myPopupWind = new MyPopupWind.Builder(this)
                .setView(R.layout.view_popup_window)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.mypopwindow_anim_style)
                .setViewOnclickListener(new MyPopupWind.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        //拍照
                        Button takePhoto = (Button) view.findViewById(R.id.btn_take_photo);
                        //取消
                        Button takeCancel = (Button) view.findViewById(R.id.btn_cancel);
                        takePhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                myGridview.setClickable(true);
                                if (Tools.hasSdcard()) {
                                    openCamera();//拍照

                                } else {
                                    Toast.makeText(MeterUserDetailActivity.this, "没有SD卡哦，不能拍照！", Toast.LENGTH_SHORT).show();
                                }

                                if (myPopupWind != null) {
                                    myPopupWind.dismiss();
                                }
                            }
                        });
                        takeCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myGridview.setClickable(true);
                                if (myPopupWind != null) {
                                    myPopupWind.dismiss();
                                }
                            }
                        });
                    }
                })
                .create();
        backgroundAlpha(0.6F);
        myPopupWind.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
        myPopupWind.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 打开相机获取图片
     */
    public void openCamera() {
        // File file = new MyPhotoUtils(MeterUserDetailActivity.this, userID).createCropFile();
        photoUtils = new MyPhotoUtils(MeterUserDetailActivity.this, userID);
        cropPhotoPath = photoUtils.generateImgePath();
        File imgFile = new File(cropPhotoPath);
        Uri tempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempUri = FileProvider.getUriForFile(MeterUserDetailActivity.this, "com.example.administrator.thinker_soft.fileprovider", imgFile);//通过FileProvider创建一个content类型的Uri
            //第二参数是在manifest.xml定义 provider的authorities属性
        } else {
            // 指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
            tempUri = Uri.fromFile(imgFile);
        }
        Intent openCameraIntent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }

        openCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        openCameraIntent.putExtra("autofocus", true); // 自动对焦
        openCameraIntent.putExtra("fullScreen", false); // 全屏
        openCameraIntent.putExtra("showActionIcons", false);
        openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        Log.i("openCamera===>", "临时保存的地址为" + tempUri.getPath());
        startActivityForResult(openCameraIntent, TAKE_PHOTO);

    }

    /**
     * 裁剪图片方法实现
     */
    protected void startCropPhoto(Intent data) {
        File file = new MyPhotoUtils(MeterUserDetailActivity.this, userID).createTempFile();
        Uri tempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempUri = FileProvider.getUriForFile(MeterUserDetailActivity.this, "com.example.administrator.thinker_soft.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            // 指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
            tempUri = Uri.fromFile(file);
        }
        if (tempUri != null) {
            File file1 = new MyPhotoUtils(MeterUserDetailActivity.this, userID).createCropFile();
            Uri cropPhotoUri = Uri.fromFile(file1);
            Log.i("startCropPhoto", "图片裁剪的uri = " + cropPhotoUri + "===" + tempUri);
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                //获取路径
                String url = PhotoUtils.getPath(MeterUserDetailActivity.this, cropPhotoUri);
//                String url = getPath(UserDetailInfoActivity.this, tempUri);
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(tempUri, "image/*");
            }
            intent.setDataAndType(tempUri, "image/*");
            // 设置裁剪
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 9);
            intent.putExtra("aspectY", 16);
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", 360);
            intent.putExtra("outputY", 640);
            intent.putExtra("noFaceDetection", true);//取消人脸识别功能
            // 当图片的宽高不足时，会出现黑边，去除黑边
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("return-data", false);//设置为不返回数据
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropPhotoUri);
            startActivityForResult(intent, CROP_SMALL_PICTURE);
            cropPhotoPath = cropPhotoUri.getPath();
            Log.i("startCropPhoto", "图片裁剪的地址为：" + cropPhotoPath);
        } else {
            Log.i("startCropPhoto", "传过来的uri为空！");
            Toast.makeText(MeterUserDetailActivity.this, "拍照失败，请重试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 保存图片到数据库 UPDATE sqlite_sequence SET seq =0 WHER Ename ='TableName';//清除数据
     */
    private void savephoto() {
        Log.e("pathList=", pathList.size() + "=and=" + cropPathLists.size());
        if (pathList.size() > 0) {
            db.delete("MeterPhoto", "newUserId=? and loginUserId=? and file_name=?", new String[]{userID, sharedPreferences_login.getString("userId", ""),fileName});  //删除security_photo表中的当前用户的照片数据
            Log.e("ddd", "删除成功" + pathList.size());
            db.execSQL("update sqlite_sequence set seq=0 where name='MeterPhoto'");
        }
        if (cropPathLists.size() != 0) {
            for (int i = 0; i < cropPathLists.size(); i++) {
                insertMeterPhoto(cropPathLists.get(i));
            }
            //  updateUserPhoto(String.valueOf(cropPathLists.size()));
        }
    }

    /**
     * 本地更新
     */
    private void updateMeterRemark(String stateRemark) {
        ContentValues values = new ContentValues();
        values.put("opened_remark", stateRemark);//抄表备注
        if (sharedPreferences.getBoolean("show_temp_data", false)) {
            db.update("MeterUser", values, "login_user_id=? and user_id=?", new String[]{"0", userID});
        } else {
            db.update("MeterUser", values, "login_user_id=? and user_id=? and file_name=?", new String[]{sharedPreferences_login.getString("userId", ""), userID,fileName});
        }

    }

    /**
     * 根据id删除数据
     */
    public int deletePhoto() {
        return db.delete("MeterPhoto", "newUerId=? and loginUserId=? and file_name=?", new String[]{userID, sharedPreferences_login.getString("userId", ""),fileName});  //删除security_photo表中的当前用户的照片数据
    }

    /**
     * 将拍的照片路径保存到本地数据库安检图片表
     */
    private void insertMeterPhoto(String photoPath) {
        ContentValues values = new ContentValues();
        values.put("newUserId", userID);
        values.put("photoPath", photoPath);
        values.put("fileName",fileName);
        values.put("loginUserId", sharedPreferences_login.getString("userId", ""));
        db.insert("MeterPhoto", null, values);

    }

    /**
     * 刷新数据
     */
    private void refreshAdapter(ArrayList<String> imageList) {
        iamgeAdapter.setGridviewImageList(imageList);
    }

    /**
     * 限制输入
     *
     * @param editText
     * @param b
     */
    private void setEnabled(EditText editText, boolean b) {
        editText.setEnabled(b);
        editText.setFocusable(b);
        editText.setFocusableInTouchMode(b);
    }


    /**
     * double相加减
     *
     * @param a
     * @param b
     * @return
     */
    private static double aDouble(double a, double b) {
        double c = a + b;
        BigDecimal bd = new BigDecimal(c);
        double d = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }

    /**
     * double 相减
     *
     * @param d1
     * @param d2
     * @return
     */
    private static double sub(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
        // 退出时销毁定位
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient = null;
        }
        db.close();
    }
}
