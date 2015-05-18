package com.ky.mainactivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import com.android.listener.IConnectInfo;
import com.android.network.IManagerNetwork;
import com.android.network.ManagerNetwork;
import com.baidu.mapapi.SDKInitializer;
import com.ky.adapter.CityAdapter;
import com.ky.db.IsLoginDB;
//import com.ky.adapter.MainExpandableAdapter;
import com.ky.passenger.R;
import com.ky.request.GetAllAppVersionRequest;
import com.ky.response.GetAllAppVersionResponse;
import com.ky.utills.BaiDuGetLocation;
import com.ky.utills.DeviceCheck;
import com.ky.utills.DeviceInfo;
import com.ky.utills.FileHelper;
import com.ky.utills.FileUtils;
import com.ky.utills.LauncherBiz;
import com.ky.utills.NetProperty;
import com.ky.utills.Configure;
import com.lhl.callback.IUpdateData;
import com.meiya.hxj.wifi.WifiUtil;
import com.meiya.hxj.wifi.WifiUtil.WifiCipherType;
import com.redbull.log.Logger;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;

/**
 * this is a base activity
 * 
 * @category to judge the net is connect
 * @category and to judge the net is private or public to control show on the
 *           activity
 * @category to control the application
 * 
 * @author Catherine.Brain
 * 
 * 
 * 
 * */
public class BaseActivity extends FragmentActivity implements OnClickListener {
	private static String TAG = "BaseActivity";
	// public static boolean flag;
	View view = null;
	FrameLayout body_Layout;
	public static Button btn_Return, btn_logo;
	public static Button btn_Barrage, btn_Remind, btn_Download, btn_Myself,
			btn_Main;
	public static TextView text_nav_Chose;
	Fragment mContext;
	Fragment main = null;
	static Context con;
	public FragmentManager mFragmentManager = null;
	public static String FRAGMENTNAME = "";
	ListView listview_city;
	LinearLayout layout_City;
	public static com.lhl.dialog.library.AlertDialog aler = null;

	// String[] strCity = { "北京", "上海", "重庆", "广州", "四川", };

	@Override
	public void onAttachFragment(Fragment fragment) {
		Logger.d(TAG, "onAttachFragment");
		super.onAttachFragment(fragment);
	}

