package com.ky.adapter;

import java.util.ArrayList;

import com.ky.passenger.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<String> mList;

	public CityAdapter(Context context, ArrayList<String> list) {
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

	HolderView holderView;

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (null == convertview) {
			convertview = LayoutInflater.from(mContext).inflate(
					R.layout.city_list, null);
			holderView = new HolderView();
			holderView.mTextView = (TextView) convertview
					.findViewById(R.id.text_city_name);
			convertview.setTag(holderView);

		}
		holderView = (HolderView) convertview.getTag();
		holderView.mTextView.setText(mList.get(position).toString());
		return convertview;
	}

	class HolderView {
		TextView mTextView;

	}

}
