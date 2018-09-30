package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.util.List;

/**
 * @author g
 * @FileName SyEastUserBean
 * @date 2018/9/29 18:42
 */
public class SyEastUserBean {

    /**
     * total : 10
     * rows : [{"safetyPlan":44,"c_properties_name":"商业用户","d_safety_inspection_date":null,"securityName":"年度安检","meterNumber":"13061077800","n_buy_sum_dosage":"69070","oldUserId":"1983","userPhone":null,"safetyInspectionId":11713,"userName":"马波","userAdress":"食堂（老冶牛肉面馆）","userId":"0800200101983"},{"safetyPlan":44,"c_properties_name":"商业用户","d_safety_inspection_date":null,"securityName":"年度安检","meterNumber":"1511036601","n_buy_sum_dosage":"24300","oldUserId":"3469","userPhone":"00000000","safetyInspectionId":11712,"userName":"蔡永健","userAdress":"食堂（农家风情园）","userId":"0800200103468"},{"safetyPlan":44,"c_properties_name":"商业用户","d_safety_inspection_date":null,"securityName":"年度安检","meterNumber":"0702762@115","n_buy_sum_dosage":"2500","oldUserId":"3842","userPhone":"8568724","safetyInspectionId":11710,"userName":"阿布都如苏丽吐拉甫","userAdress":"食堂（皇吐马尔餐厅）","userId":"0800200103841"},{"safetyPlan":44,"c_properties_name":"商业用户","d_safety_inspection_date":null,"securityName":"年度安检","meterNumber":"0702239","n_buy_sum_dosage":"18025","oldUserId":"4811","userPhone":"13909986606-8522496","safetyInspectionId":11708,"userName":"程玉龙","userAdress":"锅炉（甫华大厦门面47号）","userId":"0800200104809"},{"safetyPlan":44,"c_properties_name":"商业用户","d_safety_inspection_date":null,"securityName":"年度安检","meterNumber":"1511036495","n_buy_sum_dosage":"1000","oldUserId":"5764609036890935897","userPhone":"15882435795","safetyInspectionId":11707,"userName":"孙先国","userAdress":"金海国际 22号楼2号楼2号门面","userId":"0800200151285"},{"safetyPlan":44,"c_properties_name":"商业用户","d_safety_inspection_date":null,"securityName":"年度安检","meterNumber":"0806720","n_buy_sum_dosage":"31500","oldUserId":"13658","userPhone":"13220035707","safetyInspectionId":11704,"userName":"陈武康","userAdress":"食堂","userId":"0800200113648"},{"safetyPlan":44,"c_properties_name":"商业用户","d_safety_inspection_date":null,"securityName":"年度安检","meterNumber":"1510846525","n_buy_sum_dosage":"2800","oldUserId":"16046","userPhone":"13899142952","safetyInspectionId":11711,"userName":"张勇","userAdress":"锅炉","userId":"0800200116035"},{"safetyPlan":44,"c_properties_name":"商业用户","d_safety_inspection_date":null,"securityName":"年度安检","meterNumber":"1511036554","n_buy_sum_dosage":"15945","oldUserId":"37301","userPhone":null,"safetyInspectionId":11705,"userName":"阿布都尼比江吾麦尔","userAdress":"莎车县食堂商业用户扎贺热特色大盘鸡（依米提马木提门面）","userId":"0800200137282"},{"safetyPlan":44,"c_properties_name":"商业用户","d_safety_inspection_date":null,"securityName":"年度安检","meterNumber":"1511036780","n_buy_sum_dosage":"10175","oldUserId":"40801","userPhone":null,"safetyInspectionId":11706,"userName":"艾合麦提麦麦提","userAdress":"莎车县食堂商业用户（奥尔达餐厅）","userId":"0800200140772"},{"safetyPlan":44,"c_properties_name":"商业用户","d_safety_inspection_date":null,"securityName":"年度安检","meterNumber":"0807512","n_buy_sum_dosage":"39600","oldUserId":"6859","userPhone":"00000000","safetyInspectionId":11709,"userName":"赵建华","userAdress":"食堂（万德福）","userId":"0800200106856"}]
     * message : null
     * row : null
     * isExist : null
     */

    private int total;
    private Object message;
    private Object row;
    private Object isExist;
    private List<UserRowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getRow() {
        return row;
    }

    public void setRow(Object row) {
        this.row = row;
    }

    public Object getIsExist() {
        return isExist;
    }

    public void setIsExist(Object isExist) {
        this.isExist = isExist;
    }

    public List<UserRowsBean> getRows() {
        return rows;
    }

    public void setRows(List<UserRowsBean> rows) {
        this.rows = rows;
    }

    public static class UserRowsBean {
        /**
         * safetyPlan : 44
         * c_properties_name : 商业用户
         * d_safety_inspection_date : null
         * securityName : 年度安检
         * meterNumber : 13061077800
         * n_buy_sum_dosage : 69070
         * oldUserId : 1983
         * userPhone : null
         * safetyInspectionId : 11713
         * userName : 马波
         * userAdress : 食堂（老冶牛肉面馆）
         * userId : 0800200101983
         */

        private int safetyPlan;
        private String c_properties_name;
        private String d_safety_inspection_date;
        private String securityName;
        private String meterNumber;
        private String n_buy_sum_dosage;
        private String oldUserId;
        private String userPhone;
        private int safetyInspectionId;
        private String userName;
        private String userAdress;
        private String userId;

        public int getSafetyPlan() {
            return safetyPlan;
        }

        public void setSafetyPlan(int safetyPlan) {
            this.safetyPlan = safetyPlan;
        }

        public String getC_properties_name() {
            return c_properties_name;
        }

        public void setC_properties_name(String c_properties_name) {
            this.c_properties_name = c_properties_name;
        }

        public String getD_safety_inspection_date() {
            return d_safety_inspection_date;
        }

        public void setD_safety_inspection_date(String d_safety_inspection_date) {
            this.d_safety_inspection_date = d_safety_inspection_date;
        }

        public String getSecurityName() {
            return securityName;
        }

        public void setSecurityName(String securityName) {
            this.securityName = securityName;
        }

        public String getMeterNumber() {
            return meterNumber;
        }

        public void setMeterNumber(String meterNumber) {
            this.meterNumber = meterNumber;
        }

        public String getN_buy_sum_dosage() {
            return n_buy_sum_dosage;
        }

        public void setN_buy_sum_dosage(String n_buy_sum_dosage) {
            this.n_buy_sum_dosage = n_buy_sum_dosage;
        }

        public String getOldUserId() {
            return oldUserId;
        }

        public void setOldUserId(String oldUserId) {
            this.oldUserId = oldUserId;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public int getSafetyInspectionId() {
            return safetyInspectionId;
        }

        public void setSafetyInspectionId(int safetyInspectionId) {
            this.safetyInspectionId = safetyInspectionId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserAdress() {
            return userAdress;
        }

        public void setUserAdress(String userAdress) {
            this.userAdress = userAdress;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
