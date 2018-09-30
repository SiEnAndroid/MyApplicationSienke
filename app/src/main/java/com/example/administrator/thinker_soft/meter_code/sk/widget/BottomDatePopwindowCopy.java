package com.example.administrator.thinker_soft.meter_code.sk.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.InstructionsBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportTranCdBean;
import com.example.administrator.thinker_soft.mode.wheelview.OnWheelScrollListener;
import com.example.administrator.thinker_soft.mode.wheelview.WheelView;
import com.example.administrator.thinker_soft.mode.wheelview.adapter.AbstractWheelTextAdapter1;

import java.util.ArrayList;


/**
 * @author g
 * @FileName BottomDatePopwindow
 * @date 2018/8/31 13:22
 */
public class BottomDatePopwindowCopy extends PopupWindow implements View.OnClickListener {

    private CalendarTextAdapter mMonthAdapter;
    private final Context context;
    private WheelView wvList;


    private TextView btnSure;
    private TextView btnCancel;
    private  String tvName;
    private String ids;

    private ArrayList<InstructionsBean> arry;

    private int maxTextSize = 24;
    private int minTextSize = 14;

    private boolean issetdata = false;


    private OnBirthListener onBirthListener;

    public BottomDatePopwindowCopy(Context context1, String title, ArrayList<InstructionsBean> list) {
        super(context1);
        this.context = context1;
        this.tvName=title;
        this.arry=list;
        View view=View.inflate(context, R.layout.view_dialog_bottom,null);
        wvList = (WheelView) view.findViewById(R.id.wv_list);

        btnSure = (TextView) view.findViewById(R.id.tv_sure);
        btnCancel = (TextView) view.findViewById(R.id.tv_cancel);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.camera);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);


        mMonthAdapter = new CalendarTextAdapter(context, arry, setIndex(tvName), maxTextSize, minTextSize);
        wvList.setVisibleItems(5);
        wvList.setViewAdapter(mMonthAdapter);
        wvList.setCurrentItem(setIndex(tvName));

        ids=arry.get(0).getN_PILEME_ID();
        wvList.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMonthAdapter);
                tvName=currentText;
                ids=arry.get(wheel.getCurrentItem()).getN_PILEME_ID();
            }
        });


    }




    /**
     *
     * @param name
     */
    public int setIndex(String name) {
        int index = 0;

        for (int i=0;i<arry.size(); i++) {
            if (arry.get(i).getC_PILEME_CONTENT().equals(name)) {
                index=i;
                return index;
            }
        }
        return index;
    }
    private class CalendarTextAdapter extends AbstractWheelTextAdapter1 {
        ArrayList<InstructionsBean> list;

        protected CalendarTextAdapter(Context context, ArrayList<InstructionsBean> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index).getC_PILEME_CONTENT() + "";
        }
    }

    public void setBirthdayListener(OnBirthListener onBirthListener) {
        this.onBirthListener = onBirthListener;
    }

    @Override
    public void onClick(View v) {
        if (v == btnSure) {
            if (onBirthListener != null) {
                onBirthListener.onClick(tvName,ids);

            }
        }else {
         //   dismiss();
        }
        dismiss();

    }

    public interface OnBirthListener {
        public void onClick(String title, String ids);
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText,CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxTextSize);
            } else {
                textvew.setTextSize(minTextSize);
            }
        }
    }






}
