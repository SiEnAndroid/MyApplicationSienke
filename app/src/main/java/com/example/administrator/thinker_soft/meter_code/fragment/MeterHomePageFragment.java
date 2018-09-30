package com.example.administrator.thinker_soft.meter_code.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.CustomQueryActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MapMeterActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterDataTransferActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterStatisticsActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserQueryResultActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserUndoneActivity;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterFileSelectListAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterSingleSelectItem;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;
import com.example.administrator.thinker_soft.meter_code.sk.ui.MeterUserListsActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.LayoutUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12 0012.
 * 首页（抄表设置）
 */
public class MeterHomePageFragment extends Fragment {
    private View view;
    private CardView meterReading, meterFile, query, map, statistics, transfer;
   // private ConstraintLayout coordinatorLayout;
    private LinearLayout noData;
    private LayoutInflater layoutInflater;
    private PopupWindow fileWindow, bookWindow, undoneWindow;
    private View fileSelectView, bookView, undoneView;
    private ListView fileListView, bookListview;
    private MeterSingleSelectItem fileItem, bookItem;
    private List<MeterSingleSelectItem> fileList = new ArrayList<>();
    private List<MeterSingleSelectItem> bookList = new ArrayList<>();
    private MeterFileSelectListAdapter fileAdapter, bookAdapter;
    private SQLiteDatabase db;  //数据库
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    private boolean bookMeterState, undoneMeterState;
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (SharedPreferencesHelper.getFirm(getActivity()).equals("渝山")) {
            view = inflater.inflate(R.layout.fragment_meter_home_page_nb, null);
        } else {
            view = inflater.inflate(R.layout.fragment_meter_home_page_new, null);
        }

