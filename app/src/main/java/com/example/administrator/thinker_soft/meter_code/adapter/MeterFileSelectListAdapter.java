package com.example.administrator.thinker_soft.meter_code.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.MeterSingleSelectItem;

import java.util.List;

/**
 * Created by Administrator on 2017/7/11 0011.
 */
public class MeterFileSelectListAdapter extends BaseAdapter {
    private Context context;
    private List<MeterSingleSelectItem> itemList;
    private LayoutInflater layoutInflater;
    private int flag;

    public MeterFileSelectListAdapter(Context context, List<MeterSingleSelectItem> itemList,int flag) {
        this.context = context;
        this.itemList = itemList;
        this.flag = flag;
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
            convertView = layoutInflater.inflate(R.layout.popupwindow_meter_file_select_list_item, null);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MeterSingleSelectItem item = itemList.get(position);
        viewHolder.name.setText(item.getName());
        if(flag == 0){
            Log.i("MeterBookAdapter","抄表本适配器进来了");
            viewHolder.icon.setBackgroundResource(R.mipmap.meter_book);
            viewHolder.id.setText(item.getID());
        }else {
            Log.i("MeterFileAdapter","文件适配器进来了");
            viewHolder.icon.setBackgroundResource(R.mipmap.files);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        TextView name;
        TextView id;
    }
}
