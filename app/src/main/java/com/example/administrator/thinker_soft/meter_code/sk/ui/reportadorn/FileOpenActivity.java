package com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.OpenFiles;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author g
 * @FileName FileOpenActivity
 * @date 2018/9/26 11:27
 */
public class FileOpenActivity extends Activity {
private String path="sdcard/test5.text";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_file);
        ButterKnife.bind(this);
        
        path=getIntent().getStringExtra("path");
    }


    @OnClick({R.id.tv_text, R.id.tv_word, R.id.tv_ppt, R.id.tv_pdf, R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_text:
                try {
                    // sdcard/test5.text为本地apk文件的路径           
                    Intent intent = OpenFiles.getTextFileIntent(path);
                    startActivity(intent);
                } catch (Exception e) {
                    //没有安装第三方的软件会提示                 
                    ToastUtil.showShort(FileOpenActivity.this, "没有找到打开该文件的应用程序");
                }
                break;
            case R.id.tv_word:
                try {
                    //  sdcard/test2.docx为本地doc文件的路径                   
                    Intent intent = OpenFiles.getWordFileIntent(path);
                    startActivity(intent);
                } catch (Exception e) {
                    //没有安装第三方的软件会提示           
                    ToastUtil.showShort(FileOpenActivity.this, "没有找到打开该文件的应用程序");
                }
                break;
            case R.id.tv_ppt:
                try {
                    // sdcard/test4.ppt为本地apk文件的路径             
                    Intent intent = OpenFiles.getPPTFileIntent(path);
                    startActivity(intent);
                } catch (Exception e) {
                    //没有安装第三方的软件会提示             
                    ToastUtil.showShort(FileOpenActivity.this, "没有找到打开该文件的应用程序");
                }
                break;
            case R.id.tv_pdf:
                try {
                    //  sdcard/test1.pdf为本地pdf文件的路径           
                    Intent intent = OpenFiles.getPdfFileIntent(path);
                    startActivity(intent);
                } catch (Exception e) {
                    //没有安装第三方的软件会提示                   
                    ToastUtil.showShort(FileOpenActivity.this, "没有找到打开该文件的应用程序");
                }
                break;
            case R.id.tv_back:
                finish();
                break;
            default:
                break;
        }
    }

  
}
    
