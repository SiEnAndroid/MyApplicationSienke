package com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.AuditDonYuAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AuditDonYuBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AuditPrame;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.JsonAnalyUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AuditDonYuAcivity extends BaseActivity implements RecyclerArrayAdapter.OnItemClickListener {

    private AuditDonYuAdapter mAdapter;
    private LoadingView loadingView;//进度
    private SharedPreferences sharedPreferences_login;
    @BindView(R.id.recyclerView)
    EasyRecyclerView mRecyclerView;
    @BindView(R.id.tv_title)
    TextView title;//标题
    @BindView(R.id.clear)
    TextView clear;
    private String company_id;
    @BindView(R.id.no_data)
    LinearLayout noData;
    private AuditDonYuBean.ListBean auditBean;
    private String login_name;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_sy_audit;
    }

    @Override
    protected void initView() {
        title.setText("审核");
        clear.setText("刷新");
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        company_id = String.valueOf(sharedPreferences_login.getInt("company_id", 0));
        login_name = sharedPreferences_login.getString("login_name", "");
        mAdapter = new AuditDonYuAdapter(this);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AuditDonYuAcivity.this, LinearLayoutManager.VERTICAL, false));
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);


        AuditPrame prame=new AuditPrame();
        prame.setC_safety_inspection_member(login_name);
        AuditDonYuRequest(prame);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(AuditDonYuAcivity.this,  AuditDonYuDetailActivity.class);
        auditBean = mAdapter.getAllData().get(position);
        intent.putExtra("audit", auditBean);
        intent.putExtra("position", position);
        startActivityForResult(intent, 22);

    }

    /**
     * 监听
     * @param view
     */
    @OnClick({R.id.back,R.id.clear})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.clear:
                AuditPrame prame=new AuditPrame();
                prame.setC_safety_inspection_member(login_name);
                AuditDonYuRequest(prame);
                break;
            default:
                break;
        }
    }


    /**
     * 安检情况
     */
    private void AuditDonYuRequest(AuditPrame prame) {
        //加载
        loadingView = new LoadingView(AuditDonYuAcivity.this, R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();
        String httpUrl = new StringBuffer().append(SkUrl.SkHttp(AuditDonYuAcivity.this)).append("getDySh.do").toString();
        /*
        * 	n_company_id                          公司id                         必传
			c_user_name                           用户名                         不必传
			c_user_id                             用户id                         不必传
			c_user_address                        用户地址                        不必传
			d_anjian_inspection_date_start        安检时间—开始                   不必传
			d_anjian_inspection_date_end          安检时间—结束                   不必传
			n_anjian_securitycontent              安检情况 0表示不合格，1表示合格   不必传
			c_anjian_inspection_member            安检员                          不必传

			   .params("c_user_name" ,"")
                .params("c_user_id" ,"")
                .params("c_user_address" ,"")
                .params("d_anjian_inspection_date_start" ,"")
                .params("d_anjian_inspection_date_end" ,"")

			*/
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("aerate")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("n_company_id" ,company_id)
                .params("n_anjian_securitycontent" ,"")
                .params("c_anjian_inspection_member",  prame.getC_safety_inspection_member()==null?"": SkUrl.toURLEncoded(prame.getC_safety_inspection_member()))
                .string("");
        Request.newRequest(build, new HttpCallback() {

            private List<AuditDonYuBean.ListBean> mList;

            @Override
            public void onComplete(Response response) {
                Log.e("MeterRemarkActivity", "onComplete/response:" + response.getBody());
                Log.e("MeterRemarkActivity", "onComplete/response: content type=" + response.getContentType());
                loadDiss();

                mList = JsonAnalyUtil.AuditDonYu(response.getBody());
                mAdapter.clear();
                mAdapter.addAll(mList);
                if (mList.size()>0){
                    if (noData!=null){
                        noData.setVisibility(View.GONE);
                    }

                }else {
                    if (noData!=null) {
                        noData.setVisibility(View.VISIBLE);
                    }
                }

            }
            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
                loadDiss();
                if (mAdapter.getAllData().size()>0){
                    if (noData!=null){
                        noData.setVisibility(View.GONE);
                    }
                }else {
                    if (noData!=null) {
                        noData.setVisibility(View.VISIBLE);
                    }
                }
            }
        }).executeAsync();
    }
    private void loadDiss() {
        if (loadingView != null) {
            loadingView.dismiss();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 22) {
                Log.i("UserList=ActivityResult", "页面回调进来了");
                if (data!=null){
                    String isUp=data.getStringExtra("isUpload");
                    if (isUp!=null && isUp.equals("不通过")){
                        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
                        auditBean.setIfthrough("不通过");
                        int position = data.getIntExtra("position", 0);
                        mAdapter.notifyItemChanged(position);
                    }else if (isUp!=null && isUp.equals("通过")){
                        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                        auditBean.setIfthrough("通过");
                        int position = data.getIntExtra("position", 0);
                        mAdapter.notifyItemChanged(position);
                    }
//                    mAdapter.notifyDataSetChanged();
                }

            }
        }
    }
}
