package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterFileSelectListAdapter;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterUploadResulrtListAdapter;
import com.example.administrator.thinker_soft.meter_code.model.AreaInfo;
import com.example.administrator.thinker_soft.meter_code.model.BookInfo;
import com.example.administrator.thinker_soft.meter_code.model.MeterSingleSelectItem;
import com.example.administrator.thinker_soft.meter_code.model.UploadResultListItem;
import com.example.administrator.thinker_soft.meter_code.sk.bean.BookIdBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.MeterDataPrame;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallbackStringListener;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolFactory;
import com.example.administrator.thinker_soft.meter_code.sk.thread.ThreadPoolManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.MeterReaderDownloadActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.MeterReaderPersonnelActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.HttpUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.jude.easyrecyclerview.EasyRecyclerView.TAG;

/**
 * Created by Administrator on 2017/6/12 0012.
 * 数据传输
 */
public class MeterDataTransferActivity extends Activity {
    private ImageView back;
    private View fileSelectView, uploadView, loadingView, line;
    private CardView upload, download;
    private LayoutInflater layoutInflater;
    private PopupWindow fileWindow, uploadWindow, loadingWindow;
    private ListView fileListView, resultListview;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private LinearLayout rootLinearlayout, noData, progressLayout;
    private MeterSingleSelectItem fileItem;
    private MeterFileSelectListAdapter fileAdapter;
    private List<MeterSingleSelectItem> fileList = new ArrayList<>();
    private SQLiteDatabase db;  //数据库
    private SharedPreferences public_sharedPreferences, sharedPreferences_login, sharedPreferences;
    //  private String ip, port;  //接口ip地址   端口
    private String resultBook, resultArea; //抄表本结果，抄表分区结果
    public int responseCode = 0;
    private ArrayList<BookInfo> bookInfoArrayList = new ArrayList<>();   //抄表本集合
    private ArrayList<AreaInfo> areaInfoArrayList = new ArrayList<>();   //抄表分区集合
    private ArrayList<BookIdBean> bookList; //抄表本id集合
    private TextView title, tips, totalCount, ccurrentCount, confirm;  //加载进度的提示
    private int uploadDataCounts;
    private int currentProgress = 0;
    private List<UploadResultListItem> uploadResultListItems = new ArrayList<>();
    private MeterUploadResulrtListAdapter adapter;
    private boolean isCompleted = false;
    private boolean isFirst, isfaild;

    private String choice;//选择抄表员或者分区
    //获取序号
    private Map<String, String> mapNumber = new HashMap<>();

//    private String DB_NAME = "thinker_soft14.db";
//     private   SQLiteDatabase sqLiteDatabase;

    private Lock lock = new ReentrantLock();
    private String file_name;
    /**
     * 标记上传
     */
    private int index = 0;
    private int failCont = 0;
    private int mtNumber = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_transfer);

        bindView();
        defaultSetting();
        setViewClickListener();
    }


    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        upload = (CardView) findViewById(R.id.upload);
        download = (CardView) findViewById(R.id.download);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterDataTransferActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        public_sharedPreferences = MeterDataTransferActivity.this.getSharedPreferences("data", Context.MODE_PRIVATE);
        sharedPreferences_login = MeterDataTransferActivity.this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = MeterDataTransferActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        upload.setOnClickListener(clickListener);
        download.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    MeterDataTransferActivity.this.finish();
                    break;
                case R.id.upload:
                    //上传
//                    ThreadPoolManager.getInstance().execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(300);
//                                sqLiteDatabase = DBManager("com.example.administrator.thinker_soft");
//                                upDatas();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
                    currentProgress = 0;
                    showFileSelectWindow();
                    ThreadPoolManager.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            getFileInfo();
                        }
                    });
                    break;
                case R.id.download:
                    //绵竹
                    if (SharedPreferencesHelper.getFirm(MeterDataTransferActivity.this).equals("绵竹")) {
                        showAlertDialog(new String[]{"按抄表员", "按分区"});
                    } else {
                        showPopupwindow();
                        httpMeterBookDown();
                    }
