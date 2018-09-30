package com.example.administrator.thinker_soft.meter_code.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.BluetoothActivity;

/**
 * Created by Administrator on 2017/8/1.
 */
public class BluetoothAction implements View.OnClickListener {
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    private CheckBox switchBT = null;
    private TextView textView = null;
    private Button searchDevices = null;
    private Activity activity = null;

    private ListView unbondDevices = null;
    private ListView bondDevices = null;
    private Context context = null;
    private BluetoothActivity.BluetoothService bluetoothService = null;

    public BluetoothAction(Context context, ListView unbondDevices,
                           ListView bondDevices, CheckBox switchBT, TextView textView, Button searchDevices,
                           Activity activity) {
        super();
        this.context = context;
        this.unbondDevices = unbondDevices;
        this.bondDevices = bondDevices;
        this.switchBT = switchBT;
        this.textView = textView;
        this.searchDevices = searchDevices;
        this.activity = activity;
        this.bluetoothService = new BluetoothActivity.BluetoothService(this.context,
                this.unbondDevices, this.bondDevices, this.textView,
        this.switchBT, this.searchDevices);



    }

    public void setSwitchBT(CheckBox switchBT) {
        this.switchBT = switchBT;
    }

    public void setSearchDevices(Button searchDevices) {
        this.searchDevices = searchDevices;
    }
    public void setTextView(TextView textView){
        this.textView =textView;
    }

    public void setUnbondDevices(ListView unbondDevices) {
        this.unbondDevices = unbondDevices;
    }

    /**
     * 初始化界面
     */
    public void initView() {

        if (this.bluetoothService.isOpen()) {
            System.out.println("蓝牙有开!");
            /*textView.setText("关闭蓝牙");*/
            switchBT.setChecked(true);
            /*this.searchDevices();  */ //按理说这里应该会执行，这里就是搜索蓝牙列表的代码，你打Log看

        }
        if (!this.bluetoothService.isOpen()) {
            System.out.println("蓝牙没开!");
            /*textView.setText("打开蓝牙");*/
            this.searchDevices.setEnabled(false);
            switchBT.setChecked(false);
        }

    }

    private void searchDevices() {
        bluetoothService.searchDevices();
    }

    /**
     * 各种按钮的监听
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.searchDevices) {   //这里是点击搜索按钮，然后去搜索
            this.searchDevices();
        } else if (v.getId() == R.id.return_Bluetooth_btn) {
            activity.finish();
        } else if (v.getId() == R.id.openBluetooth_tb) {
            if (!this.bluetoothService.isOpen()) {
                // 蓝牙关闭的情况
                System.out.println("蓝牙关闭的情况");
                this.bluetoothService.openBluetooth(activity);
            } else {
                // 蓝牙打开的情况
                System.out.println("蓝牙打开的情况");
                this.bluetoothService.closeBluetooth();
            }
        }
    }
}
