package com.ky.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.android.listener.IsSuccessListener;
import com.ky.passenger.R;
import com.lhl.dialog.library.AlertDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * this is my self
 * 
 * @author Catherine.Brain
 * */
public class MySelfAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Map<String, Object>> mList;
	int point;

	public MySelfAdapter(Context context, ArrayList<Map<String, Object>> list,
			int point) {

		mContext = context;
		mList = list;
		this.point = point;
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

	HolderView holderView = null;

	@Override
	public View getView(final int position, View convertview, ViewGroup parent) {
		if (null == convertview) {
			convertview = LayoutInflater.from(mContext).inflate(
					R.layout.my_self_cell, null);
			holderView = new HolderView();
			holderView.text_package_name = (TextView) convertview
					.findViewById(R.id.text_package_name);
			holderView.text_package_opera = (TextView) convertview
					.findViewById(R.id.text_package_opera);
			holderView.btn_buy = (Button) convertview
					.findViewById(R.id.btn_buy);

			convertview.setTag(holderView);
		}
		holderView = (HolderView) convertview.getTag();
		holderView.text_package_name.setText(mList.get(position).get("name")
				.toString());
		holderView.text_package_opera.setText(mList.get(position).get("opera")
				.toString());
		holderView.btn_buy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int point = (Integer) mList.get(position).get("point");
				if (point < 100) {
					new AlertDialog(mContext, AlertDialog.ALERT_TYPE, isSuccess)
							.setTitleText("购买成功")
							.setContentText("当前点数：" + (100 - point))
							.setConfirmText("确认").setCancelText("取消").show();
				} else {
					new AlertDialog(mContext, AlertDialog.ALERT_TYPE, isSuccess)
							.setTitleText("购买失败").setContentText("当前点数不够")
							.setConfirmText("确认").setCancelText("取消").show();

				}

			}
		});
		return convertview;
	}

	class HolderView {

		TextView text_package_name, text_package_opera;
		Button btn_buy;

	}

	IsSuccessListener isSuccess = new IsSuccessListener() {

		@Override
		public void IsSuccess(Boolean isExit) {

		}
	};
}
