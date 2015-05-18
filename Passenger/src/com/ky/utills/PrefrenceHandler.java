package com.ky.utills;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefrenceHandler {

	SharedPreferences settings;

	static Context mContext;

	public static final String PREFS_DOWNLOAD_TARGET = "download_target";

	public static final String PREFS_DOWNLOAD_POSITION = "download_position";


	public PrefrenceHandler(Context context) {
		this.settings = PreferenceManager.getDefaultSharedPreferences(context);
		mContext = context;
	}

	public String getBaseIP() {
		return this.settings.getString("BASEIP", "");
	}

	public void setBaseIP(String baseIp) {
		this.settings.edit().putString("BASEIP", baseIp).commit();
	}




	public static String getServerAddress(Context context) {
		String address = context.getSharedPreferences("settingServer",
				context.MODE_PRIVATE).getString("SERVER",
				Configure.IP);
		return address;
	}

	public static String getInfos(Context context) {
		String infos = context.getSharedPreferences("aboutus",
				context.MODE_PRIVATE).getString("infos", "");
		return infos;
	}

	public static String getContract(Context context) {
		String contract = context.getSharedPreferences("aboutus",
				context.MODE_PRIVATE).getString("contract", "");
		return contract;
	}


	public void setDownloadTarget(String targetFile) {
		setString(PREFS_DOWNLOAD_TARGET, targetFile);
	}

	private void setString(String key, String str) {
		SharedPreferences.Editor mEditor = settings.edit();
		mEditor.putString(key, str);
		mEditor.commit();
	}

	public String getDownloadTarget() {
		return settings.getString(PREFS_DOWNLOAD_TARGET, "");
	}

	public void setDownLoadPos(long position) {
		setLong(PREFS_DOWNLOAD_POSITION, position);
	}

	private void setLong(String key, long Long) {
		SharedPreferences.Editor mEditor = settings.edit();
		mEditor.putLong(key, Long);
		mEditor.commit();
	}


	public long getDownloadPos() {
		return settings.getLong(PREFS_DOWNLOAD_POSITION, 0);
	}


}
