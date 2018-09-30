package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityExceptionsDonYuBean;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.DateFormatUtil;
import com.example.administrator.thinker_soft.mode.TimeUtil;

import java.util.List;

/**
 * @author 111
 */
public class SecurityExceptionsDonYuAdapter extends RecyclerView.Adapter<SecurityExceptionsDonYuAdapter.ViewHolder> {


    private  SecurityExceptionsDonYuBean resultBean;

    public SecurityExceptionsDonYuAdapter(SecurityExceptionsDonYuBean resultBean) {
        this.resultBean=resultBean;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_security_abnormal, viewGroup, false);
        SecurityExceptionsDonYuAdapter.ViewHolder viewHolder = new SecurityExceptionsDonYuAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        List<SecurityExceptionsDonYuBean.ListBean> list = resultBean.getList();
        SecurityExceptionsDonYuBean.ListBean listBean = list.get(i);

        holder.userName.setText(listBean.getUserName()+"");
        if (listBean.getSecurityNumber() == null ){
            holder.zone.setText("");
        }else {
            holder.zone.setText( String.valueOf(listBean.getOldNumber())+"");
        }
        holder.state.setText(listBean.getUserNumber()+"");
        holder.userId.setText(listBean.getSecurityScreening()+"");
        holder.oldUserid.setText( listBean.getLastSecurityCheckTime());
        holder.data.setText(listBean.getAccountOpeningDate());
        holder.phone.setText(listBean.getContactNumber()==null?"":listBean.getContactNumber());

        holder.address.setText(listBean.getUserAddress()+"");
        Log.d("pgl","=== 条目信息"+"执行了");
       /* if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new SecurityExceptionsDonYuAdapter.MyOnClickListener(position, list1.get(position)));
        }*/
    }

    @Override
    public int getItemCount() {
        return resultBean.getList().size();
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


}
