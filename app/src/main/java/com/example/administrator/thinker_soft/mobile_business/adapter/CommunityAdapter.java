package com.example.administrator.thinker_soft.mobile_business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.CommunityListViewItem;

import java.util.List;

/**
 * Created by Administrator on 2017/6/12.
 */
public class CommunityAdapter extends BaseAdapter {
    private Context context;
    private List<CommunityListViewItem> communityListViewItems;
    private LayoutInflater layoutInflater;


    public CommunityAdapter(Context context, List<CommunityListViewItem> communityListViewItemList) {
        this.context = context;
        this.communityListViewItems = communityListViewItemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (communityListViewItems == null) {
            return 0;
        } else {
            return communityListViewItems.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (communityListViewItems == null) {
            return null;
        } else {
            return communityListViewItems.get(position);
        }
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
            convertView = layoutInflater.inflate(R.layout.activity_business_community_listview_item, null);
            viewHolder.pic= (ImageView) convertView.findViewById(R.id.pic);
            viewHolder.title= (TextView) convertView.findViewById(R.id.title);
            viewHolder.neirong= (TextView) convertView.findViewById(R.id.neirong);
            viewHolder.time= (TextView) convertView.findViewById(R.id.time);
            viewHolder.pingjia= (TextView) convertView.findViewById(R.id.pingjia);
            viewHolder.huifu= (TextView) convertView.findViewById(R.id.huifu);
            viewHolder.yuedu= (TextView) convertView.findViewById(R.id.yuedu);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class ViewHolder{
        private ImageView pic;
        private TextView title;
        private TextView neirong;
        private TextView time;
        private TextView pingjia;
        private TextView huifu;
        private TextView yuedu;
    }
}
