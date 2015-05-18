package com.ky.mainactivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import com.android.viewflow.ImageAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.ky.adapter.VedioAdapter;
import com.ky.beaninfo.ColumnTypeInfo;
import com.ky.beaninfo.ColumnVedioInfo;
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
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * 
 * 这是视频列表
 * 
 * @author Catherine.Brain
 * */
public class VedioActivity extends Activity {
	String TAG = "VedioActivity";
	public static String FRAGMENTNAME = "视频";

	static final int MENU_SET_MODE = 0;

	ArrayList<LinkedHashMap<String, Object>> mListItems, newListItems;
	// private PullToRefreshGridView mPullRefreshGridView;
	GridView vedio_Grid;
	// -----------------------对Scrollview进行声明
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;

	VedioAdapter adapter = null;
	ViewFlow viewFlow;
	Button btn_Return;
	int[] imageArray = { R.drawable.image_new_top_one,
			R.drawable.image_new_top_two, R.drawable.image_news_top_three };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vedio_main);
		initView();
		UpdateData(3);
	}

	public void initView() {
		Configure.PAGER = 6;
		// BaseActivity.btn_Return.setVisibility(View.VISIBLE);
		// BaseActivity.btn_logo.setVisibility(View.GONE);
		// BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		btn_Return = (Button) findViewById(R.id.btn_return);
		btn_Return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		initFlowView();
		initAdapter();

		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {

						new GetDataTask().execute();
						// UpdateData(6);

					}
				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();

	}

	public void initFlowView() {

		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		viewFlow.setAdapter(new ImageAdapter(this, imageArray, "vedio"));
		viewFlow.setmSideBuffer(imageArray.length); // 实际图片张数

		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
		viewFlow.setTimeSpan(3000);
		viewFlow.setSelection(imageArray.length * 1000); // 设置初始位置
		viewFlow.startAutoFlowTimer();
	}

	public void initAdapter() {

		vedio_Grid = (GridView) findViewById(R.id.vedio_grid);

		mListItems = new ArrayList<LinkedHashMap<String, Object>>();
		newListItems = new ArrayList<LinkedHashMap<String, Object>>();
	}

	static int r = 6;

	public void UpdateData(int row) {
		r += row;
		GetNewsRequest new_Re = new GetNewsRequest(this, 1, r, 1, "video");
		new HttpHomeLoadDataTask(this, vedioCallBack, true, "", false)
				.execute(new_Re);

	}

	ArrayList<ColumnVedioInfo> vedioList = new ArrayList<ColumnVedioInfo>();
	ArrayList<ColumnTypeInfo> typeList = new ArrayList<ColumnTypeInfo>();

	IUpdateData vedioCallBack = new IUpdateData() {
		ColumnTypeInfo type_Info;

		ColumnVedioInfo vedioInfo;

		@Override
		public void updateUi(Object o) {
			GetContentListResponse news_Response = new GetContentListResponse();
			news_Response.paseRespones(o.toString());
			vedioList.clear();
			for (int i = 0; i < news_Response.list.size(); i++) {
				type_Info = new ColumnTypeInfo();
				type_Info.id = news_Response.list.get(i).id;
				type_Info.count = news_Response.list.get(i).count;
				type_Info.type = news_Response.list.get(i).type;
				type_Info.url = news_Response.list.get(i).url;
				// if (type_Info.type.equals("vedio")) {
				for (int j = 0; j < news_Response.videoList.size(); j++) {
					vedioInfo = new ColumnVedioInfo();
					vedioInfo.id = news_Response.videoList.get(j).id;
					vedioInfo.title = news_Response.videoList.get(j).title;
					vedioInfo.category_id = news_Response.videoList.get(j).category_id;
					vedioInfo.descriptrion = news_Response.videoList.get(j).descriptrion;
					vedioInfo.cover_id = news_Response.videoList.get(j).cover_id;
					vedioInfo.thumb = news_Response.videoList.get(j).thumb;
					vedioInfo.url = news_Response.videoList.get(j).url;
					vedioInfo.name = news_Response.videoList.get(j).name;
					vedioInfo.actor = news_Response.videoList.get(j).actor;
					vedioInfo.director = news_Response.videoList.get(j).director;
					vedioInfo.years = news_Response.videoList.get(j).years;
					vedioInfo.down_url = news_Response.videoList.get(j).down_url;
					vedioInfo.content = news_Response.videoList.get(j).content;
					vedioInfo.type = news_Response.videoList.get(j).type;
					vedioList.add(vedioInfo);
					Logger.d(TAG, "id:" + vedioInfo.id + "--type:"
							+ vedioInfo.type + "--url:" + vedioInfo.url);
					// }

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
		for (int i = 0; i < vedioList.size(); i++) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			// LinkedHashMapMap<String, Object> map = new HashMap<String,
			// Object>();
			map.put("image", Configure.image_video_Icon[i % 11]);
			map.put("title", vedioList.get(i).title);
			// map.put("description", novelList.get(i).descriptrion);

			mListItems.add(map);
			newListItems.add(map);
		}

		adapter = new VedioAdapter(this, mListItems);
		vedio_Grid.setAdapter(adapter);
		Utility.setGridViewHeightBasedOnChildren(vedio_Grid, 3, 3, "vedio");

		vedio_Grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview,
					View convertview, int position, long id) {
				Intent intent = new Intent();
				intent.setClass(VedioActivity.this, VedioDetailActivity.class);
				intent.putExtra("title", vedioList.get(position).title);
				intent.putExtra("actor", vedioList.get(position).actor);
				intent.putExtra("director", vedioList.get(position).director);
				intent.putExtra("years", vedioList.get(position).years);
				intent.putExtra("down_url", vedioList.get(position).down_url);
				intent.putExtra("description",
						vedioList.get(position).descriptrion);
				intent.putExtra("pos", position);
				intent.putExtra("id", vedioList.get(position).id);
				intent.putExtra("url", vedioList.get(position).url);
				intent.putExtra("type", typeList.get(0).type);
				intent.putExtra("thumb", vedioList.get(position).thumb);
				startActivity(intent);
				// Fragment newContent = new VedioDetailActivity();
				// BaseActivity base = (BaseActivity) this;
				// Bundle bundle = new Bundle();
				// bundle.putString("title", vedioList.get(position).title);
				// bundle.putString("actor", vedioList.get(position).actor);
				// bundle.putString("director",
				// vedioList.get(position).director);
				// bundle.putString("years", vedioList.get(position).years);
				// bundle.putString("down_url",
				// vedioList.get(position).down_url);
				// bundle.putString("description",
				// vedioList.get(position).descriptrion);
				// bundle.putInt("pos", position);
				// bundle.putString("id", vedioList.get(position).id);
				// bundle.putString("url", vedioList.get(position).url);
				// bundle.putString("type", typeList.get(0).type);
				// bundle.putString("thumb", vedioList.get(position).thumb);
				// newContent.setArguments(bundle);
				// base.switchContent(newContent, "视频", true);

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

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
