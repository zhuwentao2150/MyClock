package com.example.myclock;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

public class PlayAlarmAty extends Activity {

	private MediaPlayer mp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_player_aty);
		
		// ʹ��һ����Դ�ļ��������ʵ����
		mp = MediaPlayer.create(this, R.raw.alarm);
		
		mp.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("������onPause����");
		// ����
		finish();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("������onDestroy����");
		// �ͷ���Դ
		mp.stop();
		mp.release();
	}
	
}
