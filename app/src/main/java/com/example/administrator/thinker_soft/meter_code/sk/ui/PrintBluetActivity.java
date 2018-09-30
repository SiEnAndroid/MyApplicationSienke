package com.example.administrator.thinker_soft.meter_code.sk.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.PrintBluetAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.MeterStampBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.PrintBluetBean;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.PrintUtils;
import com.example.administrator.thinker_soft.meter_code.sk.uitls.SimpleDateUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.administrator.thinker_soft.meter_code.sk.ui.BluetoothMeteActivity.STAMP_KEY;

/**
 * 打印小票
 * Created by Administrator on 2018/4/25.
 */
public class PrintBluetActivity extends AppCompatActivity {
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    private SharedPreferences mySharedPreferences, sharedPreferences_login;
    private Cursor cursor;
    private SQLiteDatabase db;  //数据库
    private String userID = null;//用户id
    //蓝牙配置
    String DeviceName = "";
    private BluetoothAdapter mBluetoothAdapter = null;
    private String mConnectedDeviceName = null;
    private Unbinder mUnbinder;//id绑定
    private PrintBluetAdapter printBluetAdapter;//适配器
    private final int exceptionCod = 100;
    private final int exceptionOk = 101;
    //搜索弹窗提示
    ProgressDialog progressDialog = null;
    //打印的内容
    private String mPrintContent;
    private  MeterStampBean meterStamp;//详情
    //蓝牙socket对象
    private BluetoothSocket mmSocket = null;
    private final UUID uuid = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    //打印的输出流
    private static OutputStream outputStream = null;
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
    private static boolean isCont = false;//是否连接
    @BindView(R.id.rl_meterList)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_blue_total)
    TextView titleBlue;//头部


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_list);
        mUnbinder = ButterKnife.bind(this);//绑定
        initView();
        initDb();
       //new getMeterTask().execute();

    }

    private void initView() {
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置RecyclerView 布局
        mRecyclerView.setLayoutManager(layoutmanager);
        printBluetAdapter = new PrintBluetAdapter(this);
        mRecyclerView.setAdapter(printBluetAdapter);
        //添加Android自带的分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //获取地址
//        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(getDeviceAddress());
//        setConnect(device);
        meterStamp = getPersonPar();
        if (meterStamp!=null) {
            setData();
            Log.e("zz", new Gson().toJson(meterStamp).toString());
        }
        new ConnectThread(mBluetoothAdapter.getRemoteDevice(getDeviceAddress())).start();
        progressDialog = ProgressDialog.show(this, "提示", "正在连接...", true);
        Log.e("kkkk", new Gson().toJson(meterStamp).toString());

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initDb() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        mySharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userID = mySharedPreferences.getString("user_id", "");
        Log.i("user_id", "获取到的ID是" + userID);
        MySqliteHelper helper = new MySqliteHelper(this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
    }

//    /**
//     * 获取数据
//     */
//    private List<String> getUserData() {
//        List<String> printList = new ArrayList<>();
//        cursor = db.rawQuery("select * from MeterUser where login_user_id=? and user_id=?", new String[]{sharedPreferences_login.getString("userId", ""), userID});
//        Log.i("cursor", cursor.getCount() + "长度为");
//        if (cursor.getCount() == 0) {
//            return printList;
//        }
//        while (cursor.moveToNext()) {
//            printList.add(cursor.getString(cursor.getColumnIndex("user_name")));
//            printList.add(cursor.getString(cursor.getColumnIndex("user_address")));
//            printList.add(cursor.getString(cursor.getColumnIndex("meter_degrees")));
//            printList.add(cursor.getString(cursor.getColumnIndex("last_month_dosage")));
//            printList.add(cursor.getString(cursor.getColumnIndex("this_month_end_degree")));
//            printList.add(cursor.getString(cursor.getColumnIndex("this_month_dosage")));
//            //金额
//            printList.add(cursor.getString(cursor.getColumnIndex("arrearage_amount")));
//            //月数
//            printList.add(cursor.getString(cursor.getColumnIndex("arrearage_months")));
//        }
//        cursor.close();
//
//        return printList;
//    }



//    private class getMeterTask extends AsyncTask<Void , Integer,List<String>> {
//        @Override
//        protected void onPreExecute() {
//            //开始
//        }
//
//        @Override
//        protected   List<String>  doInBackground(Void... params) {
//            List<String> mList = getUserData();
//            return mList;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//           Log.e("--","当前下载进度：" + values[0] + "%");
//        }
//
//        @Override
//        protected void onPostExecute(List<String> list) {
//            super.onPostExecute(list);
//            List<PrintBluetBean> printList = new ArrayList<>();
//            String[] titleArr = getResources().getStringArray(R.array.meter_list);
////            String[] contStr = new String[]{"B-008642", "本月12日至26日", list.get(0), list.get(1),
////                    list.get(2), list.get(4), "25420吨", "70.00元", "5.4元/吨", "250吨", "1.5元/吨",
////                    "50.00元", "锦竹国润供水有限公司", "中国建设银行股份有限公司", "5105016472260000105",
////                    "锦竹市供排水总公司(玉妃路)政务中心D区(景观大道)回澜大道(德阳银行侧)", "62173366","6202781",
////                    "姚文祥", "2018-4-20  10:21", "请在指定时间内交款,逾期每天加收5%违约金"
////            };
////            for (int i = 0; i < titleArr.length; i++) {
////                PrintBluetBean bean = new PrintBluetBean();
////                bean.setTitle(String.valueOf(titleArr[i]));
////                if (i<contStr.length) {
////                    bean.setContent(contStr[i]);
////                }
////                printList.add(bean);
////            }
//
//
//
//
//            printBluetAdapter.addAll(printList);
//        }
//
//    }

    /**
     * 设置数据
     */
    private void setData(){
        List<PrintBluetBean> printList = new ArrayList<>();
        String[] titleArr = getResources().getStringArray(R.array.meter_list);
        printList.add(new PrintBluetBean(String.valueOf(titleArr[0]),meterStamp.getMeter_number()));//表号
        printList.add(new PrintBluetBean(String.valueOf(titleArr[1]),meterStamp.getMeter_data()));//缴费日期
        printList.add(new PrintBluetBean(String.valueOf(titleArr[2]),meterStamp.getUser_name()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[3]),meterStamp.getUser_address()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[4]),meterStamp.getMin_start()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[5]),meterStamp.getMax_end()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[6]),meterStamp.getAll_max()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[7]),meterStamp.getUser_amount()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[8]),meterStamp.getWeter_amount()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[9]),meterStamp.getWeter_wamount()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[10]),meterStamp.getMeter_payee()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[11]),meterStamp.getBank_name()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[12]),meterStamp.getMeter_account()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[13]),meterStamp.getPayment_address()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[14]),meterStamp.getPayment_phone()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[15]),meterStamp.getMaintain_phone()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[16]),meterStamp.getReader_name()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[17]),meterStamp.getReader_time()));
        printList.add(new PrintBluetBean(String.valueOf(titleArr[18]),meterStamp.getReader_remark()));
        printBluetAdapter.addAll(printList);
        printBluetAdapter.notifyDataSetChanged();


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
    /**
     * 获得从上一个Activity传来的蓝牙地址
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
     * 监听
     *
     * @param view
     */
    @OnClick({R.id.btn_blue_send, R.id.btn_blue_command, R.id.btn_blue_back})
    public void sendOnclick(View view) {
        switch (view.getId()) {
            case R.id.btn_blue_send:
                //打印
                StringBuilder sb = new StringBuilder();
                      sb.append(PrintUtils.printTitle("水费通知单")).append("\n\n")
                        .append("表号:").append(meterStamp.getMeter_number()).append("\n")
                        .append("缴费日期:").append(meterStamp.getMeter_data()).append("\n")
                        .append("户名:").append(meterStamp.getUser_name()).append("\n")
                        .append("地址:").append(PrintUtils.setLength(meterStamp.getUser_address(),11)).append("\n")
                        .append("起度:").append(meterStamp.getMin_start()).append("    ").append("止度：").append(meterStamp.getMax_end()).append("\n")
                        .append("合计吨数:").append(meterStamp.getAll_max()).append("\n")
                        .append("缴费金额:").append(meterStamp.getUser_amount()).append("\n")
                        .append("----------------------------").append("\n")
                        .append("生产:").append(PrintUtils.setLengths(meterStamp.getWeter_amount())).append("\n")
                        .append("生产污水处理费:").append(PrintUtils.setLengths(meterStamp.getWeter_wamount())).append("\n")
                        .append("收款单位:").append(meterStamp.getMeter_payee()).append("\n")
                        .append("开户行:").append(meterStamp.getBank_name()).append("\n")
                        .append("账号:").append(meterStamp.getMeter_account()).append("\n")
                        .append("交款地址:\n").append("锦竹市供排水总公司(玉妃路)\n政务中心D区(景观大道)\n回澜大道(德阳银行侧)").append("\n")
                     //.append("交款地址:\n").append(PrintUtils.setLength("锦竹市供排水总公司(玉妃路)政务中心D区(景观大道)回澜大道(德阳银行侧)",14)).append("\n")
                        .append("收费电话:").append(meterStamp.getPayment_phone()).append("\n")
                        .append("抄表、维修电话:").append(meterStamp.getMaintain_phone()).append("\n")
                        .append("抄表员:").append(meterStamp.getReader_name()).append("\n")
                        .append("抄表时间:").append(meterStamp.getReader_time()).append("\n")
                        .append("备注:").append(PrintUtils.setLength("请在指定时间内交款,逾期每天加收5%违约金",11)).append("\n")
                        .append(SimpleDateUtils.getTime()).append("\n")
                        .append("\n").append("\n").append("\n");
                Log.e("===ook", sb.toString());
//                //是否连接
                if (isCont) {
                    if (meterStamp!=null) {
                        //打印
//                        StringBuilder sb = new StringBuilder();
//                        sb.append("表号:").append(meterStamp.getMeter_number()).append("\n")
//                                .append("缴费日期:").append(meterStamp.getMeter_data()).append("\n")
//                                .append("户名:").append(meterStamp.getUser_name()).append("\n")
//                                .append("地址:").append(meterStamp.getUser_address()).append("\n")
//                                .append("起度:").append(meterStamp.getMin_start()).append("    ").append("止度：").append(meterStamp.getMax_end()).append("\n")
//                                .append("合计吨数:").append(meterStamp.getAll_max()).append("\n")
//                                .append("缴费金额:").append(meterStamp.getUser_amount()).append("\n")
//                                .append("----------------------").append("\n")
//                                .append("生产:").append(meterStamp.getWeter_amount()).append("\n")
//                                .append("生产污水处理费:").append(meterStamp.getWeter_wamount()).append("\n")
//                                .append("收款单位:").append(meterStamp.getMeter_payee()).append("\n")
//                                .append("开户行:").append(meterStamp.getBank_name()).append("\n")
//                                .append("账号:").append(meterStamp.getMeter_account()).append("\n")
//                                .append("交款地址:\n").append(meterStamp.getPayment_address()).append("\n")
//                                .append("收费电话:").append(meterStamp.getPayment_phone()).append("\n")
//                                .append("抄表、维修电话:").append(meterStamp.getMaintain_phone()).append("\n")
//                                .append("抄表员:").append(meterStamp.getReader_name()).append("\n")
//                                .append("抄表时间:").append(meterStamp.getReader_time()).append("\n")
//                                .append("备注:").append(meterStamp.getReader_remark()).append("\n")
//                                .append(SimpleDateUtils.getTime()).append("\n")
//                                .append("\n").append("\n").append("\n");
//                        Log.e("===ook", sb.toString());
                       send(sb.toString());
                    }else {
                        Toast.makeText(this, "打印失败。请稍后重试！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请重新连接进行打印！", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.btn_blue_command:
                //选择打印
                selectCommand();
                break;
            case R.id.btn_blue_back:
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 选择指令
     */
    public void selectCommand() {
        new AlertDialog.Builder(this).setTitle("请选择指令")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (isCont) {
                                outputStream.write(byteCommands[which]);
                            } else {
                                Toast.makeText(PrintBluetActivity.this, "设置失败！请重新连接！", Toast.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
//                            Toast.makeText(PrintBluetActivity.this, "设置指令失败！",
//                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create().show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        try {
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 发送数据
     */
    public void send(String sendData) {
        try {
            byte[] data = sendData.getBytes("gbk");
            outputStream.write(data, 0, data.length);
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
//            Toast.makeText(this, "发送失败！", Toast.LENGTH_SHORT)
//                    .show();
            // handler.sendEmptyMessage(exceptionCod); // 向Handler发送消息,更新UI

        }
    }

    /**
     * 连接为客户端
     */
    private class ConnectThread extends Thread {
        public ConnectThread(BluetoothDevice device) {
            try {
                mmSocket = device.createRfcommSocketToServiceRecord(uuid);
                DeviceName = device.getName();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        public void run() {
            //取消的发现,因为它将减缓连接
            mBluetoothAdapter.cancelDiscovery();
            try {
                //连接socket
                mmSocket.connect();
                //连接成功获取输出流
                outputStream = mmSocket.getOutputStream();
                //异常时发消息更新UI
                Message msg = new Message();
                msg.what = exceptionOk;
                // 向Handler发送消息,更新UI
                handler.sendMessage(msg);

            } catch (Exception connectException) {
                Log.e("test", "连接失败");

                connectException.printStackTrace();
                //异常时发消息更新UI
                Message msg = new Message();
                msg.what = exceptionCod;
                // 向Handler发送消息,更新UI
                handler.sendMessage(msg);

                try {
                    mmSocket.close();
                } catch (Exception closeException) {
                    closeException.printStackTrace();
                }

            }
        }
    }

    //在打印异常时更新ui
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (msg.what == exceptionCod) {
                // Toast.makeText(PrintBluetActivity.this, "打印发送失败，请稍后再试", Toast.LENGTH_SHORT).show();
                titleBlue.setText(DeviceName + "        连接失败！");
                isCont = false;

            } else if (msg.what == exceptionOk) {
                titleBlue.setText(DeviceName + "        连接成功");
                isCont = true;
                Toast.makeText(PrintBluetActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
