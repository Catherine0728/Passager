package com.ky.response;

import org.json.JSONObject;

import com.ky.beaninfo.UpdataInfo;
import com.redbull.log.Logger;

/**
 * 去想服务认证当前外网的通道
 * 
 * 
 */
public class ServiceCertificationResponse extends BaseResponse {
	public static String TAG = "ServiceCertificationResponse";
	UpdataInfo dateInfo = new UpdataInfo();

	@Override
	public void paseRespones(String js) {

		try {
			JSONObject obj = new JSONObject(js);

//			JSONObject items = obj.getJSONObject("items");

			// JSONObject localAppsJson = items.getJSONObject("localApps");

		} catch (Exception e) {
			Logger.log("ServiceCertificationResponse:" + e.toString());
		}
	}

}
