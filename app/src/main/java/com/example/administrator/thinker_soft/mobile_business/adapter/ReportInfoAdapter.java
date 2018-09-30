package com.example.administrator.thinker_soft.mobile_business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.model.ReportInfoItem;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
public class ReportInfoAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<ReportInfoItem> reportInfoItems;

    public ReportInfoAdapter(Context context, List<ReportInfoItem> reportInfoItemList) {
        this.context = context;
        this.reportInfoItems = reportInfoItemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (reportInfoItems == null) {
            return 0;
        } else {
            return reportInfoItems.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (reportInfoItems == null) {
            return null;
        } else {
            return reportInfoItems.get(position);
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
            convertView = layoutInflater.inflate(R.layout.report_listview_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ReportInfoItem item = reportInfoItems.get(position);
        viewHolder.name.setText(item.getName());
        viewHolder.time.setText(item.getTime());
        viewHolder.content.setText(item.getContent());
        return convertView;
    }

    public class ViewHolder{
        TextView name;
        TextView time;
        TextView content;
    }
}
