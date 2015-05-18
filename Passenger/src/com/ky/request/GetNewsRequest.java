package com.ky.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.ky.utills.Configure.FunctionTagTable;
import com.lhl.callback.IHomeCallBackRquest;

import android.content.Context;

public class GetNewsRequest extends BaseRequest implements IHomeCallBackRquest {
	public String TAG = "GetNewsRequest";
	public String type;
	Context mContext;
	public int id;
	public int rows;
	int page;
//	{"DATA":{"id":76,"page":1,"rows":20,"type":"news"}}
	public GetNewsRequest(Context context, int id, int row, int page,
			String type) {
		mContext = context;
		this.type = type;
		this.id = id;
		this.rows = row;
		this.page = page;
	}

	@Override
	public String GetInfo() {
		API = FunctionTagTable.GETCONTENTLIST.getApi();
		VER = FunctionTagTable.GETCONTENTLIST.getVer();
		MODE = FunctionTagTable.GETCONTENTLIST.getMode();
		JSONObject dataJson = new JSONObject();
		try {
			dataJson.put("id", id);
			dataJson.put("page", page);
			dataJson.put("rows", rows);
			dataJson.put("type", type);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getJsonString(dataJson,true);
	}

	@Override
	public FunctionTagTable GetNetTag() {
		// TODO Auto-generated method stub
		return FunctionTagTable.GETCONTENTLIST;
	}

}
