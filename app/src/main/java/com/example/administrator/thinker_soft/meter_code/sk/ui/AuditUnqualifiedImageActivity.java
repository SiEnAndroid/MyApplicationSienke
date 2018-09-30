package com.example.administrator.thinker_soft.meter_code.sk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.example.administrator.thinker_soft.meter_code.sk.adapter.MyAuditUnqualifiedImageAdapter;
import com.example.administrator.thinker_soft.meter_code.sk.bean.NoThroughBean;
import com.example.administrator.thinker_soft.meter_code.sk.bean.SecurityImageBean;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.meter_code.sk.widget.LoadingView;
import com.example.administrator.thinker_soft.meter_code.sk.widget.PhotoViewPager;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 111
 */
public class AuditUnqualifiedImageActivity extends AppCompatActivity implements View.OnClickListener {

    private int currentPosition;  // 点击进来的序号
    private int currentPosition1;  // 当前要删除的序号
    private PhotoViewPager mViewPager;
    private TextView mTvImageCount ,tips;
    private ImageView mSaveImage ,delete ,back;
    private LinearLayout rootLinearlayout;
    private ArrayList<String> cropIdLists;
    private MyAuditUnqualifiedImageAdapter adapter;
    private LayoutInflater inflater;
    private View saveView;
    private PopupWindow popupWindow;
    private RadioButton cancelRb ,saveRb;
    private SecurityImageBean imagebean;
    private LoadingView loadingView;
    private List<SecurityImageBean.ListBean> list;
    private boolean isUpload = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_abnormal_photo );

        init();
        initView();
        initData();
    }

    private void init() {
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition",0);
        cropIdLists = (ArrayList<String>) intent.getSerializableExtra("cropIdLists");
        imagebean = (SecurityImageBean) intent.getSerializableExtra("imagebean");
        list = imagebean.getList();

    }


    private void initView() {
        mViewPager = (PhotoViewPager) findViewById(R.id.view_pager_photo);
        mTvImageCount = (TextView) findViewById(R.id.tv_image_count);
        mSaveImage = (ImageView) findViewById(R.id.back);
        back = (ImageView) findViewById(R.id.back);
        delete = (ImageView) findViewById(R.id.delete);
        delete.setVisibility(View.VISIBLE);
        rootLinearlayout = (LinearLayout) findViewById(R.id.ly);

        mSaveImage.setOnClickListener(this);
        delete.setOnClickListener(this);
        mTvImageCount.setText(currentPosition+1 + "/" + cropIdLists.size());

    }

    private void initData() {
        adapter = new MyAuditUnqualifiedImageAdapter(this,cropIdLists,currentPosition);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPosition1 =   position;
                mTvImageCount.setText(String.valueOf(position+1)+"/"+cropIdLists.size());


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                if ( isUpload ){
                    Intent intent = new Intent();
                    intent.putExtra("Upload","更新");
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;
            case R.id.delete:
                // 弹出一个弹框 是否删除照片,是请求网络删除并更新
                createSaveWindow();
                break;
            default:
                break;
        }
    }



    //弹出是否保存popupwindow
    public void createSaveWindow() {
        inflater = LayoutInflater.from(AuditUnqualifiedImageActivity.this);
        saveView = inflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        popupWindow = new PopupWindow(saveView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //绑定控件ID
        cancelRb = (RadioButton) saveView.findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) saveView.findViewById(R.id.save_rb);
        tips = (TextView) saveView.findViewById(R.id.tips);
        tips.setText("是否确定删除这张照片");
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
                    // 请求网络
                requestnetwork();
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.showAtLocation(saveRb, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    /**
     * 请求网络删除该张照片
     */
    private void requestnetwork() {
        SecurityImageBean.ListBean listBean = list.get(currentPosition1);
        //加载
        loadingView = new LoadingView(AuditUnqualifiedImageActivity.this, R.style.LoadingDialog, "加载图片中...请稍后");
        loadingView.show();
        final String httpUrl = new StringBuffer().append(SkUrl.SkHttp(AuditUnqualifiedImageActivity.this)).append("delImage.do").toString();
        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .params("c_data_id",String.valueOf(listBean.getC_data_id()))
                .params("n_image_id",String.valueOf(listBean.getN_image_id()));
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e("pgl","===:"+response.getBody());
                NoThroughBean bean = new Gson().fromJson(response.getBody(), NoThroughBean.class);
                loadingView.dismiss();
                if ("删除成功".equals(bean.getMsg())){
                    // 吐司 并删除本地照片
                    handler.sendEmptyMessage(0);
                }else {
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("pgl", "===" + e.getMessage());
                Toast.makeText(AuditUnqualifiedImageActivity.this, "网络请求异常", Toast.LENGTH_SHORT).show();
                loadingView.dismiss();
            }
        }).executeAsync();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch ( msg.what) {
                case 0:
                    Toast.makeText(AuditUnqualifiedImageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    // 删除本地照片
                    deleteLocalPhotos();
                case 1:
                    Toast.makeText(AuditUnqualifiedImageActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    private void deleteLocalPhotos() {
        if ( cropIdLists.size() ==1){
            cropIdLists.remove(currentPosition1);
            Intent intent = new Intent();
            intent.putExtra("Upload","更新");
            setResult(RESULT_OK, intent);
            finish();
        }else {
            cropIdLists.remove(currentPosition1);
            adapter.notifyDataSetChanged();
            isUpload = true;
        }
    }
    /**
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭当前Activity.
     */
    @Override
    public void onBackPressed() {
        if ( isUpload ){
            Intent intent = new Intent();
            intent.putExtra("Upload","更新");
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    /**
     * 设置背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = AuditUnqualifiedImageActivity.this.getWindow().getAttributes();
        //0.0-1.0
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            //不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            AuditUnqualifiedImageActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            AuditUnqualifiedImageActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        AuditUnqualifiedImageActivity.this.getWindow().setAttributes(lp);
    }



}