        bindView();
        defaultSetting();
        setViewClickListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        bookMeterState = false;
        undoneMeterState = false;
    }

    //绑定控件
    private void bindView() {
        meterReading = (CardView) view.findViewById(R.id.meter_reading);
        meterFile = (CardView) view.findViewById(R.id.meter_file);
        query = (CardView) view.findViewById(R.id.query);
        map = (CardView) view.findViewById(R.id.map);
        statistics = (CardView) view.findViewById(R.id.statistics);
        transfer = (CardView) view.findViewById(R.id.transfer);

       // ImageView imageView = (ImageView) view.findViewById(R.id.pic_bg);

      //  LayoutUtil mLayoutUtil = LayoutUtil.getInstance();
      //  mLayoutUtil.drawViewLinearRBLayout(imageView, 0f, 360f, 0f, 0f, 0f, 0f);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(getActivity(), MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
    }

    //点击事件
    public void setViewClickListener() {
        meterReading.setOnClickListener(clickListener);
        meterFile.setOnClickListener(clickListener);
        query.setOnClickListener(clickListener);
        map.setOnClickListener(clickListener);
        statistics.setOnClickListener(clickListener);
        transfer.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.meter_reading:
                    //抄表
                    if (!"".equals(sharedPreferences.getString("currentFileName", ""))) {
//                        intent = new Intent(getActivity(), CaptureActivity.class);
//                        startActivityForResult(intent, REQUEST_CODE_SCAN);
                        showBookSelectWindow();
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                getBookInfo();
                            }
                        }.start();
                    } else {
                        Toast.makeText(getActivity(), "请先完成文件选择！", Toast.LENGTH_LONG).show();
//                     Snackbar.make(coordinatorLayout,"请您先选择抄表本！",Snackbar.LENGTH_INDEFINITE).show();
                    }
                    break;
                case R.id.meter_file:
                    //文件
                    showFileSelectWindow();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            getFileInfo();
                        }
                    }.start();
                    break;
                case R.id.query:
                    //查询
                    intent = new Intent(getActivity(), CustomQueryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.map:
                    //地图
                    intent = new Intent(getActivity(), MapMeterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.statistics:
                    //统计
                    intent = new Intent(getActivity(), MeterStatisticsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.transfer:
                    //传输
                    intent = new Intent(getActivity(), MeterDataTransferActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 抄表本弹出框选择
     */
    public void showBookSelectWindow() {
        layoutInflater = LayoutInflater.from(getActivity());
        bookView = layoutInflater.inflate(R.layout.popupwindow_meter_single_select, null);
        bookWindow = new PopupWindow(bookView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        TextView back = (TextView) bookView.findViewById(R.id.back);
        bookListview = (ListView) bookView.findViewById(R.id.list_view);
        TextView tips = (TextView) bookView.findViewById(R.id.tips);
        //设置点击事件
        tips.setText("请选择抄表本");
        bookListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                bookItem = (MeterSingleSelectItem) bookAdapter.getItem(position);
                Log.i("meterHomePage", "当前点击的item为：" + bookItem.getName());
                sharedPreferences.edit().putString("currentBookName", bookItem.getName()).apply();
                sharedPreferences.edit().putString("currentBookID", bookItem.getID()).apply();
                bookWindow.dismiss();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //跳转抄表用户列表
                //  Intent intent = new Intent(getActivity(), MeterUserListviewActivity.class);
                Intent intent = new Intent(getActivity(), MeterUserListsActivity.class);
                intent.putExtra("bookName", bookItem.getName());
                intent.putExtra("bookID", bookItem.getID());
                intent.putExtra("fileName", sharedPreferences.getString("currentFileName", ""));
                startActivity(intent);


               /* if (bookMeterState) {
                    intent = new Intent(getActivity(), MeterUserListviewActivity.class);
                } else if (undoneMeterState) {
                    intent = new Intent(getActivity(), MeterUserUndoneActivity.class);
                    intent.putExtra("type", "单个");
                }*/
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookWindow.dismiss();
            }
        });
        bookWindow.update();
        bookWindow.setFocusable(true);
        bookWindow.setTouchable(true);
        bookWindow.setOutsideTouchable(true);
        bookWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        bookWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        bookWindow.showAtLocation(bookView, Gravity.CENTER, 0, 0);
        bookWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    /**
     * 未抄抄表选择窗口
     */
    public void meterUndoneWindow() {
        layoutInflater = LayoutInflater.from(getActivity());
        undoneView = layoutInflater.inflate(R.layout.popupwindow_meter_user_undone, null);
        undoneWindow = new PopupWindow(undoneView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        TextView back = (TextView) undoneView.findViewById(R.id.back);
        TextView tips = (TextView) undoneView.findViewById(R.id.tips);
        TextView singleBook = (TextView) undoneView.findViewById(R.id.single_book);
        TextView allBook = (TextView) undoneView.findViewById(R.id.all_book);
        LinearLayout containerLayout = (LinearLayout) undoneView.findViewById(R.id.container_layout);
        //设置点击事件
        tips.setText("请选择方式");
        MyAnimationUtils.viewGroupOutAnimation(getActivity(), containerLayout, 0.1F);
        singleBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoneWindow.dismiss();
                showBookSelectWindow();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        getBookInfo();
                    }
                }.start();
            }
        });
        allBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoneWindow.dismiss();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getActivity(), MeterUserUndoneActivity.class);
                intent.putExtra("type", "所有");
                intent.putExtra("fileName", sharedPreferences.getString("currentFileName", ""));
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoneWindow.dismiss();
            }
        });
        undoneWindow.update();
        undoneWindow.setFocusable(true);
        undoneWindow.setTouchable(true);
        undoneWindow.setOutsideTouchable(true);
        undoneWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        undoneWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        undoneWindow.showAtLocation(undoneView, Gravity.CENTER, 0, 0);
        undoneWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    /**
     * 文件选择窗口
     */
    public void showFileSelectWindow() {
        layoutInflater = LayoutInflater.from(getActivity());
        fileSelectView = layoutInflater.inflate(R.layout.popupwindow_meter_single_select, null);
        fileWindow = new PopupWindow(fileSelectView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        TextView back = (TextView) fileSelectView.findViewById(R.id.back);
        fileListView = (ListView) fileSelectView.findViewById(R.id.list_view);
        TextView tips = (TextView) fileSelectView.findViewById(R.id.tips);
        noData = (LinearLayout) fileSelectView.findViewById(R.id.no_data);
        //设置点击事件
        tips.setText("请选择文件夹");
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                fileItem = (MeterSingleSelectItem) fileAdapter.getItem(position);
                Log.i("meterHomePage", "当前点击的item为：" + fileItem.getName());
                sharedPreferences.edit().putString("currentFileName", fileItem.getName()).apply();
                fileWindow.dismiss();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileWindow.dismiss();
            }
        });
        fileWindow.update();
        fileWindow.setFocusable(true);
        fileWindow.setTouchable(true);
        fileWindow.setOutsideTouchable(true);
        fileWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        fileWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        fileWindow.showAtLocation(fileSelectView, Gravity.CENTER, 0, 0);
        fileWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "扫码取消！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "扫描成功，条码值: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String result1 = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
