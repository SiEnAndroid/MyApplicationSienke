package com.example.administrator.thinker_soft.meter_code.sk.uitls;

import android.text.TextUtils;

import com.example.administrator.thinker_soft.meter_code.sk.bean.AddedBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AuditDonYuBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NewUsersBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportMessageBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityAuditBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityChecksBean;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/6.
 */

public class JsonAnalyUtil {
    /**
     * 解析单条数据
     *
     * @param strResult
     */
    public static List<SecurityAuditBean.AuditBean> analyszeAuditBean(String strResult) {
        List<SecurityAuditBean.AuditBean> dataList = null;
        try {
            dataList = new ArrayList<SecurityAuditBean.AuditBean>();
            JSONArray jsonObArray = new JSONObject(strResult).getJSONArray("list");
            for (int i = 0; i < jsonObArray.length(); i++) {
                JSONObject jsonObj = jsonObArray.getJSONObject(i);
                SecurityAuditBean.AuditBean info = new SecurityAuditBean.AuditBean();
                info.setLxdh(jsonObj.optString("lxdh", "").equals("null") ? "" : jsonObj.optString("lxdh", ""));
                info.setAjbh(jsonObj.optInt("ajbh", 0));
                info.setAqyhyy(jsonObj.optString("aqyhyy", ""));
                info.setAjbz(jsonObj.optString("ajbz", "").equals("null") ? "" : jsonObj.optString("ajbz", ""));
                info.setYhbh(jsonObj.optString("yhbh", ""));
                info.setAjsj(jsonObj.optInt("ajsj", 0));
                info.setAjzt(jsonObj.optString("ajzt", ""));
                info.setAjqk(jsonObj.optString("ajqk", ""));
                info.setXh(jsonObj.optInt("xh", 0));
                info.setAjjhmc(jsonObj.optString("ajjhmc", ""));
                info.setAjy(jsonObj.optString("ajy", ""));
                info.setLbh(jsonObj.optString("lbh", "").equals("null") ? "" : jsonObj.optString("lbh", ""));
                info.setAjlx(jsonObj.optString("ajlx", ""));
                info.setAqyhlx(jsonObj.optString("aqyhlx", ""));
                info.setYhmc(jsonObj.optString("yhmc", ""));
                info.setYhdz(jsonObj.optString("yhdz", ""));
                dataList.add(info);
            }


        } catch (JSONException e) {
            System.out.println("Json parse error");
            e.printStackTrace();
        }
        return dataList;
    }


    /**
     * 解析年计划单条数据
     *
     * @param strResult
     */
    public static List<AddedBean.AddedList> analyszeAdded(String strResult) {
        List<AddedBean.AddedList> dataList = null;
        try {
            dataList = new ArrayList<AddedBean.AddedList>();
            JSONArray jsonObArray = new JSONObject(strResult).getJSONArray("list");
            for (int i = 0; i < jsonObArray.length(); i++) {
                JSONObject jsonObj = jsonObArray.getJSONObject(i);

                AddedBean.AddedList info = new AddedBean.AddedList();

                info.setC_properties_name(jsonObj.optString("c_properties_name", ""));
                info.setXH(jsonObj.optInt("XH", 0));
                info.setC_user_id(jsonObj.optString("c_user_id", ""));
                info.setD_Contract_Date(jsonObj.optLong("d_Contract_Date", 0));
                info.setC_meter_number(jsonObj.optString("c_meter_number", ""));
                info.setC_User_Phone(jsonObj.optString("c_User_Phone", ""));
                info.setC_user_address(jsonObj.optString("c_user_address", ""));
                info.setC_old_user_id(jsonObj.optString("c_old_user_id", ""));
                info.setPre_time(jsonObj.optString("pre_time", ""));
                info.setC_area_name(jsonObj.optString("c_area_name", ""));
                info.setC_user_name(jsonObj.optString("c_user_name", ""));

                dataList.add(info);
            }


        } catch (JSONException e) {
            System.out.println("Json parse error");
            e.printStackTrace();
        }
        return dataList;
    }



