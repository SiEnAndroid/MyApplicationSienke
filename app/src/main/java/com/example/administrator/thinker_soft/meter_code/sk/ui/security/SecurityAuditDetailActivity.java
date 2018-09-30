package com.example.administrator.thinker_soft.meter_code.sk.ui.security;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.UserListviewItem;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.SecurityGridviewImageAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NoThroughBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityAuditBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityChecksBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityImageBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.SecurityAbnormalPhotoGalleryActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.mode.MyGridview;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 审核详情
 * Created by Administrator on 2018/8/6.
 */

public class SecurityAuditDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    //隐患
    @BindView(R.id.tv_yh_lx)
    TextView yhLx;
    //隐患类型
    @BindView(R.id.tv_sy_yh)
    TextView tvSyYh;
    @BindView(R.id.tv_why)
    TextView tvWhy;
    //隐患原因
    @BindView(R.id.cv_yh)
    CardView cvYh;
    /**
     * 备注
     */
    @BindView(R.id.tv_remark)
    TextView tvRemark;

    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_userId)
    TextView tvUserId;
    @BindView(R.id.tv_user_old_id)
    TextView tvUserOldId;
    @BindView(R.id.tv_area_name)
    TextView tvAreaName;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.tv_user_sy)
    TextView tvUserSy;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_sy_state)
    TextView tvSyState;

    @BindView(R.id.tv_title)
    TextView title;//标题
    @BindView(R.id.clear)
    TextView clear;
    private TextView tvDlsr;
    private LinearLayout lldlsrgridview;
    private MyGridview dlsrgridview;
    /**
     * 加载进度
     */
    private LoadingView loadingView;
    private ArrayList<String> cropIdLists;
    private SecurityGridviewImageAdapter iamgeAdapter;
    private int currentPosition;
    private CardView noThroughCardview ,throughCardview;
    private TextView notThroughTv , through_tv  ,tips;
    private LayoutInflater inflater;
    private View saveView;
    private PopupWindow popupWindow;
    private RadioButton cancelRb;
    private RadioButton saveRb;
    private SecurityAuditBean.AuditBean auditBean;
    private int position;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_audit_detail;
    }

    @Override
    protected void initView() {
        title.setText("审核详情");
        clear.setText("");

        tvDlsr = (TextView) findViewById(R.id.tv_dlsr);
        lldlsrgridview = (LinearLayout) findViewById(R.id.ll_dlsr_gridview);
        dlsrgridview = (MyGridview) findViewById(R.id.dlsr_gridview);
        noThroughCardview = (CardView) findViewById(R.id.not_through_cardview);
        notThroughTv = (TextView) findViewById(R.id.not_through_tv);
        throughCardview = (CardView) findViewById(R.id.through_cardview);
        through_tv = (TextView) findViewById(R.id.through_tv);
        getIntentData();
        setData();
        setViewClickListener();

        //  网络请求图片
        SecurityAbnormalInfoRequest();
    }

    private void setViewClickListener() {
        noThroughCardview.setOnClickListener(this);
        throughCardview.setOnClickListener(this);
    }

    /**
     * 获取Bundl数据
     */
    private void getIntentData() {
        auditBean =(SecurityAuditBean.AuditBean ) getIntent().getSerializableExtra("audit");
        position = getIntent().getIntExtra("position", 0);
    }

    @OnClick({R.id.back})
    public void OnclickAD(View view) {
        finish();
    }

    /**
     * 设置数据
     */
    private void setData() {
        if (auditBean != null) {
            tvUserName.setText(auditBean.getYhmc());
            tvUserId.setText(auditBean.getYhbh());
            tvUserOldId.setText(auditBean.getLbh());
            tvUserPhone.setText(auditBean.getLxdh());
            // tvAreaName.setText(auditBean.get);
            tvUserAddress.setText(auditBean.getYhdz());
            tvUserSy.setText(auditBean.getAjy());
            tvType.setText(auditBean.getAjlx());
            tvSyState.setText(auditBean.getAjzt());
            yhLx.setText(auditBean.getAqyhlx());
            tvRemark.setText(auditBean.getAjbz());
            tvWhy.setText(auditBean.getAqyhyy());
            if(auditBean.getAjzt().equals("安检不合格")){
                tvSyYh.setVisibility(View.VISIBLE);
                cvYh.setVisibility(View.VISIBLE);
            }else {
                tvSyYh.setVisibility(View.GONE);
                cvYh.setVisibility(View.GONE);
            }
            if ( auditBean.getIfthrough() !=null){
                noThroughCardview.setVisibility(View.GONE);
                throughCardview.setVisibility(View.GONE);
            }
        }
    }

    private void SecurityAbnormalInfoRequest() {
        //加载
        loadingView = new LoadingView(SecurityAuditDetailActivity.this, R.style.LoadingDialog, "加载图片中...请稍后");
        loadingView.show();
        final String httpUrl = new StringBuffer().append(SkUrl.SkHttp(SecurityAuditDetailActivity.this)).append("getSecurityImageDates.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .params("c_data_id",String.valueOf(auditBean.getAjbh()));
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
                        iamgeAdapter = new SecurityGridviewImageAdapter(SecurityAuditDetailActivity.this, cropIdLists);
                        dlsrgridview.setAdapter(iamgeAdapter);
                        dlsrgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                dlsrgridview.setClickable(false);
                                currentPosition = position;
                                Log.e("选择的项",position+"");
                                if (!iamgeAdapter.getDeleteShow() && (iamgeAdapter.getCount() - 1 != position)) {
                                    //  Intent intent = new Intent(MeterUserDetailActivity.this, MyPhotoGalleryActivity.class);
                                    Intent intent = new Intent(SecurityAuditDetailActivity.this, SecurityAbnormalPhotoGalleryActivity.class);
                                    intent.putExtra("currentPosition", currentPosition);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("cropIdLists",cropIdLists);
                                    intent.putExtras(bundle);
//             intent.putStringArrayListExtra("cropPathLists", cropPathLists);
                                    startActivity(intent);
                                    dlsrgridview.setClickable(true);
                                }
                            }
                        });
                        loadingView.dismiss();
                    } else {
                        loadingView.dismiss();
//                        Toast.makeText(SecurityAbnormalInfoActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        lldlsrgridview.setVisibility(View.GONE);
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
                Toast.makeText(SecurityAuditDetailActivity.this, "网络请求异常", Toast.LENGTH_SHORT).show();
                loadingView.dismiss();
            }

        }).executeAsync();
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId()){
            case R.id.not_through_cardview:
                createSaveWindow("你确定不通过吗");
                break;
            case R.id.through_cardview:
                createSaveWindow("你确定通过吗");
                break;
            default:
                break;
        }
    }

    //弹出是否保存popupwindow
    public void createSaveWindow(String str) {
        inflater = LayoutInflater.from(SecurityAuditDetailActivity.this);
        saveView = inflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        popupWindow = new PopupWindow(saveView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        cancelRb = (RadioButton) saveView.findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) saveView.findViewById(R.id.save_rb);
        tips = (TextView) saveView.findViewById(R.id.tips);
        tips.setText(str);
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
                if ("你确定通过吗".equals(tips.getText().toString()) ){
                    // 保存
                    save();
                }else if ("你确定不通过吗".equals(tips.getText().toString()) ){
                    // 请求网络
                    requestnetwork();
                }
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

    private void requestnetwork() {
        loadingView = new LoadingView(SecurityAuditDetailActivity.this, R.style.LoadingDialog, "上传中...请稍后");
        loadingView.show();

        /*
        *              n_safety_inspection_id  varchar2, --安检编号
                       n_safety_state   varchar2, --安检状态  这个不传 后台默认为0
                       c_safety_remark         varchar2, --备注
        */
        String httpUrl = new StringBuffer().append(SkUrl.SkHttp(SecurityAuditDetailActivity.this)).append("addSafityExamine.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .params("n_safety_inspection_id",String.valueOf(auditBean.getAjbh()))
                .params("c_safety_remark",auditBean.getAjbz());
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
              Log.e("pgl" ,"response==="+ response.getBody());
                NoThroughBean bean = new Gson().fromJson(response.getBody(), NoThroughBean.class);
                loadingView.dismiss();
                if ("新增成功".equals(bean.getMsg())){
                    // 吐司并改变界面,上个界面显示不通过
                    handler.sendEmptyMessage(0);
                }else {
                    //吐司失败
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("pgl", "===" + e.getMessage());
                Toast.makeText(SecurityAuditDetailActivity.this, "网络请求异常", Toast.LENGTH_SHORT).show();
                loadingView.dismiss();
            }
        }).executeAsync();
    }

    private void save() {
        // 关闭当前界面 在上个界面显示通过
        Intent intent = new Intent();
        intent.putExtra("isUpload","通过");
        intent.putExtra("position",position);
        setResult(RESULT_OK, intent);
        finish();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch ( msg.what) {
                case 0:
                    // 吐司并改变界面,上个界面显示不通过
                    Intent intent = new Intent();
                    intent.putExtra("isUpload","不通过");
                    intent.putExtra("position",position);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case 1:
                    //吐司失败
                    Toast.makeText(SecurityAuditDetailActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    /**
     * 设置背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = SecurityAuditDetailActivity.this.getWindow().getAttributes();
        //0.0-1.0
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            SecurityAuditDetailActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            SecurityAuditDetailActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        SecurityAuditDetailActivity.this.getWindow().setAttributes(lp);
    }


}
