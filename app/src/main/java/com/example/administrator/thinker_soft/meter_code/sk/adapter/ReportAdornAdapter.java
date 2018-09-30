package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportAdornBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Administrator on 2018/8/1.
 */

public class ReportAdornAdapter extends RecyclerArrayAdapter<ReportAdornBean> {

    public ReportAdornAdapter(Context context) {
        super(context);

    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReportAdornViewHolder(parent);
    }

    public class ReportAdornViewHolder extends BaseViewHolder<ReportAdornBean> {

        /**
         * 姓名
         */
        private TextView userName;
        /**
         * 业务名称
         */
        private TextView tv_type;
        private TextView tv_userId;
        private TextView tv_ywType;
        private TextView tv_qbNumber;
        private TextView tv_time;
        private TextView tv_qx;
        private CardView cv_adorn;
        private View view_lien;
        private TextView tv_blType;

        public ReportAdornViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_report_adorn);
            userName = $(R.id.tv_userName);
            tv_type = $(R.id.tv_type);
            tv_userId = $(R.id.tv_userId);
            tv_ywType = $(R.id.tv_ywType);
            tv_qbNumber = $(R.id.tv_qbNumber);
            tv_time = $(R.id.tv_time);
            tv_qx = $(R.id.tv_qx);
            cv_adorn = $(R.id.cv_adorn);
            view_lien = $(R.id.view_lien);
            tv_blType = $(R.id.tv_blType);
            

        }
        /**
         * TRANID : 26723.0
         * OPID : 1.0
         * BID : 1115.0
         * N_AUDIT_TYPE : 1.0
         * NODENAME : 库房出料（定额）
         * C_COMPANY_REMARK : 重庆渝山
         * N_COMPANY_ID : 1.0
         * BUSINESS : 思恩科夏登栖测试
         * C_USER_NAME : SUPER
         * PROCESS : 报装流程
         * TRAN_STATE : 未办理
         * DUE_STATE : 未超期
         * D_TRANSACTION_TIME_BEGIN : 2018-08-29T08:54:29
         * N_QUEUE_ID : 354.0
         * N_PROCESS_ID : 4.0
         */
        @Override
        public void setData(ReportAdornBean item) {
            super.setData(item);
            userName.setText(item.getBUSINESS());
            tv_type.setText(item.getNODENAME());
            tv_userId.setText(String.valueOf((int)item.getBID()));
            tv_ywType.setText(item.getPROCESS());
            tv_qbNumber.setText(item.getC_USER_NAME());
            String time=item.getD_TRANSACTION_TIME_DUE();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = formatter.format(new Date());

            if(time.indexOf("T") != -1) {
                String cTime = time.replace("T","  ");
                String timeExpend = getTimeExpend(str,cTime);
                tv_time.setText(timeExpend);
            }else {
                String timeExpend = getTimeExpend(str,time);
                tv_time.setText(timeExpend);
            }
            if (item.getTRAN_STATE()!=null&&item.getTRAN_STATE().equals("已办理")){
                cv_adorn.setCardBackgroundColor(Color.WHITE);
                view_lien.setBackgroundColor(Color.parseColor("#808080"));
            }else {
                cv_adorn.setCardBackgroundColor(Color.parseColor("#F4F0E5"));
                view_lien.setBackgroundColor(Color.parseColor("#efeff4"));
            }
            tv_blType.setText(item.getTRAN_STATE());
            if ("已超期".equals(item.getDUE_STATE())){
                tv_qx.setText(item.getDUE_STATE());
                tv_qx.setTextColor(getContext().getResources().getColor(R.color.red));
            }else if ("未超期".equals(item.getDUE_STATE())){
                tv_qx.setText(item.getDUE_STATE());
                tv_qx.setTextColor(getContext().getResources().getColor(R.color.green01));
            }

        }
    }

    private String getTimeExpend(String startTime, String endTime){
        //传入字串类型 2016/06/28 08:30
        long longStart = getTimeMillis(startTime); //获取开始时间毫秒数
        long longEnd = getTimeMillis(endTime);  //获取结束时间毫秒数
        long longExpend = longEnd - longStart;  //获取时间差
        long longHours = longExpend / (60 * 60 * 1000); //根据时间差来计算小时数
        long longMinutes = (longExpend - longHours * (60 * 60 * 1000)) / (60 * 1000);   //根据时间差来计算分钟数

//        1~24个小时显示   几小时
//        一小时一下显示(包括负数) 不足一小时
//        一天以上 显示  一天以上
        if (longHours>1&&24>longHours){
            return longHours + "小时" ;
        }else if (longHours<1){
            return  "不足一小时" ;
        }else if (longHours>=24){
            return  "一天以上" ;
        }
        return "";
    }


    private long getTimeMillis(String strTime) {
        long returnMillis = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = null;
        try {
            d = sdf.parse(strTime);
            returnMillis = d.getTime();
        } catch (ParseException e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        return returnMillis;
    }
}
