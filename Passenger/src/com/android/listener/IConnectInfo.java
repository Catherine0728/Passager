package com.android.listener;

import com.meiya.hxj.wifi.WifiUtil.WifiCipherType;

public interface IConnectInfo {
	public void onConnectClick(String SSID, String pwd, WifiCipherType mType);
}
