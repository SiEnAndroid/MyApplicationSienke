package com.example.administrator.thinker_soft.meter_code.sk.ui.security;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
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
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.adapter.GridviewImageAdapter;
import com.example.administrator.thinker_soft.Security_check.adapter.PopupwindowListAdapter;
import com.example.administrator.thinker_soft.Security_check.model.NormalViewHolder;
import com.example.administrator.thinker_soft.Security_check.model.PopupwindowListItem;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.GridviewImageAdapters;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.SecurityGridviewImageAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.PhotoPathBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityChecksBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityImageBean;
import com.example.administrator.thinker_soft.meter_code.sk.db.DBManage;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.PhotoGalleryActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.SecurityAbnormalPhotoGalleryActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.CommonUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.FileSizeUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PictureUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.meter_code.sk.widget.MyPopupWind;
import com.example.administrator.thinker_soft.mode.HttpUtils;
import com.example.administrator.thinker_soft.mode.MyGridview;
import com.example.administrator.thinker_soft.mode.MyPhotoUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.mode.Tools;
import com.example.administrator.thinker_soft.mode.photo.ImageUtil;
import com.example.administrator.thinker_soft.mode.photo.WaterMask;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 111 on 2018/8/3.
 */

public class SecurityAbnormalInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ACTION_TAKE_PHOTO = 101;
    private ImageView back;
    private TextView userName;
    private TextView name;   //  安检员
    private TextView userControlNo; // 用户编号
    private TextView securityType; //安检类型
    private TextView meterNumber; //抄表编号
    private TextView userPhoneNumber; //用户电话
    private TextView userAddress; //用户地址
    private TextView dlsrSecurityCheckCase; // 上次 安检情况
    private TextView dlsrSecurityHiddenType; // 上次 隐患类型
    private TextView dlsrSecurityHiddenReason; // 上次 隐患原因
    private TextView dlsrRemarks;  //上次  备注信息
    private CardView currentCaseCardview;  // 当前 安检情况
    private TextView currentSecurityCheckCase;  // 当前 安检情况
    private EditText currentRemarks;   //当前  备注信息
    private CardView saveCardview;  // 保存
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}; //权限数组
    private List<String> permissionList = new ArrayList<>();  //权限集合
    protected static final int PERMISSION_REQUEST_CODE = 1;  //6.0之后需要动态申请权限，   请求码
    private List<PopupwindowListItem> securityCaseItemList = new ArrayList<>();
    private SQLiteDatabase db;
    private SecurityChecksBean.ListBean listbean;
    private MyGridview dlsrGridview;
    private MyGridview currentGridview;
    private GridviewImageAdapter ImageAdapter;
    private int currentPosition;
    private LayoutInflater inflater;
    private View popupwindowView ,securityHiddenTypeView ,securityHiddenreasonView;
    private PopupWindow popupWindow;
    private Button takePhoto, cancel, saveBtn;
    protected static final int TAKE_PHOTO = 100;//拍照
    private LinearLayout rootLinearlayout;
    /**
     * 加载进度
     */
    private LoadingView loadingView;
    private MyPopupWind myPopupWind;
    private PopupwindowListAdapter popupwindowListAdapter;
    //对象中拿到集合
    private  ArrayList<SecurityImageBean> List = new ArrayList<>();
    private ArrayList<String> cropIdLists;
    private GridviewImageAdapters currentGridviewImageAdapter;
    private ArrayList<PhotoPathBean>  cropPathLists = new ArrayList<>(); //裁剪的图片路径集合
    private MyPhotoUtils photoUtils;
    private String cropPhotoPath;
    private LinearLayout llDlsrGridview;
    private TextView tvDlsr;
    private SecurityGridviewImageAdapter iamgeAdapter;
    private SharedPreferences sharedPreferences_login;
    private SharedPreferences sharedPreferences;
    private String strErro ="";
    private View saveView;
    private RadioButton cancelRb;
    private RadioButton saveRb;
    private LinearLayout hiddenTypeRoot;
    private LinearLayout hiddenReasonRoot;
    private LinearLayout remarksRoot;
    private CardView typeCardview;
    private TextView tips;
    private ListView listView;
    private TextView confirm;
    private LinearLayout noData;
    private TextView noDataTip;
    private LinearLayout confirmLayout;
    private TextView selectCounts;
    private List<PopupwindowListItem> securityHidenItemList = new ArrayList<>();  // 存隐患类型
    private HashMap<String, String> hiddenTypeInfoMap = new HashMap<>();  //用于保存选中的隐患类型信息
    private HashMap<String, String> hiddenReeasonInfoMap = new HashMap<>();  //用于保存选中的隐患原因信息
    private List<PopupwindowListItem> securityReasonItemList = new ArrayList<>();
    private List<String> hiddenTypeIDList = new ArrayList<>();     //用于保存隐患类型ID
    private List<String> hiddenReasonIDList = new ArrayList<>();   //用于保存隐患原因ID
    private String securityCaseItemId = "", securityHiddenItemId = "", hiddenReasonItemId = "";//安检情况类型id,安检隐患类型id,安检隐患原因id
    private String securityHiddenItemName = "", hiddenReasonItemName = "";
    private TextView  securityHiddenType, securityHiddenReason;  //安全情况,安全隐患类型，安全隐患原因
    private PopupwindowListAdapter adapter;
    private int hiddenTypeCount = 0;
    private int hiddenReasonCount = 0;
    private LinearLayout dlsrHiddenTypeRoot;
    private LinearLayout dlsrHiddenReasonRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_abnormal_info);

        listbean =(SecurityChecksBean.ListBean ) getIntent().getSerializableExtra("listBean");
