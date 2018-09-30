package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.GridviewHomePageViewHolder;
import com.example.administrator.thinker_soft.Security_check.model.GridHomePageItem;

import java.util.List;

/**
 * Created by Administrator on 2017/6/1.
 */
public class GridviewHomePageAdapter extends BaseAdapter {
    private Context context;
    private List<GridHomePageItem> gridHomePageItems;
    private LayoutInflater layoutInflater;

    public GridviewHomePageAdapter(Context context, List<GridHomePageItem> gridHomePageItems) {
        this.context = context;
        this.gridHomePageItems = gridHomePageItems;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return gridHomePageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return gridHomePageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridviewHomePageViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new GridviewHomePageViewHolder();
            convertView = layoutInflater.inflate(R.layout.gridview_homepage_item, null);
            viewHolder.imageZsbg = (ImageView) convertView.findViewById(R.id.image_zsbg);
            viewHolder.imageName = (TextView) convertView.findViewById(R.id.image_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GridviewHomePageViewHolder) convertView.getTag();
        }
        GridHomePageItem item = gridHomePageItems.get(position);
        viewHolder.imageZsbg.setImageResource(item.getImageZsbg());
        viewHolder.imageName.setText(item.getImageName());
        return convertView;
    }
}
