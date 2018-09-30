package com.example.administrator.thinker_soft.meter_code.sk.adapter;

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
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;

import java.util.ArrayList;

/**
 * @author 111
 */
public class AuditUnqualifiedImageAdapter extends BaseAdapter {

    private  ArrayList<String> cropIdLists;
    private  Context context;
    private  LayoutInflater layoutInflater;
    private  Bitmap addBitmap;

    public AuditUnqualifiedImageAdapter(Context context, ArrayList<String> cropIdLists) {
            this.context=context;
            this.cropIdLists=cropIdLists;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
            addBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.camera_true);
        }
    }

    /**
     * 判断是否显示清除按钮 true=显示
     */
    private boolean showDelete = false;
    @Override
    public int getCount() {
        // 数据集合加一，在该位置上添加加号
        return  cropIdLists == null ? 0 : cropIdLists.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return cropIdLists== null ? null : cropIdLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        AuditUnqualifiedImageAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new AuditUnqualifiedImageAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.gridview_image_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AuditUnqualifiedImageAdapter.ViewHolder) convertView.getTag();
        }

        if (cropIdLists.size() == position) {
            if(cropIdLists.size() != 9){
                viewHolder.delete.setVisibility(View.GONE);
                Glide.with(context).load(R.mipmap.camera).into(viewHolder.imageView);
            }else {
                viewHolder.delete.setVisibility(View.GONE);
                Glide.with(context).load(R.mipmap.forbidden_camera).into(viewHolder.imageView);
            }
        }else {
            if ( cropIdLists !=null){

                String httpUrl = new StringBuffer().append(SkUrl.SkHttp(context)).append("getSecurityImage.do").toString();
                // 正常显示
                // 判断是否需要显示删除按钮
                if(getDeleteShow()){
                    viewHolder.delete.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.delete.setVisibility(View.GONE);
                }
                if(cropIdLists.get(position) != null){
                    Glide.with(context)
                            .load(httpUrl+"?n_image_id="+cropIdLists.get(position))
                            .placeholder(R.drawable.load_dail)// 占位图片
                            .into(viewHolder.imageView);
                    Log.e("pgl","=== 图片URL"+"执行了:"+httpUrl+"?n_image_id="+cropIdLists.get(position));
                }


            }
        }



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
