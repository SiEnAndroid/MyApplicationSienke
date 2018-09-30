package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

import java.io.File;

/**
 * @author g
 * @FileName FileSelectAdapter
 * @date 2018/9/26 9:05
 */
public class FileSelectAdapter extends BaseAdapter {
        private File[] files;
        private boolean istop;
        private Context context;

        public FileSelectAdapter(Context context, File[] files, boolean istop) {
            this.context = context;
            this.files = files;
            this.istop = istop;
        }

        @Override
        public int getCount() {
            return files.length;
        }

        @Override
        public Object getItem(int position) {
            return files[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.item_select_file,
                        null);
                holder.iv = (ImageView) convertView
                        .findViewById(R.id.adapter_icon);
                holder.tv = (TextView) convertView
                        .findViewById(R.id.adapter_txt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 设置convertView中控件的值
            setconvertViewRow(position, holder);
            return convertView;
        }

        private void setconvertViewRow(int position, ViewHolder holder) {
            File f = files[position];
            holder.tv.setText(f.getName());
            // 不是在顶层目录
            if (!istop && position == 0) {
                // 加载后退图标
                holder.iv.setImageResource(R.mipmap.back_true);
            } else if (f.isFile()) {
                // 是文件
                // 加载文件图标
                holder.iv.setImageResource(R.mipmap.ic_file_select);
            } else {
                // 文件夹
                holder.iv.setImageResource(R.mipmap.files);
            }
        }

        class ViewHolder {
            private ImageView iv;
            private TextView tv;
        }
    
}
