package com.example.administrator.thinker_soft.meter_code.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;

public class MeterDownloadModeActivity extends AppCompatActivity {
    private RadioButton rbAllDownload, rbReaderDownlada,rbBookDownload;
    private ImageView back;
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    private RelativeLayout rlAllDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_download_mode);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        rbAllDownload = (RadioButton) findViewById(R.id.rb_all_download);//1所有
        rbReaderDownlada = (RadioButton) findViewById(R.id.rb_reader_download);//2部分
        rbBookDownload= (RadioButton) findViewById(R.id.rb_reader_book_download);//抄表员
        back = (ImageView) findViewById(R.id.back);
        rlAllDownload = (RelativeLayout) findViewById(R.id.rl_all_download);
    }

    //初始化设置
    private void defaultSetting() {
        if (SharedPreferencesHelper.getFirm(getApplicationContext()).equals("渝山")){
            rlAllDownload.setVisibility(View.GONE);
        }

        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = MeterDownloadModeActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
//        if (sharedPreferences.getBoolean("all_download", true)) {
//            rbAllDownload.setChecked(true);
//        } else {
//         rbReaderDownlada.setChecked(true);
//        }
        if (sharedPreferences.getInt("all_downloads",0)==0){
            rbBookDownload.setChecked(true);
        }else if (sharedPreferences.getInt("all_downloads",0)==1){
            rbAllDownload.setChecked(true);
        }else {
            rbReaderDownlada.setChecked(true);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
//            if (rbAllDownload.isChecked()) {
//           sharedPreferences.edit().putBoolean("all_download", true).apply();
//            } else {
//                sharedPreferences.edit().putBoolean("all_download", false).apply();
//            }
            if (rbBookDownload.isChecked()){
                sharedPreferences.edit().putInt("all_downloads", 0).apply();
            }else if (rbAllDownload.isChecked()){
                sharedPreferences.edit().putInt("all_downloads", 1).apply();
            }else {
                sharedPreferences.edit().putInt("all_downloads", 2).apply();
            }

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //点击事件
    public void setViewClickListener() {
        rbAllDownload.setOnClickListener(clickListener);
        rbReaderDownlada.setOnClickListener(clickListener);
        rbBookDownload.setOnClickListener(clickListener);
        back.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rb_all_download:
                    rbReaderDownlada.setChecked(false);
                    rbBookDownload.setChecked(false);
                    break;
                case R.id.rb_reader_download:
                    rbAllDownload.setChecked(false);
                    rbBookDownload.setChecked(false);
                    break;
                case R.id.rb_reader_book_download:
                    rbAllDownload.setChecked(false);
                    rbReaderDownlada.setChecked(false);
                    break;
                case R.id.back:

//                    if (rbAllDownload.isChecked()) {
//                     sharedPreferences.edit().putBoolean("all_download", true).apply();
//                    } else {
//                     sharedPreferences.edit().putBoolean("all_download", false).apply();
//                    }

                    if (rbBookDownload.isChecked()){
                        sharedPreferences.edit().putInt("all_downloads", 0).apply();
                    }else if (rbAllDownload.isChecked()){
                        sharedPreferences.edit().putInt("all_downloads", 1).apply();
                    }else {
                        sharedPreferences.edit().putInt("all_downloads", 2).apply();
                    }

                    finish();
                    break;
            }
        }
    };
}
