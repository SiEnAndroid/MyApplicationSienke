package com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.ReleaseAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.PickerManager;
import com.example.administrator.thinker_soft.meter_code.sk.ui.filepicker.model.FileEntity;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.ToastUtil;


import java.io.File;
import java.util.ArrayList;

/**
 * File:FilePickerShowAdapter.class
 *
 * @author Administrator
 * @date 2018/8/29 10:24
 */
public class FilePickerShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<FileEntity> mDataList;
    private OnDeleteListener mOnDeleteListener;
    private OnFileItemClickListener mOnItemClickListener;
    private static final int ITEM_TYPE_ONE = 0x00001;
    private static final int ITEM_TYPE_TWO = 0x00002;
    private OnItemListener onItemListener;

    public void setOnItemClickListener(OnFileItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        mOnDeleteListener = onDeleteListener;
    }

    public FilePickerShowAdapter(Context context, ArrayList<FileEntity> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    public void setmDataList(ArrayList<FileEntity>dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }
    public void addData(FileEntity fileEntity) {
        mDataList.add(fileEntity);
        notifyDataSetChanged();
    }
    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public ArrayList<FileEntity> getmDataList() {
        return mDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setPadding(20, 0, 20, 0);
        switch (viewType) {
            case ITEM_TYPE_ONE:
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_file_picker_show, parent, false);
                return new FileShowViewHolder(view);
            case ITEM_TYPE_TWO:
                View views = LayoutInflater.from(mContext).inflate(R.layout.view_release_msg, parent, false);
                return new ReleaseTwoHolder(views);
            default:
                return null;
        }

//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_file_picker_show, parent, false);
//        return new FileShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FilePickerShowAdapter.FileShowViewHolder) {
            bindItemReleaseHolder((FilePickerShowAdapter.FileShowViewHolder) holder, position);
        } else if (holder instanceof FilePickerShowAdapter.ReleaseTwoHolder) {
            bindItemTWOMyHolder((FilePickerShowAdapter.ReleaseTwoHolder) holder, position);
        }


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
        return mDataList.size() + 1;

    }

    private void bindItemReleaseHolder(final FileShowViewHolder holder, final int position) {

        final FileEntity fileEntity = mDataList.get(position);
        holder.mTvName.setText(getFileName(fileEntity.getPath()));
        holder.mTvDetail.setText(fileEntity.getSize());
        
        
        if (fileEntity.getFileType() != null) {
            String title = fileEntity.getFileType().getTitle();
            if (title.equals("IMG")) {
                Glide.with(mContext).load(new File(fileEntity.getPath())).into(holder.mIvType);
            } else {
                holder.mIvType.setImageResource(fileEntity.getFileType().getIconStyle());
            }
        } else {
            //file_picker_def
            holder.mIvType.setImageResource(R.mipmap.file_manager_write);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.click(holder.getAdapterPosition());
                }
            }
        });
        holder.mIvDelete.setVisibility(View.VISIBLE);
        holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("---",position+"===");
                mDataList.remove(holder.getAdapterPosition());
                PickerManager.getInstance().files.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                
//                if (mOnDeleteListener != null) {
//                    mOnDeleteListener.delete(holder.getAdapterPosition());
//                }
            }
        });

    }

    public String getFileName(String pathandname) {
        // 取得文件名
        String fileName = new File(pathandname).getName();
        Log.d("ddd", "### fileName : " + fileName );
            try {
                int start = pathandname.lastIndexOf("/");
                int end = pathandname.lastIndexOf(".");
                if (start != -1 && end != -1) {
                    return pathandname.substring(start + 1, end);
                } else {
                    return null;
                }
            }catch (Exception e){
                Log.e("pgl","取文件名异常:"+e.getMessage());
                Toast.makeText(mContext, "该文件不能上传,不然会崩溃", Toast.LENGTH_SHORT).show();
                return null;
            }



    }


    private void bindItemTWOMyHolder(final ReleaseTwoHolder holder, final int position) {

        //集合长度大于等于9张时，隐藏 图片
        if (mDataList.size() > 9) {
            holder.imageview2.setVisibility(View.GONE);
        }
        holder.imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemListener != null) {
                    onItemListener.OnFileSelect(v, position);
                }

            }
        });
    }


    class FileShowViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvType, mIvDelete;
        private TextView mTvName, mTvDetail;

        public FileShowViewHolder(View itemView) {
            super(itemView);
            mIvType = (ImageView) itemView.findViewById(R.id.iv_type);
            mIvDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvDetail = (TextView) itemView.findViewById(R.id.tv_detail);
        }
    }

    class ReleaseTwoHolder extends RecyclerView.ViewHolder {
        private final ImageView imageview2;

        public ReleaseTwoHolder(View itemView) {
            super(itemView);
            imageview2 = (ImageView) itemView.findViewById(R.id.imageview2);

        }
    }

    /**
     * 监听
     */
    public interface OnItemListener {
        void OnFileSelect(View view, int position);

    }
}
