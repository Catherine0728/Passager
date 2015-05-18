package com.redbull.log;

import android.util.Log;

/**
 * 
 * 这是一个重写类 （将日志重写） 终于可以规范了（虽然是小问题，但是，一个好的个人习惯固然重要）
 * 
 * @author Catherine.Brain
 * */
public class Logger {
	/**
	 * 
	 * @param log想要提醒我们的日志
	 * 
	 * */

	public static void log(String log) {
		// if (BuildConfig.DEBUG )
		Log.d("Passager==>", log);
	}

	public static void logError(String log) {
		// if (BuildConfig.DEBUG )
		Log.e("Passager==>", log);
	}

	public static void logWarning(String log) {
		// if (BuildConfig.DEBUG )
		Log.w("Passager==>", log);
	}

	public static void logVerbose(String log) {
		// if (BuildConfig.DEBUG )
		Log.v("Passager==>", log);
	}

	public static void logInfo(String log) {
		// if (BuildConfig.DEBUG )
		Log.i("Passager==>", log);
	}

	/**
	 * 
	 * @param log
	 *            想要提醒我们的日志
	 * @param from
	 *            来自于哪个类或者标志性的
	 * */
	public static void d(String from, String log) {
		// if (BuildConfig.DEBUG )
		Log.d(from, log);
	}

	public static void e(String from, String log) {
		// if (BuildConfig.DEBUG )
		Log.e(from, log);
	}

	public static void w(String from, String log) {
		// if (BuildConfig.DEBUG )
		Log.w(from, log);
	}

	public static void v(String from, String log) {
		// if (BuildConfig.DEBUG )
		Log.v(from, log);
	}

	public static void i(String from, String log) {
		// if (BuildConfig.DEBUG )
		Log.i(from, log);
	}

}
