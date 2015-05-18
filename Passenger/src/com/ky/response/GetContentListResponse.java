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

/**
 * 
 * 去获得每一个list
 * */
public class GetContentListResponse extends BaseResponse {
	String TAG = "GetContentListResponse";
	int COUNT;
	public ArrayList<CustermInfo> list = new ArrayList<CustermInfo>();
	public ArrayList<ColumnNewsInfo> newsList = new ArrayList<ColumnNewsInfo>();
	public ArrayList<ColumnVedioInfo> videoList = new ArrayList<ColumnVedioInfo>();




	@Override
	public void paseRespones(String js) {
		try {
			JSONObject json = new JSONObject(js);
			COUNT = json.getInt("count");
			JSONObject array = json.getJSONObject("items");
			// JSONArray array = json.getJSONArray("items");
			// for (int i = 0; i <array.length(); i++) {
			// JSONObject obj = (JSONObject) array.get(strType[i]);
			CustermInfo info = new CustermInfo();
			info.id = array.getInt("id");
			info.type = array.getString("type");
			info.count = array.getInt("count");
			info.url = array.getString("url");
			JSONArray dataArray = array.getJSONArray("data");

			for (int j = 0; j < dataArray.length(); j++) {
				JSONObject dataObj = (JSONObject) dataArray.get(j);
				// getData data = new getData();
				if (info.type.equals("video")) {
					ColumnVedioInfo vedio = new ColumnVedioInfo();
					vedio.id = dataObj.getString("id");
					vedio.title = dataObj.getString("title");
					vedio.category_id = dataObj.getString("category_id");
					vedio.descriptrion = dataObj.getString("description");
					vedio.cover_id = dataObj.getString("cover_id");
					vedio.thumb = dataObj.getString("thumb");
					vedio.url = dataObj.getString("url");
					vedio.actor = dataObj.getString("actor");
					vedio.director = dataObj.getString("director");
					vedio.years = dataObj.getString("years");
//					vedio.type = dataObj.getString("type");
					vedio.down_url = dataObj.getString("down_url");
					vedio.content = dataObj.getString("content");
					Logger.d(TAG, "the request vedio type is======>" + vedio.type);
					videoList.add(vedio);
				} else {
					ColumnNewsInfo news = new ColumnNewsInfo();
					news.id = dataObj.getString("id");
					news.title = dataObj.getString("title");
					news.category_id = dataObj.getString("category_id");
					news.descriptrion = dataObj.getString("description");
					news.cover_id = dataObj.getString("cover_id");
					news.thumb = dataObj.getString("thumb");
					news.url = dataObj.getString("url");
					news.type = dataObj.getString("type");
//					news.creat_time = dataObj.getString("create_time");
//					news.update_time = dataObj.getString("update_time");
//					news.status = dataObj.getString("status");
//					news.name = dataObj.getString("name");
					Logger.d(TAG, "the request news type is======>" + news.type);
					newsList.add(news);
				}
			}
			// info.data = obj.getString("data");
			Logger.d(TAG, "type is===>" + info.type + "====count is==》"
					+ info.count + "====data is==>" + dataArray
					+ "====url is==>" + info.url);
			list.add(info);
			Logger.d(TAG, "the count==>" + COUNT + "and the list size is==>"
					+ list.size());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
