package com.example.administrator.thinker_soft.mobile_business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.model.NewsItem;

import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */
public class NewsAdapter  extends BaseAdapter{
    private Context context;
    private List<NewsItem> newsItems;
    private LayoutInflater layoutInflater;

    public NewsAdapter(Context context, List<NewsItem> newsItemList) {
        this.context = context;
        this.newsItems = newsItemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (newsItems == null) {
            return 0;
        } else {
            return newsItems.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (newsItems == null) {
            return null;
        } else {
            return newsItems.get(position);
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
            convertView = layoutInflater.inflate(R.layout.business_news_listview_item, null);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsItem item = newsItems.get(position);
        viewHolder.time.setText(item.getTime());
        viewHolder.type.setText(item.getType());
        return convertView;
    }

    public class ViewHolder {
        public TextView time;
        public TextView type;

    }
}
