package com.example.administrator.thinker_soft.meter_code.sk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/2.
 */

public class SecurityChecksBean implements Serializable {


    /**
     * msg : 查询成功
     * list : [{"ajbh":16,"lxdh":null,"n_safety_inspection_id":414905,"aqyhyy":null,"bbh":"0134135","ajbz":null,"yhbh":"003263910000001","ajsj":1535011242000,"ajqk":"拒绝安检","ajzt":"拒绝安检","khrq":1431532800000,"scajsj":1534410937000,"ajjhmc":"异常计划测试","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00490","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":null,"yhmc":"彭礼文","yhdz":"松竹雅苑A5-2-4-3"},{"ajbh":40,"lxdh":null,"n_safety_inspection_id":414845,"aqyhyy":"燃气管线穿越客厅","bbh":"0122503","ajbz":"dd","yhbh":"003263910100058","ajsj":1535095082000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1413388800000,"scajsj":1534324717000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00025","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"户内支管系统","yhmc":"曹桥玉","yhdz":"松竹雅苑A3-3-2-2"},{"ajbh":42,"lxdh":null,"n_safety_inspection_id":414829,"aqyhyy":"入户开关漏气","bbh":"0132409","ajbz":null,"yhbh":"003263910100065","ajsj":1535095082000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1413388800000,"scajsj":1534322287000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00033","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"户内立管类","yhmc":"张成江","yhdz":"松竹雅苑A4-2-6-2"},{"ajbh":41,"lxdh":"15823737811","n_safety_inspection_id":414830,"aqyhyy":null,"bbh":"0137307","ajbz":null,"yhbh":"003263910100067","ajsj":1535095082000,"ajqk":"第一次到访不遇","ajzt":"到访不遇","khrq":1413388800000,"scajsj":1534319367000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"超级系统管理员","lbh":"00036","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":null,"yhmc":"王洪莲","yhdz":"松竹雅苑A4-3-6-2"},{"ajbh":37,"lxdh":null,"n_safety_inspection_id":414846,"aqyhyy":null,"bbh":"0143764","ajbz":"怪怪的还得回家","yhbh":"003263910100068","ajsj":1535095082000,"ajqk":"第二次到访不遇","ajzt":"到访不遇","khrq":1413388800000,"scajsj":1534385082000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"超级系统管理员","lbh":"00037","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":null,"yhmc":"王洪莲","yhdz":"松竹雅苑A4-3-6-3"},{"ajbh":20,"lxdh":null,"n_safety_inspection_id":414999,"aqyhyy":"漏气","bbh":"0141301","ajbz":"需要复检","yhbh":"003263910100082","ajsj":1535095081000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1413388800000,"scajsj":1535091759000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00056","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"安全故障","yhmc":"张兴伟","yhdz":"松竹雅苑A6-2-6-1"},{"ajbh":38,"lxdh":"15823707906","n_safety_inspection_id":414854,"aqyhyy":"私接管道，穿越禁穿区域","bbh":"0132548","ajbz":null,"yhbh":"003263910100090","ajsj":1535095082000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1413388800000,"scajsj":1534325653000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00066","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"户内支管系统","yhmc":"陈廷祥","yhdz":"松竹雅苑A8-2-7-4"},{"ajbh":36,"lxdh":"15823738560","n_safety_inspection_id":414856,"aqyhyy":"报警器失效","bbh":"0140993","ajbz":null,"yhbh":"003263910100092","ajsj":1535095082000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1413388800000,"scajsj":1534399365000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00068","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"户内立管类","yhmc":"黄国秀","yhdz":"松竹雅苑A8-3-3-3"},{"ajbh":34,"lxdh":"18702359753","n_safety_inspection_id":414857,"aqyhyy":"软管穿墙","bbh":"0137361","ajbz":null,"yhbh":"003263910100093","ajsj":1535095082000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1413388800000,"scajsj":1534468456000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00069","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"户内支管系统","yhmc":"袁  伟","yhdz":"松竹雅苑A8-3-4-2"},{"ajbh":13,"lxdh":"13984504690","n_safety_inspection_id":414863,"aqyhyy":"到访不遇","bbh":"0133808","ajbz":null,"yhbh":"003263910100105","ajsj":1535010817000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1413388800000,"scajsj":1534499576000,"ajjhmc":"异常计划测试","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00082","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"人员问题","yhmc":"高作富","yhdz":"松竹雅苑B1-1-4-4"},{"ajbh":25,"lxdh":null,"n_safety_inspection_id":414866,"aqyhyy":null,"bbh":"0140929","ajbz":"女大宝贝","yhbh":"003263910100112","ajsj":1535095081000,"ajqk":"第二次到访不遇","ajzt":"到访不遇","khrq":1413388800000,"scajsj":1534812668000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00091","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":null,"yhmc":"牟丙容","yhdz":"松竹雅苑B2-2-2-2"},{"ajbh":26,"lxdh":null,"n_safety_inspection_id":414892,"aqyhyy":null,"bbh":"0134835","ajbz":"雨uvGV有","yhbh":"003263910100188","ajsj":1535095081000,"ajqk":"第二次到访不遇","ajzt":"到访不遇","khrq":1413475200000,"scajsj":1534736054000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00188","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":null,"yhmc":"张小军","yhdz":"松竹雅苑B2-2-4-1"},{"ajbh":27,"lxdh":"18996516869","n_safety_inspection_id":414894,"aqyhyy":"软管超过2米","bbh":"0144101","ajbz":null,"yhbh":"003263910100197","ajsj":1535095081000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1413475200000,"scajsj":1534488475000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00197","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"户内立管类","yhmc":"张明超","yhdz":"松竹雅苑B6-4-4-3"},{"ajbh":33,"lxdh":"15923875137","n_safety_inspection_id":414895,"aqyhyy":null,"bbh":"0146420","ajbz":null,"yhbh":"003263910100198","ajsj":1535095082000,"ajqk":"第二次到访不遇","ajzt":"到访不遇","khrq":1413475200000,"scajsj":1534468848000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00198","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":null,"yhmc":"牟炳全","yhdz":"松竹雅苑B6-4-6-1"},{"ajbh":29,"lxdh":"18723728446","n_safety_inspection_id":370644,"aqyhyy":null,"bbh":"0140900","ajbz":null,"yhbh":"003263910100237","ajsj":1535095081000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1415203200000,"scajsj":1534484989000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00237","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":null,"yhmc":"游  秋","yhdz":"松竹雅苑A8-2-2-4"},{"ajbh":35,"lxdh":"15025580459","n_safety_inspection_id":414869,"aqyhyy":"软管穿墙,燃气管线穿越客厅","bbh":"0144938","ajbz":null,"yhbh":"003263910100251","ajsj":1535095082000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1415289600000,"scajsj":1534402679000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00251","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"户内支管系统,户内支管系统","yhmc":"刘  江","yhdz":"松竹雅苑B2-1-3-1"},{"ajbh":31,"lxdh":"13594844863","n_safety_inspection_id":414870,"aqyhyy":"到访不遇","bbh":"0132429","ajbz":null,"yhbh":"003263910100252","ajsj":1535095082000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1415289600000,"scajsj":1534470187000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00252","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"人员问题","yhmc":"常选术","yhdz":"松竹雅苑B2-1-3-2"},{"ajbh":32,"lxdh":"13635327851","n_safety_inspection_id":414871,"aqyhyy":"软管超长且老化","bbh":"0146345","ajbz":null,"yhbh":"003263910100254","ajsj":1535095082000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1414080000000,"scajsj":1534470130000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00254","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"户内支管系统","yhmc":"罗定玉","yhdz":"松竹雅苑B4-1-5-1"},{"ajbh":28,"lxdh":"15730668497","n_safety_inspection_id":414873,"aqyhyy":null,"bbh":"0136267","ajbz":null,"yhbh":"003263910100261","ajsj":1535095081000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1415289600000,"scajsj":1534485045000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00261","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":null,"yhmc":"丁  飞","yhdz":"松竹雅苑B4-3-2-2"},{"ajbh":18,"lxdh":"18716768292","n_safety_inspection_id":414874,"aqyhyy":null,"bbh":"0132404","ajbz":null,"yhbh":"003263910100263","ajsj":1535011700000,"ajqk":"拒绝安检","ajzt":"拒绝安检","khrq":1415289600000,"scajsj":1534399451000,"ajjhmc":"异常计划测试","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00263","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":null,"yhmc":"王宣成","yhdz":"松竹雅苑B6-4-2-4"},{"ajbh":30,"lxdh":"13028376569","n_safety_inspection_id":414877,"aqyhyy":"拒绝检查","bbh":"0132468","ajbz":null,"yhbh":"003263910100267","ajsj":1535095082000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1415289600000,"scajsj":1534472694000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":"00267","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"人员问题","yhmc":"田  俊","yhdz":"松竹雅苑B8-3-5-2"},{"ajbh":39,"lxdh":"13709443719","n_safety_inspection_id":414844,"aqyhyy":null,"bbh":null,"ajbz":null,"yhbh":"003263910100538","ajsj":1535095082000,"ajqk":"第一次到访不遇","ajzt":"到访不遇","khrq":1447051985000,"scajsj":1534324760000,"ajjhmc":"wdwa","c_properties_name":"集体用气","ajy":"杨秋","lbh":"00545","c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"罗茨流量计","aqyhlx":null,"yhmc":"重庆巨源不锈钢制品有限公司","yhdz":"梁平县工业园区"},{"ajbh":15,"lxdh":"13896246033","n_safety_inspection_id":414899,"aqyhyy":null,"bbh":"0433362","ajbz":null,"yhbh":"003264270100001","ajsj":1535011242000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1459995334000,"scajsj":1534485974000,"ajjhmc":"异常计划测试","c_properties_name":"居民用气","ajy":"杨秋","lbh":null,"c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":null,"yhmc":"谢志中","yhdz":"福德锦城7-3-2-2"},{"ajbh":14,"lxdh":"15178903658","n_safety_inspection_id":414900,"aqyhyy":"到访不遇","bbh":"0390692","ajbz":null,"yhbh":"003264270100016","ajsj":1535010817000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1460615088000,"scajsj":1534496915000,"ajjhmc":"异常计划测试","c_properties_name":"居民用气","ajy":"唐平","lbh":null,"c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"J2.5","aqyhlx":"人员问题","yhmc":"余成贤","yhdz":"福德锦城3-1-2-3"},{"ajbh":23,"lxdh":"暂无","n_safety_inspection_id":414904,"aqyhyy":null,"bbh":"暂无","ajbz":null,"yhbh":"003264270100045","ajsj":1535095081000,"ajqk":"第一次到访不遇","ajzt":"到访不遇","khrq":1474428879000,"scajsj":1535075661000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"超级系统管理员","lbh":null,"c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"50BW-简易型","aqyhlx":null,"yhmc":"as","yhdz":"asfasdfsadfsad"},{"ajbh":21,"lxdh":"暂无","n_safety_inspection_id":414906,"aqyhyy":null,"bbh":"暂无","ajbz":null,"yhbh":"003264280100001","ajsj":1535095081000,"ajqk":"第一次到访不遇","ajzt":"到访不遇","khrq":1461288736000,"scajsj":1535091573000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":null,"c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"LLQ(Z)-50B","aqyhlx":null,"yhmc":"zj5","yhdz":"zj5"},{"ajbh":24,"lxdh":null,"n_safety_inspection_id":414908,"aqyhyy":null,"bbh":"112","ajbz":null,"yhbh":"003264280100004","ajsj":1535095081000,"ajqk":"第一次到访不遇","ajzt":"到访不遇","khrq":1533869255000,"scajsj":1535075570000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"超级系统管理员","lbh":null,"c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"TDS50B","aqyhlx":null,"yhmc":"123","yhdz":"424"},{"ajbh":22,"lxdh":"暂无","n_safety_inspection_id":414909,"aqyhyy":"不安全","bbh":"1312","ajbz":"需要复检","yhbh":"003264280100006","ajsj":1535095081000,"ajqk":"安全隐患","ajzt":"安检不合格","khrq":1533879791000,"scajsj":1535091557000,"ajjhmc":"wdwa","c_properties_name":"居民用气","ajy":"杨秋","lbh":null,"c_company_name":"梁平营业所","ajlx":"常规安检","c_model_name":"TDS50B","aqyhlx":"私自改变用气性质","yhmc":"0303","yhdz":"dd"}]
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
         * ajbh : 16
         * lxdh : null
         * n_safety_inspection_id : 414905
         * aqyhyy : null
         * bbh : 0134135
         * ajbz : null
         * yhbh : 003263910000001
         * ajsj : 1535011242000
         * ajqk : 拒绝安检
         * ajzt : 拒绝安检
         * khrq : 1431532800000
         * scajsj : 1534410937000
         * ajjhmc : 异常计划测试
         * c_properties_name : 居民用气
         * ajy : 杨秋
         * lbh : 00490
         * c_company_name : 梁平营业所
         * ajlx : 常规安检
         * c_model_name : J2.5
         * aqyhlx : null
         * yhmc : 彭礼文
         * yhdz : 松竹雅苑A5-2-4-3
         */

