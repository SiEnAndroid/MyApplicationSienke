package com.example.administrator.thinker_soft.meter_code.sk.ui.security;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.SecurityAuditUnqualifiedAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AuditUnqualifiedBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.google.gson.Gson;

import java.util.List;

/**
 *  SecurityAuditUnqualifiedActivity class
 * @author g
 * @date 2018/8/17
 */

public class SecurityAuditUnqualifiedActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private EditText etSearch;
    private TextView tvSearch , noData ,totalNumber ,refresh;
    private RecyclerView recyclerView;
    private LoadingView loadingView;
    private List<AuditUnqualifiedBean.ListBean> list;
    private SecurityAuditUnqualifiedAdapter mAdapter;
    private SharedPreferences sharedPreferences_login;
    private String company_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_unqualified);

        initView();      // 初始化View
        defaultSetting();//初始化设置
        setOnClickListener();//点击事件

    }

    private void defaultSetting() {
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        company_id = String.valueOf(sharedPreferences_login.getInt("company_id", 0));//公司id
        // 进来就默认搜索所有
        AuditUnqualifiedRequest();

    }


    private void setOnClickListener() {
        back.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        refresh.setOnClickListener(this);
    }
    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        noData = (TextView) findViewById(R.id.no_data);
        etSearch = (EditText) findViewById(R.id.et_search);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        totalNumber =  findViewById(R.id.tv_total_user_number);
        refresh =  findViewById(R.id.refresh);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_search:
                //  搜索

                break;
            case R.id.refresh:
                //  刷新
                AuditUnqualifiedRequest();
                break;
            default:
                break;
        }
    }

    private void AuditUnqualifiedRequest() {

        //加载
        loadingView = new LoadingView(SecurityAuditUnqualifiedActivity.this, R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();                                                                                      //   auditFailed
        final String httpUrl = new StringBuffer().append(SkUrl.SkHttp(SecurityAuditUnqualifiedActivity.this)).append("auditFailed.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("aerate")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("compnayId", company_id);
        //   http://88.88.88.251:8082/SMDemo/auditFailed.do?compnayId=26
        //   http://88.88.88.251:8082/SMDemo/auditFailed.do?n_company_id=26
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e("pgl", "===" + response.getBody());

                //GSON直接解析成对象
                AuditUnqualifiedBean resultBean = new Gson().fromJson(response.getBody(), AuditUnqualifiedBean.class);
                Log.d("pgl","=== securityAbnormalAdapter"+"执行了"+new Gson().toJson(resultBean.getList()));
                if ("查询成功".equals(resultBean.getMsg())  & resultBean.getList() !=null){
                    list = resultBean.getList();
                    if (list != null){
                        totalNumber.setText(resultBean.getList().size()+"");
                        noData.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(SecurityAuditUnqualifiedActivity.this, LinearLayoutManager.VERTICAL, false));
                        //初始化适配器
                        mAdapter = new SecurityAuditUnqualifiedAdapter(list);
                        //设置适配器
                        recyclerView.setAdapter(mAdapter);
                        loadingView.dismiss();
                        //设置添加或删除item时的动画，这里使用默认动画
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        mAdapter.setOnItemClickListener(new SecurityAuditUnqualifiedAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, AuditUnqualifiedBean.ListBean data) {
//                                currentPosition = position;
                                Intent intent = new Intent(SecurityAuditUnqualifiedActivity.this,  AuditUnqualifiedInfoActivity.class);
                                intent.putExtra("Bean", list.get(position));
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivityForResult(intent, 8);
                            }

                        });
                    }else {
                        loadingView.dismiss();
                        Toast.makeText(SecurityAuditUnqualifiedActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        noData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }else {
                    noData.setVisibility(View.VISIBLE);
                    loadingView.dismiss();
                    recyclerView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(Throwable e) {
                noData.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                Log.d("pgl","==="+e.getMessage());
                Toast.makeText(SecurityAuditUnqualifiedActivity.this, "网络请求异常", Toast.LENGTH_SHORT).show();
                loadingView.dismiss();
            }
        }).executeAsync();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 8) {
                Log.i("UserList=ActivityResult", "页面回调进来了");

                if (data!=null){
                    String isUp=data.getStringExtra("isUpload");
                    if (isUp!=null && isUp.equals("成功")){
                        //  刷新
                        AuditUnqualifiedRequest();
                    }
                }

            }
        }
    }

}
