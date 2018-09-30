package com.example.administrator.thinker_soft.meter_code.sk.uitls;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/2.
 */

public class PrintUtils {
    /**
     * 打印纸一行最大的字节
     */
   // private static final int LINE_BYTE_SIZE = 32;
    private static final int LINE_BYTE_SIZE = 32;
    private static final int LINE_BYTE_SIZES = 28;
    private static final int LEFT_LENGTH = 20;

    private static final int RIGHT_LENGTH = 12;

    /**
     * 左侧汉字最多显示几个文字
     */
    private static final int LEFT_TEXT_MAX_LENGTH = 8;

    /**
     * 小票打印的名称，上限调到8个字
     */
    public static final int MEAL_NAME_MAX_LENGTH = 8;

    private static StringBuffer sb = new StringBuffer();

    /**
     * 排版居中标题
     *
     * @param title
     * @return
     */
    public static String printTitle(String title) {
        sb.delete(0, sb.length());
        //前面一半用空格填充
        for (int i = 0; i < (LINE_BYTE_SIZE - getBytesLength(title)) / 2; i++) {
            sb.append(" ");
        }
        sb.append(title);
        return sb.toString();
    }

    /**
     * 排版居中标题
     *
     * @param text
     * @return
     */
    public static String printLine(String text) {
        sb.delete(0, sb.length());
        //前面一半用空格填充
        for (int i = 0; i < (LINE_BYTE_SIZE - getBytesLength(text)) / 2; i++) {
            sb.append(" ");
        }
        sb.append(text);
        return sb.toString();
    }

    /**
     * 换行
     * @param text
     * @param max
     * @return
     */
    public static String setLength(String text,int max) {
        sb.delete(0, sb.length());
        Log.e("长度=",getBytesLength(text)+"");
        for (int x = 0; x < text.length(); x++) {
            if (x!=0 && x % max == 0){
               sb.append("\n");
            }
           sb.append(text.charAt(x));

        }
        return sb.toString();
}
    /**
     * 换行
     * @param text

     * @return
     */
    public static String setLengths(String text) {
        sb.delete(0, sb.length());
        for (int x = 0; x < text.length(); x++) {
            if (x ==12){
                sb.append("\n");
            }
            sb.append(text.charAt(x));

        }


        return sb.toString();
    }

    /**
     * 获取最大长度
     *
     * @param msgs
     * @return
     */
    private static int getMaxLength(Object[] msgs) {
        int max = 0;
        int tmp;
        for (Object oo : msgs) {
            tmp = getBytesLength(oo.toString());
            if (tmp > max) {
                max = tmp;
            }
        }
        return max;
    }

    /**
     * 排版居中内容(以':'对齐)
     *
     * @param
     * @return
     */
    public static String printMiddleMsg(LinkedHashMap<String, String> middleMsgMap) {

        sb.delete(0, sb.length());
        String separated = ":";
        int leftLength = (LINE_BYTE_SIZE - getBytesLength(separated)) / 2;
        for (Map.Entry<String, String> middleEntry : middleMsgMap.entrySet()) {
            for (int i = 0; i < (leftLength - getBytesLength(middleEntry.getKey())); i++) {
                sb.append(" ");
            }
            sb.append(middleEntry.getKey() + "：" + middleEntry.getValue());
        }
        return sb.toString();
    }

    /**
     * 打印两列
     *
     * @param leftText  左侧文字
     * @param rightText 右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printTwoData(String leftText, String rightText) {
        StringBuilder sb = new StringBuilder();
        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);
        sb.append(leftText);

        // 计算两侧文字中间的空格
        int marginBetweenMiddleAndRight = LINE_BYTE_SIZE - leftTextLength - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightText);
        return sb.toString();
    }

    /**
     * 打印三列
     *
     * @param leftText   左侧文字
     * @param middleText 中间文字
     * @param rightText  右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printThreeData(String leftText, String middleText, String rightText) {
        StringBuilder sb = new StringBuilder();
        // 左边最多显示 LEFT_TEXT_MAX_LENGTH 个汉字 + 两个点
        if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
            leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH) + "..";
        }
        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        // 计算左侧文字和中间文字的空格长度
        int marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;

        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(" ");
        }
        sb.append(middleText);

        // 计算右侧文字和中间文字的空格长度
        int marginBetweenMiddleAndRight = RIGHT_LENGTH - middleTextLength / 2 - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }

        // 打印的时候发现，最右边的文字总是偏右一个字符，所以需要删除一个空格
        sb.delete(sb.length() - 1, sb.length()).append(rightText);
        return sb.toString();
    }

    /**
     * 获取数据长度
     *
     * @param msg
     * @return
     */
    @SuppressLint("NewApi")
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }

    /**
     * 格式化名称，最多显示MEAL_NAME_MAX_LENGTH个数
     *
     * @param name
     * @return
     */
    public static String formatMealName(String name) {
        if (TextUtils.isEmpty(name)) {
            return name;
        }
        if (name.length() > MEAL_NAME_MAX_LENGTH) {
            return name.substring(0, 8) + "..";
        }
        return name;
    }

}
