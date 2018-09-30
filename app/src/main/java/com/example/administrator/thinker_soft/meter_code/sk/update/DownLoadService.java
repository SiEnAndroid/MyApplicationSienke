package com.example.administrator.thinker_soft.meter_code.sk.update;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpService;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.DownloadProgressHandler;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.FileDownLoadObserver;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.FileResponseBody;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.HttpRetrofit;
import com.example.administrator.thinker_soft.meter_code.sk.update.fileload.ProgressHelper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class DownLoadService extends Service {

    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File
            .separator + "ThinkerSoft_APP/Update";
    //private   String destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Thinker_APP/";
    /**
     * 目标文件存储的文件名
     */
    private String destFileName = "thinkerSoft.apk";

    private Context mContext;
    private int preProgress = 0;
    private int NOTIFY_ID = 100;
    private NotificationCompat.Builder builder;

    private NotificationManager notificationManager;
    private Retrofit.Builder retrofit;
    private String apkUrl;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        apkUrl=intent.getStringExtra("apkUrl");
        Log.e("url",apkUrl);
        loadFile();
        return super.onStartCommand(intent, flags, startId);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 下载文件
     */
    private void loadFile() {
        initNotification();
        HttpRetrofit.getInstance("进度条").downloadFile(apkUrl, destFileDir, destFileName, new FileDownLoadObserver<File>() {
            @Override
            public void onDownLoadSuccess(File file) {
                Log.e("zs", "请求成功");
                        // 安装软件
                        cancelNotification();
                        installApk(file);
            }

            @Override
            public void onDownLoadFail(Throwable throwable) {
                Log.e("zs", "请求失败");
                     cancelNotification();
            }

            @Override
            public void onProgress(int progress, long total) {
                Log.e("zs", progress + "----" + total);
//               updateNotification(progress * 100 / total);
            }
        });

        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
            //    Log.e("是否在主线程中运行", String.valueOf(Looper.getMainLooper() == Looper.myLooper()));
              //  Log.e("onProgress",String.format("%d%% done\n",(100 * bytesRead) / contentLength));
             //   Log.e("done","--->" + String.valueOf(done));
            //    Log.e("进度","--->" + (100 * bytesRead) / contentLength);
                updateNotification( (100 * bytesRead) / contentLength);
          
            }
        });

//        
//        //https://your.api.url/ 无效
//        retrofit.baseUrl("https://your.api.url/")
//                .client(initOkHttpClient())
//                .build()
//                .create(HttpService.class)
//                .loadFile(apkUrl)
//                .enqueue(new FileCallback(destFileDir, destFileName) {
//                    @Override
//                    public void onSuccess(File file) {
//                        Log.e("zs", "请求成功");
//                        // 安装软件
//                        cancelNotification();
//                        installApk(file);
//                    }
//
//                    @Override
//                    public void onLoading(long progress, long total) {
//                        Log.e("zs", progress + "----" + total);
//                        updateNotification(progress * 100 / total);
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e("zs", "请求失败");
//                        cancelNotification();
//                    }
//                });
    }



    /**
     * 安装软件
     *
     * @param file
     */
    private void installApk(File file) {
       // Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Intent install = new Intent(Intent.ACTION_VIEW);
//        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        install.setDataAndType(uri, "application/vnd.android.package-archive");
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.example.administrator.thinker_soft.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        // 执行意图进行安装
        mContext.startActivity(intent);

    }

    /**
     * 初始化OkHttpClient
     *
     * @return
     */
    private OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(100000, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
                        .body(new FileResponseBody(originalResponse))
                        .build();
            }
        });
        return builder.build();
    }

    /**
     * 初始化Notification通知
     */
    public void initNotification() {
        String channelID = "1";
        String channelName = "channel_name";
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName,NotificationManager.IMPORTANCE_DEFAULT);
            // 设置通知出现时不震动
            channel.enableVibration(false);
            //铃声
            channel.enableLights(false);
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
            builder =new NotificationCompat.Builder(this)
               .setSmallIcon(R.mipmap.app_icon)
                    .setContentText("0%")
                    .setContentTitle("版本更新")
                    .setChannelId(channelID)
                    .setProgress(100, 0, false);
        } else {
            builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentText("0%")
                    .setContentTitle("版本更新")
                    .setProgress(100, 0, false);


        }


        notificationManager.notify(NOTIFY_ID, builder.build());


    }

    /**
     * 更新通知
     */
    public void updateNotification(long progress) {
        int currProgress = (int) progress;
        if (preProgress < currProgress) {
            builder.setContentText(progress + "%");
            builder.setProgress( 100, (int) progress, false);
            notificationManager.notify(NOTIFY_ID, builder.build());
        }
        preProgress = (int) progress;

    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        notificationManager.cancel(NOTIFY_ID);
    }

}
