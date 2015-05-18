package com.ky.request;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.ky.utills.DeviceInfo;
import com.ky.utills.Configure;
import com.ky.utills.Configure.FunctionTagTable;
import com.lhl.callback.IHomeCallBackRquest;

public class GetAllAppVersionRequest extends BaseRequest implements
		IHomeCallBackRquest {
	String androidId;
	int version;

	public GetAllAppVersionRequest(Context context, int version) {
		this.version = version;
	}

	@Override
	public FunctionTagTable GetNetTag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String GetInfo() {
		JSONObject dataJson = new JSONObject();
		try {
			dataJson.put("version", version);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getJsonString(dataJson, true);
	}
}
