package com.ky.mainactivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sf.hmg.turntest.turntest;

import com.ky.beaninfo.ColumnNovelInfo;
import com.ky.beaninfo.ColumnNovelList;
import com.ky.beaninfo.ColumnVedioInfo;
import com.ky.operaview.IntroductionOperaView;
//import com.ky.operaview.NovelViewFlipper;
import com.ky.operaview.SeriseOperaView;
import com.ky.passenger.R;
import com.ky.request.GetContentInfoRequest;
import com.ky.request.GetContentNovelInfoRequest;
import com.ky.request.HttpHomeLoadDataTask;
import com.ky.response.GetContentNovelInfoResponse;
import com.ky.response.GetNovelChapterInfoResponse;
import com.ky.utills.Configure;
import com.lhl.callback.IUpdateData;
import com.redbull.log.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 
 * 小说详情的页面（id，type）
 * 
 * @author Catherine.Brain
 * */
public class NovelDetailActivity extends Activity implements OnClickListener {
	String TAG = "NovelDetailActivity";
	// LinearLayout layout_Fliter;
	Button btn_Down, btn_Scan;
	// btn_Last, btn_Next,
	TextView text_Daoyan, text_Year, text_Title;
	// ,
	TextView text_Intro;
	public static String FRAGMENTNAME = "小说";
	// private ArrayList<Map<String, Object>> serise_List = null;
	RadioGroup radioGroup;
	RadioButton btn_one, btn_two;
	SeriseOperaView serise;
	IntroductionOperaView intromation;
	ImageView video_Image;
	Button btn_Return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novel_detail);
		initView();
	}

	String title, daoyan, year, intro, description, id, type, url, thumb;
	// 定义一个变量来得到当前小说的下标
	int postion = 0;

	public void initView() {
		Configure.PAGER = 6;
		BaseActivity.btn_Return.setVisibility(View.VISIBLE);
		BaseActivity.btn_logo.setVisibility(View.GONE);
		BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		// layout_Fliter = (LinearLayout) view.findViewById(R.id.layout_fliter);
		btn_Down = (Button) findViewById(R.id.btn_down);
		video_Image = (ImageView) findViewById(R.id.video_image);
		// btn_Last = (Button) view.findViewById(R.id.btn_last);
		// btn_Next = (Button) view.findViewById(R.id.btn_next);
		btn_Scan = (Button) findViewById(R.id.btn_scan);
		// text_Actor = (TextView) view.findViewById(R.id.text_actor_name);
		text_Daoyan = (TextView) findViewById(R.id.text_daoyan_name);
		text_Year = (TextView) findViewById(R.id.text_years_time);
		text_Title = (TextView) findViewById(R.id.text_title);
		btn_Return = (Button) findViewById(R.id.btn_return);
		btn_Return.setOnClickListener(this);
		btn_Down.setOnClickListener(this);
		btn_Scan.setOnClickListener(this);

		if (getIntent() != null) {
			// title = getArguments().getString("title");
			// year = getArguments().getString("year");
			// daoyan = getArguments().getString("daoyan");
			// intro = getArguments().getString("intro");
			// type = getArguments().getString("type");
			url = getIntent().getStringExtra("url");
			id = getIntent().getStringExtra("id");
			type = getIntent().getStringExtra("type");
			postion = getIntent().getIntExtra("pos", 0);
			// thumb = getArguments().getString("thumb");
			Logger.d(TAG, "id:" + id + "--type:" + type + "--url:" + url);
			text_Title.setText("鬼吹灯");
			// text_Actor.setText("悬疑");
			text_Daoyan.setText("天下霸唱");
			text_Year.setText("2014年12月12日");
			if (null == url) {

			} else {
				UpdateData();

			}

		} else {
			UpdateData();
		}
		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		btn_one = (RadioButton) findViewById(R.id.btn_list);
		btn_two = (RadioButton) findViewById(R.id.btn_intro);
		btn_one.setOnClickListener(this);
		btn_two.setOnClickListener(this);
		btn_two.setTextColor(0xFF0000FF);
		text_Intro = (TextView) findViewById(R.id.text_intro);
		video_Image.setBackgroundResource(Configure.image_novel_icon[postion
				% Configure.image_novel_icon.length]);

	}

	public void SetViewFilpper(String into) {

		// 简介
		intromation = new IntroductionOperaView(this);
		View iView = intromation.initView(into);
		intromation.operaView(novelList.description);
		// 剧集
		serise = new SeriseOperaView(this);
		View sView = serise.initView();
		serise.operaView(list_chapter, id, type, url, novelList.description);
		View[] iv = { iView, sView };
		for (int i = 0; i < iv.length; i++) { // 添加图片
			// View iv = LayoutInflater.from(mContext).inflate(imgs[i], null);
			viewFlipper.addView(iv[i], new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		}

		// if (viewFlipper.isAutoStart() && !viewFlipper.isFlipping()) {
		// viewFlipper.startFlipping();
		// }
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int position) {
				ChangeView(position);
			}
		});
	}

	public void UpdateData() {

		GetContentInfoRequest novel_info = new GetContentInfoRequest(this, id,
				type, true);
		new HttpHomeLoadDataTask(this, NovelCallBack, true, "", false)
				.execute(novel_info);
	}

	ColumnNovelInfo novelInfo;
	ColumnNovelList novelList;
	ArrayList<ColumnNovelInfo> list_chapter = new ArrayList<ColumnNovelInfo>();
	IUpdateData NovelCallBack = new IUpdateData() {

		@Override
		public void updateUi(Object o) {
			GetContentNovelInfoResponse novelRes = new GetContentNovelInfoResponse();
			novelRes.paseRespones(o.toString());
			novelList = new ColumnNovelList();
			novelList.id = novelRes.novel.id;
			novelList.name = novelRes.novel.name;
			novelList.title = novelRes.novel.title;
			novelList.description = novelRes.novel.description;
			novelList.category_id = novelRes.novel.category_id;
			novelList.create_time = novelRes.novel.create_time;
			novelList.url = novelRes.novel.url;
			novelList.count = novelRes.novel.count;
			for (int i = 0; i < novelRes.infoList.size(); i++) {
				novelInfo = new ColumnNovelInfo();
				// novelInfo.actor = videoRes.vedio.actor;
				novelInfo.url = novelRes.infoList.get(i).url;
				// novelInfo.content = videoRes.vedio.content;
				novelInfo.chapter = novelRes.infoList.get(i).chapter;
				novelInfo.chapter_title = novelRes.infoList.get(i).chapter_title;
				novelInfo.type = novelRes.novel.type;
				// novelInfo.count = novelRes.novel.count;
				novelInfo.novel_id = novelRes.infoList.get(i).novel_id;
				novelInfo.id = novelRes.infoList.get(i).id;
				// new BitmapThread(thumb).start();
				list_chapter.add(novelInfo);
			}
			for (int j = 0; j < list_chapter.size(); j++) {
				Logger.d(TAG, "the list_chapter is==>" + list_chapter);

			}
			text_Title.setText(novelList.title);
			text_Year.setText(DataFormat(novelList.create_time));
			text_Daoyan.setText(novelList.name);
			SetViewFilpper(novelList.description);

		}

		@Override
		public void handleErrorData(String info) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_down:
			Intent intent = new Intent();
			intent.setClass(this, DownloadFragment.class);
			intent.putExtra("fromwhere", "novel");
			startActivity(intent);
			finish();
			break;
		case R.id.btn_scan:
			GetContents(novelList.id, novelList.type, novelList.url,
					novelList.chapter);

			break;
		case R.id.btn_return:
			finish();
			break;

		default:
			break;
		}

	}

	private ViewFlipper viewFlipper = null;

	public void ChangeView(int v) {
		switch (v) {
		case R.id.btn_intro:// intro
			viewFlipper.setDisplayedChild(0);
			intromation.operaView(novelList.description);

			btn_one.setTextColor(0xFFFFFFFF);

			btn_two.setTextColor(0xFF0000FF);
			break;
		case R.id.btn_list:// serise
			viewFlipper.setDisplayedChild(1);
			serise.operaView(list_chapter, id, type, url, novelList.description);
			btn_one.setTextColor(0xFF0000FF);
			btn_two.setTextColor(0xFFFFFFFF);

			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * 根据小说的chapter以及row去获得当前小说的contents
	 * */
	public void GetContents(String id, String type, String url, String cha) {
		GetContentNovelInfoRequest novel_info = new GetContentNovelInfoRequest(
				this, id, type, "1", "2", cha);
		new HttpHomeLoadDataTask(this, GetNovelInfoCallBack, false, "", false)
				.execute(novel_info);
	}

	ColumnNovelInfo getNovelInfo;
	ArrayList<ColumnNovelInfo> GetnovelInfoList = new ArrayList<ColumnNovelInfo>();
	IUpdateData GetNovelInfoCallBack = new IUpdateData() {

		@Override
		public void updateUi(Object o) {
			GetNovelChapterInfoResponse novelRes = new GetNovelChapterInfoResponse();
			novelRes.paseRespones(o.toString());

			for (int i = 0; i < novelRes.infoList.size(); i++) {
				getNovelInfo = new ColumnNovelInfo();
				// novelInfo.title = novelRes.novel.title;
				getNovelInfo.novel_id = novelRes.infoList.get(i).novel_id;
				// novelInfo.name = novelRes.novel.name;
				getNovelInfo.chapter = novelRes.infoList.get(i).chapter;
				getNovelInfo.chapter_title = novelRes.infoList.get(i).chapter_title;
				getNovelInfo.contents = novelRes.infoList.get(i).contents;
				getNovelInfo.id = novelRes.infoList.get(i).id;
				GetnovelInfoList.add(getNovelInfo);
			}

			Logger.d(TAG, "the contents is===>" + getNovelInfo.contents);
			myHandler.sendEmptyMessageAtTime(SUCCESS, 2000);
		}

		@Override
		public void handleErrorData(String info) {
			Logger.e(TAG, info);

		}
	};
	public static final int SUCCESS = 0x000001;
	public static final int FAILED = 0x000002;
	Handler myHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				if (getNovelInfo.contents.equals("")
						|| null == getNovelInfo.contents) {
					Toast.makeText(NovelDetailActivity.this, "暂时没有小说内容，抱歉",
							Toast.LENGTH_SHORT).show();
				} else {

					Intent intent = new Intent();
					intent.setClass(NovelDetailActivity.this, turntest.class);
					intent.putExtra("contents", getNovelInfo.contents);
					startActivity(intent);
				}

				Logger.d(TAG, "the id==>" + id + "====type:" + type + "====url"
						+ url + "----chapter" + novelInfo.chapter);
				break;

			default:
				break;
			}
		};
	};

	public String DataFormat(String time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = null;
		if (time.equals("")) {
			return "";
		}
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long loc_time = Long.valueOf(time);
		re_StrTime = sdf.format(new Date(loc_time * 1000L));
		return re_StrTime;
	}

}
