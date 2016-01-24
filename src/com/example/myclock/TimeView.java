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

	// ��View�����е��ӿؼ� ����ӳ���xml�󴥷�
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		tvTime = (TextView) findViewById(R.id.tvTime);
		
		timerHandler.sendEmptyMessage(0);
	}
	// ���ɼ�״̬�����ı��ʱ�����
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		// �ж�����ÿؼ����ڿɼ�״̬ʱ
		if(visibility == View.VISIBLE){
			timerHandler.sendEmptyMessage(0);
		}else{
			timerHandler.removeMessages(0);	// �����еĵ���Ϣ�Ƴ�
		}
	}
	// ������ʾ��ʱ��
	private void refreshTime(){
		Calendar c = Calendar.getInstance();
		String time = String.format("%d:%d:%d", c.get(Calendar.HOUR),c.get(Calendar.MINUTE),c.get(Calendar.SECOND));
		tvTime.setText(time);
	}
	// ʹ����Ϣ���ݸ����߳��ṩ��Ϣ
	private Handler timerHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			refreshTime();
			if(getVisibility() == View.VISIBLE){
				timerHandler.sendEmptyMessageDelayed(0, 1000);	// ÿ��1000����ͷ���һ��
			}
		};
	};
	
	private TextView tvTime;
}
