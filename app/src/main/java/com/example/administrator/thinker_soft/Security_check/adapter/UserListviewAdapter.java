package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.UserListviewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
public class UserListviewAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<UserListviewItem> userListviewList;  //传递过来的数据源   这个数据是会改变的，所以要有个变量来备份一下原始数据
    private List<UserListviewItem> backList;  //备用数据源
    private LayoutInflater layoutInflater;
    private MyFilter myFilter;
    public static String searchContent;

    public UserListviewAdapter(Context context, List<UserListviewItem> userListviewList) {
        this.context = context;
        this.userListviewList = userListviewList;
        //backList是暂存原来所用的数据，当筛选内容为空时，显示所有数据，并且必须 new 一个对象，
        //而不能backList = userListviewList;,这样的话当userListviewList改变时backList也就改变了
        backList = new ArrayList<>();
        backList.addAll(userListviewList);
        //backList = userListviewList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (userListviewList == null) {
            return 0;
        } else {
            Log.i("UserListviewAdapter", "UserListviewAdapter返回的数据源长度为："+userListviewList.size());
            return userListviewList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (userListviewList == null) {
            return null;
        } else {
            return userListviewList.get(position);
        }
    }

    public List<UserListviewItem> getUserListviewList() {
        return userListviewList;
    }

    public void addAll(List<UserListviewItem> allList){
        userListviewList.clear();
        backList.clear();
        userListviewList.addAll(allList);
        backList.addAll(allList);
        notifyDataSetChanged();
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
            convertView = layoutInflater.inflate(R.layout.userlist_listview_item, null);
            viewHolder.security_number = (TextView) convertView.findViewById(R.id.security_number);
            viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.task_id = (TextView) convertView.findViewById(R.id.task_id);
            viewHolder.number = (TextView) convertView.findViewById(R.id.number);
            viewHolder.phone_number = (TextView) convertView.findViewById(R.id.phone_number);
            viewHolder.security_type = (TextView) convertView.findViewById(R.id.security_type);
            viewHolder.user_id = (TextView) convertView.findViewById(R.id.user_id);
            viewHolder.user_new_id = (TextView) convertView.findViewById(R.id.user_new_id);
            viewHolder.userProperty = (TextView) convertView.findViewById(R.id.user_property);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            viewHolder.if_edit = (ImageView) convertView.findViewById(R.id.if_edit);
            viewHolder.ifChecked = (TextView) convertView.findViewById(R.id.if_checked);
            viewHolder.ifUpload = (TextView) convertView.findViewById(R.id.if_upload);
            viewHolder.lastCheckTime = (TextView) convertView.findViewById(R.id.last_check_time);
            viewHolder.tv_allQl = (TextView) convertView.findViewById(R.id.tv_allQl);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UserListviewItem userListviewItem = userListviewList.get(position);
    //    viewHolder.security_number.setText(userListviewItem.getSecurityNumber());
        viewHolder.security_number.setText(userListviewItem.getSecurityNumber());
        viewHolder.user_name.setText(userListviewItem.getUserName());
        viewHolder.task_id.setText(userListviewItem.getTaskId());
        viewHolder.number.setText(userListviewItem.getNumber());
        viewHolder.phone_number.setText(userListviewItem.getPhoneNumber());
        viewHolder.security_type.setText(userListviewItem.getSecurityType());
        viewHolder.user_id.setText(userListviewItem.getUserId());
        viewHolder.user_new_id.setText(userListviewItem.getUserNewId());
        viewHolder.userProperty.setText(userListviewItem.getUserProperty());
        viewHolder.address.setText(userListviewItem.getAdress());
        viewHolder.if_edit.setImageResource(userListviewItem.getIfEdit());
        viewHolder.ifChecked.setText(userListviewItem.getIfChecked());
        viewHolder.ifUpload.setText(userListviewItem.getIfUpload());
        viewHolder.tv_allQl.setText(userListviewItem.getSumDosage());
        if ("1980-08-08 11:11:11".equals(userListviewItem.getLastCheckTime())){
            viewHolder.lastCheckTime.setText("暂无");
        }else {
            viewHolder.lastCheckTime.setText(userListviewItem.getLastCheckTime());
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
                    viewHolder.user_new_id.setText(style);
                } else if (userId.contains(searchContent)) {
                    style = new SpannableStringBuilder(userId);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#0088ff")),
                            userId.indexOf(searchContent),
                            userId.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    viewHolder.user_id.setText(style);
                } else if (taskId.contains(searchContent)) {
                    style = new SpannableStringBuilder(taskId);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")),
                            taskId.indexOf(searchContent),
                            taskId.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    viewHolder.task_id.setText(style);
                }else if (name.contains(searchContent)) {
                    style = new SpannableStringBuilder(name);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#0088ff")),
                            name.indexOf(searchContent),
                            name.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    viewHolder.user_name.setText(style);
                }else if (address.contains(searchContent)) {
                    style = new SpannableStringBuilder(address);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#0088ff")),
                            address.indexOf(searchContent),
                            address.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    viewHolder.address.setText(style);
                }else if (meter_number.contains(searchContent)) {
                    style = new SpannableStringBuilder(meter_number);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#0088ff")),
                            meter_number.indexOf(searchContent),
                            meter_number.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    viewHolder.number.setText(style);
                }
            }
        }
        return convertView;
    }

    public class ViewHolder {
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
    }

    //当ListView调用setTextFilter()方法的时候，便会调用该方法
    @Override
    public Filter getFilter() {
        if (myFilter == null) {
            myFilter = new MyFilter();
        }
        return myFilter;
    }

    class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            searchContent = constraint.toString();
            FilterResults results = new FilterResults();
            List<UserListviewItem> list;  //筛选后的数据集合
            if (constraint.length() == 0) {  //当过滤的关键字为空的时候，则显示所有的数据
                list = backList;
            } else {    //否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();
                list.clear();
                for (UserListviewItem item : backList) {
                    if (item.getTaskId().contains(constraint) || item.getUserNewId().contains(constraint) || item.getUserId().contains(constraint) || item.getUserName().contains(constraint) || item.getNumber().contains(constraint) || item.getAdress().contains(constraint)) {
                        list.add(item);
                    }
                }
            }
            results.values = list;    //将得到的集合保存到FilterResults的value变量中
            results.count = list.size();   //将集合的大小保存到FilterResults的count变量中
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
                notifyDataSetInvalidated();  //通知数据失效
            }
        }
    }
}
