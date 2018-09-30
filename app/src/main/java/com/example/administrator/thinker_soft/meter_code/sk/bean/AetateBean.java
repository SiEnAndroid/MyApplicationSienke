package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/1.
 */

public class AetateBean  implements Serializable {

    /**
     * msg : 查询成功
     * list : [{"lbh":"80102035","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080102035","yhmc":"隆鑫苑B1-1","yhdz":"隆鑫苑B","khrq":1469062746000},{"lbh":"80102046","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080102046","yhmc":"隆鑫苑B1-1","yhdz":"隆鑫苑B","khrq":1469062746000},{"lbh":"80102085","qbbh":"0","lidh":"15215090900","fq":"01区","yhbh":"1206030080102085","yhmc":"王艳","yhdz":"廊桥左岸1-1-4-2","khrq":1489735077000},{"lbh":"80102152","qbbh":"0","lidh":"13438683062","fq":"01区","yhbh":"1206030080102152","yhmc":"赵丹丹","yhdz":"廊桥左岸1-1-8-5","khrq":1490060274000},{"lbh":"80102196","qbbh":"0","lidh":"18981625510","fq":"01区","yhbh":"1206030080102196","yhmc":"王廷昌","yhdz":"廊桥左岸3-2-1-2","khrq":1490234749000},{"lbh":"80102247","qbbh":"0","lidh":"13551589426","fq":"01区","yhbh":"1206030080102247","yhmc":"周克蓉","yhdz":"廊桥左岸8-2-101","khrq":1490512169000},{"lbh":"80102293","qbbh":"0","lidh":"18095077361","fq":"01区","yhbh":"1206030080102293","yhmc":"王雪梅","yhdz":"廊桥左岸6-2-602","khrq":1469062746000},{"lbh":"80102555","qbbh":"0","lidh":"13881622280","fq":"01区","yhbh":"1206030080102555","yhmc":"杨华","yhdz":"廊桥左岸2-2-1103","khrq":1492330000000},{"lbh":"80102811","qbbh":"0","lidh":"13551572588","fq":"01区","yhbh":"1206030080102811","yhmc":"叶子书","yhdz":"精耕街7号","khrq":1485228119000},{"lbh":"80102812","qbbh":"0","lidh":"13551572588","fq":"01区","yhbh":"1206030080102812","yhmc":"叶子书","yhdz":"精耕街7号","khrq":1485228119000},{"lbh":"80102836","qbbh":"0","lidh":"18283598706","fq":"01区","yhbh":"1206030080102836","yhmc":"汪永明","yhdz":null,"khrq":1498699752000},{"lbh":"80102839","qbbh":"0","lidh":"13551589998","fq":"01区","yhbh":"1206030080102839","yhmc":"孙平康","yhdz":null,"khrq":1498700509000},{"lbh":"80102921","qbbh":"0","lidh":"13418770163","fq":"01区","yhbh":"1206030080102921","yhmc":"廖莎莎","yhdz":null,"khrq":1498720296000},{"lbh":"80102928","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080102928","yhmc":"社区用房","yhdz":null,"khrq":1498721252000},{"lbh":"80102948","qbbh":"0","lidh":"13981612428","fq":"01区","yhbh":"1206030080102948","yhmc":"刘锦","yhdz":null,"khrq":1498723595000},{"lbh":"80102949","qbbh":"0","lidh":"13981612428","fq":"01区","yhbh":"1206030080102949","yhmc":"刘锦","yhdz":null,"khrq":1498723616000},{"lbh":"80102965","qbbh":"0","lidh":"13618156604","fq":"01区","yhbh":"1206030080102965","yhmc":"王家树","yhdz":null,"khrq":1498725461000},{"lbh":"80102968","qbbh":"0","lidh":"13648155371","fq":"01区","yhbh":"1206030080102968","yhmc":"杨在清","yhdz":null,"khrq":1498725619000},{"lbh":"80102969","qbbh":"0","lidh":"13648155371","fq":"01区","yhbh":"1206030080102969","yhmc":"杨在清","yhdz":null,"khrq":1498725692000},{"lbh":"80103049","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103049","yhmc":"德能新城28","yhdz":"小溪南街28号","khrq":1499129507000},{"lbh":"80103075","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103075","yhmc":"德能新城小溪南街80号","yhdz":"德能新城小溪南街80号","khrq":1499133264000},{"lbh":"80103257","qbbh":"0","lidh":"13551572588","fq":"01区","yhbh":"1206030080103257","yhmc":"张明芳","yhdz":"凤鸣村2","khrq":1485228119000},{"lbh":"80103299","qbbh":"0","lidh":"13551572588","fq":"01区","yhbh":"1206030080103299","yhmc":"方云群","yhdz":"凤鸣村","khrq":1485228119000},{"lbh":"80103305","qbbh":"0","lidh":"13568765957","fq":"01区","yhbh":"1206030080103305","yhmc":"孟勤成","yhdz":"凤鸣山庄围墙外凤鸣村","khrq":1485228119000},{"lbh":"80103313","qbbh":"0","lidh":"17723301874","fq":"01区","yhbh":"1206030080103313","yhmc":"程早行","yhdz":"梦都花园1-1-2-1","khrq":1504140426000},{"lbh":"80103419","qbbh":"0","lidh":"15281281793","fq":"01区","yhbh":"1206030080103419","yhmc":"彭学华、韩孝章","yhdz":"梦都花园","khrq":1504150217000},{"lbh":"80103483","qbbh":"0","lidh":"15520237861","fq":"01区","yhbh":"1206030080103483","yhmc":"柴国钊、陈仁敏","yhdz":"梦都花园","khrq":1485228119000},{"lbh":"80103574","qbbh":"0","lidh":"15881201881","fq":"01区","yhbh":"1206030080103574","yhmc":"张志林、张峻玮","yhdz":"梦都花园","khrq":1504233241000},{"lbh":"80103616","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103616","yhmc":"门市16","yhdz":"信达 3单元左","khrq":1505096689000},{"lbh":"80103626","qbbh":"0","lidh":"13551572588","fq":"01区","yhbh":"1206030080103626","yhmc":"1212","yhdz":"精耕街7号","khrq":1485228119000},{"lbh":"80103634","qbbh":"0","lidh":"13778759012","fq":"01区","yhbh":"1206030080103634","yhmc":"巫庆宏","yhdz":"凤鸣山庄8栋602号","khrq":1506560131000},{"lbh":"80103706","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103706","yhmc":"14-04","yhdz":"德能新城8#楼","khrq":1509692898000},{"lbh":"80103707","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103707","yhmc":"14-04","yhdz":"德能新城8#楼","khrq":1485228119000},{"lbh":"80103721","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103721","yhmc":"1-01","yhdz":"德能新城9#楼","khrq":1485228119000},{"lbh":"80103772","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103772","yhmc":"5-01","yhdz":"德能新城9#楼","khrq":1509778757000},{"lbh":"80103858","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103858","yhmc":"1-02","yhdz":null,"khrq":1510020179000},{"lbh":"80103873","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103873","yhmc":"4-04","yhdz":null,"khrq":1510021708000},{"lbh":"80103928","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103928","yhmc":"1-02","yhdz":null,"khrq":1510036227000},{"lbh":"80103979","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103979","yhmc":"14-2","yhdz":null,"khrq":1510040449000},{"lbh":"80103980","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103980","yhmc":"14-3","yhdz":null,"khrq":1510040524000},{"lbh":"80103981","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080103981","yhmc":"14-3","yhdz":null,"khrq":1510040561000},{"lbh":"80104084","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080104084","yhmc":"6-4","yhdz":null,"khrq":1510191843000},{"lbh":"80104132","qbbh":"0","lidh":"13551572588","fq":"01区","yhbh":"1206030080104132","yhmc":"精耕街7号2-3","yhdz":"精耕街7号2-3","khrq":1485228119000},{"lbh":"80104147","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080104147","yhmc":"德能新城15号楼4-4","yhdz":"德能新城15号楼4-4","khrq":1511401656000},{"lbh":"80104148","qbbh":"0","lidh":"13551572588","fq":"01区","yhbh":"1206030080104148","yhmc":"德能新城15号楼4-4","yhdz":"德能新城15号楼4-4","khrq":1485228119000},{"lbh":"80104192","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1206030080104192","yhmc":"德能新城15号楼15-3","yhdz":"德能新城15号楼15-3","khrq":1511404321000},{"lbh":"80101048","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1BD4B30080101048","yhmc":"2号楼2单元52号","yhdz":null,"khrq":1468464301000},{"lbh":"80101061","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1BD4B30080101061","yhmc":"2号楼2单元65号","yhdz":null,"khrq":1468464735000},{"lbh":"80101064","qbbh":"0","lidh":null,"fq":"01区","yhbh":"1BD4B30080101064","yhmc":"2号楼2单元67号","yhdz":null,"khrq":1468464819000},{"lbh":"80101338","qbbh":"0","lidh":"15284793612","fq":"01区","yhbh":"1BD4B30080101338","yhmc":"黄永霞","yhdz":"康宁路108号","khrq":1468889065000},{"lbh":"80101340","qbbh":"0","lidh":"15284793612","fq":"01区","yhbh":"1BD4B30080101340","yhmc":"黄永霞","yhdz":"康宁路108号","khrq":1469062981000}]
     * status : success
     */

    private String msg;
    private String status;
    private int pageIndex;
    private int perPageCount;
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

    public static class ListBean implements Serializable{

        /**
         * lbh : 80102035
         * qbbh : 0
         * lidh : null
         * fq : 01区
         * yhbh : 1206030080102035
         * yhmc : 隆鑫苑B1-1
         * yhdz : 隆鑫苑B
         * khrq : 1469062746000
         * yhbh ： 用户编号
         *
         * lbh：老编号
         * yhmc：用户名称
         * ｆｕ：分区
         * yhdz：用户地址
         * khrq：开户日期
         * qbbh：气表编号
         * lidh：联系电话
         */
        private String lbh;
        private String qbbh;
        private String lidh;
        private String fq;
        private String yhbh;
        private String yhmc;
        private String yhdz;
        private long khrq;
        private String jqfx;

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

        public String getJqfx() {
            return jqfx;
        }

        public void setJqfx(String jqfx) {
            this.jqfx = jqfx;
        }
    }

}
