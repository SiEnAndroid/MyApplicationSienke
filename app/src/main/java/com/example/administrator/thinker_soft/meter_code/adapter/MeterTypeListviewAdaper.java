package com.example.administrator.thinker_soft.meter_code.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.MeterTypeListviewItem;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6 0006.
 */
public class MeterTypeListviewAdaper extends BaseAdapter {
    private Context context;
    private List<MeterTypeListviewItem> itemList;
    private LayoutInflater layoutInflater;
    private int selectedPosition = 0;// 选中的位置
    private int flag;

    public MeterTypeListviewAdaper(Context context, List<MeterTypeListviewItem> itemList,int flag) {
        this.context = context;
        this.itemList = itemList;
        this.flag = flag;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
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
            convertView = layoutInflater.inflate(R.layout.popupwindow_meter_type_listview_item, null);
            viewHolder.rootLinearlayout = (LinearLayout) convertView.findViewById(R.id.root_linearlayout);
            viewHolder.name = (TextView) convertView.findViewById(R.id.type_name);
            viewHolder.id = (TextView) convertView.findViewById(R.id.type_id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MeterTypeListviewItem item = itemList.get(position);
        if(flag == 0){
            if (selectedPosition == position) {
                viewHolder.rootLinearlayout.setBackgroundColor(context.getResources().getColor(R.color.gray_thin));
                viewHolder.name.setTextColor(context.getResources().getColor(android.R.color.white));
                viewHolder.id.setTextColor(context.getResources().getColor(android.R.color.white));
            } else {
                viewHolder.rootLinearlayout.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                viewHolder.name.setTextColor(context.getResources().getColor(R.color.text_gray));
                viewHolder.id.setTextColor(context.getResources().getColor(R.color.text_gray));
            }
        }
        viewHolder.name.setText(item.getName());
        viewHolder.id.setText(item.getId());
        return convertView;
    }

    class ViewHolder {
        LinearLayout rootLinearlayout;
        TextView name;
        TextView id;
    }
}
