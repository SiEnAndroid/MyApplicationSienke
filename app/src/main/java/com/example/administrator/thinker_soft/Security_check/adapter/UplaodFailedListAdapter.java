package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.UploadFailedItem;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
public class UplaodFailedListAdapter extends BaseAdapter {
    private Context context;
    private List<UploadFailedItem> userListviewList;
    private LayoutInflater layoutInflater;

    public UplaodFailedListAdapter(Context context, List<UploadFailedItem> userListviewList) {
        this.context = context;
        this.userListviewList = userListviewList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return userListviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return userListviewList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.upload_failed_user_list_item, null);
            viewHolder.task_id = (TextView) convertView.findViewById(R.id.task_id);
            viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.user_id = (TextView) convertView.findViewById(R.id.user_id);
            viewHolder.user_new_id = (TextView) convertView.findViewById(R.id.user_new_id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UploadFailedItem item = userListviewList.get(position);
        viewHolder.user_name.setText(item.getUserName());
        viewHolder.task_id.setText(item.getTaskId());
        viewHolder.user_id.setText(item.getUserId());
        viewHolder.user_new_id.setText(item.getUserNewId());
        return convertView;
    }

    public class ViewHolder {
        TextView user_name;        //姓名
        TextView task_id;          //任务编号
        TextView user_id;         //用户编号
        TextView user_new_id;     //用户新编号
    }
}
