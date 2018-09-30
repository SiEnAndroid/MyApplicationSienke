package com.example.administrator.thinker_soft.patrol_inspection.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.CommonAdapter;
import com.example.administrator.thinker_soft.mode.ViewHolder;
import com.example.administrator.thinker_soft.patrol_inspection.untils.ChooseDialogUntil;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTaskActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_devicename)
    EditText etDevicename;
    @BindView(R.id.et_device_number)
    EditText etDeviceNumber;
    @BindView(R.id.tv_pipeline)
    TextView tvPipeline;
    @BindView(R.id.tv_before_node)
    TextView tvBeforeNode;
    @BindView(R.id.tv_device_type)
    TextView tvDeviceType;
    @BindView(R.id.tv_device_model)
    TextView etDeviceModel;
    @BindView(R.id.tv_pressure_type)
    TextView tvPressureType;
    @BindView(R.id.tv_use_state)
    TextView tvUseState;
    @BindView(R.id.tv_install_date)
    TextView tvInstallDate;
    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.et_latlng)
    EditText etLatlng;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_remark)
    EditText etRemark;
    private Intent intent;
    private String address, longitude, latitude;
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        defaultSetting();
    }

    private void defaultSetting() {
        intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        address = intent.getStringExtra("address");
        etAddress.setText(address);
        etLatlng.setText(longitude + " , " + latitude);
        c = Calendar.getInstance();
        tvInstallDate.setText(new StringBuilder().append(c.get(Calendar.YEAR)).append("-").append(c.get(Calendar.MONTH)+1).append("-").append(c.get(Calendar.DAY_OF_MONTH)));
        tvReleaseDate.setText(new StringBuilder().append(c.get(Calendar.YEAR)).append("-").append(c.get(Calendar.MONTH)+1).append("-").append(c.get(Calendar.DAY_OF_MONTH)));
    }

    @OnClick({R.id.back, R.id.save, R.id.et_title, R.id.et_devicename, R.id.et_device_number, R.id.tv_pipeline, R.id.tv_before_node, R.id.tv_device_type, R.id.tv_device_model, R.id.tv_pressure_type, R.id.tv_use_state, R.id.tv_install_date, R.id.tv_release_date, R.id.et_latlng, R.id.et_address, R.id.et_remark})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                break;
            case R.id.et_title:
                break;
            case R.id.et_devicename:
                break;
            case R.id.et_device_number:
                break;
            case R.id.tv_pipeline:
                final ArrayList<String> list=new ArrayList<>();
                for (int i=0;i<5;i++){
                    list.add("线路"+i);
                }
                ChooseDialogUntil.showSecurityCodeInputDialog(this,"所属管线:");
                ListView listView= (ListView) ChooseDialogUntil.getView(R.id.lv_patrol_choose);
                CommonAdapter<String> adapter=new CommonAdapter<String>(this,R.layout.patrol_choose_item) {
                    @Override
                    public void convertView(int i, ViewHolder viewHolder, String item) {
                        TextView textView=viewHolder.getView(R.id.category_string);
                        textView.setText(item);
                    }
                };
                adapter.addDatas(list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvPipeline.setText(list.get(position));
                    }
                });

                break;
            case R.id.tv_before_node:
                break;
            case R.id.tv_device_type:
                break;
            case R.id.tv_device_model:
                break;
            case R.id.tv_pressure_type:
                break;
            case R.id.tv_use_state:
                break;
            case R.id.tv_install_date:
                tvInstallDate.setClickable(false);
                DatePickerDialog startDateDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tvInstallDate.setText(new StringBuilder().append(year).append("-").append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "")
                                .append("-")
                                .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth + ""));
                        tvInstallDate.setClickable(true);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                startDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        tvInstallDate.setClickable(true);
                    }
                });
                startDateDialog.show();
                break;
            case R.id.tv_release_date:
                tvReleaseDate.setClickable(false);
                DatePickerDialog releaseDateDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tvReleaseDate.setText(new StringBuilder().append(year).append("-").append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "")
                                .append("-")
                                .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth + ""));
                        tvReleaseDate.setClickable(true);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                releaseDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        tvReleaseDate.setClickable(true);
                    }
                });
                releaseDateDialog.show();
                break;
            case R.id.et_latlng:
                break;
            case R.id.et_address:
                break;
            case R.id.et_remark:
                break;
        }
    }
}
