package com.ky.utills;

import android.content.Context;

/**
 * 
 * 得到自己当前所处的位置
 * 
 * *
 */
public class BaiDuGetLocation {
	Context mContext;
	private String getLatitude;
	private String getLongitude;
	private String getAddRess;

	public BaiDuGetLocation(Context context) {
		this.mContext = context;
	}

	String location;

	public String GetLocation() {

		// *定位
		BaiDuLocation baidu = new BaiDuLocation(mContext);
		baidu.InitLocation(new NetWorkInterface() {

			@Override
			public void callOvertime() {
				// TODO Auto-generated method stub

			}

			@Override
			public void CallBack(String result) {
				// TODO Auto-generated method stub
				String spStr[] = result.split(",");
				getLatitude = spStr[0];
				getLongitude = spStr[1];
				getAddRess = spStr[2];
				String[] str = getAddRess.split("市");
				Configure.location = str[0];
				System.out.println("getLatitude" + spStr[0]);
				System.out.println("getLongitude" + spStr[1]);
				System.out.println("getAddrStr" + Configure.location);
				location = Configure.location;

			}
		});
		baidu.mLocationClient.start();
		return location;
	}
}
