package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.PrintDataAction;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PrintUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SharedPreferencesHelper;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/1.
 */

public class PrintDataActivity extends Activity {
    private Context context = null;
    private SharedPreferences mySharedPreferences, sharedPreferences_login;
    private Cursor cursor;
    private SQLiteDatabase db;  //数据库
    private String userID = null;
    private String userName = null;
    private String userAdress = null;
    private String userNumber = null;
    private String lastReading = null;
    private String lastdosage = null;
    private String thisReading = null;
    private String thisdosage = null;
    private String money = null;
    private String months = null;
    private StringBuilder sb;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //荥经
        if (SharedPreferencesHelper.getFirm(PrintDataActivity.this).equals("荥经")) {
            this.setContentView(R.layout.printdata_layout_yingjing);
        }else {
            this.setContentView(R.layout.printdata_layout);
        }

        this.setTitle("蓝牙打印");
        this.context = this;
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        mySharedPreferences = context.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = mySharedPreferences.getString("user_id", "");
        Log.i("user_id", "获取到的ID是" + userID);
        MySqliteHelper helper = new MySqliteHelper(this.context, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        getUserId();
        initListener();
    }


    /**
     * 获得从上一个Activity传来的蓝牙地址
     *
     * @return String
     */
    private String getDeviceAddress() {
        // 直接通过Context类的getIntent()即可获取Intent
        Intent intent = this.getIntent();
        // 判断
        if (intent != null) {
            return intent.getStringExtra("deviceAddress");
        } else {
            return null;
        }
    }

    //查询
    private void getUserId() {
        cursor = db.rawQuery("select * from MeterUser where login_user_id=? and user_id=?", new String[]{sharedPreferences_login.getString("userId", ""), userID});
        Log.i("cursor", cursor.getCount() + "长度为");
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            userName = cursor.getString(cursor.getColumnIndex("user_name"));
            userAdress = cursor.getString(cursor.getColumnIndex("user_address"));
            lastReading = cursor.getString(cursor.getColumnIndex("meter_degrees"));
            lastdosage = cursor.getString(cursor.getColumnIndex("last_month_dosage"));
            thisReading = cursor.getString(cursor.getColumnIndex("this_month_end_degree"));
            thisdosage = cursor.getString(cursor.getColumnIndex("this_month_dosage"));
            //金额
            money = cursor.getString(cursor.getColumnIndex("arrearage_amount"));
            //月数
            months = cursor.getString(cursor.getColumnIndex("arrearage_months"));
        }
    }

    private void initListener() {
        TextView deviceName = (TextView) this.findViewById(R.id.device_name);
        TextView connectState = (TextView) this
                .findViewById(R.id.connect_state);
        TextView UserName = (TextView) this.findViewById(R.id.user_name);
        TextView UserAdress = (TextView) this.findViewById(R.id.user_address);
        TextView UserNumber = (TextView) this.findViewById(R.id.user_number);
        TextView LastReading = (TextView) this.findViewById(R.id.last_reading);
        TextView Lastdosage = (TextView) this.findViewById(R.id.last_dosage);
        TextView ThisReading = (TextView) this.findViewById(R.id.this_reading);
        TextView Thisdosage = (TextView) this.findViewById(R.id.this_dosage);
        TextView Money = (TextView) this.findViewById(R.id.money);
        TextView Months = (TextView) this.findViewById(R.id.months);

        UserName.setText(userName);
        UserAdress.setText(userAdress);
        UserNumber.setText(userID);
        LastReading.setText(lastReading);
        Lastdosage.setText(lastdosage);
        ThisReading.setText(thisReading);
        Thisdosage.setText(thisdosage);
        Money.setText(money);
        Months.setText(months);
        //荥经
        if (SharedPreferencesHelper.getFirm(context).equals("荥经")){
            sb = new StringBuilder();
            sb.append(PrintUtils.printTitle("荥经县国润供水有限责任公司")).append("\n")
                    .append(PrintUtils.printTitle("用户催缴单")).append("\n\n\n\n")
                    .append("----------------------------").append("\n")
                    .append("用户姓名:         ").append(userName).append("\n")
                    .append("地址:             ").append(userAdress).append("\n")
                    .append("用户编号:         ").append(userID).append("\n")
                    .append("上月读数:         ").append(lastReading).append("\n")
                    .append("本月读数:         ").append(thisReading).append("\n")
                    .append("上月用量:         ").append(lastdosage).append("\n")
                    .append("本月用量:         ").append(thisdosage).append("\n")
                    .append("欠费金额:         ").append(money).append("\n")
                    .append("欠费月数:         ").append(months).append("\n")
                    .append("联系电话:         ").append("0835-7623282").append("\n")
                    .append("----------------------------").append("\n\n\n\n");
            db.close();
        }else {
            //默认
            sb = new StringBuilder();
            sb.append("用户姓名:         ").append(userName).append("\n")
                    .append("地址:             ").append(userAdress).append("\n")
                    .append("用户编号:         ").append(userID).append("\n")
                    .append("上月读数:         ").append(lastReading).append("\n")
                    .append("上月用量:         ").append(lastdosage).append("\n")
                    .append("本月读数:         ").append(thisReading).append("\n")
                    .append("本月用量:         ").append(thisdosage).append("\n")
                    .append("欠费金额:         ").append(money).append("\n")
                    .append("欠费月数:         ").append(months).append("\n").append("\n").append("\n").append("\n");
            db.close();

        }


        PrintDataAction printDataAction = new PrintDataAction(this.context,
                this.getDeviceAddress(), deviceName, connectState, sb.toString());

        LinearLayout printData = (LinearLayout) this.findViewById(R.id.print_data);
        Button send = (Button) this.findViewById(R.id.send);
        Button command = (Button) this.findViewById(R.id.command);
        Button back = (Button) this.findViewById(R.id.back);
        printDataAction.setPrintData(printData);

        send.setOnClickListener(printDataAction);
        command.setOnClickListener(printDataAction);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.back:
                        finish();
                        break;
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        PrintDataService.disconnect();
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }

    public static class PrintDataService {
        private Context context = null;
        private String deviceAddress = null;
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

        public PrintDataService(Context context, String deviceAddress) {
            super();
            this.context = context;
            this.deviceAddress = deviceAddress;
            this.device = this.bluetoothAdapter.getRemoteDevice(this.deviceAddress);
        }

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
                                if (isConnection) {
                                    outputStream.write(byteCommands[which]);
                                }
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
    }
}
