package com.example.administrator.thinker_soft.mode;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.thinker_soft.R;

public class ViewHolder {

    private final SparseArray<View> mViews;
    private View mConvertView;

    public ViewHolder(Context context, ViewGroup parent, int layoutId) {
        this.mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId);
        } else {
            return (ViewHolder) convertView.getTag();
        }
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public ViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public ViewHolder setText(int viewId, String text, int flags) {
        TextView textView = getView(viewId);
        textView.getPaint().setFlags(flags);
        textView.setText(text);
        return this;
    }

    public ViewHolder setImage(int viewId, String uri) {
        ImageView imageView = getView(viewId);
        Glide.with(imageView.getContext()).load(uri).placeholder(R.mipmap.thinklogo)
                .into(imageView);
        return this;
    }


    public <T extends View> T getConvertView() {
        return (T) this.mConvertView;
    }



}
