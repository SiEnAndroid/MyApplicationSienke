package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.adapter.MeterFileSelectListAdapter;
import com.example.administrator.thinker_soft.meter_code.model.MeterSingleSelectItem;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class MeterDeleteFileActivity extends Activity {
    private ImageView back;
    private TextView totalCount,noData;
    private int fileCount;
    private SwipeMenuListView swipeMenuListView;
    private List<MeterSingleSelectItem> fileList = new ArrayList<>();
    private MeterFileSelectListAdapter fileAdapter;
    private SQLiteDatabase db;  //数据库
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    private LayoutInflater inflater;  //转换器
    private View deleteView,loadView;
    private PopupWindow deleteWindow,loadingWindow;
    private RadioButton cancel,confirm;
    private LinearLayout rootLinearlayout;
    private  MeterSingleSelectItem item;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_meter_file);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        totalCount = (TextView) findViewById(R.id.total_count);
        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.swipe_list_view);
        noData = (TextView) findViewById(R.id.no_data);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(MeterDeleteFileActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = MeterDeleteFileActivity.this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = MeterDeleteFileActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        new Thread() {
            @Override
            public void run() {
                super.run();
                getFileInfo();
            }
        }.start();
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //index的值就是在SwipeMenu依次添加SwipeMenuItem顺序值，类似数组的下标。
                //从0开始，依次是：0、1、2、3...
                switch (index) {
                    case 0:
                        // delete
                        showdeleteWindow(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                // false : 当用户触发其他地方的屏幕时候，自动收起菜单。
                // true : 不改变已经打开菜单的样式，保持原样不收起。
                return false;
            }
        });
        //长按删除
        swipeMenuListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showdeleteWindow(position);
                return false;
            }
        });

        // 监测用户在ListView的SwipeMenu侧滑事件。
        swipeMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

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
        swipeMenuListView.setCloseInterpolator(new BounceInterpolator());
        swipeMenuListView.setOpenInterpolator(new BounceInterpolator());
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    MeterDeleteFileActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    SwipeMenuCreator creator = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {
            /*// create "open" item
			SwipeMenuItem openItem = new SwipeMenuItem(
					getApplicationContext());
			// set item background
			openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
					0xCE)));
			// set item width
			openItem.setWidth(dp2px(90));
			// set item title
			openItem.setTitle("Open");
			// set item title fontsize
			openItem.setTitleSize(18);
			// set item title font color
			openItem.setTitleColor(Color.WHITE);
			// add to menu
			menu.addMenuItem(openItem);*/

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(dp2px(70));
            // set item title
            deleteItem.setTitle("删除");
            // set item title fontsize
            deleteItem.setTitleSize(16);
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

    //查询抄表文件信息
    public void getFileInfo() {
        fileList.clear();
        Cursor cursor;
        if (sharedPreferences.getBoolean("show_temp_data", false)) {   //显示演示数据
            cursor = db.rawQuery("select * from MeterFile where login_user_id=?", new String[]{"0"});//查询并获得游标
        } else {
            cursor = db.rawQuery("select * from MeterFile where login_user_id=?", new String[]{sharedPreferences_login.getString("userId", "")});//查询并获得游标
        }

        Log.i("meterHomePage", "所有表册ID个数为：" + cursor.getCount());
        fileCount = cursor.getCount();
        //如果游标为空，则显示没有数据图片
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(2);
            return;
        }
        while (cursor.moveToNext()) {
            MeterSingleSelectItem item = new MeterSingleSelectItem();
            item.setName(cursor.getString(cursor.getColumnIndex("fileName")));
            fileList.add(item);
        }
        handler.sendEmptyMessage(0);
        cursor.close();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    totalCount.setText("共" + fileCount + "个");
                    noData.setVisibility(View.GONE);
                    fileAdapter = new MeterFileSelectListAdapter(MeterDeleteFileActivity.this, fileList, 1);
                    swipeMenuListView.setAdapter(fileAdapter);
                    swipeMenuListView.setMenuCreator(creator);
                    swipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
                    MyAnimationUtils.viewGroupOutAnimation(MeterDeleteFileActivity.this, swipeMenuListView, 0.1F);
                    break;
                case 1:
                    loadingWindow.dismiss();
                    Toast.makeText(MeterDeleteFileActivity.this, "成功删除 '" + item.getName() + "' 文件！", Toast.LENGTH_SHORT).show();
                    totalCount.setText("共" + fileList.size() + "个");
                    break;
                case 2:
                    noData.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //弹出是否删除提示框
    public void showdeleteWindow(final int position) {
        inflater = LayoutInflater.from(MeterDeleteFileActivity.this);
        deleteView = inflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        deleteWindow = new PopupWindow(deleteView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        TextView tips = (TextView) deleteView.findViewById(R.id.tips);
        cancel = (RadioButton) deleteView.findViewById(R.id.cancel_rb);
        confirm = (RadioButton) deleteView.findViewById(R.id.save_rb);
        //设置点击事件
        tips.setText("删除将不可恢复，确认删除该文件吗？");
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
                item = (MeterSingleSelectItem) fileAdapter.getItem(position);
                fileList.remove(position);
                fileAdapter.notifyDataSetChanged();
                deleteWindow.dismiss();
                showLoadingPopup();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(1500);
                            db.delete("MeterFile", "login_user_id=? and fileName=?", new String[]{sharedPreferences_login.getString("userId", ""),item.getName()});
                            db.delete("MeterBook", "login_user_id=? and fileName=?", new String[]{sharedPreferences_login.getString("userId", ""),item.getName()});
                            db.delete("MeterUser", "login_user_id=? and file_name=?", new String[]{sharedPreferences_login.getString("userId", ""),item.getName()});
                            db.delete("MeterPhoto", "loginUserId=? and fileName=?", new String[]{sharedPreferences_login.getString("userId", ""),item.getName()});
                            //设置id从1开始（sqlite默认id从1开始），若没有这一句，id将会延续删除之前的id
                            db.execSQL("update sqlite_sequence set seq=0 where name='MeterFile'");
                            db.execSQL("update sqlite_sequence set seq=0 where name='MeterBook'");
                            db.execSQL("update sqlite_sequence set seq=0 where name='MeterUser'");
                            db.execSQL("update sqlite_sequence set seq=0 where name='MeterPhoto'");
                            if(!"".equals(sharedPreferences.getString("currentFileName",""))){
                                if(sharedPreferences.getString("currentFileName","").equals(item.getName())){
                                    sharedPreferences.edit().remove("currentFileName").apply();
                                }
                            }
                            handler.sendEmptyMessage(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
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

    //show加载动画
    public void showLoadingPopup() {
        inflater = LayoutInflater.from(MeterDeleteFileActivity.this);
        loadView = inflater.inflate(R.layout.popupwindow_query_loading, null);
        loadingWindow = new PopupWindow(loadView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameAnimation = (ImageView) loadView.findViewById(R.id.frame_animation);
        TextView tips = (TextView) loadView.findViewById(R.id.tips);
        tips.setText("正在删除本地数据，请稍后...");
        loadingWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        loadingWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loadingWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        loadingWindow.setAnimationStyle(R.style.camera);
        loadingWindow.update();
        loadingWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        startFrameAnimation();
        loadingWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = MeterDeleteFileActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            MeterDeleteFileActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            MeterDeleteFileActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        MeterDeleteFileActivity.this.getWindow().setAttributes(lp);
    }
}
