package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportFlowBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;


/**
 * Created by Administrator on 2018/8/1.
 */

public class ReportFlowAdapter extends RecyclerArrayAdapter<ReportFlowBean> {

    public ReportFlowAdapter(Context context) {
        super(context);

    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReportFlowViewHolder(parent);
    }

    public class ReportFlowViewHolder extends BaseViewHolder<ReportFlowBean> {
        /**
         * 姓名
         */
        private TextView name;

        public ReportFlowViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_report_flow);
            name = $(R.id.tv_name);


        }

        @Override
        public void setData(ReportFlowBean item) {
            super.setData(item);
            name.setText(item.getC_PROCESS_NAME());

        }
    }
}
