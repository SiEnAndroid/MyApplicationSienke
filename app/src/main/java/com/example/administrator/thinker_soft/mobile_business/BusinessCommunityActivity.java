package com.example.administrator.thinker_soft.mobile_business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.adapter.CommunityAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/12.
 */
public class BusinessCommunityActivity extends Activity {
    private ListView listview;
    private ImageView back;
    private TextView search;
    private ImageView more;
    private List<CommunityListViewItem> communityListViewItemList = new ArrayList<>();
    private CommunityAdapter adapter;
    private RadioButton message, send;
    private PopupWindow window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_community);//公司社区

        bindView();//绑定控件
        setOnClickListener();//点击事件

    }

    public void bindView() {
        back = (ImageView) findViewById(R.id.back);
        listview = (ListView) findViewById(R.id.listview);
        search = (TextView) findViewById(R.id.search);
        more = (ImageView) findViewById(R.id.more);
    }

    //假数据
    public void getData() {
        for (int i = 0; i < 10; i++) {
            CommunityListViewItem item = new CommunityListViewItem();
            item.setPic(R.mipmap.eg);
            item.setTitle("爱迪生积分更好的");
            item.setNeirong("动画风格家公司电话防火阀");
            item.setTime("2017/06/23");
            item.setPingjia("0评");
            item.setHuifu("5回");
            item.setYuedu("34阅");
            communityListViewItemList.add(item);
        }
    }

    public void setOnClickListener() {
        getData();
        adapter = new CommunityAdapter(BusinessCommunityActivity.this, communityListViewItemList);
        listview.setAdapter(adapter);
        back.setOnClickListener(clickListener);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = BusinessCommunityActivity.this.getLayoutInflater().inflate(R.layout.popupwidow_business_community, null);
                window = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setAnimationStyle(R.mipmap.pop_banner);
                window.setFocusable(true);
                backgroundAlpha(0.6F);   //背景变暗
                window.setOutsideTouchable(true);
                window.update();
                window.showAsDropDown(more, 0, 0);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1.0F);
                        more.setClickable(true);
                    }
                });
                message = (RadioButton) popupView.findViewById(R.id.message);
                send = (RadioButton) popupView.findViewById(R.id.send);

                message.setOnClickListener(clickListener);
                send.setOnClickListener(clickListener);
            }
        });
        search.setOnClickListener(clickListener);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                case R.id.message:
                    Intent intent1 = new Intent(BusinessCommunityActivity.this,BusinessColumeMessageActivity.class);
                    startActivity(intent1);
                    window.dismiss();
                    break;
                case R.id.send:
                    Intent intent = new Intent(BusinessCommunityActivity.this,BusinessPopPublishActivity.class);
                    startActivity(intent);
                    window.dismiss();
                    break;
                case R.id.take_photo:
                    break;
            }
        }
    };

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = BusinessCommunityActivity.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            BusinessCommunityActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            BusinessCommunityActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        BusinessCommunityActivity.this.getWindow().setAttributes(lp);
    }
}
