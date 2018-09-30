package com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportTsParame;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;
import com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn.fragment.ReportConditionFragment;
import com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn.fragment.ReportEnclosureFragment;
import com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn.fragment.ReportMessageFragment;
import com.example.administrator.thinker_soft.meter_code.sk.ui.reportadorn.fragment.ReportTransactFragment;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.EditTextUtils;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * File:ReportAdornDetailsActivity.class
 *
 * @author Administrator
 * @date 2018/8/29 10:24
 */

public class ReportAdornDetailsActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private ReportMessageFragment messageFragment;
    private ReportConditionFragment condition;
    private ReportEnclosureFragment enclosure;
    private ReportTransactFragment transact;
    private FragmentManager fragmentManager;
    private Fragment currentFragment=new Fragment();
    /**根据id取值*/
    private  String bid,proCom,queId,proId,tranId;
    private ReportTsParame parame;
  //  private boolean checked;
    private  String processName;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.clear)
    TextView clear;

    @BindView(R.id.rb_message)
    RadioButton rbMag;
    @BindView(R.id.frame_message)
    FrameLayout frameMag;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.ns_view)
    NestedScrollView nsView;
    @BindView(R.id.view_line)
    View line;
    @BindView(R.id.rb_transact)
    RadioButton rbTransact;
    @BindView(R.id.frame_transact)
    FrameLayout frameTransact;
    @Override
    protected int getContentViewID() {
        return R.layout.activity_reportadorn_details;
    }

    @Override
    protected void initView() {
        tvTitle.setText("业务详情");
        clear.setText("");
        getIntentData();
        radioGroup.setOnCheckedChangeListener(this);
        //rbMag.setOnClickListener(this);

         initFragment();
         showFragment(R.id.frame_message,messageFragment);
         //点击空白输入框失去焦点
        nsView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nsView.setFocusable(true);
                nsView.setFocusableInTouchMode(true);
                nsView.requestFocus();
                return false;
            }
        });
    }

    /**
     * 点击空白取消输入发
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (EditTextUtils.isShouldHideKeyboard(v, ev)) {
                EditTextUtils.hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 监听
     * @param view
     */
    @OnClick({R.id.back})
    public void OnclicDetails(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch(checkedId){
            case R.id.rb_message:
                //业务信息
               showFragment(R.id.frame_message,messageFragment);
                break;
            case R.id.rb_condition:
                //办理情况
                showFragment(R.id.frame_condition,condition);
                break;
            case R.id.rb_enclosure:
                //业务附件
                showFragment(R.id.frame_enclosure,enclosure);
                break;
            case R.id.rb_transact:
                //业务办理
              showFragment(R.id.frame_transact,transact);
                break;
                default:
                    break;
        }
    }

    /**
           * 初始化Fragment
           */
    private void initFragment() {
        fragmentManager=getSupportFragmentManager();
        messageFragment = ReportMessageFragment.newInstance(tranId,proCom);
        condition =  ReportConditionFragment.newInstance(bid);
        enclosure = ReportEnclosureFragment.newInstance(parame);
        transact = ReportTransactFragment.newInstance(parame);
          }


/**
     * 展示Fragment
      */
    private void showFragment(int id,Fragment fragment) {
         if (currentFragment!=fragment) {
             //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
               FragmentTransaction transaction =fragmentManager.beginTransaction();
              transaction.hide(currentFragment);
                    currentFragment = fragment;
                     if (!fragment.isAdded()) {
                         //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
                           transaction.add(id, fragment).show(fragment).commit();
                        } else {
                         //显示需要显示的fragment
                       transaction.show(fragment).commit();
              }
         }
        }

    /**
     * 获取bundle值
     */
    public void getIntentData() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle!=null){
             bid= String.valueOf(bundle.getDouble("bid"));
            proCom=bundle.getString("proCom");
            queId=bundle.getString("queId");
            proId=bundle.getString("proId");
            tranId=bundle.getString("tranId");
             processName=bundle.getString("process_name");
            String userName=bundle.getString("userName");
            String process=bundle.getString("process");
             parame=new ReportTsParame();
            parame.setBusId(bid);
            parame.setProId(proId);
            parame.setQueId(queId);
            parame.setTranId(tranId);
            parame.setUserName(userName);
            parame.setProcess(process);
            String type="未办理";
            if (processName!=null&& !processName.equals(type)){
                line.setVisibility(View.GONE);
                rbTransact.setVisibility(View.GONE);
                frameTransact.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_message:

                break;

                default:
                    break;

        }
    }
}
