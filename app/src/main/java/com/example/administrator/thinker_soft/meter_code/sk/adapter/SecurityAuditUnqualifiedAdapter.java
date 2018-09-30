package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AuditUnqualifiedBean;
import com.example.administrator.thinker_soft.mode.TimeUtil;

import java.util.List;

/**
 * Created by 111 on 2018/8/16.
 */

public class SecurityAuditUnqualifiedAdapter extends RecyclerView.Adapter<SecurityAuditUnqualifiedAdapter.ViewHolder>{
    private final List<AuditUnqualifiedBean.ListBean> list;
    private SecurityAuditUnqualifiedAdapter.OnItemClickListener onItemClickListener;

    /**
     * 设置点击事件
     */
    public void setOnItemClickListener(SecurityAuditUnqualifiedAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position, AuditUnqualifiedBean.ListBean data);
    }

    public SecurityAuditUnqualifiedAdapter(List<AuditUnqualifiedBean.ListBean> list) {
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audit_unqualified, viewGroup, false);
        SecurityAuditUnqualifiedAdapter.ViewHolder viewHolder = new SecurityAuditUnqualifiedAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        AuditUnqualifiedBean.ListBean listBean = list.get(position);
        holder.userName.setText(listBean.getYHMC()+"");
        if (listBean.getLBH() == null ){
            holder.oldNumber.setText("");
        }else {
            holder.oldNumber.setText( String.valueOf(listBean.getLBH())+"");
        }
//        holder.oldNumber.setText(String.valueOf(listBean.getLBH())+"" ==""?"":String.valueOf(listBean.getLBH())+"");
        holder.securityState.setText(listBean.getAJZT()+"");
        holder.projectName.setText(listBean.getAJJHMC()+"");

        if (String.valueOf(listBean.getAJSJ())+"" ==null ){
            holder.securityTime.setText("");
        }else {
            holder.securityTime.setText( TimeUtil.getCustonFormatTime( listBean.getAJSJ(),"yyyy-MM-dd"));
        }
        holder.phone.setText(listBean.getLXDH()==null?"":listBean.getLXDH());
        holder.securityName.setText(listBean.getAJY()==null?"":listBean.getAJY());
        holder.userNumber.setText(listBean.getYHBH()==null?"":listBean.getYHBH());
        holder.tableNumber.setText(listBean.getBBH()==null?"":listBean.getBBH());

        if (onItemClickListener != null) {
            Log.d("pgl","=== 条目信息"+position+"执行了");
            holder.itemView.setOnClickListener(new SecurityAuditUnqualifiedAdapter.MyOnClickListener(position, list.get(position)));
        }

    }
    private class MyOnClickListener implements View.OnClickListener {
        private int position;
        private AuditUnqualifiedBean.ListBean data;

        public MyOnClickListener(int position, AuditUnqualifiedBean.ListBean data) {
            this.position = position;
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, position, data);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;//用户姓名
        private TextView securityState;//安检状态
        private TextView projectName;//计划名称
        private TextView userNumber;//用户编号
        private TextView oldNumber;//老编号
        private TextView securityName;//安检员
        private TextView phone;//电话
        private TextView securityTime;//安检时间
        private TextView tableNumber;//表编号
        private TextView address;//地址

            ViewHolder(View itemView) {
                super(itemView);
                userName = (TextView) itemView.findViewById(R.id.user_name);
                securityState = (TextView)itemView.findViewById(R.id.security_state);
                projectName = (TextView)itemView.findViewById(R.id.project_name);
                userNumber =(TextView)itemView.findViewById(R.id.user_number);
                oldNumber = (TextView)itemView.findViewById(R.id.old_number);
                securityName = (TextView)itemView.findViewById(R.id.security_name);
                securityTime = (TextView)itemView.findViewById(R.id.security_time);
                tableNumber = (TextView)itemView.findViewById(R.id.table_number);
                phone = (TextView)itemView.findViewById(R.id.tv_phone_number);
                address = (TextView)itemView.findViewById(R.id.tv_address);
            }
        }

}
