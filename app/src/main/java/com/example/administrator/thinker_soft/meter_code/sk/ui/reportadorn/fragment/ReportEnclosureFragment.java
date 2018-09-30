package com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.downloadFileAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.DownloadFileBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportTsParame;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallbackStringListener;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseFragment;
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
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtil;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.DownloadProgressHandler;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.FileDownLoadObserver;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.HttpRetrofit;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.ProgressHelper;
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
import butterknife.BindView;
import butterknife.OnClick;

/**
 * File:ReportEnclosureFragment.class
 *
 * @author Administrator
 * @date 2018/8/29 11:04
 */

public class ReportEnclosureFragment extends BaseFragment implements FilePickerShowAdapter.OnItemListener {
    private static String ARG_PARAM = "fg_key";
    private FilePickerShowAdapter mAdapter;
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File
            .separator + "ThinkerSoft_APP/Enclosure";
    //private   String destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Thinker_APP/";

    /**
     * 弹出框
     */
    private MyPopupWind myPopupWind;
    /**
     * 相册
     */
    private final int TAKE_ALBUM = 156;
    /**
     * 拍照
     */
    private static final int TAKE_PHOTO = 288;
    /**
     * 选择文件
     */
    private static final int RESULT_LOAD_FILE = 0X02;
    /**
     * 进度条
     */
    private LoadingView loadingView;
    private String[] type = new String[]{"png", "jpg", "jpeg", "gif"};
    private MyPhotoUtils photoUtils;
    private String cropPhotoPath;
    private File imgFile;
    private SharedPreferences sharedPreferences_login;
    private String userId;
    private Handler handler = new Handler();
    private ReportTsParame prame;
    /**
     * 返回图片名称和路径
     */
    private Map<String, String> mapPath = new HashMap<>();
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.time_download_file)
    RecyclerView timeDownloadFile;
    private downloadFileAdapter adapter;
    private DownloadFileBean fileBean;
    private List<DownloadFileBean> mlist;

    @Override
    public void onLazyLoad() {
        //清空，默认
        PickerManager.getInstance().files.clear();
        downloadFile();
    }


    @Override
    protected int getContentViewID() {
        return R.layout.fragment_rt_enclosure;
    }

    @Override
    protected void initView() {
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        userId = sharedPreferences_login.getString("userId", "");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new FilePickerShowAdapter(getActivity(), new ArrayList<FileEntity>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemListener(this);
        mAdapter.setOnItemClickListener(new OnFileItemClickListener() {
            @Override
            public void click(int position) {
              //  startActivity(Intent.createChooser(OpenFile.openFile(PickerManager.getInstance().files.get(position).getPath(), getActivity().getApplicationContext()), "选择程序"));
            }
        });

        timeDownloadFile.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new downloadFileAdapter(getActivity());
        timeDownloadFile.setAdapter(adapter);
    }
    public static ReportEnclosureFragment newInstance(ReportTsParame parame) {
        ReportEnclosureFragment fragment = new ReportEnclosureFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM, parame);
        fragment.setArguments(bundle);
        return fragment;
    }
    @OnClick({R.id.btn_up})
    public void OnclickUP(View view) {
        if (mAdapter.getmDataList().size() > 0) {
            upDatas();
        } else {
            ToastUtil.showShort(getActivity(), "请选择图片");
        }
    }
    @Override
    public void OnFileSelect(View view, int position) {
        //检查权限
        if (PermissionUtil.requestPermissions(getActivity())) {
            showAll(view);
        }
    }

    private void downloadFile() {
        Bundle bundle = getArguments();
        prame = (ReportTsParame) bundle.getSerializable(ARG_PARAM);
        httpRequestDownloadFile();
    }

    /**
     * 请求下载的文件信息
     */
    private void httpRequestDownloadFile() {
        JSONObject object = new JSONObject();
        try {
            //业务id
            object.put("busId", prame.getBusId());
            Log.e("入参=", object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loadingView = new LoadingView(getActivity(), R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();

        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(getActivity())).append("GetPrcessFileList").toString();
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
                try {
                        JSONObject jsonObject = new JSONObject(response.getBody());
                        String code = jsonObject.optString("Code");
                        if (Integer.valueOf(code) == 1) {
                            String data = jsonObject.optString("Data");
                            mlist = new Gson().fromJson(data, new TypeToken<List<DownloadFileBean>>() {
                            }.getType());
                            adapter.clear();
                            adapter.addAll(mlist);
                            Log.e("pgl", "Data===:" + data);
                            adapter.setDeleteClickListener(new downloadFileAdapter.MeterDeleteClickListener() {
                                @Override
                                public void onDeleteClick(View view, int position) {
                                    //删除
                                //    adapter.clear();
    //                                adapter.notifyDataSetChanged();
                                    httpRequestDelete(mlist.get(position).getN_FILE_ID(),position);
                                }
                            });
                            adapter.setDownloadClickListener(new downloadFileAdapter.MeterDownloadClickListener() {
                                @Override
                                public void onDownloadClick(View view, int position) {
                                    Button btn= (Button) view;
                                    fileBean=mlist.get(position);
                                    //下载
                                    showProgressDialog(fileBean.getC_FILE_NAME(),fileBean.getC_FILE_PATH(),btn);
                                }
                            });
                            Log.e("pgl","数据加载成功");
                        }else {
                            
                            Log.e("pgl"," ====失败:"+jsonObject.optString("Msg"));
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("pgl", " ==== 异常:" + e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                loadDiss();
                Log.e("SecurityAerateActivity", "异常 onError:" + e.getMessage());
            }
        }).executeAsync();

    }

    /**
     * 上传文件
     */
    private void upDatas() {
        loadingView = new LoadingView(getActivity(), R.style.LoadingDialog, "上传中...请稍后");
        loadingView.show();
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
               String httpUrl = new StringBuffer().append(SkUrl.SkHttp(getActivity())).append("uploadOffice.do").toString();
                HttpUtils.sendFilePosts(getActivity(), httpUrl, null, getPhotoData(), new HttpCallbackStringListener() {
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
                                        StringBuffer nameBuffer = new StringBuffer();
                                        Map<String, File> photoData = getPhotoData();
                                        for (int i = 0; i < photoData.size(); i++) {
                                            String name = photoData.get("file" + i).getName();
                                            nameBuffer.append(name).append(",");
                                            Log.i("文件名=========>:", name);
                                        }

                                        JSONObject path = jsonObject.optJSONObject("path");
                                        mapPath = JsonAnalyUtil.analyszeGetPath(path);
//                                    //网络请求 创建
//                                    networkCreate();
                                        if (mapPath.size() > 0) {

                                            //清空，默认
                                            PickerManager.getInstance().files.clear();
                                            mAdapter.getmDataList().clear();
                                            mAdapter.notifyDataSetChanged();
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
//                                        httpRequestFile(fileKey.toString(),fileValue.toString());
                                            httpRequestFile(nameBuffer.toString().substring(0, nameBuffer.length() - 1), fileValue.toString().substring(0, fileValue.length() - 1));

                                        } else {
                                            loadDiss();
                                        }
                                    }else {
                                        loadDiss();
                                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    loadDiss();
                                    Toast.makeText(getContext(), "数据解析异常", Toast.LENGTH_SHORT).show();
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
                                ToastUtil.showShort(getActivity(), "网络不给力！");
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


    /**
     * 新增流程附件
     */
    private void httpRequestFile(String fileName, String filePath) {

        JSONObject object = new JSONObject();
        try {
            //业务id
            object.put("busId", prame.getBusId());
            //操作人id
            object.put("sysUId", userId);
            //文件服务地址
            object.put("filePath", filePath);
            //流程id
            object.put("proId", prame.getProId());
            //队列id
            object.put("queueId", prame.getQueId());
            //文件名称
            object.put("fileName", fileName);
            object.put("remark", "无");

            Log.e("入参=", object.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(getActivity())).append("AddProcessFile").toString();
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
                
                try {
                    JSONObject jsonObject = new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    String data = jsonObject.optString("Msg");
                    if (Integer.valueOf(code) == 1) {
                        Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
                        httpRequestDownloadFile();
                    } else {
                        Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(Throwable e) {
                loadDiss();
                Toast.makeText(getActivity(), "网络异常,连接失败", Toast.LENGTH_SHORT).show();
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
            }
        }).executeAsync();
    }

    /**
     * 流程附件删除
     */
    private void httpRequestDelete(String fileId, final int position) {

        JSONObject object = new JSONObject();
        try {
            //业务id
            object.put("fileId", fileId);

            Log.e("入参=", object.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(getActivity())).append("DelProcessFile").toString();
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
                try {
                    JSONObject jsonObject=new JSONObject(response.getBody());
                    int code = jsonObject.optInt("Code",0);
                    String msg = jsonObject.optString("Msg");
                    if (code==1){
                        String data = jsonObject.optString("Data");
                        Log.e("pgl","流程附件删除111 ==== 成功:"+position);
                 
                      //  httpRequestDownloadFile();
                        mlist.remove(position);
                         adapter.clear();
                        adapter.addAll(mlist);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        
                    }else {
                   
                        Log.e("pgl","流程附件删除222 ==== 失败:"+msg);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("流程附件删除ssss", "onError:" + e.getMessage());
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                loadDiss();
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
            }
        }).executeAsync();
    }

    private void loadDiss() {
        if (loadingView != null) {
            loadingView.dismiss();
        }

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
        View upView = LayoutInflater.from(getActivity()).inflate(R.layout.view_popup_file_window, null);
        //测量View的宽高
        CommonUtil.measureWidthAndHeight(upView);
        myPopupWind = new MyPopupWind.Builder(getActivity())
                .setView(R.layout.view_popup_file_window)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                //取值范围0.0f-1.0f 值越小越暗
                .setBackGroundLevel(0.5f)
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
                                    Toast.makeText(getActivity(), "没有SD卡哦，不能拍照！", Toast.LENGTH_SHORT).show();
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
                                Intent intent = new Intent(getActivity(), FilePickerActivity.class);
                                startActivityForResult(intent, RESULT_LOAD_FILE);
                                if (myPopupWind != null) {
                                    myPopupWind.dismiss();
                                }
                            }
                        });


                    }
                })
                .create();
        PopWindowUtil.backgroundAlpha(getActivity(), 0.6F);
        myPopupWind.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(getActivity(), 1.0F);
            }
        });
        myPopupWind.showAtLocation(getActivity().findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 打开相机获取图片
     */
    public void openCamera() {
        photoUtils = new MyPhotoUtils(getActivity(), userId);
        Log.i("photoUtils===>", "图片" + photoUtils);
        cropPhotoPath = photoUtils.generateImgePath();
        imgFile = new File(cropPhotoPath);
        Uri tempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            tempUri = FileProvider.getUriForFile(getActivity(), "com.example.administrator.thinker_soft.fileprovider", imgFile);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    //拍照
                    saveFile(PictureUtil.compressSizeImage(cropPhotoPath), cropPhotoPath);
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
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    //从系统表中查询指定Uri对应的照片
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    Log.e("多张", cursor.getColumnCount() + picturePath);
                    cursor.close();

                    saveFile(PictureUtil.compressSizeImage(picturePath), picturePath);

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
     * 下载图片
     * @param fileName
     * @param url
     * @param btn
     */
    private void showProgressDialog(String fileName, String url, final Button btn) {

        String replace = url.replace("\\","/");
        Log.e("pgl","======replace:"+replace);
        Log.e("pgl","======url:"+url);
        String httpUrl = new StringBuffer().append(SkUrl.SkHttp(getActivity())).append("downloadOffice.do?filePath=").append(replace).toString();

        //监听下载进度
        final ProgressDialog dialogs = new ProgressDialog(getActivity());
        dialogs.setProgressNumberFormat("%1d KB/%2d KB");
        dialogs.setTitle("文件下载");
        dialogs.setMessage("正在下载，请稍后...");
        dialogs.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogs.setCancelable(false);
        dialogs.show();
      //  dialogs.setMax(100);

        HttpRetrofit.getInstance("进度条").downloadFile(httpUrl, destFileDir, fileName, new FileDownLoadObserver<File>() {
            @Override
            public void onDownLoadSuccess(File file) {
                Log.e("zs", "请求成功"+file.getPath());
                dialogs.dismiss();
                String path=file.getPath();
                fileBean.setFilePath(path);
                btn.setText("打开");
               // 默认打开
                startActivity(Intent.createChooser(OpenFile.openFile(path, getActivity().getApplicationContext()), "选择程序"));

            }

            @Override
            public void onDownLoadFail(Throwable throwable) {
                Log.e("zs", "请求失败");
                dialogs.dismiss();
                ToastUtil.showShort(getActivity(),"服务器错误");

            }

            @Override
            public void onProgress(int progress, long total) {
                Log.e("zs", progress + "----" + total);
//               updateNotification(progress * 100 / total);
            }
        });

        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
                dialogs.setMax((int) (contentLength/1024));
                dialogs.setProgress((int) (bytesRead/1024));

            }
        });
//        dialogs.setProgress((int) (bytesRead / 100));

    }


    /**
     * 保存压缩后的图片
     *
     * @param bitmap
     * @param filePath2
     * @throws IOException
     */
    private void saveFile(Bitmap bitmap, String filePath2) {
        // TODO Auto-generated method stub
        File testFile = new File(filePath2);
        if (testFile.exists()) {
            testFile.delete();
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


}
