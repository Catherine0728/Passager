package com.ky.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.ky.adapter.NewsAdapter;
import com.ky.adapter.NovelAdapter;
import com.ky.beaninfo.ColumnNewsInfo;
import com.ky.beaninfo.ColumnNovelInfo;
import com.ky.beaninfo.ColumnTypeInfo;
import com.ky.passenger.R;
import com.ky.request.GetNewsRequest;
import com.ky.request.HttpHomeLoadDataTask;
import com.ky.response.GetContentListResponse;
import com.ky.utills.Configure;
import com.ky.utills.Utility;
import com.lhl.callback.IUpdateData;
import com.redbull.log.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * 小说
 * 
 * @author Catherine.Brain
 * */
public class NovelActivity extends Activity {
	String TAG = "NovelFragment";
	// View view = null;
	int name_Id;
	public static String FRAGMENTNAME = "小说";
	// ------------------
	static final int MENU_SET_MODE = 0;

	ArrayList<LinkedHashMap<String, Object>> mListItems, newListItems;
	private PullToRefreshGridView mPullRefreshGridView;
	GridView novle_grid;

	// --------------------
	Button btn_Return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novel_main);
		initView();
		if (getIntent() != null) {
			name_Id = getIntent().getIntExtra("id", 89);
		} else {
			name_Id = 89;
		}
		UpdateData(3);
	}

	NovelAdapter adapter = null;

	// ArrayList<Map<String, Object>> mList = null;

	public void initView() {
		Configure.PAGER = 6;
		// BaseActivity.btn_Return.setVisibility(View.VISIBLE);
		// BaseActivity.btn_logo.setVisibility(View.GONE);
		// BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);

		// mList = new ArrayList<Map<String, Object>>();
		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.main_novel_grid);
		btn_Return = (Button) findViewById(R.id.btn_return);
		btn_Return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		novle_grid = mPullRefreshGridView.getRefreshableView();

		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {

						new GetDataTask().execute();
						// UpdateData(6);

					}

				});

		mListItems = new ArrayList<LinkedHashMap<String, Object>>();
		newListItems = new ArrayList<LinkedHashMap<String, Object>>();

	}

	private class GetDataTask extends
			AsyncTask<Void, Void, ArrayList<LinkedHashMap<String, Object>>> {

		@Override
		protected ArrayList<LinkedHashMap<String, Object>> doInBackground(
				Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			return newListItems;
		}

		@Override
		protected void onPostExecute(
				ArrayList<LinkedHashMap<String, Object>> result) {
			newListItems = result;


			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshGridView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	static int r = 6;

	public void UpdateData(int row) {
		r += row;
		GetNewsRequest new_Re = new GetNewsRequest(this, name_Id, r, 1, "novel");
		new HttpHomeLoadDataTask(this, NovelCallBack, false, "", false)
				.execute(new_Re);

	}

	ArrayList<ColumnNovelInfo> novelList = new ArrayList<ColumnNovelInfo>();
	ArrayList<ColumnTypeInfo> typeList = new ArrayList<ColumnTypeInfo>();

	IUpdateData NovelCallBack = new IUpdateData() {
		ColumnTypeInfo type_Info;

		ColumnNovelInfo novelInfo;

		@Override
		public void updateUi(Object o) {
			GetContentListResponse news_Response = new GetContentListResponse();
			news_Response.paseRespones(o.toString());
			novelList.clear();
			for (int i = 0; i < news_Response.list.size(); i++) {
				type_Info = new ColumnTypeInfo();
				type_Info.id = news_Response.list.get(i).id;
				type_Info.count = news_Response.list.get(i).count;
				type_Info.type = news_Response.list.get(i).type;
				type_Info.url = news_Response.list.get(i).url;
				for (int j = 0; j < news_Response.newsList.size(); j++) {
					novelInfo = new ColumnNovelInfo();
					novelInfo.id = news_Response.newsList.get(j).id;
					novelInfo.title = news_Response.newsList.get(j).title;
					novelInfo.category_id = news_Response.newsList.get(j).category_id;
					novelInfo.descriptrion = news_Response.newsList.get(j).descriptrion;
					novelInfo.cover_id = news_Response.newsList.get(j).cover_id;
					novelInfo.thumb = news_Response.newsList.get(j).thumb;
					novelInfo.url = news_Response.newsList.get(j).url;
					novelInfo.type = news_Response.newsList.get(j).type;
					novelInfo.creat_time = news_Response.newsList.get(j).creat_time;
					novelInfo.update_time = news_Response.newsList.get(j).update_time;
					novelInfo.name = news_Response.newsList.get(j).name;
					novelInfo.status = news_Response.newsList.get(j).status;
					novelList.add(novelInfo);
					Logger.d(TAG, "the novel type is======>" + novelInfo.type);

				}
				typeList.add(type_Info);

			}
			myHandler.sendEmptyMessage(NEWSLIST);
		}

		@Override
		public void handleErrorData(String info) {
			Logger.e(TAG, info);

		}
	};
	public static final int NEWSLIST = 0x000001;
	Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NEWSLIST:
				LoadNovel();
				break;

			default:
				break;
			}

		};

	};

	public void LoadNovel() {
		mListItems.clear();
		newListItems.clear();
		for (int i = 0; i < novelList.size(); i++) {
			// Map<String, Object> map = new HashMap<String, Object>();
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("image", Configure.image_novel_icon[i % 9]);
			map.put("title", novelList.get(i).title);
			// map.put("description", novelList.get(i).descriptrion);

			mListItems.add(map);
			newListItems.add(map);
		}
		for (int i = 0; i < newListItems.size(); i++) {
			System.out.println("newListItems is===>" + i
					+ newListItems.get(i).get("title").toString()
					+ "and the r is==>" + r);
		}
		adapter = new NovelAdapter(this, mListItems);
		novle_grid.setAdapter(adapter);
		// Utility.setGridViewHeightBasedOnChildren(novle_grid, 3, 3, "novel");

		novle_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview,
					View convertview, int position, long id) {
				Intent intent = new Intent();
				intent.setClass(NovelActivity.this, NovelDetailActivity.class);
				intent.putExtra("id", novelList.get(position).id);
				intent.putExtra("type", typeList.get(0).type);
				intent.putExtra("pos", position);
				intent.putExtra("url", novelList.get(position).url);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
