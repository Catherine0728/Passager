package com.ky.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
 * this is the list of the news
 * 
 * @author Catherine.Brain
 * 
 * */
public class NewsAdapter extends BaseAdapter {

	Context mContext = null;
	// ArrayList<Map<String, Object>> mlist = null;
	ArrayList<LinkedHashMap<String, Object>> mlist = null;


	public NewsAdapter(Context context,
			ArrayList<LinkedHashMap<String, Object>> list) {
		mContext = context;
		mlist = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	Holder holderView = null;

	@Override
	public View getView(int position, View convertview, ViewGroup parents) {

		if (null == convertview) {
			convertview = LayoutInflater.from(mContext).inflate(
					R.layout.news_main_cell, null);
			holderView = new Holder();
			holderView.image_Head = (ImageView) convertview
					.findViewById(R.id.image_head);
			holderView.text_Title = (TextView) convertview
					.findViewById(R.id.news_text_title);
			holderView.text_Con = (TextView) convertview
					.findViewById(R.id.news_text_con);
			convertview.setTag(holderView);

		}
		holderView = (Holder) convertview.getTag();
		holderView.image_Head.setBackgroundResource((Integer) mlist.get(
				position).get("image"));
		holderView.text_Title.setText(mlist.get(position).get("title")
				.toString());
		holderView.text_Con.setText(mlist.get(position).get("content")
				.toString());

		return convertview;
	}

	class Holder {
		TextView text_Title, text_Con;
		ImageView image_Head;

	}

}
