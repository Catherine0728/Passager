package com.ky.exception;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * 
 * to get the sdCard size or the file size only you want
 * 
 * @author Catherine.Brain
 * */
public class GetFileSize {
	String logPath;

	public GetFileSize(String path) {
		logPath = path;

	}

	public void toDoFile() {
		long startTime = System.currentTimeMillis();
		try {
			long l = 0;
			File ff = new File(logPath);
			if (ff.exists()) {
				if (ff.isDirectory()) {
					// System.out.println("文件夹个数           " + getlist(ff));
					l = getFileSize(ff);
					System.out.println(logPath + "目录的大小为：" + FormetFileSize(l));
				} else if (ff.isFile()) {

					// System.out.println("     文件个数           1");
					l = getFileSizes(ff);
					System.out.println(logPath + "文件的大小为：" + FormetFileSize(l));
				}
			} else {
				System.out.println("日志文件路径不存在。。。");
			}
			if (l > 1048576) {
				System.out.println("delete");
				deleteFile(new File(logPath));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		// System.out.println("总共花费时间为：" + (endTime - startTime) + "毫秒...");

	}

	public long getFileSizes(File f) throws Exception {// 取得文件大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}

	// 递归
	public long getFileSize(File f) throws Exception// 取得文件夹大小
	{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public long getlist(File f) {// 递归求取目录文件个数
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}

	/**
	 * 删除文件夹所有内容
	 * 
	 */
	public static boolean deleteFile(File file) {
		boolean isSuccess = false;
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
			System.out.println("success");
			isSuccess = true;
		} else {
			isSuccess = false;
			System.out.println("failes");
		}
		return isSuccess;
	}
}
