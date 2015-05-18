package com.ky.utills;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class BaiDuLocation {

	public LocationListener mLocationListenner;
	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	public GeofenceClient mGeofenceClient;
	public Vibrator mVibrator;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	public static double getLatitude;
	public static double getLongitude;
	public static String getAddrStr;

	private SharedPreferences shared;
	private SharedPreferences.Editor editor;
	public NetWorkInterface Network;

	public BaiDuLocation(Context context) {
		// TODO Auto-generated constructor stub
		shared = context.getSharedPreferences("Location", context.MODE_PRIVATE);
		editor = shared.edit();

		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(context);
		mVibrator = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
	}

	public void InitLocation(NetWorkInterface Network) {
		this.Network = Network;
		System.out.println("================Network");
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值bd09ll
		int span = 3000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为3000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		mLocationClient.setLocOption(option);
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			getLatitude = location.getLatitude();
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			getLongitude = location.getLongitude();
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				getAddrStr = location.getAddrStr();
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				getAddrStr = location.getAddrStr();
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			logMsg(sb.toString());
			Log.i("BaiduLocationApiDem", sb.toString());

			if (getAddrStr != "" && getAddrStr != null
					&& !getAddrStr.equals(null)) {
				System.out.println(getAddrStr
						+ "++++++++++++++++++++++++!!!!!!!!!!!!!!!!!!!!");
				System.out.println("}}}}}}}}}}}}} ===>" + sb);
				// ArrayList<String> arr = new ArrayList<String>();
				// arr.add(getAddrStr);
				// arr.add(String.valueOf(getLatitude));
				// arr.add(String.valueOf(getLongitude));
				Network.CallBack(getLatitude + "," + getLongitude + ","
						+ getAddrStr);
				mLocationClient.stop();

			}

		}
	}

	/**
	 * 显示请求字符串
	 * 
	 * @param str
	 */
	public void logMsg(String str) {
		try {

			// if (getAddrStr != null) {
			// editor.putString("Latitude", String.valueOf(getLatitude));
			// editor.putString("Longitude", String.valueOf(getLongitude));
			// editor.putString("AddrStr", getAddrStr);
			// editor.commit();
			// mLocationClient.stop();
			// }

			// System.out.println("BaiduLocationApiDem" + str);
			// System.out.println("getLatitude = " + getLatitude);
			// System.out.println("getLongitude = " + getLongitude);
			//
			// System.out.println("getAddrStr = " + getAddrStr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
