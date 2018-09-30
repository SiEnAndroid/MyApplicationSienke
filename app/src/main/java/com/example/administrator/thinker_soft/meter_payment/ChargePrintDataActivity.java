package com.example.administrator.thinker_soft.meter_payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/1.
 * 蓝牙打印
 */
public class ChargePrintDataActivity extends Activity {
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_connect_state)
    TextView tvConnectState;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.tv_user_number)
    TextView tvUserNumber;
    @BindView(R.id.tv_this_reading)
    TextView tvThisReading;
    @BindView(R.id.tv_last_reading)
    TextView tvLastReading;
    @BindView(R.id.tv_this_dosage)
    TextView tvThisDosage;
    @BindView(R.id.tv_total)
    TextView tvTotal;//应缴
    @BindView(R.id.tv_shiJiao)
    TextView tvShij;//实缴

    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_payment_time)
    TextView tvPaymentTime;
    @BindView(R.id.tv_operator)
    TextView tvOperator;
    @BindView(R.id.print_data)
    LinearLayout printData;
    @BindView(R.id.btn_print)
    Button btnPrint;
    @BindView(R.id.btn_command)
    Button btnCommand;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.tv_balance)
    TextView tvBalance;//余额
    private Context context = null;
    private SharedPreferences mySharedPreferences, sharedPreferences_login;
    private String userID = null;
    private int dosage;
    private Intent intent;
    private String realDosage, userNumber, userName, userAddress, price, total, month, startDegree, endDegree, chargeDate, meterReder, currentBalance, adjustDosage;
    private String paidAmount;//实缴金额
    private StringBuilder sb;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();
    private BluetoothDevice device = null;
    private static BluetoothSocket bluetoothSocket = null;
    private static OutputStream outputStream = null;
    private final UUID uuid = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private boolean isConnection = false;
    final String[] items = {"复位打印机", "标准ASCII字体", "压缩ASCII字体", "字体不放大",
            "宽高加倍", "取消加粗模式", "选择加粗模式", "取消倒置打印", "选择倒置打印", "取消黑白反显", "选择黑白反显",
            "取消顺时针旋转90°", "选择顺时针旋转90°"};
    final byte[][] byteCommands = {{0x1b, 0x40},// 复位打印机
            {0x1b, 0x4d, 0x00},// 标准ASCII字体
            {0x1b, 0x4d, 0x01},// 压缩ASCII字体
            {0x1d, 0x21, 0x00},// 字体不放大
            {0x1d, 0x21, 0x11},// 宽高加倍
            {0x1b, 0x45, 0x00},// 取消加粗模式
            {0x1b, 0x45, 0x01},// 选择加粗模式
            {0x1b, 0x7b, 0x00},// 取消倒置打印
            {0x1b, 0x7b, 0x01},// 选择倒置打印
            {0x1d, 0x42, 0x00},// 取消黑白反显
            {0x1d, 0x42, 0x01},// 选择黑白反显
            {0x1b, 0x56, 0x00},// 取消顺时针旋转90°
            {0x1b, 0x56, 0x01},// 选择顺时针旋转90°
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("蓝牙打印");
        this.setContentView(R.layout.activity_charge_print);
        ButterKnife.bind(this);
        this.context = this;
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        mySharedPreferences = context.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = sharedPreferences_login.getString("userId", "");
        meterReder = sharedPreferences_login.getString("user_name", "");
        Log.i("chargePrint.user_id", "获取到的ID是" + userID);
        initData();
    }


    /**
     * 获得从上一个Activity传来的蓝牙地址
     *
     * @return String
     */
    private String getDeviceAddress() {
        // 直接通过Context类的getIntent()即可获取Intent
        intent = getIntent();
        // 判断
        if (intent != null) {
            Log.i("ChargePrintActivity", "getDeviceAddress: " + intent.getStringExtra("deviceAddress"));
            return intent.getStringExtra("deviceAddress");
        } else {
            return null;
        }
    }

    //查询
    private void initData() {
        intent = getIntent();
        userNumber = intent.getStringExtra("userNum");
        userName = intent.getStringExtra("userName");
        userAddress = intent.getStringExtra("userAddress");
        price = intent.getStringExtra("price");
        total = intent.getStringExtra("total");
        month = intent.getStringExtra("month");
        startDegree = intent.getStringExtra("start_degree");
        endDegree = intent.getStringExtra("end_degree");
        chargeDate = intent.getStringExtra("d_charge_date");
        device = bluetoothAdapter.getRemoteDevice(getDeviceAddress());
        currentBalance = intent.getStringExtra("current_balance");//余额
        adjustDosage = intent.getStringExtra("adjust_dosage");
        realDosage = intent.getStringExtra("real_dosage");
        paidAmount=intent.getStringExtra("in_money");//实缴金额
        Log.i("ChargePrintActivity", "getDeviceName: " + device.getName());
        String deviceName = device.getName();
        // 设置当前设备名称
        tvDeviceName.setText(deviceName);
        // 一上来就先连接蓝牙设备
        boolean flag = connect();
        if (flag == false) {
            // 连接失败
            tvConnectState.setText("连接失败！");
        } else {
            // 连接成功
            tvConnectState.setText("连接成功！");

        }
        tvUserName.setText(userName);
        tvUserAddress.setText(userAddress);
        tvUserNumber.setText(userNumber);
        tvLastReading.setText(startDegree);
        tvThisReading.setText(endDegree);
        tvThisDosage.setText(realDosage);
        tvTotal.setText(total);
        tvMonth.setText(month);
        tvPaymentTime.setText(chargeDate);
        tvOperator.setText(meterReder);
        tvBalance.setText(currentBalance + "");
        tvShij.setText(paidAmount);
        sb = new StringBuilder();
        sb.append("用户姓名:         ").append(userName).append("\n")
                .append("地址:             ").append(userAddress).append("\n")
                .append("用户编号:         ").append(userNumber).append("\n")
                .append("上月读数:         ").append(startDegree).append("\n")
                .append("本月读数:         ").append(endDegree).append("\n")
                .append("本月实际用量:     ").append(realDosage).append("\n")
                .append(adjustDosage.equals("0.0") ? "缴费金额:         " : "缴费金额(调整):   ").append(total).append("\n")
                .append("实缴金额:         ").append(paidAmount).append("\n")
                .append("缴费月份:         ").append(month).append("\n")
                .append("用户余额:         ").append(currentBalance).append("\n")
                .append("缴费时间:         ").append(chargeDate).append("\n")
                .append("操作员:         ").append(meterReder).append("\n").append("\n").append("\n");

    }

    @Override
    protected void onDestroy() {
        disconnect();
        super.onDestroy();
    }

    @OnClick({R.id.btn_print, R.id.btn_command, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_print:
                send(sb.toString());
                break;
            case R.id.btn_command:
                selectCommand();
                break;
            case R.id.btn_back:
                startActivity(new Intent(ChargePrintDataActivity.this, MeterPaymentActivity.class));
                finish();
                break;
        }
    }

    //    public static class PrintDataService {
