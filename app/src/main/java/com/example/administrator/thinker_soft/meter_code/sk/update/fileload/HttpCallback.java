package com.example.administrator.thinker_soft.meter_code.sk.update.fileload;

import com.example.administrator.thinker_soft.meter_code.sk.http.Response;

public interface HttpCallback {
    /**
     * 成功
     * @param response
     */
    void onComplete(Response response);
    /**
     *
     * @param e
     */
    void onError(Throwable e);
}