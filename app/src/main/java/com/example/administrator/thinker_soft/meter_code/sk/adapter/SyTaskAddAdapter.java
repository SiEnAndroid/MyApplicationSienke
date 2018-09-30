package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AddedBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SyEastTaskBean;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SimpleDateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/8/4.
 * implements Filterable
 */

public class SyTaskAddAdapter extends RecyclerView.Adapter {
    private List<SyEastTaskBean.TaskListBean> mFilterList = new ArrayList<>();
    private OnItemClickLitener mOnItemClickLitener;
    public static HashMap<Integer, Boolean> isSelected=new HashMap<>();

    public SyTaskAddAdapter(List<SyEastTaskBean.TaskListBean> datas) {
        this.mFilterList = datas;

        initCheck(false);
    }

    // 初始化map集合
    public void initCheck(boolean flag) {
        for (int i = 0; i < mFilterList.size(); i++) {
            getIsCheck().put(i, flag);
        }
    }

    public static HashMap<Integer, Boolean> getIsCheck() {
        return isSelected;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_task_listview_item, parent, false);

        return new AddedMultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddedMultiViewHolder) {
            final AddedMultiViewHolder viewHolder = (AddedMultiViewHolder) holder;
            final SyEastTaskBean.TaskListBean item = mFilterList.get(position);
            viewHolder.taskId.setText(item.getN_ANJIAN_PLAN_ID()+"");
            viewHolder.totalNumber.setText(item.getCOUNTRS()+"");
            viewHolder.restNumber.setText("( "+item.getREMAINCOUNTS()+" )");
            viewHolder.taskName.setText(item.getC_ANJIAN_PLAN_NAME());
            viewHolder.satrtDate.setText(item.getD_ANJIAN_START() != 0 ? SimpleDateUtils.longTime(item.getD_ANJIAN_START()) : "");
            viewHolder.endDate.setText(item.getD_ANJIAN_END() != 0 ? SimpleDateUtils.longTime(item.getD_ANJIAN_END()) : "");
            viewHolder.checkBox.setChecked(getIsCheck().get(position));
        
            if (mOnItemClickLitener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(viewHolder.itemView, viewHolder.getAdapterPosition(),item);
                    }
                });
            }


        }
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }


    public class AddedMultiViewHolder extends RecyclerView.ViewHolder {

        /**
         * 任务编号
         */
        public TextView taskId;
        /**
         * 总户数
         */
        public TextView totalNumber;

        /**
         * 剩余户数
         */
        public TextView restNumber;

        /**
         * 任务名称
         */
        public TextView taskName;
        /**
         * 开始时间
         */
        public TextView satrtDate;

        /**
         * 结束时间
         */
        public TextView endDate;
        //选择框
        public CheckBox checkBox;

        public AddedMultiViewHolder(View itemView) {
            super(itemView);

            taskId = (TextView) itemView.findViewById(R.id.task_id);
            totalNumber = (TextView) itemView.findViewById(R.id.total_number);
            restNumber = (TextView) itemView.findViewById(R.id.rest_number);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            satrtDate = (TextView) itemView.findViewById(R.id.start_date);
            endDate = (TextView) itemView.findViewById(R.id.end_date);
            checkBox = (CheckBox) itemView.findViewById(R.id.is_checked);

        }
    }

    /**
     * 监听
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position,SyEastTaskBean.TaskListBean bean);
    }

}
