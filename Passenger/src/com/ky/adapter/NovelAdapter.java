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
 * 填充小说的adapter
 * 
 * @author Catherine.Brain
 * */
public class NovelAdapter extends BaseAdapter {

	Context mContext;
	// ArrayList<Map<String, Object>> mList;
	ArrayList<LinkedHashMap<String, Object>> mList;

	public NovelAdapter(Context context,ArrayList<LinkedHashMap<String, Object>> list) {
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
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	HolderView holderView = null;

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		if (null == convertview) {
			convertview = LayoutInflater.from(mContext).inflate(
					R.layout.novel_main_cell, null);
			holderView = new HolderView();
			holderView.imageView = (ImageView) convertview
					.findViewById(R.id.novel_iamge);
			holderView.text_Title = (TextView) convertview
					.findViewById(R.id.text_title);

			// holderView.text_Content = (TextView) convertview
			// .findViewById(R.id.text_con);
			// holderView.text_Author = (TextView) convertview
			// .findViewById(R.id.text_author);

			convertview.setTag(holderView);
		}
		holderView = (HolderView) convertview.getTag();
		holderView.imageView.setBackgroundResource((Integer) mList
				.get(position).get("image"));
		//
		// holderView.text_Content.setText(mList.get(position).get("description")
		// .toString());
		holderView.text_Title.setText(mList.get(position).get("title")
				.toString());
		// holderView.text_Author.setText("作者："
		// + mList.get(position).get("author").toString());
		return convertview;
	}

	class HolderView {

		ImageView imageView;
		TextView text_Title;
	}

}
