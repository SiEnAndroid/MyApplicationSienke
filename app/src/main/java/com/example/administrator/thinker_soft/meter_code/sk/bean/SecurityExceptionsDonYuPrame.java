package com.example.administrator.thinker_soft.meter_code.sk.bean;

public class SecurityExceptionsDonYuPrame {

    /*
    * 	c_anjian_inspection_member  安检员   (安检员必传，超级管理员不用)
		n_company_id                公司id    必传
		c_user_id                   用户编号  不必传
		c_user_name                 用户名    不必传
		n_anjian_plan               计划编号  不必传
		d_anjian_inspection_date_start    安检开始时间   不必传
		d_anjian_inspection_date_end      安检结束时间   不必传
		d_anjian_start                    计划开始时间   不必传
		d_anjian_end                      计划结束时间   不必传

		*/

    private String nCompanyId;
    private String cUserId;
    private String cUserName;
    private String nAnjianPlan;
    private String dAnjianInspectionDateStart;
    private String dAnjianInspectionDateEnd;
    private String dAnjianStart;
    private String dAnjianEnd;
    private String cAnjianInspectionMember;

    public String getcAnjianInspectionMember() {
        return cAnjianInspectionMember;
    }

    public void setcAnjianInspectionMember(String cAnjianInspectionMember) {
        this.cAnjianInspectionMember = cAnjianInspectionMember;
    }

    public String getnCompanyId() {
        return nCompanyId;
    }

    public void setnCompanyId(String nCompanyId) {
        this.nCompanyId = nCompanyId;
    }

    public String getcUserId() {
        return cUserId;
    }

    public void setcUserId(String cUserId) {
        this.cUserId = cUserId;
    }

    public String getcUserName() {
        return cUserName;
    }

    public void setcUserName(String cUserName) {
        this.cUserName = cUserName;
    }

    public String getnAnjianPlan() {
        return nAnjianPlan;
    }

    public void setnAnjianPlan(String nAnjianPlan) {
        this.nAnjianPlan = nAnjianPlan;
    }

    public String getdAnjianInspectionDateStart() {
        return dAnjianInspectionDateStart;
    }

    public void setdAnjianInspectionDateStart(String dAnjianInspectionDateStart) {
        this.dAnjianInspectionDateStart = dAnjianInspectionDateStart;
    }

    public String getdAnjianInspectionDateEnd() {
        return dAnjianInspectionDateEnd;
    }

    public void setdAnjianInspectionDateEnd(String dAnjianInspectionDateEnd) {
        this.dAnjianInspectionDateEnd = dAnjianInspectionDateEnd;
    }

    public String getdAnjianStart() {
        return dAnjianStart;
    }

    public void setdAnjianStart(String dAnjianStart) {
        this.dAnjianStart = dAnjianStart;
    }

    public String getdAnjianEnd() {
        return dAnjianEnd;
    }

    public void setdAnjianEnd(String dAnjianEnd) {
        this.dAnjianEnd = dAnjianEnd;
    }
}
