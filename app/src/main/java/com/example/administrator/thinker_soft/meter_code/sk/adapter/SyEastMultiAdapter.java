package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.PopupwindowListItem;
import com.example.administrator.thinker_soft.Security_check.model.TaskChoose;

import java.util.HashMap;
import java.util.List;

/**
 * class
 *
 * @author g
 * @date 2018/8/24:13:21]
 */
public class SyEastMultiAdapter extends RecyclerView.Adapter {
    private List<TaskChoose> mFilterList;
    private OnItemClickLitener mOnItemClickLitener;
    private int selected = -1;

    public SyEastMultiAdapter(List<TaskChoose> datas) {
        this.mFilterList = datas;

     
    }
    public void setSelection(int position){
        this.selected = position;
        notifyDataSetChanged();
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public List<TaskChoose> getmFilterList() {
        return mFilterList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_choose_dy_item, parent, false);

        return new SyEastMultiAdapter.MultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SyEastMultiAdapter.MultiViewHolder){
            final SyEastMultiAdapter.MultiViewHolder viewHolder = (SyEastMultiAdapter.MultiViewHolder) holder;
            TaskChoose taskChoose = mFilterList.get(position);
            viewHolder.taskName.setText(taskChoose.getTaskName());
            viewHolder.taskNumber.setText(taskChoose.getTaskNumber());
            viewHolder.checkType.setText(taskChoose.getCheckType());
            viewHolder.totalUserNumber.setText(taskChoose.getTotalUserNumber());
            viewHolder.restCount.setText(taskChoose.getRestCount());
            viewHolder.endTime.setText(taskChoose.getEndTime());
            
            if(selected == position){
                viewHolder.checkBox.setChecked(true);
                viewHolder.itemView.setSelected(true);
            } else {
                viewHolder.checkBox.setChecked(false);
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
        return mFilterList.size();
    }



    class MultiViewHolder extends RecyclerView.ViewHolder{
        public TextView taskName;  //任务名称
        public TextView taskNumber;  //任务编号
        public TextView checkType;  //安检类型
        public TextView totalUserNumber;   //总用户数
        public TextView restCount;   //剩余户数
        public TextView endTime;  //结束时间
        /**选择框*/
        public CheckBox checkBox;  

        public MultiViewHolder(View itemView) {
            super(itemView);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
           taskNumber = (TextView) itemView.findViewById(R.id.task_number);
         checkType = (TextView) itemView.findViewById(R.id.check_type);
          totalUserNumber = (TextView) itemView.findViewById(R.id.total_user_number);
            restCount  = (TextView) itemView.findViewById(R.id.rest_count);
            endTime = (TextView) itemView.findViewById(R.id.end_time);
           checkBox = (CheckBox) itemView.findViewById(R.id.is_checked);
 
            
        }
    }


}
