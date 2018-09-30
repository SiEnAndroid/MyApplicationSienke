package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

/**
 * Created by Administrator on 2017/6/15.
 */
public class BusinessEmailInfoActivity extends Activity {

    private ImageView back, more;
    private PopupWindow window;
    private RadioButton huifu, zhuanfa, detail;
    private TextView type, sendName, addressee, time, fujian, content, name;
    private String timeString;
    private SQLiteDatabase db;  //数据库
    private Cursor cursor;
    private View surePopup;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private RadioButton cancelRb, saveRb;
    private TextView tips;
    private LinearLayout rootLinearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_email_info);//已发送详细

        bindView();//绑定控件
        defaultSetting();
        setOnClickListener();//点击事件
        queryOaEmail();
    }

    private void bindView() {
        back = (ImageView) findViewById(R.id.back);
        more = (ImageView) findViewById(R.id.more);
        type = (TextView) findViewById(R.id.type);
        sendName = (TextView) findViewById(R.id.send_name);
        addressee = (TextView) findViewById(R.id.addressee);
        time = (TextView) findViewById(R.id.time);
        fujian = (TextView) findViewById(R.id.fujian);
        content = (TextView) findViewById(R.id.content);
        name = (TextView) findViewById(R.id.name);
        rootLinearlayout = (LinearLayout) findViewById(R.id.rootLinearlayout);
    }

    private void defaultSetting() {
        name.setText("已发送");
        final MySqliteHelper helper = new MySqliteHelper(BusinessEmailInfoActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        Intent intent = getIntent();
        timeString = intent.getStringExtra("time");
        time.setText(timeString);
        more.setVisibility(View.GONE);
    }

    private void setOnClickListener() {
        back.setOnClickListener(clickListener);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = BusinessEmailInfoActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_business_email_inbox, null);
                window = new PopupWindow(popupView, 600, 400);
                window.setAnimationStyle(R.style.Popupwindow);
                window.setFocusable(true);
                backgroundAlpha(0.6F);   //背景变暗
                window.setOutsideTouchable(true);
                window.update();
                window.showAsDropDown(more, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1.0F);
                        more.setClickable(true);
                    }
                });

                huifu = (RadioButton) popupView.findViewById(R.id.huifu);
                zhuanfa = (RadioButton) popupView.findViewById(R.id.zhuanfa);
                detail = (RadioButton) popupView.findViewById(R.id.detail);

                huifu.setOnClickListener(clickListener);
                zhuanfa.setOnClickListener(clickListener);
                detail.setOnClickListener(clickListener);
            }
        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.huifu:
                    Intent intent = new Intent(BusinessEmailInfoActivity.this, BusinessAnswerEmailActivity.class);
                    startActivity(intent);
                    window.dismiss();
                    break;
                case R.id.zhuanfa:
                    Intent intent1 = new Intent(BusinessEmailInfoActivity.this, BusinessIntransitEmailActivity.class);
                    startActivity(intent1);
                    window.dismiss();
                    break;
                case R.id.detail:
                    window.dismiss();
                    /*showSurePopup();*/
                    break;
            }
        }
    };

    private void showSurePopup() {
        layoutInflater = LayoutInflater.from(BusinessEmailInfoActivity.this);
        surePopup = layoutInflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        popupWindow = new PopupWindow(surePopup, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        cancelRb = (RadioButton) surePopup.findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) surePopup.findViewById(R.id.save_rb);
        tips = (TextView) surePopup.findViewById(R.id.tips);
        tips.setText("确定删除本条数据");
        saveRb.setText("确定");
        //设置点击事件
        cancelRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        saveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete("OaEmail", "time=?", new String[]{timeString});  //删除OaUser表中所有数据（官方推荐方法）
                db.execSQL("update sqlite_sequence set seq=0 where name='OaEmail'");
                Toast.makeText(BusinessEmailInfoActivity.this, "清除数据成功！", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                setResult(RESULT_OK);
                finish();
            }
        });
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = BusinessEmailInfoActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            BusinessEmailInfoActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            BusinessEmailInfoActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        BusinessEmailInfoActivity.this.getWindow().setAttributes(lp);
    }

    /**
     * 根据用户ID查询用户外勤信息并显示item数据
     */
    private void queryOaEmail() {
        cursor = db.rawQuery("select * from OaEmail where time=?", new String[]{timeString});
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            type.setText(cursor.getString(cursor.getColumnIndex("type")));
            sendName.setText(cursor.getString(cursor.getColumnIndex("sendName")));
            addressee.setText(cursor.getString(cursor.getColumnIndex("recipients")));
            time.setText(cursor.getString(cursor.getColumnIndex("time")));
            content.setText(cursor.getString(cursor.getColumnIndex("content")));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }
}