//                    //苍溪
//                    if (SharedPreferencesHelper.getFirm(MeterDataTransferActivity.this).equals("苍溪")){
//                        sharedPreferences.edit().putInt("all_downloads", 0).apply();
//                    }else if  (SharedPreferencesHelper.getFirm(MeterDataTransferActivity.this).equals("南部")){
//                        sharedPreferences.edit().putInt("all_downloads", 0).apply();
//                    }else {
//                        sharedPreferences.edit().putInt("all_downloads", 1).apply();
//                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 文件选择窗口
     */
    public void showFileSelectWindow() {
        layoutInflater = LayoutInflater.from(MeterDataTransferActivity.this);
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
                getBookInfo(fileItem.getName());
                fileWindow.dismiss();
                showUploadTipsWindow(fileItem.getName());
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
        backgroundAlpha(0.6F);
        //背景变暗
        fileWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        fileWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //弹出上传数据前提示窗口
    public void showUploadTipsWindow(final String fileName) {
        layoutInflater = LayoutInflater.from(MeterDataTransferActivity.this);
        uploadView = layoutInflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        uploadWindow = new PopupWindow(uploadView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        TextView tip = (TextView) uploadView.findViewById(R.id.tips);
        RadioButton cancelRb = (RadioButton) uploadView.findViewById(R.id.cancel_rb);
        RadioButton saveRb = (RadioButton) uploadView.findViewById(R.id.save_rb);
        //设置点击事件
        tip.setText("确定要上传名为 '" + fileName + "' 的文件吗？");
        saveRb.setText("确定");
        cancelRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadWindow.dismiss();
            }
        });
        saveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadWindow.dismiss();
                isFirst = true;
                showUploadLoadingWindow(fileName);
                //上传
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                upDatas(fileName);
                    }
                });
            }
        });
        uploadWindow.update();
        uploadWindow.setFocusable(true);
        uploadWindow.setTouchable(true);
        uploadWindow.setOutsideTouchable(true);
        uploadWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        uploadWindow.setAnimationStyle(R.style.camera);
        backgroundAlpha(0.6F);   //背景变暗
        uploadWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        uploadWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //查询抄表文件信息
    public void getFileInfo() {
        fileList.clear();
        Cursor cursor = db.rawQuery("select * from MeterFile where login_user_id=?", new String[]{sharedPreferences_login.getString("userId", "")});//查询并获得游标
        Log.i("meterHomePage", "所有表册ID个数为：" + cursor.getCount());
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(6);
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

    /**
     * 封装上传的数据
     */
    private void dataToJson(String fileName) {
        uploadResultListItems.clear();
        Cursor cursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meterState=? and uploadState=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, "true", "false"});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        uploadDataCounts = cursor.getCount();
        Log.i("dataToJson", "上传总数为：" + uploadDataCounts);
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(8);
            return;
        }
        while (cursor.moveToNext()) {
            try {
                Thread.sleep(250);
                JSONObject object = new JSONObject();
                object.put("n_jw_x", cursor.getString(cursor.getColumnIndex("n_jw_x")));      //纬度
                object.put("n_jw_y", cursor.getString(cursor.getColumnIndex("n_jw_y")));      //经度
                object.put("n_meter_degrees", cursor.getString(cursor.getColumnIndex("this_month_end_degree")));       //本月止度
                object.put("nDosage", cursor.getString(cursor.getColumnIndex("this_month_dosage")));          //本月用量
                object.put("n_situation_operatorId", sharedPreferences_login.getString("userId", ""));       //操作员ID
                object.put("c_user_id", cursor.getString(cursor.getColumnIndex("user_id")));       //抄表用户ID
                object.put("n_stateId", cursor.getString(cursor.getColumnIndex("n_state_id")));       //是否估录   估录：1，正常录入：0
                object.put("d_jw_time", cursor.getString(cursor.getColumnIndex("meter_date")));       //抄表时间
                if (cursor.getString(cursor.getColumnIndex("n_state_id")).equals("1")) {
                    object.put("c_logRemark", "估录备注:" + cursor.getString(cursor.getColumnIndex("n_state_remark")));       //估录原因
                } else {
                    object.put("c_logRemark", "抄表录入");       //估录原因
                }
                Log.i("dataToJson==========>", "封装的json数据为：" + object.toString());
                uploadMeterData(object.toString(), cursor.getString(cursor.getColumnIndex("user_id")));
            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        isCompleted = true;
        if (!isfaild) {
            handler.sendEmptyMessage(10);
            Log.i("sendHandler==========>", "isfaild=false");
        }
        Log.i("sendHandler==========>", "handler.send(10)");
        cursor.close();
    }

    /**
     * 上传数据包含图片
     *
     * @param fileName
     */
    private void upData(final String fileName) {
        final List<MeterDataPrame> dataPrames = new ArrayList<>();
        final String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterDataTransferActivity.this)).append("meterReadingAdd.do").toString();
        uploadResultListItems.clear();
        final Cursor cursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meterState=? and uploadState=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, "true", "false"});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        uploadDataCounts = cursor.getCount();
        handler.sendEmptyMessage(12);
        file_name = fileName;
        Log.i("dataToJson", "上传总数为：" + uploadDataCounts);
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(8);
            return;
        }
        //绵竹
        if (SharedPreferencesHelper.getFirm(MeterDataTransferActivity.this).equals("绵竹")) {
            //获取序号
            mapNumber = new HashMap<>();
            while (cursor.moveToNext()) {
                try {
                    MeterDataPrame prame = new MeterDataPrame();
                    prame.setN_jw_x(cursor.getString(cursor.getColumnIndex("n_jw_x")));
                    prame.setN_jw_y(cursor.getString(cursor.getColumnIndex("n_jw_y")));
                    prame.setN_meter_degrees(cursor.getString(cursor.getColumnIndex("this_month_end_degree")));
                    prame.setnDosage(cursor.getString(cursor.getColumnIndex("this_month_dosage")));
                    prame.setMeter_degree(cursor.getString(cursor.getColumnIndex("meter_degrees"))); //上月用量 
                    prame.setN_situation_operatorId(sharedPreferences_login.getString("userId", ""));
                    prame.setC_user_id(cursor.getString(cursor.getColumnIndex("user_id")));
                    prame.setN_state_id(cursor.getString(cursor.getColumnIndex("n_state_id")));
                    prame.setD_jw_time(cursor.getString(cursor.getColumnIndex("meter_date")));
                    prame.setMt_number(cursor.getString(cursor.getColumnIndex("mt_number")));
                    prame.setBookID(cursor.getString(cursor.getColumnIndex("book_id")));//本id
                    for (BookIdBean bookIdBean : bookList) {
                        if (bookIdBean.getBookID().equals(cursor.getString(cursor.getColumnIndex("book_id")))) {
                            mapNumber.put(cursor.getString(cursor.getColumnIndex("book_id")), bookIdBean.getMeterNumber());
                            Log.e("bookId", cursor.getString(cursor.getColumnIndex("book_id")) + "==" + bookIdBean.getMeterNumber());
                        }
                    }
                    if (cursor.getString(cursor.getColumnIndex("n_state_id")).equals("1")) {
                        prame.setC_logRemark("估录备注:" + cursor.getString(cursor.getColumnIndex("n_state_remark")));
                    } else {
                        prame.setC_logRemark("抄表录入");
                    }
                    dataPrames.add(prame);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //排序
            // List<MeterDataPrame> dataPrames1=sortData(dataPrames);
            OfflineUserComparator offlineUserComparator = new OfflineUserComparator();
            Collections.sort(dataPrames, offlineUserComparator);
            int mtNumber = 0;
            for (final MeterDataPrame data : dataPrames) {
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("n_jw_x", data.getN_jw_x());      //纬度
                    map.put("n_jw_y", data.getN_jw_y());      //经度
                    map.put("n_meter_degrees", data.getN_meter_degrees());       //本月止度
                    map.put("nDosage", data.getnDosage());          //本月用量
                    map.put("n_situation_operatorId", data.getN_situation_operatorId());       //操作员ID
                    map.put("c_user_id", data.getC_user_id());       //抄表用户ID
                    map.put("n_stateId", data.getN_state_id());       //是否估录   估录：1，正常录入：0
                    map.put("d_jw_time", data.getD_jw_time());       //抄表时间
                    map.put("c_logRemark", data.getC_logRemark());   //估录原因
                    if (!data.getMt_number().equals("无")) {
                        String number = mapNumber.get(data.getBookID());
                        if (number != null) {
                            Log.e("number", number + "");
                            int numberId = Integer.parseInt(number);
                            mtNumber = numberId + 1;
                            mapNumber.put(data.getBookID(), numberId + 1 + "");
                            map.put("n_order_num1", numberId + "");   //抄表序号
                        }
                        //  bookList
                        // map.put("n_meter_degrees", data.getMeter_degree());       //本月止度
                        if (data.getnDosage().equals("")) {
                            map.put("nDosage", "0");   //本月用量
                        }
                        Log.e("时间==", data.getD_jw_time());
                        if (data.getD_jw_time().equals("暂无")) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            String meterDate = dateFormat.format(new Date());
                            map.put("d_jw_time", meterDate);       //抄表时间
                        }
                        if (data.getN_jw_x().equals("未获取")) {
                            map.put("n_jw_x", 0.0);      //纬度
                            map.put("n_jw_y", 0.0);      //经度
                        }
                    }
                    Log.i("dataToJson--", "封装的json数据为：" + map.toString());
                    setUploadMeterData(httpUrl, map, data.getC_user_id(), fileName, data.getBookID(), mtNumber, data.getMt_number());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            //川发展
            while (cursor.moveToNext()) {
                //JSONObject object = new JSONObject();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("n_jw_x", cursor.getString(cursor.getColumnIndex("n_jw_x")));      //纬度
                map.put("n_jw_y", cursor.getString(cursor.getColumnIndex("n_jw_y")));      //经度
                map.put("n_meter_degrees", cursor.getString(cursor.getColumnIndex("this_month_end_degree")));       //本月止度
                map.put("nDosage", cursor.getString(cursor.getColumnIndex("this_month_dosage")));          //本月用量
                map.put("n_situation_operatorId", sharedPreferences_login.getString("userId", ""));       //操作员ID
                map.put("c_user_id", cursor.getString(cursor.getColumnIndex("user_id")));       //抄表用户ID
                map.put("n_stateId", cursor.getString(cursor.getColumnIndex("n_state_id")));       //是否估录   估录：1，正常录入：0
                map.put("d_jw_time", cursor.getString(cursor.getColumnIndex("meter_date")));       //抄表时间
                //  map.put("c_user_remark", cursor.getString(cursor.getColumnIndex("user_remark")));//抄表备注
                if (cursor.getString(cursor.getColumnIndex("n_state_id")).equals("1")) {
                    map.put("c_logRemark", "估录备注:" + cursor.getString(cursor.getColumnIndex("n_state_remark")));       //估录原因
                } else {
                    map.put("c_logRemark", "抄表录入");  //估录原因
                }
                Log.i("dataToJson==========>", "封装的json数据为：" + map.toString());
                //uploadMeterData(object.toString(), cursor.getString(cursor.getColumnIndex("user_id")));
                setUploadMeterData(httpUrl, map, cursor.getString(cursor.getColumnIndex("user_id")));
            }
        }
        isCompleted = true;
        if (!isfaild) {
            handler.sendEmptyMessage(10);
            Log.i("sendHandler==========>", "isfaild=false");
        }
        Log.i("sendHandler==========>", "handler.send(10)");
        cursor.close();
    }


    /**
     * 上传数据包含图片
     */
    private void upDatas(final String fileName) {
        final List<MeterDataPrame> dataPrames = new ArrayList<>();
        final String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterDataTransferActivity.this)).append("meterReadingAdd.do").toString();
        uploadResultListItems.clear();
        final Cursor cursor = db.rawQuery("select * from MeterUser where login_user_id=? and file_name=? and meterState=? and uploadState=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, "true", "false"});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        uploadDataCounts = cursor.getCount();
        handler.sendEmptyMessage(12);
        file_name = fileName;
        Log.i("dataToJson", "上传总数为：" + uploadDataCounts);
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(8);
            return;
        }
        while (cursor.moveToNext()) {
            try {
                MeterDataPrame prame = new MeterDataPrame();
                prame.setN_jw_x(cursor.getString(cursor.getColumnIndex("n_jw_x")));
                prame.setN_jw_y(cursor.getString(cursor.getColumnIndex("n_jw_y")));
                prame.setN_meter_degrees(cursor.getString(cursor.getColumnIndex("this_month_end_degree")));
                prame.setnDosage(cursor.getString(cursor.getColumnIndex("this_month_dosage")));
                prame.setMeter_degree(cursor.getString(cursor.getColumnIndex("meter_degrees"))); //上月用量 
                prame.setN_situation_operatorId(sharedPreferences_login.getString("userId", ""));
                prame.setC_user_id(cursor.getString(cursor.getColumnIndex("user_id")));
                prame.setN_state_id(cursor.getString(cursor.getColumnIndex("n_state_id")));
                prame.setD_jw_time(cursor.getString(cursor.getColumnIndex("meter_date")));
                prame.setMt_number(cursor.getString(cursor.getColumnIndex("mt_number")));
                prame.setBookID(cursor.getString(cursor.getColumnIndex("book_id")));//本id
                if (cursor.getString(cursor.getColumnIndex("n_state_id")).equals("1")) {
                    prame.setC_logRemark("估录备注:" + cursor.getString(cursor.getColumnIndex("n_state_remark")));
                } else {
                    prame.setC_logRemark("抄表录入");
                }
                dataPrames.add(prame);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        //绵竹
        if (SharedPreferencesHelper.getFirm(MeterDataTransferActivity.this).equals("绵竹")) {
            //获取序号
            mapNumber = new HashMap<>();
            //排序
            // List<MeterDataPrame> dataPrames1=sortData(dataPrames);
            OfflineUserComparator offlineUserComparator = new OfflineUserComparator();
            Collections.sort(dataPrames, offlineUserComparator);
            mtNumber = 0;
            for (final MeterDataPrame data : dataPrames) {
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                            Map<String, Object> map = new HashMap<String, Object>();
                            //纬度
                            map.put("n_jw_x", data.getN_jw_x());      
                            map.put("n_jw_y", data.getN_jw_y());      //经度
                            map.put("n_meter_degrees", data.getN_meter_degrees());       //本月止度
                            map.put("nDosage", data.getnDosage());          //本月用量
                            map.put("n_situation_operatorId", data.getN_situation_operatorId());       //操作员ID
                            map.put("c_user_id", data.getC_user_id());       //抄表用户ID
                            map.put("n_stateId", data.getN_state_id());       //是否估录   估录：1，正常录入：0
                            map.put("d_jw_time", data.getD_jw_time());       //抄表时间
                            map.put("c_logRemark", data.getC_logRemark());   //估录原因
                            if (!data.getMt_number().equals("无")) {
                                String number = mapNumber.get(data.getBookID());
                                if (number != null) {
                                    Log.e("number", number + "");
                                    int numberId = Integer.parseInt(number);
                                    mtNumber = numberId + 1;
                                    mapNumber.put(data.getBookID(), numberId + 1 + "");
                                    map.put("n_order_num1", numberId + "");   //抄表序号
                                }
                                //  bookList
                                // map.put("n_meter_degrees", data.getMeter_degree());       //本月止度
                                if (data.getnDosage().equals("")) {
                                    map.put("nDosage", "0");   //本月用量
                                }
                                Log.e("时间==", data.getD_jw_time());
                                if (data.getD_jw_time().equals("暂无")) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                    String meterDate = dateFormat.format(new Date());
                                    map.put("d_jw_time", meterDate);       //抄表时间
                                }
                                if (data.getN_jw_x().equals("未获取")) {
                                    map.put("n_jw_x", 0.0);      //纬度
                                    map.put("n_jw_y", 0.0);      //经度
                                }
                            }
                            Log.i("dataToJson--", "封装的json数据为：" + map.toString());
                            setUploadMeterData(httpUrl, map, data.getC_user_id(), fileName, data.getBookID(), mtNumber, data.getMt_number());
                        }
                });
            }
        } else {
            //川发展
            index = 0;
            failCont = 0;
            for (final MeterDataPrame data : dataPrames) {
                ThreadPoolManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                            Map<String, Object> map = new HashMap<String, Object>();
                            //纬度
                            map.put("n_jw_x", data.getN_jw_x());
                            //经度
                            map.put("n_jw_y", data.getN_jw_y());
                            //本月止度
                            map.put("n_meter_degrees", data.getN_meter_degrees());
                            //本月用量
                            map.put("nDosage", data.getnDosage());
                            //操作员ID
                            map.put("n_situation_operatorId", data.getN_situation_operatorId());
                            //抄表用户ID
                            map.put("c_user_id", data.getC_user_id());
                            //是否估录   估录：1，正常录入：0
                            map.put("n_stateId", data.getN_state_id());
                            //抄表时间
                            map.put("d_jw_time", data.getD_jw_time());
                            //估录原因
//            map.put("c_logRemark", data.getC_logRemark());   
                            if (data.getN_state_id().equals("1")) {
                                //估录原因
                                map.put("c_logRemark", "估录备注:" + data.getC_logRemark());
                            } else {
                                //估录原因
                                map.put("c_logRemark", "抄表录入");
                            }
                            Log.i("dataToJson==========>", "封装的json数据为：" + map.toString());
                            setUploadMeterData(httpUrl, map, data.getC_user_id());
                        
                    }
                });

            }
        }
    }


    //上传抄表用户数据
    public void uploadMeterData(final String JsonData, String userID) {
        Log.i("dataToJson==========>", "封装的json数据为：" + JsonData + ",userId:" + userID);
        try {
            //请求的地址
            // String httpUrl = "http://" + ip + port + "/SMDemo/meterReadingAdd.do";
            String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterDataTransferActivity.this)).append("meterReadingAdd.do").toString();
            // String httpUrl = "http://" + ip + "9459/AppService/GetJsonByAjax.aspx?ajax=meterReadingAdd";
            // String httpUrl = SkUrl.SKURL;
            Log.i("httpUrl==========>", "" + httpUrl);
            // 根据地址创建URL对象
            URL url = new URL(httpUrl);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);//不使用缓存
            // 设置请求的方式发送POST请求必须设置允许输出
            urlConnection.setRequestMethod("POST");
            // 设置请求的超时时间
            urlConnection.setReadTimeout(3000);
            urlConnection.setConnectTimeout(3000);
            // 传递的数据
            // 设置请求的头
            //urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            //urlConnection.setRequestProperty("Origin", "http://"+ ip + port);
            urlConnection.setRequestProperty("Content-Length", String.valueOf(JsonData.getBytes().length));
            //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            //获取输出流
            OutputStream os = urlConnection.getOutputStream();
            os.write(JsonData.getBytes("UTF-8"));
            os.flush();
            os.close();
            Log.i("getResponseCode====>", "" + urlConnection.getResponseCode());
            if (urlConnection.getResponseCode() == 200) {
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    stringBuilder.append(str);
                }
                // 释放资源
                inputStream.close();
                // 返回字符串
                String result = stringBuilder.toString();
                Log.i("login_result=========>", result);
                if ("1".equals(result)) {    //"1" 代表上传成功
                    updateMeterUserUploadState(userID, "true");
                    currentProgress++;
                    handler.sendEmptyMessage(7);
                } else {
                    //失败
                    UploadResultListItem item = new UploadResultListItem();
                    item.setUserId(userID);
                    item.setResult(result);
                    uploadResultListItems.add(item);
                    handler.sendEmptyMessage(9);
                }

            } else {
                //返回码不是200
                Log.i("responsecode==========>", "code: " + urlConnection.getResponseCode());
                isfaild = true;
                handler.sendEmptyMessage(11);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求异常!");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        }
    }


    /**
     * 包含图片上传
     */
    private void setUploadMeterData(final String httpUrl, final Map<String, Object> map, final String userID) {

        HttpUtils.sendFilePosts(MeterDataTransferActivity.this, httpUrl, map, getPhotoData(userID), new HttpCallbackStringListener() {
            @Override
            public void onFinish(String response) {
                //成功
                Log.i("login_result", response+"=");
                //"1" 代表上传成功
                if ("1".equals(response)) {
                    updateMeterUserUploadState(userID, "true");
                    currentProgress++;
                    handler.sendEmptyMessage(7);
                } else {
                    //失败
                    UploadResultListItem item = new UploadResultListItem();
                    item.setUserId(userID);
                    item.setResult(response);
                    uploadResultListItems.add(item);
                    if (response.equals("本月已录入")) {
                        updateMeterUserUploadState(userID, "true2");
                    }
                    failCont++;
                    handler.sendEmptyMessage(9);
          

                }
                setEntUp();
            }

            @Override
            public void onError(Exception e) {
                //  isfaild = true;
                Log.i("login_result", e+"");
                handler.sendEmptyMessage(11);
          
                failCont++;
                setEntUp();

            }
        });
    }


    /**
     * 上传设置
     */
    private void setEntUp() {
        index++;
        Log.e("==",+index +"== "+(uploadDataCounts - 1));
        if (index == uploadDataCounts) {
            Log.e("===", uploadDataCounts + "");
            isCompleted = true;
            handler.sendEmptyMessage(10);

        }
    }

    /**
     * 包含图片上传
     */
    private void setUploadMeterData(final String httpUrl, final Map<String, Object> map, final String userID, final String fileName, final String bookID, final int userPosition, final String tag) {

        HttpUtils.sendFilePosts(MeterDataTransferActivity.this, httpUrl, map, getPhotoData(userID), new HttpCallbackStringListener() {
            @Override
            public void onFinish(String response) {
                //成功
                Log.i("login_result=========>", response);
                //"1" 代表上传成功
                if ("1".equals(response)) {
                    updateMeterUserUploadState(userID, fileName, bookID, userPosition, tag);
                    currentProgress++;
                    handler.sendEmptyMessage(7);
                } else {
                    //失败
                    UploadResultListItem item = new UploadResultListItem();
                    item.setUserId(userID);
                    item.setResult(response);
                    uploadResultListItems.add(item);
                    handler.sendEmptyMessage(9);
                    if (response.equals("本月已录入")) {
                        updateMeterUserUploadState(userID, "true2");
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                //  isfaild = true;
                Log.i("login_result=========>", e+"");
                handler.sendEmptyMessage(11);

            }
        });

    }


//    /**
//     * 包含图片上传
//     */
//    private void setUploadMeterDatas(final String httpUrl, final Map<String, Object> map, final String userID) {
//        HttpUtils.sendFilePosts(MeterDataTransferActivity.this, httpUrl, map, null, new HttpCallbackStringListener() {
//            @Override
//            public void onFinish(String response) {
//                //成功
//                Log.e("login_result=========>", response);
//                if ("1".equals(response)) {    //"1" 代表上传成功
//                    Log.e("成功=========>", response);
//                } else {
//                    //失败
//                    Log.e("失败=========>", response);
//                }
//            }
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
//
//    }

    /**
     * 读取本地的图片数据，并上传服务器
     *
     * @param newUserId
     * @return
     */
    public Map<String, File> getPhotoData(String newUserId) {
        Cursor cursor = db.rawQuery("select * from MeterPhoto where newUserId=? and loginUserId=?", new String[]{newUserId, sharedPreferences_login.getString("userId", "")});//查询并获得游标
        Map<String, File> fileMap = new HashMap<String, File>();
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return null;
        }
        File file = null;
        while (cursor.moveToNext()) {
            file = new File(cursor.getString(1));
            fileMap.put("file" + cursor.getPosition(), file);
        }
        Log.i("getUserData=>", "上传的照片流为：" + fileMap.size());
        cursor.close(); //游标关闭

        return fileMap;
    }

    /**
     * 更新抄表用户上传状态
     */
    private void updateMeterUserUploadState(String userID, String state) {
        ContentValues values = new ContentValues();
        values.put("uploadState", state);

        db.update("MeterUser", values, "login_user_id=? and user_id=? and file_name=?", new String[]{sharedPreferences_login.getString("userId", ""), userID, file_name});
    }

    /**
     * 绵竹更新抄表用户上传状态
     */
    private void updateMeterUserUploadState(String userID, String fileName, String bookID, int userPosition, String tag) {
        ContentValues values = new ContentValues();
        values.put("uploadState", "true");
        if (!tag.equals("无")) {
            values.put("meter_order_number", (userPosition - 1) + "");
            ContentValues values1 = new ContentValues();
            values1.put("numerical_id", (userPosition) + "");
            Log.e("number", (userPosition) + "");
            db.update("MeterNumerical", values1, "login_user_id=? and file_name=? and book_id=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游标
        }
        db.update("MeterUser", values, "login_user_id=? and user_id=?", new String[]{sharedPreferences_login.getString("userId", ""), userID});
    }

    //show获取数据加载动画
    public void showPopupwindow() {
        layoutInflater = LayoutInflater.from(MeterDataTransferActivity.this);
        loadingView = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        loadingWindow = new PopupWindow(loadingView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) loadingView.findViewById(R.id.frame_animation);
        tips = (TextView) loadingView.findViewById(R.id.tips);
        tips.setText("数据初始化中......");
        loadingWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        loadingWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loadingWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        loadingWindow.setAnimationStyle(R.style.camera);
        loadingWindow.update();
        loadingWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        startFrameAnimation();
        loadingWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //show上传数据加载动画

    /**
     * public void showUploadLoadingWindow(String fileName) {
     * layoutInflater = LayoutInflater.from(MeterDataTransferActivity.this);
     * loadingView = layoutInflater.inflate(R.layout.popupwindow_meter_upload_loading, null);
     * loadingWindow = new PopupWindow(loadingView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
     * title = (TextView) loadingView.findViewById(R.id.title);
     * frameAnimation = (ImageView) loadingView.findViewById(R.id.frame_animation);
     * resultListview = (ListView) loadingView.findViewById(R.id.result_listview);
     * progressLayout = (LinearLayout) loadingView.findViewById(R.id.progress_layout);
     * totalCount = (TextView) loadingView.findViewById(R.id.total_count);
     * ccurrentCount = (TextView) loadingView.findViewById(R.id.current_count);
     * confirm = (TextView) loadingView.findViewById(R.id.confirm);
     * line = loadingView.findViewById(R.id.line);
     * title.setText("正在上传 '" + fileName + "' 文件数据，请稍后...");
     * confirm.setOnClickListener(new View.OnClickListener() {
     *
     * @Override public void onClick(View v) {
     * loadingWindow.dismiss();
     * }
     * });
     * loadingWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
     * loadingWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
     * loadingWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
     * loadingWindow.setAnimationStyle(R.style.camera);
     * loadingWindow.update();
     * loadingWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
     * backgroundAlpha(0.6F);   //背景变暗
     * startFrameAnimation();
     * loadingWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
     * @Override public void onDismiss() {
     * backgroundAlpha(1.0F);
     * }
     * });
     * }
     */

    //show上传数据加载动画
    public void showUploadLoadingWindow(String fileName) {
        layoutInflater = LayoutInflater.from(MeterDataTransferActivity.this);
        loadingView = layoutInflater.inflate(R.layout.popupwindow_meter_upload_loading_number, null);
        loadingWindow = new PopupWindow(loadingView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        title = (TextView) loadingView.findViewById(R.id.title);
        frameAnimation = (ImageView) loadingView.findViewById(R.id.frame_animation);
        resultListview = (ListView) loadingView.findViewById(R.id.result_listview);
        progressLayout = (LinearLayout) loadingView.findViewById(R.id.progress_layout);
        totalCount = (TextView) loadingView.findViewById(R.id.total_count);
        ccurrentCount = (TextView) loadingView.findViewById(R.id.current_count);
        confirm = (TextView) loadingView.findViewById(R.id.confirm);
        line = loadingView.findViewById(R.id.line);

        title.setText("正在上传 '" + fileName + "' 文件数据，请稍后...");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingWindow.dismiss();
            }
        });
        loadingWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        loadingWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loadingWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        loadingWindow.setAnimationStyle(R.style.camera);
        loadingWindow.update();
//        loadingWindow.setFocusable(true);
//        loadingWindow.setTouchable(true);
//        loadingWindow.setOutsideTouchable(true);
        loadingWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        loadingWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
        WindowManager.LayoutParams lp = MeterDataTransferActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterDataTransferActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterDataTransferActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterDataTransferActivity.this.getWindow().setAttributes(lp);
    }

    //请求抄表本的数据
    private void requireMeterBookData(final String method, final String keyAndValue) {
        try {
            URL url;
            HttpURLConnection httpURLConnection;
            Log.i("MeterDataTransferFrag1", keyAndValue + "");
            Log.i("sharedPreferences====>", public_sharedPreferences.getString("IP", ""));

            // String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
            String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterDataTransferActivity.this)).append(method).toString();
            //有参数传递
            if (!keyAndValue.equals("")) {
                url = new URL(httpUrl + "?" + keyAndValue);
                //没有参数传递
            } else {
                url = new URL(httpUrl);
            }
            Log.i("MeterDataTransferFrag", url + "");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);
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
                resultBook = stringBuilder.toString();
                Log.i("MeterDataTransferFrag", resultBook);
                JSONArray jsonArray = new JSONArray(resultBook);
                if (jsonArray.length() != 0) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
            } else {
                handler.sendEmptyMessage(3);
            }
        } catch (UnsupportedEncodingException | MalformedURLException | JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求异常!");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        }
    }

    //请求抄表分区的数据
    private void requireMeterAreaData(final String method, final String keyAndValue) {
        try {
            URL url;
            HttpURLConnection httpURLConnection;
            Log.i("sharedPreferences====>", public_sharedPreferences.getString("IP", ""));

//           String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
            String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterDataTransferActivity.this)).append(method).toString();

            //有参数传递
            if (!keyAndValue.equals("")) {
                url = new URL(httpUrl + "?" + keyAndValue);
                //没有参数传递
            } else {
                url = new URL(httpUrl);
            }
            Log.i("MeterDataTransferFrag", url + "");
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
                resultArea = stringBuilder.toString();
                Log.i("MeterDataTransferFrag", resultArea);
                JSONArray jsonArray = new JSONArray(resultArea);
                if (jsonArray.length() != 0) {
                    handler.sendEmptyMessage(4);
                } else {
                    handler.sendEmptyMessage(5);
                }
            } else {
                handler.sendEmptyMessage(3);
            }
        } catch (UnsupportedEncodingException | MalformedURLException | JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("IOException==========>", "网络请求异常!");
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    fileAdapter = new MeterFileSelectListAdapter(MeterDataTransferActivity.this, fileList, 1);
                    fileListView.setAdapter(fileAdapter);
                    MyAnimationUtils.viewGroupOutAnimation(MeterDataTransferActivity.this, fileListView, 0.1F);
                    break;
                case 1:
                    try {
                        JSONArray jsonArray = new JSONArray(resultBook);
                        bookInfoArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            BookInfo item = new BookInfo();
                            item.setID(object.optInt("n_book_id", 0) + "");
                            item.setBOOK(object.optString("c_book_name", ""));
                            bookInfoArrayList.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    loadingWindow.dismiss();
                    Toast.makeText(MeterDataTransferActivity.this, "没有抄表本数据哦！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    loadingWindow.dismiss();
                    Toast.makeText(MeterDataTransferActivity.this, "网络请求超时！", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    try {
                        JSONArray jsonArray = new JSONArray(resultArea);
                        areaInfoArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            AreaInfo item = new AreaInfo();
                            item.setArea(object.optString("areaName", ""));
                            item.setID(object.optInt("areaId", 0) + "");
                            areaInfoArrayList.add(item);
                        }
                        if (bookInfoArrayList.size() != 0 || areaInfoArrayList.size() != 0) {
                            loadingWindow.dismiss();

                            if (choice.equals("按抄表员")) {
                            } else {
                                //移动抄表下载数据
                                Intent intent = new Intent(MeterDataTransferActivity.this, MeterDataDownloadActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelableArrayList("bookInfoArrayList", bookInfoArrayList);
                                bundle.putParcelableArrayList("areaInfoArrayList", areaInfoArrayList);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    if (bookInfoArrayList.size() != 0 || areaInfoArrayList.size() != 0) {
                        Intent intent = new Intent(MeterDataTransferActivity.this, MeterDataDownloadActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("bookInfoArrayList", bookInfoArrayList);
                        bundle.putParcelableArrayList("areaInfoArrayList", areaInfoArrayList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    loadingWindow.dismiss();
                    Toast.makeText(MeterDataTransferActivity.this, "没有抄表分区数据哦！", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    noData.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    ccurrentCount.setText(String.valueOf(currentProgress));
                    break;
                case 8:
                    title.setText("该文件没有可上传的数据哦！");
                    frameAnimation.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.GONE);
                    line.setVisibility(View.VISIBLE);
                    confirm.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    resultListview.setVisibility(View.VISIBLE);
                    adapter = new MeterUploadResulrtListAdapter(MeterDataTransferActivity.this, uploadResultListItems);
                    adapter.notifyDataSetChanged();
                    resultListview.setAdapter(adapter);
                    break;
                case 10:
                    if (isCompleted) {
                        frameAnimation.setVisibility(View.GONE);
                        if (failCont != 0) {
                            title.setText("数据上传完成，但有 " + failCont + " 个用户上传失败，原因详情如下：");
                            //  Toast.makeText(MeterDataTransferActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();
                        } else {
                            title.setText("数据上传完成！");
                        }
                        line.setVisibility(View.VISIBLE);
                        confirm.setVisibility(View.VISIBLE);
                        Toast.makeText(MeterDataTransferActivity.this, "数据上传完成！", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 11:
                    // totalCount.setText(String.valueOf(uploadDataCounts));
//                    frameAnimation.setVisibility(View.GONE);
//                    if (uploadResultListItems.size() != 0) {
//                        title.setText("数据上传完成，但有" + uploadResultListItems.size() + "个用户上传失败，原因详情如下：");
//                    } else {
//                        title.setText("数据上传失败！");
//                    }
//                    line.setVisibility(View.VISIBLE);
//                    confirm.setVisibility(View.VISIBLE);

                    break;
                case 12:
                    //总数
                    totalCount.setText(String.valueOf(uploadDataCounts));
                    //已上传
                    ccurrentCount.setText("0");
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    /**
     * 显示Dialog
     */

    private void showAlertDialog(final String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MeterDataTransferActivity.this);
        builder.setTitle("请选择");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            // 第二个参数是设置默认选中哪一项-1代表默认都不选
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                choice = items[which];
                Log.e("选择了", choice);
                if (choice != null && choice.equals("按抄表员")) {
                    startActivity(new Intent(MeterDataTransferActivity.this, MeterReaderPersonnelActivity.class));
                } else {
                    //下载
                    showPopupwindow();
                    httpMeterBookDown();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);// dialog弹出后，点击界面其他部分dialog消失
    }


    /**
     * 下载抄表本
     */
    private void httpMeterBookDown() {
        String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterDataTransferActivity.this)).append("findAllsBook.do").toString();
        Request.Builder build;
        if (sharedPreferences.getInt("all_downloads", 0) == 2) {
            build = new Request.Builder()
                    .url(httpUrl)
                    .method(HttpMethod.GET)
                    .encode("UTF-8")
                    .tag("MeterDataDown")
                    .headers("User-Agent", "sk-2.2")
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .params("companyId", String.valueOf(sharedPreferences_login.getInt("company_id", 0)))
                    .params("meterReaderId", sharedPreferences_login.getString("userId", ""))
                    .string("");
        } else {
            build = new Request.Builder()
                    .url(httpUrl)
                    .method(HttpMethod.GET)
                    .encode("UTF-8")
                    .tag("MeterDataDown")
                    .headers("User-Agent", "sk-2.2")
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .params("companyId", String.valueOf(sharedPreferences_login.getInt("company_id", 0)))
                    .string("");
        }
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                LoadMiss();
                Log.e("Meter", "onComplete/response:" + response.getBody());
                if (response.getBody() != null && response.getBody().length() != 0) {
                    //解析数据
                    try {
                        JSONArray jsonArray = new JSONArray(response.getBody());
                        bookInfoArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            BookInfo item = new BookInfo();
                            item.setID(object.optInt("n_book_id", 0) + "");
                            item.setBOOK(object.optString("c_book_name", ""));
                            String readerName = object.optString("c_meter_reader_name", "无");
                            item.setBOOKREMARK(readerName);
                            bookInfoArrayList.add(item);
                        }
                        //mReaderDataMap
//                        for (String name:arrayList) {
//                            areaInfos.clear();
//                            for (BookInfo bookInfo : bookInfoArrayList) {
//                                if (bookInfo.getBOOKREMARK().equals(name)) {
//                                    areaInfos.add(bookInfo);
//                                    Log.e("名称",name);
//                                }
//                            }
//                            Log.e("-----",new Gson().toJson(areaInfos));
//                            mReaderDataMap.put(name, areaInfos);
//                        }

//                        for (Map.Entry<String, ArrayList<BookInfo>> entry : mReaderDataMap.entrySet()) {
//                            System.out.println(entry.getKey() + "---" + new Gson().toJson(entry.getValue()));
//                        }

                        httpMeterAreaDown();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(MeterDataTransferActivity.this, "没有抄表分区数据哦！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                LoadMiss();
                Toast.makeText(MeterDataTransferActivity.this, "网络请求超时！", Toast.LENGTH_SHORT).show();
                Log.e("Meter", "onError:" + e.getMessage());
            }
        }).executeAsync();

    }


    /**
     * 下载抄表地区
     */
    private void httpMeterAreaDown() {
        String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterDataTransferActivity.this)).append("qureyAreaAll.do").toString();
        Request.Builder build;
        if (sharedPreferences.getInt("all_downloads", 0) == 2) {
            build = new Request.Builder()
                    .url(httpUrl)
                    .method(HttpMethod.GET)
                    .encode("UTF-8")
                    .tag("MeterDataDown")
                    .headers("User-Agent", "sk-2.2")
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .params("companyid", String.valueOf(sharedPreferences_login.getInt("company_id", 0)))
                    .params("meterReaderId", sharedPreferences_login.getString("userId", ""))
                    .string("");
        } else {
            build = new Request.Builder()
                    .url(httpUrl)
                    .method(HttpMethod.GET)
                    .encode("UTF-8")
                    .tag("MeterDataDown")
                    .headers("User-Agent", "sk-2.2")
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .params("companyid", String.valueOf(sharedPreferences_login.getInt("company_id", 0)))
                    .string("");
        }

        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e("Meter", "onComplete/response:" + response.getBody());
                LoadMiss();
                if (response.getBody() != null && response.getBody().length() != 0) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.getBody());
                        areaInfoArrayList.clear();
                        //循环添加
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            AreaInfo item = new AreaInfo();
                            item.setArea(object.optString("areaName", ""));
                            item.setID(object.optInt("areaId", 0) + "");
                            areaInfoArrayList.add(item);
                        }

                        if (sharedPreferences.getInt("all_downloads", 0) == 0) {
                            Intent intent = new Intent(MeterDataTransferActivity.this, MeterReaderDownloadActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bookInfoArrayList", bookInfoArrayList);
                            // bundle.putParcelableArrayList("areaInfoArrayList", areaInfoArrayList);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else {
                            //移动抄表下载数据
                            Intent intent = new Intent(MeterDataTransferActivity.this, MeterDataDownloadActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("bookInfoArrayList", bookInfoArrayList);
                            bundle.putParcelableArrayList("areaInfoArrayList", areaInfoArrayList);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(MeterDataTransferActivity.this, "没有抄表分区数据哦！", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Meter", "onError:" + e.getMessage());
                LoadMiss();
                Toast.makeText(MeterDataTransferActivity.this, "网络请求超时！", Toast.LENGTH_SHORT).show();
            }
        }).executeAsync();

    }

    private void LoadMiss() {
        if (loadingWindow != null) {
            loadingWindow.dismiss();
            loadingWindow = null;
        }
    }

//    //把assets目录下的db文件复制到dbpath下
//    public SQLiteDatabase DBManager(String packName) {
//        String dbPath = "/data/data/" + packName
//                + "/databases/" + DB_NAME;
//        if (!new File(dbPath).exists()) {
//            try {
//                FileOutputStream out = new FileOutputStream(dbPath);
//                InputStream in = this.getAssets().open(DB_NAME);
//                byte[] buffer = new byte[1024];
//                int readBytes = 0;
//                while ((readBytes = in.read(buffer)) != -1)
//                    out.write(buffer, 0, readBytes);
//                in.close();
//                out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return SQLiteDatabase.openOrCreateDatabase(dbPath, null);
//    }


    //查询抄表本信息
    private void getBookInfo(String fileName) {
        bookList = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("select * from MeterBook where login_user_id=? and fileName=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName});//查询并获得游标
        Log.i("meterHomePage", "抄表本个数为：" + cursor.getCount());
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            BookIdBean item = new BookIdBean();
            item.setBookName(cursor.getString(cursor.getColumnIndex("bookName")));
            item.setBookID(cursor.getString(cursor.getColumnIndex("bookId")));
            getMeterUserNumber(fileName, cursor.getString(cursor.getColumnIndex("bookId")), item);

        }
        cursor.close();
    }

    /**
     * 获取序号
     */
    private void getMeterUserNumber(String fileName, String bookID, BookIdBean item) {
//        select * from a,b where a.id = b.id
//        select * from a inner join b on a.id = b.id
        Cursor cursor;
        cursor = db.rawQuery("select * from MeterNumerical where login_user_id=? and file_name=? and book_id=?", new String[]{sharedPreferences_login.getString("userId", ""), fileName, bookID});//查询并获得游标
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            String number = cursor.getString(cursor.getColumnIndex("numerical_id"));
            item.setMeterNumber(number);
            bookList.add(item);
            Log.e("number", number);
        }
//     userPosition=Integer.parseInt(number);
        cursor.close();

    }

    /**
     * 排序
     */
    public class OfflineUserComparator implements Comparator {

        @Override
        public int compare(Object obj1, Object obj2) {
            MeterDataPrame user1 = (MeterDataPrame) obj1;
            MeterDataPrame user2 = (MeterDataPrame) obj2;
            // int flag = user2.getD_jw_time().compareTo(user1.getD_jw_time());
            int flag = user1.getD_jw_time().compareTo(user2.getD_jw_time());
            return flag;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoadMiss();

    }
}
