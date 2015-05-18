package com.ky.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.ky.passenger.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 这是首页新闻的listview
 * 
 * @author Catherine.Brain
 * */
public class MainNewsListAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<Map<String, String>> mList;

	public MainNewsListAdapter(Context context,
			ArrayList<Map<String, String>> list) {
		mContext = context;
		mList = list;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		if (null == convertview) {
			convertview = LayoutInflater.from(mContext).inflate(
					R.layout.main_news_list, null);
			new HolderView(convertview);

		}
		HolderView holderView = (HolderView) convertview.getTag();
		holderView.news_one.setText(mList.get(position).get("news_left")
				.toString());
		return convertview;
	}

	class HolderView {
		TextView news_one, news_two;

		public HolderView(View view) {
			news_one = (TextView) view.findViewById(R.id.news_one);
			view.setTag(this);
		}

	}

}
