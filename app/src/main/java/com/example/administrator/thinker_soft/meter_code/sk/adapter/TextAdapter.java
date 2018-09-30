package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;


/**
 * Created by Administrator on 2018/8/2.
 */

public class TextAdapter extends BaseAdapter{

    private  Context context;
    private  String[] array;
    private LayoutInflater inflater;

    public TextAdapter(Context context, String[] array){
        this.context=context;
        this.inflater = LayoutInflater.from(context);
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int position) {
        return array[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //有很多例子中都用到这个holder,理解下??
        ViewHolder holder = null;
        //思考这里为何要判断convertView是否为空  ？？
        if(convertView == null){
            holder = new ViewHolder();
            //把vlist layout转换成View【LayoutInflater的作用】
            convertView = inflater.inflate(R.layout.simple_list_item, null);
            //通过上面layout得到的view来获取里面的具体控件
            holder.text1 = (TextView) convertView.findViewById(R.id.text1);
            //不懂这里setTag什么意思??
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        //这里testData.get(position).get("title1"))，其实就是从list集合(testData)中取出对应索引的map，然后再根据键值对取值
        holder.text1.setText(array[position]);

        return convertView;
    }

    class ViewHolder{

        public TextView text1;
    }

}