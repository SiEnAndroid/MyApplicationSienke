package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterMapCityListAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterMapCityListItem;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/20 0020.
 */
public class MeterMapDownloadActivity extends Activity implements MKOfflineMapListener {
    private ImageView back;
    private RadioButton cityRb, downloadRb;
    private LinearLayout rootLinearlayout, leftLayout, rightLayout;
    private ListView hotCityList, allCityList;
    private SwipeMenuListView localMapSwipeList;
    private MKOfflineMap mOffline = null;
    private TextView startDownload, stopDownload, progress, currentCity, currentCityId, citySearch,noData;
    private EditText cityInput;
    private ArrayList<MeterMapCityListItem> hotCities = new ArrayList<>();
    private ArrayList<MeterMapCityListItem> allCities = new ArrayList<>();
    private ArrayList<String> hotCityNames;
    private ArrayList<String> allCityNames;
    private ArrayList<Integer> hotCityIDs = new ArrayList<>();
    private ArrayList<Integer> allCityIDs = new ArrayList<>();
    private View deleteView;
    private PopupWindow deleteWindow;
    private LayoutInflater inflater;  //转换器
    /**
     * 已下载的离线地图信息列表
     */
    private ArrayList<MKOLUpdateElement> localMapList = null;
    private LocalMapAdapter localAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_download_city_map);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
        back = (ImageView) findViewById(R.id.back);
        cityRb = (RadioButton) findViewById(R.id.city_rb);
        downloadRb = (RadioButton) findViewById(R.id.download_rb);
        progress = (TextView) findViewById(R.id.progress);
        startDownload = (TextView) findViewById(R.id.start_download);
        stopDownload = (TextView) findViewById(R.id.stop_download);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        rightLayout = (LinearLayout) findViewById(R.id.right_layout);
        currentCity = (TextView) findViewById(R.id.current_city);
        currentCityId = (TextView) findViewById(R.id.current_city_id);
        cityInput = (EditText) findViewById(R.id.city_input);
        citySearch = (TextView) findViewById(R.id.city_search);
        hotCityList = (ListView) findViewById(R.id.hotcitylist);
        allCityList = (ListView) findViewById(R.id.allcitylist);
        localMapSwipeList = (SwipeMenuListView) findViewById(R.id.swipe_local_city_list);
        noData = (TextView) findViewById(R.id.no_data);
    }

    //初始化设置
    private void defaultSetting() {
        cityRb.setChecked(true);
        stopDownload.setEnabled(false);
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        hotCityNames = new ArrayList<String>();
        // 获取热门城市
        ArrayList<MKOLSearchRecord> records1 = mOffline.getHotCityList();
        if (records1 != null) {
            for (MKOLSearchRecord r : records1) {
                MeterMapCityListItem item = new MeterMapCityListItem();
                item.setCityName(r.cityName);
                item.setCityID(String.valueOf(r.cityID));
                item.setCitySize(this.formatDataSize(r.size));
                hotCities.add(item);
                hotCityNames.add(r.cityName);
                hotCityIDs.add(r.cityID);
            }
        }
        MeterMapCityListAdapter adapter = new MeterMapCityListAdapter(MeterMapDownloadActivity.this, hotCities);
        hotCityList.setAdapter(adapter);
        MyAnimationUtils.viewGroupOutAnimation(MeterMapDownloadActivity.this, hotCityList, 0.05F);

        // 获取所有支持离线地图的城市
        allCityNames = new ArrayList<String>();
        ArrayList<MKOLSearchRecord> records2 = mOffline.getOfflineCityList();
        if (records2 != null) {
            for (MKOLSearchRecord r : records2) {
                MeterMapCityListItem item = new MeterMapCityListItem();
                item.setCityName(r.cityName);
                item.setCityID(String.valueOf(r.cityID));
                item.setCitySize(this.formatDataSize(r.size));
                allCities.add(item);
                allCityNames.add(r.cityName);
                allCityIDs.add(r.cityID);
            }
        }
        MeterMapCityListAdapter allCityAdapter = new MeterMapCityListAdapter(MeterMapDownloadActivity.this, allCities);
        allCityList.setAdapter(allCityAdapter);
        MyAnimationUtils.viewGroupOutAnimation(MeterMapDownloadActivity.this, allCityList, 0.05F);

        // 获取已下过的离线地图信息
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<>();
        }
        Log.i("MeterDownloadMap","本地离线地图集合长度为："+localMapList.size());
        if(localMapList.size() == 0){
            noData.setVisibility(View.VISIBLE);
        }else {
            noData.setVisibility(View.GONE);
        }
        localAdapter = new LocalMapAdapter();
        localMapSwipeList.setAdapter(localAdapter);
        localMapSwipeList.setMenuCreator(creator);
        localMapSwipeList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        MyAnimationUtils.viewGroupOutAnimation(MeterMapDownloadActivity.this, localMapSwipeList, 0.05F);
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {
            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                    0xCE)));
            // set item width
            openItem.setWidth(dp2px(60));
            // set item title
            openItem.setTitle("查看");
            // set item title fontsize
            openItem.setTitleSize(14);
            // set item title font color
            openItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(dp2px(60));
            // set item title
            deleteItem.setTitle("删除");
            // set item title fontsize
            deleteItem.setTitleSize(14);
            // set item title font color
            deleteItem.setTitleColor(Color.WHITE);
            // set a icon
            //deleteItem.setIcon(R.mipmap.task_manager);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        cityRb.setOnClickListener(onClickListener);
        downloadRb.setOnClickListener(onClickListener);
        startDownload.setOnClickListener(onClickListener);
        stopDownload.setOnClickListener(onClickListener);
        citySearch.setOnClickListener(onClickListener);
        //热门城市列表点击事件
        hotCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentCity.setText(hotCityNames.get(i));
                currentCityId.setText(String.valueOf(hotCityIDs.get(i)));
            }
        });
        //所有城市列表点击事件
        allCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentCity.setText(allCityNames.get(i));
                currentCityId.setText(String.valueOf(allCityIDs.get(i)));
            }
        });
        localMapSwipeList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //index的值就是在SwipeMenu依次添加SwipeMenuItem顺序值，类似数组的下标。
                //从0开始，依次是：0、1、2、3...
                MKOLUpdateElement e = (MKOLUpdateElement) localAdapter.getItem(position);
                switch (index) {
                    case 0:
                        if (e.ratio != 100) {
                            Toast.makeText(MeterMapDownloadActivity.this, "下载完成后才能查看哦！", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(MeterMapDownloadActivity.this, LocalCityMapActivity.class);
                            intent.putExtra("x", e.geoPt.longitude);
                            intent.putExtra("y", e.geoPt.latitude);
                            intent.putExtra("level", 11.0f);
                            startActivity(intent);
                        }
                        break;
                    case 1:
                        // delete
                        showdeleteWindow(e.cityID);
                        break;
                    default:
                        break;
                }
                // false : close the menu; true : not close the menu
                // false : 当用户触发其他地方的屏幕时候，自动收起菜单。
                // true : 不改变已经打开菜单的样式，保持原样不收起。
                return false;
            }
        });
        //点击item后，若未下载完成的可以点击开始继续下载
        localMapSwipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MKOLUpdateElement element = (MKOLUpdateElement) localAdapter.getItem(position);
                currentCity.setText(element.cityName);
                currentCityId.setText(String.valueOf(element.cityID));
            }
        });
        //长按删除
        localMapSwipeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MKOLUpdateElement e = (MKOLUpdateElement) localAdapter.getItem(position);
                showdeleteWindow(e.cityID);
                return false;
            }
        });

        // 监测用户在ListView的SwipeMenu侧滑事件。
        localMapSwipeList.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int pos) {
                Log.i("位置:" + pos, "开始侧滑...");
                //swipeMenuListView.smoothOpenMenu(pos);
            }

            @Override
            public void onSwipeEnd(int pos) {
                Log.i("位置:" + pos, "侧滑结束.");
            }
        });
        localMapSwipeList.setCloseInterpolator(new BounceInterpolator());
        localMapSwipeList.setOpenInterpolator(new BounceInterpolator());
    }

    //弹出是否删除提示框
    public void showdeleteWindow(final int cityID) {
        inflater = LayoutInflater.from(MeterMapDownloadActivity.this);
        deleteView = inflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        deleteWindow = new PopupWindow(deleteView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        TextView tips = (TextView) deleteView.findViewById(R.id.tips);
        RadioButton cancel = (RadioButton) deleteView.findViewById(R.id.cancel_rb);
        RadioButton confirm = (RadioButton) deleteView.findViewById(R.id.save_rb);
        //设置点击事件
        tips.setText("确认要删除该离线地图吗？");
        confirm.setText("确定");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDownloadMap(cityID);
                progress.setText("0%");
                deleteWindow.dismiss();
            }
        });
        deleteWindow.update();
        deleteWindow.setFocusable(true);
        deleteWindow.setTouchable(true);
        deleteWindow.setOutsideTouchable(true);
        deleteWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        deleteWindow.setAnimationStyle(R.style.camera);
        deleteWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);
        deleteWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MeterMapDownloadActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterMapDownloadActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterMapDownloadActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterMapDownloadActivity.this.getWindow().setAttributes(lp);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    MeterMapDownloadActivity.this.finish();
                    break;
                case R.id.city_rb:
                    showCityListLayout();
                    break;
                case R.id.download_rb:
                    showlocalCityLayout();
                    break;
                case R.id.start_download:
                    if (!"".equals(currentCityId.getText().toString())) {
                        if (localAdapter.getCount() > 0) {
                            for (int i = 0; i < localAdapter.getCount(); i++) {
                                MKOLUpdateElement e = (MKOLUpdateElement) localAdapter.getItem(i);
                                if (String.valueOf(e.cityID).equals(currentCityId.getText().toString())) {
                                    if (e.ratio != 100) {
                                        stopDownload.setEnabled(true);
                                        startDownloadMap(currentCityId.getText().toString());
                                        downloadRb.setChecked(true);
                                        showlocalCityLayout();
                                        break;
                                    } else {
                                        downloadRb.setChecked(true);
                                        showlocalCityLayout();
                                        progress.setText("已下载完成");
                                        Toast.makeText(MeterMapDownloadActivity.this, e.cityName + "离线地图已下载！", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                } else {
                                    if (i == (localAdapter.getCount() - 1)) {
                                        stopDownload.setEnabled(true);
                                        startDownloadMap(currentCityId.getText().toString());
                                        downloadRb.setChecked(true);
                                        showlocalCityLayout();
                                    }
                                }
                            }
                        } else {
                            stopDownload.setEnabled(true);
                            startDownloadMap(currentCityId.getText().toString());
                            downloadRb.setChecked(true);
                            showlocalCityLayout();
                        }
                    } else {
                        Toast.makeText(MeterMapDownloadActivity.this, "当前城市ID为空！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.stop_download:
                    stopDownloadMap(currentCityId.getText().toString());
                    stopDownload.setEnabled(false);
                    break;
                case R.id.city_search:
                    if (!"".equals(cityInput.getText().toString())) {
                        searchCity();
                    } else {
                        Toast.makeText(MeterMapDownloadActivity.this, "请输入城市名称！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public String formatDataSize(int size) {
        String ret;
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

    /**
     * 搜索离线需市
     */
    public void searchCity() {
        ArrayList<MKOLSearchRecord> records = mOffline.searchCity(cityInput.getText().toString());
        if (records == null || records.size() != 1) {
            Toast.makeText(MeterMapDownloadActivity.this, "未搜索到城市信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        currentCity.setText(cityInput.getText().toString());
        currentCityId.setText(String.valueOf(records.get(0).cityID));
    }

    /**
     * 开始下载
     */
    public void startDownloadMap(String cityID) {
        int cityid = Integer.parseInt(cityID);
        mOffline.start(cityid);
        Toast.makeText(MeterMapDownloadActivity.this, "开始下载离线地图，城市编号为: " + cityid, Toast.LENGTH_SHORT).show();
        updateView();
    }

    /**
     * 暂停下载
     */
    public void stopDownloadMap(String cityID) {
        int cityid = Integer.parseInt(cityID);
        mOffline.pause(cityid);
        Toast.makeText(MeterMapDownloadActivity.this, "暂停下载离线地图，城市编号为: " + cityid, Toast.LENGTH_SHORT).show();
        updateView();
    }

    /**
     * 删除离线地图
     */
    public void deleteDownloadMap(int cityID) {
        mOffline.remove(cityID);
        Toast.makeText(this, "删除离线地图，城市编号为: " + cityID, Toast.LENGTH_SHORT).show();
        updateView();
    }

    /**
     * 更新状态显示
     */
    public void updateView() {
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<>();
        }
        if(localMapList.size() == 0){
            noData.setVisibility(View.VISIBLE);
        }else {
            noData.setVisibility(View.GONE);
        }

        localAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        int cityid = Integer.parseInt(currentCityId.getText().toString());
        MKOLUpdateElement temp = mOffline.getUpdateInfo(cityid);
        if (temp != null && temp.status == MKOLUpdateElement.DOWNLOADING) {
            mOffline.pause(cityid);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        /**
         * 退出时，销毁离线地图模块
         */
        mOffline.destroy();
        super.onDestroy();
    }

    /**
     * 切换至城市列表
     */
    public void showCityListLayout() {
        rightLayout.setVisibility(View.GONE);
        leftLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 切换至下载管理列表
     */
    public void showlocalCityLayout() {
        rightLayout.setVisibility(View.VISIBLE);
        leftLayout.setVisibility(View.GONE);
    }

    @Override
    public void onGetOfflineMapState(int i, int i1) {
        switch (i) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(i1);
                // 处理下载进度更新提示
                if (update != null) {
                    progress.setText(String.format("%s : %d%%", update.cityName, update.ratio));
                    updateView();
                    if (update.ratio == 100) {
                        Toast.makeText(MeterMapDownloadActivity.this, update.cityName + "离线地图下载完成！ ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", i1));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
            default:
                break;
        }
    }

    /**
     * 离线地图管理列表适配器
     */
    public class LocalMapAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;

        @Override
        public int getCount() {
            return localMapList.size();
        }

        @Override
        public Object getItem(int index) {
            return localMapList.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int index, View view, ViewGroup arg2) {
            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                layoutInflater = LayoutInflater.from(MeterMapDownloadActivity.this);
                view = layoutInflater.inflate(R.layout.meter_local_map_list_item, null);
                viewHolder.cityName = (TextView) view.findViewById(R.id.city_name);
                viewHolder.cityId = (TextView) view.findViewById(R.id.city_id);
                viewHolder.update = (TextView) view.findViewById(R.id.update);
                viewHolder.ratio = (TextView) view.findViewById(R.id.ratio);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            final MKOLUpdateElement e = (MKOLUpdateElement) getItem(index);
            viewHolder.ratio.setText(e.ratio + "%");
            viewHolder.cityName.setText(e.cityName);
            viewHolder.cityId.setText(String.valueOf(e.cityID));
            if (e.update) {
                viewHolder.update.setText("可更新");
                viewHolder.update.setTextColor(getResources().getColor(R.color.red));
            } else {
                viewHolder.update.setText("最新");
                viewHolder.update.setTextColor(getResources().getColor(R.color.text_gray));
            }
            return view;
        }
    }

    public class ViewHolder {
        TextView cityName;
        TextView cityId;
        TextView update;
        TextView ratio;
    }
}
