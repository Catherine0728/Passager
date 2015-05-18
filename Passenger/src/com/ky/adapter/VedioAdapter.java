package com.ky.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ky.passenger.R;

import android.R.array;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 加载视频的adapter
 * 
 * @author Catherine.Brain
 * */
public class VedioAdapter extends BaseAdapter {

	Context mContext;
	// ArrayList<Map<String, Object>> mList;
	ArrayList<LinkedHashMap<String, Object>> mList;

	public VedioAdapter(Context context, ArrayList<LinkedHashMap<String, Object>> list) {

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
		return position;
	}

	HolderView holderView = null;

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		if (null == convertview) {
			convertview = LayoutInflater.from(mContext).inflate(
					R.layout.vedio_main_cell, null);
			holderView = new HolderView();
			holderView.iamge = (ImageView) convertview
					.findViewById(R.id.vedio_image);
			holderView.text_Title = (TextView) convertview
					.findViewById(R.id.text_title);
			convertview.setTag(holderView);
		}
		holderView = (HolderView) convertview.getTag();
		holderView.iamge.setBackgroundResource((Integer) mList.get(position)
				.get("image"));
		holderView.text_Title.setText(mList.get(position).get("title")
				.toString());
		return convertview;
	}

	class HolderView {

		ImageView iamge;
		TextView text_Title;
	}
}
