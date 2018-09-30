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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.AuditUnqualifiedImageAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.GridviewImageAdapters;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AuditUnqualifiedBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NoThroughBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.PhotoPathBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityImageBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.AuditUnqualifiedImageActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.PhotoGalleryActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.SecurityAbnormalPhotoGalleryActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.DateFormatUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.FileSizeUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PictureUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
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

import static com.example.administrator.thinker_soft.meter_code.sk.ui.security.SecurityAbnormalInfoActivity.TAKE_PHOTO;

/**
 * 异常审核详情
 * @author 111
 */
public class AuditUnqualifiedInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView clear ,title ;
    private ImageView back;
    private TextView userName ,userNumber ,tableNumber ,userOldNumber, userPhone ,userAddress;
    private TextView securityPlan,securityName ,securityType ,securityState ,checkTime ,securityTime;
    private TextView securitySituation ,securityReasons ,securityReasonsType;
    private TextView lastTimeRemark ,dlsrPicture , currentPicture;
    private MyGridview dlsrGridview ,currentGridview;
    private CardView submitCardview ;
    private AuditUnqualifiedBean.ListBean listbean;
    private EditText currentRemarks;
    private LinearLayout llDlsrGridview;
    private LoadingView loadingView;
    private ArrayList<String> cropIdLists;
    private int currentPosition;
    private AuditUnqualifiedImageAdapter iamgeAdapter;
    private SecurityImageBean imagebean;
    private GridviewImageAdapters currentGridviewImageAdapter;
    private ArrayList<PhotoPathBean>  cropPathLists = new ArrayList<>(); //裁剪的图片路径集合
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}; //权限数组
    private List<String> permissionList = new ArrayList<>();  //权限集合
    protected static final int PERMISSION_REQUEST_CODE = 1;  //6.0之后需要动态申请权限，   请求码
    private LayoutInflater inflater;
    private View popupwindowView  ,saveView;
    private PopupWindow popupWindow;
    private Button takePhoto ,cancel;
    private MyPhotoUtils photoUtils;
    private String cropPhotoPath;
    private RadioButton cancelRb , saveRb;
    private SQLiteDatabase db;
    private SharedPreferences sharedPreferences_login;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_unqualified_info);

        initView();      // 初始化View
        defaultSetting();//初始化设置
        setOnClickListener();//点击事件


    }
    private void initView() {
        clear = findViewById(R.id.clear);
        title = findViewById(R.id.tv_title);
        back = findViewById(R.id.back);
        userName = findViewById(R.id.tv_user_name);
        userNumber = findViewById(R.id.tv_userId);
        tableNumber = findViewById(R.id.table_number);
        userOldNumber = findViewById(R.id.tv_user_old_id);
        userPhone = findViewById(R.id.tv_user_phone);
        userAddress = findViewById(R.id.tv_user_address);
        securityPlan = findViewById(R.id.security_plan);
        securityName = findViewById(R.id.tv_user_sy);
        securityType = findViewById(R.id.tv_type);
        securityState = findViewById(R.id.tv_sy_state);
        checkTime = findViewById(R.id.tv_check_time);
        securityTime = findViewById(R.id.tv_security_time);
        securitySituation = findViewById(R.id.tv_security_situation);
        securityReasons = findViewById(R.id.tv_why);
        securityReasonsType = findViewById(R.id.tv_yh_lx);
        lastTimeRemark = findViewById(R.id.tv_remark);
        currentRemarks = findViewById(R.id.current_remarks);
        dlsrPicture = findViewById(R.id.tv_dlsr);
        currentPicture = findViewById(R.id.tv_current);
        dlsrGridview = findViewById(R.id.dlsr_gridview);
        currentGridview = findViewById(R.id.current_gridview);
        submitCardview = findViewById(R.id.submit_cardview);
        llDlsrGridview = findViewById(R.id.ll_dlsr_gridview);

    }

    private void defaultSetting() {
        clear.setVisibility(View.GONE);
        title.setText("异常审核详情");
        listbean =(AuditUnqualifiedBean.ListBean ) getIntent().getSerializableExtra("Bean");
        userName.setText(listbean.getYHMC());
        userNumber.setText(listbean.getYHBH());
        tableNumber.setText(listbean.getBBH());
        userOldNumber.setText(listbean.getLBH() == null ? "" : listbean.getLBH());
        userPhone.setText(listbean.getLXDH());
        userAddress.setText(listbean.getYHDZ());
        securityPlan.setText(listbean.getAJJHMC());
        securityName.setText(listbean.getAJY() ==null ?"":listbean.getAJY());
        securityType.setText(listbean.getAJLX());
        securityState.setText(listbean.getAJZT());
        checkTime.setText(listbean.getSCAJSJ() == 0 ? "" :DateFormatUtil.format(listbean.getSCAJSJ()));
        securityTime.setText(listbean.getAJSJ() == 0 ? "" :DateFormatUtil.format(listbean.getAJSJ()));
        securitySituation.setText(listbean.getAJQK() ==null ?"":listbean.getAJQK());
        securityReasons.setText(listbean.getAQYHYY() ==null ?"":listbean.getAQYHYY());
        securityReasonsType.setText(listbean.getAQYHLX() ==null ?"":listbean.getAQYHLX());
        lastTimeRemark.setText(listbean.getAJBZ() ==null ?"":listbean.getAJBZ());

        MySqliteHelper helper = new MySqliteHelper(AuditUnqualifiedInfoActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = AuditUnqualifiedInfoActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);


        // 加载图片
        auditUnqualifiedInfoRequest();
        // 上传照片的适配器
        currentGridviewAdapter();
    }


    private void setOnClickListener() {
        back.setOnClickListener(this);
        submitCardview.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch ( v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.submit_cardview:
                //  上传当前审核信息
                createSaveWindow();
                break;
            default:
                break;
        }
    }

    private void upload() {

        Map<String, File> fileMap= getPhotoData(listbean.getYHBH());  // 装 用户id
        Log.i("fileMap=>", "fileMap：" + fileMap.size());
        Log.i("fileMap=>", "getYhbh：" + listbean.getYHBH());
        if (fileMap.size() ==0){
            handler.sendEmptyMessage(3);
        }else {
            HashMap map1 = new HashMap<String, Object>();// 装 信息
            map1.put("n_safety_inspection_id", listbean.getAJBH());  //
            Log.i("getUserDataAndPost=>", "安检信息ID= 安检编号：" + listbean.getAJBH());
            map1.put("c_safety_remark",currentRemarks.getText().toString().trim() );  //
            Log.i("getUserDataAndPost=>", "c_safety_remark：" + currentRemarks.getText().toString().trim() );

            String httpUrl = new StringBuffer().append(SkUrl.SkHttp(AuditUnqualifiedInfoActivity.this)).append("addSecurityImage.do").toString();
            Log.e("getUserDataAndPost=>", "httpUrl==：" + httpUrl);
            HttpUtils httpUtils = new HttpUtils();
            String result = httpUtils.postData(httpUrl, map1, fileMap);
            NoThroughBean bean = new Gson().fromJson(result, NoThroughBean.class);

            if ("新增成功".equals(bean.getMsg())) {
                handler.sendEmptyMessage(0);
            } else if ("新增失败".equals(bean.getMsg())) {
                handler.sendEmptyMessage(1);
            } else if ("".equals(bean.getMsg())) {
                Log.e("UploadActivity=>", "网络请求错误！");
                handler.sendEmptyMessage(2);
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


    //弹出是否保存popupwindow
    public void createSaveWindow() {
        inflater = LayoutInflater.from(AuditUnqualifiedInfoActivity.this);
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
                loadingView = new LoadingView(AuditUnqualifiedInfoActivity.this, R.style.LoadingDialog, "上传中...请稍后");
                loadingView.show();
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (cropPathLists.size() != 0) {
                            db.delete("security_photo", "newUserId=? and loginUserId=? ", new String[]{listbean.getYHBH() , sharedPreferences_login.getString("userId", "")});  //删除security_photo表中的当前用户的照片数据
                            db.execSQL("update sqlite_sequence set seq=0 where name='security_photo'");
                            for (int i = 0; i < cropPathLists.size(); i++) {
                                insertSecurityPhoto(cropPathLists.get(i).getCropPath());
                            }
                            updateUserPhoto(String.valueOf(cropPathLists.size()));
                        }
                        try {
                            Thread.sleep(250);
                            //上传安检信息
                            upload();

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
        popupWindow.showAtLocation(saveRb, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    /**
     * 将拍的照片路径保存到本地数据库安检图片表
     */
    private void insertSecurityPhoto(String photoPath) {
        ContentValues values = new ContentValues();
        values.put("newUserId", listbean.getYHBH());
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
        db.update("User", values, "newUserId=? and loginUserId=?", new String[]{listbean.getYHBH(), sharedPreferences_login.getString("userId", "")});
    }

    private void currentGridviewAdapter() {
        //  当前照片适配
        currentGridviewImageAdapter = new GridviewImageAdapters(AuditUnqualifiedInfoActivity.this, cropPathLists);
        currentGridview.setAdapter(currentGridviewImageAdapter);
        currentGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentGridview.setClickable(false);
                currentPosition = position;
                if (!currentGridviewImageAdapter.getDeleteShow() && (currentGridviewImageAdapter.getCount() - 1 != position)) {
                    Intent intent = new Intent(AuditUnqualifiedInfoActivity.this, PhotoGalleryActivity.class);
                    intent.putExtra("currentPosition", currentPosition);
                    intent.putExtra("newUserId", listbean.getYHBH());
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
    }

    private void auditUnqualifiedInfoRequest() {
        //加载
        loadingView = new LoadingView(AuditUnqualifiedInfoActivity.this, R.style.LoadingDialog, "加载图片中...请稍后");
        loadingView.show();
        final String httpUrl = new StringBuffer().append(SkUrl.SkHttp(AuditUnqualifiedInfoActivity.this)).append("getSecurityImageDates.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .params("c_data_id",String.valueOf(listbean.getAJBH()));
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e("pgl", "=== c_data_id" + response.getBody());

                //GSON直接解析成对象
                imagebean = new Gson().fromJson(response.getBody(), SecurityImageBean.class);
//                Log.d("pgl","=== securityAbnormalAdapter"+"执行了:"+resultBean.getList().size());
                if ("查询成功".equals(imagebean.getMsg())) {
                    if (imagebean.getList().size() > 0) {
                        // 图片C_data_id集合
                        cropIdLists = new ArrayList<>();
                        for (int i = 0; i < imagebean.getList().size() ; i++){
                            String n_image_id = String.valueOf(imagebean.getList().get(i).getN_image_id());
                            Log.e("pgl", "=== c_data_id" + imagebean.getList().get(i).getN_image_id());
                            cropIdLists.add(i,n_image_id);
                        }
                        iamgeAdapter = new AuditUnqualifiedImageAdapter(AuditUnqualifiedInfoActivity.this, cropIdLists);
                        dlsrGridview.setAdapter(iamgeAdapter);
                        dlsrGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                dlsrGridview.setClickable(false);
                                currentPosition = position;
                                Log.e("选择的项",position+"");
                                if (!iamgeAdapter.getDeleteShow() && (iamgeAdapter.getCount() - 1 != position)) {
                                    //  Intent intent = new Intent(MeterUserDetailActivity.this, MyPhotoGalleryActivity.class);
                                    Intent intent = new Intent(AuditUnqualifiedInfoActivity.this, AuditUnqualifiedImageActivity.class);
                                    intent.putExtra("currentPosition", currentPosition);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("cropIdLists", cropIdLists);
                                    bundle.putSerializable("imagebean", imagebean);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent,10);
                                    dlsrGridview.setClickable(true);

                                }
                            }
                        });
                        loadingView.dismiss();
                    } else {
                        loadingView.dismiss();
//                        Toast.makeText(SecurityAbnormalInfoActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        llDlsrGridview.setVisibility(View.GONE);
                        dlsrPicture.setText("无图片");
                    }
                } else {
//                    Toast.makeText(SecurityAbnormalActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                    loadingView.dismiss();
                    llDlsrGridview.setVisibility(View.GONE);
                    dlsrPicture.setText("加载失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("pgl", "===" + e.getMessage());
                Toast.makeText(AuditUnqualifiedInfoActivity.this, "网络请求异常", Toast.LENGTH_SHORT).show();
                loadingView.dismiss();
            }

        }).executeAsync();
    }
    /**
     * 动态申请权限，如果6.0以上则弹出需要的权限选择框，以下则直接运行
     */
    private void requestPermissions() {
        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AuditUnqualifiedInfoActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                /*if(ActivityCompat.shouldShowRequestPermissionRationale(UserDetailInfoActivity.this,Manifest.permission.CAMERA)){
                    //已经禁止提示了
                    Toast.makeText(UserDetailInfoActivity.this, "您已禁止该权限，需要重新开启！", Toast.LENGTH_SHORT).show();
                }else {
                    ActivityCompat.requestPermissions(UserDetailInfoActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                }*/
                permissionList.add(permissions[0]);
            }
            if (ContextCompat.checkSelfPermission(AuditUnqualifiedInfoActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[1]);
            }
            if (ContextCompat.checkSelfPermission(AuditUnqualifiedInfoActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[2]);
            }

            Log.i("requestPermissions", "权限集合的长度为：" + permissionList.size());
            if (!permissionList.isEmpty()) {  //判断权限集合是否为空
                String[] permissionArray = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(AuditUnqualifiedInfoActivity.this, permissionArray, PERMISSION_REQUEST_CODE);
            } else {
                createPhotoPopupwindow();
            }
        } else {
            createPhotoPopupwindow();
        }
    }
    //弹出拍照popupwindow
    public void createPhotoPopupwindow() {
        inflater = LayoutInflater.from(AuditUnqualifiedInfoActivity.this);
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
                    Toast.makeText(AuditUnqualifiedInfoActivity.this, "没有SD卡哦，不能拍照！", Toast.LENGTH_SHORT).show();
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
        popupWindow.showAtLocation(cancel, Gravity.BOTTOM, 0, 0);
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
        WindowManager.LayoutParams lp = AuditUnqualifiedInfoActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            AuditUnqualifiedInfoActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            AuditUnqualifiedInfoActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        AuditUnqualifiedInfoActivity.this.getWindow().setAttributes(lp);
    }

    //调用相机
    public void openCamera(){
        photoUtils = new MyPhotoUtils(AuditUnqualifiedInfoActivity.this, listbean.getYHBH());
        Log.i("openCamera===>", "临时保存的地址为" + listbean.getYHBH());
        cropPhotoPath = photoUtils.generateImgePath();
        File imgFile = new File(cropPhotoPath);
//        File file = new MyPhotoUtils(SecurityAbnormalInfoActivity.this, listbean.getYhmc()+"").ImagecreateTempFile();
        Uri tempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempUri = FileProvider.getUriForFile(AuditUnqualifiedInfoActivity.this, "com.example.administrator.thinker_soft.fileprovider", imgFile);//通过FileProvider创建一个content类型的Uri
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
        startActivityForResult(openCameraIntent, 11);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    loadingView.dismiss();
                    Toast.makeText(AuditUnqualifiedInfoActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("isUpload","成功");
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case 1:
                    loadingView.dismiss();
                    Toast.makeText(AuditUnqualifiedInfoActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    loadingView.dismiss();
                    Toast.makeText(AuditUnqualifiedInfoActivity.this,"网络请求错误",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    loadingView.dismiss();
                    Toast.makeText(AuditUnqualifiedInfoActivity.this,"没有图片",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10) {
                Log.i("UserList=ActivityResult", "页面回调进来了");

                if (data!=null){
                    String isUp=data.getStringExtra("Upload");
                    if (isUp!=null && isUp.equals("更新")){
                        // 加载图片
                        auditUnqualifiedInfoRequest();
                    }
                }
            }
            if (requestCode == 11) {
                //  startCropPhoto();
                Log.i("startCropPhoto===>", "大小=" + FileSizeUtil.getFileOrFilesSize(cropPhotoPath));
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
                param.txt.add(dateNowStr);
                param.itemCount = 30;
                Bitmap bitmap = ImageUtil.getBitmap(outputUri.getPath());
                WaterMask.draw(AuditUnqualifiedInfoActivity.this, bitmap, outputUri.getPath(), param);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outputUri));

                //    cropPathLists.add(outputUri.getPath());
                cropPathLists.add(new PhotoPathBean(outputUri.getPath(), "选择类型", ""));
                Log.i("startCropPhoto===>", "图片集合长度为：" + cropPathLists.size() + "路径为" + outputUri.getPath() + "大小=" + FileSizeUtil.getFileOrFilesSize(outputUri.getPath()));
                //  refreshAdapter(cropPathLists);
                currentGridviewImageAdapter.setGridviewImageList(cropPathLists);
            }


        }
    }
}
