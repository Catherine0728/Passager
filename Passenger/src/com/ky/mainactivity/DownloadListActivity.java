package com.ky.mainactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.android.listener.CheckStateListener;
import com.ky.adapter.DownloadListAdapter_xUtils;
import com.ky.db.VideoDowningDB;
import com.ky.passenger.R;
import com.ky.utills.Configure;
import com.lhl.db.DowningVideoDB;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.sample.download.DownloadInfo;
import com.lidroid.xutils.sample.download.DownloadManager;
import com.lidroid.xutils.sample.download.DownloadService;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.logging.Logger;

/**
 * 这是利用xUtils实现的下载
 */
public class DownloadListActivity extends Activity {

	@ViewInject(R.id.download_list)
	private ListView downloadList;

	@ViewInject(R.id.btn_edit)
	private Button btn_edit;

	@ViewInject(R.id.btn_delete)
	private Button btn_Delete;
	@ViewInject(R.id.btn_select_all)
	private Button btn_Select_All;
	@ViewInject(R.id.btn_return)
	private Button btn_return;
	private DownloadManager downloadManager;
	private DownloadListAdapter_xUtils downloadListAdapter;
	private RadioGroup radioGroup;
	@ViewInject(R.id.btn_list_0)
	private RadioButton radioButton_one;
	@ViewInject(R.id.btn_list_1)
	private RadioButton radioButton_two;
	@ViewInject(R.id.btn_list_2)
	private RadioButton radioButton_three;
	private Context mAppContext;
	int fromWhere = 0;
	@ViewInject(R.id.layout_bottom)
	private LinearLayout layout_bottom;
	// 定义一个变量来表示当前选择的的单选按钮
	int RadioFlag = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_list);
		ViewUtils.inject(this);

		mAppContext = this.getApplicationContext();

		downloadManager = DownloadService.getDownloadManager(mAppContext);

		downloadListAdapter = new DownloadListAdapter_xUtils(this, 0,
				downloadManager, checkLisener);
		downloadList.setAdapter(downloadListAdapter);
		initView();
	}

	/**
	 * 初始化一控件
	 * */
	public void initView() {
		Configure.isEdit = false;
		fromWhere = getIntent().getIntExtra("fromWhere", 0);

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup_list);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				CheckButton(checkedId);
			}
		});
		if (fromWhere == 0 || fromWhere == -1) {
			radioButton_one.setChecked(true);
		} else if (fromWhere == 1) {
			radioButton_two.setChecked(true);
		} else {
			radioButton_three.setChecked(true);
		}

	}

	/**
	 * 触发点击事件
	 * 
	 * */
	public void CheckButton(int id) {
		switch (id) {
		case R.id.btn_list_0:// 视频
			downloadListAdapter.CheckUpdate(0);
			RadioFlag = 0;
			break;
		case R.id.btn_list_1:// 小说
			downloadListAdapter.CheckUpdate(1);
			RadioFlag = 1;
			break;
		case R.id.btn_list_2:// 游戏
			downloadListAdapter.CheckUpdate(2);
			RadioFlag = 2;

			break;
		default:
			break;
		}
		btn_edit.setText(R.string.edit);
		isEdit = false;
		downloadListAdapter.SetDefault(RadioFlag);
		layout_bottom.setVisibility(View.GONE);
		downloadListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();

		downloadListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy() {
		try {
			if (downloadListAdapter != null && downloadManager != null) {
				downloadManager.backupDownloadInfoList();
			}
		} catch (DbException e) {
			LogUtils.e(e.getMessage(), e);
		}
		super.onDestroy();
	}

	/**
	 * 点击停止全部
	 * 
	 * */
	private boolean isEdit = false;

	@OnClick(R.id.btn_edit)
	public void Edit(View view) {

		switch (view.getId()) {
		case R.id.btn_edit:
			if (isEdit) {
				btn_edit.setText(mAppContext.getString(R.string.edit));
				isEdit = false;
				// downloadListAdapter.holder.removeBtn
				// .setBackgroundResource(R.drawable.icon_delete);
				layout_bottom.setVisibility(View.GONE);

				Configure.isEdit = false;

			} else {

				btn_edit.setText(mAppContext.getString(R.string.cancel));
				layout_bottom.setVisibility(View.VISIBLE);
				isEdit = true;
				downloadListAdapter.SelectAll(false);
				// 点击进行操作删除
				// downloadListAdapter.holder.removeBtn
				// .setBackgroundResource(R.drawable.icon_select_default);
				downloadListAdapter.notifyDataSetChanged();
				Configure.isEdit = true;
			}
			break;

		default:
			break;
		}
	}

	// 定义一个变量来切换功能
	private boolean isSelectAll = false;

	@OnClick(R.id.btn_select_all)
	public void StopAll(View view) {
		switch (view.getId()) {
		case R.id.btn_select_all:
			if (isSelectAll) {
				btn_Select_All.setText("全选");
				downloadListAdapter.SelectAll(false);
				isSelectAll = false;
			} else {
				btn_Select_All.setText("取消全选");
				isSelectAll = true;
				downloadListAdapter.SelectAll(true);
			}

			break;
		}

	}

	Boolean Mes = false;

	@OnClick(R.id.btn_delete)
	public void DeleteAll(View view) {
		switch (view.getId()) {
		case R.id.btn_delete:
			if (downloadListAdapter.CheckSelect(0)) {
				Mes = true;
			} else {
				Mes = false;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			if (Mes) {
				builder.setMessage("确定删除所有选中？");
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
			} else {

				builder.setMessage("请选择要删除的文件！");

			}
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (Mes) {
								downloadListAdapter.Remove(RadioFlag);
								downloadListAdapter.SelectAll(false);
							} else {

							}
						}
					});
			builder.create();
			builder.show();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent().setClass(DownloadListActivity.this,
					BaseActivity.class));
			finish();
		}
		return false;
	}

	@OnClick(R.id.btn_return)
	public void btn_Return(View view) {
		startActivity(new Intent().setClass(DownloadListActivity.this,
				BaseActivity.class));
		this.finish();

	}

	// 建立一个方法来根据选中而进行动态改变
	CheckStateListener checkLisener = new CheckStateListener() {

		@Override
		public void IsChange(boolean isAllSelect) {
			if (isAllSelect) {
				System.out.println("取消全选");
				btn_Select_All.setText("取消全选");
			} else {
				System.out.println("全选");
				btn_Select_All.setText("全选");

			}

		}
	};
}