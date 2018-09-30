package com.example.administrator.thinker_soft.meter_code.sk.bean;

/**
 * @author g
 * @FileName ReportTranParameSubmit
 * @date 2018/9/3 17:43
 */
public class ReportTranParameSubmit {
    /**流程id*/
    private String transactionid;
    /**逻辑条件*/
    private String conditionid;
    /**用户id*/
    private String systemuserid;
    /**指定人员ids*/
    private String zduserids;
    /**办理批示*/
    private String tcomment;
    /** 流转方式 1 通过 2 驳回 3废弃*/
    private String trantype;
    /**  驳回时 跳转的节点 默认0*/
    private String jumpnode;
    /**驳回时,3 逐步重走 4一步重走 默认0*/
    private String bacemode;
    /**签章id*/
    private String signatureid;

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getConditionid() {
        return conditionid;
    }

    public void setConditionid(String conditionid) {
        this.conditionid = conditionid;
    }

    public String getSystemuserid() {
        return systemuserid;
    }

    public void setSystemuserid(String systemuserid) {
        this.systemuserid = systemuserid;
    }

    public String getZduserids() {
        return zduserids;
    }

    public void setZduserids(String zduserids) {
        this.zduserids = zduserids;
    }

    public String getTcomment() {
        return tcomment;
    }

    public void setTcomment(String tcomment) {
        this.tcomment = tcomment;
    }

    public String getTrantype() {
        return trantype;
    }

    public void setTrantype(String trantype) {
        this.trantype = trantype;
    }

    public String getJumpnode() {
        return jumpnode;
    }

    public void setJumpnode(String jumpnode) {
        this.jumpnode = jumpnode;
    }

    public String getBacemode() {
        return bacemode;
    }

    public void setBacemode(String bacemode) {
        this.bacemode = bacemode;
    }

    public String getSignatureid() {
        return signatureid;
    }

    public void setSignatureid(String signatureid) {
        this.signatureid = signatureid;
    }
}
