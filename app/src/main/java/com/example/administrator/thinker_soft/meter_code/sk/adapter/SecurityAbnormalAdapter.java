package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityChecksBean;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.DateFormatUtil;
import com.example.administrator.thinker_soft.mode.TimeUtil;

import java.util.List;



/**
 * Created by 111 on 2018/8/3.
 */

public class SecurityAbnormalAdapter extends RecyclerView.Adapter<SecurityAbnormalAdapter.ViewHolder>{

    private  List<SecurityChecksBean> list;
    private OnItemClickListener onItemClickListener;

    /**
     * 设置点击事件
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public SecurityAbnormalAdapter(List<SecurityChecksBean> list) {
        this.list = list;
    }
    @Override
    public int getItemCount() {
        return list.get(0).getList().size();
    }

    @Override
    public SecurityAbnormalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_security_abnormal, parent, false);
        SecurityAbnormalAdapter.ViewHolder viewHolder = new SecurityAbnormalAdapter.ViewHolder(view);
        return viewHolder;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position, SecurityChecksBean.ListBean data);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;//姓名
        private TextView zone;//老编号
        private TextView state;//用户编号
        //        private  TextView renarks; //安检备注
        private TextView userId;//安检情况
        private TextView oldUserid;//上次安检时间
        private TextView data;//开户日期
        private TextView phone;//电话
        private TextView address;//地址

        ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.tv_userName);
            zone = (TextView)itemView.findViewById(R.id.tv_zone);
            state =(TextView)itemView.findViewById(R.id.tv_qbNumber);
            zone = (TextView)itemView.findViewById(R.id.tv_zone);
            userId =(TextView)itemView.findViewById(R.id.tv_userId);
            oldUserid = (TextView)itemView.findViewById(R.id.tv_oldUser_id);
            data = (TextView)itemView.findViewById(R.id.tv_data);
            phone = (TextView)itemView.findViewById(R.id.tv_phone_number);
            address = (TextView)itemView.findViewById(R.id.tv_address);
        }
    }

    @Override
    public void onBindViewHolder(SecurityAbnormalAdapter.ViewHolder holder, int position) {
        List<SecurityChecksBean.ListBean> list1 = list.get(0).getList();
        SecurityChecksBean.ListBean listBean = list1.get(position);
        holder.userName.setText(listBean.getYhmc()+"");
            if (listBean.getLbh() == null ){
                holder.zone.setText("");
            }else {
                holder.zone.setText( String.valueOf(listBean.getLbh())+"");
            }
//        holder.zone.setText(String.valueOf(listBean.getLbh())+"" ==""?"":String.valueOf(listBean.getLbh())+"");
        holder.state.setText(listBean.getYhbh()+"");
//                renarks.setText( listBean.getAjbz()==null?"":listBean.getAjbz());
        holder.userId.setText(listBean.getAjqk()+"");
        holder.oldUserid.setText( listBean.getScajsj() == 0 ? "" : TimeUtil.getCustonFormatTime( listBean.getScajsj(),"yyyy-MM-dd"));
        holder.data.setText(DateFormatUtil.format(listBean.getKhrq())+"");
        holder.phone.setText(listBean.getLxdh()==null?"":listBean.getLxdh());

        holder.address.setText(listBean.getYhdz()+"");
        Log.d("pgl","=== 条目信息"+"执行了");
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new MyOnClickListener(position, list1.get(position)));
        }

    }
    private class MyOnClickListener implements View.OnClickListener {
        private int position;
        private SecurityChecksBean.ListBean data;

        public MyOnClickListener(int position, SecurityChecksBean.ListBean data) {
            this.position = position;
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, position, data);
        }
    }

}
