package com.ky.utills;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmHelper {

	private Context mContext;
	private AlarmManager mAlarmManager;

	public AlarmHelper(Context c) {
		mContext = c;
		mAlarmManager = (AlarmManager) c
				.getSystemService(Context.ALARM_SERVICE);
	}

	public void openAlarm(int id, long sleep, String textDis, long start) {

		Intent intent = new Intent();
		intent.putExtra("_id", id);
		intent.putExtra("_text", textDis);
		intent.putExtra("_sleep", sleep);
		intent.setClass(mContext, CallAlarm.class);
		// intent.setClass(mContext, FxService.class);
		// PendingIntent pi = PendingIntent.getService(mContext, id, intent,
		// PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, id, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		// mContext.startService(intent);
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, start, pi);
	}

	public void closeAlarm(int id, String title, String content) {
		Intent intent = new Intent();
		intent.putExtra("_id", id);
		intent.putExtra("title", title);
		intent.putExtra("content", content);
		intent.setClass(mContext, CallAlarm.class);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, id, intent, 0);
		mAlarmManager.cancel(pi);
	}
}
