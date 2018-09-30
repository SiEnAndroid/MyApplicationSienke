package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.NewTaskListviewItem;
import com.example.administrator.thinker_soft.Security_check.model.NewTaskViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */
public class NewTaskListviewAdapter extends BaseAdapter {
    private Context context;
    private List<NewTaskListviewItem> newTaskListviewItemList;
    private LayoutInflater layoutInflater;
    private static HashMap<Integer, Boolean> isCheck;

    public NewTaskListviewAdapter(Context context, List<NewTaskListviewItem> newTaskListviewItemList) {
        this.context = context;
        this.newTaskListviewItemList = newTaskListviewItemList;
        isCheck = new HashMap<Integer, Boolean>();
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
        // 默认为不选中
        initCheck(false);
    }

    // 初始化map集合
    public void initCheck(boolean flag) {
        for (int i = 0; i < newTaskListviewItemList.size(); i++) {  // map集合的数量和list的数量是一致的
            getIsCheck().put(i, flag);  // 设置默认的显示
        }
    }

    @Override
    public int getCount() {
        return newTaskListviewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return newTaskListviewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        NewTaskViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new NewTaskViewHolder();
            convertView = layoutInflater.inflate(R.layout.newtask_listview_item, null);
            viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.userProperty = (TextView) convertView.findViewById(R.id.user_property);
            viewHolder.securityState = (TextView) convertView.findViewById(R.id.security_state);
            viewHolder.securityStateRoot = (LinearLayout) convertView.findViewById(R.id.security_state_root);
            viewHolder.remarks = (TextView) convertView.findViewById(R.id.remarks);
            viewHolder.remarksRoot = (LinearLayout) convertView.findViewById(R.id.remarks_root);
            viewHolder.number = (TextView) convertView.findViewById(R.id.number);
            viewHolder.phone_number = (TextView) convertView.findViewById(R.id.phone_number);
            viewHolder.user_id = (TextView) convertView.findViewById(R.id.user_id);
            viewHolder.old_user_id = (TextView) convertView.findViewById(R.id.old_user_id);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NewTaskViewHolder) convertView.getTag();
        }

        NewTaskListviewItem item = newTaskListviewItemList.get(position);
        if (item.getFlag() == 0) {
            viewHolder.securityStateRoot.setVisibility(View.VISIBLE);
            viewHolder.remarksRoot.setVisibility(View.VISIBLE);
            viewHolder.securityState.setText(item.getSecurityState());
            viewHolder.remarks.setText(item.getRemarks());
        } else if (item.getFlag() == 1) {
            viewHolder.securityStateRoot.setVisibility(View.GONE);
            viewHolder.remarksRoot.setVisibility(View.GONE);
        } else {
            viewHolder.securityStateRoot.setVisibility(View.VISIBLE);
            viewHolder.securityState.setText(item.getSecurityState());
            viewHolder.remarksRoot.setVisibility(View.GONE);
        }
        if (!item.getUserName().equals("null")) {
            viewHolder.user_name.setText(item.getUserName());
        } else {
            viewHolder.user_name.setText("无");
        }
        viewHolder.userProperty.setText(item.getUserproperty());
        if (!item.getNumber().equals("null")) {
            viewHolder.number.setText(item.getNumber());
        } else {
            viewHolder.number.setText("无");
        }
        if (!item.getPhoneNumber().equals("null")) {
            viewHolder.phone_number.setText(item.getPhoneNumber());
        } else {
            viewHolder.phone_number.setText("无");
        }
        if (!item.getUserId().equals("null")) {
            viewHolder.user_id.setText(item.getUserId());
        } else {
            viewHolder.user_id.setText("无");
        }
        if (!item.getOldUserId().equals("null")) {
            viewHolder.old_user_id.setText(item.getOldUserId());
        } else {
            viewHolder.old_user_id.setText("无");
        }
        if (!item.getAdress().equals("null")) {
            viewHolder.address.setText(item.getAdress());
        } else {
            viewHolder.address.setText("无");
        }
        // 设置状态
        viewHolder.checkBox.setChecked(getIsCheck().get(position));
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsCheck() {
        return isCheck;
    }

    public static void setIsCheck(HashMap<Integer, Boolean> isCheck) {
        NewTaskListviewAdapter.isCheck = isCheck;
        Log.i("setIsCheck", "setIsCheck进来了！" + isCheck);
    }
}
