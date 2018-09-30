package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.EmailInfoAdapter;
import com.example.administrator.thinker_soft.mobile_business.model.EmailViewHolder;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.myapplicaction.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */
public class BusinessSentActivity extends Activity {
    private ImageView back;
    private Button checked, delete;
    private ListView listViewEmail;
    private LinearLayout rootLinearlayout;
    private LayoutInflater layoutInflater;
    private BusinessEmailListviewItem item;
    private EmailInfoAdapter adapter;
    private List<BusinessEmailListviewItem> businessEmailListviewItemList = new ArrayList<>();
    private SharedPreferences sharedPreferences_login;
    private Cursor cursor;
    private SQLiteDatabase db;  //数据库
    private RadioButton cancelRb, saveRb;
    private TextView tips;
    private View surePopup;
    private PopupWindow popupWindow;
    private int currentPosition;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter = new EmailInfoAdapter(BusinessSentActivity.this, businessEmailListviewItemList, 0);
                    adapter.notifyDataSetChanged();
                    listViewEmail.setAdapter(adapter);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_email);

        bindView();//绑定控件
        defaultSetting();
        setViewClickListener();//点击事件
    }

    private void defaultSetting() {
        sharedPreferences_login = getSharedPreferences("login_info", Context.MODE_PRIVATE);
        final MySqliteHelper helper = new MySqliteHelper(BusinessSentActivity.this, MyApplication.DATA_BASE_VERSION);
        db = helper.getWritableDatabase();
        new Thread() {
            @Override
            public void run() {
                super.run();
                queryOaEmail();
                if (cursor.getCount() != 0) {
                    handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }


    public void bindView() {
        listViewEmail = (ListView) findViewById(R.id.listview_email);
        delete = (Button) findViewById(R.id.delete);
        back = (ImageView) findViewById(R.id.back);
        checked = (Button) findViewById(R.id.checked);
        rootLinearlayout = (LinearLayout) findViewById(R.id.root_linearlayout);

    }

    public void setViewClickListener() {
        checked.setOnClickListener(clickListener);
        delete.setOnClickListener(clickListener);
        adapter = new EmailInfoAdapter(BusinessSentActivity.this, businessEmailListviewItemList, 0);
        listViewEmail.setAdapter(adapter);
        back.setOnClickListener(clickListener);
        listViewEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmailViewHolder viewHolder = (EmailViewHolder) view.getTag();
                viewHolder.check = (CheckBox) findViewById(R.id.check);
                item = (BusinessEmailListviewItem) adapter.getItem(position);
                currentPosition = position;
                Intent intent = new Intent(BusinessSentActivity.this, BusinessEmailInfoActivity.class);
                intent.putExtra("time", item.getTime());
                startActivityForResult(intent, currentPosition);
            }
        });
        listViewEmail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                item = (BusinessEmailListviewItem) adapter.getItem(position);
                showSurePopup();
                return true;
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
                case R.id.checked:
                    break;
                case R.id.delete:
                    for (int i = 0; i < businessEmailListviewItemList.size(); i++) {
                        BusinessEmailListviewItem item = businessEmailListviewItemList.get((int) adapter.getItemId(i));
                        if (item.getCheck()){
                            Log.i("getCheck","点击进来了");
                            showSurePopup();
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 根据用户ID查询日程安排并显示listview数据
     */
    private void queryOaEmail() {
        businessEmailListviewItemList.clear();
        cursor = db.rawQuery("select * from OaEmail where userId=?", new String[]{sharedPreferences_login.getString("userId", "")});
        Log.i("queryOaUserInfo", "集合长度为：" + cursor.getCount());
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            BusinessEmailListviewItem item = new BusinessEmailListviewItem();
            item.setEmailAdress(cursor.getString(cursor.getColumnIndex("inboxAddress")));
            item.setContent(cursor.getString(cursor.getColumnIndex("content")));
            item.setTime(cursor.getString(cursor.getColumnIndex("time")));
            item.setTitle(cursor.getString(cursor.getColumnIndex("type")));
            businessEmailListviewItemList.add(item);
        }
    }

    private void showSurePopup() {
        layoutInflater = LayoutInflater.from(BusinessSentActivity.this);
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
                db.delete("OaEmail", "time=?", new String[]{item.getTime()});  //删除OaUser表中所有数据（官方推荐方法）
                db.execSQL("update sqlite_sequence set seq=0 where name='OaEmail'");
                businessEmailListviewItemList.remove(currentPosition);
                adapter.notifyDataSetChanged();
                Toast.makeText(BusinessSentActivity.this, "清除数据成功！", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
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
        WindowManager.LayoutParams lp = BusinessSentActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            BusinessSentActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            BusinessSentActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        BusinessSentActivity.this.getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == currentPosition) {
                businessEmailListviewItemList.remove(currentPosition);
                adapter.notifyDataSetChanged();
                listViewEmail.setAdapter(adapter);
            }
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
