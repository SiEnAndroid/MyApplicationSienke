package com.example.administrator.thinker_soft.meter_code.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserCoordinateManageItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/1 0001.
 */
public class MeterCoordinateManageRecycleAdapter extends RecyclerView.Adapter<MeterCoordinateManageRecycleAdapter.CoordinateViewHolder> {
    private ArrayList<MeterUserCoordinateManageItem> itemList;  //传递过来的数据源
    private Context mContext;
    private OnRecyclLeftItemListener leftItemListener;
    private OnRecyclRightItemListener rightItemListener;

    public MeterCoordinateManageRecycleAdapter(Context context, ArrayList<MeterUserCoordinateManageItem> itemList) {
        this.mContext = context;
        this.itemList = itemList;
    }

    public void setLeftItemListener(OnRecyclLeftItemListener leftListener) {
        this.leftItemListener = leftListener;
    }

    public void setRightItemListener(OnRecyclRightItemListener rightListener) {
        this.rightItemListener = rightListener;
    }

    @Override
    public CoordinateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.meter_user_coordinate_manege_list_item, parent, false);
        // 实例化viewholder
        CoordinateViewHolder viewHolder = new CoordinateViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CoordinateViewHolder holder, final int position) {
        final MeterUserCoordinateManageItem item = itemList.get(position);
        holder.meterId.setText(item.getMeterID());
        holder.userName.setText(item.getUserName());
        holder.userId.setText(item.getUserID());
        holder.meterNumber.setText(item.getMeterNumber());
        holder.longitude.setText(item.getLongitude());
        holder.latitude.setText(item.getLatitude());
        holder.address.setText(item.getAddress());
        holder.leftCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftItemListener != null) {
                    //接口实例化后的而对象，调用重写后的方法
                    leftItemListener.OnRecycleLeftItemClick(v, position);
                }
            }
        });
        holder.obtainCoodinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rightItemListener != null){
                    rightItemListener.OnRecycleRightItemClick(v,position);
                }
            }
        });
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    public class CoordinateViewHolder extends RecyclerView.ViewHolder {
        CardView leftCardview;   //点击左边布局进入抄表详情页
        TextView meterId;
        TextView userName;
        TextView userId;
        TextView meterNumber;
        TextView longitude;
        TextView latitude;
        TextView address;
        CardView obtainCoodinate;

        public CoordinateViewHolder(View itemView) {
            super(itemView);
            meterId = (TextView) itemView.findViewById(R.id.meter_id);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userId = (TextView) itemView.findViewById(R.id.user_id);
            meterNumber = (TextView) itemView.findViewById(R.id.meter_number);
            longitude = (TextView) itemView.findViewById(R.id.longitude);
            latitude = (TextView) itemView.findViewById(R.id.latitude);
            address = (TextView) itemView.findViewById(R.id.address);
            leftCardview = (CardView) itemView.findViewById(R.id.left_cardview);
            obtainCoodinate = (CardView) itemView.findViewById(R.id.obtain_coordinate);
        }
    }

    /**
     * 左边item点击接口
     */
    public interface OnRecyclLeftItemListener {
        void OnRecycleLeftItemClick(View view, int position);
    }

    /**
     * 右边item点击接口
     */
    public interface OnRecyclRightItemListener {
        void OnRecycleRightItemClick(View view, int position);
    }
}
