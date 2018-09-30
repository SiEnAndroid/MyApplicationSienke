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
import com.example.administrator.thinker_soft.mobile_business.BusinessEmailActivity;
import com.example.administrator.thinker_soft.mobile_business.BusinessNetPhoneBookActivity;

/**
 * Created by Administrator on 2017/6/9.
 */
public class NetFragment extends Fragment {
    private View view;
    private TextView email;
    private TextView phoneBook;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_network, null);

        bindView(); //绑定控件ID
        setViewClickListener();//点击事件

        return view;
    }

    //绑定控件ID
    public void bindView(){
        email = (TextView) view.findViewById(R.id.email);
        phoneBook = (TextView) view.findViewById(R.id.phone_book);



    }

    //点击事件
    public void setViewClickListener(){
        email.setOnClickListener(clickListener);
        phoneBook.setOnClickListener(clickListener);
    }
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.email:
                    Intent intent = new Intent(getActivity(), BusinessEmailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.phone_book:
                    Intent intent1 = new Intent(getActivity(), BusinessNetPhoneBookActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    };
}
