package com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportFlowBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportMessageBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseFragment;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

/**
 * 业务信息
 * File:ReportMessageFragment.class
 * @author Administrator
 * @date 2018/8/29 11:02
 */

public class ReportMessageFragment extends BaseFragment {
    private static String ARG_PARAM = "fg_key";
    private static String ARG_ID = "fg_id";
    @BindView(R.id.tv_ywNumber)
    TextView tvYwNumber;
    @BindView(R.id.tv_ywName)
    TextView tvYwName;
    @BindView(R.id.tv_ywType)
    TextView tvYwType;
    @BindView(R.id.tv_ywState)
    TextView tvYwState;
    @BindView(R.id.tv_ywApply)
    TextView tvYwApply;
    @BindView(R.id.tv_ywTime)
    TextView tvYwTime;
    @BindView(R.id.tv_ywFirm)
    TextView tvYwFirm;
    @BindView(R.id.tv_ywDescribe)
    TextView tvYwDescribe;

    private LoadingView loadingView;

    @Override
    public void onLazyLoad() {
        Bundle bundle = getArguments();
        String bid = bundle.getString(ARG_PARAM);
        String proCom = bundle.getString(ARG_ID);
        httpRequest(bid, proCom);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_rt_message;
    }

    @Override
    protected void initView() {

    }

    public static ReportMessageFragment newInstance(String tranId, String str) {
        ReportMessageFragment fragment = new ReportMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM, tranId);
        bundle.putString(ARG_ID, str);
        fragment.setArguments(bundle);
        return fragment;
    }


    /**
     * 获取报装数据
     */
    private void httpRequest(String tranId, String proCom) {
        JSONObject object = new JSONObject();
        try {
            //业务id
            object.put("tranId", tranId);
            //流程标识（流程列表的“c_process_requestion_com”）
            object.put("proCom", proCom);
        Log.e("入参=",object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loadingView = new LoadingView(getActivity(), R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();


        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(getActivity())).append("GetBusinessDetail").toString();
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
                        List<ReportMessageBean> mlist = new Gson().fromJson(data, new TypeToken<List<ReportMessageBean>>() {
                        }.getType());
                        if (mlist.get(0).getState_text()==null){
                            setData(mlist.get(0));
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

    /**
     * 设置数据
     */
    private void setData(ReportMessageBean bean){
        if (bean!=null){
            tvYwNumber.setText(ceilInt(bean.getN_BUSINESS_ID())+"");
            tvYwName.setText(bean.getC_BUSINESS_NAME());
            tvYwType.setText(bean.getC_INSTALL_TYPE_NAME());
            String type="停用";
            //0 停用 1 启用 2 归档
            switch (ceilInt(bean.getN_BUSINESS_STATE())){
                case 0:
                    type="停用";
                    break;
                case 1:
                    type="启用";
                    break;
                case 2:
                    type="归档";
                    break;

                default:
                        break;

            }
            tvYwState.setText(type);
            tvYwApply.setText(bean.getC_USER_NAME());
            String time=bean.getD_APPLICATION_OPERATION_TIME();
            if(time.indexOf("T") != -1) {
                String cTi = time.replace("T","  ");
                tvYwTime.setText(cTi);
            }else {

                tvYwTime.setText(time);
            }
            tvYwFirm.setText(bean.getC_COMPANY_NAME());
            tvYwDescribe.setText(bean.getC_APPLICATION_REMARK());
        }

    }

    public static int ceilInt(double number){
        return (int) Math.ceil(number);
    }

}
