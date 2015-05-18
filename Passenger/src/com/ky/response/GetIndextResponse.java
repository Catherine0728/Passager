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

public class GetIndextResponse extends BaseResponse {
	String TAG = "GetIndextResponse";
	int COUNT;
	public ArrayList<CustermInfo> list = new ArrayList<CustermInfo>();
	public ArrayList<ColumnGameInfo> gameList = new ArrayList<ColumnGameInfo>();
	public ArrayList<ColumnNewsInfo> newsList = new ArrayList<ColumnNewsInfo>();
	public ArrayList<ColumnVedioInfo> vedioList = new ArrayList<ColumnVedioInfo>();
	public ArrayList<ColumnNovelInfo> novelList = new ArrayList<ColumnNovelInfo>();



	String[] strType = { "video", "news", "novel", "game" };

	@Override
	public void paseRespones(String js) {
		try {
			JSONObject json = new JSONObject(js);
			COUNT = json.getInt("count");
			JSONObject array = json.getJSONObject("items");
			// JSONArray array = json.getJSONArray("items");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = (JSONObject) array.get(strType[i]);
				CustermInfo info = new CustermInfo();
				info.id = obj.getInt("id");
				info.type = obj.getString("type");
				info.count = obj.getInt("count");
				info.url = obj.getString("url");
				JSONArray dataArray = obj.getJSONArray("data");
				if (info.type.equals("video")) {
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataObj = (JSONObject) dataArray.get(j);
						// getData data = new getData();

						// if (j == 0) {
						ColumnVedioInfo vedio = new ColumnVedioInfo();
						vedio.id = dataObj.getString("id");
						vedio.title = dataObj.getString("title");
						vedio.category_id = dataObj.getString("category_id");
						vedio.descriptrion = dataObj.getString("description");
						vedio.cover_id = dataObj.getString("cover_id");
						vedio.thumb = dataObj.getString("thumb");
						vedio.url = dataObj.getString("url");
						vedio.type = dataObj.getString("type");
						// vedio.actor = dataObj.getString("actor");
						// vedio.director = dataObj.getString("director");
						// vedio.years = dataObj.getString("years");
						// vedio.down_url = dataObj.getString("down_url");
						// vedio.content = dataObj.getString("content");
						vedioList.add(vedio);
					}
				} else if (info.type.equals("news")) {
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataObj = (JSONObject) dataArray.get(j);
						ColumnNewsInfo news = new ColumnNewsInfo();
						news.id = dataObj.getString("id");
						news.title = dataObj.getString("title");
						news.category_id = dataObj.getString("category_id");
						news.descriptrion = dataObj.getString("description");
						news.cover_id = dataObj.getString("cover_id");
						news.thumb = dataObj.getString("thumb");
						news.url = dataObj.getString("url");
						news.type = dataObj.getString("type");
						newsList.add(news);
					}
				} else if (info.type.equals("novel")) {

					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataObj = (JSONObject) dataArray.get(j);
						ColumnNovelInfo novel = new ColumnNovelInfo();
						novel.id = dataObj.getString("id");
						novel.title = dataObj.getString("title");
						novel.category_id = dataObj.getString("category_id");
						novel.descriptrion = dataObj.getString("description");
						novel.cover_id = dataObj.getString("cover_id");
						novel.thumb = dataObj.getString("thumb");
						novel.url = dataObj.getString("url");
						novel.type = dataObj.getString("type");
						novelList.add(novel);
					}
				} else if (info.type.equals("game")) {
					for (int j = 0; j < dataArray.length(); j++) {
						JSONObject dataObj = (JSONObject) dataArray.get(j);
						ColumnGameInfo game = new ColumnGameInfo();
						game.id = dataObj.getString("id");
						game.title = dataObj.getString("title");
						game.category_id = dataObj.getString("category_id");
						game.descriptrion = dataObj.getString("description");
						game.cover_id = dataObj.getString("cover_id");
						game.thumb = dataObj.getString("thumb");
						game.url = dataObj.getString("url");
						game.type = dataObj.getString("type");
						gameList.add(game);

					}
				}

				// for (int j = 0; j < dataArray.length(); j++) {
				// JSONObject dataObj = (JSONObject) dataArray.get(j);
				// // getData data = new getData();
				//
				// if (j == 0) {
				// ColumnVedioInfo vedio = new ColumnVedioInfo();
				// vedio.id = dataObj.getString("id");
				// vedio.title = dataObj.getString("title");
				// vedio.category_id = dataObj.getString("category_id");
				// vedio.descriptrion = dataObj.getString("description");
				// vedio.cover_id = dataObj.getString("cover_id");
				// vedio.thumb = dataObj.getString("thumb");
				// vedio.url = dataObj.getString("url");
				// vedio.type = dataObj.getString("type");
				// // vedio.actor = dataObj.getString("actor");
				// // vedio.director = dataObj.getString("director");
				// // vedio.years = dataObj.getString("years");
				// // vedio.down_url = dataObj.getString("down_url");
				// // vedio.content = dataObj.getString("content");
				// vedioList.add(vedio);
				// } else if (j == 1) {
				// ColumnNewsInfo news = new ColumnNewsInfo();
				// news.id = dataObj.getString("id");
				// news.title = dataObj.getString("title");
				// news.category_id = dataObj.getString("category_id");
				// news.descriptrion = dataObj.getString("description");
				// news.cover_id = dataObj.getString("cover_id");
				// news.thumb = dataObj.getString("thumb");
				// news.url = dataObj.getString("url");
				// news.type = dataObj.getString("type");
				// newsList.add(news);
				// } else if (j == 2) {
				// ColumnNovelInfo novel = new ColumnNovelInfo();
				// novel.id = dataObj.getString("id");
				// novel.title = dataObj.getString("title");
				// novel.category_id = dataObj.getString("category_id");
				// novel.descriptrion = dataObj.getString("description");
				// novel.cover_id = dataObj.getString("cover_id");
				// novel.thumb = dataObj.getString("thumb");
				// novel.url = dataObj.getString("url");
				// novel.type = dataObj.getString("type");
				// novelList.add(novel);
				// } else if (j == 3) {
				// ColumnGameInfo game = new ColumnGameInfo();
				// game.id = dataObj.getString("id");
				// game.title = dataObj.getString("title");
				// game.category_id = dataObj.getString("category_id");
				// game.descriptrion = dataObj.getString("description");
				// game.cover_id = dataObj.getString("cover_id");
				// game.thumb = dataObj.getString("thumb");
				// game.url = dataObj.getString("url");
				// game.type = dataObj.getString("type");
				// gameList.add(game);
				//
				// }
				// }
				// info.data = obj.getString("data");
				Logger.d(TAG, "type is===>" + info.type + "====count is==》"
						+ info.count + "====data is==>" + dataArray
						+ "====url is==>" + info.url);
				list.add(info);
			}
			Logger.d(TAG, "the count==>" + COUNT + "and the list size is==>"
					+ list.size());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
