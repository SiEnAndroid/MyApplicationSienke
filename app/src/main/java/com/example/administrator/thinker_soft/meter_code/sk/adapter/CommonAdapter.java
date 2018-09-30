package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/8/2.
 */

public abstract class CommonAdapter<T, V extends CommonAdapter.ViewHolder> extends BaseAdapter {
    //上下文
    private Context mContext;
    //集合数据
    private List<T> mDatas;
    //布局ID
    @LayoutRes
    private int mLayoutId;
    //解析布局LayoutInflater
    private LayoutInflater inflater;

    public CommonAdapter(Context context, @LayoutRes int layoutId){
        this(context,new ArrayList<T>(), layoutId);
    }
    public CommonAdapter(Context context,List<T> datas,@LayoutRes int layoutId){
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //这里和我们平时的判断是一样的
        if (convertView == null) {
            //解析对应的布局
            convertView = inflater.inflate(mLayoutId,null);
            /**
             * 这里是我们这里要重点理解的，来看看这里表示什么意思呢？
             * 首先看我们上面有个泛型V extends CommonAdapter.ViewHolder
             * 其实这里主要的目的就是拿到泛型V的Class类型，通过该类型，
             * 我们就可以创建子类的实例了，getClass().getGenericSuperclass()表示获取
             * 带有泛型的父类，相当于拿到了我们这里带有参数的CommonAdapter了，然后强转
             * 成ParameterizedType类型，通过getActualTypeArguments()拿到我们需要的泛型
             * 参数位置，0代表T泛型参数，1表示V泛型参数，所以这里通过getActualTypeArguments()[1]
             * 拿到了我们的泛型V。最后转换成我们的Class类型，就拿到了CommonAdapter.ViewHolder子类的类型了
             */
            Class clazz = (Class)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            Log.d("IT_Real", "getView: className" + clazz.getSimpleName());
            try {
                //通过反射拿到子类带有一个参数的构造方法
                Constructor constructor = clazz.getConstructor(View.class);
                //开始创建子类实例，并将convertView作为参数传过去，这样convertView就保存到对应类中了
                ViewHolder viewHolder = (ViewHolder) constructor.newInstance(convertView);
                //设置标志为ViewHolder类对象
                convertView.setTag(viewHolder);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        //获取具体的子类实例
        V holder = (V) convertView.getTag();
        //子类重写该方法，可以通过这两个参数拿到对应的参数了。
        onBind(holder,mDatas.get(position));
        //返回convertView;
        return convertView;
    }

    /**
     * 把所有添加数据的方法都聚集到这个类中，避免刷新数据时
     * 出现两种所指地址不相同的情况。外界不需要自己定义数据集了
     * 直接调用这些方法来存数据，和刷新数据
     * @param data
     */
    public void add(T data){
        mDatas.add(data);
        notifyDataSetChanged();
    }
    public void add(int index, T data){
        mDatas.add(index,data);
        notifyDataSetChanged();
    }
    public void addAll(Collection<? extends T> collection){
        mDatas.addAll(collection);
        notifyDataSetChanged();
    }
    public void remove(T data){
        mDatas.remove(data);
        notifyDataSetChanged();
    }
    public void remove(int index){
        mDatas.remove(index);
        notifyDataSetChanged();
    }
    public void clear(){
        mDatas.clear();
        notifyDataSetChanged();
    }

    /**
     * 子类实现该方法
     * @param holder ViewHolder的具体子类对象
     * @param data 要设置的数据
     */
    public abstract void onBind(V holder,T data);

    public static class ViewHolder{
        private View mItemView;
        //用于保存所有布局中的控件
        private SparseArray<View> mViews;
        public ViewHolder(View itemView){
            mItemView = itemView;
            mViews = new SparseArray<>();
        }

        /**
         * 为子类提供该方法，只需要之类传对应的
         * 控件id我们即可为他返回一个控件对象、
         * 同时通过SparseArray<View>省去了我们每次复用
         * 相同控件时，去重复findViewById，提高了程序的效率
         * @param viewId
         * @param <T>
         * @return
         */
        protected <T extends View> T getView(int viewId){
            View view = mViews.get(viewId);
            if(view == null){
                view = mItemView.findViewById(viewId);
                mViews.put(viewId,view);
            }
            return (T)view;
        }
    }

}