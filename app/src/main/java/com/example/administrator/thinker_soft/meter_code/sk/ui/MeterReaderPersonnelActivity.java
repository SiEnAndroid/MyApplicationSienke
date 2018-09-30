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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.AreaDataAdapter;
import com.example.administrator.thinker_soft.meter_code.model.AreaInfo;
import com.example.administrator.thinker_soft.meter_code.sk.bean.MeterDataPrame;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 抄表员列表
 * Created by Administrator on 2018/5/17.
 */

public class MeterReaderPersonnelActivity extends AppCompatActivity {
    private Unbinder mUnbinder;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    /**
     * 加载进度
     */
    private LoadingView loadingView;
    /**
     * 数据库
     */
    private SQLiteDatabase db;
    /**
     * 本地储存
     */
    private SharedPreferences public_sharedPreferences, sharedPreferences_login;
    /**
     * 抄表员
     */
    private AreaDataAdapter areaAdapter;

    /**
     * 设置默认值
     */
    private int lastposition = 0;
    /**
     * 抄表id
     */
    private String mReaderId;
    /**
     * 保存数据
     */
    private LayoutInflater layoutInflater;
    private View fileNameView, view, line;
    private PopupWindow fileNamePopup, loadingPopup;
    private EditText fileNameEdit;
    private TextView title, tips, confirm, ensure;  //加载进度的提示
    private Map<String, String> bookMap = new HashMap<>();
    private JSONObject userObject;     //用户object对象
    private JSONArray userJsonArray;   //用户JsonArray对象

