package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.PopupwindowListItem;

import java.util.List;

/**
 * class
 *
 * @author g
 * @date 2018/8/24:12:58]
 */
public class SingleAdapter extends RecyclerView.Adapter {
    private List<PopupwindowListItem> datas;
    private int selected = -1;
    public SingleAdapter(List<PopupwindowListItem> datas) {
        this.datas = datas;
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setSelection(int position){
        this.selected = position;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_box, parent, false);

        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof SingleViewHolder){
            final SingleViewHolder viewHolder = (SingleViewHolder) holder;
            String name = datas.get(position).getItemName();
            viewHolder.mTvName.setText(name);

            if(selected == position){
                viewHolder.mCheckBox.setChecked(true);
                viewHolder.itemView.setSelected(true);
            } else {
                viewHolder.mCheckBox.setChecked(false);
                viewHolder.itemView.setSelected(false);
            }

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
        return datas.size();
    }

    class SingleViewHolder extends RecyclerView.ViewHolder{
        TextView mTvName;
        CheckBox mCheckBox;

        public SingleViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.check_state);

        }
    }
    public interface OnItemClickLitener{
        void onItemClick(View view,int position);
    }

}