//    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
//            .getDefaultAdapter();
//    private BluetoothDevice device = null;
//    private static BluetoothSocket bluetoothSocket = null;
//    private static OutputStream outputStream = null;
//    private final UUID uuid = UUID
//            .fromString("00001101-0000-1000-8000-00805F9B34FB");
//    private boolean isConnection = false;
//    final String[] items = {"复位打印机", "标准ASCII字体", "压缩ASCII字体", "字体不放大",
//            "宽高加倍", "取消加粗模式", "选择加粗模式", "取消倒置打印", "选择倒置打印", "取消黑白反显", "选择黑白反显",
//            "取消顺时针旋转90°", "选择顺时针旋转90°"};
//    final byte[][] byteCommands = {{0x1b, 0x40},// 复位打印机
//            {0x1b, 0x4d, 0x00},// 标准ASCII字体
//            {0x1b, 0x4d, 0x01},// 压缩ASCII字体
//            {0x1d, 0x21, 0x00},// 字体不放大
//            {0x1d, 0x21, 0x11},// 宽高加倍
//            {0x1b, 0x45, 0x00},// 取消加粗模式
//            {0x1b, 0x45, 0x01},// 选择加粗模式
//            {0x1b, 0x7b, 0x00},// 取消倒置打印
//            {0x1b, 0x7b, 0x01},// 选择倒置打印
//            {0x1d, 0x42, 0x00},// 取消黑白反显
//            {0x1d, 0x42, 0x01},// 选择黑白反显
//            {0x1b, 0x56, 0x00},// 取消顺时针旋转90°
//            {0x1b, 0x56, 0x01},// 选择顺时针旋转90°
//    };

//    public PrintDataService(Context context, String deviceAddress) {
//        super();
//        this.context = context;
//        this.deviceAddress = deviceAddress;
//        this.device = this.bluetoothAdapter.getRemoteDevice(this.deviceAddress);
//    }

    /**
     * 获取设备名称
     *
     * @return String
     */
    public String getDeviceName() {
        return this.device.getName();
    }

    /**
     * 连接蓝牙设备
     */
    public boolean connect() {
        if (!this.isConnection) {
            try {
                bluetoothSocket = this.device
                        .createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                this.isConnection = true;
                if (this.bluetoothAdapter.isDiscovering()) {
                    System.out.println("关闭适配器！");
                    this.bluetoothAdapter.isDiscovering();
                }
            } catch (Exception e) {
                Toast.makeText(this.context, "连接失败！", Toast.LENGTH_SHORT).show();
                return false;
            }
            Toast.makeText(this.context, this.device.getName() + "连接成功！",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return true;
        }
    }

    /**
     * 断开蓝牙设备连接
     */
    public static void disconnect() {
        System.out.println("断开蓝牙设备连接");
        try {
            bluetoothSocket.close();
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 选择指令
     */
    public void selectCommand() {
        new AlertDialog.Builder(context).setTitle("请选择指令")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            outputStream.write(byteCommands[which]);
                        } catch (IOException e) {
                            Toast.makeText(context, "设置指令失败！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create().show();
    }

    /**
     * 发送数据
     */
    public void send(String sendData) {
        if (this.isConnection) {
            System.out.println("开始打印！！");
            try {
                byte[] data = sendData.getBytes("gbk");
                outputStream.write(data, 0, data.length);
                outputStream.flush();
            } catch (IOException e) {
                Toast.makeText(this.context, "发送失败！", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(this.context, "设备未连接，请重新连接！", Toast.LENGTH_SHORT)
                    .show();
        }
    }
//    }
}
