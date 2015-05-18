package com.ky.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ky.utills.Configure.FunctionTagTable;
import com.lhl.callback.IHomeCallBackRquest;
import com.redbull.log.Logger;

/**
 * 
 * 获得当前的点数
 * */
public class GetPointRequest extends BaseRequest implements IHomeCallBackRquest {
	public String ReTAG = "GetPointRequest";
	Context mContext;
	String user_id, content;
	String key;

	public GetPointRequest(Context context, String user_Id, String content,
			String key) {
		mContext = context;
		this.user_id = user_Id;
		this.content = content;
		this.key = key;

	}

	@Override
	public String GetInfo() {
		API = FunctionTagTable.GETSUBMITPOINT.getApi();
		VER = FunctionTagTable.GETSUBMITPOINT.getVer();
		MODE = FunctionTagTable.GETSUBMITPOINT.getMode();
		JSONObject dataJson = new JSONObject();
		try {
			dataJson.put("user_id", user_id);
			dataJson.put("content", content);
			dataJson.put("key", key);
		} catch (JSONException e) {
			Logger.e(TAG, e + "");
		}
		return getJsonString(dataJson,true);
	}

	@Override
	public FunctionTagTable GetNetTag() {
		// TODO Auto-generated method stub
		return FunctionTagTable.GETSUBMITPOINT;
	}
}
