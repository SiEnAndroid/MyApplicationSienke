package com.example.administrator.thinker_soft.meter_code.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.MeterUserDetailActivity;
import com.example.administrator.thinker_soft.meter_code.activity.ObtainMeterCoodinateActivity;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserCoordinateManageItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/19 0019.
 */
public class MeterUserCoordinateManageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MeterUserCoordinateManageItem> itemList;  //传递过来的数据源

    public MeterUserCoordinateManageAdapter(Context context, ArrayList<MeterUserCoordinateManageItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    private LayoutInflater layoutInflater;
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
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.meter_user_coordinate_manege_list_item,null);
            viewHolder.meterId = (TextView) convertView.findViewById(R.id.meter_id);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.userId = (TextView) convertView.findViewById(R.id.user_id);
            viewHolder.meterNumber = (TextView) convertView.findViewById(R.id.meter_number);
            viewHolder.longitude = (TextView) convertView.findViewById(R.id.longitude);
            viewHolder.latitude = (TextView) convertView.findViewById(R.id.latitude);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            viewHolder.leftLayout = (LinearLayout) convertView.findViewById(R.id.left_layout);
            viewHolder.obtainCoodinate = (ImageView) convertView.findViewById(R.id.obtain_coordinate);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MeterUserCoordinateManageItem item = itemList.get(position);
        viewHolder.meterId.setText(item.getMeterID());
        viewHolder.userName.setText(item.getUserName());
        viewHolder.userId.setText(item.getUserID());
        viewHolder.meterNumber.setText(item.getMeterNumber());
        viewHolder.longitude.setText(item.getLongitude());
        viewHolder.latitude.setText(item.getLatitude());
        viewHolder.address.setText(item.getAddress());

        viewHolder.leftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MeterUserDetailActivity.class);
                intent.putExtra("user_id", item.getUserID());
                context.startActivity(intent);
            }
        });
        viewHolder.obtainCoodinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ObtainMeterCoodinateActivity.class);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        LinearLayout leftLayout;   //点击左边布局进入抄表详情页
        TextView meterId;
        TextView userName;
        TextView userId;
        TextView meterNumber;
        TextView longitude;
        TextView latitude;
        TextView address;
        ImageView obtainCoodinate;
    }
}
