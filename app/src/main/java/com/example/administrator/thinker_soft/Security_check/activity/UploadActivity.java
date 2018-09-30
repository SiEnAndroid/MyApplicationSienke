package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.adapter.UplaodFailedListAdapter;
import com.example.administrator.thinker_soft.Security_check.adapter.UploadListViewAdapter;
import com.example.administrator.thinker_soft.Security_check.model.UploadFailedItem;
import com.example.administrator.thinker_soft.Security_check.model.UploadListViewItem;
import com.example.administrator.thinker_soft.Security_check.model.UploadViewHolder;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.mode.HttpUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/16.
 */
public class UploadActivity extends Activity {
    private ImageView back;
    private TextView noData;
    private ListView listView;
    private LinearLayout uploadTaskSelectLayout,upLoad;
    private List<UploadListViewItem> uploadListViewItemList = new ArrayList<>();
    private HashMap<String, String> map = new HashMap<String, String>();
    private HashMap<String, Object> map1;
    //private String ip, port;  //接口ip地址   端口
    private SQLiteDatabase db;  //数据库
    private UploadListViewAdapter adapter;   //适配器
    private MySqliteHelper helper; //数据库帮助类
    private String checkState;
    private SharedPreferences sharedPreferences, sharedPreferences_login, public_sharedPreferences;
    private String defaul = "";//默认的全部不勾选
    private TextView selectAll, reverse, selectCancel;
    private HttpUtils httpUtils;
    private String newUserId;
    private Map<String, File> fileMap;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private TextView title, tips, totalCount, ccurrentCount, confirm;  //加载进度的提示
    private ListView resultListview;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private View loadingView, line;
    private LinearLayout rootLinearlayout, progressLayout;
    private int uploadTotalCount;  //上传总用户数
    private int currentUploadNumber = 0;//记录已上传的用户数
    private int noCheckNumber = 0;//记录未安检用户数
    private List<UploadFailedItem> failedItemList = new ArrayList<>();
    private UplaodFailedListAdapter failedListAdapter;
    private boolean isCompleted = false;    //记录是否上传完成

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        defaultSetting();//初始化设置
        //绑定控件
        bindView();
        //获取数据
        new Thread() {
            @Override
            public void run() {
                getTaskData(sharedPreferences_login.getString("userId", ""));//读取下载到本地的任务数据
                super.run();
            }
        }.start();
        setViewClickListener();//点击事件
    }


    //初始化设置
    private void defaultSetting() {
        helper = new MySqliteHelper(UploadActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getReadableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        public_sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        httpUtils = new HttpUtils();
        //初始化勾选框信息，默认都是以未勾选为单位
        for (int i = 0; i < uploadListViewItemList.size(); i++) {
            defaul = defaul + "0";
        }
    }

    //获得保存在这个activity中的选择框选中状态信息
    public void getCheckStateInfo() {
        Log.i("getCheckStateInfo==>", "读取上次保存的状态方法进来了！循环次数为：" + uploadListViewItemList.size());
        for (int i = 0; i < checkState.length(); i++) {
            Log.i("getCheckStateInfo==>", "checkState的长度为：" + checkState.length());
            if (checkState.charAt(i) == '1') {
                UploadListViewAdapter.getIsCheck().put(i, true);
                Log.i("getCheckStateInfo==>", "读取上次保存的状态进来了！");
            }
        }
    }

    //绑定控件ID
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listview);
        upLoad = (LinearLayout) findViewById(R.id.up_load);
        selectAll = (TextView) findViewById(R.id.select_all);
        reverse = (TextView) findViewById(R.id.reverse);
        selectCancel = (TextView) findViewById(R.id.select_cancel);
        noData = (TextView) findViewById(R.id.no_data);
        uploadTaskSelectLayout = (LinearLayout) findViewById(R.id.upload_task_select_layout);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //点击事件
    private void setViewClickListener() {
        upLoad.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);
        selectAll.setOnClickListener(onClickListener);
        reverse.setOnClickListener(onClickListener);
        selectCancel.setOnClickListener(onClickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UploadViewHolder holder = (UploadViewHolder) view.getTag();
                holder.checkBox.toggle();
                UploadListViewAdapter.getIsCheck().put(position, holder.checkBox.isChecked());
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.up_load:
                    upLoad.setEnabled(false);
                    uploadTotalCount=0;
                    saveTaskInfo(); //保存选中的任务编号信息
                    Log.i("UploadActivity", "长度为：" + map.size());
                    if (map.size() != 0) {
                        showPopupwindow();  // 显示上传进度提示
                        new Thread() {
                            @Override
                            public void run() {
                                for (String taskId : map.keySet()) {
                                    getUploadUserTotalNumber(taskId, sharedPreferences_login.getString("userId", ""));  //根据任务编号获得需要上传的所有用户数量，并作为最大进度
                                }
                                if (uploadTotalCount != 0) {
                                    try {
                                        Thread.sleep(250);
                                        handler.sendEmptyMessage(3);
                                        for (String taskId : map.keySet()) {
                                            getUserDataAndPost(taskId);  //根据任务编号去查询用户信息
                                            Log.i("UploadActivity", "上传的任务编号为：" + taskId);
                                        }
                                        isCompleted = true;
                                      handler.sendEmptyMessage(8);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    handler.sendEmptyMessage(6);
                                }
                            }
                        }.start();
                        saveCheckStateInfo();//保存选中状态，将信息写入preference保存以备下一次读取使用
                    } else {
                        Toast.makeText(UploadActivity.this, "您还没有选择任务哦~", Toast.LENGTH_SHORT).show();
                    }
                    upLoad.setEnabled(true);
                    break;
                case R.id.select_all:
                    selectAll();
                    break;
                case R.id.reverse:
                    reverse();
                    break;
                case R.id.select_cancel:
                    selectCancle();
                    break;
                    default:
                        break;
            }
        }
    };

    //全选
    public void selectAll() {
        for (int i = 0; i < uploadListViewItemList.size(); i++) {
            adapter.getIsCheck().put(i, true);
        }
        adapter.notifyDataSetChanged();
    }

    //反选
    public void reverse() {
        for (int i = 0; i < uploadListViewItemList.size(); i++) {
            if (adapter.getIsCheck().get(i)) {
                adapter.getIsCheck().put(i, false);
            } else {
                adapter.getIsCheck().put(i, true);
            }
        }
        adapter.notifyDataSetChanged();
    }

    //取消选择
    public void selectCancle() {
        for (int i = 0; i < uploadListViewItemList.size(); i++) {
            if (adapter.getIsCheck().get(i)) {
                adapter.getIsCheck().put(i, false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    //保存选中的任务编号信息
    public void saveTaskInfo() {
        int count = adapter.getCount();
        Log.i("saveTaskInfo====>", "长度为：" + count);
        for (int i = 0; i < count; i++) {
            if (UploadListViewAdapter.getIsCheck().get(i)) {
                UploadListViewItem item = uploadListViewItemList.get((int) adapter.getItemId(i));
                map.put(item.getTaskNumber(), item.getTaskName());
                Log.i("saveTaskInfo=========>", "这次被勾选第" + i + "个，任务编号为：" + item.getTaskNumber());
            }
        }
    }


    //保存选中状态，将信息写入preference保存以备下一次读取使用
    public void saveCheckStateInfo() {
        String flag = "";
        int count = adapter.getCount();
        Log.i("count====>", "长度为：" + count);
        for (int i = 0; i < count; i++) {
            if (UploadListViewAdapter.getIsCheck().get(i)) {  //判断如果此时是选中状态就保存到SharedPreferences，“1”表示选中，0表示没选中
                flag = flag + '1';
            } else {
                flag = flag + '0';
            }
        }
        sharedPreferences.edit().putString("upload_check_state", flag).apply();//将数据已字符串形式保存起来，下次读取再用
        Log.i("saveCheckStateInfo=>", "checkState状态为：" + flag);
    }

    //读取下载到本地的任务数据
    public void getTaskData(String loginUserId) {
        Cursor cursor = db.rawQuery("select * from Task where loginUserId=?", new String[]{loginUserId});//查询并获得游标
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(2);
            return;
        }
        while (cursor.moveToNext()) {
            UploadListViewItem item = new UploadListViewItem();
            item.setTaskName(cursor.getString(cursor.getColumnIndex("taskName")));
            item.setTaskNumber(cursor.getString(cursor.getColumnIndex("taskId")));
            item.setCheckType(cursor.getString(cursor.getColumnIndex("securityType")));
            item.setTotalUserNumber(cursor.getString(cursor.getColumnIndex("totalCount")));
            item.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
            item.setRestCount("（" + cursor.getString(cursor.getColumnIndex("restCount")) + "）");
            uploadListViewItemList.add(item);
        }
        //cursor游标操作完成以后,一定要关闭
        cursor.close();
        handler.sendEmptyMessage(1);
    }

    //读取本地安检用户数据，并上传服务器
    public void getUserDataAndPost(final String taskId) {
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginUserId=?", new String[]{taskId, sharedPreferences_login.getString("userId", "")});//查询并获得游标
        map1 = new HashMap<String, Object>();
        Log.i("getUserDataAndPost=>", "上传的用户数为：" + cursor.getCount());
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("true")) {  //判断是否为安检过的，未安检的不上传
                if (cursor.getString(cursor.getColumnIndex("ifUpload")).equals("false")) { //判断是否为未上传，上传的用户数据不再上传
              
                        map1.put("n_safety_inspection_id", cursor.getString(cursor.getColumnIndex("securityNumber")));
                        Log.i("getUserDataAndPost=>", "安检ID为：" + cursor.getString(cursor.getColumnIndex("securityNumber")));

                        map1.put("c_safety_securitycontent", cursor.getString(cursor.getColumnIndex("security_case_id")));
                        Log.i("getUserDataAndPost=>", "安检情况是：" + cursor.getString(cursor.getColumnIndex("security_case_id")));

                        map1.put("c_safety_remark", cursor.getString(cursor.getColumnIndex("remarks")));
                        Log.i("getUserDataAndPost=>", "备注是：" + cursor.getString(cursor.getColumnIndex("remarks")));

                        map1.put("n_safety_hidden_id", 0);
                        Log.i("getUserDataAndPost=>", "隐患类型是：" + cursor.getString(cursor.getColumnIndex("security_hidden_id")));

                        map1.put("n_safety_hidden_reason_id", cursor.getString(cursor.getColumnIndex("security_hidden_reason_id")));
                        Log.i("getUserDataAndPost=>", "隐患原因ID是：" + cursor.getString(cursor.getColumnIndex("security_hidden_reason_id")));

                        map1.put("d_safety_inspection_date", cursor.getString(cursor.getColumnIndex("currentTime")));
                        Log.i("getUserDataAndPost=>", "安检的时间是：" + cursor.getString(cursor.getColumnIndex("currentTime")));

                        map1.put("n_safety_state", cursor.getString(cursor.getColumnIndex("security_state")));
                        Log.i("getUserDataAndPost=>", "安检状态是：" + cursor.getString(cursor.getColumnIndex("security_state")));

                        map1.put("c_safety_inspection_member", sharedPreferences_login.getString("user_name", ""));
                        Log.i("getUserDataAndPost=>", "安检人员是：" + sharedPreferences_login.getString("user_name", ""));

                        map1.put("c_user_id", cursor.getString(cursor.getColumnIndex("newUserId")));
                        Log.i("getUserDataAndPost=>", "新用户ID是：" + cursor.getString(cursor.getColumnIndex("newUserId")));
                        newUserId = cursor.getString(cursor.getColumnIndex("newUserId"));

                        map1.put("c_meter_number", cursor.getString(cursor.getColumnIndex("meterNumber")));
                        Log.i("getUserDataAndPost=>", "表编号是：" + cursor.getString(cursor.getColumnIndex("meterNumber")));

                        map1.put("c_user_phone", cursor.getString(cursor.getColumnIndex("userPhone")));
                        Log.i("getUserDataAndPost=>", "手机号码是：" + cursor.getString(cursor.getColumnIndex("userPhone")));

                        map1.put("c_user_address", cursor.getString(cursor.getColumnIndex("userAddress")));
                        Log.i("getUserDataAndPost=>", "地址是：" + cursor.getString(cursor.getColumnIndex("userAddress")));

                        getPhotoData(newUserId);
                        String httpUrl=new StringBuffer().append(SkUrl.SkHttp(UploadActivity.this)).append("updateInspection.do").toString();
                       // httpUtils.postData(httpUrl, map1, fileMap);
                        HttpUtils httpUtils=new HttpUtils();
                        String result= httpUtils.postData(httpUrl, map1, fileMap);
                        Log.i("httpUtils=>", "上传的地址为：" + httpUrl);
                        if ("保存成功".equals(result)) {
                            //如果返回保存成功则将用户表的上传状态改为true
                            updateUserUploadState(newUserId);  
                            currentUploadNumber++;
                            handler.sendEmptyMessage(4);
                        } else if ("保存失败".equals(result)) {
                            Log.i("UploadActivity=>", "保存失败！");
                            UploadFailedItem item = new UploadFailedItem();
                            item.setTaskId(taskId);
                            item.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
                            item.setUserId(cursor.getString(cursor.getColumnIndex("oldUserId")));
                            item.setUserNewId(cursor.getString(cursor.getColumnIndex("newUserId")));
                            failedItemList.add(item);
                            handler.sendEmptyMessage(5);
                            break;
                        } else if ("".equals(result)) {
                            Log.i("UploadActivity=>", "网络请求错误！");
                            handler.sendEmptyMessage(7);
                            break;
                        }
                 
                }
            } else {
                noCheckNumber++;
                Log.i("getUserDataAndPost====", "未安检户数为：" + noCheckNumber);
            }
        }
        cursor.close(); //游标关闭
    }

    //读取本地安检过的并且未上传的用户数据总数
    public void getUploadUserTotalNumber(String taskId, String loginUserId) {
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginUserId=?", new String[]{taskId, loginUserId});//查询并获得游标
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("true") && cursor.getString(cursor.getColumnIndex("ifUpload")).equals("false")) {  //当检测到是安检过，并且未上传的时候进来
                uploadTotalCount++;
            }
        }
        cursor.close(); //游标关闭
    }

    /**
     * 更新用户表上传状态
     */
    private void updateUserUploadState(String newUserId) {
        ContentValues values = new ContentValues();
        values.put("ifUpload", "true");
        db.update("User", values, "newUserId=? and loginUserId=?", new String[]{newUserId, sharedPreferences_login.getString("userId", "")});
    }

    //show上传popupwindow
    public void showPopupwindow() {
        layoutInflater = LayoutInflater.from(UploadActivity.this);
        loadingView = layoutInflater.inflate(R.layout.popupwindow_upload_progressbar, null);
        popupWindow = new PopupWindow(loadingView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        title = (TextView) loadingView.findViewById(R.id.title);
        frameAnimation = (ImageView) loadingView.findViewById(R.id.frame_animation);
        resultListview = (ListView) loadingView.findViewById(R.id.result_listview);
        progressLayout = (LinearLayout) loadingView.findViewById(R.id.progress_layout);
        totalCount = (TextView) loadingView.findViewById(R.id.total_count);
        ccurrentCount = (TextView) loadingView.findViewById(R.id.current_count);
        confirm = (TextView) loadingView.findViewById(R.id.confirm);
        line = loadingView.findViewById(R.id.line);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        startFrameAnimation();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                upLoad.setEnabled(true);
                backgroundAlpha(1.0F);
            }
        });
    }

    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = UploadActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            UploadActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            UploadActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        UploadActivity.this.getWindow().setAttributes(lp);
    }

    //读取保存到本地的图片数据，并上传服务器
    public void getPhotoData(String newUserId) {
        Cursor cursor = db.rawQuery("select * from security_photo where newUserId=? and loginUserId=?", new String[]{newUserId, sharedPreferences_login.getString("userId", "")});//查询并获得游标
        fileMap = new HashMap<String, File>();
        File file = null;
        while (cursor.moveToNext()) {
            file = new File(cursor.getString(1));
            fileMap.put("file" + cursor.getPosition(), file);
        }
        Log.i("getUserData=>", "上传的照片流为：" + fileMap.size());
        cursor.close(); //游标关闭
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    uploadTaskSelectLayout.setVisibility(View.VISIBLE);
                    adapter = new UploadListViewAdapter(UploadActivity.this, uploadListViewItemList);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    checkState = sharedPreferences.getString("upload_check_state", defaul); //如果没有获取到的话默认是0
                    if (!checkState.equals("")) {
                        getCheckStateInfo();//获得保存在这个activity中的选择框选中状态信息
                    }
                    break;
                case 2:
                    upLoad.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    totalCount.setText(String.valueOf(uploadTotalCount));
                    break;
                case 4:
                    ccurrentCount.setText(String.valueOf(currentUploadNumber));
                    break;
                case 5:
                    resultListview.setVisibility(View.VISIBLE);
                    failedListAdapter = new UplaodFailedListAdapter(UploadActivity.this, failedItemList);
                    failedListAdapter.notifyDataSetChanged();
                    resultListview.setAdapter(failedListAdapter);
                    break;
                case 6:
                    title.setText("当前勾选的任务没有可上传的用户哦！");
                    progressLayout.setVisibility(View.GONE);
                    frameAnimation.setVisibility(View.GONE);
                    line.setVisibility(View.VISIBLE);
                    confirm.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    title.setText("上传出错啦，请您检测网络或IP端口设置是否正确！");
                    progressLayout.setVisibility(View.GONE);
                    frameAnimation.setVisibility(View.GONE);
                    line.setVisibility(View.VISIBLE);
                    confirm.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    if (isCompleted) {
                        if (failedItemList.size() == 0) {
                            title.setText("数据上传完成！共上传了" + currentUploadNumber + "个用户数据");
                        }else {
                            title.setText("数据上传完成！共上传了" + currentUploadNumber + "个用户数据，其中有"+noCheckNumber+"个用户上传失败，列表如下：");
                        }
                        frameAnimation.setVisibility(View.GONE);
                        line.setVisibility(View.VISIBLE);
                        confirm.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放和数据库的连接
        db.close();
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
