package com.example.administrator.thinker_soft.meter_code.sk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpCallback;
import com.example.administrator.thinker_soft.meter_code.sk.http.HttpMethod;
import com.example.administrator.thinker_soft.meter_code.sk.http.Request;
import com.example.administrator.thinker_soft.meter_code.sk.http.Response;
import com.example.administrator.thinker_soft.meter_code.sk.http.SkUrl;
import com.example.administrator.thinker_soft.mode.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.administrator.thinker_soft.meter_code.sk.uitls.EditTextUtils.hideKeyboard;
import static com.example.administrator.thinker_soft.meter_code.sk.uitls.EditTextUtils.isShouldHideKeyboard;

/**
 * 备注
 * Created by Administrator on 2018/4/22.
 */

public class MeterRemarkActivity extends AppCompatActivity implements View.OnClickListener {
    private Unbinder mUnbinder;
    private String remarkStr;//备注说明
    private String userId;
    @BindView(R.id.back)
    ImageView back;//返回
    @BindView(R.id.save)
    ImageView save;//保存
    @BindView(R.id.et_opened_remark)
    EditText remark;//备注

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_remark);
        mUnbinder= ButterKnife.bind(this);//绑定
        initView();
    }

    private void initView() {

        if (getIntent() != null) {
            remarkStr=getIntent().getStringExtra("remark");
            remark.setText(remarkStr);
            userId=getIntent().getStringExtra("user_id");
            //是否有值
            if (remarkStr.length()>0){
                remark.setSelection(remarkStr.length());//将光标移至文字末尾
            }
        }
        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.save:
                setAve();
                break;

            case  R.id.back:
                //返回
                setRemark();
                break;

            default:
                break;

        }
    }

    /**
     * 修改
     */
    private void setRemark(){
     String rk= remark.getText().toString().trim();
        if (TextUtils.isEmpty(rk)){
            finish();
            return;
        }
        if (rk.equals(remarkStr)){
            Intent intent = new Intent();
            intent.putExtra("remarks", rk);
            intent.putExtra("status", "false");
            setResult(RESULT_OK, intent);
            finish();
        }else {
            httoMeterRemark(userId,rk);

        }
        Tools.hideSoftInput(MeterRemarkActivity.this, remark);
    }
    /**
     * 保存
     */
    private void setAve(){
        String rk= remark.getText().toString().trim();
        httoMeterRemark(userId,rk);
        Tools.hideSoftInput(MeterRemarkActivity.this, remark);
    }
    /**
     * 修改备注
     */
    private void httoMeterRemark(String cUserId, final String cOpenedRemark){
        String httpUrl = new StringBuffer().append(SkUrl.SkHttp(MeterRemarkActivity.this)).append("updateOpenedRemark.do").toString();

        Request.Builder build = new Request.Builder()
                .url(httpUrl)
                .method(HttpMethod.GET)
                .encode("UTF-8")
                .tag("remark")
                .headers("User-Agent", "sk-1.0")
                .headers("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .params("cUserId", cUserId)
                .params("cOpenedRemark", SkUrl.toURLEncoded(cOpenedRemark))
                .string("");
        Request.newRequest(build, new HttpCallback() {
            @Override
            public void onComplete(Response response) {
                Log.e("MeterRemarkActivity", "onComplete/response:" + response.getBody());
                Log.e("MeterRemarkActivity", "onComplete/response: content type=" + response.getContentType());
                Intent intent = new Intent();
                intent.putExtra("remarks", cOpenedRemark);
                intent.putExtra("status", "true");
                setResult(RESULT_OK, intent);
                Toast.makeText(MeterRemarkActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("MeterRemarkActivity", "onError:" + e.getMessage());
                Toast.makeText(MeterRemarkActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("remarks", remarkStr);
                intent.putExtra("status", "false");
                setResult(RESULT_OK, intent);
                finish();
            }
        }).executeAsync();
    }
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
