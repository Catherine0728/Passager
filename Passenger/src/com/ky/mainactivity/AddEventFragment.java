package com.ky.mainactivity;

import java.util.Calendar;

import com.android.viewflow.TimeSetActivity;
import com.ky.db.GetInterCutDB;
import com.ky.passenger.R;
import com.ky.utills.AlarmHelper;
import com.ky.utills.DateToFormat;
import com.ky.utills.ObjectPool;
import com.ky.utills.Configure;
import com.redbull.log.Logger;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

/**
 * this is radd the remind
 * 
 * @author Catherine.Brain
 * */
public class AddEventFragment extends Fragment implements OnClickListener {
	public static String TAG = "AddEventFragment";
	View view = null;
	Button btn_okay;
	EditText edit_Con;
	Button btn_Time;
	public static String FRAGMENTNAME = "取消";
	LinearLayout layout_Touch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == view) {
			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.add_event, null);
		}
		ViewGroup p = (ViewGroup) view.getParent();
		if (p != null) {
			p.removeView(view);
		}
		initView();
		return view;
	}

	public void initView() {
		Configure.PAGER = 6;
		BaseActivity.btn_Return.setVisibility(View.VISIBLE);
		BaseActivity.btn_logo.setVisibility(View.GONE);
		BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		layout_Touch = (LinearLayout) view.findViewById(R.id.layout_touch);
		layout_Touch.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				close();
				return false;
			}
		});
		btn_okay = (Button) view.findViewById(R.id.btn_okay);
		edit_Con = (EditText) view.findViewById(R.id.edit_content);
		btn_Time = (Button) view.findViewById(R.id.edit_time);
		btn_Time.setOnClickListener(this);
		btn_okay.setOnClickListener(this);
		ObjectPool.mAlarmHelper = new AlarmHelper(getActivity());
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		BaseActivity base = (BaseActivity) getActivity();
		switch (v.getId()) {
		case R.id.btn_okay:

			newContent = new RemindMeFragment();
			base.switchContent(newContent, "添加", false);

			break;
		case R.id.edit_time:
			conText = edit_Con.getText().toString().trim();
			if (conText.equals("") || conText == null) {

				edit_Con.setError("请输入提醒内容！");
			} else {
				Logger.d(TAG, "the content is===>" + conText);
				Intent intent = new Intent(getActivity(), TimeSetActivity.class);
				intent.putExtra("content", conText);
				startActivityForResult(intent, REQUEST_CODE);

			}
			break;
		default:
			break;
		}

	}

	final int RESULT_CODE = 101;
	final int REQUEST_CODE = 1;
	String conText = "别忘了您的发车时间哦！";
	int hour, min;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_CODE) {
				String result = data.getStringExtra("time");
				btn_Time.setText("设置的时间为:" + result);
				GetInterCutDB db = new GetInterCutDB(getActivity());
				db.insert(conText, result);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void ToastTime() {
		final Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(System.currentTimeMillis());
		int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
		int mMinute = mCalendar.get(Calendar.MINUTE);
		new TimePickerDialog(getActivity(),
				new TimePickerDialog.OnTimeSetListener() {
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						mCalendar.setTimeInMillis(System.currentTimeMillis());

						mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						mCalendar.set(Calendar.MINUTE, minute);
						mCalendar.set(Calendar.SECOND, 0);
						mCalendar.set(Calendar.MILLISECOND, 0);
						hour = hourOfDay;
						min = minute;
						DateToFormat datetoF = new DateToFormat();
						datetoF.getHOrS(mCalendar);
						ObjectPool.mAlarmHelper.openAlarm(0, 5000, conText,
								mCalendar.getTimeInMillis());
						Logger.d(TAG,
								"the time is==>" + mCalendar.getTimeInMillis());
						Logger.d(TAG,
								"the curt id==>" + System.currentTimeMillis());
					}
				}, mHour, mMinute, true).show();
	}

	/**
	 * 
	 * 关闭软键盘
	 * **/
	public void close() {

		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_Con.getWindowToken(), 0);
	}
}
