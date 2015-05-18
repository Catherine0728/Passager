package com.ky.utills;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @描述 网络工具类，用于wap设置代理，判断网络类型
 * 
 * @author
 * 
 */
public class NetWorkUtills {

	public static String imageUrl = "http://img0.imgtn.bdimg.com/it/u=1946512697,838287685&fm=23&gp=0.jpg";

	/**
	 * 检测无线wifi是否连接
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return false;
		}
		int netType = info.getType();
		// int netSubType = info.getSubtype();
		if (ConnectivityManager.TYPE_WIFI == netType) {
			return info.isConnectedOrConnecting();
		}
		return false;
	}

	/**
	 * 检查移动数据是否连接了
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isConnected();
			}
		}
		return false;
	}

	/**
	 * 检查移动数据是否有可用的移动网络数据
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileAvailable(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 检查网络是否可用
	 * 
	 * @return true可用,false不可用
	 */
	public static boolean isNetworkValidate(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnected()
				&& cm.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) {
			return cm.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	/**
	 * 获取手机Wifi的mac地址
	 * 
	 * @return mac地址,获取失败返回null
	 */
	public static String getWifiMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return wifi.getConnectionInfo().getMacAddress();
	}

	/**
	 * 获取路由器Mac
	 */
	public static String getRouterMac(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return wifi.getConnectionInfo().getBSSID();
	}

	/**
	 * 获取wifi名字
	 */
	public static String getWifiName(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return wifi.getConnectionInfo().getSSID();
	}

	/**
	 * 获取手机ip
	 * 
	 * @return
	 */
	public static String getLocalIp(Context context) {
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = intToIp(ipAddress);
		return ip;
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}
}
