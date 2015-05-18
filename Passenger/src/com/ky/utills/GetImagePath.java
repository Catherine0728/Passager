package com.ky.utills;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Environment;

/**
 * @author Catherine.Brain 去获得指定路径里面的图片
 * */
public class GetImagePath {

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

	public int getpicpath(String sdpath) {
		// 打开SD卡目录
		File file = new File(sdpath);
		// 获取SD卡目录列表
		File[] files = file.listFiles();
		System.out.println("the file is===>" + files);
		for (int z = 0; z < files.length; z++) {
			// System.out.println("the file is===>" + files[z]);
			File f = files[z];
			if (f.isFile()) {
				isfile(f);
			} else {
				notfile(f);
			}
		}
		return files.length;
	}

	ArrayList<Map<String, String>> listsd = new ArrayList<Map<String, String>>();
	Map<String, String> mapsd;

	// 如果是文件的话，就跳入isfile(File file);的方法中，如果是文件夹的话就跳入notfile（File file）;方法中。
	public void isfile(File file) {
		// 如果是有文件isfile 就配合他文件名的后缀名是否为图片，是的话放进mapsd中，再加到list中。
		// String fnm = file.getPath();

		mapsd = new HashMap<String, String>();
		String filename = file.getName();
		int idx = filename.lastIndexOf(".");

		if (idx <= 0) {
			return;
		}
		String suffix = filename.substring(idx + 1, filename.length());
		if (suffix.toLowerCase().equals("jpg")
				|| suffix.toLowerCase().equals("jpeg")
				|| suffix.toLowerCase().equals("bmp")
				|| suffix.toLowerCase().equals("png")
				|| suffix.toLowerCase().equals(".gif")) {

			mapsd.put("Image_Path", file.getPath().toString());
			listsd.add(mapsd);

		}
	}

	// 如果有文件是文件夹的话，就继续用file.listFile()打开它的文件夹里面的内容，再用for语句判断它里面的文件是否有文件，如果有就isfile();无，就利用getpicpath()打开它。
	public void notfile(File file) {
		File[] files = file.listFiles();
		if (files == null) {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			File fis = files[i];
			if (fis.isFile()) {
				isfile(fis);
			} else {
				String SDpath = fis.getPath();
				File fileSD = new File(SDpath);
				File[] filess = fileSD.listFiles();
				if (filess == null) {
					return;
				}
				for (int j = 0; j < filess.length; j++) {
					getpicpath(fileSD.toString());
				}
			}
		}
	}

	public ArrayList<String> getIamge() {
		ArrayList<String> image_Path = new ArrayList<String>();
		for (Map<String, String> name : listsd) {
			image_Path.add(name.get("Image_Path"));
		}
		// System.out.println("the listimage is===>" + image_Path);
		return image_Path;

	}
}
