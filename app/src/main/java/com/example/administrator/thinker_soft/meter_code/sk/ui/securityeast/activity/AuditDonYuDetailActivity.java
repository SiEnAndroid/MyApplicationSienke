package com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.activity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.SecurityGridviewImageAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AuditDonYuBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NoThroughBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityAuditBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityImageBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.SecurityAbnormalPhotoGalleryActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.AerateDetailActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.SecurityAuditDetailActivity;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.mode.MyGridview;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuditDonYuDetailActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.clear)
    TextView clear;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.zhanghao_button)
    LinearLayout zhanghaoButton;
    @BindView(R.id.tv_userId)
    TextView tvUserId;
    @BindView(R.id.tv_user_old_id)
    TextView tvUserOldId;
    @BindView(R.id.table_number)
    TextView tableNumber;
    @BindView(R.id.tv_area_name)
    TextView tvAreaName;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.tv_user_sy)
    TextView tvUserSy;
    @BindView(R.id.name_of_security_check_plan)
    TextView nameOfSecurityCheckPlan;
    @BindView(R.id.tv_sy_state)
    TextView tvSyState;
    @BindView(R.id.security_number)
    TextView securityNumber;
    @BindView(R.id.security_time)
    TextView securityTime;
    @BindView(R.id.security_note)
    TextView securityNote;
    @BindView(R.id.last_check_time)
    TextView lastCheckTime;
    @BindView(R.id.tv_sy_yh)
    TextView tvSyYh;
    @BindView(R.id.tv_why)
    TextView tvWhy;
    @BindView(R.id.tv_yh_lx)
    TextView tvYhLx;
    @BindView(R.id.security_situation)
    TextView securitySituation;
    @BindView(R.id.cv_yh)
    CardView cvYh;
    @BindView(R.id.tv_dlsr)
    TextView tvDlsr;
    @BindView(R.id.dlsr_gridview)
    MyGridview dlsrGridview;
    @BindView(R.id.ll_dlsr_gridview)
    LinearLayout llDlsrGridview;
    @BindView(R.id.tv_remark)
    EditText tvRemark;
    @BindView(R.id.not_through_tv)
    TextView notThroughTv;
    @BindView(R.id.not_through_cardview)
    CardView notThroughCardview;
    @BindView(R.id.through_tv)
    TextView throughTv;
    @BindView(R.id.through_cardview)
    CardView throughCardview;
    private AuditDonYuBean.ListBean bean;
    private int position;
    private LayoutInflater inflater;
    private View saveView;
    private PopupWindow popupWindow;
    private RadioButton cancelRb;
    private RadioButton saveRb;
    private TextView tips;
    private LoadingView loadingView;
    private ArrayList<String> cropIdLists;
    private SecurityGridviewImageAdapter iamgeAdapter;
    private int currentPosition;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_audit_detail_don_yu;
    }

    @Override
    protected void initView() {
        tvTitle.setText("审核详情");
        clear.setText("");

        getIntentData();
        setData();
        setViewClickListener();

        //  网络请求图片
        AuditDonYuDetailRequest();
    }
    /**
     * 设置数据
     */
    private void setData() {
        if (bean != null) {
            tvUserName.setText(bean.getUserName());
            tvUserId.setText(bean.getUserNumber());
            tvUserOldId.setText(bean.getTheOldNumber());
            tvUserPhone.setText(bean.getContactNumber());
            tvUserAddress.setText(bean.getUserAddress());
            tvUserSy.setText(bean.getSubject());
            tvYhLx.setText(bean.getTypesOfSafetyHazards());
            tvSyState.setText(bean.getSecurityStatus());
            tvWhy.setText(bean.getCausesOfPotentialSafetyHazards());
            nameOfSecurityCheckPlan.setText(bean.getNameOfSecurityCheckPlan());
            securityNumber.setText(bean.getSecurityNumber());
            securityTime.setText(bean.getSecurityTime());
            securityNote.setText(bean.getSecurityNotes());
            lastCheckTime.setText(bean.getLastSecurityCheckTime());
            securitySituation.setText(bean.getSecurityScreening());
            tableNumber.setText(bean.getTableNumber());

//            if(bean.getAjzt().equals("安检不合格")){
                tvSyYh.setVisibility(View.VISIBLE);
                cvYh.setVisibility(View.VISIBLE);
//            }else {
//                tvSyYh.setVisibility(View.GONE);
//                cvYh.setVisibility(View.GONE);
//            }
            if ( bean.getIfthrough() !=null){
                notThroughCardview.setVisibility(View.GONE);
                throughCardview.setVisibility(View.GONE);
            }
        }
    }

    private void setViewClickListener() {
    }

    /**
     * 请求图片
     */
    private void AuditDonYuDetailRequest() {
        //加载
        loadingView = new LoadingView(AuditDonYuDetailActivity.this, R.style.LoadingDialog, "加载图片中...请稍后");
        loadingView.show();
        final String httpUrl = new StringBuffer().append(SkUrl.SkHttp(AuditDonYuDetailActivity.this)).append("getSecurityImageDates.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .params("c_data_id",String.valueOf(bean.getSecurityNumber()));
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
                        iamgeAdapter = new SecurityGridviewImageAdapter(AuditDonYuDetailActivity.this, cropIdLists);
                        dlsrGridview.setAdapter(iamgeAdapter);
                        dlsrGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                dlsrGridview.setClickable(false);
                                currentPosition = position;
                                Log.e("选择的项",position+"");
                                if (!iamgeAdapter.getDeleteShow() && (iamgeAdapter.getCount() - 1 != position)) {
                                    //  Intent intent = new Intent(MeterUserDetailActivity.this, MyPhotoGalleryActivity.class);
                                    Intent intent = new Intent(AuditDonYuDetailActivity.this, SecurityAbnormalPhotoGalleryActivity.class);
                                    intent.putExtra("currentPosition", currentPosition);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("cropIdLists", cropIdLists);
                                    intent.putExtras(bundle);
//             intent.putStringArrayListExtra("cropPathLists", cropPathLists);
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
                Toast.makeText(AuditDonYuDetailActivity.this, "网络请求异常", Toast.LENGTH_SHORT).show();
                loadingView.dismiss();
            }

        }).executeAsync();
    }

    /**
     * 获取Bundl数据
     */
    public void getIntentData() {
        bean =(AuditDonYuBean.ListBean) getIntent().getSerializableExtra("audit");
        position = getIntent().getIntExtra("position", 0);
    }

    @OnClick({R.id.back,R.id.not_through_cardview,R.id.through_cardview})
    public void onclick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
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
        inflater = LayoutInflater.from(AuditDonYuDetailActivity.this);
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
        loadingView = new LoadingView(AuditDonYuDetailActivity.this, R.style.LoadingDialog, "上传中...请稍后");
        loadingView.show();

        /*
        *   c_user_name                        用户名称          必传
			n_anjian_inspection_id             安检编号          必传
			c_examin_remark                    不通过备注        不必传
			d_examin_time                      安检审核时间      不必传
			n_auditor_id                       审核人id         不必传
        */
        String httpUrl = new StringBuffer().append(SkUrl.SkHttp(AuditDonYuDetailActivity.this)).append("insertDyShBtg.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .params("c_user_name",bean.getUserName())
                .params("n_safety_inspection_id",String.valueOf(bean.getSecurityNumber()))
                .params("c_examin_remark",tvRemark.getText().toString()+"")
                .params("n_auditor_id","")
                .params("d_examin_time","");
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
                Toast.makeText(AuditDonYuDetailActivity.this, "网络请求异常", Toast.LENGTH_SHORT).show();
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

    /**
     * 设置背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = AuditDonYuDetailActivity.this.getWindow().getAttributes();
        //0.0-1.0
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            AuditDonYuDetailActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            AuditDonYuDetailActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        AuditDonYuDetailActivity.this.getWindow().setAttributes(lp);
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
                    Toast.makeText(AuditDonYuDetailActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };
}
