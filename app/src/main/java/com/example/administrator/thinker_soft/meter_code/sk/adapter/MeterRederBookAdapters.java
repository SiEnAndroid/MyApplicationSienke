package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.BookInfo;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * Created by Administrator on 2018/4/20.
 */
public class MeterRederBookAdapters extends RecyclerView.Adapter  {

    private Context mContext;
    //这个是checkbox的Hashmap集合
    private static HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
    //这个是数据集合
    private ArrayList<BookInfo> mList = new ArrayList<>();
    private MeterItemClickListener onItemClickListener;

    public ArrayList<BookInfo> getmList() {
        return mList;
    }

    public void setmList(ArrayList<BookInfo> list) {
        if (list!=null) {
            mList.clear();
            map.clear();
            mList.addAll(list);
            for (int i = 0; i < mList.size(); i++) {
                map.put(i, false);
            }
            notifyDataSetChanged();
        }
    }

    public static HashMap<Integer, Boolean> getMap() {
        return map;
    }

    public void refreshView() {
        notifyDataSetChanged();
    }

    public MeterRederBookAdapters(Context context) {
        this.mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.meter_area_listview_item, parent, false);
        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        MyHolder holder = (MyHolder) viewHolder;
        holder.meterName.setText(mList.get(position).getBOOK());
        holder.checkState.setChecked(getMap().get(position));
        holder.dataId.setText(mList.get(position).getID());

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, position);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView meterName;//名称
        private TextView dataId;//id
        public CheckBox checkState;//id

        public MyHolder(View itemView) {
            super(itemView);
            meterName = (TextView) itemView.findViewById(R.id.meter_name);
            dataId = (TextView) itemView.findViewById(R.id.data_id);
            checkState = (CheckBox) itemView.findViewById(R.id.check_state);
        }
    }


    public interface  MeterItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setmItemClickListener(MeterItemClickListener mItemClickListener) {
        this.onItemClickListener = mItemClickListener;
    }




//    public int getBookNum() {
//        return bookNum;
//    }

//    private  int bookNum = 0; // 记录抄表本选中的条目数量

//    public  void setBookNum(int bookNum) {
//        this.bookNum = bookNum;
//    }


//    /**
//     * 全选
//     */
//    public int  All() {
//        bookNum=0;
//        bookNum=mList.size();
//        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
//        boolean shouldall = true;
//        for (Map.Entry<Integer, Boolean> entry : entries) {
//            Boolean value = entry.getValue();
//            if (!value) {
//                shouldall = true;
//                break;
//            }
//        }
//        for (Map.Entry<Integer, Boolean> entry : entries) {
//            entry.setValue(shouldall);
//        }
//        notifyDataSetChanged();
//        return bookNum;
//    }
//
//    private void saveId(){
//        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
//        for (Map.Entry<Integer, Boolean> entry : entries) {
//            Boolean value = entry.getValue();
//            if (value) {
//            }
//            }
//        }
//
//
//    /**
//     * 取消
//     */
//    public int quit() {
//        bookNum=0;
//        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
//        for (Map.Entry<Integer, Boolean> entry : entries) {
//            entry.setValue(false);
//        }
//        notifyDataSetChanged();
//        return bookNum;
//    }
//
//    /**
//     * 反选
//     */
//    public int neverall() {
//       // bookNum=0;
//        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
//        for (Map.Entry<Integer, Boolean> entry : entries) {
//            entry.setValue(!entry.getValue());
//            if (entry.getValue()){
//                bookNum++;//数量加一
//            }else {
//                bookNum--;//数量减一
//            }
//        }
//        notifyDataSetChanged();
//        return bookNum;
//    }

//    /**
//     * 单选
//     *
//     * @param postion
//     */
//    public void singlesel(int postion) {
//        Set<Map.Entry<Integer, Boolean>> entries = map.entrySet();
//        for (Map.Entry<Integer, Boolean> entry : entries) {
//            entry.setValue(false);
//        }
//        map.put(postion, true);
//        notifyDataSetChanged();
//    }

    //这里主要初始化布局控件








}
