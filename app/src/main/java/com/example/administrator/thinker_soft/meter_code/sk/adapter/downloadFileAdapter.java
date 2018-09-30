package com.example.administrator.thinker_soft.meter_code.sk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.bean.DownloadFileBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.ReportAdornBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 111
 */
public class downloadFileAdapter extends RecyclerArrayAdapter<DownloadFileBean> {



    public downloadFileAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new DownloadFileHolder(parent);
    }

    @Override
    public void OnBindViewHolder(BaseViewHolder holder, final int position) {
        super.OnBindViewHolder(holder, position);
        DownloadFileHolder holder1 = (DownloadFileHolder) holder;
        if (onDownloadClickListener != null) {
            holder1.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDownloadClickListener.onDownloadClick(v, position);
                }
            });
        }
        if (onDeleteClickListener != null) {
            holder1.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClickListener.onDeleteClick(v, position);
                }
            });
        }

    }
    public class DownloadFileHolder extends BaseViewHolder<DownloadFileBean> {
        private TextView fileName;
        private TextView fileOperationTime;
        private TextView userName;
        private TextView nodeName;
        private TextView processName;
        private TextView businessName;
        private TextView fileRemark;
        private Button download;
        private Button delete;
        public DownloadFileHolder(ViewGroup parent) {
            super(parent, R.layout.item_download_file);
            fileName = $(R.id.c_file_name);
            fileOperationTime = $(R.id.d_file_operation_time);
            userName = $(R.id.c_user_name);
            nodeName = $(R.id.c_node_name);
            processName = $(R.id.c_process_name);
            businessName = $(R.id.c_business_name);
            fileRemark = $(R.id.c_file_remark);
            download = $(R.id.download);
            delete = $(R.id.delete);
        }
        /*
        * "N_FILE_ID":83,
        "C_PROCESS_NAME":"报装流程",
        "C_BUSINESS_NAME":"a",
        "C_NODE_NAME":"现场勘测(零星)",
        "C_FILE_NAME":"方根安检_二维码.png",
        "C_FILE_PATH":"D:/SMDemo/images/2018-09/20180927160643889方根安检_二维码.png",
        "C_USER_NAME":"SUPER",
        "D_FILE_OPERATION_TIME":"2018-09-27T16:06:45",
        "C_FILE_REMARK":"无"
        * */

        @Override
        public void setData(DownloadFileBean item) {
            super.setData(item);
            fileName.setText(item.getC_FILE_NAME());
            String time = item.getD_FILE_OPERATION_TIME();
            if(time.indexOf("T") != -1) {
                String[] ts = time.split("T");
                fileOperationTime.setText(ts[0]+" "+ts[1]);
            }else {
                fileOperationTime.setText(time);
            }
            userName.setText(item.getC_USER_NAME());
            nodeName.setText(item.getC_NODE_NAME());
            processName.setText(item.getC_PROCESS_NAME());
            businessName.setText(item.getC_BUSINESS_NAME());
            fileRemark.setText(item.getC_FILE_REMARK());
        }
    }
    private MeterDownloadClickListener onDownloadClickListener;
    private MeterDeleteClickListener onDeleteClickListener;

    public interface  MeterDownloadClickListener{
        void onDownloadClick(View view, int position);
    }
    public interface  MeterDeleteClickListener{
        void onDeleteClick(View view, int position);
    }

    public void setDownloadClickListener(MeterDownloadClickListener mDownloadClick) {
        this.onDownloadClickListener = mDownloadClick;
    }
    public void setDeleteClickListener(MeterDeleteClickListener mDeleteClick) {
        this.onDeleteClickListener = mDeleteClick;
    }


}
