package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/17.
 */

public class AetateBelowBean implements Serializable{

    /**
     * msg : 查询成功
     * pageIndex : 1
     * perPageCount : 10
     * list : [{"R":1,"lbh":null,"qbbh":"30323380","lidh":"15282634333","jqfx":"右进左出","bz":null,"fq":"东城大道","yhbh":"003053400300097","yhmc":"重庆南地置业有限公司","yhdz":"万盛东城大道107号4幢11-7","khrq":1448467200000},{"R":2,"lbh":null,"qbbh":"30330529","lidh":"15282634333","jqfx":"左进右出","bz":null,"fq":"东城大道","yhbh":"003053400300119","yhmc":"重庆南地置业有限公司","yhdz":"万盛东城大道107号4幢13-9","khrq":1448467200000}]
     * status : success
     */

    private String msg;
    private int pageIndex;
    private int perPageCount;
    private String status;
    private List<AetateBelowListBean> list;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPerPageCount() {
        return perPageCount;
    }

    public void setPerPageCount(int perPageCount) {
        this.perPageCount = perPageCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AetateBelowListBean> getList() {
        return list;
    }

    public void setList(List<AetateBelowListBean> list) {
        this.list = list;
    }

    public static class AetateBelowListBean implements Serializable {
        /**
         * R : 1
         * lbh : null
         * qbbh : 30323380
         * lidh : 15282634333
         * jqfx : 右进左出
         * bz : null
         * fq : 东城大道
         * yhbh : 003053400300097
         * yhmc : 重庆南地置业有限公司
         * yhdz : 万盛东城大道107号4幢11-7
         * khrq : 1448467200000
         */

        private int R;
        private String lbh;
        private String qbbh;
        private String lidh;
        private String jqfx;
        private String bz;
        private String fq;
        private String yhbh;
        private String yhmc;
        private String yhdz;
        private long khrq;

        public int getR() {
            return R;
        }

        public void setR(int R) {
            this.R = R;
        }

        public String getLbh() {
            return lbh;
        }

        public void setLbh(String lbh) {
            this.lbh = lbh;
        }

        public String getQbbh() {
            return qbbh;
        }

        public void setQbbh(String qbbh) {
            this.qbbh = qbbh;
        }

        public String getLidh() {
            return lidh;
        }

        public void setLidh(String lidh) {
            this.lidh = lidh;
        }

        public String getJqfx() {
            return jqfx;
        }

        public void setJqfx(String jqfx) {
            this.jqfx = jqfx;
        }

        public String getBz() {
            return bz;
        }

        public void setBz(String bz) {
            this.bz = bz;
        }

        public String getFq() {
            return fq;
        }

        public void setFq(String fq) {
            this.fq = fq;
        }

        public String getYhbh() {
            return yhbh;
        }

        public void setYhbh(String yhbh) {
            this.yhbh = yhbh;
        }

        public String getYhmc() {
            return yhmc;
        }

        public void setYhmc(String yhmc) {
            this.yhmc = yhmc;
        }

        public String getYhdz() {
            return yhdz;
        }

        public void setYhdz(String yhdz) {
            this.yhdz = yhdz;
        }

        public long getKhrq() {
            return khrq;
        }

        public void setKhrq(long khrq) {
            this.khrq = khrq;
        }
    }
}
