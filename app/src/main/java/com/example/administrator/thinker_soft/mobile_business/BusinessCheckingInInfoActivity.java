package com.example.administrator.thinker_soft.mobile_business;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.activity.MyPhotoGalleryActivity;
import com.example.administrator.thinker_soft.Security_check.adapter.GridviewImageAdapter;
import com.example.administrator.thinker_soft.mode.MyPhotoUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.mode.Tools;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/6/13.
 */
public class BusinessCheckingInInfoActivity extends Activity {
    private ImageView back;
    private GridView gridView;
    private LinearLayout map;
    private RadioButton cancelRb, saveRb;
    private Cursor cursor;
    private RelativeLayout linkman;
    private TextView dizhi, time, clear, securityType;
    private EditText customerName;
    private GridviewImageAdapter adapter;
    private String securityId;
    private SQLiteDatabase db;  //数据库
    private SharedPreferences sharedPreferences, sharedPreferences_login;
    private int currentPosition = 0;
    private String cropPhotoPath;  //裁剪的图片路径
    private ArrayList<String> cropPathLists = new ArrayList<>();  //裁剪的图片路径集合
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}; //权限数组
    private List<String> permissionList = new ArrayList<>();  //权限集合
    protected static final int PERMISSION_REQUEST_CODE = 1;  //6.0之后需要动态申请权限，   请求码
    private LayoutInflater inflater;  //转换器
    private View popupwindowView, saveView;
    private PopupWindow popupWindow;
    private TextView keHu, shangJi, xiangMu, riCheng, renWu;
    private PopupWindow window;
    protected static final int TAKE_PHOTO = 100;//拍照
    private LinearLayout rootLinearlayout;
    protected static final int CROP_SMALL_PICTURE = 300;  //裁剪成小图片
    private ArrayList<String> cropPathLists_back = new ArrayList<>();  //大图页面返回的图片路径集合
    private Button takePhoto, cancel, saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_business_checking_in_info);//考勤详细

        bindView();//绑定控件
        defaultSetting();
        setOnClickListener();//点击事件

    }


    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        clear = (TextView) findViewById(R.id.clear);
        map = (LinearLayout) findViewById(R.id.map);
        linkman = (RelativeLayout) findViewById(R.id.linkman);
        dizhi = (TextView) findViewById(R.id.dizhi);
        gridView = (GridView) findViewById(R.id.gridview);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
        saveBtn = (Button) findViewById(R.id.save_btn);
        time = (TextView) findViewById(R.id.time);
        customerName = (EditText) findViewById(R.id.customer_name);
        securityType = (TextView) findViewById(R.id.security_type);
    }

    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(BusinessCheckingInInfoActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        time.setText(dateFormat.format(new Date()));
    }

    public void setOnClickListener() {
        back.setOnClickListener(clickListener);
        clear.setOnClickListener(clickListener);
        map.setOnClickListener(clickListener);
        linkman.setOnClickListener(clickListener);
        saveBtn.setOnClickListener(clickListener);

        adapter = new GridviewImageAdapter(BusinessCheckingInInfoActivity.this, cropPathLists);
        //gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.setClickable(false);
                currentPosition = position;
                if (!adapter.getDeleteShow() && (adapter.getCount() - 1 != position)) {
                    Intent intent = new Intent(BusinessCheckingInInfoActivity.this, MyPhotoGalleryActivity.class);
                    intent.putExtra("currentPosition", currentPosition);
                    intent.putStringArrayListExtra("cropPathLists", cropPathLists);
                    Log.i("UserDetailInfoActivity", "点击图片跳转进来到预览详情页面的图片数量为：" + cropPathLists.size());
                    startActivityForResult(intent, 500);
                    gridView.setClickable(true);
                }
                // 如果单击时删除按钮处在显示状态，则隐藏它
                if (adapter.getDeleteShow()) {
                    adapter.setDeleteShow(false);
                    adapter.notifyDataSetChanged();
                } else {
                    if (adapter.getCount() - 1 == position) {
                        // 判断是否达到了可添加图片最大数
                        if (cropPathLists.size() != 9) {
                            requestPermissions();      //检测权限
                        }
                    }
                }
            }
        });

        securityType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = BusinessCheckingInInfoActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_business_checkinginfo, null);
                window = new PopupWindow(popupView, 600, 400);
                window.setAnimationStyle(R.style.Popupwindow);
                window.setFocusable(true);
                backgroundAlpha(0.6F);   //背景变暗
                window.setOutsideTouchable(true);
                window.update();
                window.showAsDropDown(securityType, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1.0F);
                        securityType.setClickable(true);
                    }
                });
                keHu = (TextView) popupView.findViewById(R.id.kehu);
                shangJi = (TextView) popupView.findViewById(R.id.shangji);
                renWu = (TextView) popupView.findViewById(R.id.renwu);
                riCheng = (TextView) popupView.findViewById(R.id.richeng);
                xiangMu = (TextView) popupView.findViewById(R.id.xiangmu);

                keHu.setOnClickListener(clickListener);
                shangJi.setOnClickListener(clickListener);
                renWu.setOnClickListener(clickListener);
                riCheng.setOnClickListener(clickListener);
                xiangMu.setOnClickListener(clickListener);
            }
        });
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.clear:
                    db.delete("OaUser", null, null);  //删除OaUser表中所有数据（官方推荐方法）
                    db.delete("OaUserOutWork", null, null);
                    db.execSQL("update sqlite_sequence set seq=0 where name='OaUser'");
                    db.execSQL("update sqlite_sequence set seq=0 where name='OaUserOutWork'");
                    Toast.makeText(BusinessCheckingInInfoActivity.this, "清除数据成功！", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.map:
                    Intent intent1 = new Intent(BusinessCheckingInInfoActivity.this, BusinessCheckingIninfoMapActivity.class);
                    startActivityForResult(intent1, 200);
                    break;
                case R.id.linkman:
                    Intent intent = new Intent(BusinessCheckingInInfoActivity.this, BusinessNetPhoneBookActivity.class);
                    startActivity(intent);
                    break;
                case R.id.save_btn:
                    createSavePopupwindow();
                    break;
                case R.id.kehu:
                    securityType.setText("客户");
                    window.dismiss();
                    break;
                case R.id.xiangmu:
                    securityType.setText("项目");
                    window.dismiss();
                    break;
                case R.id.renwu:
                    securityType.setText("任务");
                    window.dismiss();
                    break;
                case R.id.richeng:
                    securityType.setText("日程");
                    window.dismiss();
                    break;
                case R.id.shangji:
                    securityType.setText("商机");
                    window.dismiss();
                    break;
            }
        }
    };

    //弹出是否保存popupwindow
    public void createSavePopupwindow() {
        inflater = LayoutInflater.from(BusinessCheckingInInfoActivity.this);
        saveView = inflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        popupWindow = new PopupWindow(saveView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
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
                insertOaUserOutWork();
                insertOaUser();
                if (cropPathLists.size() != 0) {
                    for (int i = 0; i < cropPathLists.size(); i++) {
                        insertOaPhoto(cropPathLists.get(i));
                    }
                }
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                popupWindow.dismiss();
                finish();
            }
        });
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
                saveBtn.setClickable(true);
            }
        });
    }

    private void insertOaPhoto(String photoPath) {
        ContentValues values = new ContentValues();
        values.put("photoPath", photoPath);
        values.put("userId", sharedPreferences_login.getString("userId", ""));
        db.insert("oaPhoto", null, values);
    }

    /**
     * 将信息保存到本地数据库OA用户外勤信息表
     */
    private void insertOaUserOutWork() {
        ContentValues values = new ContentValues();
        values.put("userId", sharedPreferences_login.getString("userId", ""));
        values.put("checkTime", time.getText().toString().trim());
        values.put("checkAddress", dizhi.getText().toString().trim());
        values.put("contactType", securityType.getText().toString().trim());
        values.put("customerName", customerName.getText().toString().trim());
        db.insert("OaUserOutWork", null, values);
    }

    /**
     * 将信息保存到本地数据库OA用户基础信息表
     */  
    private void insertOaUser() {
        ContentValues values = new ContentValues();
        cursor = db.rawQuery("select * from OaUser where userId=?", new String[]{sharedPreferences_login.getString("userId", "")});//根据用户ID查询用户外勤次数
        if (cursor.getCount() == 0) {
            values.put("userId", sharedPreferences_login.getString("userId", ""));
            values.put("outWork", "1");
            db.insert("OaUser", null, values);
        } else {
            while (cursor.moveToNext()) {
                values.put("outWork", Integer.parseInt(cursor.getString(cursor.getColumnIndex("outWork"))) + 1 + "");
                Log.i("insertOaUser", "签到次数" + Integer.parseInt(cursor.getString(cursor.getColumnIndex("outWork"))) + 1);
            }
            db.update("OaUser", values, "userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
        }
    }

    /**
     * 动态申请权限，如果6.0以上则弹出需要的权限选择框，以下则直接运行
     */
    private void requestPermissions() {
        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(BusinessCheckingInInfoActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                /*if(ActivityCompat.shouldShowRequestPermissionRationale(UserDetailInfoActivity.this,Manifest.permission.CAMERA)){
                    //已经禁止提示了
                    Toast.makeText(UserDetailInfoActivity.this, "您已禁止该权限，需要重新开启！", Toast.LENGTH_SHORT).show();
                }else {
                    ActivityCompat.requestPermissions(UserDetailInfoActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
                }*/
                permissionList.add(permissions[0]);
            }
            if (ContextCompat.checkSelfPermission(BusinessCheckingInInfoActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[1]);
            }
            if (ContextCompat.checkSelfPermission(BusinessCheckingInInfoActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[2]);
            }

            Log.i("requestPermissions", "权限集合的长度为：" + permissionList.size());
            if (!permissionList.isEmpty()) {  //判断权限集合是否为空
                String[] permissionArray = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(BusinessCheckingInInfoActivity.this, permissionArray, PERMISSION_REQUEST_CODE);
            } else {
                createPhotoPopupwindow();
            }
        } else {
            createPhotoPopupwindow();
        }
    }

    //弹出拍照popupwindow
    public void createPhotoPopupwindow() {
        inflater = LayoutInflater.from(BusinessCheckingInInfoActivity.this);
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
                    Toast.makeText(BusinessCheckingInInfoActivity.this, "没有SD卡哦，不能拍照！", Toast.LENGTH_SHORT).show();
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


    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = BusinessCheckingInInfoActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            BusinessCheckingInInfoActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            BusinessCheckingInInfoActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        BusinessCheckingInInfoActivity.this.getWindow().setAttributes(lp);
    }

    //调用相机
    public void openCamera() {
        File file = new MyPhotoUtils(BusinessCheckingInInfoActivity.this, securityId).createTempFile();
        Uri tempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempUri = FileProvider.getUriForFile(BusinessCheckingInInfoActivity.this, "com.example.administrator.myapplicationsienke.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            // 指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
            tempUri = Uri.fromFile(file);
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
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

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
            if (cursor != null)
                cursor.close();
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
                case 200:
                    if (data != null) {
                        dizhi.setText(data.getStringExtra("location"));
                    }
                    break;
                case TAKE_PHOTO:
                    startCropPhoto();
                    break;
                case CROP_SMALL_PICTURE:
                    Log.i("MeterUserDetailActivity", "图片裁剪回调进来了！ ");
                    File file = new MyPhotoUtils(BusinessCheckingInInfoActivity.this, securityId).createTempFile();
                    Uri tempUri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tempUri = FileProvider.getUriForFile(BusinessCheckingInInfoActivity.this, "com.example.administrator.myapplicationsienke.fileprovider", file);//通过FileProvider创建一个content类型的Uri
                    } else {
                        // 指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        tempUri = Uri.fromFile(file);
                    }
                    if (tempUri != null) {
                        File file1 = new File(tempUri.getPath());
                        if (file1.exists()) {
                            Log.i("MeterUserDetailActivity", "删除原图！ ");
                            file1.delete();
                        }
                    }
                    cropPathLists.add(cropPhotoPath);
                    Log.i("startCropPhoto===>", "图片集合长度为：" + cropPathLists.size() + "路径为" + cropPhotoPath);
                    handler.sendEmptyMessage(1);
                    break;
                case 500:
                    if (data != null) {
                        cropPathLists_back = data.getStringArrayListExtra("cropPathLists_back");
                        handler.sendEmptyMessage(2);
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            //Toast.makeText(UserDetailInfoActivity.this, "您取消了拍照哦", Toast.LENGTH_SHORT).show();
        }

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.i("MeterUserDetailActivity", "显示图片进来了！ ");
                    adapter = new GridviewImageAdapter(BusinessCheckingInInfoActivity.this, cropPathLists);
                    adapter.notifyDataSetChanged();
                    gridView.setAdapter(adapter);
                    break;
                case 2:
                    adapter = new GridviewImageAdapter(BusinessCheckingInInfoActivity.this, cropPathLists_back);
                    cropPathLists = cropPathLists_back;
                    adapter.notifyDataSetChanged();
                    gridView.setAdapter(adapter);
                    break;
            }
        }
    };

    /**
     * 裁剪图片方法实现
     */
    protected void startCropPhoto() {
        File file = new MyPhotoUtils(BusinessCheckingInInfoActivity.this, securityId).createTempFile();
        Uri tempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempUri = FileProvider.getUriForFile(BusinessCheckingInInfoActivity.this, "com.example.administrator.myapplicationsienke.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            // 指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
            tempUri = Uri.fromFile(file);
        }
        if (tempUri != null) {
            File file1 = new MyPhotoUtils(BusinessCheckingInInfoActivity.this, securityId).createCropFile();
            Uri cropPhotoUri = Uri.fromFile(file1);
            Log.i("startCropPhoto", "图片裁剪的uri = " + cropPhotoUri);
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url = getPath(BusinessCheckingInInfoActivity.this, tempUri);
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
            Toast.makeText(BusinessCheckingInInfoActivity.this, "拍照失败，请重试！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(BusinessCheckingInInfoActivity.this, "必须同意所有权限才能操作哦！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    //用户同意授权
                    createPhotoPopupwindow();//调用相机
                } else {
                    //用户拒绝授权
                    Toast.makeText(BusinessCheckingInInfoActivity.this, "您拒绝了授权！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }
}
