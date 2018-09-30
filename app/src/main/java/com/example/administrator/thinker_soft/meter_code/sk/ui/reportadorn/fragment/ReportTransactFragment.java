package com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

import com.example.administrator.thinker_soft.meter_code.sk.adapter.FilterAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.InstructionsBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportConditionBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportTranCdBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportTranParameSubmit;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportTranSysName;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportTransactBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportTransactPwBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportTsParame;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseFragment;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.EditTextUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.JsonAnalyUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.BottomDatePopwindow;
import com.example.administrator.thinker_soft.meter_code.sk.widget.BottomDatePopwindowCopy;
import com.example.administrator.thinker_soft.meter_code.sk.widget.UIHandler;
import com.example.administrator.thinker_soft.mode.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 业务办理
 * File:ReportTransactFragment.class
 *
 * @author Administrator
 * @date 2018/8/29 11:04
 */

public class ReportTransactFragment extends BaseFragment {
    private static String ARG_PARAM = "fg_key";
    /**
     * id
     */
    private ReportTsParame prame;
    /**
     * 条件数据   ReportTranCdBean
     */
    private ArrayList<ReportTranCdBean> list = new ArrayList<>();
    /**
     * 驳回数据
     */
    private ArrayList<ReportTranCdBean> listRg = new ArrayList<>();
    /**
     * 批示数据
     */
    private ArrayList<InstructionsBean> instructions = new ArrayList<>();
    /**
     * 办理人数据
     */
    private ArrayList<ReportTranCdBean> listTs = new ArrayList<>();
    /**
     * 办理人筛选
     */
    private List<ReportTranSysName> blList = new ArrayList<>();
    /**
     * 弹出框
     */
    private BottomDatePopwindow bottomDatePopwindow;
    private BottomDatePopwindow bottomDateRg;
    private BottomDatePopwindow bottomDateTs;
    private BottomDatePopwindowCopy bottomDateis;
    private PopupWindow popupWindow;
    /**
     * 密码弹出框
     */
    private PopupWindow passwordPopupWindow;
    private PopupWindow bxPopupWindow;
    /**
     * 筛选适配器
     */
    private FilterAdapter adapter;
    private SharedPreferences sharedPreferences_login;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户密码
     */
    private String passWordSt;
    /**
     * 用户签章id
     */
    private String okId;
    /**
     * 流程条件id
     */
    private String ctId;
    /**
     * 指定人员ids
     */
    private String tpId;
    /**
     * 批示说明
     */
    private String approval;
    /**
     * 驳回id
     */
    private String regresId;

    /**
     * 更新Ui
     */
    private UIMyHandler myHandler = new UIMyHandler(this);

    @BindView(R.id.et_condition)
    EditText etCondition;
    @BindView(R.id.et_regresses)
    EditText etRegresses;
    @BindView(R.id.et_transact)
    EditText etTransact;
    @BindView(R.id.tv_node)
    TextView tvNode;

    /**
     * 批示
     */
    @BindView(R.id.et_approval)
    EditText etApproval;
    @BindView(R.id.rb_roam)
    RadioButton rbRoam;
    @BindView(R.id.rb_rebut)
    RadioButton rbRebut;
    @BindView(R.id.rb_cancel)
    RadioButton rbCancel;
    @BindView(R.id.layout_ct)
    LinearLayout lyCt;
    @BindView(R.id.layout_rg)
    LinearLayout lyRg;
    @BindView(R.id.layout_rs)
    LinearLayout lyRs;
    @BindView(R.id.image_transact)
    ImageView imageTs;
    @BindView(R.id.iv_instructions)
    ImageView ivInstructions;
    private String userID;


    @Override
    public void onLazyLoad() {
        Bundle bundle = getArguments();
        userID = sharedPreferences_login.getString("userId", "");
        //获取里面的Persion里面的数据
        prame = (ReportTsParame) bundle.getSerializable(ARG_PARAM);
        httpConditionRequest();
        httpRegreRequest();
        httpInstructionsRequest();


        EditTextUtils.setEnabled(etRegresses, false);
        EditTextUtils.setEnabled(etCondition, false);
        EditTextUtils.setEnabled(etTransact, false);
    }



