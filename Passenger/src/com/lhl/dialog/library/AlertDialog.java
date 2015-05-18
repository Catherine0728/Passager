package com.lhl.dialog.library;

import android.R.layout;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import com.android.listener.IsSuccessListener;
import com.ky.mainactivity.BaseActivity;
import com.ky.mainactivity.LoginFragment;
import com.ky.mainactivity.MySelfInFragment;
import com.ky.mainactivity.RegisterFragment;
import com.ky.passenger.R;
import com.redbull.log.Logger;

public class AlertDialog extends Dialog implements View.OnClickListener {
	private View mDialogView;
	private AnimationSet mModalInAnim;
	private AnimationSet mModalOutAnim;
	private Animation mOverlayOutAnim;
	private Animation mErrorInAnim;
	private AnimationSet mErrorXInAnim;
	private AnimationSet mSuccessLayoutAnimSet;
	private Animation mSuccessBowAnim;
	private int mAlertType;

	/* 去写我自己的东西 */

	TextView text_Result, text_Left_Point, text_isLogin, text_Toast,
			text_Register;
	Button btn_Login, btn_Sure, btn_Exit;
	// ListView list_point;
	RadioGroup redioGroup;
	LinearLayout layout_btn;
	LinearLayout layout_isregister;
	public static final int NOLOGIN_TYPE = 0;
	public static final int POINT_TYPE = 1;
	public static final int SUCCESS_TYPE = 2;
	public static final int FAILED_TYPE = 3;
	public static final int ALERT_TYPE = 4;
	public static final int SUCC_BUY = 5;
	public static final int SUCCESS_REG = 6;
	public static final int SUCCESS_LOGIN = 7;
	public static final int FAILED_LOGIN = 8;
	public static final int FAILED_REG = 9;
	static IsSuccessListener issuccess;

	public static interface OnSweetClickListener {
		public void onClick(AlertDialog sweetAlertDialog);
	}

	public Context mContext;

	public AlertDialog(Context context) {

		this(context, NOLOGIN_TYPE, issuccess);

	}

