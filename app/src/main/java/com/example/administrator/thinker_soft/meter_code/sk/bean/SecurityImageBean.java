package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 111 on 2018/8/6.
 */

public class SecurityImageBean implements Serializable {

    /**
     * msg : 查询成功
     * list : [{"n_image_id":1560573,"c_data_id":"313940","c_image_name":"TU1.jpg","c_image_remark":"ccssss","b_image_data":null,"c_image_tablekey":"yx_safety_inspection","n_image_width":365,"n_image_height":317,"d_date":null},{"n_image_id":1560574,"c_data_id":"313940","c_image_name":"TU2.jpg","c_image_remark":"iouiouio","b_image_data":null,"c_image_tablekey":"yx_safety_inspection","n_image_width":396,"n_image_height":306,"d_date":null}]
     * status : success
     */

    private String msg;
    private String status;
    private List<ListBean> list;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        /**
         * n_image_id : 1560573
         * c_data_id : 313940
         * c_image_name : TU1.jpg
         * c_image_remark : ccssss
         * b_image_data : null
         * c_image_tablekey : yx_safety_inspection
         * n_image_width : 365
         * n_image_height : 317
         * d_date : null
         */

        private int n_image_id;
        private String c_data_id;
        private String c_image_name;
        private String c_image_remark;
        private String b_image_data;
        private String c_image_tablekey;
        private int n_image_width;
        private int n_image_height;
        private String d_date;

        public int getN_image_id() {
            return n_image_id;
        }

        public void setN_image_id(int n_image_id) {
            this.n_image_id = n_image_id;
        }

        public String getC_data_id() {
            return c_data_id;
        }

        public void setC_data_id(String c_data_id) {
            this.c_data_id = c_data_id;
        }

        public String getC_image_name() {
            return c_image_name;
        }

        public void setC_image_name(String c_image_name) {
            this.c_image_name = c_image_name;
        }

        public String getC_image_remark() {
            return c_image_remark;
        }

        public void setC_image_remark(String c_image_remark) {
            this.c_image_remark = c_image_remark;
        }

        public String getB_image_data() {
            return b_image_data;
        }

        public void setB_image_data(String b_image_data) {
            this.b_image_data = b_image_data;
        }

        public String getC_image_tablekey() {
            return c_image_tablekey;
        }

        public void setC_image_tablekey(String c_image_tablekey) {
            this.c_image_tablekey = c_image_tablekey;
        }

        public int getN_image_width() {
            return n_image_width;
        }

        public void setN_image_width(int n_image_width) {
            this.n_image_width = n_image_width;
        }

        public int getN_image_height() {
            return n_image_height;
        }

        public void setN_image_height(int n_image_height) {
            this.n_image_height = n_image_height;
        }

        public String getD_date() {
            return d_date;
        }

        public void setD_date(String d_date) {
            this.d_date = d_date;
        }
    }
}
