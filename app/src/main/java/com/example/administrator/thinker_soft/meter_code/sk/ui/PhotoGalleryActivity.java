package com.example.administrator.thinker_soft.meter_code.sk.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.adapter.MyPhotoAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.PhotoPathBean;
import com.example.administrator.thinker_soft.meter_code.sk.widget.PhotoViewPager;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.io.File;
import java.util.ArrayList;

/**
 * 图片查看器
 * Created by Administrator on 2018/6/27.
 */

public class PhotoGalleryActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = PhotoGalleryActivity.class.getSimpleName();
    private PhotoViewPager mViewPager;
    private int currentPosition;
    private MyPhotoAdapter adapter;
    private TextView mTvImageCount;
    private ImageView mSaveImage;//返回
    private ImageView delete;//删除

    //  private ArrayList<String> cropPathLists;  //原始的图片路径集合
    private ArrayList<PhotoPathBean> pathList;
    private LayoutInflater inflater;  //转换器
    private View deleteView;
    private PopupWindow popupWindow;
    private LinearLayout rootLinearlayout;
    private String newUserId;
    private long currentId;
    private TextView tips;
    private RadioButton cancelRb, saveRb;
    private SQLiteDatabase db;  //数据库
    private MySqliteHelper helper; //数据库帮助类
    private SharedPreferences sharedPreferences_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (PhotoViewPager) findViewById(R.id.view_pager_photo);
        mTvImageCount = (TextView) findViewById(R.id.tv_image_count);
        mSaveImage = (ImageView) findViewById(R.id.back);
        delete= (ImageView) findViewById(R.id.delete);
        rootLinearlayout= (LinearLayout) findViewById(R.id.ly);

        mSaveImage.setOnClickListener(this);
        delete.setOnClickListener(this);

    }

    private void initData() {
        helper = new MySqliteHelper(PhotoGalleryActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getReadableDatabase();
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition", 0);
        newUserId = intent.getStringExtra("newUserId");
        // cropPathLists = intent.getStringArrayListExtra("cropPathLists");
        pathList = (ArrayList<PhotoPathBean>) intent.getSerializableExtra("cropPathLists");
//        cropPathLists = new ArrayList<>();
//        for (PhotoPathBean bean:pathList){
//            cropPathLists.add(bean.getCropPath());
//        }

//        Log.i("MyPhotoGalleryActivity", "获取到的图片路径为：" + pathList.get(0).getCropPath());
        sharedPreferences_login = this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        adapter = new MyPhotoAdapter(pathList, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition+1 + "/" + pathList.size());

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentId = position;
                currentPosition = position;
                mTvImageCount.setText(currentPosition + 1 + "/" + pathList.size());
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.delete){
            showDeletePopup();
        }else {
            Intent intent = new Intent();
            Bundle bundle=new Bundle();
            bundle.putSerializable("cropPathLists_back",pathList);
            intent.putExtras(bundle);
            //  intent.putStringArrayListExtra("cropPathLists_back", pathList);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    //弹出删除照片前提示popupwindow
    public void showDeletePopup() {
        inflater = LayoutInflater.from(PhotoGalleryActivity.this);
        deleteView = inflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        popupWindow = new PopupWindow(deleteView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        tips = (TextView) deleteView.findViewById(R.id.tips);
        cancelRb = (RadioButton) deleteView.findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) deleteView.findViewById(R.id.save_rb);
        //设置点击事件
        tips.setText("确定要删除该照片吗？");
        saveRb.setText("确定");
        cancelRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        saveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (pathList.size() == 1) {   //如果还有一张图片，则删除了直接返回上个页面
                    popupWindow.dismiss();
                    File file = new File(pathList.get((int) currentId).getCropPath());
                    file.delete();
                    Log.i("showDeletePopup", "删除本地图片：" + currentId);
                    pathList.remove(currentPosition);
                    deletePhoto();
                    Log.i("showDeletePopup", "图片集合移除了：" + currentPosition);
                    adapter.notifyDataSetChanged();
                    Intent intent = new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("cropPathLists_back",pathList);
                    intent.putExtras(bundle);
                    // intent.putStringArrayListExtra("cropPathLists_back", pathList);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {  //删除刷新
                    popupWindow.dismiss();
                    File file = new File(pathList.get((int) currentId).getCropPath());
                    file.delete();
                    //FileUtil.deleteDir(pathList.get((int) currentId).getCropPath());
                    Log.i("showDeletePopup", "多张时删除本地图片：" + currentId);
                    pathList.remove(currentPosition);
                    deletePhoto();
                    Log.i("showDeletePopup", "多张时图片集合移除了：" + currentPosition);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
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

    //读取保存到本地的图片数据，并上传服务器
    public void deletePhoto() {
        Cursor cursor = db.rawQuery("select * from security_photo where newUserId=? and loginUserId=?", new String[]{newUserId, sharedPreferences_login.getString("userId", "")});//查询并获得游标
        while (cursor.moveToNext()) {
            String path=cursor.getString(cursor.getColumnIndex("photoPath"));
            db.delete("security_photo","photoPath=?",new String[]{path});
        }
        cursor.close(); //游标关闭
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = PhotoGalleryActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            PhotoGalleryActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            PhotoGalleryActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        PhotoGalleryActivity.this.getWindow().setAttributes(lp);
    }

    /**
     * 监听Back键按下事件,方法1:
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可
     */
    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("按下了back键   onBackPressed()");
    }*/

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            //  intent.putStringArrayListExtra("cropPathLists_back", pathList);
            Bundle bundle=new Bundle();
            bundle.putSerializable("cropPathLists_back",pathList);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
