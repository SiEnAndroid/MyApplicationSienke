package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.AreaDataAdapter;
import com.example.administrator.thinker_soft.meter_code.adapter.BookDataAdapter;
import com.example.administrator.thinker_soft.meter_code.model.AreaInfo;
import com.example.administrator.thinker_soft.meter_code.model.BookInfo;
import com.example.administrator.thinker_soft.meter_code.model.MeterAreaViewHolder;
import com.example.administrator.thinker_soft.meter_code.model.MeterBookViewHolder;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 移动抄表下载数据
 */
public class MeterDataDownloadActivity extends Activity {
    private ImageView back;
    private ListView booklistView;
    private ListView arealistView;
    private BookDataAdapter bookAdapter;
    private AreaDataAdapter areaAdapter;
    private EditText begianNum;
    private EditText endNum;
    private Button downLoadBtn;
    private TextView selectBookNumber, selectAreaNumber, clear, noAreaData, noBookData;
    private RadioButton bookSelectAll, bookReverse, bookSelectCancel, areaSelectAll, areaReverse, areaSelectCancel;
    private List<BookInfo> bookInfoList = new ArrayList<>();      //抄表本集合
    private List<AreaInfo> areaInfoList = new ArrayList<>();   //抄表分区集合
    private int bookNum = 0; // 记录抄表本选中的条目数量
    private int areaNum = 0; // 记录抄表分区选中的条目数量
    private HashMap<String, Integer> bookIDMap = new HashMap<String, Integer>();  //用于保存选中的抄表本ID
    private HashMap<String, Integer> areaIDMap = new HashMap<String, Integer>();  //用于保存选中的抄表分区ID
    private ArrayList<Integer> bookIDList = new ArrayList<>();  //用于保存抄表本ID值
    private ArrayList<Integer> areaIDLsit = new ArrayList<>();  //用于保存抄表分区ID值
    private SharedPreferences public_sharedPreferences, sharedPreferences_login;
   // private String ip, port;  //接口ip地址   端口
    public int responseCode = 0;
    private String userResult;  //请求抄表用户数据结果
    private LayoutInflater layoutInflater;
    private PopupWindow loadingPopup, fileNamePopup;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private LinearLayout rootLinearlayout;
    private View view, fileNameView, line;
    private TextView title, tips, confirm, ensure;  //加载进度的提示
    private EditText fileNameEdit;
    private String bookParams = "";  //抄表本参数
    private String areaParams = "";  //抄表分区参数
    private JSONObject userObject;     //用户object对象
    private JSONArray userJsonArray;   //用户JsonArray对象
    private SQLiteDatabase db;  //数据库
    private Map<String, String> bookMap = new HashMap<>();
    private Map<String, String> bookAndAreaMap = new HashMap<>();
    private Map<String, String> areaMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_data_download);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件ID
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        clear = (TextView) findViewById(R.id.clear);
        selectBookNumber = (TextView) findViewById(R.id.select_book_number);
        selectAreaNumber = (TextView) findViewById(R.id.select_area_number);
        booklistView = (ListView) findViewById(R.id.meter_book_lv);
        arealistView = (ListView) findViewById(R.id.meter_area_lv);
        noAreaData = (TextView) findViewById(R.id.no_area_data);
        noBookData = (TextView) findViewById(R.id.no_book_data);
        begianNum = (EditText) findViewById(R.id.begain_num);
        endNum = (EditText) findViewById(R.id.end_num);
        downLoadBtn = (Button) findViewById(R.id.downLoad_btn);
        bookSelectAll = (RadioButton) findViewById(R.id.book_select_all);
        bookReverse = (RadioButton) findViewById(R.id.book_reverse);
        bookSelectCancel = (RadioButton) findViewById(R.id.book_select_cancel);
        areaSelectAll = (RadioButton) findViewById(R.id.area_select_all);
        areaReverse = (RadioButton) findViewById(R.id.area_reverse);
        areaSelectCancel = (RadioButton) findViewById(R.id.area_select_cancel);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterDataDownloadActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        public_sharedPreferences = MeterDataDownloadActivity.this.getSharedPreferences("data", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle meterData = intent.getExtras();
            if (meterData != null) {
                Log.i("MeterDataDownload", "数据接收成功");

                ArrayList<BookInfo> bookInfoArrayList = meterData.getParcelableArrayList("bookInfoArrayList");
                ArrayList<AreaInfo> areaInfoArrayList = meterData.getParcelableArrayList("areaInfoArrayList");
                //初始化抄表分区listview
                if (areaInfoArrayList != null) {
                    for (int i = 0; i < areaInfoArrayList.size(); i++) {
                        areaInfoList.add(areaInfoArrayList.get(i));
                    }
                    if (areaInfoList.size() == 0) {
                        noAreaData.setVisibility(View.VISIBLE);
                    }
                    areaAdapter = new AreaDataAdapter(MeterDataDownloadActivity.this, areaInfoList);
                    arealistView.setAdapter(areaAdapter);
                    MyAnimationUtils.viewGroupOutAnimation(MeterDataDownloadActivity.this, arealistView, 0.1F);
                }
                //初始化抄表本listview
                if (bookInfoArrayList != null) {
                    for (int i = 0; i < bookInfoArrayList.size(); i++) {
                        bookInfoList.add(bookInfoArrayList.get(i));
                    }
                    if (bookInfoList.size() == 0) {
                        noBookData.setVisibility(View.VISIBLE);
                    }
                    bookAdapter = new BookDataAdapter(MeterDataDownloadActivity.this, bookInfoList);
                    booklistView.setAdapter(bookAdapter);
                    MyAnimationUtils.viewGroupOutAnimation(MeterDataDownloadActivity.this, booklistView, 0.1F);
                }
            }
        }
    }

    //点击事件
    private void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        clear.setOnClickListener(onClickListener);
        arealistView.setOnItemClickListener(new OnItemClickListener() {   //抄表分区item点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeterAreaViewHolder holder = (MeterAreaViewHolder) view.getTag();
                holder.checkedState.toggle();
                AreaDataAdapter.getIsCheck().put(position, holder.checkedState.isChecked());
                if (holder.checkedState.isChecked()) {
                    areaNum++;
                } else {
                    areaNum--;
                }
                //TextView显示
                selectAreaNumber.setText("(" + areaNum + ")");
            }
        });
        booklistView.setOnItemClickListener(new OnItemClickListener() {   //抄表本item点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeterBookViewHolder holder = (MeterBookViewHolder) view.getTag();
                holder.checkedState.toggle();
                BookDataAdapter.getIsCheck().put(position, holder.checkedState.isChecked());
                // 调整选定条目
                if (holder.checkedState.isChecked()) {
                    bookNum++;
                } else {
                    bookNum--;
                }


                //TextView显示
                selectBookNumber.setText("(" + bookNum + ")");
            }
        });
        bookSelectAll.setOnClickListener(onClickListener);
        bookReverse.setOnClickListener(onClickListener);
        bookSelectCancel.setOnClickListener(onClickListener);
        areaSelectAll.setOnClickListener(onClickListener);
        areaReverse.setOnClickListener(onClickListener);
        areaSelectCancel.setOnClickListener(onClickListener);
        downLoadBtn.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.clear:
                    //清除
                    db.delete("MeterUser", null, null);  //删除User表中当前用户的所有数据（官方推荐方法）
                    db.delete("MeterFile", null, null);
                    db.delete("MeterBook", null, null);
                    db.delete("MeterNumerical", null, null);
                    db.delete("MeterPhoto", null, null);
                    //设置id从1开始（sqlite默认id从1开始），若没有这一句，id将会延续删除之前的id
                    db.execSQL("update sqlite_sequence set seq=0 where name='MeterUser'");
                    db.execSQL("update sqlite_sequence set seq=0 where name='MeterFile'");
                    db.execSQL("update sqlite_sequence set seq=0 where name='MeterBook'");
                    db.execSQL("update sqlite_sequence set seq=0 where name='MeterNumerical'");
                    db.execSQL("update sqlite_sequence set seq=0 where name='MeterPhoto'");
                    Toast.makeText(MeterDataDownloadActivity.this, "清除数据成功！", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.book_select_all:
                    bookSelectAll();
                    break;
                case R.id.book_reverse:
                    bookReverse();
                    break;
                case R.id.book_select_cancel:
                    bookSelectCancle();
                    break;
                case R.id.area_select_all:
                    areaSelectAll();
                    break;
                case R.id.area_reverse:
                    areaReverse();
                    break;
                case R.id.area_select_cancel:
                    areaSelectCancle();
                    break;
                case R.id.downLoad_btn:
                    //下载
                    new Thread() {
                        @Override
                        public void run() {
                            saveBookInfo();  //保存选中的抄表本ID信息
                            saveAreaInfo();  //保存选中的抄表分区ID信息
                            getMeterUserData();   //获取抄表用户数据
                        }
                    }.start();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取抄表本参数
     */
    private void getBookParams() {
        bookParams = "";
        for (int i = 0; i < bookIDList.size(); i++) {
            if (bookIDList.size() == 1) {  //两个编号不为空，抄表本选择了一条
                bookParams = bookIDMap.get("bookID" + bookIDList.get(0)) + "";
            } else {   //两个编号不为空，抄表本选择了多条
                if (i != (bookIDList.size() - 1)) {  //如果不是集合的最后一个元素后面就加逗号
                    bookParams += bookIDMap.get("bookID" + bookIDList.get(i)) + ",";
                } else {  //最后一个不加逗号
                    bookParams += bookIDMap.get("bookID" + bookIDList.get(i)) + "";
                }
            }
        }
    }

    /**
     * 获取抄表分区参数
     */
    private void getAreaParams() {
        areaParams = "";
        for (int i = 0; i < areaIDLsit.size(); i++) {
            if (areaIDLsit.size() == 1) {  //两个编号不为空，抄表分区选择了一条
                areaParams = areaIDMap.get("areaID" + areaIDLsit.get(0)) + "";
            } else {  //两个编号不为空，抄表分区选择了多条
                if (i != (areaIDLsit.size() - 1)) {  //如果不是集合的最后一个元素后面就加逗号
                    areaParams += areaIDMap.get("areaID" + areaIDLsit.get(i)) + ",";
                } else { //最后一个不加逗号
                    areaParams += areaIDMap.get("areaID" + areaIDLsit.get(i)) + "";
                }
            }
        }
    }

    /**
     * 获取抄表用户数据
     */
    private void getMeterUserData() {
        if (!"".equals(begianNum.getText().toString().trim()) && !"".equals(endNum.getText().toString().trim())) { //首先判断两个编号都不为空
            if (bookIDMap.size() != 0 && areaIDMap.size() != 0) { //两个编号不为空，并且抄表本、抄表分区都已经选择
                handler.sendEmptyMessage(9);
                //此时会下载三个部分的数据
                getBookParams();  //获取抄表本参数
                getAreaParams();  //获取抄表分区参数
                String params = "u.c_user_id between" + begianNum.getText().toString() +
                        " and " + endNum.getText().toString() + " and b.n_book_id in(" + bookParams +
                        ") and a.n_area_id in(" + areaParams + ")";
                try {
                    params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                    requireMeterUserData("findUserMeter.do", params);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {  //要么抄表本、抄表分区其中一个已经选择，要么两个都没选择
                if (bookIDMap.size() == 0 && areaIDMap.size() == 0) {//两个编号不为空，但是抄表本、抄表分区都未选择
                    handler.sendEmptyMessage(9);
                    //此时只会下载 编号范围 部分的数据
                    String params = "u.c_user_id between" + begianNum.getText().toString() +
                            " and " + endNum.getText().toString();
                    try {
                        params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                        requireMeterUserData("findUserMeter.do", params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else if (bookIDMap.size() == 0) { //两个编号不为空，抄表分区已经选择，但是抄表本未选择
                    handler.sendEmptyMessage(9);
                    //此时会下载 编号范围、抄表分区 两个部分的数据
                    getAreaParams();  //获取抄表分区参数
                    String params = "u.c_user_id between" + begianNum.getText().toString() +
                            " and " + endNum.getText().toString() +
                            " and a.n_area_id in(" + areaParams + ")";
                    try {
                        params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                        requireMeterUserData("findUserMeter.do", params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else if (areaIDMap.size() == 0) { //两个编号不为空，抄表本已经选择，但是抄表分区未选择
                    handler.sendEmptyMessage(9);
                    //此时会下载 编号范围、抄表本 两个部分的数据
                    getBookParams();  //获取抄表本参数
                    String params = "u.c_user_id between" + begianNum.getText().toString() +
                            " and " + endNum.getText().toString() + " and b.n_book_id in(" + bookParams + ")";
                    try {
                        params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                        requireMeterUserData("findUserMeter.do", params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {  //要么两个编号都为空，要么其中一个为空
            if ("".equals(begianNum.getText().toString().trim()) && "".equals(endNum.getText().toString().trim())) { //两个编号都为空
                if (bookIDMap.size() != 0 && areaIDMap.size() != 0) {  //抄表本、抄表分区都已经选择
                    handler.sendEmptyMessage(9);
                    //此时会下载 抄表本、抄表分区 两个部分的数据
                    getBookParams();  //获取抄表本参数
                    getAreaParams();  //获取抄表分区参数
                    Log.i("getMeterUserData", "抄表本参数为：" + bookParams);
                    String params = "b.n_book_id in(" + bookParams + ") and a.n_area_id in(" + areaParams + ")";
                    try {
                        params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                        requireMeterUserData("findUserMeter.do", params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (bookIDMap.size() == 0 && areaIDMap.size() == 0) { //两个编号都为空，并且抄表本、抄表分区都未选择
                        //此时提示选择其中之一
                        handler.sendEmptyMessage(6);
                    } else if (bookIDMap.size() == 0) { //两个编号都为空，抄表分区已经选择，但是抄表本未选择
                        handler.sendEmptyMessage(9);
                        //此时只会下载 抄表分区 部分的数据
                        getAreaParams();  //获取抄表分区参数
                        String params = "a.n_area_id in(" + areaParams + ")";
                        try {
                            params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                            requireMeterUserData("findUserMeter.do", params);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else if (areaIDMap.size() == 0) { //两个编号都为空，抄表本已经选择，但是抄表分区未选择
                        handler.sendEmptyMessage(9);
                        //此时只会下载 抄表本 部分的数据
                        getBookParams();  //获取抄表本参数
                        String params = "b.n_book_id in(" + bookParams + ")";
                        try {
                            params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                            requireMeterUserData("findUserMeter.do", params);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else { //其中一个编号为空
                handler.sendEmptyMessage(8);
            }
        }
    }

    private void getParamsAndLoading() {
        showLoadingPopup();
        tips.setText("正在获取用户数据，请稍后...");
    }

    //加载进度动画
    public void showLoadingPopup() {
        layoutInflater = LayoutInflater.from(MeterDataDownloadActivity.this);
        view = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        loadingPopup = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) view.findViewById(R.id.frame_animation);
        title = (TextView) view.findViewById(R.id.title);
        tips = (TextView) view.findViewById(R.id.tips);
        line = view.findViewById(R.id.line);
        ensure = (TextView) view.findViewById(R.id.ensure);
        ensure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPopup.dismiss();
            }
        });
        loadingPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        loadingPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loadingPopup.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        loadingPopup.setAnimationStyle(R.style.camera);
        loadingPopup.update();
        loadingPopup.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        startFrameAnimation();
        loadingPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //show文件命名窗口
    public void showNameFilePopup() {
        layoutInflater = LayoutInflater.from(MeterDataDownloadActivity.this);
        fileNameView = layoutInflater.inflate(R.layout.popupwindow_meter_name_the_file, null);
        fileNamePopup = new PopupWindow(fileNameView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        frameAnimation = (ImageView) fileNameView.findViewById(R.id.frame_animation);
        fileNameEdit = (EditText) fileNameView.findViewById(R.id.file_name_edit);
        confirm = (TextView) fileNameView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(fileNameEdit.getText().toString())) {
                    fileNamePopup.dismiss();
                    showLoadingPopup();
                    tips.setText("正在配置数据库文件，请稍后...");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                if (0 == queryMeterFileName(fileNameEdit.getText().toString())) {
                                    insertMeterFile();                                 //将抄表文件名保存至本地 MeterFile 表
                                    bookMap.clear();
                                    for (int i = 0; i < userJsonArray.length(); i++) {
                                        try {
                                            userObject = userJsonArray.getJSONObject(i);
                                            insertMeterUserData(i+1); //将抄表用户数据插入本地数据库
                                            bookMap.put(userObject.optInt("n_book_id", 0) + "", userObject.optString("c_book_name", ""));
                                            //下载条数是否大于5

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    for (String bookID : bookMap.keySet()) {
                                        insertMeterBook(bookID, bookMap.get(bookID));   //将抄表本数据保存至本地 MeterBook 表
                                        Log.i("bookMap", "保存的表册ID为：" + bookID + "表册名为：" + bookMap.get(bookID) + "抄表文件名称为：" + fileNameEdit.getText().toString());
                                    }
//                                    /**
//                                     * 更新下载进度
//                                     */
//                                    for (int j = 0; j < 21; j++) {
//                                        Thread.sleep(250);
//                                        Message message = new Message();
//                                        message.what = 4;
//                                        message.arg1 = 5 * j;
//                                        handler.sendMessage(message);
//                                    }
                                    /**
                                     * 更新下载进度
                                     */
                                    if (userJsonArray.length()<500){
                                        for (int j = 0; j < 21; j++) {
                                            Thread.sleep(250);
                                            Message message = new Message();
                                            message.what = 12;
                                            message.arg1 = 5 * j;
                                            handler.sendMessage(message);
                                        }
                                    }
                                    //下载成功
                                    handler.sendEmptyMessage(11);


                                } else {
                                    Thread.sleep(1000);
                                    handler.sendEmptyMessage(7);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } else {
                    Toast.makeText(MeterDataDownloadActivity.this, "文件名不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fileNamePopup.update();
        fileNamePopup.setFocusable(true);
        fileNamePopup.setOutsideTouchable(true);
        fileNamePopup.setBackgroundDrawable(getResources().getDrawable(R.drawable.business_check_shape));
        fileNamePopup.setAnimationStyle(R.style.camera);
        fileNamePopup.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        fileNamePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
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
        WindowManager.LayoutParams lp = MeterDataDownloadActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterDataDownloadActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterDataDownloadActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterDataDownloadActivity.this.getWindow().setAttributes(lp);
    }

    //请求抄表用户数据
    private void requireMeterUserData(final String method, final String keyAndValue) {
        try {
            URL url;
            HttpURLConnection httpURLConnection;
            Log.i("sharedPreferences====>", public_sharedPreferences.getString("IP", ""));

          //  String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
            String httpUrl=new StringBuffer().append(SkUrl.SkHttp(MeterDataDownloadActivity.this)).append(method).toString();

            //有参数传递
            if (!keyAndValue.equals("")) {
                url = new URL(httpUrl + "?param1=" + keyAndValue);
                //没有参数传递
            } else {
                url = new URL(httpUrl);
            }
            Log.i("MeterDataDownloadAct", url + "");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(60000);
            httpURLConnection.setReadTimeout(60000);
            httpURLConnection.connect();
            //传回的数据解析成String
            Log.i("MeterDataDownloadAct", "responseCode=" + httpURLConnection.getResponseCode());
            if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    stringBuilder.append(str);
                }
                userResult = stringBuilder.toString();
                Log.i("MeterDataDownloadAct", userResult);
                userJsonArray = new JSONArray(userResult);
                if (userJsonArray.length() != 0) {
                    handler.sendEmptyMessage(5);
                } else {
                    handler.sendEmptyMessage(1);
                }
            } else {
                Log.i("IOException==========>", "网络请求异常!");
                handler.sendEmptyMessage(2);
            }
        } catch (UnsupportedEncodingException | MalformedURLException | JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求超时!");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    title.setText("正在配置数据库文件，请稍后...");
                    break;
                case 1:
                    loadingPopup.dismiss();
                    Toast.makeText(MeterDataDownloadActivity.this, "没有抄表用户数据哦！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    loadingPopup.dismiss();
                    Toast.makeText(MeterDataDownloadActivity.this, "网络请求异常！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    loadingPopup.dismiss();
                    Toast.makeText(MeterDataDownloadActivity.this, "网络请求超时！", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
//                    if (msg.arg1 == 0) {
//                        title.setVisibility(View.VISIBLE);
//                        title.setText("正在下载" + userJsonArray.length() + "条数据，请稍后...");
//                    }
//                    tips.setText("下载进度为：" + msg.arg1 + "%");
//                    if (msg.arg1 == 100) {
//                        title.setText("数据下载成功！");
//                        frameAnimation.setVisibility(View.GONE);
//                        tips.setVisibility(View.GONE);
//                        line.setVisibility(View.VISIBLE);
//                        ensure.setVisibility(View.VISIBLE);
//                        //Toast.makeText(MeterDataDownloadActivity.this, "数据下载成功！", Toast.LENGTH_SHORT).show();
//                    }
                    if (msg.arg1 == 10) {
                        title.setVisibility(View.VISIBLE);
                        title.setText("正在下载" + userJsonArray.length() + "条数据，请稍后...");
                    }
                    tips.setText("下载进度为：" + msg.arg1 * 100/userJsonArray.length() + "%");
                    Log.e("计算=",(msg.arg1+1) * 100/(userJsonArray.length())+"");
                    break;
                case 5:
                    loadingPopup.dismiss();
                    showNameFilePopup();  //用户给下载文件命名
                    break;
                case 6:
                    Toast.makeText(MeterDataDownloadActivity.this, "至少选择其中一个区域才能下载哦", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    loadingPopup.dismiss();
                    showNameFilePopup();
                    Toast.makeText(MeterDataDownloadActivity.this, "该文件名已存在，请您重新命名！", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(MeterDataDownloadActivity.this, "请您将区间填写完整！", Toast.LENGTH_SHORT).show();
                    break;
                case 9:
                    getParamsAndLoading();
                    break;

                case 11:
                    title.setText("数据下载成功！");
                    frameAnimation.setVisibility(View.GONE);
                    tips.setVisibility(View.GONE);
                    line.setVisibility(View.VISIBLE);
                    ensure.setVisibility(View.VISIBLE);
                    break;

                case 12:
                    if (msg.arg1 == 0) {
                        title.setVisibility(View.VISIBLE);
                        title.setText("正在下载" + userJsonArray.length() + "条数据，请稍后...");
                    }
                    tips.setText("下载进度为：" + msg.arg1 + "%");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 将抄表用户数据保存至本地 MeterUser 表
     */
    private void insertMeterUserData(int i) {
        ContentValues values = new ContentValues();
        values.put("login_user_id", sharedPreferences_login.getString("userId", ""));          //登录人ID
        values.put("meter_reader_id", userObject.optInt("n_user_meter_reader_id", 0) + "");    //抄表员ID
        values.put("meter_reader_name", userObject.optString("c_meter_user_name", ""));        //抄表员名称
        values.put("meter_date", "暂无");                                                      //抄表时间
        values.put("user_phone", userObject.optString("c_user_phone", ""));                    //用户电话
        values.put("user_amount", userObject.optInt("n_amount", 0) + "");                      //用户余额
        values.put("meter_degrees", userObject.optInt("n_meter_degrees", 0) + "");             //上月读数
        values.put("meter_number", userObject.optString("c_meter_number", ""));                //表编号
        values.put("arrearage_months", userObject.optInt("cnt", 0) + "");                      //欠费月数
        values.put("mix_state", userObject.optInt("n_mix_state", 0) + "");                     //混合使用状态（0正常  1混合）
        values.put("meter_order_number", userObject.optInt("n_order_num1", 0));                //抄表序号
        values.put("arrearage_amount", userObject.optInt("arrearageAmount", 0) + "");          //欠费金额
        values.put("area_id", userObject.optInt("n_area_id", 0) + "");                         //抄表本分区ID
        values.put("area_name", userObject.optString("c_area_name", ""));                      //抄表本分区名称
        values.put("user_name", userObject.optString("c_user_name", ""));                      //用户名
        values.put("last_month_dosage", userObject.optInt("n_dosage", 0) + "");                //上月用量
        values.put("property_id", userObject.optInt("n_properties_id", 0) + "");               //性质ID
        values.put("property_name", userObject.optString("c_properties_name", ""));            //性质名称
        values.put("user_id", userObject.optString("c_user_id", ""));                          //用户ID
        values.put("book_id", userObject.optInt("n_book_id", 0) + "");                         //抄表本ID
        values.put("float_range", userObject.optString("floatOver", ""));                      //浮动范围
        values.put("meterState", "false");                                                     //抄表状态
        values.put("dosage_change", userObject.optInt("n_change_meter", 0) + "");              //更换量
        values.put("user_address", userObject.optString("c_user_address", ""));                //用户地址
        values.put("start_dosage", userObject.optInt("n_minimum", 0) + "");                    //启用量
        values.put("old_user_id", userObject.optString("c_old_user_id", ""));                  //用户老编号
        values.put("book_name", userObject.optString("c_book_name", ""));                      //抄表本名称
        values.put("meter_model", userObject.optString("c_model_name", ""));                   //表型号
        values.put("rubbish_cost", userObject.optInt("rubbishCost", 0) + "");                  //垃圾费
        values.put("remission", userObject.optDouble("n_remission", 0) + "");                   //加减量
        values.put("locationAddress", "未定位");                                                //定位地址
        values.put("file_name", fileNameEdit.getText().toString());                            //本地文件名
        values.put("uploadState", "false");                                                     //上传状态
        if (userObject.optString("n_stateId").equals("null") || userObject.optString("n_stateId").equals("")) {
            values.put("n_state_id", "0");                                                     //估录
        } else {
            values.put("n_state_id", userObject.optString("n_stateId"));                                                     //上传状态
        }
        if (TextUtils.isEmpty(userObject.optString("c_opened_remark"))||userObject.optString("c_opened_remark").equals("null")) {
            values.put("opened_remark", "无");
            Log.e("无",userObject.optString("c_opened_remark"));
        } else {
            values.put("opened_remark", userObject.optString("c_opened_remark", ""));//启用说明
            Log.e("有",userObject.optString("c_opened_remark"));
        }
        if (TextUtils.isEmpty(userObject.optString("c_user_remark"))||userObject.optString("c_user_remark").equals("null")) {
            values.put("user_remark", "无");
        } else {
            values.put("user_remark", userObject.optString("c_user_remark", ""));//抄表备注
        }

        if (userObject.optString("c_opened_remark", "").equals("11")){
            values.put("n_user_state","停水");
        }else if (userObject.optString("c_opened_remark", "").equals("12")){
            values.put("n_user_state","堵水");
        }else {
            values.put("n_user_state","正常");
        }



        //下面这些个字段抄表完成后需上传
        values.put("this_month_dosage", "");                                               //本月用量
        values.put("this_month_end_degree", "");                                               //本月止度
        values.put("n_jw_x", "未获取");                                                        //纬度
        values.put("n_jw_y", "未获取");                                                        //经度
        values.put("d_jw_time", "未获取");                                                     //抄表时间
        values.put("n_state_remark", "");                                                     //抄表时间

        db.insert("MeterUser", null, values);
        Log.i("insertMeterUserData", "用户数据插入成功");
        if (userJsonArray.length() >500) {
            if (i % 10 == 0) {
                Message message = new Message();
                message.what = 4;
                message.arg1 = i;
                handler.sendMessage(message);
            }
        }
    }

    /**
     * 将抄表本数据保存至本地 MeterBook 表
     */
    private void insertMeterBook(String bookID, String bookName) {
        ContentValues values = new ContentValues();
        values.put("bookId", bookID);                                                             //抄表本ID
        values.put("bookName", bookName);                                                         //抄表本名称
        values.put("fileName", fileNameEdit.getText().toString());                                //抄表文件名称
        values.put("login_user_id", sharedPreferences_login.getString("userId", ""));             //当前登录人的ID
        values.put("login_user_name", sharedPreferences_login.getString("user_name", ""));        //当前登录人的名称
        db.insert("MeterBook", null, values);
    }

    /**
     * 查询MeterFile表中文件名是否存在
     */
    public int queryMeterFileName(String fileName) {
        Cursor cursor = db.rawQuery("select * from MeterFile where login_user_id=? and fileName=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName});//查询并获得游标
        Log.i("queryMeterFileName", "查询文件表结果为：" + cursor.getCount());
        //如果游标为空，则可以下载
        if (cursor.getCount() == 0) {
            return 0;
        }
        cursor.close();
        return cursor.getCount();
    }

    /**
     * 将抄表文件数据保存至本地 MeterFile 表
     */
    private void insertMeterFile() {
        ContentValues values = new ContentValues();
        values.put("fileName", fileNameEdit.getText().toString());                                //抄表文件名称
        values.put("login_user_id", sharedPreferences_login.getString("userId", ""));             //当前登录人的ID
        values.put("login_user_name", sharedPreferences_login.getString("user_name", ""));        //当前登录人的名称
        db.insert("MeterFile", null, values);
    }

    // 刷新抄表本listview和TextView的显示
    private void bookDataChanged() {
        // 通知listView刷新
        bookAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        selectBookNumber.setText("(" + bookNum + ")");
    }

    // 刷新抄表分区listview和TextView的显示
    private void areaDataChanged() {
        // 通知listView刷新
        areaAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        selectAreaNumber.setText("(" + areaNum + ")");
    }

    //抄表本全选
    public void bookSelectAll() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            BookDataAdapter.getIsCheck().put(i, true);
        }
        bookNum = bookInfoList.size();
        bookDataChanged();
    }

    //抄表本反选
    public void bookReverse() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookDataAdapter.getIsCheck().put(i, false);
                bookNum--;//数量减一
            } else {
                BookDataAdapter.getIsCheck().put(i, true);
                bookNum++;//数量加一
            }
        }
        bookDataChanged();
    }

    //抄表本取消选择
    public void bookSelectCancle() {
        for (int i = 0; i < bookInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookDataAdapter.getIsCheck().put(i, false);
                bookNum--; //数量减一
            }
        }
        bookDataChanged();
    }

    //抄表分区全选
    public void areaSelectAll() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            AreaDataAdapter.getIsCheck().put(i, true);
        }
        areaNum = areaInfoList.size();
        areaDataChanged();
    }

    //抄表分区反选
    public void areaReverse() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            if (AreaDataAdapter.getIsCheck().get(i)) {
                AreaDataAdapter.getIsCheck().put(i, false);
                areaNum--;
            } else {
                AreaDataAdapter.getIsCheck().put(i, true);
                areaNum++;
            }
        }
        areaDataChanged();
    }

    //抄表分区取消选择
    public void areaSelectCancle() {
        for (int i = 0; i < areaInfoList.size(); i++) {
            if (AreaDataAdapter.getIsCheck().get(i)) {
                AreaDataAdapter.getIsCheck().put(i, false);
                areaNum--;
            }
        }
        areaDataChanged();
    }

    //保存选中的抄表本ID信息
    public void saveBookInfo() {
        bookIDMap.clear();
        bookIDList.clear();
        for (int i = 0; i < bookInfoList.size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookInfo bookInfo = bookInfoList.get((int) bookAdapter.getItemId(i));
                bookIDMap.put("bookID" + i, Integer.parseInt(bookInfo.getID()));
                Log.i("bookID=========>", "这次被勾选第" + i + "个，抄表本ID为：" + bookInfo.getID());
                bookIDList.add(i);
            }
        }
    }

    //保存选中的抄表分区ID信息
    public void saveAreaInfo() {
        areaIDMap.clear();
        areaIDLsit.clear();
        for (int i = 0; i < areaInfoList.size(); i++) {
            if (AreaDataAdapter.getIsCheck().get(i)) {
                AreaInfo areaInfo = areaInfoList.get((int) areaAdapter.getItemId(i));
                areaIDMap.put("areaID" + i, Integer.parseInt(areaInfo.getID()));
                Log.i("areaID=========>", "这次被勾选第" + i + "个，抄表分区ID为：" + areaInfo.getID());
                areaIDLsit.add(i);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        if (loadingPopup != null) {
            if (loadingPopup.isShowing()) {
                loadingPopup.dismiss();
            }
        }
    }
}
