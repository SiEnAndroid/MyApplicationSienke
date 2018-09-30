package com.example.administrator.thinker_soft.Security_check.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
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
import com.example.administrator.thinker_soft.Security_check.adapter.TaskFileDeleteListAdapter;
import com.example.administrator.thinker_soft.mode.MyAnimationUtils;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.Security_check.model.TaskChoose;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/15 0015.
 */
public class TaskFileDeleteActivity extends Activity {
    private ImageView back;
    private TextView totalCountTv,noData;
    private int totalCount;
    private CardView deleteAll;
    private LinearLayout rootLinearlayout;
    private SwipeMenuListView swipeMenuListView;
    private SharedPreferences sharedPreferences_login, sharedPreferences;
    private LayoutInflater inflater;  //转换器
    private View view,deleteView,loadView;
    private PopupWindow popupWindow,deleteWindow,loadingWindow;
    private RadioButton cancel,confirm;
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;
    private TaskFileDeleteListAdapter adapter;
    private List<TaskChoose> taskfileList = new ArrayList<>();
    private TaskChoose item;
    private SQLiteDatabase db;  //数据库
    private MySqliteHelper helper; //数据库帮助类
    private int restCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_task_file);

        bindView();
        defaultSetting();
        setViewClickListener();
    }

    //绑定控件
    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        totalCountTv = (TextView) findViewById(R.id.total_count);
        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.swipe_list_view);
        noData = (TextView) findViewById(R.id.no_data);
        deleteAll = (CardView) findViewById(R.id.delete_all);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);
    }

    //初始化设置
    private void defaultSetting() {
        MySqliteHelper helper = new MySqliteHelper(TaskFileDeleteActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences_login = TaskFileDeleteActivity.this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
//        sharedPreferences = TaskFileDeleteActivity.this.getSharedPreferences(sharedPreferences_login.getString("login_name", "") + "data", Context.MODE_PRIVATE);
        sharedPreferences = TaskFileDeleteActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId", "") + "data", Context.MODE_PRIVATE);
        new Thread() {
            @Override
            public void run() {
                super.run();
                getTaskData(sharedPreferences_login.getString("userId", ""));
            }
        }.start();
    }

    //点击事件
    public void setViewClickListener() {
        back.setOnClickListener(onClickListener);
        deleteAll.setOnClickListener(onClickListener);
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
                    TaskFileDeleteActivity.this.finish();
                    break;
                case R.id.delete_all:
                    clearAllDataPopup();
                    break;
                default:
                    break;
            }
        }
    };

    //弹出是否删除提示框
    public void showdeleteWindow(final int position) {
        inflater = LayoutInflater.from(TaskFileDeleteActivity.this);
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
                item = (TaskChoose) adapter.getItem(position);
                taskfileList.remove(position);
                adapter.notifyDataSetChanged();
                deleteWindow.dismiss();
                showLoadingPopup();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(800);
                            db.delete("Task", "loginUserId=? and taskId=?", new String[]{sharedPreferences_login.getString("userId", ""),item.getTaskNumber()});
                            db.delete("User", "loginUserId=? and taskId=?", new String[]{sharedPreferences_login.getString("userId", ""),item.getTaskNumber()});
                            if(!"".equals(sharedPreferences.getString("currentTaskId",""))){
                                if(sharedPreferences.getString("currentTaskId","").equals(item.getTaskNumber())){
                                    sharedPreferences.edit().remove("currentTaskId").apply();
                                    sharedPreferences.edit().remove("currentTaskName").apply();
                                    sharedPreferences.edit().putString("currentTaskId","").apply();
                                    sharedPreferences.edit().putString("currentTaskName","").apply();
                                   // sharedPreferences.edit().remove("currentTaskId").commit();
                                }

                            }
                            handler.sendEmptyMessage(2);
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

    //弹出清空数据前提示popupwindow
    public void clearAllDataPopup() {
        inflater = LayoutInflater.from(TaskFileDeleteActivity.this);
        view = inflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        TextView tips = (TextView) view.findViewById(R.id.tips);
        cancel = (RadioButton) view.findViewById(R.id.cancel_rb);
        confirm = (RadioButton) view.findViewById(R.id.save_rb);
        //设置点击事件
        tips.setText("确定要清空所有本地数据吗！（包括任务、用户和照片数据）");
        confirm.setText("确定");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showLoadingPopup();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(800);
                            clearData();
                            clearPhoto();
                            if(!"".equals(sharedPreferences.getString("currentTaskId",""))){
                                if(sharedPreferences.getString("currentTaskId","").equals(item.getTaskNumber())){
                                    sharedPreferences.edit().remove("currentTaskId").apply();

                                }

                            }
                            handler.sendEmptyMessage(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                sharedPreferences.edit().putString("currentTaskId","").apply();
                sharedPreferences.edit().putString("currentTaskName","").apply();
                String currentTaskId = sharedPreferences.getString("currentTaskId", "");
                Toast.makeText(TaskFileDeleteActivity.this, "清除数据成功！", Toast.LENGTH_SHORT).show();
                sharedPreferences.edit().putBoolean("clear_data",true);
                sharedPreferences.edit().apply();
            }
        });
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    private void clearData() {
        helper = new MySqliteHelper(TaskFileDeleteActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        sharedPreferences.edit().clear().apply();
        db.delete("User", "loginUserId=?", new String[]{sharedPreferences_login.getString("userId","")});  //删除User表中当前用户的所有数据（官方推荐方法）
        db.delete("Task", "loginUserId=?", new String[]{sharedPreferences_login.getString("userId","")});  //删除Task表中当前用户的所有数据
        //设置id从1开始（sqlite默认id从1开始），若没有这一句，id将会延续删除之前的id
        /*db.execSQL("update sqlite_sequence set seq=0 where name='User'");
        db.execSQL("update sqlite_sequence set seq=0 where name='Task'");*/
    }

    private boolean clearPhoto() {
       // File file = new File(Environment.getExternalStorageDirectory() , "ThinkerSoft_"+sharedPreferences_login.getString("userId","") + "/crop");
       File file = new File(Environment.getExternalStorageDirectory() , "ThinkerSoft_"+sharedPreferences_login.getString("userId","") + "/Icon_image");

        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                Log.i("clearPhoto=>", "删除的照片文件夹路径为：" + file.getPath());
                file.delete();
                return true;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File childFiles[] = file.listFiles(); // 声明目录下所有的文件 files[];
                if (childFiles == null || childFiles.length == 0) {
                    file.delete();
                    return true;
                }
                for (int i = 0; i < childFiles.length; i++) { // 遍历目录下所有的文件
                    childFiles[i].delete(); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
            return true;
        } else {
            Log.i("clearPhoto=>", "文件不存在！");
            return false;
        }
    }

    //读取下载到本地的任务数据
    public void getTaskData(String loginUserId) {
        Cursor cursor = db.rawQuery("select * from Task where loginUserId=?", new String[]{loginUserId});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        totalCount = cursor.getCount();
        if (cursor.getCount() == 0) {
            handler.sendEmptyMessage(0);
            return;
        }
        while (cursor.moveToNext()) {
            TaskChoose item = new TaskChoose();
            item.setTaskName(cursor.getString(cursor.getColumnIndex("taskName")));
            item.setTaskNumber(cursor.getString(cursor.getColumnIndex("taskId")));
            item.setTotalUserNumber(cursor.getString(cursor.getColumnIndex("totalCount")));
            item.setRestCount("(" + cursor.getString(cursor.getColumnIndex("restCount")) + ")");
            taskfileList.add(item);
        }
        //cursor游标操作完成以后,一定要关闭
        cursor.close();
        handler.sendEmptyMessage(1);
    }

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
            deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.red)));
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    deleteAll.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                    Toast.makeText(TaskFileDeleteActivity.this, "您还没有任务哦，快去下载吧！~", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    totalCountTv.setText("共" + totalCount + "个");
                    adapter = new TaskFileDeleteListAdapter(TaskFileDeleteActivity.this,taskfileList);
                    swipeMenuListView.setAdapter(adapter);
                    swipeMenuListView.setMenuCreator(creator);
                    swipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
                    MyAnimationUtils.viewGroupOutAnimation(TaskFileDeleteActivity.this, swipeMenuListView, 0.1F);
                    break;
                case 2:
                    loadingWindow.dismiss();
                    Toast.makeText(TaskFileDeleteActivity.this, "成功删除 '" + item.getTaskName() + "' 文件！", Toast.LENGTH_SHORT).show();
                    totalCountTv.setText("共" + taskfileList.size() + "个");
                    if(taskfileList.size() == 0){
                        deleteAll.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    taskfileList.clear();
                    adapter.notifyDataSetChanged();
                    loadingWindow.dismiss();
                    totalCountTv.setText("共" + taskfileList.size() + "个");
                    noData.setVisibility(View.VISIBLE);
                    deleteAll.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //show加载动画
    public void showLoadingPopup() {
        inflater = LayoutInflater.from(TaskFileDeleteActivity.this);
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
        WindowManager.LayoutParams lp = TaskFileDeleteActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            TaskFileDeleteActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            TaskFileDeleteActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        TaskFileDeleteActivity.this.getWindow().setAttributes(lp);
    }
}
