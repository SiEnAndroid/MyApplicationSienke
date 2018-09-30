package com.example.administrator.thinker_soft.meter_code.activity;

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
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import com.example.administrator.thinker_soft.meter_code.adapter.MeterUserListRecycleAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterTypeListviewItem;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;
import com.example.administrator.thinker_soft.meter_payment.Utils.MyDialog;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.mode.Tools;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 抄表用户列表
 */
public class MeterUserListviewActivity extends AppCompatActivity {
    private ImageView back, more;
    private LinearLayout rootLinearlayout;
    private RelativeLayout title;
    private TextView noData, currentPageTv, totalPageTv, bookName;
    private EditText etSpot;
    private Button btnGoSpot;
    private ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();
    private SQLiteDatabase db;  //数据库
    private Cursor totalCountCursor, userLimitCursor, notCopyCountCursor;
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    private int dataStartCount = 0;   //用于分页查询，表示从第几行开始
    private int currentPage = 1;  //当前页数
    private int totalPage;    //总页数
    private int pageCount;//每页显示条数
    private MeterUserListviewItem item;
    private int currentPosition;  //点击当前抄表用户的item位置
    private List<MeterTypeListviewItem> areaItemList = new ArrayList<>();
    private List<MeterTypeListviewItem> bookItemList = new ArrayList<>();
    private List<String> bookIDList = new ArrayList<>();  //存放表册ID的集合
    private String bookID, book_name, fileName;
    private LayoutInflater layoutInflater;
    private PopupWindow fastMeterWindow;
    private PopupWindow popupWindow;
    private View fastMeterview;
    private MeterUserListRecycleAdapter.ViewHolder viewHolder;
    private String meterDate;
    private SimpleDateFormat dateFormat;
    private RelativeLayout layout;
    private int position;
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
    /**
     * 目标项是否在最后一个可见项之后
     */
    private boolean mShouldScroll;
    /**
     * 记录目标项位置
     */
    private int mToPosition;
    // 定位相关
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();

    private String longitude, latitude, locationAddress;
    /**
     * 查询数据的总条数，用来判断是否还需要上拉加载。
     */
    private int totalCount;
    private LinearLayoutManager linearLayoutManager;
    private int listPosition;
    private String positionFileName;
    private String positionBookName;
    private boolean queryAllMeter = true;
    private PopupWindow userStatePop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_user_listview);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //绑定控件
    private void bindView() {
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
        title = (RelativeLayout) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        bookName = (TextView) findViewById(R.id.book_name);
        noData = (TextView) findViewById(R.id.no_data);
        currentPageTv = (TextView) findViewById(R.id.current_page_tv);
        totalPageTv = (TextView) findViewById(R.id.total_page_tv);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        etSpot = (EditText) findViewById(R.id.et_spot);
        btnGoSpot = (Button) findViewById(R.id.btn_go_spot);
        more = (ImageView) findViewById(R.id.more);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterUserListviewActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = MeterUserListviewActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        mLayoutManager = new LinearLayoutManager(MeterUserListviewActivity.this, LinearLayoutManager.VERTICAL, false);

        if (sharedPreferences.getString("page_count", "").equals("")) {
            Toast.makeText(MeterUserListviewActivity.this, "默认每页加载50条数据!", Toast.LENGTH_SHORT).show();
            sharedPreferences.edit().putString("page_count", "50").apply();
        }
        if (Integer.valueOf(sharedPreferences.getString("page_count", "")) <= 0) {
            Toast.makeText(MeterUserListviewActivity.this, "每页加载数据必须大于0,现在更正为50条!", Toast.LENGTH_SHORT).show();
            pageCount = 50;
            sharedPreferences.edit().putString("page_count", "50").apply();
        } else {
            pageCount = Integer.valueOf(sharedPreferences.getString("page_count", ""));
        }
        listPosition = sharedPreferences.getInt("list_position", 0);
        positionFileName = sharedPreferences.getString("position_file_name", "");
        positionBookName = sharedPreferences.getString("position_book_name", "");
//        if (sharedPreferences.getString("position_file_name", "").equals(fileName) && sharedPreferences.getString("position_book_name", "").equals(book_name)) {
        Log.i("MeterUserListView", "defaultSetting: list_position:" + listPosition + ",book_name:" + positionBookName + ",file_name" + positionFileName);
        /**
         * 设置 下拉刷新 Header 和 footer 风格样式
         */
        //mRefreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //mRefreshLayout.setRefreshHeader(new DeliveryHeader(this));
        //mRefreshLayout.setRefreshHeader(new CircleHeader(this));
        //mRefreshLayout.setRefreshHeader(new DropboxHeader(this));
        //mRefreshLayout.setRefreshHeader(new FunGameHeader(this));
        //mRefreshLayout.setRefreshHeader(new FalsifyHeader(this));
        //mRefreshLayout.setRefreshHeader(new PhoenixHeader(this));
        mRefreshLayout.setRefreshHeader(new WaterDropHeader(this));
        mRefreshLayout.setPrimaryColorsId(R.color.theme_colors, android.R.color.white);
        //mRefreshLayout.setRefreshHeader(new WaveSwipeHeader(this));
        //mRefreshLayout.setRefreshHeader(new TaurusHeader(this));
        //mRefreshLayout.setRefreshHeader(new StoreHouseHeader(this));
        //mRefreshLayout.setRefreshHeader(new FunGameHitBlockHeader(this));
        //mRefreshLayout.setRefreshHeader(new FunGameBattleCityHeader(this));
        //mRefreshLayout.setRefreshHeader(new FlyRefreshHeader(this));
        //设置 Footer 为 球脉冲
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        mRefreshLayout.setDisableContentWhenRefresh(true);  //是否在刷新的时候禁止内容的一切手势操作（默认false）
        mRefreshLayout.setDisableContentWhenLoading(true);  //是否在加载的时候禁止内容的一切手势操作（默认false）
        /**
         * 获取上个页面传过来的参数`
         */
        Intent intent = getIntent();
        if (intent != null) {
            fileName = intent.getStringExtra("fileName");
            bookID = intent.getStringExtra("bookID");
            book_name = intent.getStringExtra("bookName");
            bookName.setText("当前：" + book_name);
            if (!"".equals(bookID) && !"".equals(fileName)) {
                Log.i("meter_user", "");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        getTotalUserCount();
                        getMeterUserData(fileName, bookID, dataStartCount);   //默认读取本地的抄表分区用户数据
                    }
                }.start();
            }
        }
        Log.i("MeterUserListView", "defaultSetting: book_name:" + bookName + ",file_name" + fileName);
