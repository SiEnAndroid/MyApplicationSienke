package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.thinker_soft.meter_code.sk.bean.PhotoPathBean;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/8/7.
 */

public class MyPhotoAdapter extends PagerAdapter {
    public static final String TAG = MyPhotoAdapter.class.getSimpleName();
    private List<PhotoPathBean> photoPathLists;
    private AppCompatActivity activity;
    private File file;


    public MyPhotoAdapter(List<PhotoPathBean> imageUrls, AppCompatActivity activity) {
        this.photoPathLists = imageUrls;
        this.activity = activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //   String url = photoPathLists.get(position);
        PhotoView photoView = new PhotoView(activity);
        if(photoPathLists.get(position) != null){
            file = new File(photoPathLists.get(position).getCropPath());
        //  Glide.with(activity).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).into(photoView);
            Glide.with(activity).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).into(photoView);
        }
        container.addView(photoView);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                activity.finish();
            }
        });
        return photoView;
    }

    @Override
    public int getCount() {
        return photoPathLists != null ? photoPathLists.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
