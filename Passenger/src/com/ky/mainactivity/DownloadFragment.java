package com.ky.mainactivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.HttpHandler;

import com.android.network.DownloadProgressListener;
import com.android.network.FileDownloader;
import com.ky.adapter.DownloadListAdapter;
import com.ky.adapter.DownloadedListAdapter;
import com.ky.db.GameDownedDB;
import com.ky.db.GameDowningDB;
import com.ky.db.VideoDownedDB;
import com.ky.db.VideoDowningDB;
import com.ky.passenger.R;
import com.ky.utills.DeviceCheck;
import com.ky.utills.GetVedioPath;
import com.ky.utills.Configure;
import com.ky.utills.Utility;
import com.redbull.log.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * this is download
 * 
 * @author Catherine.Brain
 * 
 * 
 *         如果这里是外网的环境，那么我们就只能为期提供已下载的列表
 * 
 * 
 * 
 * 
 *         下载应该注意： 下载里面有视频以及小说以及游戏的下载，那么这里对于这三种数据，我们应该分别存储，然后根据不同的字段查出来。
 * 
 *         在数据库里面应该要判断当前的下载项目是否已经存在在数据库里面了，如果已经存在的话，那么我们应该不向里面添加了。然后，在显示的时候，
 *         我们就可以根据 数据库里面的数据来进行查询显示。
 * 
 * 
 * 
 *         对于已经下载的视频，出现了加载很久的效果，这里应该是我操作的失误，应该判断两次，也就是这里还是需要将已经下载好的视频同时记录在数据库里面，
 *         这样
 *         在文件夹里面查出来的数据在一一到数据库里面进行比对，这样，如果数据库里面也有这个数据，才代表真正的被下载了，所以，就不会存在显示还未下载完的视频
 * 
 * */
public class DownloadFragment extends Activity implements OnClickListener {
	public static String TAG = "DownloadFragment";
	View view = null;
	public static String FRAGMENTNAME = "编辑";
	String path;

	RadioGroup radioGroup;
	RadioButton btn_one, btn_two, btn_three;
	LinearLayout layout_toast;
	RadioGroup radio_G;
	RadioButton btn_downing, btn_downned;
	ListView list_offline;
	Button text_all, text_left;
	View space_v;
	public static LinearLayout layout_bottom_space, layout_allcontent;
	LayoutInflater inflater = null;
	/* the 0 is stand the downing and the 1 stand the downloaded */
	public static int LEFTORRIGHT = 0;
	public static Button btn_select, btn_delete;
	public static DownloadListAdapter adapter = null;
	public static DownloadedListAdapter downloadedAdapter = null;
	private ArrayList<Map<String, String>> mList = null;
	private ArrayList<Map<String, String>> downloadedList = null;
	// boolean isEx = false;
	String fromwhere;
	/* 导航栏 */
	Button btn_Return;
	TextView text_Title;
	public static Button btn_selectAll;
	Button btn_exit;

