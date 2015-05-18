package com.ky.utills;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;

/**
 * @author Catherine.Brain 去获得指定路径里面的图片，视频以及游戏
 * */
public class GetVedioPath {

	/**
	 * 先判断是否有SD卡
	 * */
	public String isSdcard() {
		File sdcardDir = null;
		boolean isSDExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (isSDExist) {
			// 如果存在SDcard 就找到跟目录
			sdcardDir = Environment.getExternalStorageDirectory();
			return sdcardDir.toString();
		} else {
			return null;
		}
	}

	public int getvediopath(String sdpath, String key) {
		// 打开SD卡目录
		File file = new File(sdpath);
		// 获取SD卡目录列表
		File[] files = file.listFiles();
		int len = isfile(files, key);
		// return files.length;
		return len;
	}

	ArrayList<Map<String, String>> listsd = null;
	Map<String, String> mapsd;
	int fileLength = 0;

	// 如果是文件的话，就跳入isfile(File file);的方法中，如果是文件夹的话就跳入notfile（File file）;方法中。
	public int isfile(File[] file, String key) {
		// 如果是有文件isfile 就配合他文件名的后缀名是否为图片，是的话放进mapsd中，再加到list中。
		// String fnm = file.getPath();
		listsd = new ArrayList<Map<String, String>>();

		File[] new_file = file;

		for (int i = 0; i < new_file.length; i++) {
			File fis = new_file[i];
			if (fis.isFile()) {
				String filename = new_file[i].getName();
				int idx = filename.lastIndexOf(".");

				if (idx <= 0) {
					return 0;
				}
				// 获取url的后缀，即链接的类型
				String videoType = filename.substring(idx);
				// String suffix = filename.substring(idx + 1,
				// filename.length());
				if (key.equals("vedio_path")) {
					if (videoType.equalsIgnoreCase(".mp4")
							|| videoType.equalsIgnoreCase(".3gp")
							|| videoType.equalsIgnoreCase(".AVI")
							|| videoType.equalsIgnoreCase(".WMV")
							|| videoType.equalsIgnoreCase(".rmvb")
							|| videoType.equalsIgnoreCase(".flv")) {
						mapsd = new HashMap<String, String>();
						mapsd.put(key, new_file[i].getPath().toString());
						listsd.add(mapsd);

					}
				} else if (key.equals("novel_path")) {
				} else if (key.equals("game_path")) {

					if (videoType.equalsIgnoreCase(".apk")) {
						mapsd = new HashMap<String, String>();
						mapsd.put(key, new_file[i].getPath().toString());
						listsd.add(mapsd);

					}
				} else {
				}

				fileLength = listsd.size();
			} else {
				String SDpath = fis.getPath();
				File fileSD = new File(SDpath);
				File[] filess = fileSD.listFiles();
				if (filess == null) {
					return 0;
				}
				int len = 0;
				for (int j = 0; j < filess.length; j++) {
					len = getvediopath(fileSD.toString(), key);
				}
				fileLength += len;
			}
		}
		return fileLength;

	}

	// 如果有文件是文件夹的话，就继续用file.listFile()打开它的文件夹里面的内容，再用for语句判断它里面的文件是否有文件，如果有就isfile();无，就利用getpicpath()打开它。
	public void notfile(File file, String key) {
		File[] files = file.listFiles();
		if (files == null) {
			return;
		}
		isfile(files, key);

	}

	/**
	 * 
	 * 获得视频以及游戏的列表
	 * */
	public ArrayList<String> getVdio(String key) {
		ArrayList<String> vedio_Path = new ArrayList<String>();
		for (Map<String, String> name : listsd) {
			vedio_Path.add(name.get(key));
		}
		System.out.println("the " + key + " is===>" + vedio_Path);
		return vedio_Path;

	}

}