    /**
     * 解析新增数据
     *
     * @param strResult
     */
    public static List<NewUsersBean.ListBean> newUsers(String strResult) {
        List<NewUsersBean.ListBean> dataList = null;
        try {
            dataList = new ArrayList<NewUsersBean.ListBean>();
            JSONArray jsonObArray = new JSONObject(strResult).getJSONArray("list");
            for (int i = 0; i < jsonObArray.length(); i++) {
                JSONObject jsonObj = jsonObArray.getJSONObject(i);

                NewUsersBean.ListBean info = new NewUsersBean.ListBean();
                /**
                 * userNumber         用户编号 : 0650100000041
                 * userAddress         用户地址 : 1
                 * propertyName          性质名称 : 居民用户
                 * gasMeterNumber          气表编号 : null
                 * contactNumber         联系电话 : 1
                 * accountOpeningTime         开户时间 : 2018-09-27 11:09:23
                 * partitionName         分区名称 : 默认
                 * lastSecurityCheckTime         上次安检时间 : null
                 * userName         用户名称 : 1
                 */
                info.setAccountOpeningTime(jsonObj.optString("开户时间",""));
                info.setContactNumber(jsonObj.optString("联系电话",""));
                info.setGasMeterNumber(jsonObj.optString("气表编号",""));
                info.setLastSecurityCheckTime(jsonObj.optString("上次安检时间",""));
                info.setPartitionName(jsonObj.optString("分区名称",""));
                info.setPropertyName(jsonObj.optString("性质名称",""));
                info.setUserAddress(jsonObj.optString("用户地址",""));
                info.setUserName(jsonObj.optString("用户名称",""));
                info.setUserNumber(jsonObj.optString("用户编号",""));
                dataList.add(info);
            }


        } catch (JSONException e) {
            System.out.println("Json parse error");
            e.printStackTrace();
        }
        return dataList;
    }




