package com.example.administrator.thinker_soft.meter_code.sk.ui.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.AddedMultiAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.TextAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AddedBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AetatePrame;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AetateYearPrame;
import com.example.administrator.thinker_soft.meter_code.sk.db.DBManage;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 新增计划
 * Created by Administrator on 2018/8/4.
 */

public class SecurityAddedActivity  extends BaseActivity implements AddedMultiAdapter.OnItemClickLitener, OnLoadmoreListener, OnRefreshListener {
    private AddedMultiAdapter addedMultiAdapter;
    private  List<AddedBean.AddedList> datas=new ArrayList<>();
    private List<AddedBean.AddedList> selectDatas = new ArrayList<>();
    private  TextAdapter adapter;
    private  List<String> mList;
    private  List<String> mListID;
    private String tyID="";//任务编号
    private SharedPreferences sharedPreferences_login;
    private SharedPreferences sharedPreferences ;
    private String userID;//用户id
    private String company_id;//公司id
    private  LoadingView loadingView;//进度条
    private String login_name;
    private String noTask="";

    private int page=1;//页数
    private int count=50;//条数

    /**
     * 下载经度更新
     */
    private LinearLayout linearlayoutDown;
    private ProgressBar progressBar;
    private TextView progressName,progressPercent;
    private Button finishBtn;
    private int currentProgress = 0;
    private int currentPercent = 0;

    private boolean queryAll=true;//年计划内和外
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_title)
    TextView textView;
    @BindView(R.id.tv_total_user_number)
    TextView totalNumber;//条数
    @BindView(R.id.et_search)
    ClearEditText etSearch;
    @BindView(R.id.tv_type)
    TextView type;//类型
    @BindView(R.id.no_data)
    LinearLayout noData;//空数据
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;//刷新控件

    @Override
    protected int getContentViewID() {
        return R.layout.activity_sy_added;
    }

    @Override
    protected void initView() {

        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        userID=sharedPreferences_login.getString("userId", "");
        login_name=sharedPreferences_login.getString("login_name", "");
        sharedPreferences = getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        company_id = String.valueOf(sharedPreferences_login.getInt("company_id", 0));//公司id
     // company_id="26";


      //  mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addedMultiAdapter = new AddedMultiAdapter(datas);
        addedMultiAdapter.setOnItemClickLitener(this);
        mRecyclerView.setAdapter(addedMultiAdapter);
         mRefreshLayout.setRefreshHeader(new WaterDropHeader(this));
        mRefreshLayout.setPrimaryColorsId(R.color.theme_colors, android.R.color.white);
        mRefreshLayout.setDisableContentWhenRefresh(true);  //是否在刷新的时候禁止内容的一切手势操作（默认false）
        mRefreshLayout.setDisableContentWhenLoading(true);  //是否在加载的时候禁止内容的一切手势操作（默认false）
        mRefreshLayout.setOnLoadmoreListener(this);//上拉加载
        mRefreshLayout.setOnRefreshListener(this);//下拉刷新
      //  mRefreshLayout.setEnableRefresh(false);
             mRecyclerView.requestFocus();

        String currentTaskName = sharedPreferences.getString("currentTaskName", "");
        if (!"".equals(sharedPreferences.getString("currentTaskName",""))) {
            textView.setText(sharedPreferences.getString("currentTaskName",""));
            tyID = sharedPreferences.getString("currentTaskId", "");
            noTask="YesTask";
        }else {
            textView.setText("新增任务");
        }

//        //搜索
//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
//               // addedMultiAdapter.getFilter().filter(sequence.toString());
//
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });

       // httoYearRequest("2",page,count);//年计划内
        setsearch("2");
