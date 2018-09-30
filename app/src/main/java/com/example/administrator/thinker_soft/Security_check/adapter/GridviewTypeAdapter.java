package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.GridviewTypeItem;
import com.example.administrator.thinker_soft.Security_check.model.GridviewTypeViewholder;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */
public class GridviewTypeAdapter extends BaseAdapter {
    private Context context;
    private List<GridviewTypeItem> gridviewTypeItems;
    private LayoutInflater layoutInflater;
    private static HashMap<Integer, Boolean> isCheck = new HashMap<Integer, Boolean>();

    public GridviewTypeAdapter(Context context, List<GridviewTypeItem> gridviewTypeItems) {
        this.context = context;
        this.gridviewTypeItems = gridviewTypeItems;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
        // 默认为不选中
        initCheck(false);
    }

    // 初始化map集合
    public void initCheck(boolean flag) {
        for (int i = 0; i < gridviewTypeItems.size(); i++) {  // map集合的数量和list的数量是一致的
            getIsCheck().put(i, flag);  // 设置默认的显示
        }
    }

    @Override
    public int getCount() {
        return gridviewTypeItems.size();
    }

    @Override
    public Object getItem(int position) {
        return gridviewTypeItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridviewTypeViewholder viewHolder;
        if (convertView == null) {
            viewHolder = new GridviewTypeViewholder();
            convertView = layoutInflater.inflate(R.layout.gridview_type_item, null);
            viewHolder.typeName = (TextView) convertView.findViewById(R.id.type_name);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.is_checked);
            viewHolder.typeId = (TextView) convertView.findViewById(R.id.type_id);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (GridviewTypeViewholder) convertView.getTag();
        }
        GridviewTypeItem item = gridviewTypeItems.get(position);
        viewHolder.typeId.setText(item.getTypeId());
        viewHolder.typeName.setText(item.getTypeName());
        // 设置状态
        viewHolder.checkBox.setChecked(getIsCheck().get(position));
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsCheck() {
        return isCheck;
    }

    public static void setIsCheck(HashMap<Integer, Boolean> isCheck) {
        GridviewTypeAdapter.isCheck = isCheck;
    }
}
