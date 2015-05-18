package com.ky.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ky.utills.Configure.FunctionTagTable;
import com.lhl.callback.IHomeCallBackRquest;
import com.redbull.log.Logger;

/**
 * 
 * 访问首页的API，然后获得当前的列表
 * */
public class GetIndexRequest extends BaseRequest implements IHomeCallBackRquest {
	public String ReTAG = "GetIndexRequest";
	public int id;
	Context mContext;

	public GetIndexRequest(Context context, int id) {
		mContext = context;
		this.id = id;

	}

	@Override
	public String GetInfo() {
		API = FunctionTagTable.GETINDEX.getApi();
		VER = FunctionTagTable.GETINDEX.getVer();
		MODE = FunctionTagTable.GETINDEX.getMode();
		JSONObject dataJson = new JSONObject();
		try {
			dataJson.put("id", id);
		} catch (JSONException e) {
			Logger.e(ReTAG, e + "");
		}
		return getJsonString(dataJson, true);
	}

	@Override
	public FunctionTagTable GetNetTag() {
		// TODO Auto-generated method stub
		return FunctionTagTable.GETINDEX;
	}

}
