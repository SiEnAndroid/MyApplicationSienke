package com.example.administrator.thinker_soft.mobile_business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.PhoneBookListviewItem;

import java.util.List;

/**
 * Created by Administrator on 2017/6/10.
 */
public class PhoneBookAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<PhoneBookListviewItem> phoneBookListviewItems;


    public PhoneBookAdapter(Context context, List<PhoneBookListviewItem> phoneBookListviewItemList) {
        this.context = context;
        this.phoneBookListviewItems = phoneBookListviewItemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (phoneBookListviewItems == null) {
            return 0;
        } else {
            return phoneBookListviewItems.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (phoneBookListviewItems == null) {
            return null;
        } else {
            return phoneBookListviewItems.get(position);
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
            convertView = layoutInflater.inflate(R.layout.business_net_book_listview_item, null);
            viewHolder.name_pic = (ImageView) convertView.findViewById(R.id.name_pic);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView name_pic;
        TextView name;
    }
}
