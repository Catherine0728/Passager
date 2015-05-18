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
public class GetNewsContentInfoResponse extends BaseResponse {
	String TAG = "GetNewsContentInfoResponse";
	// public ArrayList<ColumnNewsInfo> newsList = new
	// ArrayList<ColumnNewsInfo>();
	// public ArrayList<ColumnVedioInfo> videoList = new
	// ArrayList<ColumnVedioInfo>();
	public ColumnNewsInfo news;

	@Override
	public void paseRespones(String js) {
		try {
			JSONObject json = new JSONObject(js);
			JSONArray itemArray = json.getJSONArray("item");
			for (int j = 0; j < itemArray.length(); j++) {
				JSONObject item_obj =(JSONObject) itemArray.get(j);
				news = new ColumnNewsInfo();
				news.id = item_obj.getString("id");
				news.title = item_obj.getString("title");
				news.category_id = item_obj.getString("category_id");
				news.descriptrion = item_obj.getString("description");
				news.creat_time = item_obj.getString("create_time");
//				news.thumb = item_obj.getString("thumb");
//				news.url = item_obj.getString("url");
//				news.type = item_obj.getString("type");
				news.content = item_obj.getString("content");
				news.name = item_obj.getString("name");
				Logger.d(TAG, "id:"+news.id+"---title:"+news.title+"---content:"+news.content);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
