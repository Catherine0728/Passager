package com.ky.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ky.beaninfo.ColumnNovelInfo;
import com.ky.beaninfo.ColumnNovelList;
import com.redbull.log.Logger;

/**
 * 
 * 去获得小说的详情页面数据
 * */
public class GetContentNovelInfoResponse extends BaseResponse {
	String TAG = "GetContentNovelInfoResponse";
	/** 这是获取小说详情页面的主要信息下面的data信息 */
	public ArrayList<ColumnNovelInfo> infoList = new ArrayList<ColumnNovelInfo>();
	public ColumnNovelInfo novelInfo;

	/** 这是获取小说详情页面的主要信息 */
	// ArrayList<ColumnNovelList> novelList = new ArrayList<ColumnNovelList>();
	public ColumnNovelList novel;

	public class novelItem {
		String page;
		String row;
		String count;
		String type;
		String data;

	}

	ArrayList<novelItem> novelItem = new ArrayList<novelItem>();

	@Override
	public void paseRespones(String js) {
		try {
			JSONObject json = new JSONObject(js);
			JSONObject itemObj = json.getJSONObject("item");
			novel = new ColumnNovelList();
			novel.id = itemObj.getString("id");
			novel.type = itemObj.getString("type");
			novel.count = itemObj.getString("count");
			novel.name = itemObj.getString("name");
			novel.title = itemObj.getString("title");
			novel.description = itemObj.getString("description");
			novel.create_time = itemObj.getString("create_time");
			novel.category_id = itemObj.getString("category_id");
			novel.url = itemObj.getString("url");
			JSONArray data = itemObj.getJSONArray("data");
			for (int j = 0; j < data.length(); j++) {
				JSONObject item_obj = (JSONObject) data.get(j);
				novelInfo = new ColumnNovelInfo();
				novelInfo.id = item_obj.getString("id");
				novelInfo.novel_id = item_obj.getString("novel_id");
				novelInfo.chapter = item_obj.getString("chapter");
				novelInfo.chapter_title = item_obj.getString("chapter_title");
				novelInfo.url = item_obj.getString("url");
				novelInfo.type = item_obj.getString("type");
				Logger.d(TAG, "id:" + novel.id + "---contents:"
						+ novelInfo.contents);
				infoList.add(novelInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
