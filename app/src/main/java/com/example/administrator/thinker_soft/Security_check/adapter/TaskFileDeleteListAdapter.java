package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.TaskChoose;

import java.util.List;

/**
 * Created by Administrator on 2017/8/15 0015.
 */
public class TaskFileDeleteListAdapter extends BaseAdapter {
    private Context context;
    private List<TaskChoose> itemList;
    private LayoutInflater layoutInflater;

    public TaskFileDeleteListAdapter(Context context, List<TaskChoose> itemList) {
        this.context = context;
        this.itemList = itemList;
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.task_file_delete_list_item, null);
            viewHolder.task_name = (TextView) convertView.findViewById(R.id.task_name);
            viewHolder.task_number = (TextView) convertView.findViewById(R.id.task_number);
            viewHolder.total_user_number = (TextView) convertView.findViewById(R.id.total_user_number);
            viewHolder.restCount = (TextView) convertView.findViewById(R.id.rest_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TaskChoose item = itemList.get(position);
        viewHolder.task_name.setText(item.getTaskName());
        viewHolder.task_number.setText(item.getTaskNumber());
        viewHolder.total_user_number.setText(item.getTotalUserNumber());
        viewHolder.restCount.setText(item.getRestCount());
        return convertView;
    }

    class ViewHolder {
        TextView task_name;
        TextView task_number;
        TextView total_user_number;
        TextView restCount;
    }
}
