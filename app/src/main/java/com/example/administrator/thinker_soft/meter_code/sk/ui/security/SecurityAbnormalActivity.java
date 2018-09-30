package com.example.administrator.thinker_soft.meter_code.sk.ui.security;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.SecurityAbnormalAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.TextAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AetatePrame;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityChecksBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.DateFormatUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.ClearEditText;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.meter_code.sk.widget.UIHandler;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 *   安检异常
 * Created by 111 on 2018/8/2.
 */

 public class SecurityAbnormalActivity extends Activity implements  View.OnClickListener {

    private ImageView back;
    private TextView name;
    private RecyclerView recyclerView;
    private TextView noData;
    private  EditText etSearch;
    private RelativeLayout rootRelativeLayout;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private PopupWindow popupWindow;
    private SharedPreferences sharedPreferences_login;
    private SharedPreferences sharedPreferences;
    private String company_id  ,systemuserId;
    private String endTime ,statTime;
    private ClearEditText timeChoice;
    private LoadingView loadingView;
    //对象中拿到集合
    private  ArrayList<SecurityChecksBean> List = new ArrayList<>();
    private SecurityAbnormalAdapter mAdapter;
    private TextView tvTimechoice  ,tvType ,tvSearch ,tvTotalUserNumber;
    private TextAdapter adapter;
    private boolean blChek = false;
    private int maxDay;//日期
    private UIMyHandler myHandler = new UIMyHandler(this);
    private AetatePrame prame;
    private int currentPosition;
    private String UserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_abnormal);
        initView();
    }


    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        noData = (TextView) findViewById(R.id.no_data);
        timeChoice = (ClearEditText) findViewById(R.id.et_content);
        tvTimechoice = (TextView) findViewById(R.id.tv_time_choice);
        etSearch = (EditText) findViewById(R.id.etSearch);
        rootRelativeLayout = (RelativeLayout)findViewById(R.id.root_relativelayout);

        tvType = (TextView) findViewById(R.id.tv_type);
        tvSearch = (TextView) findViewById(R.id.tv_search);

        tvTotalUserNumber = findViewById(R.id.tv_total_user_number);

        defaultSetting();//初始化设置
