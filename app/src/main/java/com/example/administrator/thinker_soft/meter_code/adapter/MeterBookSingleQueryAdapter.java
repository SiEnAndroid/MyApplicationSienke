package com.example.administrator.thinker_soft.meter_code.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.MeterBookSingleQueryLvItem;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5 0005.
 */
public class MeterBookSingleQueryAdapter extends BaseAdapter {
    private Context context;
    private List<MeterBookSingleQueryLvItem> itemList;
    private LayoutInflater layoutInflater;

    public MeterBookSingleQueryAdapter(Context context, List<MeterBookSingleQueryLvItem> itemList) {
        this.itemList = itemList;
        this.context = context;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.meter_book_query_single_select_listview_item, null);
            viewHolder.bookName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.bookID = (TextView) convertView.findViewById(R.id.id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MeterBookSingleQueryLvItem item = itemList.get(position);
        viewHolder.bookName.setText(item.getBookName());
        viewHolder.bookID.setText(item.getBookId());
        return convertView;
    }

    class ViewHolder {
        TextView bookName;
        TextView bookID;
    }
}
