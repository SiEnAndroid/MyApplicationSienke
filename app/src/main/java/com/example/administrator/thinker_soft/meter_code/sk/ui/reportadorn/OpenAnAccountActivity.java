package com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.MeterDataTransferActivity;
import com.example.administrator.thinker_soft.meter_code.model.UploadResultListItem;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.GridviewImageAdapters;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.ReleaseAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.SecurityAbnormalAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NatureNameBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.PhotoPathBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ProjectTypeBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityChecksBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallbackStringListener;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.FilePickerActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.PickerManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.adapter.FilePickerShowAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.adapter.OnFileItemClickListener;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.model.FileEntity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.model.FileType;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.util.OpenFile;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.CommonUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.HttpUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.JsonAnalyUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PermissionUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PictureUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.BottomNatureNameDatePopwindow;
import com.example.administrator.thinker_soft.meter_code.sk.widget.BottomProjectDatePopwindow;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.meter_code.sk.widget.MyPopupWind;
import com.example.administrator.thinker_soft.mode.MyPhotoUtils;
import com.example.administrator.thinker_soft.mode.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 111
 */
public class OpenAnAccountActivity extends AppCompatActivity implements View.OnClickListener, FilePickerShowAdapter.OnItemListener {

    private ImageView back;
    //裁剪的图片路径集合
    private ArrayList<PhotoPathBean> cropPathLists = new ArrayList<>();
    //默认值
    private int currentPosition = 0;
    //自定义弹出框
    private MyPopupWind myPopupWind;
    //相册
    private final int TAKE_ALBUM = 156;
    //拍照
    private static final int TAKE_PHOTO = 288;
    //查看
    protected static final int CHECK_PICTURE = 199;
    private CardView notice_type;
    private EditText businessName, projectType, userNatureName, phoneNumber, contact, idNumber, address, note, apCount;
    private SharedPreferences sharedPreferences_login;
    // private TextView tvRepair;
    private ArrayList<String> fileNameList = new ArrayList<>();
    private MyPhotoUtils photoUtils;
    private File imgFile;
    private LoadingView loadingView;
    private BottomNatureNameDatePopwindow bottomDateTs;
    private String userId, NatureNameId, ProjectTypeId, cropPhotoPath;
    private LinearLayout uploadAFile, llPictures;
    private BottomProjectDatePopwindow bottomDateTs1;
    //路径集合
    private ArrayList<String> listPath = new ArrayList<>();

    private static final int RESULT_LOAD_FILE = 0X02;
    /**
     * 文件选择列表
     */
    private RecyclerView mRecyclerView;
    //private ReleaseAdapter mAdapter;
    private FilePickerShowAdapter mAdapter;

    /**
     * 图片类型
     */
    private String[] type = new String[]{"png", "jpg", "jpeg", "gif"};
    private Handler handler = new Handler();

