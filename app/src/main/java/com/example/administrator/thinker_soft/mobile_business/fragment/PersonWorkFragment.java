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
import com.example.administrator.thinker_soft.mobile_business.BusinessCheckingInActivity;
import com.example.administrator.thinker_soft.mobile_business.BusinessFlowActivity;
import com.example.administrator.thinker_soft.mobile_business.BusinessPersonSettingActivity;
import com.example.administrator.thinker_soft.mobile_business.BusinessScheduleActivity;
import com.example.administrator.thinker_soft.mobile_business.BusinessWorkReportActivity;

/**
 * Created by Administrator on 2017/6/9.
 */
public class PersonWorkFragment extends Fragment {

    private View view;
    private TextView schedule, personSet, workReport, GPS, examine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person_work, null);
        bindView(); //绑定控件ID
        setViewClickListener();//点击事件
        return view;
    }

    //绑定控件ID
    public void bindView() {
        schedule = (TextView) view.findViewById(R.id.schedule);
        personSet = (TextView) view.findViewById(R.id.person_set);
        workReport = (TextView) view.findViewById(R.id.work_report);
        GPS = (TextView) view.findViewById(R.id.GPS);
        examine = (TextView) view.findViewById(R.id.examine);
    }

    //点击事件
    public void setViewClickListener() {
        schedule.setOnClickListener(clickListener);
        personSet.setOnClickListener(clickListener);
        workReport.setOnClickListener(clickListener);
        GPS.setOnClickListener(clickListener);
        examine.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.schedule:
                    Intent intent = new Intent(getActivity(), BusinessScheduleActivity.class);
                    startActivity(intent);
                    break;
                case R.id.person_set:

                    Intent intent1 = new Intent(getActivity(), BusinessPersonSettingActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.work_report:
                    Intent intent2 = new Intent(getActivity(), BusinessWorkReportActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.GPS:
                    Intent intent3 = new Intent(getActivity(), BusinessCheckingInActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.examine:
                    Intent intent4 = new Intent(getActivity(), BusinessFlowActivity.class);
                    startActivity(intent4);
                    break;
            }
        }
    };
}
