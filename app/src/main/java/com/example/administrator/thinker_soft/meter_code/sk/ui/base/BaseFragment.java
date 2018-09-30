package com.example.administrator.thinker_soft.meter_code.sk.ui.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.thinker_soft.meter_code.sk.uitls.NetUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/5/4.
 */

public abstract  class BaseFragment extends Fragment {

    private View mRootView;
    private Unbinder mUnbinder;//绑定
    /**
     * 懒加载过
     */
    private boolean isLazyLoaded;

    private boolean isPrepared;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mRootView == null) {
            //绑定view
            mRootView = inflater.inflate(getContentViewID(), container,false);
            mUnbinder= ButterKnife.bind(this, mRootView);
            initView();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
           mUnbinder.unbind();
        }
        return mRootView;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        //只有Fragment onCreateView好了，
        //另外这里调用一次lazyLoad(）
        lazyLoad();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        lazyLoad();
    }

    /**
     * 调用懒加载
     */

    private void lazyLoad() {
        if (getUserVisibleHint() && isPrepared && !isLazyLoaded) {
            onLazyLoad();
            isLazyLoaded = true;
        }
    }

    @UiThread
    public abstract void onLazyLoad();



    /**
     * 根据传入的类(class)打开指定的activity
     * @param pClass
     */

    /**
     * 跳转Activity
     *
     * @param clazz
     */
    protected void GoActivity(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
        //getActivity().overridePendingTransition(R.anim.trans_next_in, R.anim.trans_next_out);
    }

    /**
     * 跳转Activity bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void GoActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);

        }
        startActivity(intent);
    }

    /**
     * 请求码跳转Activity
     *
     * @param clazz
     * @param requestCode
     */
    protected void GoActivityResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 请求码跳转Activity  bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void GoActivityResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 布局
     * @return
     */
    protected abstract int getContentViewID();
    /**
     * 布局
     * @return
     */
    protected abstract void initView();

    /**
     * 是否连接网络
     * @return
     */
    protected boolean checkNet() {
        return NetUtils.isNetworkAvailable(getActivity());
    }
}
