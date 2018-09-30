package com.example.administrator.thinker_soft.mobile_business.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.BusinessCommunityActivity;
import com.example.administrator.thinker_soft.mobile_business.BusinessMessageActivity;
import com.example.administrator.thinker_soft.mobile_business.BusinessNewsActivity;

/**
 * Created by Administrator on 2017/6/9.
 */
public class PublicMessageFragment extends Fragment {
    private View view;
    private TextView news,message,community;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_public_message, null);

        bindView(); //绑定控件ID
        setViewClickListener();//点击事件

        return view;
    }

    //绑定控件ID
    public void bindView(){
        news = (TextView) view.findViewById(R.id.news);
        message = (TextView) view.findViewById(R.id.message);
        community = (TextView) view.findViewById(R.id.community);
    }

    //点击事件
    public void setViewClickListener(){
        news.setOnClickListener(clickListener);
        message.setOnClickListener(clickListener);
        community.setOnClickListener(clickListener);
    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.news:
                    Intent intent = new Intent(getActivity(), BusinessNewsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.message:
                    Intent intent1 = new Intent(getActivity(), BusinessMessageActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.community:
                    Intent intent2 = new Intent(getActivity(), BusinessCommunityActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    };
}
