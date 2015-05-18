package com.ky.mainactivity;

import com.ky.passenger.R;
import com.ky.utills.Configure;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * this is about us
 * 
 * @author Catherine.Brain
 * */
public class AboutUsFragment extends Fragment {
	View view = null;
	ImageView image_About_Us;
	TextView text_About_Us;
	public static String FRAGMENTNAME = "关于";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == view) {
			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.about_us, null);
		}
		ViewGroup p = (ViewGroup) view.getParent();
		if (p != null) {
			p.removeView(view);
		}
		initView();
		return view;
	}

	public void initView() {
		Configure.PAGER = 6;
		BaseActivity.btn_Return.setVisibility(View.VISIBLE);
		BaseActivity.btn_logo.setVisibility(View.GONE);
		BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		image_About_Us = (ImageView) view.findViewById(R.id.image_about_us);
		text_About_Us = (TextView) view.findViewById(R.id.text_intro);
		text_About_Us.setText("你好，小编，知道我们是谁吗？不知道呀，不知道没关系哦，关键是您现在"
				+ "在用我们公司的项目，所以呢，您觉得我们这个项目怎么样，是不是还有什么不足的地方，如果有的话，您一定"
				+ "要积极参与我们的互动，然后，将您的要求以及需求说出来，我们一定尽力的配合去改！");

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