//        mRecyclerView.setFocusable(true);
//        mRecyclerView.setFocusableInTouchMode(true);
        mRecyclerView.requestFocus();
//        mRecyclerView.requestFocusFromTouch();

//        }
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        btnGoSpot.setOnClickListener(clickListener);
        more.setOnClickListener(clickListener);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*if (newState == RecyclerView.SCROLL_STATE_IDLE) {   // 当不滚动或者滚动停止时
                    currentPosition = ((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition();
                    layout = (RelativeLayout) mLayoutManager.findViewByPosition(currentPosition);
                    layout.findViewById(R.id.red_stroke).setVisibility(View.VISIBLE);
                    Log.i("MeterUserListview", "滚动后的位置是：" + currentPosition);
                    //mLayoutManager.getChildAt(currentPosition).findViewById(R.id.red_stroke).setVisibility(View.VISIBLE);
                }*/
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager instanceof LinearLayoutManager) {
                    linearLayoutManager = (LinearLayoutManager) manager;
                    position = linearLayoutManager.findLastVisibleItemPosition();
                }
                if (mShouldScroll) {
                    mShouldScroll = false;
                    smoothMoveToPosition(mRecyclerView, mToPosition);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        /**
         * 下拉刷新监听
         */
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(800);
                if (currentPageTv.getText().equals("1")) {
                    Toast.makeText(MeterUserListviewActivity.this, "已经是第一页哦！", Toast.LENGTH_SHORT).show();
                } else {
                    dataStartCount = 0;
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            if (queryAllMeter) {
                                userLists.clear();
                                getMeterUserData(fileName, bookID, dataStartCount);  //读取抄表本用户数据
                                handler.sendEmptyMessage(8);
                            } else {
                                userLists.clear();
                                getNotCopyUserData(fileName, bookID, dataStartCount);
                                handler.sendEmptyMessage(8);
                            }
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
                    Toast.makeText(MeterUserListviewActivity.this, "已经是最后一页哦！", Toast.LENGTH_SHORT).show();
                } else {
                    if (sharedPreferences.getString("page_count", "").equals("")) {
                        dataStartCount += 50;
                    } else {
                        dataStartCount += Integer.valueOf(sharedPreferences.getString("page_count", ""));
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Log.i("MeterUserLVActivity", "run: " + (userLists.size() == totalCount ? true : false));
                            if (queryAllMeter) {
                                getTotalUserCount();
                                if (userLists.size() == totalCount) {
                                    handler.sendEmptyMessage(6);
                                } else {
                                    getMeterUserData(fileName, bookID, dataStartCount);  //读取抄表本用户数据
                                    handler.sendEmptyMessage(7);
                                }
                            } else {
                                getNotCopyUserCount();
                                if (userLists.size() == totalCount) {
                                    handler.sendEmptyMessage(6);
                                } else {
                                    getNotCopyUserData(fileName, bookID, dataStartCount);
                                    handler.sendEmptyMessage(7);
                                }
                            }
                        }
                    }.start();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
            LinearLayoutManager llm = (LinearLayoutManager) manager;
            int position = llm.findFirstVisibleItemPosition();
            sharedPreferences.edit().putInt("list_position", position).apply();
            sharedPreferences.edit().putString("position_file_name", fileName).apply();
            sharedPreferences.edit().putString("position_book_name", book_name).apply();
        }
        return super.onKeyDown(keyCode, event);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
                    LinearLayoutManager llm = (LinearLayoutManager) manager;
                    int position = llm.findFirstVisibleItemPosition();
                    sharedPreferences.edit().putInt("list_position", position).apply();
                    sharedPreferences.edit().putString("position_file_name", fileName).apply();
                    sharedPreferences.edit().putString("position_book_name", book_name).apply();
                    Log.i("MeterUserListView", "onClick: " + sharedPreferences.getInt("list_position", 0));
                    MeterUserListviewActivity.this.finish();
                    break;
                case R.id.btn_go_spot:
                    if (etSpot.getText().toString().equals("")) {
                        Toast.makeText(MeterUserListviewActivity.this, "请输入用户编号进行查询跳转！", Toast.LENGTH_SHORT).show();
                    } else {
                        etSpot.setText(etSpot.getText().toString().trim().toUpperCase());
                        new Thread() {
                            @Override
                            public void run() {
                                if (!userLists.isEmpty()) {
                                    serchSpot();
                                }
                            }
                        }.start();
                    }
                    break;
                case R.id.more:
                    showMoreWindow();
                    break;
                default:
                    break;
            }
        }
    };


    private void showMoreWindow() {
        View contentView = getLayoutInflater().inflate(R.layout.popwindow_meter_user_state, null);
        userStatePop = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
                dataStartCount = 0;
                userLists.clear();
                currentPageTv.setText("1");
                getTotalUserCount();
                getMeterUserData(fileName, bookID, dataStartCount);
                queryAllMeter = true;
                userStatePop.dismiss();
            }
        });
        notCopyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataStartCount = 0;
                userLists.clear();
                currentPageTv.setText("1");
                getNotCopyUserCount();
                getNotCopyUserData(fileName, bookID, dataStartCount);
                queryAllMeter = false;
                userStatePop.dismiss();
            }
        });
        userStatePop.setFocusable(true);
        userStatePop.setOutsideTouchable(true);
        userStatePop.setAnimationStyle(R.style.mypopwindow_anim_style);
        userStatePop.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        userStatePop.showAsDropDown(more, -180, 10);
        backgroundAlpha(0.6F);
        userStatePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    private void serchSpot() {
        boolean isSearch = false;
        for (int i = 0; i < userLists.size(); i++) {
            MeterUserListviewItem item = userLists.get(i);
            if (item.getOldUserID().equals(etSpot.getText().toString().trim()) || item.getUserID().equals(etSpot.getText().toString().trim())) {
                isSearch = true;
                Message msg = new Message();
                msg.arg1 = i;
                msg.what = 10;
                handler.sendMessage(msg);
            }
        }
        if (!isSearch) {
            if (userLists.size() != totalCount) {
                if (sharedPreferences.getString("page_count", "").equals("")) {
                    dataStartCount += 50;
                } else {
                    dataStartCount += Integer.valueOf(sharedPreferences.getString("page_count", ""));
                }
                getMeterUserData(fileName, bookID, dataStartCount);  //读取抄表本用户数据
                serchSpot();
                handler.sendEmptyMessage(7);
            } else {
                handler.sendEmptyMessage(11);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    /**
                     * 使用 RecyclerView 控件
                     */
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    } else {
                        //跳转
                        mAdapter = new MeterUserListRecycleAdapter(userLists, new MeterUserListRecycleAdapter.onRecyclerViewItemClick() {
                            @Override
                            public void onItemClick(View v, int position) {
                                //跳转
                                currentPosition = position;
                                Log.i("MeterUserListviewAc", "RecycleView的点击事件进来了！");
                                if (null != mRecyclerView.getChildViewHolder(v)) {
                                    viewHolder = (MeterUserListRecycleAdapter.ViewHolder) mRecyclerView.getChildViewHolder(v);
                                    item = (MeterUserListviewItem) viewHolder.itemView.getTag();
                                    if (sharedPreferences.getBoolean("detail_meter", true)) {
                                        Intent intent = new Intent(MeterUserListviewActivity.this, MeterUserDetailActivity.class);
                                        intent.putExtra("user_id", item.getUserID());
                                        intent.putExtra("upload_state", item.getUploadState());
                                        startActivityForResult(intent, currentPosition);
                                    } else {
                                        if (item.getUploadState().equals("已上传")) {
                                            Toast.makeText(MeterUserListviewActivity.this, "用户数据已上传，不能继续抄表！", Toast.LENGTH_SHORT).show();
                                        } else {
                                            layout = (RelativeLayout) mLayoutManager.findViewByPosition(currentPosition);
                                            showFastMeterWindow(item.getLastMonthDegree(), Integer.parseInt(item.getLastMonthDegree()) + Integer.parseInt(item.getChangeDosage()) + Double.parseDouble(item.getRemission()));   //弹出快捷抄表框
                                        }
                                    }
                                }

                            }
                        }, new MeterUserListRecycleAdapter.onRecyclerViewItemLongClick() {
                            @Override
                            public void onItemLongClick(View v, int position) {
                                viewHolder = (MeterUserListRecycleAdapter.ViewHolder) mRecyclerView.getChildViewHolder(v);
                                item = (MeterUserListviewItem) viewHolder.itemView.getTag();
                                startNavi(item.getAddress());
                            }
                        });
                        // 设置布局管理器
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        // 设置adapter
                        mRecyclerView.setAdapter(mAdapter);
                        if (positionFileName.equals(fileName) && positionBookName.equals(book_name)) {
                            if (userLists.size() < listPosition) {
                                int i = Math.round(listPosition / pageCount);
                                dataStartCount = dataStartCount * i;
                                getMeterUserData(fileName, bookID, dataStartCount);  //读取抄表本用户数据
                                handler.sendEmptyMessage(1);
                            }
                            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
                            linearLayoutManager.scrollToPositionWithOffset(listPosition, 0);
                        }
                        MyAnimationUtils.viewGroupOutAlphaAnimation(MeterUserListviewActivity.this, mRecyclerView, 0.1F);
                        //设置增加或删除条目的动画
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    }
                    break;
                case 1:
                    currentPageTv.setText(totalPage + "");
                    break;
                case 5:
                    noData.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    Toast.makeText(MeterUserListviewActivity.this, "已经是最后一页哦！", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    currentPageTv.setText(String.valueOf(Integer.parseInt(currentPageTv.getText().toString()) + 1));
                    break;
                case 8:
                    currentPageTv.setText("1");
                    break;
                case 9:
                    totalPageTv.setText(totalPage + "");
                    break;
                case 10:
                    int position = msg.arg1;
                    smoothMoveToPosition(mRecyclerView, position);
                    break;
                case 11:
                    Toast.makeText(MeterUserListviewActivity.this, "没有查找到相关用户！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    /**
     * 弹出快捷抄表窗口
     */
    public void showFastMeterWindow(final String lastMonthDegree, final Double dosage) {
        initLocation();
        layoutInflater = LayoutInflater.from(MeterUserListviewActivity.this);
        fastMeterview = layoutInflater.inflate(R.layout.popupwindow_fast_meter, null);
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
                Tools.hideSoftInput(MeterUserListviewActivity.this, meterEdit);
                fastMeterWindow.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(meterEdit.getText().toString())) {
                    if (Integer.parseInt(meterEdit.getText().toString()) >= Integer.parseInt(lastMonthDegree)) {
                        final String thisMonthDegree = meterEdit.getText().toString();
                        final double thisMonthDosage = Integer.valueOf(thisMonthDegree) - dosage;
                        if ((thisMonthDosage - Double.valueOf(item.getLastMonthDosage())) > Double.valueOf(item.getLastMonthDosage()) / 2) {
                            final MyDialog myDialog = new MyDialog();
                            myDialog.show("用量异常提示", "用量较上月超出过多,请确认数据.若无误请点击确认！", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Tools.hideSoftInput(MeterUserListviewActivity.this, meterEdit);
                                    fastMeterWindow.dismiss();
                                    item.setThisMonthDegree(thisMonthDegree);
                                    item.setThisMonthDosage(String.valueOf(thisMonthDosage));
                                    item.setIfEdit(R.mipmap.meter_true);
                                    item.setMeterState("已抄");
                                    mAdapter.notifyDataSetChanged();
//                        Tools.moveToPosition((LinearLayoutManager) mLayoutManager, mRecyclerView, currentPosition + 1);
                                    updateMeterUserInfo(thisMonthDegree, String.valueOf(thisMonthDosage), item.getUserID());
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myDialog.dismiss();
                                }
                            }, getSupportFragmentManager());
                        } else {
                            Tools.hideSoftInput(MeterUserListviewActivity.this, meterEdit);
                            fastMeterWindow.dismiss();
                            item.setThisMonthDegree(thisMonthDegree);
                            item.setThisMonthDosage(String.valueOf(thisMonthDosage));
                            item.setIfEdit(R.mipmap.meter_true);
                            item.setMeterState("已抄");
                            mAdapter.notifyDataSetChanged();
//                        Tools.moveToPosition((LinearLayoutManager) mLayoutManager, mRecyclerView, currentPosition + 1);
                            updateMeterUserInfo(thisMonthDegree, String.valueOf(thisMonthDosage), item.getUserID());
                        }
                    } else {
                        Toast.makeText(MeterUserListviewActivity.this, "本月读数不能小于上月读数哦！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MeterUserListviewActivity.this, "请您输入本月读数！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fastMeterWindow.update();
        fastMeterWindow.setFocusable(true);
        fastMeterWindow.setTouchable(true);
        fastMeterWindow.setOutsideTouchable(true);
        fastMeterWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        fastMeterWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        fastMeterWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        fastMeterWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
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
        mLocClient = new LocationClient(getApplicationContext()); //声明LocationClient类
        mLocClient.registerLocationListener(myListener);     //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll"); // 设置坐标类型
        //option.setScanSpan(1000);
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
//        longitudeTv.setText(longitude);
//        latitudeTv.setText(latitude);
//        addressTv.setText(locationAddress);
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


    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MeterUserListviewActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterUserListviewActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterUserListviewActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterUserListviewActivity.this.getWindow().setAttributes(lp);
    }

    //查询抄表用户总数
    public void getTotalUserCount() {
        if (sharedPreferences.getBoolean("show_temp_data", false)) {
            totalCountCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=?", new String[]{"0", fileName, bookID});//查询并获得游标
        } else {
            totalCountCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游标
        }
        //如果游标为空，则显示没有数据图片
        totalCount = totalCountCursor.getCount();
        Log.i("MeterUserLVActivity", "总的查询到" + totalCountCursor.getCount() + "条数据！");
        if (totalCountCursor.getCount() == 0) {
            return;
        }
        totalPage = totalCount % pageCount > 0 ? totalCount / pageCount + 1 : totalCount / pageCount;
        Log.i("MeterUserLVActivity", "总的页数" + totalPage + "页！");
        handler.sendEmptyMessage(9);
        while (totalCountCursor.moveToNext()) {

        }
        totalCountCursor.close();
    }


    //读取本地的抄表分区用户数据
    public void getMeterUserData(String fileName, String bookID, int dataStartCount) {
//        userLists.clear();
        if (!"".equals(sharedPreferences.getString("page_count", ""))) {
            if (sharedPreferences.getBoolean("show_temp_data", false)) {   //显示演示数据
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{"0", fileName, bookID});//查询并获得游标
            } else {
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? order by meter_order_number limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游标
            }
        } else {
            int i = totalCount - dataStartCount;
            int updaCount = 0;
            if (i >= pageCount) {
                updaCount = pageCount;
            } else {
                updaCount = i;
            }
            if (sharedPreferences.getBoolean("show_temp_data", false)) {  //显示演示数据
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + updaCount, new String[]{"0", fileName, bookID});//查询并获得游
            } else {
                if (dataStartCount == -1) {
                    userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游标
                } else {
                    userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? order by meter_order_number limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游标
                }
            }
        }

        Log.i("MeterUserLVActivity", "分页查询到" + userLimitCursor.getCount() + "条数据！");
        //如果游标为空，则显示没有数据图片
        if (userLimitCursor.getCount() == 0) {
            handler.sendEmptyMessage(6);
            return;
        }
        while (userLimitCursor.moveToNext()) {
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
            } else {
                item.setUploadState("已上传");
            }
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
            //item.setRemark(userLimitCursor.getString(userLimitCursor.getColumnIndex("opened_remark")));
            item.setRemark(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_remark")));
            userLists.add(item);
        }
        Log.i("MeterUserLVActivity", "getMeterUserData: userLists.size():" + userLists.size());
        Collections.sort(userLists);
        handler.sendEmptyMessage(0);

    }

    //查询未抄表用户总数
    public void getNotCopyUserCount() {
        if (sharedPreferences.getBoolean("show_temp_data", false)) {
            notCopyCountCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=?", new String[]{"0", fileName, bookID});//查询并获得游标
        } else {
            notCopyCountCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? and meterState=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID, "false"});//查询并获得游标
        }
        //如果游标为空，则显示没有数据图片
        totalCount = notCopyCountCursor.getCount();
        Log.i("MeterUserLVActivity", "总的查询到" + notCopyCountCursor.getCount() + "条数据！");
        if (notCopyCountCursor.getCount() == 0) {
            return;
        }
        totalPage = totalCount % pageCount > 0 ? totalCount / pageCount + 1 : totalCount / pageCount;
        Log.i("MeterUserLVActivity", "总的页数" + totalPage + "页！");
        handler.sendEmptyMessage(9);
        while (notCopyCountCursor.moveToNext()) {

        }
        notCopyCountCursor.close();
    }


    //读取本地的抄表分区未抄表用户数据
    public void getNotCopyUserData(String fileName, String bookID, int dataStartCount) {
//        userLists.clear();
        if (!"".equals(sharedPreferences.getString("page_count", ""))) {
            if (sharedPreferences.getBoolean("show_temp_data", false)) {   //显示演示数据
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{"0", fileName, bookID});//查询并获得游标
            } else {
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? and meterState=? order by meter_order_number limit " + dataStartCount + "," + Integer.parseInt(sharedPreferences.getString("page_count", "")), new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID, "false"});//查询并获得游标
            }
        } else {
            int i = totalCount - dataStartCount;
            int updaCount = 0;
            if (i >= pageCount) {
                updaCount = pageCount;
            } else {
                updaCount = i;
            }
            if (sharedPreferences.getBoolean("show_temp_data", false)) {  //显示演示数据
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? limit " + dataStartCount + "," + updaCount, new String[]{"0", fileName, bookID});//查询并获得游
            } else {
                userLimitCursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and book_id=? order by meter_order_number limit " + dataStartCount + "," + updaCount, new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游
            }

        }
        Log.i("MeterUserLVActivity", "分页查询到" + userLimitCursor.getCount() + "条数据！");
        //如果游标为空，则显示没有数据图片
        if (userLimitCursor.getCount() == 0) {
            handler.sendEmptyMessage(6);
            return;
        }
        while (userLimitCursor.moveToNext()) {
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
            } else {
                item.setUploadState("已上传");
            }
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
            // item.setRemark(userLimitCursor.getString(userLimitCursor.getColumnIndex("opened_remark")));
            item.setRemark(userLimitCursor.getString(userLimitCursor.getColumnIndex("user_remark")));
            userLists.add(item);
        }
        Log.i("MeterUserLVActivity", "getMeterUserData: userLists.size():" + userLists.size());
        Collections.sort(userLists);
        handler.sendEmptyMessage(0);
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
            mToPosition = position;
            mShouldScroll = true;
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
                    item.setRemark(data.getStringExtra("this_user_remark"));
                    item.setIfEdit(R.mipmap.meter_true);
                    item.setMeterState("已抄");
                    mAdapter.notifyDataSetChanged();
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
                OpenClientUtil.getLatestBaiduMapApp(MeterUserListviewActivity.this);
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
