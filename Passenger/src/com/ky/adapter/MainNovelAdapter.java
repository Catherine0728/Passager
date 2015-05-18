package com.ky.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.ky.passenger.R;
import com.ky.utills.Configure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/***
 * 首页小说推荐的adapter
 * 
 * @author Catherine.Brain
 * 
 * */
public class MainNovelAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Map<String, Object>> mList;

	public MainNovelAdapter(Context context, ArrayList<Map<String, Object>> list) {
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
					R.layout.main_novel, null);
			new HolderView(convertview);
		}
		HolderView hodlerView = (HolderView) convertview.getTag();
		hodlerView.novel_Image.setImageResource((Integer) mList.get(position)
				.get("image"));
		hodlerView.text_View
				.setText(mList.get(position).get("name").toString());
		return convertview;
	}

	class HolderView {
		private ImageView novel_Image;
		private TextView text_View;

		public HolderView(View view) {
			novel_Image = (ImageView) view.findViewById(R.id.nevol_image);
			text_View = (TextView) view.findViewById(R.id.text_novel_title);
			view.setTag(this);

		}

	}

}
