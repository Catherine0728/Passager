package com.ky.service;

import java.util.Timer;
import java.util.TimerTask;

import com.android.viewflow.TimeDisplay;
import com.ky.mainactivity.BaseActivity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * 
 * @ClassName: AlarmReceiver
 * @Description: 闹铃时间到了会进入这个广播，这个时候可以做一些该做的业务。
 * @author HuHood
 * @date 2013-11-25 下午4:44:30
 * 
 */
public class AlarmReceiver extends BroadcastReceiver {
	// Timer timer;
	// TimerTask task;
	// long timeInterval = 1000 * 60 * 20;
	Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		String text = intent.getStringExtra("content");
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
		intent.putExtra("content", text);
		intent.setClass(mContext, TimeDisplay.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		mContext.startActivity(intent);
		remind();
	}

	public void remind() {
		
		Vibrator vibratorm = (Vibrator) mContext
				.getSystemService(Service.VIBRATOR_SERVICE);
		vibratorm.vibrate(new long[]{100,5000,500,5000,5000}, -1);
//		vibratorm.vibrate(new long[] { 100, 50, 100, 50, 100, 50, 100, 100, 50,
//				100, 50, 100, 50, 100, 50, 100, 50, 100, 50, 100, 100, 50, 100,
//				50, 100, 50, 100, 100, 50, 100, 50, 100, 50, 100 }, -1);
	}

}
