package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.util.List;

/**
 * @author g
 * @FileName SyEastReasonBean
 * @date 2018/9/29 11:01
 */
public class SyEastReasonBean {

    /**
     * total : 4
     * rows : [{"n_safety_hidden_reason_id":4,"n_safety_hidden_id":4,"n_safety_hidden_reason_name":"气表锈蚀严重","n_safety_hidden_reason_steat":1},{"n_safety_hidden_reason_id":2,"n_safety_hidden_id":2,"n_safety_hidden_reason_name":"线路老化","n_safety_hidden_reason_steat":1},{"n_safety_hidden_reason_id":1,"n_safety_hidden_id":3,"n_safety_hidden_reason_name":"软管老化","n_safety_hidden_reason_steat":1},{"n_safety_hidden_reason_id":3,"n_safety_hidden_id":1,"n_safety_hidden_reason_name":"软管超过两米","n_safety_hidden_reason_steat":1}]
     * message : null
     */

    private int total;
    private Object message;
    private List<RowsBean> rows;

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

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * n_safety_hidden_reason_id : 4
         * n_safety_hidden_id : 4
         * n_safety_hidden_reason_name : 气表锈蚀严重
         * n_safety_hidden_reason_steat : 1
         */

        private int n_safety_hidden_reason_id;
        private int n_safety_hidden_id;
        private String n_safety_hidden_reason_name;
        private int n_safety_hidden_reason_steat;

        public int getN_safety_hidden_reason_id() {
            return n_safety_hidden_reason_id;
        }

        public void setN_safety_hidden_reason_id(int n_safety_hidden_reason_id) {
            this.n_safety_hidden_reason_id = n_safety_hidden_reason_id;
        }

        public int getN_safety_hidden_id() {
            return n_safety_hidden_id;
        }

        public void setN_safety_hidden_id(int n_safety_hidden_id) {
            this.n_safety_hidden_id = n_safety_hidden_id;
        }

        public String getN_safety_hidden_reason_name() {
            return n_safety_hidden_reason_name;
        }

        public void setN_safety_hidden_reason_name(String n_safety_hidden_reason_name) {
            this.n_safety_hidden_reason_name = n_safety_hidden_reason_name;
        }

        public int getN_safety_hidden_reason_steat() {
            return n_safety_hidden_reason_steat;
        }

        public void setN_safety_hidden_reason_steat(int n_safety_hidden_reason_steat) {
            this.n_safety_hidden_reason_steat = n_safety_hidden_reason_steat;
        }
    }
}
