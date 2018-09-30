package com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.PopupwindowListItem;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.GridviewImageAdapters;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.MultiAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.MultisAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.OnItemClickLitener;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.SingleAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NewsReviseParams;
import com.example.administrator.thinker_soft.meter_code.sk.bean.PhotoPathBean;
import com.example.administrator.thinker_soft.meter_code.sk.db.DBManage;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.observer.ObserverManager;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.PhotoGalleryActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.SecurityUserDetailActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.EditTextUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.FileUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtils;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.meter_code.sk.widget.UIHandler;
import com.example.administrator.thinker_soft.mode.HttpUtils;
import com.example.administrator.thinker_soft.mode.MyGridview;
import com.example.administrator.thinker_soft.mode.MyPhotoUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.mode.Tools;
import com.example.administrator.thinker_soft.mode.photo.ImageUtil;
import com.example.administrator.thinker_soft.mode.photo.WaterMask;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author g
 * @FileName SyEastCheckListDatailActivity
 * @date 2018/9/30 10:43
 */
public class SyEastCheckListDatailActivity extends BaseActivity {

    /**
     * 安检情况数据
     */
    private List<PopupwindowListItem> securityCaseItemList;
    /**
     * 安检隐患数据
     */
    private List<PopupwindowListItem> securityCaseHiddenType;
    /**
     * 安检隐患原因数据
     */
    private List<PopupwindowListItem> securityCaseHiddenReason;
    /**
     * 用于保存选中的隐患类型信息
     */
    private List<PopupwindowListItem> selectDatas = new ArrayList<>();
    /**
     * 用于保存选中的隐患原因信息
     */
    private List<PopupwindowListItem> selectDatasWhy = new ArrayList<>();
    /**
     * 单选
     */
    private SingleAdapter singleAdapter;
    /**
     * 多选
     */
    private MultiAdapter multiAdapter;
    private MultisAdapter multiAdapterWhy;
    private PopupWindow popupWindow, popupWindowMulti, popupWindowMultiWhy, popupwindowView,poupwindowUp;
    LinearLayout  noData,noDataWhy;
    private int selectorPosition = 0;
    private int currentPosition = 0;
    private GridviewImageAdapters iamgeAdapter;
    /**
     * 裁剪的图片路径集合
     */
    private ArrayList<PhotoPathBean> cropPathLists = new ArrayList<>();
    /**大图页面返回的图片路径集合*/
    private ArrayList<String>filePathLists = new ArrayList<>();
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    private boolean flag = false;
    /**安检时间*/
    private String ajTime;
    /**本次安检时间*/
    private String checkTime;
    private String newUserId;
    private String nameSigle;
    /**
     * 权限集合
     */
    private List<String> permissionList = new ArrayList<>();
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private MyPhotoUtils photoUtils;
    private String userName, meterNumber, securityNumber, phoneNumber, address, securityType, securityCase, hiddenType, hiddenReason, remarks;

    private SharedPreferences sharedPreferences, sharedPreferences_login;
    /**公司id*/
    private String company_id;

    /**修改信息*/
    private String userNameEt,userPhone;
    private String idSigle;
    private String strErro;
    private LoadingView loadingView;
    /**安检情况类型id,安检隐患类型id,安检隐患原因id*/
    private String securityCaseItemId = "", securityHiddenItemId = "", hiddenReasonItemId = "";
    private String securityCaseItemName = "", securityHiddenItemName = "", hiddenReasonItemName = "";
    /**
     * 裁剪的图片路径
     */
    private String cropPhotoPath;
    private UIMyHandler myHandler = new UIMyHandler(this);
    /**
     * 拍照
     */
    protected static final int TAKE_PHOTO = 100;
    protected static final int REQUEST_PHOTO = 500;
    /**
     * 6.0之后需要动态申请权限，   请求码
     */
    protected static final int PERMISSION_REQUEST_CODE = 1;
    
