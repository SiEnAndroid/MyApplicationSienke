package com.example.administrator.thinker_soft.meter_code.sk.update.fileload;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class FileCallback implements Callback<ResponseBody>{
    /**
     * 订阅下载进度
     */
    //private CompositeSubscription rxSubscriptions = new CompositeSubscription();
    private  final CompositeDisposable disposables = new CompositeDisposable();

    Observable observable;
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;

    public FileCallback(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
        subscribeLoadProgress();// 订阅下载进度
    }
    /**
     * 成功后回调
     */
    public abstract void onSuccess(File file);

    /**
     * 下载过程回调
     */
    public abstract void onLoading(long progress, long total);

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            saveFile(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File saveFile(Response<ResponseBody> response) throws Exception {
        InputStream in = null;
        FileOutputStream out = null;
        byte[] buf = new byte[2048*10];
        int len;
        try {
            File dir = new File(destFileDir);
            // 如果文件不存在新建一个
            if (!dir.exists()) {
                dir.mkdirs();
            }
            in = response.body().byteStream();
          File file = new File(dir,destFileName);
            out = new FileOutputStream(file);
            while ((len = in.read(buf)) != -1){
                out.write(buf,0,len);
            }
            // 回调成功的接口
            onSuccess(file);
            unSubscribe();// 取消订阅
            return file;
        }finally {
            in.close();
            out.close();
        }
    }
    /**
     * 订阅文件下载进度
     */
    private void subscribeLoadProgress() {

        disposables.add(RxBus.getInstance()
                .toObservale(FileLoadingBean.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileLoadingBean>() {
                    @Override
                    public void accept(FileLoadingBean fileLoadingBean) throws Exception {
                        onLoading(fileLoadingBean.getProgress(), fileLoadingBean.getTotal());
                    }
                }));
//        rxSubscriptions.add(RxBus.getDefault()
//                .toObservable(FileLoadingBean.class)
//                .onBackpressureBuffer()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<FileLoadingBean>() {
//                    @Override
//                    public void call(FileLoadingBean fileLoadEvent) {
//                        onLoading(fileLoadEvent.getProgress(), fileLoadEvent.getTotal());
//                    }
//                }));
    }


//    /**
//     * 取消订阅，防止内存泄漏
//     */
//    private void unSubscribe() {
//        if (!rxSubscriptions.isUnsubscribed()) {
//            rxSubscriptions.unsubscribe();
//        }
//    }

    /**
     * 取消订阅，防止内存泄漏
     */
    public void unSubscribe(){
        if (disposables != null&& disposables.isDisposed()) {
            disposables.dispose();
        }
    }
//
//    /**
//     * rxjava进行注册
//     * @param disposable
//     */
//
//    public void addSubscription(Disposable disposable) {//rxjava进行注册
//        if (disposables == null) {
//            disposables= new CompositeDisposable();
//        }
//        disposables.add(disposable);
//    }
}
