package com.ky.utills;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.redbull.log.Logger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * to judge the WIFI or the net's properties
 * 
 * @author Catherine.Brain
 * */
public class NetProperty {
	/**
	 * 判断是否有网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conManager == null) {
			Logger.log("network is not available");
			return false;
		} else {
			NetworkInfo[] info = conManager.getAllNetworkInfo();
			if (info == null) {
				Logger.log("network is not available");
				return false;
			} else {
				for (int i = 0; i < info.length; i++) {
					if (info[i].isAvailable() && info[i].isConnected()) {
						Logger.log("network is available");
						return true;
					}
				}
			}

		}
		Logger.log("network is not available");
		return false;
	}

	// 如果为-1 则没有网络连接
	public static int getAPNType(Context context) {
		int netType;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null) {
			return -1;
		} else {
			netType = networkInfo.getType();
		}
		System.out.println("-------------------");
		return netType;
	}

	// wifi连接
	public static boolean isWifiConnect(Context context) {
		int netType = getAPNType(context);
		if (netType == ConnectivityManager.TYPE_WIFI) {
			return true;
		} else {
			return false;
		}
	}

	// 以太网连接判断
	public static boolean isEthernetConnect(Context context) {
		int netType = getAPNType(context);
		if (netType == ConnectivityManager.TYPE_ETHERNET) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取网址内容
	 */
	public static String getContent(String url) throws Exception {
		StringBuilder sb = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		// 设置网络超时参数
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpResponse response = client.execute(new HttpGet(url));
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"), 8192);
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
		}
		return sb.toString();
	}

	public static String getSSID(Context context) {

		WifiManager wifiManager = (WifiManager) context
				.getSystemService(context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		System.out.println("wifiInfo"+ wifiInfo.toString());
		System.out.println("SSID"+wifiInfo.getSSID());
		System.out.println("getBSSID" + wifiInfo.getBSSID());
		Configure.MAC=wifiInfo.getBSSID();
		return wifiInfo.getSSID();
	}

	public static String getBSSID(Context context) {

		WifiManager wifiManager = (WifiManager) context
				.getSystemService(context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		System.out.println("wifiInfo"+ wifiInfo.toString());
		System.out.println("SSID"+wifiInfo.getSSID());
		System.out.println("getBSSID" + wifiInfo.getBSSID());
		Configure.MAC=wifiInfo.getBSSID();
		return wifiInfo.getBSSID();
	}

}
