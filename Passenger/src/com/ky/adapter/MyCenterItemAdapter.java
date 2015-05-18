package com.ky.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.ky.passenger.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * 这是一个填写个人中心条目的adapter
 * */
public class MyCenterItemAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<Map<String, Object>> mList;

	public MyCenterItemAdapter(Context context,
			ArrayList<Map<String, Object>> list) {
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
					R.layout.my_center_item, null);
			holderView = new HolderView();
			holderView.mTextView = (TextView) convertview
					.findViewById(R.id.btn_item);
			convertview.setTag(holderView);

		}
		holderView = (HolderView) convertview.getTag();
		holderView.mTextView.setText(mList.get(position).get("title")
				.toString());
		holderView.mTextView.setCompoundDrawablesWithIntrinsicBounds(
				(Integer) mList.get(position).get("image"), 0, R.drawable.icon_my_to, 0);
		return convertview;
	}

	class HolderView {
		TextView mTextView;

	}

}
