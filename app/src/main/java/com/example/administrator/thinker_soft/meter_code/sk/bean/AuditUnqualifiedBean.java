package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author 111
 * @date 2018/8/16
 */

public class AuditUnqualifiedBean implements Serializable {


    /**
     * msg : 查询成功
     * list : [{"SCAJSJ":null,"AQYHLX":"户内立管类","BBH":"0144101","AJLX":"常规安检","YHMC":"张明超","YHDZ":"松竹雅苑B6-4-4-3","XH":1,"AJY":"杨秋","AJBH":414894,"LBH":"00197","LXDH":"18996516869","AQYHYY":"软管超过2米","YHBH":"003263910100197","AJZT":"安检不合格","AJJHMC":"17月度测试","AJQK":"安全隐患","AJSJ":1534488475000,"AJBZ":null},{"SCAJSJ":null,"AQYHLX":"户内支管系统,户内支管系统","BBH":"0144938","AJLX":"常规安检","YHMC":"刘  江","YHDZ":"松竹雅苑B2-1-3-1","XH":2,"AJY":"杨秋","AJBH":414869,"LBH":"00251","LXDH":"15025580459","AQYHYY":"软管穿墙,燃气管线穿越客厅","YHBH":"003263910100251","AJZT":"安检不合格","AJJHMC":"梁平月计划2","AJQK":"安全隐患","AJSJ":1534402679000,"AJBZ":null},{"SCAJSJ":null,"AQYHLX":"户内支管系统","BBH":"0137361","AJLX":"常规安检","YHMC":"袁  伟","YHDZ":"松竹雅苑A8-3-4-2","XH":3,"AJY":"杨秋","AJBH":414857,"LBH":"00069","LXDH":"18702359753","AQYHYY":"软管穿墙","YHBH":"003263910100093","AJZT":"安检不合格","AJJHMC":"梁平月计划2","AJQK":"安全隐患","AJSJ":1534468456000,"AJBZ":null},{"SCAJSJ":null,"AQYHLX":"户内立管类","BBH":"0140993","AJLX":"常规安检","YHMC":"黄国秀","YHDZ":"松竹雅苑A8-3-3-3","XH":4,"AJY":"杨秋","AJBH":414856,"LBH":"00068","LXDH":"15823738560","AQYHYY":"报警器失效","YHBH":"003263910100092","AJZT":"安检不合格","AJJHMC":"梁平月计划2","AJQK":"安全隐患","AJSJ":1534399365000,"AJBZ":null},{"SCAJSJ":null,"AQYHLX":"户内支管系统","BBH":"0132548","AJLX":"常规安检","YHMC":"陈廷祥","YHDZ":"松竹雅苑A8-2-7-4","XH":5,"AJY":"杨秋","AJBH":414854,"LBH":"00066","LXDH":"15823707906","AQYHYY":"私接管道，穿越禁穿区域","YHBH":"003263910100090","AJZT":"安检不合格","AJJHMC":"梁平月计划2","AJQK":"安全隐患","AJSJ":1534325653000,"AJBZ":null},{"SCAJSJ":null,"AQYHLX":"户内支管系统","BBH":"0122503","AJLX":"常规安检","YHMC":"曹桥玉","YHDZ":"松竹雅苑A3-3-2-2","XH":6,"AJY":"杨秋","AJBH":414845,"LBH":"00025","LXDH":"15870432369","AQYHYY":"燃气管线穿越客厅","YHBH":"003263910100058","AJZT":"安检不合格","AJJHMC":"梁平月计划2","AJQK":"安全隐患","AJSJ":1534324717000,"AJBZ":"dd"},{"SCAJSJ":null,"AQYHLX":"户内立管类","BBH":"0132409","AJLX":"常规安检","YHMC":"张成江","YHDZ":"松竹雅苑A4-2-6-2","XH":7,"AJY":"杨秋","AJBH":414829,"LBH":"00033","LXDH":"13896389426","AQYHYY":"入户开关漏气","YHBH":"003263910100065","AJZT":"安检不合格","AJJHMC":"0815月度计划测试","AJQK":"安全隐患","AJSJ":1534317618000,"AJBZ":null},{"SCAJSJ":null,"AQYHLX":null,"BBH":"0136434","AJLX":"常规安检","YHMC":"陈宝林","YHDZ":"松竹雅苑B2-3-2-2","XH":8,"AJY":"杨秋","AJBH":414867,"LBH":"00096","LXDH":"13635385026","AQYHYY":null,"YHBH":"003263910100115","AJZT":"安检合格","AJJHMC":"梁平月计划2","AJQK":"合格","AJSJ":1534399342000,"AJBZ":null},{"SCAJSJ":null,"AQYHLX":null,"BBH":"0132942","AJLX":"常规安检","YHMC":"赵富贵","YHDZ":"松竹雅苑B1-1-5-4","XH":9,"AJY":"杨秋","AJBH":414864,"LBH":"00083","LXDH":"18184722702","AQYHYY":null,"YHBH":"003263910100106","AJZT":"安检合格","AJJHMC":"梁平月计划2","AJQK":"合格","AJSJ":1534324659000,"AJBZ":null},{"SCAJSJ":null,"AQYHLX":null,"BBH":"0132441","AJLX":"常规安检","YHMC":"陆云兵","YHDZ":"松竹雅苑A9-1-2-4","XH":10,"AJY":"杨秋","AJBH":414860,"LBH":"00072","LXDH":"13996589452","AQYHYY":null,"YHBH":"003263910100096","AJZT":"安检合格","AJJHMC":"梁平月计划2","AJQK":"合格","AJSJ":1534399303000,"AJBZ":null},{"SCAJSJ":null,"AQYHLX":null,"BBH":"0137899","AJLX":"常规安检","YHMC":"黄明德","YHDZ":"松竹雅苑B6-2-5-4","XH":11,"AJY":"杨秋","AJBH":414837,"LBH":"00125","LXDH":"15683540921","AQYHYY":null,"YHBH":"003263910100136","AJZT":"安检合格","AJJHMC":"0815月度计划测试","AJQK":"合格","AJSJ":1534325339000,"AJBZ":null},{"SCAJSJ":null,"AQYHLX":null,"BBH":"0132469","AJLX":"常规安检","YHMC":"陆云兵","YHDZ":"松竹雅苑A8-3-2-4","XH":12,"AJY":"杨秋","AJBH":414855,"LBH":"00067","LXDH":"13996589452","AQYHYY":null,"YHBH":"003263910100091","AJZT":"安检合格","AJJHMC":"梁平月计划2","AJQK":"复检合格","AJSJ":1534469149000,"AJBZ":null},{"SCAJSJ":null,"AQYHLX":null,"BBH":"0132536","AJLX":"临时安检","YHMC":"张风文","YHDZ":"松竹雅苑B1-2-3-1","XH":13,"AJY":"杨秋","AJBH":414889,"LBH":"00399","LXDH":"18580926226","AQYHYY":null,"YHBH":"003263910100401","AJZT":"到访不遇","AJJHMC":"17临时计划测试","AJQK":"第一次到访不遇","AJSJ":1534468925000,"AJBZ":null},{"SCAJSJ":1471245700000,"AQYHLX":null,"BBH":"0141255","AJLX":"临时安检","YHMC":"李中明","YHDZ":"松竹雅苑A5-1-2-3","XH":14,"AJY":"杨秋","AJBH":414841,"LBH":"00038","LXDH":"18723684171","AQYHYY":null,"YHBH":"003263910100069","AJZT":"到访不遇","AJJHMC":"0815临时计划测试","AJQK":"第一次到访不遇","AJSJ":1534320514000,"AJBZ":null},{"SCAJSJ":null,"AQYHLX":null,"BBH":"0134835","AJLX":"常规安检","YHMC":"张小军","YHDZ":"松竹雅苑B2-2-4-1","XH":15,"AJY":"杨秋","AJBH":414892,"LBH":"00188","LXDH":null,"AQYHYY":null,"YHBH":"003263910100188","AJZT":"到访不遇","AJJHMC":"17月度测试","AJQK":"第二次到访不遇","AJSJ":1534736054000,"AJBZ":"雨uvGV有"},{"SCAJSJ":null,"AQYHLX":null,"BBH":"0118267","AJLX":"常规安检","YHMC":"李远英","YHDZ":"松竹雅苑A9-2-4-3","XH":16,"AJY":null,"AJBH":414976,"LBH":"00184","LXDH":"15223769253","AQYHYY":null,"YHBH":"003263910100184","AJZT":"未安检","AJJHMC":"0815月度计划测试","AJQK":null,"AJSJ":null,"AJBZ":null}]
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

    public static class ListBean implements Serializable {
        /**
         * SCAJSJ : null
         * AQYHLX : 户内立管类
         * BBH : 0144101
         * AJLX : 常规安检
         * YHMC : 张明超
         * YHDZ : 松竹雅苑B6-4-4-3
         * XH : 1
         * AJY : 杨秋
         * AJBH : 414894
         * LBH : 00197
         * LXDH : 18996516869
         * AQYHYY : 软管超过2米
         * YHBH : 003263910100197
         * AJZT : 安检不合格
         * AJJHMC : 17月度测试
         * AJQK : 安全隐患
         * AJSJ : 1534488475000
         * AJBZ : null
         */

