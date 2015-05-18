package com.ky.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ky.beaninfo.ColumnGameInfo;
import com.ky.beaninfo.ColumnNewsInfo;
import com.ky.beaninfo.ColumnNovelInfo;
import com.ky.beaninfo.ColumnVedioInfo;
import com.ky.beaninfo.CustermInfo;
import com.redbull.log.Logger;

/*获取当前用户的点数*/
public class GetPointResponse extends BaseResponse {
	String TAG = "GetPointResponse";
	public String user_id;
	public int point;
	public String userName;

	@Override
	public void paseRespones(String js) {
		try {
			JSONObject json = new JSONObject(js);
			JSONObject item = json.getJSONObject("item");
//			user_id = item.getString("user_id");
			point = item.getInt("point");
//			userName = item.getString("username");
			Logger.d(TAG, "the point is==>" + point);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
