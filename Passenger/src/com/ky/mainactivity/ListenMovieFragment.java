package com.ky.mainactivity;

import com.ky.passenger.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * this is listen movie
 * 
 * @author Catherine.Brain
 * */
public class ListenMovieFragment extends Fragment {
	View view = null;
	TextView text_Daoyan, text_Actor, text_Year, text_Title, text_pro;
	LinearLayout layout_Pro, layout_Failed, layout_Success;
	public static String FRAGMENTNAME = "电影";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == view) {
			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.listen_moive, null);
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
		text_pro = (TextView) view.findViewById(R.id.text_same_voice_pro);
		layout_Failed = (LinearLayout) view.findViewById(R.id.layout_failed);
		layout_Pro = (LinearLayout) view.findViewById(R.id.layout_process);
		// layout_Success=(LinearLayout) view.findViewById(R.id.layout_success);
		text_pro.setText("请到前台领取收音机或者打开手机收音机进行声音的同步....");
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

}
