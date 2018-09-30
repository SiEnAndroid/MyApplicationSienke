package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.adapter.FileManagerListviewAdapter;
import com.example.administrator.thinker_soft.Security_check.model.FileManagerListviewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */
public class FileManageActivity extends Activity {
    private ImageView back;
    private ListView slideCutListView;
    private List<FileManagerListviewItem> fileManagerListviewItemList = new ArrayList<>();
    private FileManagerListviewAdapter fileManagerListviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        //暂时获取假数据
        getData();
        //绑定控件
        bindView();
        //点击事件
        setOnClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        slideCutListView = (ListView) findViewById(R.id.slideCutListView);
    }

    //点击事件
    private void setOnClickListener() {
        back.setOnClickListener(clickListener);
        fileManagerListviewAdapter = new FileManagerListviewAdapter(FileManageActivity.this, fileManagerListviewItemList);
        slideCutListView.setAdapter(fileManagerListviewAdapter);
        slideCutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FileManageActivity.this, TaskChooseActivity.class);
                startActivity(intent);
            }
        });
        /*//长按删除Item
        slideCutListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(FileManageActivity.this);
                builder.setMessage("确定删除？");
                builder.setTitle("提示");

                //添加AlerDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(fileManagerListviewItemList.remove(position)!=null){
                            System.out.println("success");
                        }else {
                            System.out.println("failed");
                        }
                        fileManagerListviewAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(),"删除列表项",Toast.LENGTH_SHORT).show();
                    }
                });

                //添加AlerDialog.Bulider的对象setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
                return true;
            }
        });*/
        slideCutListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    fileManagerListviewAdapter.closeOtherItem();
                    Log.i("onScrollStateChanged=>","垂直滑动进来了！");
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    FileManageActivity.this.finish();
                    break;
            }
        }
    };

    //暂时获取假数据
    public void getData() {
        for (int i = 0; i < 20; i++) {
            FileManagerListviewItem item = new FileManagerListviewItem();
            item.setSlideContent("安检任务");
            fileManagerListviewItemList.add(item);
        }
    }
}
