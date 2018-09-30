package com.example.administrator.thinker_soft.meter_code.sk.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.example.administrator.thinker_soft.meter_code.adapter.AreaDataAdapter;
import com.example.administrator.thinker_soft.meter_code.adapter.BookDataAdapter;
import com.example.administrator.thinker_soft.meter_code.model.AreaInfo;
import com.example.administrator.thinker_soft.meter_code.model.BookInfo;
import com.example.administrator.thinker_soft.meter_code.model.MeterBookViewHolder;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.google.gson.Gson;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 抄表员
 * Created by Administrator on 2018/4/19.
 */

public class MeterReaderDownloadActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private Unbinder mUnbinder;
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    /**
     * 默认值
     */
    private long lastClickTime = 0;
    private int lastposition = 0;
    /**
     * 根据抄表员添加
     */
    private LinkedHashMap<String, ArrayList<BookInfo>> mReaderDataMap = new LinkedHashMap<String, ArrayList<BookInfo>>();
    //  private Map<String, ArrayList<BookInfo>> mReaderDataMap = new HashMap<>();
    // private List<BookInfo> bookInfoList;//地区
    //  private List<AreaInfo> areaInfoArrayList;//本
    /**
     * 抄表本
     */
    private BookDataAdapter bookAdapter;
    /**
     * 抄表员
     */
    private AreaDataAdapter areaAdapter;
    /**
     * 本地储存
     */
    private SharedPreferences public_sharedPreferences, sharedPreferences_login;
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * 用于保存选中的抄表本ID
     */
    private HashMap<String, Integer> bookIDMap = new HashMap<String, Integer>();
    /**
     * 用于保存选中的抄表分区ID
     */
    private HashMap<String, Integer> areaIDMap = new HashMap<String, Integer>();
    /**
     * 用于保存抄表本ID值
     */
    private ArrayList<Integer> bookIDList = new ArrayList<>();
    /**
     * 加载进度的提示
     */
    private TextView title, tips, confirm, ensure;
    private View view, fileNameView, line;
    /**
     * 加载弹出框
     */
    private PopupWindow loadingPopup, fileNamePopup;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private LayoutInflater layoutInflater;
    /**
     * 文件名称
     */
    private EditText fileNameEdit;
    /**
     * 用户object对象
     */
    private JSONObject userObject;
    /**
     * 用户JsonArray对象
     */
    private JSONArray userJsonArray;
    private Map<String, String> bookMap = new HashMap<>();
    /**
     * 请求抄表用户数据结果
     */
    private String userResult;
    /**
     * 抄表本参数
     */
    private String bookParams = "";
    /**
     * 抄表分区参数
     */
    private String areaParams = "";
    private int bookNum = 0; // 记录抄表本选中的条目数量

    @BindView(R.id.tv_select_book_number)
    TextView bookNumber;//选择的本数
    @BindView(R.id.listView_reader)
    ListView listViewReader; //抄表员列表
    @BindView(R.id.tv_noArea_data)
    TextView noAreaData;//没有数据
    @BindView(R.id.listView_book)
    ListView listViewBook;//抄表本
    @BindView(R.id.radio_select_rg)
    RadioGroup radioGroup;//选择
    @BindView(R.id.back)
    ImageView back;//返回
    @BindView(R.id.clear)
    TextView clear;//清除
    @BindView(R.id.radio_btn_book_select_cancel)
    RadioButton cancle;//取消
    @BindView(R.id.layout_meter)
    LinearLayout rootLinearlayout;
    @BindView(R.id.no_book_data)
    TextView noBookData;//没有数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_reader);
        mUnbinder = ButterKnife.bind(this);//绑定
        initView();

    }

    /**
     * 初始化
     */
    private void initView() {

        radioGroup.setOnCheckedChangeListener(this);
        back.setOnClickListener(this);
        clear.setOnClickListener(this);

        MySqliteHelper helper = new MySqliteHelper(MeterReaderDownloadActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        public_sharedPreferences = MeterReaderDownloadActivity.this.getSharedPreferences("data", Context.MODE_PRIVATE);
        Bundle meterData = this.getIntent().getExtras();
        if (meterData != null) {
            //地区
            List<BookInfo> bookInfoList = meterData.getParcelableArrayList("bookInfoArrayList");
            //areaInfoArrayList = meterData.getParcelableArrayList("areaInfoArrayList");
            if (bookInfoList.size() == 0) {
                noBookData.setVisibility(View.VISIBLE);
            }
            //相同元素
            ArrayList<BookInfo> areaInfos;
            ArrayList<String> arrayList = new ArrayList<String>();//抄表员
            List<AreaInfo> areaInfos1 = new ArrayList<>();//抄表员列表
            ListIterator<BookInfo> iter = bookInfoList.listIterator();
            while (iter.hasNext()) {
                BookInfo per = (BookInfo) iter.next();
                if (!arrayList.contains(per.getBOOKREMARK())) {
                    //contains方法判断是否包含,底层依赖equals方法
                    arrayList.add(per.getBOOKREMARK());
                    AreaInfo areaInfo = new AreaInfo();
                    areaInfo.setArea(per.getBOOKREMARK());
                    areaInfos1.add(areaInfo);//添加抄表员

                    Log.e("不相同的==", per.getBOOKREMARK());
                    areaInfos = new ArrayList<BookInfo>();
                    for (BookInfo bookInfo : bookInfoList) {
                        if (bookInfo.getBOOKREMARK().equals(per.getBOOKREMARK())) {
                            areaInfos.add(bookInfo);
                            Log.e("名称", per.getBOOKREMARK());
                        }
                    }
                    Log.e("-----", new Gson().toJson(areaInfos));
                    mReaderDataMap.put(per.getBOOKREMARK(), areaInfos);
                }
            }
            //  添加adapter
            areaAdapter = new AreaDataAdapter(MeterReaderDownloadActivity.this, areaInfos1);
            listViewReader.setAdapter(areaAdapter);
            //是否有数据
            if (areaAdapter.getList().get(0).getArea().equals("无")) {
                noAreaData.setVisibility(View.VISIBLE);
                listViewReader.setVisibility(View.GONE);
            } else {
                if (areaAdapter.getList().size() > 0) {
                    AreaDataAdapter.getIsCheck().put(0, true);
                }
            }
            MyAnimationUtils.viewGroupOutAnimation(MeterReaderDownloadActivity.this, listViewReader, 0.1F);

            listViewReader.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //防止快速点击
//                    long currentTime = Calendar.getInstance().getTimeInMillis();
//                    if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
//                        lastClickTime = currentTime;
                    if (lastposition != position) {
                        //单选
                        AreaDataAdapter.getIsCheck().put(position, !AreaDataAdapter.getIsCheck().get(position));
                        AreaDataAdapter.getIsCheck().put(lastposition, false);
                        Log.e("index", lastposition + "===" + position);
                        //刷新
                        updateView(lastposition);
                        updateView(position);
                        //赋值
                        lastposition = position;
                        //默认选中
                        if (position == 0) {
                            AreaDataAdapter.getIsCheck().put(position, true);
                            updateView(position);
                        }
                        // areaAdapter.notifyDataSetChanged();
                        bookAdapter = new BookDataAdapter(MeterReaderDownloadActivity.this, mReaderDataMap.get(areaAdapter.getList().get(position).getArea()));
                        listViewBook.setAdapter(bookAdapter);
                        cancle.setChecked(true);//取消选择
                        bookNum = 0;
                        bookDataChanged();//刷新
                    }

                }
                //      }
            });

            MyAnimationUtils.viewGroupOutAnimation(MeterReaderDownloadActivity.this, listViewReader, 0.1F);
            bookAdapter = new BookDataAdapter(MeterReaderDownloadActivity.this, mReaderDataMap.get(arrayList.get(0)));
            listViewBook.setAdapter(bookAdapter);

            MyAnimationUtils.viewGroupOutAnimation(MeterReaderDownloadActivity.this, listViewBook, 0.1F);
            listViewBook.setOnItemClickListener(this);