	// 定义一个变量来得到下载文件的名字
	String down_filename;
	// 这是已经下载好的文件的数据库
	VideoDownedDB videoDB = null;
	// 这是正在下载的文件的数据库
	VideoDowningDB v_DownDB = null;
	// 这是游戏下载的数据库
	GameDowningDB Game_Downing_DB = null;
	// 这是游戏已经下载好的数据库
	GameDownedDB game_DownedDB = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download);
		initView();
	}

	/**
	 * 
	 * 在结束当前activity的时候保存一些数据
	 * */
	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);

	}

	/**
	 * 
	 * 初始化所有视图
	 * */
	String[] VedioPath = { "http://hwx.chinafilmad.com/lc.mp4",
			"http://hwx.chinafilmad.com/tbbdh.mp4",
			"http://hwx.chinafilmad.com/hjdl.mp4",
			"http://hwx.chinafilmad.com/ccnn.mp4",
			"http://hwx.chinafilmad.com/letitgo.mp4",
			"http://hwx.chinafilmad.com/rewind.mp4 " };
	String[] GamePath = {
			"http://gdown.baidu.com/data/wisegame/945e6c492987b32b/91zhushou_39870.apk",
			"http://gdown.baidu.com/data/wisegame/fb9ec53a3faf29ca/anzhuoshichang_87.apk",
			"http://gdown.baidu.com/data/wisegame/874981595f6b0b92/zhenyouwang_16.apk",
			"http://gdown.baidu.com/data/wisegame/44678f0ac42a4755/oupengliulanqi_50.apk",
			"http://gdown.baidu.com/data/wisegame/19af598924f0f158/lianlianmianfeiWiFi_254.apk",
			"http://gdown.baidu.com/data/wisegame/f790f5818d8d9f93/laizidoudizhu_15.apk" };

	public void initView() {
		Configure.PAGER = 3;
		getVedioPath = new GetVedioPath();
		Intent intent = getIntent();
		fromwhere = intent.getStringExtra("fromwhere");
		path = intent.getStringExtra("path");
//		System.out.println("得到的路径是===>" + path + "-----解密后的路径是====>"
//				+ encode2(path));

		text_Title = (TextView) findViewById(R.id.text_nav_title);
		text_Title.setText("编辑");
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		btn_one = (RadioButton) findViewById(R.id.btn_0);
		btn_two = (RadioButton) findViewById(R.id.btn_1);

		btn_three = (RadioButton) findViewById(R.id.btn_2);
		btn_Return = (Button) findViewById(R.id.btn_return);
		btn_selectAll = (Button) findViewById(R.id.btn_allselect);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		list_offline = (ListView) findViewById(R.id.list_down);
		btn_downing = (RadioButton) findViewById(R.id.btn_downing);
		btn_downned = (RadioButton) findViewById(R.id.btn_downed);
		layout_toast = (LinearLayout) findViewById(R.id.layout_toast);
		radio_G = (RadioGroup) findViewById(R.id.radioGroup);
		layout_bottom_space = (LinearLayout) findViewById(R.id.layout_bottom_space);
		layout_allcontent = (LinearLayout) findViewById(R.id.layout_bottom_nomal);
		text_all = (Button) findViewById(R.id.btn_allcontent);
		text_left = (Button) findViewById(R.id.btn_left);
		SetMomeryInfo();
		btn_Return.setOnClickListener(this);
		text_Title.setOnClickListener(this);

		btn_selectAll.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		if (fromwhere.equals("game")) {
			int i = (int) (Math.random() * 5 + 1);
			path = GamePath[i];
			// path =
			// "http://gdown.baidu.com/data/wisegame/fb9ec53a3faf29ca/anzhuoshichang_87.apk";
			down_filename = path.substring(path.lastIndexOf('/') + 1);
			/* 将数据添加到游戏的数据库 */
			if (null == Game_Downing_DB) {
				Game_Downing_DB = new GameDowningDB(this);
			}
			// 添加单独的下载
			if (Game_Downing_DB.select(down_filename) == 0) {
				Game_Downing_DB.insert("34", path, down_filename);
			}
			btn_three.setChecked(true);
			ToDown();
		} else if (fromwhere.equals("novel")) {
			Logger.d(TAG, "novel download");
			btn_two.setChecked(true);
		} else if (fromwhere.equals("vedio")) {
			int i = (int) (Math.random() * 5 + 1);
			path = VedioPath[i];
			down_filename = path.substring(path.lastIndexOf('/') + 1);
			/* 将数据添加到视频的数据库 */
			if (null == v_DownDB) {
				v_DownDB = new VideoDowningDB(this);
			}
			// 添加单独的下载
			if (v_DownDB.select(down_filename) == 0) {
				v_DownDB.insert(path, down_filename, "0", fromwhere, 0, 0);
			}

			btn_one.setChecked(true);
			ToDown();
		} else if (fromwhere.equals("base")) {
			Logger.d(TAG, "base download");
			btn_downing.setChecked(true);
			// 查询下载的内容

			opera_ing();

		}

		radio_G.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup radio, int position) {
				checkContent(position);
			}
		});

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int position) {
				ChangeView(position);
			}
		});
		/**
		 * 
		 * 查询正在下载
		 * */
		Downhandler.sendEmptyMessage(DOWNING);
	}

	/**
	 * 
	 * 点击进来开始下载....,首页(base)点击进来不用下载
	 * */
	public void ToDown() {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			if (null == path) {
				// 显示SDCard错误信息
				System.out.println("the path is====>" + path);
				Toast.makeText(DownloadFragment.this, "视频文件不存在！", 1).show();
			} else {

				download(path, Environment.getExternalStorageDirectory(),
						down_filename);

			}

		} else {
			// 显示SDCard错误信息
			Toast.makeText(DownloadFragment.this, R.string.sdcarderror, 1)
					.show();
		}
	}

	/**
	 * 
	 * 得到设备的内存信息
	 * 
	 * */

	public void SetMomeryInfo() {

		text_all.setText("总容量:" + DeviceCheck.getTotalMemory(this));
		text_left.setText("剩余容量:" + DeviceCheck.getAvailMemory(this));
	}

	/**
	 * 
	 * 查询正在下载的文件
	 * */
	public ArrayList<Map<String, String>> checkDowing() {
		ArrayList<Map<String, String>> downing = new ArrayList<Map<String, String>>();
		if (fromwhere.equals("game")) {
			if (null == Game_Downing_DB) {
				Game_Downing_DB = new GameDowningDB(DownloadFragment.this);

			}
			Map<String, String> map = null;
			Cursor corsorisDown = Game_Downing_DB.select();
			if (corsorisDown.moveToFirst()) {
				list_offline.setVisibility(view.VISIBLE);
				layout_toast.setVisibility(View.GONE);
				for (corsorisDown.moveToFirst(); !corsorisDown.isAfterLast(); corsorisDown
						.moveToNext()) {
					map = new HashMap<String, String>();
					String Down_Name = corsorisDown.getString(corsorisDown
							.getColumnIndex(Game_Downing_DB.n_NAME));
					String down_url = corsorisDown.getString(corsorisDown
							.getColumnIndex(Game_Downing_DB.n_URL));
					Logger.d(TAG, "正在下载的游戏的名字===2" + Down_Name);

					if (!Down_Name.equals("")) {
						map.put("name", Down_Name);
						map.put("url", down_url);
					}
					downing.add(map);

				}
			}
		} else if (fromwhere.equals("novel")) {
		} else {
			if (null == v_DownDB) {
				v_DownDB = new VideoDowningDB(DownloadFragment.this);

			}
			Map<String, String> map = null;
			Cursor corsorisDown = v_DownDB.selectCategory("vedio");
			if (corsorisDown.moveToFirst()) {
				System.out.println("数据库里面的数据条数为===>" + corsorisDown.getCount());
				for (corsorisDown.moveToFirst(); !corsorisDown.isAfterLast(); corsorisDown
						.moveToNext()) {
					map = new HashMap<String, String>();
					String Down_Name = corsorisDown.getString(corsorisDown
							.getColumnIndex(v_DownDB.n_NAME));
					String down_url = corsorisDown.getString(corsorisDown
							.getColumnIndex(v_DownDB.n_URL));
					Logger.d(TAG, "正在下载的视频的名字===2" + Down_Name);

					if (!Down_Name.equals("")) {
						map.put("name", Down_Name);
						map.put("url", down_url);
					}
					downing.add(map);

				}
			}

		}

		return downing;
	}

	private void checkContent(int v) {

		switch (v) {
		case R.id.btn_downing:

			btn_downing.setTextColor(0xFFFFFFFF);
			btn_downned.setTextColor(0xFF444444);
			Downhandler.sendEmptyMessage(DOWNING);
			break;
		case R.id.btn_downed:
			Downhandler.sendEmptyMessage(DOWNED);

			btn_downned.setTextColor(0xFFFFFFFF);
			btn_downing.setTextColor(0xFF444444);

			Configure.ISDOWNED = 1;

			break;
		default:
			break;
		}
		System.out.println("the fromwhere is=====>" + fromwhere);
		layout_bottom_space.setVisibility(View.GONE);
		layout_allcontent.setVisibility(View.VISIBLE);
	}

	/**
	 * 切换到正在下载的页面
	 * */
	public void Downing() {

		LEFTORRIGHT = 0;
		Configure.ISDOWNED = 0;

		getDownloadingAdapter();
		// text_Title.setText("编辑");

	}

	public void ChangeView(int id) {
		switch (id) {
		case R.id.btn_1:// 视频
			fromwhere = "novel";

			break;
		case R.id.btn_0:// 小说
			fromwhere = "vedio";

			break;
		case R.id.btn_2:// 游戏
			fromwhere = "game";

			break;
		default:
			break;
		}
		btn_downing.setChecked(true);
		// 查询下载的内容
		Downhandler.sendEmptyMessage(DOWNING);
		opera_ing();

		layout_bottom_space.setVisibility(View.GONE);
		layout_allcontent.setVisibility(View.VISIBLE);

	}

	/**
	 * 
	 * 
	 * 加载正在下载的数据
	 * */
	public void getDownloadingAdapter() {
		ArrayList<Map<String, String>> getDown = new ArrayList<Map<String, String>>();
		if (null == mList) {
			mList = new ArrayList<Map<String, String>>();

		}
		mList.clear();
		getDown = checkDowing();

		if (getDown.size() == 0) {
			layout_toast.setVisibility(View.VISIBLE);
			list_offline.setVisibility(View.GONE);
			text_Title.setText("下载");
		} else {
			text_Title.setText("编辑");
			layout_toast.setVisibility(View.GONE);
			list_offline.setVisibility(View.VISIBLE);
			for (int i = 0; i < getDown.size(); i++) {
				Logger.d(TAG, "正在下载的名字为====" + getDown.get(i).toString());
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", getDown.get(i).get("name").toString());
				map.put("serise", "第一集");
				map.put("speed", "0%");
				map.put("url", getDown.get(i).get("url").toString());
				mList.add(map);
			}
			adapter = new DownloadListAdapter(DownloadFragment.this, mList);
			list_offline.setAdapter(adapter);
			// Utility.setListViewHeightBasedOnChildren(list_offline);
		}

	}

	/**
	 * 
	 * 加载已经下载好的数据
	 * */
	public void getDownloadedAdapter(int len,
			ArrayList<Map<String, String>> mList) {
		if (len == 0) {
			/* 提示暂时没有任何视频 */
			layout_toast.setVisibility(View.VISIBLE);
			list_offline.setVisibility(View.GONE);
		} else {
			layout_toast.setVisibility(View.GONE);
			list_offline.setVisibility(View.VISIBLE);
			downloadedList = new ArrayList<Map<String, String>>();
			Map<String, String> map = null;
			downloadedList.clear();
			for (int i = 0; i < len; i++) {
				map = new HashMap<String, String>();

				map.put("name", mList.get(i).get("name"));
				map.put("size", "20M");
				map.put("url", mList.get(i).get("url").toString());
				Logger.d(TAG,
						"the mlist name is==>"
								+ mList.get(i).get("name").toString()
								+ "and the filename is=-=====>"
								+ mList.get(i).get("url").toString());
				downloadedList.add(map);
			}
			downloadedAdapter = new DownloadedListAdapter(
					DownloadFragment.this, downloadedList, fromwhere);
			list_offline.setAdapter(downloadedAdapter);
			// Utility.setListViewHeightBasedOnChildren(list_offline);
			downloadedAdapter.notifyDataSetChanged();
		}

	}

	ArrayList<String> isDowning = new ArrayList<String>();
	ArrayList<String> downingVedio = null;

	/**
	 * 
	 * 
	 * 查询已经下载好的文件
	 * */

	public ArrayList<Map<String, String>> LoadingDowned() {
		// 用来装视频的数据
		ArrayList<Map<String, String>> vedio = new ArrayList<Map<String, String>>();

		// vedio.clear();
		Map<String, String> map = null;
		downingVedio = new ArrayList<String>();
		if (fromwhere.equals("game")) {
			// 查询已经下载的游戏的数据库
			downingVedio = TOGetFile(isSdcard() + Configure.GameFile,
					"game_path");
			if (downingVedio.size() != 0) {

				for (int i = 0; i < downingVedio.size(); i++) {
					int idx = downingVedio.get(i).toString().lastIndexOf("/");
					String name = downingVedio.get(i).toString()
							.substring(idx + 1);
					System.out.println("已经下载好的game文件的有：" + name);
					if (null == game_DownedDB) {
						game_DownedDB = new GameDownedDB(DownloadFragment.this);

					}
					if (game_DownedDB.select(name) == 0) {

					} else {

						map = new HashMap<String, String>();
						map.put("url", name);
						map.put("name", name);
						vedio.add(map);
					}

				}
			}
			// if (null == game_DownedDB) {
			// game_DownedDB = new GameDownedDB(DownloadFragment.this);
			//
			// }
			// Cursor corsor = game_DownedDB.select();
			// if (corsor.moveToFirst()) {
			// for (corsor.moveToFirst(); !corsor.isAfterLast(); corsor
			// .moveToNext()) {
			// // 得到下载好的游戏的数据大小
			// String size = corsor.getString(corsor
			// .getColumnIndex(game_DownedDB.n_Size));
			// // 得到游戏所在本地的地址
			// String url = corsor.getString(corsor
			// .getColumnIndex(game_DownedDB.n_URL));
			// // 得到下载好的游戏的名字
			// String name = corsor.getString(corsor
			// .getColumnIndex(game_DownedDB.n_NAME));
			//
			// map = new HashMap<String, String>();
			// map.put("url", url);
			// map.put("name", name);
			// map.put("size", size);
			// vedio.add(map);
			// }
			//
			// for (int i = 0; i < vedio.size(); i++) {
			// System.out.println("已经下载好的文件的有："
			// + vedio.get(i).get("name").toString());
			// }
			// }
			else {

				list_offline.setVisibility(View.GONE);
				layout_toast.setVisibility(view.VISIBLE);

			}
		} else if (fromwhere.equals("novel")) {
			// // 查询已经下载的小说的数据库
			// ArrayList<String> novel = TOGetFile(isSdcard()
			// + StaticVariable.VedioFile, "vedio_path");
			// for (int i = 0; i < novel.size(); i++) {
			// int idx = novel.get(i).toString().lastIndexOf("/");
			// System.out.println("已经下载好的novel文件的有："
			// + novel.get(i).toString().substring(idx + 1));
			// }

		} else {
			// 查询已经下载的视频的数据库
			downingVedio = TOGetFile(isSdcard() + Configure.VedioFile,
					"vedio_path");
			if (downingVedio.size() != 0) {

				for (int i = 0; i < downingVedio.size(); i++) {
					int idx = downingVedio.get(i).toString().lastIndexOf("/");
					String name = downingVedio.get(i).toString()
							.substring(idx + 1);
					System.out.println("已经下载好的vedio文件的有：" + name);
					if (null == videoDB) {
						videoDB = new VideoDownedDB(DownloadFragment.this);
					}
					if (videoDB.select(name) == 0) {

					} else {
						map = new HashMap<String, String>();
						map.put("url", name);
						map.put("name", name);
						vedio.add(map);

					}

				}
			}
			// if (null == videoDB) {
			// videoDB = new VideoDownedDB(DownloadFragment.this);
			//
			// }
			// Cursor corsor = videoDB.select();
			// if (corsor.moveToFirst()) {
			// for (corsor.moveToFirst(); !corsor.isAfterLast(); corsor
			// .moveToNext()) {
			// String url = corsor.getString(corsor
			// .getColumnIndex(videoDB.n_URL));
			// String name = corsor.getString(corsor
			// .getColumnIndex(videoDB.n_NAME));
			//
			// map = new HashMap<String, String>();
			// map.put("url", url);
			// map.put("name", name);
			// vedio.add(map);
			// }
			//
			// for (int i = 0; i < vedio.size(); i++) {
			// System.out.println("已经下载好的文件的有："
			// + vedio.get(i).get("name").toString() + "以及路径是==="
			// + vedio.get(i).get("url").toString());
			// }
			// }
			else {

				list_offline.setVisibility(View.GONE);
				layout_toast.setVisibility(view.VISIBLE);

			}
		}
		return vedio;

	}

	String url;
	public String result = null;
	ArrayList<String> vedio_Path;
	GetVedioPath getVedioPath = null;

	public ArrayList<String> TOGetFile(String fileName, String key) {

		vedio_Path = new ArrayList<String>();
		// 访问之前呢，我们需要去查看是否有文件，然后取出来
		if (null == isSdcard()) {
			Logger.e("GetSendData", "内存卡不存在");
			return null;
		} else {
			if (!new File(fileName).exists()) {
				Logger.e(TAG, key + "文件夹不存在！");
				return null;
			} else {
				int len = getVedioPath.getvediopath(fileName, key);
				if (len == 0) {
					Logger.e(TAG, "文件长度为0");
				} else {

					vedio_Path = getVedioPath.getVdio(key);
				}
				return vedio_Path;
			}

		}
	}

	/**
	 * 先判断是否有SD卡
	 * */
	private String isSdcard() {
		File sdcardDir = null;
		boolean isSDExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (isSDExist) {
			// 如果存在SDcard 就找到跟目录
			sdcardDir = Environment.getExternalStorageDirectory();
			return sdcardDir.toString();
		} else {
			return null;
		}
	}

	/**
	 * 主线程(UI线程) 对于显示控件的界面更新只是由UI线程负责，如果是在非UI线程更新控件的属性值，更新后的显示界面不会反映到屏幕上
	 * 如果想让更新后的显示界面反映到屏幕上，需要用Handler设置。
	 * 
	 * @param path
	 * @param savedir
	 */
	// public final static int SIZE = 0x220000;
	public final static int DownOver = 0x330000;
	// 开启3个线程进行下载
	FileDownloader loader;
	String fileName;
	DownTask downtask = null;
	// 应该定义一个bool值来表示是否继续下载
	public boolean isGoDowning = true;

	private void download(final String path, final File savedir,
			final String down_filename) {
		Logger.d(TAG, "开始下载了，the path is====" + path);
		downtask = new DownTask(savedir);
		downtask.start();
	}

	/**
	 * 当Handler被创建会关联到创建它的当前线程的消息队列，该类用于往消息队列发送消息 消息队列中的消息由当前线程内部进行处理
	 * 使用Handler更新UI界面信息。
	 */
	public static final int DELETE = 0x000022;
	public static final int DOWN_SUCCESS = 0x000033;
	public static final int DownWrong = 0x000044;
	public static final int DOWNED = 0x000055;
	public static final int DOWNING = 0x000066;
	File file;
	String target = null;
	Handler Downhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 1:

				if (null == adapter.holderView.processBar
						|| null == adapter.holderView || null == adapter) {

				} else {
					adapter.getResult(msg.getData().getInt("size"));
					adapter.setMax(loader.getFileSize());
					// 显示下载成功信息
					if (adapter.holderView.processBar.getProgress() == adapter.holderView.processBar
							.getMax()) {
						fileName = loader.file_name;
						String downed_title = adapter.holderView.text_Name
								.getText().toString();
						if (fileName.indexOf(".apk") != -1) {
							// 如果是apk的话就执行安装

							file = new File(target + fileName);

							if (fromwhere.equals("game")) {
								// 加入到游戏下载好的序列中
								if (null == game_DownedDB) {
									game_DownedDB = new GameDownedDB(
											DownloadFragment.this);
								}

								if (game_DownedDB.select(fileName) == 0) {
									game_DownedDB.insert("20", fileName,
											downed_title);
									Downhandler.sendEmptyMessage(DOWN_SUCCESS);
								} else {

								}
								if (null == Game_Downing_DB) {
									Game_Downing_DB = new GameDowningDB(
											DownloadFragment.this);

								}

								Game_Downing_DB.delete(downed_title);
							}
						}

						else if (fromwhere.equals("novel")) {

						} else {

							// 加入到视频下载好的序列中
							if (null == videoDB) {
								videoDB = new VideoDownedDB(
										DownloadFragment.this);
							}
							if (videoDB.select(fileName) == 0) {
								videoDB.insert(fileName, downed_title, 0);
								System.out.println("the insert url is===>"
										+ fileName + "---and the title is===>"
										+ downed_title);
							}
							if (null == v_DownDB) {
								v_DownDB = new VideoDowningDB(
										DownloadFragment.this);

							}

							// v_DownDB.delete(downed_title);
						}

						Logger.d(TAG, "下载好了的视频的名字===" + downed_title);

						mList = checkDowing();
						for (int i = 0; i < mList.size(); i++) {
							System.out.println("正在下载的列表=====》"
									+ mList.get(i).get("name"));

						}
						if (mList.size() == 0) {
							isGoDowning = false;
						} else {
							getDownloadingAdapter();
							adapter.reGet(mList);
							adapter.notifyDataSetChanged();

						}

					}

				}

				break;
			case -1:
				// // 显示下载错误信息
				Toast.makeText(DownloadFragment.this, R.string.error,
						Toast.LENGTH_SHORT).show();
				break;
			case DELETE:
				loader.fileService.delete(fileName);
				break;
			case DOWN_SUCCESS:
				// 下载apk成功，然后就开始安装

				openFile(file);
				break;
			case DownWrong:
				// 下载的不是apk文件
				Toast.makeText(DownloadFragment.this, "下载文件不是apk文件", 1).show();
				break;
			case DownOver:
				System.out.println("收到命令，下载完成");
				// 显示下载成功信息
				// if (adapter.holderView.processBar.getProgress() ==
				// adapter.holderView.processBar
				// .getMax()) {
				adapter.holderView.layout_line.setVisibility(View.GONE);
				fileName = loader.file_name;
				String downed_title = adapter.holderView.text_Name.getText()
						.toString();

				if (fromwhere.equals("game")) {
					if (fileName.indexOf(".apk") != -1) {
						// 如果是apk的话就执行安装
						System.out.println("下载好了的游戏的名字===" + fileName);
						file = new File(target + fileName);

						// 加入到游戏下载好的序列中
						if (null == game_DownedDB) {
							game_DownedDB = new GameDownedDB(
									DownloadFragment.this);
						}

						if (game_DownedDB.select(fileName) == 0) {
							game_DownedDB.insert("20", fileName, downed_title);
							Downhandler.sendEmptyMessage(DOWN_SUCCESS);
						} else {

						}
						if (null == Game_Downing_DB) {
							Game_Downing_DB = new GameDowningDB(
									DownloadFragment.this);

						}

						Game_Downing_DB.delete(downed_title);

					}
				} else if (fromwhere.equals("novel")) {

				} else {

					// 加入到视频下载好的序列中
					if (null == videoDB) {
						videoDB = new VideoDownedDB(DownloadFragment.this);
					}
					if (videoDB.select(fileName) == 0) {
						videoDB.insert(fileName, downed_title, 0);
						System.out.println("the insert url is===>" + fileName
								+ "---and the title is===>" + downed_title);
					}
					if (null == v_DownDB) {
						v_DownDB = new VideoDowningDB(DownloadFragment.this);

					}

					v_DownDB.delete(downed_title);
				}

				Logger.d(TAG, "下载好了的视频的名字===" + downed_title);

				mList = checkDowing();
				for (int i = 0; i < mList.size(); i++) {
					System.out.println("正在下载的列表=====》"
							+ mList.get(i).get("name"));

				}
				if (mList.size() == 0) {
					isGoDowning = false;
				} else {
					getDownloadingAdapter();
					adapter.reGet(mList);
					adapter.notifyDataSetChanged();

				}

				// }
				break;
			case DOWNED:
				LEFTORRIGHT = 1;
				ArrayList<Map<String, String>> getVedio = LoadingDowned();
				if (getVedio.size() == 0) {
					text_Title.setText("下载");
					/* 提示暂时没有任何视频 */
					layout_toast.setVisibility(View.VISIBLE);
					list_offline.setVisibility(View.GONE);
				} else {
					getDownloadedAdapter(getVedio.size(), getVedio);
					text_Title.setText("编辑");

				}
				break;
			case DOWNING:
				Downing();
				break;
			}

		}

	};

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

	/**
	 * 下载
	 * 
	 * @param url
	 */

	FinalHttp finalHttp = new FinalHttp();
	HttpHandler httpHandler;
	// CommonToast toast;
	String extensionName;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_return:
			startActivity(new Intent().setClass(this, BaseActivity.class));
			finish();
			break;
		case R.id.text_nav_title:
			String title = text_Title.getText().toString().trim();
			if (title.equals("编辑")) {
				if (LEFTORRIGHT == 0) {
					adapter.toShow(1);
				} else {
					downloadedAdapter.toShow(1);

				}
				btn_selectAll.setText("全选");
				text_Title.setText("取消");
				layout_bottom_space.setVisibility(View.VISIBLE);
				layout_allcontent.setVisibility(View.GONE);
			} else if (title.equals("取消")) {
				text_Title.setText("编辑");
				opera_ing();
				layout_bottom_space.setVisibility(View.GONE);
				layout_allcontent.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btn_allselect:
			String name = btn_selectAll.getText().toString().trim();

			if (name.equals("全选")) {
				btn_selectAll.setText("取消全选");
				if (LEFTORRIGHT == 0) {
					for (int i = 0; i < mList.size(); i++) {
						adapter.chooseState(i, true, false);
					}
				} else {
					for (int i = 0; i < downloadedList.size(); i++) {
						downloadedAdapter.chooseState(i, true, false);
					}

				}

			} else if (name.equals("取消全选")) {
				btn_selectAll.setText("全选");
				if (LEFTORRIGHT == 0) {
					for (int i = 0; i < mList.size(); i++) {
						adapter.chooseState(i, false, false);
					}
				} else {
					for (int i = 0; i < downloadedList.size(); i++) {
						downloadedAdapter.chooseState(i, false, false);
					}

				}

			}
			break;
		case R.id.btn_exit:
			Cursor cursor = null;
			/* 删除数据库里面的全部事件 */
			if (Configure.ISDOWNED == 0) {
				// 这是在正在下载的界面
				if (null == v_DownDB) {
					v_DownDB = new VideoDowningDB(DownloadFragment.this);

				}
				cursor = v_DownDB.selectISDelete(1);
				if (cursor.moveToFirst()) {
					ShowToast("你确认要删除选中", 1);
				} else {
					ShowToast("请先选择需要删除的文件", 0);
				}
			} else {
				// 这是已经下载的文件
				if (null == videoDB) {
					videoDB = new VideoDownedDB(DownloadFragment.this);
				}
				cursor = videoDB.selectISDelete("1");
				ShowDownedToast();
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 显示下载的选中需要删除的
	 * 
	 * */

	public void ShowToast(String Title, final int is_go) {

		AlertDialog.Builder builder = new Builder(DownloadFragment.this);
		builder.setTitle("提示");
		builder.setMessage(Title);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (is_go == 0) {

				} else {
					btn_selectAll.setText("全选");
					text_Title.setText("编辑");
					layout_bottom_space.setVisibility(View.GONE);
					layout_allcontent.setVisibility(View.VISIBLE);
					/* 删除数据库里面的全部事件 */
					if (Configure.ISDOWNED == 0) {
						// 这是在正在下载的界面
						if (null == v_DownDB) {
							v_DownDB = new VideoDowningDB(DownloadFragment.this);

						}
						v_DownDB.selectISDelete(1);
						v_DownDB.deleteData(1);
						getDownloadingAdapter();

					} else {
						File file = null;
						if (fromwhere.equals("vedio")) {
							file = new File(isSdcard()
									+ Configure.VedioFile);
						} else if (fromwhere.equals("game")) {
							file = new File(isSdcard()
									+ Configure.GameFile);
						} else {
							file = new File(isSdcard()
									+ Configure.VedioFile);
						}
						File[] file_Array = file.listFiles();
						ArrayList<Integer> getChoose = downloadedAdapter
								.getChoose();
						if (getChoose.size() == 0) {

						} else {
							for (int i = 0; i < getChoose.size(); i++) {
								System.out.println("选中删除的内容的下标包括===1"
										+ getChoose.get(i));
								System.out.println("选中删除的内容的下标包括===2"
										+ file_Array[getChoose.get(i)]);
								if (file_Array[getChoose.get(i)].delete()) {
									Downhandler.sendEmptyMessage(DOWNED);
								} else {
									Downhandler.sendEmptyMessage(DOWNED);
								}
							}

						}

					}

				}

			}
		});
		builder.create().show();
	}

	/**
	 * 
	 * 查询显示已经下载的需要删除的
	 * */
	public void ShowDownedToast() {
		// File file = new File(isSdcard() + StaticVariable.VedioFile);
		// File[] file_Array = file.listFiles();
		ArrayList<Integer> getChoose = downloadedAdapter.getChoose();
		if (getChoose.size() == 0) {
			ShowToast("请先选择需要删除的文件", 0);
		} else {
			ShowToast("你确认要删除选中", 1);

		}

	}

	public void opera_ing() {
		if (LEFTORRIGHT == 0) {
			if (null == adapter) {

			} else {
				adapter.toShow(0);

			}

		} else {
			if (null == downloadedAdapter) {

			} else {

				downloadedAdapter.toShow(0);
			}

		}

	}

	/* 监听按键 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent().setClass(this, BaseActivity.class));
			finish();
			return false;

		}
		return super.onKeyDown(keyCode, event);
	}

	/* 定义一个下载的类 */
	class DownTask extends Thread {
		File savedir = null;

		public DownTask(File savedir) {
			super();
			this.savedir = savedir;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Looper.prepare();

			if (fromwhere.equals("game")) {
				target = savedir.toString() + Configure.GameFile;
			} else {

				target = savedir.toString() + Configure.VedioFile;
			}

			File filesavedir = new File(target);
			if (!filesavedir.exists()) {
				filesavedir.mkdir();
			}
			loader = new FileDownloader(DownloadFragment.this, path,
					filesavedir, 3, down_filename);

			try {
				loader.download(new DownloadProgressListener() {

					@Override
					public void onDownloadSize(int size, boolean isGoing) {
						if (isGoing) {
							// Message msg = Message.obtain(Downhandler, 1);
							Message msg = new Message();
							msg.what = 1;
							msg.getData().putInt("size", size);
							Downhandler.sendMessage(msg);// 发送消息

						} else {
							Downhandler.sendEmptyMessage(DownOver);
						}

					}

				});
			} catch (Exception e) {
				Downhandler.sendEmptyMessage(-1);
			}

		}

	}

	// 写一个让线程停止的方法
	@SuppressWarnings("deprecation")
	public void Exit() {
		if (null != downtask) {
			downtask.stop();
		}
	}

	/**
	 * 简单异或加密解密算法
	 * 
	 * @param str
	 *            要加密的字符串
	 * @return
	 */
	private static String encode2(String str) {
		int code = 112; // 密钥
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			charArray[i] = (char) (charArray[i] ^ code);
		}
		return new String(charArray);
	}
}
