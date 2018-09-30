package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.adapter.NewTaskListviewAdapter;
import com.example.administrator.thinker_soft.Security_check.model.NewTaskListviewItem;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.mode.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */
public class NewTaskDetailActivity extends Activity {
    private View view, saveView;
    private ImageView back, editDelete;
    private ListView listView;
    private TextView filter, save, no_data;
    private EditText etSearch;//搜索框
    private TextView searchBtn, tips;
    private PopupWindow popupWindow;
    private View filterView;
    private RadioButton userName, userId, meterNumber, address, cancelRb, saveRb;
    private LayoutInflater inflater;  //转换器
    private List<NewTaskListviewItem> newTaskListviewItemList = new ArrayList<>();
   // private String ip, port;  //接口ip地址   端口
    private String result; //网络请求结果
    private SharedPreferences sharedPreferences, sharedPreferences_login, public_sharedPreferences;
    private SharedPreferences.Editor editor;
    private LayoutInflater layoutInflater;
    private LinearLayout rootLinearlayout,newTaskSelectLayout;
    private TextView selectAll, reverse, selectCancel,totalUserNumber;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    public int responseCode = 0;
    private NewTaskListviewAdapter newTaskListviewAdapter;
    private ArrayList<NewTaskListviewItem> parclebleList = new ArrayList<>();
    private ArrayList<NewTaskListviewItem> selectDatas = new ArrayList<>();
    private List<String> userOldIdLists = new ArrayList<>();
    private RadioButton selectRb1, selectRb2;
    private String securityType;
    private RadioGroup radioGroup;
    private int indexCount=0;//添加的个数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_datail);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setOnClickListener();//点击事件
    }

    //绑定控件ID
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listview);
        filter = (TextView) findViewById(R.id.filter);
        etSearch = (EditText) findViewById(R.id.etSearch);
        searchBtn = (TextView) findViewById(R.id.search_btn);
        save = (TextView) findViewById(R.id.save);
        no_data = (TextView) findViewById(R.id.no_data);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
        newTaskSelectLayout = (LinearLayout) findViewById(R.id.new_task_select_layout);
        selectAll = (TextView) findViewById(R.id.select_all);
        reverse = (TextView) findViewById(R.id.reverse);
        selectCancel = (TextView) findViewById(R.id.select_cancel);
        totalUserNumber = (TextView) findViewById(R.id.total_user_number);
        editDelete = (ImageView) findViewById(R.id.edit_delete);
        selectRb1 = (RadioButton) findViewById(R.id.select_rb1);
        selectRb2 = (RadioButton) findViewById(R.id.select_rb2);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = NewTaskDetailActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        public_sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        //获取上个页面传递过来的参数
        Intent intent = getIntent();
        securityType = intent.getStringExtra("security_type");
        if(securityType.equals("不是复检")){
            radioGroup.setVisibility(View.VISIBLE);
        }else {
            radioGroup.setVisibility(View.GONE);
        }
        selectRb1.setChecked(true);
        if (no_data.getVisibility() == View.GONE) {
            no_data.setVisibility(View.VISIBLE);
        }
        parclebleList.clear();


        //开启支线程进行请求任务信息
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    if (selectRb1.isChecked()) {
                        requireMyTask("getCostomer.do", "userName=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=0");
                    } else if (selectRb2.isChecked()) {
                        requireMyTask("getCostomer.do", "userName=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=1");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    //点击事件
    private void setOnClickListener() {
        back.setOnClickListener(onClickListener);
        filter.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);
        searchBtn.setOnClickListener(onClickListener);
        selectAll.setOnClickListener(onClickListener);
        reverse.setOnClickListener(onClickListener);
        selectCancel.setOnClickListener(onClickListener);
        editDelete.setOnClickListener(onClickListener);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (newTaskListviewItemList.size() != 0) {
                    if (TextUtils.isEmpty(s.toString().trim())) {
                        newTaskListviewItemList.clear();
                        newTaskListviewAdapter.notifyDataSetChanged();
                        editDelete.setVisibility(View.GONE);  //当输入框为空时，叉叉消失
                        newTaskSelectLayout.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
                    } else {
                        editDelete.setVisibility(View.VISIBLE);  //反之则显示
                    }
                } else {
                    if (TextUtils.isEmpty(s.toString().trim())) {
                        editDelete.setVisibility(View.GONE);  //当输入框为空时，叉叉消失
                    } else {
                        editDelete.setVisibility(View.VISIBLE);  //反之则显示
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             //   NewTaskViewHolder holder = (NewTaskViewHolder) view.getTag();
             //   holder.checkBox.toggle();
              //  NewTaskListviewAdapter.getIsCheck().put(position, holder.checkBox.isChecked());

                if (newTaskListviewAdapter.getIsCheck().get(position)) {
                    newTaskListviewAdapter.getIsCheck().put(position, false);
                    selectDatas.remove(newTaskListviewItemList.get(position));
                } else {
                    newTaskListviewAdapter.getIsCheck().put(position, true);
                    selectDatas.add(newTaskListviewItemList.get(position));
                }
                newTaskListviewAdapter.notifyDataSetChanged();
                indexCount=selectDatas.size();

                Log.i("NewTaskDetailActivity", "列表点击事件进来了！");
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    NewTaskDetailActivity.this.finish();
                    break;
                case R.id.filter:
                    filter.setClickable(false);
                    createfilterPopupwindow();  //筛选框
                    break;
                case R.id.save:
                    save.setClickable(false);
                    Log.i("NewTaskDetailActivity", "显示的数量为：" + newTaskListviewItemList.size());
                    if (newTaskListviewItemList.size() != 0) {
                        saveTaskInfo();//保存选中的用户信息
                        if (parclebleList.size() != 0) {
                            createSavePopupwindow(indexCount);
                        } else {
                            save.setClickable(true);
                            Toast.makeText(NewTaskDetailActivity.this, "添加用户才能保存哦！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        save.setClickable(true);
                        Toast.makeText(NewTaskDetailActivity.this, "请您添加用户！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.search_btn:
                    if(Tools.isInputMethodOpened(NewTaskDetailActivity.this)){
                        Tools.hideSoftInput(NewTaskDetailActivity.this,etSearch);
                    }
                    no_data.setVisibility(View.GONE);
                    if (newTaskListviewItemList.size() != 0) {
                        newTaskListviewItemList.clear();
                        newTaskListviewAdapter.notifyDataSetChanged();
                        newTaskSelectLayout.setVisibility(View.GONE);
                    }
                    if(securityType.equals("不是复检")){
                        if (filter.getText().equals("姓名")) {
                            if (etSearch.getText().length() >= 2) {
                                showPopupwindow();
                                //开启支线程进行请求任务信息
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                            if (selectRb1.isChecked()) {
                                                requireMyTask("getCostomer.do", "userName=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=0");
                                            } else if (selectRb2.isChecked()) {
                                                requireMyTask("getCostomer.do", "userName=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=1");
                                            }
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        super.run();
                                    }
                                }.start();
                            } else {
                                Toast.makeText(NewTaskDetailActivity.this, "请至少输入两个字！", Toast.LENGTH_SHORT).show();
                            }
                        } else if (filter.getText().equals("用户编号")) {
                            if (etSearch.getText().length() != 0) {
                                showPopupwindow();
                                //开启支线程进行请求任务信息
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                            if (selectRb1.isChecked()) {
                                                requireMyTask("getCostomer.do", "userId=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=0");
                                            } else if (selectRb2.isChecked()) {
                                                requireMyTask("getCostomer.do", "userId=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=1");
                                            }
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        super.run();
                                    }
                                }.start();
                            } else {
                                Toast.makeText(NewTaskDetailActivity.this, "请输入用户编号！", Toast.LENGTH_SHORT).show();
                            }
                        } else if (filter.getText().equals("表编号")) {
                            if (etSearch.getText().length() != 0) {
                                showPopupwindow();
                                //开启支线程进行请求任务信息
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                            if (selectRb1.isChecked()) {
                                                requireMyTask("getCostomer.do", "meterNumber=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=0");
                                            } else if (selectRb2.isChecked()) {
                                                requireMyTask("getCostomer.do", "meterNumber=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=1");
                                            }
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        super.run();
                                    }
                                }.start();
                            } else {
                                Toast.makeText(NewTaskDetailActivity.this, "请输入表编号！", Toast.LENGTH_SHORT).show();
                            }
                        } else if (filter.getText().equals("地址")) {
                            if (etSearch.getText().length() != 0) {
                                showPopupwindow();
                                //开启支线程进行请求任务信息
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                            if (selectRb1.isChecked()) {
                                                requireMyTask("getCostomer.do", "userAdress=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=0");
                                            } else if (selectRb2.isChecked()) {
                                                requireMyTask("getCostomer.do", "userAdress=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=1");
                                            }
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        super.run();
                                    }
                                }.start();
                            } else {
                                Toast.makeText(NewTaskDetailActivity.this, "请输入地址！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {      //查询复检的用户数据
                        if (filter.getText().equals("姓名")) {
                            if (etSearch.getText().length() >= 2) {
                                showPopupwindow();
                                //开启支线程进行请求任务信息
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                            requireMyTask("getCostomer.do", "userName=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=2");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        super.run();
                                    }
                                }.start();
                            } else {
                                Toast.makeText(NewTaskDetailActivity.this, "请至少输入两个字！", Toast.LENGTH_SHORT).show();
                            }
                        } else if (filter.getText().equals("用户编号")) {
                            if (etSearch.getText().length() != 0) {
                                showPopupwindow();
                                //开启支线程进行请求任务信息
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                            requireMyTask("getCostomer.do", "userId=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=2");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        super.run();
                                    }
                                }.start();
                            } else {
                                Toast.makeText(NewTaskDetailActivity.this, "请输入用户编号！", Toast.LENGTH_SHORT).show();
                            }
                        } else if (filter.getText().equals("表编号")) {
                            if (etSearch.getText().length() != 0) {
                                showPopupwindow();
                                //开启支线程进行请求任务信息
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                            requireMyTask("getCostomer.do", "meterNumber=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=2");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        super.run();
                                    }
                                }.start();
                            } else {
                                Toast.makeText(NewTaskDetailActivity.this, "请输入表编号！", Toast.LENGTH_SHORT).show();
                            }
                        } else if (filter.getText().equals("地址")) {
                            if (etSearch.getText().length() != 0) {
                                showPopupwindow();
                                //开启支线程进行请求任务信息
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(2000);
                                            requireMyTask("getCostomer.do", "userAdress=" + URLEncoder.encode(etSearch.getText().toString().trim(), "UTF-8") + "&flag=2");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        super.run();
                                    }
                                }.start();
                            } else {
                                Toast.makeText(NewTaskDetailActivity.this, "请输入地址！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                case R.id.edit_delete:
                    if (newTaskListviewItemList.size() != 0) {
                        newTaskListviewItemList.clear();
                        newTaskListviewAdapter.notifyDataSetChanged();
                        newTaskSelectLayout.setVisibility(View.GONE);
                        editDelete.setVisibility(View.GONE);  //当输入框为空时，叉叉消失
                        etSearch.setText("");
                    } else {
                        etSearch.setText("");
                        editDelete.setVisibility(View.GONE);  //当输入框为空时，叉叉消失
                        newTaskSelectLayout.setVisibility(View.GONE);
                    }
                    no_data.setVisibility(View.VISIBLE);
                    break;
                case R.id.select_rb1:
                    selectRb1.setChecked(true);
                    break;
                case R.id.select_rb2:
                    selectRb2.setChecked(true);
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
            }
        }
    };

    //全选
    public void selectAll() {
        for (int i = 0; i < newTaskListviewItemList.size(); i++) {
            newTaskListviewAdapter.getIsCheck().put(i, true);
            selectDatas.add(newTaskListviewItemList.get(i));

        }
        if (newTaskListviewAdapter!=null){
            newTaskListviewAdapter.notifyDataSetChanged();
        }
        indexCount=selectDatas.size();
    }

    //反选
    public void reverse() {
        for (int i = 0; i < newTaskListviewItemList.size(); i++) {
            if (newTaskListviewAdapter.getIsCheck().get(i)) {
                newTaskListviewAdapter.getIsCheck().put(i, false);
                selectDatas.remove(newTaskListviewItemList.get(i));
            } else {
                newTaskListviewAdapter.getIsCheck().put(i, true);
                selectDatas.add(newTaskListviewItemList.get(i));
            }
        }
        if (newTaskListviewAdapter!=null){
            newTaskListviewAdapter.notifyDataSetChanged();
        }
        indexCount=selectDatas.size();
    }

    //取消选择
    public void selectCancle() {
        for (int i = 0; i < newTaskListviewItemList.size(); i++) {
            if (newTaskListviewAdapter.getIsCheck().get(i)) {
                newTaskListviewAdapter.getIsCheck().put(i, false);
                selectDatas.remove(newTaskListviewItemList.get(i));
            }
        }
        if (newTaskListviewAdapter!=null){
            newTaskListviewAdapter.notifyDataSetChanged();
        }
        indexCount=selectDatas.size();
    }

    //保存选中的用户信息
    public void saveTaskInfo() {
        int count = newTaskListviewAdapter.getCount();
        Log.i("count====>", "长度为：" + count);
//
//        for (int i = 0;i<selectDatas.size();i++){
//            if (newTaskListviewAdapter.getIsCheck().get(i)) {
//                Log.i("NewTaskDetailActivity", "点击的位置是：" + i);
//                NewTaskListviewItem item = newTaskListviewItemList.get((int) newTaskListviewAdapter.getItemId(i));
//                if(userOldIdLists.size() != 0){     //继续添加用户的时候
//                    Log.i("NewTaskDetailActivity", "继续添加用户的时候：" );
//                    if(userOldIdLists.contains(item.getOldUserId())){
//                        Log.i("NewTaskDetailActivity", "老编号重复：" );
//                    }else {
//                        userOldIdLists.add(item.getOldUserId());
//                        parclebleList.add(item);
//                    }
//                }else {       //第一次添加用户的时候
//                    Log.i("NewTaskDetailActivity", "第一次添加用户的时候：" );
//                    userOldIdLists.add(item.getOldUserId());
//                    parclebleList.add(item);
//                }
//                Log.i("NewTaskDetailActivity", "用户老编号集合长度为：" + userOldIdLists.size());
//                Log.i("NewTaskDetailActivity", "用户集合长度为：" + parclebleList.size());
//            }else {
//                Log.i("NewTaskDetailActivity", "点击的位置是：" + i);
//                NewTaskListviewItem item = newTaskListviewItemList.get((int) newTaskListviewAdapter.getItemId(i));
//                if(userOldIdLists.size() != 0){
//                    if(userOldIdLists.contains(item.getOldUserId())){
//                        Log.i("NewTaskDetailActivity", "老编号重复：" );
//                        userOldIdLists.remove(item.getOldUserId());
//                        parclebleList.remove(item);
//                    }
//                }
//            }
//        }

        for (int i = 0; i < count; i++) {
            if (newTaskListviewAdapter.getIsCheck().get(i)) {
                Log.i("NewTaskDetailActivity", "点击的位置是：" + i);
                NewTaskListviewItem item = newTaskListviewItemList.get((int) newTaskListviewAdapter.getItemId(i));
                if(userOldIdLists.size() != 0){     //继续添加用户的时候
                    Log.i("NewTaskDetailActivity", "继续添加用户的时候：" );
                    if(userOldIdLists.contains(item.getOldUserId())){
                        Log.i("NewTaskDetailActivity", "老编号重复：" );
                    }else {
                        userOldIdLists.add(item.getOldUserId());
                        parclebleList.add(item);
                    }
                }else {       //第一次添加用户的时候
                    Log.i("NewTaskDetailActivity", "第一次添加用户的时候：" );
                    userOldIdLists.add(item.getOldUserId());
                    parclebleList.add(item);
                }
                Log.i("NewTaskDetailActivity", "用户老编号集合长度为：" + userOldIdLists.size());
                Log.i("NewTaskDetailActivity", "用户集合长度为：" + parclebleList.size());


            }else {
                Log.i("NewTaskDetailActivity", "点击的位置是：" + i);
                NewTaskListviewItem item = newTaskListviewItemList.get((int) newTaskListviewAdapter.getItemId(i));
                if(userOldIdLists.size() != 0){
                    if(userOldIdLists.contains(item.getOldUserId())){
                        Log.i("NewTaskDetailActivity", "老编号重复：" );
                        userOldIdLists.remove(item.getOldUserId());
                        parclebleList.remove(item);
                    }
                }
            }
        }

    }

    //show弹出框
    public void showPopupwindow() {
        layoutInflater = LayoutInflater.from(NewTaskDetailActivity.this);
        view = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) view.findViewById(R.id.frame_animation);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        //popupWindow.update();
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        startFrameAnimation();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //筛选框popupwindow
    public void createfilterPopupwindow() {
        inflater = LayoutInflater.from(NewTaskDetailActivity.this);
        filterView = inflater.inflate(R.layout.popupwindow_userlist_choose, null);
        popupWindow = new PopupWindow(filterView, filter.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        userName = (RadioButton) filterView.findViewById(R.id.user_name);
        userId = (RadioButton) filterView.findViewById(R.id.user_id);
        meterNumber = (RadioButton) filterView.findViewById(R.id.meter_number);
        address = (RadioButton) filterView.findViewById(R.id.address);
        //设置点击事件
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                filter.setClickable(true);
                filter.setText(userName.getText());
                if (no_data.getVisibility() == View.VISIBLE) {
                    no_data.setVisibility(View.GONE);
                }
            }
        });
        userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                filter.setClickable(true);
                filter.setText(userId.getText());
                if (no_data.getVisibility() == View.VISIBLE) {
                    no_data.setVisibility(View.GONE);
                }
            }
        });
        meterNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                filter.setClickable(true);
                filter.setText(meterNumber.getText());
                if (no_data.getVisibility() == View.VISIBLE) {
                    no_data.setVisibility(View.GONE);
                }
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                filter.setClickable(true);
                filter.setText(address.getText());
                if (no_data.getVisibility() == View.VISIBLE) {
                    no_data.setVisibility(View.GONE);
                }
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_spinner_shape));
        popupWindow.setAnimationStyle(R.style.Popupwindow);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.showAsDropDown(filter, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
                filter.setClickable(true);
            }
        });
    }

    //弹出保存前提示popupwindow
    public void createSavePopupwindow(final int count) {
        layoutInflater = LayoutInflater.from(NewTaskDetailActivity.this);
        saveView = layoutInflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        popupWindow = new PopupWindow(saveView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        tips = (TextView) saveView.findViewById(R.id.tips);
        cancelRb = (RadioButton) saveView.findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) saveView.findViewById(R.id.save_rb);
        //设置点击事件
        tips.setText("您已添加了" + count + "个用户，还继续添加吗？");
        cancelRb.setText("继续添加");
        saveRb.setText("完成添加");
        cancelRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        saveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewTaskDetailActivity.this, "成功添加了" + count + "个用户！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
//                intent.putParcelableArrayListExtra("parclebleList", parclebleList);
//                Log.i("NewTaskDetailActivity", "parclebleList长度为：" + parclebleList.size());
                intent.putParcelableArrayListExtra("parclebleList", selectDatas);
                Log.i("NewTaskDetailActivity", "parclebleList长度为：" + selectDatas.size());

                setResult(Activity.RESULT_OK, intent);
                finish();
                popupWindow.dismiss();
            }
        });
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
                save.setClickable(true);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = NewTaskDetailActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            NewTaskDetailActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            NewTaskDetailActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        NewTaskDetailActivity.this.getWindow().setAttributes(lp);
    }

    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }

    //请求网络数据
    private void requireMyTask(final String method, final String keyAndValue) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url;
                    HttpURLConnection httpURLConnection;
                    Log.i("sharedPreferences====>", public_sharedPreferences.getString("IP", ""));

                   // String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
                  String httpUrl=new StringBuffer().append(SkUrl.SkHttp(NewTaskDetailActivity.this)).append(method).toString();
                    //有参数传递
                    if (!keyAndValue.equals("")) {
                        url = new URL(httpUrl + "?" + keyAndValue);
                        //没有参数传递
                    } else {
                        url = new URL(httpUrl);
                    }
                    Log.i("NewTaskDetailActivity", url + "");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(6000);
                    httpURLConnection.setReadTimeout(6000);
                    httpURLConnection.connect();
                    //传回的数据解析成String
                    Log.i("responseCode====>", httpURLConnection.getResponseCode() + "");
                    if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        result = stringBuilder.toString();
                        Log.i("NewTaskDetailActivity", result);
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.optInt("total", 0) != 0) {
                            handler.sendEmptyMessage(1);
                        } else {
                            try {
                                Thread.sleep(3000);
                                handler.sendEmptyMessage(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            Thread.sleep(3000);
                            handler.sendEmptyMessage(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException==========>", "网络请求异常!");
                    handler.sendEmptyMessage(3);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        Log.i("NewTaskDetailActivity", "jsonArray==" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            NewTaskListviewItem item = new NewTaskListviewItem();
                            if(object.optInt("flag",0) == 0){
                                if(!"null".equals(object.optString("c_user_name", ""))){
                                    item.setUserName(object.optString("c_user_name", ""));
                                }else {
                                    item.setUserName("暂无");
                                }
                                if(!"null".equals(object.optString("c_properties_name", ""))){
                                    item.setUserproperty(object.optString("c_properties_name", ""));
                                }else {
                                    item.setUserproperty("暂无");
                                }
                                if(object.optInt("n_safety_state",0) == 0){
                                    item.setSecurityState("未安检");
                                }else if(object.optInt("n_safety_state",0) == 1){
                                    item.setSecurityState("安检合格");
                                }else if(object.optInt("n_safety_state",0) == 2){
                                    item.setSecurityState("安检不合格");
                                }else if(object.optInt("n_safety_state",0) == 3){
                                    item.setSecurityState("超过安检时间");
                                }else if(object.optInt("n_safety_state",0) == 4){
                                    item.setSecurityState("到访不遇");
                                }else if(object.optInt("n_safety_state",0) == 5){
                                    item.setSecurityState("拒绝安检");
                                }
                                if(!object.optString("c_safety_remark","").equals("null")){
                                    item.setRemarks(object.optString("c_safety_remark",""));
                                }else {
                                    item.setRemarks("暂无");
                                }
                                if(!"null".equals(object.optString("c_meter_number", ""))){
                                    item.setNumber(object.optString("c_meter_number", ""));
                                }else {
                                    item.setNumber("暂无");
                                }
                                if(!"null".equals(object.optString("c_user_phone", ""))){
                                    item.setPhoneNumber(object.optString("c_user_phone", ""));
                                }else {
                                    item.setPhoneNumber("暂无");
                                }
                                if(!"null".equals(object.optString("c_user_id", ""))){
                                    item.setUserId(object.optString("c_user_id", ""));
                                }else {
                                    item.setUserId("暂无");
                                }
                                if(!"null".equals(object.optString("c_old_user_id", ""))){
                                    item.setOldUserId(object.optString("c_old_user_id", ""));
                                }else {
                                    item.setOldUserId("暂无");
                                }
                                if(!"null".equals(object.optString("c_user_address", ""))){
                                    item.setAdress(object.optString("c_user_address", ""));
                                }else {
                                    item.setAdress("暂无");
                                }
                                item.setFlag(0);
                            }else if(object.optInt("flag",0) == 1){
                                if(!"null".equals(object.optString("c_user_name", ""))){
                                    item.setUserName(object.optString("c_user_name", ""));
                                }else {
                                    item.setUserName("暂无");
                                }
                                if(!"null".equals(object.optString("c_properties_name", ""))){
                                    item.setUserproperty(object.optString("c_properties_name", ""));
                                }else {
                                    item.setUserproperty("暂无");
                                }
                                if(!"null".equals(object.optString("c_meter_number", ""))){
                                    item.setNumber(object.optString("c_meter_number", ""));
                                }else {
                                    item.setNumber("暂无");
                                }
                                if(!"null".equals(object.optString("c_user_phone", ""))){
                                    item.setPhoneNumber(object.optString("c_user_phone", ""));
                                }else {
                                    item.setPhoneNumber("暂无");
                                }
                                if(!"null".equals(object.optString("c_user_id", ""))){
                                    item.setUserId(object.optString("c_user_id", ""));
                                }else {
                                    item.setUserId("暂无");
                                }
                                if(!"null".equals(object.optString("c_old_user_id", ""))){
                                    item.setOldUserId(object.optString("c_old_user_id", ""));
                                }else {
                                    item.setOldUserId("暂无");
                                }
                                if(!"null".equals(object.optString("c_user_address", ""))){
                                    item.setAdress(object.optString("c_user_address", ""));
                                }else {
                                    item.setAdress("暂无");
                                }
                                item.setFlag(1);
                            }else if(object.optInt("flag",0) == 2){
                                if(!"null".equals(object.optString("c_user_name", ""))){
                                    item.setUserName(object.optString("c_user_name", ""));
                                }else {
                                    item.setUserName("暂无");
                                }
                                if(!"null".equals(object.optString("c_properties_name", ""))){
                                    item.setUserproperty(object.optString("c_properties_name", ""));
                                }else {
                                    item.setUserproperty("暂无");
                                }
                                if(object.optInt("n_safety_state",0) == 0){
                                    item.setSecurityState("未安检");
                                }else if(object.optInt("n_safety_state",0) == 1){
                                    item.setSecurityState("安检合格");
                                }else if(object.optInt("n_safety_state",0) == 2){
                                    item.setSecurityState("安检不合格");
                                }else if(object.optInt("n_safety_state",0) == 3){
                                    item.setSecurityState("超过安检时间");
                                }else if(object.optInt("n_safety_state",0) == 4){
                                    item.setSecurityState("到访不遇");
                                }else if(object.optInt("n_safety_state",0) == 5){
                                    item.setSecurityState("拒绝安检");
                                }
                                if(!"null".equals(object.optString("c_meter_number", ""))){
                                    item.setNumber(object.optString("c_meter_number", ""));
                                }else {
                                    item.setNumber("暂无");
                                }
                                if(!"null".equals(object.optString("c_user_phone", ""))){
                                    item.setPhoneNumber(object.optString("c_user_phone", ""));
                                }else {
                                    item.setPhoneNumber("暂无");
                                }
                                if(!"null".equals(object.optString("c_user_id", ""))){
                                    item.setUserId(object.optString("c_user_id", ""));
                                }else {
                                    item.setUserId("暂无");
                                }
                                if(!"null".equals(object.optString("c_old_user_id", ""))){
                                    item.setOldUserId(object.optString("c_old_user_id", ""));
                                }else {
                                    item.setOldUserId("暂无");
                                }
                                if(!"null".equals(object.optString("c_user_address", ""))){
                                    item.setAdress(object.optString("c_user_address", ""));
                                }else {
                                    item.setAdress("暂无");
                                }
                                item.setFlag(2);
                            }
                            newTaskListviewItemList.add(item);
                        }
                        if (newTaskListviewItemList.size() != 0) {
                            totalUserNumber.setText(String.valueOf(jsonObject.optInt("total", 0)));  //搜索到的用户总数
                            no_data.setVisibility(View.GONE);
                            Log.i("NewTaskDetailActivity", "传入的数据长度为：" + newTaskListviewItemList.size());
                            newTaskListviewAdapter = new NewTaskListviewAdapter(NewTaskDetailActivity.this, newTaskListviewItemList);
                            newTaskListviewAdapter.notifyDataSetChanged();
                            listView.setAdapter(newTaskListviewAdapter);
                            newTaskSelectLayout.setVisibility(View.VISIBLE);
                        }
                        Thread.sleep(500);
                        popupWindow.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    popupWindow.dismiss();
                    no_data.setVisibility(View.VISIBLE);
                    Toast.makeText(NewTaskDetailActivity.this, "没有相应的数据！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    popupWindow.dismiss();
                    no_data.setVisibility(View.VISIBLE);
                    Toast.makeText(NewTaskDetailActivity.this, "网络请求超时！", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
