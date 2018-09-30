package com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.ReportConditionAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportConditionBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportMessageBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseFragment;
import com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn.ReportFlowActivity;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

/**
 *  办理情况
 * File:ReportConditionFragment.class
 *
 * @author Administrator
 * @date 2018/8/29 11:02
 */

public class ReportConditionFragment extends BaseFragment {
    private ReportConditionAdapter adapter;
    private LoadingView loadingView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private static String ARG_PARAM = "fg_key";
    @Override
    public void onLazyLoad() {
        Bundle bundle = getArguments();
        String bid = bundle.getString(ARG_PARAM);
        httpRequest(bid);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_rt_condition;
    }

    @Override
    protected void initView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ReportConditionAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setNestedScrollingEnabled(false);

    }
    public static ReportConditionFragment newInstance(String str) {
        ReportConditionFragment fragment = new ReportConditionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM, str);
        fragment.setArguments(bundle);
        return fragment;
    }


    /**
     * 获取办理情况
     */
    private void httpRequest(String bid) {
        JSONObject object = new JSONObject();
        try {
            //业务id
            object.put("busId", bid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadingView = new LoadingView(getActivity(), R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();


        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(getActivity())).append("SelectTranList").toString();
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
                    JSONObject  jsonObject=new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    if (Integer.valueOf(code)==1){
                        String data = jsonObject.optString("Data");
                        List<ReportConditionBean> mlist = new Gson().fromJson(data, new TypeToken<List<ReportConditionBean>>() {
                        }.getType());
                        if (mlist.get(0).getState_text()==null){
                            adapter.clear();
                            adapter.addAll(mlist);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
}
