package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.BookInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2018/4/19.
 * 抄表本
 */

public class MeterReaderBookDownAdapter extends RecyclerArrayAdapter<BookInfo> {

    private HashMap<Integer, Boolean> map=new HashMap<Integer, Boolean>();
    public MeterReaderBookDownAdapter(Context context) {
        super(context);
        initCheck(false);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeterReaderBookDownViewHolder(parent);
    }
    // 初始化map集合
    public void initCheck(boolean flag) {
        for (int i = 0; i < getAllData().size(); i++) {  // map集合的数量和list的数量是一致的
            map.put(i, i==0?true:flag);  // 设置默认的显示
        }
    }
    /**
     * 全选
     */
    public void All() {
        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
        boolean shouldall = false;
        for (Map.Entry<Integer, Boolean> entry : entries) {
            Boolean value = entry.getValue();
            if (!value) {
                shouldall = true;
                break;
            }
        }
        for (Map.Entry<Integer, Boolean> entry : entries) {
            entry.setValue(shouldall);
        }
        notifyDataSetChanged();
    }

    /**
     * 反选
     */
    public void neverall() {
        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
        for (Map.Entry<Integer, Boolean> entry : entries) {
            entry.setValue(!entry.getValue());
        }
        notifyDataSetChanged();
    }

    /**
     * 单选
     *
     * @param postion
     */
    public void singlesel(int postion) {
        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
        for (Map.Entry<Integer, Boolean> entry : entries) {
            entry.setValue(false);
        }
        map.put(postion, true);
        notifyDataSetChanged();
    }
    public class MeterReaderBookDownViewHolder extends BaseViewHolder<BookInfo> {
        private TextView meterName;//名称
        private TextView dataId;//id
        private CheckBox checkState;//id

        public MeterReaderBookDownViewHolder(ViewGroup parent) {
            super(parent, R.layout.meter_area_listview_item);
            meterName=$(R.id.meter_name);
            dataId=$(R.id.data_id);
            checkState=$(R.id.check_state);

        }

        @Override
        public void setData(BookInfo data) {
            super.setData(data);
            meterName.setText(data.getBOOK());
         checkState.setChecked(map.get(getAdapterPosition()));
            dataId.setText(data.getID());
         // checkState.setChecked(getIsCheck().get(position));
        }


    }
}
