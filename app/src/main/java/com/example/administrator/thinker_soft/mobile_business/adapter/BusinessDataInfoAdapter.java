package com.example.administrator.thinker_soft.mobile_business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.model.DataInfoItem;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */
public class BusinessDataInfoAdapter extends BaseAdapter {
    private Context context;
    private List<DataInfoItem> dataInfoItemList;
    private LayoutInflater layoutInflater;

    public BusinessDataInfoAdapter(Context context, List<DataInfoItem> itemList) {
        this.context = context;
        this.dataInfoItemList = itemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (dataInfoItemList == null) {
            return 0;
        } else {
            return dataInfoItemList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (dataInfoItemList == null) {
            return null;
        } else {
            return dataInfoItemList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.activity_businessdata_listview_item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataInfoItem item = dataInfoItemList.get(position);
        viewHolder.title.setText(item.getTitle());
        viewHolder.time.setText(item.getTime());
        return convertView;
    }

    public class ViewHolder {
        public TextView title;
        public TextView time;
    }
}
