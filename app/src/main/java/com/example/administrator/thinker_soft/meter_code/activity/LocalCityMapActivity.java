package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class LocalCityMapActivity extends Activity {
    private ImageView back;
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_city_map);
        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        mMapView = (MapView) findViewById(R.id.local_map);
    }

    //初始化设置
    private void defaultSetting() {
        MapStatus.Builder builder = new MapStatus.Builder();
        LatLng center = new LatLng(39.915071, 116.403907); // 默认 天安门
        float zoom = 11.0f; // 默认 11级
        Intent intent = getIntent();
        if (null != intent) {
            Log.i("LocalCityMapActivity","经度为："+intent.getDoubleExtra("y", 0.0)+"纬度为："+intent.getDoubleExtra("x", 0.0));
            center = new LatLng(intent.getDoubleExtra("y", 39.915071), intent.getDoubleExtra("x", 116.403907));
            zoom = intent.getFloatExtra("level", 11.0f);
        }
        MapStatus mMapStatus = builder
                .target(center)
                .zoom(zoom)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mMapView = new MapView(this, new BaiduMapOptions());
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    LocalCityMapActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        MapView.setMapCustomEnable(false);
        mMapView.onDestroy();
    }

}
