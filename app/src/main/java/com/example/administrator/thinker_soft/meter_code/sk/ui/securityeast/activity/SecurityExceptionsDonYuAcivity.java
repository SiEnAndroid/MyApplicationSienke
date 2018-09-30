package com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.example.administrator.thinker_soft.meter_code.sk.adapter.NewUsersAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.SecurityAbnormalAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.SecurityExceptionsDonYuAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.TextAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NewUsersBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityChecksBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityExceptionsDonYuBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityExceptionsDonYuPrame;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.SecurityAbnormalActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.SecurityAbnormalInfoActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.DateFormatUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.JsonAnalyUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.ClearEditText;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 东渝 的 异常安检
 *
 * @author 111
 */
public class SecurityExceptionsDonYuAcivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.clear)
    TextView clear;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_time_choice)
    TextView tvTimeChoice;
    @BindView(R.id.et_search)
    ClearEditText etSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.no_data)
    LinearLayout noData;
    private TextAdapter adapter;
    private String endTime;
    private String statTime;
    private SecurityExceptionsDonYuPrame prame;
    private SharedPreferences sharedPreferences_login;
    private String userID;
    private String loginName;
    private SharedPreferences sharedPreferences;
    private String companyId;
    private boolean blChek = false;
    private int maxDay;
    private LoadingView loadingView;

    @Override
    protected int getContentViewID() {
        return R.layout.acivity_security_exceptions_don_yu;
    }

    @Override
    protected void initView() {
        clear.setText("刷新");
        tvTitle.setText("异常安检");
        defaultSetting();
    }

    private void defaultSetting() {
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        userID = sharedPreferences_login.getString("userId", "");
        loginName = sharedPreferences_login.getString("login_name", "");
        sharedPreferences = getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        companyId = String.valueOf(sharedPreferences_login.getInt("company_id", 0));//公司id

        initRequest();
    }

    private void initRequest() {
        endTime = DateFormatUtil.getCurrentTime();
        statTime = DateFormatUtil.DateToString(DateFormatUtil.getDateBefore(DateFormatUtil.StringToDate(endTime), 7));
        tvTimeChoice.setText(new StringBuffer().append(statTime).append("至").append(endTime).toString());
        /*
        n_company_id                公司id    必传
		c_user_id                   用户编号  不必传
		c_user_name                 用户名    不必传
		n_anjian_plan               计划编号  不必传
		d_anjian_inspection_date_start    安检开始时间   不必传
		d_anjian_inspection_date_end      安检结束时间   不必传
		d_anjian_start                    计划开始时间   不必传
		d_anjian_end                      计划结束时间   不必传
		*/
        prame = new SecurityExceptionsDonYuPrame();
        prame.setnCompanyId(companyId);
        prame.setdAnjianInspectionDateEnd(endTime);
        prame.setdAnjianInspectionDateStart(statTime);
        // 网络请求初始数据
        setsearch();
    }


    /**
     * 监听
     *
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_type, R.id.tv_time_choice, R.id.clear,R.id.et_search})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.clear:
                // 网络请求 刷新数据
                SecurityExceptionsDonYuPrame prame = new SecurityExceptionsDonYuPrame();
                prame.setnCompanyId(companyId);//公司id
                SecurityExceptionsDonYuRequest(prame);
                break;
            case R.id.tv_type:
                //类型选择
                showListWindow();
                break;
            case R.id.tv_time_choice:
                //选择时间
                blChek = false;
                showTimeWindow("选择开始时间");
                break;
            case R.id.et_search:
                setsearch();
                break;
            default:
                break;
        }
    }

    /**
     * 类型选择
     */
    private void showListWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(SecurityExceptionsDonYuAcivity.this);
        View popView = layoutInflater.inflate(R.layout.popupwindow_text, null);
        final PopupWindow popListWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ListView listView = (ListView) popView.findViewById(R.id.listView);
        setDataPop(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) adapter.getItem(position);
                tvType.setText(str);
                //开户日期
                if (str.equals("安检时间")) {
                    etSearch.setVisibility(View.GONE);
                    tvTimeChoice.setVisibility(View.VISIBLE);
                } else if (str.equals("计划时间")) {
                    etSearch.setVisibility(View.GONE);
                    tvTimeChoice.setVisibility(View.VISIBLE);
                } else {
                    tvTimeChoice.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                }
