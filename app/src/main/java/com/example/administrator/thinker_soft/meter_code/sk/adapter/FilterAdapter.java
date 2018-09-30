package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportTranSysName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author g
 * @FileName FilterAdapter
 * @date 2018/9/3 11:19
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> implements Filterable {

    private List<ReportTranSysName> mSourceList = new ArrayList<>();
    private List<ReportTranSysName> mFilterList = new ArrayList<>();
    private Context mContext;
    private OnItemClickLitener mOnItemClickLitener;

    public FilterAdapter(Context context) {
        this.mContext=context;
    }

    public void setmOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void appendList(List<ReportTranSysName> list) {
        mSourceList = list;
        //这里需要初始化filterList
        mFilterList = list;
    }

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterHolder(LayoutInflater.from(mContext).inflate(R.layout.item_birth_year, parent, false));
    }

    @Override
    public void onBindViewHolder(final FilterHolder holder, final int position) {
        //这里也是过滤后的list
        holder.tv.setText(mFilterList.get(position).getC_USER_NAME());
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(holder.itemView, holder.getLayoutPosition(),mFilterList.get(position).getC_USER_NAME(),mFilterList.get(position).getN_SYSTEMUSER_ID()+"");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //注意这里需要是过滤后的list
        return mFilterList.size();
    }
    //重写getFilter()方法
    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mFilterList = mSourceList;
                } else {
                    List<ReportTranSysName> filteredList = new ArrayList<>();
                    for (ReportTranSysName item : mSourceList) {
                        if (item.getC_USER_NAME().contains(charString)) {
                            filteredList.add(item);
                        }
                    }
                    mFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }
            //把过滤后的值返回出来
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterList = (ArrayList<ReportTranSysName>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class FilterHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public FilterHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tempValue);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position,String name,String id);
    }

}


