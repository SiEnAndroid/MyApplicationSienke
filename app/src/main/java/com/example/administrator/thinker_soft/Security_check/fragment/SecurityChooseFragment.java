package com.example.administrator.thinker_soft.Security_check.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.activity.DataTransferActivity;
import com.example.administrator.thinker_soft.Security_check.activity.SecurityStatisticsActivity;
import com.example.administrator.thinker_soft.Security_check.activity.UserListActivity;
import com.example.administrator.thinker_soft.Security_check.adapter.TaskChooseAdapter;
import com.example.administrator.thinker_soft.Security_check.model.TaskChoose;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.SecurityAbnormalActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.SecurityAddedActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.SecurityAerateActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.SecurityAuditActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.SecurityAuditUnqualifiedActivity;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16 0016.
 */
public class SecurityChooseFragment extends Fragment {
    private View view;
    //继续安检  //用户列表 //未检用户 //新建任务   //安检统计     //任务选择//安检计划 //安检情况
    private CardView userListCardview, taskCardview, newTaskCardview, statisticsCardview, transfer,cvCase;
    private SharedPreferences sharedPreferences,sharedPreferences_login ,shared;
    private LayoutInflater inflater;  //转换器
    private View taskView;
    private PopupWindow popupWindow;
    private ListView listView;
    private LinearLayout coordinatorLayout;
    private LinearLayout noData;
    private TaskChooseAdapter adapter;   //适配器
    private SQLiteDatabase db;  //数据库
    private MySqliteHelper helper; //数据库帮助类
    private List<TaskChoose> taskChooseList = new ArrayList<>();

    private CardView aerate;//通气安检
    private CardView abnormal;//安检异常
    private CardView abnormalAudit; // 异常审核

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_security, null);

        bindView();//绑定控件ID
        defaultSetting();//初始化设置
        setViewClickListener();//点击事件
        return view;
    }

    //绑定控件
    public void bindView() {
        coordinatorLayout = (LinearLayout) view.findViewById(R.id.coordinator_layout);
        userListCardview = (CardView) view.findViewById(R.id.user_list_cardview);
        taskCardview = (CardView) view.findViewById(R.id.task_cardview);
        newTaskCardview = (CardView) view.findViewById(R.id.new_task_cardview);
        statisticsCardview = (CardView) view.findViewById(R.id.statistics_cardview);
        transfer = (CardView) view.findViewById(R.id.transfer);
        aerate=(CardView) view.findViewById(R.id.cv_aerate);
        cvCase=(CardView)view.findViewById(R.id.cv_case);
        abnormal=(CardView) view.findViewById(R.id.cv_abnormal);
        abnormalAudit =(CardView) view.findViewById(R.id.abnormal_audit);
    }

    //初始化设置
    private void defaultSetting() {
        helper = new MySqliteHelper(getActivity(), MyApplication.DATA_BASE_VERSION);
        db = helper.getReadableDatabase();
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences(sharedPreferences_login.getString("userId","")+"data", Context.MODE_PRIVATE);
   
    }

    //点击事件
    private void setViewClickListener() {
        userListCardview.setOnClickListener(clickListener);
        taskCardview.setOnClickListener(clickListener);
        newTaskCardview.setOnClickListener(clickListener);
        statisticsCardview.setOnClickListener(clickListener);
        transfer.setOnClickListener(clickListener);
        aerate.setOnClickListener(clickListener);
        cvCase.setOnClickListener(clickListener);
        abnormal.setOnClickListener(clickListener);
        abnormalAudit.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.user_list_cardview:
                    //  安检
                    if (sharedPreferences.getBoolean("show_temp_data", false)) {  //显示演示数据
                        intent = new Intent(getActivity(), UserListActivity.class);
                        startActivity(intent);
                    }else {
                        String currentTaskId = sharedPreferences.getString("currentTaskId", "");
                        if (sharedPreferences.getString("currentTaskId","").equals("")) {
                            Snackbar.make(coordinatorLayout,"请您先选择任务！",Snackbar.LENGTH_SHORT).show();
                        }else {
                            intent = new Intent(getActivity(), UserListActivity.class);
                            startActivity(intent);
                        }
                    }
                    break;
                case R.id.task_cardview:
                    //  任务
                    taskChooseWindow();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            //读取下载到本地的任务数据
                            getTaskData(sharedPreferences_login.getString("userId", ""));
                        }
                    }.start();
                    break;
                case R.id.new_task_cardview:
                    //新增
//                    if (Tools.NetIsAvilable(getActivity())) {
    //              intent = new Intent(getActivity(), NewTaskActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(getActivity(), "网络未连接，请打开网络！", Toast.LENGTH_SHORT).show();
//                    }
                    intent = new Intent(getActivity(), SecurityAddedActivity.class);
                    startActivity(intent);

                    break;
                case R.id.statistics_cardview:
                    // 统计
                    Intent intent4 = new Intent(getActivity(), SecurityStatisticsActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.transfer:
                    //传输
                    intent = new Intent(getActivity(), DataTransferActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cv_aerate:
                    //通气安检
                    intent = new Intent(getActivity(), SecurityAerateActivity.class);
                    startActivity(intent);
                    break;
                case  R.id.cv_case:
                    //审核
                    intent = new Intent(getActivity(), SecurityAuditActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cv_abnormal:
                    //异常安检
                    intent = new Intent(getActivity(), SecurityAbnormalActivity.class);
                    startActivity(intent);
                    break;
                case R.id.abnormal_audit:
                    //异常审核
                    intent = new Intent(getActivity(), SecurityAuditUnqualifiedActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    //读取下载到本地的任务数据
    public void getTaskData(String loginUserId) {
        taskChooseList.clear();
        Cursor cursor = db.rawQuery("select * from Task where loginUserId=?", new String[]{loginUserId});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(0);
            return;
        }
        while (cursor.moveToNext()) {
            TaskChoose taskChoose = new TaskChoose();
            taskChoose.setTaskName(cursor.getString(cursor.getColumnIndex("taskName")));
            taskChoose.setTaskNumber(cursor.getString(cursor.getColumnIndex("taskId")));
            taskChoose.setCheckType(cursor.getString(cursor.getColumnIndex("securityType")));
            taskChoose.setTotalUserNumber(cursor.getString(cursor.getColumnIndex("totalCount")));
            taskChoose.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
            taskChoose.setRestCount("(" + cursor.getString(cursor.getColumnIndex("restCount")) + ")");
            taskChooseList.add(taskChoose);
        }
        //cursor游标操作完成以后,一定要关闭
        cursor.close();
        handler.sendEmptyMessage(1);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    noData.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "您还没有任务哦，快去下载吧！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    adapter = new TaskChooseAdapter(getActivity(), taskChooseList);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 选择任务窗口
     */
    public void taskChooseWindow() {
        inflater = LayoutInflater.from(getActivity());
        taskView = inflater.inflate(R.layout.poupwindow_task_select, null);
        popupWindow = new PopupWindow(taskView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        listView = (ListView) taskView.findViewById(R.id.list_view);
        noData = (LinearLayout) taskView.findViewById(R.id.no_data);
        LinearLayout confirmLayout = (LinearLayout) taskView.findViewById(R.id.confirm_layout);
        noData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {           //listview点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                TaskChoose item = (TaskChoose) adapter.getItem(position);
                sharedPreferences.edit().putString("currentTaskName",item.getTaskName()).apply();
                sharedPreferences.edit().putString("currentTaskId",item.getTaskNumber()).apply();

            }
        });
        confirmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(coordinatorLayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getActivity().getWindow().setAttributes(lp);
    }
}
