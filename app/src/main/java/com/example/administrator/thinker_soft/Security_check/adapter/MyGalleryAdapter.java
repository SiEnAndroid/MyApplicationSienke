package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.thinker_soft.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14 0014.
 */
public class MyGalleryAdapter extends BaseAdapter {
    private Context context;
    private List<String> photoPathLists = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private File file;

    public MyGalleryAdapter(Context context,List<String> photoPathLists) {
        this.context = context;
        this.photoPathLists = photoPathLists;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return photoPathLists.size();
    }

    @Override
    public Object getItem(int position) {
        return photoPathLists.get(position);
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
            convertView = layoutInflater.inflate(R.layout.gallery_photo_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.photo_detail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(photoPathLists.get(position) != null){
            file = new File(photoPathLists.get(position));
            Glide.with(context).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.imageView);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