    /**
     * 返回图片名称和路径
     */
    private Map<String, String> mapPath = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_an_account);
        bindView();
        defaultSetting();
        setViewClickListener();
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        notice_type = (CardView) findViewById(R.id.notice_type);
        businessName = (EditText) findViewById(R.id.business_name);
        projectType = (EditText) findViewById(R.id.project_type);
        userNatureName = (EditText) findViewById(R.id.user_nature_name);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        contact = (EditText) findViewById(R.id.contact);
        idNumber = (EditText) findViewById(R.id.id_number);
        address = (EditText) findViewById(R.id.address);
        note = (EditText) findViewById(R.id.note);
        // tvRepair = (TextView) findViewById(R.id.tv_repair);

        uploadAFile = (LinearLayout) findViewById(R.id.Upload_a_file);
        llPictures = (LinearLayout) findViewById(R.id.ll_pictures);
        apCount = (EditText) findViewById(R.id.ap_count);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


    }

    private void defaultSetting() {
//        uploadAFile.setVisibility(View.GONE);
//        llPictures.setVisibility(View.GONE);
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        userId = sharedPreferences_login.getString("userId", "");
        // 初始化图片
        //  initImg();
        //设置图片列表
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new FilePickerShowAdapter(this, new ArrayList<FileEntity>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemListener(this);
        mAdapter.setOnItemClickListener(new OnFileItemClickListener() {
            @Override
            public void click(int position) {
                startActivity(Intent.createChooser(OpenFile.openFile(PickerManager.getInstance().files.get(position).getPath(), getApplicationContext()), "选择程序"));
            }
        });
        //mAdapter = new ReleaseAdapter(OpenAnAccountActivity.this, mList);
        //  mRecyclerView.setAdapter(mAdapter);
        //  mAdapter.setOnItemListener(this);

    }

    private void setViewClickListener() {
        back.setOnClickListener(this);
        notice_type.setOnClickListener(this);
        projectType.setOnClickListener(this);
        userNatureName.setOnClickListener(this);
        //tvRepair.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.notice_type:
//                mapPath.put("20180911_165814_02601225930", "D:/SMDemo/images/2018-09/20180911_165814_02601225930.jpg");
//                mapPath.put("20180911_160251_02601225929", "D:/SMDemo/images/2018-09/20180911_160251_02601225929.jpg");
//                Set<Map.Entry<String, String>> set = mapPath.entrySet();
//                StringBuffer fileKey=new StringBuffer();
//                StringBuffer fileValue=new StringBuffer();
//                
//                for (Map.Entry<String, String> me : set) {
//                    // 根据键值对对象获取键和值          
//                    String key = me.getKey();
//                    String value = me.getValue();
//                    fileKey.append(key).append(",");
//                    fileValue.append(value).append(",");
//                    System.out.println(key + "---" + value);
//                }
//                //网络请求 创建
//                networkCreate(fileKey.toString(),fileValue.toString());
                if (!TextUtils.isEmpty(businessName.getText().toString().trim()) && !TextUtils.isEmpty(projectType.getText().toString().trim()) && !TextUtils.isEmpty(userNatureName.getText().toString().trim())&&
                        !TextUtils.isEmpty(phoneNumber.getText().toString().trim())  && !TextUtils.isEmpty(contact.getText().toString().trim()) && !TextUtils.isEmpty(address.getText().toString().trim())) {
                    if (mAdapter.getmDataList().size()>0){
                        upData();
                    }else{
                        //网络请求 创建
                        networkCreate(null, null); 
                    }
                
                    
                }else {
                ToastUtil.showShort(this, R.string.new_toast);
                }
                break;
            case R.id.project_type:
                //网络请求 项目类型名称
                networkProjectType();
                break;
            case R.id.user_nature_name:
                //网络请求 用户性质名称
                networkNatureName();
                break;
            //   case R.id.tv_repair:
                 // 点击上传文件
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivityForResult(intent,1);
//                Intent intents = new Intent(getApplicationContext(), FileSelectActivity.class);
//                startActivityForResult(intents, RESULT_LOAD_FILE);
//                break;
            default:
                break;
        }
    }

    /**
     * 通过手机选择文件
     */
    public void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件上传"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "请安装一个文件管理器.", Toast.LENGTH_SHORT).show();

        }
    }


    /**
     * 关闭进度
     */
    private void loadDiss() {
        if (loadingView != null) {
            loadingView.dismiss();
        }
    }

    /**
     * 上传文件
     */
    private void upData() {
        loadingView = new LoadingView(OpenAnAccountActivity.this, R.style.LoadingDialog, "上传中...请稍后");
        loadingView.show();
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                String httpUrl = new StringBuffer().append(SkUrl.SkHttp(OpenAnAccountActivity.this)).append("uploadOffice.do").toString();
                HttpUtils.sendFilePosts(OpenAnAccountActivity.this, httpUrl, null, getPhotoData(), new HttpCallbackStringListener() {
                    @Override
                    public void onFinish(final String response) {
                        //成功
                        Log.i("login_result=========>", response);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //mapPath=new HashMap<String,String>();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg=jsonObject.optString("msg");
                                    if("上传成功".equals(msg)){
                                        JSONObject jsonObject1=jsonObject.getJSONObject("path");
                                        StringBuffer nameBuffer = new StringBuffer();
                                        Map<String, File> photoData = getPhotoData();
                                        for (int i = 0; i < photoData.size(); i++) {
                                            String name = photoData.get("file" + i).getName();
                                            nameBuffer.append(name).append(",");
                                            Log.i("文件名=========>:", name);
                                        }

                                        mapPath = JsonAnalyUtil.analyszeGetPath(jsonObject1);
//                                      //网络请求 创建
//                                      networkCreate();
                                        if(mapPath.size()>0) {
                                            Set<Map.Entry<String, String>> set = mapPath.entrySet();

                                            StringBuffer fileKey = new StringBuffer();
                                            StringBuffer fileValue = new StringBuffer();

                                            for (Map.Entry<String, String> me : set) {
                                                // 根据键值对对象获取键和值
                                                String key = me.getKey();
                                                String value = me.getValue();
                                                fileKey.append(key).append(",");
                                                fileValue.append(value).append(",");
                                                System.out.println(key + "---" + value);
                                            }
                                            //网络请求 创建
                                            networkCreate(nameBuffer.toString().substring(0, nameBuffer.length() - 1), fileValue.toString().substring(0, fileValue.length() - 1));
                                        }else {
                                            loadDiss();
                                        }
                                    }else {
                                        loadDiss();
                                        Toast.makeText(OpenAnAccountActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(OpenAnAccountActivity.this, "数据解析异常", Toast.LENGTH_SHORT).show();
                                    loadDiss();
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(Exception e) {
                        //  isfaild = true;
                        Log.i("login_result=========>", e + "");
                        loadDiss();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(OpenAnAccountActivity.this, "网络不给力！");
                            }
                        });
                    }
                });

            }
        });

    }

    /**
     * 读取本地的图片数据，并上传服务器
     *
     * @return
     */
    public Map<String, File> getPhotoData() {

        ArrayList<FileEntity> fileEntityArrayList = mAdapter.getmDataList();
        Map<String, File> fileMap = new HashMap<String, File>();

        File file = null;
        int i = 0;
        for (FileEntity entiy : fileEntityArrayList) {
            file = new File(entiy.getPath());
            fileMap.put("file" + i, file);
            i++;
        }
        Log.i("getUserData=>", "上传的照片流为：" + fileMap.size() + mAdapter.getmDataList().size());

        return fileMap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    //拍照
                    saveFile(PictureUtil.compressSizeImage(cropPhotoPath), cropPhotoPath,"删");
//                    FileEntity fileEntityp=new FileEntity(0,"IMG",cropPhotoPath);
//                    ArrayList<FileEntity> files = PickerManager.getInstance().files;
//                    files.add(fileEntityp);
//                    mAdapter.setmDataList(files);

                    FileEntity fileEntity = new FileEntity(cropPhotoPath, new File(cropPhotoPath), false);
                    fileEntity.setFileType(new FileType("IMG", type, 0));
                    ArrayList<FileEntity> file = PickerManager.getInstance().files;
                    file.add(fileEntity);
                    mAdapter.setmDataList(file);
                    Log.e("返回=", new Gson().toJson(PickerManager.getInstance().files));
                    // cropPathLists.add(new PhotoPathBean(cropPhotoPath, "选择类型", ""));
                    break;

                case TAKE_ALBUM:
                    //相册选择
                    Uri selectedImage = data.getData();
                    //获取系统返回的照片的Uri
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    //从系统表中查询指定Uri对应的照片
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    Log.e("多张", cursor.getColumnCount() + picturePath);
                    cursor.close();

                    saveFile(PictureUtil.compressSizeImage(picturePath), picturePath,null);

                    FileEntity fileEntitys = new FileEntity(picturePath, new File(picturePath), false);
                    fileEntitys.setFileType(new FileType("IMG", type, 0));
                    ArrayList<FileEntity> files = PickerManager.getInstance().files;
                    files.add(fileEntitys);

                    mAdapter.setmDataList(files);
                    Log.e("返回=", new Gson().toJson(PickerManager.getInstance().files));
                    //cropPathLists.add(new PhotoPathBean(picturePath, "选择类型", ""));

                    break;

                case RESULT_LOAD_FILE:
                    //从文件中选择
                    Log.e("返回=", new Gson().toJson(PickerManager.getInstance().files));
                    mAdapter.setmDataList(PickerManager.getInstance().files);


                    break;
                default:
                    break;
            }

        }
    }


    /**
     * 保存压缩后的图片
     *
     * @param bitmap
     * @param filePath2
     * @throws IOException
     */
    private void saveFile(Bitmap bitmap, String filePath2,String tag) {
        // TODO Auto-generated method stub
        File testFile = new File(filePath2);
        if (tag!=null){
            if (testFile.exists()) {
                testFile.delete();
            }   
        }
        File myCaptureFile = new File(filePath2);
        System.out.println("------filePath2==" + filePath2);

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 100表示不进行压缩，70表示压缩率为30%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);

        try {
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            String[] projection = { "_data" };
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it  Or Log it.
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * 全屏弹出
     *
     * @param view
     */

    public void showAll(View view) {
        if (myPopupWind != null && myPopupWind.isShowing()) {
            return;
        }
        View upView = LayoutInflater.from(this).inflate(R.layout.view_popup_file_window, null);
        //测量View的宽高
        CommonUtil.measureWidthAndHeight(upView);
        myPopupWind = new MyPopupWind.Builder(this)
                .setView(R.layout.view_popup_file_window)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.mypopwindow_anim_style)
                .setViewOnclickListener(new MyPopupWind.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        //拍照
                        Button takePhoto = (Button) view.findViewById(R.id.btn_take_photo);
                        //从相册
                        Button albumPhoto = (Button) view.findViewById(R.id.btn_album_photo);
                        //取消
                        Button takeCancel = (Button) view.findViewById(R.id.btn_cancel);
                        //从文件中选择

                        Button selectFlie = (Button) view.findViewById(R.id.btn_select_file);

                        takePhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (Tools.hasSdcard()) {
                                    openCamera();//拍照
                                } else {
                                    Toast.makeText(OpenAnAccountActivity.this, "没有SD卡哦，不能拍照！", Toast.LENGTH_SHORT).show();
                                }
                                if (myPopupWind != null) {
                                    myPopupWind.dismiss();
                                }
                            }
                        });
                        takeCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (myPopupWind != null) {
                                    myPopupWind.dismiss();
                                }
                            }
                        });
                        albumPhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //启动相册
                                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(albumIntent, TAKE_ALBUM);
                                if (myPopupWind != null) {
                                    myPopupWind.dismiss();
                                }

                            }
                        });

                        selectFlie.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(OpenAnAccountActivity.this, FilePickerActivity.class);
                                startActivityForResult(intent, RESULT_LOAD_FILE);
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

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = OpenAnAccountActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            OpenAnAccountActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            OpenAnAccountActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        OpenAnAccountActivity.this.getWindow().setAttributes(lp);
    }


    /* */

    /**
     * 打开相机获取图片
     */
    public void openCamera() {
        // File file = new MyPhotoUtils(MeterUserDetailActivity.this, userID).createCropFile();
        photoUtils = new MyPhotoUtils(OpenAnAccountActivity.this, userId);
        Log.i("photoUtils===>", "图片" + photoUtils);
        cropPhotoPath = photoUtils.generateImgePath();
        imgFile = new File(cropPhotoPath);
        Uri tempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            tempUri = FileProvider.getUriForFile(OpenAnAccountActivity.this, "com.example.administrator.thinker_soft.fileprovider", imgFile);
            //第二参数是在manifest.xml定义 provider的authorities属性
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

    @Override
    public void OnFileSelect(View view, int position) {
        //   
//        if ((int)view.getTag()==6){
//            Intent intent = new Intent(getApplicationContext(), FileOpenActivity.class);
//            intent.putExtra("path",mAdapter.getmList().get(position));
//            startActivity(intent); 
//        }else {
        //  Intent intents = new Intent(getApplicationContext(), FileSelectActivity.class);
//            startActivityForResult(intents, RESULT_LOAD_FILE);
        // }
        //检查权限
        if (PermissionUtil.requestPermissions(OpenAnAccountActivity.this)) {
            showAll(view);
        }

    }


    private void networkNatureName() {
        loadingView = new LoadingView(OpenAnAccountActivity.this, R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();

        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(OpenAnAccountActivity.this)).append("GetPropertiesList").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.POST)
                .encode("UTF-8")
                .tag("aerate")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/json;charset=UTF-8")
                .string("");
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e("MeterRemarkActivity", "onComplete/response:" + response.getBody());
                Log.e("MeterRemarkActivity", "onComplete/response: content type=" + response.getContentType());
                loadDiss();
                View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
                try {
                    JSONObject jsonObject = new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    if (Integer.valueOf(code) == 1) {
                        String data = jsonObject.optString("Data");
                        Log.e("PGL", "===data:" + data);
                        List<NatureNameBean> mlist = new Gson().fromJson(data, new TypeToken<List<NatureNameBean>>() {
                        }.getType());

                        bottomDateTs = new BottomNatureNameDatePopwindow(OpenAnAccountActivity.this, mlist.get(0).getC_PROPERTIES_NAME(), (ArrayList<NatureNameBean>) mlist);
                        bottomDateTs.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                        bottomDateTs.setBirthdayListener(new BottomNatureNameDatePopwindow.OnBirthListener() {
                            @Override
                            public void onClick(String title, String ids) {
                                userNatureName.setText(title);
                                userNatureName.setSelection(title.length());
                                NatureNameId = ids;
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                loadDiss();
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
                //空数据
            }
        }).executeAsync();
    }


    private void networkProjectType() {
        loadingView = new LoadingView(OpenAnAccountActivity.this, R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();

        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(OpenAnAccountActivity.this)).append("GetProjectType").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.POST)
                .encode("UTF-8")
                .tag("aerate")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/json;charset=UTF-8")
                .string("");
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e("MeterRemarkActivity", "onComplete/response:" + response.getBody());
                Log.e("MeterRemarkActivity", "onComplete/response: content type=" + response.getContentType());
                loadDiss();
                View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
                try {
                    JSONObject jsonObject = new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    if (Integer.valueOf(code) == 1) {
                        String data = jsonObject.optString("Data");
                        List<ProjectTypeBean> mlist = new Gson().fromJson(data, new TypeToken<List<ProjectTypeBean>>() {
                        }.getType());
                        bottomDateTs1 = new BottomProjectDatePopwindow(OpenAnAccountActivity.this, mlist.get(0).getC_PROJECT_TYPE_NAME(), (ArrayList<ProjectTypeBean>) mlist);
                        bottomDateTs1.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                        bottomDateTs1.setBirthdayListener(new BottomProjectDatePopwindow.OnBirthListener() {
                            @Override
                            public void onClick(String title, String ids) {
                                projectType.setText(title);
                                projectType.setSelection(title.length());
                                ProjectTypeId = ids;
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                loadDiss();
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
                //空数据
            }
        }).executeAsync();
    }

    /**
     * @param fileName
     * @param filePaths
     */
    private void networkCreate(String fileName, String filePaths) {

            JSONObject object = new JSONObject();
            try {
                //conditionId
                object.put("businessName", businessName.getText().toString().trim());
                object.put("proTypeId", ProjectTypeId);
                object.put("proTypeName", projectType.getText().toString().trim());
                object.put("propertiesId", NatureNameId);
                object.put("propertiesName", userNatureName.getText().toString().trim());
                if (apCount.getText() == null) {
                    object.put("apCount", "1");
                } else {
                    try {
                        Integer integer = Integer.valueOf(apCount.getText().toString().trim());
                        object.put("apCount", apCount.getText().toString().trim());
                    } catch (Throwable e) {
                        e.printStackTrace();
                        ToastUtil.showShort(this, R.string.new_ap_Count);
                        apCount.setText("");
                    }
                }
                object.put("apPhone", phoneNumber.getText().toString().trim());
                object.put("agentName", contact.getText().toString().trim());
                object.put("resAddr", address.getText().toString().trim());
                object.put("opUserId", userId);
                object.put("agentCard", idNumber.getText().toString().trim());
                object.put("apRemark", note.getText().toString().trim());
                object.put("fileNames", fileName);
                object.put("filePaths",filePaths);
                Log.e("pgl", "=上传参数===" + object.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String httpUrl = new StringBuffer().append(SkUrl.YSHttp(this)).append("AddApplication").toString();
            Request.Builder build = new Request.Builder()
                    .url(httpUrl)
                    .method(HttpMethod.POST)
                    .encode("UTF-8")
                    .tag("aerate")
                    .headers("User-Agent", "sk-1.0")
                    .headers("Content-Type", "application/json;charset=UTF-8")
                    .string(object.toString());
            Request.newRequest(build, new HttpCallback() {
                @Override
                public void onComplete(Response response) {
                    Log.e("MeterRemarkActivity", "onComplete/response:" + response.getBody());
                    Log.e("MeterRemarkActivity", "onComplete/response: content type=" + response.getContentType());
                    loadDiss();
                    View parent = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
                    try {
                        JSONObject jsonObject = new JSONObject(response.getBody());
                        String code = jsonObject.optString("Code");
                        String data = jsonObject.optString("Msg");
                        if (Integer.valueOf(code) == 1) {
                            Toast.makeText(OpenAnAccountActivity.this, data, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(OpenAnAccountActivity.this, data, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(OpenAnAccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    loadDiss();
                    Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
                }
            }).executeAsync();
       
    }


}
