package com.example.administrator.thinker_soft.Security_check.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.adapter.PopupwindowListAdapter;
import com.example.administrator.thinker_soft.Security_check.model.NormalViewHolder;
import com.example.administrator.thinker_soft.Security_check.model.PopupwindowListItem;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.GridviewImageAdapters;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NewsReviseParams;
import com.example.administrator.thinker_soft.meter_code.sk.bean.PhotoPathBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.observer.ObserverManager;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.PhotoGalleryActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.AuditUnqualifiedInfoActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.EditTextUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.FileSizeUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.FileUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PictureUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.mode.HttpUtils;
import com.example.administrator.thinker_soft.mode.MyPhotoUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.mode.Tools;
import com.example.administrator.thinker_soft.mode.photo.ImageUtil;
import com.example.administrator.thinker_soft.mode.photo.WaterMask;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 安检详细信息
 * Created by Administrator on 2017/3/16 0016.
 */
public class UserDetailInfoActivity extends AppCompatActivity {
    private ImageView back;  //返回
    private GridView gridView;
    private LinearLayout rootLinearlayout, remarksRoot,confirmLayout,noData;
    private RelativeLayout rootRelative;
    private LinearLayout hiddenTypeRoot, hiddenReasonRoot;
    private TextView securityCaseTv, securityHiddenType, securityHiddenReason;  //安全情况,安全隐患类型，安全隐患原因
    private CardView caseCardview,typeCardview,reasonCardview,saveCardview;
    private Button takePhoto, cancel;  //保存、拍照、相册、取消
    private ListView listView;
    private TextView tips,confirm,selectCounts,noDataTip ;
    private RadioButton cancelRb, saveRb, remarksRb1, remarksRb2;
    private LayoutInflater inflater;  //转换器
    private View noOperateView, popupwindowView, securityCaseView, securityHiddenTypeView, securityHiddenreasonView, saveView;
    private PopupWindow popupWindow;
    int sdkVersion = Build.VERSION.SDK_INT;  //当前SDK版本
    private int TYPE_FILE_CROP_IMAGE = 2;
    protected static final int TAKE_PHOTO = 100;//拍照
    protected static final int CROP_SMALL_PICTURE = 300;  //裁剪成小图片
    protected static final int PERMISSION_REQUEST_CODE = 1;  //6.0之后需要动态申请权限，   请求码
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}; //权限数组
    private List<String> permissionList = new ArrayList<>();  //权限集合
    private SharedPreferences sharedPreferences, sharedPreferences_login;
    private SharedPreferences.Editor editor;
    private String ifUpload,taskId;
    private EditText meterNumberEt, userPhoneNumber, userAddress;
    private TextView /*userNewIdTv*/  /*securityTypeTv*/saveTv ,tvSecurityType,tvUserid ;

    private GridviewImageAdapters iamgeAdapter;
    private PopupwindowListAdapter adapter;
    private String cropPhotoPath;  //裁剪的图片路径
  //  private ArrayList<String> cropPathLists = new ArrayList<>();  //裁剪的图片路径集合
    //裁剪的图片路径集合
    private ArrayList<PhotoPathBean> cropPathLists = new ArrayList<>();

    private ArrayList<String>filePathLists = new ArrayList<>();  //大图页面返回的图片路径集合
    private int currentPosition = 0;
    private SQLiteDatabase db;  //数据库
    private List<PopupwindowListItem> securityCaseItemList = new ArrayList<>();
    private List<PopupwindowListItem> securityHidenItemList = new ArrayList<>();
    private List<PopupwindowListItem> securityReasonItemList = new ArrayList<>();
    private String securityCaseItemId = "", securityHiddenItemId = "", hiddenReasonItemId = "";//安检情况类型id,安检隐患类型id,安检隐患原因id
    private String securityCaseItemName = "", securityHiddenItemName = "", hiddenReasonItemName = "";
    private String userNewId, userName, meterNumber,securityNumber, phoneNumber, address, securityType, securityCase, hiddenType, hiddenReason, remarks;
    private int hiddenTypeCount = 0;
    private int hiddenReasonCount = 0;
    private HashMap<String, String> hiddenTypeInfoMap = new HashMap<>();  //用于保存选中的隐患类型信息
    private HashMap<String, String> hiddenReeasonInfoMap = new HashMap<>();  //用于保存选中的隐患原因信息
    private List<String> hiddenTypeIDList = new ArrayList<>();     //用于保存隐患类型ID
    private List<String> hiddenReasonIDList = new ArrayList<>();   //用于保存隐患原因ID
    private boolean flag = false;
    private MyPhotoUtils photoUtils;//路径
    private String strErro="";//上传
    private String isChecked="未检";
    private String securityType1;
    private LoadingView loadingView;
    private TextView tvEdit;//编辑
    private EditText userNameEd;//姓名
    private String userNameEt,userPhone;//电话和姓名
    private String company_id;//公司id
    private String newUserId;

    /**本次安检时间*/
    private String checkTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_info);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setViewClickListener();//点击事件
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        securityCaseTv = (TextView) findViewById(R.id.security_check_case);
        securityHiddenType = (TextView) findViewById(R.id.security_hidden_type);
        securityHiddenReason = (TextView) findViewById(R.id.security_hidden_reason);
        caseCardview = (CardView) findViewById(R.id.case_cardview);
        typeCardview = (CardView) findViewById(R.id.type_cardview);
        reasonCardview = (CardView) findViewById(R.id.reason_cardview);
        /*userNewIdTv = (TextView) findViewById(R.id.user_new_id);*/
        userNameEd = (EditText) findViewById(R.id.user_name);
        meterNumberEt = (EditText) findViewById(R.id.meter_number);
        userAddress = (EditText) findViewById(R.id.user_address);
        /*securityTypeTv = (TextView) findViewById(R.id.check_type);*/
        userPhoneNumber = (EditText) findViewById(R.id.user_phone_number);
        remarksRb1 = (RadioButton) findViewById(R.id.remarks_rb1);
        remarksRb2 = (RadioButton) findViewById(R.id.remarks_rb2);
        hiddenTypeRoot = (LinearLayout) findViewById(R.id.hidden_type_root);
        hiddenReasonRoot = (LinearLayout) findViewById(R.id.hidden_reason_root);
        saveCardview = (CardView) findViewById(R.id.save_cardview);
        saveTv = (TextView) findViewById(R.id.save_tv);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
        rootRelative = (RelativeLayout) findViewById(R.id.root_relative);
        remarksRoot = (LinearLayout) findViewById(R.id.remarks_root);
        gridView = (GridView) findViewById(R.id.gridview);
        tvSecurityType = (TextView) findViewById(R.id.tv_security_type);
        tvUserid = (TextView) findViewById(R.id.tv_userid);
        tvEdit= (TextView) findViewById(R.id.tv_edit);
        //设置不可编辑
        EditTextUtils.setEnabled(meterNumberEt,false);
        EditTextUtils.setEnabled(userAddress,false);
        EditTextUtils.setEnabled(userNameEd,false);
        EditTextUtils.setEnabled(userPhoneNumber,false);

    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = UserDetailInfoActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        company_id=sharedPreferences_login.getInt("company_id",0)+"";
        MySqliteHelper helper = new MySqliteHelper(UserDetailInfoActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        /**
         * 获取上一个页面传过来的用户ID
         */
        Intent intent = getIntent();
        if (intent != null) {
            userNewId = intent.getStringExtra("user_new_id");

            taskId = intent.getStringExtra("taskId");
            ifUpload = intent.getStringExtra("if_upload");
            isChecked =intent.getStringExtra("is_checked");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    getUserData(userNewId, sharedPreferences_login.getString("userId", ""));
                }
            }.start();
        }
    }

    //点击事件
    private void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        caseCardview.setOnClickListener(onClickListener);
        typeCardview.setOnClickListener(onClickListener);
        reasonCardview.setOnClickListener(onClickListener);
        saveCardview.setOnClickListener(onClickListener);
        tvEdit.setOnClickListener(onClickListener);
        userAddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
                if ((v.getId() == R.id.user_address && canVerticalScroll(userAddress))) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });
        iamgeAdapter = new GridviewImageAdapters(UserDetailInfoActivity.this, cropPathLists);
        //gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(iamgeAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                gridView.setClickable(false);
