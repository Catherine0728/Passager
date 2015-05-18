package com.ky.mainactivity;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.PSource;

import com.android.listener.IConnectInfo;
import com.android.network.IManagerNetwork;
import com.android.network.ManagerNetwork;
import com.android.widget.StringTools;
import com.ky.adapter.GameLeftAdapter;
import com.ky.adapter.GameListAdapter;
import com.ky.adapter.MainNewsListAdapter;
import com.ky.adapter.MainNovelAdapter;
import com.ky.adapter.MainVideoAdapter;
import com.ky.beaninfo.ColumnGameInfo;
import com.ky.beaninfo.ColumnNewsInfo;
import com.ky.beaninfo.ColumnNovelInfo;
import com.ky.beaninfo.ColumnTypeInfo;
import com.ky.beaninfo.ColumnVedioInfo;
import com.ky.db.IsLoginDB;
import com.ky.passenger.R;
import com.ky.request.GetContentInfoRequest;
import com.ky.request.GetIndexRequest;
import com.ky.request.HttpHomeLoadDataTask;
import com.ky.request.ServiceCertificationRequest;
import com.ky.response.GetIndextResponse;
import com.ky.response.ServiceCertificationResponse;
import com.ky.utills.DeviceCheck;
import com.ky.utills.Configure;
import com.ky.utills.Utility;
import com.lhl.callback.IUpdateData;
import com.lhl.db.DowningVideoDB;
import com.lhl.net.ACache;
import com.lhl.net.BitmapWorkerTask;
import com.lhl.net.BitmapWorkerTask.ImageCallBack;
import com.lhl.net.BitmapWorkerTask.NewImageCallBack;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.sample.download.DownloadManager;
import com.lidroid.xutils.sample.download.DownloadService;
import com.lidroid.xutils.util.LogUtils;
import com.meiya.hxj.wifi.WifiUtil;
import com.meiya.hxj.wifi.WifiUtil.WifiCipherType;
import com.redbull.log.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 首页
 * 
 * @author Catherine.Brain
 * */
public class MainActivity extends Fragment implements OnClickListener {
	public static String TAG = "MainActivity";
	View view = null;
	public static LinearLayout layout_wifi_appear, layout_four_fouse;
	Button btn_watch_moive, btn_listen_moive, btn_download_moive,
			btn_play_barrage;
	public static Button btn_news, btn_vedio, btn_novel, btn_game;
	LinearLayout layout_news, layout_video, layout_neovl, layout_game;
	Context mContext;
	public static String FRAGMENTNAME = "北京";
	/* 这是新闻的控件声明 */
	ImageView news_Image_One, news_Image_Two;
	TextView news_Text_One, news_Text_Two;
	GridView news_Grid;

	/* 视频的控件声明 */
	ImageView vedio_Image_One, vedio_Image_Two;
	TextView vedio_text_one, vedio_text_two;
	RelativeLayout relayout_vedio_one, relayout_vedio_two;
	/* 小说的控件声明 */
	GridView novel_Grid;
	static Context con;
	/* 游戏的控件声明 */
	ListView game_ListView, game_leftView;
	LinearLayout layout_broadcast;
	Button btn_broadcast;

	Fragment newContent = null;
	BaseActivity base = null;

	// cache

	String pageBgUrl;

	ACache cache;
	private Context mAppContext;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Logger.d(TAG, "onCreateView");
		if (null == view) {
			view = LayoutInflater.from(getActivity()).inflate(R.layout.main,
					null);
		}
		ViewGroup p = (ViewGroup) view.getParent();
		if (p != null) {
			p.removeView(view);
		}
		// setContentView(R.layout.main);
		// myThread.start();
		mContext = this.getActivity();
		base = (BaseActivity) mContext;

		cache = ACache.get(getActivity());

		pageBgUrl = cache.getAsString("pageBg");
		con = getActivity();
		// ToConnectWIFI();
		LoadImageBack();
		initView();
		// GetCity();
		UpdateData();

