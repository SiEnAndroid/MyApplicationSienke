package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.UserListviewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author g
 * @FileName SyCheckListAdapter
 * @date 2018/9/30 10:11
 */
public class SyCheckListAdapter extends RecyclerView.Adapter<SyCheckListAdapter.FilterHolder> implements Filterable {

    /**
     * 传递过来的数据源   这个数据是会改变的，所以要有个变量来备份一下原始数据
     */
    private List<UserListviewItem> userListviewList;

    /**
     * 备用数据源
     */
    private List<UserListviewItem> backList;
    public static String searchContent;

    private Context mContext;
    private OnItemClickLitener mOnItemClickLitener;

    public SyCheckListAdapter(Context context, List<UserListviewItem> userListviewList) {
        this.mContext = context;
        this.userListviewList = userListviewList;
        backList = new ArrayList<>();
        backList.addAll(userListviewList);
    }

    public void setmOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterHolder(LayoutInflater.from(mContext).inflate(R.layout.userlist_listview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final FilterHolder holder, final int position) {
        //这里也是过滤后的list

        UserListviewItem userListviewItem = userListviewList.get(position);
        holder.security_number.setText(userListviewItem.getSecurityNumber());
        holder.user_name.setText(userListviewItem.getUserName());
        holder.task_id.setText(userListviewItem.getTaskId());
        holder.number.setText(userListviewItem.getNumber());
        holder.phone_number.setText(userListviewItem.getPhoneNumber());
        holder.security_type.setText(userListviewItem.getSecurityType());
        holder.user_id.setText(userListviewItem.getUserId());
        holder.user_new_id.setText(userListviewItem.getUserNewId());
        holder.userProperty.setText(userListviewItem.getUserProperty());
        holder.address.setText(userListviewItem.getAdress());
        holder.if_edit.setImageResource(userListviewItem.getIfEdit());
        holder.ifChecked.setText(userListviewItem.getIfChecked());
        holder.ifUpload.setText(userListviewItem.getIfUpload());
        holder.tv_allQl.setText(userListviewItem.getSumDosage());
        if ("1980-08-08 11:11:11".equals(userListviewItem.getLastCheckTime())){
            holder.lastCheckTime.setText("暂无");
        }else {
            holder.lastCheckTime.setText(userListviewItem.getLastCheckTime());
        }
        if (userListviewList != null) {
            if (searchContent != null) {
                String userId = userListviewItem.getUserId();
                String userNewId = userListviewItem.getUserNewId();
                String name = userListviewItem.getUserName();
                String taskId = userListviewItem.getTaskId();
                String address = userListviewItem.getAdress();
                String meter_number = userListviewItem.getNumber();
                SpannableStringBuilder style = null;
                if(userNewId.contains(searchContent)){
                    style = new SpannableStringBuilder(userNewId);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#0088ff")),
                            userNewId.indexOf(searchContent),
                            userNewId.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    holder.user_new_id.setText(style);
                } else if (userId.contains(searchContent)) {
                    style = new SpannableStringBuilder(userId);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#0088ff")),
                            userId.indexOf(searchContent),
                            userId.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    holder.user_id.setText(style);
                } else if (taskId.contains(searchContent)) {
                    style = new SpannableStringBuilder(taskId);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")),
                            taskId.indexOf(searchContent),
                            taskId.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    holder.task_id.setText(style);
                }else if (name.contains(searchContent)) {
                    style = new SpannableStringBuilder(name);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#0088ff")),
                            name.indexOf(searchContent),
                            name.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    holder.user_name.setText(style);
                }else if (address.contains(searchContent)) {
                    style = new SpannableStringBuilder(address);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#0088ff")),
                            address.indexOf(searchContent),
                            address.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    holder.address.setText(style);
                }else if (meter_number.contains(searchContent)) {
                    style = new SpannableStringBuilder(meter_number);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#0088ff")),
                            meter_number.indexOf(searchContent),
                            meter_number.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    holder.number.setText(style);
                }
            }
        }
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //注意这里需要是过滤后的list
        return userListviewList.size();
    }
  

    //重写getFilter()方法
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                searchContent = constraint.toString();
                FilterResults results = new FilterResults();
                List<UserListviewItem> list;  
                //筛选后的数据集合
                if (constraint.length() == 0) { 
                    //当过滤的关键字为空的时候，则显示所有的数据
                    list = backList;
                } else {  
                    //否则把符合条件的数据对象添加到集合中
                    list = new ArrayList<>();
                    list.clear();
                    for (UserListviewItem item : backList) {
                        if (item.getTaskId().contains(constraint) || item.getUserNewId().contains(constraint) || item.getUserId().contains(constraint) || item.getUserName().contains(constraint) || item.getNumber().contains(constraint) || item.getAdress().contains(constraint)) {
                            list.add(item);
                        }
                    }
                }
                //将得到的集合保存到FilterResults的value变量中
                results.values = list;
                //将集合的大小保存到FilterResults的count变量中
                results.count = list.size();   
                Log.i("performFiltering", "performFiltering进来了！");
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userListviewList = (List<UserListviewItem>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();  //通知数据发生了改变
                    Log.i("publishResults", "publishResults进来了！数据源长度为："+userListviewList.size());
                } else {
                    notifyDataSetChanged();  //通知数据失效
                }
            }  
        };
    }

    
    
    
    class FilterHolder extends RecyclerView.ViewHolder {
        TextView security_number;  //安检编号
        TextView user_name;        //姓名
        TextView task_id;          //任务编号
        TextView number;          //表编号
        TextView phone_number;    //电话号码
        TextView security_type;   //安检类型
        TextView user_id;         //用户编号
        TextView user_new_id;     //用户新编号
        TextView userProperty;    //用气性质
        TextView address;   //地址
        ImageView if_edit;   //是否编辑（图片）
        TextView ifChecked;  //文字
        TextView ifUpload;   //是否上传
        TextView lastCheckTime;   //上次安检时间
        TextView tv_allQl;//总购气量

        public FilterHolder(View itemView) {
            super(itemView);

            security_number = itemView.findViewById(R.id.security_number);
            user_name = itemView.findViewById(R.id.user_name);
            task_id = itemView.findViewById(R.id.task_id);
            number = itemView.findViewById(R.id.number);
            phone_number = itemView.findViewById(R.id.phone_number);
            security_type = itemView.findViewById(R.id.security_type);
            user_id = itemView.findViewById(R.id.user_id);
            user_new_id = itemView.findViewById(R.id.user_new_id);
            userProperty = itemView.findViewById(R.id.user_property);
            address = itemView.findViewById(R.id.address);
            if_edit = itemView.findViewById(R.id.if_edit);
            ifChecked = itemView.findViewById(R.id.if_checked);
            ifUpload = itemView.findViewById(R.id.if_upload);
            lastCheckTime = itemView.findViewById(R.id.last_check_time);
            tv_allQl = itemView.findViewById(R.id.tv_allQl);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

}