    @BindView(R.id.listView_meter)
    ListView mListView;
    @BindView(R.id.tv_book_data)
    TextView noData;//空数据
    @BindView(R.id.btn_person_down)
    Button personDown;//下载
    @BindView(R.id.meter_layout)
    LinearLayout layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_personnel);
        //绑定
        mUnbinder = ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        MySqliteHelper helper = new MySqliteHelper(MeterReaderPersonnelActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        public_sharedPreferences = MeterReaderPersonnelActivity.this.getSharedPreferences("data", Context.MODE_PRIVATE);
        //加载
        loadingView = new LoadingView(MeterReaderPersonnelActivity.this, R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();
//        ThreadPoolManager.getInstance().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(500);
//                    //getData();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        getData();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastposition!=position) {
                    //单选
                    AreaDataAdapter.getIsCheck().put(position, !AreaDataAdapter.getIsCheck().get(position));
                    AreaDataAdapter.getIsCheck().put(lastposition, false);
                    mReaderId = areaAdapter.getList().get(position).getID();
                    Log.e("index", lastposition + "===" + position + "---" + mReaderId);
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
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (db != null) {
            db.close();
        }
        if (loadingView != null) {
            loadingView.dismiss();
        }
    }

    private void updateView(int itemIndex) {
        //得到第一个可显示控件的位置，
        int visiblePosition = mListView.getFirstVisiblePosition();
        //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
        if (itemIndex - visiblePosition >= 0) {
            //得到要更新的item的view
            View view = mListView.getChildAt(itemIndex - visiblePosition);
            //调用adapter更新界面
            areaAdapter.updateView(view, itemIndex);
        }
    }

    /**
     * 监听
     *
     * @param view
     */
    @OnClick({R.id.back, R.id.clear, R.id.btn_person_down})
    public void onClickPerson(View view) {

        switch (view.getId()) {
            case R.id.back://返回
                finish();
                break;
            case R.id.clear://清除
                db.delete("MeterUser", null, null);  //删除User表中当前用户的所有数据（官方推荐方法）
                db.delete("MeterFile", null, null);
                db.delete("MeterBook", null, null);
           //     db.delete("MeterNumerical", null, null);
                db.delete("MeterPhoto", null, null);
                //设置id从1开始（sqlite默认id从1开始），若没有这一句，id将会延续删除之前的id
                db.execSQL("update sqlite_sequence set seq=0 where name='MeterUser'");
                db.execSQL("update sqlite_sequence set seq=0 where name='MeterFile'");
                db.execSQL("update sqlite_sequence set seq=0 where name='MeterBook'");
//                db.execSQL("update sqlite_sequence set seq=0 where name='MeterNumerical'");
                db.execSQL("update sqlite_sequence set seq=0 where name='MeterPhoto'");
                Toast.makeText(MeterReaderPersonnelActivity.this, "清除数据成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_person_down://下载
                Log.e("选择的id", mReaderId);
                getParamsAndLoading();
                String params = "u.n_user_meter_reader_id=" + mReaderId;
                try {
                    params = URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20");
                    setParamsData(params);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 获取抄表员
     */
    private void getData() {
        String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterReaderPersonnelActivity.this)).append("getMeterReaderName.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("MeterDataDowns")
                .headers("User-Agent", "sk-2.2")
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .params("meterReaderId", "10")
                .string("");
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                loadingView.dismiss();
                Log.e("Meter", "onComplete/response:" + response.getBody());
                // MeterReaderPersonBean personBean=fromToJson(response.getBody());
                try {
                    JSONObject jsonObject = new JSONObject(response.getBody());
                    JSONArray jsonArray = new JSONArray(jsonObject.optString("list"));
                    if (jsonArray.length() > 0) {
                        List<AreaInfo> areaInfos1 = new ArrayList<>();//抄表员列表
                        //添加数据
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AreaInfo areaInfo = new AreaInfo();
                            areaInfo.setArea(jsonArray.optJSONObject(i).optString("cMeterReaderName", ""));
                            areaInfo.setID(String.valueOf(jsonArray.optJSONObject(i).optInt("nMeterReaderId", 0)));
                            areaInfos1.add(areaInfo);//添加抄表员
                        }
                        //  添加adapter
                        areaAdapter = new AreaDataAdapter(MeterReaderPersonnelActivity.this, areaInfos1);
                        mListView.setAdapter(areaAdapter);
                        //是否有数据
                        if (areaAdapter.getList().get(0).getArea().equals("无")) {
                            noData.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                            personDown.setVisibility(View.GONE);
                        } else {
                            if (areaAdapter.getList().size() > 0) {
                                AreaDataAdapter.getIsCheck().put(0, true);
                                mReaderId = areaAdapter.getList().get(0).getID();
                            }
                        }
                        //设置动画
                        MyAnimationUtils.viewGroupOutAnimation(MeterReaderPersonnelActivity.this, mListView, 0.1F);

                    } else {
                        noData.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                        personDown.setVisibility(View.GONE);
                        Toast.makeText(MeterReaderPersonnelActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    noData.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    personDown.setVisibility(View.GONE);
                    Toast.makeText(MeterReaderPersonnelActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                loadingView.dismiss();
                noData.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                personDown.setVisibility(View.GONE);
                Toast.makeText(MeterReaderPersonnelActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Log.e("Meter", "onError:" + e.getMessage());
            }
        }).executeAsync();

    }


    private void getParamsAndLoading() {
        showLoadingPopup();
        tips.setText("正在获取用户数据，请稍后...");
    }

    /**
     * 下载用户数据
     */
    private void setParamsData(String string) {
        String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterReaderPersonnelActivity.this)).append("findUserMeter.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("MeterDataDown")
                .headers("User-Agent", "sk-2.2")
                //  .headers("Content-Type", "application/x-www-form-urlencoded")
                .params("param1", string)
                .string("");
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e("Meter", "onComplete/response:" + response.getBody());
                try {
                    userJsonArray = new JSONArray(response.getBody());
                    if (userJsonArray.length() != 0) {
                        handler.sendEmptyMessage(5);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (loadingPopup != null) {
                    loadingPopup.dismiss();

                }
                Toast.makeText(MeterReaderPersonnelActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Log.e("Meter", "onError:" + e.getMessage());
            }
        }).executeAsync();

    }


    //加载进度动画
    public void showLoadingPopup() {
        layoutInflater = LayoutInflater.from(MeterReaderPersonnelActivity.this);
        view = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
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
        loadingPopup.showAtLocation(layout, Gravity.CENTER, 0, 0);
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
        layoutInflater = LayoutInflater.from(MeterReaderPersonnelActivity.this);
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
//                                    List<JSONObject> Jsonlist = new ArrayList<JSONObject> ();
//                                    JSONObject jsonObj = null;
//                                    for (int i = 0; i < userJsonArray.length(); i++) {
//                                        jsonObj = userJsonArray.optJSONObject(i);
//                                        Jsonlist.add(jsonObj);
//                                    }
                                    //排序操作
//                                    JsonComparator pComparator =  new JsonComparator("");
//                                    Collections.sort(Jsonlist,pComparator);
                                    // Collections.sort(userJsonArray);//排序

                                    for (int i = 0; i < userJsonArray.length(); i++) {
                                        //  try {
                                        userObject = userJsonArray.optJSONObject(i);
                                        insertMeterUserData(i + 1);   //将抄表用户数据插入本地数据库
                                        bookMap.put(userObject.optInt("n_book_id", 0) + "", userObject.optString("c_book_name", ""));


//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                       }
                                    }
                                    for (String bookID : bookMap.keySet()) {
                                        insertMeterBook(bookID, bookMap.get(bookID));   //将抄表本数据保存至本地 MeterBook 表
                                        insertMeterUserNumber(bookID, bookMap.get(bookID));
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
                    Toast.makeText(MeterReaderPersonnelActivity.this, "文件名不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fileNamePopup.update();
        fileNamePopup.setFocusable(true);
        fileNamePopup.setOutsideTouchable(true);
        fileNamePopup.setBackgroundDrawable(getResources().getDrawable(R.drawable.business_check_shape));
        fileNamePopup.setAnimationStyle(R.style.camera);
        fileNamePopup.showAtLocation(layout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        fileNamePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
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
                    Toast.makeText(MeterReaderPersonnelActivity.this, "没有抄表用户数据哦！", Toast.LENGTH_SHORT).show();
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
//
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
                    if (loadingPopup != null) {
                        loadingPopup.dismiss();
                    }
                    showNameFilePopup();  //用户给下载文件命名
                    break;
                case 7:
                    if (loadingPopup != null) {
                        loadingPopup.dismiss();
                    }
                    Toast.makeText(MeterReaderPersonnelActivity.this, "该文件名已存在，请您重新命名！", Toast.LENGTH_SHORT).show();
                    showNameFilePopup();
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
        values.put("mt_number", i);                                                             //序号设置
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
        values.put("this_month_dosage", "");                                                //本月用量
        values.put("this_month_end_degree", "");                                            //本月止度
        values.put("n_jw_x", "未获取");                                                      //纬度
        values.put("n_jw_y", "未获取");                                                      //经度
        values.put("d_jw_time", "未获取");                                                   //抄表时间
        values.put("n_state_remark", "");                                                    //抄表时间

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
     * 获取序号保存至本地 MeterNumerical 表
     */
    private void insertMeterUserNumber(String bookID, String bookName) {
        ContentValues values = new ContentValues();
        values.put("book_id", bookID);                                                             //抄表本ID
        values.put("book_name", bookName);                                                         //抄表本名称
        values.put("file_name", fileNameEdit.getText().toString().trim());                                //抄表文件名称
        values.put("login_user_id", sharedPreferences_login.getString("userId", ""));             //当前登录人的ID
        values.put("numerical_id", "1");
        Log.e("文件名称", fileNameEdit.getText().toString());
        db.insert("MeterNumerical", null, values);
    }

    /**
     *将抄表文件数据保存至本地 MeterFile 表
     */
    private void insertMeterFile() {
        ContentValues values = new ContentValues();
        values.put("fileName", fileNameEdit.getText().toString());                                //抄表文件名称
        values.put("login_user_id", sharedPreferences_login.getString("userId", ""));             //当前登录人的ID
        values.put("login_user_name", sharedPreferences_login.getString("user_name", ""));        //当前登录人的名称
        db.insert("MeterFile", null, values);
    }

    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MeterReaderPersonnelActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterReaderPersonnelActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterReaderPersonnelActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterReaderPersonnelActivity.this.getWindow().setAttributes(lp);
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

    //根据泛型返回解析制定的类型
    public <T> T fromToJson(String json) {
        Type type = new TypeToken<T>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

}
