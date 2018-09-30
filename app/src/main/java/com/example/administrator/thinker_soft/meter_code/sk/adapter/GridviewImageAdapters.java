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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.PhotoPathBean;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/7.
 */

public class GridviewImageAdapters extends BaseAdapter {
    private Context context;
    private ArrayList<PhotoPathBean> gridviewImageList;
    private LayoutInflater layoutInflater;
    private File file;
    private Bitmap addBitmap;

    private ClickTypeListener listener;
    /**
     * 判断是否显示清除按钮 true=显示
     */
    private boolean showDelete = false;

    public GridviewImageAdapters(Context context, ArrayList<PhotoPathBean> gridviewImageList) {
        this.context = context;
        this.gridviewImageList = gridviewImageList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
            addBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.camera_true);
        }
    }


    /**
     * 设置数据
     *
     * @param gridviewImageList
     */
    public void setGridviewImageList(ArrayList<PhotoPathBean> gridviewImageList) {
        this.gridviewImageList = gridviewImageList;
        notifyDataSetChanged();
    }



    /**
     * 设置监听
     *
     * @param listener
     */
    public void setListener(ClickTypeListener listener) {
        this.listener = listener;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.gridview_image_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete);
         //   viewHolder.type = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//


        //图片最大张数
        if (gridviewImageList.size() == position) {
            if (gridviewImageList.size() != 9) {
                viewHolder.delete.setVisibility(View.GONE);

                Glide.with(context).load(R.mipmap.camera).into(viewHolder.imageView);

            } else {
                viewHolder.delete.setVisibility(View.GONE);

                Glide.with(context).load(R.mipmap.forbidden_camera).into(viewHolder.imageView);

            }

        } else {
            PhotoPathBean photoPathBean = gridviewImageList.get(position);
            String cropPath = photoPathBean.getCropPath();
            Log.i("GridviewImageAdapters", "此时的照片路径为：" + cropPath);
         //   viewHolder.type.setText(gridviewImageList.get(position).getType());
            // 正常显示
            // 判断是否需要显示删除按钮   /storage/emulated/0/ThinkerSoft_super/Icon_image/20180809_175907_003263910100462.jpg
//            if (getDeleteShow()) {
//                viewHolder.delete.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.delete.setVisibility(View.GONE);
//            }

            if (gridviewImageList.get(position) != null) {
                file = new File(gridviewImageList.get(position).getCropPath());
                if (file != null) {
                    Log.i("GridviewImageAdapters", "file != null：");
              // Glide.with(context).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.imageView);
                    Glide.with(context).load(file).into(viewHolder.imageView);
                }else {
                    Log.i("GridviewImageAdapters", "file == null：");
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
//        //设置类型
//        viewHolder.type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    listener.onClickTyper(position);
//                }
//            }
//        });

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        ImageView delete;
        TextView type;//类型
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

    /**
     * 类型接口
     */
    public interface ClickTypeListener {
        void onClickTyper(int position);

    }
}