//                currentPosition = position;
//                if (!iamgeAdapter.getDeleteShow() && (iamgeAdapter.getCount() - 1 != position)) {
//                    Intent intent = new Intent(UserDetailInfoActivity.this, MyPhotoGalleryActivity.class);
//                    intent.putExtra("currentPosition", currentPosition);
//                    intent.putExtra("newUserId",userNewId);
//
//                    intent.putStringArrayListExtra("cropPathLists", cropPathLists);
//                    Log.i("UserDetailInfoActivity", "点击图片跳转进来到预览详情页面的图片数量为：" + cropPathLists.size());
//                    startActivityForResult(intent, 500);
//                    gridView.setClickable(true);
//                }
                gridView.setClickable(false);
                currentPosition = position;
                Log.e("选择的项",position+"");
                if (!iamgeAdapter.getDeleteShow() && (iamgeAdapter.getCount() - 1 != position)) {
                    //  Intent intent = new Intent(MeterUserDetailActivity.this, MyPhotoGalleryActivity.class);
                    Intent intent = new Intent(UserDetailInfoActivity.this, PhotoGalleryActivity.class);
                    intent.putExtra("currentPosition", currentPosition);
                    intent.putExtra("newUserId", userNewId);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("cropPathLists",cropPathLists);
                    intent.putExtras(bundle);
//             intent.putStringArrayListExtra("cropPathLists", cropPathLists);
                    Log.i("UserDetailInfoActivity", "点击图片跳转进来到预览详情页面的图片数量为：" + cropPathLists.size());
                    startActivityForResult(intent, 500);
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
        /*gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!(position == cropPathLists.size())) {
                    // 如果删除按钮已经显示了则不再设置
                    if (!adapter.getDeleteShow()) {
                        adapter.setDeleteShow(true);
                        adapter.notifyDataSetChanged();
                    }
                }
                // 返回true，停止事件向下传播
                return true;
            }
        });*/
    }

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    //如果安检状态为true，再次进入安检详情页的时候，如果在原来的基础上新增图片或者更换图片，只要没点击保存按钮，则不记录本次的更改
//                    if (cropPathLists.size() != 0) {
//                        for (int i = 0; i < cropPathLists.size(); i++) {
   //                         deletePicture(new File(cropPathLists.get(i).getCropPath()));
