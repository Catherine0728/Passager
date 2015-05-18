package com.ky.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.ky.utills.Configure.FunctionTagTable;
import com.lhl.callback.IHomeCallBackRquest;

import android.content.Context;

public class GetContentInfoRequest extends BaseRequest implements
		IHomeCallBackRquest {
	public String TAG = "GetContentInfoRequest";
	public String type;
	Context mContext;
	public String id;
	public int rows;
	int page;

	// 定义一个boolean去判断当前的请求是否是novel发出来的
	boolean isNovel = false;

	// {"DATA":{"id":76,"page":1,"rows":20,"type":"news"}}
	public GetContentInfoRequest(Context context, String id, String type,
			boolean isNovel) {
		mContext = context;
		this.type = type;
		this.id = id;
		// this.rows = row;
		this.isNovel = isNovel;
		// this.page = page;
	}

	@Override
	public String GetInfo() {
		if (isNovel) {
			API = FunctionTagTable.GETCONTENTNOVEL.getApi();
			VER = FunctionTagTable.GETCONTENTNOVEL.getVer();
			MODE = FunctionTagTable.GETCONTENTNOVEL.getMode();
		} else {
			API = FunctionTagTable.GETCONTENTINFO.getApi();
			VER = FunctionTagTable.GETCONTENTINFO.getVer();
			MODE = FunctionTagTable.GETCONTENTINFO.getMode();

		}

		JSONObject dataJson = new JSONObject();
		try {
			dataJson.put("id", id);
			// dataJson.put("page", page);
			// dataJson.put("rows", rows);
			dataJson.put("type", type);
			if (isNovel) {
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getJsonString(dataJson, true);
	}

	@Override
	public FunctionTagTable GetNetTag() {
		// TODO Auto-generated method stub
		if (isNovel) {
			return FunctionTagTable.GETCONTENTNOVEL;
		} else {
			return FunctionTagTable.GETCONTENTINFO;

		}

	}

}
