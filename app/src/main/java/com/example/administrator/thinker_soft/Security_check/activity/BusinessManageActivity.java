package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.Security_check.adapter.BusinessManageAdapter;
import com.example.administrator.thinker_soft.Security_check.model.BusinessManageListviewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/2 0002.
 */
public class BusinessManageActivity extends Activity {
    private ImageView back;
    private ListView listView;
    private List<BusinessManageListviewItem> businessManageListviewItemList = new ArrayList<>();
    //退出程序
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_manage_homepage);

        getData();//暂时获取假数据
        bindView();//绑定控件ID
        setViewClickListener();//点击事件
    }

    //绑定控件ID
    public void bindView(){
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.business_manage_listview);
    }

    //点击事件
    private void setViewClickListener(){
        back.setOnClickListener(clickListener);
        BusinessManageAdapter adapter = new BusinessManageAdapter(BusinessManageActivity.this,businessManageListviewItemList);
        listView.setAdapter(adapter);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
            }
        }
    };

    public void getData(){
        for(int i=0;i<20;i++){
            BusinessManageListviewItem manageListviewItem = new BusinessManageListviewItem();
            businessManageListviewItemList.add(manageListviewItem);
        }
    }


    /**
     * 捕捉返回事件按钮
     * 因为此 Activity继承 TabActivity,用 onKeyDown无响应，所以改用 dispatchKeyEvent
     * 所以改用 dispatchKeyEvent
     * <p/>
     * 一般的 Activity 用 onKeyDown就可以了
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            //-------------Activity.this的context 返回当前activity的上下文，属于activity，activity 摧毁他就摧毁
            //-------------getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