//        position = Integer.parseInt(getIntent().getStringExtra("position"));

        bindView();//绑定控件
//        ImageAdapter = new GridviewImageAdapter(SecurityAbnormalInfoActivity.this, cropPathLists);
        defaultSetting();//初始化设置
        setViewClickListener();//点击事件
    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    private void defaultSetting() {
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = SecurityAbnormalInfoActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        // meterReaderName.setText(sharedPreferences_login.getString("user_name",""));
        // company_id=sharedPreferences_login.getInt("company_id",0)+"";
        name.setText( sharedPreferences_login.getString("user_name",""));
        userName.setText(listbean.getYhmc()+"");
        userControlNo.setText(listbean.getYhbh()+"");
        securityType.setText(listbean.getAjlx()+"");
        meterNumber.setText(listbean.getBbh()+"");
        userPhoneNumber.setText(listbean.getLxdh()+"");
        userAddress.setText(listbean.getYhdz()+"");
        dlsrRemarks.setText(listbean.getAjbz() ==null?"":listbean.getAjbz()+"");
        dlsrSecurityCheckCase.setText(listbean.getAjqk()+"");

        currentCaseCardview.setOnClickListener(this);
        if (!"安全隐患".equals( listbean.getAjqk())){
            noShowdlsrHiddenTypeAndReason();
        }else {
            dlsrSecurityHiddenType.setText(listbean.getAqyhlx()+"");
            dlsrSecurityHiddenReason.setText(listbean.getAqyhyy()+"");
        }

        MySqliteHelper helper = new MySqliteHelper(SecurityAbnormalInfoActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();


         //  网络请求图片
        SecurityAbnormalInfoRequest();

        noShowHiddenTypeAndReason();


        //  当前照片适配
        currentGridviewImageAdapter = new GridviewImageAdapters(SecurityAbnormalInfoActivity.this, cropPathLists);
        currentGridview.setAdapter(currentGridviewImageAdapter);
        currentGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentGridview.setClickable(false);
                currentPosition = position;
                if (!currentGridviewImageAdapter.getDeleteShow() && (currentGridviewImageAdapter.getCount() - 1 != position)) {
                    Intent intent = new Intent(SecurityAbnormalInfoActivity.this, PhotoGalleryActivity.class);
                    intent.putExtra("currentPosition", currentPosition);
                    intent.putExtra("newUserId", listbean.getYhbh());
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("cropPathLists",cropPathLists);
                    intent.putExtras(bundle);
//             intent.putStringArrayListExtra("cropPathLists", cropPathLists);
                    Log.i("UserDetailInfoActivity", "点击图片跳转进来到预览详情页面的图片数量为：" + cropPathLists.size());
                    startActivityForResult(intent, 500);
                    currentGridview.setClickable(true);
                }
                // 如果单击时删除按钮处在显示状态，则隐藏它
                if (currentGridviewImageAdapter.getDeleteShow()) {
                    currentGridviewImageAdapter.setDeleteShow(false);
                    currentGridviewImageAdapter.notifyDataSetChanged();
                } else {
                    if (currentGridviewImageAdapter.getCount() - 1 == position) {
                        // 判断是否达到了可添加图片最大数
                        if (cropPathLists.size() != 9) {
                            requestPermissions();      //检测权限
                        }
                    }
                }
            }
        });
        securityCaseItemList = DBManage.getInstance(SecurityAbnormalInfoActivity.this).getSecurityCheckCase();
          //  安全隐患 0  ; 合格 1  ; 复检合格 2  ;  拒绝安检 3 ; 第一次到访不遇 4  ; 第二次到访不遇 5 ; 第三次到访不遇 6;
        //状态(0未安检，1安检合格，2安检不合格，3 超过安检时间,4，到访不遇，5拒绝安检)     是id
          /*
            1、安全隐患，第二次到访不遇
            显示：复检合格
            2、第一次到访不遇
            显示：第二次到访不遇，安全隐患、复检合格
            3、拒绝安检
            显示：第一次到访不遇，第二次到访不遇，安全隐患、复检合格
            4、超过安检时间
            第一次到访不遇，第二次到访不遇，安全隐患、复检合格、拒绝安检
            */
        Log.e("pgl","=== securityCaseItemList"+"执行了:"+listbean.getAjqk());
        if ("安全隐患".equals(listbean.getAjqk())){
            securityCaseItemList.remove(0);
            securityCaseItemList.remove(0);
            securityCaseItemList.remove(1);
            securityCaseItemList.remove(1);
            securityCaseItemList.remove(1);
            securityCaseItemList.remove(1);
        }else if ("第二次到访不遇".equals(listbean.getAjqk())){
            securityCaseItemList.remove(0);
            securityCaseItemList.remove(0);
            securityCaseItemList.remove(1);
            securityCaseItemList.remove(1);
            securityCaseItemList.remove(1);
            securityCaseItemList.remove(1);
        }else if ("第一次到访不遇".equals(listbean.getAjqk())){  // 502
            securityCaseItemList.remove(1);
            securityCaseItemList.remove(2);
            securityCaseItemList.remove(2);
            securityCaseItemList.remove(3);
        }
        if ("拒绝安检".equals(listbean.getAjqk())){ // 4502
            securityCaseItemList.remove(1);
            securityCaseItemList.remove(2);
            securityCaseItemList.remove(4);
        }
        if ("超过安检时间".equals(listbean.getAjqk())){ // 45023
            securityCaseItemList.remove(1);
            securityCaseItemList.remove(5);
        }
    }

    private void noShowdlsrHiddenTypeAndReason() {
        dlsrHiddenReasonRoot.setVisibility(View.GONE);
        dlsrHiddenTypeRoot.setVisibility(View.GONE);
    }

    private void bindView() {
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
        back = (ImageView) findViewById(R.id.back);
        userName = (TextView) findViewById(R.id.user_name);
        name = (TextView) findViewById(R.id.name);
        userControlNo = (TextView) findViewById(R.id.user_control_no);
        securityType = (TextView) findViewById(R.id.security_type);
        meterNumber = (TextView) findViewById(R.id.meter_number);
        userPhoneNumber = (TextView) findViewById(R.id.user_phone_number);
        userAddress = (TextView) findViewById(R.id.user_address);
        dlsrSecurityCheckCase = (TextView) findViewById(R.id.dlsr_security_check_case);
        dlsrSecurityHiddenType = (TextView) findViewById(R.id.dlsr_security_hidden_type);
        dlsrSecurityHiddenReason = (TextView) findViewById(R.id.dlsr_security_hidden_reason);
        dlsrRemarks = (TextView) findViewById(R.id.dlsr_remarks);

        currentCaseCardview = (CardView) findViewById(R.id.current_case_cardview);
        currentSecurityCheckCase = (TextView) findViewById(R.id.current_security_check_case);

        hiddenTypeRoot = (LinearLayout) findViewById(R.id.hidden_type_root);
        hiddenReasonRoot = (LinearLayout) findViewById(R.id.hidden_reason_root);
        remarksRoot = (LinearLayout) findViewById(R.id.remarks_root);

        saveCardview = (CardView) findViewById(R.id.save_cardview);
        currentRemarks = (EditText) findViewById(R.id.current_remarks);

        dlsrGridview = (MyGridview) findViewById(R.id.dlsr_gridview);
        currentGridview = (MyGridview) findViewById(R.id.current_gridview);
        llDlsrGridview = (LinearLayout) findViewById(R.id.ll_dlsr_gridview);
        tvDlsr = (TextView) findViewById(R.id.tv_dlsr);
        securityHiddenType = (TextView) findViewById(R.id.security_hidden_type);
        securityHiddenReason = (TextView) findViewById(R.id.security_hidden_reason);

        typeCardview = (CardView) findViewById(R.id.type_cardview);
        dlsrHiddenReasonRoot = (LinearLayout) findViewById(R.id.dlsr_hidden_reason_root);
        dlsrHiddenTypeRoot = (LinearLayout) findViewById(R.id.dlsr_hidden_type_root);


    }
    private void setViewClickListener() {
        back.setOnClickListener(this);
        saveCardview.setOnClickListener(this);
        typeCardview.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.current_case_cardview:
                //安检情况
                showAll();
                break;
            case R.id.dlsr_gridview:

//                SecurityAbnormalInfoRequest();
                break;
            case R.id.save_cardview:
                //  上传当前安检信息
                createSaveWindow();

                break;
            case R.id.type_cardview:
                if(!"点击选择".equals(currentSecurityCheckCase.getText())){
                    securityHiddenTypeWindow();
                    //获取安全隐患列表信息
                    new Thread() {
                        @Override
                        public void run() {
                            getSecurityHiddenType();
                        }
                    }.start();
                }else {
                    Toast.makeText(SecurityAbnormalInfoActivity.this, "请您先选择安检情况！", Toast.LENGTH_LONG).show();
                }

                break;

            default:
                break;
        }
    }
    /**
     * 读取安全隐患类型列表
     */
    public void getSecurityHiddenType() {
        securityHidenItemList.clear();
        Cursor cursor = db.query("security_hidden", null, null, null, null, null, null);//查询并获得游标
        //如果游标为空，则返回空
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(3);
            return;
        }
        while (cursor.moveToNext()) {
            PopupwindowListItem item = new PopupwindowListItem();
            item.setItemId(cursor.getString(cursor.getColumnIndex("n_safety_hidden_id")));
            item.setItemName(cursor.getString(cursor.getColumnIndex("n_safety_hidden_name")));
            securityHidenItemList.add(item);
        }
        handler.sendEmptyMessage(4);
        Log.i("getSecurityHiddenType", " 安全隐患个数为：" + securityHidenItemList.size());
        cursor.close();
    }


    /**
     * 弹出安全隐患类型窗口
     */
    public void securityHiddenTypeWindow() {
        inflater = LayoutInflater.from(SecurityAbnormalInfoActivity.this);
        securityHiddenTypeView = inflater.inflate(R.layout.popupwindow_security_hidden_type_or_reason, null);
        popupWindow = new PopupWindow(securityHiddenTypeView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        tips = (TextView) securityHiddenTypeView.findViewById(R.id.tips);
        listView = (ListView) securityHiddenTypeView.findViewById(R.id.list_view);
        confirm = (TextView) securityHiddenTypeView.findViewById(R.id.confirm);
        noData = (LinearLayout) securityHiddenTypeView.findViewById(R.id.no_data);
        noDataTip = (TextView) securityHiddenTypeView.findViewById(R.id.no_data_tip);
        confirmLayout = (LinearLayout) securityHiddenTypeView.findViewById(R.id.confirm_layout);
        selectCounts = (TextView) securityHiddenTypeView.findViewById(R.id.select_counts);
        tips.setText("请选择安全隐患类型（可多选）");
        noDataTip.setText("暂无数据");
        hiddenTypeCount = 0;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NormalViewHolder viewHolder = (NormalViewHolder) view.getTag();
                viewHolder.checkedState.toggle();
                PopupwindowListAdapter.getIsCheck().put(position, viewHolder.checkedState.isChecked());
                if (viewHolder.checkedState.isChecked()) {
                    hiddenTypeCount++;
                } else {
                    hiddenTypeCount--;
                }
                //显示类型选中个数
                selectCounts.setText("(" + hiddenTypeCount + ")");
            }
        });
        confirmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHiddenTypeInfo();
                if(securityHidenItemList.size() != 0){
                    if (hiddenTypeInfoMap.size() != 0) {
                        popupWindow.dismiss();
                        String typeName = "";
                        String typeId = "";
                        /*//通过Map.values()遍历所有的value，但不能遍历key
                        for (String value : hiddenTypeInfoMap.values()) {
                            typeName += value + ",";
                        }*/
                        hiddenTypeIDList.clear();
                        for (String key : hiddenTypeInfoMap.keySet()) {
                            typeName += hiddenTypeInfoMap.get(key) + ",";
                            typeId += key + ",";
                            hiddenTypeIDList.add(key);
                        }
                        securityHiddenItemId = typeId.substring(0, typeId.length()-1);
                        securityHiddenItemName = typeName.substring(0, typeName.length()-1);
                        securityHiddenType.setText(securityHiddenItemName);
                        securityReasonItemList.clear();
                        for (int i = 0; i < hiddenTypeIDList.size(); i++) {
                            getSecurityHiddenReason(hiddenTypeIDList.get(i));
                        }
                        if (securityReasonItemList.size() != 0) {
                            handler.sendEmptyMessage(5);
                        } else {
                            handler.sendEmptyMessage(6);
                        }
                    } else {
                        Toast.makeText(SecurityAbnormalInfoActivity.this, "请您选择隐患类型！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }


    //读取安全隐患原因
    public void getSecurityHiddenReason(String itemId) {
        Cursor cursor = db.rawQuery("select * from security_hidden_reason where n_safety_hidden_id=?", new String[]{itemId});//查询并获得游标
        //如果游标为空，则返回空
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            PopupwindowListItem item = new PopupwindowListItem();
            item.setItemId(cursor.getString(cursor.getColumnIndex("n_safety_hidden_reason_id")));
            item.setItemName(cursor.getString(cursor.getColumnIndex("n_safety_hidden_reason_name")));
            securityReasonItemList.add(item);
        }
        Log.i("getSecurityHiddenReason", " 安全隐患原因个数为：" + securityReasonItemList.size());
        cursor.close();
    }

    //弹出是否保存popupwindow
    public void createSaveWindow() {
        inflater = LayoutInflater.from(SecurityAbnormalInfoActivity.this);
        saveView = inflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        popupWindow = new PopupWindow(saveView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        cancelRb = (RadioButton) saveView.findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) saveView.findViewById(R.id.save_rb);
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
                loadingView = new LoadingView(SecurityAbnormalInfoActivity.this, R.style.LoadingDialog, "上传中...请稍后");
                loadingView.show();
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
//                        updateUserInfo();
//                        if (isChecked.equals("未检")){
//                            updateTaskInfo(taskId,sharedPreferences_login.getString("userId", ""));
//                        }
                        if (cropPathLists.size() != 0) {
                            db.delete("security_photo", "newUserId=? and loginUserId=? ", new String[]{listbean.getYhbh() , sharedPreferences_login.getString("userId", "")});  //删除security_photo表中的当前用户的照片数据
                            db.execSQL("update sqlite_sequence set seq=0 where name='security_photo'");
                            for (int i = 0; i < cropPathLists.size(); i++) {
                                insertSecurityPhoto(cropPathLists.get(i).getCropPath());
                            }
                            updateUserPhoto(String.valueOf(cropPathLists.size()));
                        }
                        updateUserCheckedState();
                        try {
                            Thread.sleep(250);
                                //上传安检信息
                            uploadCurrentSecurityInformation();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                });


                popupWindow.dismiss();

            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }
    //更新用户表是否安检状态
    public void updateUserCheckedState() {
        ContentValues values = new ContentValues();
        values.put("ifChecked", "true");
        Log.i("UserList=update", "更新安检状态为true");
        db.update("User", values, "securityNumber=? and loginUserId=?", new String[]{String.valueOf(listbean.getAjbh()), sharedPreferences_login.getString("userId", "")});
    }
    /**
     * 将拍的照片路径保存到本地数据库安检图片表
     */
    private void insertSecurityPhoto(String photoPath) {
        ContentValues values = new ContentValues();
        values.put("newUserId", listbean.getYhbh());
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
        db.update("User", values, "newUserId=? and loginUserId=?", new String[]{listbean.getYhbh(), sharedPreferences_login.getString("userId", "")});
    }


    private void uploadCurrentSecurityInformation() {
        if ("点击选择".equals(currentSecurityCheckCase.getText())){
            handler.sendEmptyMessage(6);
        }else {
            Map<String, File> photoData = getPhotoData(listbean.getYhbh());
            Log.i("fileMap=>", "fileMap：" + photoData.size());
            Log.i("fileMap=>", "getYhbh：" + listbean.getYhbh());
            if (photoData.size() ==0){
                handler.sendEmptyMessage(7);
            }else {
                HashMap   map1 = new HashMap<String, Object>();// 装 信息
                Map<String, File>   fileMap= getPhotoData(listbean.getYhbh());  // 装 用户id
     /*
    private Integer n_safety_inspection_id;//安检信息ID
    private String d_safety_inspection_review;//录入时间
    private Integer n_safety_date_type;//安检数据的数据类型(0,第一安检,1,到访不遇复检,2安全隐患安检,3拒绝安检,4超过安检时间)
    private Integer n_safety_plan;//安检计划ID
	private String c_user_id;//用户编号
	private String c_safety_inspection_member;//安检员
	private String d_safety_inspection_date;//安检时间
	private String c_safety_type;//安检类型(2年度计划 3 复检 4通气安检 1 常规安检)
	private String c_safety_securitycontent;//安检情况(yx_security)   是String
	private Integer n_safety_state;//状态(0未安检，1安检合格，2安检不合格，3 超过安检时间,4，到访不遇，5拒绝安检)     是id
	private String c_safety_remark;//安检备注
	private Integer n_data_state;//数据状态(1有效,0无效)
	private Integer n_safety_hidden_id;//安全隐患类型(YX_SAFETY_HIDDEN)
	private String n_safety_hidden_reason_id;//安全隐患原因(YX_SAFETY_HIDDEN_REASON)
                */
                Date d = new Date();
                System.out.println(d);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateNowStr = sdf.format(d);  //2018-08-11 12:17:57
                // 当前安检时间 :
                map1.put("n_safety_inspection_id",listbean.getN_safety_inspection_id());  //安检信息ID= 安检编号
                Log.i("getUserDataAndPost=>", "安检信息ID= 安检编号：" +listbean.getAjbh());
                map1.put("c_user_id",listbean.getYhbh());  //用户编号
                Log.i("getUserDataAndPost=>", "c_user_id 用户编号：" +listbean.getYhbh());
                map1.put("c_safety_inspection_member",listbean.getAjy());  //安检员
                Log.i("getUserDataAndPost=>", "c_safety_inspection_member 安检员：" +listbean.getAjy());
                map1.put("d_safety_inspection_date",dateNowStr);  ////安检时间
                Log.i("getUserDataAndPost=>", "d_safety_inspection_date 安检时间：" +dateNowStr);
                map1.put("c_safety_type",listbean.getAjlx());  ////安检类型
                Log.i("getUserDataAndPost=>", "c_safety_type 安检类型：" +listbean.getAjlx());
//                map1.put("n_safety_hidden_id",securityHiddenItemId);  ////安全隐患类型
//                map1.put("n_safety_hidden_ids",0);  ////安全隐患类型
                Log.i("getUserDataAndPost=>", "n_safety_hidden_id 安全隐患类型id：" +securityHiddenItemId);
                map1.put("n_safety_hidden_reason_id",hiddenReasonItemId);  ////安全隐患原因
                Log.i("getUserDataAndPost=>", "n_safety_hidden_reason_id 安全隐患原因id：" +hiddenReasonItemId);
                map1.put("c_user_address",listbean.getYhdz());  //
                Log.i("getUserDataAndPost=>", "c_user_address 用户地址：" +listbean.getYhdz());
//                map1.put("c_user_phone",listbean.getLxdh());    电话 为空会报错
                Log.i("getUserDataAndPost=>", "c_user_phone 电话：" +listbean.getLxdh());
                map1.put("c_meter_number",listbean.getBbh());
                Log.i("getUserDataAndPost=>", "c_meter_number 表编号：" +listbean.getBbh());
                map1.put("c_safety_type",listbean.getYhbh());
                Log.i("getUserDataAndPost=>", "c_safety_type 用户编号：" +listbean.getYhbh());

                map1.put("c_safety_securitycontent", securityCaseItemId );
//                map1.put("n_safety_state", securityCaseItemId);
                Log.i("getUserDataAndPost=>", "c_safety_securitycontent 安检情况是：" +securityCaseItemId);
//                Log.i("getUserDataAndPost=>", "n_safety_state 安检情况id是：" +securityCaseItemId);
                map1.put("c_safety_remark",currentRemarks.getText());
                Log.i("getUserDataAndPost=>", "备注是：" +currentRemarks.getText());
                String httpUrl=new StringBuffer().append(SkUrl.SkHttp(SecurityAbnormalInfoActivity.this)).append("updateInspection.do").toString();
                HttpUtils httpUtils=new HttpUtils();
                String result= httpUtils.postData(httpUrl, map1, fileMap);
                Log.i("httpUtils=>", "上传的地址为：" + httpUrl);
                Log.e("httpUtils=>", "result：" + result);
                if ("保存成功".equals(result)) {
//                    updateUserUploadState(newUserId);   //如果返回保存成功则将用户表的上传状态改为true
                    handler.sendEmptyMessage(1);
                } else if ("保存失败".equals(result)) {
                    strErro =result;
                    handler.sendEmptyMessage(2);

                } else if ("".equals(result)) {
                    Log.i("UploadActivity=>", "网络请求错误！");
                    handler.sendEmptyMessage(9);
                }
            }
        }
    }
    //读取保存到本地的图片数据，并上传服务器
    public Map<String, File> getPhotoData(String newUserId) {
        Log.i("getUserData=>", "照片：" + newUserId);
        String userId = sharedPreferences_login.getString("userId", "");
        Log.i("getUserData=>", "userId：" + userId);
        Cursor cursor = db.rawQuery("select * from security_photo where newUserId=? and loginUserId=?", new String[]{newUserId , sharedPreferences_login.getString("userId", "")});//查询并获得游标
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

    private void SecurityAbnormalInfoRequest() {
        //加载
        loadingView = new LoadingView(SecurityAbnormalInfoActivity.this, R.style.LoadingDialog, "加载图片中...请稍后");
        loadingView.show();
        final String httpUrl = new StringBuffer().append(SkUrl.SkHttp(SecurityAbnormalInfoActivity.this)).append("getSecurityImageDates.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .params("c_data_id",String.valueOf(listbean.getAjbh()));
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e("pgl", "=== c_data_id" + response.getBody());

                //GSON直接解析成对象
                SecurityImageBean imagebean = new Gson().fromJson(response.getBody(), SecurityImageBean.class);
//                Log.d("pgl","=== securityAbnormalAdapter"+"执行了:"+resultBean.getList().size());
                if ("查询成功".equals(imagebean.getMsg())) {
                    if (imagebean.getList().size() > 0) {
                        // 图片C_data_id集合
                        cropIdLists = new ArrayList<>();
                        for (int i = 0 ;i < imagebean.getList().size() ;i++){
                            String n_image_id = String.valueOf(imagebean.getList().get(i).getN_image_id());
                            Log.e("pgl", "=== c_data_id" + imagebean.getList().get(i).getN_image_id());
                            cropIdLists.add(i,n_image_id);
                        }
                        iamgeAdapter = new SecurityGridviewImageAdapter(SecurityAbnormalInfoActivity.this, cropIdLists);
                        dlsrGridview.setAdapter(iamgeAdapter);
                        dlsrGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                dlsrGridview.setClickable(false);
                                currentPosition = position;
                                Log.e("选择的项",position+"");
                                if (!iamgeAdapter.getDeleteShow() && (iamgeAdapter.getCount() - 1 != position)) {
                                    //  Intent intent = new Intent(MeterUserDetailActivity.this, MyPhotoGalleryActivity.class);
                                    Intent intent = new Intent(SecurityAbnormalInfoActivity.this, SecurityAbnormalPhotoGalleryActivity.class);
                                    intent.putExtra("currentPosition", currentPosition);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("cropIdLists",cropIdLists);
                                    intent.putExtras(bundle);
//             intent.putStringArrayListExtra("cropPathLists", cropPathLists);
                                    Log.i("UserDetailInfoActivity", "点击图片跳转进来到预览详情页面的图片数量为：" + cropPathLists.size());
                                    startActivity(intent);
                                    dlsrGridview.setClickable(true);

                                }
                            }
                        });
                        loadingView.dismiss();
                    } else {
                        loadingView.dismiss();
//                        Toast.makeText(SecurityAbnormalInfoActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        llDlsrGridview.setVisibility(View.GONE);
                        tvDlsr.setText("无图片");
                    }
                } else {
//                    Toast.makeText(SecurityAbnormalActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                    loadingView.dismiss();
                    tvDlsr.setText("加载失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("pgl", "===" + e.getMessage());
                Toast.makeText(SecurityAbnormalInfoActivity.this, "网络请求异常", Toast.LENGTH_SHORT).show();
                loadingView.dismiss();
            }

        }).executeAsync();
    }
    /**
     * 全屏弹出
     */

    public void showAll() {
        if (myPopupWind != null && myPopupWind.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.popupwindow_security_hidden_type_or_reason, null);
        //测量View的宽高
        CommonUtil.measureWidthAndHeight(upView);
        myPopupWind = new MyPopupWind.Builder(this)
                .setView(R.layout.popupwindow_security_hidden_type_or_reason)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.mypopwindow_anim_style)
                .setViewOnclickListener(new MyPopupWind.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        //绑定控件ID
                        TextView tips = (TextView) view.findViewById(R.id.tips);
                        ListView listView = (ListView) view.findViewById(R.id.list_view);
                        TextView confirm = (TextView) view.findViewById(R.id.confirm);
                        LinearLayout noData = (LinearLayout) view.findViewById(R.id.no_data);
                        TextView noDataTip = (TextView) view.findViewById(R.id.no_data_tip);
                        LinearLayout confirmLayout = (LinearLayout) view.findViewById(R.id.confirm_layout);
                        tips.setText("请选择安检情况");
                        noDataTip.setText("暂无数据");
                        confirm.setText("取消");
                        if (securityCaseItemList != null) {
                            popupwindowListAdapter = new PopupwindowListAdapter(SecurityAbnormalInfoActivity.this, securityCaseItemList, 0);
                            listView.setAdapter(popupwindowListAdapter);
                        }
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            //listview点击事件
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                PopupwindowListItem item = (PopupwindowListItem) popupwindowListAdapter.getItem(position);
                                 securityCaseItemId = item.getItemId();
                                currentSecurityCheckCase.setText(item.getItemName());

                                if ((currentSecurityCheckCase.getText().equals("安全隐患"))) {
                                    showHiddenTypeAndReason();
                                } else {
                                    noShowHiddenTypeAndReason();
                                }
                                myPopupWind.dismiss();

                            }
                        });
                        confirmLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myPopupWind.dismiss();
                            }
                        });

                    }
                })
                .create();
        PopWindowUtil.backgroundAlpha(SecurityAbnormalInfoActivity.this, 0.6F);   //背景变暗
        myPopupWind.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SecurityAbnormalInfoActivity.this, 1.0F);   //背景变暗
            }
        });
        myPopupWind.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }
    //当不是安检合格的时候，显示安全隐患和安全隐患原因
    public void showHiddenTypeAndReason() {
        hiddenTypeRoot.setVisibility(View.VISIBLE);
        hiddenReasonRoot.setVisibility(View.VISIBLE);
//        remarksRoot.setVisibility(View.VISIBLE);
    }

    //当是安检合格的时候，不显示安全隐患和安全隐患原因
    public void noShowHiddenTypeAndReason() {
        hiddenTypeRoot.setVisibility(View.GONE);
        hiddenReasonRoot.setVisibility(View.GONE);
//        remarksRoot.setVisibility(View.GONE);
    }


    /**
     * 动态申请权限，如果6.0以上则弹出需要的权限选择框，以下则直接运行
     */
    private void requestPermissions() {
        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SecurityAbnormalInfoActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                /*if(ActivityCompat.shouldShowRequestPermissionRationale(UserDetailInfoActivity.this,Manifest.permission.CAMERA)){
                    //已经禁止提示了
                    Toast.makeText(UserDetailInfoActivity.this, "您已禁止该权限，需要重新开启！", Toast.LENGTH_SHORT).show();
                }else {
                    ActivityCompat.requestPermissions(UserDetailInfoActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                }*/
                permissionList.add(permissions[0]);
            }
            if (ContextCompat.checkSelfPermission(SecurityAbnormalInfoActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[1]);
            }
            if (ContextCompat.checkSelfPermission(SecurityAbnormalInfoActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[2]);
            }

            Log.i("requestPermissions", "权限集合的长度为：" + permissionList.size());
            if (!permissionList.isEmpty()) {  //判断权限集合是否为空
                String[] permissionArray = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(SecurityAbnormalInfoActivity.this, permissionArray, PERMISSION_REQUEST_CODE);
            } else {
                createPhotoPopupwindow();
            }
        } else {
            createPhotoPopupwindow();
        }
    }
    //弹出拍照popupwindow
    public void createPhotoPopupwindow() {
        inflater = LayoutInflater.from(SecurityAbnormalInfoActivity.this);
        popupwindowView = inflater.inflate(R.layout.popupwindow_security_userinfo_take_photo, null);
        popupWindow = new PopupWindow(popupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID

        takePhoto = (Button) popupwindowView.findViewById(R.id.take_photo);
        cancel = (Button) popupwindowView.findViewById(R.id.cancel);

        //设置点击事件
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto.setClickable(false);
                popupWindow.dismiss();
                currentGridview.setClickable(true);
                if (Tools.hasSdcard()) {
                    openCamera();//拍照
                } else {
                    Toast.makeText(SecurityAbnormalInfoActivity.this, "没有SD卡哦，不能拍照！", Toast.LENGTH_SHORT).show();
                }
                takePhoto.setClickable(true);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                currentGridview.setClickable(true);
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.showAtLocation(rootLinearlayout, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
                currentGridview.setClickable(true);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = SecurityAbnormalInfoActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            SecurityAbnormalInfoActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            SecurityAbnormalInfoActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        SecurityAbnormalInfoActivity.this.getWindow().setAttributes(lp);
    }


    //调用相机
    public void openCamera(){
        photoUtils = new MyPhotoUtils(SecurityAbnormalInfoActivity.this, listbean.getYhbh());
        Log.i("openCamera===>", "临时保存的地址为" + listbean.getYhbh());
        cropPhotoPath = photoUtils.generateImgePath();
        File imgFile = new File(cropPhotoPath);
//        File file = new MyPhotoUtils(SecurityAbnormalInfoActivity.this, listbean.getYhmc()+"").ImagecreateTempFile();
        Uri tempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempUri = FileProvider.getUriForFile(SecurityAbnormalInfoActivity.this, "com.example.administrator.thinker_soft.fileprovider", imgFile);//通过FileProvider创建一个content类型的Uri
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

    //页面回调方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {   //如果返回码是可用的
            switch (requestCode) {
                case TAKE_PHOTO:
                    //  startCropPhoto();
                    Log.i("startCropPhoto===>", "大小="+ FileSizeUtil.getFileOrFilesSize(cropPhotoPath));
                   File temFile = photoUtils.outputIamge(PictureUtil.compressSizeImage(cropPhotoPath));
                    File file = new File(cropPhotoPath);
                    file.delete();
                    Uri outputUri = Uri.fromFile(temFile);

                    // 下面就是照片上加时间
                    WaterMask.WaterMaskParam param = new WaterMask.WaterMaskParam();
                    Date d = new Date();
                    System.out.println(d);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateNowStr = sdf.format(d);  //2018-08-11 12:17:57
                    param.txt.add( dateNowStr);
                    param.itemCount = 30;

                    Bitmap bitmap = ImageUtil.getBitmap(outputUri.getPath());

                    WaterMask.draw(SecurityAbnormalInfoActivity.this,bitmap,outputUri.getPath(),param);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outputUri));

                    //    cropPathLists.add(outputUri.getPath());
                    cropPathLists.add(new PhotoPathBean(outputUri.getPath(),"选择类型",""));
                    Log.i("startCropPhoto===>", "图片集合长度为：" + cropPathLists.size() + "路径为" + outputUri.getPath()+"大小="+FileSizeUtil.getFileOrFilesSize(outputUri.getPath()));
                    //  refreshAdapter(cropPathLists);
                    currentGridviewImageAdapter.setGridviewImageList(cropPathLists);
                    break;
                case 500:
                    if (data != null) {
                        //    cropPathLists_back = data.getStringArrayListExtra("cropPathLists_back");
                        cropPathLists.clear();
                        cropPathLists= (ArrayList<PhotoPathBean>) data.getSerializableExtra("cropPathLists_back");
                        handler.sendEmptyMessage(0);
                    }
                    break;
                 default:
                     break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            //Toast.makeText(UserDetailInfoActivity.this, "您取消了拍照哦", Toast.LENGTH_SHORT).show();
        }

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    currentGridviewImageAdapter.setGridviewImageList(cropPathLists);
                    break;
                case 1:
//                    currentGridviewImageAdapter.setGridviewImageList(cropPathLists);
//                    Toast.makeText(SecurityAbnormalInfoActivity.this,"上传成功"+ strErro,Toast.LENGTH_SHORT).show();
                    //上传
                    Intent intent = new Intent();
                    intent.putExtra("isUpload","成功");
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case 2:
                    Toast.makeText(SecurityAbnormalInfoActivity.this,"上传失败"+ strErro,Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent();
                    setResult(RESULT_OK, intent2);
                    finish();
                    break;
                case 3:
                    confirm.setText("取消");
                    noData.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    confirm.setText("确定");
                    adapter = new PopupwindowListAdapter(SecurityAbnormalInfoActivity.this, securityHidenItemList,1);     //隐患类型
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    break;
                case 5:
                    securityHiddenReasonWindow();
                    confirm.setText("确定");
                    adapter = new PopupwindowListAdapter(SecurityAbnormalInfoActivity.this, securityReasonItemList,1);    //隐患原因
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    break;
                case 6:
                    loadingView.dismiss();
                    Toast.makeText(SecurityAbnormalInfoActivity.this, "亲,请选择安检情况", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(SecurityAbnormalInfoActivity.this, "亲,请添加图片哦", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };



    /**
     * 弹出安全隐患原因窗口
     */
    public void securityHiddenReasonWindow() {
        inflater = LayoutInflater.from(SecurityAbnormalInfoActivity.this);
        securityHiddenreasonView = inflater.inflate(R.layout.popupwindow_security_hidden_type_or_reason, null);
        popupWindow = new PopupWindow(securityHiddenreasonView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        tips = (TextView) securityHiddenreasonView.findViewById(R.id.tips);
        listView = (ListView) securityHiddenreasonView.findViewById(R.id.list_view);
        confirm = (TextView) securityHiddenreasonView.findViewById(R.id.confirm);
        noData = (LinearLayout) securityHiddenreasonView.findViewById(R.id.no_data);
        noDataTip = (TextView) securityHiddenreasonView.findViewById(R.id.no_data_tip);
        confirmLayout = (LinearLayout) securityHiddenreasonView.findViewById(R.id.confirm_layout);
        selectCounts = (TextView) securityHiddenreasonView.findViewById(R.id.select_counts);
        tips.setText("请选择安全隐患原因（可多选）");
        noDataTip.setText("暂无数据");
        hiddenReasonCount = 0;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NormalViewHolder viewHolder = (NormalViewHolder) view.getTag();
                viewHolder.checkedState.toggle();
                PopupwindowListAdapter.getIsCheck().put(position, viewHolder.checkedState.isChecked());
                if (viewHolder.checkedState.isChecked()) {
                    hiddenReasonCount++;
                } else {
                    hiddenReasonCount--;
                }
                //显示类型选中个数
                selectCounts.setText("(" + hiddenReasonCount + ")");
            }
        });
        confirmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHiddenReasonInfo();
                if (hiddenReeasonInfoMap.size() != 0) {
                    popupWindow.dismiss();
                    String reasonName = "";
                    String reasonId = "";
                    for (String key : hiddenReeasonInfoMap.keySet()) {
                        reasonName += hiddenReeasonInfoMap.get(key) + ",";
                        reasonId += key + ",";
                        hiddenReasonIDList.add(key);
                    }
                    hiddenReasonItemId = reasonId.substring(0, reasonId.length()-1);
                    hiddenReasonItemName = reasonName.substring(0, reasonName.length()-1);
                    securityHiddenReason.setText(hiddenReasonItemName);
                } else {
                    Toast.makeText(SecurityAbnormalInfoActivity.this, "请您选择隐患原因！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.Popupwindow);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
                securityHiddenReason.setClickable(true);
            }
        });
    }

    /**
     * 保存隐患选中的隐患原因信息
     */
    public void saveHiddenReasonInfo() {
        hiddenReeasonInfoMap.clear();
        for (int i = 0; i < securityReasonItemList.size(); i++) {
            if (PopupwindowListAdapter.getIsCheck().get(i)) {
                PopupwindowListItem item = securityReasonItemList.get((int) adapter.getItemId(i));
                hiddenReeasonInfoMap.put(item.getItemId(), item.getItemName());
                Log.i("saveHiddenReasonInfo", "这次被勾选的原因ID为：" + item.getItemId() + ",名称为：" + item.getItemName());
            }
        }
    }
    /**
     * 保存隐患选择隐患类型信息
     */
    public void saveHiddenTypeInfo() {
        hiddenTypeInfoMap.clear();
        for (int i = 0; i < securityHidenItemList.size(); i++) {
            if (PopupwindowListAdapter.getIsCheck().get(i)) {
                PopupwindowListItem item = securityHidenItemList.get((int) adapter.getItemId(i));
                hiddenTypeInfoMap.put(item.getItemId(), item.getItemName());
                Log.i("saveHiddenTypeInfo", "这次被勾选的隐患类型ID为：" + item.getItemId() + ",名称为：" + item.getItemName());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
        db.close();
    }

}
