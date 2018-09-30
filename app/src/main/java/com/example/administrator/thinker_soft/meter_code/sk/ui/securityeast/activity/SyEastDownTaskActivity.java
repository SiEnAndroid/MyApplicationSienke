package com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;


import com.example.administrator.thinker_soft.meter_code.sk.adapter.SyTaskAddAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastTaskBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastUserBean;
import com.example.administrator.thinker_soft.meter_code.sk.db.DBManage;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpService;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SimpleDateUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtils;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.HttpRetrofit;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;

/**
 * 任务列表
 *
 * @author g
 * @FileName SyEastDownTaskActivity
 * @date 2018/9/29 15:00
 */
public class SyEastDownTaskActivity extends BaseActivity implements SyTaskAddAdapter.OnItemClickLitener {
    private SyTaskAddAdapter syTaskAddAdapter;
    private SyEastTaskBean taskBean;
    /**
     * 用于保存选中的信息
     */
    private List<SyEastTaskBean.TaskListBean> selectDatas = new ArrayList<>();
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    //设置标记
    private int index=0;

    /**
     * 进度条
     */
    private LoadingView loadingView;
    private SharedPreferences sharedPreferences_login;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_dy_down_task;
    }

    @Override
    protected void initView() {
        tvTitle.setText("安检计划");
        getIntentTask();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        db = MySqliteHelper.getInstance(this).getWritableDatabase();
        if (taskBean.getList().size() > 0) {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            syTaskAddAdapter = new SyTaskAddAdapter(taskBean.getList());
            // 设置布局管理器
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(syTaskAddAdapter);
            syTaskAddAdapter.setOnItemClickLitener(this);
        }

    }

    /**
     * 监听
     *
     * @param view
     */
    @OnClick({R.id.btn_down, R.id.back})
    public void onClickTask(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;

            case R.id.btn_down:
                //下载
                if (selectDatas.size()>0){
                    setRequest();
                }else {
                    ToastUtils.showShort(SyEastDownTaskActivity.this,"至少选择一个任务进行下载");
                }
               
                break;

            default:
                break;
        }


    }

    /**
     * 设置请求
     */
    private void setRequest() {
     //  String httpUrl="http://49.4.70.220:9470/SMDemo/getUserCheck.do?safetyPlan=44&safetyState=0";
        showLoadView();
         index=0;
        for (int i = 0; i < selectDatas.size(); i++) {
             final String securityIds = selectDatas.get(i).getN_ANJIAN_PLAN_ID()+"";
            final SyEastTaskBean.TaskListBean taskListBean= selectDatas.get(i);
            
            if (DBManage.getInstance(this).getTaskData(securityIds, sharedPreferences_login.getString("userId", ""))) {
                ToastUtils.showShort(SyEastDownTaskActivity.this, "编号为" + securityIds + "的任务本地已存在，不能重复下载哦");
                dissLoadView();
                return;
            }else {
                //下载安检用户
                HttpRetrofit.setrRequest(this, HttpRetrofit.getInstance(null).getApiServise(HttpService.class).getUserCheck(securityIds, "0"),
                        new HttpRetrofit.OnRequestCall<SyEastUserBean>() {

                            @Override
                            public void onSuccess(SyEastUserBean bean) {
                                if (index==selectDatas.size()-1){
                                    dissLoadView();
                                    createNoTaskPopup("安检任务下载成功");
                                }
                                index++;
                                
                              if (bean.getRows().size()>0&& bean.getTotal()!=0){
                                  //任务id
                               if(securityIds.equals(String.valueOf(taskListBean.getN_ANJIAN_PLAN_ID()))){
                                   insertTaskDataBase(taskListBean);
                                  }
                                  for (SyEastUserBean.UserRowsBean userBean :bean.getRows()) {
                                      insertUserDataBase(userBean);
                                  }
                              }
                                
                                
                            }
                            @Override
                            public void onError(String msg) {
                                Log.e("error", msg);
                                if (index==selectDatas.size()-1){
                                    dissLoadView();
                                }else {
                                    createNoTaskPopup("下载失败！");
                                }
                                index++;
                            }
                        });
            }
        }

    }
    /**
     * 没有任务下载的提示
     */
    public void createNoTaskPopup(String why) {
        final View taskView = LayoutInflater.from(SyEastDownTaskActivity.this).inflate(R.layout.popupwindow_no_task, null);
        final PopupWindow    taskWindow = new PopupWindow(taskView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        //任务提示
        TextView oneTips = taskView.findViewById(R.id.one_tips);
        oneTips.setText(why);
        TextView confirm = taskView.findViewById(R.id.confirm);
        //设置点击事件
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskWindow.dismiss(); 
          
            }
        });
        taskWindow.update();
        taskWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        taskWindow.setAnimationStyle(R.style.camera);
        //背景变暗
        PopWindowUtil.backgroundAlpha(SyEastDownTaskActivity.this, 0.6F);
        taskWindow.showAtLocation(taskView, Gravity.CENTER, 0, 0);
        taskWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SyEastDownTaskActivity.this, 1.0F);
            }
        });
    }
    /**
     * 加载进度
     */
    private void showLoadView() {
        //加载
        loadingView = new LoadingView(this, R.style.LoadingDialog, "下载中...请稍后");
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
     * 获取任务对象
     */
    private void getIntentTask() {

        Intent intent = getIntent();
        // 实例化一个Bundle  
        Bundle bundle = intent.getExtras();
        //获取里面的Persion里面的数据  
        taskBean = (SyEastTaskBean) bundle.getSerializable("task_bean");

    }

    @Override
    public void onItemClick(View view, int position, SyEastTaskBean.TaskListBean bean) {

        if (!SyTaskAddAdapter.getIsCheck().get(position)) {
            // 修改map的值保存状态
            SyTaskAddAdapter.getIsCheck().put(position, true);
            syTaskAddAdapter.notifyItemChanged(position);
            // 修改map的值保存状态
            selectDatas.add(bean);

        } else {
            // 修改map的值保存状态
            SyTaskAddAdapter.getIsCheck().put(position, false);
            syTaskAddAdapter.notifyItemChanged(position);
            // 修改map的值保存状态
            selectDatas.remove(bean);
        }
        btnDown.setText("下载(" + selectDatas.size() + ")任务");
    }


    /**
     * 任务数据存到本地数据库任务表
     */
    
    private void insertTaskDataBase(SyEastTaskBean.TaskListBean taskListBean) {
        ContentValues values = new ContentValues();
        //任务名称
        values.put("taskName",taskListBean.getC_ANJIAN_PLAN_NAME()==null?"暂无":taskListBean.getC_ANJIAN_PLAN_NAME());
        //任务id
        values.put("taskId",taskListBean.getN_ANJIAN_PLAN_ID()==0?"0":taskListBean.getN_ANJIAN_PLAN_ID()+"");
        //
        values.put("securityType",taskListBean.getC_ANJIAN_PLAN_NAME()==null?"暂无":taskListBean.getC_ANJIAN_PLAN_NAME());
        //安检总户数
        values.put("totalCount",taskListBean.getCOUNTRS()==0?"0":taskListBean.getCOUNTRS()+"");
        //未安检户数
        values.put("restCount",taskListBean.getREMAINCOUNTS()==0?"0":taskListBean.getREMAINCOUNTS()+"");
        //结束时间
        values.put("endTime",taskListBean.getD_ANJIAN_END()==0?"暂无": SimpleDateUtils.longTime(taskListBean.getD_ANJIAN_START())+"");
       //登录名称
        values.put("loginName", sharedPreferences_login.getString("login_name", ""));
        values.put("loginUserId", sharedPreferences_login.getString("userId", ""));
        db.insert("Task", null, values);
    }

    /**
     * 用户信息数据存到本地数据库用户表
     */

    private void insertUserDataBase(SyEastUserBean.UserRowsBean userBean ) {
        ContentValues values = new ContentValues();
        //任务id
        values.put("taskId",userBean.getSafetyPlan()==0?"暂无":userBean.getSafetyPlan()+"");
        //用户性质
        values.put("userProperty",userBean.getC_properties_name()==null?"暂无":userBean.getC_properties_name());
        //开始时间
        values.put("lastCheckTime",userBean.getD_safety_inspection_date()==null?"暂无":userBean.getD_safety_inspection_date());
        //安检类型
        values.put("securityType",userBean.getSecurityName()==null?"暂无":userBean.getSecurityName());
        //用户编号
        values.put("meterNumber",userBean.getMeterNumber()==null?"暂无":userBean.getMeterNumber());
        //老编号
        values.put("oldUserId",userBean.getOldUserId()==null?"暂无":userBean.getOldUserId());
        //手机号
        values.put("userPhone",userBean.getUserPhone()==null?"暂无":userBean.getUserPhone());
        //用户名称
        values.put("userName",userBean.getUserName()==null?"暂无":userBean.getUserName());
        //用户地址
        values.put("userAddress",userBean.getUserAdress()==null?"暂无":userBean.getUserAdress());
        //用户新编号
        values.put("newUserId",userBean.getUserId()==null?"暂无":userBean.getUserId());
        //sum_dosage
        values.put("sum_dosage", userBean.getN_buy_sum_dosage()==null?"暂无":userBean.getN_buy_sum_dosage());
        values.put("ifChecked", "false");
        values.put("security_case", "");
        values.put("remarks", "");
        values.put("security_hidden", "");
        values.put("security_hidden_reason", "");
        values.put("security_case_id", "");
        values.put("security_hidden_id", "");
        values.put("security_hidden_reason_id", "");
        values.put("photoNumber", "0");
        values.put("ifUpload", "false");
        values.put("currentTime", "");
        values.put("ifPass", "");
        values.put("loginName", sharedPreferences_login.getString("login_name", ""));
        values.put("loginUserId", sharedPreferences_login.getString("userId", ""));
        values.put("security_state", "0");
        db.insert("User", null, values);
        Log.e("插入","成功--");

    }

}