        private long SCAJSJ;
        private String AQYHLX;
        private String BBH;
        private String AJLX;
        private String YHMC;
        private String YHDZ;
        private int XH;
        private String AJY;
        private long AJBH;
        private String LBH;
        private String LXDH;
        private String AQYHYY;
        private String YHBH;
        private String AJZT;
        private String AJJHMC;
        private String AJQK;
        private long AJSJ;
        private String AJBZ;

        public long getSCAJSJ() {
            return SCAJSJ;
        }

        public void setSCAJSJ(long SCAJSJ) {
            this.SCAJSJ = SCAJSJ;
        }

        public String getAQYHLX() {
            return AQYHLX;
        }

        public void setAQYHLX(String AQYHLX) {
            this.AQYHLX = AQYHLX;
        }

        public String getBBH() {
            return BBH;
        }

        public void setBBH(String BBH) {
            this.BBH = BBH;
        }

        public String getAJLX() {
            return AJLX;
        }

        public void setAJLX(String AJLX) {
            this.AJLX = AJLX;
        }

        public String getYHMC() {
            return YHMC;
        }

        public void setYHMC(String YHMC) {
            this.YHMC = YHMC;
        }

        public String getYHDZ() {
            return YHDZ;
        }

        public void setYHDZ(String YHDZ) {
            this.YHDZ = YHDZ;
        }

