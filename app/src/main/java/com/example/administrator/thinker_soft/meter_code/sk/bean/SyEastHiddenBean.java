package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.util.List;

/**
 * @author g
 * @FileName SyEastHiddenBean
 * @date 2018/9/29 10:58
 */
public class SyEastHiddenBean {

    /**
     * total : 4
     * rows : [{"n_safety_hidden_id":2,"n_safety_hidden_name":"线路老化","n_safety_hidden_state":1},{"n_safety_hidden_id":3,"n_safety_hidden_name":"软管老化","n_safety_hidden_state":1},{"n_safety_hidden_id":1,"n_safety_hidden_name":"软管过长","n_safety_hidden_state":1},{"n_safety_hidden_id":4,"n_safety_hidden_name":"气表问题","n_safety_hidden_state":1}]
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
         * n_safety_hidden_id : 2
         * n_safety_hidden_name : 线路老化
         * n_safety_hidden_state : 1
         */

        private int n_safety_hidden_id;
        private String n_safety_hidden_name;
        private int n_safety_hidden_state;

        public int getN_safety_hidden_id() {
            return n_safety_hidden_id;
        }

        public void setN_safety_hidden_id(int n_safety_hidden_id) {
            this.n_safety_hidden_id = n_safety_hidden_id;
        }

        public String getN_safety_hidden_name() {
            return n_safety_hidden_name;
        }

        public void setN_safety_hidden_name(String n_safety_hidden_name) {
            this.n_safety_hidden_name = n_safety_hidden_name;
        }

        public int getN_safety_hidden_state() {
            return n_safety_hidden_state;
        }

        public void setN_safety_hidden_state(int n_safety_hidden_state) {
            this.n_safety_hidden_state = n_safety_hidden_state;
        }
    }
}
