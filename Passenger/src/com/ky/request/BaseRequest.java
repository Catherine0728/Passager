package com.ky.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.util.Base64;

import com.ky.utills.Configure;
import com.redbull.log.Logger;

/**
 * 
 * 所有请求的基类
 * */
public class BaseRequest {
	public static String TAG = "BaseRequest";
	public static String API;
	public static String VER;
	public static String MODE;
	public static final String DATA = "DATA";
	/* 是否传递前奏过去，默认为true，就是要传递 */
	public static boolean isSendAp = true;

	public String getJsonString(JSONObject dataJson, Boolean isSend) {

		JSONObject json = new JSONObject();
		isSendAp = isSend;

		Logger.log("API:" + API);
		Logger.log("VER:" + VER);
		Logger.log("MODE:" + MODE);
		try {
			if (isSendAp) {
				json.put("API", API);
				json.put("VER", VER);
				json.put("MODE", MODE);
			} else {

			}

			if (Configure.RAW.equals(MODE)) {
				json.put(DATA, dataJson);
			} else if (Configure.B64.equals(MODE)) {
				String base64Str = Base64.encodeToString(dataJson.toString()
						.getBytes(), 1);
				Logger.log(base64Str);
				json.put(DATA, base64Str);
			}

			// json.put(DATA, dataJson);
		} catch (JSONException e) {
			Logger.e(TAG, "getJsonString error" + e.getLocalizedMessage());
		}

		Logger.d(TAG, "the request json is===>" + json.toString());
		return json.toString();
	}

}
