package com.example.administrator.thinker_soft.meter_code.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.MeterMapCityListItem;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20 0020.
 */
public class MeterMapCityListAdapter extends BaseAdapter{
    private Context context;
    private List<MeterMapCityListItem> itemList;
    private LayoutInflater layoutInflater;

    public MeterMapCityListAdapter(Context context, List<MeterMapCityListItem> itemList) {
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
            convertView = layoutInflater.inflate(R.layout.meter_map_city_list_item, null);
            viewHolder.cityName = (TextView) convertView.findViewById(R.id.city_name);
            viewHolder.cityID = (TextView) convertView.findViewById(R.id.city_id);
            viewHolder.citySize = (TextView) convertView.findViewById(R.id.city_size);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MeterMapCityListItem item = itemList.get(position);
        viewHolder.cityName.setText(item.getCityName());
        viewHolder.cityID.setText(item.getCityID());
        viewHolder.citySize.setText(item.getCitySize());
        return convertView;
    }

    class ViewHolder {
        TextView cityName;
        TextView cityID;
        TextView citySize;
    }
}
