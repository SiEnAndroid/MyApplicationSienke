package com.example.administrator.thinker_soft.mobile_business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.BusinessNewsItem;

import java.util.List;

/**
 * Created by Administrator on 2017/6/16.
 */
public class BusinessNewsAdapter extends BaseAdapter {
    private Context context;
    private List<BusinessNewsItem> businessNewsItems;
    private LayoutInflater layoutInflater;

    public BusinessNewsAdapter(Context context, List<BusinessNewsItem> businessNewsItemList) {
        this.context = context;
        this.businessNewsItems = businessNewsItemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (businessNewsItems == null) {
            return 0;
        } else {
            return businessNewsItems.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (businessNewsItems == null) {
            return null;
        } else {
            return businessNewsItems.get(position);
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
            convertView = layoutInflater.inflate(R.layout.activity_business_news_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView name;
        public TextView time;
    }
}
