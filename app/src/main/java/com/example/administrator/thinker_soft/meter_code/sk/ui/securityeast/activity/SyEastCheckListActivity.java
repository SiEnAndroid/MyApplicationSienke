package com.example.administrator.thinker_soft.meter_code.sk.ui.securityeast.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author g
 * @FileName SyEastCheckListActivity
 * @date 2018/9/30 9:12
 */
public class SyEastCheckListActivity extends BaseActivity {
    
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.clear)
    TextView clear;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.no_data)
    TextView noData;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_sy_check_list;
    }

    @Override
    protected void initView() {
        tvTitle.setText("");
        setddrawable();
    }

    /**
     * 设置添加按钮
     */
    private void setddrawable() {
        Drawable drawableRight = getResources().getDrawable(
                R.drawable.more_settings_selector);
        clear.setCompoundDrawablesWithIntrinsicBounds(null,
                null, drawableRight, null);
        clear.setCompoundDrawablePadding(5);


    }

    
    
}