//        //获取计划
//        List<TaskChoose>  securityCaseItemList = DBManage.getInstance(SecurityAddedActivity.this).getTaskData(userID);
//        mList=new ArrayList<>();
//        mListID=new ArrayList<>();
//        for (TaskChoose choose:securityCaseItemList){
//            mList.add(choose.getTaskName());
//            mListID.add(choose.getTaskNumber());
//        }


    }

    /**
     * 监听
     * @param view
     */
    @OnClick({R.id.back, R.id.tv_select_all, R.id.tv_reverse,R.id.tv_select_cancel,R.id.tv_more,R.id.tv_add,R.id.tv_search,R.id.tv_type})
    public void OnclickAdderd(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
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
                cancel();
                break;
            case R.id.tv_more:
                //更多
                showMoreWindow(view);
                break;
            case R.id.tv_add:
                //新增
                if (noTask.equals("YesTask")){
                    if(selectDatas!=null&&selectDatas.size()>0){
                        //年计化内  网络请求
                        httoYearAddRequest("2");
                    }else {
                        ToastUtil.showShort(SecurityAddedActivity.this,"请选择新增人员");

                    }

                }else {
                    ToastUtil.showShort(SecurityAddedActivity.this,"抱歉!,本地还没有任务请先下载");
                }

                break;

            case R.id.tv_search:
                datas.clear();
                page=1;
                count=50;
                //搜索
                setsearch("2");

                break;
            case R.id.tv_type:
                //类型选择
                showListWindow();
                break;

                default:
                    break;

        }
    }





    /**
     * 类型选择
     */
    private void showListWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(SecurityAddedActivity.this);
        View popView = layoutInflater.inflate(R.layout.popupwindow_text, null);
        final PopupWindow popListWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ListView listView = (ListView) popView.findViewById(R.id.listView);
        setDataPop(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) adapter.getItem(position);
                type.setText(str);
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
        PopWindowUtil.backgroundAlpha(SecurityAddedActivity.this, 0.6F);
        popListWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        popListWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SecurityAddedActivity.this, 1.0F);
            }
        });

    }




    /**
     * 全选
     */
    public void all(){
        selectDatas.clear();

        for (int i = 0; i < datas.size(); i++) {
            AddedMultiAdapter.isSelected.put(i, true);
            selectDatas.add(datas.get(i));
        }
        addedMultiAdapter.notifyDataSetChanged();
        textView.setText("已选中"+selectDatas.size()+"项");
    }
    /**
     * 反选
     */
    public void inverse(){
        for (int i=0; i<datas.size(); i++) {
            if(AddedMultiAdapter.isSelected.get(i)){
                AddedMultiAdapter.isSelected.put(i,false);
                selectDatas.remove(datas.get(i));
            } else {
                AddedMultiAdapter.isSelected.put(i,true);
                selectDatas.add(datas.get(i));
            }
        }
        addedMultiAdapter.notifyDataSetChanged();
        textView.setText("已选中"+selectDatas.size()+"项");

    }
    /**
     * 取消已选
     */
    public void cancel(){
        for (int i=0; i<datas.size(); i++) {
            if(AddedMultiAdapter.isSelected.get(i)){
                AddedMultiAdapter.isSelected.put(i,false);
                selectDatas.remove(datas.get(i));
            }
        }
        addedMultiAdapter.notifyDataSetChanged();
        textView.setText("已选中"+selectDatas.size()+"项");
    }


    /**
     * 弹出框
     *
     * @param view
     */
    private void showMoreWindow(View view) {
        View contentView = getLayoutInflater().inflate(R.layout.popwindow_meter_user_state, null);
        final PopupWindow    popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView allUser = (TextView) contentView.findViewById(R.id.all_user);
        final TextView notCopyUser = (TextView) contentView.findViewById(R.id.not_copy_user);
        allUser.setText("年计划内");
        notCopyUser.setText("年计划外");
        if (queryAll) {
            notCopyUser.setTextColor(Color.BLACK);
            allUser.setTextColor(Color.BLUE);
        } else {
            notCopyUser.setTextColor(Color.BLUE);
            allUser.setTextColor(Color.BLACK);
        }
        allUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAll=true;
             //   httoYearRequest("2",page,count);
                setsearch("2");
                popupWindow.dismiss();

            }
        });
        notCopyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAll=false;
               // httoYearRequest("1",page,count);
                setsearch("1");
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.showAsDropDown(view, -PopWindowUtil.dip2px(SecurityAddedActivity.this,68), 0);
        PopWindowUtil. backgroundAlpha(SecurityAddedActivity.this, 0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SecurityAddedActivity.this, 1.0F);
            }
        });
    }

