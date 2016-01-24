package com.example.myclock;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TabHost;

public class MainActivity extends Activity {

	private TabHost tabHoat;
	private StopWatchView stopWatchView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tabHoat = (TabHost) findViewById(android.R.id.tabhost);
		tabHoat.setup();
		
		tabHoat.addTab(tabHoat.newTabSpec("tabTime").setIndicator("ʱ��").setContent(R.id.tabTime));
		tabHoat.addTab(tabHoat.newTabSpec("tabAlarm").setIndicator("����").setContent(R.id.tabAlorm));
		tabHoat.addTab(tabHoat.newTabSpec("tabTimer").setIndicator("��ʱ��").setContent(R.id.tabtimer));
		tabHoat.addTab(tabHoat.newTabSpec("tabStopWatch").setIndicator("���").setContent(R.id.tabStopMatch));
		
		stopWatchView = (StopWatchView) findViewById(R.id.tabStopMatch);
	}
	
	@Override
	protected void onDestroy() {
		stopWatchView.onDestory();
		super.onDestroy();
	}


}
