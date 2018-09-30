package com.example.administrator.thinker_soft.meter_code.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mode.Tools;

public class MeterPageCountSettingsActivity extends Activity {
	private ImageView back;
	private EditText pageCountEdit;
	private CardView save;
	private SharedPreferences sharedPreferences_login,sharedPreferences;
	private LinearLayout rootLinearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meter_page_count_settings);

		bindView();
		defaultSetting();
		setViewClickListener();
	}

	//绑定控件
	private void bindView() {
		back = (ImageView) findViewById(R.id.back);
		rootLinearLayout = (LinearLayout) findViewById(R.id.root_linearlayout);
		pageCountEdit = (EditText) findViewById(R.id.page_count_edit);
		save = (CardView) findViewById(R.id.save);
	}

	//初始化设置
	private void defaultSetting() {
		sharedPreferences_login = MeterPageCountSettingsActivity.this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
		sharedPreferences = MeterPageCountSettingsActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId","")+"data", Context.MODE_PRIVATE);
		if(!sharedPreferences.getString("page_count","").equals("")){
			pageCountEdit.setText(sharedPreferences.getString("page_count",""));
		}else {
			pageCountEdit.setText("50");
		}
	}

	//点击事件
	public void setViewClickListener() {
		back.setOnClickListener(onClickListener);
		save.setOnClickListener(onClickListener);
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.back:
					MeterPageCountSettingsActivity.this.finish();
					break;
				case R.id.save:
					if(!"".equals(pageCountEdit.getText().toString())){
						sharedPreferences.edit().putString("page_count",pageCountEdit.getText().toString()).apply();
						Toast.makeText(MeterPageCountSettingsActivity.this,"设置成功！",Toast.LENGTH_SHORT).show();
						rootLinearLayout.setFocusable(true);
						rootLinearLayout.setFocusableInTouchMode(true);
						pageCountEdit.setTextColor(getResources().getColor(R.color.text_gray));
						Tools.hideSoftInput(MeterPageCountSettingsActivity.this,pageCountEdit);
					}else {
						Toast.makeText(MeterPageCountSettingsActivity.this,"请输入页数！",Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
			}
		}
	};
}
