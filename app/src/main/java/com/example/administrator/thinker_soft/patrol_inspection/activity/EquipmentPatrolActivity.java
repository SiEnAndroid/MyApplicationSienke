package com.example.administrator.thinker_soft.patrol_inspection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.patrol_inspection.adapter.EquipmentPatrolAdapter;
import com.example.administrator.thinker_soft.patrol_inspection.model.EquipmentItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EquipmentPatrolActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.lv_equipment)
    ListView lvEquipment;

    private EquipmentPatrolAdapter adapter;
    private ArrayList<EquipmentItem> equipmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_patrol);
        ButterKnife.bind(this);
        initData();
        defaultSetting();
    }

    private void initData() {
        EquipmentItem equipmentItem = new EquipmentItem();
        equipmentItem.setState("巡检中");
        equipmentItem.setDistance("200米");
        equipmentItem.setEquipmentInfo("gs");
        equipmentItem.setEquipmentName("阀门：");
        equipmentList.add(equipmentItem);
        EquipmentItem equipmentItem1 = new EquipmentItem();
        equipmentItem1.setState("巡检中");
        equipmentItem1.setDistance("300米");
        equipmentItem1.setEquipmentInfo("tyx");
        equipmentItem1.setEquipmentName("调压箱：");
        equipmentList.add(equipmentItem1);
        EquipmentItem equipmentItem2 = new EquipmentItem();
        equipmentItem2.setState("未巡检");
        equipmentItem2.setDistance("500米");
        equipmentItem2.setEquipmentInfo("tyx2");
        equipmentItem2.setEquipmentName("调压箱：");
        equipmentList.add(equipmentItem2);
    }

    private void defaultSetting() {
        adapter = new EquipmentPatrolAdapter(this, equipmentList);
        lvEquipment.setAdapter(adapter);
        lvEquipment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EquipmentPatrolActivity.this, PatrolEnteringActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
