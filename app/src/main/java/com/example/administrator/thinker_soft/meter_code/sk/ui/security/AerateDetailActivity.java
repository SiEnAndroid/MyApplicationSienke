package com.example.administrator.thinker_soft.meter_code.sk.ui.security;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.activity.UserDetailInfoActivity;
import com.example.administrator.thinker_soft.Security_check.adapter.PopupwindowListAdapter;
import com.example.administrator.thinker_soft.Security_check.model.PopupwindowListItem;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.GridviewImageAdapters;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AetateBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AetateDetailPrame;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NewsReviseParams;
import com.example.administrator.thinker_soft.meter_code.sk.bean.PhotoPathBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityChecksBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.observer.ObserverManager;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.PhotoGalleryActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.CommonUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.DateFormatUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.FileSizeUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PermissionUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PhotoUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PictureUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.meter_code.sk.widget.MyPopupWind;
import com.example.administrator.thinker_soft.meter_code.sk.widget.UIHandler;
import com.example.administrator.thinker_soft.mode.HttpUtils;
import com.example.administrator.thinker_soft.mode.MyGridview;
import com.example.administrator.thinker_soft.mode.MyPhotoUtils;
import com.example.administrator.thinker_soft.mode.Tools;
import com.example.administrator.thinker_soft.mode.photo.ImageUtil;
import com.example.administrator.thinker_soft.mode.photo.WaterMask;
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
import butterknife.OnClick;



/**
 * 通气安检详情
 * Created by Administrator on 2018/8/2.
 */

