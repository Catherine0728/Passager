package com.ky.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ky.beaninfo.ColumnGameInfo;
import com.ky.beaninfo.ColumnNewsInfo;
import com.ky.beaninfo.ColumnNovelInfo;
import com.ky.beaninfo.ColumnVedioInfo;
import com.redbull.log.Logger;

/**
 * 
 * 去获得每一个list对应的info
 * */
public class GetContentInfoResponse extends BaseResponse {
	String TAG = "GetContentInfoResponse";
	// public ArrayList<ColumnNewsInfo> newsList = new
	// ArrayList<ColumnNewsInfo>();
	// public ArrayList<ColumnVedioInfo> videoList = new
	// ArrayList<ColumnVedioInfo>();
	public ColumnVedioInfo vedio;

	@Override
	public void paseRespones(String js) {
		try {
			JSONObject json = new JSONObject(js);
			JSONArray itemArray = json.getJSONArray("item");
			for (int i = 0; i < itemArray.length(); i++) {
				JSONObject array = (JSONObject) itemArray.get(i);
				vedio = new ColumnVedioInfo();
				vedio.id = array.getString("id");
				vedio.title = array.getString("title");
				vedio.category_id = array.getString("category_id");
				vedio.descriptrion = array.getString("description");
				// vedio.cover_id = array.getString("cover_id");
				// vedio.thumb = array.getString("thumb");
//				vedio.down_url = array.getString("down_url");
				vedio.actor = array.getString("actor");
				vedio.director = array.getString("director");
				vedio.years = array.getString("years");
				// vedio.type = array.getString("type");
				vedio.content = array.getString("content");
				vedio.name = array.getString("name");
				Logger.d(TAG, "id:" + vedio.id + "---title:" + vedio.title
						+ "---content:" + vedio.content+"---years"+vedio.years);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
