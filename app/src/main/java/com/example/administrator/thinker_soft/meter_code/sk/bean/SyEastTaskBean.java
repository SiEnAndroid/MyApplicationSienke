package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author g
 * @FileName SyEastTaskBean
 * @date 2018/9/29 16:00
 */
public class SyEastTaskBean implements Serializable {

    /**
     * msg : 查询成功
     * list : [{"COUNTRS":12,"REMAINCOUNTS":11,"N_ANJIAN_PLAN_ID":73,"C_ANJIAN_STAFF":"SUPER","C_ANJIAN_REMARK":null,"C_ANJIAN_PLAN_NAME":"计划时间测试2","D_ANJIAN_END":1538064000000,"C_COMPANY_NAME":"垫江东渝然气有限公司","D_ANJIAN_START":1535731200000},{"COUNTRS":3,"REMAINCOUNTS":2,"N_ANJIAN_PLAN_ID":71,"C_ANJIAN_STAFF":"YCKK,SUPER","C_ANJIAN_REMARK":"测试","C_ANJIAN_PLAN_NAME":"9月计划","D_ANJIAN_END":1538236800000,"C_COMPANY_NAME":"垫江东渝然气有限公司","D_ANJIAN_START":1535731200000},{"COUNTRS":3,"REMAINCOUNTS":1,"N_ANJIAN_PLAN_ID":70,"C_ANJIAN_STAFF":"SUPER","C_ANJIAN_REMARK":null,"C_ANJIAN_PLAN_NAME":"制定计划","D_ANJIAN_END":1538236800000,"C_COMPANY_NAME":"垫江东渝然气有限公司","D_ANJIAN_START":1535731200000},{"COUNTRS":10,"REMAINCOUNTS":7,"N_ANJIAN_PLAN_ID":72,"C_ANJIAN_STAFF":"cs,SUPER","C_ANJIAN_REMARK":"测试","C_ANJIAN_PLAN_NAME":"安检测试1","D_ANJIAN_END":1538236800000,"C_COMPANY_NAME":"垫江东渝然气有限公司","D_ANJIAN_START":1535731200000}]
     * status : success
     */

    private String msg;
    private String status;
    private List<TaskListBean> list;

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

    public List<TaskListBean> getList() {
        return list;
    }

    public void setList(List<TaskListBean> list) {
        this.list = list;
    }

    public static class TaskListBean implements Serializable{
        /**
         * COUNTRS : 12
         * REMAINCOUNTS : 11
         * N_ANJIAN_PLAN_ID : 73
         * C_ANJIAN_STAFF : SUPER
         * C_ANJIAN_REMARK : null
         * C_ANJIAN_PLAN_NAME : 计划时间测试2
         * D_ANJIAN_END : 1538064000000
         * C_COMPANY_NAME : 垫江东渝然气有限公司
         * D_ANJIAN_START : 1535731200000
         */

        private int COUNTRS;
        private int REMAINCOUNTS;
        private int N_ANJIAN_PLAN_ID;
        private String C_ANJIAN_STAFF;
        private Object C_ANJIAN_REMARK;
        private String C_ANJIAN_PLAN_NAME;
        private long D_ANJIAN_END;
        private String C_COMPANY_NAME;
        private long D_ANJIAN_START;

        public int getCOUNTRS() {
            return COUNTRS;
        }

        public void setCOUNTRS(int COUNTRS) {
            this.COUNTRS = COUNTRS;
        }

        public int getREMAINCOUNTS() {
            return REMAINCOUNTS;
        }

        public void setREMAINCOUNTS(int REMAINCOUNTS) {
            this.REMAINCOUNTS = REMAINCOUNTS;
        }

        public int getN_ANJIAN_PLAN_ID() {
            return N_ANJIAN_PLAN_ID;
        }

        public void setN_ANJIAN_PLAN_ID(int N_ANJIAN_PLAN_ID) {
            this.N_ANJIAN_PLAN_ID = N_ANJIAN_PLAN_ID;
        }

        public String getC_ANJIAN_STAFF() {
            return C_ANJIAN_STAFF;
        }

        public void setC_ANJIAN_STAFF(String C_ANJIAN_STAFF) {
            this.C_ANJIAN_STAFF = C_ANJIAN_STAFF;
        }

        public Object getC_ANJIAN_REMARK() {
            return C_ANJIAN_REMARK;
        }

        public void setC_ANJIAN_REMARK(Object C_ANJIAN_REMARK) {
            this.C_ANJIAN_REMARK = C_ANJIAN_REMARK;
        }

        public String getC_ANJIAN_PLAN_NAME() {
            return C_ANJIAN_PLAN_NAME;
        }

        public void setC_ANJIAN_PLAN_NAME(String C_ANJIAN_PLAN_NAME) {
            this.C_ANJIAN_PLAN_NAME = C_ANJIAN_PLAN_NAME;
        }

        public long getD_ANJIAN_END() {
            return D_ANJIAN_END;
        }

        public void setD_ANJIAN_END(long D_ANJIAN_END) {
            this.D_ANJIAN_END = D_ANJIAN_END;
        }

        public String getC_COMPANY_NAME() {
            return C_COMPANY_NAME;
        }

        public void setC_COMPANY_NAME(String C_COMPANY_NAME) {
            this.C_COMPANY_NAME = C_COMPANY_NAME;
        }

        public long getD_ANJIAN_START() {
            return D_ANJIAN_START;
        }

        public void setD_ANJIAN_START(long D_ANJIAN_START) {
            this.D_ANJIAN_START = D_ANJIAN_START;
        }
    }
}
