package com.example.administrator.thinker_soft.meter_code.sk.update.fileload;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


public class RxBus {

//    private static volatile RxBus mInstance;
//    private final Subject<Object, Object> bus;
//
//    private RxBus() {
//        bus = new SerializedSubject<>(PublishSubject.create());
//    }
//
//    /**
//     * 单例RxBus
//     *
//     * @return
//     */
//    public static RxBus getDefault() {
//        RxBus rxBus = mInstance;
//        if (mInstance == null) {
//            synchronized (RxBus.class) {
//                rxBus = mInstance;
//                if (mInstance == null) {
//                    rxBus = new RxBus();
//                    mInstance = rxBus;
//                }
//            }
//        }
//        return rxBus;
//    }
//
//    /**
//     * 发送一个新事件
//     *
//     * @param o
//     */
//    public void post(Object o) {
//        bus.onNext(o);
//    }
//
//    /**
//     * 返回特定类型的被观察者
//     *
//     * @param eventType
//     * @param <T>
//     * @return
//     */
//    public <T> Observable<T> toObservable(Class<T> eventType) {
//        return bus.ofType(eventType);
//    }

    private static volatile RxBus mInstance;
    private final Subject<Object> subject = PublishSubject.create().toSerialized();
    private Disposable dispoable;


    private RxBus() {
    }

    public static RxBus getInstance() {
        if (mInstance == null) {
            synchronized (RxBus.class) {
                if (mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }


    /**
     * 发送事件
     * @param object
     */
    public void send(Object object) {
        subject.onNext(object);
    }


    /**
     * @param classType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservale(Class<T> classType) {
        return subject.ofType(classType);
    }


    /**
     * 订阅
     * @param bean
     * @param consumer
     */
    public void subscribe(Class bean, Consumer consumer) {
        dispoable = toObservale(bean).subscribe(consumer);
    }

    /**
     * 取消订阅
     */
    public void unSubcribe(){
        if (dispoable != null && dispoable.isDisposed()){
            dispoable.dispose();
        }

    }
}
