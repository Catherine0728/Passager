package com.ky.mainactivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ky.beaninfo.ColumnNewsInfo;
import com.ky.beaninfo.ColumnVedioInfo;
import com.ky.passenger.R;
import com.ky.request.GetContentInfoRequest;
import com.ky.request.HttpHomeLoadDataTask;
import com.ky.response.GetContentInfoResponse;
import com.ky.response.GetNewsContentInfoResponse;
import com.ky.utills.Configure;
import com.lhl.callback.IUpdateData;
import com.redbull.log.Logger;

import android.app.Activity;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 新闻详情的页面
 * 
 * @author Catherine.Brain
 * */
public class NewDetailActivity extends Activity implements OnClickListener {
	public static String TAG = "NewDetailActivity";
	public static String FRAGMENTNAME = "新闻";

	// View view = null;
	TextView text_Title, text_Time, text_Src, text_Con;

	// ImageView image_Bg;
	// Button btn_Last, btn_Next;
	Button btn_return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		initView();
	}

	String title, content, time, src, id, type, url;

	public void initView() {
		Configure.PAGER = 6;
		text_Title = (TextView) findViewById(R.id.text_title);
		text_Time = (TextView) findViewById(R.id.text_time);
		text_Src = (TextView) findViewById(R.id.text_src);
		text_Con = (TextView) findViewById(R.id.text_content);
		btn_return = (Button) findViewById(R.id.btn_return);
		btn_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		if (getIntent() != null) {
			title = getIntent().getStringExtra("title");
			src = getIntent().getStringExtra("src");
			content = getIntent().getStringExtra("description");
			time = getIntent().getStringExtra("time");
			url = getIntent().getStringExtra("url");
			id = getIntent().getStringExtra("id");
			type = getIntent().getStringExtra("type");
			if (null == url) {
				text_Title.setText(title);
				text_Time.setText("发布时间：" + time);
				text_Src.setText(src);
				text_Con.setText(content);
			} else {
				UpdateData();

			}

		} else {
			UpdateData();
		}

	}

	public void UpdateData() {

		GetContentInfoRequest vedio_info = new GetContentInfoRequest(this, id,
				type, false);
		new HttpHomeLoadDataTask(this, VedioInfoCallBack, true, "", false)
				.execute(vedio_info);
	}

	ColumnNewsInfo news;
	IUpdateData VedioInfoCallBack = new IUpdateData() {

		@Override
		public void updateUi(Object o) {
			GetNewsContentInfoResponse newsRes = new GetNewsContentInfoResponse();
			newsRes.paseRespones(o.toString());
			news = new ColumnNewsInfo();
			news.title = newsRes.news.title;
			news.creat_time = newsRes.news.creat_time;
			news.name = newsRes.news.name;
			news.category_id = newsRes.news.category_id;
			news.descriptrion = newsRes.news.descriptrion;
			news.content = newsRes.news.content;
			news.id = newsRes.news.id;
			text_Title.setText(news.title);
			text_Time.setText(DataFormat(news.creat_time));
			text_Src.setText("百度新闻客户端");
			text_Con.setText(news.content);

		}

		@Override
		public void handleErrorData(String info) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onStop() {
		Logger.d(TAG, "onStop");
		super.onStop();
	}

	@Override
	public void onResume() {
		Logger.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

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
