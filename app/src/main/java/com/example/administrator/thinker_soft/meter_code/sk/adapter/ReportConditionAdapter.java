package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportConditionBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * @author g
 * @FileName ReportConditionAdapter
 * @date 2018/8/31 12:43
 */
public class ReportConditionAdapter extends RecyclerArrayAdapter<ReportConditionBean> {

    public ReportConditionAdapter(Context context) {
        super(context);

    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReportConditionViewHolder(parent);
    }

    public class ReportConditionViewHolder extends BaseViewHolder<ReportConditionBean> {
        /**
         * 节点名称
         */
        private TextView tv_node;
        /**
         * 办理状态
         */
        private TextView tv_state;
        /**
         * 办理人
         */
        private TextView tv_transactor;
        /**
         * 办理时间
         */
        private TextView tv_time;
        /**
         * 办理批示
         */
        private TextView tv_approval;

        public ReportConditionViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_reprot_condition);
            tv_node = $(R.id.tv_node);
            tv_state = $(R.id.tv_state);
            tv_transactor = $(R.id.tv_transactor);
            tv_time = $(R.id.tv_time);
            tv_approval = $(R.id.tv_approval);

        }

        @Override
        public void setData(ReportConditionBean item) {
            super.setData(item);
            tv_node.setText(item.getC_NODE_NAME());
            tv_state.setText(item.getTRANSACTION_STATE());
        if (item.getTRANSACTION_STATE()!=null&&item.getTRANSACTION_STATE().equals("流转")){
            tv_state.setTextColor(Color.parseColor("#4aa44a"));
        }else if (item.getTRANSACTION_STATE()!=null&&item.getTRANSACTION_STATE().equals("正在办理")){
            tv_state.setTextColor(Color.parseColor("#FFFD3C3C"));
        }
            tv_transactor.setText(item.getC_USER_NAME());
            tv_time.setText(item.getTRANSACTION_TIME_END());
            tv_approval.setText(item.getN_TRANSACTION_COMMENT()==null?"":item.getN_TRANSACTION_COMMENT());

        }
    }
}
