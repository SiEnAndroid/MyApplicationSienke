package com.example.administrator.thinker_soft.Security_check.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.CommonAdapter;
import com.example.administrator.thinker_soft.mode.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.et_feedback_info)
    EditText etFeedbackInfo;
    @BindView(R.id.cardview_feedback_info)
    CardView cardviewFeedbackInfo;
    @BindView(R.id.et_qq_number)
    EditText etQqNumber;
    @BindView(R.id.qq_number)
    CardView qqNumber;
    @BindView(R.id.tv_feedback_type)
    TextView tvFeedbackType;
    @BindView(R.id.cardview_feedback_type)
    CardView cardviewFeedbackType;
    @BindView(R.id.btn_feedback_commit)
    Button btnFeedbackCommit;
    @BindView(R.id.layout_charge)
    LinearLayout layoutCharge;
    @BindView(R.id.feedback_layout)
    LinearLayout feedbackLayout;

    private ListView lvType;
    private CommonAdapter<String> typeAdapter;
    private ArrayList<String> typeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        typeList.add("发现了新BUG！");
        typeList.add("有些小意见要提！");
        typeList.add("就打个酱油！");
        typeAdapter = new CommonAdapter<String>(this, R.layout.popupwindow_feedback_type_list_item) {
            @Override
            public void convertView(int i, ViewHolder viewHolder, String item) {
                viewHolder.setText(R.id.type_name, item);
            }
        };
        typeAdapter.addDatas(typeList);
    }

    @OnClick({R.id.back, R.id.cardview_feedback_type, R.id.btn_feedback_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.cardview_feedback_type:
                showTypeWindow();
                break;
            case R.id.btn_feedback_commit:
                break;
        }
    }

    private void showTypeWindow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View feedbackTypeView = inflater.inflate(R.layout.popupwindow_feedback_type, null);
        final PopupWindow typeWindow = new PopupWindow(feedbackTypeView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView cancel = (TextView) feedbackTypeView.findViewById(R.id.btn_cancel);
        TextView confirm = (TextView) feedbackTypeView.findViewById(R.id.btn_confirm);
        lvType = (ListView) feedbackTypeView.findViewById(R.id.lv_feedback_type);
        lvType.setAdapter(typeAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeWindow.dismiss();
            }
        });
        lvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvFeedbackType.setText(typeAdapter.getItem(position));
                typeWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        typeWindow.update();
        typeWindow.setFocusable(true);
        typeWindow.setTouchable(true);
        typeWindow.setOutsideTouchable(true);
        typeWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        typeWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        backgroundAlpha(0.6F);   //背景变暗
        typeWindow.showAtLocation(feedbackLayout, Gravity.CENTER, 0, 0);
        typeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        this.getWindow().setAttributes(lp);
    }
}