//            readerDownAdapter.addAll(bookInfoArrayList);


        }

    }

    private void updateView(int itemIndex) {
        //得到第一个可显示控件的位置，
        int visiblePosition = listViewReader.getFirstVisiblePosition();
        //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
        if (itemIndex - visiblePosition >= 0) {
            //得到要更新的item的view
            View view = listViewReader.getChildAt(itemIndex - visiblePosition);
            //调用adapter更新界面
            areaAdapter.updateView(view, itemIndex);
        }
    }

    /**
     * 获得从上一个Activity获取数据
     *
     * @return String
     */
    private List<BookInfo> getMeterData() {
        // 直接通过Context类的getIntent()即可获取Intent

        Bundle meterData = getIntent().getExtras();
        // 判断
        if (meterData != null) {
            return meterData.getParcelableArrayList("bookInfoArrayList");
        } else {
            return null;
        }
    }

    /**
     * 下载
     *
     * @param view
     */
    @OnClick({R.id.btn_downLoad_btn})
    public void downloadsOnclick(View view) {
        saveBookInfo();  //保存选中的抄表本ID信息

    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.radio_btn_book_select_all:
                //全选
                SelectAll();
                break;
            case R.id.radio_btn_book_reverse:
                //反选
                bookReverse();
                break;
            case R.id.radio_btn_book_select_cancel:
                //取消
                bookSelectCancle();

                break;
            default:
                break;

        }
        //设置选中的条数
        //  bookNumber.setText("(" +bookNum+ ")");
    }


    /**
     * 监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.clear:
                //清除
                db.delete("MeterUser", null, null);  //删除User表中当前用户的所有数据（官方推荐方法）
                db.delete("MeterFile", null, null);
                db.delete("MeterBook", null, null);
               // db.delete("MeterNumerical", null, null);
                db.delete("MeterPhoto", null, null);
                //设置id从1开始（sqlite默认id从1开始），若没有这一句，id将会延续删除之前的id
                db.execSQL("update sqlite_sequence set seq=0 where name='MeterUser'");
                db.execSQL("update sqlite_sequence set seq=0 where name='MeterFile'");
                db.execSQL("update sqlite_sequence set seq=0 where name='MeterBook'");
               // db.execSQL("update sqlite_sequence set seq=0 where name='MeterNumerical'");
                db.execSQL("update sqlite_sequence set seq=0 where name='MeterPhoto'");
                Toast.makeText(MeterReaderDownloadActivity.this, "清除数据成功！", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    /**
     * 获取抄表用户数据
     */
    private void getMeterUserData() {
        // begianNum.getText().toString().trim()
        //endNum.getText().toString()
        String begianMum = "";
        String endNum = "";

        if (!"".equals(begianMum) && !"".equals(begianMum)) { //首先判断两个编号都不为空
            if (bookIDMap.size() != 0 && areaIDMap.size() != 0) { //两个编号不为空，并且抄表本、抄表分区都已经选择
                handler.sendEmptyMessage(9);
                //此时会下载三个部分的数据
                getBookParams();  //获取抄表本参数
                // getAreaParams();  //获取抄表分区参数
                String params = "u.c_user_id between" + begianMum +
                        " and " + endNum + " and b.n_book_id in(" + bookParams +
                        ") and a.n_area_id in(" + areaParams + ")";
                try {
                    params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                    requireMeterUserData("findUserMeter.do", params);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("--", "qqqqqqq");
            } else {  //要么抄表本、抄表分区其中一个已经选择，要么两个都没选择
                if (bookIDMap.size() == 0 && areaIDMap.size() == 0) {//两个编号不为空，但是抄表本、抄表分区都未选择
                    handler.sendEmptyMessage(9);
                    //此时只会下载 编号范围 部分的数据
                    String params = "u.c_user_id between" + begianMum +
                            " and " + endNum;
                    try {
                        params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                        requireMeterUserData("findUserMeter.do", params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.e("--", "|||||||||");
                } else if (bookIDMap.size() == 0) { //两个编号不为空，抄表分区已经选择，但是抄表本未选择
                    handler.sendEmptyMessage(9);
                    //此时会下载 编号范围、抄表分区 两个部分的数据
                    //getAreaParams();  //获取抄表分区参数
                    String params = "u.c_user_id between" + begianMum +
                            " and " + endNum +
                            " and a.n_area_id in(" + areaParams + ")";
                    try {
                        params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                        requireMeterUserData("findUserMeter.do", params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.e("--", ">>>>>>>>>");
                } else if (areaIDMap.size() == 0) { //两个编号不为空，抄表本已经选择，但是抄表分区未选择
                    handler.sendEmptyMessage(9);
                    //此时会下载 编号范围、抄表本 两个部分的数据
                    getBookParams();  //获取抄表本参数
                    String params = "u.c_user_id between" + begianMum +
                            " and " + endNum + " and b.n_book_id in(" + bookParams + ")";
                    try {
                        params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                        requireMeterUserData("findUserMeter.do", params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.e("--", "kkkkkkk");
                }
            }
        } else {  //要么两个编号都为空，要么其中一个为空
            if ("".equals(begianMum) && "".equals(endNum)) { //两个编号都为空
                if (bookIDMap.size() != 0 && areaIDMap.size() != 0) {  //抄表本、抄表分区都已经选择
                    handler.sendEmptyMessage(9);
                    //此时会下载 抄表本、抄表分区 两个部分的数据
                    getBookParams();  //获取抄表本参数
                    // getAreaParams();  //获取抄表分区参数
                    Log.i("getMeterUserData", "抄表本参数为：" + bookParams);
                    String params = "b.n_book_id in(" + bookParams + ") and a.n_area_id in(" + areaParams + ")";
                    try {
                        params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                        requireMeterUserData("findUserMeter.do", params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.e("--", "ffgffff");
                } else {
                    if (bookIDMap.size() == 0 && areaIDMap.size() == 0) { //两个编号都为空，并且抄表本、抄表分区都未选择
                        //此时提示选择其中之一
                        handler.sendEmptyMessage(6);
                    } else if (bookIDMap.size() == 0) { //两个编号都为空，抄表分区已经选择，但是抄表本未选择
                        handler.sendEmptyMessage(9);
                        //此时只会下载 抄表分区 部分的数据
                        //getAreaParams();  //获取抄表分区参数
                        String params = "a.n_area_id in(" + areaParams + ")";
                        try {
                            params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                            requireMeterUserData("findUserMeter.do", params);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.e("--", "nnnnnnn");
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
                    Log.e("--", "bbbbb");
                }
            } else { //其中一个编号为空
                handler.sendEmptyMessage(8);
            }
        }
    }

    //
    private void getParamsAndLoading() {
        showLoadingPopup();
        tips.setText("正在获取用户数据，请稍后...");
    }

    //加载进度动画
    public void showLoadingPopup() {
        LayoutInflater layoutInflater = LayoutInflater.from(MeterReaderDownloadActivity.this);
        View view = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        loadingPopup = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) view.findViewById(R.id.frame_animation);
        title = (TextView) view.findViewById(R.id.title);
        tips = (TextView) view.findViewById(R.id.tips);
        line = view.findViewById(R.id.line);
        ensure = (TextView) view.findViewById(R.id.ensure);
        ensure.setOnClickListener(new View.OnClickListener() {
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

    //
    //show文件命名窗口
    public void showNameFilePopup() {
        layoutInflater = LayoutInflater.from(MeterReaderDownloadActivity.this);
        fileNameView = layoutInflater.inflate(R.layout.popupwindow_meter_name_the_file, null);
        fileNamePopup = new PopupWindow(fileNameView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        frameAnimation = (ImageView) fileNameView.findViewById(R.id.frame_animation);
        fileNameEdit = (EditText) fileNameView.findViewById(R.id.file_name_edit);
        confirm = (TextView) fileNameView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
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
                                            insertMeterUserData(i+1);   //将抄表用户数据插入本地数据库
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
                                    if (userJsonArray.length() < 500) {
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
                    Toast.makeText(MeterReaderDownloadActivity.this, "文件名不能为空！", Toast.LENGTH_SHORT).show();
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

    //
    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MeterReaderDownloadActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterReaderDownloadActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterReaderDownloadActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterReaderDownloadActivity.this.getWindow().setAttributes(lp);
    }

    //
    //请求抄表用户数据
    private void requireMeterUserData(final String method, final String keyAndValue) {
        try {
            URL url;
            HttpURLConnection httpURLConnection;
            Log.i("sharedPreferences====>", public_sharedPreferences.getString("IP", ""));

            //  String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
            String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterReaderDownloadActivity.this)).append(method).toString();

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
            if (httpURLConnection.getResponseCode() == 200) {
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
                    Toast.makeText(MeterReaderDownloadActivity.this, "没有抄表用户数据哦！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    loadingPopup.dismiss();
                    Toast.makeText(MeterReaderDownloadActivity.this, "网络请求异常！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    loadingPopup.dismiss();
                    Toast.makeText(MeterReaderDownloadActivity.this, "网络请求超时！", Toast.LENGTH_SHORT).show();
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
                    tips.setText("下载进度为：" + msg.arg1 * 100 / userJsonArray.length() + "%");

                    Log.e("计算=", (msg.arg1 + 1) * 100 / (userJsonArray.length()) + "");
                    break;
                case 5:
                    loadingPopup.dismiss();
                    showNameFilePopup();  //用户给下载文件命名
                    break;
                case 6:
                    Toast.makeText(MeterReaderDownloadActivity.this, "至少选择其中一个抄表本才能下载哦", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    if (loadingPopup != null) {
                        loadingPopup.dismiss();
                    }

                    showNameFilePopup();
                    Toast.makeText(MeterReaderDownloadActivity.this, "该文件名已存在，请您重新命名！", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(MeterReaderDownloadActivity.this, "请您将区间填写完整！", Toast.LENGTH_SHORT).show();
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
        values.put("meter_order_number", userObject.optInt("n_order_num1", 0) + "");           //抄表序号
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
        values.put("remission", userObject.optDouble("n_remission", 0) + "");                     //加减量
        values.put("locationAddress", "未定位");                                                //定位地址
        values.put("file_name", fileNameEdit.getText().toString());                            //本地文件名
        values.put("uploadState", "false");                                                     //上传状态
        if (userObject.optString("n_stateId").equals("null") || userObject.optString("n_stateId").equals("")) {
            values.put("n_state_id", "0");                                                     //估录
        } else {
            values.put("n_state_id", userObject.optString("n_stateId"));                                                     //上传状态
        }
        if (TextUtils.isEmpty(userObject.optString("c_opened_remark")) || userObject.optString("c_opened_remark").equals("null")) {
            values.put("opened_remark", "无");
            Log.e("无", userObject.optString("c_opened_remark"));
        } else {
            values.put("opened_remark", userObject.optString("c_opened_remark", ""));//启用说明
            Log.e("有", userObject.optString("c_opened_remark"));
        }
        if (TextUtils.isEmpty(userObject.optString("c_user_remark")) || userObject.optString("c_user_remark").equals("null")) {
            values.put("user_remark", "无");
        } else {
            values.put("user_remark", userObject.optString("c_user_remark", ""));//抄表备注
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


    //
//    //保存选中的抄表本ID信息
    public void saveBookInfo() {
        bookIDMap.clear();
        bookIDList.clear();
        for (int i = 0; i < bookAdapter.getList().size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookInfo bookInfo = bookAdapter.getList().get(i);
                bookIDMap.put("bookID" + i, Integer.parseInt(bookInfo.getID()));
                Log.i("bookID=========>", "这次被勾选第" + i + "个，抄表本ID为：" + bookInfo.getID());
                bookIDList.add(i);
            }
        }
        //下载
        new Thread() {
            @Override
            public void run() {
                bookParams = "";
                getMeterUserData();   //获取抄表用户数据
            }
        }.start();
    }

    /**
     * 获取抄表本参数
     */
    private void getBookParams() {

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
     * 多选
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
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
        bookNumber.setText("(" + bookNum + ")");
    }

    //抄表本全选
    public void SelectAll() {
        for (int i = 0; i < bookAdapter.getList().size(); i++) {
            BookDataAdapter.getIsCheck().put(i, true);
        }
        bookNum = bookAdapter.getList().size();
        bookDataChanged();
    }


    //抄表本反选
    private void bookReverse() {
        for (int i = 0; i < bookAdapter.getList().size(); i++) {
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
    private void bookSelectCancle() {
        for (int i = 0; i < bookAdapter.getList().size(); i++) {
            if (BookDataAdapter.getIsCheck().get(i)) {
                BookDataAdapter.getIsCheck().put(i, false);
                bookNum--; //数量减一
            }
        }
        bookDataChanged();
    }

    // 刷新抄表本listview和TextView的显示
    private void bookDataChanged() {
        // 通知listView刷新
        bookAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        bookNumber.setText("(" + bookNum + ")");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        db.close();
        if (loadingPopup != null) {
            if (loadingPopup.isShowing()) {
                loadingPopup.dismiss();
                loadingPopup = null;
            }
        }
    }


}
