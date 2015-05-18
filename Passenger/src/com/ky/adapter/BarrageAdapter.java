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
 * this is a adapter of the barrage
 * 
 * @author Catherine.Brain
 * */
public class BarrageAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Map<String, String>> mList;

	public BarrageAdapter(Context context, ArrayList<Map<String, String>> list) {

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
					R.layout.barrage_cell, null);
			holderView = new HolderView();
			holderView.text_Name = (TextView) convertview
					.findViewById(R.id.text_name);
			holderView.text_Time = (TextView) convertview
					.findViewById(R.id.text_time);
			holderView.text_Content = (TextView) convertview
					.findViewById(R.id.text_con);
			convertview.setTag(holderView);
		}
		holderView = (HolderView) convertview.getTag();
		holderView.text_Name
				.setText(mList.get(position).get("name").toString());
		holderView.text_Time
				.setText(mList.get(position).get("time").toString());
		holderView.text_Content.setText(mList.get(position).get("content")
				.toString());
		return convertview;
	}

	class HolderView {

		TextView text_Name, text_Content, text_Time;
	}

	public void reGetList(ArrayList<Map<String, String>> m_List) {
		mList = m_List;
		this.notifyDataSetChanged();

	}
}
