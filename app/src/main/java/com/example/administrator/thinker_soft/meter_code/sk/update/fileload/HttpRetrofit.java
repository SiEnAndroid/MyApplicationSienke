package com.example.administrator.thinker_soft.meter_code.sk.update.fileload;

import android.content.Context;
import android.database.Cursor;

import com.example.administrator.thinker_soft.Security_check.model.PopupwindowListItem;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpService;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.NetUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtils;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author g
 * @FileName HttpRetrofit
 * @date 2018/9/20 11:28
 */
public class HttpRetrofit {
    private static HttpRetrofit instance;
    private Retrofit retrofit;
    /**
     * 超时时间 10s
     */
    private static final int DEFAULT_TIME_OUT = 15;
    /**
     * 请求失败重连次数
     */
    private int RETRY_COUNT = 0;

    public HttpRetrofit(String tag) {
        retrofit = new Retrofit.Builder()
                //基础url
                .baseUrl(SkUrl.SkHttp(MyApplication.getContext()))
                
                //将返回的数据转为Gson
                .addConverterFactory(GsonConverterFactory.create())
                //添加Rxjava的回调
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //添加okHttpClient
                //.client(tag.equals("进度条")?ProgressHelper.addProgress(null).build():genericClient())
                .client(tag == null ? genericClient() : ProgressHelper.addProgress(null).build())
                .build();

    }

    /**
     * 构建请求
     *
     * @param cls 请求接口
     * @param <T>
     * @return
     */
    public <T> T getApiServise(Class<T> cls) {
        return retrofit.create(cls);
    }


    public static HttpRetrofit getInstance(String tag) {
        if (instance == null) {
            synchronized (HttpRetrofit.class) {
                if (instance == null) {
                    instance = new HttpRetrofit(tag);
                }
            }
        }
        return instance;
    }

    private OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                //连接 超时时间
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                //写操作 超时时间
                .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                //读操作 超时时间
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                //错误重连
                .retryOnConnectionFailure(true)

                .addInterceptor(new Interceptor() {
                    //添加拦截器，可进行其他相关操作
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        request = request
                                .newBuilder()
                                .build();
                        return chain.proceed(request);
                    }
                })
                //okHttpClient log日志打印
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        return httpClient;
    }

    /**
     * @param <T>
     */
    public interface OnRequestCall<T> {
        void onSuccess(T msg);

        void onError(String msg);
    }

    /**
     * 执行网络请求
     */
    public static <T> void setrRequest(Context context, Observable<T> observable,
                                       final OnRequestCall<T> listener) {
        if (!NetUtils.isNetworkAvailable(context)) {
            ToastUtils.showShort(context, "网络不可用,请检查网络");
            return;
        }

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                                   if (listener != null) {
                                       listener.onError(e.toString());
                                   }
                               }

                               @Override
                               public void onComplete() {

                               }

                               @Override
                               public void onSubscribe(Disposable disposable) {

                               }

                               @Override
                               public void onNext(T data) {
                                   if (listener != null) {
                                       listener.onSuccess(data);
                                   }
                               }
                           }
                );
    }

    

    /**
     * 下载单文件
     *
     * @param url                  文件地址
     * @param destDir              存储文件夹
     * @param fileName             存储文件名
     * @param fileDownLoadObserver 监听回调
     */
    public Disposable downloadFile(@NonNull String url, final String destDir, final String fileName, final FileDownLoadObserver<File> fileDownLoadObserver) {
        return getApiServise(HttpService.class)
                .loadFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(@NonNull ResponseBody responseBody) throws Exception {
                        return fileDownLoadObserver.saveFile(responseBody, destDir, fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(@NonNull File file) throws Exception {
                        fileDownLoadObserver.onDownLoadSuccess(file);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        fileDownLoadObserver.onDownLoadFail(throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        fileDownLoadObserver.onComplete();
                    }
                });

    }


    /**
     * 
     * @param listener
     * @param <T>
     * @return
     */
    public static<T> void readOR( Observable<T> observable,final OnRequestCall<T> listener){
        
        observable.subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(T data) {
                if (listener != null) {
                    listener.onSuccess(data);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onError(e.toString());
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 获取按
     *
     * @return
     */
    public static <T> Observable<T> getOnSubscrib(ObservableOnSubscribe<T> onSubscribe) {
        return Observable.create(onSubscribe).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }
    /**
     * 读写数据库
     * @param flowable 
     * @return
     * @param <T>
     */
    public static <T> Flowable<T> readFlowaDb(FlowableOnSubscribe<T> flowable) {
        return Flowable.create(flowable, BackpressureStrategy.BUFFER);
                
    }
    
}
    
    
