package com.example.administrator.thinker_soft.mobile_business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.BusinessFlowListviewItem;

import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */
public class BusinessFlowAdapter extends BaseAdapter {
    private Context context;
    private List<BusinessFlowListviewItem> businessFlowListviewItems;
    private LayoutInflater layoutInflater;

    public BusinessFlowAdapter(Context context, List<BusinessFlowListviewItem> businessFlowListviewItemList) {
        this.context = context;
        this.businessFlowListviewItems = businessFlowListviewItemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (businessFlowListviewItems == null) {
            return 0;
        } else {
            return businessFlowListviewItems.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (businessFlowListviewItems == null) {
            return null;
        } else {
            return businessFlowListviewItems.get(position);
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
            convertView = layoutInflater.inflate(R.layout.business_flow_listview_item, null);
            viewHolder.pic = (ImageView) convertView.findViewById(R.id.pic);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BusinessFlowListviewItem item = businessFlowListviewItems.get(position);
        viewHolder.name.setText(item.getName());
        viewHolder.time.setText(item.getTime());
        viewHolder.title.setText(item.getTitle());
        return convertView;
    }

    public class ViewHolder{
        public ImageView pic;
        public TextView name;
        public TextView time;
        public TextView title;
    }
}
