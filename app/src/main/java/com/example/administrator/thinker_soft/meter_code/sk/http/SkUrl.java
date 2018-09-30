package com.example.administrator.thinker_soft.meter_code.sk.http;

import android.content.Context;
import android.net.Uri;

import com.example.administrator.thinker_soft.Security_check.activity.MoveGuideActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;

import java.net.URLEncoder;

/**
 * Created by Administrator on 2018/3/21.
 */

public class SkUrl {
    //public static  final String SKIP="49.4.70.220";//盛焰
    // public static  final String SKIP="47.94.3.48";//川发展/47.94.3.48 8088 //南部福康218.89.55.103 8088
    private static final String SMDEMO = "/SMDemo/";

    /**
     * 川发展
     */
    public static final String SKIP = "47.94.3.48";
    public static final String SKPORT = "8088";//端口9459 /8088
    /**
     * 苍溪、盛焰
     */
    public static final String SKSY = "49.4.70.220";
    public static final String SKSYPORT = "9459";
    /**
     * 南部、南部福康
     */
    public static final String SKNB = "218.89.55.103";
    public static final String SKNBPORT = "8088";

    /**
     * 渝川移动安检
     */
    public static final String SYNB = "218.70.82.26";
    public static final String SYNBPORT = "9480";

    /**
     * 方根移动安检
     */
    public static final String SYFG = "49.4.70.220";
    public static final String SYFGPORT = "9470";

    /**
     * 渝山
     */
    public static final String SYYS = "222.179.44.86";
    public static final String SYYSPORT = "13003";
    public static final String SYYSPORTS = "13002";

    /**
     * 渝山
     */
//    public static  final String SYYS="88.88.88.104";
//    public static  final String SYYSPORT="13003";
//    public static  final String SYYSPORTS="8080";
    
    /**
     * 东渝移动安检
     */
    public static final String SYDY = "88.88.88.251";
    public static final String SYDYPORT = "8082";
    

    /**
     * MeterDataTransferActivity 数据传输
     */
    public static final String SKURL = "http://47.94.3.48:9459/AppService/GetJsonByAjax.aspx?ajax=meterReadingAdd";
    /**
     * PatrolHomePageActivity
     */
    public static final String SKURLA = "http://88.88.88.253:2000/SMDemo/login.do";
    
    /**
     * 报装url
     */
    public static final String SKWEB = "http://sygzh.cqsyrq.com/WebServer/simulator.html?appPage=App/index.html";

    public static final String LOTURL = "/AppService/GetJsonByAjax.aspx?jsonpcallback=&ajax=";


    public static String encode(String url) {
        if (url == null) {
            return null;
        }
        //去掉所用空格
        String str2 = url.replace(" ", "");
        String allowedChars = "._-$,;~()/";
        String urlEncoded = Uri.encode(url, allowedChars);
        return urlEncoded;
    }

    /**
     * httpURl
     *
     * @param context
     * @return
     */
    public final static String SkHttp(Context context) {
        String ip = SharedPreferencesHelper.getIp(context);
        String port = SharedPreferencesHelper.getPort(context);
        return new StringBuffer().append("http://").append(ip).append(":").append(port).append(SMDEMO).toString();

    }

    /**
     * httpURl
     *
     * @param context
     * @return
     */
    public final static String YSHttp(Context context) {
        String ip = SharedPreferencesHelper.getIp(context);
        return new StringBuffer().append("http://").append(ip).append(":").append(SYYSPORTS).append(LOTURL).toString();

    }


    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {

        }
        return "";
    }


}
