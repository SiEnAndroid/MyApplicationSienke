package com.example.administrator.thinker_soft.patrol_inspection.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class PatrolHomePageActivity extends Activity implements SensorEventListener {

    private MapView mMapView;
    private ImageView location;
    private BaiduMap mBaiduMap;
    private Button btn_look, btn_add, btn_line, btn_mine;
    private SensorManager mSensorManager;
    private BitmapDescriptor mCurrentMarker;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private String locationAddress;
    private String latitude;
    private String longitude;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_home_page);

        bindView();
        defaultSetting();
        requireLocationPermission();
        setViewClickListener();
    }

    private void requireLocationPermission() {
        //Android 6.0判断用户是否授予定位权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(PatrolHomePageActivity.this, "自Android 6.0开始需要打开位置权限", Toast.LENGTH_SHORT).show();
                }
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ACCESS_COARSE_LOCATION);
            } else {
                initLocation();
            }
        } else {
            initLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户允许改权限，0表示允许，-1表示拒绝 PERMISSION_GRANTED = 0， PERMISSION_DENIED = -1
                //permission was granted, yay! Do the contacts-related task you need to do.
                //这里进行授权被允许的处理
                initLocation();
            } else {
                //permission denied, boo! Disable the functionality that depends on this permission.
                //这里进行权限被拒绝的处理
                Toast.makeText(PatrolHomePageActivity.this, "请求权限失败！", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //绑定控件
    private void bindView() {
        mMapView = (MapView) findViewById(R.id.patrol_map);
        location = (ImageView) findViewById(R.id.location);
        btn_look = (Button) findViewById(R.id.patrol_look);
        btn_add = (Button) findViewById(R.id.patrol_add);
        btn_mine = (Button) findViewById(R.id.patrol_mine);
        btn_line = (Button) findViewById(R.id.patrol_line);
    }

    //初始化设置
    private void defaultSetting() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        MapView.setMapCustomEnable(true);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    }

    //点击事件
    public void setViewClickListener() {
        location.setOnClickListener(btnClickListener);
        btn_look.setOnClickListener(clickListener);
        btn_add.setOnClickListener(clickListener);
        btn_mine.setOnClickListener(clickListener);
        btn_line.setOnClickListener(clickListener);

        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

            }
        });
    }

    View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (mCurrentMode) {
                case NORMAL:
                    location.setImageResource(R.mipmap.follow);
                    mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                    mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.overlook(0);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    break;
                case COMPASS:
                    location.setImageResource(R.mipmap.location);
                    mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                    mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                            mCurrentMode, true, mCurrentMarker));
                    MapStatus.Builder builder1 = new MapStatus.Builder();
                    builder1.overlook(0);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
                    break;
                case FOLLOWING:
                    location.setImageResource(R.mipmap.compass);
                    mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                    mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                            mCurrentMode, true, mCurrentMarker));
                    break;
                default:
                    break;
            }
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.patrol_look:
                    intent = new Intent(PatrolHomePageActivity.this, PatrolListActivity.class);
                    startActivity(intent);
                    break;
                case R.id.patrol_mine:
                    intent = new Intent(PatrolHomePageActivity.this, MineActivity.class);
                    startActivity(intent);
                    break;
                case R.id.patrol_line:
                    loginByPost("SUPER", "000");
                    break;
                case R.id.patrol_add:
                    intent = new Intent(PatrolHomePageActivity.this, AddTaskActivity.class);
                    intent.putExtra("address", locationAddress);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    startActivity(intent);
                    break;
            }
        }
    };

    //post请求
    public void loginByPost(final String userName, final String userPass) {
        new Thread() {
            @Override
            public void run() {
                try {
                    //请求的地址

                    String httpUrl=new StringBuffer().append(SkUrl.SkHttp(PatrolHomePageActivity.this)).append("/SMDemo/login.do").toString();
                 // String httpUrl = SkUrl.SKURLA;
                    Log.i("httpUrl==========>", "" + httpUrl);
                    // 根据地址创建URL对象
                    URL url = new URL(httpUrl);
                    // 根据URL对象打开链接2017-09-08
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    // 发送POST请求必须设置允许输出
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setUseCaches(false);//不使用缓存
                    // 设置请求的方式
                    urlConnection.setRequestMethod("POST");
                    // 设置请求的超时时间
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setConnectTimeout(5000);
                    // 传递的数据
                    String data = "username=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + URLEncoder.encode(userPass, "UTF-8");
                    Log.i("data==========>", "data=" + data);
                    // 设置请求的头
                    //urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    //urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    //urlConnection.setRequestProperty("Origin", "http://"+ ip + port);
                    Log.i("data==========>", "data=" + data);
                    // 设置请求的头
                    //urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //urlConnection.setRequestProperty("Origin", "http://"+ ip + port);
                    urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
                    //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                    //获取输出流
                    OutputStream os = urlConnection.getOutputStream();
                    os.write(data.getBytes("UTF-8"));
                    os.flush();
                    os.close();
                    Log.i("getResponseCode====>", "" + urlConnection.getResponseCode());
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream inputStream = urlConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        // 释放资源
                        inputStream.close();
                        // 返回字符串
                        String result = stringBuilder.toString();
                        Log.i("login_result=========>", result);
                        JSONObject jsonObject = new JSONObject(result);
//                        if (jsonObject.optInt("messg", 0) == 1) {
//                            editor.putInt("company_id", jsonObject.optInt("companyid", 0));
//                            editor.putString("user_name", jsonObject.optString("userName", ""));
//                            editor.putString("userId", jsonObject.optInt("systemuserId", 0) + "");
//                            // TODO: 2017/10/11
////                            if (sharedPreferences_login.getLong("creat_date", 0) != 0) {
////                                long start_time = sharedPreferences_login.getLong("creat_date", 0);
////                                Log.i("delta-T", " start_time=" + sharedPreferences_login.getLong("creat_date", 0));
////                                long end_time = System.currentTimeMillis();
////                                Log.i("delta-T", " end_time=" + System.currentTimeMillis());
//////                                int s = (int) ((end_time - start_time) / (ONE_DAY_MS));
////                                int s = TimeUtil.betweenDays(start_time, end_time);
////                                Log.i("delta-T", " creat_date!=0,時間差=" + s);
////                                if (s > 30) {
////                                    handler.sendEmptyMessage(4);
////                                    return;
////                                }
////                            } else {
////                                //直接倒转到移动抄表页面
////                                Log.i("delta-T", " creat_date=0,時間=" + System.currentTimeMillis());
////                                editor.putLong("creat_date", System.currentTimeMillis());
////                            }
//                            editor.apply();
//                            Log.i("MobileSecurityLogin", "当前用户公司ID是：" + sharedPreferences_login.getInt("company_id", 0));
//                            Log.i("MobileSecurityLogin", "当前用户是：" + sharedPreferences_login.getString("user_name", ""));
//                            handler.sendEmptyMessage(1);
//                        }
//                        if (jsonObject.optInt("messg", 0) == 0) {
//                            handler.sendEmptyMessage(2);
//                        }
//                    } else {
//                        handler.sendEmptyMessage(3);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException==========>", "网络请求异常!");
//                    handler.sendEmptyMessage(3);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    //初始化定位
    private void initLocation() {
        // 定位初始化
        mLocClient = new LocationClient(getApplicationContext()); //声明LocationClient类
        mLocClient.registerLocationListener(myListener);     //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setOpenGps(true); // 打开gps
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setNeedDeviceDirect(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double x = event.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map_meter_icon view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            int code = location.getLocType();
            Log.i("MyLocationListenner", "code是：" + code);
            /*// 定位接口可能返回错误码,要根据结果错误码,来判断是否是正确的地址;
            int locType = location.getLocType();
            switch (locType) {
                case BDLocation.TypeCacheLocation:
                case BDLocation.TypeOffLineLocation:
                case BDLocation.TypeGpsLocation:
                case BDLocation.TypeNetWorkLocation:
                    *//*radius = bdLocation.getRadius();
                    user_latitude = bdLocation.getLatitude();
                    user_longitude = bdLocation.getLongitude();
                    mCurrentX = bdLocation.getDirection();*//*
                    break;
                default:
                    String s = location.getLocTypeDescription();
                    break;
            }*/
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());

            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locationAddress = location.getAddrStr();
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;  //这个判断是为了防止每次定位都重新设置中心点和marker
//                setMarker();
                setUserMapCenter();
            }
            // 开启定位图层
            mBaiduMap.setMyLocationEnabled(true);
            Log.i("MyLocationListenner", sb.toString());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    /**
     * 添加marker
     */
    private void setMarker() {
        //定义Maker坐标点
        LatLng point = new LatLng(mCurrentLat, mCurrentLon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.location_marker);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    /**
     * 设置中心点
     */
    private void setUserMapCenter() {
        Log.v("pcw", "setUserMapCenter : lat : " + mCurrentLat + " lon : " + mCurrentLon);
        LatLng cenpt = new LatLng(mCurrentLat, mCurrentLon);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

}
