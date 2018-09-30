package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.DownloadListvieItem;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
public class DownloadListViewAdapter extends BaseAdapter {
    private Context context;
    private List<DownloadListvieItem> downloadListvieItemList;
    private LayoutInflater layoutInflater;

    public DownloadListViewAdapter(Context context, List<DownloadListvieItem> downloadListvieItemList) {
        this.context = context;
        this.downloadListvieItemList = downloadListvieItemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }


    @Override
    public int getCount() {
        return downloadListvieItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return downloadListvieItemList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.download_listview_item, null);
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.task_name);
            viewHolder.taskNumber = (TextView) convertView.findViewById(R.id.task_number);
            viewHolder.checkType = (TextView) convertView.findViewById(R.id.check_type);
            viewHolder.totalUserNumber = (TextView) convertView.findViewById(R.id.total_user_number);
            viewHolder.endTime = (TextView) convertView.findViewById(R.id.end_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DownloadListvieItem item = downloadListvieItemList.get(position);
        viewHolder.taskName.setText(item.getTaskName());
        viewHolder.taskNumber.setText(item.getTaskNumber()+"");
        viewHolder.checkType.setText(item.getCheckType());
        viewHolder.totalUserNumber.setText(item.getTotalUserNumber()+"");
        viewHolder.endTime.setText(item.getEndTime());
        return convertView;
    }

    class ViewHolder {
        TextView taskName;  //任务名称
        TextView taskNumber;  //任务编号
        TextView checkType;  //安检类型
        TextView totalUserNumber;   //总用户数
        TextView endTime;  //结束时间
    }
}