        public int getXH() {
            return XH;
        }

        public void setXH(int XH) {
            this.XH = XH;
        }

        public String getAJY() {
            return AJY;
        }

        public void setAJY(String AJY) {
            this.AJY = AJY;
        }

        public long getAJBH() {
            return AJBH;
        }

        public void setAJBH(long AJBH) {
            this.AJBH = AJBH;
        }

        public String getLBH() {
            return LBH;
        }

        public void setLBH(String LBH) {
            this.LBH = LBH;
        }

        public String getLXDH() {
            return LXDH;
        }

        public void setLXDH(String LXDH) {
            this.LXDH = LXDH;
        }

        public String getAQYHYY() {
            return AQYHYY;
        }

        public void setAQYHYY(String AQYHYY) {
            this.AQYHYY = AQYHYY;
        }

        public String getYHBH() {
            return YHBH;
        }

        public void setYHBH(String YHBH) {
            this.YHBH = YHBH;
        }

        public String getAJZT() {
            return AJZT;
        }

        public void setAJZT(String AJZT) {
            this.AJZT = AJZT;
        }

        public String getAJJHMC() {
            return AJJHMC;
        }

        public void setAJJHMC(String AJJHMC) {
            this.AJJHMC = AJJHMC;
        }

        public String getAJQK() {
            return AJQK;
        }

        public void setAJQK(String AJQK) {
            this.AJQK = AJQK;
        }

        public long getAJSJ() {
            return AJSJ;
        }

        public void setAJSJ(long AJSJ) {
            this.AJSJ = AJSJ;
        }

        public String getAJBZ() {
            return AJBZ;
        }

        public void setAJBZ(String AJBZ) {
            this.AJBZ = AJBZ;
        }




    }
}