	String tag = "main";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.basemain);

		Intent intent = getIntent();
		tag = intent.getStringExtra("tag");
		Logger.d(TAG, "the tag is===>" + tag);
		main = new MainActivity();

		// 判断savedInstanceState不为空的时候，获取当前的mContent
		if (null == mFragmentManager) {
			mFragmentManager = getSupportFragmentManager();
		}
		if (savedInstanceState != null) {
			mContext = mFragmentManager.getFragment(savedInstanceState,
					"mContext");

		} else {
			mFragmentManager.beginTransaction().add(R.id.body_layout, main)
					.commitAllowingStateLoss();
		}

		if (mContext == null) {

			mContext = main;
		}
		con = this;
		Intent in_Modifer = getIntent();
		boolean isModifer = in_Modifer.getBooleanExtra("isModifer", false);
		if (isModifer) {
			/* 如果为真，就代表内存里面有文件，且已经修改了 */
			FileHelper helper = new FileHelper(getApplicationContext());

			String content = helper.readSDFile("test.txt");
			String[] str = content.split("-");

			Configure.IP = "http://" + str[0].toString().trim() + "/";
			Configure.NETHomePath = Configure.IP + "keyun_api/client/";
			Configure.OUTHomePath = Configure.IP + "keyun_api/server/";
			Configure.WIFISSID = str[2].toString().trim();
			Configure.SSIDPWd = str[3].toString().trim();
			System.out.println("服务器的Ip:" + Configure.IP + "客户端的Ip:" + str[1]
					+ "wifi的ssid是：" + Configure.WIFISSID + "widi的密码为："
					+ Configure.SSIDPWd);

		} else {
			/* 如果不为真，就代表是设置默认的值 */
			Configure.IP = "http://ky.api.rssrc.com/";
			Configure.NETHomePath = Configure.IP + "client/";
			Configure.OUTHomePath = Configure.IP + "server/";
		}
		GetLocation();
		initView();

	}

	/**
	 * 
	 * 判断当前是否有用户 如果没有，我们就跳转到登录的界面上去，如果有，我们就直接post认证以及获取数据
	 * 
	 * */

	public void TOJudgeUserExist() {
		if (null == isLogin) {
			isLogin = new IsLoginDB(this);
		}

		Cursor cursor = isLogin.select();
		if (cursor.moveToFirst()) {
			/* 有用户 */
			System.out.println("当前有用户");
			GetMAC();
			SendReQuest();

		} else {
			/* 没有用户 */
			System.out.println("当前没有用户");
			// startActivity(new Intent().setClass(this, LoginFragment.class));
		}
	}

	/**
	 * 
	 * 在得到当前已经有用户的时候，我们去得到设备的MAC地址，然后，去请求认证
	 * */
	public void GetMAC() {
		Configure.WIFIMAC = DeviceCheck.getWifiMac(this);
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		Configure.AndroidID = DeviceCheck.int2IPStr(ipAddress);
		Configure.MAC = NetProperty.getBSSID(BaseActivity.this);
		System.out.println("the mac is===>" + Configure.MAC);

		Logger.d("SendReQuest", "the mac is===>" + Configure.MAC
				+ "and the androidid is===>" + Configure.AndroidID
				+ "the wifimac is===>" + Configure.WIFIMAC);
	}

	/**
	 * 
	 * 在知道当前的用户后，我们向服务器发送自己的认证消息，获得外网的权限
	 * */
	public void SendReQuest() {
		Logger.d("SendReQuest", "server-SendReQuest");
		Map<String, String> map = new HashMap<String, String>();
		map.put("mac", Configure.WIFIMAC);
		// map.put("gw_address", StaticVariable.AndroidID);
		map.put("gw_id", Configure.MAC);

		ManagerNetwork
				.doPost("http://sqwifi.zaitianweb.com/index.php/api/Userauth/noAuthApp/",
						map, new IManagerNetwork() {
							@Override
							public void onUrlError() {
								Logger.e(TAG, "路径错误");
							}

							@Override
							public void onSuccessDatas(String str) {
								System.out
										.println("onSuccessDatas-----and the str is==="
												+ str);

								Token = str;
								handler.sendEmptyMessage(DOPOST);
							}

							@Override
							public void onReadDataTimeOut() {
								Logger.e(TAG, "onReadDataTimeOut");

							}

							public void onError(Exception e) {
								// myHandler.sendEmptyMessage(ERROR);
								Logger.e(TAG, "onError===>" + e);

							}
						});

	}

	protected void onSaveInstanceState(Bundle outState) {
		Logger.d(TAG, "onSaveInstanceState,and the mContext is==>" + mContext);
		super.onSaveInstanceState(outState);

		mFragmentManager.putFragment(outState, "mContext", mContext);

	}

	/**
	 * 
	 * 去获得有用户后的界面
	 * */
	public void GetClientInfo() {

		initView();

	}

	/**
	 * 
	 * 切换模块的内容
	 * 
	 * @param fragment
	 */
	Fragment SetPassFra = new SetPasswordFragment();

	public void switchContent(Fragment fragment, String FragmentTitle,
			Boolean isAdd) {
		Logger.d(TAG, "switchContent");
		if (Configure.PAGER == 0) {
			text_nav_Chose.setText(Configure.location);
		} else {

			text_nav_Chose.setText(FragmentTitle);
		}

		layout_City.setVisibility(view.GONE);
		text_nav_Chose.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		text_nav_Chose.setOnClickListener(this);
		// btn_Return.setVisibility(View.VISIBLE);
		// if (null == mFragmentManager) {
		// mFragmentManager = getSupportFragmentManager();
		// }
		mContext = fragment;
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();

		fragmentTransaction

		.replace(R.id.body_layout, fragment).setTransition(
				FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		if (!isAdd) {
			Logger.d(TAG, "应该被销毁");
		} else {

			fragmentTransaction.addToBackStack(null);
			Logger.d(TAG, "没有被销毁");
		}

		fragmentTransaction.commitAllowingStateLoss();

	}

	public void DeviceDisplayMetrics() {
		DisplayMetrics dm = new DisplayMetrics();
		Display display = getWindowManager().getDefaultDisplay();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		Logger.d(TAG, "the width is===>" + width + "===and the height is===>"
				+ height + "==and the display is==>" + display.getHeight()
				+ "===" + display.getWidth());
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		if (null != timer) {
			timer.cancel();

		}
		super.onStop();
	};

	@Override
	protected void onDestroy() {
		if (null != timer) {
			timer.cancel();
		}
		super.onDestroy();
	}

	/**
	 * 
	 * 去得到用户的地理位置
	 * */
	BaiDuGetLocation getLocation = null;
	String location;

	public void GetLocation() {
		getLocation = new BaiDuGetLocation(con);
		location = getLocation.GetLocation();
		if (null == location) {
			location = "北京";
		}
		// System.out.println("the loacation is ====>" + StaticVariable.location
		// + "----the location is===" + location);
	}

	/**
	 * 初始化所有控件
	 * */
	private void initView() {
		body_Layout = (FrameLayout) findViewById(R.id.body_layout);
		text_nav_Chose = (TextView) findViewById(R.id.text_nav_title);
		text_nav_Chose.setText(location);
		btn_Return = (Button) findViewById(R.id.btn_return);
		btn_logo = (Button) findViewById(R.id.btn_logo);
		btn_Barrage = (Button) findViewById(R.id.btn_barrage);
		btn_Remind = (Button) findViewById(R.id.btn_remind);
		btn_Download = (Button) findViewById(R.id.btn_download);
		btn_Myself = (Button) findViewById(R.id.btn_myself);
		btn_Main = (Button) findViewById(R.id.btn_main);
		text_nav_Chose.setOnClickListener(this);
		btn_Barrage.setOnClickListener(this);
		btn_Remind.setOnClickListener(this);
		btn_Download.setOnClickListener(this);
		btn_Myself.setOnClickListener(this);
		btn_Main.setOnClickListener(this);
		btn_Return.setOnClickListener(this);
		text_nav_Chose.setOnClickListener(this);
		if (text_nav_Chose.getText().toString().trim().equals("北京")) {
			text_nav_Chose.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.icon_more, 0);
		} else {

			text_nav_Chose.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		}
		listview_city = (ListView) findViewById(R.id.listview_city);
		layout_City = (LinearLayout) findViewById(R.id.layout_city);
		// initAdapter();
		ToConnectWIFI();

	}

	public void ToConnectWIFI() {

		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		openWifi();
		list = wifiManager.getScanResults();
		for (int i = 0; i < list.size(); i++) {
			ScanResult scanResult = list.get(i);
			if (scanResult.SSID.indexOf(Configure.WIFISSID) != -1) {
				ConnectInfo(Configure.WIFISSID, Configure.SSIDPWd);
				break;
			}
		}

	}

	/**
	 * 打开WIFI
	 */
	private void openWifi() {
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}

	}

	static IConnectInfo connect_info = null;

	private WifiManager wifiManager;
	List<ScanResult> list;

	public void ConnectInfo(String ssid, String pwd) {

		connect_info = new IConnectInfo() {

			@Override
			public void onConnectClick(String SSID, String pwd,
					WifiCipherType mType) {

				WifiUtil.addNetWork(
						WifiUtil.createWifiConfig(SSID, pwd, mType), con);

			}
		};
		if (connect_info != null) {
			connect_info.onConnectClick(ssid, pwd,
					WifiUtil.getWifiCipher("[WPA2-EAP-CCMP][ESS]"));

		}
		handler.sendEmptyMessageDelayed(GETWIFI, 6000);
		// TOJudgeUserExist();
	}

	int version;

	public void UpdateVersion() {

		try {
			version = getVersionName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("the version is===>" + version);
		GetAllAppVersionRequest gaavrRequest = new GetAllAppVersionRequest(
				BaseActivity.this, version);
		new com.ky.request.HttpHomeLoadDataTask(BaseActivity.this, getVersion,
				false, "http://www/baidu.com", true).execute(gaavrRequest);

	}

	IUpdateData getVersion = new IUpdateData() {

		@Override
		public void updateUi(Object o) {
			GetAllAppVersionResponse allversion = new GetAllAppVersionResponse();
			allversion.paseRespones(o.toString());
			allversion.dateInfo.version = 3;
			if (allversion.dateInfo.version > Configure.localVersion) {
				showUpdataDialog();

			} else {

			}

		}

		@Override
		public void handleErrorData(String info) {
			System.out.println("handleerrror");
			// 待处理
			Message msg = new Message();
			msg.what = GET_UNDATAINFO_ERROR;
			handler.sendMessage(msg);
		}
	};

	public void initAdapter() {
		// final ArrayList<String> mList = new ArrayList<String>();
		// for (int i = 0; i < strCity.length; i++) {
		// mList.add(strCity[i]);
		//
		// }
		// CityAdapter adapter = new CityAdapter(con, mList);
		// listview_city.setAdapter(adapter);
		// listview_city.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> adapterview,
		// View converview, int position, long id) {
		// text_nav_Chose.setText(mList.get(position).toString());
		// layout_City.setVisibility(view.GONE);
		// }
		// });
		// ;

	}

	ArrayList<String> type_Name = null;
	ArrayList<Object> img = null;

	java.util.Timer timer;

	@Override
	protected void onResume() {
		if (Configure.ISOUTNET) {
			SwitchBtn();
			MainActivity.layout_wifi_appear.setVisibility(View.GONE);
			btn_Barrage.setVisibility(View.GONE);
			btn_Main.setVisibility(View.VISIBLE);
		} else {

		}
		timer = new java.util.Timer();
		// // 启动计时器
		timer.schedule(new Job(), 100, 5 * 1000);
		UpdateVersion();
		super.onResume();
	}

	class Job extends TimerTask {

		@Override
		public void run() {

			// 判断网络
			if (NetProperty.getAPNType(BaseActivity.this) != -1) {
				// 有网络
				netErrorHandler.sendEmptyMessage(SUCCESS);

			} else {

				netErrorHandler.sendEmptyMessage(FAILED);

			}
			DeviceDisplayMetrics();
			DeviceInfo.DeviceDisplayMetrics(con);
		}

	}

	public void setView(View v) {
		body_Layout.addView(v);
		setContentView(view);
	}

	public static final int SUCCESS = 0x00000;
	public static final int FAILED = 0x00001;
	Handler netErrorHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case SUCCESS:
				// text_nav_Chose.setText(location);
				String ssid = NetProperty.getSSID(BaseActivity.this);
				// if (ssid.indexOf(WIFISSID) != -1) {
				if (ssid.indexOf("sap37") != -1) {
					MainActivity.layout_wifi_appear.setVisibility(View.VISIBLE);
					Configure.ISOUTNET = false;
					btn_Barrage.setVisibility(View.VISIBLE);
					btn_Main.setVisibility(View.GONE);
					if (Configure.PAGER == 4) {
						System.out.println("去改变我的页面数据");
						CheckDB();
					} else if (Configure.PAGER == 0) {
					}
				} else {
					Configure.ISOUTNET = true;
					MainActivity.layout_wifi_appear.setVisibility(View.GONE);
					btn_Barrage.setVisibility(View.GONE);
					btn_Main.setVisibility(View.VISIBLE);
					if (Configure.PAGER == 0) {
						/**
						 * 
						 * 如果当前页面是首页的页面，我们才显示该显示的
						 * */
						SwitchBtn();
						// GetFoucs();
					} else if (Configure.PAGER == 4) {
						System.out.println("去改变我的页面数据");
						CheckDB();
					} else {
						System.out
								.println("当前页面的pager是====》" + Configure.PAGER);
						DefaultBtn();
					}

				}

				break;
			case FAILED:
				Configure.ISOUTNET = true;
				if (isShow == 0) {
					showExitDialog();
				} else {

				}

				break;
			default:
				break;
			}
		};
	};

	public void GetFoucs() {

		MainActivity.btn_news.setFocusable(true);
		MainActivity.btn_news.setFocusableInTouchMode(true);
		MainActivity.btn_news.requestFocus();
	}

	/* 这是判断dialog是否显示,0代表显示，1代表没有显示 */

	public static int isShow = 0;

	public void showExitDialog() {
		isShow = 1;
		AlertDialog.Builder builder = new Builder(BaseActivity.this);
		builder.setMessage("请检查您的网络连接");
		builder.setTitle("提示").setPositiveButton("退出",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						System.exit(0);
						isShow = 0;
					}
				});
		builder.setNegativeButton("继续", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ShowDownlad();
				isShow = 0;
			}
		});
		builder.create().show();
	}

	public void ShowDownlad() {
		Intent intent = new Intent();
		intent.setClass(this, DownloadFragment.class);
		intent.putExtra("fromwhere", "base");
		startActivity(intent);

		btn_Barrage.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_barrage_default, 0, 0);
		btn_Remind.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_alert_default, 0, 0);
		btn_Download.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_base_down, 0, 0);
		btn_Myself.setCompoundDrawablesWithIntrinsicBounds(0,

		R.drawable.icon_my_default, 0, 0);
		btn_Main.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_base_main, 0, 0);
		/* 修改获得焦点的颜色 */
		btn_Barrage.setTextColor(0xFF000000);
		btn_Remind.setTextColor(0xFF000000);
		btn_Download.setTextColor(0xFFFFFFFF);
		btn_Myself.setTextColor(0xFF000000);
		btn_Main.setTextColor(0xFF000000);
	}

	public void TOToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	Boolean count = true;

	@Override
	public void onClick(View v) {
		Fragment newContent;
		switch (v.getId()) {

		case R.id.text_nav_title:
			if (text_nav_Chose.getText().toString().trim().equals("四川")) {
				if (count) {
					Logger.d(TAG, "下拉框应该出来");
					layout_City.setVisibility(view.VISIBLE);
					// count = false;

				} else {
					layout_City.setVisibility(view.GONE);
					// count = true;
				}

			} else if (text_nav_Chose.getText().toString().trim().equals("添加")) {

				newContent = new AddEventFragment();
				switchContent(newContent, "添加", true);

			} else {

				Logger.d(TAG, "下我不是城市");
				layout_City.setVisibility(view.GONE);
			}

			break;
		case R.id.btn_barrage:
			startActivity(new Intent().setClass(this, BarrageFragment.class));
			this.finish();
			break;
		case R.id.btn_main:
			newContent = new MainActivity();

			switchContent(newContent, "首页", false);
			SwitchBtn();
			break;

		case R.id.btn_remind:

			newContent = new RemindMeFragment();
			switchContent(newContent, "添加", false);
			btn_Barrage.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_barrage_default, 0, 0);
			btn_Remind.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_base_alert, 0, 0);
			btn_Download.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_base_default, 0, 0);
			btn_Myself.setCompoundDrawablesWithIntrinsicBounds(0,

			R.drawable.icon_my_default, 0, 0);
			btn_Main.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_base_main, 0, 0);
			/* 修改获得焦点的颜色 */
			btn_Barrage.setTextColor(0xFF000000);
			btn_Remind.setTextColor(0xFFFFFFFF);
			btn_Download.setTextColor(0xFF000000);
			btn_Myself.setTextColor(0xFF000000);
			btn_Main.setTextColor(0xFF000000);
			break;
		case R.id.btn_download:
			Intent intent = new Intent(this, DownloadListActivity.class);
			intent.putExtra("fromWhere", -1);
			startActivity(intent);
			String path = Configure.VEDIOTEST;
			this.finish();
			// System.out.println(Environment.getExternalStorageState() +
			// "------"
			// + Environment.MEDIA_MOUNTED);
			//
			// if (Environment.getExternalStorageState().equals(
			// Environment.MEDIA_MOUNTED)) {
			// Intent intent = new Intent();
			// intent.setClass(this, DownloadFragment.class);
			// intent.putExtra("fromwhere", "base");
			// intent.putExtra("path", path);
			// startActivity(intent);
			// finish();
			//
			// } else {
			// // 显示SDCard错误信息
			// Toast.makeText(this, R.string.sdcarderror, 1).show();
			// }
			btn_Barrage.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_barrage_default, 0, 0);
			btn_Remind.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_alert_default, 0, 0);
			btn_Download.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_base_down, 0, 0);
			btn_Myself.setCompoundDrawablesWithIntrinsicBounds(0,

			R.drawable.icon_my_default, 0, 0);
			btn_Main.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_base_main, 0, 0);
			/* 修改获得焦点的颜色 */
			btn_Barrage.setTextColor(0xFF000000);
			btn_Remind.setTextColor(0xFF000000);
			btn_Download.setTextColor(0xFFFFFFFF);
			btn_Myself.setTextColor(0xFF000000);
			btn_Main.setTextColor(0xFF000000);
			break;
		case R.id.btn_myself:
			newContent = new MySelfOutFragment();
			switchContent(newContent, "登录", false);
			btn_Barrage.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_barrage_default, 0, 0);
			btn_Remind.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_alert_default, 0, 0);
			btn_Download.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_base_default, 0, 0);
			btn_Myself.setCompoundDrawablesWithIntrinsicBounds(0,

			R.drawable.icon_base_mycenter, 0, 0);
			btn_Main.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_base_main, 0, 0);
			/* 修改获得焦点的颜色 */
			btn_Barrage.setTextColor(0xFF000000);
			btn_Remind.setTextColor(0xFF000000);
			btn_Download.setTextColor(0xFF000000);
			btn_Myself.setTextColor(0xFFFFFFFF);
			btn_Main.setTextColor(0xFF000000);

			break;
		case R.id.btn_return:
			if (Configure.PAGER == 1 || Configure.PAGER == 2
					|| Configure.PAGER == 3 || Configure.PAGER == 4) {
				switchContent(new MainActivity(), "首页", false);
			} else {
				if (null == mFragmentManager) {
					mFragmentManager = getSupportFragmentManager();
				} else {
					mFragmentManager.popBackStack();
				}
				DefaultBtn();
			}
			text_nav_Chose.invalidate();
			break;

		default:
			break;
		}

	}

	public void SwitchBtn() {

		btn_Main.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_main_default, 0, 0);
		btn_Remind.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_alert_default, 0, 0);
		btn_Download.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_base_default, 0, 0);
		btn_Myself.setCompoundDrawablesWithIntrinsicBounds(0,

		R.drawable.icon_my_default, 0, 0);
		btn_Barrage.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_barrage_default, 0, 0);
		/* 修改获得焦点的颜色 */
		btn_Barrage.setTextColor(0xFF000000);
		btn_Remind.setTextColor(0xFF000000);
		btn_Download.setTextColor(0xFF000000);
		btn_Myself.setTextColor(0xFF000000);
		btn_Main.setTextColor(0xFFFFFFFF);
	}

	/**
	 * 什么按钮也不进行选中
	 * */
	public void DefaultBtn() {

		btn_Main.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_base_main, 0, 0);
		btn_Remind.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_alert_default, 0, 0);
		btn_Download.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_base_default, 0, 0);
		btn_Myself.setCompoundDrawablesWithIntrinsicBounds(0,

		R.drawable.icon_my_default, 0, 0);
		btn_Barrage.setCompoundDrawablesWithIntrinsicBounds(0,
				R.drawable.icon_barrage_default, 0, 0);
		/* 修改获得焦点的颜色 */
		btn_Barrage.setTextColor(0xFF000000);
		btn_Remind.setTextColor(0xFF000000);
		btn_Download.setTextColor(0xFF000000);
		btn_Myself.setTextColor(0xFF000000);
		btn_Main.setTextColor(0xFF000000);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (Configure.PAGER == 1 || Configure.PAGER == 2
					|| Configure.PAGER == 3 || Configure.PAGER == 4
					|| Configure.PAGER == 5) {
				Logger.d(TAG, "点击返回键");
				switchContent(new MainActivity(), "首页", false);
				return false;
			} else if (Configure.PAGER == 0) {

				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
				}
				return true;
			} else {

				DefaultBtn();
			}

		}
		return super.onKeyDown(keyCode, event);

	}

	private long exitTime = 0;

	String username;
	int point;
	IsLoginDB isLogin = null;

	public void CheckDB() {
		if (null == isLogin) {
			isLogin = new IsLoginDB(this);
		}

		Cursor cursor = isLogin.select();
		if (cursor.moveToFirst()) {
			Logger.d(TAG, "当前由用户");
			username = cursor.getString(cursor
					.getColumnIndex(isLogin.u_UserName));
			point = cursor.getInt(cursor.getColumnIndex(isLogin.u_Point));
			if (null == username || username.equals("")) {
				MySelfOutFragment.btn_Login.setText("立即登录");
				MySelfOutFragment.btn_exit.setVisibility(View.GONE);
			} else {
				MySelfOutFragment.btn_Login.setText(username);
				MySelfOutFragment.btn_exit.setVisibility(View.VISIBLE);
			}

		} else {
			Logger.d(TAG, "当前没用户");
			MySelfOutFragment.btn_Login.setText("立即登录");
			MySelfOutFragment.btn_exit.setVisibility(View.GONE);

		}
		Logger.d(TAG,
				"the login text is==>" + MySelfOutFragment.btn_Login.getText());
	}

	/*
	 * 
	 * 弹出对话框通知用户更新程序
	 * 
	 * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
	 * 3.通过builder 创建一个对话框 4.对话框show()出来
	 */
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(this);
		builer.setTitle("版本升级");
		builer.setMessage("检测到最新版本，请及时更新");
		// 当点确定按钮时从服务器上下载 新的apk 然后安装
		builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "下载apk,更新");
				// downLoadApk();
				String updaturl = "http://www.tvapk.com/android/vod/10736.htm/youKXL.apk";
				downloadapk("update" + Configure.getExtensionName(updaturl),
						updaturl);

			}
		});
		// 当点取消按钮时进行登录
		builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				LoginMain();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	// /*
	// * 从服务器中下载APK
	// */
	// protected void downLoadApk() {
	// final ProgressDialog pd; // 进度条对话框
	// pd = new ProgressDialog(this);
	// pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	// pd.setMessage("正在下载更新");
	// pd.show();
	// new Thread() {
	// @Override
	// public void run() {
	// try {
	// File file = getFileFromServer(
	// "http://192.168.1.187:8080/mobilesafe.apk", pd);
	// sleep(3000);
	// installApk(file);
	// pd.dismiss(); // 结束掉进度条对话框
	// } catch (Exception e) {
	// Message msg = new Message();
	// msg.what = DOWN_ERROR;
	// handler.sendMessage(msg);
	// e.printStackTrace();
	// }
	// }
	// }.start();
	// }

	public static final int DOWN_ERROR = 0x000011;
	public static final int GET_UNDATAINFO_ERROR = 0x000022;
	public static final int UPDATA_CLIENT = 0x000033;

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

	/*
	 * 进入程序的主界面
	 */
	private void LoginMain() {
		// Intent intent = new Intent(this, MainActivity.class);
		// startActivity(intent);
		// // 结束掉当前的activity
		// this.finish();
	}

	/*
	 * 获取当前程序的版本号
	 */
	private int getVersionName() {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			Configure.localVersion = packInfo.versionCode;
			int currentapiVersion = android.os.Build.VERSION.SDK_INT;
			Logger.i(TAG, "当前获取的版本code是=====>" + Configure.localVersion
					+ "----当前的系统版本的API是====》" + currentapiVersion
					+ "---当前的系统的release的系统版本==="
					+ android.os.Build.VERSION.RELEASE + "=====当前系统的sdk版本是==="
					+ android.os.Build.VERSION.SDK + "===当前系统的手机的型号是===="
					+ android.os.Build.MODEL);
		} catch (NameNotFoundException e) {

			e.printStackTrace();
		}
		return Configure.localVersion;

	}

	//
	// /**
	// * 从服务器下载apk:
	// * */
	// public static File getFileFromServer(String path, ProgressDialog pd)
	// throws Exception {
	// // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
	// if (Environment.getExternalStorageState().equals(
	// Environment.MEDIA_MOUNTED)) {
	// URL url = new URL(path);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setConnectTimeout(5000);
	// // 获取到文件的大小
	// pd.setMax(conn.getContentLength());
	// InputStream is = conn.getInputStream();
	// File file = new File(Environment.getExternalStorageDirectory(),
	// "updata.apk");
	// FileOutputStream fos = new FileOutputStream(file);
	// BufferedInputStream bis = new BufferedInputStream(is);
	// byte[] buffer = new byte[1024];
	// int len;
	// int total = 0;
	// while ((len = bis.read(buffer)) != -1) {
	// fos.write(buffer, 0, len);
	// total += len;
	// // 获取当前下载量
	// pd.setProgress(total);
	// }
	// fos.close();
	// bis.close();
	// is.close();
	// return file;
	// } else {
	// return null;
	// }
	// }
	public String Token = "";
	public static final int DOPOST = 0x000001;
	public static Boolean isHap = false;
	public static final int GETWIFI = 0x000002;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_CLIENT:
				// 对话框通知用户升级程序
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				// 服务器超时
				Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1)
						.show();
				LoginMain();
				break;
			case DOWN_ERROR:
				// 下载apk失败
				Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
				LoginMain();
				break;
			case DOPOST:
				if (isHap) {

				} else {
					if (Token.equals("error")) {
						Toast.makeText(BaseActivity.this, "抱歉，网络路上有点堵！",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(BaseActivity.this, "恭喜，外网已经为您打开！",
								Toast.LENGTH_SHORT).show();
					}
					isHap = true;

				}

				break;
			case GETWIFI:

				TOJudgeUserExist();
				break;
			}
		}
	};
	/**
	 * 下载
	 * 
	 * @param url
	 */
	String target = "/mnt/sdcard/";
	FinalHttp finalHttp = new FinalHttp();
	HttpHandler httpHandler;
	// CommonToast toast;
	String extensionName;

	public void downloadapk(final String fileName, final String fileUrl) {
		Logger.log("currentFileName:" + fileName + ",fileUrl:" + fileUrl);
		// 判断文件是否存在(如果存在则删除)
		try {
			boolean isFileExist = FileUtils.delectFile(target + fileName);
			Logger.log("isFileExist:" + isFileExist);
		} catch (IOException e) {
			Logger.log("deleteFoder failed:" + e.getLocalizedMessage());
		}

		httpHandler = finalHttp.download(fileUrl, target + fileName, true,
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
						Logger.log(t.getAbsolutePath() + "======<><><><><><>");

						// 如果是apk的话就执行安装
						if (".apk".equals(extensionName)) {
							// 强制安装
							boolean isInstall = LauncherBiz
									.execCommand("system/bin/pm install -r "
											+ "mnt/sdcard/" + fileName);

							Logger.log("isInstall:" + isInstall);
						} else {
							Logger.log("下载文件不是apk文件");
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						Logger.log("下载apk失败");
						Logger.log(strMsg + "=====================");
					}

				});
	}
}
