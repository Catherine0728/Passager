package com.ky.utills;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * String转换的工�?
 * 
 * @author Catherine
 * 
 */
public class StringTools {

	/**
	 * @param isostr
	 * @return 默认值变成utf-8类型
	 */
	public static String defaultToUtf(String isostr) {
		if (isostr != null) {
			try {
				isostr = new String(isostr.getBytes(), "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isostr;
	}

	/**
	 * �?测字符串是否为空或无内容
	 * 
	 * @param srcString
	 * @return
	 */
	public static boolean isNullOrEmpty(String srcString) {
		if (srcString != null && !srcString.equals("")
				&& !srcString.equals("null")) {
			return false;
		} else {
			return true;
		}
	}

	public static String getSystemTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		String time = sdf.format(date);
		return time;
	}

	/**
	 * 时间转化显示
	 * 
	 * @param time
	 * @return
	 */
	public static String generateTime(long time) {
		time = time / 1000;
		long hh = time / 3600;
		long mm = time % 3600 / 60;
		long ss = time % 60;
		String strTemp = null;
		if (0 != hh) {
			strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
		} else {
			strTemp = String.format("%02d:%02d", mm, ss);
		}

		return strTemp;
	}

	/**
	 * 秒的格式转化
	 * 
	 * @param paramLong
	 * @return
	 */
	public static String longToSec(long paramLong) {
		float f = (float) paramLong / 1000.0F;
		return new DecimalFormat("#.#").format(f) + "�?";
	}

	/**
	 * 获取后缀�?
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot);
			}
		}
		return filename;
	}

	/**
	 * 获取文件�?
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('/');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				filename = filename.substring(dot + 1);
				return filename;
			}
		}
		return filename;
	}

	/**
	 * 字符串比较大�?
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean compare(String s1, String s2) {

		int i = s2.compareTo(s1);
		boolean update;
		if (i <= 0) {
			update = false;
		} else {
			update = true;
		}
		return update;
	}

}
