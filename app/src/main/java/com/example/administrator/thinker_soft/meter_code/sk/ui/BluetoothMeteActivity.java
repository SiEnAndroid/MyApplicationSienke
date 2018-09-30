package com.example.administrator.thinker_soft.meter_code.sk.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.activity.PrintDataActivity;
import com.example.administrator.thinker_soft.meter_code.sk.bean.MeterStampBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 蓝牙打印
 * Created by Administrator on 2018/4/24.
 */

public class BluetoothMeteActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemLongClickListener {
    private Unbinder mUnbinder;
    public static final String STAMP_KEY="metreReader";
    /**适配*/
    private BluetoothAdapter mBluetoothAdapter = null;
    /**匹配成功*/
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    /** 用于存放未配对蓝牙设备*/
    private ArrayList<BluetoothDevice> mNewDevicesList = new ArrayList<BluetoothDevice>();
    /**用于存放已经配对蓝牙设备*/
    private ArrayList<BluetoothDevice> mPairedDevicesList = new ArrayList<BluetoothDevice>();
    /**未匹配*/
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    private static final int REQUEST_ENABLE_BT = 101;//打开蓝牙

    private boolean searchAgain = false;
    @BindView(R.id.check_openBluetooth)
    CheckBox checkBluetooth;//打开或关闭
    @BindView(R.id.paired_devices)
    ListView pairedListView;//匹配
    @BindView(R.id.new_devices)
    ListView newDevicesListView;
    @BindView(R.id.return_Bluetooth_btn)
    ImageView back;//返回


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_mete);
        mUnbinder = ButterKnife.bind(this);//绑定
        initView();
    }

    private void initView() {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "您的设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }

        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.item_device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.item_device_name);
        //匹配
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBluetoothAdapter.cancelDiscovery();
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);
              Intent intent = new Intent(BluetoothMeteActivity.this, PrintDataActivity.class);
                Bundle bundle = new Bundle();
           //    Intent intent = new Intent(BluetoothMeteActivity.this, PrintBluetActivity.class);
                bundle.putString("deviceAddress",address);
           //     MeterStampBean stampBean = getPersonPar();
           //     Log.e("bbbbbb", new Gson().toJson(stampBean).toString());
            //    if (stampBean!=null){
             //       bundle.putParcelable(BluetoothMeteActivity.STAMP_KEY, stampBean);
             //   }
                intent.putExtras(bundle);
                startActivity(intent);
                //finish();


            }
        });
        //未匹配
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
        pairedListView.setOnItemLongClickListener(this);
        checkBluetooth.setOnCheckedChangeListener(this);
        initIntentFilter();
        if (!mBluetoothAdapter.isEnabled()) {
            //打开蓝牙
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            checkBluetooth.setChecked(true);
            setPairedData();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 获得从上一个Activity传来的数据
     *
     * @return String
     */
    private MeterStampBean getPersonPar() {
        // 直接通过Context类的getIntent()即可获取Intent
        Intent intent = this.getIntent();
        // 判断
        if (intent != null) {
            return (MeterStampBean) getIntent().getParcelableExtra(
                    STAMP_KEY);
        } else {
            return null;
        }
    }
    /**
     * 搜索蓝牙
     *
     * @param view
     */
    @OnClick(R.id.btn_searchDevices)
    public void searchOnclick(View view) {
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(BluetoothMeteActivity.this, "请打开蓝牙后进行搜索!", Toast.LENGTH_SHORT).show();
        }
        doDiscovery();
    }

    /**
     * 搜索实现
     */
    private void doDiscovery() {
        searchAgain = true;
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * 注册广播
     */
    private void initIntentFilter() {
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        registerReceiver(mReceiver, intentFilter);

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        ProgressDialog progressDialog = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (!mNewDevicesList.contains(device)) {
                        mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                        mNewDevicesList.add(device);
                    }

                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                if (searchAgain) {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setTitle("请稍等...");
                    progressDialog.setMessage("搜索蓝牙设备中...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();

                    setPairedData();
                } else {
                    mBluetoothAdapter.cancelDiscovery();
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                System.out.println("设备搜索完毕");
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    //  mNewDevicesArrayAdapter.add("没有匹配的设备");
                }
                //    addUnbondDevicesToListView();

            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    System.out.println("--------关闭蓝牙-----------");
                        /*textView.setText("打开蓝牙");*/


                } else if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    System.out.println("--------打开蓝牙-----------");
                        /*textView.setText("关闭蓝牙");*/

                }
            }
        }
    };


    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 匹配的设备
     */
    private void setPairedData() {
        mPairedDevicesArrayAdapter.clear();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mPairedDevicesList.add(device);
            }
        } else {
            //  mPairedDevicesArrayAdapter.add("没有匹配的设备");
            Log.e("---没有", "匹配的设备");
        }
        mPairedDevicesArrayAdapter.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "蓝牙已打开", Toast.LENGTH_SHORT).show();
                    checkBluetooth.setChecked(true);
                } else {
                    Toast.makeText(this, "蓝牙没有打开", Toast.LENGTH_SHORT).show();
                    checkBluetooth.setChecked(false);
                }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        if (isChecked) {
            //选中
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            checkBluetooth.setChecked(true);
        } else {
            checkBluetooth.setChecked(false);
            //取消选中
            mBluetoothAdapter.disable();//关闭蓝牙
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
                //连接蓝牙
//                Method createBondMethod = BluetoothDevice.class
//                        .getMethod("createBond");
//                Boolean returnValue = (Boolean) createBondMethod.invoke(mNewDevicesList.get(arg2));
                if (createBond(mNewDevicesList.get(arg2))) {
                    // 将绑定好的设备添加的已绑定list集合
                    mPairedDevicesArrayAdapter.add(mNewDevicesArrayAdapter.getItem(arg2));
                    // 将绑定好的设备从未绑定list集合中移除
                    mNewDevicesArrayAdapter.remove(mNewDevicesArrayAdapter.getItem(arg2));
                    mPairedDevicesList.add(mNewDevicesList.get(arg2));
                    mNewDevicesList.remove(arg2);
                }else {
                    Toast.makeText(BluetoothMeteActivity.this, "配对失败！", Toast.LENGTH_SHORT)
                            .show();
                }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        this.unregisterReceiver(mReceiver);
    }
    /**
     * 与设备解除配对
     *
     * @param device
     */
    public boolean removeBond(BluetoothDevice device) {
        Boolean returnValue = null;
        try {
            Method removeBondMethod = device.getClass().getMethod("removeBond");
            returnValue = (Boolean)removeBondMethod.invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnValue.booleanValue();
    }
    /**
     * 与设备配对
     *
     * @param device
     */
    public boolean createBond(BluetoothDevice device) {
        Boolean returnValue = null;
        try {
            Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
            returnValue = (Boolean) createBondMethod.invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
            returnValue=false;
        }
        return returnValue.booleanValue();
    }
    /**
     * 取消配对
     */
    public boolean cancelBondProcess(BluetoothDevice device) {
        Boolean returnValue = null;
        try {
            Method createBondMethod = device.getClass().getMethod("cancelBondProcess");
            returnValue = (Boolean) createBondMethod.invoke(device);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return returnValue.booleanValue();
    }
    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        deleteDialog(BluetoothMeteActivity.this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             if (removeBond(mPairedDevicesList.get(position))){
                 mPairedDevicesList.remove(parent);
                 mPairedDevicesArrayAdapter.remove(mPairedDevicesArrayAdapter.getItem(position));
             };
            }
        });

        return true;
    }

    /**
     * 删除
     * @param context
     * @param listener
     */
    public  void deleteDialog(Context context, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("是否删除?").setPositiveButton("确定", listener).setNegativeButton("取消", null);
        builder.create().show();
    }
}
