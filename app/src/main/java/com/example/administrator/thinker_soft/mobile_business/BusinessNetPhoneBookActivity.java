package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.PhoneBookAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/10.
 */
public class BusinessNetPhoneBookActivity extends Activity {
    private ListView bookListview;
    private ImageView back, close;
    private List<PhoneBookListviewItem> phoneBookListviewItemList = new ArrayList<>();
    private PhoneBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_net_book);

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        close = (ImageView) findViewById(R.id.close);
        bookListview = (ListView) findViewById(R.id.book_listview);
    }

    //假数据
    public void getData() {
        for (int i = 0; i < 10; i++) {
            PhoneBookListviewItem item = new PhoneBookListviewItem();
            item.setName("安安");
            phoneBookListviewItemList.add(item);
        }
    }

    public void setOnClickListener() {
        getData();
        adapter = new PhoneBookAdapter(BusinessNetPhoneBookActivity.this, phoneBookListviewItemList);
        bookListview.setAdapter(adapter);
        back.setOnClickListener(clickListener);
        close.setOnClickListener(clickListener);
        bookListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.close:
                    break;
            }
        }
    };
}
