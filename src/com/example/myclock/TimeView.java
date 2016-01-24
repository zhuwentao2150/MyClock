package com.example.myclock;

import java.util.Calendar;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimeView extends LinearLayout {

	public TimeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public TimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TimeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	// 当View中所有的子控件 均被映射成xml后触发
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		tvTime = (TextView) findViewById(R.id.tvTime);
		
		timerHandler.sendEmptyMessage(0);
	}
	// 当可见状态发生改变的时候调用
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		// 判断如果该控件属于可见状态时
		if(visibility == View.VISIBLE){
			timerHandler.sendEmptyMessage(0);
		}else{
			timerHandler.removeMessages(0);	// 把所有的的消息移除
		}
	}
	// 更新显示的时间
	private void refreshTime(){
		Calendar c = Calendar.getInstance();
		String time = String.format("%d:%d:%d", c.get(Calendar.HOUR),c.get(Calendar.MINUTE),c.get(Calendar.SECOND));
		tvTime.setText(time);
	}
	// 使用消息传递给主线程提供消息
	private Handler timerHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			refreshTime();
			if(getVisibility() == View.VISIBLE){
				timerHandler.sendEmptyMessageDelayed(0, 1000);	// 每隔1000毫秒就发送一次
			}
		};
	};
	
	private TextView tvTime;
}
