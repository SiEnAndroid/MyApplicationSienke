package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.PrintBluetBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * 小票adapter
 * Created by Administrator on 2018/4/25.
 */

public class PrintBluetAdapter extends RecyclerArrayAdapter<PrintBluetBean> {
    public PrintBluetAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new PrintBluetViewHolder(parent);
    }

    public class PrintBluetViewHolder extends BaseViewHolder<PrintBluetBean> {
        private TextView title;//标题
        private TextView connect;//内容

        public PrintBluetViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_print_blue);
            title=$(R.id.tv_blue_title);
            connect=$(R.id.tv_blue_connect);
        }
        @Override
        public void setData(PrintBluetBean item) {
            super.setData(item);
            title.setText(item.getTitle());
            connect.setText(item.getContent());
        }
    }
}
