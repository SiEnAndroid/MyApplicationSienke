package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.BluetoothAction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Administrator on 2017/8/1.
 */
public class BluetoothActivity extends Activity {
    private Context context = null;
    private SharedPreferences sharedPreferences_login, sharedPreferences;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setTitle("蓝牙打印");
        setContentView(R.layout.bluetooth_layout);
        this.initListener();
        defaultSetting();
        setViewClickListener();
    }


    private void initListener() {
        ListView unbondDevices = (ListView) this
                .findViewById(R.id.unbondDevices);
        ListView bondDevices = (ListView) this.findViewById(R.id.bondDevices);
        TextView textView = (TextView) this.findViewById(R.id.text);
        CheckBox switchBT = (CheckBox) this.findViewById(R.id.openBluetooth_tb);
        Button searchDevices = (Button) this.findViewById(R.id.searchDevices);

        BluetoothAction bluetoothAction = new BluetoothAction(this.context,
                unbondDevices, bondDevices, switchBT, textView, searchDevices,
                BluetoothActivity.this);

        ImageView returnButton = (ImageView) this
                .findViewById(R.id.return_Bluetooth_btn);
        bluetoothAction.setSearchDevices(searchDevices);
        bluetoothAction.initView();

        switchBT.setOnClickListener(bluetoothAction);
        searchDevices.setOnClickListener(bluetoothAction);
        returnButton.setOnClickListener(bluetoothAction);
    }

    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = BluetoothActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("openBluetooth_tb", true)) {

        }
    }

    private void setViewClickListener() {
    }
    /*//屏蔽返回键的代码:
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    public static class BluetoothService {
        private Context context = null;
        private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        private ArrayList<BluetoothDevice> unbondDevices = null; // 用于存放未配对蓝牙设备
        private ArrayList<BluetoothDevice> bondDevices = null;// 用于存放已配对蓝牙设备
        private TextView textView = null;
        private Button searchDevices = null;
        private CheckBox switchBT = null;
        private ListView unbondDevicesListView = null;
        private ListView bondDevicesListView = null;
        private boolean searchAgain = false;
        private Set<BluetoothDevice> bluetoothDeviceSet;

        /**
         * 添加已绑定蓝牙设备到ListView
         */
        private void addBondDevicesToListView() {
            ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
            int count = this.bondDevices.size();
            System.out.println("已绑定设备数量：" + count);
            for (int i = 0; i < count; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("deviceName", this.bondDevices.get(i).getName());
                data.add(map);// 把item项的数据加到data中
            }
            String[] from = {"deviceName"};
            int[] to = {R.id.device_name};
            SimpleAdapter simpleAdapter = new SimpleAdapter(this.context, data,
                    R.layout.bonddevice_item, from, to);
            // 把适配器装载到listView中
            this.bondDevicesListView.setAdapter(simpleAdapter);

            this.bondDevicesListView
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {
                            BluetoothDevice device = bondDevices.get(arg2);
                            //跳转蓝牙打印页面
                            Intent intent = new Intent(context, PrintDataActivity.class);
                          //  Intent intent = new Intent(context, PrintBluetActivity.class);
                            /*intent.setClassName(context,
                                    ".meter_code.PrintDataActivity");*/
                            intent.putExtra("deviceAddress", device.getAddress());
                            context.startActivity(intent);

                        }
                    });

        }

        /**
         * 添加未绑定蓝牙设备到ListView
         */
        private void addUnbondDevicesToListView() {
            ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
            int count = this.unbondDevices.size();
            System.out.println("未绑定设备数量：" + count);
            for (int i = 0; i < count; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("deviceName", this.unbondDevices.get(i).getName());
                data.add(map);// 把item项的数据加到data中
            }
            String[] from = {"deviceName"};
            int[] to = {R.id.device_name};
            SimpleAdapter simpleAdapter = new SimpleAdapter(this.context, data,
                    R.layout.unbonddevice_item, from, to);

            // 把适配器装载到listView中
            this.unbondDevicesListView.setAdapter(simpleAdapter);

            // 为每个item绑定监听，用于设备间的配对
            this.unbondDevicesListView
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {
                            try {
                                Method createBondMethod = BluetoothDevice.class
                                        .getMethod("createBond");
                                createBondMethod
                                        .invoke(unbondDevices.get(arg2));
                                // 将绑定好的设备添加的已绑定list集合
                                bondDevices.add(unbondDevices.get(arg2));
                                // 将绑定好的设备从未绑定list集合中移除
                                unbondDevices.remove(arg2);
                                addBondDevicesToListView();
                                addUnbondDevicesToListView();
                            } catch (Exception e) {
                                Toast.makeText(context, "配对失败！", Toast.LENGTH_SHORT)
                                        .show();
                            }

                        }
                    });
        }

        public BluetoothService(Context context, ListView unbondDevicesListView,
                                ListView bondDevicesListView, TextView textView, CheckBox switchBT, Button searchDevices) {
            this.context = context;
            this.unbondDevicesListView = unbondDevicesListView;
            this.bondDevicesListView = bondDevicesListView;
            /*this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();*/
            this.unbondDevices = new ArrayList<BluetoothDevice>();
            this.bondDevices = new ArrayList<BluetoothDevice>();
            this.textView = textView;
            this.switchBT = switchBT;
            this.searchDevices = searchDevices;
            this.initIntentFilter();
            if (bluetoothAdapter != null && isOpen()) {
                bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();
                bondDevices = new ArrayList<>(bluetoothDeviceSet);
                addBondDevicesToListView();
            }

        }

        private void initIntentFilter() {
            // 设置广播信息过滤
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            // 注册广播接收器，接收并处理搜索结果
            context.registerReceiver(receiver, intentFilter);

        }

        /**
         * 打开蓝牙
         */
        public void openBluetooth(Activity activity) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, 1);

        }

        /**
         * 关闭蓝牙
         */
        public void closeBluetooth() {
            this.bluetoothAdapter.disable();
        }

        /**
         * 判断蓝牙是否打开
         *
         * @return boolean
         */
        public boolean isOpen() {
            return this.bluetoothAdapter.isEnabled();

        }

        /**
         * 搜索蓝牙设备
         */
        public void searchDevices() {
            searchAgain = true;
            this.bondDevices.clear();
            this.unbondDevices.clear();

            // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
            this.bluetoothAdapter.startDiscovery();

        }

        /**
         * 添加未绑定蓝牙设备到list集合
         *
         * @param device
         */
        public void addUnbondDevices(BluetoothDevice device) {
            System.out.println("未绑定设备名称：" + device.getName());
            if (!this.unbondDevices.contains(device)) {
                this.unbondDevices.add(device);
            }
        }

        /**
         * 添加已绑定蓝牙设备到list集合
         *
         * @param device
         */
        public void addBandDevices(BluetoothDevice device) {
            System.out.println("已绑定设备名称：" + device.getName());
            if (!this.bondDevices.contains(device)) {
                this.bondDevices.add(device);
            }
        }

        /**
         * 蓝牙广播接收器
         */
        private BroadcastReceiver receiver = new BroadcastReceiver() {

            ProgressDialog progressDialog = null;

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent
                            .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
//                        addBandDevices(device);
                    } else {
                        addUnbondDevices(device);
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    if (searchAgain) {
                        progressDialog = ProgressDialog.show(context, "请稍等...",
                                "搜索蓝牙设备中...", true);
                        bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();
                        bondDevices = new ArrayList<>(bluetoothDeviceSet);
                        addBondDevicesToListView();
                    } else {
                        bluetoothAdapter.cancelDiscovery();
                    }

                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                        .equals(action)) {
                    System.out.println("设备搜索完毕");
                    if (progressDialog!=null) {
                        progressDialog.dismiss();
                    }

                    addUnbondDevicesToListView();
//                    addBondDevicesToListView();
                    // bluetoothAdapter.cancelDiscovery();
                }
                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                        System.out.println("--------关闭蓝牙-----------");
                        /*textView.setText("打开蓝牙");*/
                        searchDevices.setEnabled(true);
                        bondDevicesListView.setEnabled(true);
                        unbondDevicesListView.setEnabled(true);

                    } else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                        System.out.println("--------打开蓝牙-----------");
                        /*textView.setText("关闭蓝牙");*/
                        searchDevices.setEnabled(false);
                        bondDevicesListView.setEnabled(false);
                        unbondDevicesListView.setEnabled(false);
                    }
                }

            }

        };

    }
}
