package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.PopupwindowListItem;

import java.util.HashMap;
import java.util.List;

/**
 * class
 *
 * @author g
 * @date 2018/8/24:13:21]
 */
public class MultiAdapter extends RecyclerView.Adapter {
    private List<PopupwindowListItem> mFilterList;
    private OnItemClickLitener mOnItemClickLitener;
    public static HashMap<Integer, Boolean> isSelected;

    public MultiAdapter(List<PopupwindowListItem> datas) {
        this.mFilterList = datas;

        init();
    }

    public void clear(){
        this.mFilterList.clear();
        init();

    }

    // 初始化 设置所有item都为未选择
    public void init() {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < mFilterList.size(); i++) {
            isSelected.put(i, false);
        }
    }
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_box, parent, false);

        return new MultiAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MultiAdapter.MultiViewHolder){
            final MultiAdapter.MultiViewHolder viewHolder = (MultiAdapter.MultiViewHolder) holder;
            PopupwindowListItem bean = mFilterList.get(position);
            viewHolder.mTvName.setText(bean.getItemName());
            viewHolder.mCheckBox.setChecked(isSelected.get(position));
            viewHolder.itemView.setSelected(isSelected.get(position));
            if (mOnItemClickLitener != null)
            {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mOnItemClickLitener.onItemClick(viewHolder.itemView, viewHolder.getAdapterPosition());
                    }
                });
            }


        }
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }



    class MultiViewHolder extends RecyclerView.ViewHolder{
        TextView mTvName;
        CheckBox mCheckBox;
        public MultiViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.check_state);



        }
    }


}
