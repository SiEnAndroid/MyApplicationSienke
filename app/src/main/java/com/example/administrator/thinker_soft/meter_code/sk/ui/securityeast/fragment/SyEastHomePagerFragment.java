package com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.activity.IpSettingActivity;
import com.example.administrator.thinker_soft.Security_check.activity.UserListActivity;
import com.example.administrator.thinker_soft.Security_check.model.PopupwindowListItem;
import com.example.administrator.thinker_soft.Security_check.model.TaskChoose;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.MultiAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.OnItemClickLitener;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.SyEastMultiAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastTaskBean;
import com.example.administrator.thinker_soft.meter_code.sk.db.DBManage;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpService;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseFragment;
import com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.activity.AuditDonYuAcivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.activity.NewDonYuAcivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.activity.NotThroughDonYuAcivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.activity.SecurityExceptionsDonYuAcivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.activity.SyEastDownTaskActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.HttpRetrofit;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import butterknife.OnClick;



/**
 * @author g
 * @FileName SyEastHomePagerFragment
 * @date 2018/9/28 16:09
 */
public class SyEastHomePagerFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 弹出框
     */
    private PopupWindow downWindow, taskWindow,popupTkWindow;
    private RecyclerView downRecyclerView;
    /**
     * 日历
     */
    private Calendar calendar;
    /**
     * 多选适配
     */
    private MultiAdapter multiAdapter;
    private List<PopupwindowListItem> stateList;
    /**
     * 用于保存选中的信息
     */
    private List<PopupwindowListItem> selectDatas = new ArrayList<>();
    /**
     * 空数据
     */
    private TextView noData;
    /**
     * 日期选择
     */
    private TextView startDateTv, endDateTv;

    private SharedPreferences sharedPreferences_login;
    private  SharedPreferences sharedPreferences;
    private String companyId, userId, userName;
    /**
     * 进度条
     */
    private LoadingView loadingView;
    private Intent intent;
    /**任务选择*/
    private RecyclerView tkRecyclerView;
    private SyEastMultiAdapter syEastMultiAdapter;
    private int select=0;

    /**
     * 实例
     *
     * @return
     */
    public static SyEastHomePagerFragment newInstance() {
        SyEastHomePagerFragment fragment = new SyEastHomePagerFragment();
        return fragment;
    }

    @Override
    public void onLazyLoad() {
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_dy_home_pager;
    }

    @Override
    protected void initView() {
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
      
        //公司id
        companyId = String.valueOf(sharedPreferences_login.getInt("company_id", 0));
        userId = sharedPreferences_login.getString("userId", "");
        sharedPreferences = getActivity().getSharedPreferences(userId+"data", Context.MODE_PRIVATE);
        userName = sharedPreferences_login.getString("user_name", "");
    }

    /**
     * 监听
     *
     * @param view
     */
    @OnClick({R.id.cd_aj, R.id.cd_rw, R.id.cd_tj,
            R.id.cd_add, R.id.cd_sh, R.id.cd_wtg,
            R.id.cd_yc, R.id.cd_up, R.id.cd_down, R.id.cd_check})
    public void onClickPage(View view) {
        switch (view.getId()) {
            case R.id.cd_aj:
                //安检
                String cerentId= sharedPreferences.getString("currentTaskId","");
                if (TextUtils.isEmpty(cerentId)){
                    Snackbar.make(view,"请您先选择任务！",Snackbar.LENGTH_SHORT).show();
                }else {
                    GoActivity(UserListActivity.class);
                }
                break;
            case R.id.cd_rw:
                //任务
                taskChooseWindow();
                break;
            case R.id.cd_tj:
                //统计

                break;
            case R.id.cd_add:
                //新增
                GoActivity(NewDonYuAcivity.class);
                break;
            case R.id.cd_sh:
                //审核
                GoActivity(AuditDonYuAcivity.class);
                break;
            case R.id.cd_wtg:
                //安检未通过录入
                GoActivity(NotThroughDonYuAcivity.class);
                break;
            case R.id.cd_yc:
                //安检异常
                GoActivity(SecurityExceptionsDonYuAcivity.class);
                break;
            case R.id.cd_up:
                //上传

                break;
            case R.id.cd_down:
                //下载
                showDownPopup();
                break;
            case R.id.cd_check:
                //查询

                break;

            default:
                break;
        }


    }
    /**
     * 加载进度
     */
    private void showLoadView() {
        //加载
        loadingView = new LoadingView(getActivity(), R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();
    }

    /**
     * 关闭进度
     */
    private void dissLoadView() {
        if (loadingView != null) {
            loadingView.dismiss();
        }
    }
    /**
     * 设置请求
     */
    private void setRequest() {
        showLoadView();
        String stDate = startDateTv.getText().toString().trim();
        String edDate = endDateTv.getText().toString().trim();
        //安检计划
        HttpRetrofit.setrRequest(getActivity(), HttpRetrofit.getInstance(null)
                        .getApiServise(HttpService.class)
                        .getSecurityTask(companyId, userName, stDate, edDate),
                new HttpRetrofit.OnRequestCall<SyEastTaskBean>() {

                    @Override
                    public void onSuccess(SyEastTaskBean bean) {
                        dissLoadView();
                        //成功
                        if (bean.getStatus() != null && bean.getStatus().equals("success") && bean.getList().size() > 0) {
                            // 实例化一个Bundle  
                            Bundle bundle1 = new Bundle();
                            // 把Persion数据放入到bundle中  
                            bundle1.putSerializable("task_bean", bean);
                            GoActivity(SyEastDownTaskActivity.class, bundle1);
                        } else {
                            createNoTaskPopup(bean.getMsg());
                        }

                    }

                    @Override
                    public void onError(String msg) {
                        Log.e("error", msg);
                        dissLoadView();
                        createNoTaskPopup("服务器错误");
                        
                    }
                });
    }

    /**
     * 没有任务下载的提示
     */
    public void createNoTaskPopup(String why) {
        View taskView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_no_task, null);
        taskWindow = new PopupWindow(taskView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        //任务提示
        TextView oneTips = taskView.findViewById(R.id.one_tips);
        oneTips.setText("     "+why+"    ");
        TextView confirm = taskView.findViewById(R.id.confirm);
        //设置点击事件
        confirm.setOnClickListener(this);
        taskWindow.update();
        taskWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        taskWindow.setAnimationStyle(R.style.camera);
        //背景变暗
        PopWindowUtil.backgroundAlpha(getActivity(), 0.6F);
        taskWindow.showAtLocation(taskView, Gravity.CENTER, 0, 0);
        taskWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(getActivity(), 1.0F);
            }
        });
    }


    /**
     * 下载筛选窗口
     */
    public void showDownPopup() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_download_dy, null);
        downWindow = new PopupWindow(view, MyApplication.screenWidth - 80, LinearLayout.LayoutParams.WRAP_CONTENT);
        downRecyclerView = view.findViewById(R.id.recycler_view);
        noData = view.findViewById(R.id.no_data);
        CardView startData = view.findViewById(R.id.start_date);
        startDateTv = view.findViewById(R.id.start_date_tv);
        CardView endDate = view.findViewById(R.id.end_date);
        endDateTv = view.findViewById(R.id.end_date_tv);
        RadioButton cancelFilter = view.findViewById(R.id.cancel_filter);
        RadioButton downFilter = view.findViewById(R.id.down_filter);
        setDownAdapter();

        startData.setOnClickListener(this);
        endDate.setOnClickListener(this);
        cancelFilter.setOnClickListener(this);
        downFilter.setOnClickListener(this);
        //设置点击事件
        calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        startDateTv.setText(dateFormat.format(new Date()));
        endDateTv.setText(dateFormat.format(new Date()));
        downWindow.update();
        downWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        downWindow.setAnimationStyle(R.style.camera);
        downWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        //背景变暗
        PopWindowUtil.backgroundAlpha(getActivity(), 0.6F);
        downWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(getActivity(), 1.0F);
            }
        });
    }

    /**
     * 安检状态Adapter
     */
    private void setDownAdapter() {
        //设置颜色分割线
        StaggeredGridLayoutManager layoutmanager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //设置RecyclerView 布局
        downRecyclerView.setLayoutManager(layoutmanager);
        //  获取安检状态
        HttpRetrofit.readOR(DBManage.getInstance(getActivity()).getSyState(), new HttpRetrofit.OnRequestCall<List<PopupwindowListItem>>() {
            @Override
            public void onSuccess(List<PopupwindowListItem> msg) {
                stateList = msg;
                multiAdapter = new MultiAdapter(msg);
                downRecyclerView.setAdapter(multiAdapter);
                multiAdapter.setOnItemClickLitener(new OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (!MultiAdapter.isSelected.get(position)) {
                            // 修改map的值保存状态
                            MultiAdapter.isSelected.put(position, true);
                            multiAdapter.notifyItemChanged(position);
                            selectDatas.add(stateList.get(position));
                        } else {
                            // 修改map的值保存状态
                            MultiAdapter.isSelected.put(position, false);
                            multiAdapter.notifyItemChanged(position);
                            selectDatas.remove(stateList.get(position));
                        }
                    }
                });
                Log.e("数据", new Gson().toJson(msg));
            }

            @Override
            public void onError(String msg) {
                noData.setVisibility(View.VISIBLE);
            }
        });


    }

    /**
     * 任务选择
     */
    private void setTaskAdapter(final TextView noData){
        //设置颜色分割线

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //设置RecyclerView 布局
        tkRecyclerView.setLayoutManager(mLayoutManager);
        //  获取安检状态
        HttpRetrofit.readOR(DBManage.getInstance(getActivity()).getTaskDataList(sharedPreferences_login.getString("userId", "")), new HttpRetrofit.OnRequestCall<List<TaskChoose>>() {
            @Override
            public void onSuccess(final List<TaskChoose> msg) {
                Log.e("数据", new Gson().toJson(msg));
                if (msg.size()>0){
                    noData.setVisibility(View.GONE);
                    syEastMultiAdapter = new SyEastMultiAdapter(msg);
                    tkRecyclerView.setAdapter(syEastMultiAdapter);
                    syEastMultiAdapter.setSelection(select);
                    syEastMultiAdapter.setOnItemClickLitener(new OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            syEastMultiAdapter.setSelection(position);
                            //选择的项
                            select=position;
                            //保存任务数据
                            TaskChoose item=syEastMultiAdapter.getmFilterList().get(position);
                            sharedPreferences.edit().putString("currentTaskName",item.getTaskName()).apply();
                            sharedPreferences.edit().putString("currentTaskId",item.getTaskNumber()).apply();
                            //  sharedPreferences.edit().putString("currentEndTime",item.getEndTime());
                            if (popupTkWindow!=null){
                                popupTkWindow.dismiss();
                            }
                            createNoTaskPopup("选择了'"+item.getTaskName()+"'任务");
                        }
                    });
                }else {
                    noData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String msg) {
                noData.setVisibility(View.VISIBLE);
            }
        });
        
    }

    /**
     * 点击
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_date:
                //开始时间
                String startTitle = "开始时间";
                showTimeWindow(startTitle, 0);
                break;
            case R.id.end_date:
                //结束时间
                String endTitle = "结束时间";
                showTimeWindow(endTitle, 1);

                break;
            case R.id.cancel_filter:
                //取消
                if (downWindow != null) {
                    downWindow.dismiss();
                }

                break;
            case R.id.down_filter:
                //下载
                if (downWindow != null) {
                    downWindow.dismiss();
                }
                
                 setRequest();
                break;
            case R.id.confirm:
                //取消
                TaskDiss();
              
                break;
            default:
                break;

        }

    }

    /**
     * 取消任务框
     */
    private void TaskDiss(){
        //取消
        if (taskWindow != null) {
            taskWindow.dismiss();
        }
    }
    /**
     * 时间选择
     */
    public void showTimeWindow(String strTitle, final int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View mView = layoutInflater.inflate(R.layout.view_date_dialog, null);
        final PopupWindow popWindow = new PopupWindow(mView, MyApplication.screenWidth - 80, LinearLayout.LayoutParams.WRAP_CONTENT);
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
        //月份是从0开始算的
        final int month = c.get(Calendar.MONTH) + 1;
        final int day = c.get(Calendar.DAY_OF_MONTH);

        //设置年份
        np1.setMaxValue(2999);
        //中间参数 设置默认值
        np1.setValue(year);
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
            public int maxDay;

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
            public int maxDay;

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

                popWindow.dismiss();
                if (i == 0) {
                    startDateTv.setText(years + "-" + month1 + "-" + day1);
                } else if (i == 1) {
                    endDateTv.setText(years + "-" + month1 + "-" + day1);
                }

            }
        });
        popWindow.update();
        popWindow.setFocusable(true);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        PopWindowUtil.backgroundAlpha(getActivity(), 0.6F);
        popWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(getActivity(), 1.0F);
            }
        });
    }

    /**
     * 选择任务窗口
     */
    public void taskChooseWindow() {
       View taskView = LayoutInflater.from(getActivity()).inflate(R.layout.poupwindow_task_dy_select, null);
        popupTkWindow = new PopupWindow(taskView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        tkRecyclerView =  taskView.findViewById(R.id.recyclerView);
        TextView  noDataTask =  taskView.findViewById(R.id.tv_no_data);
        LinearLayout confim = taskView.findViewById(R.id.confirm_layout);


        setTaskAdapter(noDataTask);
        confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupTkWindow.dismiss();
            }
        });
        popupTkWindow.setFocusable(true);
        popupTkWindow.setOutsideTouchable(true);
        popupTkWindow.update();
        popupTkWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupTkWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupTkWindow.showAtLocation(taskView, Gravity.CENTER, 0, 0);
        PopWindowUtil.backgroundAlpha(getActivity(), 0.6F);
        popupTkWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(getActivity(), 1.0F);
            }
        });
    }

}
