package com.example.administrator.thinker_soft.meter_code.sk.http;

/**
 * HttpURLConnection网络请求返回监听器
 */
public interface HttpCallbackStringListener {
    // 网络请求成功
    void onFinish(String response);

    // 网络请求失败
    void onError(Exception e);
}
