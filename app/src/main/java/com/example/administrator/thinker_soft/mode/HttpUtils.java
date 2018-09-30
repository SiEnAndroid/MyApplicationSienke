package com.example.administrator.thinker_soft.mode;

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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpUtils {
   // public String result;  //返回的结果

    //public static HttpUtils instance = null;

//    public HttpUtils() {
//
//    }
//    /**
//     * 初始化
//     *
//     * @return
//     */
//    public static final HttpUtils getInstance() {
//        if (instance == null)
//            instance = new HttpUtils();
//        return instance;
//    }


    public void uploadImage(String httpUrl, final Map<String, Object> map) {
        // TODO Auto-generated method stub
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader in = null;
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(10000); // 连接超时为10秒
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);// 设置请求数据类型并设置boundary部分；
            connection.connect();
            //获取输出流
            DataOutputStream ds = new DataOutputStream(
                    connection.getOutputStream());
            Set<Map.Entry<String, Object>> paramEntrySet = map.entrySet();
            Iterator paramIterator = paramEntrySet.iterator();
            while (paramIterator.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) paramIterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; " + "name=\""
                        + key + "\"" + end + end + value);
                ds.writeBytes(end);
                ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                ds.flush();
            }
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\"file"
                    + "\";filename=\"" + "image1.png" + "\"" + end);
            // ds.writeBytes("Content-type:application/octet-stream");
            ds.writeBytes(end);
            // * 取得文件的FileInputStream *//*
//			FileInputStream fStream = new FileInputStream(
//					file.getAbsolutePath());
            // * 设置每次写入1024bytes *//*
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /*// * 从文件读取数据至缓冲区 *//**//*
            while ((length = inputStream.read(buffer)) != -1) {
                // * 将资料写入DataOutputStream中 *//**//*
                ds.write(buffer, 0, length);
                ds.flush();//刷新数据
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.flush();
            ds.close();
            inputStream.close();*/
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                char[] buf = new char[1024];
                int len = -1;
                while ((len = in.read(buf, 0, buf.length)) != -1) {
                    stringBuilder.append(buf, 0, len);
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("Upload", "result===========>" + stringBuilder.toString());
        /*try {
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            String message = jsonObject.optString("message", "");
            Log.i("Upload", "message=" + message);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    //post提交form-data类型的数据name=value
    public   String postData(String httpUrl, Map<String, Object> map, Map<String, File> fileMap) {
         String result;  //返回的结果
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader in = null;
        String end = "\r\n";//结束符【换行】
        String twoHyphens = "--";//分隔符开头字符
        String boundary = "SJDASJODAODASSD";//
        HttpURLConnection connection = null;
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();//打开连接对象
            connection.setDoInput(true); //允许输入流
            connection.setDoOutput(true);//设置允许输出
            connection.setUseCaches(false);//设置不缓存
            connection.setConnectTimeout(10000); // 连接超时为10秒
            connection.setRequestMethod("POST");//设置请求方式
//            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);// 设置请求数据类型并设置boundary部分；
            connection.connect();//开启连接
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());//获得输出流的对象
//            Iterator paramIterator = map_meter_icon.keySet().iterator();//set集合的迭代器
            Set<Map.Entry<String, Object>> paramEntrySet = map.entrySet();
            Iterator paramIterator = paramEntrySet.iterator();
            while (paramIterator.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) paramIterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();
//                Log.i("postData", "上传的key值为：" + key);
//                Log.i("postData", "上传的value信息为：" + URLEncoder.encode(value.toString(), "UTF-8"));
                ds.writeBytes(twoHyphens + boundary + end);//--SJDASJODAODASSD
                ds.writeBytes("Content-Disposition: form-data;accept-charset=\"utf-8\"; " + "name=\"" + key + "\"" + end + end + URLEncoder.encode(value.toString(), "UTF-8"));//Content-Disposition: form-data; name="key"
                ds.writeBytes(end);
            }
            /*for (String str : map_meter_icon.keySet()) {//循环取出key和value
                Object value = map_meter_icon.get(str);//取得value值
                ds.writeBytes(twoHyphens + boundary + end);//--SJDASJODAODASSD
                ds.writeBytes("Content-Disposition: form-data; " + "name=\"" + URLEncoder.encode(str, "UTF-8") + "\"" + end + end +  value.toString());//Content-Disposition: form-data; name="key"
                Log.i("postData", "上传的key值为：" + str);
                Log.i("postData", "上传的备注信息为：" + value.toString());
                ds.writeBytes(end);
            }*/
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
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            int statusCode = connection.getResponseCode();//获得响应状态码
            Log.i("postData", "postData:相应状态码： " + statusCode);
            if (statusCode == HttpURLConnection.HTTP_OK) {
                char[] buf = new char[1024];
                int len = -1;
                while ((len = in.read(buf, 0, buf.length)) != -1) {
                    stringBuilder.append(buf, 0, len);
                }
            }

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
        result = stringBuilder.toString();
        Log.i("Post", "result===========>" + result);

        return result;
    }
}