    public static ReportTransactFragment newInstance(ReportTsParame persion) {
        ReportTransactFragment fragment = new ReportTransactFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM, persion);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_rt_transact;
    }

    /**
     */
    @Override
    protected void initView() {
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        userId = sharedPreferences_login.getString("userId", "");
//        //输入监听
        etTransact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("dd", "onTextChanged: " + charSequence.toString());
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    if (adapter != null) {
                        adapter.getFilter().filter(charSequence.toString());
                  //      myHandler.removeCallbacks(mRunnable);
//                //800毫秒没有输入认为输入完毕
                //   myHandler.postDelayed(mRunnable, 1000);
//                        if (charSequence.toString().length()>=1){
//                 myHandler.sendEmptyMessage(1);
//                        }
                    } else {
                        if (blList.size() > 0) {
                            createPopupwindow(lyRs);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
               // myHandler.sendEmptyMessage(1);
//                myHandler.removeCallbacks(mRunnable);
//                //800毫秒没有输入认为输入完毕
//                myHandler.postDelayed(mRunnable, 2000);
            }
        });
//        //焦点监听
//        etTransact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    Log.e("获得焦点", "1"+"---"+tpId+"---"+blList.size());
//                    // 获得焦点
//                    String type = "3";
//                    if (tpId.equals(type)) {
//                        Log.e("获得焦点", "2"+"---"+blList.size());
//                        if (blList.size()>0) {
//                            createPopupwindow(lyRs);
//                        }
//                   }
//                } else {
//                    // 失去焦点
//                    if (popupWindow!=null&&popupWindow.isShowing()) {
//                        popupWindow.dismiss();
//                    }
//                    Log.e("失去焦点", "1");
//                }
//            }
//        });

        etTransact.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //完成自己的事件
                    myHandler.sendEmptyMessage(1);
                }
                return false;
            }
        });
        //办理人设置
        if (prame != null) {
            tvNode.setText(prame.getProcess());
        }

    }

    /**
     * 弱引用hander
     */
    private static class UIMyHandler extends UIHandler<ReportTransactFragment> {

        public UIMyHandler(ReportTransactFragment cls) {
            super(cls);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ReportTransactFragment activity = ref.get();
            if (activity != null) {
                if (activity.getActivity().isFinishing()) {
                    return;
                }

                switch (msg.what) {
                    case 1:
                        activity.createPopupwindow(activity.lyRs);
                        Tools.hideSoftInput(activity.getActivity(), activity.etTransact);
                        break;
                    default:
                        break;

                }
            }
        }
    }

    /**
     * 点击监听
     *
     * @param view
     */
    @OnClick({R.id.layout_ct, R.id.layout_rg, R.id.layout_rs, R.id.btn_submit,R.id.iv_instructions})
    public void OnclickTransact(View view) {
        View parent = ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
        switch (view.getId()) {
            case R.id.layout_ct:
                //流转条件
                if (rbRebut.isChecked()) {
                    ToastUtil.showShort(getActivity(), "驳回不能选择流转条件！");
                } else {
                    if (bottomDatePopwindow == null) {
                        //数据是否存在
                        if (list.size() > 0) {
                            bottomDatePopwindow = new BottomDatePopwindow(getActivity(), list.get(0).getName(), list);
                            bottomDatePopwindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                            bottomDatePopwindow.setBirthdayListener(new BottomDatePopwindow.OnBirthListener() {
                                @Override
                                public void onClick(String title, String ids) {
                                    etCondition.setText(title);
                                    ctId = ids;
                                    httpRsRuest();
                                }
                            });
                        } else {
                            ToastUtil.showShort(getActivity(), "暂无数据！");
                        }
                    } else {
                        if (!bottomDatePopwindow.isShowing()) {
                            bottomDatePopwindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                        } else {
                            bottomDatePopwindow.dismiss();
                        }
                    }
                }
                break;
            case R.id.layout_rg:
                //回退点：
                if (rbRoam.isChecked()) {
                    ToastUtil.showShort(getActivity(), "流转不能选择回退！");
                } else {
                    if (bottomDateRg == null) {
                        //数据是否存在
                        if (listRg.size() > 0) {
                            bottomDateRg = new BottomDatePopwindow(getActivity(), listRg.get(0).getName(), listRg);
                            bottomDateRg.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                            bottomDateRg.setBirthdayListener(new BottomDatePopwindow.OnBirthListener() {
                                @Override
                                public void onClick(String title, String ids) {
                                    etRegresses.setText(title);
                                    regresId = ids;
                                }
                            });
                        } else {

                            ToastUtil.showShort(getActivity(), "暂无数据！");
                        }

                    } else {
                        if (!bottomDateRg.isShowing()) {
                            bottomDateRg.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                        } else {
                            bottomDateRg.dismiss();
                        }
                    }
                }
                break;
            case R.id.layout_rs:
                //办理人
                if (bottomDateTs == null) {
                    //数据是否存在
                    if (listTs.size() > 0) {
                        bottomDateTs = new BottomDatePopwindow(getActivity(), listTs.get(0).getName(), listTs);
                        bottomDateTs.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                        bottomDateTs.setBirthdayListener(new BottomDatePopwindow.OnBirthListener() {
                            @Override
                            public void onClick(String title, String ids) {
                                etTransact.setText(title);
                                etTransact.setSelection(title.length());
                                tpId = ids;
                            }
                        });
                    } else {
                        ToastUtil.showShort(getActivity(), "暂无数据！");
                    }
                } else {
                    if (!bottomDateTs.isShowing()) {
                        bottomDateTs.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                    } else {
                        bottomDateTs.dismiss();
                    }
                }
                break;

            case R.id.btn_submit:
                //提交
                httpPpRuest(userId);
                String transactStr = etTransact.getText().toString().trim();
                approval = etApproval.getText().toString().trim();
                String etRg=etRegresses.getText().toString().trim();

                if (!TextUtils.isEmpty(approval) && !TextUtils.isEmpty(transactStr)) {

                    //是否存在驳回
                    if (rbRebut.isChecked()) {
                        if (TextUtils.isEmpty(etRg)){
                            ToastUtil.showShort(getActivity(), "当前节点不能进行回退!");
                        }else {
                            createPwPopupwindow();
                        }
                    }else {
                        createPwPopupwindow();
                    }

                } else {
                    ToastUtil.showShort(getActivity(), "请将信息填写完整!");
                }

                break;
            case R.id.iv_instructions:
                // 批示
//                httpInstructionsRequest();
                if (bottomDateis == null) {
                    //数据是否存在
                    if (instructions.size() > 0) {
                        bottomDateis = new BottomDatePopwindowCopy(getActivity(), instructions.get(0).getC_PILEME_CONTENT(), instructions);
                        bottomDateis.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                        bottomDateis.setBirthdayListener(new BottomDatePopwindowCopy.OnBirthListener() {
                            @Override
                            public void onClick(String title, String ids) {
                                etApproval.setText(title);
//                                regresId = ids;
                            }
                        });
                    } else {
                        ToastUtil.showShort(getActivity(), "暂无数据！");
                    }

                } else {
                    if (!bottomDateis.isShowing()) {
                        bottomDateis.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
                    } else {
                        bottomDateis.dismiss();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取条件
     */
    private void httpConditionRequest() {
        JSONObject object = new JSONObject();
        try {
            //当前队列id
            object.put("queId", prame.getQueId());
            //当前流程id
            object.put("proId", prame.getProId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(getActivity())).append("GetConditions").toString();
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

                try {
                    JSONObject  jsonObject=new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    if (Integer.valueOf(code)==1){
                        String data = jsonObject.optString("Data");
                        List<ReportTranCdBean> mlist = new Gson().fromJson(data, new TypeToken<List<ReportTranCdBean>>() {
                        }.getType());
                        list.addAll(mlist);
                        ctId = list.get(0).getId();
                        etCondition.setText(list.get(0).getName());
                        httpRsRuest();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
            }
        }).executeAsync();

    }

    /**
     * 获取驳回节点
     */
    private void httpRegreRequest() {
        JSONObject object = new JSONObject();
        try {
            //当前业务id
            object.put("busId", prame.getBusId());
            //当前流程id
            object.put("proId", prame.getProId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(getActivity())).append("GetJumps").toString();
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

                try {
                    JSONObject  jsonObject=new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    if (Integer.valueOf(code)==1){
                        String data = jsonObject.optString("Data");
                        List<ReportTranCdBean> mlist = new Gson().fromJson(data, new TypeToken<List<ReportTranCdBean>>() {
                        }.getType());
                        if (mlist.get(0).getState_text() == null) {
                            listRg.clear();
                            listRg.addAll(mlist);
                            etRegresses.setText(listRg.get(0).getName());
                            regresId = listRg.get(0).getId();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
            }
        }).executeAsync();

    }

    /**
     * 获取批示
     */
    private void httpInstructionsRequest() {
        JSONObject object = new JSONObject();
        try {
            //操作员id
            object.put("sysUId", userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(getActivity())).append("GetPilemeContent").toString();
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

                try {
                    JSONObject  jsonObject=new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    if (Integer.valueOf(code)==1){
                        String data = jsonObject.optString("Data");
                        Log.e("pgl","httpInstructionsRequest ==== 成功:"+data);
                        List<InstructionsBean> mlist = new Gson().fromJson(data, new TypeToken<List<InstructionsBean>>() {
//                        List<ReportTranCdBean> mlist = new Gson().fromJson(data, new TypeToken<List<ReportTranCdBean>>() {
                        }.getType());

//                        if (mlist.get(0).getC_PILEME_CONTENT() == null) {
//                            instructions.clear();
                            instructions.addAll(mlist);
//                            etApproval.setText(instructions.get(0).getC_PILEME_CONTENT());
//                            regresId = instructions.get(0).getN_PILEME_ID();
//                        }
                    }else {
                        Log.e("pgl","httpInstructionsRequest ====失败:"+jsonObject.optString("Msg"));
                    }
                } catch (JSONException e) {
                    Log.e("pgl","httpInstructionsRequest ==== 异常:"+e.getMessage());
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
            }
        }).executeAsync();
    }

    /**
     * 查询办理人
     */
    private void httpRsRuest() {
        JSONObject object = new JSONObject();
        try {
            //conditionId
            object.put("conditionId", ctId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(getActivity())).append("GetTranNext").toString();
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
                try {
                    JSONObject  jsonObject=new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    if (Integer.valueOf(code)==1){
                        String data = jsonObject.optString("Data");
                        ReportTransactBean bean = new Gson().fromJson(data, ReportTransactBean.class);
                        tpId = bean.getTransatp();
                        if (!TextUtils.isEmpty(bean.getTransactor())) {
                            tvNode.setText(bean.getTransnode());
//                    etTransact.setText(result.get(0));
//                    etTransact.setSelection(result.get(0).length());
                            //等于3在查询
                            String type = "3";
                            if (bean.getTransatp().equals(type)) {
                                etTransact.setText("");
                                httpRsRuest(bean.getTransactor());
                                imageTs.setVisibility(View.VISIBLE);
                                EditTextUtils.setEnabled(etTransact, true);
                            } else {
                                etTransact.setText(bean.getTransactor());
                                imageTs.setVisibility(View.GONE);
                                EditTextUtils.setEnabled(etTransact, false);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                List<String> result = Arrays.asList(bean.getTransactor().split(","));
//                //设置办理人数据
//                for (int i = 0; i < result.size(); i++) {
//                    ReportTranCdBean cdBean = new ReportTranCdBean();
//                    cdBean.setName(result.get(i));
//                    cdBean.setId(bean.getTransatp());
//                    listTs.add(cdBean);
//                }
            }
            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
            }
        }).executeAsync();

    }


    /**
     * 工作人员列表
     */
    private void httpRsRuest(String Name) {
        JSONObject object = new JSONObject();
        try {
            //conditionId
            object.put("sysName", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(getActivity())).append("SelectSystemuserByName").toString();
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

                try {
                    JSONObject  jsonObject=new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    if (Integer.valueOf(code)==1){
                        String data = jsonObject.optString("Data");
                        List<ReportTranSysName> mlist = new Gson().fromJson(data, new TypeToken<List<ReportTranSysName>>() {
                        }.getType());
                        blList.clear();
                        blList.addAll(mlist);
                        //添加值
                        listTs.clear();
                        for (ReportTranSysName sysName : mlist) {
                            if (sysName.getC_USER_NAME() != null) {
                                ReportTranCdBean cdBean = new ReportTranCdBean();
                                cdBean.setName(sysName.getC_USER_NAME());
                                cdBean.setId((int) sysName.getN_SYSTEMUSER_ID() + "");
                                listTs.add(cdBean);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
            }
        }).executeAsync();

    }


    /**
     * 匹配用户信息
     */
    private void httpPpRuest(String sysId) {
        JSONObject object = new JSONObject();
        try {
            //conditionId
            object.put("sysId", sysId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(getActivity())).append("GetUserSignature").toString();
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
                try {
                    JSONObject  jsonObject=new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    if (Integer.valueOf(code)==1){
                        String data = jsonObject.optString("Data");
                        List<ReportTransactPwBean> mlist = new Gson().fromJson(data, new TypeToken<List<ReportTransactPwBean>>() {
                        }.getType());
                        passWordSt = mlist.get(0).getC_SIGNATURE_PASSWORD();
                        okId = mlist.get(0).getN_SIGNATURE_ID() + "";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
            }
        }).executeAsync();

    }

    /**
     * 提交
     */
    private void httpSubmitRuest(ReportTranParameSubmit parame) {
        JSONObject object = new JSONObject();
        try {
            //流程id
            object.put("transactionid", parame.getTransactionid());
            //逻辑条件
            object.put("conditionid", parame.getConditionid());
            //用户id
            object.put("systemuserid", parame.getSystemuserid());
            //指定人员ids
            object.put("zduserids", parame.getZduserids());
            //办理批示
            object.put("tcomment", parame.getTcomment());
            //流转方式 1 通过 2 驳回 3废弃
            object.put("trantype", parame.getTrantype());
            //驳回时 跳转的节点 默认0
            object.put("jumpnode", parame.getJumpnode());
            //驳回时,3 逐步重走 4一步重走 默认0
            object.put("bacemode", parame.getBacemode());
            //签章id
            object.put("signatureid", parame.getSignatureid());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String httpUrl = new StringBuffer().append(SkUrl.YSHttp(getActivity())).append("TranNext").toString();
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
                try {
                    JSONObject  jsonObject=new JSONObject(response.getBody());
                    String code = jsonObject.optString("Code");
                    if (Integer.valueOf(code)==1){
                        ToastUtil.showShort(getActivity(), "提交成功！");
                        reIntent();
                        getActivity().finish();
                    }else {
                        String msg = jsonObject.optString("Msg");
                        ToastUtil.showShort(getActivity(), "提交失败:"+msg+"!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
                ToastUtil.showShort(getActivity(), "服务器异常");
            }
        }).executeAsync();

    }


    /**
     * 下拉人员选择
     * popupwindow
     */
    public void createPopupwindow(View view) {
        if (popupWindow == null) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View filterView = inflater.inflate(R.layout.popupwindow_recyclerview, null);
            popupWindow = new PopupWindow(filterView, view.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
            //绑定控件ID
            RecyclerView recyclerView = filterView.findViewById(R.id.recyclerView);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            //添加Android自带的分割线
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(manager);

            final List<ReportTranSysName> mList = new ArrayList<>();


            adapter = new FilterAdapter(getActivity());
            if (blList.size() > 0) {
                adapter.appendList(blList);
            }
            recyclerView.setAdapter(adapter);
            adapter.setmOnItemClickLitener(new FilterAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position,String name,String id) {
                    popupWindow.dismiss();
                    etTransact.setText(name);
                    etTransact.setSelection(name.length());
                    //获取id
                    tpId = id+ "";
                }
            });
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.update();
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_spinner_shape));
            popupWindow.setAnimationStyle(R.style.Popupwindow);
            popupWindow.showAsDropDown(view, 0, 0);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });
        } else {
            if (!popupWindow.isShowing()) {
                popupWindow.showAsDropDown(view, 0, 0);
            } else {
                popupWindow.dismiss();
            }
        }
    }


    /**
     * 密码弹出框
     * popupwindow
     */
    public void createPwPopupwindow() {
        if (passwordPopupWindow == null) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View filterView = inflater.inflate(R.layout.popupwindow_password, null);
            passwordPopupWindow = new PopupWindow(filterView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //密码输入
            final EditText passWord = filterView.findViewById(R.id.et_password);
            TextView next = filterView.findViewById(R.id.tv_next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pwText = passWord.getText().toString().trim();
                    if (!TextUtils.isEmpty(passWordSt)) {
                        if (passWordSt.equals(pwText)) {
                            passwordPopupWindow.dismiss();

                            //是否存在驳回
                            if (rbRebut.isChecked()) {
                                createBxPopupwindow();
                            } else {
                                //提交
                                ReportTranParameSubmit parameSubmit = new ReportTranParameSubmit();
                                parameSubmit.setTransactionid(prame.getTranId());
                                parameSubmit.setConditionid(ctId);
                                parameSubmit.setSystemuserid(userId);
                                parameSubmit.setZduserids(tpId);
                                parameSubmit.setTcomment(approval);
                                if (rbRoam.isChecked()) {
                                    //通过
                                    parameSubmit.setTrantype("1");
                                } else {
                                    //废弃
                                    parameSubmit.setTrantype("3");
                                }
                                parameSubmit.setJumpnode("0");
                                parameSubmit.setBacemode("0");
                                parameSubmit.setSignatureid(okId);
                                httpSubmitRuest(parameSubmit);

                            }
                        } else {
                            ToastUtil.showShort(getActivity(), "密码输入不正确");
                        }
                    }
                }
            });

            passwordPopupWindow.setFocusable(true);
            passwordPopupWindow.setOutsideTouchable(true);
            passwordPopupWindow.update();
            passwordPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_spinner_shape));
            passwordPopupWindow.setAnimationStyle(R.style.Popupwindow);
            passwordPopupWindow.showAtLocation(((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0), Gravity.CENTER, 0, 0);
            PopWindowUtil.backgroundAlpha(getActivity(), 0.6F);
            passwordPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    PopWindowUtil.backgroundAlpha(getActivity(), 1.0F);
                }
            });
        } else {
            if (!passwordPopupWindow.isShowing()) {
                PopWindowUtil.backgroundAlpha(getActivity(), 0.6F);
                passwordPopupWindow.showAtLocation(((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0), Gravity.CENTER, 0, 0);
            } else {
                passwordPopupWindow.dismiss();
                PopWindowUtil.backgroundAlpha(getActivity(), 1.0F);
            }
        }
    }


    /**
     * 驳回弹出框
     * popupwindow
     */
    public void createBxPopupwindow() {
        if (bxPopupWindow == null) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View filterView = inflater.inflate(R.layout.popupwindow_bh, null);
            bxPopupWindow = new PopupWindow(filterView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            CardView next = filterView.findViewById(R.id.cv_next);
            CardView down = filterView.findViewById(R.id.cv_down);
            //逐步重走
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //3
                    bxPopupWindow.dismiss();
                    ReportTranParameSubmit parameSubmit = new ReportTranParameSubmit();


                    parameSubmit.setTransactionid(prame.getTranId());
                    parameSubmit.setConditionid(ctId);
                    parameSubmit.setSystemuserid(userId);
                    parameSubmit.setZduserids(tpId);
                    parameSubmit.setTcomment(approval);
                    //驳回
                    parameSubmit.setTrantype("2");
                    parameSubmit.setJumpnode(regresId);
                    parameSubmit.setBacemode("3");
                    parameSubmit.setSignatureid(okId);


                    httpSubmitRuest(parameSubmit);

                }
            });
            //一步重走
            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //4
                    bxPopupWindow.dismiss();
                    ReportTranParameSubmit parameSubmit = new ReportTranParameSubmit();
                    parameSubmit.setTransactionid(prame.getTranId());
                    parameSubmit.setConditionid(ctId);
                    parameSubmit.setSystemuserid(userId);
                    parameSubmit.setZduserids(tpId);
                    parameSubmit.setTcomment(approval);
                    //驳回
                    parameSubmit.setTrantype("2");
                    parameSubmit.setJumpnode(regresId);
                    parameSubmit.setBacemode("4");
                    parameSubmit.setSignatureid(okId);
                    httpSubmitRuest(parameSubmit);

                }
            });

            bxPopupWindow.setFocusable(true);
            bxPopupWindow.setOutsideTouchable(true);
            bxPopupWindow.update();
            bxPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_spinner_shape));
            bxPopupWindow.setAnimationStyle(R.style.Popupwindow);
            bxPopupWindow.showAtLocation(((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0), Gravity.CENTER, 0, 0);
            PopWindowUtil.backgroundAlpha(getActivity(), 0.6F);
            bxPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    PopWindowUtil.backgroundAlpha(getActivity(), 1.0F);
                }
            });
        } else {
            if (!bxPopupWindow.isShowing()) {
                PopWindowUtil.backgroundAlpha(getActivity(), 0.6F);
                bxPopupWindow.showAtLocation(((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0), Gravity.CENTER, 0, 0);
            } else {
                bxPopupWindow.dismiss();
                PopWindowUtil.backgroundAlpha(getActivity(), 1.0F);
            }
        }
    }

    private void  reIntent(){
        Intent intent = new Intent();
        intent.putExtra("ok","成功" );
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

}
