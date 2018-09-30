package com.example.administrator.thinker_soft.patrol_inspection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.MeterMapDownloadActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.pic_bg)
    ImageView picBg;
    @BindView(R.id.go_on_patrol)
    CardView goOnPatrol;
    @BindView(R.id.patrol_plan)
    CardView patrolPlan;
    @BindView(R.id.statistics)
    CardView statistics;
    @BindView(R.id.map_download)
    CardView mapDownload;
    @BindView(R.id.transfer)
    CardView transfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.go_on_patrol, R.id.patrol_plan, R.id.statistics, R.id.map_download, R.id.transfer})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.go_on_patrol:
                intent=new Intent(this,PatrolListActivity.class);
                startActivity(intent);
                break;
            case R.id.patrol_plan:
                intent=new Intent(this,PatrolListActivity.class);
                startActivity(intent);
                break;
            case R.id.statistics:
                break;
            case R.id.map_download:
                intent = new Intent(this, MeterMapDownloadActivity.class);
                startActivity(intent);
                break;
            case R.id.transfer:
                break;
        }
    }
}