    /**
     * 解析报装数据
     *
     * @param strResult
     */
    public static List<ReportMessageBean> analyszetAdorn(String strResult) {
        List<ReportMessageBean> dataList = null;
        try {
            dataList = new ArrayList<ReportMessageBean>();
            JSONArray jsonObArray = new JSONArray(strResult);
            for (int i = 0; i < jsonObArray.length(); i++) {
                JSONObject jsonObj = jsonObArray.getJSONObject(i);

                ReportMessageBean info = new ReportMessageBean();

//                info.setBzNumber(jsonObj.optDouble("报装编号",0));
//                info.setYwNumber(jsonObj.optDouble("业务编号",0));
//                info.setOperator(jsonObj.optString("操作人",""));
//                info.setTime(jsonObj.optString("申请时间",""));
//                info.setType(jsonObj.optString("报装类型",""));
//                info.setPjType(jsonObj.optString("项目类型",""));
//                info.setName(jsonObj.optString("申请名称",""));
//                info.setApplyNumber(jsonObj.optDouble("申请户数",0));
//                info.setBusinessName(jsonObj.optString("业务名称",""));
//                info.setTransactor(jsonObj.optString("申办人",""));
//                info.setPhone(jsonObj.optString("电话号码",""));
//                info.setCard(jsonObj.optString("身份证",""));
//                info.setClerk(jsonObj.optString("所属公司",""));
//                info.setRemarks(jsonObj.optString("申请备注",""));
                dataList.add(info);
            }


        } catch (JSONException e) {
            System.out.println("Json parse error");
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 解析年计划单条数据
     *
     * @param strResult
     */
    public static SecurityChecksBean analyszeCheck(String strResult) {

        SecurityChecksBean checksBean = null;
//        try {
//            checksBean=new SecurityChecksBean();
//            JSONArray jsonObArray = new JSONObject(strResult).getJSONArray("list");
//            for(int i=0;i<jsonObArray.length();i++){
//                JSONObject jsonObj = jsonObArray.getJSONObject(i);
//
//                AddedBean.AddedList info= new AddedBean.AddedList();
//
//                info.setC_properties_name(jsonObj.optString("c_properties_name",""));
//                info.setXH(jsonObj.optInt("XH",0));
//                info.setC_user_id(jsonObj.optString("c_user_id",""));
//                info.setD_Contract_Date(jsonObj.optLong("d_Contract_Date",0));
//                info.setC_meter_number(jsonObj.optString("c_meter_number",""));
//                info.setC_User_Phone(jsonObj.optString("c_User_Phone",""));
//                info.setC_user_address(jsonObj.optString("c_user_address",""));
//                info.setC_old_user_id(jsonObj.optString("c_old_user_id",""));
//                info.setPre_time(jsonObj.optString("pre_time",""));
//                info.setC_area_name(jsonObj.optString("c_area_name",""));
//                info.setC_user_name(jsonObj.optString("c_user_name",""));
//
//                dataList.add(info);
//            }
//
//
//        } catch (JSONException e) {
//            System.out.println("Json parse error");
//            e.printStackTrace();
//        }
        return checksBean;
    }

    /**
     * 包装数据提交
     *
     * @param strResult
     */
    public static Map<String, String> analyszeTransac(String strResult) {
        Map<String, String> map = new HashMap<String, String>(16);
        try {
            JSONArray jsonObArray = new JSONArray(strResult);
            for (int i = 0; i < jsonObArray.length(); i++) {
                JSONObject jsonObj = jsonObArray.getJSONObject(i);
                map.put("state_id", jsonObj.optInt("state_id", 0) + "");
                map.put("state_text", jsonObj.optString("state_text", ""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                JSONObject jsonObj = new JSONObject(strResult);
                map.put("state_id", jsonObj.optInt("state_id", 0) + "");
                map.put("state_text", jsonObj.optString("state_text", ""));


            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }


        return map;

    }


    /**
     * 将json格式的字符串转成Map对象
     */
    public static Map<String, String> analyszeGetPath(JSONObject jsonObject) {
        Map<String, String> map = new HashMap<String, String>();
        Iterator it = jsonObject.keys();
        // 遍历jsonObject数据，添加到Map对象 
        while (it.hasNext()) {
            String key = String.valueOf(it.next());
            //注意：这里获取value使用的是optString            
            String value = (String) jsonObject.optString(key);
            map.put(key, value);
        }
        return map;

    }
        /**
         * 判断是否为json数据
         * @param json
         * @return
         */
        public static boolean isGoodJson (String json){
            if (TextUtils.isEmpty(json)) {
                return false;
            }
            try {
                new JsonParser().parse(json);
                return true;
            } catch (JsonSyntaxException e) {
                return false;
            } catch (JsonParseException e) {
                return false;
            }
        }


    public static List<AuditDonYuBean.ListBean> AuditDonYu(String body) {

        List<AuditDonYuBean.ListBean> dataList = null;
        try {
            dataList = new ArrayList<AuditDonYuBean.ListBean>();
            JSONArray jsonObArray = new JSONObject(body).getJSONArray("list");
            for (int i = 0; i < jsonObArray.length(); i++) {
                JSONObject jsonObj = jsonObArray.getJSONObject(i);

                AuditDonYuBean.ListBean info = new AuditDonYuBean.ListBean();
                /**
                 * userAddress     用户地址 : 垫江英吉某一个小区
                 * serialNumber    序号 : 1
                 * subject    安检员 : SUPER
                 * tableNumber     表编号 : null
                 * securityTime      安检时间 : 2018-09-21 12:52:14
                 * securityNotes     安检备注 : 是豆腐干
                 * lastSecurityCheckTime     上次安检时间 : null
                 * causesOfPotentialSafetyHazards     安全隐患原因 : 原因3333,原因1
                 * securityNumber      安检编号 : 82
                 * userName     用户名称 : 黄泽
                 * securityStatus     安检状态 : 安检不合格
                 * userNumber     用户编号 : 0650100000006
                 * theOldNumber     老编号 : 01000004
                 * nameOfSecurityCheckPlan     安检计划名称 : 9月计划
                 * contactNumber     联系电话 : 0
                 * securityScreening     安检情况 : 不合格
                 * typesOfSafetyHazards     安全隐患类型 : 隐患类型3,隐患类型1
                 */
                info.setUserAddress(jsonObj.optString("用户地址",""));
                info.setSerialNumber(jsonObj.optString("序号",""));
                info.setSubject(jsonObj.optString("安检员",""));
                info.setTableNumber(jsonObj.optString("表编号",""));
                info.setSecurityTime(jsonObj.optString("安检时间",""));
                info.setSecurityNotes(jsonObj.optString("安检备注",""));
                info.setLastSecurityCheckTime(jsonObj.optString("上次安检时间",""));
                info.setCausesOfPotentialSafetyHazards(jsonObj.optString("安全隐患原因",""));
                info.setSecurityNumber(jsonObj.optString("安检编号",""));
                info.setUserName(jsonObj.optString("用户名称",""));
                info.setSecurityStatus(jsonObj.optString("安检状态",""));
                info.setUserNumber(jsonObj.optString("用户编号",""));
                info.setTheOldNumber(jsonObj.optString("老编号",""));
                info.setNameOfSecurityCheckPlan(jsonObj.optString("安检计划名称",""));
                info.setContactNumber(jsonObj.optString("联系电话",""));
                info.setSecurityScreening(jsonObj.optString("安检情况",""));
                info.setTypesOfSafetyHazards(jsonObj.optString("安全隐患类型",""));

                dataList.add(info);
            }


        } catch (JSONException e) {
            System.out.println("Json parse error");
            e.printStackTrace();
        }
        return dataList;
    }
}
