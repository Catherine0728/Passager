package com.lhl.exception;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;

/**
 * this is a class to get the request and the response.......it have maked the
 * client interaction with server
 * 
 * @author Catherine.Brain
 */
public class CrashInteraction {

	/**
	 * to write the log int he localpath
	 */
	public static String logIPath = "mnt/sdcard/Android/logG";
	// int count = 0;
	GetFileSize getSize;
	private static CrashInteraction INSTANCE = new CrashInteraction();
	Context mContext;
	String request;

	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	private CrashInteraction() {

	}

	/**
	 * 获取CrashInteraction实例 ,单例模式
	 * 
	 * @return
	 */
	public static CrashInteraction getInstance() {
		return INSTANCE;
	}

	public void init(Context context) {
		// TODO Auto-generated method stub
		mContext = context;
		getSize = new GetFileSize(logIPath);

	}

	public void initStr(String str) {

		request = str;
		getSize.toDoFile();
		saveCrashInfo(request);
	}

	private File saveCrashInfo(String str) {

		StringBuffer sb = new StringBuffer();

		sb.append(str);

		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());

			File dir = new File(logIPath);

			String fileName = "get" + time + "-" + timestamp + ".log";
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
			fos.close();

			return file;
		} catch (Exception e) {
			System.out.println("an error occured while writing file..." + e);
		}

		return null;
	}

	/**
	 * 删除文件夹所有内
	 * 
	 */
	public boolean deleteFile(File file) {
		boolean isSuccess = false;
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文
				file.delete(); // delete()方法 你应该知
			} else if (file.isDirectory()) { // 否则如果它
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					this.deleteFile(files[i]); // 把每个
				}
			}
			file.delete();
			isSuccess = true;
		} else {
			isSuccess = false;
		}
		return isSuccess;
	}
}
