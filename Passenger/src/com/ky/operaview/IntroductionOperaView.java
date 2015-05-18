package com.ky.operaview;

import com.ky.passenger.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 本书简介
 * 
 * @author Catherine.Brain 2014/9/25-10-08
 * */
public class IntroductionOperaView extends LinearLayout {
	private Context mContext;
	IntroductionOperaView instanse;
	View view;
	private TextView text_Intro;

	public IntroductionOperaView(Context context) {
		super(context);
		mContext = context;
	}

	public View initView(String str) {
		view = LayoutInflater.from(mContext).inflate(R.layout.introduction,
				null);
		text_Intro = (TextView) view.findViewById(R.id.text_intro);
		return view;

	}

	public void operaView(String str) {

		text_Intro.setText(str.toString());
	}

}
