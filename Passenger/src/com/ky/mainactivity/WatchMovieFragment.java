package com.ky.mainactivity;

import com.ky.passenger.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * this is watch movie in the scene
 * 
 * @author Catherine.Brain
 * */
public class WatchMovieFragment extends Fragment implements OnClickListener {
	View view = null;
	TextView text_Daoyan, text_Actor, text_Year, text_Title;
	Button btn_Road_Watch, btn_Same_Voice, btn_Play_Screen;
	public static String FRAGMENTNAME = "电影";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == view) {
			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.watch_moive, null);
		}
		ViewGroup p = (ViewGroup) view.getParent();
		if (p != null) {
			p.removeView(view);
		}
		initView();
		return view;
	}

	public void initView() {
		BaseActivity.btn_Return.setVisibility(View.VISIBLE);
		BaseActivity.btn_logo.setVisibility(View.GONE);
		BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		text_Actor = (TextView) view.findViewById(R.id.text_actor_name);
		text_Daoyan = (TextView) view.findViewById(R.id.text_daoyan_name);
		text_Year = (TextView) view.findViewById(R.id.text_years_time);
		text_Title = (TextView) view.findViewById(R.id.text_title);
		btn_Play_Screen = (Button) view.findViewById(R.id.btn_play_screen);
		btn_Same_Voice = (Button) view.findViewById(R.id.btn_same_voice);
		btn_Road_Watch = (Button) view.findViewById(R.id.btn_road);
		btn_Play_Screen.setOnClickListener(this);
		btn_Same_Voice.setOnClickListener(this);
		btn_Road_Watch.setOnClickListener(this);
		text_Title.setText("鬼吹灯");
		text_Actor.setText("悬疑");
		text_Daoyan.setText("天下霸唱");
		text_Year.setText("2014年12月12日");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		Fragment newContent;
		BaseActivity base = (BaseActivity) getActivity();
		switch (v.getId()) {
		case R.id.btn_play_screen:
			// newContent = new BarrageFragment();
			// base.switchContent(newContent, "弹幕", true);
			startActivity(new Intent().setClass(getActivity(),
					BarrageFragment.class));
			getActivity().finish();

			break;
		case R.id.btn_same_voice:
			newContent = new ListenMovieFragment();
			base.switchContent(newContent, "听电影", true);

			break;
		case R.id.btn_road:
			// newContent = new DownloadFragment();
			// Bundle bundle = new Bundle();
			// bundle.putString("fromwhere", "watch");
			// newContent.setArguments(bundle);
			// base.switchContent(newContent, "删除", true);
			Intent intent = new Intent();
			intent.setClass(getActivity(), DownloadFragment.class);
			intent.putExtra("fromwhere", "base");
			startActivity(intent);
			getActivity().finish();
			break;

		default:
			break;
		}

	}

}