		initNews();
		initVedio();
		initNovel();
		initGame();
		getFoucs();
		mAppContext = inflater.getContext().getApplicationContext();
		downloadManager = DownloadService.getDownloadManager(mAppContext);
		return view;
	}

	/**
	 * 
	 * 去获得当前的城市名字
	 * */
	GetCity getCity = null;

	public void GetCity() {
		getCity = new GetCity(getActivity());

		String cityName = getCity.getCity();
		Toast.makeText(getActivity(), "我当前的位置是===>" + cityName,
				Toast.LENGTH_SHORT).show();
	}

	ImageView[] posts = { news_Image_One, news_Image_Two, vedio_Image_One,
			vedio_Image_Two };

	public void LoadImageBack() {
		posts[0] = (ImageView) view.findViewById(R.id.news_image_one);
		posts[1] = (ImageView) view.findViewById(R.id.news_image_two);
		posts[2] = (ImageView) view.findViewById(R.id.vedio_image_one);
		posts[3] = (ImageView) view.findViewById(R.id.vedio_image_two);
		for (int j = 0; j < imageStr.length; j++) {

			new BitmapWorkerTask(getActivity(), posts[j], j, true, true,
					new NewImageCallBack() {

						@Override
						public void post(Bitmap bitmap, int imgNum) {
							System.out.println("the imgNum is===>" + imgNum
									+ "and the bitmap is-=====>" + bitmap);
							posts[imgNum].setImageBitmap(bitmap);
						}
					}).execute(imageStr[j]);
		}
	}

	/**
	 * 
	 * 去连接到固定的WIFI 无线：MTALL-000F11 密码：12345678
	 * */
	String SSID = "MTALL-000F11";
	String SSIDPWd = "12345678";

	public void ToConnectWIFI() {
		ConnectInfo(SSID, SSIDPWd);

	}

	static IConnectInfo connect_info = null;

	public void ConnectInfo(String ssid, String pwd) {

		connect_info = new IConnectInfo() {

			@Override
			public void onConnectClick(String SSID, String pwd,
					WifiCipherType mType) {

				WifiUtil.addNetWork(
						WifiUtil.createWifiConfig(SSID, pwd, mType), con);
				TOJudgeUserExist();
			}
		};
		if (connect_info != null) {
			connect_info.onConnectClick(ssid, pwd,
					WifiUtil.getWifiCipher("[WPA2-EAP-CCMP][ESS]"));
		}

	}

	/**
	 * 
	 * 判断当前是否有用户 如果没有，我们就跳转到登录的界面上去，如果有，我们就直接post认证以及获取数据
	 * 
	 * */
	IsLoginDB isLogin = null;

	public void TOJudgeUserExist() {
		if (null == isLogin) {
			isLogin = new IsLoginDB(getActivity());
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
			// startActivity(new Intent().setClass(getActivity(),
			// LoginFragment.class));
		}
	}

	/**
	 * 
	 * 在得到当前已经有用户的时候，我们去得到设备的MAC地址，然后，去请求认证
	 * */
	public void GetMAC() {
		Configure.WIFIMAC = DeviceCheck.getWifiMac(getActivity());
		WifiManager wifiManager = (WifiManager) getActivity().getSystemService(
				getActivity().WIFI_SERVICE);

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		Configure.AndroidID = DeviceCheck.int2IPStr(ipAddress);
		Configure.MAC = DeviceCheck.getRouterMac(getActivity());
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
								myHandler.sendEmptyMessage(DOPOST);
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

		// ServiceCertificationRequest serviceRequest = new
		// ServiceCertificationRequest(
		// getActivity(), StaticVariable.MAC, StaticVariable.AndroidID);

		// new HttpHomeLoadDataTask(
		// getActivity(),
		// RquestCallBack,
		// false,
		// "http://sqwifi.zaitianweb.com/index.php/api/Userauth/noAuthApp/",
		// false).execute(serviceRequest);

	}

	IUpdateData RquestCallBack = new IUpdateData() {

		@Override
		public void updateUi(Object o) {
			Logger.d("updateUi", "server-updateUi");
			ServiceCertificationResponse serviceRespomnse = new ServiceCertificationResponse();
			serviceRespomnse.paseRespones(o.toString());

		}

		@Override
		public void handleErrorData(String info) {
			Logger.d("handleErrorData", "server-" + info);

		}
	};
	/* 定义一个来获得钥匙 */
	public String Token = "";

	public void DoPost(Boolean flags, String Url) {
		ManagerNetwork.getFinalURL(Url, new IManagerNetwork() {
			@Override
			public void onUrlError() {
				Logger.e(TAG, "onUrlError");
			}

			@Override
			public void onSuccessDatas(String str) {
				System.out.println("onSuccessDatas-------and the str is==="
						+ str);
				Token = str;
			}

			@Override
			public void onReadDataTimeOut() {
				Logger.d(TAG, "onReadDataTimeOut");

			}

			public void onError(Exception e) {
				Logger.e(TAG, "onError===>" + e);

			}
		});

	}

	public void getFoucs() {

		btn_news.setFocusable(true);
		btn_news.setFocusableInTouchMode(true);
		btn_news.requestFocus();

	}

	@Override
	public void onStart() {
		Logger.d(TAG, "onStart");
		btn_news.setFocusable(true);
		btn_news.setFocusableInTouchMode(true);
		btn_news.requestFocus();
		super.onStart();
	}

	@Override
	public void onResume() {
		Logger.d(TAG, "onResume");
		btn_news.setFocusable(true);
		btn_news.setFocusableInTouchMode(true);
		btn_news.requestFocus();
		super.onResume();
	}

	private void initView() {
		Configure.PAGER = 0;
		BaseActivity.text_nav_Chose.setText("北京");
		BaseActivity.text_nav_Chose.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.icon_dingwei, 0, 0, 0);
		BaseActivity.btn_Return.setVisibility(View.GONE);
		BaseActivity.btn_logo.setVisibility(View.VISIBLE);
		layout_wifi_appear = (LinearLayout) view
				.findViewById(R.id.layout_wifi_appear);
		layout_four_fouse = (LinearLayout) view
				.findViewById(R.id.layout_four_fouse);

		btn_download_moive = (Button) view
				.findViewById(R.id.btn_download_moive);
		btn_watch_moive = (Button) view.findViewById(R.id.btn_watch_moive);
		btn_listen_moive = (Button) view.findViewById(R.id.btn_listen_moive);
		btn_play_barrage = (Button) view.findViewById(R.id.btn_play_barrage);
		btn_vedio = (Button) view.findViewById(R.id.btn_vedio);
		btn_game = (Button) view.findViewById(R.id.btn_game);

		btn_news = (Button) view.findViewById(R.id.btn_news);
		btn_news.setFocusable(true);
		btn_news.setFocusableInTouchMode(true);
		btn_novel = (Button) view.findViewById(R.id.btn_nevol);
		layout_game = (LinearLayout) view
				.findViewById(R.id.layout_game_download);
		layout_neovl = (LinearLayout) view.findViewById(R.id.layout_nevol);
		layout_news = (LinearLayout) view.findViewById(R.id.layout_news);
		layout_video = (LinearLayout) view.findViewById(R.id.layout_vedio);
		layout_broadcast = (LinearLayout) view
				.findViewById(R.id.layout_broadcast);
		btn_broadcast = (Button) view.findViewById(R.id.btn_broadcast_icon);
		btn_broadcast.setOnClickListener(this);
		layout_game.setOnClickListener(this);
		layout_neovl.setOnClickListener(this);
		layout_news.setOnClickListener(this);
		layout_video.setOnClickListener(this);
		btn_download_moive.setOnClickListener(this);
		btn_watch_moive.setOnClickListener(this);
		btn_listen_moive.setOnClickListener(this);
		btn_play_barrage.setOnClickListener(this);
		btn_vedio.setOnClickListener(this);
		btn_game.setOnClickListener(this);
		btn_news.setOnClickListener(this);
		btn_novel.setOnClickListener(this);
		if (Configure.ISOUTNET) {
			Logger.d(TAG, " 离线状态");
			/* 离线状态 */
			layout_wifi_appear.setVisibility(View.GONE);
			BaseActivity.btn_Barrage.setVisibility(View.GONE);
			BaseActivity.btn_Main.setVisibility(View.VISIBLE);
		} else {
			Logger.d(TAG, " 在线状态");
			layout_wifi_appear.setVisibility(View.VISIBLE);
			BaseActivity.btn_Barrage.setVisibility(View.VISIBLE);
			BaseActivity.btn_Main.setVisibility(View.GONE);
			BaseActivity.btn_Barrage.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_barrage_default, 0, 0);
			BaseActivity.btn_Remind.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_alert_default, 0, 0);
			BaseActivity.btn_Download.setCompoundDrawablesWithIntrinsicBounds(
					0, R.drawable.icon_base_default, 0, 0);
			BaseActivity.btn_Myself.setCompoundDrawablesWithIntrinsicBounds(0,

			R.drawable.icon_my_default, 0, 0);
			BaseActivity.btn_Main.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.icon_base_main, 0, 0);
			BaseActivity.btn_Barrage.setTextColor(0xFF000000);
			BaseActivity.btn_Remind.setTextColor(0xFF000000);
			BaseActivity.btn_Download.setTextColor(0xFF000000);
			BaseActivity.btn_Myself.setTextColor(0xFF000000);
			BaseActivity.btn_Main.setTextColor(0xFF000000);

		}
	}

	private String[] imageStr = {

			"http://c.hiphotos.baidu.com/image/w%3D230/sign=0cc95c1fbb389b5038ffe751b535e5f1/18d8bc3eb13533fab893d52faad3fd1f41345b8d.jpg",
			"http://a.hiphotos.baidu.com/image/w%3D230/sign=b6cc0fb1bb014a90813e41be99763971/63d0f703918fa0eca021d40e249759ee3c6ddbcb.jpg",
			"http://b.hiphotos.baidu.com/image/w%3D230/sign=4507c505369b033b2c88fbd925ce3620/203fb80e7bec54e7a7fcc21fbb389b504fc26a9b.jpg",
			"http://a.hiphotos.baidu.com/image/w%3D230/sign=3147a323b01bb0518f24b42b067ada77/279759ee3d6d55fb2acbd9276e224f4a20a4dda6.jpg" };

	public void initNews() {

		for (int i = 0; i < imageStr.length; i++) {
			// new BitmapThread(imageStr[i]).start();
		}

		news_Text_One = (TextView) view.findViewById(R.id.news_text_one);
		news_Text_Two = (TextView) view.findViewById(R.id.news_text_two);

	}

	GridView vedio_Grid;

	public void initVedio() {
		vedio_Grid = (GridView) view.findViewById(R.id.main_video_grid);
		v_List = new ArrayList<Map<String, Object>>();

		vedio_text_one = (TextView) view.findViewById(R.id.vedio_text_one);
		vedio_text_two = (TextView) view.findViewById(R.id.vedio_text_two);
		relayout_vedio_one = (RelativeLayout) view
				.findViewById(R.id.relayout_vedio_one);
		relayout_vedio_two = (RelativeLayout) view
				.findViewById(R.id.relayout_vedio_two);

	}

	ArrayList<Map<String, Object>> n_List = null;
	ArrayList<Map<String, Object>> v_List = null;

	public void initNovel() {
		novel_Grid = (GridView) view.findViewById(R.id.novel_grid);
		n_List = new ArrayList<Map<String, Object>>();

	}

	ArrayList<Map<String, Object>> g_List = null;

	public void initGame() {
		game_ListView = (ListView) view.findViewById(R.id.game_list);
		// game_leftView = (ListView) view.findViewById(R.id.game_left_list);
		g_List = new ArrayList<Map<String, Object>>();
		game_Left = new ArrayList<Map<String, Object>>();

	}

	public void UpdateData() {
		btn_news.setFocusable(true);
		btn_news.setFocusableInTouchMode(true);
		GetColumnInfo();
		/*
		 * 发送消息，获取数据
		 */
		myHandler.sendEmptyMessage(1);
		if (!StringTools.isNullOrEmpty(pageBgUrl)) {
			Logger.d(TAG, "UpdateData");
		}
		// new BitmapWorkerTask(this, layout_news, true, true, pageBgcallback)
		// .execute(new String[] { pageBgUrl });
		// }
	}

	public void GetColumnInfo() {

		GetIndexRequest index_Re = new GetIndexRequest(getActivity(), 1);
		new HttpHomeLoadDataTask(getActivity(), ColumnCallBack, true, "", false)
				.execute(index_Re);
	}

	int novel_ID, vedio_ID, news_ID, game_ID;
	ArrayList<ColumnNovelInfo> novelList = new ArrayList<ColumnNovelInfo>();
	ArrayList<ColumnNewsInfo> newsList = new ArrayList<ColumnNewsInfo>();
	ArrayList<ColumnGameInfo> gameList = new ArrayList<ColumnGameInfo>();
	ArrayList<ColumnVedioInfo> vedioList = new ArrayList<ColumnVedioInfo>();
	ArrayList<ColumnTypeInfo> typeList = new ArrayList<ColumnTypeInfo>();
	IUpdateData ColumnCallBack = new IUpdateData() {
		ColumnNovelInfo novel_Info;
		ColumnGameInfo game_Info;
		ColumnVedioInfo vedio_Info;
		ColumnNewsInfo news_Info;
		ColumnTypeInfo type_Info;

		@Override
		public void updateUi(Object o) {
			GetIndextResponse index_Response = new GetIndextResponse();
			index_Response.paseRespones(o.toString());
			typeList.clear();
			novelList.clear();
			gameList.clear();
			vedioList.clear();
			newsList.clear();
			for (int i = 0; i < index_Response.list.size(); i++) {
				type_Info = new ColumnTypeInfo();
				type_Info.id = index_Response.list.get(i).id;
				type_Info.count = index_Response.list.get(i).count;
				type_Info.type = index_Response.list.get(i).type;
				type_Info.url = index_Response.list.get(i).url;
				if (type_Info.type.equals("video")) {
					for (int j = 0; j < index_Response.vedioList.size(); j++) {
						vedio_Info = new ColumnVedioInfo();
						vedio_Info.id = index_Response.vedioList.get(j).id;
						vedio_Info.title = index_Response.vedioList.get(j).title;
						vedio_Info.category_id = index_Response.vedioList
								.get(j).category_id;
						vedio_Info.descriptrion = index_Response.vedioList
								.get(j).descriptrion;
						vedio_Info.cover_id = index_Response.vedioList.get(j).cover_id;
						vedio_Info.thumb = index_Response.vedioList.get(j).thumb;
						vedio_Info.url = index_Response.vedioList.get(j).url;
						vedio_Info.type = index_Response.vedioList.get(j).type;
						vedioList.add(vedio_Info);

					}
					myHandler.sendEmptyMessage(VEDIOINFO);

				} else if (type_Info.type.equals("news")) {
					for (int j = 0; j < index_Response.newsList.size(); j++) {
						news_Info = new ColumnNewsInfo();
						news_Info.id = index_Response.newsList.get(j).id;
						news_Info.title = index_Response.newsList.get(j).title;
						news_Info.category_id = index_Response.newsList.get(j).category_id;
						news_Info.descriptrion = index_Response.newsList.get(j).descriptrion;
						news_Info.cover_id = index_Response.newsList.get(j).cover_id;
						news_Info.thumb = index_Response.newsList.get(j).thumb;
						news_Info.url = index_Response.newsList.get(j).url;
						news_Info.type = index_Response.newsList.get(j).type;
						newsList.add(news_Info);
					}
					myHandler.sendEmptyMessage(NEWSINFO);

				} else if (type_Info.type.equals("game")) {
					for (int j = 0; j < index_Response.gameList.size(); j++) {
						game_Info = new ColumnGameInfo();
						game_Info.id = index_Response.gameList.get(j).id;
						game_Info.title = index_Response.gameList.get(j).title;
						game_Info.category_id = index_Response.gameList.get(j).category_id;
						game_Info.descriptrion = index_Response.gameList.get(j).descriptrion;
						game_Info.cover_id = index_Response.gameList.get(j).cover_id;
						game_Info.thumb = index_Response.gameList.get(j).thumb;
						game_Info.url = index_Response.gameList.get(j).url;
						game_Info.type = index_Response.gameList.get(j).type;
						// new BitmapThread(game_Info.thumb).start();
						gameList.add(game_Info);
					}
					myHandler.sendEmptyMessage(GAMEINFO);

				} else if (type_Info.type.equals("novel")) {
					novel_ID = type_Info.id;
					for (int j = 0; j < index_Response.novelList.size(); j++) {
						novel_Info = new ColumnNovelInfo();
						novel_Info.id = index_Response.novelList.get(j).id;
						novel_Info.title = index_Response.novelList.get(j).title;
						novel_Info.category_id = index_Response.novelList
								.get(j).category_id;
						novel_Info.descriptrion = index_Response.novelList
								.get(j).descriptrion;
						novel_Info.cover_id = index_Response.novelList.get(j).cover_id;
						novel_Info.thumb = index_Response.novelList.get(j).thumb;
						novel_Info.url = index_Response.novelList.get(j).url;
						novel_Info.type = index_Response.novelList.get(j).type;
						novelList.add(novel_Info);
					}
					myHandler.sendEmptyMessage(NOVELINFO);
				}
				typeList.add(type_Info);
			}
			initListener();
			Configure.ISGETDATA = true;
		}

		@Override
		public void handleErrorData(String info) {
			Logger.e(TAG, info);
			Configure.ISGETDATA = false;
		}
	};

	public void initListener() {
		Logger.e("initListener", "我在监听里面");

	}

	ImageCallBack pageBgcallback = new ImageCallBack() {

		@Override
		public void post(Bitmap bitmap) {
			Drawable drawable = new BitmapDrawable(bitmap);

		}

	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_listen_moive:
			if (Configure.ISGETDATA) {
				newContent = new ListenMovieFragment();
				base.switchContent(newContent, "现场看电影", true);
			} else {

				showExitDialog();
			}

			break;
		case R.id.btn_watch_moive:
			if (Configure.ISGETDATA) {
				newContent = new WatchMovieFragment();
				base.switchContent(newContent, "现场看电影", true);
			} else {

				showExitDialog();
			}

			break;
		case R.id.btn_download_moive:
			if (Configure.ISGETDATA) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), DownloadFragment.class);
				intent.putExtra("fromwhere", "main");
				startActivity(intent);
				getActivity().finish();
			} else {

				showExitDialog();
			}

			break;
		case R.id.btn_play_barrage:
			if (Configure.ISGETDATA) {
				// newContent = new BarrageFragment();
				// base.switchContent(newContent, "现场看电影", true);
				startActivity(new Intent().setClass(getActivity(),
						BarrageFragment.class));
				getActivity().finish();
			} else {

				showExitDialog();
			}

			break;
		case R.id.btn_vedio:
			if (Configure.ISGETDATA) {
				// newContent = new VedioFragment();
				// base.switchContent(newContent, "视频", true);
				Intent intentVedio = new Intent();
				intentVedio.setClass(getActivity(), VedioActivity.class);
				startActivity(intentVedio);
			} else {

				showExitDialog();
			}

			break;
		case R.id.btn_game:
			// Totoast("游戏");
			game_ListView.setFocusable(true);
			game_ListView.setFocusableInTouchMode(true);
			game_ListView.requestFocus();
			break;
		case R.id.btn_news:
			if (Configure.ISGETDATA) {
				// newContent = new NewsFragment();
				// base.switchContent(newContent, "新闻", true);
				Intent intentNews = new Intent();
				intentNews.setClass(getActivity(), NewsActivity.class);
				startActivity(intentNews);
			} else {

				showExitDialog();
			}

			break;
		case R.id.btn_nevol:
			if (Configure.ISGETDATA) {
				// newContent = new NovelActivity();
				// Bundle bundle = new Bundle();
				// bundle.putInt("id", novel_ID);
				// newContent.setArguments(bundle);
				// base.switchContent(newContent, "小说", true);
				Intent intentNovel = new Intent();
				intentNovel.putExtra("id", novel_ID);
				intentNovel.setClass(getActivity(), NovelActivity.class);
				startActivity(intentNovel);
			} else {

				showExitDialog();
			}

			break;
		case R.id.layout_game_download:
			break;
		case R.id.layout_vedio:
			if (Configure.ISGETDATA) {
				Intent intentVedio = new Intent();
				intentVedio.setClass(getActivity(), VedioActivity.class);
				startActivity(intentVedio);
				// newContent = new VedioFragment();
				// base.switchContent(newContent, "视频", true);
			} else {

				showExitDialog();
			}

			break;
		case R.id.layout_nevol:
			if (Configure.ISGETDATA) {
				// newContent = new NovelActivity();
				// Bundle bundle = new Bundle();
				// bundle.putInt("id", novel_ID);
				// newContent.setArguments(bundle);
				// base.switchContent(newContent, "小说", true);
				Intent intentNovel = new Intent();
				intentNovel.putExtra("id", novel_ID);
				intentNovel.setClass(getActivity(), NovelActivity.class);
				startActivity(intentNovel);
			} else {

				showExitDialog();
			}

			break;
		case R.id.layout_news:
			if (Configure.ISGETDATA) {
				// newContent = new NewsActivity();
				// base.switchContent(newContent, "新闻", true);
				Intent intentNews = new Intent();
				intentNews.setClass(getActivity(), NewsActivity.class);
				startActivity(intentNews);
			} else {

				showExitDialog();
			}

			break;

		default:
			break;
		}

	}

	public static final int NEWSINFO = 0x000001;
	public static final int NOVELINFO = 0x000002;
	public static final int GAMEINFO = 0x000003;
	public static final int VEDIOINFO = 0x000004;
	public static final int DOPOST = 0x000005;

	Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NEWSINFO:
				LoadNews();
				break;
			case NOVELINFO:
				LoadNovel();

				break;
			case GAMEINFO:
				LoadGame();

				break;
			case VEDIOINFO:
				LoadVedio();
				break;
			case DOPOST:
				// ManagerNetwork.getFinalURL(Token);
				// DoPost(false, Token);
				if (Token.equals("error")) {
					Toast.makeText(getActivity(), "抱歉，网络路上有点堵！",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "恭喜，外网已经为您打开！",
							Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}

		};
	};
	int j;

	public void LoadNews() {
		Logger.d(TAG, "LoadNews");
		TextView[] newsTexts = { news_Text_One, news_Text_Two };
		if (newsList.size() == 0) {

		} else {
			for (int i = 0; i < 2; i++) {
				j = i;
				posts[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(mContext, NewDetailActivity.class);
						intent.putExtra("title", newsList.get(j).title);
						intent.putExtra("id", newsList.get(j).id);
						intent.putExtra("type", newsList.get(j).type);
						intent.putExtra("url", newsList.get(j).url);
						intent.putExtra("thumb", newsList.get(j).thumb);
						Logger.d(TAG,
								"id:" + newsList.get(j).id + "--type:"
										+ newsList.get(j).type + "--url:"
										+ newsList.get(j).url);
						startActivity(intent);
					}
				});
			}

			for (int i = 0; i < newsTexts.length; i++) {
				j = i;
				newsTexts[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(mContext, NewDetailActivity.class);
						intent.putExtra("title", newsList.get(j).title);
						intent.putExtra("id", newsList.get(j).id);
						intent.putExtra("type", newsList.get(j).type);
						intent.putExtra("url", newsList.get(j).url);
						intent.putExtra("thumb", newsList.get(j).thumb);
						Logger.d(TAG,
								"id:" + newsList.get(j).id + "--type:"
										+ newsList.get(j).type + "--url:"
										+ newsList.get(j).url);
						startActivity(intent);
					}
				});
			}
		}

		news_Grid = (GridView) view.findViewById(R.id.news_grid);
		ArrayList<Map<String, String>> mList = new ArrayList<Map<String, String>>();
		if (newsList.size() < 3) {
			// int length = newsList.size();
			// if (length == 0) {
			// newsTexts[0].setVisibility(View.GONE);
			// newsTexts[1].setVisibility(View.GONE);
			// newsIamge[0].setVisibility(View.GONE);
			// newsIamge[1].setVisibility(View.GONE);
			// } else if (length == 1) {
			// newsTexts[0].setText(newsList.get(0).title.toString());
			// newsTexts[1].setVisibility(View.INVISIBLE);
			// newsIamge[1].setVisibility(View.INVISIBLE);
			// } else {
			// newsTexts[0].setVisibility(View.VISIBLE);
			// newsTexts[1].setVisibility(View.VISIBLE);
			// newsIamge[0].setVisibility(View.VISIBLE);
			// newsIamge[1].setVisibility(View.VISIBLE);
			for (int i = 0; i < newsList.size(); i++) {
				newsTexts[i].setText(newsList.get(i).title.toString());
				// }
			}
		} else {
			news_Text_One.setText(newsList.get(0).title.toString());
			news_Text_Two.setText(newsList.get(1).title.toString());
			for (int i = 2; i < newsList.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("news_left", newsList.get(i).title.toString());

				mList.add(map);
			}
			MainNewsListAdapter newsAdapter = new MainNewsListAdapter(
					getActivity(), mList);
			news_Grid.setAdapter(newsAdapter);
			Utility.setGridViewHeightBasedOnChildren(news_Grid, 2, 2,
					"loadnews");

		}

		news_Grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertview,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(mContext, NewDetailActivity.class);
				intent.putExtra("title", newsList.get(position + 2).title);
				intent.putExtra("id", newsList.get(position + 2).id);
				intent.putExtra("type", newsList.get(position + 2).type);
				intent.putExtra("url", newsList.get(position + 2).url);
				intent.putExtra("thumb", newsList.get(position + 2).thumb);
				Logger.d(TAG,
						"id:" + newsList.get(position + 2).id + "--type:"
								+ newsList.get(position + 2).type + "--url:"
								+ newsList.get(position + 2).url);
				startActivity(intent);
			}
		});
		Logger.e("LoadNews",
				"the gridview height is===>" + news_Grid.getHeight());
	}

	public void LoadVedio() {
		TextView[] vedioText = { vedio_text_one, vedio_text_two };
		Logger.d(TAG, "LoadVedio");
		v_List.clear();
		int length = vedioList.size();
		if (length > 8) {
			length = 8;
		} else {

		}
		for (int i = 0; i < length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", Configure.image_video_Icon[i % 10]);
			map.put("name", vedioList.get(i).title);
			map.put("id", vedioList.get(i).id);
			map.put("type", vedioList.get(i).type);
			map.put("url", vedioList.get(i).url);
			map.put("thumb", vedioList.get(i).thumb);
			v_List.add(map);
		}
		if (v_List.size() <= 2) {
			if (v_List.size() == 1) {
				Logger.d("LoadVedio", "只有一条数据");
				vedio_text_one.setText(vedioList.get(0).title);
				relayout_vedio_two.setVisibility(View.INVISIBLE);
				vedio_Image_One.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(getActivity(),
								VedioDetailActivity.class);
						intent.putExtra("title", vedioList.get(0).title);
						intent.putExtra("id", vedioList.get(0).id);
						intent.putExtra("type", vedioList.get(0).type);
						intent.putExtra("url", vedioList.get(0).url);
						intent.putExtra("pos", 0);
						intent.putExtra("thumb", vedioList.get(0).thumb);
						Logger.d(TAG,
								"id:" + vedioList.get(0).id + "--type:"
										+ vedioList.get(0).type + "--url:"
										+ vedioList.get(0).url);
						startActivity(intent);
					}
				});
			} else {
				Logger.d("LoadVedio", "只有两条数据");
				vedio_text_one.setText(vedioList.get(0).title);
				vedio_text_two.setText(vedioList.get(1).title);
				for (int i = 0; i < 2; i++) {
					final int j = i;
					posts[i + 2].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							Intent intent = new Intent();
							intent.setClass(getActivity(),
									VedioDetailActivity.class);
							intent.putExtra("title", vedioList.get(j).title);
							intent.putExtra("id", vedioList.get(j).id);
							intent.putExtra("type", vedioList.get(j).type);
							intent.putExtra("url", vedioList.get(j).url);
							intent.putExtra("thumb", vedioList.get(j).thumb);
							intent.putExtra("pos", j);
							Logger.d(TAG, "id:" + vedioList.get(j).id
									+ "--type:" + vedioList.get(j).type
									+ "--url:" + vedioList.get(j).url);
							startActivity(intent);

						}
					});
				}
			}

		} else {
			Logger.d("LoadVedio", "只超过两条数据");

			vedio_text_one.setText(vedioList.get(0).title);
			vedio_text_two.setText(vedioList.get(1).title);
			for (int i = 0; i < 2; i++) {
				final int j = i;
				posts[i + 2].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(getActivity(),
								VedioDetailActivity.class);
						intent.putExtra("title", vedioList.get(j).title);
						intent.putExtra("id", vedioList.get(j).id);
						intent.putExtra("type", vedioList.get(j).type);
						intent.putExtra("url", vedioList.get(j).url);
						intent.putExtra("pos", j);
						intent.putExtra("thumb", vedioList.get(j).thumb);
						Logger.d(TAG,
								"id:" + vedioList.get(j).id + "--type:"
										+ vedioList.get(j).type + "--url:"
										+ vedioList.get(j).url);
						startActivity(intent);

					}
				});
			}
			MainVideoAdapter videoAdapter = new MainVideoAdapter(getActivity(),
					v_List);
			vedio_Grid.setAdapter(videoAdapter);
			Utility.setGridViewHeightBasedOnChildren(vedio_Grid, 3, 2,
					"loadvideo");
			vedio_Grid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View convertview,
						int position, long id) {
					Intent intent = new Intent();
					intent.setClass(getActivity(), VedioDetailActivity.class);
					intent.putExtra("title", vedioList.get(position + 2).title);
					intent.putExtra("id", vedioList.get(position + 2).id);
					intent.putExtra("type", vedioList.get(position + 2).type);
					intent.putExtra("url", vedioList.get(position + 2).url);
					intent.putExtra("thumb", vedioList.get(position + 2).thumb);
					intent.putExtra("pos", position);
					Logger.d(TAG, "id:" + vedioList.get(position + 2).id
							+ "--type:" + vedioList.get(position + 2).type
							+ "--url:" + vedioList.get(position + 2).url);
					startActivity(intent);
				}
			});
		}
	}

	public void LoadNovel() {
		Logger.d(TAG, "LoadNovel");
		int length = novelList.size();

		if (length > 9) {
			length = 9;
		} else {

		}
		for (int i = 0; i < length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", Configure.image_novel_icon[i % 9]);
			map.put("name", novelList.get(i).title.toString());
			n_List.add(map);
		}
		MainNovelAdapter novelAdapter = new MainNovelAdapter(getActivity(),
				n_List);
		novel_Grid.setAdapter(novelAdapter);
		Utility.setGridViewHeightBasedOnChildren(novel_Grid, 3, 2, "loadnovel");
		novel_Grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertview,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(mContext, NovelDetailActivity.class);
				intent.putExtra("title", novelList.get(position).title);
				intent.putExtra("id", novelList.get(position).id);
				intent.putExtra("type", novelList.get(position).type);
				intent.putExtra("url", novelList.get(position).url);
				intent.putExtra("pos", position);
				intent.putExtra("thumb", novelList.get(position).thumb);
				Logger.d(TAG + "LoadNovel", "id:" + novelList.get(position).id
						+ "--type:" + novelList.get(position).type + "--url:"
						+ novelList.get(position).url);
				startActivity(intent);
			}
		});
		Logger.e("LoadNovel",
				"the gridview height is===>" + novel_Grid.getHeight());
	}

	ArrayList<Map<String, Object>> game_Left;
	String[] gamelftName = { "LOL", "Dota", "FlyBird", "SuperMarry" };

	public void LoadGame() {
		Logger.d(TAG, "LoadGame");
		int length = gameList.size();
		if (length > 5) {
			length = 5;
		} else {

		}
		for (int i = 0; i < length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			// new BitmapThread(gameList.get(i).thumb).start();
			// map.put("image", imageList.get(i));
			map.put("image", Configure.game_icon[i % 8]);
			map.put("name", gameList.get(i).title);
			map.put("size", "12M");
			map.put("down", "下载");

			Logger.d("LoadGame", "the gamelist is===>" + gameList.get(i).title);
			g_List.add(map);
		}
		GameListAdapter gameAdapter = new GameListAdapter(getActivity(),
				g_List, downloadManager);
		game_ListView.setAdapter(gameAdapter);
		Utility.setListViewHeightBasedOnChildren(game_ListView);
		game_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview,
					View converview, int position, long id) {

				// Intent intent = new Intent();
				// intent.setClass(getActivity(), DownloadFragment.class);
				// intent.putExtra("fromwhere", "game");
				// startActivity(intent);
				// getActivity().finish();
				if (Down()) {
					Intent intent = new Intent(getActivity(),
							DownloadListActivity.class);
					intent.putExtra("fromWhere", 2);
					getActivity().startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "当前应用已经下载过了！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	// -------------------------------这是xUtils下载-------------------------------------
	// 声明一个db
	DowningVideoDB DowningDB = null;
	public String Flag = "apk";

	// 定义一个变量来得到当前需要下载的路径
	private String path = "";
	// 定义一个变量来得到需要下载文件的名字
	private String down_filename = "";
	private DownloadManager downloadManager;

	public boolean Down() {
		if (null == DowningDB) {
			DowningDB = new DowningVideoDB(getActivity());
		}
		int i = (int) (Math.random() * 5 + 1);
		path = Configure.GamePath[i];
		down_filename = path.substring(path.lastIndexOf('/') + 1);
		// String target = "/sdcard/xUtils/" +
		// System.currentTimeMillis()
		// + "lzfile.apk";
		System.out.println("将要下载的文件名为====》" + down_filename);
		String target = "";
		if (down_filename.contains(".apk")) {
			target = Configure.GameFile + down_filename;
			Flag = "apk";
		} else if (down_filename.contains(".mp4")) {

			target = Configure.VedioFile + down_filename;
			Flag = "mp4";
		} else {
			target = Configure.NovelFile + down_filename;
			Flag = "novel";
		}
		if (DowningDB.select(down_filename) == 0) {
			DowningDB.insert("0", path, down_filename, 0);

			try {
				downloadManager.addNewDownload(path.toString(), down_filename,
						target, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
						false, Flag, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
						null);
			} catch (DbException e) {
				LogUtils.e(e.getMessage(), e);
			}
			return true;
		}
		return false;

	}

	// ------------------------这是xUtils的下载需要的---------------------------
	public void Totoast(String str) {

		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
	}

	public class BitmapThread extends Thread {

		private String startBg;

		public BitmapThread(String start) {
			startBg = start;
		}

		@Override
		public void run() {
			try {
				URL url = new URL(startBg);
				Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
				Logger.log("bitmap:" + bitmap
						+ "----------------------------------------------");
				// cache.put("startBitmap", bitmap);
				Message msg = new Message();
				msg.obj = bitmap;
				imageHandler.sendMessage(msg);

			} catch (IOException e) {
				Logger.log("------------" + e.toString() + "<><><><><><>");
				try {
					android.os.Debug
							.dumpHprofData("/mnt/sdcard/hljhomedump.hprof");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
	Handler imageHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				getImageBack(imageList);
				break;

			default:
				break;
			}
			Bitmap bitmap = (Bitmap) msg.obj;

			imageList.add(bitmap);
			imageHandler.sendEmptyMessageDelayed(1, 1000);
			super.handleMessage(msg);
		}
	};

	public void getImageBack(ArrayList<Bitmap> list) {

		news_Image_One.setImageBitmap(list.get(0));
		news_Image_Two.setImageBitmap(list.get(1));
		vedio_Image_One.setImageBitmap(list.get(2));
		vedio_Image_Two.setImageBitmap(list.get(3));
	}

	/* 这是判断dialog是否显示,0代表没有显示，1代表显示 */

	public static int isShow = 0;

	public void showExitDialog() {
		isShow = 1;
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("网络出现问题.....");
		builder.setTitle("提示").setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						isShow = 0;
					}
				});
		builder.create().show();
	}
}