    @BindView(R.id.tv_userName)
    EditText tvUserName;
    @BindView(R.id.tv_userid)
    TextView tvUserid;
    @BindView(R.id.tv_security_type)
    TextView tvSecurityType;
    @BindView(R.id.meter_number)
    EditText tvMeterNumber;
    @BindView(R.id.user_phone_number)
    EditText userPhoneNumber;
    @BindView(R.id.tv_stateTime)
    TextView tvStateTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.user_address)
    EditText userAddress;
    @BindView(R.id.tv_reason_situation)
    TextView tvSituation;
    @BindView(R.id.tv_reason_type)
    TextView tvType;
    @BindView(R.id.tv_hidden)
    TextView tvReason;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.gridView_pop)
    MyGridview gridView;
    @BindView(R.id.clear)
    TextView clear;
    @BindView(R.id.layout_reason_type)
    LinearLayout layoutRype;

    @BindView(R.id.layout_hidden)
    LinearLayout layoutHidden;


    /**
     * 用户id
     */
    private String userNewId;
    private String ifUpload, taskId;
    private String isChecked = "未检";
    /**
     * 安检结束时间
     */
    private String endTime;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_case)
    TextView tvCase;

    @BindView(R.id.layout)
    LinearLayout layout;
    @Override
    protected int getContentViewID() {
        return R.layout.activity_dy_check_datail;
    }

    @Override
    protected void initView() {
        tvTitle.setText("安检详细信息");
        clear.setText("编辑");
        clear.setVisibility(View.GONE);
        securityCaseItemList = DBManage.getInstance(SyEastCheckListDatailActivity.this).getSecurityCheckCase();
        securityCaseHiddenType = DBManage.getInstance(SyEastCheckListDatailActivity.this).getSecurityHiddenType();
        db = MySqliteHelper.getInstance(SyEastCheckListDatailActivity.this).getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = SyEastCheckListDatailActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        company_id = sharedPreferences_login.getInt("company_id", 0) + "";
        getIntentData();
        setGridviewImage();
        //设置不可编辑
        EditTextUtils.setEnabled(tvUserName,false);
        EditTextUtils.setEnabled(userAddress,false);
        EditTextUtils.setEnabled(tvMeterNumber,false);
        EditTextUtils.setEnabled(userPhoneNumber,false);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            userNewId = intent.getStringExtra("user_new_id");
            taskId = intent.getStringExtra("taskId");
            ifUpload = intent.getStringExtra("if_upload");
            isChecked = intent.getStringExtra("is_checked");
            endTime = intent.getStringExtra("end_time");
            //获取默认数据
            ThreadPoolManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    getUserData(userNewId, sharedPreferences_login.getString("userId", ""));
                }
            });
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!flag){
            if ("已上传".equals(ifUpload)) {
                //createNoOperate();
//                saveCardview.setEnabled(false);
//                cv_situation.setEnabled(false);
//                cv_type.setEnabled(false);
//                cv_why_reason.setEnabled(false);
                tvCase.setText("已上传，不可更改");
                EditTextUtils.setEnabled(etRemark,false);

            }
            flag = true;
        }
    }
    /**
     * 监听
     *
     * @param view
     */
    @OnClick({R.id.layout_reason_situation, R.id.layout_reason_type, R.id.layout_hidden, R.id.cv_case, R.id.back, R.id.clear})
    public void OnclickCheck(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.layout_reason_situation:
                //安检情况
                showPopWindowSingle();
                break;
            case R.id.layout_reason_type:
                //隐患类型
                //安检类型
                if(!"点击选择".equals(tvSituation.getText())){
                    showPopWindowMulti();
                }else {
                    ToastUtils.showShort(SyEastCheckListDatailActivity.this, "请您先选择安检情况!");
                
                }
                break;
            case R.id.layout_hidden:
                //隐患原因
                if(securityCaseHiddenReason!=null) {
                    //安检原因
                    initMultiWhy();
                }else {
                    ToastUtils.showShort(SyEastCheckListDatailActivity.this, "请您先选安检类型!");
                }
                break;
            case R.id.clear:
                //编辑
                if (clear.getText().toString().trim().equals("编辑")){
                    clear.setText("完成");
                    EditTextUtils.setEnabled(tvUserName,true);
                    EditTextUtils.setEnabled(userPhoneNumber,true);
                    tvUserName.setBackgroundColor(Color.parseColor("#efeff4"));
                    userPhoneNumber.setBackgroundColor(Color.parseColor("#efeff4"));
                }else {
                    clear.setText("编辑");
                    EditTextUtils.setEnabled(tvUserName,false);
                    EditTextUtils.setEnabled(userPhoneNumber,false);
                    tvUserName.setBackgroundColor(Color.WHITE);
                    userPhoneNumber.setBackgroundColor(Color.WHITE);
                    //修改信息
                    String name=tvUserName.getText().toString().trim();
                    String phone=userPhoneNumber.getText().toString().trim();
                    if (!userNameEt.equals(name)||!userPhone.equals(phone)){
                        NewsReviseParams params=new NewsReviseParams();
                        params.setC_meter_number(meterNumber);
                        params.setC_user_id(userNewId);
                        params.setC_user_phone(phone);
                        params.setC_user_name(name);
                        params.setN_meter_direction("");
                        params.setN_log_operator_id(company_id);
                        String log=new StringBuffer().append(!userNameEt.equals(name)?"[用户名由"+userNameEt+"改为"+name+"]":"")
                                .append(!phone.equals(userPhone)?"[电话号码由"+userPhone+"改为"+phone+"]":"")
                                .toString();
                        params.setC_log(log);
                        ReviseRequest(params);
                    }
                }
                break;
            case R.id.cv_case:
                //保存

                break;

            default:
                break;

        }
    }

    /**
     * 图片展示
     */
    private void setGridviewImage() {
        iamgeAdapter = new GridviewImageAdapters(SyEastCheckListDatailActivity.this, cropPathLists);
        gridView.setAdapter(iamgeAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                gridView.setClickable(false);
                currentPosition = position;
                Log.e("选择的项", position + "");
                if (!iamgeAdapter.getDeleteShow() && (iamgeAdapter.getCount() - 1 != position)) {
                    //  Intent intent = new Intent(MeterUserDetailActivity.this, MyPhotoGalleryActivity.class);
                    Intent intent = new Intent(SyEastCheckListDatailActivity.this, PhotoGalleryActivity.class);
                    intent.putExtra("currentPosition", currentPosition);
                    intent.putExtra("newUserId", userNewId);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cropPathLists", cropPathLists);
                    intent.putExtras(bundle);
                    Log.i("UserDetailInfoActivity", "点击图片跳转进来到预览详情页面的图片数量为：" + cropPathLists.size());
                    startActivityForResult(intent, REQUEST_PHOTO);
                    gridView.setClickable(true);

                }

                // 如果单击时删除按钮处在显示状态，则隐藏它
                if (iamgeAdapter.getDeleteShow()) {
                    iamgeAdapter.setDeleteShow(false);
                    iamgeAdapter.notifyDataSetChanged();
                } else {
                    if (iamgeAdapter.getCount() - 1 == position) {
                        // 判断是否达到了可添加图片最大数
                        if (cropPathLists.size() != 9) {
                            requestPermissions();      //检测权限
                        }
                    }
                }
            }
        });

    }

    /**
     * 根据用户新编号查询用户信息
     */
    public void getUserData(String userId, String loginUserId) {
        //查询并获得游标
        Cursor cursor = db.rawQuery("select * from User where newUserId=? and loginUserId=?", new String[]{userId, loginUserId});
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            userName = cursor.getString(cursor.getColumnIndex("userName"));
            meterNumber = cursor.getString(cursor.getColumnIndex("meterNumber"));
            securityNumber = cursor.getString(cursor.getColumnIndex("securityNumber"));
            Log.i("meterNumber", "meterNumber=" + meterNumber);
            phoneNumber = cursor.getString(cursor.getColumnIndex("userPhone"));
            newUserId = cursor.getString(cursor.getColumnIndex("newUserId"));
            address = cursor.getString(cursor.getColumnIndex("userAddress"));
            securityType = cursor.getString(cursor.getColumnIndex("securityType"));
            securityCase = cursor.getString(cursor.getColumnIndex("security_case"));
            hiddenType = cursor.getString(cursor.getColumnIndex("security_hidden"));
            hiddenReason = cursor.getString(cursor.getColumnIndex("security_hidden_reason"));
            ajTime=cursor.getString(cursor.getColumnIndex("currentTime"));
            remarks = cursor.getString(cursor.getColumnIndex("remarks"));
            if (!"0".equals(cursor.getString(cursor.getColumnIndex("photoNumber")))) {
                //读取照片信息并显示
                querySecurityPhoto(userNewId);
            }
        }
        myHandler.sendEmptyMessage(0);
        cursor.close(); //游标关闭
    }

    /**
     * 根据安检ID查询用户是否处于安检状态，如果是安检状态，则显示上次安检所记录的图片，否则不显示
     */
    private void querySecurityPhoto(String newUserId) {
        //查询并获得游标
        Cursor cursor = db.rawQuery("select * from security_photo where newUserId=? and loginUserId=?", new String[]{newUserId, sharedPreferences_login.getString("userId", "")});
        while (cursor.moveToNext()) {
            PhotoPathBean bean = new PhotoPathBean();
            bean.setCropPath(cursor.getString(cursor.getColumnIndex("photoPath")));
            bean.setType("");
            bean.setTypeId("");
            cropPathLists.add(bean);
            filePathLists.add(cursor.getString(cursor.getColumnIndex("photoPath")));
        }
        myHandler.sendEmptyMessage(1);
        Log.i("querySecurityPhoto", "上次照片数量为：" + cropPathLists.size());
    }

    /**
     * 安检情况
     */
    private void showPopWindowSingle() {

        if (popupWindow == null) {
            View contentView = getLayoutInflater().inflate(
                    R.layout.view_pop_security, null);
            popupWindow = new PopupWindow(contentView, MyApplication.screenWidth - 80, LinearLayout.LayoutParams.WRAP_CONTENT);
            //绑定控件ID
            RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
            LinearLayout linearLayout = (LinearLayout) contentView.findViewById(R.id.confirm_layout);
            TextView title = (TextView) contentView.findViewById(R.id.tv_title);
            noData=(LinearLayout) contentView.findViewById(R.id.no_data);
            title.setText("选择安全情况");
            //设置颜色分割线
            StaggeredGridLayoutManager layoutmanager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            //设置RecyclerView 布局
            recyclerView.setLayoutManager(layoutmanager);

            singleAdapter = new SingleAdapter(securityCaseItemList);
            recyclerView.setAdapter(singleAdapter);
            //空数据
            if (securityCaseItemList.size()==0){
                noData.setVisibility(View.VISIBLE);
            }
            singleAdapter.setOnItemClickLitener(new SingleAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    singleAdapter.setSelection(position);
                    nameSigle = securityCaseItemList.get(position).getItemName();
                    idSigle=securityCaseItemList.get(position).getItemId();
                }

            });
            //确认
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    if (!TextUtils.isEmpty(nameSigle)){
                        tvSituation.setText(nameSigle);
                        securityCaseItemId =idSigle;
                        securityCaseItemName =nameSigle;

                    }
                    if ((tvSituation.getText().toString().equals("安全隐患"))) {
                        showHiddenTypeAndReason();
                    } else {
                        if (!TextUtils.isEmpty(nameSigle)) {
                            noShowHiddenTypeAndReason();
                        }
                    }

                }
            });
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.update();
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
            popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            //背景变暗
            PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 0.6F);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 1.0F);
                }
            });
        } else {
            if (!popupWindow.isShowing()) {
                //背景变暗
                PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 0.6F);
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                if (securityCaseItemList.size()==0){
                    noData.setVisibility(View.VISIBLE);
                }
            } else {
                popupWindow.dismiss();
            }
        }
    }

    /**
     * 隐患类型
     */
    private void showPopWindowMulti() {
        if (popupWindowMulti == null) {
            View contentView = getLayoutInflater().inflate(
                    R.layout.view_pop_security, null);
            popupWindowMulti = new PopupWindow(contentView, MyApplication.screenWidth - 80, LinearLayout.LayoutParams.WRAP_CONTENT);
            //绑定控件ID
            RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
            TextView title = (TextView) contentView.findViewById(R.id.tv_title);

            title.setText("选择隐患类型");
            //设置颜色分割线
            StaggeredGridLayoutManager layoutmanager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            //设置RecyclerView 布局
            recyclerView.setLayoutManager(layoutmanager);
            LinearLayout linearLayout = (LinearLayout) contentView.findViewById(R.id.confirm_layout);
            multiAdapter = new MultiAdapter(securityCaseHiddenType);
            recyclerView.setAdapter(multiAdapter);
            multiAdapter.setOnItemClickLitener(new OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (!MultiAdapter.isSelected.get(position)) {
                        // 修改map的值保存状态
                        MultiAdapter.isSelected.put(position, true);
                        multiAdapter.notifyItemChanged(position);
                        selectDatas.add(securityCaseHiddenType.get(position));

                    } else {
                        // 修改map的值保存状态
                        MultiAdapter.isSelected.put(position, false);
                        multiAdapter.notifyItemChanged(position);
                        selectDatas.remove(securityCaseHiddenType.get(position));
                    }
                }
            });
            //确认
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindowMulti.dismiss();
                    securityCaseHiddenReason = new ArrayList<>();
                    StringBuffer buffer = new StringBuffer();
                    StringBuffer bufferId = new StringBuffer();
                    StringBuffer buffername= new StringBuffer();
                    int i=0;
                    for (PopupwindowListItem item : selectDatas) {
                        if (i==selectDatas.size()-1){
                            buffer.append(item.getItemName());
                            buffername.append(item.getItemName());
                            bufferId.append(item.getItemId());
                        }else {
                            buffer.append(item.getItemName() + " | ");
                            buffername.append(item.getItemName()+",");
                            bufferId.append(item.getItemId()+",");
                        }
                        i++;
                        getSecurityHiddenReason(item.getItemId());
                    }
                    String reasonStr=buffer.toString();

                    if (!TextUtils.isEmpty(reasonStr)){
                        tvType.setText(reasonStr);
                        securityHiddenItemId=bufferId.toString();
                        securityHiddenItemName=buffername.toString();
                    }

                    //清除数据
                    if (multiAdapterWhy != null) {
                        multiAdapterWhy.clear();
                        selectDatasWhy.clear();
                    }
                }
            });
            popupWindowMulti.setFocusable(true);
            popupWindowMulti.setOutsideTouchable(true);
            popupWindowMulti.update();
            popupWindowMulti.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
            popupWindowMulti.setAnimationStyle(R.style.mypopwindow_anim_style);
            popupWindowMulti.showAtLocation(layout, Gravity.CENTER, 0, 0);
            //背景变暗
            PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 0.6F);
            popupWindowMulti.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 1.0F);
                }
            });
        } else {
            if (!popupWindowMulti.isShowing()) {
                //背景变暗
                PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 0.6F);
                popupWindowMulti.showAtLocation(layout, Gravity.CENTER, 0, 0);

            } else {
                popupWindowMulti.dismiss();
            }
        }
    }

    /**
     * 隐患原因
     */
    private void initMultiWhy() {
        if (popupWindowMultiWhy == null) {
            View contentView = getLayoutInflater().inflate(
                    R.layout.view_pop_security, null);
            popupWindowMultiWhy = new PopupWindow(contentView, MyApplication.screenWidth - 80, LinearLayout.LayoutParams.WRAP_CONTENT);
            //绑定控件ID
            RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
            TextView title = (TextView) contentView.findViewById(R.id.tv_title);
            noDataWhy=(LinearLayout) contentView.findViewById(R.id.no_data);
            title.setText("选择隐患原因");
            //设置颜色分割线
            StaggeredGridLayoutManager layoutmanager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            //设置RecyclerView 布局
            recyclerView.setLayoutManager(layoutmanager);
            LinearLayout linearLayout = (LinearLayout) contentView.findViewById(R.id.confirm_layout);
            multiAdapterWhy = new MultisAdapter(securityCaseHiddenReason);
            recyclerView.setAdapter(multiAdapterWhy);
            if (securityCaseHiddenReason.size()==0){
                noDataWhy.setVisibility(View.VISIBLE);
            }else {
                noDataWhy.setVisibility(View.GONE);
            }
            multiAdapterWhy.setOnItemClickLitener(new OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (!MultisAdapter.isSelected.get(position)) {
                        // 修改map的值保存状态
                        MultisAdapter.isSelected.put(position, true);
                        multiAdapterWhy.notifyItemChanged(position);
                        selectDatasWhy.add(securityCaseHiddenReason.get(position));

                    } else {
                        // 修改map的值保存状态
                        MultisAdapter.isSelected.put(position, false);
                        multiAdapterWhy.notifyItemChanged(position);
                        selectDatasWhy.remove(securityCaseHiddenReason.get(position));
                    }
                }
            });
            //确认
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindowMultiWhy.dismiss();
                    StringBuffer buffer = new StringBuffer();
                    StringBuffer bufferId = new StringBuffer();
                    StringBuffer buffername= new StringBuffer();
                    buffer.setLength(0);
                    bufferId.setLength(0);
                    buffername.setLength(0);
                    int i=0;
                    for (PopupwindowListItem item : selectDatasWhy) {
                        if (i==selectDatasWhy.size()-1){
                            buffer.append(item.getItemName());
                            buffername.append(item.getItemName());
                            bufferId.append(item.getItemId());
                        }else {
                            buffer.append(item.getItemName() + " | ");
                            buffername.append(item.getItemName()+",");
                            bufferId.append(item.getItemId()+",");
                        }
                        i++;
                    }
                    String reasonStr=buffer.toString();
                    if (!TextUtils.isEmpty(reasonStr)){
                        tvReason.setText(reasonStr);
                        hiddenReasonItemId=bufferId.toString();
                        hiddenReasonItemName=buffername.toString();
                    }

                }
            });
            popupWindowMultiWhy.setFocusable(true);
            popupWindowMultiWhy.setOutsideTouchable(true);
            popupWindowMultiWhy.update();
            popupWindowMultiWhy.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
            popupWindowMultiWhy.setAnimationStyle(R.style.mypopwindow_anim_style);
            popupWindowMultiWhy.showAtLocation(layout, Gravity.CENTER, 0, 0);
            //背景变暗
            PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 0.6F);
            popupWindowMultiWhy.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 1.0F);
                }
            });
        } else {
            if (!popupWindowMultiWhy.isShowing()) {
                //背景变暗
                PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 0.6F);
                popupWindowMultiWhy.showAtLocation(layout, Gravity.CENTER, 0, 0);
                multiAdapterWhy.setmFilterList(securityCaseHiddenReason);
                selectDatasWhy.clear();
                if (securityCaseHiddenReason.size()==0){
                    noDataWhy.setVisibility(View.VISIBLE);
                }else {
                    noDataWhy.setVisibility(View.GONE);
                }
            } else {
                popupWindowMultiWhy.dismiss();
            }
        }
    }


    /**
     * 弹出是否保存popupwindow
     */

    public void createSaveWindow() {
        if (poupwindowUp == null) {
            View contentView = getLayoutInflater().inflate(
                    R.layout.popupwindow_user_detail_info_save, null);

            poupwindowUp = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //绑定控件ID
            RadioButton cancelRb = (RadioButton) contentView.findViewById(R.id.cancel_rb);
            RadioButton   saveRb = (RadioButton) contentView.findViewById(R.id.save_rb);
            //设置点击事件
            cancelRb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    poupwindowUp.dismiss();
                }
            });
            saveRb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //安检时间
                    checkTime= Tools.getCurrentTime();
                    tvStateTime.setText(checkTime);
                    ThreadPoolManager.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            updateUserInfo();

                            if (isChecked.equals("未检")) {
                                updateTaskInfo(taskId, sharedPreferences_login.getString("userId", ""));
                            }
                            //保存图片
                            savephoto();
                            //用户表是否安检状态
                            updateUserCheckedState();
                            try {
                                Thread.sleep(250);
                                //0自动上传，100手动上传
                                if (sharedPreferences.getInt("security_up", 100) == 0) {
                                    myHandler.sendEmptyMessage(6);
                                    //上传安检信息
                                    getUserDataAndPost(taskId);
                                } else {
                                    Intent intent3 = new Intent();
                                    intent3.putExtra("checkTime",checkTime);
                                    setResult(Activity.RESULT_OK, intent3);
                                    finish();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
                    });

                    poupwindowUp.dismiss();

                }
            });
            poupwindowUp.setTouchable(true);
            poupwindowUp.setFocusable(true);
            poupwindowUp.setOutsideTouchable(true);
            poupwindowUp.update();
            poupwindowUp.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
            poupwindowUp.setAnimationStyle(R.style.camera);
            poupwindowUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
            PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 0.6F);
            poupwindowUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 1.0F);
                }
            });
        }else {
            if (!poupwindowUp.isShowing()) {
                //背景变暗
                PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 0.6F);
                poupwindowUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
            } else {
                poupwindowUp.dismiss();
            }
        }
    }


    /**
     * 读取安全隐患原因
     *
     * @param itemId
     */
    public void getSecurityHiddenReason(String itemId) {
        //查询并获得游标
        Cursor cursor = db.rawQuery("select * from security_hidden_reason where n_safety_hidden_id=?", new String[]{itemId});
        //如果游标为空，则返回空
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            PopupwindowListItem item = new PopupwindowListItem();
            item.setItemId(cursor.getString(cursor.getColumnIndex("n_safety_hidden_reason_id")));
            item.setItemName(cursor.getString(cursor.getColumnIndex("n_safety_hidden_reason_name")));
            securityCaseHiddenReason.add(item);
        }
        Log.i("getSecurityHiddenReason", " 安全隐患原因个数为：" + securityCaseHiddenReason.size());
        cursor.close();
    }

    /**
     * 将安检的信息保存至本地
     */
    private void updateUserInfo() {
        ContentValues values = new ContentValues();
        if (!"".equals(securityCaseItemId)) {
            //安检情况ID
            values.put("security_case", securityCaseItemName);
            values.put("security_case_id", securityCaseItemId);
        }
        if (!( tvSituation.getText().toString().equals("合格") || tvSituation.getText().toString().equals("复检合格"))){
            if (!(tvSituation.getText().toString().equals("合格"))){
                    values.put("security_state", "0");
            } else {
                values.put("security_state", "1");
                Log.e("===== values.put:","1");
            }
            
            values.put("remarks", etRemark.getText().toString().trim());
            
            if (!"".equals(securityHiddenItemId)) {
                //隐患类型ID
                values.put("security_hidden_id", securityHiddenItemId);
                values.put("security_hidden", securityHiddenItemName);
            }
            if (!"".equals(hiddenReasonItemId)) {
                //隐患原因ID
                values.put("security_hidden_reason_id", hiddenReasonItemId);
                values.put("security_hidden_reason", hiddenReasonItemName);
            }
            values.put("ifPass", "false");
        } else {   // 如果是合格或者复检合格，则插入空的隐患类型和原因
            values.put("remarks", "");
            values.put("security_hidden", "");
            values.put("security_hidden_reason", "");
            values.put("security_hidden_id", "");
            values.put("security_hidden_reason_id", "");
            values.put("ifPass", "true");
            values.put("security_state", "1");
        }
        values.put("currentTime", Tools.getCurrentTime());
        db.update("User", values, "newUserId=? and loginUserId=?", new String[]{userNewId, sharedPreferences_login.getString("userId", "")});

    }


    /**
     * 安检任务
     * @param taskId
     * @param loginUserId
     */
    private void updateTaskInfo(String taskId, String loginUserId) {
        ContentValues values = new ContentValues();
        Cursor cursor = db.rawQuery("select * from Task where taskId=? and loginUserId=?", new String[]{taskId, loginUserId});//查询并获得游标
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            values.put("restCount", String.valueOf(Integer.parseInt(cursor.getString(cursor.getColumnIndex("restCount")))-1));
        }
        db.update("Task", values, "taskId=? and loginUserId=?", new String[]{taskId, loginUserId});

        cursor.close();
    }

    /**
     * 保存图片到数据库 UPDATE sqlite_sequence SET seq =0 WHER Ename ='TableName';//清除数据
     */
    private void savephoto() {
        if (cropPathLists.size() != 0) {
            //删除security_photo表中的当前用户的照片数据
            db.delete("security_photo", "newUserId=? and loginUserId=?", new String[]{userNewId, sharedPreferences_login.getString("userId", "")});
            db.execSQL("update sqlite_sequence set seq=0 where name='security_photo'");
            for (int i = 0; i < filePathLists.size(); i++) {
                FileUtil.deleteDir(filePathLists.get(i));
            }

            for (int i = 0; i < cropPathLists.size(); i++) {
                insertSecurityPhoto(cropPathLists.get(i).getCropPath());
            }
            updateUserPhoto(String.valueOf(cropPathLists.size()));
        }
    }
    /**
     * 将拍的照片路径保存到本地数据库安检图片表
     */
    private void insertSecurityPhoto(String photoPath) {
        ContentValues values = new ContentValues();
        values.put("newUserId", userNewId);
        values.put("photoPath", photoPath);
        values.put("loginUserId", sharedPreferences_login.getString("userId", ""));
        db.insert("security_photo", null, values);
    }

    /**
     * 将拍的照片张数保存到本地数据库安检图片表
     */
    private void updateUserPhoto(String photoNumber) {
        ContentValues values = new ContentValues();
        values.put("photoNumber", photoNumber);
        db.update("User", values, "newUserId=? and loginUserId=?", new String[]{userNewId, sharedPreferences_login.getString("userId", "")});
    }

    /**
     * 更新用户表是否安检状态
     */
    public void updateUserCheckedState() {
        ContentValues values = new ContentValues();
        values.put("ifChecked", "true");
        Log.i("UserList=update", "更新安检状态为true");
        db.update("User", values, "securityNumber=? and loginUserId=?", new String[]{securityNumber, sharedPreferences_login.getString("userId", "")});
    }


    /**
     * 读取本地安检用户数据，并上传服务器
     * @param taskId
     */
    public void getUserDataAndPost(final String taskId) {

        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginUserId=? and meterNumber=?", new String[]{taskId, sharedPreferences_login.getString("userId", ""),meterNumber});//查询并获得游标
        HashMap map1 = new HashMap<String, Object>();
        Log.i("getUserDataAndPost=>", "上传的用户数为：" + cursor.getCount());
        while (cursor.moveToNext()) {
            Log.i("getUserDataAndPost=>", "安检：" + cursor.getString(cursor.getColumnIndex("ifChecked"))+cursor.getString(cursor.getColumnIndex("ifUpload")));
            //判断是否为安检过的，未安检的不上传
            if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("true")) {
                //判断是否为未上传，上传的用户数据不再上传
                if (cursor.getString(cursor.getColumnIndex("ifUpload")).equals("false")) {
                    try {
                        //加载
                        Thread.sleep(250);
                        map1.put("n_safety_inspection_id", cursor.getString(cursor.getColumnIndex("securityNumber")));
                        Log.i("getUserDataAndPost=>", "安检ID为：" + cursor.getString(cursor.getColumnIndex("securityNumber")));

                        map1.put("c_safety_securitycontent", cursor.getString(cursor.getColumnIndex("security_case_id")));
                        Log.i("getUserDataAndPost=>", "安检情况是：" + cursor.getString(cursor.getColumnIndex("security_case_id")));

                        map1.put("c_safety_remark", cursor.getString(cursor.getColumnIndex("remarks")));
                        Log.i("getUserDataAndPost=>", "备注是：" + cursor.getString(cursor.getColumnIndex("remarks")));

                        map1.put("n_safety_hidden_id",0);
                        Log.i("getUserDataAndPost=>", "隐患类型是：" + cursor.getString(cursor.getColumnIndex("security_hidden_id")));

                        map1.put("n_safety_hidden_reason_id", cursor.getString(cursor.getColumnIndex("security_hidden_reason_id")));
                        Log.i("getUserDataAndPost=>", "隐患原因ID是：" + cursor.getString(cursor.getColumnIndex("security_hidden_reason_id")));

                        map1.put("d_safety_inspection_date", cursor.getString(cursor.getColumnIndex("currentTime")));
                        Log.i("getUserDataAndPost=>", "安检的时间是：" + cursor.getString(cursor.getColumnIndex("currentTime")));

                        map1.put("n_safety_state", cursor.getString(cursor.getColumnIndex("security_state")));
                        Log.i("getUserDataAndPost=>", "安检状态是：" + cursor.getString(cursor.getColumnIndex("security_state")));

                        map1.put("c_safety_inspection_member", sharedPreferences_login.getString("user_name", ""));
                        Log.i("getUserDataAndPost=>", "安检人员是：" + sharedPreferences_login.getString("user_name", ""));

                        map1.put("c_user_id", cursor.getString(cursor.getColumnIndex("newUserId")));
                        Log.i("getUserDataAndPost=>", "新用户ID是：" + cursor.getString(cursor.getColumnIndex("newUserId")));
                        String  newUserId = cursor.getString(cursor.getColumnIndex("newUserId"));

                        map1.put("c_meter_number", cursor.getString(cursor.getColumnIndex("meterNumber")));
                        Log.i("getUserDataAndPost=>", "表编号是：" + cursor.getString(cursor.getColumnIndex("meterNumber")));

                        map1.put("c_user_phone", cursor.getString(cursor.getColumnIndex("userPhone")));
                        Log.i("getUserDataAndPost=>", "手机号码是：" + cursor.getString(cursor.getColumnIndex("userPhone")));

                        map1.put("c_user_address", cursor.getString(cursor.getColumnIndex("userAddress")));
                        Log.i("getUserDataAndPost=>", "地址是：" + cursor.getString(cursor.getColumnIndex("userAddress")));

                        Map<String, File> fileMap= getPhotoData(newUserId);
                        Log.i("fileMap=>", "fileMap：" + fileMap.size());
                        if (fileMap.size() ==0){
                            myHandler.sendEmptyMessage(2);
                            break;
                        }
                        //上传路径
                        String httpUrl=new StringBuffer().append(SkUrl.SkHttp(SyEastCheckListDatailActivity.this)).append("updateInspection.do").toString();
                        HttpUtils httpUtils=new HttpUtils();
                        String result=httpUtils.postData(httpUrl, map1, fileMap);
                        Log.i("httpUtils=>", "上传的地址为：" + httpUrl);
                        if ("保存成功".equals(result)) {
                            //如果返回保存成功则将用户表的上传状态改为true
                            updateUserUploadState(newUserId);
                            myHandler.sendEmptyMessage(3);
                        } else if ("保存失败".equals(result)) {

                            strErro=result;
                            myHandler.sendEmptyMessage(4);
                            break;
                        } else if ("".equals(result)) {

                            Log.i("UploadActivity=>", "网络请求错误！");
                            myHandler.sendEmptyMessage(5);
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        cursor.close(); //游标关闭
    }

    private void showLoad(){
        loadingView = new LoadingView(SyEastCheckListDatailActivity.this, R.style.LoadingDialog, "上传中...请稍后");
        loadingView.show();
    }
    private void loadDiss() {
        if (loadingView != null) {
            loadingView.dismiss();
        }

    }

    //读取保存到本地的图片数据，并上传服务器
    public Map<String, File> getPhotoData(String newUserId) {
        //查询并获得游标
        Cursor cursor = db.rawQuery("select * from security_photo where newUserId=? and loginUserId=?", new String[]{newUserId, sharedPreferences_login.getString("userId", "")});
        Map<String, File> fileMap = new HashMap<String, File>();
        File file = null;
        while (cursor.moveToNext()) {
            file = new File(cursor.getString(1));
            Log.i("getUserData=>", "上传的照片：" + file.toString());
            fileMap.put("file" + cursor.getPosition(), file);
        }
        Log.i("getUserData=>", "上传的照片流为：" + fileMap.size());
        cursor.close(); //游标关闭
        return fileMap;
    }
    /**
     * 更新用户表上传状态
     */
    private void updateUserUploadState(String newUserId) {
        ContentValues values = new ContentValues();
        values.put("ifUpload", "true");
        db.update("User", values, "newUserId=? and loginUserId=?", new String[]{newUserId, sharedPreferences_login.getString("userId", "")});
    }

    /**
     * 动态申请权限，如果6.0以上则弹出需要的权限选择框，以下则直接运行
     */
    private void requestPermissions() {
        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SyEastCheckListDatailActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[0]);
            }
            if (ContextCompat.checkSelfPermission(SyEastCheckListDatailActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[1]);
            }
            if (ContextCompat.checkSelfPermission(SyEastCheckListDatailActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[2]);
            }

            Log.i("requestPermissions", "权限集合的长度为：" + permissionList.size());
            //判断权限集合是否为空
            if (!permissionList.isEmpty()) {
                String[] permissionArray = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(SyEastCheckListDatailActivity.this, permissionArray, PERMISSION_REQUEST_CODE);
            } else {
                createPhotoPopupwindow();
            }
        } else {
            createPhotoPopupwindow();
        }
    }

    /**
     * 弹出拍照popupwindow
     */
    public void createPhotoPopupwindow() {
        if (popupwindowView == null) {
            View contentView = getLayoutInflater().inflate(R.layout.popupwindow_security_userinfo_take_photo, null);
            popupwindowView = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //绑定控件ID
            final Button takePhoto = (Button) contentView.findViewById(R.id.take_photo);
            Button cancel = (Button) contentView.findViewById(R.id.cancel);
            //设置点击事件
            takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto.setClickable(false);
                    popupwindowView.dismiss();
                    gridView.setClickable(true);
                    if (Tools.hasSdcard()) {
                        openCamera();//拍照
                    } else {
                        Toast.makeText(SyEastCheckListDatailActivity.this, "没有SD卡哦，不能拍照！", Toast.LENGTH_SHORT).show();
                    }
                    takePhoto.setClickable(true);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupwindowView.dismiss();
                    gridView.setClickable(true);
                }
            });
            popupwindowView.setFocusable(true);
            popupwindowView.setOutsideTouchable(true);
            popupwindowView.update();
            popupwindowView.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
            popupwindowView.setAnimationStyle(R.style.camera);
            popupwindowView.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
            //背景变暗
            PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 0.6F);
            popupwindowView.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //背景变暗
                    PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 1.0F);
                    gridView.setClickable(true);
                }
            });
        } else {
            if (!popupwindowView.isShowing()) {
                //背景变暗
                PopWindowUtil.backgroundAlpha(SyEastCheckListDatailActivity.this, 0.6F);
                popupwindowView.showAtLocation(layout, Gravity.BOTTOM, 0, 0);

            } else {
                popupwindowView.dismiss();
            }
        }
    }

    //调用相机
    public void openCamera() {

        photoUtils = new MyPhotoUtils(SyEastCheckListDatailActivity.this, userNewId);
        cropPhotoPath = photoUtils.generateImgePath();
        File imgFile = new File(cropPhotoPath);
        Uri tempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            tempUri = FileProvider.getUriForFile(SyEastCheckListDatailActivity.this, "com.example.administrator.thinker_soft.fileprovider", imgFile);
        } else {
            // 指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
            tempUri = Uri.fromFile(imgFile);
        }
        Intent openCameraIntent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        //设置Action为拍照
        openCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        // 自动对焦
        openCameraIntent.putExtra("autofocus", true);
        // 全屏
        openCameraIntent.putExtra("fullScreen", false);
        openCameraIntent.putExtra("showActionIcons", false);
        openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        Log.i("openCamera===>", "临时保存的地址为" + tempUri.getPath());

        startActivityForResult(openCameraIntent, TAKE_PHOTO);

    }

    /**
     * 页面回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果返回码是可用的
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
//                    File temFile = photoUtils.outputIamge(PictureUtil.compressSizeImage(cropPhotoPath));
                    File file = new File(cropPhotoPath);
//                    file.delete();
                    Uri outputUri = Uri.fromFile(file);
                    // 下面就是照片上加时间
                    WaterMask.WaterMaskParam param = new WaterMask.WaterMaskParam();
                    Date d = new Date();
                    System.out.println(d);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //2018-08-11 12:17:57
                    String dateNowStr = sdf.format(d);
                    param.txt.add(dateNowStr);
                    param.itemCount = 30;
                    Bitmap bitmap = ImageUtil.getBitmap(cropPhotoPath);
                    WaterMask.draw(SyEastCheckListDatailActivity.this, bitmap, cropPhotoPath, param);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outputUri));

                    cropPathLists.add(new PhotoPathBean(cropPhotoPath, "选择类型", ""));
                    //   Log.i("startCropPhoto===>", "图片集合长度为：" + cropPathLists.size() + "路径为" + cropPhotoPath + "大小=" + FileSizeUtil.getFileOrFilesSize(cropPhotoPath));
                    iamgeAdapter.setGridviewImageList(cropPathLists);
                    break;
                case REQUEST_PHOTO:
                    if (data != null) {
                        cropPathLists.clear();
                        cropPathLists = (ArrayList<PhotoPathBean>) data.getSerializableExtra("cropPathLists_back");
                        iamgeAdapter.setGridviewImageList(cropPathLists);
                    }
                    break;

                default:
                    break;
            }
        }
    }



    /**
     * Handler
     */
    private static class UIMyHandler extends UIHandler<SyEastCheckListDatailActivity> {

        public UIMyHandler(SyEastCheckListDatailActivity cls) {
            super(cls);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SyEastCheckListDatailActivity activity = ref.get();
            if (activity != null) {
                if (activity.isFinishing()) {
                    return;
                }
                switch (msg.what) {
                    case 0:
                        activity.tvUserName.setText(activity.userName);

                        activity.tvUserid.setText( activity.userNewId);
                        activity.tvSecurityType.setText( activity.securityType);
                        activity.tvMeterNumber.setText( activity.meterNumber);
                        activity. userPhoneNumber.setText( activity.phoneNumber);
                        activity.userAddress.setText( activity.address);
                        activity.tvStateTime.setText(activity.ajTime);
                        activity.tvEndTime.setText(activity.endTime);
                        activity.etRemark.setText(activity.remarks);
                        //名字
                        activity.userNameEt=activity.userName;
                        //电话
                        activity. userPhone=activity.phoneNumber;
                        if("".equals(activity.securityCase)){
                            activity.tvSituation.setText("点击选择");
                        }else {
                            activity.tvSituation.setText(activity.securityCase);
                        }
                        if (!(  activity.tvSituation.getText().equals("合格") ||   activity.tvSituation.getText().equals("复检合格")||   activity.tvSituation.getText().equals("第一次到访不遇")||   activity.tvSituation.getText().equals("第二次到访不遇"))) {
                            activity.showHiddenTypeAndReason();
                        } else {
                            activity.noShowHiddenTypeAndReason();
                        }
                        if("".equals(activity.hiddenType)){
                            activity.tvType.setText("点击选择");
                        }else {
                            activity.tvType.setText(activity.hiddenType);
                        }
                        if("".equals(activity.hiddenReason)){
                            activity.tvReason.setText("点击选择");
                        }else {
                            activity.tvReason.setText(activity.hiddenReason);
                        }
                        if ("需要复检".equals(activity.remarks)) {
                        //    activity. remarksRb1.setChecked(true);
                        } else if ("现场已整改".equals(activity.remarks)) {
                           // activity.remarksRb2.setChecked(true);
                        } else {
                          //  activity.remarksRb1.setChecked(false);
                          //  activity.remarksRb2.setChecked(false);
                        }
                        Log.e("类型",activity.securityType+activity.endTime);
                        //安检类型
                        if (activity.securityType.equals("常规安检")||activity.securityType.equals("年度安检")){
                            for (int i=0;i<activity.securityCaseItemList.size();i++){
                                Log.e("类型",activity.securityCaseItemList.get(i).getItemName());
                                if ( activity.securityCaseItemList.get(i).getItemName().equals("复检合格")){
                                    activity.securityCaseItemList.remove(i);
                                    Log.e("类型","---");
                                }
                            }
                        }

                        break;
                    case 1:
                        activity.iamgeAdapter.setGridviewImageList(activity.cropPathLists);
                        break;
                    case 2:
                        //是否添加图片
                        activity.loadDiss();
                        Toast.makeText(activity, "亲,请添加图片哦", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        //上传成功
                        activity.loadDiss();
                        Toast.makeText(activity,"上传成功",Toast.LENGTH_SHORT).show();
                        //上传
                        Intent intent = new Intent();
                        intent.putExtra("isUpload","成功");
                        intent.putExtra("checkTime",activity.checkTime);
                        activity.setResult(Activity.RESULT_OK, intent);
                        activity.finish();
                        break;
                    case 4:
                        //上传失败
                        activity.loadDiss();
                        Toast.makeText(activity,"上传失败"+activity.strErro,Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent();
                        intent2.putExtra("checkTime",activity.checkTime);
                        activity.setResult(Activity.RESULT_OK, intent2);
                        activity.finish();
                        break;
                    case 5:
                        //网络异常
                        activity.loadDiss();
                        Toast.makeText(activity,"网络异常！",Toast.LENGTH_SHORT).show();
                        Intent intent3 = new Intent();
                        intent3.putExtra("checkTime",activity.checkTime);
                        activity.setResult(Activity.RESULT_OK, intent3);
                        activity.finish();
                        break;
                    case 6:
                        //进度
                        activity.showLoad();
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * 当不是安检合格的时候，显示安全隐患和安全隐患原因
     */

    public void showHiddenTypeAndReason() {
        layoutHidden.setVisibility(View.VISIBLE);
        layoutRype.setVisibility(View.VISIBLE);

    }
    public void noShowHiddenTypeAndReason() {
        layoutHidden.setVisibility(View.GONE);
        layoutRype.setVisibility(View.GONE);

    }



    /**
     * 修改信息
     */
    private void ReviseRequest(final NewsReviseParams param) {
        //加载
        loadingView = new LoadingView(SyEastCheckListDatailActivity.this, R.style.LoadingDialog, "修改中...请稍后");
        loadingView.show();
        String httpUrl=new StringBuffer().append(SkUrl.SkHttp(SyEastCheckListDatailActivity.this)).append("updateUserMsg.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.POST)
                .encode("UTF-8")
                .tag("add")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("c_user_name",param.getC_user_name())
                .params("c_user_id",param.getC_user_id())
                .params("n_log_operator_id",param.getN_log_operator_id())
                .params("c_log",param.getC_log())
                .params("c_meter_number",param.getC_meter_number())
                .params("n_meter_direction",param.getN_meter_direction())
                .params("c_user_phone",param.getC_user_phone())
                .string("");
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response)  {
                Log.e("MeterRemarkActivity", "onComplete/response:" + response.getBody());
                Log.e("MeterRemarkActivity", "onComplete/response: content type=" + response.getContentType());
                loadDiss();
                try {
                    JSONObject jsonObject=new JSONObject(response.getBody());
                    String success= jsonObject.optString("status","");
                    if (success.equals("success")){
                        NewsReviseParams params=new NewsReviseParams();
                        params.setC_user_name(param.getC_user_name());
                        params.setC_user_phone(param.getC_user_phone());
                        ObserverManager.getInstance().notifyObserver(new Gson().toJson(params));
                        //更新
                        updateUserData(param.getC_user_name(),param.getC_user_phone());
                        ToastUtil.showShort(SyEastCheckListDatailActivity.this,"修改成功");
                    }else {
                        ToastUtil.showShort(SyEastCheckListDatailActivity.this,"修改失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(SyEastCheckListDatailActivity.this,"修改失败");
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
                loadDiss();
                ToastUtil.showShort(SyEastCheckListDatailActivity.this,"修改失败");

            }
        }).executeAsync();
    }


    /**
     * 更新用户表用户信息
     */
    private void updateUserData(String name,String phone) {
        ContentValues values = new ContentValues();
        values.put("userName", name);
        values.put("userPhone",phone);
        db.update("User", values, "newUserId=? and loginUserId=?", new String[]{newUserId, sharedPreferences_login.getString("userId", "")});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (db != null) {
            db.close();
        }
    }

}
