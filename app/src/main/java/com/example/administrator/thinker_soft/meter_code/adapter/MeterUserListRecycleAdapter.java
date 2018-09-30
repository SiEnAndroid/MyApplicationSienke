package com.example.administrator.thinker_soft.meter_code.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/26 0026.
 */
public class MeterUserListRecycleAdapter extends RecyclerView.Adapter<MeterUserListRecycleAdapter.ViewHolder> {
    private ArrayList<MeterUserListviewItem> itemList;  //传递过来的数据源
    private onRecyclerViewItemClick mOnRvItemClick;
    private onRecyclerViewItemLongClick mOnRvItemLongClick;

    public MeterUserListRecycleAdapter(ArrayList<MeterUserListviewItem> itemList, onRecyclerViewItemClick onRvItemClick, onRecyclerViewItemLongClick mOnRvItemLongClick) {
        this.itemList = itemList;
        this.mOnRvItemClick = onRvItemClick;
        this.mOnRvItemLongClick = mOnRvItemLongClick;
    }

    public void updateData(ArrayList<MeterUserListviewItem> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.meter_user_list_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 绑定数据
        MeterUserListviewItem item = itemList.get(position);
        //编号
        holder.meterId.setText(item.getMeterID());
        holder.userName.setText(item.getUserName());
        holder.userId.setText(item.getUserID());
        holder.meterNumber.setText(item.getMeterNumber());
        holder.oldUserID.setText(item.getOldUserID());
        holder.lastMonthDegree.setText(item.getLastMonthDegree());
        holder.lastMonthDosage.setText(item.getLastMonthDosage());
        holder.thisMonthDegree.setText(item.getThisMonthDegree());
        holder.thisMonthDosage.setText(item.getThisMonthDosage());
        holder.address.setText(item.getAddress());
        holder.uploadState.setText(item.getUploadState());
        holder.meterState.setText(item.getMeterState());
        holder.ifEdit.setImageResource(item.getIfEdit());
        holder.tvRemark.setText(item.getRemark());
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView meterId;
        TextView userName;
        TextView userId;
        TextView meterNumber;
        TextView oldUserID;
        TextView lastMonthDegree;
        TextView lastMonthDosage;
        TextView thisMonthDegree;
        TextView thisMonthDosage;
        TextView address;
        TextView uploadState;
        TextView meterState;
        ImageView ifEdit;
        TextView tvRemark;//备注

        public ViewHolder(View itemView) {
            super(itemView);
            meterId = (TextView) itemView.findViewById(R.id.meter_id);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userId = (TextView) itemView.findViewById(R.id.user_id);
            meterNumber = (TextView) itemView.findViewById(R.id.meter_number);
            oldUserID = (TextView) itemView.findViewById(R.id.old_user_id);
            lastMonthDegree = (TextView) itemView.findViewById(R.id.last_month_degree);
            lastMonthDosage = (TextView) itemView.findViewById(R.id.last_month_dosage);
            thisMonthDegree = (TextView) itemView.findViewById(R.id.this_month_degree);
            thisMonthDosage = (TextView) itemView.findViewById(R.id.this_month_dosage);
            address = (TextView) itemView.findViewById(R.id.address);
            uploadState = (TextView) itemView.findViewById(R.id.upload_state);
            meterState = (TextView) itemView.findViewById(R.id.meter_state);
            ifEdit = (ImageView) itemView.findViewById(R.id.if_edit);
            tvRemark = (TextView) itemView.findViewById(R.id.tv_remark);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnRvItemClick != null) {
                mOnRvItemClick.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnRvItemLongClick != null) {
                mOnRvItemLongClick.onItemLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }

    /**
     * item点击接口
     */
    public interface onRecyclerViewItemClick {
        void onItemClick(View v, int position);
    }

    /**
     * item长按点击接口
     */
    public interface onRecyclerViewItemLongClick {
        void onItemLongClick(View v, int position);
    }
}