public class AerateDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private AetateBean.ListBean aetate;//安检信息
    private PopupwindowListAdapter popupwindowListAdapter;
    private List<PopupwindowListItem> securityCaseItemList;//安检情况
    private String typeID;//气表方向id
    private String company_id, companyName;//公司id
    //存储
    private SharedPreferences sharedPreferences_login;

    //图片适配器
    private GridviewImageAdapters iamgeAdapter;
    //拍照
    private static final int TAKE_PHOTO = 288;
    //查看
    protected static final int CHECK_PICTURE = 299;

    //裁剪的图片路径集合
    private ArrayList<PhotoPathBean> cropPathLists = new ArrayList<>();
    private MyPhotoUtils photoUtils;//路径
    //拍照图片路径地址
    private String cropPhotoPath;
    private String userID = "1";
    private UIMyHandler myHandler = new UIMyHandler(this);
    private String userNameEt,userPhoeEt,meterNumberEt,AspectTv;

    /**
     * 加载进度
     */
    private LoadingView loadingView;
    private MyPopupWind myPopupWind;
    @BindView(R.id.tv_title)
    TextView title;//标题
    @BindView(R.id.clear)
    TextView clear;
    @BindView(R.id.security_check_case)
    TextView checkCase;//安检情况
    @BindView(R.id.user_name)
    EditText userName;//用户名
    @BindView(R.id.tv_userNumber)
    TextView tvUserNumber;//编号
    @BindView(R.id.ed_userMeterNumber)
    EditText edUserMeterNumber;//气表编号
    @BindView(R.id.tv_userZone)
    TextView tvUserZone;//分区
    @BindView(R.id.tv_edPhone)
    EditText tvEdPhone;//手机号
    @BindView(R.id.user_address)
    EditText userAddress;//地址
    @BindView(R.id.et_remark)
    EditText remark;//备注
    @BindView(R.id.gridView)
    MyGridview myGridview;//图片

    @BindView(R.id.rb_security_rb1)
    RadioButton radioButtonY;//合格
    @BindView(R.id.rb_security_rb2)
    RadioButton radioButtonN;//不合格
    @BindView(R.id.tv_aspect)
    TextView tvAspect;//气表方向

    @Override
    protected int getContentViewID() {
        return R.layout.activity_aerate_detail;
    }

    @Override
    protected void initView() {
        title.setText("通气安检");
        clear.setText("编辑");
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        // meterReaderName.setText(sharedPreferences_login.getString("user_name",""));
         company_id=sharedPreferences_login.getInt("company_id",0)+"";
        companyName = sharedPreferences_login.getString("user_name", "");
//        companyName = sharedPreferences_login.getString("user_name", "");
//        company_id = "5";

        getIntentData();
       // securityCaseItemList = DBManage.getInstance(AerateDetailActivity.this).getSecurityCheckCase();

        setFocusable(tvEdPhone, false);
        setFocusable(userAddress, false);
        setFocusable(edUserMeterNumber, false);
        setFocusable(userName, false);
        tvAspect.setClickable(false);
        setData();
        //初始化
        iamgeAdapter = new GridviewImageAdapters(AerateDetailActivity.this, cropPathLists);
        //设置adapter
        myGridview.setAdapter(iamgeAdapter);
        //监听
        myGridview.setOnItemClickListener(this);
    }


    /**
     * 获取Bundl数据
     */
    private void getIntentData() {
      /*  Intent intent = getIntent();
        // 实例化一个Bundle
        Bundle bundle = intent.getExtras();
        //获取数据
        aetate = (AetateBean.ListBean) bundle.getSerializable("aetate");*/

        aetate =(AetateBean.ListBean) getIntent().getSerializableExtra("aetate");
    }


    /**
     * 设置用户数据
     */
    private void setData() {
        if (aetate != null) {
            userName.setText(aetate.getYhmc());
            tvUserNumber.setText(aetate.getYhbh());
            edUserMeterNumber.setText(aetate.getQbbh());
            tvUserZone.setText(aetate.getFq());
            tvEdPhone.setText(aetate.getLidh());
            userAddress.setText(aetate.getYhdz());
            userID = aetate.getYhbh();
            tvAspect.setText(aetate.getJqfx());
            //进气类型
            if (aetate.getJqfx().equals("无")){
                typeID="0";
            }else if (aetate.getJqfx().equals("左进右出")){
                typeID="1";
            }else {
                typeID="2";
            }
            //附默认值
            userNameEt=aetate.getYhmc();
            userPhoeEt=tvEdPhone.getText().toString().trim();
            meterNumberEt=aetate.getQbbh();
            AspectTv=aetate.getJqfx();
        }
    }

    @OnClick({R.id.back, R.id.cv_condition, R.id.btn_save,R.id.clear,R.id.tv_aspect})
    public void OnclickDetail(View view) {
        switch (view.getId()) {
            case R.id.back:

                finish();
                break;
            case R.id.cv_condition:
                //安检情况
                //  showAll();
                break;
            case R.id.btn_save:
                //上传
                createSaveWindow();

                break;
            case R.id.clear:
                //编辑
                if (clear.getText().toString().trim().equals("编辑")){
                    clear.setText("完成");
                    setFocusable(userName, true);
                    setFocusable(edUserMeterNumber, true);
                    setFocusable(tvEdPhone, true);
                    tvAspect.setClickable(true);
                    userName.setBackgroundColor(Color.parseColor("#efeff4"));
                    edUserMeterNumber.setBackgroundColor(Color.parseColor("#efeff4"));
                    tvEdPhone.setBackgroundColor(Color.parseColor("#efeff4"));
                    tvAspect.setBackgroundColor(Color.parseColor("#efeff4"));

                }else {
                    clear.setText("编辑");
                    setFocusable(tvEdPhone, false);
                    setFocusable(edUserMeterNumber, false);
                    setFocusable(userName, false);
                    tvAspect.setClickable(false);
                    userName.setBackgroundColor(Color.WHITE);
                    edUserMeterNumber.setBackgroundColor(Color.WHITE);
                    tvEdPhone.setBackgroundColor(Color.WHITE);
                    tvAspect.setBackgroundColor(Color.WHITE);
                    //修改信息=[电话号码由13647666071  改为13647666071][气表编号由13647666071  改为30330785]&c_meter_number=30330785&n_meter_direction=0&c_user_phone=13647666071
                    String name=userName.getText().toString().trim();
                    String phone=tvEdPhone.getText().toString().trim();
                    String meterNumber=edUserMeterNumber.getText().toString().trim();
                    String aspect=tvAspect.getText().toString().trim();
                    if (!userNameEt.equals(name)||!userPhoeEt.equals(phone)
                            ||!meterNumberEt.equals(meterNumber)||!aspect.equals(AspectTv)){
                        NewsReviseParams params=new NewsReviseParams();
                        params.setC_meter_number(meterNumber);
                        params.setC_user_id(userID);
                        params.setC_user_phone(phone);
                        params.setC_user_name(name);
                        params.setN_meter_direction(typeID);
                        params.setN_log_operator_id(company_id);
                     String log=new StringBuffer().append(!userNameEt.equals(name)?userID+"[:用户名由"+userNameEt+"改为"+name+"]":"")
                             .append(!aspect.equals(AspectTv)?"[气表方向由"+AspectTv+"改为"+aspect+"]":"")
                             .append(!phone.equals(userPhoeEt)?"[电话号码由"+userPhoeEt+"改为"+phone+"]":"")
                             .append(!meterNumber.equals(meterNumberEt)?"[气表编号由"+meterNumberEt+"改为"+meterNumber+"]":"")
                             .toString();
                        params.setC_log(log);
                        //提交
                        ReviseRequest(params);
                    }

                }
                break;
            case R.id.tv_aspect:
                //进去方向
                showAll();
                break;
            default:
                break;
        }


    }

    private void upload() {

        String strRemark = remark.getText().toString().trim();
        AetateDetailPrame prame = new AetateDetailPrame();
        prame.setC_user_id(tvUserNumber.getText().toString());//用户编号
        prame.setC_safety_inspection_member(companyName);      //安检员
        prame.setN_company_id(company_id);  //公司id
        prame.setC_safety_plan_member(companyName);//操作员
        if (radioButtonY.isChecked()){
            prame.setC_safety_securitycontent("1"); //安检情况
        }else {
            prame.setC_safety_securitycontent("2"); //安检情况不合格
        }

        prame.setC_safety_remark(strRemark);   //安检备注
        prame.setD_safety_inspection_date(DateFormatUtil.getCurrentTimes()); //安检时间
        //提交
        httoAerateDetailRemark(prame);
        //radioButtonY.isChecked()
        //上传路径
        //String httpUrl=new StringBuffer().append(SkUrl.SkHttp(AerateDetailActivity.this)).append("updateInspection.do").toString();
//              HttpUtils httpUtils=new HttpUtils();
//              String result=httpUtils.postData(httpUrl, map1, fileMap);
//              Log.i("httpUtils=>", "上传的地址为：" + httpUrl);
    }

    /**
     * 限制输入
     *
     * @param editText
     * @param bt
     */
    private void setFocusable(EditText editText, boolean bt) {
        editText.setCursorVisible(bt);
        editText.setFocusable(bt);
        editText.setFocusableInTouchMode(bt);
        if (bt) {
            editText.requestFocus();
        }
    }

    /**
     * 弹出是否保存popupwindow
     */
    public void createSaveWindow() {
        LayoutInflater   inflater = LayoutInflater.from(AerateDetailActivity.this);
       View saveView = inflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        final PopupWindow popupWindow = new PopupWindow(saveView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        RadioButton     cancelRb = (RadioButton) saveView.findViewById(R.id.cancel_rb);
        RadioButton   saveRb = (RadioButton) saveView.findViewById(R.id.save_rb);
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

                upload();
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
        PopWindowUtil.backgroundAlpha(AerateDetailActivity.this, 0.6F);   //背景变暗
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                PopWindowUtil.backgroundAlpha(AerateDetailActivity.this, 1.0F);   //背景变暗
            }
        });
    }
    /**
     * 全屏弹出
     */

    public void showAll() {
        if (myPopupWind != null && myPopupWind.isShowing()){
            return;
        }
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
                       // tips.setText("请选择安检情况");
                        tips.setText("请选择气表方向");
                        noDataTip.setText("暂无数据");
                        confirm.setText("取消");
//                        if (securityCaseItemList != null) {
//                            popupwindowListAdapter = new PopupwindowListAdapter(AerateDetailActivity.this, securityCaseItemList, 0);
//                            listView.setAdapter(popupwindowListAdapter);
//                        }
                    //    0,'无', 1, '左进右出', 2, '右进左出'

                        securityCaseItemList=new ArrayList<PopupwindowListItem>();
                        PopupwindowListItem item=new PopupwindowListItem();
                        item.setItemId("0");
                        item.setItemName("无");
                        PopupwindowListItem item1=new PopupwindowListItem();
                        item1.setItemId("1");
                        item1.setItemName("左进右出");
                        PopupwindowListItem item2=new PopupwindowListItem();
                        item2.setItemId("2");
                        item2.setItemName("右进左出");
                        securityCaseItemList.add(item);
                        securityCaseItemList.add(item1);
                        securityCaseItemList.add(item2);

                        popupwindowListAdapter = new PopupwindowListAdapter(AerateDetailActivity.this, securityCaseItemList, 0);
                         listView.setAdapter(popupwindowListAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            //listview点击事件
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                PopupwindowListItem item = (PopupwindowListItem) popupwindowListAdapter.getItem(position);
                                 typeID = item.getItemId();
                                checkCase.setText(item.getItemName());

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
        PopWindowUtil.backgroundAlpha(AerateDetailActivity.this, 0.6F);   //背景变暗
        myPopupWind.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(AerateDetailActivity.this, 1.0F);   //背景变暗
            }
        });
        myPopupWind.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }


    /**
     * 提交通气安检
     */
    private void httoAerateDetailRemark(final AetateDetailPrame prame) {
        //加载
        loadingView = new LoadingView(AerateDetailActivity.this, R.style.LoadingDialog, "申请中...请稍后");
        loadingView.show();

        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                HashMap   maps = new HashMap<String, Object>();
                maps.put("c_safety_plan_member", prame.getC_safety_plan_member());
                maps.put("n_company_id", prame.getN_company_id());
                maps.put("c_user_id", prame.getC_user_id());
                maps.put("c_safety_inspection_member", prame.getC_safety_inspection_member());
                maps.put("c_safety_securitycontent", prame.getC_safety_securitycontent());
                maps.put("c_safety_remark", prame.getC_safety_remark());
                maps.put("d_safety_inspection_date", prame.getD_safety_inspection_date());
                String httpUrl = new StringBuffer().append(SkUrl.SkHttp(AerateDetailActivity.this)).append("insertSecurityCheck.do").toString();


                Map<String, File>    fileMap= getPhotoData();
                HttpUtils httpUtils=new HttpUtils();
                String result=httpUtils.postData(httpUrl, maps, fileMap);
                Message message=new Message();
                message.obj=result;
                message.what=1;
                myHandler.sendMessage(message);

            }

        });


    }


    private void loadDiss() {
        if (loadingView != null) {
            loadingView.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        myGridview.setClickable(false);
        int currentPosition = position;
        if (!iamgeAdapter.getDeleteShow() && (iamgeAdapter.getCount() - 1 != position)) {
            Intent intent = new Intent(AerateDetailActivity.this, PhotoGalleryActivity.class);
            intent.putExtra("currentPosition", currentPosition);
            intent.putExtra("newUserId", userID);
            Bundle bundle = new Bundle();
            bundle.putSerializable("cropPathLists", cropPathLists);
            intent.putExtras(bundle);
//          intent.putStringArrayListExtra("cropPathLists", cropPathLists);
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
                    if (PermissionUtil.requestPermissions(AerateDetailActivity.this)) {
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
                                    Toast.makeText(AerateDetailActivity.this, "没有SD卡哦，不能拍照！", Toast.LENGTH_SHORT).show();
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
        PopWindowUtil.backgroundAlpha(AerateDetailActivity.this, 0.6F);
        myPopupWind.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(AerateDetailActivity.this, 1.0F);
            }
        });
        myPopupWind.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 打开相机获取图片
     */
    public void openCamera() {
        photoUtils = new MyPhotoUtils(AerateDetailActivity.this, userID);
        cropPhotoPath = photoUtils.generateImgePath();

        File imgFile = new File(cropPhotoPath);
        Uri tempUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tempUri = FileProvider.getUriForFile(AerateDetailActivity.this, "com.example.administrator.thinker_soft.fileprovider", imgFile);//通过FileProvider创建一个content类型的Uri
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    //拍照
                    Log.i("startCropPhoto===>", "大小=" + FileSizeUtil.getFileOrFilesSize(cropPhotoPath));
                    try {
                        PhotoUtils.saveFile(PictureUtil.compressSizeImage(cropPhotoPath),cropPhotoPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    WaterMask.draw(AerateDetailActivity.this, bitmap, outputUri.getPath(), param);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outputUri));



                    PhotoPathBean bean = new PhotoPathBean();
                    bean.setCropPath(outputUri.getPath());
                    bean.setType("");
                    bean.setTypeId("");
                    cropPathLists.add(bean);

                    Log.i("startCropPhoto===>", "图片集合长度为：" + cropPathLists.size() + "路径为" + cropPhotoPath + "大小=" + FileSizeUtil.getFileOrFilesSize(cropPhotoPath));
                    iamgeAdapter.setGridviewImageList(cropPathLists);
                    break;

                case CHECK_PICTURE:
                    //查看
                    if (data != null) {
                        cropPathLists.clear();
                        //   cropPathLists = data.getStringArrayListExtra("cropPathLists_back");
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
     * 读取保存到本地的图片数据，并上传服务器
     *
     * @return
     */
    public Map<String, File> getPhotoData() {
        Map<String, File> fileMap = new HashMap<String, File>();
        File file = null;
        for (int i=0;i<cropPathLists.size();i++) {
            PhotoPathBean photoPathBean=cropPathLists.get(i);
            file = new File(photoPathBean.getCropPath());
            fileMap.put("file" + i+1, file);
        }
        Log.i("getUserData=>", "上传的照片流为：" + fileMap.size());

        return fileMap;
    }




    /**
     * 修改信息
     */
    private void ReviseRequest(NewsReviseParams param) {
        //加载
        loadingView = new LoadingView(AerateDetailActivity.this, R.style.LoadingDialog, "修改中...请稍后");
        loadingView.show();
        String httpUrl=new StringBuffer().append(SkUrl.SkHttp(AerateDetailActivity.this)).append("updateUserMsg.do").toString();
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
                        ToastUtil.showShort(AerateDetailActivity.this,"修改成功");
                        NewsReviseParams params=new NewsReviseParams();
                        String name=userName.getText().toString().trim();
                        String phone=tvEdPhone.getText().toString().trim();
                        String meterNumber=edUserMeterNumber.getText().toString().trim();
                        params.setC_user_name(name);
                        params.setC_user_phone(phone);
                        params.setC_meter_number(meterNumber);
                        ObserverManager.getInstance().notifyObserver(new Gson().toJson(params));

                    }else {
                        ToastUtil.showShort(AerateDetailActivity.this,"修改失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(AerateDetailActivity.this,"修改失败");
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
                loadDiss();
                ToastUtil.showShort(AerateDetailActivity.this,"修改失败");

            }
        }).executeAsync();
    }


    /**
     * Handler
     */
    private class UIMyHandler extends UIHandler<AerateDetailActivity> {

        public UIMyHandler(AerateDetailActivity cls) {
            super(cls);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AerateDetailActivity activity = ref.get();
            if (activity != null) {
                if (activity.isFinishing()){
                    return;
                }
                switch (msg.what) {
                    case 1:
                        activity.loadDiss();
                        String json= (String) msg.obj;
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String success = jsonObject.optString("status", "");
                            if (success.equals("success")) {
                                Intent intent = new Intent();
                                intent.putExtra("isUpload","成功");
                                setResult(RESULT_OK, intent);
                                finish();
                            }else {
                                ToastUtil.showShort(activity, "提交失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtil.showShort(activity, "提交失败");
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}




