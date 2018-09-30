package com.example.administrator.thinker_soft.meter_code.sk.bean;

import android.support.v7.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/6.
 */

public class SecurityAuditBean implements Serializable {

    /**
     * msg : 查询成功
     * list : [{"lxdh":"18184722702","ajbh":414146,"aqyhyy":null,"ajbz":null,"yhbh":"003263910100106","ajsj":1533196729000,"ajzt":"安检合格","ajqk":"合格","xh":1,"ajjhmc":"月度计划测试","ajy":"超级系统管理员","lbh":"00083","ajlx":"常规安检","aqyhlx":null,"yhmc":"赵富贵","yhdz":"松竹雅苑B1-1-5-4"},{"lxdh":"13996591126","ajbh":414145,"aqyhyy":null,"ajbz":null,"yhbh":"003263910100061","ajsj":1533196511000,"ajzt":"安检合格","ajqk":"合格","xh":2,"ajjhmc":"月度计划测试","ajy":"超级系统管理员","lbh":"00028","ajlx":"常规安检","aqyhlx":null,"yhmc":"杨成江","yhdz":"松竹雅苑A4-1-2-3"},{"lxdh":"13676537292","ajbh":414118,"aqyhyy":null,"ajbz":null,"yhbh":"003264270100012","ajsj":1533373650000,"ajzt":"安检合格","ajqk":"合格","xh":3,"ajjhmc":"梁平08月计划","ajy":"超级系统管理员","lbh":null,"ajlx":"常规安检","aqyhlx":null,"yhmc":"方光兵","yhdz":"福德锦城4-2-3-1"},{"lxdh":"18168986650","ajbh":414144,"aqyhyy":null,"ajbz":null,"yhbh":"003263910100572","ajsj":1533202907000,"ajzt":"安检合格","ajqk":"合格","xh":4,"ajjhmc":"月度计划测试","ajy":"超级系统管理员","lbh":"00579","ajlx":"常规安检","aqyhlx":null,"yhmc":"王  艳","yhdz":"松竹雅苑B6-3-3-4"}]
     * status : success
     */

    private String msg;
    private String status;
    private List<AuditBean> list;

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

    public List<AuditBean> getList() {
        return list;
    }

    public void setList(List<AuditBean> list) {
        this.list = list;
    }

    public static class AuditBean implements Serializable {
        /**
         * * lxdh : 18184722702 联系电话
         * ajbh : 414146 安检编号
         * aqyhyy : null 安全隐患原因
         * ajbz : null 备注
         * yhbh : 003263910100106 用户编号
         * ajsj : 1533196729000 安检时间
         * ajzt : 安检合格 安检状态
         * ajqk : 合格 安检情况
         * xh : 1 序号
         * ajjhmc : 月度计划测试 安检计划名称
         * ajy : 超级系统管理员 安检员
         * lbh : 00083 老编号
         * ajlx : 常规安检 安检类型
         * aqyhlx : null 隐患原因类型
         * yhmc : 赵富贵 安检对象
         * yhdz : 松竹雅苑B1-1-5-4 安检地址
         */

        private String lxdh;
        private int ajbh;
        private String aqyhyy;
        private String ajbz;
        private String yhbh;
        private long ajsj;
        private String ajzt;
        private String ajqk;
        private int xh;
        private String ajjhmc;
        private String ajy;
        private String lbh;
        private String ajlx;
        private String aqyhlx;
        private String yhmc;
        private String yhdz;
        private String ifthrough;

        public String getIfthrough() {
            return ifthrough;
        }

        public void setIfthrough(String ifthrough) {
            this.ifthrough = ifthrough;
        }

        public String getLxdh() {
            return lxdh;
        }

        public void setLxdh(String lxdh) {
            this.lxdh = lxdh;
        }

        public int getAjbh() {
            return ajbh;
        }

        public void setAjbh(int ajbh) {
            this.ajbh = ajbh;
        }

        public String getAqyhyy() {
            return aqyhyy;
        }

        public void setAqyhyy(String aqyhyy) {
            this.aqyhyy = aqyhyy;
        }

        public String getAjbz() {
            return ajbz;
        }

        public void setAjbz(String ajbz) {
            this.ajbz = ajbz;
        }

        public String getYhbh() {
            return yhbh;
        }

        public void setYhbh(String yhbh) {
            this.yhbh = yhbh;
        }

        public long getAjsj() {
            return ajsj;
        }

        public void setAjsj(long ajsj) {
            this.ajsj = ajsj;
        }

        public String getAjzt() {
            return ajzt;
        }

        public void setAjzt(String ajzt) {
            this.ajzt = ajzt;
        }

        public String getAjqk() {
            return ajqk;
        }

        public void setAjqk(String ajqk) {
            this.ajqk = ajqk;
        }

        public int getXh() {
            return xh;
        }

        public void setXh(int xh) {
            this.xh = xh;
        }

        public String getAjjhmc() {
            return ajjhmc;
        }

        public void setAjjhmc(String ajjhmc) {
            this.ajjhmc = ajjhmc;
        }

        public String getAjy() {
            return ajy;
        }

        public void setAjy(String ajy) {
            this.ajy = ajy;
        }

        public String getLbh() {
            return lbh;
        }

        public void setLbh(String lbh) {
            this.lbh = lbh;
        }

        public String getAjlx() {
            return ajlx;
        }

        public void setAjlx(String ajlx) {
            this.ajlx = ajlx;
        }

        public String getAqyhlx() {
            return aqyhlx;
        }

        public void setAqyhlx(String aqyhlx) {
            this.aqyhlx = aqyhlx;
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

        @Override
        public String toString() {
            return "AuditBean{" +
                    "lxdh='" + lxdh + '\'' +
                    ", ajbh=" + ajbh +
                    ", aqyhyy='" + aqyhyy + '\'' +
                    ", ajbz='" + ajbz + '\'' +
                    ", yhbh='" + yhbh + '\'' +
                    ", ajsj=" + ajsj +
                    ", ajzt='" + ajzt + '\'' +
                    ", ajqk='" + ajqk + '\'' +
                    ", xh=" + xh +
                    ", ajjhmc='" + ajjhmc + '\'' +
                    ", ajy='" + ajy + '\'' +
                    ", lbh='" + lbh + '\'' +
                    ", ajlx='" + ajlx + '\'' +
                    ", aqyhlx='" + aqyhlx + '\'' +
                    ", yhmc='" + yhmc + '\'' +
                    ", yhdz='" + yhdz + '\'' +
                    ", ifthrough='" + ifthrough + '\'' +
                    '}';
        }


    }
}
