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
 * 这是游戏左边的目录
 * 
 * @author Catherine.Brain
 * */
public class GameLeftAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<Map<String, Object>> mList;

	public GameLeftAdapter(Context context, ArrayList<Map<String, Object>> list) {
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
					R.layout.main_game_left, null);
			new HolderView(convertview);

		}
		HolderView holderView = (HolderView) convertview.getTag();
		holderView.game_Iamge.setImageResource((Integer) mList.get(position)
				.get("image"));
		holderView.text_Name
				.setText(mList.get(position).get("name").toString());
		return convertview;
	}

	class HolderView {
		ImageView game_Iamge;
		TextView text_Name;

		public HolderView(View view) {
			game_Iamge = (ImageView) view.findViewById(R.id.game_image_one);
			text_Name = (TextView) view.findViewById(R.id.game_text_one);
			view.setTag(this);
		}

	}

}
