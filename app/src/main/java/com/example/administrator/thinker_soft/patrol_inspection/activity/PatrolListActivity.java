package com.example.administrator.thinker_soft.patrol_inspection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.patrol_inspection.adapter.PatrolAdapter;
import com.example.administrator.thinker_soft.patrol_inspection.model.PatrolListItem;

import java.util.ArrayList;

public class PatrolListActivity extends AppCompatActivity {

    private ImageView back;
    private RecyclerView mRecyclerView;
    private PatrolAdapter mAdapter;
    private RecyclerView.LayoutManager mManager;
    private ArrayList<PatrolListItem> patrolList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_list);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //事件监听
    private void setViewClickListener() {
        back.setOnClickListener(clickListener);
    }

    //初始化设置
    private void defaultSetting() {
        for (int i = 0; i < 3; i++) {
            PatrolListItem p = new PatrolListItem();
            p.setDate("2017.11.1" + i);
            if (i == 0) {
                p.setState("已经完成");
            } else {
                p.setState("暂停中");
            }
            p.setTitle("test" + i);
            patrolList.add(p);
        }
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new PatrolAdapter(patrolList, new PatrolAdapter.onRecyclerItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Intent intent = new Intent(PatrolListActivity.this, EquipmentPatrolActivity.class);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        mRecyclerView = (RecyclerView) findViewById(R.id.patrol_recyclerview);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
            }
        }
    };

}
