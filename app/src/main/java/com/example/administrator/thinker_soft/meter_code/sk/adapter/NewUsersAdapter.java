package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AddedBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NewUsersBean;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewUsersAdapter extends RecyclerView.Adapter<NewUsersAdapter.ViewHolder>{

    private  List<NewUsersBean.ListBean> newUsersList;
    private NewUsersAdapter.OnItemClickListener onItemClickListener;
    public static HashMap<Integer, Boolean> isSelected;

    public NewUsersAdapter(List<NewUsersBean.ListBean> newUsersList) {
        this.newUsersList = newUsersList;
        init();
    }
    public void clear(){
        this.newUsersList.clear();
        // this.mSourceList.clear();
        init();

    }

    public void addAll(List<NewUsersBean.ListBean> mFilterListS) {
        //   mFilterList.addAll(mFilterListS);
        //     this.mSourceList.addAll(mFilterList);
        init();
    }

    public void setmFilterList(List<NewUsersBean.ListBean> mFilterList) {
        this.newUsersList = mFilterList;
        //   this.mSourceList.addAll(mFilterList);
        //   init();
    }

    // 初始化 设置所有item都为未选择
    public void init() {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < newUsersList.size(); i++) {
            isSelected.put(i, false);
        }
    }


    /**
     * 设置点击事件
     */
    public void setOnItemClickListener(NewUsersAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position, NewUsersBean.ListBean data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_new_users, viewGroup, false);
        NewUsersAdapter.ViewHolder viewHolder = new NewUsersAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NewUsersBean.ListBean listBean = newUsersList.get(position);

        holder.mTvName.setText(listBean.getUserName()+"");
        if (listBean.getGasMeterNumber() == null ){
            holder.tvNumber.setText("无");
        }else {
            holder.tvNumber.setText( String.valueOf(listBean.getGasMeterNumber())+"");
        }
        holder.newNumber.setText(listBean.getUserNumber()+"");
//                renarks.setText( listBean.getAjbz()==null?"":listBean.getAjbz());
        holder.partitionName.setText(listBean.getPartitionName()+"");

//        holder.oldUserid.setText( listBean.getScajsj() == 0 ? "" : TimeUtil.getCustonFormatTime( listBean.getScajsj(),"yyyy-MM-dd"));
        holder.lastSecurityCheckTime.setText( listBean.getLastSecurityCheckTime() );
//        holder.data.setText(DateFormatUtil.format(listBean.getKhrq())+"");
        holder.accountOpeningTime.setText(listBean.getAccountOpeningTime());
        holder.phone.setText(listBean.getContactNumber()==null?"":listBean.getContactNumber());

        holder.address.setText(listBean.getUserAddress()+"");
        Log.d("pgl","=== 条目信息"+"执行了");
        holder.mCheckBox.setChecked(isSelected.get(position));
        holder.itemView.setSelected(isSelected.get(position));
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new NewUsersAdapter.MyOnClickListener(position, newUsersList.get(position)));
        }

    }

    @Override
    public int getItemCount() {
        return newUsersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvName;//用户名
        CheckBox mCheckBox;
        TextView tvNumber;//气表编号
        TextView phone;//电话
        TextView newNumber;//用户编好
        TextView address;//用户地址

        TextView partitionName;//分区
        TextView accountOpeningTime;//开户时间
        TextView lastSecurityCheckTime;//上次安检时间

        ViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_userName);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            phone = (TextView) itemView.findViewById(R.id.tv_phone_number);
            newNumber = (TextView) itemView.findViewById(R.id.tv_user_id);
            address=(TextView) itemView.findViewById(R.id.tv_address);
            partitionName =(TextView) itemView.findViewById(R.id.partition_name);
            accountOpeningTime =(TextView) itemView.findViewById(R.id.account_opening_time);
            lastSecurityCheckTime =(TextView) itemView.findViewById(R.id.last_security_check_time);

        }
    }



    private class MyOnClickListener implements View.OnClickListener {
        private int position;
        private NewUsersBean.ListBean data;

        public MyOnClickListener(int position, NewUsersBean.ListBean data) {
            this.position = position;
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, position, data);
        }
    }

}
