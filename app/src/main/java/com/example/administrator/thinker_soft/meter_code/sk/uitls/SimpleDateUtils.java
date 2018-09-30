package com.example.administrator.thinker_soft.meter_code.sk.uitls;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/5/2.
 */

public class SimpleDateUtils {


    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getTime() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     * long转换时间
     * @param time
     * @return
     */
    public static String longTime(long time) {
        //long time = Long.parseLong(seconds) * 1000 - 8 * 3600 * 1000;
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        return format2.format(new Date(time));
    }
}
