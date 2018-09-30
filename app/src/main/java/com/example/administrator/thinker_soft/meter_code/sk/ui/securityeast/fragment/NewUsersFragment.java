package com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.NewUsersAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.TextAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NewUsersBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NewUsersPrame;
import com.example.administrator.thinker_soft.meter_code.sk.db.DBManage;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseFragment;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.DateFormatUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.JsonAnalyUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PopWindowUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtil;
import com.example.administrator.thinker_soft.meter_code.sk.widget.ClearEditText;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新增用户
 * @author 111
 */
public class NewUsersFragment extends BaseFragment implements NewUsersAdapter.OnItemClickListener, OnLoadmoreListener, OnRefreshListener {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_search)
    ClearEditText etSearch;
    @BindView(R.id.tv_type)
    TextView type;//类型
    @BindView(R.id.no_data)
    LinearLayout noData;//空数据
    @BindView(R.id.tv_time_choice)
    TextView tvTimeChoice;
    @BindView(R.id.tv_total_user_number)
    TextView totalNumber;//条数
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;//刷新控件


    private int page=1;//页数
    private int count=50;//条数
    private SharedPreferences sharedPreferences_login;
    private String userID;
    private String login_name;
    private SharedPreferences sharedPreferences;
    private String company_id;
    private String endTime ,statTime;
    private String noTask="";
    /**
     * 新增用户选中的数据
     */
    private static  List<NewUsersBean.ListBean> datas = new ArrayList<>();
    private NewUsersAdapter newUsersAdapter;
    private NewUsersPrame prame;
    private LoadingView loadingView;
    /**
     * 新增用户下拉的数据
     */
    private  List<NewUsersBean.ListBean> newUsersList = new ArrayList<>();
    private TextAdapter adapter;

    private boolean blChek = false;
    private int maxDay;
    private FragmentActivity activity1;
    private String tyID ="";
    /**
     * 下载经度更新
     */
    private LinearLayout linearlayoutDown;
    private ProgressBar progressBar;
    private TextView progressName,progressPercent;
    private Button finishBtn;
    private int currentProgress = 0;
    private int currentPercent = 0;


    @Override
    protected int getContentViewID() {
        return R.layout.fragment_new_users;
    }

    @Override
    protected void initView() {
        activity1 = getActivity();
        bindView();
        defaultSetting();
        setViewClickListener();
    }

    @Override
    public void onLazyLoad() {

    }

    private void bindView() {

    }

    private void defaultSetting() {
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        userID = sharedPreferences_login.getString("userId", "");
        login_name = sharedPreferences_login.getString("login_name", "");
        sharedPreferences =getActivity().getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        //公司id
        company_id = String.valueOf(sharedPreferences_login.getInt("company_id", 0));
        if (!"".equals(sharedPreferences.getString("currentTaskName",""))) {
            tyID = sharedPreferences.getString("currentTaskId", "");
            noTask ="YesTask";
        }


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newUsersAdapter = new NewUsersAdapter(newUsersList);
        newUsersAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(newUsersAdapter);
        mRecyclerView.requestFocus();

        mRefreshLayout.setRefreshHeader(new WaterDropHeader(getActivity()));
        mRefreshLayout.setPrimaryColorsId(R.color.theme_colors, android.R.color.white);
        mRefreshLayout.setDisableContentWhenRefresh(true);  //是否在刷新的时候禁止内容的一切手势操作（默认false）
        mRefreshLayout.setDisableContentWhenLoading(true);  //是否在加载的时候禁止内容的一切手势操作（默认false）
        mRefreshLayout.setOnLoadmoreListener(this);//上拉加载
        mRefreshLayout.setOnRefreshListener(this);//下拉刷新

        initRequest();

    }

    private void initRequest() {
        endTime = DateFormatUtil.getCurrentTime();
        statTime = DateFormatUtil.DateToString(DateFormatUtil.getDateBefore(DateFormatUtil.StringToDate(endTime),7));
        tvTimeChoice.setText(new StringBuffer().append(statTime).append("至").append(endTime).toString());
        prame = new NewUsersPrame();
        prame.setN_company_id(company_id);
        prame.setEndtime(endTime);
        prame.setStarttime(statTime);
        httoYearNewRequest(prame,page,count);
    }


    private void setViewClickListener() {

    }



    /**
     * 监听
     * @param view
     */
    @OnClick({R.id.tv_search,R.id.tv_type,R.id.tv_time_choice,R.id.tv_select_all,R.id.tv_reverse,R.id.tv_select_cancel})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                datas.clear();
                //搜索
                setsearch();
                break;
            case R.id.tv_select_all:
                //全选
                all();
                break;
            case R.id.tv_reverse:
                //反选
                inverse();
                break;
            case R.id.tv_select_cancel:
                //取消
