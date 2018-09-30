package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/7/19 0019.
 */
public class ObtainMeterCoodinateActivity extends Activity{
    private ImageView back;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView title,navigation;
    private LayoutInflater inflater;  //转换器
    private View view;
    private PopupWindow tipsWindow;
    private TextView confirm;
    private LinearLayout rootLinearlayout;
    private LatLng center;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.mipmap.location_marker);
    public Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_meter);
        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        navigation = (TextView) findViewById(R.id.navigation);
        mMapView = (MapView) findViewById(R.id.map_meter);
    }

    //初始化设置
    private void defaultSetting() {
        title.setText("抄表地理位置");
        navigation.setVisibility(View.GONE);
        mBaiduMap = mMapView.getMap();
        Intent intent = getIntent();
        if (intent != null) {
            Log.i("ObtainMeterCoodinateAc","传过来的坐标为"+intent.getStringExtra("latitude")+intent.getStringExtra("longitude"));
            if(intent.getStringExtra("latitude") != null && intent.getStringExtra("longitude") != null){
                center = new LatLng(Double.parseDouble(intent.getStringExtra("latitude")),Double.parseDouble(intent.getStringExtra("longitude"))); //获取传过来的经纬度坐标
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(center)
                        .zoom(18.0F)
                        .build();
                //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                //改变地图状态
                mBaiduMap.setMapStatus(mMapStatusUpdate);
                setMarker(center);
            }else {
                title.setText("抄表地理位置为空");
            }
        }
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                showTipsWindow(latLng);
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showTipsWindow(marker.getPosition());
                return true;
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    ObtainMeterCoodinateActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    //弹出是否保存坐标提示框
    public void showTipsWindow(final LatLng latLng) {
        inflater = LayoutInflater.from(ObtainMeterCoodinateActivity.this);
        view = inflater.inflate(R.layout.popupwindow_no_task, null);
        tipsWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        TextView tips = (TextView) view.findViewById(R.id.one_tips);
        confirm = (TextView) view.findViewById(R.id.confirm);
        //设置点击事件
        tips.setText("当前坐标为："+latLng.latitude+" , "+latLng.longitude);
        confirm.setText("知道了");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipsWindow.dismiss();
            }
        });
        tipsWindow.update();
        tipsWindow.setFocusable(true);
        tipsWindow.setTouchable(true);
        tipsWindow.setOutsideTouchable(true);
        tipsWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        tipsWindow.setAnimationStyle(R.style.camera);
        tipsWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);
        tipsWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ObtainMeterCoodinateActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            ObtainMeterCoodinateActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            ObtainMeterCoodinateActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        ObtainMeterCoodinateActivity.this.getWindow().setAttributes(lp);
    }

    /**
     * 添加marker
     */
    private void setMarker(LatLng center) {
        MarkerOptions markerOptions = new MarkerOptions().position(center).icon(markerIcon);
        // 掉下动画
        markerOptions.animateType(MarkerOptions.MarkerAnimateType.drop);
        mMarker = (Marker) (mBaiduMap.addOverlay(markerOptions));
    }

    @Override
    protected void onPause() {
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
        // 回收 bitmap 资源
        markerIcon.recycle();
    }
}
