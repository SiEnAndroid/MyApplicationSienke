package com.example.administrator.thinker_soft.meter_code.sk.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Request {
    private static final int CONNECT_TIME_OUT_MILLISECOND = 10000;
    private static final int READ_TIME_OUT_MILLISECOND = 20000;
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String ACCEPT_CHARSET = "Accept-Charset";
    private static final String TYPE_TEXT_HTML = "text/html";
    private static final String TYPE_JSON = "application/json";

    private String url;
    private String encode;
    private List<String> params;
    private List<String> headers;
    private String tag;
    private String string;
    private HttpMethod method;

    private HttpURLConnection connection;
    private static HttpCallback mHttpCallback;

    private Request(Builder builder) {
        this.url = builder.url;
        this.encode = builder.encode;
        this.tag = builder.tag;
        this.params = builder.params;
        this.headers = builder.headers;
        this.method = builder.method;
        this.string = builder.string;
    }

    public static Request newRequest(Builder builder) {
        return newRequest(builder, null);
    }

    public static Request newRequest(Builder builder, HttpCallback httpCallback) {
        mHttpCallback = httpCallback;
        return new Request(builder);
    }

    /**
     * 异步请求
     */
    public void executeAsync() {
        HttpAsyncTask asyncTask = new HttpAsyncTask(this, connection, mHttpCallback);
        asyncTask.execute();
    }

    /**
     * 同步请求
     */
    public Response execute() throws IOException {
        if (method == HttpMethod.POST) {
            post();
        } else {
            get();
        }
        return response(connection);
    }

    /**
     * get请求
     */
    private void get() throws IOException {
        URL requestUrl = new URL(spliceUrl(url));

        // 打开一个HttpURLConnection连接
        connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod(String.valueOf(method));
        //设置请求中的媒体类型信息。
        connection.setRequestProperty(ACCEPT_CHARSET, encode);
//        connection.setRequestProperty(CONTENT_TYPE, encode);
//        connection.setRequestProperty(CONTENT_TYPE, TYPE_TEXT_HTML);
        connection.setRequestProperty(CONTENT_TYPE, TYPE_JSON);
        // 设置连接主机超时时间
        connection.setConnectTimeout(CONNECT_TIME_OUT_MILLISECOND);
        //设置从主机读取数据超时
        connection.setReadTimeout(READ_TIME_OUT_MILLISECOND);
        connection.setDoInput(true);
        // 设置是否使用缓存  默认是true
        connection.setUseCaches(false);
        //设置请求头信息
        addHeaders();
        // 开始连接
        connection.connect();


    }

    /**
     * get请求拼接url
     */
    private String spliceUrl(String url) {
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("?");
        for (int i = 0; i < params.size(); i += 2) {
            sb.append(params.get(i));
            sb.append("=");
            sb.append(params.get(i + 1));
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        Log.e("pgl","=====url :"+sb.toString());
        return sb.toString();
    }

    /*
     * post请求
     */
    private void post() throws IOException {
        URL requestUrl = new URL(url);
        Log.e("URL===",url);
        connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod(String.valueOf(method));
        connection.setRequestProperty(ACCEPT_CHARSET, encode);
        connection.setRequestProperty(CONTENT_TYPE, encode);
        connection.setRequestProperty(CONTENT_TYPE, TYPE_TEXT_HTML);
        connection.setRequestProperty(CONTENT_TYPE, TYPE_JSON);
        connection.setConnectTimeout(CONNECT_TIME_OUT_MILLISECOND);
        connection.setReadTimeout(READ_TIME_OUT_MILLISECOND);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        addHeaders();
        connection.connect();
        postBody();
    }
    /*
     * post请求
     */
    private void post(Map<String, File> fileMap) throws IOException {
        URL requestUrl = new URL(url);
        Log.e("URL===",url);
        connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod(String.valueOf(method));
        connection.setRequestProperty(ACCEPT_CHARSET, encode);
        connection.setRequestProperty(CONTENT_TYPE, encode);
        connection.setRequestProperty(CONTENT_TYPE, TYPE_TEXT_HTML);
        connection.setRequestProperty(CONTENT_TYPE, TYPE_JSON);
        connection.setConnectTimeout(CONNECT_TIME_OUT_MILLISECOND);
        connection.setReadTimeout(READ_TIME_OUT_MILLISECOND);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        addHeaders();
        connection.connect();
        postBody();
        postFile(fileMap);
    }

    private void postFile(Map<String, File> fileMap) {
        BufferedReader in = null;
        String end = "\r\n";//结束符【换行】
        String twoHyphens = "--";//分隔符开头字符
        String boundary = "SJDASJODAODASSD";//
        try {
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());//获得输出流的对象
            if (!fileMap.isEmpty()) {
                for (String str : fileMap.keySet()) {
                    File file = fileMap.get(str);//取得key值
                    Log.i("file",file.toString());
                    ds.writeBytes(twoHyphens + boundary + end);//--SJDASJODAODASSD
                    ds.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + URLEncoder.encode(file.getName(), "UTF-8") + "\"" + end + "Content-Type: image/jpg" + end + end);//Content-Disposition: form-data; name="key"
                    InputStream is = new FileInputStream(file);
                    // FileInputStream is = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    while ((len = is.read(bytes)) != -1) {
                        ds.write(bytes, 0, len);
                    }
                    is.close();
                    Log.i("postData", "上传的图片key值为：" + str);
                    ds.writeBytes(end);
                }
            }
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);//--SJDASJODAODASSD
            ds.flush();//刷新数据
            ds.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // TODO Auto-generated catch block
            Log.i("MalformedURLException", "IOException:"+e.toString());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i("postData", "postData: IOException:"+e.toString());
            e.printStackTrace();
        }
    }

    /*
     * post请求上传数据
     */
    private void postBody() throws IOException {
        postForm();
        postString();
    }

    /*
     * 上传表单
     */
    private void postForm() throws IOException {
        if (params != null && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.size(); i += 2) {
                sb.append(params.get(i));
                sb.append("=");
                sb.append(params.get(i + 1));
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            Log.e("入参=",sb.toString());
            connection.getOutputStream().write(sb.toString().getBytes(encode));
        }
    }

    /*
     * 上传字符串，如json字符串
     */
    private void postString() throws IOException {
        if (string != null && !string.isEmpty()) {
            Log.e("入参=[",string+"]");
            connection.getOutputStream().write(string.getBytes(encode));
        }
    }

    /*
     * 设置请求头
     */
    private void addHeaders() {
        if (headers != null && headers.size() > 0) {
            for (int i = 0; i < headers.size(); i += 2) {
                connection.setRequestProperty(headers.get(i), headers.get(i + 1));
            }
        }
    }

    /*
     * 获取响应结果
     */
    private Response response(HttpURLConnection connection) throws IOException {
        Response resp = new Response.Builder()
                .code(connection.getResponseCode())
                .message(connection.getResponseMessage())
                .method(connection.getRequestMethod())
                .contentType(connection.getContentType())
                .contentLength(connection.getContentLength())
                .body(getResponseBody(connection.getInputStream()))
                .build();
        connection.disconnect();
        return resp;
    }

    /*
     * 获取响应的数据
     */
    private String getResponseBody(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, encode));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\r\n");
        }
        br.close();
        return sb.toString();
    }

    public String getUrl() {
        return url;
    }

    public String getEncode() {
        return encode;
    }

    public String getTag() {
        return tag;
    }

    public String getRequestMethod() {
        return String.valueOf(method);
    }

    public static class Builder {

        private String url;
        private String encode = "UTF-8";
        private String tag = "";
        private List<String> params = new ArrayList<>();
        private List<String> headers = new ArrayList<>();
        private HttpMethod method = HttpMethod.GET;
        private String string;

        public Builder url(String url) {
            if (url == null || url.isEmpty()) {
                throw new IllegalArgumentException("url can not be null or empty");
            }
            this.url = url;
            return this;
        }

        public Builder encode(String encode) {
            this.encode = encode;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder params(String key, String value) {
            params.add(key);
            params.add(value);
            return this;
        }

        public Builder headers(String key, String values) {
            headers.add(key);
            headers.add(values);
            return this;
        }

        public Builder string(String string) {
            this.string = string;
            return this;
        }

        public Request build() {
            return new Request(this);
        }

    }

}
