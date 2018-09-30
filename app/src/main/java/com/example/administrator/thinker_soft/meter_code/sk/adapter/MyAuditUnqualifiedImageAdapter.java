package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.ui.AuditUnqualifiedImageActivity;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

/**
 * @author 111
 */
public class MyAuditUnqualifiedImageAdapter extends PagerAdapter {

    private  AuditUnqualifiedImageActivity context;
    private  ArrayList<String> cropIdLists;
    private  int currentPosition;

    public MyAuditUnqualifiedImageAdapter(AuditUnqualifiedImageActivity context, ArrayList<String> cropIdLists, int currentPosition) {
        super();
        this.context=context;
        this.cropIdLists=cropIdLists;
        this.currentPosition=currentPosition;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //   String url = photoPathLists.get(position);
        PhotoView photoView = new PhotoView(context);
        if(cropIdLists.get(position) != null){
            String httpUrl = new StringBuffer().append(SkUrl.SkHttp(context)).append("getSecurityImage.do").toString();
            //  Glide.with(activity).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).into(photoView);
            Glide.with(context).load(httpUrl+"?n_image_id="+cropIdLists.get(position))
                    .placeholder(R.drawable.load_dail)// 占位图片
                    .into(photoView);
        }
        container.addView(photoView);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
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