//    /**
//     * 类型选择
//     */
//    private void showListWindow() {
//      LayoutInflater layoutInflater = LayoutInflater.from(SecurityAddedActivity.this);
 //       View popView = layoutInflater.inflate(R.layout.popupwindow_text, null);
//        final PopupWindow popListWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        ListView listView = (ListView) popView.findViewById(R.id.listView);
//        setData(listView);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String str = (String) adapter.getItem(position);
//                textView.setText(str);
//                if(selectDatas!=null&&selectDatas.size()>0){
//                    //年计化内  网络请求
//                    httoYearAddRequest("2");
//                }else {
//                    ToastUtil.showShort(SecurityAddedActivity.this,"请选择新增人员");
//                    showPopupwindow();
//                    setProgress();
//                }
//
//                //  插入数据到数据库里
//                InsertUserData();
////                //年计化内  网络请求
////                httoYearAddRequest("2");
//                //  插入数据到数据库里
//
////                if (queryAll){
////                    httoYearAddRequest("2");
////                }else {
////                    httoYearAddRequest("1");
////                }
//
//                popListWindow.dismiss();
//            }
//        });
//        popListWindow.update();
//        popListWindow.setFocusable(true);
//        popListWindow.setTouchable(true);
//        popListWindow.setOutsideTouchable(true);
//        popListWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
//        popListWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
//        PopWindowUtil.backgroundAlpha(SecurityAddedActivity.this, 0.6F);   //背景变暗
//        popListWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        popListWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                PopWindowUtil.backgroundAlpha(SecurityAddedActivity.this, 1.0F);
//            }
//        });
//    }

    /**
     * 添加数据
     */
    private void setData(ListView listView) {
        adapter = new TextAdapter(this,mList.toArray(new String[mList.size()]));
        listView.setAdapter(adapter);

    }
    /**
     * 添加数据
     */
    private void setDataPop(ListView listView) {
        String[] array = new String[]{ "姓名", "用户编号", "表编号", "地址"};
        adapter = new TextAdapter(this, array);
//        adapter.addAll(Arrays.asList(array));
        listView.setAdapter(adapter);

    }
    /**
     * item监听
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        if(!AddedMultiAdapter.isSelected.get(position)){
            AddedMultiAdapter.isSelected.put(position, true); // 修改map的值保存状态
            addedMultiAdapter.notifyItemChanged(position);
            selectDatas.add(datas.get(position));

        }else {
            AddedMultiAdapter.isSelected.put(position, false); // 修改map的值保存状态
            addedMultiAdapter.notifyItemChanged(position);
            selectDatas.remove(datas.get(position));
        }
        textView.setText("已选中"+selectDatas.size()+"项");
    }


    /**
     * 年内计划
     */
    private void httoYearRequest(AetateYearPrame prame, final int pageIndex, int perPageCount) {
        if(pageIndex==1) {
            //加载
            loadingView = new LoadingView(SecurityAddedActivity.this, R.style.LoadingDialog, "加载中...请稍后");
            loadingView.show();
        }

        String httpUrl = new StringBuffer().append(SkUrl.SkHttp(SecurityAddedActivity.this)).append("getSecurityUser.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("aerate")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("year", prame.getYear())
                .params("pageIndex",pageIndex+"")
                .params("perPageCount",perPageCount+"")
                .params("n_company_id",company_id)
                .params("c_user_id", prame.getC_user_id() == null ? "" : prame.getC_user_id())
                .params("c_user_name", prame.getC_user_name() == null ? "" : SkUrl.toURLEncoded(prame.getC_user_name()))
                .params("c_old_user_id", prame.getC_meter_number() == null ? "" : prame.getC_meter_number())
                .params("c_user_address", prame.getC_user_address() == null ? "" : SkUrl.toURLEncoded(prame.getC_user_address()))
                .string("");
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e("MeterRemarkActivity", "onComplete/response:" + response.getBody());
                Log.e("MeterRemarkActivity", "onComplete/response: content type=" + response.getContentType());
                if(pageIndex==1) {
                    loadDiss();
                }
                List<AddedBean.AddedList> mList= JsonAnalyUtil.analyszeAdded(response.getBody());
//                datas = new ArrayList<>();
                datas.addAll(mList);
                addedMultiAdapter.addAll(mList);
                addedMultiAdapter.notifyDataSetChanged();
                totalNumber.setText(datas.size()+"");
                Log.e("List=", mList.size()+"===="+datas.size());

                //空数据
                if (mList.size() > 0) {
                    if (noData != null) {
                        noData.setVisibility(View.GONE);
                    }
                } else {
                    if (noData != null) {
                        noData.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
                if(pageIndex==1) {
                    loadDiss();
                }
                //空数据
                if (datas.size() > 0) {
                    if (noData != null) {
                        noData.setVisibility(View.GONE);
                    }
                } else {
                    if (noData != null) {
                        noData.setVisibility(View.VISIBLE);
                    }
                }
            }
        }).executeAsync();
    }


    /**
     * 年内计划新增
     */
    private void httoYearAddRequest(String plan) {
        //加载
        loadingView = new LoadingView(SecurityAddedActivity.this, R.style.LoadingDialog, "加载中...请稍后");
        loadingView.show();
        //http://88.88.88.251:8082/SMDemo/addSecurityInUser.do?c_user_id=5&n_safety_plan=1
        //3、年外新增
       // http://88.88.88.251:8082/SMDemo/addSecurityOutUser.do?c_user_id=5&n_safety_plan=1
        String httpUrl;
        if (plan.equals("2")){
         //年计划内新增
        httpUrl = new StringBuffer().append(SkUrl.SkHttp(SecurityAddedActivity.this)).append("addSecurityInUser.do").toString();
        }else {
            //年计划外新增
         httpUrl = new StringBuffer().append(SkUrl.SkHttp(SecurityAddedActivity.this)).append("addSecurityOutUser.do").toString();
        }
       String json=dataToJson();

        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.POST)
                .encode("UTF-8")
                .tag("add")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("safetyInspection",json)
                .string("");
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response)  {
                Log.e("MeterRemarkActivity", "onComplete/response:" + response.getBody());
                Log.e("MeterRemarkActivity", "onComplete/response: content type=" + response.getContentType());
                loadDiss();
               // {"msg":"新增成功","list":[],"status":"success"}

                try {
                    JSONObject  jsonObject=new JSONObject(response.getBody());
                    String success= jsonObject.optString("status","");
                    if (success.equals("success")){
                      //  ToastUtil.showShort(SecurityAddedActivity.this,"新增成功");
                        showPopupwindow();
                        page=1;
                        count=50;
                        addedMultiAdapter.clear();
                        datas.clear();
                        addedMultiAdapter.notifyDataSetChanged();
                       // httoYearRequest("2",page,count);//年计划内
                        setsearch("2");
                        setSafety(tyID);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(SecurityAddedActivity.this,"新增失败");
                }


            }

            @Override
            public void onError(Throwable e) {
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());
                loadDiss();
                ToastUtil.showShort(SecurityAddedActivity.this,"新增失败");

            }
        }).executeAsync();
    }


    //将数据转换成Json格式
    public String dataToJson() {
     JSONObject jsonObject = new JSONObject();

        try {
        //  JSONObject object = new JSONObject();
            //object.put("c_safety_plan_name", taskNameEt.getText().toString());      //安检任务名称d
           // JSONArray jsonArray = new JSONArray();
            JSONArray jsonArray=new JSONArray();
            for (int i = 0; i < selectDatas.size(); i++) {
                JSONObject object1 = new JSONObject();
                object1.put("c_user_id", selectDatas.get(i).getC_user_id());
                object1.put("n_safety_plan", tyID);
                jsonArray.put(i, object1);
            }
           jsonObject.put("safetyInspection", jsonArray);
            Log.i("dataToJson==========>", "封装的json数据为：" + jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    /**
     * 根据任务ID查询任务编号
     */
    private void setSafety(String resultTaskId){
        String httpUrl=new StringBuffer().append(SkUrl.SkHttp(SecurityAddedActivity.this)).append("getUserCheck.do").toString();
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
                Log.e("MeterRemarkActivity", "onComplete/response:" + response.getBody());
                Log.e("MeterRemarkActivity", "onComplete/response: content type=" + response.getContentType());
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
                            Log.e("josn",new Gson().toJson(selectDatas));

                            for (AddedBean.AddedList addedList :selectDatas){
                                Log.e("MeterRemarkActivity",  addedList.getC_user_name());
                                if (object.optString("userName","").equals(addedList.getC_user_name())){
                                    //插入本地数据
                                    DBManage.getInstance(SecurityAddedActivity.this).insertUser(object,tyID,login_name,userID);
                                    DBManage.getInstance(SecurityAddedActivity.this).updateTaskInfo(tyID,userID,1);
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
                Log.e("SecurityAerateActivity", "onError:" + e.getMessage());


            }
        }).executeAsync();
    }



    private void loadDiss() {
        if (loadingView != null) {
            loadingView.dismiss();
        }
    }
    //show弹出框
    public void showPopupwindow() {
        LayoutInflater    inflater = LayoutInflater.from(SecurityAddedActivity.this);
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
        PopWindowUtil. backgroundAlpha(SecurityAddedActivity.this,0.6F);   //背景变暗
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopWindowUtil.backgroundAlpha(SecurityAddedActivity.this,1.0F);
            }
        });
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    progressPercent.setText(String.valueOf(msg.arg2));
                    progressBar.setProgress(msg.arg1);
                    Log.i("down_progress=>", " 任务进度为：" + progressBar.getProgress());
                    break;
                case 11:
                    progressName.setText("任务新增成功！");
                    linearlayoutDown.setVisibility(View.GONE);
                    finishBtn.setVisibility(View.VISIBLE);
                    currentProgress = 0;
                    currentPercent = 0;
                    break;
            }
        }
    };

    /**
     * 上拉加载
     * @param refreshlayout
     */
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        refreshlayout.finishLoadmore(800);
        page++;
     //   httoYearRequest("2",page,count);//年计划内
        setsearch("2");
    }
    /**
     * 搜索
     */
    private void setsearch(String year) {
        AetateYearPrame prame = new AetateYearPrame();
        prame.setYear(year);
        //搜索
        String strType = type.getText().toString().trim();
        switch (strType) {
            case "姓名":
                prame.setC_user_name(etSearch.getText().toString().trim());
                break;
            case "用户编号":
                prame.setC_user_id(etSearch.getText().toString().trim());
                break;
            case "表编号":
                prame.setC_meter_number(etSearch.getText().toString().trim());
                break;
            case "地址":
                prame.setC_user_address(etSearch.getText().toString().trim());
                break;
            default:
                break;
        }
        //年计划内
        httoYearRequest(prame,page,count);
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
        addedMultiAdapter.clear();
        datas.clear();
        addedMultiAdapter.notifyDataSetChanged();
        setsearch("2");
     //   httoYearRequest("2",page,count);//年计划内
    }
}