//                cancel();
                //   新增  上传
                //新增
                if (noTask.equals("YesTask")){
                    if(datas!=null&&datas.size()>0){
                        //新增 人员上传 网络请求
                        AddPeopleRequest();
                    }else {
                        ToastUtil.showShort(getActivity(),"请选择新增人员");
                    }

                }else {
                    ToastUtil.showShort(getActivity(),"抱歉!,本地还没有任务请先下载");
                }
                break;
            case R.id.tv_type:
                //类型选择
                showListWindow();
                break;
            case R.id.tv_time_choice:
                //选择时间
                blChek = false;
                showTimeWindow("选择开始时间");
                break;
            default:
                break;
        }
    }

    /**
     * 上传 新增用户
     */
    private void AddPeopleRequest() {
        //加载
        loadingView = new LoadingView(getActivity(), R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();

        String  httpUrl = new StringBuffer().append(SkUrl.SkHttp(getActivity())).append("insertDyXzyh.do").toString();
        /*
         上传数据:
		c_user_id                    用户id               必传
		c_anjian_inspection_member   安检员               不必传
		n_anjian_plan                计划编号             不必传
		c_anjian_remark              安检备注             不必传
		*/
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.POST)
                .encode("UTF-8")
                .tag("add")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("c_user_id",userID)
                .params("c_anjian_inspection_member",login_name)
                .params("n_anjian_plan", tyID)
                .params("c_anjian_remark","")
                .string("");
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response)  {
                Log.e("NewUsersFragment", "onComplete/response:" + response.getBody());
                Log.e("NewUsersFragment", "onComplete/response: content type=" + response.getContentType());
                loadDiss();
                // {"msg":"新增成功","list":[],"status":"success"}

                try {
                    JSONObject jsonObject=new JSONObject(response.getBody());
                    String success= jsonObject.optString("status","");
                    if (success.equals("success")){
                        //  ToastUtil.showShort(SecurityAddedActivity.this,"新增成功");
                        showPopupwindow();
                        page=1;
                        count=50;
                        newUsersAdapter.clear();
                        datas.clear();
                        newUsersAdapter.notifyDataSetChanged();
                        // httoYearRequest("2",page,count);//年计划内
                        setsearch();
                        setSafety(tyID);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(getActivity(),"新增失败");
                }


            }

            @Override
            public void onError(Throwable e) {
                Log.e("NewUsersFragment", "onError:" + e.getMessage());
                loadDiss();
                ToastUtil.showShort(getActivity(),"新增失败");

            }
        }).executeAsync();
    }

    private void loadDiss() {
        if (loadingView != null) {
            loadingView.dismiss();
        }
    }


    /**
     * 根据任务ID查询任务编号
     */
    private void setSafety(String resultTaskId){
        String httpUrl=new StringBuffer().append(SkUrl.SkHttp(getActivity())).append("getUserCheck.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("add")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("safetyPlan",resultTaskId)
                .params("resultTaskId","0")
                .string("");
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response)  {
                Log.e("NewUsersFragment", "onComplete/response:" + response.getBody());
                Log.e("NewUsersFragment", "onComplete/response: content type=" + response.getContentType());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.getBody());
                    if (!jsonObject.optString("total", "").equals("0")) {
                        // selectDatas
                        JSONArray array = jsonObject.getJSONArray("rows");
                        //是否有数据
                        if (array.length()>0){
                            setProgress();
                        }
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject   object = array.getJSONObject(i);
                            Log.e("josn",new Gson().toJson(datas));

                            for (NewUsersBean.ListBean NewUsersList :datas){
                                Log.e("pgl",  NewUsersList.getUserName());
                                if (object.optString("userName","").equals(NewUsersList.getUserName())){
                                    //插入本地数据
                                    DBManage.getInstance(getActivity()).insertUser(object,tyID,login_name,userID);
                                    DBManage.getInstance(getActivity()).updateTaskInfo(tyID,userID,1);
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("NewUsersFragment", "onError:" + e.getMessage());
            }
        }).executeAsync();
    }

    public void setProgress() {
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 5; i++) {
                        Thread.sleep(300);
                        currentProgress += 10 * 5 / 5;
                        currentPercent = (1000 * (i + 1)) / (10 * 5);
                        Message msg = new Message();
                        msg.what = 10;
                        msg.arg1 = currentProgress;
                        msg.arg2 = currentPercent;
                        handler.sendMessage(msg);
                        Log.i("upload_task_progress=>", " 更新进度条" + currentProgress);
                        Log.i("upload_task_progress=>", " 下载进度: " + currentPercent);
                    }
                    handler.sendEmptyMessage(11);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //show弹出框
    public void showPopupwindow() {
        LayoutInflater    inflater = LayoutInflater.from(getActivity());
        View  view = inflater.inflate(R.layout.popupwindow_download_progressbar, null);
        final PopupWindow    popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearlayoutDown = (LinearLayout) view.findViewById(R.id.linearlayout_down);
        finishBtn = (Button) view.findViewById(R.id.finish_btn);
        progressBar = (ProgressBar) view.findViewById(R.id.download_progress);
        progressName = (TextView) view.findViewById(R.id.progress_name);
        progressPercent = (TextView) view.findViewById(R.id.progress_percent);
        progressBar.setMax(50);
        progressName.setText("任务正在保存，请稍后...");
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                popupWindow.dismiss();

            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);
        PopWindowUtil. backgroundAlpha(getActivity(),0.6F);   //背景变暗
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(getActivity(),1.0F);
            }
        });
    }
    /**
     * 搜索
     */
    private void setsearch() {
        NewUsersPrame prame=new NewUsersPrame();
        prame.setN_company_id(company_id);//公司id
        //搜索
        String strType=type.getText().toString().trim();
        switch (strType){
            case "开户日期":
                prame.setEndtime(endTime);
                prame.setStarttime(statTime);
                break;
            case "姓名":
                prame.setC_user_name(etSearch.getText().toString().trim());
                break;
            case "用户编号":
                prame.setC_user_id(etSearch.getText().toString().trim());
                break;
            case "气表编号":
                prame.setC_meter_number(etSearch.getText().toString().trim());
                break;
            case "地址":
                prame.setC_user_address(etSearch.getText().toString().trim());
                break;
            default:
                break;
        }
        httoYearNewRequest(prame,page,count);
    }
    /**
     * 请求新增用户数据
     * @param prame
     */
    private void httoYearNewRequest(NewUsersPrame prame,final int pageIndex, int perPageCount) {

        //加载
        loadingView = new LoadingView(getActivity(), R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();
        final String httpUrl = new StringBuffer().append(SkUrl.SkHttp(getActivity())).append("getDyXzyhCx.do").toString();
        Log.e("pgl","==="+httpUrl);
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("aerate")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("n_company_id", prame.getN_company_id())
                .params("c_user_id", prame.getC_user_id()==null?"":prame.getC_user_id())
                .params("c_user_name", prame.getC_user_name()==null?"":SkUrl.toURLEncoded(prame.getC_user_name()))
                .params("starttime", prame.getStarttime()==null?"":prame.getStarttime())
                .params("endtime", prame.getEndtime()==null?"":prame.getEndtime())
                .params("c_user_address", prame.getC_user_address()==null?"":prame.getC_user_address())
                .params("pageIndex",pageIndex+"")
                .params("perPageCount",perPageCount+"")
                .string("");

        Log.e("pgl", "===   n_company_id=" + prame.getN_company_id());
        Log.e("pgl", "===httpUrl=" + build.toString());
        Log.e("pgl", "===   开始时间=" + prame.getStarttime());
        Log.e("pgl","===:结束时间=" + prame.getEndtime());

        Request.newRequest(build, new HttpCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(Response response) {
                Log.e("pgl","==="+response.getBody());
                //GSON直接解析成对象
                NewUsersBean resultBean = new Gson().fromJson(response.getBody(),NewUsersBean.class);
                List<NewUsersBean.ListBean> mList= JsonAnalyUtil.newUsers(response.getBody());

                Log.d("pgl","=== securityAbnormalAdapter"+"执行了"+new Gson().toJson(resultBean.getList()));
                if ("查询成功".equals(resultBean.getMsg())){
                    if (resultBean.getList().size()>0){
                        totalNumber.setText(resultBean.getList().size()+"");
                        noData.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        loadingView.dismiss();
                        newUsersList.clear();
                        newUsersList.addAll(mList);
                        newUsersAdapter.addAll(newUsersList);
                        newUsersAdapter.notifyDataSetChanged();

                       /*
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        newUsersList.clear();
                        newUsersList.add(0,resultBean);
                        //初始化适配器
                        NewUsersAdapter mAdapter = new NewUsersAdapter(newUsersList);
                        //设置适配器
                        mRecyclerView.setAdapter(mAdapter);

                        //设置添加或删除item时的动画，这里使用默认动画
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                        tvTotalUserNumber.setText(resultBean.getList().size()+"");
                        mAdapter.setOnItemClickListener(new NewUsersAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position, NewUsersBean.ListBean data) {
//                                currentPosition = position;
                                Intent intent = new Intent(getContext(),  SecurityAbnormalInfoActivity.class);
                                intent.putExtra("listBean",data);
                                startActivityForResult(intent, 9);
                            }
                        });*/


                    }else {
                        loadingView.dismiss();
                        Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                        noData.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                    }
                }else {
                    noData.setVisibility(View.VISIBLE);
                    loadingView.dismiss();
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(Throwable e) {
                noData.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                Log.d("pgl","==="+e.getMessage());
                Toast.makeText(getActivity(), "网络请求异常", Toast.LENGTH_SHORT).show();
                loadingView.dismiss();
            }
        }).executeAsync();
    }

    @Override
    public void onItemClick(View view, int position, NewUsersBean.ListBean data) {
        if(!NewUsersAdapter.isSelected.get(position)){
            NewUsersAdapter.isSelected.put(position, true); // 修改map的值保存状态
            newUsersAdapter.notifyItemChanged(position);
            datas.add(newUsersList.get(position));

        }else {
            NewUsersAdapter.isSelected.put(position, false); // 修改map的值保存状态
            newUsersAdapter.notifyItemChanged(position);
            datas.remove(newUsersList.get(position));
        }
//        textView.setText("已选中"+datas.size()+"项");
        Log.e("pgl","==已选中:"+datas.size()+"项");
    }

    /**
     * 类型选择
     */
    private void showListWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View popView = layoutInflater.inflate(R.layout.popupwindow_text, null);
        final PopupWindow popListWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ListView listView = (ListView) popView.findViewById(R.id.listView);
        setDataPop(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) adapter.getItem(position);
                type.setText(str);
                //开户日期
                if (str.equals("开户日期")) {
                    etSearch.setVisibility(View.GONE);
                    tvTimeChoice.setVisibility(View.VISIBLE);
                } else {
                    Log.e("pgl","===  timeChoice");
                    tvTimeChoice.setVisibility(View.GONE);
                    etSearch.setVisibility(View.VISIBLE);
                }
                datas.clear();
                popListWindow.dismiss();
            }
        });
        popListWindow.update();
        popListWindow.setFocusable(true);
        popListWindow.setTouchable(true);
        popListWindow.setOutsideTouchable(true);
        popListWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popListWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        //背景变暗
        PopWindowUtil.backgroundAlpha(getActivity(), 0.6F);
        popListWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        popListWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(getActivity(), 1.0F);
            }
        });
    }
    /**
     * 添加数据
     */
    private void setDataPop(ListView listView) {
        String[] array = new String[]{ "开户日期","姓名", "用户编号", "气表编号", "地址"};
        adapter = new TextAdapter(getActivity(), array);
//        adapter.addAll(Arrays.asList(array));
        listView.setAdapter(adapter);

    }


    /**
     * 时间选择
     */
    public void showTimeWindow(String strTitle) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
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
                    handler.sendEmptyMessage(0);
                    blChek = true;
                    statTime = years + "-" + month1 + "-" + day1;
                    tvTimeChoice.setText(statTime);
                    Log.e("pgl","===第一次点击");
                } else {
                    Log.e("pgl","==1111=选择结束时间");
                    endTime = years + "-" + month1 + "-" + day1;
                    tvTimeChoice.setText(statTime + "至" + endTime);
                    //范围
                    if (checkInput()){
                        Log.e("pgl","==2222=选择结束时间");
                        popWindow.dismiss();
                        NewUsersPrame prame=new NewUsersPrame();
                        prame.setN_company_id(company_id);
                        prame.setStarttime(statTime);
                        prame.setEndtime(endTime);
                        httoYearNewRequest(prame,page,count);
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
        PopWindowUtil.backgroundAlpha(getActivity(), 0.6F);   //背景变暗
        popWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(getActivity(), 1.0F);
            }
        });
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showTimeWindow("选择结束时间");
                    break;
                case 1:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };
    /**
     * 时间间隔
     * @return
     */
    public boolean checkInput() {
        if (TextUtils.isEmpty(statTime)) {
            Toast.makeText(getActivity(), "请输入开始时间~", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (TextUtils.isEmpty(endTime)) {
            Toast.makeText(activity1, "请输入结束时间~", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dateStart = dateFormat.parse(statTime);
            Date dateEnd = dateFormat.parse(endTime);
            if (dateStart.getTime() > dateEnd.getTime()) {
                Toast.makeText(activity1, "开始时间需要小于结束时间哦~~",
                        Toast.LENGTH_SHORT).show();

                return false;
            } else if (dateStart.getTime() >System.currentTimeMillis()) {
                Toast.makeText(activity1, "开始时间大于现在时间哦~~",
                        Toast.LENGTH_SHORT).show();
                return false;
            } else if (dateEnd.getTime() > System.currentTimeMillis()) {
                Toast.makeText(activity1, "结束时间大于现在时间哦~~",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(activity1, "数据格式有误！", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;

    }


    /**
     * 全选
     */
    public void all(){
        datas.clear();

        for (int i = 0; i < newUsersList.size(); i++) {
            NewUsersAdapter.isSelected.put(i, true);
            datas.add(newUsersList.get(i));
        }
        newUsersAdapter.notifyDataSetChanged();
//        textView.setText("已选中"+selectDatas.size()+"项");
    }
    /**
     * 反选
     */
    public void inverse(){
        Log.e("pgl","=====反选:");
        for (int i=0; i<newUsersList.size(); i++) {
            if(NewUsersAdapter.isSelected.get(i)){
                NewUsersAdapter.isSelected.put(i,false);
                datas.remove(newUsersList.get(i));
            } else {
                NewUsersAdapter.isSelected.put(i,true);
                datas.add(newUsersList.get(i));
            }
        }
        newUsersAdapter.notifyDataSetChanged();
//        textView.setText("已选中"+selectDatas.size()+"项");

    }
    /**
     * 取消已选
     */
    public void cancel(){
        for (int i=0; i<newUsersList.size(); i++) {
            if(NewUsersAdapter.isSelected.get(i)){
                NewUsersAdapter.isSelected.put(i,false);
                datas.remove(newUsersList.get(i));
            }
        }
        newUsersAdapter.notifyDataSetChanged();
//        textView.setText("已选中"+selectDatas.size()+"项");
    }

    /**
     * 上拉加载
     * @param refreshlayout
     */
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        refreshlayout.finishLoadmore(800);
        page++;
        setsearch();
    }
    /**
     * 下拉刷新
     * @param refreshlayout
     */
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        refreshlayout.finishRefresh(800);
        page=1;
        count=50;
        newUsersAdapter.clear();
        datas.clear();
        newUsersAdapter.notifyDataSetChanged();
        setsearch();
    }
}
