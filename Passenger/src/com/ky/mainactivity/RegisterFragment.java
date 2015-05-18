package com.ky.mainactivity;

import java.util.Random;

import com.android.listener.IsSuccessListener;
import com.ky.beaninfo.UserInfo;
import com.ky.passenger.R;
import com.ky.request.HttpHomeLoadDataTask;
import com.ky.request.RegisterRequest;
import com.ky.response.RegisterResponse;
import com.lhl.callback.IUpdateData;
import com.redbull.log.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * this is register
 * 
 * @author Catherine.Brain
 * */
public class RegisterFragment extends Activity implements OnClickListener {
	String TAG = "RegisterFragment";
	View view = null;
	Button btn_Register, btn_GotCode;
	public static String FRAGMENTNAME = "注册";
	EditText text_Name, text_pass, text_phoneNumber;
	String userName, pass, phoneNumber;
	Context mContext;
	LinearLayout layout_Touch;
	TextView text_error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		initView();
		mContext = this.getApplicationContext();
	}

	public void initView() {
		text_error = (TextView) findViewById(R.id.text_error);
		// BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		btn_Register = (Button) findViewById(R.id.btn_register);
		btn_Register.setOnClickListener(this);
		// btn_GotCode = (Button) view.findViewById(R.id.btn_get_code);
		// btn_GotCode.setOnClickListener(this);
		text_Name = (EditText) findViewById(R.id.edit_username);
		text_pass = (EditText) findViewById(R.id.edit_Pass);
		// text_rePass = (EditText) findViewById(R.id.edit_RePass);
		// text_rePass.setTransformationMethod(new WordsReplace());
		// text_pass.setTransformationMethod(new WordsReplace());
		text_phoneNumber = (EditText) findViewById(R.id.edit_Number);

		layout_Touch = (LinearLayout) findViewById(R.id.layout_touch);
		layout_Touch.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Logger.d(TAG, "我触摸了");
				close();
				return false;
			}
		});
		layout_Touch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Logger.d(TAG, "我点击了");
				close();
			}
		});
		text_phoneNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count == 11) {
					btn_Register.setFocusable(true);
					btn_Register.setFocusableInTouchMode(true);
					btn_Register.requestFocus();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		// Fragment newContent;
		// BaseActivity base = (BaseActivity) mContext;
		switch (v.getId()) {
		case R.id.btn_get_code:

			break;

		case R.id.btn_register:
			close();
			userName = text_Name.getText().toString().trim();
			pass = text_pass.getText().toString().trim();
			// rePass = text_rePass.getText().toString().trim();
			phoneNumber = text_phoneNumber.getText().toString().trim();
			System.out.println("the username is==>" + userName
					+ "the pass is===>" + pass + "the phonenumber is===>"
					+ phoneNumber);
			if (userName.equals("")) {
				text_error.setText("用户名不能为空");
				text_error.setVisibility(View.VISIBLE);
			} else if (phoneNumber.equals("")) {
				text_error.setText("电话号码不能为空");
				text_error.setVisibility(View.VISIBLE);
			} else if (pass.equals("")) {
				text_error.setText("密码不能为空");
				text_error.setVisibility(View.VISIBLE);
			} else if (phoneNumber.length() < 10) {
				text_error.setText("手机号码位数不够，请检查");
				text_error.setVisibility(View.VISIBLE);
			}

			else {
				// 然后向服务器提交数据
				PostData();

			}

			break;
		default:
			break;
		}

	}

	public void PostData() {
		// 提交用户名，密码，电话号码
		int email = getEmail();
		RegisterRequest register = new RegisterRequest(mContext, userName,
				phoneNumber, pass, Math.abs(email) + "@163.com", false);
		new HttpHomeLoadDataTask(mContext, RegisterCallBack, false, "", false)
				.execute(register);
	}

	// String user_Id;
	IUpdateData RegisterCallBack = new IUpdateData() {

		@Override
		public void updateUi(Object o) {
			RegisterResponse register_response = new RegisterResponse(false);
			register_response.paseRespones(o.toString());
			UserInfo userinfo = new UserInfo();
			userinfo.id = register_response.useInfo.id;
			// new AlertDialog(RegisterFragment.this, AlertDialog.SUCCESS_REG,
			// isSuccess).setTitleText("注册成功，前去登录！").show();

			// dialogToShow();
			startActivity(new Intent().setClass(RegisterFragment.this,
					LoginFragment.class));
			RegisterFragment.this.finish();
		}

		@Override
		public void handleErrorData(String info) {
			// TODO Auto-generated method stub
			Logger.e(TAG, info);
			if (info.equals("注册失败")) {
				// new AlertDialog(RegisterFragment.this,
				// AlertDialog.SUCCESS_REG,
				// isSuccess).setTitleText("注册失败，请重试").show();
				text_error.setText(info);
				text_error.setVisibility(View.VISIBLE);
				text_pass.setText("");
				text_Name.setText("");
				text_phoneNumber.setText("");

				text_Name.setFocusable(true);
				text_Name.setFocusableInTouchMode(true);
			}

		}
	};

	public int getEmail() {
		int email = 0;
		Random rand = new Random();
		email = (rand.nextInt() % 100000000) * 1000000;
		Logger.d(TAG, "the email is===>" + email);
		return email;

	}

	IsSuccessListener isSuccess = new IsSuccessListener() {

		@Override
		public void IsSuccess(Boolean isExit) {
			if (isExit) {
				startActivity(new Intent().setClass(RegisterFragment.this,
						LoginFragment.class));
				finish();
			} else {
			}
		}
	};

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

	public void dialogToShow() {
		Logger.log("dialogToShow<><><><><><> ");
		final AlertDialog mProgress = new AlertDialog.Builder(
				RegisterFragment.this).create();
		mProgress.show();
		mProgress.getWindow().setContentView(R.layout.app_before_dialog);
		new Thread() {
			public void run() {
				try {
					sleep(2000);

				} catch (InterruptedException e) {
					Logger.log("error:" + e.toString());
				} finally {
					Logger.log("mProgress dismiss<><><><><><> ");
					mProgress.dismiss();
					startActivity(new Intent().setClass(RegisterFragment.this,
							BaseActivity.class));
					RegisterFragment.this.finish();
				}
			}
		}.start();
	}

	/**
	 * 
	 * 关闭软键盘
	 * **/
	public void close() {

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(text_Name.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(text_pass.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(text_phoneNumber.getWindowToken(), 0);
	}
}