//        setOnClickListener();//点击事件

    }

    private void defaultSetting() {
        back.setOnClickListener(this);
        //获取用户id
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        systemuserId = sharedPreferences_login.getString("userId", ""); // 安检员的id
        UserName = sharedPreferences_login.getString("user_name",""); // 安检员的名称
          company_id = String.valueOf(sharedPreferences_login.getInt("company_id", 0));//公司id
//        company_id="26";
        endTime = DateFormatUtil.getCurrentTime();
        statTime = DateFormatUtil.DateToString(DateFormatUtil.getDateBefore(DateFormatUtil.StringToDate(endTime),7));
        tvTimechoice.setText(new StringBuffer().append(statTime).append("至").append(endTime).toString());
        prame = new AetatePrame();
        prame.setN_company_id(company_id);
//        prame.setEndtime(endTime);
//        prame.setStarttime(statTime);
        SecurityAbnormalRequest(prame);

        tvType.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        tvTimechoice.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_type:
                //类型选择
                showListWindow();
                break;
            case R.id.tv_time_choice:
                //选择时间
                showTimeWindow("选择开始时间");
                blChek = false;
                break;
            case R.id.tv_search:
                AetatePrame prame=new AetatePrame();
                prame.setN_company_id(company_id);//公司id
                //搜索
                String strType=tvType.getText().toString().trim();
                switch (strType){
                    case "安检日期":
                        prame.setEndtime(endTime);
                        prame.setStarttime(statTime);
                        break;
                    case "姓名":
                        prame.setC_user_name(timeChoice.getText().toString().trim());
                        break;
                    case "用户编号":
                        prame.setC_user_id(timeChoice.getText().toString().trim());
                        break;
                    case "表编号":
                        prame.setC_old_user_id(timeChoice.getText().toString().trim());
                        break;
                    case "地址":
                        prame.setC_user_address(timeChoice.getText().toString().trim());
                        break;
                        default:
                            break;
                }
                SecurityAbnormalRequest(prame);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 9) {
                Log.i("UserList=ActivityResult", "页面回调进来了");

                if (data!=null){
                    String isUp=data.getStringExtra("isUpload");
                    Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
                    if (isUp!=null && isUp.equals("成功")){
                    }
                }
                SecurityAbnormalRequest(prame);
            }
        }
    }

    /**
     *网络请求
     * @param prame
     */
    private void SecurityAbnormalRequest(AetatePrame prame){
        //加载
        loadingView = new LoadingView(SecurityAbnormalActivity.this, R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();
        final String httpUrl = new StringBuffer().append(SkUrl.SkHttp(SecurityAbnormalActivity.this)).append("getSecurityCheckAbnormal.do").toString();
        Log.e("pgl","==="+httpUrl);
        //   http://88.88.88.251:8082/SMDemo/getSecurityCheckAbnormal.do
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("aerate")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("n_company_id", prame.getN_company_id())
                .params("n_abnormal_member", systemuserId)
                .params("n_abnormal_username", SkUrl.toURLEncoded(UserName))
                .params("c_user_id", prame.getC_user_id()==null?"":prame.getC_user_id())
                .params("c_old_user_id", prame.getC_old_user_id()==null?"":prame.getC_old_user_id())
                .params("c_user_name", prame.getC_user_name()==null?"":SkUrl.toURLEncoded(prame.getC_user_name()))
                .params("starttime", prame.getStarttime()==null?"":prame.getStarttime())
                .params("endtime", prame.getEndtime()==null?"":prame.getEndtime())
                .params("c_user_address", prame.getC_user_address()==null?"":prame.getC_user_address())
                .string("");

        Log.e("pgl", "===   n_company_id=" + prame.getN_company_id());
        Log.e("pgl", "===httpUrl=" + build.toString());
        Log.e("pgl", "===   开始时间=" + prame.getStarttime());
        Log.e("pgl","===:结束时间=" + prame.getEndtime());
        /*
            params.put("n_company_id", (String)request.getParameter("n_company_id"));//'公司编号'
			params.put("c_user_id", (String)request.getParameter("c_user_id"));//'用户编号'
			params.put("c_old_user_id", (String)request.getParameter("c_old_user_id"));//'老编号'
			params.put("c_user_name", (String)request.getParameter("c_user_name"));//'%用户地址%'
			params.put("starttime", (String)request.getParameter("starttime"));//安检开始时间
			params.put("endtime", (String)request.getParameter("endtime"));//安检结束时间
        * */
        Request.newRequest(build, new HttpCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(Response response) {
                Log.e("pgl","==="+response.getBody());
                //GSON直接解析成对象
                SecurityChecksBean resultBean = new Gson().fromJson(response.getBody(),SecurityChecksBean.class);
             Log.d("pgl","=== securityAbnormalAdapter"+"执行了"+new Gson().toJson(resultBean.getList()));
                if ("查询成功".equals(resultBean.getMsg())){
                    if (resultBean.getList().size()>0){
                        noData.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(SecurityAbnormalActivity.this, LinearLayoutManager.VERTICAL, false));
                        List.clear();
                        List.add(0,resultBean);
                        //初始化适配器
                        mAdapter = new SecurityAbnormalAdapter(List);
                        //设置适配器
                        recyclerView.setAdapter(mAdapter);
                        loadingView.dismiss();
                        //设置添加或删除item时的动画，这里使用默认动画
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        tvTotalUserNumber.setText(resultBean.getList().size()+"");
                        mAdapter.setOnItemClickListener(new SecurityAbnormalAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, SecurityChecksBean.ListBean data) {
                                currentPosition = position;
                                Intent intent = new Intent(SecurityAbnormalActivity.this,  SecurityAbnormalInfoActivity.class);
                                intent.putExtra("listBean",data);
                                startActivityForResult(intent, 9);
                            }
                        });
                    }else {
                        loadingView.dismiss();
                        Toast.makeText(SecurityAbnormalActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        noData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }else {
                    noData.setVisibility(View.VISIBLE);
                    loadingView.dismiss();
                    recyclerView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(Throwable e) {
                noData.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                Log.d("pgl","==="+e.getMessage());
                Toast.makeText(SecurityAbnormalActivity.this, "网络请求异常", Toast.LENGTH_SHORT).show();
                loadingView.dismiss();
            }
        }).executeAsync();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 类型选择
     */
    private void showListWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(SecurityAbnormalActivity.this);
        View popView = layoutInflater.inflate(R.layout.popupwindow_text, null);
        final PopupWindow popListWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ListView listView = (ListView) popView.findViewById(R.id.listView);
        setData(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) adapter.getItem(position);
                tvType.setText(str);
                //开户日期
                if (str.equals("安检日期")) {
                    tvTimechoice.setVisibility(View.VISIBLE);
                    timeChoice.setVisibility(View.GONE);
                } else {
                    tvTimechoice.setVisibility(View.GONE);
                    Log.e("pgl","===  timeChoice");
                    timeChoice.setVisibility(View.VISIBLE);
                }

                popListWindow.dismiss();
            }
        });
        popListWindow.update();
        popListWindow.setFocusable(true);
        popListWindow.setTouchable(true);
        popListWindow.setOutsideTouchable(true);
        popListWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popListWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        PopWindowUtil.backgroundAlpha(SecurityAbnormalActivity.this, 0.6F);   //背景变暗
        popListWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        popListWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SecurityAbnormalActivity.this, 1.0F);
            }
        });

    }

    /**
     * 添加数据
     */
    private void setData(ListView listView) {
        String[] array = new String[]{"安检日期", "姓名", "用户编号", "表编号", "地址"};
        adapter = new TextAdapter(this,array);
//        adapter.addAll(Arrays.asList(array));
        listView.setAdapter(adapter);

    }


    //show弹出框
    public void showPopupwindow(){
        LayoutInflater layoutInflater = LayoutInflater.from(SecurityAbnormalActivity.this);
        View view = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) view.findViewById(R.id.frame_animation);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        //popupWindow.update();