//                resultTv.setText("扫码结果为：" + result1);
                queryMeterUserInfo(result1);
            }
        }
    }

    /**
     * 查询抄表用户信息
     *
     * @param userID
     */
    public void queryMeterUserInfo(String userID) {
        userLists.clear();
        Cursor cursor;
//        if (userID!=null){
        cursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and user_id=?", new String[]{sharedPreferences_login.getString("userId", ""), sharedPreferences.getString("currentFileName", ""), userID});//查询并获得游标
//        }else {
//        cursor=db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meter_number=?", new String[]{sharedPreferences_login.getString("userId", ""),sharedPreferences.getString("currentFileName",""),meterNumber});//查询并获得游标
//        }
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(3);
            return;
        }
        while (cursor.moveToNext()) {
            MeterUserListviewItem item = new MeterUserListviewItem();
            item.setMeterID(cursor.getString(cursor.getColumnIndex("meter_order_number")));
            item.setUserName(cursor.getString(cursor.getColumnIndex("user_name")));
            item.setUserID(cursor.getString(cursor.getColumnIndex("user_id")));
            if (!cursor.getString(cursor.getColumnIndex("old_user_id")).equals("null")) {
                item.setOldUserID(cursor.getString(cursor.getColumnIndex("old_user_id")));
            } else {
                item.setOldUserID("无");
            }
            if (!cursor.getString(cursor.getColumnIndex("meter_number")).equals("null")) {
                item.setMeterNumber(cursor.getString(cursor.getColumnIndex("meter_number")));
            } else {
                item.setMeterNumber("无");
            }
            item.setLastMonthDegree(cursor.getString(cursor.getColumnIndex("meter_degrees")));
            item.setLastMonthDosage(cursor.getString(cursor.getColumnIndex("last_month_dosage")));
            item.setAddress(cursor.getString(cursor.getColumnIndex("user_address")));
            if (cursor.getString(cursor.getColumnIndex("uploadState")).equals("false")) {
                item.setUploadState("");
            } else {
                item.setUploadState("已上传");
            }
            if (cursor.getString(cursor.getColumnIndex("meterState")).equals("false")) {
                item.setMeterState("未抄");
                item.setIfEdit(R.mipmap.meter_false);
                item.setThisMonthDegree("无");
                item.setThisMonthDosage("无");
            } else {
                item.setMeterState("已抄");
                item.setIfEdit(R.mipmap.meter_true);
                item.setThisMonthDegree(cursor.getString(cursor.getColumnIndex("this_month_end_degree")));
                item.setThisMonthDosage(cursor.getString(cursor.getColumnIndex("this_month_dosage")));
            }
            userLists.add(item);
        }
        handler.sendEmptyMessage(4);
        cursor.close();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    fileAdapter = new MeterFileSelectListAdapter(getActivity(), fileList, 1);
                    fileListView.setAdapter(fileAdapter);
                    MyAnimationUtils.viewGroupOutAnimation(getActivity(), fileListView, 0.1F);
                    break;
                case 1:
                    noData.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    //抄表可选文件
                    bookAdapter = new MeterFileSelectListAdapter(getActivity(), bookList, 0);
                    bookListview.setAdapter(bookAdapter);
                    MyAnimationUtils.viewGroupOutAnimation(getActivity(), bookListview, 0.1F);
                    break;
                case 3:
                    Toast.makeText(getActivity(), "未查到用户信息，请您核对编号是否正确！", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Intent intent = new Intent(getActivity(), MeterUserQueryResultActivity.class);
                    intent.putParcelableArrayListExtra("meter_user_info", userLists);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //查询抄表文件信息
    public void getFileInfo() {
        fileList.clear();
        Cursor cursor;
        if (sharedPreferences.getBoolean("show_temp_data", false)) {
            Log.i("getFileInfo", "演示数据进来了");
            cursor = db.rawQuery("select * from MeterFile where login_user_id=?", new String[]{"0"});//查询并获得游标
        } else {
            cursor = db.rawQuery("select * from MeterFile where login_user_id=?", new String[]{sharedPreferences_login.getString("userId", "")});//查询并获得游标
        }
        Log.i("meterHomePage", "所有表册ID个数为：" + cursor.getCount());
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(1);
            return;
        }
        while (cursor.moveToNext()) {
            MeterSingleSelectItem item = new MeterSingleSelectItem();
            item.setName(cursor.getString(cursor.getColumnIndex("fileName")));
            fileList.add(item);
        }
        handler.sendEmptyMessage(0);
        cursor.close();
    }

    //查询抄表本信息
    public void getBookInfo() {
        bookList.clear();
        Cursor cursor;
        if (sharedPreferences.getBoolean("show_temp_data", false)) {
            cursor = db.rawQuery("select * from MeterBook where login_user_id=? and fileName=?", new String[]{"0", sharedPreferences.getString("currentFileName", "")});//查询并获得游标
        } else {
            cursor = db.rawQuery("select * from MeterBook where login_user_id=? and fileName=?", new String[]{sharedPreferences_login.getString("userId", ""), sharedPreferences.getString("currentFileName", "")});//查询并获得游标
        }
        Log.i("meterHomePage", "抄表本个数为：" + cursor.getCount());
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            MeterSingleSelectItem item = new MeterSingleSelectItem();
            item.setName(cursor.getString(cursor.getColumnIndex("bookName")));
            item.setID(cursor.getString(cursor.getColumnIndex("bookId")));
            bookList.add(item);
        }
        handler.sendEmptyMessage(2);
        cursor.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        if (bookWindow != null) {
            bookWindow = null;
        }
    }
}
