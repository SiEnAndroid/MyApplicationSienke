package com.example.administrator.thinker_soft.mobile_business;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.BusinessViewPageAdapter;
import com.example.administrator.thinker_soft.mobile_business.fragment.NetFragment;
import com.example.administrator.thinker_soft.mobile_business.fragment.PersonWorkFragment;
import com.example.administrator.thinker_soft.mobile_business.fragment.PublicMessageFragment;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2017/6/8.
 */
public class MobileBusinessActivity extends FragmentActivity {

    private ViewPager viewPager;
    private ImageView back, more;
    private RadioButton publicMessage, net, person;
    private TextView name;
    private List<Fragment> fragmentList;
    private BusinessViewPageAdapter adapter;

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("SecurityChooseActivity", "onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("SecurityChooseActivity", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("SecurityChooseActivity", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("SecurityChooseActivity", "onRestart");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_business_homepage);

        bindView();//绑定控件
        defaultSetting();//初始化设置
        setViewPager();//设置viewPager
        setViewClickListener();//点击事件
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        more = (ImageView) findViewById(R.id.more);
        viewPager = (ViewPager) findViewById(R.id.business_viewpager);
        publicMessage = (RadioButton) findViewById(R.id.public_message);
        net = (RadioButton) findViewById(R.id.net);
        person = (RadioButton) findViewById(R.id.person);
        name = (TextView) findViewById(R.id.name);

    }
    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        publicMessage.setOnClickListener(clickListener);
        net.setOnClickListener(clickListener);
        person.setOnClickListener(clickListener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        publicMessage.setChecked(true);
                        net.setChecked(false);
                        person.setChecked(false);
                        name.setText("公共信息");
                        break;
                    case 1:
                        publicMessage.setChecked(false);
                        net.setChecked(true);
                        person.setChecked(false);
                        name.setText("网络通讯");
                        break;
                    case 2:
                        publicMessage.setChecked(false);
                        net.setChecked(false);
                        person.setChecked(true);
                        name.setText("个人办公");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
                case R.id.more:
                    break;
                case R.id.public_message:
                    viewPager.setCurrentItem(0);
                    name.setText("公共信息");
                    break;
                case R.id.net:
                    viewPager.setCurrentItem(1);
                    name.setText("网络通讯");
                    break;
                case R.id.person:
                    viewPager.setCurrentItem(2);
                    name.setText("个人办公");
                    break;
            }
        }
    };
    //初始化设置
    private void defaultSetting() {
        publicMessage.setChecked(true);
    }

    //设置viewPager
    private void setViewPager() {
        fragmentList = new ArrayList<>();
        //添加fragment到list
        fragmentList.add(new PublicMessageFragment());
        fragmentList.add(new NetFragment());
        fragmentList.add(new PersonWorkFragment());
        //避免报空指针
        if (fragmentList != null) {
            adapter = new BusinessViewPageAdapter(getSupportFragmentManager(), fragmentList);
        }
        viewPager.setAdapter(adapter);
    }
}

