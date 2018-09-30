package com.example.administrator.thinker_soft.mobile_business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.BusinessColumnListviewItem;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
public class ColumnMessageAdapter extends BaseAdapter {
    private Context context;
    private List<BusinessColumnListviewItem> businessColumnListviewItems;
    private LayoutInflater layoutInflater;

    public ColumnMessageAdapter(Context context, List<BusinessColumnListviewItem> businessColumnListviewItemList) {
        this.context = context;
        this.businessColumnListviewItems = businessColumnListviewItemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (businessColumnListviewItems == null) {
            return 0;
        } else {
            return businessColumnListviewItems.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (businessColumnListviewItems == null) {
            return null;
        } else {
            return businessColumnListviewItems.get(position);
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
            convertView = layoutInflater.inflate(R.layout.business_column_message_item, null);
            viewHolder.pic= (ImageView) convertView.findViewById(R.id.pic);
            viewHolder.title= (TextView) convertView.findViewById(R.id.title);
            viewHolder.leixin= (TextView) convertView.findViewById(R.id.leixin);
            viewHolder.leirong= (TextView) convertView.findViewById(R.id.leirong);
            viewHolder.name= (TextView) convertView.findViewById(R.id.name);
            viewHolder.time= (TextView) convertView.findViewById(R.id.time);
            viewHolder.huifu= (TextView) convertView.findViewById(R.id.huifu);
            viewHolder.pinglun= (TextView) convertView.findViewById(R.id.pinglun);
            viewHolder.yuedu= (TextView) convertView.findViewById(R.id.yuedu);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView pic;
        public TextView title;
        public TextView leirong;
        public TextView leixin;
        public TextView time;
        public TextView name;
        public TextView yuedu;
        public TextView pinglun;
        public TextView huifu;
    }
}
