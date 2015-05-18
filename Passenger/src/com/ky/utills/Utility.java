package com.ky.utills;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.redbull.log.Logger;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
	static String TAG = "Utility";

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		Logger.d(TAG, "the totalHeight is===>" + listView.getDividerHeight());
		listView.setLayoutParams(params);
	}

	public static void setGridViewHeightBasedOnChildren(GridView gridView,
			int num, int row, String tag) {
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int count = 0;
		if (listAdapter.getCount() % num == 0) {
			count = listAdapter.getCount() / num;
		} else {
			count = listAdapter.getCount() / num + 1;

		}

		for (int i = 0; i < count; i++) {
			View listItem = listAdapter.getView(i, null, gridView);
			listItem.measure(1, 1);
			totalHeight += listItem.getMeasuredHeight();
			Logger.d(TAG + "---" + tag, "the totalHeight is===>" + totalHeight);
		}

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight + (5 * (count));
		Logger.d(TAG + "---" + tag, "the count is===>" + count
				+ "and the params height is[===>" + params.height
				+ "-----the gridview height is==" + gridView.getHeight());
		gridView.setLayoutParams(params);
	}

	public static void setListViewHeightBasedOnChildren(
			PullToRefreshListView mPullRefreshListView) {
//		ListAdapter listAdapter = listView.getAdapter();
//		if (listAdapter == null) {
//			// pre-condition
//			return;
//		}
//
//		int totalHeight = 0;
//		for (int i = 0; i < listAdapter.getCount(); i++) {
//			View listItem = listAdapter.getView(i, null, listView);
//			listItem.measure(0, 0);
//			totalHeight += listItem.getMeasuredHeight();
//		}
//
//		ViewGroup.LayoutParams params = listView.getLayoutParams();
//		params.height = totalHeight
//				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//		Logger.d(TAG, "the totalHeight is===>" + listView.getDividerHeight());
//		listView.setLayoutParams(params);

	}
}