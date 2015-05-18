package com.ky.mainactivity;

import com.ky.passenger.R;
import com.ky.utills.Configure;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * this is feedback
 * 
 * @author Catherine.Brain
 * */
public class FeedBackFragment extends Fragment implements OnClickListener {
	View view = null;
	public static EditText edit_Mess, edit_Eamil;
	Button btn_Send;
	public static TextView text_error;
	public static String FRAGMENTNAME = "反馈";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == view) {
			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.feedback, null);
		}
		ViewGroup p = (ViewGroup) view.getParent();
		if (p != null) {
			p.removeView(view);
		}
		initView();
		return view;
	}

	public void initView() {
		Configure.PAGER=6;	
		BaseActivity.btn_Return.setVisibility(View.VISIBLE);
		BaseActivity.btn_logo.setVisibility(View.GONE);
		BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		edit_Eamil = (EditText) view.findViewById(R.id.edit_email);
		edit_Mess = (EditText) view.findViewById(R.id.edit_mess);
		btn_Send = (Button) view.findViewById(R.id.btn_send);
		text_error = (TextView) view.findViewById(R.id.text_error);
		btn_Send.setOnClickListener(this);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	BaseActivity base = (BaseActivity) getActivity();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			String getMes = edit_Mess.getText().toString().trim();
			String getEamil = edit_Eamil.getText().toString().trim();
			if (null == getMes || null == getEamil || "".equals(getMes)
					|| "".equals(getEamil)) {
				text_error.setVisibility(View.VISIBLE);

			} else {
				text_error.setVisibility(View.INVISIBLE);
				Fragment newContent = new MySelfOutFragment();
				BaseActivity base = (BaseActivity) getActivity();
				base.switchContent(newContent, "我的", false);
			}

			break;

		default:
			break;
		}

	}

}
