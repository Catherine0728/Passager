package com.ky.utills;

import com.ky.mainactivity.BaseActivity;
import com.lhl.dialog.library.AlertDialog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class CallAlarm extends BroadcastReceiver {
	// AlertDialog alert = null;
	// WebView webView;
	// View v = null;
	// Context mContext;
	public static final int CREATE_DIALOG = 0x001;
	public static final int CANCEL_DIALOG = 0x002;
	// ToastDialog dialog = null;
	private String TAG = "CallAlarm";
	Intent intent;
	Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		this.intent = intent;
		/**
		 * 
		 * 第一种形式，销毁不了对话框了
		 * */
		myHandler.sendEmptyMessage(CREATE_DIALOG);

	}

	Thread myThread = new Thread() {

		public void run() {
			System.out.println("我在休眠的线程了");
			try {
				sleep(5000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				// StaticVirable.ISDO = 0;
				myHandler.sendEmptyMessage(CANCEL_DIALOG);
				// myHandler.sendEmptyMessageAtTime(, 3000);
			}
		};
	};
	Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case CREATE_DIALOG:
				System.out.println("CREATE_DIALOG");
				// new AlertDialog(mContext, AlertDialog.NOLOGIN_TYPE)
				// .setTitleText("你的时间已到").show();
				String text = intent.getStringExtra("_text");
				Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();

				break;
			case CANCEL_DIALOG:
				break;

			}
		};

	};

}
