package com.ky.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ky.beaninfo.UpdataInfo;
import com.ky.utills.JsonTools;
import com.redbull.log.Logger;

/**
 * 获取所有的app版本
 * 
 * @author Catherine
 * 
 */
public class GetAllAppVersionResponse extends BaseResponse {

	public UpdataInfo dateInfo = new UpdataInfo();

	@Override
	public void paseRespones(String js) {

		try {
			JSONObject obj = new JSONObject(js);

			JSONObject items = obj.getJSONObject("items");

			JSONObject localAppsJson = items.getJSONObject("localApps");
			dateInfo.description = JsonTools.getString(localAppsJson,
					"description");
			dateInfo.version = JsonTools.getInt(localAppsJson, "version");
			dateInfo.md5 = JsonTools.getString(localAppsJson, "md5");
			dateInfo.url = JsonTools.getString(localAppsJson, "url");
			dateInfo.forceUpdateCode = JsonTools.getString(localAppsJson,
					"forceUpdateCode");

		} catch (Exception e) {
			Logger.log("GetAllAppVersionResponse:" + e.toString());
		}
	}

}
