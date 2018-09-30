package com.example.administrator.thinker_soft.patrol_inspection.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.administrator.thinker_soft.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PatrolEnteringActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.cb_isusing)
    CheckBox cbIsusing;
    @BindView(R.id.cb_facade)
    CheckBox cbFacade;
    @BindView(R.id.cb_isleakage)
    CheckBox cbIsleakage;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_entering);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.btn_commit, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_commit:
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
