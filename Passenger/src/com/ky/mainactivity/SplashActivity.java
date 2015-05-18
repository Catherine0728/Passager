package com.ky.mainactivity;

import java.io.IOException;

import com.ky.passenger.R;
import com.ky.utills.FileHelper;
import com.ky.utills.NetProperty;
import com.ky.utills.Configure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * 功能：使用ViewPager实现初次进入应用时的引导页
 * 
 * (1)判断是否是首次加载应用--采取读取SharedPreferences的方法 (2)是，则进入引导activity；否，则进入MainActivity
 * (3)5s后执行(2)操作
 * 
 * @author sz082093
 * 
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		boolean mFirst = isFirstEnter(SplashActivity.this, SplashActivity.this
				.getClass().getName());
		if (mFirst)
			mHandler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY, 2000);
		else
			mHandler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY, 2000);
	}

	// ****************************************************************
	// 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段
	// ****************************************************************
	private static final String SHAREDPREFERENCES_NAME = "my_pref";
	private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

	private boolean isFirstEnter(Context context, String className) {
		if (context == null || className == null
				|| "".equalsIgnoreCase(className))
			return false;
		String mResultStr = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
				KEY_GUIDE_ACTIVITY, "");// 取得所有类名 如 com.my.MainActivity
		if (mResultStr.equalsIgnoreCase("false"))
			return false;
		else
			return true;
	}

	// *************************************************
	// Handler:跳转至不同页面
	// *************************************************
	private final static int SWITCH_MAINACTIVITY = 1000;
	private final static int SWITCH_GUIDACTIVITY = 1001;
	public Handler mHandler = new Handler() {
		Intent mIntent = new Intent();

		public void handleMessage(Message msg) {
			switch (msg.what) {

			case SWITCH_MAINACTIVITY:
				// 判断网络
				if (NetProperty.getAPNType(SplashActivity.this) != -1) {
					// 有网络
					mHandler.sendEmptyMessage(SUCCESS);

				} else {

					mHandler.sendEmptyMessage(FAILED);

				}

				break;
			case SWITCH_GUIDACTIVITY:
				mIntent = new Intent();
				mIntent.setClass(SplashActivity.this, GuideActivity.class);
				SplashActivity.this.startActivity(mIntent);
				SplashActivity.this.finish();
				break;
			case SUCCESS:
				Boolean isModifer = GetSDText();
				String ssid = NetProperty.getSSID(SplashActivity.this);
				if (ssid.indexOf("sap37") != -1) {
					Configure.ISOUTNET = false;
				} else {
					Configure.ISOUTNET = true;

				}
				mIntent.setClass(SplashActivity.this, BaseActivity.class);
				mIntent.putExtra("isModifer", isModifer);
				SplashActivity.this.startActivity(mIntent);
				SplashActivity.this.finish();
				// new GetCityTwo(getApplicationContext());
				break;
			case FAILED:
				Configure.ISOUTNET = true;
				mIntent.setClass(SplashActivity.this, BaseActivity.class);
				SplashActivity.this.startActivity(mIntent);
				SplashActivity.this.finish();

				break;
			}
			super.handleMessage(msg);
		}
	};

	public static final int SUCCESS = 0x000001;
	public static final int FAILED = 0x000002;
	private FileHelper helper;

	/**
	 * 
	 * 
	 * 写一个方法，去获取当前需要的一些认证信息
	 * */
	public boolean GetSDText() {

		helper = new FileHelper(getApplicationContext());
//		try {
//			System.out.println("创建文件："
//					+ helper.createSDFile("test.txt").getAbsolutePath());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		// System.out.println("删除文件是否成功:" + helper.deleteSDFile("xx.txt"));
		if (helper.ISExistFile("test.txt")) {

			/* 存在，就应该读取里面的内容 */
//			helper.writeSDFile(
//					"192.168.150.18-192.168.1.14-sadgasgdagf-12345678",
//					"test.txt");
			return true;
		} else {

			/* 不存在 */

			return false;
		}

	}
}