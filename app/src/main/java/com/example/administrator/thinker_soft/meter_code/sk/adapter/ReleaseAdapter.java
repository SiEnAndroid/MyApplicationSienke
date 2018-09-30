package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn.FileOpenActivity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.CommonUtil;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/9.
 */

public class ReleaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<String> mList;
    private final LayoutInflater inflater;
    private static final int ITEM_TYPE_ONE = 0x00001;
    private static final int ITEM_TYPE_TWO = 0x00002;
    private OnItemListener onItemListener;

    /**
     * 这里之所以用多行视图，因为我们默认的有一张图片的（那个带+的图片，用户点击它才会才会让你去选择图片）
     * 集合url为空的时候，默认显示它，当它达到集合9时，这个图片会自动隐藏。
     */
    public ReleaseAdapter(Activity context, ArrayList<String> mList) {
        this.activity = context;
        this.mList = mList;
        inflater = LayoutInflater.from(context);
    }

    public ArrayList<String> getmList() {
        return mList;
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setPadding(20, 0, 20, 0);
        switch (viewType) {
            case ITEM_TYPE_ONE:
                return new ReleaseHolder(inflater.inflate(R.layout.view_releas_msg_two, parent, false));
            case ITEM_TYPE_TWO:
                return new ReleaseTwoHolder(inflater.inflate(R.layout. view_release_msg, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReleaseHolder) {
            bindItemReleaseHolder((ReleaseHolder) holder, position);
        } else if (holder instanceof ReleaseTwoHolder) {
            bindItemTWOMyHolder((ReleaseTwoHolder) holder, position);
        }
    }

    private void bindItemTWOMyHolder(final ReleaseTwoHolder holder, final int position) {
        Log.e("Adapter", listSize() + "");
        //集合长度大于等于9张时，隐藏 图片
        if (listSize() >= 9) {
            holder.imageview2.setVisibility(View.GONE);
        }
        holder.imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择图片 9 - listSize()
                if (mList.size()>9) {
                    ToastUtil.showShort(activity,"选择文件不能超过9个!");
                }else {
                    if (onItemListener!=null){
                        v.setTag(1);
                        onItemListener.OnFileSelect(v,position);
                    }  
                }
            }
        });
    }

    private void bindItemReleaseHolder(ReleaseHolder holder, final int position) {
//        Glide.with(activity)
//                .load(mList.get(position))
//                .centerCrop()
//                .error(R.mipmap.ic_file_select)
//                .into(holder.imageview);
        holder.fileName.setText(mList.get(position));
        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(6);
                onItemListener.OnFileSelect(v,position);
                        
            }
        });
        
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return ITEM_TYPE_TWO;
        } else {
            return ITEM_TYPE_ONE;
        }
    }

    @Override
    public int getItemCount() {
        Log.e("getItemCount", mList.size() + 1 + "");
        return mList.size() + 1;
    }

    class ReleaseHolder extends RecyclerView.ViewHolder {
        private final ImageView imageview;
        private final  ImageView delete;
        private final TextView fileName;
        public ReleaseHolder(View itemView) {
            super(itemView);
            imageview = (ImageView) itemView.findViewById(R.id.imageview);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            fileName=itemView.findViewById(R.id.tv_cont);
            
        }
    }

    class ReleaseTwoHolder extends RecyclerView.ViewHolder {
        private final ImageView imageview2;

        public ReleaseTwoHolder(View itemView) {
            super(itemView);
            imageview2 = (ImageView) itemView.findViewById(R.id.imageview2);

        }
    }

    //对外暴露方法  。点击添加图片（类似于上啦加载数据）
    public void addMoreItem(ArrayList<String> loarMoreDatas) {
        mList.addAll(loarMoreDatas);
        notifyDataSetChanged();
    }
    public void  addItem(String loar){
        mList.add(loar);
        notifyDataSetChanged();
    }

    //得到集合长度
    public int listSize() {
        int size = mList.size();
        return size;
    }

    /**
     * 监听
     */
    public interface  OnItemListener{
        void  OnFileSelect(View view,int position);
        
    }
}
