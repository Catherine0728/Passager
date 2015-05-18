package com.android.widget;





import com.ky.passenger.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

/**
 * 自定义Dialog
 * 
 * @author Catherine
 * 
 */
public class CustomProgressDialog extends ProgressDialog {

	private Context mContext;

	public CustomProgressDialog(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customprogressdialog);
	}

	public static CustomProgressDialog show(Context context) {
		CustomProgressDialog dialog = new CustomProgressDialog(context);
		dialog.show();
		return dialog;
	}

}
