package com.ky.mainactivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import com.ky.adapter.MyCenterItemAdapter;
import com.ky.adapter.RemindMeAdapter;
import com.ky.db.IsLoginDB;
import com.ky.mainactivity.BaseActivity.Job;
import com.ky.passenger.R;
import com.ky.request.GetAllAppVersionRequest;
import com.ky.response.GetAllAppVersionResponse;
import com.ky.utills.DataClearTools;
import com.ky.utills.DeviceInfo;
import com.ky.utills.FileUtils;
import com.ky.utills.LauncherBiz;
import com.ky.utills.NetProperty;
import com.ky.utills.Configure;
import com.ky.utills.Utility;
import com.lhl.callback.IUpdateData;
import com.redbull.log.Logger;

import android.R.array;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * this is my center
 * 
 * @author Catherine.Brain
 * */
public class MySelfOutFragment extends Fragment implements OnClickListener {
	String TAG = "MySelfOutFragment";
	View view = null;
	public static Button btn_My, btn_About, btn_Clear_Cache, btn_Advice,
			btn_Login, btn_exit;
	public static String FRAGMENTNAME = "我的";
	ListView list_Center;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == view) {
			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.my_center, null);
		}
		ViewGroup p = (ViewGroup) view.getParent();
		if (p != null) {
			p.removeView(view);
		}
		initView();
		return view;
	}

	public void initView() {
		System.out.println("this is initview");
		Configure.PAGER = 4;
		BaseActivity.btn_Return.setVisibility(View.VISIBLE);
		BaseActivity.btn_logo.setVisibility(View.GONE);
		BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		btn_Login = (Button) view.findViewById(R.id.btn_login);
		// btn_My = (Button) view.findViewById(R.id.btn_my);
		// btn_About = (Button) view.findViewById(R.id.btn_about_us);
		//
		// btn_Advice = (Button) view.findViewById(R.id.btn_advice);
		// btn_Clear_Cache = (Button) view.findViewById(R.id.btn_clear_cache);
		list_Center = (ListView) view.findViewById(R.id.list_center);

		// btn_My.setOnClickListener(this);
		// btn_About.setOnClickListener(this);
		// btn_Advice.setOnClickListener(this);
		// btn_Clear_Cache.setOnClickListener(this);
		btn_exit = (Button) view.findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(this);
		btn_Login.setOnClickListener(this);
		CheckDB();
		getAdapter();
	}

	MyCenterItemAdapter myAdapter = null;
	ArrayList<Map<String, Object>> mList = null;
	String[] strArray = { "我的账户", "关于我们", "意见反馈", "清除缓存", "版本更新" };
	int[] imageArray = { R.drawable.icon_my, R.drawable.icon_down,
			R.drawable.icon_barrage, R.drawable.icon_alert,
			R.drawable.icon_flush };

	public void getAdapter() {

		mList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < strArray.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", strArray[i]);
			map.put("image", imageArray[i]);
			mList.add(map);
		}
		if (null == myAdapter) {
			myAdapter = new MyCenterItemAdapter(getActivity(), mList);
		}
		list_Center.setAdapter(myAdapter);
		Utility.setListViewHeightBasedOnChildren(list_Center);
		list_Center.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 0) {
					myUser();

				} else if (position == 1) {
					AboutUs();
				} else if (position == 2) {
					feedBack();
				} else if (position == 3) {
					clearCache();

				} else if (position == 4) {
					UpdateVersion();
				}

			}
		});

	}

	int point;
	IsLoginDB isLogin = null;
	String username = null;

	public void CheckDB() {
		if (null == isLogin) {
			isLogin = new IsLoginDB(getActivity());
		}

		Cursor cursor = isLogin.select();
		Logger.e(TAG, "the cours is==>" + cursor);
		if (cursor.moveToFirst()) {
			Logger.d(TAG, "当前由用户");
			username = cursor.getString(cursor
					.getColumnIndex(isLogin.u_UserName));
			point = cursor.getInt(cursor.getColumnIndex(isLogin.u_Point));
			if (null == username || username.equals("")) {
				btn_Login.setText("立即登录");
				btn_exit.setVisibility(View.GONE);
			} else {
				btn_Login.setText(username);

				btn_exit.setVisibility(View.VISIBLE);
			}

		} else {
			Logger.d(TAG, "当前没用户");
			btn_Login.setText("立即登录");
			btn_exit.setVisibility(View.GONE);
		}
		Logger.d(TAG, "the login text is==>" + btn_Login.getText());
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		BaseActivity base = (BaseActivity) getActivity();
		switch (v.getId()) {
		case R.id.btn_login:
			if (btn_Login.getText().toString().trim().equals("立即登录")) {
				// newContent = new LoginFragment();
				// base.switchContent(newContent, "登录", true);
				startActivity(new Intent().setClass(getActivity(),
						LoginFragment.class));
			} else {
				newContent = new MySelfInFragment();
				Bundle bundle = new Bundle();
				bundle.putString("userName", btn_Login.getText().toString()
						.trim());
				bundle.putString("point", "34");
				newContent.setArguments(bundle);
				base.switchContent(newContent, "我的账户", true);
			}

			break;
		case R.id.btn_exit:
			DataClearTools.cleanApplicationData(getActivity(), null);
			startActivity(new Intent().setClass(getActivity(),
					LoginFragment.class));
			getActivity().finish();
			break;
		default:
			break;
		}

	}

	public void myUser() {
		Fragment newContent = null;
		BaseActivity base = (BaseActivity) getActivity();
		if (btn_Login.getText().toString().trim().equals("立即登录")) {
			// newContent = new LoginFragment();
			// base.switchContent(newContent, "登录",true);
			startActivity(new Intent().setClass(getActivity(),
					LoginFragment.class));
			// getActivity().finish();
		} else {
			newContent = new MySelfInFragment();
			Bundle bundle = new Bundle();
			bundle.putString("userName", btn_Login.getText().toString().trim());
			bundle.putInt("point", point);
			newContent.setArguments(bundle);
			base.switchContent(newContent, "我的账户", true);
		}
	}

	public void AboutUs() {
		Fragment newContent = null;
		BaseActivity base = (BaseActivity) getActivity();
		newContent = new AboutUsFragment();

		base.switchContent(newContent, "关于", true);
	}

	public void clearCache() {
		Fragment newContent = null;
		BaseActivity base = (BaseActivity) getActivity();
		/**
		 * 
		 * 实现缓存清理
		 * */
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("确定要清除所有缓存");
		builder.setTitle("提示");
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}

		});
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				DataClearTools.cleanApplicationData(getActivity(), null);
			}

		});
		builder.create().show();
	}

	public void feedBack() {
		Fragment newContent = null;
		BaseActivity base = (BaseActivity) getActivity();
		newContent = new FeedBackFragment();
		base.switchContent(newContent, "反馈", true);

	}

	/**
	 * 
	 * 
	 * 检测更新版本 默认当前的版本是1
	 * */
	int version = 1;

	public void UpdateVersion() {

		try {
			version = Configure.localVersion;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Logger.i(TAG, "当前系统的版本是====>" + version);
		showUpdataDialog("发现最新版本，请立即后台更新", 1);
		// GetAllAppVersionRequest gaavrRequest = new GetAllAppVersionRequest(
		// getActivity(), version);
		// new com.ky.request.HttpHomeLoadDataTask(getActivity(), getVersion,
		// false, "http://www/baidu.com", true).execute(gaavrRequest);

	}

	IUpdateData getVersion = new IUpdateData() {

		@Override
		public void updateUi(Object o) {
			GetAllAppVersionResponse allversion = new GetAllAppVersionResponse();
			allversion.paseRespones(o.toString());
			Configure.serverVersion = allversion.dateInfo.version;
			Logger.i(TAG, "服务器的版本是===》" + Configure.serverVersion);
			allversion.dateInfo.version = 1;
			if (allversion.dateInfo.version > Configure.localVersion) {
				Log.i(TAG, "版本号不同 ,提示用户升级 ");
				Message msg = new Message();
				msg.what = UPDATA_CLIENT;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = SAME;
				handler.sendMessage(msg);
			}

		}

		@Override
		public void handleErrorData(String info) {
			Log.e(TAG, "升级遇到问题");
			// 待处理
			Message msg = new Message();
			msg.what = GET_UNDATAINFO_ERROR;
			handler.sendMessage(msg);
		}
	};

	/*
	 * 
	 * 弹出对话框通知用户更新程序
	 * 
	 * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
	 * 3.通过builder 创建一个对话框 4.对话框show()出来
	 */
	protected void showUpdataDialog(String dialogTitle, final int flag) {
		AlertDialog.Builder builer = new Builder(getActivity());
		builer.setTitle("版本检测");
		builer.setMessage(dialogTitle);
		if (flag == 0) {

		} else {

			// 当点确定按钮时从服务器上下载 新的apk 然后安装

			builer.setPositiveButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		}

		// 当点取消按钮时进行登录
		builer.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (flag == 0) {
					dialog.dismiss();
				} else {

					Log.i(TAG, "下载apk,更新");
					// downLoadApk();
					String updaturl = "http://gdown.baidu.com/data/wisegame/44678f0ac42a4755/oupengliulanqi_50.apk";
					extensionName = Configure.getExtensionName(updaturl);
					downloadapk("update.apk", updaturl);

				}
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	// /*
	// * 获取当前程序的版本号
	// */
	// private int getVersionName() {
	// // 获取packagemanager的实例
	// PackageManager packageManager = getActivity().getPackageManager();
	// // getPackageName()是你当前类的包名，0代表是获取版本信息
	// PackageInfo packInfo = null;
	// try {
	// packInfo = packageManager.getPackageInfo(getActivity()
	// .getPackageName(), 0);
	// Configure.localVersion = packInfo.versionCode;
	// int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	// Logger.i(TAG, "当前获取的版本code是=====>" + Configure.localVersion
	// + "----当前的系统版本是====》" + currentapiVersion
	// + "---当前的系统的release===" + android.os.Build.VERSION.RELEASE
	// + "=====当前系统的sdk版本是===" + android.os.Build.VERSION.SDK
	// + "===当前系统的模式是====" + android.os.Build.MODEL);
	// } catch (NameNotFoundException e) {
	//
	// e.printStackTrace();
	// }
	// return Configure.localVersion;
	//
	// }

	public static final int UPDATA_CLIENT = 0x000033;
	public static final int DOWN_ERROR = 0x000011;
	public static final int DOWN_SUCCESS = 0x000044;
	public static final int GET_UNDATAINFO_ERROR = 0x000022;
	public static final int DownWrong = 0x000055;
	public static final int SAME = 0x000066;

	// 安装apk
	protected void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	public static Boolean isHap = false;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_CLIENT:
				/**
				 * 第一：得到服务器的版本，然后将现在的版本与服务器的版本进行对吧， 如果这俩的版本不一样，那么，就对于得到的链接进行下载
				 * */

				showUpdataDialog("检查到最新版本，请立即后台更新", 1);
				break;
			case SAME:
				/**
				 * 版本相同，提示用户暂时不用更新
				 * */
				showUpdataDialog("当前已经是最新版本！", 0);
				break;
			case GET_UNDATAINFO_ERROR:
				// 服务器超时
				Toast.makeText(getActivity(), "获取服务器更新信息失败", 1).show();
				// LoginMain();
				break;
			case DOWN_ERROR:
				// 下载apk失败
				Toast.makeText(getActivity(), "下载新版本失败", 1).show();
				// LoginMain();
				break;
			case DOWN_SUCCESS:
				// 下载apk成功，然后就开始安装
				openFile(new File(Configure.saveFileName));
				break;
			case DownWrong:
				// 下载的不是apk文件
				Toast.makeText(getActivity(), "下载文件不是apk文件", 1).show();
				break;
			}
		}
	};
	/**
	 * 下载
	 * 
	 * @param url
	 */
	FinalHttp finalHttp = new FinalHttp();
	HttpHandler httpHandler;
	// CommonToast toast;
	String extensionName;
	File file;

	public void downloadapk(final String fileName, final String fileUrl) {
		Logger.log("currentFileName:" + fileName + ",filedownUrl:" + fileUrl
				+ "下载的本地路径是===" + Configure.saveFileName);
		File saveFile = new File(Configure.savePath);
		if (!saveFile.exists()) {
			if (saveFile.mkdir()) {

			} else {

			}
		} else {
			// 判断文件是否存在(如果存在则删除)
			try {
				boolean isFileExist = FileUtils
						.delectFile(Configure.saveFileName);
				Logger.log("isFileExist:" + isFileExist);
			} catch (IOException e) {
				Logger.log("deleteFoder failed:" + e.getLocalizedMessage());
			}

		}

		httpHandler = finalHttp.download(fileUrl, Configure.saveFileName, true,
				new AjaxCallBack<File>() {
					@Override
					public void onStart() {
						Logger.log("开始下载");
					}

					@Override
					public void onLoading(long count, long current) {
						Logger.log(current + "/" + count);
					}

					@Override
					public void onSuccess(final File t) {
						Logger.log("下载成功");

						// 如果是apk的话就执行安装
						if (".apk".equals(extensionName)) {
							// // 强制安装
							// boolean isInstall = LauncherBiz
							// .execCommand("system/bin/pm install -r "
							// + target + fileName);
							// Logger.log("isInstall:" + isInstall);
							// file = new File(Configure.saveFileName);

							handler.sendEmptyMessage(DOWN_SUCCESS);
						} else {
							handler.sendEmptyMessage(DownWrong);
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						handler.sendEmptyMessage(DOWN_ERROR);
					}

				});
	}

	/**
	 * 
	 * 如果在这里，我们安装成功，就可以直接弹出安装界面了 在手机上打开文件
	 */
	private void openFile(File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		/* 调用getMIMEType()来取得MimeType */
		String type = "application/vnd.android.package-archive";
		/* 设置intent的file与MimeType */
		intent.setDataAndType(Uri.fromFile(f), type);
		startActivity(intent);
	}
}
