package com.example.administrator.thinker_soft.meter_code.sk.ui.security;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.SecurityAerateAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.SecurityAerateBelowAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.TextAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AetateBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AetateBelowBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AetatePrame;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NewsReviseParams;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.observer.ObserverListener;
import com.example.administrator.thinker_soft.meter_code.sk.observer.ObserverManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.DateFormatUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.ClearEditText;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.meter_code.sk.widget.UIHandler;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通气安检数据
 * Created by Administrator on 2018/8/1.
 */

public class SecurityAerateActivity extends BaseActivity implements RecyclerArrayAdapter.OnItemClickListener, OnLoadmoreListener, ObserverListener {
    private SecurityAerateAdapter mAdapter;//通气安检
    private SecurityAerateBelowAdapter belowAdapter;//安检不合格
    private TextAdapter adapter;//类型
    private int maxDay;//日期
    private String statTime, endTime;
    private boolean blChek = false;
    private UIMyHandler myHandler = new UIMyHandler(this);
    private String company_id;//公司id
    private TextView tvNumber;
    private int page=1;//页数
    private int count=50;//条数
    private int aetag=1;//通气安检
    private  PopupWindow   popupWindow;
    private int ctPosition;
    /**
     * 加载进度
     */
    private LoadingView loadingView;
    //存储
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    @BindView(R.id.tv_title)
    TextView title;//标题
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_time_choice)
    TextView timeChoice;//时间选择
    @BindView(R.id.et_content)
    ClearEditText editText;//搜索
    @BindView(R.id.tv_type)
    TextView type;//类型
    @BindView(R.id.no_data)
    LinearLayout noData;//空数据
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;//刷新控件
    @BindView(R.id.more)
    ImageView mageMore;//更多
    private AetatePrame prame;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_ay_aerate;
    }

    @Override
    protected void initView() {
        tvNumber = (TextView) findViewById(R.id.ic_nb).findViewById(R.id.tv_total_user_number);
        title.setText("通气安检");
        //获取用户id
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        company_id = String.valueOf(sharedPreferences_login.getInt("company_id", 0));//公司id
//        company_id = "5";

        mAdapter = new SecurityAerateAdapter(this);
        belowAdapter=new SecurityAerateBelowAdapter(this);

        // 设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
      //  mRefreshLayout.setRefreshHeader(new WaterDropHeader(this));
        mRefreshLayout.setPrimaryColorsId(R.color.theme_colors, android.R.color.white);
        mAdapter.setOnItemClickListener(this);
        belowAdapter.setOnItemClickListener(this);
       mRefreshLayout.setDisableContentWhenRefresh(true);  //是否在刷新的时候禁止内容的一切手势操作（默认false）
       mRefreshLayout.setDisableContentWhenLoading(true);  //是否在加载的时候禁止内容的一切手势操作（默认false）
        mRefreshLayout.setOnLoadmoreListener(this);//上拉加载
        mRefreshLayout.setEnableRefresh(false);
       mRecyclerView.requestFocus();

        endTime = DateFormatUtil.getCurrentTime();
        statTime = DateFormatUtil.DateToString(DateFormatUtil.getDateBefore(DateFormatUtil.StringToDate(endTime), 7));
        // timeChoice.setText(new StringBuffer().append(statTime).append("至").append(endTime).toString());
        timeChoice.setText("点击选择起止日期");
        prame = new AetatePrame();
        prame.setN_company_id(company_id);
//        prame.setEndtime(endTime);
//        prame.setStarttime(statTime);
        prame.setC_user_name("");
        prame.setPageIndex(page);
       prame.setPerPageCount(count);
        httoAerateRemark(prame);
        //注册
        ObserverManager.getInstance().add(this);
    }



    /**
     * 监听
     *
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_type, R.id.tv_time_choice, R.id.tv_search,R.id.more})
    public void OnclickAerate(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.tv_type:
                //类型选择
                showListWindow();

                break;
            case R.id.tv_time_choice:
                //选择时间
                showTimeWindow("选择开始时间");
                blChek = false;
                break;
            case R.id.tv_search:
                page=1;
                if (aetag==1){
                    mAdapter.clear();
                }else {
                    belowAdapter.clear();
                }

//                AetatePrame prame = new AetatePrame();
//                prame.setPageIndex(page);
//                prame.setPerPageCount(count);
//                prame.setN_company_id(company_id);//公司id
//                //搜索
//                String strType = type.getText().toString().trim();
//                switch (strType) {
//                    case "开户日期":
//                        prame.setEndtime(endTime);
//                        prame.setStarttime(statTime);
//                        break;
//                    case "姓名":
//                        prame.setC_user_name(editText.getText().toString().trim());
//                        break;
//                    case "用户编号":
//                        prame.setC_old_user_id(editText.getText().toString().trim());
//                        break;
//                    case "表编号":
//                        prame.setC_old_user_id(editText.getText().toString().trim());
//                        break;
//                    case "地址":
//                        prame.setC_user_address(editText.getText().toString().trim());
//                        break;
//                }
//
//                httoAerateRemark(prame);
                 setsearch();
                break;
            case R.id.more:
                //更多
                showPopupwindow();
                break;

            default:
                break;
        }

    }

    /**
     * 类型选择
     */
    private void showListWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(SecurityAerateActivity.this);
        View popView = layoutInflater.inflate(R.layout.popupwindow_text, null);
        final PopupWindow popListWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ListView listView = (ListView) popView.findViewById(R.id.listView);
        setData(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) adapter.getItem(position);
                type.setText(str);
                //开户日期
                if (str.equals("开户日期")) {
                    timeChoice.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.GONE);
                } else {
                    timeChoice.setVisibility(View.GONE);
                    editText.setVisibility(View.VISIBLE);
                }
                mAdapter.clear();
                popListWindow.dismiss();
            }
        });
        popListWindow.update();
        popListWindow.setFocusable(true);
        popListWindow.setTouchable(true);
        popListWindow.setOutsideTouchable(true);
        popListWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popListWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        PopWindowUtil.backgroundAlpha(SecurityAerateActivity.this, 0.6F);   //背景变暗
        popListWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        popListWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SecurityAerateActivity.this, 1.0F);
            }
        });

    }


    //系统设置popupwindow
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showPopupwindow() {
        if (popupWindow == null) {
        LayoutInflater  inflater = LayoutInflater.from(SecurityAerateActivity.this);
        View conView = inflater.inflate(R.layout.view_pup_window, null);
          popupWindow = new PopupWindow(conView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        RadioButton   aeration = (RadioButton) conView.findViewById(R.id.rb_aeration);//通气安检
        RadioButton   below = (RadioButton) conView.findViewById(R.id.rb_aerate_below);//通气安检不和格

//        if (aetag==1){
//            aeration.setChecked(true);
//        }else{
//            below.setChecked(true);
//        }
        aeration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                aetag=1;
                page=1;
                mAdapter.clear();
                belowAdapter.clear();
                mRecyclerView.setAdapter(mAdapter);
                setsearch();
            }
        });
        below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                aetag=2;
                page=1;
                belowAdapter.clear();
                mAdapter.clear();
                mRecyclerView.setAdapter(belowAdapter);
                setsearch();
            }
        });


        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAsDropDown(mageMore, -PopWindowUtil.dip2px(SecurityAerateActivity.this,68), 0);
        PopWindowUtil.backgroundAlpha(SecurityAerateActivity.this, 0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SecurityAerateActivity.this, 1.0F);
            }
        });
        }else {
            if (!popupWindow.isShowing()) {
                PopWindowUtil.backgroundAlpha(SecurityAerateActivity.this, 0.6F);
                popupWindow.showAsDropDown(mageMore, -PopWindowUtil.dip2px(SecurityAerateActivity.this,68), 0);
            }else{
                popupWindow.dismiss();
                PopWindowUtil.backgroundAlpha(SecurityAerateActivity.this, 1.0F);
            }
        }
    }

    /**
     * 搜索
     */
    private void setsearch() {
        AetatePrame prame = new AetatePrame();
        prame.setN_company_id(company_id);//公司id
        prame.setPageIndex(page);
        prame.setPerPageCount(count);
        //搜索
        String strType = type.getText().toString().trim();
        switch (strType) {
            case "开户日期":
                prame.setEndtime(endTime);
                prame.setStarttime(statTime);
                break;
            case "姓名":
                prame.setC_user_name(editText.getText().toString().trim());
                break;
            case "用户编号":
                prame.setC_user_id(editText.getText().toString().trim());
                break;
            case "表编号":
                prame.setC_old_user_id(editText.getText().toString().trim());
                break;
            case "地址":
                prame.setC_user_address(editText.getText().toString().trim());
                break;
             default:
                 break;
        }
        httoAerateRemark(prame);

    }

    /**
     * 添加数据
     */
    private void setData(ListView listView) {
        String[] array = new String[]{"开户日期", "姓名", "用户编号", "表编号", "地址"};
        adapter = new TextAdapter(this, array);
//        adapter.addAll(Arrays.asList(array));
        listView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(int position) {
        if (aetag == 1) {
            ctPosition = position;
          /*  Bundle extras = new Bundle();
            AetateBean.ListBean bean = mAdapter.getAllData().get(position);
            extras.putSerializable("aetate", bean);
            GoActivity(AerateDetailActivity.class, extras);*/
            AetateBean.ListBean bean = mAdapter.getAllData().get(position);
            Intent intent = new Intent(SecurityAerateActivity.this,  AerateDetailActivity.class);
            intent.putExtra("aetate",bean);
            startActivityForResult(intent, 15);
            //安检详情
        } else {
          /*  ctPosition = position;
            Bundle extras = new Bundle();
            AetateBean.ListBean bean = belowAdapter.getAllData().get(position);
            extras.putSerializable("aetate", bean);
            //安检详情
            GoActivity(AerateDetailActivity.class, extras);*/

            AetateBean.ListBean bean = belowAdapter.getAllData().get(position);
            Intent intent = new Intent(SecurityAerateActivity.this,  AerateDetailActivity.class);
            intent.putExtra("aetate",bean);
            startActivityForResult(intent, 16);
        }
    }
    /**
     * 通气安检
     */
    private void httoAerateRemark(AetatePrame prame) {
        if (page==1){
            //加载
            loadingView = new LoadingView(SecurityAerateActivity.this, R.style.LoadingDialog, "加载中...请稍后");
            loadingView.show();
        }
        String httpUrl = new StringBuffer().append(SkUrl.SkHttp(SecurityAerateActivity.this)).append("getSecurityCheck.do").toString();
        Request.Builder build;
        //aetag==1 通气安检，2 通气安检不合格
        if (aetag==1){
             build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("aerate")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("c_user_id", prame.getC_user_id() == null ? "" : prame.getC_user_id())
                .params("n_company_id", prame.getN_company_id())
                .params("c_user_name", prame.getC_user_name() == null ? "" : SkUrl.toURLEncoded(prame.getC_user_name()))
                .params("c_old_user_id", prame.getC_old_user_id() == null ? "" : prame.getC_old_user_id())
                .params("c_user_address", prame.getC_user_address() == null ? "" : SkUrl.toURLEncoded(prame.getC_user_address()))
                .params("starttime", prame.getStarttime() == null ? "" : prame.getStarttime())
                .params("endtime", prame.getEndtime() == null ? "" : prame.getEndtime())
                .params("pageIndex",prame.getPageIndex()+"")
                .params("perPageCount",prame.getPerPageCount()+"")
                .string("");
        }else {
             build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("aerate")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("c_user_id", prame.getC_user_id() == null ? "" : prame.getC_user_id())
                .params("n_company_id", prame.getN_company_id())
                .params("c_user_name", prame.getC_user_name() == null ? "" : SkUrl.toURLEncoded(prame.getC_user_name()))
                .params("c_old_user_id", prame.getC_old_user_id() == null ? "" : prame.getC_old_user_id())
                .params("c_user_address", prame.getC_user_address() == null ? "" : SkUrl.toURLEncoded(prame.getC_user_address()))
                .params("starttime", prame.getStarttime() == null ? "" : prame.getStarttime())
                .params("endtime", prame.getEndtime() == null ? "" : prame.getEndtime())
                .params("pageIndex",prame.getPageIndex()+"")
                .params("perPageCount",prame.getPerPageCount()+"")
                 .params("n_check_statu","0")
                .string("");
        }
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e("MeterRemarkActivity", "onComplete/response:" + response.getBody());
                Log.e("MeterRemarkActivity", "onComplete/response: content type=" + response.getContentType());
                if (page==1) {
                    loadDiss();
                }
                //通气安检
                if (aetag==1) {
                    AetateBean aetateBean = new Gson().fromJson(response.getBody(), AetateBean.class);
                //添加数据
                if (aetateBean.getList() != null) {
                    mAdapter.addAll(aetateBean.getList());
                    tvNumber.setText(mAdapter.getAllData().size() + "");
                }
                }else {
                    //不合格
                //    AetateBelowBean belowBean = new Gson().fromJson(response.getBody(), AetateBelowBean.class);
                    AetateBean aetateBean = new Gson().fromJson(response.getBody(), AetateBean.class);
                    if (aetateBean.getList() != null) {
                    belowAdapter.addAll(aetateBean.getList());
                tvNumber.setText(belowAdapter.getAllData().size() + "");
                }
                }


                //第一页
                if (page==1){
                    if (aetag==1) {
                        //空数据
                        if (mAdapter.getAllData().size() > 0) {
                            if (noData != null) {
                                noData.setVisibility(View.GONE);
                            }
                        } else {
                            if (noData != null) {
                                noData.setVisibility(View.VISIBLE);
                            }
                        }
                    }else {
                        //空数据
                        if (belowAdapter.getAllData().size() > 0) {
                            if (noData != null) {
                                noData.setVisibility(View.GONE);
                            }
                        } else {
                            if (noData != null) {
                                noData.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
                //第一页
                if (page==1) {
                    loadDiss();
                    if (noData != null) {
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


    /**
     * 时间选择
     */
    public void showTimeWindow(String strTitle) {
        LayoutInflater layoutInflater = LayoutInflater.from(SecurityAerateActivity.this);
        View mView = layoutInflater.inflate(R.layout.view_date_dialog, null);
        final PopupWindow popWindow = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        final TextView title = (TextView) mView.findViewById(R.id.tips);
        title.setText(strTitle);
        final RadioButton back = (RadioButton) mView.findViewById(R.id.rd_cancel);
        final RadioButton confirm = (RadioButton) mView.findViewById(R.id.rd_ok);
        final NumberPicker np1 = (NumberPicker) mView.findViewById(R.id.np1);
        final NumberPicker np2 = (NumberPicker) mView.findViewById(R.id.np2);
        final NumberPicker np3 = (NumberPicker) mView.findViewById(R.id.np3);

        //获取当前日期
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH) + 1;//月份是从0开始算的
        final int day = c.get(Calendar.DAY_OF_MONTH);

        //设置年份
        np1.setMaxValue(2999);
        np1.setValue(year); //中间参数 设置默认值
        np1.setMinValue(1900);

        //设置月份
        np2.setMaxValue(12);
        np2.setValue(month);
        np2.setMinValue(1);

        //设置天数
        np3.setMaxValue(31);
        np3.setValue(day);
        np3.setMinValue(1);

        //年份滑动监听
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.i("NumberPicker", "oldVal-----" + oldVal + "-----newVal-----" + newVal);
                //平年闰年判断
                if (newVal % 4 == 0) {
                    maxDay = 29;
                } else {
                    maxDay = 28;
                }
                //设置天数的最大值
                np3.setMaxValue(maxDay);
            }
        });

        //月份滑动监听
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.i("NumberPicker", "oldVal-----" + oldVal + "-----newVal-----" + newVal);
                //月份判断
                switch (newVal) {
                    case 2:
                        if (np1.getValue() % 4 == 0) {
                            maxDay = 29;
                        } else {
                            maxDay = 28;
                        }
                        break;
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        maxDay = 31;
                        break;
                    default:
                        maxDay = 30;
                        break;
                }
                //设置天数的最大值
                np3.setMaxValue(maxDay);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int years = np1.getValue();
                int months = np2.getValue();
                int days = np3.getValue();
                if (!blChek) {
                    popWindow.dismiss();
                    myHandler.sendEmptyMessage(0);
                    blChek = true;
                    statTime = years + "-" + months + "-" + days;
                    timeChoice.setText(statTime);
                } else {
                    endTime = years + "-" + months + "-" + days;
                    timeChoice.setText(statTime + "至" + endTime);
                    //范围
                    if (checkInput()) {
                        popWindow.dismiss();
                        if (aetag==1){
                            mAdapter.clear();
                        }else {
                            belowAdapter.clear();
                        }
                        setsearch();
                    }

                }

            }
        });
        popWindow.update();
        popWindow.setFocusable(true);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        PopWindowUtil.backgroundAlpha(SecurityAerateActivity.this, 0.6F);   //背景变暗
        popWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SecurityAerateActivity.this, 1.0F);
            }
        });
    }

    /**
     * 时间间隔
     *
     * @return
     */
    public boolean checkInput() {
        if (TextUtils.isEmpty(statTime)) {
            Toast.makeText(SecurityAerateActivity.this, "请输入开始时间~", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (TextUtils.isEmpty(endTime)) {
            Toast.makeText(SecurityAerateActivity.this, "请输入结束时间~", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dateStart = dateFormat.parse(statTime);
            Date dateEnd = dateFormat.parse(endTime);
            if (dateStart.getTime() > dateEnd.getTime()) {
                Toast.makeText(SecurityAerateActivity.this, "开始时间需要小于结束时间哦~~",
                        Toast.LENGTH_SHORT).show();

                return false;
            } else if (dateStart.getTime() >System.currentTimeMillis()) {
                Toast.makeText(SecurityAerateActivity.this, "开始时间大于现在时间哦~~",
                        Toast.LENGTH_SHORT).show();
                return false;
            } else if (dateEnd.getTime() > System.currentTimeMillis()) {
                Toast.makeText(SecurityAerateActivity.this, "结束时间大于现在时间哦~~",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(SecurityAerateActivity.this, "数据格式有误！", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;

    }

    /**
     * 上拉加载
     * @param refreshlayout
     */
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        refreshlayout.finishLoadmore(800);
        page++;
        setsearch();

    }

    @Override
    public void observerUpData(String content) {
        Log.e("观察",content);
        NewsReviseParams params=new Gson().fromJson(content,NewsReviseParams.class);
        AetateBean.ListBean bean = mAdapter.getAllData().get(ctPosition);
        bean.setYhmc(params.getC_user_name());
        bean.setLidh(params.getC_user_phone());
        bean.setQbbh(params.getC_meter_number());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ObserverManager.getInstance().remove(this);
    }

    /**
     * Handler
     */
    private static class UIMyHandler extends UIHandler<SecurityAerateActivity> {

        public UIMyHandler(SecurityAerateActivity cls) {
            super(cls);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SecurityAerateActivity activity = ref.get();
            if (activity != null) {
                if (activity.isFinishing()) {
                    return;
                }
                switch (msg.what) {
                    case 0:
                        activity.showTimeWindow("选择结束时间");
                        break;
                    case 1:
                        //选择时间
                        activity.showTimeWindow("选择开始时间");
                        activity.blChek = false;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 15) {
                Log.i("pgl", "通气页面回调进来了");

                if (data!=null){
                    String isUp=data.getStringExtra("isUpload");
                    if (isUp!=null && isUp.equals("成功")){
                        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                }
                aetag=1;
                page=1;
                mAdapter.clear();
                belowAdapter.clear();
                mRecyclerView.setAdapter(mAdapter);
                setsearch();
            }
            if (requestCode == 16) {
                Log.i("pgl", "通气不合格页面回调进来了");
                if (data!=null){
                    String isUp=data.getStringExtra("isUpload");
                    if (isUp!=null && isUp.equals("成功")){
                        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                }
                aetag=2;
                page=1;
                belowAdapter.clear();
                mAdapter.clear();
                mRecyclerView.setAdapter(belowAdapter);
                setsearch();
            }
        }
    }
}
