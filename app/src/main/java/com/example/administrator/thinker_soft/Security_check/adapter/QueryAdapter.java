package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.QueryListviewItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/24 0024.
 */
public class QueryAdapter extends BaseAdapter {
    private Context context;
    private List<QueryListviewItem> queryListviewItemList;
    private LayoutInflater layoutInflater;

    public QueryAdapter(Context context,List<QueryListviewItem> queryListviewItemList){
        this.context = context;
        this.queryListviewItemList = queryListviewItemList;
        if(context != null){
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return queryListviewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return queryListviewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.query_listview_item,null);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.pay_state = (TextView) convertView.findViewById(R.id.pay_state);
            viewHolder.start_degree = (TextView) convertView.findViewById(R.id.start_degree);
            viewHolder.end_degree = (TextView) convertView.findViewById(R.id.end_degree);
            viewHolder.unit_cost = (TextView) convertView.findViewById(R.id.unit_cost);
            viewHolder.integrated_water = (TextView) convertView.findViewById(R.id.integrated_water);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        QueryListviewItem queryListviewItem = queryListviewItemList.get(position);
        Log.i("queryListviewItemList",""+position);
        //转换时期格式
        Date date;
        String time;
        try {
            date = new SimpleDateFormat("yyyyMM").parse(queryListviewItem.getDate());
            time = new SimpleDateFormat("yyyy年MM月").format(date);
            viewHolder.date.setText(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //当返回状态码是1表示已缴纳费用，0代表未缴纳费用
        if(queryListviewItem.getPay_state().equals("已缴费")){
            viewHolder.pay_state.setText("已缴纳");
            //Color.parseColor("#638C0A")
            viewHolder.pay_state.setBackgroundColor(context.getResources().getColor(R.color.green));
        }else{
            viewHolder.pay_state.setText("未缴纳");
            viewHolder.pay_state.setBackgroundColor(context.getResources().getColor(R.color.red));
        }
        viewHolder.start_degree.setText(queryListviewItem.getStart_degree()+"");//起度
        viewHolder.end_degree.setText(queryListviewItem.getEnd_degree()+"");  //止度
        viewHolder.unit_cost.setText(queryListviewItem.getUnit_cost()+"");   //单价
        viewHolder.integrated_water.setText(queryListviewItem.getIntegrated_water()+""); //综合水费
        return convertView;
    }

    class ViewHolder{
        TextView date;
        TextView pay_state;
        TextView start_degree;
        TextView end_degree;
        TextView unit_cost;
        TextView integrated_water;
    }
}
