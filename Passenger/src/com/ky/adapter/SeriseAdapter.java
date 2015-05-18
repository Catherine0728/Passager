package com.ky.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.ky.beaninfo.ColumnNovelInfo;
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
 * 章节列表
 * 
 * @author Catherine.Brain
 * */
public class SeriseAdapter extends BaseAdapter {
	private Context mContxt;
	// private ArrayList<Map<String, Object>> mList;
	private ArrayList<ColumnNovelInfo> mList;
	LayoutInflater inflater;

	public SeriseAdapter(Context context, ArrayList<ColumnNovelInfo> list) {
		mContxt = context;
		mList = list;
		inflater = LayoutInflater.from(mContxt);
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

	public void reGet(ArrayList<ColumnNovelInfo> list) {

		mList = list;
	}

	HolderView holderView;

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		if (null == convertview) {
			convertview = inflater.inflate(R.layout.serise_cell, null);
			holderView = new HolderView();
			// holderView.image_Img = (ImageView) convertview
			// .findViewById(R.id.image_img);

			holderView.text_Name = (TextView) convertview
					.findViewById(R.id.text_name);
			holderView.text_Serise = (TextView) convertview
					.findViewById(R.id.text_serise);
			convertview.setTag(holderView);

		}
		holderView = (HolderView) convertview.getTag();
		holderView.text_Name.setText(mList.get(position).chapter_title);
		holderView.text_Serise.setText(mList.get(position).chapter.toString());
		// holderView.image_Img.setBackgroundResource((Integer) mList
		// .get(position).get("image"));
		return convertview;
	}

	class HolderView {
		// ImageView image_Img;
		TextView text_Name, text_Serise;
	}

}
