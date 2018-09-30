package com.example.administrator.thinker_soft.meter_code.sk.uitls;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallbackStringListener;
import com.example.administrator.thinker_soft.meter_code.sk.http.ResponseCall;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/10.
 */

public class HttpUtils {
    /**
     * 发送文件post请求
     *
     * @param urlstr
     * @param map
     * @return
     * @throws IOException
     */
    public static void sendFilePosts(final Context context, final String urlstr, final Map<String, Object> map,
                                     final Map<String, File> files, final HttpCallbackStringListener listener) {
        String boundary = "SJDASJODAODASSD";//
        // 因为网络请求是耗时操作，所以需要另外开启一个线程来执行该任务。
//        threadPool.execute(new Runnable() {
//            @Override
//            public void run() {
        URL url;
        HttpURLConnection connection = null;

        try {
            url = new URL(urlstr);
            connection = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            // 设置运行输出
            connection.setDoOutput(true);
            // 设置运行输入
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Android FL");
            connection.setRequestProperty("Charsert", "UTF-8");
            connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            OutputStream out = new DataOutputStream(connection.getOutputStream());
            byte[] end_data = ("--" + boundary + "--\r\n").getBytes();// 定义最后数据分隔线
            // 文件
            if (files != null && !files.isEmpty()) {
                for (Map.Entry<String, File> entry : files.entrySet()) {
                    File file = entry.getValue();
                    //  String fileName = entry.getKey();

                    StringBuilder sb = new StringBuilder();
                    sb.append("--");
                    sb.append(boundary);
                    sb.append("\r\n");
//                            sb.append("Content-Disposition: form-data;name=\"" + fileName
//                                    + "\";filename=\"" + file.getName() + "\"\r\n");
                    sb.append("Content-Disposition: form-data; name=\"file\";filename=\"" + file.getName() + "\"\r\n");
                    sb.append("Content-Type: image/jpg\r\n\r\n");

                    byte[] data = sb.toString().getBytes();
                    out.write(data);

                    DataInputStream in = new DataInputStream(new FileInputStream(
                            file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
                    in.close();
                }
            }

            // 数据参数
            if (map != null && !map.isEmpty()) {

                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("--");
                    sb.append(boundary);
                    sb.append("\r\n");
                    sb.append("Content-Disposition: form-data; name=\""
                            + entry.getKey() + "\"");
                    sb.append("\r\n");
                    sb.append("\r\n");
                    sb.append(entry.getValue());
                    sb.append("\r\n");
                    byte[] data = sb.toString().getBytes();
                    out.write(data);
                }
            }
            out.write(end_data);
            out.flush();
            out.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inStream = connection.getInputStream();
                byte[] number = read(inStream);
                String json = new String(number);
//            return json;
                new ResponseCall(context, listener).doSuccess(json);
            } else {
                new ResponseCall(context, listener).doFail(
                        new NetworkErrorException("response err code:" +
                                connection.getResponseCode()));
            }
        } catch (MalformedURLException e) {
            if (listener != null) {
                // 回调onError()方法
                new ResponseCall(context, listener).doFail(e);
            }
        } catch (IOException e) {
            if (listener != null) {
                // 回调onError()方法
                new ResponseCall(context, listener).doFail(e);
            }
        } finally {
            if (connection != null) {
                // 最后记得关闭连接
                connection.disconnect();
            }
        }
        //     }
//            });
    }



 
    
    /**
     * 读取输入流数据 InputStream
     *
     * @param inStream
     * @return
     * @throws IOException
     */
    private static byte[] read(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
