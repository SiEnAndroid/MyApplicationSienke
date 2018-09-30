package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.adapter.UserListviewAdapter;
import com.example.administrator.thinker_soft.Security_check.model.UserListviewItem;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NewsReviseParams;
import com.example.administrator.thinker_soft.meter_code.sk.observer.ObserverListener;
import com.example.administrator.thinker_soft.meter_code.sk.observer.ObserverManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.SecurityUserDetailActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.activity.SyEastCheckListDatailActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.google.gson.Gson;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/3/16.
 */
public class UserListActivity extends Activity implements ObserverListener {
    private ImageView back, editDelete, more;
    private ListView listView;
    private TextView noData, name;
    private List<UserListviewItem> userListviewItemList = new ArrayList<>();
    private ArrayList<String> stringList = new ArrayList<>();//保存字符串参数
    private int task_total_numb = 0;
    private SQLiteDatabase db;  //数据库
    private MySqliteHelper helper; //数据库帮助类
    private int currentPosition;  //点击listview
    private UserListviewAdapter userListviewAdapter;
    private UserListviewItem item;
    private SharedPreferences sharedPreferences, sharedPreferences_login;
    private SharedPreferences.Editor editor;
    private EditText etSearch;
    private PopupWindow popupWindow;
    private int continuePosition = 0;  //继续安检位置
    private TextView tvNumber;//总条数
    private String endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setOnClickListener();//点击事件
    }

    //绑定控件ID
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        more = (ImageView) findViewById(R.id.more);
        listView = (ListView) findViewById(R.id.listview);
        noData = (TextView) findViewById(R.id.no_data);
        etSearch = (EditText) findViewById(R.id.etSearch);
        editDelete = (ImageView) findViewById(R.id.edit_delete);
        tvNumber = (TextView) findViewById(R.id.ic_nb).findViewById(R.id.tv_total_user_number);

    }

    //初始化设置
    private void defaultSetting() {
        helper = new MySqliteHelper(UserListActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        endTime = sharedPreferences.getString("currentEndTime", "");
        if (!"".equals(sharedPreferences.getString("currentTaskName", ""))) {
            name.setText(sharedPreferences.getString("currentTaskName", ""));
        } else {
            name.setText("用户列表");
        }
        new Thread() {
            @Override
            public void run() {
                if (sharedPreferences.getBoolean("show_temp_data", false)) {   //显示演示数据
                    getTempData();
                } else {
                    getUserData(sharedPreferences.getString("currentTaskId", ""), sharedPreferences_login.getString("userId", ""));//读取所有安检用户数据
                    Log.i("UserListActivity----", "查询的任务编号是：" + sharedPreferences.getString("currentTaskId", ""));
                }
            }
        }.start();

        //注册
        ObserverManager.getInstance().add(this);
    }

    //点击事件
    private void setOnClickListener() {
        back.setOnClickListener(onClickListener);
        name.setOnClickListener(onClickListener);
        more.setOnClickListener(onClickListener);
        editDelete.setOnClickListener(onClickListener);
        listView.setTextFilterEnabled(true);  // 开启过滤功能
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("UserListActivity", "userListviewItemList点击事件进来了，集合长度为：" + userListviewItemList.size());
                //item = userListviewItemList.get((int) parent.getAdapter().getItemId(position));
                item = (UserListviewItem) userListviewAdapter.getItem(position);
                currentPosition = position;
                if (SharedPreferencesHelper.getFirm(UserListActivity.this).equals("渝川安检")) {
                    Intent intent = new Intent(UserListActivity.this, UserDetailInfoActivity.class);
                    intent.putExtra("user_new_id", item.getUserNewId());
                    intent.putExtra("taskId", item.getTaskId());
                    intent.putExtra("if_upload", item.getIfUpload());
                    intent.putExtra("is_checked", item.getIfChecked());
                    startActivityForResult(intent, currentPosition);
                } else if (SharedPreferencesHelper.getFirm(UserListActivity.this).equals("东渝安检")){
                    //东渝安检
                    Intent intents = new Intent(UserListActivity.this, SyEastCheckListDatailActivity.class);
                    intents.putExtra("user_new_id", item.getUserNewId());
                    intents.putExtra("taskId", item.getTaskId());
                    intents.putExtra("if_upload", item.getIfUpload());
                    intents.putExtra("is_checked", item.getIfChecked());
                    intents.putExtra("end_time", endTime);
                    startActivityForResult(intents, currentPosition);
                }else {
                    Intent intents = new Intent(UserListActivity.this, SecurityUserDetailActivity.class);
                    intents.putExtra("user_new_id", item.getUserNewId());
                    intents.putExtra("taskId", item.getTaskId());
                    intents.putExtra("if_upload", item.getIfUpload());
                    intents.putExtra("is_checked", item.getIfChecked());
                    intents.putExtra("end_time", endTime);
                    startActivityForResult(intents, currentPosition); 
                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("UserListActivity", "onTextChanged进来了");
                if (TextUtils.isEmpty(s.toString().trim())) {
                    listView.clearTextFilter();    //搜索文本为空时，清除ListView的过滤
                    if (userListviewAdapter != null) {
                        userListviewAdapter.getFilter().filter("");
                    }
                    if (editDelete.getVisibility() == View.VISIBLE) {
                        editDelete.setVisibility(View.GONE);  //当输入框为空时，叉叉消失
                    }
                } else {
                    if (userListviewAdapter != null) {
                        userListviewAdapter.getFilter().filter(s);
                    }
                    //listView.setFilterText(s.toString().trim());  //设置过滤关键字
                    if (editDelete.getVisibility() == View.GONE) {
                        editDelete.setVisibility(View.VISIBLE);  //反之则显示
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("UserListActivity_after", "afterTextChanged进来了");
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    UserListActivity.this.finish();
                    break;
                case R.id.name:
                    if (userListviewItemList.size() != 0) {
                        //让listview跳到第一个用户
                        listView.setSelection(0);
                    }
                    break;
                case R.id.more:
                    showMoreWindow();
                    break;
                case R.id.edit_delete:
                    etSearch.setText("");
                    editDelete.setVisibility(View.GONE);
                    if (userListviewAdapter != null) {
                        userListviewAdapter.getFilter().filter("");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.i("UserListActivity", "UserListActivit的数据源长度为：" + userListviewItemList.size());
                    userListviewAdapter = new UserListviewAdapter(UserListActivity.this, userListviewItemList);
                    listView.setAdapter(userListviewAdapter);
                    userListviewAdapter.notifyDataSetChanged();
                    tvNumber.setText(userListviewItemList.size() + "");
                    break;
                case 1:
                    noData.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    userListviewAdapter = new UserListviewAdapter(UserListActivity.this, userListviewItemList);
                    userListviewAdapter.notifyDataSetChanged();
                    listView.setAdapter(userListviewAdapter);
                    //让listview显示上次安检的位置
                    listView.setSelection(continuePosition);
                    MyAnimationUtils.viewGroupOutAlphaAnimation(UserListActivity.this, listView, 0.2F);
                    tvNumber.setText(userListviewItemList.size() + "");
                    Log.i("ContinueCheckActivity", "列表显示当前的位置是：" + continuePosition);
                    break;
                case 3:
                    timeSequence();
                    handler.sendEmptyMessage(0);
                    break;
                case 4:
                    addressSequence();
                    if (!"".equals(sharedPreferences.getString("currentTaskId", ""))) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 按安检时间来排序
     */
    private void timeSequence() {
        Collections.sort(userListviewItemList, new Comparator<UserListviewItem>() {
            @Override
            public int compare(UserListviewItem o1, UserListviewItem o2) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date1 = dateFormat.parse(o1.getLastCheckTime().toString());
                    Date date2 = dateFormat.parse(o2.getLastCheckTime().toString());
                    if (date1 != null & date2 != null) {
                        return date1.compareTo(date2);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }

    /**
     * 排序
     *
     * @param mList
     */
    private void sortData(List<UserListviewItem> mList) {

        SortClass sort = new SortClass();
        Collections.sort(mList, sort);

    }

    /**
     * @ClassName: SortClass
     * @Description: 按时间降序排列
     * @date: 2018年7月1日 下午5:21:26
     */
    static class SortClass implements Comparator {
        @Override
        public int compare(Object obj0, Object obj1) {
            UserListviewItem map0 = (UserListviewItem) obj0;
            UserListviewItem map1 = (UserListviewItem) obj1;
            int flag = map0.getThisTime().compareTo(map1.getThisTime());
            // 不取反，则按正序排列
            return -flag;
        }
    }

    /**
     * 按安检地址来排序
     */
    private void addressSequence() {
        Collections.sort(userListviewItemList, new Comparator<UserListviewItem>() {
            @Override
            public int compare(UserListviewItem o1, UserListviewItem o2) {
                String s1 = converterToFirstSpell(o1.getAdress());
                String s2 = converterToFirstSpell(o2.getAdress());
                return Collator.getInstance(Locale.ENGLISH).compare(s1, s2);
            }
        });

    }

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
//    public static String converterToFirstSpell(String chines){
//        String pinyinName = "";
//        char[] nameChar = chines.toCharArray();
//        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        for (int i = 0; i < nameChar.length; i++) {
//            if (nameChar[i] > 128) {
//                try {
//                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
//                } catch (BadHanyuPinyinOutputFormatCombination e) {
//                    e.printStackTrace();
//                }
//            }else{
//                pinyinName += nameChar[i];
//            }
//        }
//        return pinyinName;
//    }
    public static String converterToFirstSpell(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        String output = "";

        try {
            for (char curchar : input) {
                if (java.lang.Character.toString(curchar).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
                    output += temp[0];
                } else {
                    output += java.lang.Character.toString(curchar);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

    private void showMoreWindow() {
        View contentView = getLayoutInflater().inflate(R.layout.popupwindow_sucurity_select_list, null);
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        RadioButton homeCheck = contentView.findViewById(R.id.home_check);
        RadioButton continueCheck = contentView.findViewById(R.id.continue_check);
        RadioButton undoneCheck = contentView.findViewById(R.id.undone_check);
        RadioButton haveDoneCheck = contentView.findViewById(R.id.have_done_check);
        RadioButton haveUpload = contentView.findViewById(R.id.have_upload);
        RadioButton time_sequence = contentView.findViewById(R.id.time_sequence);
        RadioButton address_sorting = contentView.findViewById(R.id.address_sorting);
        time_sequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                handler.sendEmptyMessage(3);
            }
        });
        address_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                handler.sendEmptyMessage(4);
            }
        });
        homeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                new Thread() {
                    @Override
                    public void run() {
                        if (sharedPreferences.getBoolean("show_temp_data", false)) {   //显示演示数据
                            getTempData();
                        } else {
                            getUserData(sharedPreferences.getString("currentTaskId", ""), sharedPreferences_login.getString("userId", ""));//读取所有安检用户数据
                            Log.i("UserListActivity----", "查询的任务编号是：" + sharedPreferences.getString("currentTaskId", ""));
                        }
                    }
                }.start();
            }
        });
        continueCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                new Thread() {
                    @Override
                    public void run() {
                        if (sharedPreferences.getBoolean("show_temp_data", false)) {
                            getTempData();
                        } else {
                            if (!"".equals(sharedPreferences.getString("currentTaskId", ""))) {
                                getContinueCheckPosition(sharedPreferences.getString("currentTaskId", "")); //获取继续安检的item位置
                                handler.sendEmptyMessage(2);
                            } else {
                                handler.sendEmptyMessage(1);
                            }
                        }
                    }
                }.start();
            }
        });
        undoneCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                new Thread() {
                    @Override
                    public void run() {
                        getNoCheckUser(sharedPreferences.getString("currentTaskId", ""), sharedPreferences_login.getString("userId", ""));//读取所有未安检用户数据
                        Log.i("UserListActivity----", "查询的任务编号是：" + sharedPreferences.getString("currentTaskId", ""));
                    }
                }.start();
            }
        });
        haveDoneCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                new Thread() {
                    @Override
                    public void run() {
                        getHaveCheckedUser(sharedPreferences.getString("currentTaskId", ""), sharedPreferences_login.getString("userId", ""));//读取所有已安检用户
                    }
                }.start();
            }
        });
        haveUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                new Thread() {
                    @Override
                    public void run() {
                        getHaveUploadUser(sharedPreferences.getString("currentTaskId", ""), sharedPreferences_login.getString("userId", ""));//读取所有已上传用户
                    }
                }.start();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.showAsDropDown(more, -180, 10);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = UserListActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            UserListActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            UserListActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        UserListActivity.this.getWindow().setAttributes(lp);
    }

    //获取继续安检的item位置
    public void getContinueCheckPosition(String taskId) {
        Log.i("ContinueCheckPosition", "获取继续安检位置进来了！");
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginUserId=?", new String[]{taskId, sharedPreferences_login.getString("userId", "")});//查询并获得游标
        //在页面finish之前，从上到下查询本地数据库没有安检的用户，相对应的item位置，查询到一个就break
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("false")) {
                continuePosition = cursor.getPosition();  //查询未安检的用户位置
                break;
            }
        }
        cursor.close(); //游标关闭
    }


    /**
     * 获取未抄用户
     *
     * @param taskId
     * @param loginUserId
     */
    public void getNoCheckUser(String taskId, String loginUserId) {
        userListviewItemList.clear();
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginUserId=?", new String[]{taskId, loginUserId});
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(1);
            return;
        }
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("false")) {
                UserListviewItem item = new UserListviewItem();
                item.setSecurityNumber(cursor.getString(cursor.getColumnIndex("securityNumber")));
                item.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
                item.setTaskId(cursor.getString(cursor.getColumnIndex("taskId")));
                item.setNumber(cursor.getString(cursor.getColumnIndex("meterNumber")));
                item.setPhoneNumber(cursor.getString(cursor.getColumnIndex("userPhone")));
                item.setSecurityType(cursor.getString(cursor.getColumnIndex("securityType")));
                item.setUserId(cursor.getString(cursor.getColumnIndex("oldUserId")));
                item.setUserNewId(cursor.getString(cursor.getColumnIndex("newUserId")));
                item.setAdress(cursor.getString(cursor.getColumnIndex("userAddress")));
                item.setUserProperty(cursor.getString(cursor.getColumnIndex("userProperty")));
                Log.i("UserList=cursor", "安检状态为 = " + cursor.getString(cursor.getColumnIndex("ifChecked")));
                if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("true")) {
                    Log.i("UserList=cursor", "安检状态为true");
                    item.setIfEdit(R.mipmap.meter_true);
                    item.setIfChecked("已检");
                } else {
                    Log.i("UserList=cursor", "安检状态为false");
                    item.setIfEdit(R.mipmap.meter_false);
                    item.setIfChecked("未检");
                }
                if (cursor.getString(cursor.getColumnIndex("ifUpload")).equals("true")) {
                    item.setIfUpload("已上传");
                } else {
                    item.setIfUpload("");
                }
                item.setLastCheckTime(cursor.getString(cursor.getColumnIndex("lastCheckTime")));
                userListviewItemList.add(item);
            }
        }
        //cursor游标操作完成以后,一定要关闭
        cursor.close();
        handler.sendEmptyMessage(0);
    }

    /**
     * 获取已抄数据
     *
     * @param taskId
     * @param loginUserId
     */
    public void getHaveCheckedUser(String taskId, String loginUserId) {
        userListviewItemList.clear();
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginUserId=?", new String[]{taskId, loginUserId});
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(1);
            return;
        }
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("true")) {
                UserListviewItem item = new UserListviewItem();
                item.setSecurityNumber(cursor.getString(cursor.getColumnIndex("securityNumber")));
                item.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
                item.setTaskId(cursor.getString(cursor.getColumnIndex("taskId")));
                item.setNumber(cursor.getString(cursor.getColumnIndex("meterNumber")));
                item.setPhoneNumber(cursor.getString(cursor.getColumnIndex("userPhone")));
                item.setSecurityType(cursor.getString(cursor.getColumnIndex("securityType")));
                item.setUserId(cursor.getString(cursor.getColumnIndex("oldUserId")));
                item.setUserNewId(cursor.getString(cursor.getColumnIndex("newUserId")));
                item.setAdress(cursor.getString(cursor.getColumnIndex("userAddress")));
                item.setUserProperty(cursor.getString(cursor.getColumnIndex("userProperty")));
                Log.i("UserList=cursor", "安检状态为 = " + cursor.getString(cursor.getColumnIndex("ifChecked")));
                if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("true")) {
                    Log.i("UserList=cursor", "安检状态为true");
                    item.setIfEdit(R.mipmap.meter_true);
                    item.setIfChecked("已检");
                } else {
                    Log.i("UserList=cursor", "安检状态为false");
                    item.setIfEdit(R.mipmap.meter_false);
                    item.setIfChecked("未检");
                }
                if (cursor.getString(cursor.getColumnIndex("ifUpload")).equals("true")) {
                    item.setIfUpload("已上传");
                } else {
                    item.setIfUpload("");
                }
                item.setLastCheckTime(cursor.getString(cursor.getColumnIndex("lastCheckTime")));
                userListviewItemList.add(item);
            }
        }
        //cursor游标操作完成以后,一定要关闭
        cursor.close();
        handler.sendEmptyMessage(0);
    }

    /**
     * 获取已上传的用户
     *
     * @param taskId
     * @param loginUserId
     */
    public void getHaveUploadUser(String taskId, String loginUserId) {
        userListviewItemList.clear();
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginUserId=?", new String[]{taskId, loginUserId});
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(1);
            return;
        }
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("ifUpload")).equals("true")) {
                UserListviewItem item = new UserListviewItem();
                item.setSecurityNumber(cursor.getString(cursor.getColumnIndex("securityNumber")));
                item.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
                item.setTaskId(cursor.getString(cursor.getColumnIndex("taskId")));
                item.setNumber(cursor.getString(cursor.getColumnIndex("meterNumber")));
                item.setPhoneNumber(cursor.getString(cursor.getColumnIndex("userPhone")));
                item.setSecurityType(cursor.getString(cursor.getColumnIndex("securityType")));
                item.setUserId(cursor.getString(cursor.getColumnIndex("oldUserId")));
                item.setUserNewId(cursor.getString(cursor.getColumnIndex("newUserId")));
                item.setAdress(cursor.getString(cursor.getColumnIndex("userAddress")));
                item.setUserProperty(cursor.getString(cursor.getColumnIndex("userProperty")));
                Log.i("UserList=cursor", "安检状态为 = " + cursor.getString(cursor.getColumnIndex("ifChecked")));
                if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("true")) {
                    Log.i("UserList=cursor", "安检状态为true");
                    item.setIfEdit(R.mipmap.meter_true);
                    item.setIfChecked("已检");
                } else {
                    Log.i("UserList=cursor", "安检状态为false");
                    item.setIfEdit(R.mipmap.meter_false);
                    item.setIfChecked("未检");
                }
                if (cursor.getString(cursor.getColumnIndex("ifUpload")).equals("true")) {
                    item.setIfUpload("已上传");
                } else {
                    item.setIfUpload("");
                }
                item.setLastCheckTime(cursor.getString(cursor.getColumnIndex("lastCheckTime")));
                userListviewItemList.add(item);
            }
        }
        //cursor游标操作完成以后,一定要关闭
        cursor.close();
        handler.sendEmptyMessage(0);
    }

    //获取任务编号参数
    public void getTaskParams() {
        if (sharedPreferences.getStringSet("stringSet", null) != null && sharedPreferences.getInt("task_total_numb", 0) != 0) {
            Iterator iterator = sharedPreferences.getStringSet("stringSet", null).iterator();
            while (iterator.hasNext()) {
                stringList.add(iterator.next().toString());
                Log.i("UserListActivity====>", "得到的参数为：" + stringList);
            }
            task_total_numb = sharedPreferences.getInt("task_total_numb", 0);
        }
    }

    /**
     * 查询方法参数详解
     * <p/>
     * public Cursor query (boolean distinct, String table, String[] columns,
     * String selection, String[] selectionArgs,
     * String groupBy, String having,
     * String orderBy, String limit,
     * CancellationSignal cancellationSignal)
     * <p/>
     * 参数介绍 :
     * <p/>
     * -- 参数① distinct : 是否去重复, true 去重复;
     * <p/>
     * -- 参数② table : 要查询的表名;
     * <p/>
     * -- 参数③ columns : 要查询的列名, 如果为null, 就会查询所有的列;
     * <p/>
     * -- 参数④ whereClause : 条件查询子句, 在这里可以使用占位符 "?";
     * <p/>
     * -- 参数⑤ whereArgs : whereClause查询子句中的传入的参数值, 逐个替换 "?" 占位符;
     * <p/>
     * -- 参数⑥ groupBy: 控制分组, 如果为null 将不会分组;
     * <p/>
     * -- 参数⑦ having : 对分组进行过滤;
     * <p/>
     * -- 参数⑧ orderBy : 对记录进行排序;
     * <p/>
     * -- 参数⑨ limite : 用于分页, 如果为null, 就认为不进行分页查询;
     * <p/>
     * -- 参数⑩ cancellationSignal : 进程中取消操作的信号, 如果操作被取消, 当查询命令执行时会抛出 OperationCanceledException 异常;
     */

    //读取下载到本地的用户数据
    public void getUserData(String taskId, String loginUserId) {
        userListviewItemList.clear();
        Cursor cursor = db.rawQuery("select * from User where taskId=? and loginUserId=? order by userAddress", new String[]{taskId, loginUserId});//查询并获得游标
//        Cursor cursor = db.rawQuery("select * from User where taskId=?  order by userAddress", new String[]{taskId});//查询并获得游标
        Log.i("UserListActivityget=", "任务编号是：" + taskId);
        Log.i("UserListActivityget=", "loginUserId：" + loginUserId);
        Log.i("UserListActivityget=", "有" + cursor.getCount() + "条数据！");
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(1);
            return;
        }
        while (cursor.moveToNext()) {
            UserListviewItem userListviewItem = new UserListviewItem();
            userListviewItem.setSecurityNumber(cursor.getString(cursor.getColumnIndex("securityNumber")));
            userListviewItem.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            userListviewItem.setTaskId(cursor.getString(cursor.getColumnIndex("taskId")));

            userListviewItem.setNumber(cursor.getString(cursor.getColumnIndex("meterNumber")));
            userListviewItem.setPhoneNumber(cursor.getString(cursor.getColumnIndex("userPhone")));
            userListviewItem.setSecurityType(cursor.getString(cursor.getColumnIndex("securityType")));
            userListviewItem.setUserId(cursor.getString(cursor.getColumnIndex("oldUserId")));
            userListviewItem.setUserNewId(cursor.getString(cursor.getColumnIndex("newUserId")));
            userListviewItem.setAdress(cursor.getString(cursor.getColumnIndex("userAddress")));
            userListviewItem.setUserProperty(cursor.getString(cursor.getColumnIndex("userProperty")));
            //总购气量
            userListviewItem.setSumDosage(cursor.getString(cursor.getColumnIndex("sum_dosage")));

            Log.i("UserList=cursor", "安检状态为 = " + cursor.getString(cursor.getColumnIndex("ifChecked")));
            if (cursor.getString(cursor.getColumnIndex("ifChecked")).equals("true")) {
                Log.i("UserList=cursor", "安检状态为true");
                userListviewItem.setIfEdit(R.mipmap.meter_true);
                userListviewItem.setIfChecked("已检");
            } else {
                Log.i("UserList=cursor", "安检状态为false");
                userListviewItem.setIfEdit(R.mipmap.meter_false);
                userListviewItem.setIfChecked("未检");
            }
            if (cursor.getString(cursor.getColumnIndex("ifUpload")).equals("true")) {
                userListviewItem.setIfUpload("已上传");
            } else {
                userListviewItem.setIfUpload("");
            }
            // 新增一条 上次安检时间
            userListviewItem.setLastCheckTime(cursor.getString(cursor.getColumnIndex("lastCheckTime")));
            userListviewItemList.add(userListviewItem);
            Log.i("UserListActivityget=", "用户列表的长度为：" + userListviewItemList.size());
        }
        cursor.close(); //游标关闭
        handler.sendEmptyMessage(0);
    }

    public void getTempData() {
        for (int i = 0; i < 20; i++) {
            UserListviewItem userListviewItem = new UserListviewItem();
            userListviewItem.setSecurityNumber("001");
            userListviewItem.setUserName("马云");
            userListviewItem.setTaskId("007");
            userListviewItem.setNumber("002");
            userListviewItem.setPhoneNumber("123456789");
            userListviewItem.setSecurityType("年度安检");
            userListviewItem.setUserId("110");
            userListviewItem.setUserNewId("010");
            userListviewItem.setAdress("重庆市江北区鲁溉路星耀天地");
            userListviewItem.setUserProperty("居民");
            if (i == 0 || i == 2) {
                userListviewItem.setIfEdit(R.mipmap.meter_true);
                userListviewItem.setIfChecked("已检");
                userListviewItem.setIfUpload("已上传");
            } else {
                userListviewItem.setIfEdit(R.mipmap.meter_false);
                userListviewItem.setIfChecked("未检");
                userListviewItem.setIfUpload("");
            }
            userListviewItemList.add(userListviewItem);
        }
        handler.sendEmptyMessage(0);
    }

    //更新用户表是否安检状态
    public void updateUserCheckedState() {
        ContentValues values = new ContentValues();
        values.put("ifChecked", "true");
        Log.i("UserList=update", "更新安检状态为true");
        db.update("User", values, "securityNumber=? and loginUserId=?", new String[]{item.getSecurityNumber(), sharedPreferences_login.getString("userId", "")});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == currentPosition) {
                //   updateUserCheckedState(); //更新本地数据库用户表安检状态
                item.setIfEdit(R.mipmap.meter_true);
                item.setIfChecked("已检");
                Log.i("UserList=ActivityResult", "页面回调进来了");
                if (data != null) {
                    String isUp = data.getStringExtra("isUpload");
                    if (isUp != null && isUp.equals("成功")) {
                        item.setIfUpload("已上传");
                        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                }
                userListviewAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObserverManager.getInstance().remove(this);
        if (db != null) {
            db.close(); //释放和数据库的连接
        }
        UserListviewAdapter.searchContent = "";
    }

    @Override
    public void observerUpData(String content) {
        NewsReviseParams params = new Gson().fromJson(content, NewsReviseParams.class);
        item.setPhoneNumber(params.getC_user_phone());
        item.setUserName(params.getC_user_name());
        userListviewAdapter.notifyDataSetChanged();
    }
}
