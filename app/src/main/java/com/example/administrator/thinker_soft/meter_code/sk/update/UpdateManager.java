package com.example.administrator.thinker_soft.meter_code.sk.update;

import android.app.AlertDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.util.Log;
import android.widget.Toast;

import com.example.administrator.thinker_soft.meter_code.sk.uitls.DeviceUtils;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.DownloadProgressHandler;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.ProgressHelper;

/**
 * Created by zs on 2016/7/7.
 */
public class UpdateManager {

    private Context mContext;
    /**版本说明*/
    private String version_info;
    private double version_code;
    private String url;


    public UpdateManager(Context context,String vsInfo,double vsCode,String url) {
        this.mContext = context;
        this.version_info=vsInfo;
        this.version_code=vsCode;
        this.url=url;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate(final boolean isToast) {
        /**
         * 在这里请求后台接口，获取更新的内容和最新的版本号
         */
        // 当前的版本号
        double mVersion_code = Double.parseDouble(DeviceUtils.getVersionName(mContext));
        Log.i("mvesionCode",mVersion_code+"");
        if (mVersion_code < version_code) {
            // 显示提示对话
            showNoticeDialog(version_info);
        } else {
            if (isToast) {
                Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 显示更新对话框
     *
     * @param version_info
     */
    private void showNoticeDialog(String version_info) {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("更新提示");
        builder.setMessage(version_info);
        // 更新
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent  intent=new Intent(mContext, DownLoadService.class);
                intent.putExtra("apkUrl",url);
                mContext.startService(intent);

//                //监听下载进度
//                final ProgressDialog dialogs = new ProgressDialog(mContext);
//                dialogs.setProgressNumberFormat("%1d KB/%2d KB");
//                dialogs.setTitle("更新版本");
//                dialogs.setMessage("正在下载，请稍后...");
//                dialogs.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                dialogs.setCancelable(false);
//                dialogs.show();
//                ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
//                    @Override
//                    protected void onProgress(long bytesRead, long contentLength, boolean done) {
//                     //   Log.e("是否在主线程中运行", String.valueOf(Looper.getMainLooper() == Looper.myLooper()));
//                      //  Log.e("onProgress",String.format("%d%% done\n",(100 * bytesRead) / contentLength));
//                     //   Log.e("done","--->" + String.valueOf(done));
//                        dialogs.setMax((int) (contentLength/1024));
//                        dialogs.setProgress((int) (bytesRead/1024));
//
//                        if(done){
//                            dialogs.dismiss();
//                        }
//                    }
//                });

            }
        });
        // 稍后更新
        builder.setNegativeButton("以后更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();


      
    }
}
