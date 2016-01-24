package com.example.myclock;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class TimerView extends LinearLayout {

	public TimerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimerView(Context context) {
		super(context);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// 实例化底下的四个按钮
		// 暂停
		btnPause = (Button) findViewById(R.id.btnPause);
		btnPause.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopTime();
				btnPause.setVisibility(View.GONE);
				btnReset.setVisibility(View.VISIBLE);
				btnResume.setVisibility(View.VISIBLE);
			}
		});
		// 重置
		btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				stopTime();
				etHour.setText("0");
				etMin.setText("0");
				etSec.setText("0");
				btnPause.setVisibility(View.GONE);
				btnReset.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);
			}
		});
		// 继续
		btnResume = (Button) findViewById(R.id.btnResume);
		btnResume.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startTimer();
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnReset.setVisibility(View.VISIBLE);
			}
		});
		// 开始
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startTimer();
				btnStart.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnReset.setVisibility(View.VISIBLE);
			}
		});
		// 实例化钟表的时间类
		etHour = (EditText) findViewById(R.id.etHour);
		etMin = (EditText) findViewById(R.id.etMin);
		etSec = (EditText) findViewById(R.id.etSec);
		
		// 设置该控件的事件监听
		etHour.setText("00");
		etHour.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(!TextUtils.isEmpty(s)){
					int value = Integer.parseInt(s.toString());	// 取得该控件中的时间
					if(value > 59){
						etHour.setText("59");
					}else if(value < 0){
						etHour.setText("0");
					}
				}
				checkToEnableBtnStart();
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		etMin.setText("00");
		etMin.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(!TextUtils.isEmpty(s)){
					int value = Integer.parseInt(s.toString());	// 取得该控件中的时间
					if(value > 59){
						etMin.setText("59");
					}else if(value < 0){
						etMin.setText("0");
					}
				}
				checkToEnableBtnStart();
			}
			public void afterTextChanged(Editable s) {
			}
			
		});
		etSec.setText("00");
		etSec.addTextChangedListener(new TextWatcher(){
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(!TextUtils.isEmpty(s)){
					int value = Integer.parseInt(s.toString());	// 取得该控件中的时间
					if(value > 59){
						etSec.setText("59");
					}else if(value < 0){
						etSec.setText("0");
					}
				}
				checkToEnableBtnStart();
			}
			public void afterTextChanged(Editable s) {
			}
			
		});
		// 设置按钮的显示格式
		btnStart.setVisibility(View.VISIBLE);	// 把按钮设置为可见
		btnStart.setEnabled(false);				// 把按钮设置为一个不可见的状态
		btnPause.setVisibility(View.GONE);		// 把该控件设置为不可见
		btnResume.setVisibility(View.GONE);		// 设置为不可见的状态
		btnReset.setVisibility(View.GONE);		// 不可见状态
	}// end onFinishInflate
	// 检查开始按钮是否可以呈现可点击状态
	private void checkToEnableBtnStart(){
		// 当满足这些条件的时候就让按钮是可用状态，大于0且不为空，TextUtils.isEmpty(str)，如果str为空则返回true
		btnStart.setEnabled((!TextUtils.isEmpty(etHour.getText()) && Integer.parseInt(etHour.getText().toString()) > 0) 
				|| (!TextUtils.isEmpty(etMin.getText()) && Integer.parseInt(etMin.getText().toString()) > 0) 
				|| (!TextUtils.isEmpty(etSec.getText()) && Integer.parseInt(etSec.getText().toString()) > 0));
	}
	// 启动倒计时
	private void startTimer(){
		if(timerTask == null){
			allTimerCount = Integer.parseInt(etHour.getText().toString())*60*60 + Integer.parseInt(etMin.getText().toString())*60 + Integer.parseInt(etSec.getText().toString());
			timerTask = new TimerTask(){
				public void run() {
					allTimerCount--;
					handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);
					if(allTimerCount<=0){	// 倒计时结束
						handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
						stopTime();
					}
				}
			};
			
			timer.schedule(timerTask, 1000, 1000);
		}
	}//end startTimer()
	// 停止更新时间
	private void stopTime(){
		if(timerTask != null){
			timerTask.cancel();
			timerTask = null;
		}
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WHAT_TIME_TICK:
				// 实时更新时间显示UI
				int hour = allTimerCount/60/60;
				int min = (allTimerCount/60) % 60;
				int sec = allTimerCount % 60;
				etHour.setText(hour + "");
				etMin.setText(min + "");
				etSec.setText(sec + "");
				
				break;
			case MSG_WHAT_TIME_IS_UP:
				new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("倒计时结束").setNegativeButton("确定", null).show();
				btnReset.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);
				
				break;

			default:
				break;
			}
		};
	};
	
	private static final int MSG_WHAT_TIME_IS_UP = 1;
	private static final int MSG_WHAT_TIME_TICK = 2;
	
	private int allTimerCount = 0;
	private Timer timer = new Timer();
	private TimerTask timerTask = null;
	private Button btnStart, btnPause, btnResume, btnReset;
	private EditText etHour, etMin, etSec;

}
