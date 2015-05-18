package com.ky.mainactivity;

import com.android.listener.IsSuccessListener;
import com.ky.beaninfo.UserInfo;
import com.ky.db.IsLoginDB;
import com.ky.passenger.R;
import com.ky.request.HttpHomeLoadDataTask;
import com.ky.request.RegisterRequest;
import com.ky.response.RegisterResponse;
import com.ky.utills.Configure;
import com.lhl.callback.IUpdateData;
//import com.lhl.dialog.library.AlertDialog;
import com.redbull.log.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * this is login
 * 
 * @author Catherine.Brain
 * */
public class LoginFragment extends Activity implements OnClickListener {
	String TAG = "LoginFragment";
	View view = null;
	Button btn_Login;
	TextView text_Register, text_For;
	public static String FRAGMENTNAME = "登录";
	EditText edit_userName, edit_Pass;
	String userName, PassWord;
	Context mCotnext;
	LinearLayout layout_Touch;
	TextView text_error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mCotnext = this.getApplicationContext();
		initView();
	}

	public void initView() {
		text_error = (TextView) findViewById(R.id.text_error);
		// BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		btn_Login = (Button) findViewById(R.id.btn_Login);
		text_For = (TextView) findViewById(R.id.text_forget);
		text_Register = (TextView) findViewById(R.id.text_register);
		edit_Pass = (EditText) findViewById(R.id.edit_Pass);
		// edit_Pass.setTransformationMethod(new WordsReplace());
		edit_userName = (EditText) findViewById(R.id.edit_username);
		layout_Touch = (LinearLayout) findViewById(R.id.layout_touch);
		layout_Touch.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Logger.d(TAG, "我触摸了");
				close();
				return false;
			}
		});
		
		btn_Login.setOnClickListener(this);
		text_For.setOnClickListener(this);
		text_Register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// Fragment newContent;
		// BaseActivity base = (BaseActivity) mCotnext;
		switch (v.getId()) {
		case R.id.btn_Login:
			userName = edit_userName.getText().toString().trim();
			PassWord = edit_Pass.getText().toString().trim();
			if (userName.equals("")) {
				text_error.setText("用户名不能为空");
				text_error.setVisibility(View.VISIBLE);

			} else if (PassWord.equals("")) {
				text_error.setText("密码不能为空");
				text_error.setVisibility(View.VISIBLE);
			} else {
				PostData();

			}

			break;
		case R.id.text_forget:
			// newContent = new ForPasswordFragment();
			// base.switchContent(newContent, "设置密码", false);
			finish();
			break;
		case R.id.text_register:
			startActivity(new Intent().setClass(mCotnext,
					RegisterFragment.class));
			finish();
			// newContent = new RegisterFragment();
			// base.switchContent(newContent, "注册", false);
			break;
		default:
			break;
		}

	}

	// Fragment newContent;
	// BaseActivity base = (BaseActivity) mCotnext;

	public void PostData() {
		RegisterRequest login = new RegisterRequest(this, userName, "",
				PassWord, "", true);
		if (Configure.ISOUTNET) {
			new HttpHomeLoadDataTask(this, LoginCallBack, false,
					Configure.OUTHomePath
							+ "primary/1.0/RegUser.php?ma=login", false)
					.execute(login);
		} else {
			new HttpHomeLoadDataTask(this, LoginCallBack, false,
					Configure.NETHomePath
							+ "primary/1.0/RegUser.php?ma=login", false)
					.execute(login);

		}

	}

	UserInfo userinfo;
	IUpdateData LoginCallBack = new IUpdateData() {

		@Override
		public void updateUi(Object o) {
			RegisterResponse login_response = new RegisterResponse(true);
			login_response.paseRespones(o.toString());
			userinfo = new UserInfo();
			userinfo.id = login_response.useInfo.id;
			userinfo.username = login_response.useInfo.username;
			userinfo.email = login_response.useInfo.email;
			userinfo.mobile = login_response.useInfo.mobile;
			userinfo.status = login_response.useInfo.status;
			userinfo.point = login_response.useInfo.point;
			userinfo.key = login_response.useInfo.key;
			Configure.key = userinfo.key;

			IsLoginDB islogin = new IsLoginDB(mCotnext);
			if (islogin.select(userinfo.id) == 0) {
				islogin.insert(userinfo.id, userName, userinfo.mobile,
						userinfo.email, userinfo.status, userinfo.key,
						Integer.parseInt(userinfo.point));
			} else {
				islogin.update(userinfo.id, userName, userinfo.mobile,
						userinfo.email, userinfo.status, userinfo.key,
						Integer.parseInt(userinfo.point));
			}
			// new AlertDialog(LoginFragment.this, AlertDialog.SUCCESS_LOGIN,
			// isSuccess).setTitleText("登录成功").show();
			Intent intent = new Intent();
			intent.setClass(LoginFragment.this, BaseActivity.class);
			intent.putExtra("tag", "myself");
			startActivity(intent);

			finish();
		}

		@Override
		public void handleErrorData(String info) {
			Logger.e(TAG, info);
			if (info.equals("登录失败")) {
				// new AlertDialog(LoginFragment.this, AlertDialog.FAILED_LOGIN,
				// isSuccess).setTitleText("登录失败").show();
				text_error.setText(info);
				text_error.setVisibility(View.VISIBLE);
			} else {

			}

		}
	};
	// String user_Id;
	public static final int SUCCESS = 0x000001;
	public static final int FAILED = 0x000002;
	public static final int GOREGISTER = 0x000003;
	public static final int GOSELF = 0x000004;
	IsSuccessListener isSuccess = new IsSuccessListener() {

		@Override
		public void IsSuccess(Boolean isExit) {
			if (isExit) {
				Intent intent = new Intent();
				intent.setClass(LoginFragment.this, BaseActivity.class);
				intent.putExtra("tag", "myself");
				startActivity(intent);

				finish();
			} else {

			}

		}
	};

	/**
	 * 
	 * 关闭软键盘
	 * **/
	public void close() {

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_Pass.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_userName.getWindowToken(), 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		} else {

			return super.onKeyDown(keyCode, event);
		}

	}

}
