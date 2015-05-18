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
 * this is a adapter of Remind me
 * 
 * @author Catherine.Brain
 * */
public class RemindMeAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Map<String, String>> mList;

	public RemindMeAdapter(Context context, ArrayList<Map<String, String>> list) {

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
					R.layout.remind_cell, null);
			holderView = new HolderView();
			holderView.text_Content = (TextView) convertview
					.findViewById(R.id.text_con);
			holderView.text_Time = (TextView) convertview
					.findViewById(R.id.text_time);
			convertview.setTag(holderView);
		}
		holderView = (HolderView) convertview.getTag();
		holderView.text_Content.setText(mList.get(position).get("con")
				.toString());
		holderView.text_Time
				.setText(mList.get(position).get("time").toString());
		return convertview;
	}

	class HolderView {

		TextView text_Content, text_Time;
	}

	public void ToNotify() {

		this.notifyDataSetChanged();
	}

	public void reGet(ArrayList<Map<String, String>> list) {

		mList = list;
		this.notifyDataSetChanged();
	}
}
