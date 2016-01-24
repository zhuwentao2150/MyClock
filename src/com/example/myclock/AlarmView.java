package com.example.myclock;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

/**
 * ʵ��������
 * @author Administrator
 *
 */
public class AlarmView extends LinearLayout {
	
	private Button btnAddAlarm;	// ������Ӱ�ť
	private ListView lvAlarmList;	// �ؼ�ListView
	private static final String KEY_ALARM_LIST = "alarmList";
	private ArrayAdapter<AlarmData> adapter;	// ��ΪListView��������
	private AlarmManager alarmManager;
	
	public AlarmView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public AlarmView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AlarmView(Context context) {
		super(context);
		init();
	}
	// ��ʼ�����ӷ���
	private void init(){
		alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);	// ʹ�����ӷ���
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
		lvAlarmList = (ListView) findViewById(R.id.lvAlarmList);
		
		adapter = new ArrayAdapter<AlarmView.AlarmData>(getContext(), android.R.layout.simple_list_item_1);
		lvAlarmList.setAdapter(adapter);
		// �Ѵ洢�����ݶ�ȡ����
		readSavedAlarmList();
		
		btnAddAlarm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addAlarm();
			}
		});
		// ����list��������ɾ���Ի���
		lvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// �Ի�������
				new AlertDialog.Builder(getContext())
				.setTitle("����ѡ��")
				// CharSequence��Stringʵ�ֵ�һ���ӿڣ��൱�򵥣�����Ҫ����һ���ַ�������ÿ������������CharSequence�ķ�����������ʵ�ʴ���String����
				.setItems(new CharSequence[]{"ɾ��"}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							// ɾ������
							deleteAlarm(position);
							break;

						default:
							break;
						}
					}
				}).setNegativeButton("ȡ��", null).show();
				
				return true;
			}
		});
	}
	// ɾ�����ӵķ���
	private void deleteAlarm(int position){
		AlarmData ad = adapter.getItem(position);
		adapter.remove(ad);
		// ɾ��֮�����±���һ��
		saveAlarmList();
		// �������Ƴ���
		alarmManager.cancel(PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(), AlarmReceiver.class), 0));
	}
	// �������
	private void addAlarm(){
		
		Calendar c = Calendar.getInstance();
		
		// ��һ����������ʱ���ѡ���
		new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
			// TimePickerDialog��onTimeSetΪ��ִ������?
			// �������������һ��boolean���͵Ŀ�����������ִ��onTimeSet�д����ִ�д���
			boolean kzq = true;
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				if(kzq){
					// ��ѡ���ʱ������Calendar��
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
					calendar.set(Calendar.MINUTE, minute);
					calendar.set(Calendar.SECOND, 0);	// ����������
					calendar.set(Calendar.MILLISECOND, 0);	// �Ѻ���������
					// ��ȡϵͳ�Դ�����������
					Calendar currentTime = Calendar.getInstance();
					// ����û����õ�ʱ��ȵ�ǰ��ʱ��ҪС����ôʱ���������һ��
					if(calendar.getTimeInMillis() <= currentTime.getTimeInMillis()){
						calendar.setTimeInMillis(calendar.getTimeInMillis()+24*60*60*1000);	// ������24Сʱ
					}
					
					AlarmData ad = new AlarmData(calendar.getTimeInMillis());
					adapter.add(ad);
					// ��һ�����������ã���Ҫ�����ø����ӵ�״̬���磺.RTC����������Ӳ���������ô�Ͳ���ִ�У�RTC_WAKEUP�����Թػ���ʱ��ֱ�ӻ���ϵͳ
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 
							ad.getTime(), 
							5*60*1000, 
							// ʹ��getId()��ǰʱ����Ϊ�����룬����з���
							PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(),AlarmReceiver.class), 0));
					saveAlarmList();
					kzq = false;
				}
			}
		}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
		
	}
	// �洢���ӵ�ʱ�䵽SharedPreferences
	private void saveAlarmList(){
		Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE).edit();
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<adapter.getCount(); i++){
			sb.append(adapter.getItem(i).getTime()).append(",");	// ��������˶�����ӣ���ô�м���ö��Ÿ���
		}
		// sb����Ϊ0�Ļ�����ôsb.length()-1�ͻᱨ��
		if(sb.length() > 1){
			String content = sb.toString().substring(0, sb.length()-1);	// �����һ��λ�õĶ���ȥ��
			editor.putString(KEY_ALARM_LIST, content);
			System.out.println(content);
		}else{
			editor.putString(KEY_ALARM_LIST, null);
		}
		editor.commit();
	}
	// ��ȡ�û����õ�������Ϣ
	public void readSavedAlarmList(){
		SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
		String content = sp.getString(KEY_ALARM_LIST, null);
		
		if(content != null){
			// ���ݶ�����ȡ��ʱ�䣬�����������
			String[] timeStrings = content.split(",");
			for(String timestring : timeStrings){
				// ��ȡ�õ�ʱ��洢��������
				adapter.add(new AlarmData(Long.parseLong(timestring)));
			}
			
		}
		
	}
	

	// ����һ���������洢���õ�ʱ���������
	private static class AlarmData{
		public AlarmData(long time){
			this.time = time;
			// ʹ��time������һ��ʱ��
			date = Calendar.getInstance();
			date.setTimeInMillis(time);
			// ָ��Ҫ�ڽ�������ʾ��ʱ���ǩ
			timeLabel = String.format("%d��%d�� %d:%d", 
					date.get(Calendar.MONTH)+1,
					date.get(Calendar.DAY_OF_MONTH),
					date.get(Calendar.HOUR_OF_DAY),
					date.get(Calendar.MINUTE));
		}
		
		public long getTime(){
			return time;
		}
		public String getTimeLobel(){
			return timeLabel;
		}
		@Override
		public String toString() {
			return getTimeLobel();
		}
		// �������ӵ������룬Ϊ�˷�ֹԽ��ķ��գ�������ֵ��С��
		public int getId(){
			return (int)getTime()/1000/60;
		}
		private String timeLabel = "";	// ��ȡ�ı���ʽ��ʱ���ǩ
		private long time = 0;	// ����Ҫ��������ʱ��
		private Calendar date;
		
	}
}
