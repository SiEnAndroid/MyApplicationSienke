package com.example.administrator.thinker_soft.meter_code.sk.update.fileload;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * @author g
 * @FileName ProgressHandler
 * @date 2018/9/25 15:48
 */
public abstract class ProgressHandler {
    protected abstract void sendMessage(ProgressBean progressBean);

    protected abstract void handleMessage(Message message);

    protected abstract void onProgress(long progress, long total, boolean done);

    protected static class ResponseHandler extends Handler {

        private ProgressHandler mProgressHandler;
        public ResponseHandler(ProgressHandler mProgressHandler, Looper looper) {
            super(looper);
            this.mProgressHandler = mProgressHandler;
        }

        @Override
        public void handleMessage(Message msg) {
            mProgressHandler.handleMessage(msg);
        }
    }

}
