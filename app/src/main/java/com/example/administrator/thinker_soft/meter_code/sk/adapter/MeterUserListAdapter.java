package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by Administrator on 2018/4/22.
 */

public class MeterUserListAdapter extends RecyclerArrayAdapter<MeterUserListviewItem> {

    public MeterUserListAdapter(Context context) {
        super(context);

    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeterUserListViewHolder(parent);
    }

    public class MeterUserListViewHolder extends BaseViewHolder<MeterUserListviewItem> {

        private TextView meterId;
        private TextView userName;
        private TextView userId;
        private TextView meterNumber;
        private TextView oldUserID;
        private TextView lastMonthDegree;
        private TextView lastMonthDosage;
        private TextView thisMonthDegree;
        private TextView thisMonthDosage;
        private TextView address;
        private TextView uploadState;
        private TextView meterState;
        private ImageView ifEdit;
        //备注
        private TextView tvRemark;
        private  TextView tvYs;

        public MeterUserListViewHolder(ViewGroup parent) {
            super(parent, R.layout.meter_user_list_item);
            meterId = $(R.id.meter_id);
            userName = $(R.id.user_name);
            userId = $(R.id.user_id);
            meterNumber = $(R.id.meter_number);
            oldUserID = $(R.id.old_user_id);
            lastMonthDegree = $(R.id.last_month_degree);
            lastMonthDosage = $(R.id.last_month_dosage);
            thisMonthDegree = $(R.id.this_month_degree);
            thisMonthDosage = $(R.id.this_month_dosage);
            address = $(R.id.address);
            uploadState = $(R.id.upload_state);
            meterState = $(R.id.meter_state);
            ifEdit = $(R.id.if_edit);
            tvRemark = $(R.id.tv_remark);
            tvYs = $(R.id.tv_ys);
            


        }

        @Override
        public void setData(MeterUserListviewItem item) {
            super.setData(item);
            //编号
            meterId.setText(item.getMeterID());
            userName.setText(item.getUserName());
            userId.setText(item.getUserID());
            meterNumber.setText(item.getMeterNumber());
            oldUserID.setText(item.getOldUserID());
            lastMonthDegree.setText(item.getLastMonthDegree());
            lastMonthDosage.setText(item.getLastMonthDosage());
            thisMonthDegree.setText(item.getThisMonthDegree());
            thisMonthDosage.setText(item.getThisMonthDosage());
            address.setText(item.getAddress());
            uploadState.setText(item.getUploadState());
            meterState.setText(item.getMeterState());
            ifEdit.setImageResource(item.getIfEdit());
            tvRemark.setText(item.getRemark());
            itemView.setTag(item);
            tvYs.setTextColor(item.getRedColor());
            thisMonthDegree.setTextColor(item.getRedColor());
            thisMonthDosage.setTextColor(item.getRedColor());
      
        }
    }
}
