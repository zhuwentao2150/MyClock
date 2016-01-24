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
 * 实现闹钟类
 * @author Administrator
 *
 */
public class AlarmView extends LinearLayout {
	
	private Button btnAddAlarm;	// 添加闹钟按钮
	private ListView lvAlarmList;	// 控件ListView
	private static final String KEY_ALARM_LIST = "alarmList";
	private ArrayAdapter<AlarmData> adapter;	// 作为ListView的适配器
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
	// 初始化闹钟服务
	private void init(){
		alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);	// 使用闹钟服务
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
		lvAlarmList = (ListView) findViewById(R.id.lvAlarmList);
		
		adapter = new ArrayAdapter<AlarmView.AlarmData>(getContext(), android.R.layout.simple_list_item_1);
		lvAlarmList.setAdapter(adapter);
		// 把存储的数据读取出来
		readSavedAlarmList();
		
		btnAddAlarm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addAlarm();
			}
		});
		// 设置list长按弹出删除对话框
		lvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// 对话框设置
				new AlertDialog.Builder(getContext())
				.setTitle("操作选项")
				// CharSequence是String实现的一个接口，相当简单，就是要求是一串字符。所以每个参数类型是CharSequence的方法，都可以实际代入String对象。
				.setItems(new CharSequence[]{"删除"}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							// 删除操作
							deleteAlarm(position);
							break;

						default:
							break;
						}
					}
				}).setNegativeButton("取消", null).show();
				
				return true;
			}
		});
	}
	// 删除闹钟的方法
	private void deleteAlarm(int position){
		AlarmData ad = adapter.getItem(position);
		adapter.remove(ad);
		// 删除之后重新保存一下
		saveAlarmList();
		// 把闹钟移除掉
		alarmManager.cancel(PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(), AlarmReceiver.class), 0));
	}
	// 添加闹钟
	private void addAlarm(){
		
		Calendar c = Calendar.getInstance();
		
		// 打开一个设置闹钟时间的选择框
		new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
			// TimePickerDialog中onTimeSet为何执行两次?
			// 解决方法：设置一个boolean类型的控制器来控制执行onTimeSet中代码的执行次数
			boolean kzq = true;
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				if(kzq){
					// 把选择的时间存放在Calendar中
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
					calendar.set(Calendar.MINUTE, minute);
					calendar.set(Calendar.SECOND, 0);	// 把秒数清零
					calendar.set(Calendar.MILLISECOND, 0);	// 把毫秒数清零
					// 获取系统自带的日历服务
					Calendar currentTime = Calendar.getInstance();
					// 如果用户设置的时间比当前的时间要小，那么时间就往后推一天
					if(calendar.getTimeInMillis() <= currentTime.getTimeInMillis()){
						calendar.setTimeInMillis(calendar.getTimeInMillis()+24*60*60*1000);	// 往后推24小时
					}
					
					AlarmData ad = new AlarmData(calendar.getTimeInMillis());
					adapter.add(ad);
					// 第一个参数的作用：主要是设置该闹钟的状态，如：.RTC：如果该闹钟不再运行那么就不会执行，RTC_WAKEUP：可以关机的时候直接唤醒系统
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 
							ad.getTime(), 
							5*60*1000, 
							// 使用getId()当前时间作为请求码，这个有风险
							PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(),AlarmReceiver.class), 0));
					saveAlarmList();
					kzq = false;
				}
			}
		}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
		
	}
	// 存储闹钟的时间到SharedPreferences
	private void saveAlarmList(){
		Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE).edit();
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<adapter.getCount(); i++){
			sb.append(adapter.getItem(i).getTime()).append(",");	// 如果设置了多个闹钟，那么中间就用逗号隔开
		}
		// sb长度为0的话，那么sb.length()-1就会报错，
		if(sb.length() > 1){
			String content = sb.toString().substring(0, sb.length()-1);	// 把最后一个位置的逗号去掉
			editor.putString(KEY_ALARM_LIST, content);
			System.out.println(content);
		}else{
			editor.putString(KEY_ALARM_LIST, null);
		}
		editor.commit();
	}
	// 读取用户设置的数据信息
	public void readSavedAlarmList(){
		SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
		String content = sp.getString(KEY_ALARM_LIST, null);
		
		if(content != null){
			// 根据逗号来取出时间，存放在数组中
			String[] timeStrings = content.split(",");
			for(String timestring : timeStrings){
				// 把取得的时间存储在数组中
				adapter.add(new AlarmData(Long.parseLong(timestring)));
			}
			
		}
		
	}
	

	// 创建一个类用来存储设置的时间各个参数
	private static class AlarmData{
		public AlarmData(long time){
			this.time = time;
			// 使用time来创建一个时间
			date = Calendar.getInstance();
			date.setTimeInMillis(time);
			// 指定要在界面上显示的时间标签
			timeLabel = String.format("%d月%d日 %d:%d", 
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
		// 设置闹钟的请求码，为了防止越界的风险，把它的值变小来
		public int getId(){
			return (int)getTime()/1000/60;
		}
		private String timeLabel = "";	// 获取文本形式的时间标签
		private long time = 0;	// 闹钟要响起来的时间
		private Calendar date;
		
	}
}
