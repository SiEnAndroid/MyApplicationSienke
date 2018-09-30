package com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.ReportAdornAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportAdornBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil.backgroundAlpha;

/**
 * 移动报装
 * File:ReportAdornActivity.class
 *
 * @author Administrator
 * @date 2018/8/29 9:13
 */

public class ReportAdornActivity extends BaseActivity implements RecyclerArrayAdapter.OnItemClickListener, View.OnClickListener {
    private ReportAdornAdapter adornAdapter;
    private String proCom;
    private String processName;
         private static  int TAPE=66;
    /**
     * 存储
     */
    private SharedPreferences sharedPreferences_login;
    private String userID;
    /**
     * 进度
     */
    private LoadingView loadingView;
    /**
     * 更多
     */
    private PopupWindow popupWindow;
    private String type;
    private Double processId;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    /**
     * 空数据
     */
    @BindView(R.id.no_data)
    LinearLayout noData;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_reportadorn;
    }

    @Override
    protected void initView() {

        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        userID = sharedPreferences_login.getString("userId", "");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adornAdapter = new ReportAdornAdapter(this);
        mRecyclerView.setAdapter(adornAdapter);
        adornAdapter.setOnItemClickListener(this);
        type = "0";
        getIntentData();

    }



    /**
     * 监听
     *
     * @param view
     */
    @OnClick({R.id.back, R.id.more})
    public void OnclickAudit(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.more:
                //更多
                showMoreWindow(view);
                break;
            default:
                break;
        }
    }


    /**
     * 弹出框
     *
     * @param view
     */
    private void showMoreWindow(View view) {
        if (popupWindow == null) {
            View contentView = getLayoutInflater().inflate(R.layout.popwindow_type, null);
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final RadioButton allBtn = contentView.findViewById(R.id.rd_all);
            final RadioButton doneBtn = contentView.findViewById(R.id.rd_done);
            final RadioButton notBtn = contentView.findViewById(R.id.rd_not);
            allBtn.setOnClickListener(this);
            doneBtn.setOnClickListener(this);
            notBtn.setOnClickListener(this);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
            popupWindow.showAsDropDown(view, -PopWindowUtil.dip2px(ReportAdornActivity.this, 68), 0);
            backgroundAlpha(ReportAdornActivity.this, 0.6F);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(ReportAdornActivity.this, 1.0F);
                }
            });
        } else {
            if (!popupWindow.isShowing()) {
                backgroundAlpha(ReportAdornActivity.this, 0.6F);
                popupWindow.showAsDropDown(view, -PopWindowUtil.dip2px(ReportAdornActivity.this, 68), 0);
            } else {
                popupWindow.dismiss();
                backgroundAlpha(ReportAdornActivity.this, 1.0F);
            }

        }

    }


    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        String queId = String.valueOf((int) adornAdapter.getAllData().get(position).getN_QUEUE_ID());
        String proId = String.valueOf((int) adornAdapter.getAllData().get(position).getN_PROCESS_ID());
        String tranId = String.valueOf((int) adornAdapter.getAllData().get(position).getTRANID());
        bundle.putDouble("bid", adornAdapter.getAllData().get(position).getBID());
        bundle.putString("userName",adornAdapter.getAllData().get(position).getC_USER_NAME());
        bundle.putString("process",adornAdapter.getAllData().get(position).getPROCESS());
        bundle.putString("proCom", proCom);
        bundle.putString("queId", queId);
        bundle.putString("tranId", tranId);
        bundle.putString("proId", proId);
        bundle.putString("process_name",adornAdapter.getAllData().get(position).getTRAN_STATE());
        //跳转详情
        GostartForResult(ReportAdornDetailsActivity.class,TAPE, bundle);
    }

    /**
     * 获取报装列表数据
     */
    private void httpRequest(double proceId) {
        JSONObject object = new JSONObject();
        try {
            //工作人员id
            object.put("OpId", userID);
            //流程id
         object.put("procesId",proceId+"");
            //状态id  (全部-1，已办理1，未办理0)
            object.put("status", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loadingView = new LoadingView(ReportAdornActivity.this, R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();

        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(ReportAdornActivity.this)).append("GetBusiness").toString();
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
                adornAdapter.clear();
                try {
                    JSONObject  jsonObject=new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    if (Integer.valueOf(code)==1){
                        String data = jsonObject.optString("Data");
                        List<ReportAdornBean> mlist = new Gson().fromJson(data, new TypeToken<List<ReportAdornBean>>() {
                        }.getType());
                        if (mlist.get(0).getState_text() == null) {
                            adornAdapter.addAll(mlist);
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
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
                //空数据
                if (noData != null) {
                    noData.setVisibility(View.VISIBLE);
                }
            }
        }).executeAsync();
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
     * 获取bundle值
     */
    public void getIntentData() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            processId = bundle.getDouble("process_id");
            proCom = bundle.getString("proCom");
            processName = bundle.getString("process_name");
            httpRequest(processId);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // (全部-1，已办理1，未办理0)
            case R.id.rd_all:
                //全部-1
                dissPw();
                type = "-1";
                httpRequest(processId);
                break;
            case R.id.rd_done:
                //已办理1
                dissPw();
                type = "1";
                httpRequest(processId);
                break;
            case R.id.rd_not:
                //未办理0
                type = "0";
                dissPw();
                httpRequest(processId);
                break;

            default:
                break;

        }
    }

    /**
     * 关闭
     */
    private void dissPw() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == TAPE) {
                if (data != null) {
                 String ok=data.getStringExtra("ok");
                 if (ok!=null&&ok.equals("成功")){
                     httpRequest(processId);
                 }

                }
        }
    }
}