//        popupWindow.showAtLocation(rootRelativeLayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        startFrameAnimation();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = SecurityAbnormalActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            SecurityAbnormalActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            SecurityAbnormalActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        SecurityAbnormalActivity.this.getWindow().setAttributes(lp);
    }
    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }

    /**
     * 时间选择
     */
    public void showTimeWindow(String strTitle) {
        LayoutInflater layoutInflater = LayoutInflater.from(SecurityAbnormalActivity.this);
        View mView = layoutInflater.inflate(R.layout.view_date_dialog, null);
        final PopupWindow popWindow = new PopupWindow(mView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        final TextView title = (TextView) mView.findViewById(R.id.tips);
        title.setText(strTitle);
        final RadioButton back = (RadioButton) mView.findViewById(R.id.rd_cancel);
        final RadioButton confirm = (RadioButton) mView.findViewById(R.id.rd_ok);
        final NumberPicker np1 = (NumberPicker) mView.findViewById(R.id.np1);
        final NumberPicker np2 = (NumberPicker) mView.findViewById(R.id.np2);
        final NumberPicker np3 = (NumberPicker) mView.findViewById(R.id.np3);

        //获取当前日期
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH) + 1;//月份是从0开始算的
        final int day = c.get(Calendar.DAY_OF_MONTH);

        //设置年份
        np1.setMaxValue(2999);
        np1.setValue(year); //中间参数 设置默认值
        np1.setMinValue(1900);

        //设置月份
        np2.setMaxValue(12);
        np2.setValue(month);
        np2.setMinValue(1);

        //设置天数
        np3.setMaxValue(31);
        np3.setValue(day);
        np3.setMinValue(1);

        //年份滑动监听
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.i("NumberPicker", "oldVal-----" + oldVal + "-----newVal-----" + newVal);
                //平年闰年判断
                if (newVal % 4 == 0) {
                    maxDay = 29;
                } else {
                    maxDay = 28;
                }
                //设置天数的最大值
                np3.setMaxValue(maxDay);
            }
        });

        //月份滑动监听
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.i("NumberPicker", "oldVal-----" + oldVal + "-----newVal-----" + newVal);
                //月份判断
                switch (newVal) {
                    case 2:
                        if (np1.getValue() % 4 == 0) {
                            maxDay = 29;
                        } else {
                            maxDay = 28;
                        }
                        break;
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        maxDay = 31;
                        break;
                    default:
                        maxDay = 30;
                        break;
                }
                //设置天数的最大值
                np3.setMaxValue(maxDay);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int years = np1.getValue();
                int months = np2.getValue();
                int days = np3.getValue();
                String month1;
                String day1;
                if (months <10 ){
                    month1 = "0"+months;
                }else {
                    month1 =  months+"";
                }
                if (days <10 ){
                    day1 = "0"+days;
                }else {
                    day1 = ""+days;
                }
                if (!blChek) {
                    popWindow.dismiss();
                    myHandler.sendEmptyMessage(0);
                    blChek = true;
                    statTime = years + "-" + month1 + "-" + day1;
                    tvTimechoice.setText(statTime);
                } else {
                    endTime = years + "-" + month1 + "-" + day1;
                    tvTimechoice.setText(statTime + "至" + endTime);
                    //范围
                    if (checkInput()){
                        popWindow.dismiss();
                        AetatePrame prame=new AetatePrame();
                        prame.setN_company_id(company_id);
                        prame.setStarttime(statTime);
                        prame.setEndtime(endTime);
                        SecurityAbnormalRequest(prame);
                    }

                }

            }
        });
        popWindow.update();
        popWindow.setFocusable(true);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        PopWindowUtil.backgroundAlpha(SecurityAbnormalActivity.this, 0.6F);   //背景变暗
        popWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SecurityAbnormalActivity.this, 1.0F);
            }
        });
    }

    /**
     * 时间间隔
     * @return
     */
    public boolean checkInput() {
        if (TextUtils.isEmpty(statTime)) {
            Toast.makeText(SecurityAbnormalActivity.this, "请输入开始时间~", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (TextUtils.isEmpty(endTime)) {
            Toast.makeText(SecurityAbnormalActivity.this, "请输入结束时间~", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dateStart = dateFormat.parse(statTime);
            Date dateEnd = dateFormat.parse(endTime);
            if (dateStart.getTime() > dateEnd.getTime()) {
                Toast.makeText(SecurityAbnormalActivity.this, "开始时间需要小于结束时间哦~~",
                        Toast.LENGTH_SHORT).show();

                return false;
            } else if (dateStart.getTime() > new Date().getTime()) {
                Toast.makeText(SecurityAbnormalActivity.this, "开始时间大于现在时间哦~~",
                        Toast.LENGTH_SHORT).show();
                return false;
            } else if (dateEnd.getTime() > new Date().getTime()) {
                Toast.makeText(SecurityAbnormalActivity.this, "结束时间大于现在时间哦~~",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(SecurityAbnormalActivity.this, "数据格式有误！", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;

    }

    /**
     * Handler
     */
    private static class UIMyHandler extends UIHandler<SecurityAbnormalActivity> {

        public UIMyHandler(SecurityAbnormalActivity cls) {
            super(cls);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SecurityAbnormalActivity activity = ref.get();
            if (activity != null) {
                if (activity.isFinishing())
                    return;
                switch (msg.what) {
                    case 0:
                        activity.showTimeWindow("选择结束时间");
                        break;
                    case 1:
                        //选择时间
                        activity.showTimeWindow("选择开始时间");
                        activity.blChek = false;
                        break;

                }
            }
        }
    }


}
