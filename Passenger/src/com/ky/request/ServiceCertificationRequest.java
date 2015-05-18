package com.ky.request;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.ky.utills.DeviceInfo;
import com.ky.utills.Configure;
import com.ky.utills.Configure.FunctionTagTable;
import com.lhl.callback.IHomeCallBackRquest;
import com.redbull.log.Logger;

/**
 * 
 * 去将用户信息发给服务器，然后获取认证
 * */
public class ServiceCertificationRequest implements IHomeCallBackRquest {
	public static String TAG = "ServiceCertificationRequest";
	String androidId, androidMac;

	public ServiceCertificationRequest(Context context, String mac, String id) {
		androidId = id;
		androidMac = mac;
	}

	@Override
	public String GetInfo() {
		JSONObject dataJson = new JSONObject();
		try {
			dataJson.put("mac", androidMac);
			dataJson.put("gw_address", androidId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Logger.d(TAG, "the request json is===>" + dataJson.toString());
		return dataJson.toString();
	}

	@Override
	public FunctionTagTable GetNetTag() {
		// TODO Auto-generated method stub
		return null;
	}

}
