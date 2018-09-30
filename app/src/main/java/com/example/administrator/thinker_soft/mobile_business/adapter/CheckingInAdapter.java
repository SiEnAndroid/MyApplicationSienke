package com.example.administrator.thinker_soft.mobile_business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.model.BusinessCheckinginItem;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */
public class CheckingInAdapter extends BaseAdapter {

    private Context context;
    private List<BusinessCheckinginItem> itemList;
    private LayoutInflater layoutInflater;

    public CheckingInAdapter(Context context, List<BusinessCheckinginItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (itemList == null) {
            return 0;
        } else {
            return itemList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (itemList == null) {
            return null;
        } else {
            return itemList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.business_checking_listview_item, null);
            viewHolder.xianshi = (TextView) convertView.findViewById(R.id.xianshi);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BusinessCheckinginItem item = itemList.get(position);
        viewHolder.xianshi.setText(item.getAddress());
        viewHolder.time.setText(item.getTime());
        return convertView;
    }

    public class ViewHolder {
        public TextView xianshi;
        public TextView time;
    }
}
