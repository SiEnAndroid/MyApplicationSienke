package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.SecurityAbnormalPhotoGalleryActivity;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

/**
 * Created by 111 on 2018/8/8.
 */

public class MySecurityAbnormalPhotoAdapter extends PagerAdapter {

    private  SecurityAbnormalPhotoGalleryActivity activity;
    private  ArrayList<String> cropIdLists;
    private  int currentPosition;

    public MySecurityAbnormalPhotoAdapter(SecurityAbnormalPhotoGalleryActivity activity, ArrayList<String> cropIdLists, int currentPosition ){
        super();
        this.activity=activity;
        this.cropIdLists=cropIdLists;
        this.currentPosition=currentPosition;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //   String url = photoPathLists.get(position);
        PhotoView photoView = new PhotoView(activity);
        if(cropIdLists.get(position) != null){
            String httpUrl = new StringBuffer().append(SkUrl.SkHttp(activity)).append("getSecurityImage.do").toString();
            //  Glide.with(activity).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).into(photoView);
            Glide.with(activity).load(httpUrl+"?n_image_id="+cropIdLists.get(position))
                    .placeholder(R.drawable.load_dail)// 占位图片
                    .into(photoView);
        }
        container.addView(photoView);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        return photoView;
    }

    @Override
    public int getCount() {
        return cropIdLists != null ? cropIdLists.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
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
