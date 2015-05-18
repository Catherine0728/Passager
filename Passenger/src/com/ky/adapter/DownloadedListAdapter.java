package com.ky.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import com.ky.db.VideoDownedDB;
import com.ky.mainactivity.DownloadFragment;
import com.ky.mainactivity.VedioPlayActivity;
import com.ky.passenger.R;
import com.ky.utills.Configure;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/***
 * 
 * this is list about my favorite
 * 
 * 这是已经下载的页面，处理好全选
 * 
 * @author Catherine.Brain
 * 
 * */

public class DownloadedListAdapter extends BaseAdapter {
	public static String TAG = "DownloadedListAdapter";
	private Context mContext;
	private ArrayList<Map<String, String>> mList;
	private LayoutInflater inflater;
	DownloadedListAdapter instance;
	// 定义一个字符串，然后进行判断，然后根据判断点击是否进入视频的页面
	public String fromWhere = "vedio";

	// ArrayList<String> VedioList = null;

	public DownloadedListAdapter(Context context,
			ArrayList<Map<String, String>> list, String fromWhere) {
		mContext = context;
		mList = list;
		isChice = new boolean[mList.size()];
		for (int i = 0; i < mList.size(); i++) {
			isChice[i] = false;
		}
		inflater = LayoutInflater.from(mContext);
		this.fromWhere = fromWhere;
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
	}

	HolderView holderView;

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		if (null == convertview) {
			convertview = inflater.inflate(R.layout.downloaded, null);
			holderView = new HolderView();
			// holderView.image_Img = (ImageView) convertview
			// .findViewById(R.id.image_img);
			holderView.text_Name = (TextView) convertview
					.findViewById(R.id.text_name);
			// holderView.layout_myfav_go = (LinearLayout) convertview
			// .findViewById(R.id.layout_myfav_go);
			holderView.layout_toselect = (LinearLayout) convertview
					.findViewById(R.id.layout_myfav_toselect);
			holderView.image_myfav_toselect = (ImageView) convertview
					.findViewById(R.id.image_mygav_toselect);
			holderView.btn_check = (Button) convertview
					.findViewById(R.id.btn_check);
			holderView.text_size = (TextView) convertview
					.findViewById(R.id.text_size);
			holderView.btn_check.setTag(position);
			holderView.image_myfav_toselect.setTag(position);
			convertview.setTag(holderView);
		}
		if (fromWhere.equals("game")) {
			holderView.btn_check.setText("安装");
		} else {

			holderView.btn_check.setText("查看");
		}

		holderView = (HolderView) convertview.getTag();
		// holderView.image_Img.setBackgroundResource((Integer) mList
		// .get(position).get("image"));
		holderView.text_size
				.setText(mList.get(position).get("size").toString());
		holderView.text_Name
				.setText(mList.get(position).get("name").toString());
		// VedioList = new ArrayList<String>();
		System.out.println("mList.get(position).getng()"
				+ mList.get(position).get("url").toString());
		if (LR == 0) {
			holderView.layout_toselect.setVisibility(View.GONE);
		} else {

			holderView.layout_toselect.setVisibility(View.VISIBLE);
		}
		// VedioList.add(mList.get(position).get("vedio").toString());
		holderView.btn_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int pos = (Integer) v.getTag();
				if (holderView.text_Name.getText().toString().indexOf(".apk") != -1) {
					File file = new File(Configure.StroageFileName
							+ Configure.GameFile
							+ holderView.text_Name.getText().toString());
					openFile(file);
				} else {
					Intent intent = new Intent();
					intent.putExtra("vedio", mList.get(pos).get("url")
							.toString());
					intent.setClass(mContext, VedioPlayActivity.class);
					mContext.startActivity(intent);

				}

			}
		});
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

							DownloadFragment.btn_selectAll.setText("全选");
							notifyDataSetChanged();
						}
					}
				});
		holderView.image_myfav_toselect
				.setBackgroundDrawable(getView(position));
		return convertview;
	}

	class HolderView {

		Button btn_check;
		private TextView text_Name, text_size;
		private LinearLayout layout_toselect;
		private ImageView image_myfav_toselect;

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

	/**
	 * 
	 * 利用一个方法来得到那些是已经选中的，那些是没有选中的
	 * */

	public ArrayList<Integer> getChoose = null;

	public ArrayList<Integer> getChoose() {
		getChoose = new ArrayList<Integer>();
		for (int i = 0; i < mList.size(); i++) {
			if (isChice[i] == false) {

			} else {
				getChoose.add(i);

			}

		}
		return getChoose;

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

	private boolean isChice[];
	/**
	 * 
	 * 
	 * @param position用来表示点击的位置
	 * @param flags用来判断是否需要全选
	 *            。如果为false就是不用
	 * */
	private boolean isAll = false;
	VideoDownedDB videoDB = null;

	public void chooseState(int position, boolean flags, boolean flag) {
		isAll = flags;
		if (isAll) {

			isChice[position] = isChice[position] == true ? true : true;
		} else {
			isChice[position] = isChice[position] == true ? false : true;
			// isChice[position] = isChice[position] == true ? false : flag;
		}

		this.notifyDataSetChanged();
	}

	// 主要就是下面的代码了
	private LayerDrawable getView(int post) {
		if (null == videoDB) {
			videoDB = new VideoDownedDB(mContext);
		}

		videoDB.update(mList.get(post).get("name").toString(), mList.get(post)
				.get("url").toString(), 0);
		Bitmap bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(
				R.drawable.icon_selector_false)).getBitmap();
		Bitmap bitmap2 = null;
		LayerDrawable la = null;
		if (isChice[post] == true) {
			bitmap2 = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.icon_selector_true);
			videoDB.update(mList.get(post).get("name").toString(),
					mList.get(post).get("url").toString(), 1);
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

	/**
	 * 
	 * 如果在这里，我们安装成功，就可以直接弹出安装界面了 在手机上打开文件
	 */
	public void openFile(File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		/* 调用getMIMEType()来取得MimeType */
		String type = "application/vnd.android.package-archive";
		/* 设置intent的file与MimeType */
		intent.setDataAndType(Uri.fromFile(f), type);
		mContext.startActivity(intent);
	}

}
