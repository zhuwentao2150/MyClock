package com.example.myclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("闹钟执行了");
		// 取得系统自带的闹钟服务
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		// 取消掉一个当前执行的闹钟
		am.cancel(PendingIntent.getBroadcast(context, getResultCode(), new Intent(context, AlarmReceiver.class), 0));
		
		Intent i = new Intent(context, PlayAlarmAty.class);
		// 设置启动的模式，创建一个新的任务
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

}
