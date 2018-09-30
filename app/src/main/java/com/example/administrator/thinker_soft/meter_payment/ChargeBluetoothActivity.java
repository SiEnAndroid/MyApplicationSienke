package com.example.administrator.thinker_soft.meter_payment;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/1.
 * 在线抄表蓝牙打印小票
 */
public class ChargeBluetoothActivity extends Activity {
    @BindView(R.id.return_Bluetooth_btn)
    ImageView returnBluetoothBtn;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.openBluetooth_tb)
    CheckBox openBluetoothTb;
    @BindView(R.id.searchDevices)
    Button searchDevices;
    @BindView(R.id.unbondDevices)
    ListView lv_unbondDevices;
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @BindView(R.id.bondDevices)
    ListView lv_bondDevices;
    private Context context = null;
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();
    private Set<BluetoothDevice> bluetoothDeviceSet;
    private ArrayList<BluetoothDevice> unbondDevices = new ArrayList<>(); // 用于存放未配对蓝牙设备
    private ArrayList<BluetoothDevice> bondDevices = new ArrayList<>();// 用于存放已配对蓝牙设备
    private Intent intent;
    private String realDosage, userNumber, userName, userAddress, price, total, month, startDegree, endDegree, chargeDate, meterReder, currentBalance, adjustDosage;
  private String paidAmount;//实缴金额
    private boolean searchAgain = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setTitle("蓝牙打印");
        setContentView(R.layout.bluetooth_layout);
        ButterKnife.bind(this);
        defaultSetting();
        initView();
        initIntentFilter();
        if (bluetoothAdapter != null && isOpen()) {
            bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();
            bondDevices = new ArrayList<>(bluetoothDeviceSet);
            addBondDevicesToListView();
        }
    }

    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = ChargeBluetoothActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        intent = getIntent();
        userNumber = intent.getStringExtra("userNum");
        userName = intent.getStringExtra("userName");
        userAddress = intent.getStringExtra("userAddress");
        price = intent.getStringExtra("price");
        total = intent.getStringExtra("total");
        paidAmount=intent.getStringExtra("in_money");//实缴金额
        month = intent.getStringExtra("month");
        startDegree = intent.getStringExtra("start_degree");
        endDegree = intent.getStringExtra("end_degree");
        chargeDate = intent.getStringExtra("d_charge_date");
        meterReder = intent.getStringExtra("meter_reder");
        realDosage = intent.getStringExtra("real_dosage");
        currentBalance = intent.getStringExtra("current_balance");//余额
        adjustDosage = intent.getStringExtra("adjust_dosage");
        if (sharedPreferences.getBoolean("openBluetooth_tb", true)) {

        }
    }

    @OnClick({R.id.return_Bluetooth_btn, R.id.searchDevices, R.id.openBluetooth_tb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.return_Bluetooth_btn:
                finish();
                break;
            case R.id.searchDevices:
                searchDevices();
                break;
            case R.id.openBluetooth_tb:
                if (!isOpen()) {
                    // 蓝牙关闭的情况
                    System.out.println("蓝牙关闭的情况");
                    openBluetooth(this);
                } else {
                    // 蓝牙打开的情况
                    System.out.println("蓝牙打开的情况");
                    closeBluetooth();
                }
                break;
        }
    }

    /**
     * 初始化界面
     */
    public void initView() {

        if (isOpen()) {
            System.out.println("蓝牙有开!");
            /*textView.setText("关闭蓝牙");*/
            openBluetoothTb.setChecked(true);
            /*this.searchDevices();  */ //按理说这里应该会执行，这里就是搜索蓝牙列表的代码，你打Log看

        }
        if (!isOpen()) {
            System.out.println("蓝牙没开!");
            /*textView.setText("打开蓝牙");*/
            this.searchDevices.setEnabled(false);
            openBluetoothTb.setChecked(false);
        }

    }
    /*//屏蔽返回键的代码:
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/


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
        final int[] to = {R.id.device_name};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this.context, data,
                R.layout.bonddevice_item, from, to);
        // 把适配器装载到listView中
        lv_bondDevices.setAdapter(simpleAdapter);

        lv_bondDevices
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        BluetoothDevice device = bondDevices.get(arg2);
                        Intent intent = new Intent(context, ChargePrintDataActivity.class);
                            /*intent.setClassName(context,
                                    ".meter_code.PrintDataActivity");*/
                        intent.putExtra("userNum", userNumber);
                        intent.putExtra("userName", userName);
                        intent.putExtra("userAddress", userAddress);
                        intent.putExtra("price", price);
                        intent.putExtra("total", total);
                        intent.putExtra("month", month);
                        intent.putExtra("start_degree", startDegree);
                        intent.putExtra("end_degree", endDegree);
                        intent.putExtra("d_charge_date", chargeDate);
                        intent.putExtra("deviceAddress", device.getAddress());
                        intent.putExtra("meter_reder", meterReder);
                        intent.putExtra("current_balance", currentBalance);
                        intent.putExtra("adjust_dosage", adjustDosage);
                        intent.putExtra("real_dosage", realDosage);
                        intent.putExtra("in_money",paidAmount);//实缴金额
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
        lv_unbondDevices.setAdapter(simpleAdapter);

        // 为每个item绑定监听，用于设备间的配对
        lv_unbondDevices
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
//                    addBandDevices(device);
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
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                addUnbondDevicesToListView();
//                addBondDevicesToListView();
                // bluetoothAdapter.cancelDiscovery();
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    System.out.println("--------关闭蓝牙-----------");
                        /*textView.setText("打开蓝牙");*/
                    searchDevices.setEnabled(true);
                    lv_bondDevices.setEnabled(true);
                    lv_unbondDevices.setEnabled(true);

                } else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    System.out.println("--------打开蓝牙-----------");
                        /*textView.setText("关闭蓝牙");*/
                    searchDevices.setEnabled(false);
                    lv_bondDevices.setEnabled(false);
                    lv_unbondDevices.setEnabled(false);
                }
            }

        }

    };

}
