package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.UserListviewItem;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityAuditBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 * @date 2018/8/6
 */

public class SecurityAuditAdapter extends RecyclerArrayAdapter<SecurityAuditBean.AuditBean> {


    public SecurityAuditAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new SecurityAuditViewHolder(parent);
    }

    public class SecurityAuditViewHolder extends BaseViewHolder<SecurityAuditBean.AuditBean> {

        private TextView tkNumber;//安检编号
        private TextView userName;//用户名
        private TextView userNumber;//用户编号
        private TextView syName;//安检员
        private TextView syType;//安检类型
        private TextView syState;//安检状态
        private TextView oldId;//老编号
        private TextView phone;//用户电话
        private TextView address;//用户地址
        private TextView ifUpload;//通过不
        private ImageView ifEdit;//图片

        public SecurityAuditViewHolder(ViewGroup parent) {
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


        }

        @Override
        public void setData(SecurityAuditBean.AuditBean item) {
            super.setData(item);
            tkNumber.setText(item.getAjbh()+"");
            userName.setText(item.getYhmc());
            userNumber.setText(item.getYhbh());
            syName.setText(item.getAjy());
            syType.setText(item.getAjlx());
            syState.setText(item.getAjzt());
            oldId.setText(item.getLbh());
            phone.setText(item.getLxdh());
            address.setText(item.getYhdz());

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