//                        }
//                    }
                    finish();
                    break;
                case R.id.case_cardview:
                    securityCasePopupwindow();
                    //获取安检情况列表信息
                    new Thread() {
                        @Override
                        public void run() {
                            getSecurityCheckCase();
                        }
                    }.start();

                    break;
                case R.id.type_cardview:
                    if(!"点击选择".equals(securityCaseTv.getText())){
                        securityHiddenTypeWindow();
                        //获取安全隐患列表信息
                        new Thread() {
                            @Override
                            public void run() {
                                getSecurityHiddenType();
                            }
                        }.start();
                    }else {
                        Toast.makeText(UserDetailInfoActivity.this, "请您先选择安检情况！", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.reason_cardview:
                    if (hiddenTypeIDList.size() != 0) {
                        //获取隐患原因
                        new Thread() {
                            @Override
                            public void run() {
                                securityReasonItemList.clear();
                                for (int i = 0; i < hiddenTypeIDList.size(); i++) {
                                    getSecurityHiddenReason(hiddenTypeIDList.get(i));
                                }
                                if (securityReasonItemList.size() != 0) {
                                    handler.sendEmptyMessage(5);
                                } else {
                                    handler.sendEmptyMessage(6);
                                }
                            }
                        }.start();
                    }else {
                        Toast.makeText(UserDetailInfoActivity.this, "请您先选择隐患类型！", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.save_cardview:
                    //保存
                    if (!userAddress.getText().toString().trim().equals("")) {

                        if (securityCaseTv.getText().toString().equals("安全隐患") ) {
                            if (!"点击选择".equals(securityCaseTv.getText().toString()) && !"点击选择".equals(securityHiddenType.getText().toString())
                                    && !"点击选择".equals(securityHiddenReason.getText().toString()) && (remarksRb1.isChecked() || remarksRb2.isChecked())) {
                                createSaveWindow();
                            } else {
                                Toast.makeText(UserDetailInfoActivity.this, "请您将安检信息填写完整！", Toast.LENGTH_LONG).show();
                            }

                        } else if ( "点击选择".equals(securityCaseTv.getText().toString() )){
                            Toast.makeText(UserDetailInfoActivity.this, "请您将安检信息填写完整！", Toast.LENGTH_LONG).show();
                        }else {
                            createSaveWindow();
                        }


                    } else {
                        Toast.makeText(UserDetailInfoActivity.this, "用户地址不能为空哦！", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.tv_edit:
                    //编辑
                    if (tvEdit.getText().toString().trim().equals("编辑")){
                        tvEdit.setText("完成");
                        EditTextUtils.setEnabled(userNameEd,true);
                        EditTextUtils.setEnabled(userPhoneNumber,true);
                        userNameEd.setBackgroundColor(Color.parseColor("#efeff4"));
                        userPhoneNumber.setBackgroundColor(Color.parseColor("#efeff4"));

                    }else {
                        tvEdit.setText("编辑");
                        EditTextUtils.setEnabled(userNameEd,false);
                        EditTextUtils.setEnabled(userPhoneNumber,false);
                        userNameEd.setBackgroundColor(Color.WHITE);
                        userPhoneNumber.setBackgroundColor(Color.WHITE);
                        //修改信息
                        String name=userNameEd.getText().toString().trim();
                        String phone=userPhoneNumber.getText().toString().trim();
                        if (!userNameEt.equals(name)||!userPhone.equals(phone)){
                            NewsReviseParams params=new NewsReviseParams();
                            params.setC_meter_number(meterNumber);
                            params.setC_user_id(userNewId);
                            params.setC_user_phone(phone);
                            params.setC_user_name(name);
                            params.setN_meter_direction("");
                            params.setN_log_operator_id(company_id);
                            String log=new StringBuffer().append(!userNameEt.equals(name)?userNewId+"[用户名由"+userNameEt+"改为"+name+"]":"")
                                    .append(!phone.equals(userPhone)?"[电话号码由"+userPhone+"改为"+phone+"]":"")
                                    .toString();
                            params.setC_log(log);
                            ReviseRequest(params);
                        }
                    }

                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 动态申请权限，如果6.0以上则弹出需要的权限选择框，以下则直接运行
     */
    private void requestPermissions() {
        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(UserDetailInfoActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                /*if(ActivityCompat.shouldShowRequestPermissionRationale(UserDetailInfoActivity.this,Manifest.permission.CAMERA)){
                    //已经禁止提示了
                    Toast.makeText(UserDetailInfoActivity.this, "您已禁止该权限，需要重新开启！", Toast.LENGTH_SHORT).show();
                }else {
                    ActivityCompat.requestPermissions(UserDetailInfoActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                }*/
                permissionList.add(permissions[0]);
            }
            if (ContextCompat.checkSelfPermission(UserDetailInfoActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[1]);
            }
            if (ContextCompat.checkSelfPermission(UserDetailInfoActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[2]);
            }

            Log.i("requestPermissions", "权限集合的长度为：" + permissionList.size());
            if (!permissionList.isEmpty()) {  //判断权限集合是否为空
                String[] permissionArray = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(UserDetailInfoActivity.this, permissionArray, PERMISSION_REQUEST_CODE);
            } else {
                createPhotoPopupwindow();
            }
        } else {
            createPhotoPopupwindow();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!flag){
            if ("已上传".equals(ifUpload)) {
                //createNoOperate();
                saveCardview.setEnabled(false);
                saveTv.setText("已上传，不可更改");
            }
            flag = true;
        }
    }

    //弹出popupwindow让用户不可操作
    public void createNoOperate() {
        inflater = LayoutInflater.from(UserDetailInfoActivity.this);
        noOperateView = inflater.inflate(R.layout.popupwindow_no_operate, null);
        popupWindow = new PopupWindow(noOperateView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.showAsDropDown(rootRelative, 0, 0);
    }

    //弹出拍照popupwindow
    public void createPhotoPopupwindow() {
        inflater = LayoutInflater.from(UserDetailInfoActivity.this);
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
                gridView.setClickable(true);
                if (Tools.hasSdcard()) {
                    openCamera();//拍照
                } else {
                    Toast.makeText(UserDetailInfoActivity.this, "没有SD卡哦，不能拍照！", Toast.LENGTH_SHORT).show();
                }
                takePhoto.setClickable(true);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                gridView.setClickable(true);
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
                gridView.setClickable(true);
            }
        });
    }

    //弹出是否保存popupwindow
    public void createSaveWindow() {
        inflater = LayoutInflater.from(UserDetailInfoActivity.this);
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
                loadingView = new LoadingView(UserDetailInfoActivity.this, R.style.LoadingDialog, "上传中...请稍后");
                loadingView.show();
                //安检时间
                checkTime=Tools.getCurrentTime();
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        updateUserInfo();
                        if (isChecked.equals("未检")){
                            updateTaskInfo(taskId,sharedPreferences_login.getString("userId", ""));
                        }
                        //保存图片
                        savephoto();
//                        if (cropPathLists.size() != 0) {
//                            db.delete("security_photo", "newUserId=? and loginUserId=?", new String[]{userNewId, sharedPreferences_login.getString("userId", "")});  //删除security_photo表中的当前用户的照片数据
//                            db.execSQL("update sqlite_sequence set seq=0 where name='security_photo'");
//                            for (int i = 0; i < cropPathLists.size(); i++) {
//                                insertSecurityPhoto(cropPathLists.get(i).getCropPath());
//                            }

                       // }
                        //用户表是否安检状态
                        updateUserCheckedState();
                        try {
                            Thread.sleep(250);
                            //0自动上传，100手动上传
                            if (sharedPreferences.getInt("security_up", 100)==0) {
                                //上传安检信息
                                getUserDataAndPost(taskId);
                            }else{
                                handler.sendEmptyMessage(11);
                                Intent intent3 = new Intent();
                                intent3.putExtra("checkTime",checkTime);
                                setResult(Activity.RESULT_OK, intent3);
                                finish();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(11);
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

    /**
     * 弹出安全情况窗口
     */
    public void securityCasePopupwindow() {
        inflater = LayoutInflater.from(UserDetailInfoActivity.this);
        securityCaseView = inflater.inflate(R.layout.popupwindow_security_hidden_type_or_reason, null);
        popupWindow = new PopupWindow(securityCaseView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        tips = (TextView) securityCaseView.findViewById(R.id.tips);
        listView = (ListView) securityCaseView.findViewById(R.id.list_view);
        confirm = (TextView) securityCaseView.findViewById(R.id.confirm);
        noData = (LinearLayout) securityCaseView.findViewById(R.id.no_data);
        noDataTip = (TextView) securityCaseView.findViewById(R.id.no_data_tip);
        confirmLayout = (LinearLayout) securityCaseView.findViewById(R.id.confirm_layout);
        tips.setText("请选择安检情况");
        noDataTip.setText("暂无数据");
        confirm.setText("取消");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {           //listview点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupwindowListItem item = (PopupwindowListItem) adapter.getItem(position);
                popupWindow.dismiss();
                securityCaseItemId = item.getItemId();       //获得当前用户点击的安检情况itemID
                securityCaseItemName = item.getItemName();
                securityCaseTv.setText(securityCaseItemName);    //点击之后，设置对应的名称
                Log.i("createSecurityCasePopu", "选中的安检情况ID :" + securityCaseItemId +"==="+securityCaseItemName);
//                if (!(securityCaseTv.getText().equals("合格") || securityCaseTv.getText().equals("复检合格"))) {
//                    showHiddenTypeAndReason();
//                } else {
//                    noShowHiddenTypeAndReason();
//                }
                if ((securityCaseTv.getText().equals("安全隐患"))) {
                    showHiddenTypeAndReason();
                } else {
                    noShowHiddenTypeAndReason();
                }
            }
        });
        confirmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //当不是安检合格的时候，显示安全隐患和安全隐患原因
    public void showHiddenTypeAndReason() {
        hiddenTypeRoot.setVisibility(View.VISIBLE);
        hiddenReasonRoot.setVisibility(View.VISIBLE);
        remarksRoot.setVisibility(View.VISIBLE);
    }

    //当是安检合格的时候，不显示安全隐患和安全隐患原因
    public void noShowHiddenTypeAndReason() {
        hiddenTypeRoot.setVisibility(View.GONE);
        hiddenReasonRoot.setVisibility(View.GONE);
        remarksRoot.setVisibility(View.GONE);
    }

    /**
     * 弹出安全隐患类型窗口
     */
    public void securityHiddenTypeWindow() {
        inflater = LayoutInflater.from(UserDetailInfoActivity.this);
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
                        Toast.makeText(UserDetailInfoActivity.this, "请您选择隐患类型！", Toast.LENGTH_SHORT).show();
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

    /**
     * 弹出安全隐患原因窗口
     */
    public void securityHiddenReasonWindow() {
        inflater = LayoutInflater.from(UserDetailInfoActivity.this);
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
                    Toast.makeText(UserDetailInfoActivity.this, "请您选择隐患原因！", Toast.LENGTH_SHORT).show();
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

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = UserDetailInfoActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            UserDetailInfoActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            UserDetailInfoActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        UserDetailInfoActivity.this.getWindow().setAttributes(lp);
    }


    //调用相机
    public void openCamera() {
       // File file = new MyPhotoUtils(UserDetailInfoActivity.this, userNewId).createTempFile();
         photoUtils= new MyPhotoUtils(UserDetailInfoActivity.this, userNewId);
        cropPhotoPath = photoUtils.generateImgePath();
       File imgFile = new File(cropPhotoPath);
        Uri tempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempUri = FileProvider.getUriForFile(UserDetailInfoActivity.this, "com.example.administrator.thinker_soft.fileprovider", imgFile);//通过FileProvider创建一个content类型的Uri
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
    // myFile/ThinkerSoft_super/Icon_image/20180809_083710_003263910100494.jpg

        startActivityForResult(openCameraIntent, TAKE_PHOTO);

    }

    //以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)){
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    //页面回调方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {   //如果返回码是可用的
            switch (requestCode) {
                case TAKE_PHOTO:
                    Log.i("startCropPhoto===>", "大小="+ FileSizeUtil.getFileOrFilesSize(cropPhotoPath));
                 /*   try {
                        saveFile(PictureUtil.compressSizeImage(cropPhotoPath),cropPhotoPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    File temFile = photoUtils.outputIamge(PictureUtil.compressSizeImage(cropPhotoPath));
                    File file = new File(cropPhotoPath);
                    file.delete();
                    Uri outputUri = Uri.fromFile(temFile);
                    // 下面就是照片上加时间
                    WaterMask.WaterMaskParam param = new WaterMask.WaterMaskParam();
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateNowStr = sdf.format(d);  //2018-08-11 12:17:57
                    param.txt.add(dateNowStr);
                    param.itemCount = 30;
                    Bitmap bitmap = ImageUtil.getBitmap(outputUri.getPath());
                    WaterMask.draw(UserDetailInfoActivity.this, bitmap, outputUri.getPath(), param);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outputUri));

                    cropPathLists.add(new PhotoPathBean(outputUri.getPath(),"选择类型",""));
                    Log.i("startCropPhoto===>", "图片集合长度为：" + cropPathLists.size() + "路径为" + cropPhotoPath+"大小="+FileSizeUtil.getFileOrFilesSize(cropPhotoPath));
                    iamgeAdapter.setGridviewImageList(cropPathLists);
                    break;
                case CROP_SMALL_PICTURE:
//                    Log.i("MeterUserDetailActivity", "图片裁剪回调进来了！ ");
//                    File file = new MyPhotoUtils(UserDetailInfoActivity.this, userNewId).createTempFile();
//                    Uri tempUri;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        tempUri = FileProvider.getUriForFile(UserDetailInfoActivity.this, "com.example.administrator.thinker_soft.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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
//                    handler.sendEmptyMessage(1);
                    break;
                      case 500:
                    if (data != null) {
                    //    cropPathLists_back = data.getStringArrayListExtra("cropPathLists_back");
                        cropPathLists.clear();
                        cropPathLists= (ArrayList<PhotoPathBean>) data.getSerializableExtra("cropPathLists_back");
                        handler.sendEmptyMessage(2);
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            //Toast.makeText(UserDetailInfoActivity.this, "您取消了拍照哦", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 保存压缩后的图片
     * @param bitmap
     * @param filePath2
     * @throws IOException
     */
    private void saveFile(Bitmap bitmap, String filePath2) throws Exception {
        // TODO Auto-generated method stub
        File testFile = new File(filePath2);
        if (testFile.exists()) {
            testFile.delete();
        }

        File myCaptureFile = new File(filePath2);
        System.out.println("------filePath2==" + filePath2);
        BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
        // 100表示不进行压缩，70表示压缩率为30%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
        bos.flush();
        bos.close();
    }




    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    /*userNewIdTv.setText(userNewId);*/
                    userNameEd.setText(userName);

                    tvUserid.setText(userNewId);
                    tvSecurityType.setText(securityType);
                    meterNumberEt.setText(meterNumber);
                    userPhoneNumber.setText(phoneNumber);
                    userNameEt=userName;//名字
                    userPhone=phoneNumber;//电话

                    userAddress.setText(address);
                    Log.i("pgl===   用户名称   >", "userName：" + userName);
                    Log.i("pgl===   用户新编号   >", "userName：" + userNewId);
                    Log.i("pgl===  安检类型  >", "securityType：" + securityType);
                    Log.i("pgl===  表编号 >", "meterNumber：" + meterNumber);
                    Log.i("pgl===     用户电话号码  >", "phoneNumber：" + phoneNumber);
                    Log.i("pgl===    用户地址  >", "userAddress：" + address);

                    /*securityTypeTv.setText(securityType);*/
                    if("".equals(securityCase)){
                        securityCaseTv.setText("点击选择");
                    }else {
                        securityCaseTv.setText(securityCase);
                    }
                    if (!(securityCaseTv.getText().equals("合格") || securityCaseTv.getText().equals("复检合格")|| securityCaseTv.getText().equals("第一次到访不遇")|| securityCaseTv.getText().equals("第二次到访不遇"))) {
                        showHiddenTypeAndReason();
                    } else {
                        noShowHiddenTypeAndReason();
                    }
                    if("".equals(hiddenType)){
                        securityHiddenType.setText("点击选择");
                    }else {
                        securityHiddenType.setText(hiddenType);
                    }
                    if("".equals(hiddenReason)){
                        securityHiddenReason.setText("点击选择");
                    }else {
                        securityHiddenReason.setText(hiddenReason);
                    }
                    if ("需要复检".equals(remarks)) {
                        remarksRb1.setChecked(true);
                    } else if ("现场已整改".equals(remarks)) {
                        remarksRb2.setChecked(true);
                    } else {
                        remarksRb1.setChecked(false);
                        remarksRb2.setChecked(false);
                    }
                    break;
                case 1:
                    iamgeAdapter.setGridviewImageList(cropPathLists);
                    break;
                case 2:
                    iamgeAdapter.setGridviewImageList(cropPathLists);

                    break;
                case 3:
                    securityCaseItemList.remove(2);
                    securityCaseItemList.remove(5);
                    adapter = new PopupwindowListAdapter(UserDetailInfoActivity.this, securityCaseItemList,0);      //安检情况
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    break;
                case 4:
                    confirm.setText("确定");
                    adapter = new PopupwindowListAdapter(UserDetailInfoActivity.this, securityHidenItemList,1);     //隐患类型
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    break;
                case 5:
                    securityHiddenReasonWindow();
                    confirm.setText("确定");
                    adapter = new PopupwindowListAdapter(UserDetailInfoActivity.this, securityReasonItemList,1);    //隐患原因
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    break;
                case 6:
                    confirm.setText("取消");
                    noData.setVisibility(View.VISIBLE);
                    break;

                case 7:
                    loadDiss();
                    Toast.makeText(UserDetailInfoActivity.this,"上传成功"+strErro,Toast.LENGTH_SHORT).show();
                    //上传
                    Intent intent = new Intent();
                    intent.putExtra("isUpload","成功");
                    intent.putExtra("checkTime",checkTime);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    break;
                case 8:
                    loadDiss();
                    Toast.makeText(UserDetailInfoActivity.this,"上传失败"+strErro,Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent();
                    intent2.putExtra("checkTime",checkTime);
                    setResult(Activity.RESULT_OK, intent2);
                    finish();
                    break;
                case 9:
                    loadDiss();
                    Toast.makeText(UserDetailInfoActivity.this,"网络异常！",Toast.LENGTH_SHORT).show();
                    Intent intent3 = new Intent();
                    intent3.putExtra("checkTime",checkTime);
                    setResult(Activity.RESULT_OK, intent3);
                    finish();
                    break;
                case 10:
                    loadDiss();
                    Toast.makeText(UserDetailInfoActivity.this, "亲,请添加图片哦", Toast.LENGTH_SHORT).show();
                    break;
                case 11:
                    loadDiss();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 根据用户新编号查询用户信息
     */    public void getUserData(String userId, String loginUserId) {

        Cursor cursor = db.rawQuery("select * from User where newUserId=? and loginUserId=?", new String[]{userId, loginUserId});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            userName = cursor.getString(cursor.getColumnIndex("userName"));
            meterNumber = cursor.getString(cursor.getColumnIndex("meterNumber"));
            securityNumber=cursor.getString(cursor.getColumnIndex("securityNumber"));
            Log.i("meterNumber","meterNumber="+meterNumber);
            phoneNumber = cursor.getString(cursor.getColumnIndex("userPhone"));
              newUserId = cursor.getString(cursor.getColumnIndex("newUserId"));
            address = cursor.getString(cursor.getColumnIndex("userAddress"));
            securityType = cursor.getString(cursor.getColumnIndex("securityType"));
            securityCase = cursor.getString(cursor.getColumnIndex("security_case"));
            hiddenType = cursor.getString(cursor.getColumnIndex("security_hidden"));
            hiddenReason = cursor.getString(cursor.getColumnIndex("security_hidden_reason"));
            remarks = cursor.getString(cursor.getColumnIndex("remarks"));
            if (!"0".equals(cursor.getString(cursor.getColumnIndex("photoNumber")))) {
                //读取照片信息并显示
                querySecurityPhoto(userNewId);
            }
        }
        handler.sendEmptyMessage(0);
        cursor.close(); //游标关闭
    }
//    /**
//     * 刷新数据
//     */
//    private void refreshAdapter(ArrayList<String> imageList) {
//        iamgeAdapter.setGridviewImageList(imageList);
//    }
    /**
     * 假数据
     */
    public void getTempData(){
        userName = "马云";
        meterNumber = "123";
        phoneNumber = "188888866666";
        address = "重庆市江北区鲁溉路";
        securityType = "常规安检";
        /*userNewIdTv.setText(userNewId);*/
        userNameEd.setText(userName);
        meterNumberEt.setText(meterNumber);
        userPhoneNumber.setText(phoneNumber);
        userAddress.setText(address);
        /*securityTypeTv.setText(securityType);*/
    }

    /**
     * 读取安全情况列表
     */
    public void getSecurityCheckCase() {
        securityCaseItemList.clear();
        Cursor cursor = db.query("security_content", null, null, null, null, null, null);//查询并获得游标
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(6);
            return;
        }
        while (cursor.moveToNext()) {
            PopupwindowListItem item = new PopupwindowListItem();
            item.setItemId(cursor.getString(cursor.getColumnIndex("securityId")));
            item.setItemName(cursor.getString(cursor.getColumnIndex("securityName")));
            securityCaseItemList.add(item);
        }
        handler.sendEmptyMessage(3);
        cursor.close();
    }

    /**
     * 读取安全隐患类型列表
     */
    public void getSecurityHiddenType() {
        securityHidenItemList.clear();
        Cursor cursor = db.query("security_hidden", null, null, null, null, null, null);//查询并获得游标
        //如果游标为空，则返回空
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(6);
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

    /**
     * 裁剪图片方法实现
     */
    protected void startCropPhoto() {
        File file = new MyPhotoUtils(UserDetailInfoActivity.this, userNewId).createTempFile();
        Uri tempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempUri = FileProvider.getUriForFile(UserDetailInfoActivity.this, "com.example.administrator.thinker_soft.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            // 指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
            tempUri = Uri.fromFile(file);
        }
        if (tempUri != null) {
            File file1 = new MyPhotoUtils(UserDetailInfoActivity.this, userNewId).createCropFile();
            Uri cropPhotoUri = Uri.fromFile(file1);
            Log.i("startCropPhoto", "图片裁剪的uri = " + cropPhotoUri);
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
             //   String url = getPath(UserDetailInfoActivity.this, tempUri);
                String url = getPath(UserDetailInfoActivity.this, cropPhotoUri);
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
            Toast.makeText(UserDetailInfoActivity.this, "拍照失败，请重试！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 返回的时候，删除所有拍照的图片
     */
    private void deletePicture(File file) {
        if (file.exists()) {
            file.delete();
        } else {
            Log.i("deletePicture", "没有相应的图片文件");
        }
    }
    //更新用户表是否安检状态
    public void updateUserCheckedState() {
        ContentValues values = new ContentValues();
        values.put("ifChecked", "true");
        Log.i("UserList=update", "更新安检状态为true");
        db.update("User", values, "securityNumber=? and loginUserId=?", new String[]{securityNumber, sharedPreferences_login.getString("userId", "")});
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
     * 将安检的信息保存至本地
     */
    private void updateUserInfo() {
        ContentValues values = new ContentValues();
        if (!"".equals(securityCaseItemId)) {      //安检情况ID
            values.put("security_case", securityCaseItemName);
            values.put("security_case_id", securityCaseItemId);
        }
        if (!( securityCaseTv.getText().toString().equals("合格") || securityCaseTv.getText().toString().equals("复检合格"))){
            if (remarksRb1.isChecked()) {
                remarks = remarksRb1.getText().toString().trim();
                if (securityCaseTv.getText().toString().equals("安全隐患")) {
                    values.put("security_state", "2");
                } else if (securityCaseTv.getText().toString().equals("拒绝安检")) {
                    values.put("security_state", "5");
                } else if (securityCaseTv.getText().toString().equals("第一次到访不遇") || securityCaseTv.getText().toString().equals("第二次到访不遇") || securityCaseTv.getText().toString().equals("第三次到访不遇")) {
                    values.put("security_state", "4");
                }else if(securityCaseTv.getText().toString().equals("超过安检时间")){
                    values.put("security_state", "3");
                }
            } else if (remarksRb2.isChecked()) {
                remarks = remarksRb2.getText().toString().trim();
                values.put("security_state", "1");
                Log.e("===== values.put:","1");
            }
            values.put("remarks", remarks);
            if (!"".equals(securityHiddenItemId)) {         //隐患类型ID
                Log.e("securityHiddenItemId:",securityHiddenItemId+"===="+securityHiddenItemName);
                values.put("security_hidden_id", securityHiddenItemId);
                values.put("security_hidden", securityHiddenItemName);
            }
            if (!"".equals(hiddenReasonItemId)) {               //隐患原因ID
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
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);  //2018-08-11 12:17:57
        values.put("lastCheckTime", dateNowStr);
        db.update("User", values, "newUserId=? and loginUserId=?", new String[]{userNewId, sharedPreferences_login.getString("userId", "")});
    }

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
     * 根据安检ID查询用户是否处于安检状态，如果是安检状态，则显示上次安检所记录的图片，否则不显示
     */
    private void querySecurityPhoto(String newUserId) {
        Cursor cursor = db.rawQuery("select * from security_photo where newUserId=? and loginUserId=?", new String[]{newUserId, sharedPreferences_login.getString("userId", "")});//查询并获得游标
        while (cursor.moveToNext()) {
           // cropPathLists.add(cursor.getString(cursor.getColumnIndex("photoPath")));
            PhotoPathBean bean=new PhotoPathBean();
            bean.setCropPath(cursor.getString(cursor.getColumnIndex("photoPath")));
            bean.setType("");
            bean.setTypeId("");
            cropPathLists.add(bean);
            filePathLists.add(cursor.getString(cursor.getColumnIndex("photoPath")));
        }
        handler.sendEmptyMessage(1);
        Log.i("querySecurityPhoto", "上次照片数量为：" + cropPathLists.size());
    }
    /**
     * 保存图片到数据库 UPDATE sqlite_sequence SET seq =0 WHER Ename ='TableName';//清除数据
     */
    private void savephoto() {
        if (cropPathLists.size() != 0) {
            db.delete("security_photo", "newUserId=? and loginUserId=?", new String[]{userNewId, sharedPreferences_login.getString("userId", "")});  //删除security_photo表中的当前用户的照片数据
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
     * 保存裁剪之后的图片数据
     *
     * @param bitmaps
     */
    private void saveImage(List<Bitmap> bitmaps) {
        String filePath;
        File file;
        try {
            for (int i = 0; i < bitmaps.size(); i++) {
                Bitmap photo = bitmaps.get(i);
                if (Tools.hasSdcard()) {
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sienke/files/img/" + userNewId + "_" + i + ".jpg";
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                } else {
                    filePath = "data/data/" + UserDetailInfoActivity.this.getPackageName() + "/Sienke/files/img/" + userNewId + "_" + i + ".jpg";
                    file = new File("data/data/" + UserDetailInfoActivity.this.getPackageName());
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                Log.i("UserDetailInfoActivity", "file=>" + filePath);
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);//通过file打开输出流
                //将bitmap写入到文件
                photo.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);//图片压缩
                fileOutputStream.flush();
                fileOutputStream.close();
                Log.i("UserDetailInfoActivity", "bitmap写入到文件");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(UserDetailInfoActivity.this, "必须同意所有权限才能操作哦！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    //用户同意授权
                    createPhotoPopupwindow();//调用相机
                } else {
                    //用户拒绝授权
                    Toast.makeText(UserDetailInfoActivity.this, "您拒绝了授权！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    //读取本地安检用户数据，并上传服务器
    public void getUserDataAndPost(final String taskId) {
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginUserId=? and meterNumber=?", new String[]{taskId, sharedPreferences_login.getString("userId", ""),meterNumber});//查询并获得游标
        HashMap   map1 = new HashMap<String, Object>();
        Log.i("getUserDataAndPost=>", "上传的用户数为：" + cursor.getCount());
        while (cursor.moveToNext()) {
            Log.i("getUserDataAndPost=>", "安检：" + cursor.getString(cursor.getColumnIndex("ifChecked"))+cursor.getString(cursor.getColumnIndex("ifUpload")));
            if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("true")) {  //判断是否为安检过的，未安检的不上传
                if (cursor.getString(cursor.getColumnIndex("ifUpload")).equals("false")) { //判断是否为未上传，上传的用户数据不再上传
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

                        Map<String, File>   fileMap= getPhotoData(newUserId);
                        Log.i("fileMap=>", "fileMap：" + fileMap.size());
                        if (fileMap.size() ==0){
                            handler.sendEmptyMessage(10);
                            break;
                        }
                        //上传路径
                        String httpUrl=new StringBuffer().append(SkUrl.SkHttp(UserDetailInfoActivity.this)).append("updateInspection.do").toString();
                        HttpUtils httpUtils=new HttpUtils();
                        String result=httpUtils.postData(httpUrl, map1, fileMap);
                        Log.i("httpUtils=>", "上传的地址为：" + httpUrl);
                        if ("保存成功".equals(result)) {

                            updateUserUploadState(newUserId);   //如果返回保存成功则将用户表的上传状态改为true
                            handler.sendEmptyMessage(7);
                        } else if ("保存失败".equals(result)) {

                            strErro=result;
                            handler.sendEmptyMessage(8);
                            break;
                        } else if ("".equals(result)) {

                            Log.i("UploadActivity=>", "网络请求错误！");
                            handler.sendEmptyMessage(9);
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
    private void loadDiss() {
        if (loadingView != null) {
            loadingView.dismiss();
        }

    }

    //读取保存到本地的图片数据，并上传服务器
    public Map<String, File> getPhotoData(String newUserId) {
        Cursor cursor = db.rawQuery("select * from security_photo where newUserId=? and loginUserId=?", new String[]{newUserId, sharedPreferences_login.getString("userId", "")});//查询并获得游标
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
     * 更新用户表用户信息
     */
    private void updateUserData(String name,String phone) {
        ContentValues values = new ContentValues();
        values.put("userName", name);
        values.put("userPhone",phone);
        db.update("User", values, "newUserId=? and loginUserId=?", new String[]{newUserId, sharedPreferences_login.getString("userId", "")});
    }


    /**
     * 修改信息
     */
        private void ReviseRequest(final NewsReviseParams param) {
            //加载
            loadingView = new LoadingView(UserDetailInfoActivity.this, R.style.LoadingDialog, "修改中...请稍后");
            loadingView.show();
            String httpUrl=new StringBuffer().append(SkUrl.SkHttp(UserDetailInfoActivity.this)).append("updateUserMsg.do").toString();
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
                        JSONObject  jsonObject=new JSONObject(response.getBody());
                        String success= jsonObject.optString("status","");
                        if (success.equals("success")){
                            NewsReviseParams params=new NewsReviseParams();
                            params.setC_user_name(param.getC_user_name());
                            params.setC_user_phone(param.getC_user_phone());
                            ObserverManager.getInstance().notifyObserver(new Gson().toJson(params));
                            //更新
                            updateUserData(param.getC_user_name(),param.getC_user_phone());
                            ToastUtil.showShort(UserDetailInfoActivity.this,"修改成功");
                        }else {
                            ToastUtil.showShort(UserDetailInfoActivity.this,"修改失败");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.showShort(UserDetailInfoActivity.this,"修改失败");
                    }

                }

                @Override
                public void onError(Throwable e) {
                    Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
                    loadDiss();
                    ToastUtil.showShort(UserDetailInfoActivity.this,"修改失败");

                }
            }).executeAsync();
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
