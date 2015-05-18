package com.ky.mainactivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import com.android.viewflow.ImageAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.ky.adapter.NewsAdapter;
import com.ky.beaninfo.ColumnNewsInfo;
import com.ky.beaninfo.ColumnTypeInfo;
import com.ky.passenger.R;
import com.ky.request.GetNewsRequest;
import com.ky.request.HttpHomeLoadDataTask;
import com.ky.response.GetContentListResponse;
import com.ky.utills.Configure;
import com.ky.utills.Utility;
import com.lhl.callback.IUpdateData;
import com.lidroid.xutils.sample.download.DownloadService;
import com.redbull.log.Logger;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 
 * main of news 新闻列表，这里采用了将scrollview下拉以及上拉刷新的节奏
 * 
 * @author Catherine.Brain
 * */
public class NewsActivity extends Activity {
	public static String TAG = "NewsFragment";
	Context mContext;
	public static TextView text_Main;
	public static String FRAGMENTNAME = "新闻";
	// -----------------------

	static final int MENU_MANUAL_REFRESH = 0;
	static final int MENU_DISABLE_SCROLL = 1;
	static final int MENU_SET_MODE = 2;
	static final int MENU_DEMO = 3;

	ArrayList<LinkedHashMap<String, Object>> mListItems, newListItems;
	// private PullToRefreshListView mPullRefreshListView;
	ListView actualListView;
	Button btn_Return;
	// -------------------------------

	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;

	NewsAdapter adapter = null;
	ViewFlow viewFlow;
	int[] imageArray = { R.drawable.image_new_top_one,
			R.drawable.image_new_top_two, R.drawable.image_news_top_three };
	String[] strName = { "每一步只为你", "敢死队", "女特警，打手无虚发" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_main);
		initView();
		UpdateData(5);
	}

	public void initView() {
		Configure.PAGER = 6;

		// --------------实例化scrollview

		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						new GetDataTask().execute();
						// UpdateData(5);

					}
				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
		initFlowView();
		actualListView = (ListView) findViewById(R.id.list_new);

		mListItems = new ArrayList<LinkedHashMap<String, Object>>();
		newListItems = new ArrayList<LinkedHashMap<String, Object>>();
		btn_Return = (Button) findViewById(R.id.btn_return);
		btn_Return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	/**
	 * 
	 * 对scrollview进行刷新处理
	 * */
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// Do some stuff here

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshScrollView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	public void initFlowView() {

		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		text_Main = (TextView) findViewById(R.id.text_View);
		viewFlow.setAdapter(new ImageAdapter(this, imageArray, "news"));
		viewFlow.setmSideBuffer(imageArray.length); // 实际图片张数

		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
		viewFlow.setTimeSpan(3000);
		viewFlow.setSelection(imageArray.length * 1000); // 设置初始位置
		viewFlow.startAutoFlowTimer();
	}

	static int r = 5;

	public void UpdateData(int row) {
		r += row;
		GetNewsRequest new_Re = new GetNewsRequest(this, 76, r, 1, "news");
		new HttpHomeLoadDataTask(this, NewCallBack, true, "", false)
				.execute(new_Re);

	}

	ArrayList<ColumnNewsInfo> newsList = new ArrayList<ColumnNewsInfo>();
	ArrayList<ColumnTypeInfo> typeList = new ArrayList<ColumnTypeInfo>();

	IUpdateData NewCallBack = new IUpdateData() {
		ColumnTypeInfo type_Info;

		ColumnNewsInfo newInfo;

		@Override
		public void updateUi(Object o) {
			GetContentListResponse news_Response = new GetContentListResponse();
			news_Response.paseRespones(o.toString());
			newsList.clear();
			for (int i = 0; i < news_Response.list.size(); i++) {
				type_Info = new ColumnTypeInfo();
				type_Info.id = news_Response.list.get(i).id;
				type_Info.count = news_Response.list.get(i).count;
				type_Info.type = news_Response.list.get(i).type;
				type_Info.url = news_Response.list.get(i).url;
				// if (type_Info.type.equals("vedio")) {
				for (int j = 0; j < news_Response.newsList.size(); j++) {
					newInfo = new ColumnNewsInfo();
					newInfo.id = news_Response.newsList.get(j).id;
					newInfo.title = news_Response.newsList.get(j).title;
					newInfo.category_id = news_Response.newsList.get(j).category_id;
					newInfo.descriptrion = news_Response.newsList.get(j).descriptrion;
					newInfo.cover_id = news_Response.newsList.get(j).cover_id;
					newInfo.thumb = news_Response.newsList.get(j).thumb;
					newInfo.url = news_Response.newsList.get(j).url;
					newInfo.type = news_Response.newsList.get(j).type;
					newsList.add(newInfo);

					Logger.d(TAG, "the news type is======>" + newInfo.type);

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
				LoadNews();
				break;

			default:
				break;
			}

		};

	};

	public void LoadNews() {
		mListItems.clear();
		newListItems.clear();
		for (int i = 0; i < newsList.size(); i++) {
			// Map<String, Object> map = new HashMap<String, Object>();
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("title", newsList.get(i).title);
			map.put("image",
					Configure.imageNews[i % Configure.imageNews.length]);
			map.put("content", newsList.get(i).descriptrion);

			mListItems.add(map);
			newListItems.add(map);
		}

		adapter = new NewsAdapter(this, mListItems);
		actualListView.setAdapter(adapter);
		Utility.setListViewHeightBasedOnChildren(actualListView);

		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview,
					View convertview, int position, long id) {
				Intent intent = new Intent();
				intent.setClass(mContext, NewDetailActivity.class);
				intent.putExtra("title", newsList.get(position).title);
				intent.putExtra("id", newsList.get(position).id);
				intent.putExtra("type", typeList.get(0).type);
				intent.putExtra("url", newsList.get(position).url);
				intent.putExtra("thumb", newsList.get(position).thumb);
				Logger.d(TAG,
						"id:" + newsList.get(position).id + "--type:"
								+ newsList.get(position).type + "--url:"
								+ newsList.get(position).url);
				startActivity(intent);

			}
		});
	}

	@Override
	public void onStop() {
		Logger.d(TAG, "onStop");
		super.onStop();
	}

	@Override
	public void onResume() {
		Logger.d(TAG, "onResume");
		super.onResume();
	}

}
