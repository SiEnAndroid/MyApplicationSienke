package com.example.administrator.thinker_soft.meter_code.sk.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;

public class HttpAsyncTask extends AsyncTask<Void, Void, Response> {

    private Request request;
    private HttpURLConnection connection;
    private HttpCallback httpCallback;
    private Exception exception;

    public HttpAsyncTask(Request request, HttpURLConnection connection, HttpCallback httpCallback) {
        this.connection = connection;
        this.request = request;
        this.httpCallback = httpCallback;
    }

    @Override
    protected Response doInBackground(Void... params) {
        try {
            if (request != null) {
                return request.execute();
            }
        } catch (IOException e) {
            if (connection != null) {
                connection.disconnect();
            }
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (response == null) {
            if (httpCallback != null) {
                if (exception != null) {
                    httpCallback.onError(exception);
                } else {
                    httpCallback.onError(new Exception("UnKnown Exception"));
                }
            }
        } else {
            if (httpCallback != null) {
                httpCallback.onComplete(response);
            }
        }
    }
}
