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
 * this is set the password
 * 
 * @author Catherine.Brain
 * */
public class SetPasswordFragment extends Fragment implements OnClickListener {
	View view = null;
	Button btn_Okay;
	public static String FRAGMENTNAME = "设置";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == view) {
			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.set_password, null);
		}
		ViewGroup p = (ViewGroup) view.getParent();
		if (p != null) {
			p.removeView(view);
		}
		initView();
		return view;
	}

	public void initView() {
		BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		btn_Okay = (Button) view.findViewById(R.id.btn_okay);
		btn_Okay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Fragment newContent;
		BaseActivity base = (BaseActivity) getActivity();
		switch (v.getId()) {
		case R.id.btn_okay:
			newContent = new MySelfInFragment();
			Bundle bundle = new Bundle();
			bundle.putString("userName", "lhl");
			bundle.putString("point", "34");
			newContent.setArguments(bundle);
			base.switchContent(newContent, "我的",false);
			break;

		default:
			break;
		}

	}

}
