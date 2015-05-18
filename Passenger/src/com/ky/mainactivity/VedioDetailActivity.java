package com.ky.mainactivity;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.crypto.spec.PSource;

import com.ky.beaninfo.ColumnVedioInfo;
import com.ky.db.VideoDowningDB;
import com.ky.passenger.R;
import com.ky.request.GetContentInfoRequest;
import com.ky.request.HttpHomeLoadDataTask;
import com.ky.response.GetContentInfoResponse;
import com.ky.response.GetContentListResponse;
import com.ky.utills.Configure;
import com.lhl.callback.IUpdateData;
import com.lhl.db.DowningVideoDB;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.sample.download.DownloadManager;
import com.lidroid.xutils.sample.download.DownloadService;
import com.lidroid.xutils.util.LogUtils;
import com.redbull.log.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 视频详情
 * 
 * @author Catherine.Brain
 * */
public class VedioDetailActivity extends Activity implements OnClickListener {
	String TAG = "VedioDetailFragment";
	// View view = null;
	TextView text_Daoyan, text_Actor, text_Year, text_Intro, text_Name;
	Button btn_Down;
	// btn_Last, btn_Next,
	public static String FRAGMENTNAME = "详情";
	ImageView vedioImage;
	private Context mAppContext;
	Button btn_return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vedio_detail);
		initView();
		mAppContext = getApplicationContext();
		downloadManager = DownloadService.getDownloadManager(mAppContext);
	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// if (null == view) {
	// view = LayoutInflater.from(getActivity()).inflate(
	// R.layout.vedio_detail, null);
	//
	// }
	// ViewGroup p = (ViewGroup) view.getParent();
	// if (p != null) {
	// p.removeView(view);
	// }
	//
	// return view;
	// }

	String actor, director, years, intro, title, thumb, down_url, url, id,
			type;
	// 得到小标，然后得到响应的图片
	int position = 0;

	public void initView() {
		Configure.PAGER = 6;
		text_Actor = (TextView) findViewById(R.id.text_actor_name);
		text_Daoyan = (TextView) findViewById(R.id.text_daoyan_name);
		text_Year = (TextView) findViewById(R.id.text_years_time);
		text_Intro = (TextView) findViewById(R.id.text_intro);
		vedioImage = (ImageView) findViewById(R.id.video_image);
		text_Name = (TextView) findViewById(R.id.text_name_name);
		btn_return = (Button) findViewById(R.id.btn_return);
		btn_return.setOnClickListener(this);
		// btn_Last = (Button) view.findViewById(R.id.btn_last);
		// btn_Next = (Button) view.findViewById(R.id.btn_next);
		btn_Down = (Button) findViewById(R.id.btn_down);
		btn_Down.setOnClickListener(this);
		// btn_Last.setOnClickListener(this);
		// btn_Next.setOnClickListener(this);
		if (getIntent() != null) {
			title = getIntent().getStringExtra("title");
			actor = getIntent().getStringExtra("actor");
			intro = getIntent().getStringExtra("description");
			years = getIntent().getStringExtra("years");
			director = getIntent().getStringExtra("director");
			thumb = getIntent().getStringExtra("thumb");
			// down_url = getArguments().getString("down_url");
			url = getIntent().getStringExtra("url");
			id = getIntent().getStringExtra("id");
			type = getIntent().getStringExtra("type");
			position = getIntent().getIntExtra("pos", 0);
			System.out.println("the postion is===>" + position);
			Logger.d(TAG, "id:" + id + "--type:" + type + "--url:" + url);
			if (null == url) {
				text_Actor.setText("李连杰、周迅");
				text_Daoyan.setText("徐克、张之亮");
				text_Year.setText("2014年12月1日");
				text_Intro.setText("《龙门飞甲》是《新龙门客栈》的续集，中国内地继香港后大中华区"
						+ "的第一部真正意义上的3D武侠电影，也成为了继粤语后华语电影史上第一部获得官方认"
						+ "证的IMAX 3D电影。由博纳影业集团出品，徐克、张之亮联合执导，徐克、"
						+ "何冀平、朱雅欐合作编剧，李连杰、周迅、陈坤、李宇春、桂纶镁、刘雅婷（刘澍颖）及范晓"
						+ "萱联袂主演该片讲述的是龙门客栈被烧毁三年后的故事，明朝西厂督主雨化田追杀怀了"
						+ "龙胎的妃子和抗击朝廷的赵怀安而来到这处客栈，当年风骚的老板娘金镶玉早已神秘失踪，"
						+ "只剩下逃过火劫的伙计们重起炉灶，痴等老板娘回来。西厂密探、鞑靼商队等江湖各路人马"
						+ "，再度相逢。几乎被世人遗忘的边关客栈，再次因缘际会地风起云涌");

			} else {

				UpdateData();
			}

		} else {
			UpdateData();
		}
		vedioImage.setBackgroundResource(Configure.image_video_Icon[position
				% Configure.image_video_Icon.length]);
		// vedioImage.setBackgroundResource(R.drawable.image_novel_one);

	}

	public void UpdateData() {

		GetContentInfoRequest vedio_info = new GetContentInfoRequest(this, id,
				type, false);
		new HttpHomeLoadDataTask(this, VedioInfoCallBack, true, "", false)
				.execute(vedio_info);
	}

	ColumnVedioInfo videoInfo;
	IUpdateData VedioInfoCallBack = new IUpdateData() {

		@Override
		public void updateUi(Object o) {
			GetContentInfoResponse videoRes = new GetContentInfoResponse();
			videoRes.paseRespones(o.toString());
			videoInfo = new ColumnVedioInfo();
			videoInfo.actor = videoRes.vedio.actor;
			videoInfo.director = videoRes.vedio.director;
			videoInfo.content = videoRes.vedio.content;
			videoInfo.title = videoRes.vedio.title;
			videoInfo.creat_time = videoRes.vedio.creat_time;
			videoInfo.name = videoRes.vedio.name;
			videoInfo.category_id = videoRes.vedio.category_id;
			videoInfo.years = videoRes.vedio.years;
			videoInfo.id = videoRes.vedio.id;
			// new BitmapThread(thumb).start();
			text_Actor.setText(videoInfo.actor);
			text_Daoyan.setText(videoInfo.director);
			text_Intro.setText(videoInfo.content);
			text_Year.setText(videoInfo.years);
			text_Name.setText(videoInfo.title);
		}

		@Override
		public void handleErrorData(String info) {
			// TODO Auto-generated method stub

		}
	};

	public class BitmapThread extends Thread {

		private String startBg;

		public BitmapThread(String start) {
			startBg = start;
		}

		@Override
		public void run() {
			try {
				Logger.log("startBg:" + startBg);
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

	Handler imageHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Bitmap bitmap = (Bitmap) msg.obj;
			// pre.setImageBitmap(bitmap);
			super.handleMessage(msg);
		}
	};

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// Fragment newContent;
		// BaseActivity base = (BaseActivity) getActivity();
		switch (v.getId()) {
		case R.id.btn_down:
			// String path = Configure.VEDIOTEST;
			//
			// Logger.d(TAG, "点击下载了，the path is====" + encode2(path));
			// path = encode2(path);
			// // String path = down_url;
			// System.out.println(Environment.getExternalStorageState() +
			// "------"
			// + Environment.MEDIA_MOUNTED);
			//
			// if (Environment.getExternalStorageState().equals(
			// Environment.MEDIA_MOUNTED)) {
			// // VideoDownDB videoDB = new VideoDownDB(getActivity());
			// // if (videoDB.select(title) == 0) {
			// // videoDB.insert(path, title);
			// // }
			// Intent intent = new Intent();
			// intent.setClass(getActivity(), DownloadFragment.class);
			// intent.putExtra("fromwhere", "vedio");
			// intent.putExtra("path", path);
			// intent.putExtra("name", title);
			// startActivity(intent);
			// getActivity().finish();
			//
			// } else {
			// // 显示SDCard错误信息
			// Toast.makeText(getActivity(), R.string.sdcarderror, 1).show();
			// }

			if (Down()) {
				Intent intent = new Intent(this, DownloadListActivity.class);
				intent.putExtra("fromWhere", 0);
				startActivity(intent);
			} else {
				Toast.makeText(this, "当前文件已经下载过了！", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		case R.id.btn_return:
			finish();
			break;
		}

	}

	// -------------------------------这是xUtils下载-------------------------------------
	// 声明一个db
	DowningVideoDB DowningDB = null;
	public String Flag = "mp4";

	// 定义一个变量来得到当前需要下载的路径
	private String path = "";
	// 定义一个变量来得到需要下载文件的名字
	private String down_filename = "";
	private DownloadManager downloadManager;

	public boolean Down() {
		if (null == DowningDB) {
			DowningDB = new DowningVideoDB(this);
		}
		int i = (int) (Math.random() * 5 + 1);
		path = Configure.VedioPath[i];
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
