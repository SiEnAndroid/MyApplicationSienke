package com.example.administrator.thinker_soft.mode;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/8/10 0010.
 */
public class TempDataTools {
    private SQLiteDatabase db;  //数据库
    private LayoutInflater layoutInflater;
    private View loadingView;
    private AnimationDrawable animationDrawable;
    private PopupWindow popupWindow;

    public TempDataTools(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 将抄表用户数据保存至本地 MeterUser 表
     */
    public void insertMeterUserData() {
        ContentValues values = new ContentValues();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                values.put("login_user_id", "0");          //登录人ID
                values.put("meter_reader_id", "1");    //抄表员ID
                values.put("meter_reader_name", "张三");        //抄表员名称
                values.put("meter_date", "暂无");                                                      //抄表时间
                values.put("user_phone", "18323407446");                    //用户电话
                values.put("user_amount", "0");                      //用户余额
                values.put("meter_degrees", "10");             //上月读数
                values.put("meter_number", "123");                //表编号
                values.put("arrearage_months", "2");                      //欠费月数
                values.put("mix_state", "0");                     //混合使用状态（0正常  1混合）
                values.put("meter_order_number", "10" + i + j);           //抄表序号
                values.put("arrearage_amount", "0");          //欠费金额
                values.put("area_id", "2");                         //抄表本分区ID
                values.put("area_name", "江北");                      //抄表本分区名称
                values.put("user_name", "李四" + j);                      //用户名
                values.put("last_month_dosage", "0");                //上月用量
                values.put("property_id", "1");               //性质ID
                values.put("property_name", "居民");            //性质名称
                values.put("user_id", "6666" + i + j);                          //用户ID
                values.put("book_id", "" + i);                         //抄表本ID
                values.put("float_range", "0");                      //浮动范围
                values.put("meterState", "false");                                                     //抄表状态
                values.put("dosage_change", "0");              //更换量
                values.put("user_address", "重庆市江北区鲁溉路");                //用户地址
                values.put("start_dosage", "0");                    //启用量
                values.put("old_user_id", "999999");                  //用户老编号
                values.put("book_name", "测试抄表本" + i);                      //抄表本名称
                values.put("meter_model", "2");                   //表型号
                values.put("rubbish_cost", "0");                  //垃圾费
                values.put("remission", "0");                     //加减量
                values.put("locationAddress", "未定位");                                                //定位地址
                values.put("file_name", "测试文件" + i);                            //本地文件名
                values.put("uploadState", "false");                                                     //上传状态
                //下面这些个字段抄表完成后需上传
                values.put("this_month_dosage", "");                                               //本月用量
                values.put("this_month_end_degree", "");                                               //本月止度
                values.put("n_jw_x", "未获取");                                                        //纬度
                values.put("n_jw_y", "未获取");                                                        //经度
                values.put("d_jw_time", "未获取");                                                     //抄表时间

                db.insert("MeterUser", null, values);
            }
        }
        Log.i("insertMeterUserData", "用户数据插入成功");
    }

    /**
     * 将抄表本数据保存至本地 MeterBook 表
     */
    public void insertMeterBook() {
        ContentValues values = new ContentValues();
        for (int i = 0; i < 5; i++) {
            values.put("bookId", "" + i);                                                             //抄表本ID
            values.put("bookName", "测试抄表本" + i);                                                         //抄表本名称
            values.put("fileName", "测试文件" + i);                                //抄表文件名称
            values.put("login_user_id", "0");             //当前登录人的ID
            values.put("login_user_name", "SUPER");        //当前登录人的名称
            db.insert("MeterBook", null, values);
        }
    }

    /**
     * 将抄表文件数据保存至本地 MeterFile 表
     */
    public void insertMeterFile() {
        ContentValues values = new ContentValues();
        for (int i = 0; i < 5; i++) {
            values.put("fileName", "测试文件" + i);                                //抄表文件名称
            values.put("login_user_id", "0");             //当前登录人的ID
            values.put("login_user_name", "SUPER");        //当前登录人的名称
            db.insert("MeterFile", null, values);
        }
    }

    //show获取数据加载动画
    public void showPopupwindow(final Activity activity, View view) {
        layoutInflater = LayoutInflater.from(activity);
        loadingView = layoutInflater.inflate(R.layout.popupwindow_query_loading, null);
        popupWindow = new PopupWindow(loadingView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ImageView frameAnimation = (ImageView) loadingView.findViewById(R.id.frame_animation);
        TextView tips = (TextView) loadingView.findViewById(R.id.tips);
        tips.setText("正在初始化演示数据，请稍后...");
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(activity.getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.update();
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        backgroundAlpha(activity, 0.6F);   //背景变暗
        startFrameAnimation(frameAnimation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(activity, 1.0F);
            }
        });
    }

    public void windowDissmiss() {
        Log.i("windowDissmiss", "窗口消失进来了！");
        popupWindow.dismiss();
    }

    //开始帧动画
    public void startFrameAnimation(ImageView view) {
        view.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) view.getDrawable();
        animationDrawable.start();
    }

    //设置背景透明度
    public void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }
}
