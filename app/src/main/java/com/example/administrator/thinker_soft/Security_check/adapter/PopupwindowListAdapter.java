package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.NormalViewHolder;
import com.example.administrator.thinker_soft.Security_check.model.PopupwindowListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */
public class PopupwindowListAdapter extends BaseAdapter {
    private Context context;
    private List<PopupwindowListItem> popupwindowListItemList = new ArrayList<>();
    private int flag;
    private LayoutInflater layoutInflater;
    private static HashMap<Integer, Boolean> isCheck = new HashMap<Integer, Boolean>();

    public PopupwindowListAdapter(Context context,List<PopupwindowListItem> lists,int flag) {
        this.context = context;
        this.popupwindowListItemList = lists;
        this.flag = flag;
        if(context != null){
            layoutInflater = LayoutInflater.from(context);
        }
        // 默认为不选中
        initCheck(false);
    }

    // 初始化map集合
    public void initCheck(boolean flag) {
        for (int i = 0; i < popupwindowListItemList.size(); i++) {  // map集合的数量和list的数量是一致的
            getIsCheck().put(i, flag);  // 设置默认的显示
        }
    }

    public static HashMap<Integer, Boolean> getIsCheck() {
        return isCheck;
    }

    public static void setIsCheck(HashMap<Integer, Boolean> isCheck) {
        PopupwindowListAdapter.isCheck = isCheck;
    }

    @Override
    public int getCount() {
        return popupwindowListItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return popupwindowListItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NormalViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new NormalViewHolder();
            convertView = layoutInflater.inflate(R.layout.popupwindow_list_item, null);
            viewHolder.itemId = (TextView) convertView.findViewById(R.id.item_id);
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.checkedState = (CheckBox) convertView.findViewById(R.id.check_state);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NormalViewHolder) convertView.getTag();
        }
        PopupwindowListItem item = popupwindowListItemList.get(position);
        if(flag == 0){
            viewHolder.itemName.setText(item.getItemName());
            viewHolder.itemId.setText(item.getItemId());
            viewHolder.checkedState.setVisibility(View.GONE);
        }else {
            viewHolder.itemName.setText(item.getItemName());
            viewHolder.itemId.setText(item.getItemId());
            viewHolder.checkedState.setVisibility(View.VISIBLE);
            viewHolder.checkedState.setChecked(getIsCheck().get(position));
        }
        return convertView;
    }
}
