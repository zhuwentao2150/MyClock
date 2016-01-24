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
		
		tabHoat.addTab(tabHoat.newTabSpec("tabTime").setIndicator("时钟").setContent(R.id.tabTime));
		tabHoat.addTab(tabHoat.newTabSpec("tabAlarm").setIndicator("闹钟").setContent(R.id.tabAlorm));
		tabHoat.addTab(tabHoat.newTabSpec("tabTimer").setIndicator("计时器").setContent(R.id.tabtimer));
		tabHoat.addTab(tabHoat.newTabSpec("tabStopWatch").setIndicator("秒表").setContent(R.id.tabStopMatch));
		
		stopWatchView = (StopWatchView) findViewById(R.id.tabStopMatch);
	}
	
	@Override
	protected void onDestroy() {
		stopWatchView.onDestory();
		super.onDestroy();
	}


}