//                datas.clear();
                popListWindow.dismiss();
            }
        });
        popListWindow.update();
        popListWindow.setFocusable(true);
        popListWindow.setTouchable(true);
        popListWindow.setOutsideTouchable(true);
        popListWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popListWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        //背景变暗
        PopWindowUtil.backgroundAlpha(SecurityExceptionsDonYuAcivity.this, 0.6F);
        popListWindow.showAtLocation(SecurityExceptionsDonYuAcivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        popListWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SecurityExceptionsDonYuAcivity.this, 1.0F);
            }
        });
    }

    /**
     * 添加数据
     */
    private void setDataPop(ListView listView) {
        String[] array = new String[]{"安检时间", "计划时间", "计划编号", "用户名", "用户编号"};
        adapter = new TextAdapter(SecurityExceptionsDonYuAcivity.this, array);
//        adapter.addAll(Arrays.asList(array));
        listView.setAdapter(adapter);

    }


    /**
     * 搜索
     */
    private void setsearch() {
        SecurityExceptionsDonYuPrame prame = new SecurityExceptionsDonYuPrame();
        prame.setnCompanyId(companyId);//公司id
        //搜索
        String strType = tvType.getText().toString().trim();
        switch (strType) {
            case "安检时间":
                prame.setdAnjianInspectionDateEnd(endTime);
                prame.setdAnjianInspectionDateStart(statTime);
                break;
            case "计划时间":
                prame.setdAnjianEnd(endTime);
                prame.setdAnjianStart(statTime);
                break;
            case "计划编号":
                prame.setnAnjianPlan(etSearch.getText().toString().trim());
                break;
            case "用户名":
                prame.setcUserName(etSearch.getText().toString().trim());
                break;
            case "用户编号":
                prame.setcUserId(etSearch.getText().toString().trim());
                break;
            default:
                break;
        }
        SecurityExceptionsDonYuRequest(prame);
    }

    /**
     * 网络请求 东渝 异常安检 下拉列表数据
     *
     * @param prame
     */
    private void SecurityExceptionsDonYuRequest(SecurityExceptionsDonYuPrame prame) {
        //加载
        loadingView = new LoadingView(this, R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();
        final String httpUrl = new StringBuffer().append(SkUrl.SkHttp(this)).append("getDyYcaj.do").toString();
        Log.e("pgl","==="+httpUrl);
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("aerate")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("c_anjian_inspection_member",loginName)
                .params("n_company_id",companyId)
                .params("c_user_id",prame.getcUserId()==null?"":prame.getcUserId())
                .params("c_user_name",prame.getcUserName()==null?"":prame.getcUserName())
                .params("n_anjian_plan",prame.getnAnjianPlan()==null?"":prame.getnAnjianPlan())
                .params("d_anjian_inspection_date_start",prame.getdAnjianInspectionDateStart()==null?"":prame.getdAnjianInspectionDateStart())
                .params("d_anjian_inspection_date_end",prame.getdAnjianInspectionDateEnd()==null?"":prame.getdAnjianInspectionDateEnd())
                .params("d_anjian_start",prame.getdAnjianStart()==null?"":prame.getdAnjianStart())
                .params("d_anjian_end",prame.getdAnjianEnd()==null?"":prame.getdAnjianEnd())
                .string("");
        /*
        * 	上传数据:
		c_anjian_inspection_member  安检员   (安检员必传，超级管理员不用)
		n_company_id                公司id    必传
		c_user_id                   用户编号  不必传
		c_user_name                 用户名    不必传
		n_anjian_plan               计划编号  不必传
		d_anjian_inspection_date_start    安检开始时间   不必传
		d_anjian_inspection_date_end      安检结束时间   不必传
		d_anjian_start                    计划开始时间   不必传
		d_anjian_end                      计划结束时间   不必传
		*/
        Request.newRequest(build, new HttpCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(Response response) {
                Log.e("pgl","==="+response.getBody());
                SecurityExceptionsDonYuBean resultBean = new Gson().fromJson(response.getBody(),SecurityExceptionsDonYuBean.class);

                if ("查询成功".equals(resultBean.getMsg())){
                    loadingView.dismiss();
                    if (resultBean.getList().size()>0){
                        noData.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        recyclerView.setLayoutManager(new LinearLayoutManager(SecurityExceptionsDonYuAcivity.this, LinearLayoutManager.VERTICAL, false));
                        //初始化适配器
                        SecurityExceptionsDonYuAdapter mAdapter = new SecurityExceptionsDonYuAdapter(resultBean);
                        //设置适配器
                        recyclerView.setAdapter(mAdapter);
                        //设置添加或删除item时的动画，这里使用默认动画
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                       /* mAdapter.setOnItemClickListener(new SecurityAbnormalAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, SecurityChecksBean.ListBean data) {
                                currentPosition = position;
                                Intent intent = new Intent(SecurityExceptionsDonYuAcivity.this,  SecurityAbnormalInfoActivity.class);
                                intent.putExtra("listBean",data);
                                startActivityForResult(intent, 9);
                            }
                        });*/
                    }else {
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
                Toast.makeText(SecurityExceptionsDonYuAcivity.this, "网络请求异常", Toast.LENGTH_SHORT).show();
                loadingView.dismiss();
            }
        }).executeAsync();

    }


    /**
     * 时间选择
     */
    public void showTimeWindow(String strTitle) {
        LayoutInflater layoutInflater = LayoutInflater.from(SecurityExceptionsDonYuAcivity.this);
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
        final int month = c.get(Calendar.MONTH) + 1; //月份是从0开始算的
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
                String month1;
                String day1;
                if (months < 10) {
                    month1 = "0" + months;
                } else {
                    month1 = months + "";
                }
                if (days < 10) {
                    day1 = "0" + days;
                } else {
                    day1 = "" + days;
                }

                if (!blChek) {
                    popWindow.dismiss();
                    handler.sendEmptyMessage(0);
                    blChek = true;
                    statTime = years + "-" + month1 + "-" + day1;
                    tvTimeChoice.setText(statTime);
                    Log.e("pgl", "===第一次点击");
                } else {
                    Log.e("pgl", "==1111=选择结束时间");
                    endTime = years + "-" + month1 + "-" + day1;
                    tvTimeChoice.setText(statTime + "至" + endTime);
                    //范围
                    if (checkInput()) {
                        Log.e("pgl", "==2222=选择结束时间");
                        popWindow.dismiss();

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
        PopWindowUtil.backgroundAlpha(SecurityExceptionsDonYuAcivity.this, 0.6F);   //背景变暗
        popWindow.showAtLocation(SecurityExceptionsDonYuAcivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SecurityExceptionsDonYuAcivity.this, 1.0F);
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
            Toast.makeText(SecurityExceptionsDonYuAcivity.this, "请输入开始时间~", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (TextUtils.isEmpty(endTime)) {
            Toast.makeText(SecurityExceptionsDonYuAcivity.this, "请输入结束时间~", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dateStart = dateFormat.parse(statTime);
            Date dateEnd = dateFormat.parse(endTime);
            if (dateStart.getTime() > dateEnd.getTime()) {
                Toast.makeText(SecurityExceptionsDonYuAcivity.this, "开始时间需要小于结束时间哦~~",
                        Toast.LENGTH_SHORT).show();

                return false;
            } else if (dateStart.getTime() > System.currentTimeMillis()) {
                Toast.makeText(SecurityExceptionsDonYuAcivity.this, "开始时间大于现在时间哦~~",
                        Toast.LENGTH_SHORT).show();
                return false;
            } else if (dateEnd.getTime() > System.currentTimeMillis()) {
                Toast.makeText(SecurityExceptionsDonYuAcivity.this, "结束时间大于现在时间哦~~",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(SecurityExceptionsDonYuAcivity.this, "数据格式有误！", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showTimeWindow("选择结束时间");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


}
