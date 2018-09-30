package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.BusinessManageListviewItem;

import java.util.List;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public class BusinessManageAdapter extends BaseAdapter {
    private Context context;
    private List<BusinessManageListviewItem> businessManageListviewItemList;
    private LayoutInflater layoutInflater;

    public BusinessManageAdapter(Context context, List<BusinessManageListviewItem> businessManageListviewItemList) {
        this.context = context;
        this.businessManageListviewItemList = businessManageListviewItemList;
        if(context != null){
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return businessManageListviewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return businessManageListviewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.business_manage_listview_item,null);
        }
        return convertView;
    }
}
