package com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

/**
 * File:FilePickerViewHolder.class
 *
 * @author Administrator
 * @date 2018/8/29 10:24
 */
public class FilePickerViewHolder extends RecyclerView.ViewHolder {
    protected RelativeLayout layoutRoot;
    protected ImageView ivType,ivChoose;
    protected TextView tvName;
    protected TextView tvDetail;
    public FilePickerViewHolder(View itemView) {
        super(itemView);
        ivType = (ImageView) itemView.findViewById(R.id.iv_type);
        layoutRoot = (RelativeLayout) itemView.findViewById(R.id.layout_item_root);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvDetail = (TextView) itemView.findViewById(R.id.tv_detail);
        ivChoose = (ImageView) itemView.findViewById(R.id.iv_choose);
    }
}
