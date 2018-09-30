package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterUserListRecycleAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/12 0012.
 */
public class MeterUserUndoneActivity extends Activity {
    private ImageView back, pageTurning;
    private LinearLayout selectPage;
    private TextView noData, lastPage, nextPage, currentPageTv, totalPageTv, bookName;
    private ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();
    private SQLiteDatabase db;  //数据库
    private Cursor totalCountCursor, userLimitCursor;
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    private int dataStartCount = 0;   //用于分页查询，表示从第几行开始
    private int currentPage = 1;  //当前页数
    private int totalPage;    //总页数
    private MeterUserListviewItem item;
    private int currentPosition;  //点击当前抄表用户的item位置
    private String bookID, book_name, fileName, type;
    /**
     * 下拉刷新，上拉加载
     */
    private RefreshLayout mRefreshLayout;
    /**
     * RecyclerView相关
     */
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_user_listview);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        bookName = (TextView) findViewById(R.id.book_name);
        noData = (TextView) findViewById(R.id.no_data);
        selectPage = (LinearLayout) findViewById(R.id.select_page);
        lastPage = (TextView) findViewById(R.id.last_page);
        nextPage = (TextView) findViewById(R.id.next_page);
        currentPageTv = (TextView) findViewById(R.id.current_page_tv);
        totalPageTv = (TextView) findViewById(R.id.total_page_tv);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterUserUndoneActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = MeterUserUndoneActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        /**
         * 设置 下拉刷新 Header 和 footer 风格样式
         */
        mRefreshLayout.setRefreshHeader(new WaterDropHeader(this));
        mRefreshLayout.setPrimaryColorsId(R.color.theme_colors, android.R.color.white);
        //设置 Footer 为 球脉冲
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        mRefreshLayout.setDisableContentWhenRefresh(true);  //是否在刷新的时候禁止内容的一切手势操作（默认false）
        mRefreshLayout.setDisableContentWhenLoading(true);  //是否在加载的时候禁止内容的一切手势操作（默认false）
        /**
         * 获取上个页面传过来的参数
         */
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            fileName = intent.getStringExtra("fileName");
            if ("单个".equals(type)) {
                bookID = intent.getStringExtra("bookID");
                book_name = intent.getStringExtra("bookName");
                bookName.setText("当前：" + book_name);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        getTotalUserCount();
                        getMeterUserData(fileName, bookID, dataStartCount);   //查询单个抄表本未抄表用户
                        handler.sendEmptyMessage(0);
                    }
                }.start();
            } else {
                bookName.setText("当前：所有");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        getTotalUserCount();
                        getMeterUserData(fileName, bookID, dataStartCount);   //查询所有未抄表用户
                        handler.sendEmptyMessage(0);
                    }
                }.start();
            }
        }
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        pageTurning.setOnClickListener(clickListener);
        lastPage.setOnClickListener(clickListener);
        nextPage.setOnClickListener(clickListener);
        /**
         * 下拉刷新监听
         */
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(800);
                if (currentPageTv.getText().equals("1")) {
                    Toast.makeText(MeterUserUndoneActivity.this, "已经是第一页哦！", Toast.LENGTH_SHORT).show();
                } else {
                    dataStartCount -= 50;
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            getMeterUserData(fileName, bookID, dataStartCount);  //读取抄表本用户数据
                            handler.sendEmptyMessage(2);
                        }
                    }.start();
                }
            }
        });
        /**
         * 上拉加载监听
         */
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(800);
                if (currentPageTv.getText().equals(totalPageTv.getText())) {
                    Toast.makeText(MeterUserUndoneActivity.this, "已经是最后一页哦！", Toast.LENGTH_SHORT).show();
                } else {
                    dataStartCount += 50;
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            getMeterUserData(fileName, bookID, dataStartCount);  //读取抄表本用户数据
                            handler.sendEmptyMessage(1);
                        }
                    }.start();
                }
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    MeterUserUndoneActivity.this.finish();
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
                    /**
                     * 使用 RecyclerView 控件
                     */
                    mLayoutManager = new LinearLayoutManager(MeterUserUndoneActivity.this, LinearLayoutManager.VERTICAL, false);
                    mAdapter = new MeterUserListRecycleAdapter(userLists, new MeterUserListRecycleAdapter.onRecyclerViewItemClick() {
                        @Override
                        public void onItemClick(View v, int position) {
                            currentPosition = position;
                            Log.i("MeterUserListviewAc", "RecycleView的点击事件进来了！");
                            if (null != mRecyclerView.getChildViewHolder(v)) {
                                RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(v);
                                item = (MeterUserListviewItem) viewHolder.itemView.getTag();
                                Intent intent = new Intent(MeterUserUndoneActivity.this, MeterUserDetailActivity.class);
                                intent.putExtra("user_id", item.getUserID());
                                startActivityForResult(intent, currentPosition);
                            }
                        }
                    }, new MeterUserListRecycleAdapter.onRecyclerViewItemLongClick() {
                        @Override
                        public void onItemLongClick(View v, int position) {

                        }
                    });
                    // 设置布局管理器
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    // 设置adapter
                    mRecyclerView.setAdapter(mAdapter);
                    MyAnimationUtils.viewGroupOutAlphaAnimation(MeterUserUndoneActivity.this, mRecyclerView, 0.1F);
                    //设置增加或删除条目的动画
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    break;
                case 1:
                    mLayoutManager = new LinearLayoutManager(MeterUserUndoneActivity.this, LinearLayoutManager.VERTICAL, false);
                    mAdapter = new MeterUserListRecycleAdapter(userLists, new MeterUserListRecycleAdapter.onRecyclerViewItemClick() {
                        @Override
                        public void onItemClick(View v, int position) {
                            currentPosition = position;
                            Log.i("MeterUserListviewAc", "RecycleView的点击事件进来了！");
                            if (null != mRecyclerView.getChildViewHolder(v)) {
                                RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(v);
                                item = (MeterUserListviewItem) viewHolder.itemView.getTag();
                                Intent intent = new Intent(MeterUserUndoneActivity.this, MeterUserDetailActivity.class);
                                intent.putExtra("user_id", item.getUserID());
                                startActivityForResult(intent, currentPosition);
                            }
                        }
                    }, new MeterUserListRecycleAdapter.onRecyclerViewItemLongClick() {
                        @Override
                        public void onItemLongClick(View v, int position) {

                        }
                    });
                    mAdapter.notifyDataSetChanged();
                    // 设置布局管理器
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    // 设置adapter
                    mRecyclerView.setAdapter(mAdapter);
                    MyAnimationUtils.viewGroupOutAlphaAnimation(MeterUserUndoneActivity.this, mRecyclerView, 0.1F);
                    //设置增加或删除条目的动画
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    currentPageTv.setText(String.valueOf(Integer.parseInt(currentPageTv.getText().toString()) + 1));
                    break;
                case 2:
                    mLayoutManager = new LinearLayoutManager(MeterUserUndoneActivity.this, LinearLayoutManager.VERTICAL, false);
                    mAdapter = new MeterUserListRecycleAdapter(userLists, new MeterUserListRecycleAdapter.onRecyclerViewItemClick() {
                        @Override
                        public void onItemClick(View v, int position) {
                            currentPosition = position;
                            Log.i("MeterUserListviewAc", "RecycleView的点击事件进来了！");
                            if (null != mRecyclerView.getChildViewHolder(v)) {
                                RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(v);
                                item = (MeterUserListviewItem) viewHolder.itemView.getTag();
                                Intent intent = new Intent(MeterUserUndoneActivity.this, MeterUserDetailActivity.class);
                                intent.putExtra("user_id", item.getUserID());
                                startActivityForResult(intent, currentPosition);
                            }
                        }
                    }, new MeterUserListRecycleAdapter.onRecyclerViewItemLongClick() {
                        @Override
                        public void onItemLongClick(View v, int position) {

                        }
                    });
                    mAdapter.notifyDataSetChanged();
                    // 设置布局管理器
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    // 设置adapter
                    mRecyclerView.setAdapter(mAdapter);
                    MyAnimationUtils.viewGroupOutAlphaAnimation(MeterUserUndoneActivity.this, mRecyclerView, 0.1F);
                    //设置增加或删除条目的动画
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    currentPageTv.setText(String.valueOf(Integer.parseInt(currentPageTv.getText().toString()) - 1));
                    break;
                case 3:
                    if (noData.getVisibility() == View.GONE) {
                        noData.setVisibility(View.VISIBLE);
                    }
                    pageTurning.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //查询抄表用户总数
    public void getTotalUserCount() {
        if ("单个".equals(type)) {
            if (sharedPreferences.getBoolean("show_temp_data", false)) {
                totalCountCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? and meterState=?", new String[]{"0", fileName, bookID, "false"});//查询并获得游标
            }else {
                totalCountCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? and meterState=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID, "false"});//查询并获得游标
            }
        } else {
            if (sharedPreferences.getBoolean("show_temp_data", false)) {
                totalCountCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meterState=?", new String[]{"0", fileName, "false"});//查询并获得游标
            }else {
                totalCountCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meterState=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, "false"});//查询并获得游标
            }
        }
        //如果游标为空，则显示没有数据图片
        Log.i("MeterUserUndoneActivity", "总的查询到" + totalCountCursor.getCount() + "条数据！");
        if (totalCountCursor.getCount() == 0) {
            return;
        }
        while (totalCountCursor.moveToNext()) {

        }
        totalCountCursor.close();
    }

    //读取本地的抄表分区用户数据
    public void getMeterUserData(String fileName, String bookID, int dataStartCount) {
        userLists.clear();
        if ("单个".equals(type)) {
            if (!"".equals(sharedPreferences.getString("page_count", ""))) {
                if (sharedPreferences.getBoolean("show_temp_data", false)) {
                    userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? and meterState=? limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{"0", fileName, bookID, "false"});//查询并获得游标
                }else {
                    userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? and meterState=? limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID, "false"});//查询并获得游标
                }
            } else {
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? and meterState=? limit " + dataStartCount + ",50", new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID, "false"});//查询并获得游标
            }
        } else {
            if (!"".equals(sharedPreferences.getString("page_count", ""))) {
                if (sharedPreferences.getBoolean("show_temp_data", false)) {
                    userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meterState=? limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{"0", fileName, "false"});//查询并获得游标
                }else {
                    userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meterState=? limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, "false"});//查询并获得游标
                }
            } else {
                if (sharedPreferences.getBoolean("show_temp_data", false)) {
                    userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meterState=? limit " + dataStartCount + ",50", new String[]{"0", fileName, "false"});//查询并获得游标
                }else {
                    userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meterState=? limit " + dataStartCount + ",50", new String[]{sharedPreferences_login.getString("userId", ""), fileName, "false"});//查询并获得游标
                }
            }
        }
        Log.i("MeterUserLVActivity", "分页查询到" + userLimitCursor.getCount() + "条数据！");
        //如果游标为空，则显示没有数据图片
        if (userLimitCursor.getCount() == 0) {
            handler.sendEmptyMessage(3);
            return;
        }
        while (userLimitCursor.moveToNext()) {
            MeterUserListviewItem item = new MeterUserListviewItem();
            item.setMeterID(userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_order_number")));
            item.setUserName(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_name")));
            item.setUserID(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_id")));
            if (!userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_number")).equals("null")) {
                item.setMeterNumber(userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_number")));
            } else {
                item.setMeterNumber("无");
            }
            item.setLastMonthDegree(userLimitCursor.getString(userLimitCursor.getColumnIndex("meter_degrees")));
            item.setLastMonthDosage(userLimitCursor.getString(userLimitCursor.getColumnIndex("last_month_dosage")));
            item.setAddress(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_address")));
            if (userLimitCursor.getString(userLimitCursor.getColumnIndex("meterState")).equals("false")) {
                item.setMeterState("未抄");
                item.setIfEdit(R.mipmap.meter_false);
                item.setThisMonthDegree("无");
                item.setThisMonthDosage("无");
            } else {
                item.setMeterState("已抄");
                item.setIfEdit(R.mipmap.meter_true);
                item.setThisMonthDegree(userLimitCursor.getString(userLimitCursor.getColumnIndex("this_month_end_degree")));
                item.setThisMonthDosage(userLimitCursor.getString(userLimitCursor.getColumnIndex("this_month_dosage")));
            }
            userLists.add(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == currentPosition) {
                if (data != null) {
                    item.setThisMonthDegree(data.getStringExtra("this_month_end_degree"));
                    item.setThisMonthDosage(data.getStringExtra("this_month_dosage"));
                    item.setIfEdit(R.mipmap.meter_true);
                    item.setMeterState("已抄");
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //cursor游标操作完成以后,一定要关闭
        if (totalCountCursor != null) {
            totalCountCursor.close();
        }
        if (userLimitCursor != null) {
            userLimitCursor.close();
        }
        db.close();
    }
}
