package com.ky.mainactivity;

import com.ky.passenger.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * this is forget password
 * 
 * @author Catherine.Brain
 * */
public class ForPasswordFragment extends Fragment implements OnClickListener {
	View view = null;
	Button btn_Next, btn_getCode;
	public static String FRAGMENTNAME="密码";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == view) {
			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.for_password, null);
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
		btn_Next = (Button) view.findViewById(R.id.btn_next);
		btn_Next.setOnClickListener(this);
		btn_getCode = (Button) view.findViewById(R.id.btn_get_code);
		btn_getCode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		BaseActivity base = (BaseActivity) getActivity();
		switch (v.getId()) {
		case R.id.btn_next:
			newContent = new SetPasswordFragment();
			base.switchContent(newContent,"设置密码",false);
			break;
		case R.id.btn_get_code:

			break;
		default:
			break;
		}

	}

}
