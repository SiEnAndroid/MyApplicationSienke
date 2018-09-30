package com.example.administrator.thinker_soft.Security_check.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.model.FileManagerListviewItem;
import com.example.administrator.thinker_soft.mode.MySlideDelete;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Administrator on 2017/3/20.
 */
public class FileManagerListviewAdapter extends BaseAdapter {
    private Context context;
    private List<FileManagerListviewItem> slideDeleteList;
    private LayoutInflater layoutInflater;
    private List<MySlideDelete> slideDeleteArrayList = new ArrayList<>();

    public FileManagerListviewAdapter(Context context, List<FileManagerListviewItem> slideDeleteList) {
        this.context = context;
        this.slideDeleteList = slideDeleteList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }


    @Override
    public int getCount() {
        return slideDeleteList.size();
    }

    @Override
    public Object getItem(int position) {
        return slideDeleteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.file_manager_slidedelete_item, null);
            viewHolder.mySlideDelete = (MySlideDelete) convertView.findViewById(R.id.slide_delete);
            viewHolder.slideContent = (TextView) convertView.findViewById(R.id.slide_content);
            viewHolder.slideDeleteLayout = (LinearLayout) convertView.findViewById(R.id.slide_delete_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FileManagerListviewItem item = slideDeleteList.get(position);
        viewHolder.slideContent.setText(item.getSlideContent()+"        "+position);
        viewHolder.mySlideDelete.setOnSlideDeleteListener(new MySlideDelete.OnSlideDeleteListener() {
            @Override
            public void onOpen(MySlideDelete slideDelete) {
                closeOtherItem();
                slideDeleteArrayList.add(slideDelete);
                Log.i("closeOtherItem===>","进来了！");
            }

            @Override
            public void onClose(MySlideDelete slideDelete) {
                slideDeleteArrayList.remove(slideDelete);
            }
        });
        viewHolder.slideDeleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideDeleteList.remove(position);
                Log.i("slideDeleteLayout===>","删除的位置是："+position);
                Log.i("slideDeleteLayout===>","长度为！"+slideDeleteList.size());
                Log.i("slideDeleteLayout===>","删除监听进来了！");
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder {
        MySlideDelete mySlideDelete;
        TextView slideContent;
        LinearLayout slideDeleteLayout;
    }

    //滑动另一个item，关闭其他的item
    public void closeOtherItem(){
        // 采用Iterator的原因是for是线程不安全的，迭代器是线程安全的
        ListIterator<MySlideDelete> slideDeleteListIterator = slideDeleteArrayList.listIterator();
        while(slideDeleteListIterator.hasNext()){
            MySlideDelete slideDelete = slideDeleteListIterator.next();
            slideDelete.isShowDelete(false);
        }
        slideDeleteArrayList.clear();
    }


}
