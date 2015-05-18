package com.ky.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.ky.adapter.RemindMeAdapter;
import com.ky.db.GetInterCutDB;
import com.ky.passenger.R;
import com.ky.utills.Configure;
import com.ky.utills.Utility;
import com.redbull.log.Logger;

import android.R.array;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * this is remind me s
 * 
 * @author Catherine.Brain
 * */
public class RemindMeFragment extends Fragment implements OnClickListener {
	View view = null;
	// Button btn_add;
	ListView list_Remind;
	RemindMeAdapter adapter;
	ArrayList<Map<String, String>> mList;
	public static String FRAGMENTNAME = "添加";
	GetInterCutDB getDB = null;
	LinearLayout layout_Toast;
	LinearLayout layout_Touch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == view) {
			view = LayoutInflater.from(getActivity()).inflate(R.layout.remind,
					null);
		}
		ViewGroup p = (ViewGroup) view.getParent();
		if (p != null) {
			p.removeView(view);
		}

		// if (getArguments() != null) {
		// conText = getArguments().getString("content");
		// Time = getArguments().getString("time");
		// isFrom = getArguments().getInt("from");
		// Toast.makeText(getActivity(), conText + "===" + Time,
		// Toast.LENGTH_LONG).show();
		// } else {
		// Toast.makeText(getActivity(), "空", Toast.LENGTH_LONG).show();
		//
		// }
		initView();

		return view;
	}

	String con = "暂时没有提醒内容";

	public void CheckDB() {
		mList = new ArrayList<Map<String, String>>();
		Map<String, String> map;

		if (null == getDB) {
			getDB = new GetInterCutDB(getActivity());

		}
		Cursor corsor = getDB.select();
		corsor.requery();
		if (corsor.moveToFirst()) {
			layout_Toast.setVisibility(View.GONE);
			for (corsor.moveToFirst(); !corsor.isAfterLast(); corsor
					.moveToNext()) {
				String content = corsor.getString(corsor
						.getColumnIndex(getDB.M_CONTINUE));
				String time = corsor.getString(corsor.getColumnIndex(getDB.TIME));
				String id = corsor.getString(corsor.getColumnIndex(getDB.ID));
				System.out.println("the content is====?" + content
						+ "the time is====?" + time + "the id is===>" + id);

				if (null != content || !content.equals("")) {
					con = content;
				} else {

				}
				map = new HashMap<String, String>();
				map.put("con", con);
				map.put("time", time);
				mList.add(map);
			}
		} else {
			layout_Toast.setVisibility(View.VISIBLE);

		}
	
		Logger.d("提醒的内容是==>", con + "and the mlist is==>" + mList);
	}

	String conText, Time;

	// int isFrom = 0;

	public void initView() {
		Configure.PAGER = 2;

		BaseActivity.btn_Return.setVisibility(View.VISIBLE);
		BaseActivity.btn_logo.setVisibility(View.GONE);
		BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		layout_Touch = (LinearLayout) view.findViewById(R.id.layout_touch);
		layout_Touch.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				close();
				return false;
			}
		});
		layout_Toast = (LinearLayout) view.findViewById(R.id.layout_toast);
		CheckDB();
		list_Remind = (ListView) view.findViewById(R.id.list_remind_me);

		adapter = new RemindMeAdapter(getActivity(), mList);
		list_Remind.setAdapter(adapter);
		// Utility.setListViewHeightBasedOnChildren(list_Remind);
		list_Remind.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("长按了。。。。。");
				deletItem(mList.get(position).get("con").toString());

				return false;
			}
		});
		list_Remind.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("点击。。。。。");
				deletItem(mList.get(position).get("con").toString());

			}
		});
	}

	public void deletItem(final String pos) {

		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("确定删除？");
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (null == getDB) {
					getDB = new GetInterCutDB(getActivity());
				}
				getDB.delete(pos);
				CheckDB();
				adapter.reGet(mList);
			}

		});
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}

		});
		builder.create().show();

	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		BaseActivity base = (BaseActivity) getActivity();
		switch (v.getId()) {
		// case R.id.btn_add:
		// newContent = new AddEventFragment();
		// base.switchContent(newContent, "添加", true);
		// break;
		default:
			break;
		}

	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}



}
