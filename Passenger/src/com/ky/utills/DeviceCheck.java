package com.ky.utills;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import com.redbull.log.Logger;

/**
 * 
 * 这是一个关于设备信息的类
 * */
public class DeviceCheck {

	/**
	 * androidId
	 * 
	 * @param context
	 * @return
	 */
	public static String getAndroidId(Context context) {
		String str = null;
		str = getOnlyId(context);
		return str;
	}

	/**
	 * getEthernetMac
	 * 
	 * @param context
	 * @return
	 */
	public static String getEthernetMac(Context context) {
		String ethernetMac = "00.00.00.00.00.00";
		// if (NetUtils.getAPNType(context) != -1) {
		// if (NetUtils.isEthernetConnect(context)) {
		ethernetMac = EthernetInfo.getMacAddress();
		// }
		// }
		if (StringTools.isNullOrEmpty(ethernetMac)) {
			ethernetMac = fetch_ethmac();
		}

		return ethernetMac;
	}

	/**
	 * fetch_ethmac
	 * 
	 * @return
	 */
	public static String fetch_ethmac() {
		String result = null;
		String[] args = { "/system/bin/cat", "/sys/class/net/eth0/address" };
		result = run(args, "system/bin/");
		result = result.substring(0, result.indexOf("\n")).toUpperCase();
		if (result.length() > 27) {
			result = result.substring(0, 27);
		}
		return result;
	}

	/**
	 * fetch_wlanmac
	 * 
	 * @return
	 */
	public static String fetch_wlanmac() {
		String result = null;
		try {
			String[] args = { "/system/bin/cat", "/sys/class/net/wlan0/address" };
			result = run(args, "system/bin/");
			result = result.substring(0, result.indexOf("\n"));
			if (result.length() > 28) {
				result = result.substring(0, 28);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public synchronized static String run(String[] cmd, String workdirectory) {
		String result = "";
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			InputStream in = null;
			if (workdirectory != null) {
				builder.directory(new File(workdirectory));
				builder.redirectErrorStream(true);
				Process process = builder.start();
				in = process.getInputStream();
				byte[] re = new byte[1024];
				while (in.read(re) != -1)
					result = result + new String(re);
			}
			if (in != null) {
				in.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	/**
	 * getWifiMac
	 * 
	 * @param context
	 * @return
	 */
	public static String getWifiMac(Context context) {
		String wifiMac = "00.00.00.00.00.00";
		WifiManager wifiMgr = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info && !StringTools.isNullOrEmpty(info.getMacAddress())) {
			wifiMac = info.getMacAddress();
			if (StringTools.isNullOrEmpty(wifiMac)) {
				wifiMac = fetch_wlanmac();
			}
		}
		return wifiMac;
	}

	/**
	 * 获取路由器Mac
	 */
	public static String getRouterMac(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			String netName = wifiInfo.getSSID(); // 获取被连接网络的名称
			String netMac = wifiInfo.getBSSID(); // 获取被连接网络的mac地址
			
			String localMac = wifiInfo.getMacAddress();// 获得本机的MAC地址
			Log.d("getRouterMac", "getRouterMac---netName:" + netName); // ---netName:HUAWEI
															// MediaPad
			Log.d("getRouterMac", "getRouterMac---netMac:" + wifiInfo.getBSSID()); // ---netMac:78:f5:fd:ae:b9:97
			Log.d("getRouterMac", "getRouterMac---localMac:" + localMac); // ---localMac:BC:76:70:9F:56:BD
		}
		return wifiManager.getConnectionInfo().getBSSID();
	}

	/**
	 * getGateWay
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getGateWay(Context mContext) {
		WifiManager wifiMgr = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcpInfo = null;
		if (wifiMgr != null) {
			dhcpInfo = wifiMgr.getDhcpInfo();
		}
		if (dhcpInfo != null) {
			return int2IPStr(dhcpInfo.gateway);

		}
		return "";
	}

	/**
	 * intת��ΪString
	 * 
	 * @param ip
	 * @return
	 */
	public static String int2IPStr(int ip) {
		int[] b = new int[4];
		b[0] = (ip >> 24) & 0xff;
		b[1] = (ip >> 16) & 0xff;
		b[2] = (ip >> 8) & 0xff;
		b[3] = (ip & 0xff);
		String ipStr;
		ipStr = Integer.toString(b[3]) + "." + Integer.toString(b[2]) + "."
				+ Integer.toString(b[1]) + "." + Integer.toString(b[0]);
		Logger.log("ipStr:" + ipStr);
		return ipStr;
	}

	/**
	 * wifi
	 * 
	 * @param context
	 * @return
	 */
	public static String getOnlyId(Context context) {

		return Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ANDROID_ID);

	}

	/**
	 * 
	 * 判断是否连接的是WIFI
	 * */
	public static void isNetworkAvailable(Context mContext) {
		// 得到网络连接信息
		ConnectivityManager manager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (gprs == State.CONNECTED || gprs == State.CONNECTING) {
			Configure.WIFIMAC = getWifiMac(mContext);
			Configure.ETHMAC = getEthernetMac(mContext);
			System.out.println("the wifi is close");

		}
		// 判断为wifi状态下才加载广告，如果是GPRS手机网络则不加载！
		if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			System.out.println("the wifi is open");
			Configure.WIFIMAC = getWifiMac(mContext);
			Configure.ETHMAC = getEthernetMac(mContext);
			Configure.MAC = Configure.WIFIMAC;
		} else {
			Configure.MAC = Configure.ETHMAC;
		}
	}

	public static String intToIp(int i) {
		return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
				+ ((i >> 8) & 0xFF) + "." + (i & 0xFF);
	}

	/**
	 * 
	 * 获取当前android设备可用内存的大小
	 * */
	public static String getAvailMemory(Context context) {// 获取android当前可用内存大小

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存

		return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
	}

	/**
	 * 
	 * 获取系统的内存信息文件
	 * */
	public static String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

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
		String ip = intToIptwo(ipAddress);
		return ip;
	}

	private static String intToIptwo(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

}
