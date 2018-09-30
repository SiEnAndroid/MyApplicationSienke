package com.example.administrator.thinker_soft.patrol_inspection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.patrol_inspection.model.EquipmentItem;

import java.util.ArrayList;

public class EquipmentPatrolAdapter extends BaseAdapter {
    private Context mcontext;
    private ArrayList<EquipmentItem> itemList;
    private LayoutInflater inflater;

    public EquipmentPatrolAdapter(Context mcontext, ArrayList<EquipmentItem> itemList) {
        this.mcontext = mcontext;
        this.itemList = itemList;
        if (mcontext != null) {
            inflater = LayoutInflater.from(mcontext);
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
            convertView = inflater.inflate(R.layout.equipment_patrol_item, null);
            viewHolder.equipmentName = (TextView) convertView.findViewById(R.id.equipment_name);
            viewHolder.equipmentInfo = (TextView) convertView.findViewById(R.id.equipment_info);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            viewHolder.state = (TextView) convertView.findViewById(R.id.state);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        EquipmentItem item = itemList.get(position);
        viewHolder.equipmentName.setText(item.getEquipmentName());
        viewHolder.equipmentInfo.setText(item.getEquipmentInfo());
        viewHolder.distance.setText(item.getDistance());
        viewHolder.state.setText(item.getState());
        return convertView;
    }

    static class ViewHolder {
        private TextView equipmentName;
        private TextView equipmentInfo;
        private TextView distance;
        private TextView state;
    }
}