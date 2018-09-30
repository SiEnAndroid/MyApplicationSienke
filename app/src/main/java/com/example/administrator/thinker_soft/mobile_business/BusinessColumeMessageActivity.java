package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.ColumnMessageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
public class BusinessColumeMessageActivity extends Activity {
    private ImageView back;
    private RadioButton all,byt1,byt2,byt3,byt4;
    private ListView listView;
    private TextView search;
    private ColumnMessageAdapter adapter;
    private List<BusinessColumnListviewItem> businessColumnListviewItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_column_message);//栏目信息

        bindView();//绑定控件
        setOnClickListener();//点击事件
    }

    public void bindView(){
        back = (ImageView) findViewById(R.id.back);
        all = (RadioButton) findViewById(R.id.all);
        byt1 = (RadioButton) findViewById(R.id.byt1);
        byt2 = (RadioButton) findViewById(R.id.byt2);
        byt3 = (RadioButton) findViewById(R.id.byt3);
        byt4 = (RadioButton) findViewById(R.id.byt4);
        listView = (ListView) findViewById(R.id.listview);
        search = (TextView) findViewById(R.id.search);
    }

    //假数据
    public void getData(){
        for(int i=0;i<10;i++){
            BusinessColumnListviewItem item = new BusinessColumnListviewItem();
            item.setHuifu("0回");
            item.setLeirong("当时发生大法师打发斯蒂芬");
            item.setLeixin("文化生活");
            item.setName("张黎明");
            item.setPinglun("0评");
            item.setYuedu("0阅");
            item.setTime("06/04 17:12");
            item.setTitle("大事发生的");
            businessColumnListviewItemList.add(item);
        }
    }

    public void setOnClickListener(){
        back.setOnClickListener(clickListener);
        all.setOnClickListener(clickListener);
        byt1.setOnClickListener(clickListener);
        byt2.setOnClickListener(clickListener);
        byt3.setOnClickListener(clickListener);
        byt4.setOnClickListener(clickListener);
        search.setOnClickListener(clickListener);
        getData();
        adapter= new ColumnMessageAdapter(BusinessColumeMessageActivity.this,businessColumnListviewItemList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
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
}