        private int ajbh;
        private String lxdh;
        private long n_safety_inspection_id;
        private String aqyhyy;
        private String bbh;
        private String ajbz;
        private String yhbh;
        private long ajsj;
        private String ajqk;
        private String ajzt;
        private long khrq;
        private long scajsj;
        private String ajjhmc;
        private String c_properties_name;
        private String ajy;
        private String lbh;
        private String c_company_name;
        private String ajlx;
        private String c_model_name;
        private String aqyhlx;
        private String yhmc;
        private String yhdz;

        public int getAjbh() {
            return ajbh;
        }

        public void setAjbh(int ajbh) {
            this.ajbh = ajbh;
        }

        public String getLxdh() {
            return lxdh;
        }

        public void setLxdh(String lxdh) {
            this.lxdh = lxdh;
        }

        public long getN_safety_inspection_id() {
            return n_safety_inspection_id;
        }

        public void setN_safety_inspection_id(long n_safety_inspection_id) {
            this.n_safety_inspection_id = n_safety_inspection_id;
        }

        public String getAqyhyy() {
            return aqyhyy;
        }

        public void setAqyhyy(String aqyhyy) {
            this.aqyhyy = aqyhyy;
        }

        public String getBbh() {
            return bbh;
        }

        public void setBbh(String bbh) {
            this.bbh = bbh;
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

        public String getAjqk() {
            return ajqk;
        }

        public void setAjqk(String ajqk) {
            this.ajqk = ajqk;
        }

        public String getAjzt() {
            return ajzt;
        }

        public void setAjzt(String ajzt) {
            this.ajzt = ajzt;
        }

        public long getKhrq() {
            return khrq;
        }

        public void setKhrq(long khrq) {
            this.khrq = khrq;
        }

        public long getScajsj() {
            return scajsj;
        }

        public void setScajsj(long scajsj) {
            this.scajsj = scajsj;
        }

        public String getAjjhmc() {
            return ajjhmc;
        }

        public void setAjjhmc(String ajjhmc) {
            this.ajjhmc = ajjhmc;
        }

        public String getC_properties_name() {
            return c_properties_name;
        }

        public void setC_properties_name(String c_properties_name) {
            this.c_properties_name = c_properties_name;
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

        public String getC_company_name() {
            return c_company_name;
        }

        public void setC_company_name(String c_company_name) {
            this.c_company_name = c_company_name;
        }

        public String getAjlx() {
            return ajlx;
        }

        public void setAjlx(String ajlx) {
            this.ajlx = ajlx;
        }

        public String getC_model_name() {
            return c_model_name;
        }

        public void setC_model_name(String c_model_name) {
            this.c_model_name = c_model_name;
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
    }
}
