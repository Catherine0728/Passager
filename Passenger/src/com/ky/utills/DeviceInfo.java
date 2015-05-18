package com.ky.utills;

import com.redbull.log.Logger;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;

public class DeviceInfo {
	public static String TAG = "DeviceInfo";

	public static void DeviceDisplayMetrics(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();

		int width = display.getWidth();
		int height = display.getHeight();
		Logger.d(TAG, "the width is==>" + width + "==and the height is===>"
				+ height);
	}

	// android 版本
	public static String getAndroidVersion() {
		return Build.VERSION.RELEASE;

	}

//	/**
//	 * 固件版本
//	 */
//	public static String getFirmWare() {
//		String firmware = getString("ro.product.firmware");
//		return firmware;
//	}

	/**
	 * launcher版本
	 */
	public static String getVerName(Context context) {
		String str = null;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			str = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 唯一标示
	 * 
	 * @param context
	 * @return
	 */
	public static String getOnlyId(Context context) {

		return Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ANDROID_ID);

	}
}
