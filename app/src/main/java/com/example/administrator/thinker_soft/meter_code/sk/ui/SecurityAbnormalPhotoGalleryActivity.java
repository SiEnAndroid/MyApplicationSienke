package com.example.administrator.thinker_soft.meter_code.sk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.MySecurityAbnormalPhotoAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.widget.PhotoViewPager;

import java.util.ArrayList;


/**
 * Created by 111 on 2018/8/8.
 */

public class SecurityAbnormalPhotoGalleryActivity  extends AppCompatActivity implements View.OnClickListener {

    private int currentPosition;  // 点击进来的序号
    private PhotoViewPager mViewPager;
    private TextView mTvImageCount;
    private ImageView mSaveImage;
    private ImageView delete;
    private LinearLayout rootLinearlayout;
    private ImageView back;
    private ArrayList<String> cropIdLists;
    private MySecurityAbnormalPhotoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_abnormal_photo);

        init();
        initView();
        initData();
    }

    private void init() {
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition",0);
        cropIdLists = (ArrayList<String>) intent.getSerializableExtra("cropIdLists");

    }


    private void initView() {
        mViewPager = (PhotoViewPager) findViewById(R.id.view_pager_photo);
        mTvImageCount = (TextView) findViewById(R.id.tv_image_count);
        mSaveImage = (ImageView) findViewById(R.id.back);
        back = (ImageView) findViewById(R.id.back);
        rootLinearlayout = (LinearLayout) findViewById(R.id.ly);

        mSaveImage.setOnClickListener(this);

        mTvImageCount.setText(currentPosition+1 + "/" + cropIdLists.size());

    }

    private void initData() {
        adapter = new MySecurityAbnormalPhotoAdapter(this,cropIdLists,currentPosition);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTvImageCount.setText(String.valueOf(position+1)+"/"+cropIdLists.size());


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;

            default:
                break;
        }
    }
}
