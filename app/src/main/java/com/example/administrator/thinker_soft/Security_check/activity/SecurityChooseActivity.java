package com.example.administrator.thinker_soft.Security_check.activity;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.adapter.SecurityCheckViewPagerAdapter;
import com.example.administrator.thinker_soft.Security_check.fragment.MyInfoFragment;
import com.example.administrator.thinker_soft.Security_check.fragment.SecurityChooseFragment;
import com.example.administrator.thinker_soft.Security_check.fragment.SecurityChooseRootFragment;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;
import com.example.administrator.thinker_soft.meter_payment.Utils.MyDialog;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.mode.Tools;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */
public class SecurityChooseActivity extends FragmentActivity {
    private RadioButton settings, quite; //文件管理 系统设置 退出应用
    private boolean isFirst;   //是否第一次近日app
    private LayoutInflater inflater; //转换器
    private View popupwindowView, quiteView;
    private Button cancelRb, saveRb;
    private LinearLayout rootLinearlayout;
    private RelativeLayout rootRelative;
    private PopupWindow popupWindow, quitePopup;
    private ImageView back, security_check_go;
    private RadioButton optionRbt;  //选项按钮
    private TextView name, userName, tips;
    private RadioButton dataTransferRbt, mime;  //数据传输按钮
    private List<Fragment> fragmentList;
    private ViewPager viewPager;
    private SecurityCheckViewPagerAdapter adapter;
    private SharedPreferences sharedPreferences, sharedPreferences_login, public_sharedPreferences;
    private SharedPreferences.Editor editor;
    private SQLiteDatabase db;  //数据库
    private MySqliteHelper helper; //数据库帮助类
   // private String ip, port;  //接口ip地址   端口
    public int responseCode = 0;
    private String stateResult, contentResult, hiddenResult, reasonResult; //网络请求结果
    private JSONObject stateObject, contentObject, hiddenObject, reasonObject;
    private SoundPool sp;//声明一个SoundPool
    private int music;//定义一个整型用load（）；来设置suondID
    private RadioButton versionInfo;
    private long exitTime = 0;//退出程序
    //强制竖屏
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("SecurityChooseActivity", "onResume");
    }

    public void checkIpAndPort() {
        Log.i("SecurityChooseActivity", "checkIpAndPort执行了！");
        db.delete("SecurityState", null, null);  //删除SecurityState表中的所有数据
        db.delete("security_content", null, null);  //删除security_content表中的所有数据
        db.delete("security_hidden", null, null);  //删除security_hidden表中的所有数据
        db.delete("security_hidden_reason", null, null);  //删除security_hidden_reason表中的所有数据
        //设置id从1开始（sqlite默认id从1开始），若没有这一句，id将会延续删除之前的id
        db.execSQL("update sqlite_sequence set seq=0 where name='SecurityState'");
        db.execSQL("update sqlite_sequence set seq=0 where name='security_content'");
        db.execSQL("update sqlite_sequence set seq=0 where name='security_hidden'");
        db.execSQL("update sqlite_sequence set seq=0 where name='security_hidden_reason'");

        //开启支线程进行请求任务信息
        new Thread() {
            @Override
            public void run() {
                requireSecurityState("findSecurityState.do "); //安检状态
                Log.i("findSecurityState", "进来了");
                requireSecurityContent("findSecurityContent.do");//安检内容
                requireSafetyHidden("findSafetyHidden.do");//安检原因类型
                requireSafetyReason("findSafetyReason.do");//安检原因
                super.run();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("SecurityChooseActivity", "onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("SecurityChooseActivity", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("SecurityChooseActivity", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("SecurityChooseActivity", "onRestart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setViewPager();//设置viewPager
        setViewClickListener();//点击事件
        Log.i("SecurityChooseActivity", "onCreate");
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        security_check_go = (ImageView) findViewById(R.id.security_check_go);
        optionRbt = (RadioButton) findViewById(R.id.option_rbt);
        mime = (RadioButton) findViewById(R.id.mime);
        viewPager = (ViewPager) findViewById(R.id.security_viewpager);
        name = (TextView) findViewById(R.id.name);
        userName = (TextView) findViewById(R.id.user_name);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
        rootRelative = (RelativeLayout) findViewById(R.id.root_relative);
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        security_check_go.setOnClickListener(onClickListener);
        optionRbt.setOnClickListener(onClickListener);
        mime.setOnClickListener(onClickListener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        optionRbt.setChecked(true);
                        mime.setChecked(false);
                        name.setText("首页");
                        break;
                    case 1:
                        optionRbt.setChecked(false);
                        mime.setChecked(true);
                        name.setText("我");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    SecurityChooseActivity.this.finish();
                    break;
                case R.id.security_check_go:
                    createPopupwindow();
                    break;
                case R.id.option_rbt:
                    sp.play(music, 1, 1, 0, 0, 1);
                    viewPager.setCurrentItem(0);
                    name.setText("首页");
                    break;
                case R.id.mime:
                    sp.play(music, 1, 1, 0, 0, 1);
                    viewPager.setCurrentItem(1);
                    name.setText("我");
                    break;
                default:
                    break;
            }
        }
    };

    //系统设置popupwindow
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void createPopupwindow() {
        inflater = LayoutInflater.from(SecurityChooseActivity.this);
        popupwindowView = inflater.inflate(R.layout.popup_window_security, null);
        popupWindow = new PopupWindow(popupwindowView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        //file = (RadioButton) popupwindowView.findViewById(R.id.file);//文件管理
        settings = (RadioButton) popupwindowView.findViewById(R.id.settings);//系统设置
        quite = (RadioButton) popupwindowView.findViewById(R.id.quite);//安全退出
        versionInfo = (RadioButton) popupwindowView.findViewById(R.id.version_info);//版本信息
        versionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                final MyDialog myDialog = new MyDialog();
                myDialog.show("版本信息", "版本号:" + getLocalVersionName(SecurityChooseActivity.this), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDialog.dismiss();
                    }
                }, getSupportFragmentManager());
            }
        });
        //设置点击事件
        /*file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.file:
                        Intent intent = new Intent(SecurityChooseActivity.this, FileManageActivity.class);
                        startActivity(intent);
                        popupWindow.dismiss();
                        break;
                }
            }
        });*/
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.settings:
                        Intent intent = new Intent(SecurityChooseActivity.this, SystemSettingActivity.class);
                        startActivity(intent);
                        popupWindow.dismiss();
                        break;
                }
            }
        });

        quite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.exit(0);
                popupWindow.dismiss();
                showQuitePopup();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAsDropDown(security_check_go, -200, 0);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }
    /**
     * 获取软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
    //弹出退出登录前提示popupwindow
    public void showQuitePopup() {
        inflater = LayoutInflater.from(SecurityChooseActivity.this);
        quiteView = inflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        quitePopup = new PopupWindow(quiteView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        tips = (TextView) quiteView.findViewById(R.id.tips);
        cancelRb = (RadioButton) quiteView.findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) quiteView.findViewById(R.id.save_rb);
        //设置点击事件
        tips.setText("退出后不会删除历史数据，下次登录依然可以使用本账号！");
        saveRb.setTextColor(getResources().getColor(R.color.red));
        saveRb.setText("退出登录");
        cancelRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitePopup.dismiss();
            }
        });
        saveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitePopup.dismiss();
                Intent intent = new Intent(SecurityChooseActivity.this, MobileSecurityLoginActivity.class);
                startActivity(intent);
                sharedPreferences_login.edit().putBoolean("have_logined", false).apply();
                finish();
            }
        });
        quitePopup.update();
        quitePopup.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        quitePopup.setAnimationStyle(R.style.camera);
        quitePopup.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        quitePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = SecurityChooseActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            SecurityChooseActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            SecurityChooseActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        SecurityChooseActivity.this.getWindow().setAttributes(lp);
    }

    //初始化设置
    private void defaultSetting() {
        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.beep, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        optionRbt.setChecked(true);
        helper = new MySqliteHelper(SecurityChooseActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);  //退出登录以后需要这个备份记录是否更换账号
        sharedPreferences = this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        public_sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = public_sharedPreferences.edit();
        isFirst = sharedPreferences.getBoolean("FIRST", true);
        if (isFirst) {
            Log.i("SecurityChooseActivity", "第一次进入APP");  //第一次进入APP不管IP端口是否改变都要下载
            sharedPreferences.edit().putBoolean("FIRST", false).apply();
            checkIpAndPort();
        } else {
            /*if (public_sharedPreferences.getBoolean("ip_port_changed", false)) {
                Log.i("SecurityChooseActivity", "IP和端口改变了！");*/
            if (Tools.NetIsAvilable(SecurityChooseActivity.this)) {
                checkIpAndPort();
                Log.i("SecurityChooseActivity", "进来了");
                editor.putBoolean("ip_port_changed", false);
                editor.apply();
            } else {
                Log.i("SecurityChooseActivity", "网络未连接");
                //Toast.makeText(SecurityChooseActivity.this, "网络未连接，请打开网络！", Toast.LENGTH_SHORT).show();
            }
       /* }*/
        }
        userName.setText(sharedPreferences_login.getString("user_name", "")); //设置登录用户的名称
        if (sharedPreferences_login.getBoolean("user_exchanged", false)) {
            Log.i("user_exchanged", "用户改变了");
        }
    }

   /* @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            setIntent(intent);
            int number = getIntent().getIntExtra("down", 0);
            Log.i("onNewIntent", "onNewIntent进来了！返回的结果=" + number);
            if (number == 1) {     //获取任务选择页面传过来的参数，如果参数为 1 ，则让viewpager显示数据传输fragment
                viewPager.setCurrentItem(1);
                dataTransferRbt.setChecked(true);
            }
            params = getIntent().getExtras();
            if (params != null) {
                Log.i("SecurityChooseFragment=", "bundle不为空");
                task_total_numb = sharedPreferences.getInt("task_total_numb",0);
                Log.i("SecurityChooseFragment=", "task_total_numb=" + task_total_numb);
                stringList = params.getStringArrayList("taskId");
                if (task_total_numb != 0) {
                    stringSet.clear();
                    for (int i = 0; i < task_total_numb; i++) {
                        stringSet.add(stringList.get(i));
                        Log.i("onNewIntent====>", "得到的参数为：" + stringList.get(i));
                    }
                }
                sharedPreferences.edit().putStringSet("stringSet", stringSet).apply();
            }
        }
    }*/

    //设置viewPager
    private void setViewPager() {
        fragmentList = new ArrayList<>();
        //添加fragment到list
        if (SharedPreferencesHelper.getFirm(SecurityChooseActivity.this).equals("渝川安检")){
            fragmentList.add(new SecurityChooseFragment());
        }else {
            fragmentList.add(new SecurityChooseRootFragment());
        }
        fragmentList.add(new MyInfoFragment());
        //避免报空指针
        if (fragmentList != null) {
            adapter = new SecurityCheckViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //请求安检状态网络数据
    private void requireSecurityState(final String method) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url;
                    HttpURLConnection httpURLConnection;
                    Log.i("requireSecurityState", public_sharedPreferences.getString("security_ip", ""));
//                    if (!public_sharedPreferences.getString("security_ip", "").equals("")) {
//                        ip = public_sharedPreferences.getString("security_ip", "");
//                    } else {
//                        ip = "192.168.2.201:";
//                    }
//                    if (!public_sharedPreferences.getString("security_port", "").equals("")) {
//                        port = public_sharedPreferences.getString("security_port", "");
//                    } else {
//                        port = "8080";
//                    }

                   // String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
                    String httpUrl=new StringBuffer().append(SkUrl.SkHttp(SecurityChooseActivity.this)).append(method).toString();
                    url = new URL(httpUrl);
                    Log.i("url=============>", url + "");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
                    httpURLConnection.setRequestProperty("Content-Type", "applicaton/json;charset=UTF-8");
                    httpURLConnection.setConnectTimeout(6000);
                    httpURLConnection.setReadTimeout(6000);
                    httpURLConnection.connect();
                    //传回的数据解析成String
                    if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        stateResult = stringBuilder.toString();
                        Log.i("stateResult=====>", stateResult);
                        JSONObject jsonObject = new JSONObject(stateResult);
                        if (jsonObject.optInt("total", 0) != 0) {
                            handler.sendEmptyMessage(2);
                        } else {
                            handler.sendEmptyMessage(3);
                        }
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException==========>", "网络请求异常!");
                    handler.sendEmptyMessage(1);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //请求安检内容网络数据
    private void requireSecurityContent(final String method) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url;
                    HttpURLConnection httpURLConnection;

                    //String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
                    String httpUrl=new StringBuffer().append(SkUrl.SkHttp(SecurityChooseActivity.this)).append(method).toString();
                    url = new URL(httpUrl);
                    Log.i("url=============>", url + "");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
                    httpURLConnection.setRequestProperty("Content-Type", "applicaton/json;charset=UTF-8");
                    httpURLConnection.setConnectTimeout(6000);
                    httpURLConnection.setReadTimeout(6000);
                    httpURLConnection.connect();
                    //传回的数据解析成String
                    if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        contentResult = stringBuilder.toString();
                        Log.i("contentResult=====>", contentResult);
                        JSONObject jsonObject = new JSONObject(contentResult);
                        if (!jsonObject.optString("total", "").equals("0")) {
                            handler.sendEmptyMessage(4);
                        } else {
                            handler.sendEmptyMessage(5);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException==========>", "网络请求异常!");
                    handler.sendEmptyMessage(1);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //请求安检隐患类型网络数据
    private void requireSafetyHidden(final String method) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url;
                    HttpURLConnection httpURLConnection;

                    //String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
                    String httpUrl=new StringBuffer().append(SkUrl.SkHttp(SecurityChooseActivity.this)).append(method).toString();
                    url = new URL(httpUrl);
                    Log.i("url=============>", url + "");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
                    httpURLConnection.setRequestProperty("Content-Type", "applicaton/json;charset=UTF-8");
                    httpURLConnection.setConnectTimeout(6000);
                    httpURLConnection.setReadTimeout(6000);
                    httpURLConnection.connect();
                    //传回的数据解析成String
                    if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        hiddenResult = stringBuilder.toString();
                        Log.i("hiddenResult=====>", hiddenResult);
                        JSONObject jsonObject = new JSONObject(hiddenResult);
                        if (!jsonObject.optString("total", "").equals("0")) {
                            handler.sendEmptyMessage(6);
                        } else {
                            handler.sendEmptyMessage(7);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException==========>", "网络请求异常!");
                    handler.sendEmptyMessage(1);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //请求安检隐患原因网络数据
    private void requireSafetyReason(final String method) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url;
                    HttpURLConnection httpURLConnection;
                  //  String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
                    String httpUrl=new StringBuffer().append(SkUrl.SkHttp(SecurityChooseActivity.this)).append(method).toString();
                    url = new URL(httpUrl);
                    Log.i("url=============>", url + "");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
                    httpURLConnection.setRequestProperty("Content-Type", "applicaton/json;charset=UTF-8");
                    httpURLConnection.setConnectTimeout(6000);
                    httpURLConnection.setReadTimeout(6000);
                    httpURLConnection.connect();
                    //传回的数据解析成String
                    if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        reasonResult = stringBuilder.toString();
                        Log.i("reasonResult=====>", reasonResult);
                        JSONObject jsonObject = new JSONObject(reasonResult);
                        if (!jsonObject.optString("total", "").equals("0")) {
                            handler.sendEmptyMessage(8);
                        } else {
                            handler.sendEmptyMessage(9);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException==========>", "网络请求异常!");
                    handler.sendEmptyMessage(1);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(SecurityChooseActivity.this, "网络请求超时！", Toast.LENGTH_SHORT).show();
                    Log.i("SecurityChooseActivity", "网络请求超时！");
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(stateResult);
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            stateObject = jsonArray.getJSONObject(j);
                            insertSecurityState();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    if (!sharedPreferences.getBoolean("show_temp_data", false)) {  //显示演示数据
                        Toast.makeText(SecurityChooseActivity.this, "没有任务状态信息！", Toast.LENGTH_SHORT).show();
                    }
                    Log.i("SecurityChooseActivity", "没有任务状态信息！");
                    break;
                case 4:
                    try {
                        JSONObject jsonObject = new JSONObject(contentResult);
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            contentObject = jsonArray.getJSONObject(j);
                            insertSecurityContent();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    if (!sharedPreferences.getBoolean("show_temp_data", false)) {  //显示演示数据
                        Toast.makeText(SecurityChooseActivity.this, "没有安检内容信息！", Toast.LENGTH_SHORT).show();
                    }
                    Log.i("SecurityChooseActivity", "没有安检内容信息！");
                    break;
                case 6:
                    try {
                        JSONObject jsonObject = new JSONObject(hiddenResult);
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            hiddenObject = jsonArray.getJSONObject(j);
                            insertSecurityHidden();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    if (!sharedPreferences.getBoolean("show_temp_data", false)) {  //显示演示数据
                        Toast.makeText(SecurityChooseActivity.this, "没有安全隐患信息！", Toast.LENGTH_SHORT).show();
                    }
                    Log.i("SecurityChooseActivity", "没有安全隐患信息！");
                    break;
                case 8:
                    try {
                        JSONObject jsonObject = new JSONObject(reasonResult);
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            reasonObject = jsonArray.getJSONObject(j);
                            insertSecurityHiddenReason();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    if (!sharedPreferences.getBoolean("show_temp_data", false)) {  //显示演示数据
                        Toast.makeText(SecurityChooseActivity.this, "没有安全隐患原因信息！", Toast.LENGTH_SHORT).show();
                    }
                    Log.i("SecurityChooseActivity", "没有安全隐患原因信息！");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //安检状态数据存到本地数据库安检状态表
    private void insertSecurityState() {
        ContentValues values = new ContentValues();
        values.put("securityId", stateObject.optInt("securityId", 0) + "");
        values.put("securityName", stateObject.optString("securityName", ""));
        db.insert("SecurityState", null, values);
    }

    //安检内容数据存到本地数据库安检内容表
    private void insertSecurityContent() {
        ContentValues values = new ContentValues();
        values.put("securityId", contentObject.optInt("securityId", 0) + "");
        values.put("securityName", contentObject.optString("securityName", ""));
        db.insert("security_content", null, values);
    }

    //安检隐患类型数据存到本地数据库安检内容表
    private void insertSecurityHidden() {
        ContentValues values = new ContentValues();
        values.put("n_safety_hidden_id", hiddenObject.optInt("n_safety_hidden_id", 0) + "");
        values.put("n_safety_hidden_name", hiddenObject.optString("n_safety_hidden_name", ""));
        db.insert("security_hidden", null, values);
    }

    //安检隐患原因数据存到本地数据库安检内容表
    private void insertSecurityHiddenReason() {
        ContentValues values = new ContentValues();
        values.put("n_safety_hidden_reason_id", reasonObject.optInt("n_safety_hidden_reason_id", 0) + "");
        values.put("n_safety_hidden_id", reasonObject.optInt("n_safety_hidden_id", 0) + "");
        values.put("n_safety_hidden_reason_name", reasonObject.optString("n_safety_hidden_reason_name", ""));
        db.insert("security_hidden_reason", null, values);
    }
    /**
     * 捕捉返回事件按钮
     * 因为此 Activity继承 TabActivity,用 onKeyDown无响应，
     * 所以改用 dispatchKeyEvent
     * <p>
     * 一般的 Activity 用 onKeyDown就可以了
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Log.i("exitTime==========>", System.currentTimeMillis() - exitTime + "");
            //-------------Activity.this的context 返回当前activity的上下文，属于activity，activity 摧毁他就摧毁
            //-------------getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放和数据库的连接
        db.close();
    }

}
