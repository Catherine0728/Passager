package com.ky.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.ky.utills.Configure.FunctionTagTable;
import com.lhl.callback.IHomeCallBackRquest;

import android.content.Context;
/**
 * 
 * 点击章节去请求偶的每一个数据   contents
 * */
public class GetContentNovelInfoRequest extends BaseRequest implements
		IHomeCallBackRquest {
	public String TAG = "GetContentNovelInfoRequest";
	public String type;
	Context mContext;
	public String id;
	public String rows, chapter;
	String page;

	public GetContentNovelInfoRequest(Context context, String id, String type,
			String page, String rows, String chapter) {
		mContext = context;
		this.type = type;
		this.id = id;
		this.page = page;
		this.rows = rows;
		this.chapter = chapter;
	}

	@Override
	public String GetInfo() {
		API = FunctionTagTable.GETCONTENTNOVELINFO.getApi();
		VER = FunctionTagTable.GETCONTENTNOVELINFO.getVer();
		MODE = FunctionTagTable.GETCONTENTNOVELINFO.getMode();
		JSONObject dataJson = new JSONObject();
		try {
			dataJson.put("id", id);
			dataJson.put("page", page);
			dataJson.put("rows", rows);
			dataJson.put("type", type);
			dataJson.put("chapter", chapter);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getJsonString(dataJson,true);
	}

	@Override
	public FunctionTagTable GetNetTag() {
		// TODO Auto-generated method stub
		return FunctionTagTable.GETCONTENTNOVELINFO;
	}

}
