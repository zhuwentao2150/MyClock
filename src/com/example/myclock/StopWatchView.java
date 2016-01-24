package com.example.myclock;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StopWatchView extends LinearLayout {

	public StopWatchView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		// 初始化要显示的文字
		tvHour = (TextView) findViewById(R.id.timeHour);
		tvHour.setText("0");
		tvMin = (TextView) findViewById(R.id.timeMin);
		tvMin.setText("0");
		tvSec = (TextView) findViewById(R.id.timeSec);
		tvSec.setText("0");
		tvMSec = (TextView) findViewById(R.id.timeMSec);
		tvMSec.setText("0");
		
		btnStart = (Button) findViewById(R.id.btnSWStart);
		btnResume = (Button) findViewById(R.id.btnSwResume);
		btnPause = (Button) findViewById(R.id.btnSWPause);
		btnReset = (Button) findViewById(R.id.btnSWReset);
		btnLop = (Button) findViewById(R.id.btnSWLop);
		// 设置开始按钮的事件监听
		btnStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startTimer();
				
				btnStart.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnLop.setVisibility(View.VISIBLE);
			}
		});
		// 暂停事件监听
		btnPause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				stopTimer();
				
				btnPause.setVisibility(View.GONE);
				btnResume.setVisibility(View.VISIBLE);	// 继续
				btnLop.setVisibility(View.GONE);
				btnReset.setVisibility(View.VISIBLE);		// 结束
				
			}
		});
		// 继续按钮事件监听
		btnResume.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				startTimer();
				
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnReset.setVisibility(View.GONE);
				btnLop.setVisibility(View.VISIBLE);
				
			}
		});
		// 结束按钮事件监听
		btnReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopTimer();
				tenMSecs = 0;
				adapter.clear();
				
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnReset.setVisibility(View.GONE);
				btnLop.setVisibility(View.GONE);
				
				btnStart.setVisibility(View.VISIBLE);
			}
		});
		// 计时事件监听
		btnLop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.insert(String.format("%d:%d:%d:%d", tenMSecs/100/60/60, tenMSecs/100/60%60, tenMSecs/100%60, tenMSecs%100), 0);
			}
		});
		
		// 设置部分按钮不可见
		btnResume.setVisibility(View.GONE);
		btnPause.setVisibility(View.GONE);
		btnReset.setVisibility(View.GONE);
		btnLop.setVisibility(View.GONE);
		
		lvTimeList = (ListView) findViewById(R.id.lvWatchTime);
		adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
		lvTimeList.setAdapter(adapter);
		
		showTimeTask = new TimerTask(){
			@Override
			public void run() {
				hander.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
			}
		};
		timer.schedule(showTimeTask, 200, 200);
	}
	// 开始计时
	private void startTimer(){
		if(timerTask == null){
			timerTask  = new TimerTask(){
				@Override
				public void run() {
					tenMSecs++;
				}
			};
			timer.schedule(timerTask, 10, 10);
		}
	}
	// 停止计时
	private void stopTimer(){
		if(timerTask != null){
			timerTask.cancel();
			timerTask = null;
		}
	}
	
	private int tenMSecs = 0;
	private Timer timer = new Timer();
	private TimerTask timerTask = null;
	private TimerTask showTimeTask = null;
	
	private TextView tvHour,tvMin,tvSec,tvMSec;
	private Button btnStart,btnResume,btnPause,btnReset,btnLop;
	private ListView lvTimeList;
	private ArrayAdapter<String> adapter;
	
	private Handler hander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WHAT_SHOW_TIME:
				tvHour.setText(tenMSecs/100/60/60+"");
				tvMin.setText(tenMSecs/100/60%60+"");
				tvSec.setText(tenMSecs/100%60+"");
				tvMSec.setText(tenMSecs%100+"");
				break;

			default:
				break;
			}
		};
	};
	private static final int MSG_WHAT_SHOW_TIME = 1;
	
	// 在主线程中调用，关闭掉所有的timer
	public void onDestory(){
		timer.cancel();
	}
}
