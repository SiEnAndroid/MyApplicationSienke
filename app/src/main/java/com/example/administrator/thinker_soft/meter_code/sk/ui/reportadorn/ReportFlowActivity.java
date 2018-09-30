package com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.activity.MoveHomePageActivity;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.ReportFlowAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportAdornBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportFlowBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.JsonAnalyUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  流程选择
 * @author g
 * @FileName ReportFlowActivity
 * @date 2018/8/31 12:08
 */
public class ReportFlowActivity extends BaseActivity implements RecyclerArrayAdapter.OnItemClickListener, View.OnClickListener {
    private ReportFlowAdapter adapter;
    /**进度*/
    private  LoadingView loadingView;
    /**标题*/
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.clear)
    TextView cancel;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    /**空数据*/
    @BindView(R.id.no_data)
    LinearLayout noData;
    @Override
    protected int getContentViewID() {
        return R.layout.activity_rt_flow;
    }

    @Override
    protected void initView() {
        title.setText("选择流程");
        cancel.setText("新增");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReportFlowAdapter(this);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        cancel.setOnClickListener(this);
        httpListRequest();


    }

    /**
     * 监听
     * @param view
     */
    @OnClick({R.id.back})
    public void OnclicFlow(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    public void onItemClick(int position) {
        Bundle bundle=new Bundle();
        bundle.putDouble("process_id",adapter.getAllData().get(position).getN_PROCESS_ID());
        bundle.putString("proCom",adapter.getAllData().get(position).getC_PROCESS_REQUESTION_COM());
        bundle.putString("process_name",adapter.getAllData().get(position).getC_PROCESS_NAME());

        //跳转详情
        GoActivity(ReportAdornActivity.class,bundle);
    }

    /**
     * 获取流程列表
     */
    private void httpListRequest() {
        loadingView = new LoadingView(ReportFlowActivity.this, R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();
        String httpUrl =new StringBuffer().append(SkUrl.YSHttp(ReportFlowActivity.this)).append("GetProcessList").toString();
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
                loadDiss();
                Log.e("MeterRemarkActivity", "onComplete/response:" + response.getBody());
                Log.e("MeterRemarkActivity", "onComplete/response: content type=" + response.getContentType());
                try {
                    JSONObject  jsonObject=new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    if (Integer.valueOf(code)==1){
                        String data = jsonObject.optString("Data");
                        List<ReportFlowBean> mlist = new Gson().fromJson(data, new TypeToken<List<ReportFlowBean>>() {
                        }.getType());
                        if (mlist.get(0).getState_text() == null) {
                            adapter.addAll(mlist);
                            if (noData != null) {
                                noData.setVisibility(View.GONE);
                            }
                        } else {
                            if (noData != null) {
                                noData.setVisibility(View.VISIBLE);
                            }
                        }
                    }else {
                        String msg = jsonObject.optString("Msg");
                        Log.e("pgl", "onError:===" + msg);
                        if (noData != null) {
                            noData.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (noData != null) {
                        noData.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onError(Throwable e) {
                loadDiss();
                if (noData != null) {
                    noData.setVisibility(View.VISIBLE);
                }
                Log.e("ReportFlowActivity", "onError:" + e.getMessage());
            }
        }).executeAsync();

    }
    private void loadDiss() {
        if (loadingView != null) {
            loadingView.dismiss();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear:
                Intent intent = new Intent(ReportFlowActivity.this, OpenAnAccountActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        
    }
}
