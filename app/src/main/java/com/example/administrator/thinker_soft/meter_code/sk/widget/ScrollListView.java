package com.example.administrator.thinker_soft.meter_code.sk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * class
 *
 * @author g
 * @date 2018/8/23:9:17]
 */
public class ScrollListView extends ListView {

    public ScrollListView(Context context) {
        super(context);
    }

    public ScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
