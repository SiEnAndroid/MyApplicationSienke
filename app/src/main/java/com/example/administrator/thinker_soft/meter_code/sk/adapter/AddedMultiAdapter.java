package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.AddedBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/8/4.
 *  implements Filterable
 */

public class AddedMultiAdapter extends RecyclerView.Adapter {
  //  private List<AddedBean.AddedList> mSourceList = new ArrayList<>();
    private List<AddedBean.AddedList> mFilterList = new ArrayList<>();
    private OnItemClickLitener mOnItemClickLitener;
    public static HashMap<Integer, Boolean> isSelected;

    public AddedMultiAdapter(List<AddedBean.AddedList> datas) {
        this.mFilterList = datas;
       // this.mSourceList=datas;
        init();
    }

    public void clear(){
        this.mFilterList.clear();
       // this.mSourceList.clear();
        init();

    }

    public void addAll(List<AddedBean.AddedList> mFilterListS) {
         //   mFilterList.addAll(mFilterListS);
   //     this.mSourceList.addAll(mFilterList);
            init();
    }

    public void setmFilterList(List<AddedBean.AddedList> mFilterList) {
        this.mFilterList = mFilterList;
     //   this.mSourceList.addAll(mFilterList);
     //   init();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_added, parent, false);

        return new AddedMultiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AddedMultiViewHolder){
            final AddedMultiViewHolder viewHolder = (AddedMultiViewHolder) holder;
            AddedBean.AddedList bean = mFilterList.get(position);
            viewHolder.mTvName.setText(bean.getC_user_name());
            viewHolder.tvNumber.setText(bean.getC_meter_number());
            viewHolder.phone.setText(bean.getC_User_Phone());
            viewHolder.newNumber.setText(bean.getC_user_id());
            viewHolder.oldNumber.setText(bean.getC_old_user_id());
            viewHolder.property.setText(bean.getC_properties_name());
            viewHolder.address.setText(bean.getC_user_address());
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


//    //重写getFilter()方法
//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            //执行过滤操作
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                String charString = charSequence.toString();
//                if (charString.isEmpty()) {
//                    //没有过滤的内容，则使用源数据
//                    mFilterList = mSourceList;
//                } else {
//                    List<AddedBean.AddedList> filteredList = new ArrayList<>();
////                    for (String str : mSourceList) {
////                        //这里根据需求，添加匹配规则
////                        if (str.contains(charString)) {
////                           filteredList.add(str);
////                        }
////                    }
//                    for (AddedBean.AddedList item : mSourceList) {
//                        if (item.getC_user_id().contains(charString) || item.getC_user_name().contains(charString)
//                                || item.getC_meter_number().contains(charString) || item.getC_properties_name().contains(charString)
//                                || item.getC_meter_number().contains(charString) || item.getC_User_Phone().contains(charString)
//                                || item.getC_user_address().contains(charString)|| item.getPre_time().contains(charString)) {
//                            filteredList.add(item);
//                        }
//                    }
//
//
//                    mFilterList = filteredList;
//                }
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = mFilterList;
//                return filterResults;
//            }
//            //把过滤后的值返回出来
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                mFilterList = (ArrayList<AddedBean.AddedList>) filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }


    class AddedMultiViewHolder extends RecyclerView.ViewHolder{
        TextView mTvName;//用户名
        CheckBox mCheckBox;
        TextView tvNumber;//气表编号
        TextView phone;//电话
        TextView newNumber;//用户编好
        TextView  oldNumber;//旧编号
        TextView property;//性质
        TextView address;//用户地址
        public AddedMultiViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tv_userName);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
            phone = (TextView) itemView.findViewById(R.id.tv_phone_number);
            newNumber = (TextView) itemView.findViewById(R.id.tv_user_id);
            oldNumber=(TextView) itemView.findViewById(R.id.old_user_id);
            property=(TextView) itemView.findViewById(R.id.tv_user_property);
            address=(TextView) itemView.findViewById(R.id.tv_address);



        }
    }

    /**
     * 监听
     */
    public  interface OnItemClickLitener{
       void onItemClick(View view,int position);
    }

}
