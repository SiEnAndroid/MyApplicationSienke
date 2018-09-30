package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterUserListviewAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/5 0005.
 */
public class MeterUserQueryResultActivity extends Activity {
    private ImageView back;
    private ListView listView;
    private ArrayList<MeterUserListviewItem> userLists = new ArrayList<>();
    private MeterUserListviewAdapter adapter;
    private MeterUserListviewItem item;
    private int currentPosition;  //点击当前抄表用户的item位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_user_query_result);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.listview);
    }

    //初始化设置
    private void defaultSetting() {
        Intent intent = getIntent();
        if(intent != null){
            userLists = intent.getParcelableArrayListExtra("meter_user_info");
            adapter = new MeterUserListviewAdapter(MeterUserQueryResultActivity.this, userLists);
            listView.setAdapter(adapter);
            MyAnimationUtils.viewGroupOutAlphaAnimation(MeterUserQueryResultActivity.this,listView,0.1F);
        }
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                item = (MeterUserListviewItem) adapter.getItem(position);
                Intent intent = new Intent(MeterUserQueryResultActivity.this, MeterUserDetailActivity.class);
                intent.putExtra("user_id",item.getUserID());
                startActivityForResult(intent,currentPosition);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                item= (MeterUserListviewItem) adapter.getItem(position);
                startNavi(item.getAddress());
                return true;
            }
        });
    }
    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi(String address) {
        try {
            Intent mapIntent = new Intent();
            mapIntent.setData(Uri.parse("baidumap://map/geocoder?src=openApiDemo&address="+address));
            startActivity(mapIntent);
            //BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showTipDialog();
        }
    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showTipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(MeterUserQueryResultActivity.this);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    MeterUserQueryResultActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == currentPosition){
                if(data != null){
                    item.setThisMonthDegree(data.getStringExtra("this_month_end_degree"));
                    item.setThisMonthDosage(data.getStringExtra("this_month_dosage"));
                    item.setIfEdit(R.mipmap.meter_true);
                    item.setMeterState("已抄");
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
