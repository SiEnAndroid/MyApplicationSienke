package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.UploadListViewItem;
import com.example.administrator.thinker_soft.Security_check.model.UploadViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
public class UploadListViewAdapter extends BaseAdapter {
    private Context context;
    private List<UploadListViewItem> uploadListViewItemList;
    private LayoutInflater layoutInflater;
    private static HashMap<Integer, Boolean> isCheck = new HashMap<Integer, Boolean>();

    public UploadListViewAdapter(Context context, List<UploadListViewItem> uploadListViewItemList) {
        this.context = context;
        this.uploadListViewItemList = uploadListViewItemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
        // 默认为不选中
        initCheck(false);
    }
    // 初始化map集合
    public void initCheck(boolean flag) {
        for (int i = 0; i < uploadListViewItemList.size(); i++) {  // map集合的数量和list的数量是一致的
            getIsCheck().put(i, flag);  // 设置默认的显示
        }
    }

    @Override
    public int getCount() {
        return uploadListViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return uploadListViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UploadViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new UploadViewHolder();
            convertView = layoutInflater.inflate(R.layout.upload_listview_item, null);
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.task_name);
            viewHolder.taskNumber = (TextView) convertView.findViewById(R.id.task_number);
            viewHolder.checkType = (TextView) convertView.findViewById(R.id.check_type);
            viewHolder.totalUserNumber = (TextView) convertView.findViewById(R.id.total_user_number);
            viewHolder.restCount = (TextView) convertView.findViewById(R.id.rest_count);
            viewHolder.endTime = (TextView) convertView.findViewById(R.id.end_time);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.is_checked);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UploadViewHolder) convertView.getTag();
        }
        UploadListViewItem uploadListViewItem = uploadListViewItemList.get(position);
        viewHolder.taskName.setText(uploadListViewItem.getTaskName());
        viewHolder.taskNumber.setText(uploadListViewItem.getTaskNumber());
        viewHolder.checkType.setText(uploadListViewItem.getCheckType());
        viewHolder.totalUserNumber.setText(uploadListViewItem.getTotalUserNumber());
        viewHolder.restCount.setText(uploadListViewItem.getRestCount());
        viewHolder.endTime.setText(uploadListViewItem.getEndTime());
        viewHolder.checkBox.setChecked(getIsCheck().get(position));
        return convertView;
    }
    public static HashMap<Integer, Boolean> getIsCheck() {
        return isCheck;
    }

    public static void setIsCheck(HashMap<Integer, Boolean> isCheck) {
        UploadListViewAdapter.isCheck = isCheck;
    }
}
