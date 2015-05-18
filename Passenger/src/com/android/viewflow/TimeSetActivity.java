package com.android.viewflow;

import java.util.Calendar;
import java.util.TimeZone;
import com.ky.passenger.R;
import com.ky.service.AlarmReceiver;
import com.redbull.log.Logger;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

/***
 * 
 * 设置时间
 **/
public class TimeSetActivity extends Activity {
	String TAG = "TimeSetActivity";
	Button btn_Okay;
	private TimePicker mTimePicker;

	int mHour = -1;
	int mMinute = -1;

	String content = "别忘了您的发车时间哦！";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.time_set);
		initView();
		Intent i = getIntent();
		content = i.getStringExtra("content");
		Logger.d(TAG, "the content is==>" + content);
	}

	public void initView() {
		btn_Okay = (Button) findViewById(R.id.btn_ok);

		// 获取当前时间
		Calendar calendar = Calendar.getInstance();
		// calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//		calendar.setTimeZone(TimeZone.getDefault());
		if (mHour == -1 && mMinute == -1) {
			mHour = calendar.get(Calendar.HOUR_OF_DAY);
			mMinute = calendar.get(Calendar.MINUTE);
		}

		mTimePicker = (TimePicker) findViewById(R.id.timePicker);
		mTimePicker.setCurrentHour(mHour);
		mTimePicker.setCurrentMinute(mMinute);
		mTimePicker
				.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

					@Override
					public void onTimeChanged(TimePicker view, int hourOfDay,
							int minute) {

						mHour = hourOfDay;
						mMinute = minute;
					}
				});
		final int RESULT_CODE = 101;
		btn_Okay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TimeSetActivity.this,
						AlarmReceiver.class);
				intent.putExtra("content", content);
				PendingIntent sender = PendingIntent.getBroadcast(
						TimeSetActivity.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				// 过10s 执行这个闹铃
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				// calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//				calendar.setTimeZone(TimeZone.getDefault());

				calendar.set(Calendar.HOUR_OF_DAY, mHour);
				calendar.set(Calendar.MINUTE, mMinute-1);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				// 进行闹铃注册
				AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
				manager.set(AlarmManager.RTC_WAKEUP,
						calendar.getTimeInMillis(), sender);

//				Toast.makeText(TimeSetActivity.this, "设置提醒成功!",
//						Toast.LENGTH_LONG).show();
				Intent in = new Intent();
				in.putExtra("time", mHour + ":" + mMinute);
				setResult(RESULT_CODE, in);
				finish();
			}
		});

	}

}
