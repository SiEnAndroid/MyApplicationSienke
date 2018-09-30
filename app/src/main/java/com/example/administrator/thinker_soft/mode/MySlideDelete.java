package com.example.administrator.thinker_soft.mode;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/4/7 0007.
 */
public class MySlideDelete extends ViewGroup {
    private View myContent;       //内容部分
    private View myDelete;        //删除部分
    private int myContentWidth;   //内容宽度
    private int myContentHeight;  //内容高度
    private int myDeleteWidth;    //删除的宽度
    private int myDeleteHeight;   //删除的高度
    private ViewDragHelper viewDragHelper;
    private OnSlideDeleteListener onSlideDeleteListener;

    public MySlideDelete(Context context) {
        super(context);
    }

    public MySlideDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySlideDelete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        myContent = getChildAt(0);
        myDelete = getChildAt(1);
        viewDragHelper = ViewDragHelper.create(this, new MyDragHelper());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 这跟myContent的父亲的大小有关，父亲是宽填充父窗体，高度是和孩子一样是55dp
        myContent.measure(widthMeasureSpec, heightMeasureSpec); // 测量内容部分的大小
        LayoutParams layoutParams = myDelete.getLayoutParams();
        int deleteWidth = MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY);
        int deleteHeight = MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY);
        // 这个参数就需要指定为精确大小
        myDelete.measure(deleteWidth, deleteHeight); // 测量删除部分的大小
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        myContentWidth = myContent.getMeasuredWidth();
        myContentHeight = myContent.getMeasuredHeight();
        myContent.layout(0, 0, myContentWidth, myContentHeight); // 摆放内容部分的位置
        myDeleteWidth = myDelete.getMeasuredWidth();
        myDeleteHeight = myDelete.getMeasuredHeight();
        myDelete.layout(myContentWidth, 0, myContentWidth + myDeleteWidth, myDeleteHeight); // 摆放删除部分的位置
    }

    class MyDragHelper extends ViewDragHelper.Callback {
        /**
         * Touch的down事件会回调这个方法 tryCaptureView
         *
         * @return : ViewDragHelper是否继续分析处理 child的相关touch事件
         * @Child：指定要动的孩子 （哪个孩子需要动起来）
         * @pointerId: 点的标记
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return myContent == child || myDelete == child;
        }

        // Touch的move事件会回调这面这几个方法
        // clampViewPositionHorizontal
        // clampViewPositionVertical
        // onViewPositionChanged

        /**
         * 捕获了水平方向移动的位移数据
         *
         * @param child 移动的孩子View
         * @param left  父容器的左上角到孩子View的距离
         * @param dx    增量值，其实就是移动的孩子View的左上角距离控件（父亲）的距离，包含正负
         * @return 如何动
         * <p/>
         * 调用完此方法，在android2.3以上就会动起来了，2.3以及以下是海动不了的
         * 2.3不兼容的问题，clampViewPositionHorizontal和clampViewPositionHorizontal所产生的动画效果在2.3以上才会有效果，
         * 如果要达到兼容，我们就需要借助onViewPositionChanged方法。
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == myContent) { // 解决内容部分左右拖动的越界问题
                if (left > 0) {
                    return 0;
                } else if (-left > myDeleteWidth) {
                    return -myDeleteWidth;
                }
            }
            if (child == myDelete) { // 解决删除部分左右拖动的越界问题
                if (left < myContentWidth - myDeleteWidth) {
                    return myContentWidth - myDeleteWidth;
                } else if (left > myContentWidth) {
                    return myContentWidth;
                }
            }
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return super.clampViewPositionVertical(child, top, dy);
        }

        /**
         * 当View的位置改变时的回调
         *
         * @param changedView 哪个View的位置改变了
         * @param left        changedView的left
         * @param top         changedView的top
         * @param dx          x方向的上的增量值
         * @param dy          y方向上的增量值
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            //super.onViewPositionChanged(changedView, left, top, dx, dy);
            invalidate();
            if (changedView == myContent) { // 如果移动的是mContent
                //我们移动mContent的实惠要相应的联动改变mDelete的位置
                //用mDelete的layout方法，改变mDelete的位置
                int tempDeleteLeft = myContentWidth + left;
                int tempDeleteRight = myContentWidth + left + myDeleteWidth;
                myDelete.layout(tempDeleteLeft, 0, tempDeleteRight, myDeleteHeight);
            } else { // touch的是mDelete
                int tempContentLeft = left - myContentWidth;
                int tempContentRight = left;
                myContent.layout(tempContentLeft, 0, tempContentRight, myContentHeight);
            }
        }

        /**
         * 相当于Touch的up的事件会回调onViewReleased这个方法
         *
         * @param releasedChild
         * @param xvel          x方向的速率
         * @param yvel          y方向的速率
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            // 方法的参数里面没有left，可以用 getLeft()这个方法
            int mConLeft = myContent.getLeft();
            // 这里没必要分两个孩子判断
            if (-mConLeft > myDeleteWidth / 2) {
                isShowDelete(true);
                if(onSlideDeleteListener != null){
                    onSlideDeleteListener.onOpen(MySlideDelete.this); // 调用接口打开的方法
                }
            } else {
                isShowDelete(false);
                if(onSlideDeleteListener != null){
                    onSlideDeleteListener.onClose(MySlideDelete.this); // 调用接口的关闭的方法
                }
            }
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        /**
         * 需要做一些过渡动画，才显得自然
         * ViewDragHelper里面给我们提供了一个方法，smoothSlideViewTo(View child, int finalLeft, int finalTo)，
         * smooth是平滑的意思，这个方法就是帮助我们做平滑滑动的。
         *
         * @param child
         * @param finalLeft
         * @param finalTop
         * @return
         */
    }

    /**
     * 是否展示delete部分
     *
     * @param isShowDelete
     */
    public void isShowDelete(boolean isShowDelete) {
        if (isShowDelete) {
            //mContent.layout(-mDeleteWidth,0,mContentWidth-mDeleteWidth,mContentHeight);
            //mDelete.layout(mContentWidth-mDeleteWidth,0,mContentWidth,mDeleteHeight);
            //采用ViewDragHelper的 smoothSlideViewTo 方法让移动变得顺滑自然，不会太生硬
            //smoothSlideViewTo只是模拟了数据，但是不会真正的动起来，动起来需要调用 invalidate
            // 而 invalidate 通过调用draw()等方法之后最后还是还是会调用 computeScroll 这个方法
            // 所以，使用 smoothSlideViewTo 做过渡动画需要结合  invalidate方法 和 computeScroll方法
            // smoothSlideViewTo的动画执行时间没有暴露的参数可以设置，但是这个时间是google给我们经过大量计算给出合理时间
            viewDragHelper.smoothSlideViewTo(myContent, -myDeleteWidth, 0);
            viewDragHelper.smoothSlideViewTo(myDelete, myContentWidth - myDeleteWidth, 0);
        } else {
            //mContent.layout(0,0,mContentWidth,mContentHeight);
            //mDelete.layout(mContentWidth, 0, mContentWidth + mDeleteWidth, mDeleteHeight);
            viewDragHelper.smoothSlideViewTo(myContent, 0, 0);
            viewDragHelper.smoothSlideViewTo(myDelete, myContentWidth, 0);
        }
        invalidate();
    }

    @Override
    public void computeScroll() {
        //super.computeScroll();
        // 把捕获的View适当的时间移动，其实也可以理解为 smoothSlideViewTo 的模拟过程还没完成
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
        // 其实这个动画过渡的过程大概在怎么走呢？
        // 1、smoothSlideViewTo方法进行模拟数据，模拟后就就调用invalidate();
        // 2、invalidate()最终调用computeScroll，computeScroll做一次细微动画，
        //    computeScroll判断模拟数据是否彻底完成，还没完成会再次调用invalidate
        // 3、递归调用，知道数据noni完成。
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        /**Process a touch event received by the parent view. This method will dispatch callback events
         as needed before returning. The parent view's onTouchEvent implementation should call this. */
        viewDragHelper.processTouchEvent(event); // 使用ViewDragHelper必须复写onTouchEvent并调用这个方法
        return true; //消费这个touch
    }

    public void setOnSlideDeleteListener(OnSlideDeleteListener onSlideDeleteListener){
        this.onSlideDeleteListener = onSlideDeleteListener;
    }

    // SlideDlete的接口
    public interface OnSlideDeleteListener {
        void onOpen(MySlideDelete slideDelete);
        void onClose(MySlideDelete slideDelete);
    }

}
