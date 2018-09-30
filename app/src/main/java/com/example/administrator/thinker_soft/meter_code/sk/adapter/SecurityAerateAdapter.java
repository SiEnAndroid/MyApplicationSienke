package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AetateBean;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.DateFormatUtil;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;



/**
 * Created by Administrator on 2018/8/1.
 */

public class SecurityAerateAdapter  extends RecyclerArrayAdapter<AetateBean.ListBean> {

    public SecurityAerateAdapter(Context context) {
        super(context);

    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new SecurityAerateViewHolder(parent);
    }

    public class SecurityAerateViewHolder extends BaseViewHolder<AetateBean.ListBean> {

        private TextView userName;//姓名
        private TextView userId;//用户编号
        private TextView oldUserid;//老编号
        private TextView zone;//分区
        private TextView state;//安检状态
        private TextView qbNumber;//气表编号
        private TextView data;//开户日期
        private TextView phone;//电话
        private TextView address;//地址


        public SecurityAerateViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_aerate);
            userName = $(R.id.tv_userName);
            qbNumber = $(R.id.tv_qbNumber);
            zone = $(R.id.tv_zone);
            userId = $(R.id.tv_userId);
            oldUserid = $(R.id.tv_oldUser_id);
            data = $(R.id.tv_data);
            phone = $(R.id.tv_phone_number);
            address = $(R.id.tv_address);


        }

        @Override
        public void setData(AetateBean.ListBean item) {
            super.setData(item);
            userName.setText(item.getYhmc());
            userId.setText(item.getYhbh());
            oldUserid.setText(item.getLbh());
            qbNumber.setText(item.getQbbh());
            zone.setText(item.getFq());
            data.setText(DateFormatUtil.format(item.getKhrq()));
            phone.setText(item.getLidh());
            address.setText(item.getYhdz());
        }
    }
}
