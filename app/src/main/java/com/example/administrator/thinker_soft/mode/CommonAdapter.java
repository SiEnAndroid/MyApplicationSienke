package com.example.administrator.thinker_soft.mode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<T> mDatas = new ArrayList<>();
    private final int mItemLayoutId;
    private ViewHolder viewHolder;

    public CommonAdapter(Context context, int itemLayoutId) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mItemLayoutId = itemLayoutId;
    }


    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public T getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = getViewHolder(view, viewGroup);
        this.viewHolder=viewHolder;
        convertView(i, viewHolder, (T) getItem(i));
        return viewHolder.getConvertView();
    }

    public abstract void convertView(int i, ViewHolder viewHolder, T item);

    private ViewHolder getViewHolder(View view, ViewGroup viewGroup) {
        return ViewHolder.get(mContext, view, viewGroup, mItemLayoutId);
    }

    public void addDatas(List<T> data) {
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(T data) {
        mDatas.add(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        mDatas.clear();
    }

    public List<T> getData() {
        return this.mDatas;
    }

    public void remove(T imageFilePath) {
        mDatas.remove(imageFilePath);
        notifyDataSetChanged();
    }
    public void remove(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
    }

}
