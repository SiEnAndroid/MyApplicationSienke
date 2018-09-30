package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.BookInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by Administrator on 2018/4/19.
 * 抄表员
 */

public class MeterReaderDownAdapter extends RecyclerArrayAdapter<BookInfo> {


    public MeterReaderDownAdapter(Context context) {
        super(context);
        initCheck(false);

    }

    // 初始化map集合
    public void initCheck(boolean flag) {
        for (int i = 0; i < getAllData().size(); i++) {  // map集合的数量和list的数量是一致的
            getAllData().get(i).setChecked(flag);  // 设置默认的显示
        }
    }

//    /**
//     * 单选
//     *
//     * @param postion
//     */
//    public void singlesel(int postion) {
//        for (BookInfo info : getAllData()) {
//            info.setChecked(false);
//        }
//        getAllData().get(postion).setChecked(true);
//        notifyDataSetChanged();
//    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeterReaderDownViewHolder(parent);
    }

    public class MeterReaderDownViewHolder extends BaseViewHolder<BookInfo> {
        private TextView meterName;//名称
        private TextView dataId;//id
        private CheckBox checkState;//id


        public MeterReaderDownViewHolder(ViewGroup parent) {
            super(parent, R.layout.meter_area_listview_item);
            meterName = $(R.id.meter_name);
            dataId = $(R.id.data_id);
            checkState = $(R.id.check_state);


        }

        @Override
        public void setData(BookInfo data) {
            super.setData(data);
            meterName.setText(data.getBOOKREMARK());
            dataId.setVisibility(View.GONE);
            checkState.setChecked(data.isChecked());
            //dataId.setText(data.getID());
            //checkState.setChecked(mSelectedPos==getAdapterPosition());

        }
    }
}
