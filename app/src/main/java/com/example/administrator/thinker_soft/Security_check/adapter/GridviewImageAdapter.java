package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.PhotoPathBean;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/21 0021.
 */
public class GridviewImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> gridviewImageList;
    private LayoutInflater layoutInflater;
    private File file;
    private Bitmap addBitmap;
    /**
     * 判断是否显示清除按钮 true=显示
     */
    private boolean showDelete = false;

    public GridviewImageAdapter(Context context, ArrayList<String> gridviewImageList) {
        this.context = context;
        this.gridviewImageList = gridviewImageList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
            addBitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.camera_true);
        }
    }

    public void setGridviewImageList(ArrayList<String> gridviewImageList) {
        this.gridviewImageList = gridviewImageList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        // 数据集合加一，在该位置上添加加号
        return gridviewImageList == null ? 0 : gridviewImageList.size() + 1;
        //return gridviewImageList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return gridviewImageList == null ? null : gridviewImageList.get(position);
        //return gridviewImageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.gridview_image_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (gridviewImageList.size() == position) {
            if(gridviewImageList.size() != 9){
                viewHolder.delete.setVisibility(View.GONE);
                Glide.with(context).load(R.mipmap.camera).into(viewHolder.imageView);
            }else {
                viewHolder.delete.setVisibility(View.GONE);
                Glide.with(context).load(R.mipmap.forbidden_camera).into(viewHolder.imageView);
            }
        }else {
            Log.i("GridviewImageAdapter","此时的照片路径为："+gridviewImageList.get(position));
            // 正常显示
            // 判断是否需要显示删除按钮
            if(getDeleteShow()){
                viewHolder.delete.setVisibility(View.VISIBLE);
            }else {
                viewHolder.delete.setVisibility(View.GONE);
            }
            if(gridviewImageList.get(position) != null){
                file = new File(gridviewImageList.get(position));
                if(file != null){
                    Glide.with(context).load(file).into(viewHolder.imageView);
                }
            }
        }

        //设置删除按钮的监听事件
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridviewImageList.remove(position);
                file.delete();
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        ImageView delete;
    }

    /**
     * 设置图片显示状态
     *
     * @param clear 图片状态
     */
    public void setDeleteShow(boolean clear) {
        showDelete = clear;
    }

    /**
     * 图片显示状态
     *
     * @return 状态 true=显示
     */
    public boolean getDeleteShow() {
        return showDelete;
    }
}
