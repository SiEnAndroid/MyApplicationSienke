package com.example.administrator.thinker_soft.meter_code.sk.http;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试
 * Created by Administrator on 2018/3/22.
 */

public class Text {
    private static String TAG = "TEXT=";
    private ArrayList<String> arrayList = new ArrayList<>();
    Lock lock = new ReentrantLock();

    public static void main(String[] args) {

        final Text test = new Text();
        for (int i = 0; i < 3; i++) {

            final Integer count = i;
            new Thread("甲") {
                public void run() {
                    test.insert(Thread.currentThread(), count);
                }
            }.start();
            new Thread("乙") {
                public void run() {
                    test.insert(Thread.currentThread(), count);
                }

            }.start();

        }
        for (String str : test.arrayList) {
            System.out.println(str);
        }


    }

//        StuThread1 stuThread1 = new StuThread1();
//        StuThread2 stuThread2 = new StuThread2();
//        for (int i = 0; i < 2; i++) {
//            new Thread(stuThread1).start();
//            new Thread(stuThread2).start();
//        }


//    private static synchronized void writeLog() {
//        for (int i = 0; i < 3; i++) {
//            try {
//                Log.e(TAG, "showLog: " + Thread.currentThread().getName() + "写入中");
//                Thread.sleep(new Random().nextInt(1000));
//                Log.e(TAG, "showLog: " + Thread.currentThread().getName() + "写入完成");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private static void readLog() {
//        synchronized (this) {
//            for (int i = 0; i < 3; i++) {
//                try {
//                    Log.e(TAG, "showLog: " + Thread.currentThread().getName() + "读取中");
//                    Thread.sleep(new Random().nextInt(1000));
//                    Log.e(TAG, "showLog: " + Thread.currentThread().getName() + "读取完成");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//static class  StuThread1 implements Runnable {
//        @Override
//        public void run() {
//            writeLog();
//        }
//    }
//
//    static class StuThread2 implements Runnable {
//        @Override
//        public void run() {
//            readLog();
//        }
//    }


    public void insert(Thread thread, Integer count) {

        lock.lock();
        try {
            //线程获取到了锁
            for (int i = 0; i < 5; i++) {
                arrayList.add("第" + count + "次" + "线程" + thread.getName() + i);
            }
        } catch (Exception e) {

        } finally {
            //线程释放锁
            lock.unlock();
        }
    }

}
