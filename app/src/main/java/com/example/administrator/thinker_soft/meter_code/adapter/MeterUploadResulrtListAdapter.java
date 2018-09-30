package com.example.administrator.thinker_soft.meter_code.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.UploadResultListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7 0007.
 */
public class MeterUploadResulrtListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<UploadResultListItem> uploadResultListItems = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public MeterUploadResulrtListAdapter(Context context, List<UploadResultListItem> listItemList) {
        this.mcontext = context;
        this.uploadResultListItems = listItemList;
        if(context != null){
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return uploadResultListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return uploadResultListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.meter_upload_result_list_item,null);
            viewHolder.userID = (TextView) convertView.findViewById(R.id.user_id);
            viewHolder.result = (TextView) convertView.findViewById(R.id.result);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UploadResultListItem item = uploadResultListItems.get(position);
        viewHolder.userID.setText(item.getUserId());
        viewHolder.result.setText(item.getResult());
        return convertView;
    }

    class ViewHolder {
        TextView userID;
        TextView result;
    }
}
