package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AuditDonYuBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

public class AuditDonYuAdapter extends RecyclerArrayAdapter<AuditDonYuBean.ListBean> {
    public AuditDonYuAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new  AuditDonYuViewHolder(parent);

    }

    public class AuditDonYuViewHolder extends BaseViewHolder<AuditDonYuBean.ListBean> {

        private TextView tkNumber;//安检编号
        private TextView userName;//用户名
        private TextView userNumber;//用户编号
        private TextView syName;//安检员
        private TextView syType;//安检类型
        private TextView syState;//安检状态
        private TextView securityType;//安检状态
        private TextView oldId;//老编号
        private TextView phone;//用户电话
        private TextView userPhone;//用户电话
        private TextView address;//用户地址
        private TextView ifUpload;//通过不
        private ImageView ifEdit;//图片
        /**
         * userAddress     用户地址 : 垫江英吉某一个小区
         * serialNumber    序号 : 1
         * subject    安检员 : SUPER
         * tableNumber     表编号 : null
         * securityTime      安检时间 : 2018-09-21 12:52:14
         * securityNotes     安检备注 : 是豆腐干
         * lastSecurityCheckTime     上次安检时间 : null
         * causesOfPotentialSafetyHazards     安全隐患原因 : 原因3333,原因1
         * securityNumber      安检编号 : 82
         * userName     用户名称 : 黄泽
         * securityStatus     安检状态 : 安检不合格
         * userNumber     用户编号 : 0650100000006
         * theOldNumber     老编号 : 01000004
         * nameOfSecurityCheckPlan     安检计划名称 : 9月计划
         * contactNumber     联系电话 : 0
         * securityScreening     安检情况 : 不合格
         * typesOfSafetyHazards     安全隐患类型 : 隐患类型3,隐患类型1
         */

        public AuditDonYuViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_audit);
            tkNumber=$(R.id.tv_task_id);
            userName=$(R.id.tv_user_name);
            userNumber=$(R.id.tv_user_umber);
            syName=$(R.id.tv_sy_name);
            syType=$(R.id.tv_security_type);
            syState=$(R.id.tv_sy_states);
            oldId=$(R.id.tv_user_oldId);
            phone=$(R.id.tv_user_phone);
            address=$(R.id.tv_user_address);
            ifUpload=$(R.id.if_upload);
            ifEdit =$(R.id.if_edit);
            userPhone =$(R.id.user_phone);
            securityType =$(R.id.security_type);
        }
        @Override
        public void setData(AuditDonYuBean.ListBean item) {
            super.setData(item);
            userPhone.setText("联系电话 : ");
            securityType.setText("安全隐患类型 : ");
            tkNumber.setText(item.getSecurityNumber()+"");
            userName.setText(item.getUserName());
            userNumber.setText(item.getUserNumber());
            syName.setText(item.getSubject());
            syType.setText(item.getTypesOfSafetyHazards());
            syState.setText(item.getSecurityStatus());
            oldId.setText(item.getTheOldNumber());
            phone.setText(item.getContactNumber());
            address.setText(item.getUserAddress());

            //判断是否显示不通过 true:不通过  false:通过  null:显示通过和不通过按钮
            if(item.getIfthrough() !=null){
                ifUpload.setVisibility(View.VISIBLE);
                ifEdit.setVisibility(View.VISIBLE);
                ifUpload.setText(item.getIfthrough());
            }else {
                ifUpload.setVisibility(View.GONE);
                ifEdit.setVisibility(View.GONE);
            }

        }

    }


}
