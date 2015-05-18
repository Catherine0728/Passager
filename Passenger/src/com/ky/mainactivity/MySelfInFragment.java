package com.ky.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ky.adapter.MySelfAdapter;
import com.ky.passenger.R;
import com.ky.utills.Configure;
import com.ky.utills.Utility;
import com.lhl.dialog.library.AlertDialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * this is my center,and there is in
 * 
 * @author Catherine.Brain
 * */
public class MySelfInFragment extends Fragment {
	View view = null;
	ListView list_Package;
	MySelfAdapter adapter = null;
	ArrayList<Map<String, Object>> mList;
	TextView text_Motto, text_Point;
	public static String FRAGMENTNAME = "我的";
	String userName;
	int point;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == view) {
			view = LayoutInflater.from(getActivity()).inflate(R.layout.my_self,
					null);
		}
		ViewGroup p = (ViewGroup) view.getParent();
		if (p != null) {
			p.removeView(view);
		}
		if (getArguments() != null) {
			userName = getArguments().getString("userName");
			point = getArguments().getInt("point", 30);
		} else {

		}
		initView();
		return view;
	}

	String[] strName = { "套餐一", "套餐二", "套餐三" };
	String[] strOpera = { "100/10元", "150/14元", "200/18元" };

	public void initView() {
		BaseActivity.btn_Return.setVisibility(View.VISIBLE);
		BaseActivity.btn_logo.setVisibility(View.GONE);
		Configure.PAGER = 5;
		BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		text_Motto = (TextView) view.findViewById(R.id.text_motto);
		text_Motto.setText(userName);
		text_Point = (TextView) view.findViewById(R.id.text_point);
		text_Point.setText(point + "");
		list_Package = (ListView) view.findViewById(R.id.list_package);
		mList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 3; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", strName[i]);
			map.put("opera", strOpera[i]);
			map.put("point", point);
			mList.add(map);
		}
		adapter = new MySelfAdapter(getActivity(), mList, point);
		list_Package.setAdapter(adapter);
		Utility.setListViewHeightBasedOnChildren(list_Package);
	}
}
