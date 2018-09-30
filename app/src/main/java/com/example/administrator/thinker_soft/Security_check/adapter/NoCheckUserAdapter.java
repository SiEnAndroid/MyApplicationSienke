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
public class NoCheckUserAdapter extends BaseAdapter implements Filterable{
    private Context context;
    private List<UserListviewItem> itemList;
    private LayoutInflater layoutInflater;
    private MyFilter myFilter;
    public static String searchContent;
    private List<UserListviewItem> backList;  //备用数据源

    public NoCheckUserAdapter(Context context, List<UserListviewItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        backList = itemList;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.userlist_listview_item, null);
            viewHolder.security_number = (TextView) convertView.findViewById(R.id.security_number);
            viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.number = (TextView) convertView.findViewById(R.id.number);
            viewHolder.phone_number = (TextView) convertView.findViewById(R.id.phone_number);
            viewHolder.security_type = (TextView) convertView.findViewById(R.id.security_type);
            viewHolder.user_id = (TextView) convertView.findViewById(R.id.user_id);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            viewHolder.if_edit = (ImageView) convertView.findViewById(R.id.if_edit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UserListviewItem item = itemList.get(position);
        viewHolder.security_number.setText(item.getSecurityNumber());
        Log.i("security_number=====>", "security_number=" + item.getSecurityNumber());
        viewHolder.user_name.setText(item.getUserName());
        viewHolder.number.setText(item.getNumber());
        if (!item.getPhoneNumber().equals("null")) {
            viewHolder.phone_number.setText(item.getPhoneNumber());
        } else {
            viewHolder.phone_number.setText("无");
        }
        viewHolder.security_type.setText(item.getSecurityType());
        if (!item.getUserId().equals("null")) {
            viewHolder.user_id.setText(item.getUserId());
        } else {
            viewHolder.user_id.setText("无");
        }
        viewHolder.address.setText(item.getAdress());
        viewHolder.if_edit.setImageResource(item.getIfEdit());

        if (itemList != null) {
            if (searchContent != null) {
                String phoneNumber = item.getPhoneNumber();
                String name = item.getUserName();
                String address = item.getAdress();
                String meter_number = item.getNumber();
                SpannableStringBuilder style = null;
                if (phoneNumber.contains(searchContent)) {
                    style = new SpannableStringBuilder(phoneNumber);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#4EA0DC")),
                            phoneNumber.indexOf(searchContent),
                            phoneNumber.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    viewHolder.phone_number.setText(style);
                } else if (name.contains(searchContent)) {
                    style = new SpannableStringBuilder(name);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#4EA0DC")),
                            name.indexOf(searchContent),
                            name.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    viewHolder.user_name.setText(style);
                }else if (address.contains(searchContent)) {
                    style = new SpannableStringBuilder(address);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#4EA0DC")),
                            address.indexOf(searchContent),
                            address.indexOf(searchContent) + searchContent.length(),
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    viewHolder.address.setText(style);
                }else if (meter_number.contains(searchContent)) {
                    style = new SpannableStringBuilder(meter_number);
                    style.setSpan(new ForegroundColorSpan(Color.parseColor("#4EA0DC")),
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
        TextView user_name;  //姓名
        TextView number;  //表编号
        TextView phone_number;  //电话号码
        TextView security_type;   //安检类型
        TextView user_id;  //用户编号
        TextView address;   //地址
        ImageView if_edit;   //是否编辑
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
                    if (item.getPhoneNumber().contains(constraint) || item.getUserName().contains(constraint) || item.getNumber().contains(constraint) || item.getAdress().contains(constraint)) {
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
            itemList = (List<UserListviewItem>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();  //通知数据发生了改变
                Log.i("publishResults", "publishResults进来了！");
            } else {
                notifyDataSetInvalidated();  //通知数据失效
            }
        }
    }
}
