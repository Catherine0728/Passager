package com.ky.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.ky.db.VideoDowningDB;
import com.ky.mainactivity.DownloadFragment;
import com.ky.passenger.R;
import com.ky.utills.Configure;

import android.content.Context;
import android.database.Cursor;
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
import android.widget.Toast;

/***
 * 
 * this is list about download
 * 
 * 这是正在下载以及已经下载的显示列表的适配器
 * 
 * @author Catherine.Brain
 * 
 * */
public class DownloadListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Map<String, String>> mList;
	private LayoutInflater inflater;
	DownloadListAdapter instance;

	public DownloadListAdapter(Context context,
			ArrayList<Map<String, String>> list) {
		mContext = context;
		mList = list;
		isChice = new boolean[mList.size()];
		for (int i = 0; i < mList.size(); i++) {
			isChice[i] = false;
		}
		inflater = LayoutInflater.from(mContext);
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

	public void reGet(ArrayList<Map<String, String>> list) {
		mList = list;
		notifyDataSetChanged();
	}

	public HolderView holderView;

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		if (null == convertview) {
			convertview = inflater.inflate(R.layout.offline_list_cell, null);
			holderView = new HolderView();
			// holderView.image_Img = (ImageView) convertview
			// .findViewById(R.id.image_img);
			holderView.text_Name = (TextView) convertview
					.findViewById(R.id.text_name);
			holderView.layout_toselect = (LinearLayout) convertview
					.findViewById(R.id.layout_myfav_toselect);
			holderView.layout_line = (LinearLayout) convertview
					.findViewById(R.id.layout_line);

			holderView.image_myfav_toselect = (ImageView) convertview
					.findViewById(R.id.image_mygav_toselect);
			holderView.text_Serise = (TextView) convertview
					.findViewById(R.id.text_serise);
			holderView.processBar = (ProgressBar) convertview
					.findViewById(R.id.download_progress);
			holderView.text_Speed = (TextView) convertview
					.findViewById(R.id.text_size);
			holderView.image_myfav_toselect.setTag(position);
			convertview.setTag(holderView);
		}
		holderView = (HolderView) convertview.getTag();
		if (LR == 0) {
			holderView.layout_toselect.setVisibility(View.GONE);
		} else {

			holderView.layout_toselect.setVisibility(View.VISIBLE);
		}
		// holderView.image_Img.setBackgroundResource(R.drawable.image_vedio_eight);
		holderView.text_Name
				.setText(mList.get(position).get("name").toString());
		holderView.text_Serise.setText(mList.get(position).get("serise")
				.toString());
		holderView.image_myfav_toselect
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int pos = (Integer) v.getTag();
						chooseState(pos, false, true);
						if (isAllSelect()) {
							System.out.println("已经全选了");
							DownloadFragment.btn_selectAll.setText("取消全选");
							notifyDataSetChanged();

						} else {
							notifyDataSetChanged();

						}
						DownloadFragment.btn_selectAll.setText("全选");
					}
				});
		holderView.image_myfav_toselect
				.setBackgroundDrawable(getView(position));
		return convertview;
	}

	int result = 0;

	public void getResult(int result) {
		this.result = result;
		System.out.println("正在下载的文件的进度为=====" + result + "=====文件的最大值是====>"
				+ holderView.processBar.getMax());
		holderView.processBar.setProgress(result);
		float num = (float) holderView.processBar.getProgress()
				/ (float) holderView.processBar.getMax();
		int text_Res = (int) (num * 100);
		holderView.text_Speed.setText(text_Res + "%");
		notifyDataSetChanged();
	}

	/**
	 * 设置进度条的总长度
	 * 
	 * */
	public void setMax(int maxSize) {
		System.out.println("正在下载的文件的最大值为=====" + maxSize);
		holderView.processBar.setMax(maxSize);
		notifyDataSetChanged();
	}

	public class HolderView {
		// private ImageView image_Img;
		public TextView text_Name, text_Serise, text_Speed;
		public ProgressBar processBar;
		public LinearLayout layout_toselect, layout_line;
		public ImageView image_myfav_toselect;

	}

	/****
	 * define a int to is to show the left
	 * 
	 * 
	 * the 1 is to show the 0 is not to show
	 * */
	public int LR = 0;

	public void toShow(int l_or_r) {
		LR = l_or_r;
		notifyDataSetChanged();
	}

	/**
	 * 
	 * 利用一个方法来得到是否已经全选，然后自己变换底部栏的选项
	 * */
	public boolean isAllSelect() {
		Boolean Flags = false;
		for (int i = 0; i < mList.size(); i++) {
			if (isChice[i] == false) {
				Flags = false;
				break;
			} else {
				Flags = true;

			}

		}
		return Flags;

	}

	private boolean isChice[];
	/**
	 * 
	 * 
	 * @param position用来表示点击的位置
	 * @param flags用来判断是否需要全部下载
	 *            。如果为false就是不用
	 * */
	private boolean isAll = false;
	VideoDowningDB v_DownDB = null;

	public void chooseState(int position, boolean flags, boolean flag) {

		isAll = flags;
		if (isAll) {

			isChice[position] = isChice[position] == true ? true : true;
		} else {
			// isChice[position] = isChice[position] == true ? false : flag;
			isChice[position] = isChice[position] == true ? false : true;
		}
		this.notifyDataSetChanged();
	}

	// 主要就是下面的代码了
	private LayerDrawable getView(int post) {
		/* 将数据添加到视频的数据库 */
		if (null == v_DownDB) {
			v_DownDB = new VideoDowningDB(mContext);
		}
		v_DownDB.update(mList.get(post).get("name").toString(), mList.get(post)
				.get("url").toString(), 0, "vedio", 0, 0);

		Bitmap bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(
				R.drawable.icon_selector_false)).getBitmap();
		Bitmap bitmap2 = null;
		LayerDrawable la = null;
		if (isChice[post] == true) {
			bitmap2 = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.icon_selector_true);
			v_DownDB.update(mList.get(post).get("name").toString(),
					mList.get(post).get("url").toString(), 1, "vedio", 0, 0);
		}

		if (bitmap2 != null) {
			Drawable[] array = new Drawable[1];
			// array[0] = new BitmapDrawable(bitmap);
			array[0] = new BitmapDrawable(bitmap2);
			la = new LayerDrawable(array);
			la.setLayerInset(0, 0, 0, 0, 0); // 第几张图离各边的间距
			// la.setLayerInset(1, 0, 65, 65, 0);
		} else {
			Drawable[] array = new Drawable[1];
			array[0] = new BitmapDrawable(bitmap);
			la = new LayerDrawable(array);
			la.setLayerInset(0, 0, 0, 0, 0);
		}
		return la; // 返回叠加后的图
	}

	public void Notify() {

		this.notifyDataSetChanged();
	}

}
