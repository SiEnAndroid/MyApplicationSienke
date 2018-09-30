package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.util.List;

/**
 * @author g
 * @FileName SyEastStateBean
 * @date 2018/9/29 10:49
 */
public class SyEastStateBean {

    /**
     * total : 4
     * rows : [{"securityId":1,"securityName":"常规安检","securityType":0,"securityState":1},{"securityId":2,"securityName":"年度安检","securityType":0,"securityState":1},{"securityId":3,"securityName":"复检","securityType":0,"securityState":1},{"securityId":4,"securityName":"通气安检","securityType":0,"securityState":1}]
     * message : null
     */

    private int total;
    private String message;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
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
         * securityId : 1
         * securityName : 常规安检
         * securityType : 0
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
