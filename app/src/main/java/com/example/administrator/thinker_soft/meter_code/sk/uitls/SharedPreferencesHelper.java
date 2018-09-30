package com.example.administrator.thinker_soft.meter_code.sk.uitls;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.thinker_soft.Security_check.activity.IpSettingActivity;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/21.
 */

public class SharedPreferencesHelper {

    /**
     * 登录
     */
    public final static String LOGIN_IN = "login_info";
    /**
     * ip和端口
     */
    public final static String DATA_IP = "data";
    /**其他数据*/
    public  final  static String OTHER="other";
    //公司名称
    public final static String SP_FIRM="firm";

    private SharedPreferences sharedPreferences;
    /**
     * 保存手机里面的名字
     */
    private SharedPreferences.Editor editor;

    public SharedPreferencesHelper(Context context, String FILE_NAME) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 存储
     */
    public void putSharedPreference(String key, Object object) {
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /**
     * 获取保存的数据
     */
    public Object getSharedPreference(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sharedPreferences.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultObject);
        } else {
            return sharedPreferences.getString(key, null);
        }
    }

    /**
     * 移除某个key值已经对应的值
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否存在
     */
    public Boolean contain(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    /**
     * 是否登录
     *
     * @return
     */
    public static boolean IsLogin() {
        return false;

    }

    /**
     * if (!public_sharedPreferences.getString("security_ip", "").equals("")) {
     * ip = public_sharedPreferences.getString("security_ip", "");
     * } else {
     * ip = "192.168.2.201:";
     * }
     * if (!public_sharedPreferences.getString("security_port", "").equals("")) {
     * port = public_sharedPreferences.getString("security_port", "");
     * } else {
     * port = "8080";
     * }
     * @return
     */
    public static String getIp(Context context) {
        SharedPreferencesHelper sharedPreferences = new SharedPreferencesHelper(context, SharedPreferencesHelper.DATA_IP);
        String ipStr = (String) sharedPreferences.getSharedPreference("security_ip", "");
        if (!ipStr.equals("")) {
            return ipStr;
        } else {
            //苍溪
            if (getFirm(context).equals("苍溪")){
                return SkUrl.SKSY;
            }else if  (getFirm(context).equals("南部")){
                //南部
                return SkUrl.SKNB;
            } else if(SharedPreferencesHelper.getFirm(context).equals("渝川安检")) {
                //渝川安检
                return SkUrl.SYNB;
            }else if (SharedPreferencesHelper.getFirm(context).equals("方根安检")){
                //方根安检
                return SkUrl.SYFG;
            }else if (SharedPreferencesHelper.getFirm(context).equals("东渝安检")){
                //东渝安检
                return SkUrl.SYDY;
            }else if (SharedPreferencesHelper.getFirm(context).equals("渝山")){
                //渝山
                return SkUrl.SYYS;
            }else {
                return SkUrl.SKIP;
            }

        }
    }
    /**
     * 端口
     *
     * @return
     */
    public static String getPort(Context context) {
        SharedPreferencesHelper sharedPreferences = new SharedPreferencesHelper(context, SharedPreferencesHelper.DATA_IP);
        String portStr = (String) sharedPreferences.getSharedPreference("security_port", "");
        if (!portStr.equals("")) {
            return portStr;
        } else {
            //苍溪
            if (getFirm(context).equals("苍溪")) {
                return SkUrl.SKSYPORT;
            } else if (getFirm(context).equals("南部")) {
                //南部
                return SkUrl.SKNBPORT;
            } else if (SharedPreferencesHelper.getFirm(context).equals("渝川安检")) {
                //渝川
                return SkUrl.SYNBPORT;

            } else if (SharedPreferencesHelper.getFirm(context).equals("方根安检")){
                //方根安检
                return SkUrl.SYFGPORT;
            }else if (SharedPreferencesHelper.getFirm(context).equals("东渝安检")){
                //东渝安检
                return SkUrl.SYDYPORT;
            }else if (SharedPreferencesHelper.getFirm(context).equals("渝山")){
                //渝山
                return SkUrl.SYYSPORT;
            }else{
                return SkUrl.SKPORT;
            }

        }
    }

    /**
     * 公司名称
     * @param context
     * @return
     */
    public static  String getFirm (Context context){
        SharedPreferencesHelper sharedPreferencesfirm=new SharedPreferencesHelper(context,SharedPreferencesHelper.SP_FIRM);
        String portStr = (String) sharedPreferencesfirm.getSharedPreference("firmName", "川发展");
        return portStr;
    }

}
