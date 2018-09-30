package com.example.administrator.thinker_soft.Security_check.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.activity.TaskFileDeleteActivity;
import com.example.administrator.thinker_soft.meter_code.activity.MeterSettingsActivity;
import com.example.administrator.thinker_soft.meter_code.sk.bean.UpDateBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.security.SecurityUpActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.fragment.SyEastHomePagerFragment;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.DeviceUtils;
import com.example.administrator.thinker_soft.meter_code.sk.update.UpdateManager;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/8/21 0021.
 */
public class MyInfoFragment extends Fragment{
    private View view;
    private CardView fileManage,meterSettings,cv_up,cv_update;
    private TextView tvName,tv_userId,tv_cyID;
    private SharedPreferences sharedPreferences_login;
    /**申请权限的请求码*/
    private final int REQUEST_WRITE=1;
    /**版本信息*/
    private UpDateBean dateBean;
    private  TextView tv_new;
    private    String apKurl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_info_sec, null);

        bindView();
        defaultSetting();
        setViewClickListener();
        return view;
    }


    public static MyInfoFragment newInstance() {
        MyInfoFragment fragment = new MyInfoFragment();

        return fragment;
    }
    //绑定控件
    private void bindView() {
        fileManage = (CardView) view.findViewById(R.id.file_manage);
        meterSettings = (CardView) view.findViewById(R.id.meter_settings);
        cv_up = (CardView) view.findViewById(R.id.cv_up);
        cv_update = (CardView) view.findViewById(R.id.cv_update);
        tvName= (TextView) view.findViewById(R.id.tv_name);
        tv_userId= (TextView) view.findViewById(R.id.tv_userId);
        tv_cyID= (TextView) view.findViewById(R.id.tv_cyID);
        tv_new= (TextView) view.findViewById(R.id.tv_new);
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        tv_cyID.setText(sharedPreferences_login.getInt("company_id",0)+"");
        tvName.setText(sharedPreferences_login.getString("user_name",""));
        tv_userId.setText(sharedPreferences_login.getString("userId",""));

    }

    //点击事件
    public void setViewClickListener() {
        fileManage.setOnClickListener(clickListener);
        meterSettings.setOnClickListener(clickListener);
        cv_up.setOnClickListener(clickListener);
        cv_update.setOnClickListener(clickListener);
   serRequest();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.file_manage:
                    intent = new Intent(getActivity(), TaskFileDeleteActivity.class);
                    startActivity(intent);
                    break;
                case R.id.meter_settings:
                    intent = new Intent(getActivity(), MeterSettingsActivity.class);
                    intent.putExtra("anJian","安检");
                    startActivity(intent);
                    break;
                case R.id.cv_up:
                    //上传模式
                    intent = new Intent(getActivity(), SecurityUpActivity.class);
                    startActivity(intent);
                    break;

                case R.id.cv_update:
                    //版本更新
                    setUpdate();
                    break;

                default:
                    break;
            }
        }
    };

    private void setUpdate(){

        //判断是否6.0以上的手机   不是就不用
        if(Build.VERSION.SDK_INT>=23){
            //判断是否有这个权限
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                //第一请求权限被取消显示的判断，一般可以不写
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(getActivity(),"我们需要这个权限给你提供存储服务",Toast.LENGTH_SHORT).show();

                }else {
                    //2、申请权限: 参数二：权限的数组；参数三：请求码
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE);
                }
            }else {
                if (apKurl!=null){
                //"http://gdown.baidu.com/data/wisegame/f5eae17c4f4bb40b/baidushoujizhushou_16795794.apk"

                new UpdateManager(getActivity(),"更新内容:\n" + dateBean.getUpdateEmotion(),Double.parseDouble(dateBean.getVersion()),apKurl).checkUpdate(false);
                }
                }
        } else{
            if (apKurl!=null){
               new UpdateManager(getActivity(),"更新内容:\n" + dateBean.getUpdateEmotion(),Double.parseDouble(dateBean.getVersion()),apKurl).checkUpdate(false);
            }
        }
    }

    /**
     * 判断授权的方法  授权成功直接调用写入方法  这是监听的回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (dateBean!=null){
                new UpdateManager(getActivity(),"更新内容:\n" + dateBean.getUpdateEmotion(),Double.parseDouble(dateBean.getVersion()),apKurl).checkUpdate(false);
            }
        }
    }


    /**
     * 获取版本信息
     */
    private void serRequest(){
        final String httpUrl = new StringBuffer().append(SkUrl.SkHttp(getActivity())).append("getVersion.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("aerate")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .string("");

        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e("pgl", "===" + response.getBody());

                UpDateBean upDateBean=new Gson().fromJson(response.getBody(),UpDateBean.class);
                dateBean=upDateBean;


                //截取#之前的字符串
//                String str = "sdfs#d";
//                str.substring(0, str.indexOf("#"));
//                //截取之后的字符
//                String userId = "21321321?u=2132132132";
                // apKurl = dateBean.getUrl().substring(0,  dateBean.getUrl().indexOf("thinker"));
                apKurl=dateBean.getUrl();
                Log.e("url",apKurl);
                if(dateBean.getVersion()!=null) {
                    // 当前的版本号
                    double mVersion_code = Double.parseDouble(DeviceUtils.getVersionName(getActivity()));
                    double version_code = Double.parseDouble(dateBean.getVersion());
                    if (mVersion_code < version_code) {
                        tv_new.setVisibility(View.VISIBLE);
                    } else {
                        tv_new.setVisibility(View.GONE);
                    }
                }

            }
            @Override
            public void onError(Throwable e) {

            }
        }).executeAsync();
    }
}
