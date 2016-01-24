package com.example.myclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("����ִ����");
		// ȡ��ϵͳ�Դ������ӷ���
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		// ȡ����һ����ǰִ�е�����
		am.cancel(PendingIntent.getBroadcast(context, getResultCode(), new Intent(context, AlarmReceiver.class), 0));
		
		Intent i = new Intent(context, PlayAlarmAty.class);
		// ����������ģʽ������һ���µ�����
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

}
