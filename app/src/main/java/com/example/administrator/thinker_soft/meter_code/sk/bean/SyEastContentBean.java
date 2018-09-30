package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.util.List;

/**
 * @author g
 * @FileName SyEastContentBean
 * @date 2018/9/29 10:54
 */
public class SyEastContentBean {

    /**
     * total : 7
     * rows : [{"securityId":5,"securityName":"安全隐患","securityType":1,"securityState":1},{"securityId":6,"securityName":"合格","securityType":1,"securityState":1},{"securityId":7,"securityName":"复检合格","securityType":1,"securityState":1},{"securityId":8,"securityName":"拒绝安检","securityType":1,"securityState":1},{"securityId":9,"securityName":"第一次到访不遇","securityType":1,"securityState":1},{"securityId":10,"securityName":"第二次到访不遇","securityType":1,"securityState":1},{"securityId":11,"securityName":"第三次到访不遇","securityType":1,"securityState":1}]
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
         * securityId : 5
         * securityName : 安全隐患
         * securityType : 1
         * securityState : 1
         */

        private int securityId;
        private String securityName;
        private int securityType;
        private int securityState;

        public int getSecurityId() {
            return securityId;
        }

        public void setSecurityId(int securityId) {
            this.securityId = securityId;
        }

        public String getSecurityName() {
            return securityName;
        }

        public void setSecurityName(String securityName) {
            this.securityName = securityName;
        }

        public int getSecurityType() {
            return securityType;
        }

        public void setSecurityType(int securityType) {
            this.securityType = securityType;
        }

        public int getSecurityState() {
            return securityState;
        }

        public void setSecurityState(int securityState) {
            this.securityState = securityState;
        }
    }
}
