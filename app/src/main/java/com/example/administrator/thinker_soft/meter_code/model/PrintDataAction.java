package com.example.administrator.thinker_soft.meter_code.model;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.PrintDataActivity;

/**
 * Created by Administrator on 2017/8/1.
 */
public class PrintDataAction implements View.OnClickListener {
    private Context context = null;
    private TextView deviceName = null;
    private TextView connectState = null;
    private LinearLayout printData;
    private String deviceAddress = null;
    private PrintDataActivity.PrintDataService printDataService = null;
    private String printDataString;

    public PrintDataAction(Context context, String deviceAddress,  TextView deviceName, TextView connectState,String printData) {
        super();
        this.context = context;
        this.deviceAddress = deviceAddress;
        this.deviceName = deviceName;
        this.connectState = connectState;
        this.printDataString = printData;

        /*this.UserName = UserName;*/
        this.printDataService = new PrintDataActivity.PrintDataService(this.context,
                this.deviceAddress);
        this.initView();
    }

    private void initView() {
        // 设置当前设备名称
        this.deviceName.setText(this.printDataService.getDeviceName());
        // 一上来就先连接蓝牙设备
        boolean flag = this.printDataService.connect();
        if (flag == false) {
            // 连接失败
            this.connectState.setText("连接失败！");
        } else {
            // 连接成功
            this.connectState.setText("连接成功！");

        }
    }

    public void setPrintData(LinearLayout printData) {
        this.printData = printData;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send) {
            /*String sendData = this.printData.getText().toString();
            this.printDataService.send(printData.toString());*/
            this.printDataService.send(printDataString);
        } else if (v.getId() == R.id.command) {
            this.printDataService.selectCommand();

        }
    }
}