	public AlertDialog(Context context, int alertType,
			IsSuccessListener isSuccess) {
		super(context, R.style.alert_dialog);
		mContext = context;
		setCancelable(true);
		setCanceledOnTouchOutside(false);
		mAlertType = alertType;
		this.issuccess = isSuccess;
		mErrorInAnim = OptAnimationLoader.loadAnimation(getContext(),
				R.anim.error_frame_in);
		mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
				getContext(), R.anim.error_x_in);
		// 2.3.x system don't support alpha-animation on layer-list drawable
		// remove it from animation set
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			List<Animation> childAnims = mErrorXInAnim.getAnimations();
			int idx = 0;
			for (; idx < childAnims.size(); idx++) {
				if (childAnims.get(idx) instanceof AlphaAnimation) {
					break;
				}
			}
			if (idx < childAnims.size()) {
				childAnims.remove(idx);
			}
		}
		mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(),
				R.anim.success_bow_roate);
		mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader
				.loadAnimation(getContext(), R.anim.success_mask_layout);
		mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
				getContext(), R.anim.modal_in);
		mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
				getContext(), R.anim.modal_out);
		mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mDialogView.setVisibility(View.GONE);
				mDialogView.post(new Runnable() {
					@Override
					public void run() {
						AlertDialog.super.dismiss();
					}
				});
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		// dialog overlay fade out
		mOverlayOutAnim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				WindowManager.LayoutParams wlp = getWindow().getAttributes();
				wlp.alpha = 1 - interpolatedTime;
				getWindow().setAttributes(wlp);
			}
		};
		mOverlayOutAnim.setDuration(120);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_alert_dialog);
		mDialogView = getWindow().getDecorView().findViewById(
				android.R.id.content);
		initView();

	}

	public void initView() {
		text_Register = (TextView) findViewById(R.id.text_two);
		text_isLogin = (TextView) findViewById(R.id.text_toast_point);
		text_Result = (TextView) findViewById(R.id.text_result);
		text_Left_Point = (TextView) findViewById(R.id.text_left_point);
		text_Toast = (TextView) findViewById(R.id.text_toast);
		btn_Login = (Button) findViewById(R.id.btn_login);
		btn_Exit = (Button) findViewById(R.id.btn_exit);
		btn_Sure = (Button) findViewById(R.id.btn_sure);
		text_Register.setOnClickListener(this);
		btn_Sure.setOnClickListener(this);
		btn_Login.setOnClickListener(this);
		btn_Exit.setOnClickListener(this);
		// list_point = (ListView) findViewById(R.id.list);
		redioGroup = (RadioGroup) findViewById(R.id.radiogroup_point);
		layout_btn = (LinearLayout) findViewById(R.id.layout_btn);
		layout_isregister = (LinearLayout) findViewById(R.id.layout_isregister);
		changeAlertType(mAlertType, true);
	}

	private void restore() {
	}

	private void playAnimation() {
		if (mAlertType == FAILED_TYPE) {
		} else if (mAlertType == SUCCESS_TYPE) {
		}
	}

	private void changeAlertType(int alertType, boolean fromCreate) {
		mAlertType = alertType;
		if (mDialogView != null) {
			if (!fromCreate) {
				// restore all of views state before switching alert type
				restore();
			}
			switch (mAlertType) {
			case NOLOGIN_TYPE:
				text_isLogin.setVisibility(View.VISIBLE);
				text_Left_Point.setVisibility(View.GONE);
				btn_Login.setVisibility(View.VISIBLE);
				layout_isregister.setVisibility(View.VISIBLE);
				text_Left_Point.setText(contentText);
				text_isLogin.setText(titleText);
				btn_Login.setText("登录");
				layout_btn.setVisibility(View.GONE);

				break;
			case SUCCESS_LOGIN:
				text_isLogin.setVisibility(View.VISIBLE);
				text_Left_Point.setVisibility(View.GONE);
				btn_Login.setVisibility(View.VISIBLE);
				layout_isregister.setVisibility(View.VISIBLE);
				text_Left_Point.setText(contentText);
				text_isLogin.setText(titleText);
				btn_Login.setText("确定");
				layout_btn.setVisibility(View.GONE);

				break;
			case FAILED_LOGIN:
				text_isLogin.setVisibility(View.VISIBLE);
				text_Left_Point.setVisibility(View.GONE);
				btn_Login.setVisibility(View.VISIBLE);
				layout_isregister.setVisibility(View.VISIBLE);
				text_Left_Point.setText(contentText);
				text_isLogin.setText(titleText);
				btn_Login.setText("重新登录");
				layout_btn.setVisibility(View.GONE);

				break;
			case FAILED_REG:
				text_isLogin.setVisibility(View.VISIBLE);
				text_Left_Point.setVisibility(View.GONE);
				btn_Login.setVisibility(View.VISIBLE);
				layout_isregister.setVisibility(View.VISIBLE);
				text_Left_Point.setText(contentText);
				text_isLogin.setText(titleText);
				btn_Login.setText("重新注册");
				layout_btn.setVisibility(View.GONE);

				break;
			case SUCCESS_REG:
				text_isLogin.setVisibility(View.VISIBLE);
				text_Left_Point.setVisibility(View.GONE);
				btn_Login.setVisibility(View.VISIBLE);
				layout_isregister.setVisibility(View.VISIBLE);
				text_Left_Point.setText(contentText);
				text_isLogin.setText(titleText);
				btn_Login.setText("确定");
				layout_btn.setVisibility(View.GONE);

				break;
			case ALERT_TYPE:
				text_isLogin.setVisibility(View.VISIBLE);
				text_Left_Point.setVisibility(View.GONE);
				btn_Login.setVisibility(View.VISIBLE);
				layout_isregister.setVisibility(View.GONE);
				text_Left_Point.setText(contentText);
				text_isLogin.setText(titleText);
				btn_Login.setText("确定");
				layout_btn.setVisibility(View.GONE);

				break;
			case SUCCESS_TYPE:
				btn_Login.setVisibility(View.GONE);
				layout_isregister.setVisibility(View.GONE);
				text_isLogin.setVisibility(View.GONE);

				btn_Exit.setVisibility(View.VISIBLE);
				btn_Sure.setVisibility(View.VISIBLE);
				text_Left_Point.setVisibility(View.VISIBLE);
				text_Result.setVisibility(View.VISIBLE);
				text_Result.setText(titleText);
				btn_Exit.setText(CancelText);
				btn_Sure.setText(confiremText);
				text_Left_Point.setText(contentText);
				layout_btn.setVisibility(View.VISIBLE);
				break;
			case FAILED_TYPE:
				text_Left_Point.setVisibility(View.GONE);
				btn_Login.setVisibility(View.GONE);
				layout_isregister.setVisibility(View.GONE);

				text_Result.setVisibility(View.VISIBLE);
				btn_Exit.setVisibility(View.VISIBLE);
				btn_Sure.setVisibility(View.VISIBLE);
				redioGroup.setVisibility(View.VISIBLE);
				text_Result.setText(titleText);
				btn_Exit.setText(CancelText);
				btn_Sure.setText(confiremText);
				layout_btn.setVisibility(View.VISIBLE);

				break;
			case POINT_TYPE:

				btn_Login.setVisibility(View.GONE);
				layout_isregister.setVisibility(View.GONE);

				text_Left_Point.setVisibility(View.VISIBLE);
				text_isLogin.setVisibility(View.VISIBLE);
				btn_Exit.setVisibility(View.VISIBLE);
				btn_Sure.setVisibility(View.VISIBLE);
				text_Toast.setVisibility(View.VISIBLE);
				layout_btn.setVisibility(View.VISIBLE);

				text_isLogin.setText(titleText);

				text_Left_Point.setText(contentText);
				btn_Exit.setText(CancelText);
				btn_Sure.setText(confiremText);
				text_Toast.setText("友情提醒，发布消息遵守法律法规");
				break;
			}
		}
	}

	public int getAlerType() {
		return mAlertType;
	}

	public void changeAlertType(int alertType) {
		changeAlertType(alertType, false);
	}

	public String getTitleText() {
		return titleText;
	}

	String titleText;

	public AlertDialog setTitleText(String text) {
		titleText = text;

		return this;
	}

	public AlertDialog setCustomImage(Drawable drawable) {
		return this;
	}

	public AlertDialog setCustomImage(int resourceId) {
		return setCustomImage(getContext().getResources().getDrawable(
				resourceId));
	}

	public String getContentText() {

		return contentText;
	}

	String contentText;

	public AlertDialog setContentText(String text) {
		contentText = text;
		return this;
	}

	public boolean isShowCancelButton() {
		return true;
	}

	public AlertDialog showCancelButton(boolean isShow) {
		return this;
	}

	public String getCancelText() {
		return CancelText;
	}

	String CancelText;

	public AlertDialog setCancelText(String text) {
		CancelText = text;
		return this;
	}

	String confiremText;

	public String getConfirmText() {
		return confiremText;
	}

	public AlertDialog setConfirmText(String text) {
		confiremText = text;
		return this;
	}

	protected void onStart() {
		mDialogView.startAnimation(mModalInAnim);
	}

	public void dismiss() {
		mDialogView.startAnimation(mModalOutAnim);
	}

	String TAG = "alertDialog";

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_exit:
			if (mAlertType == POINT_TYPE) {
				issuccess.IsSuccess(false);
			}
			dismiss();
			break;
		case R.id.btn_sure:
			if (mAlertType == POINT_TYPE) {
				issuccess.IsSuccess(true);
			}
			dismiss();
			break;

		case R.id.btn_login:
			if (mAlertType == SUCC_BUY) {
				Logger.d(TAG, "type is==>" + mAlertType);
				dismiss();
			} else if (mAlertType == NOLOGIN_TYPE) {
				Logger.d(TAG, "type is==>" + mAlertType);
				mContext.startActivity(new Intent().setClass(mContext,
						LoginFragment.class));

			} else if (mAlertType == SUCCESS_LOGIN) {
				issuccess.IsSuccess(true);
				dismiss();

			} else if (mAlertType == SUCCESS_REG) {
				Logger.d(TAG, "type is==>" + mAlertType);
				issuccess.IsSuccess(true);

			} else if (mAlertType == FAILED_LOGIN) {
				Logger.d(TAG, "type is==>" + mAlertType);
				issuccess.IsSuccess(false);
				mContext.startActivity(new Intent().setClass(mContext,
						LoginFragment.class));
			} else if (mAlertType == FAILED_REG) {
				issuccess.IsSuccess(false);
				Logger.d(TAG, "type is==>" + mAlertType);
				mContext.startActivity(new Intent().setClass(mContext,
						RegisterFragment.class));

			} else {
				dismiss();
			}

			dismiss();
			break;
		case R.id.text_two:
			// newContent = new RegisterFragment();
			// base.switchContent(newContent, "登录", true);
			mContext.startActivity(new Intent().setClass(mContext,
					RegisterFragment.class));
			dismiss();
			break;
		default:
			break;
		}
	}
}
