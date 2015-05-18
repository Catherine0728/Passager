package com.ky.exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;





import com.redbull.log.Logger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

public class CrashHandler implements UncaughtExceptionHandler {

	/**
	 * 记录错误日志文件到本地路径
	 */
	public static String logEPath = "mnt/sdcard/Android/logE";

	// CrashHandler实例
	private static CrashHandler INSTANCE = new CrashHandler();
	// 程序的Context对象
	private Context mContext;
	GetFileSize getSize;
	// int count = 0;
	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	private CrashHandler() {

	}

	/**
	 * 获取CrashHandler实例 ,单例模式
	 * 
	 * @return
	 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {

		mContext = context;
		getSize = new GetFileSize(logEPath);
		// 获取系统默认的UncaughtException
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3 * 1000);
			} catch (Exception e) {
				Logger.e("Error","error:" + e.getMessage() + "," + e.toString());
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			myHandler.sendEmptyMessage(1);
			System.exit(0);
			// to restart the project

		}
	}

	Handler myHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			toReRoot();
		};
	};

	public void toReRoot() {

		// VLCApplication.EditShare(false);
		// Intent i = new Intent().setClass(mContext, BeforePreActivity.class);
		// /**
		// * i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); the sentence is to over
		// * whole the activity of the process
		// * */
		// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// mContext.startActivity(i);
		PackageManager manager = mContext.getPackageManager();
		Intent it = manager.getLaunchIntentForPackage("com.example.clock");
		it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(it);

	}

	/**
	 * 自定义错误处理，收集错误信息 发送错误报告等操作均在此完成
	 * 
	 * @param ex
	 * @return 处理了该异常信息返回true，否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				CommonToast toast = new CommonToast(mContext);
				toast.setDuration(1000);
				toast.setText("很抱歉，程序获取数据受阻，将为您重新加载。。");
				toast.show();
				Looper.loop();
				super.run();
			}

		}.start();
		getSize.toDoFile();
		// 保存日志文件到本地
		saveCrashInfo(ex);
		return true;
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return
	 */
	private File saveCrashInfo(Throwable ex) {
		File Lfile = new File(logEPath);

		StringBuffer sb = new StringBuffer();

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();

		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);

		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());

			File dir = new File(logEPath);

			String fileName = "homecrash-" + time + "-" + timestamp + ".log";
			if (!dir.exists()) {
				System.out.println("dir.mkdirs();");
				dir.mkdirs();
			}

			File file = new File(dir, fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);

			fos.write(sb.toString().getBytes());
			System.out.println("fos is==>" + fos);
			fos.close();

			return file;
		} catch (Exception e) {
			Logger.e("an error occured while writing file...", "" + e);
		}
		return null;
	}

}
