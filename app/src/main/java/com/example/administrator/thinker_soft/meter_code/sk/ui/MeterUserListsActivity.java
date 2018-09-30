package com.example.administrator.thinker_soft.meter_code.sk.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserDetailActivity;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.MeterUserListAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolManager;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;
import com.example.administrator.thinker_soft.meter_code.sk.widget.UIHandler;
import com.example.administrator.thinker_soft.meter_payment.Utils.MyDialog;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.mode.Tools;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil.backgroundAlpha;

/**
 * 抄表用户列表
 * Created by Administrator on 2018/4/26.
 */

public class MeterUserListsActivity extends AppCompatActivity implements OnRefreshListener, OnLoadmoreListener, RecyclerArrayAdapter.OnItemClickListener, RecyclerArrayAdapter.OnItemLongClickListener {
    private Unbinder mUnbinder;
    /**
     * 总数据
     */
    private static final int MESSAGE_DATA_All = 1;
    /**
     * 添加
     */
    private static final int MESSAGE_DATA_ADD = 2;
    /**
     * 列表滚动位置
     */
    private static final int MESSAGE_DATA_INDEX = 3;
    /**
     * 没有数据
     */
    private static final int MESSAGE_DATA_NO = 4;
    /**
     * 搜索本地
     */
    private static final int MESSAGE_DATA_SEARCH = 5;
    private static final int MESSAGE_DSTA_DISS = 6;
    /**
     * 刷新
     */
    private static final int MESSAGE_DATA_REFRESH = 7;
    /**
     * intent值
     */
    private String bookID, book_name, fileName;
    /**
     * 适配器
     */
    private MeterUserListAdapter meterUserListAdapter;
    private LinearLayoutManager mLayoutManager;
    /**
     * 搜索是否成功
     */
    private boolean aBoolean = true;
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**数据库*/
    //  private SQLiteDatabase add_db;
    /**
     * 存储
     */
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    /**
     * 日期
     */
    private SimpleDateFormat dateFormat;
    /**
     * 更新Ui
     */
    private UIMyHandler myHandler = new UIMyHandler(this);
    /**
     * 选择抄表
     */
    private PopupWindow popupWindow;
    /**
     * 快捷抄表
     */
    private PopupWindow fastMeterWindow;
    /**
     * 设置选选择抄表和未抄表
     */
    private boolean queryAllMeter = true;
    /**
     * 总大小
     */
    private int allCount;
    /**
     * 每页条数
     */
    private int dataCount = 0;
    private static int currentPosition = 0;
    /**
     * 定位相关
     */
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    private String longitude, latitude, locationAddress;
    //    //搜索弹窗提示
//   ProgressDialog progressDialog = null;
    @BindView(R.id.book_name)
    TextView bookName;//文件名称
    @BindView(R.id.et_spot)
    EditText edSpot;//搜索
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;//刷新控件
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;//用户列表
    @BindView(R.id.no_data)
    TextView noData;//空白页
    @BindView(R.id.root_linearlayout)
    LinearLayout layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_user_listview);
        mUnbinder = ButterKnife.bind(this);//绑定
        initView();
        initDb();
    }


    /**
     * 初始化视图
     */
    private void initView() {
        bookID = getIntentStr("bookID");
        book_name = getIntentStr("bookName");
        fileName = getIntentStr("fileName");
        bookName.setText("当前：" + book_name);
        mLayoutManager = new LinearLayoutManager(this);
        meterUserListAdapter = new MeterUserListAdapter(this);
//        mLayoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
//        mLayoutManager.setReverseLayout(true);//列表翻转
        // 设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        recyclerView.setAdapter(meterUserListAdapter);
        meterUserListAdapter.setOnItemClickListener(this);//点击监听
        meterUserListAdapter.setOnItemLongClickListener(this);//长按监听
        mRefreshLayout.setRefreshHeader(new WaterDropHeader(this));
        mRefreshLayout.setPrimaryColorsId(R.color.theme_colors, android.R.color.white);
        //设置 Footer 为 球脉冲
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        mRefreshLayout.setDisableContentWhenRefresh(true);  //是否在刷新的时候禁止内容的一切手势操作（默认false）
        mRefreshLayout.setDisableContentWhenLoading(true);  //是否在加载的时候禁止内容的一切手势操作（默认false）
        mRefreshLayout.setOnRefreshListener(this);//下拉刷新
        mRefreshLayout.setOnLoadmoreListener(this);//上拉加载
        recyclerView.requestFocus();
    }

    /**
     * 初始化数据
     */
    private void initDb() {
        MySqliteHelper helper = new MySqliteHelper(MeterUserListsActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
//        DatabaseContext dbContext=new DatabaseContext(MeterUserListsActivity.this);
//        MySQLiteOpenHelpers helpers = new MySQLiteOpenHelpers(dbContext,1);
//        add_db=helpers.getWritableDatabase();

        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        //设置默认条数
        if (sharedPreferences.getString("page_count", "").equals("")) {
            sharedPreferences.edit().putString("page_count", "50").apply();
        }
        if (Integer.valueOf(sharedPreferences.getString("page_count", "")) <= 0) {
            sharedPreferences.edit().putString("page_count", "50").apply();
        }
        //  progressDialog = ProgressDialog.show(MeterUserListsActivity.this, "提示", "加载中...", true);
        //获取默认数据
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                //  getTotalUserCount();
                if (bookID != null && fileName != null) {
                    getTotalUserCount();//用户列表总数
                    //绵竹
                    if (SharedPreferencesHelper.getFirm(MeterUserListsActivity.this).equals("绵竹")) {

                    } else {
                        getMeterUserData(fileName, bookID, dataCount, MeterUserListsActivity.MESSAGE_DATA_REFRESH);
                    }
                    //             getTotalUserAll();
                }
            }
        });

    }


    /**
     * 获取intent值
     *
     * @param key
     * @return
     */
    private String getIntentStr(String key) {
        // 直接通过Context类的getIntent()即可获取Intent
        Intent intent = this.getIntent();
        // 判断
        if (intent != null) {
            return intent.getStringExtra(key);
        } else {
            return null;
        }
    }

    /**
     * 监听
     *
     * @param view
     */
    @OnClick({R.id.back, R.id.more, R.id.btn_go_spot})
    public void setOnclick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.more:
                //其他
                showMoreWindow(view);
                break;
            case R.id.btn_go_spot:
                //搜索
                if (edSpot.getText().toString().trim().equals("")) {
                    Toast.makeText(MeterUserListsActivity.this, "请输入用户编号进行查询跳转！", Toast.LENGTH_SHORT).show();
                } else {
                    edSpot.setText(edSpot.getText().toString().trim().toUpperCase());
                    if (!meterUserListAdapter.getAllData().isEmpty()) {
                        if (aBoolean) {
                            //查询本地数据库
                            ThreadPoolManager.getInstance().execute(new Runnable() {
                                @Override
                                public void run() {
                                    //当前列表未找到
                                    if (!serchUser(meterUserListAdapter.getAllData(), edSpot.getText().toString().trim())) {
                                        searchUserList(edSpot.getText().toString().trim());
                                    }
                                }
                            });
                        } else {
                            if (!serchUser(meterUserListAdapter.getAllData(), edSpot.getText().toString().trim())) {
                                Log.e("dddd", "搜索失败");
                                //搜索失败
                                myHandler.sendEmptyMessage(MESSAGE_DATA_SEARCH);
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }

    }


    /**
     * 加载条数
     *
     * @return
     */
    private int setCount() {
        int pageCount;
        if (sharedPreferences.getString("page_count", "").equals("")) {
            sharedPreferences.edit().putString("page_count", "50").apply();
        }
        if (Integer.valueOf(sharedPreferences.getString("page_count", "")) <= 0) {
            //Toast.makeText(MeterUserListsActivity.this, "每页加载数据必须大于0,现在更正为50条!", Toast.LENGTH_SHORT).show();
            pageCount = 50;
            sharedPreferences.edit().putString("page_count", "50").apply();
        } else {
            pageCount = Integer.valueOf(sharedPreferences.getString("page_count", ""));
        }
        return pageCount;
    }

    /**
     * 查询抄表用户总数
     */
    public void getTotalUserCount() {

        ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();
        Cursor userLimitCursor;
        if (sharedPreferences.getBoolean("show_temp_data", false)) {
            //未登录
            userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=?", new String[]{"0", fileName, bookID});//查询并获得游标
        } else {
            userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游标
        }
        //如果游标为空，则显示没有数据图片
        allCount = userLimitCursor.getCount();
        Log.i("MeterUserLVActivity", "总的查询到" + userLimitCursor.getCount() + "条数据！");
        if (userLimitCursor.getCount() == 0) {
            myHandler.sendEmptyMessage(MESSAGE_DATA_NO);
            userLimitCursor.close();
            return;
        }
        // totalPage = totalCount % pageCount > 0 ? totalCount / pageCount + 1 : totalCount / pageCount;
        //  Log.i("MeterUserLVActivity", "总的页数" + totalPage + "页！");
//        while (userLimitCursor.moveToNext()) {
////            userLists.add(setMeteruser(userLimitCursor));
//            //插入数据
//            insertMeterUserData(userLimitCursor);
//        }
        userLimitCursor.close();

//        Message msg = myHandler.obtainMessage(MeterUserListsActivity.MESSAGE_DATA_All);
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("allData", userLists);
//        msg.setData(bundle);
//        myHandler.sendMessage(msg);

    }
//    /**
//     * 查询抄表用户总数
//     */
//    public void getTotalUserAll() {
//        int i=1;
//        add_db.delete("MeterUsers", null, null);  //删除User表中当前用户的所有数据（官方推荐方法）
//        //设置id从1开始（sqlite默认id从1开始），若没有这一句，id将会延续删除之前的id
//        add_db.execSQL("update sqlite_sequence set seq=0 where name='MeterUsers'");
//
//        Cursor userLimitCursor;
//        if (sharedPreferences.getBoolean("show_temp_data", false)) {
//            //未登录
//            userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=?", new String[]{"0"});//查询并获得游标
//        } else {
//            userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=?", new String[]{sharedPreferences_login.getString("userId", "")});//查询并获得游标
//        }
//        //如果游标为空，则显示没有数据图片
//        allCount = userLimitCursor.getCount();
//        Log.i("MeterUserLVActivity", "总的查询到" + userLimitCursor.getCount() + "条数据！");
//        if (userLimitCursor.getCount() == 0) {
//            myHandler.sendEmptyMessage(MESSAGE_DATA_NO);
//            userLimitCursor.close();
//            return;
//        }
//        while (userLimitCursor.moveToNext()) {
//            //插入数据
//            insertMeterUserData(userLimitCursor,i);
//            i++;
//        }
//        userLimitCursor.close();
//        //搜索失败
//        myHandler.sendEmptyMessage(MESSAGE_DSTA_DISS);
//
//    }

    /**
     * 搜索数据库中的用户
     */
    private void searchUserList(String edText) {

        aBoolean = false;
        ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();
        Cursor userLimitCursor;
        if (sharedPreferences.getBoolean("show_temp_data", false)) {
            //未登录
            userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=?", new String[]{"0", fileName, bookID});//查询并获得游标
        } else {
            userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游标
        }
        if (userLimitCursor.getCount() == 0) {
            myHandler.sendEmptyMessage(MESSAGE_DATA_SEARCH);
            userLimitCursor.close();
            return;
        }
        while (userLimitCursor.moveToNext()) {
            userLists.add(setMeteruser(userLimitCursor));

        }
        userLimitCursor.close();
        //按顺序排列
        Collections.sort(userLists);
        Message msg = myHandler.obtainMessage(MeterUserListsActivity.MESSAGE_DATA_All);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("allData", userLists);
        msg.setData(bundle);
        myHandler.sendMessage(msg);
        //搜索
        if (!serchUser(userLists, edText)) {
            //搜索失败
            myHandler.sendEmptyMessage(MESSAGE_DATA_SEARCH);
        }
    }


    /**
     * 读取本地的抄表分区用户数据
     *
     * @param fileName
     * @param bookID
     * @param dataStartCount
     */

    public void getMeterUserData(String fileName, String bookID, int dataStartCount, int tag) {
        ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();//用户数据
        Cursor userLimitCursor;
        if (!"".equals(sharedPreferences.getString("page_count", ""))) {
            if (sharedPreferences.getBoolean("show_temp_data", false)) {
                //显示演示数据
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{"0", fileName, bookID});//查询并获得游标
            } else {
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? order by meter_order_number limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游标
            }
        } else {
            if (sharedPreferences.getBoolean("show_temp_data", false)) {
                //显示演示数据
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + 50, new String[]{"0", fileName, bookID});//查询并获得游
            } else {
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? order by meter_order_number limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游标
            }
        }
        Log.i("MeterUserLVActivity", "分页查询到" + userLimitCursor.getCount() + "条数据！");

        while (userLimitCursor.moveToNext()) {
            userLists.add(setMeteruser(userLimitCursor));
        }
        userLimitCursor.close();
        Log.i("MeterUserLVActivity", "getMeterUserData: userLists.size():" + meterUserListAdapter.getAllData().size());
        Collections.sort(userLists);
        Message msg = myHandler.obtainMessage(tag);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("addData", userLists);
        msg.setData(bundle);
        myHandler.sendMessage(msg);

    }

    /**
     * 读取本地的抄表分区未抄表用户数据
     *
     * @param fileName
     * @param bookID
     * @param dataStartCount
     */
    public void getNotCopyUserData(String fileName, String bookID, int dataStartCount, int tag) {
        ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();//用户数据
        Cursor userLimitCursor;
        if (!"".equals(sharedPreferences.getString("page_count", ""))) {
            if (sharedPreferences.getBoolean("show_temp_data", false)) {   //显示演示数据
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{"0", fileName, bookID});//查询并获得游标
            } else {
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? and meterState=? order by meter_order_number limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID, "false"});//查询并获得游标
            }
        } else {
            if (sharedPreferences.getBoolean("show_temp_data", false)) {  //显示演示数据
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + 50, new String[]{"0", fileName, bookID});//查询并获得游
            } else {
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? order by meter_order_number limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游
            }
        }
        Log.i("MeterUserLVActivity", "分页查询到" + userLimitCursor.getCount() + "条数据！");

        while (userLimitCursor.moveToNext()) {
            userLists.add(setMeteruser(userLimitCursor));
        }
        Log.i("MeterUserLVActivity", "getMeterUserData: userLists.size():" + userLists.size());
        Collections.sort(userLists);
        Message msg = myHandler.obtainMessage(tag);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("addData", userLists);
        msg.setData(bundle);
        myHandler.sendMessage(msg);

    }


    /**
     * 绵竹 . 读取本地的抄表分区用户数据
     *
     * @param fileName
     * @param bookID
     * @param dataStartCount
     */

    public void getMeterUserDataMZ(String fileName, String bookID, int dataStartCount, int tag) {
        ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();//用户数据
        Cursor userLimitCursor;
        if (!"".equals(sharedPreferences.getString("page_count", ""))) {
            if (sharedPreferences.getBoolean("show_temp_data", false)) {
                //显示演示数据
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{"0", fileName, bookID});//查询并获得游标
            } else {
                userLimitCursor = db.rawQuery("select * from(select * from (select * from MeterUser u where meter_order_number<>0 order by meter_order_number)\n" +
                        "union all\n" +
                        "select * from MeterUser u where meter_order_number=0) where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});
                Log.e("进入", "ddd");
            }
        } else {
            if (sharedPreferences.getBoolean("show_temp_data", false)) {
                //显示演示数据
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + 50, new String[]{"0", fileName, bookID});//查询并获得游
            } else {
                userLimitCursor = db.rawQuery("select * from(select * from (select * from MeterUser u where meter_order_number<>0 order by meter_order_number)\n" +
                        "union all\n" +
                        "select * from MeterUser u where meter_order_number=0) where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});
            }
        }
        Log.i("MeterUserLVActivity", "分页查询到" + userLimitCursor.getCount() + "条数据！");

        while (userLimitCursor.moveToNext()) {
            userLists.add(setMeteruser(userLimitCursor));
        }
        userLimitCursor.close();
        Log.i("MeterUserLVActivity", "getMeterUserData: userLists.size():" + meterUserListAdapter.getAllData().size());
        Collections.sort(userLists);
        Message msg = myHandler.obtainMessage(tag);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("addData", userLists);
        msg.setData(bundle);
        myHandler.sendMessage(msg);

    }

    /**
     * 绵竹 . 读取本地的抄表分区未抄表用户数据
     *
     * @param fileName
     * @param bookID
     * @param dataStartCount
     */
    public void getNotCopyUserDataMZ(String fileName, String bookID, int dataStartCount, int tag) {
        ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();//用户数据
        Cursor userLimitCursor;
        if (!"".equals(sharedPreferences.getString("page_count", ""))) {
            if (sharedPreferences.getBoolean("show_temp_data", false)) {   //显示演示数据
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{"0", fileName, bookID});//查询并获得游标
            } else {
                userLimitCursor = db.rawQuery("select * from(select * from (select * from MeterUser u where meter_order_number<>0 order by meter_order_number)\n" +
                        "union all\n" +
                        "select * from MeterUser u where meter_order_number=0) where login_user_id=? and file_name=? and book_id=? and meterState=?  limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID, "false"});//查询并获得游标
            }
        } else {
            if (sharedPreferences.getBoolean("show_temp_data", false)) {  //显示演示数据
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + 50, new String[]{"0", fileName, bookID});//查询并获得游
            } else {
                userLimitCursor = db.rawQuery("select * from(select * from (select * from MeterUser u where meter_order_number<>0 order by meter_order_number)\n" +
                        "union all\n" +
                        "select * from MeterUser u where meter_order_number=0) where login_user_id=? and file_name=? and book_id=? and meterState=?  limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID, "false"});//查询并获得游标
            }
        }
        Log.i("MeterUserLVActivity", "分页查询到" + userLimitCursor.getCount() + "条数据！");

        while (userLimitCursor.moveToNext()) {
            userLists.add(setMeteruser(userLimitCursor));
        }
        Log.i("MeterUserLVActivity", "getMeterUserData: userLists.size():" + userLists.size());
        Collections.sort(userLists);
        Message msg = myHandler.obtainMessage(tag);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("addData", userLists);
        msg.setData(bundle);
        myHandler.sendMessage(msg);

    }


    /**
     * 设置数据
     *
     * @param userLimitCursor
     * @return
     */
    private MeterUserListviewItem setMeteruser(Cursor userLimitCursor) {
        MeterUserListviewItem item = new MeterUserListviewItem();
        item.setMeterID(userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_order_number")));
        if (!userLimitCursor.getString(userLimitCursor.getColumnIndex("user_name")).equals("null")) {
            item.setUserName(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_name")));
        } else {
            item.setUserName("无");
        }
        item.setUserID(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_id")));
        if (!userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_number")).equals("null")) {
            item.setMeterNumber(userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_number")));
        } else {
            item.setMeterNumber("无");
        }
        if (!userLimitCursor.getString(userLimitCursor.getColumnIndex("old_user_id")).equals("null")) {
            item.setOldUserID(userLimitCursor.getString(userLimitCursor.getColumnIndex("old_user_id")));
        } else {
            item.setOldUserID("无");
        }
        item.setChangeDosage(userLimitCursor.getString(userLimitCursor.getColumnIndex("dosage_change")));
        item.setRemission(userLimitCursor.getString(userLimitCursor.getColumnIndex("remission")));
        item.setLastMonthDegree(userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_degrees")));
        item.setLastMonthDosage(userLimitCursor.getString(userLimitCursor.getColumnIndex("last_month_dosage")));
        item.setAddress(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_address")));
        if (userLimitCursor.getString(userLimitCursor.getColumnIndex("uploadState")).equals("false")) {
            item.setUploadState("");
        } else if (userLimitCursor.getString(userLimitCursor.getColumnIndex("uploadState")).equals("true")) {
            item.setUploadState("已上传");
        } else {
            item.setUploadState("已录入");
        }

        if (userLimitCursor.getString(userLimitCursor.getColumnIndex("meterState")).equals("false")) {
            item.setMeterState("未抄");
            item.setIfEdit(R.mipmap.meter_false);
            item.setThisMonthDegree("无");
            item.setThisMonthDosage("无");
            item.setRedColor(Color.parseColor("#7d7d7d"));
        } else {
            item.setMeterState("已抄");
            item.setIfEdit(R.mipmap.meter_true_red);
            item.setRedColor(Color.parseColor("#FF0000"));
            item.setThisMonthDegree(userLimitCursor.getString(userLimitCursor.getColumnIndex("this_month_end_degree")));
            item.setThisMonthDosage(userLimitCursor.getString(userLimitCursor.getColumnIndex("this_month_dosage")));
        }
        //item.setRemark(userLimitCursor.getString(userLimitCursor.getColumnIndex("opened_remark")));
        item.setRemark(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_remark")));
        return item;
    }

//    private void insertMeterUserData(Cursor userLimitCursor,int i) {
//        ContentValues values = new ContentValues();
//        values.put("login_user_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("login_user_id")));          //登录人ID
//        values.put("meter_reader_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_reader_id")));    //抄表员ID
//        values.put("meter_reader_name",  userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_reader_name")));        //抄表员名称
//        values.put("meter_date", userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_date")));                                                      //抄表时间
//        values.put("user_phone", userLimitCursor.getString(userLimitCursor.getColumnIndex("user_phone")));                    //用户电话
//        values.put("user_amount", userLimitCursor.getString(userLimitCursor.getColumnIndex("user_amount")));                      //用户余额
//        values.put("meter_degrees", userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_degrees")));             //上月读数
//        values.put("meter_number", userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_number")));                //表编号
//        values.put("arrearage_months", userLimitCursor.getString(userLimitCursor.getColumnIndex("arrearage_months")));                      //欠费月数
//        values.put("mix_state", userLimitCursor.getString(userLimitCursor.getColumnIndex("mix_state")));                     //混合使用状态（0正常  1混合）
//        values.put("meter_order_number",userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_order_number")));           //抄表序号
//        values.put("arrearage_amount", userLimitCursor.getString(userLimitCursor.getColumnIndex("arrearage_amount")));          //欠费金额
//        values.put("area_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("area_id")));                         //抄表本分区ID
//        values.put("area_name",userLimitCursor.getString(userLimitCursor.getColumnIndex("area_name")));                      //抄表本分区名称
//        values.put("user_name", userLimitCursor.getString(userLimitCursor.getColumnIndex("user_name")));                      //用户名
//        values.put("last_month_dosage", userLimitCursor.getString(userLimitCursor.getColumnIndex("last_month_dosage")));                //上月用量
//        values.put("property_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("property_id")));               //性质ID
//        values.put("property_name", userLimitCursor.getString(userLimitCursor.getColumnIndex("property_name")));            //性质名称
//        values.put("user_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("user_id")));                          //用户ID
//        values.put("book_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("book_id")));                         //抄表本ID
//        values.put("float_range", userLimitCursor.getString(userLimitCursor.getColumnIndex("float_range")));                      //浮动范围
//        values.put("meterState", userLimitCursor.getString(userLimitCursor.getColumnIndex("meterState")));                                                     //抄表状态
//        values.put("dosage_change", userLimitCursor.getString(userLimitCursor.getColumnIndex("dosage_change")));              //更换量
//        values.put("user_address", userLimitCursor.getString(userLimitCursor.getColumnIndex("user_address")));                //用户地址
//        values.put("start_dosage", userLimitCursor.getString(userLimitCursor.getColumnIndex("start_dosage")));                    //启用量
//        values.put("old_user_id",userLimitCursor.getString(userLimitCursor.getColumnIndex("old_user_id")));                  //用户老编号
//        values.put("book_name", userLimitCursor.getString(userLimitCursor.getColumnIndex("book_name")));                      //抄表本名称
//        values.put("meter_model", userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_model")));                   //表型号
//        values.put("rubbish_cost", userLimitCursor.getString(userLimitCursor.getColumnIndex("rubbish_cost")));                  //垃圾费
//        values.put("remission", userLimitCursor.getString(userLimitCursor.getColumnIndex("remission")));                     //加减量
//        values.put("locationAddress", userLimitCursor.getString(userLimitCursor.getColumnIndex("locationAddress")));                                                //定位地址
//        values.put("file_name",userLimitCursor.getString(userLimitCursor.getColumnIndex("file_name")));                            //本地文件名
//        values.put("uploadState", userLimitCursor.getString(userLimitCursor.getColumnIndex("uploadState")));                                                     //上传状态
//        values.put("n_state_id", userLimitCursor.getString(userLimitCursor.getColumnIndex("n_state_id")));                                                     //估录
//        values.put("opened_remark", userLimitCursor.getString(userLimitCursor.getColumnIndex("opened_remark")));
//        values.put("user_remark", userLimitCursor.getString(userLimitCursor.getColumnIndex("user_remark")));
//        values.put("n_user_state",userLimitCursor.getString(userLimitCursor.getColumnIndex("n_user_state")));
//        //下面这些个字段抄表完成后需上传
//        values.put("this_month_dosage", userLimitCursor.getString(userLimitCursor.getColumnIndex("this_month_dosage")));                                               //本月用量
//        values.put("this_month_end_degree", userLimitCursor.getString(userLimitCursor.getColumnIndex("this_month_end_degree")));                                               //本月止度
//        values.put("n_jw_x", userLimitCursor.getString(userLimitCursor.getColumnIndex("n_jw_x")));                                                        //纬度
//        values.put("n_jw_y", userLimitCursor.getString(userLimitCursor.getColumnIndex("n_jw_y")));                                                        //经度
//        values.put("d_jw_time", userLimitCursor.getString(userLimitCursor.getColumnIndex("d_jw_time")));                                                     //抄表时间
//        values.put("n_state_remark",userLimitCursor.getString(userLimitCursor.getColumnIndex("n_state_remark")));                                                     //抄表时间
//
//        add_db.insert("MeterUsers", null, values);
//        Log.e("insertMeterUserData", "用户数据插入成功"+i);
//    }


    /**
     * 搜索用户
     */
    private boolean serchUser(List<MeterUserListviewItem> mData, String edText) {
        boolean isSearch = false;

        for (int i = 0; i < mData.size(); i++) {
            MeterUserListviewItem item = mData.get(i);
            if (item.getOldUserID().contains(edText) || item.getUserID().contains(edText) || item.getUserName().contains(edText)) {
                Message msg = myHandler.obtainMessage(MeterUserListsActivity.MESSAGE_DATA_INDEX);
                Bundle bundle = new Bundle();
                bundle.putInt("index", i);
                msg.setData(bundle);
                myHandler.sendMessage(msg);
                return true;
            }
        }

        return isSearch;
    }

    /**
     * 弹出框
     *
     * @param view
     */
    private void showMoreWindow(View view) {
        View contentView = getLayoutInflater().inflate(R.layout.popwindow_meter_user_state, null);
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView allUser = (TextView) contentView.findViewById(R.id.all_user);
        final TextView notCopyUser = (TextView) contentView.findViewById(R.id.not_copy_user);
        if (queryAllMeter) {
            notCopyUser.setTextColor(Color.BLACK);
            allUser.setTextColor(Color.BLUE);
        } else {
            notCopyUser.setTextColor(Color.BLUE);
            allUser.setTextColor(Color.BLACK);
        }
        allUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCount = 0;
                //所有用户
                meterUserListAdapter.clear();
                queryAllMeter = true;
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        getMeterUserData(fileName, bookID, dataCount, MeterUserListsActivity.MESSAGE_DATA_REFRESH);
                    }
                });
                popupWindow.dismiss();

            }
        });
        notCopyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCount = 0;
                //未抄抄表
                meterUserListAdapter.clear();
                queryAllMeter = false;
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        getNotCopyUserData(fileName, bookID, dataCount, MeterUserListsActivity.MESSAGE_DATA_REFRESH);
                    }
                });
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        // popupWindow.showAsDropDown(view, -180, 10);
        popupWindow.showAsDropDown(view, -PopWindowUtil.dip2px(MeterUserListsActivity.this, 73), 0);
        backgroundAlpha(MeterUserListsActivity.this, 0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(MeterUserListsActivity.this, 1.0F);
            }
        });
    }

    /**
     * 弹出快捷抄表窗口
     */
    public void showFastMeterWindow(final String lastMonthDegree, int position) {
        initLocation();
        final MeterUserListviewItem item = meterUserListAdapter.getAllData().get(position);
        View fastMeterview = getLayoutInflater().inflate(R.layout.popupwindow_fast_meter, null);
        fastMeterWindow = new PopupWindow(fastMeterview, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        RadioButton cancel = (RadioButton) fastMeterview.findViewById(R.id.cancel_rb);
        final EditText meterEdit = (EditText) fastMeterview.findViewById(R.id.meter_edit);
        RadioButton save = (RadioButton) fastMeterview.findViewById(R.id.save_rb);
        //设置点击事件
        meterEdit.setFocusable(true);
        meterEdit.setFocusableInTouchMode(true);
        meterEdit.requestFocus();
//        Tools.showInputMethod(this,meterEdit);
        Tools.showSoftInput(this, meterEdit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.hideSoftInput(MeterUserListsActivity.this, meterEdit);
                fastMeterWindow.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(meterEdit.getText().toString())) {
                    if (Integer.parseInt(meterEdit.getText().toString()) >= Integer.parseInt(lastMonthDegree)) {
                        final String thisMonthDegree = meterEdit.getText().toString();
                        //用量
                        int degree = Integer.parseInt(thisMonthDegree) - Integer.parseInt(item.getLastMonthDegree());
                        //换表用量
                        double replaceDegree = aDouble((double) Integer.parseInt(item.getChangeDosage()), Double.parseDouble(item.getRemission()));
                        Double monthDosage = aDouble((double) degree, replaceDegree);
                        if (monthDosage < 0) {
                            monthDosage = 0.0;
                        }
                        Log.e("2用量", monthDosage + "");

                        int lastDosage = Integer.valueOf(item.getLastMonthDosage());
                        //超出用量
                        double lasts = sub(monthDosage, Double.parseDouble(item.getLastMonthDosage()));

                        if (monthDosage > lastDosage + lastDosage / 2) {
                            final MyDialog myDialog = new MyDialog();
                            //
                            final Double finalMonthDosage = monthDosage;
                            //"用量异常提示", "用量较上月超出过多,请确认数据.若无误请点击确认！"
                            myDialog.show("用量异常提示", "本次录入水量为" + thisMonthDegree + ",用量较上月超出" + (int) lasts + "吨,请确认数据，若无误请点击确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Tools.hideSoftInput(MeterUserListsActivity.this, meterEdit);
                                    fastMeterWindow.dismiss();
                                    item.setThisMonthDegree(thisMonthDegree);
                                    item.setThisMonthDosage(String.valueOf(finalMonthDosage));
                                    item.setIfEdit(R.mipmap.meter_true_red);
                                    item.setRedColor(Color.parseColor("#FF0000"));
                                    item.setMeterState("已抄");
                                    meterUserListAdapter.notifyDataSetChanged();
                                    updateMeterUserInfo(thisMonthDegree, String.valueOf(finalMonthDosage), item.getUserID());
                                }

                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myDialog.dismiss();
                                }
                            }, getSupportFragmentManager());
                        } else {
                            Tools.hideSoftInput(MeterUserListsActivity.this, meterEdit);
                            fastMeterWindow.dismiss();
                            item.setThisMonthDegree(thisMonthDegree);
                            item.setThisMonthDosage(String.valueOf(monthDosage));
                            item.setIfEdit(R.mipmap.meter_true_red);
                            item.setRedColor(Color.parseColor("#FF0000"));
                            item.setMeterState("已抄");
                            meterUserListAdapter.notifyDataSetChanged();
//                        Tools.moveToPosition((LinearLayoutManager) mLayoutManager, mRecyclerView, currentPosition + 1);
                            updateMeterUserInfo(thisMonthDegree, String.valueOf(monthDosage), item.getUserID());
                        }
                    } else {
                        Toast.makeText(MeterUserListsActivity.this, "本月读数不能小于上月读数哦！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MeterUserListsActivity.this, "请您输入本月读数！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fastMeterWindow.update();
        fastMeterWindow.setFocusable(true);
        fastMeterWindow.setTouchable(true);
        fastMeterWindow.setOutsideTouchable(true);
        fastMeterWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        fastMeterWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        PopWindowUtil.backgroundAlpha(MeterUserListsActivity.this, 0.6F);   //背景变暗
        fastMeterWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
        fastMeterWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(MeterUserListsActivity.this, 1.0F);
            }
        });
    }

    /**
     * 下拉刷新监听
     */
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        refreshlayout.finishRefresh(800);
        if (queryAllMeter) {
            //读取抄表本用户数据
            ThreadPoolManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    dataCount = 0;
                    getMeterUserData(fileName, bookID, dataCount, MeterUserListsActivity.MESSAGE_DATA_REFRESH);
                }
            });
        } else {
            //未抄表用户
            ThreadPoolManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    dataCount = 0;
                    getNotCopyUserData(fileName, bookID, dataCount, MeterUserListsActivity.MESSAGE_DATA_REFRESH);
                }
            });


        }
    }

    /**
     * 上拉加载监听
     */
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        refreshlayout.finishLoadmore(800);

        if (queryAllMeter) {
            if (meterUserListAdapter.getAllData().size() == allCount) {
                Toast.makeText(MeterUserListsActivity.this, "已经是最后一页哦！", Toast.LENGTH_SHORT).show();
            } else {
                //读取抄表本用户数据
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        dataCount += 50;
                        getMeterUserData(fileName, bookID, dataCount, MeterUserListsActivity.MESSAGE_DATA_ADD);
                    }
                });

            }
        } else {
            if (meterUserListAdapter.getAllData().size() == allCount) {
                Toast.makeText(MeterUserListsActivity.this, "已经是最后一页哦！", Toast.LENGTH_SHORT).show();
            } else {
                //未抄表用户
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        dataCount += 50;
                        getNotCopyUserData(fileName, bookID, dataCount, MeterUserListsActivity.MESSAGE_DATA_ADD);
                    }
                });


            }
        }
    }
    /**
     * 列表点击
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        MeterUserListviewItem item = meterUserListAdapter.getAllData().get(position);
        currentPosition = position;
        //赋值
        if (sharedPreferences.getBoolean("detail_meter", true)) {
            Intent intent = new Intent(MeterUserListsActivity.this, MeterUserDetailActivity.class);
            intent.putExtra("user_id", item.getUserID());
            intent.putExtra("upload_state", item.getUploadState());
            intent.putExtra("fileName", fileName);
            startActivityForResult(intent, position);
        } else {
            //是否已上传
            if (meterUserListAdapter.getAllData().get(position).getUploadState().equals("已上传")) {
                Toast.makeText(MeterUserListsActivity.this, "用户数据已上传，不能继续抄表！", Toast.LENGTH_SHORT).show();
            } else {
//                //用量
//                int  degree=Integer.parseInt(item.getThisMonthDegree()) - Integer.parseInt(item.getLastMonthDegree());
//                //换表用量
//                double replaceDegree=aDouble((double)Integer.parseInt(item.getChangeDosage()),Double.parseDouble(item.getRemission()));
//                Double   monthDosage =aDouble((double)degree,replaceDegree) ;
//                if (monthDosage < 0) {
//                    monthDosage = 0.0;
//                }
//                Log.e("2用量",monthDosage+"");
                //弹出快捷抄表框
                showFastMeterWindow(item.getLastMonthDegree(), position);
            }
        }
    }

    /**
     * 列表长按
     *
     * @param position
     * @return
     */
    @Override
    public boolean onItemLongClick(int position) {
        //跳转百度地图
        startNavi(meterUserListAdapter.getAllData().get(position).getAddress());
        return true;
    }

    /**
     * 滑动到指定位置
     *
     * @param mRecyclerView
     * @param position
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));

        if (position < firstItem) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);

        }
    }

    /**
     * 弱引用hander
     */
    private static class UIMyHandler extends UIHandler<MeterUserListsActivity> {

        public UIMyHandler(MeterUserListsActivity cls) {
            super(cls);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MeterUserListsActivity activity = ref.get();
            if (activity != null) {
                if (activity.isFinishing())
                    return;
                switch (msg.what) {

                    case MESSAGE_DATA_All: {
                        //绵竹
                        if (SharedPreferencesHelper.getFirm(activity).equals("绵竹")) {
                            activity.meterUserListAdapter.clear();
                            //总数据
                            ArrayList<MeterUserListviewItem> list = msg.getData().getParcelableArrayList("allData");
                            Collections.sort(list);
                            //排序
                            List<MeterUserListviewItem> listZero = new ArrayList<>();
                            List<MeterUserListviewItem> listAll = new ArrayList<>();
                            for (MeterUserListviewItem itemUser : list) {
                                if (itemUser.getMeterID().equals("0")) {
                                    listZero.add(itemUser);
                                } else {
                                    listAll.add(itemUser);
                                }
                            }
                            activity.meterUserListAdapter.addAll(listAll);
                            activity.meterUserListAdapter.addAll(listZero);
                            activity.meterUserListAdapter.notifyDataSetChanged();
                        } else {
                            //其他
                            activity.meterUserListAdapter.clear();
                            //总数据
                            ArrayList<MeterUserListviewItem> list = msg.getData().getParcelableArrayList("allData");
                            activity.meterUserListAdapter.addAll(list);
                            activity.meterUserListAdapter.notifyDataSetChanged();
                        }

                        break;
                    }
                    case MESSAGE_DATA_ADD: {
                        //添加
                        ArrayList<MeterUserListviewItem> list = msg.getData().getParcelableArrayList("addData");
                        activity.meterUserListAdapter.addAll(list);
                        List<MeterUserListviewItem> lists = activity.meterUserListAdapter.getAllData();
                        Collections.sort(lists);
                        activity.meterUserListAdapter.clear();
                        activity.meterUserListAdapter.addAll(lists);
                        //   System.out.println("list-》 默认排序后的状态");
                        activity.meterUserListAdapter.notifyDataSetChanged();

                        break;
                    }
                    case MESSAGE_DATA_INDEX: {
                        //滚动到顶部
                        if (msg.getData().getInt("index") != -1) {
                            activity.recyclerView.scrollToPosition(msg.getData().getInt("index"));
                            //滚动位置
                            //activity.smoothMoveToPosition(activity.recyclerView, msg.getData().getInt("index"));
                            LinearLayoutManager mLayoutManager = (LinearLayoutManager) activity.recyclerView.getLayoutManager();
                            mLayoutManager.scrollToPositionWithOffset(msg.getData().getInt("index"), 0);
                        }
                        break;
                    }
                    case MESSAGE_DATA_NO: {
                        //空白页
                        activity.noData.setVisibility(View.VISIBLE);
                        break;
                    }
                    case MESSAGE_DATA_SEARCH: {
                        //未搜索到用户
                        Toast.makeText(activity, "没有查找到相关用户！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case MESSAGE_DSTA_DISS: {//关闭
                        //  activity.dissDialog();
                        break;
                    }
                    case MESSAGE_DATA_REFRESH: {
                        //刷新
                        ArrayList<MeterUserListviewItem> list = msg.getData().getParcelableArrayList("addData");
                        Collections.sort(list);
                        activity.meterUserListAdapter.clear();
                        activity.meterUserListAdapter.addAll(list);
                        //System.out.println("list-》 默认排序后的状态");
                        activity.meterUserListAdapter.notifyDataSetChanged();
                    }
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 保存抄表信息到本地数据库
     */
    private void updateMeterUserInfo(String thisMonthDegree, String thisMonthDosage, String userID) {
        ContentValues values = new ContentValues();
        values.put("n_jw_x", latitude);
        values.put("n_jw_y", longitude);
        values.put("locationAddress", locationAddress);
        values.put("this_month_end_degree", thisMonthDegree);
        values.put("this_month_dosage", "" + thisMonthDosage);
        values.put("meter_date", dateFormat.format(new Date()));
        values.put("meterState", "true");
        if (sharedPreferences.getBoolean("show_temp_data", false)) {
            db.update("MeterUser", values, "login_user_id=? and user_id=?", new String[]{"0", userID});
        } else {
            db.update("MeterUser", values, "login_user_id=? and user_id=?", new String[]{sharedPreferences_login.getString("userId", ""), userID});
        }
    }

    //初始化定位
    private void initLocation() {
        // 定位初始化
        if (mLocClient == null) {
            mLocClient = new LocationClient(getApplicationContext()); //声明LocationClient类
        }
        mLocClient.registerLocationListener(myListener);     //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        option.setOpenGps(true); // 打开gps
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setNeedDeviceDirect(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map_meter_icon view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            int code = location.getLocType();
            Log.i("MyLocationListenner", "code是：" + code);
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());

            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            locationAddress = location.getAddrStr();
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            Log.i("currentAddress", locationAddress);
            Log.i("MyLocationListenner", sb.toString());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == currentPosition) {
                if (data != null) {
                    MeterUserListviewItem item = meterUserListAdapter.getAllData().get(currentPosition);
                    item.setThisMonthDegree(data.getStringExtra("this_month_end_degree"));
                    item.setThisMonthDosage(data.getStringExtra("this_month_dosage"));
                    item.setRemark(data.getStringExtra("this_user_remark"));
                    item.setIfEdit(R.mipmap.meter_true_red);
                    item.setMeterState("已抄");
                    item.setRedColor(Color.parseColor("#FF0000"));

                    if (data.getStringExtra("uploadState") != null) {
                        if (data.getStringExtra("uploadState").equals("1")) {
                            item.setUploadState("已上传");
                        }
                    }

                    meterUserListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi(String address) {
        try {
            Intent mapIntent = new Intent();
            mapIntent.setData(Uri.parse("baidumap://map/geocoder?src=openApiDemo&address=" + address));
            startActivity(mapIntent);
            //BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showTipDialog();
        }
    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showTipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(MeterUserListsActivity.this);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

//    /**
//     * 关闭弹出框
//     */
//    private void dissDialog(){
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//    }

    /**
     * double相加减
     *
     * @param a
     * @param b
     * @return
     */
    private static double aDouble(double a, double b) {
        double c = a + b;
        BigDecimal bd = new BigDecimal(c);
        double d = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }

    /**
     * double 相减
     *
     * @param d1
     * @param d2
     * @return
     */
    private static double sub(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        if (fastMeterWindow != null) {
            fastMeterWindow.dismiss();
        }

        // 退出时销毁定位
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient = null;
        }
        db.close();
//        if (add_db!=null){
//            add_db.close();
//        }
        //  dissDialog();
    }


}
