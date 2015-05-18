package com.ky.operaview;

import java.util.ArrayList;
import java.util.Map;

import sf.hmg.turntest.turntest;

import com.ky.adapter.SeriseAdapter;
import com.ky.beaninfo.ColumnNovelInfo;
import com.ky.passenger.R;
import com.ky.request.GetContentNovelInfoRequest;
import com.ky.request.HttpHomeLoadDataTask;
import com.ky.response.GetContentNovelInfoResponse;
import com.ky.response.GetNovelChapterInfoResponse;
import com.ky.utills.Utility;
import com.lhl.callback.IUpdateData;
import com.redbull.log.Logger;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 
 章节列表 *
 * 
 * @author Catherine.Brain 2014/9/25-10-08
 * */
public class SeriseOperaView extends LinearLayout {
	String TAG = "SeriseOperaView";
	private Context mContext;
	SeriseOperaView instanse;
	View view;
	// private ArrayList<Map<String, Object>> mList;
	// ArrayList<ColumnNovelInfo> mList;
	SeriseAdapter adapter;
	private ListView mListView;

	public SeriseOperaView(Context context) {
		super(context);
		mContext = context;
	}

	// RelativeLayout layout_Serise;

	public View initView() {
		view = LayoutInflater.from(mContext).inflate(R.layout.serise, null);
		// mList = list;
		mListView = (ListView) view.findViewById(R.id.list_serise);
		// layout_Serise = (RelativeLayout)
		// view.findViewById(R.id.layout_serise);
		return view;

	}

	public void operaView(final ArrayList<ColumnNovelInfo> list,
			final String id, final String type, final String url,
			final String contents) {
		if (list.size() == 0) {
			// layout_Serise.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		} else {
			for (int i = 0; i < list.size(); i++) {

				Logger.d(TAG, "the list is===" + list.get(i).chapter_title);
			}

			// layout_Serise.setVisibility(View.GONE);
			adapter = new SeriseAdapter(mContext, list);
			mListView.setSelector(color.transparent);
			// adapter.reGet(list);
			mListView.setAdapter(adapter);
			// Utility.setListViewHeightBasedOnChildren(mListView);
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> ada, View v,
						int position, long arg3id) {
					GetContents(id, type, url, list.get(position).chapter);
				}
			});
		}
	}

	public void GetContents(String id, String type, String url, String cha) {
		GetContentNovelInfoRequest novel_info = new GetContentNovelInfoRequest(
				mContext, id, type, "1", "2", cha);
		new HttpHomeLoadDataTask(mContext, GetNovelInfoCallBack, false, "",
				false).execute(novel_info);
	}

	ColumnNovelInfo getNovelInfo;
	ArrayList<ColumnNovelInfo> GetnovelInfoList = new ArrayList<ColumnNovelInfo>();
	IUpdateData GetNovelInfoCallBack = new IUpdateData() {

		@Override
		public void updateUi(Object o) {
			GetNovelChapterInfoResponse novelRes = new GetNovelChapterInfoResponse();
			novelRes.paseRespones(o.toString());

			for (int i = 0; i < novelRes.infoList.size(); i++) {
				getNovelInfo = new ColumnNovelInfo();
				// novelInfo.title = novelRes.novel.title;
				getNovelInfo.novel_id = novelRes.infoList.get(i).novel_id;
				// novelInfo.name = novelRes.novel.name;
				getNovelInfo.chapter = novelRes.infoList.get(i).chapter;
				getNovelInfo.chapter_title = novelRes.infoList.get(i).chapter_title;
				getNovelInfo.contents = novelRes.infoList.get(i).contents;
				getNovelInfo.id = novelRes.infoList.get(i).id;
				GetnovelInfoList.add(getNovelInfo);
			}

			Logger.d(TAG, "the contents is===>" + getNovelInfo.contents);
			myHandler.sendEmptyMessageAtTime(SUCCESS, 2000);

		}

		@Override
		public void handleErrorData(String info) {
			Logger.e(TAG, info);

		}
	};
	public static final int SUCCESS = 0x000001;
	public static final int FAILED = 0x000002;
	Handler myHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				Intent intent = new Intent();
				intent.setClass(mContext, turntest.class);
				// intent.putExtra("id", getNovelInfo.id);
				// intent.putExtra("type", getNovelInfo.type);
				// intent.putExtra("url", getNovelInfo.url);
				// intent.putExtra("chapter", list.get(position).chapter);
				intent.putExtra("contents", getNovelInfo.contents);
				mContext.startActivity(intent);
				Logger.d(TAG, "the contents is==>" + getNovelInfo.contents);
				break;

			default:
				break;
			}
		};
	};
}
