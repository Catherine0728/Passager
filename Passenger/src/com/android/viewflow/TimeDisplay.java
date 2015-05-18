package com.android.viewflow;

import com.ky.passenger.R;
import com.redbull.log.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class TimeDisplay extends Activity {
	public String TAG = "TimeDisplay";
	TextView text;
	Button btn;
	String con = "别忘了您的发车时间哦！";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.display_text);
		Intent intent = getIntent();
		con = intent.getStringExtra("content");
		Logger.d(TAG, "the content is==>" + con);
		initView();
	}

	public void initView() {
		text = (TextView) findViewById(R.id.text_display);
		text.setText(con);
		btn = (Button) findViewById(R.id.btn_close);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}
