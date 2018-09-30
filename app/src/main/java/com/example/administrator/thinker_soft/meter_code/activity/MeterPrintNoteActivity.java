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

public class MeterPrintNoteActivity extends Activity{
	private ImageView back;
	private EditText editPrintNote;
	private CardView save;
	private SharedPreferences sharedPreferences_login,sharedPreferences;
	private LinearLayout rootLinearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meter_print_note);

		bindView();
		defaultSetting();
		setViewClickListener();
	}

	//绑定控件
	private void bindView() {
		back = (ImageView) findViewById(R.id.back);
		rootLinearLayout = (LinearLayout) findViewById(R.id.root_linearlayout);
		editPrintNote = (EditText) findViewById(R.id.edit_print_note);
		save = (CardView) findViewById(R.id.save);
	}

	//初始化设置
	private void defaultSetting() {
		sharedPreferences_login = MeterPrintNoteActivity.this.getSharedPreferences("login_info", Context.MODE_PRIVATE);
		sharedPreferences = MeterPrintNoteActivity.this.getSharedPreferences(sharedPreferences_login.getString("userId","")+"data", Context.MODE_PRIVATE);
		if(!"".equals(sharedPreferences.getString("meter_print_note",""))){
			editPrintNote.setText(sharedPreferences.getString("meter_print_note",""));
		}else {
			editPrintNote.setText("");
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
					MeterPrintNoteActivity.this.finish();
					break;
				case R.id.save:
					if(!"".equals(editPrintNote.getText().toString())){
						sharedPreferences.edit().putString("meter_print_note",editPrintNote.getText().toString()).apply();
						Toast.makeText(MeterPrintNoteActivity.this,"保存成功！",Toast.LENGTH_SHORT).show();
						rootLinearLayout.setFocusable(true);
						rootLinearLayout.setFocusableInTouchMode(true);
						editPrintNote.setTextColor(getResources().getColor(R.color.text_gray));
						Tools.hideSoftInput(MeterPrintNoteActivity.this,editPrintNote);
					}else {
						Toast.makeText(MeterPrintNoteActivity.this,"请输入打印备注！",Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
			}
		}
	};
}
