package com.ky.mainactivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.listener.IsSuccessListener;
import com.ky.adapter.BarrageAdapter;
import com.ky.db.GetBarrageDB;
import com.ky.db.IsLoginDB;
import com.ky.passenger.R;
import com.ky.request.GetPointRequest;
import com.ky.request.HttpHomeLoadDataTask;
import com.ky.response.GetPointResponse;
import com.ky.utills.Configure;
import com.lhl.callback.IUpdateData;
import com.lhl.dialog.library.AlertDialog;
import com.redbull.log.Logger;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * this is about barrage
 * 
 * @author Catherine.Brain
 * */
public class BarrageFragment extends Activity implements OnClickListener {
	String TAG = "BarrageFragment";
	View view = null;
	TextView text_Daoyan, text_Actor, text_Year, text_Title;
	ListView barrageList;
	BarrageAdapter adapter;
	ArrayList<Map<String, String>> mList;
	EditText edit_Input;
	Button btn_Comm, btn_return;
	public static String FRAGMENTNAME = "弹幕";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barrage);
		initView();
	}

	public void initView() {
		Configure.PAGER = 1;
		// BaseActivity.btn_Return.setVisibility(View.VISIBLE);
		// BaseActivity.text_nav_Chose.setText(FRAGMENTNAME);
		btn_return = (Button) findViewById(R.id.btn_return);
		text_Actor = (TextView) findViewById(R.id.text_actor_name);
		text_Daoyan = (TextView) findViewById(R.id.text_daoyan_name);
		text_Year = (TextView) findViewById(R.id.text_years_time);
		text_Title = (TextView) findViewById(R.id.text_title);
		barrageList = (ListView) findViewById(R.id.barrage_list);
		btn_Comm = (Button) findViewById(R.id.btn_comment);
		edit_Input = (EditText) findViewById(R.id.edit_input);
//		btn_Comm.requestFocus();
//		BTN_COMM.SETFOCUSABLE(TRUE);
//		BTN_COMM.SETFOCUSABLEINTOUCHMODE(TRUE);
		btn_Comm.setOnClickListener(this);
	
		btn_return.setOnClickListener(this);

		UpdateData();
		GetList();
	}

	public void GetList() {
		/** 去获得弹幕消息 */

		GetBarrage();

		GetMes(mList);
	}

	String username, userId, userKey, userStatus, userMobile, userEmail;
	IsLoginDB isLogin;

	public void CheckDB() {
		isLogin = new IsLoginDB(this);
		Cursor cursor = isLogin.select();
		Logger.e(TAG, "the cours is==>" + cursor);
		if (cursor.moveToFirst()) {
			Logger.d(TAG, "当前有用户");
			username = cursor.getString(cursor
					.getColumnIndex(isLogin.u_UserName));
			userId = cursor.getString(cursor.getColumnIndex(isLogin.u_user_id));
			userKey = cursor.getString(cursor.getColumnIndex(isLogin.u_key));
			userMobile = cursor.getString(cursor
					.getColumnIndex(isLogin.u_Phone));
			userEmail = cursor
					.getString(cursor.getColumnIndex(isLogin.u_email));
			if (null == username || username.equals("")) {
				new AlertDialog(this, AlertDialog.NOLOGIN_TYPE, isSuccess)
						.setTitleText("您还未登录，请登录").show();
			} else {
				Logger.d(TAG, "the username is==>" + username
						+ "and the userId is===>" + userId
						+ "and the key is====>" + userKey);
			}

		} else {
			Logger.d(TAG, "当前没用户");
			new AlertDialog(this, AlertDialog.NOLOGIN_TYPE, isSuccess)
					.setTitleText("您还未登录，请登录").show();

		}

	}

	public void GetBarrage() {

		if (null != mList) {
			mList.clear();
		} else {
			mList = new ArrayList<Map<String, String>>();
		}
		Map<String, String> map = null;
		if (null == barrage) {
			barrage = new GetBarrageDB(this);
		}
		Cursor cursor = barrage.select();

		if (cursor.moveToFirst()) {

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {

				String content = cursor.getString(cursor
						.getColumnIndex(barrage.M_CONTINUE));
				String time = cursor.getString(cursor
						.getColumnIndex(barrage.M_TIME));
				map = new HashMap<String, String>();
				if (null == username) {
					map.put("name", "xxx");
				} else {

					map.put("name", username);
				}

				map.put("time", time);
				map.put("content", content);
				mList.add(map);
				System.out.println("have list,and the content is====>"
						+ content + "and the time is===>" + time);

			}
		} else {
			// System.out.println("no list");
			// for (int i = 0; i < 2; i++) {
			// map = new HashMap<String, String>();
			// map.put("name", "元芳");
			// map.put("time", "2014-11-22  15:30");
			// map.put("content", "元芳，你怎么看？");
			// mList.add(map);
			// }
		}

		for (int i = 0; i < mList.size(); i++) {
			System.out.println("the list is===>" + mList.get(i).toString());
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	String edit_Text, commit_Time;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_comment:
			CheckDB();
			edit_Text = edit_Input.getText().toString().trim();
			// // 先去查询当前是否有用户
			// IsLoginDB isloginDB = new IsLoginDB(getActivity());
			// Cursor corsor = isloginDB.select();
			// if (corsor.moveToFirst()) {
			if (null == edit_Text || edit_Text.equals("")) {
				// edit_Input.setError("请输入内容！");

			} else {
				/** 去提交弹幕信息，以及获得·自己当前的点数是否可以发布消息 */

				PostData(edit_Text);
				SimpleDateFormat sDateFormat = new SimpleDateFormat(
						"MM-dd hh:mm:ss");
				commit_Time = sDateFormat.format(new java.util.Date());

			}
			break;
		case R.id.btn_return:
			startActivity(new Intent().setClass(this, BaseActivity.class));
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * 去获得当前影院正在播放的内容
	 * */
	public void UpdateData() {
		text_Title.setText("鬼吹灯");
		text_Actor.setText("悬疑");
		text_Daoyan.setText("天下霸唱");
		text_Year.setText("2014年12月12日");
	}

	public void PostData(String Content) {

		GetPointRequest getPoint = new GetPointRequest(this, userId, Content,
				userKey);
		new HttpHomeLoadDataTask(this, GetPointCallBack, false, "", false)
				.execute(getPoint);

	}

	IUpdateData GetPointCallBack = new IUpdateData() {

		@Override
		public void updateUi(Object o) {
			// TODO Auto-generated method stub
			GetPointResponse getPointRes = new GetPointResponse();
			getPointRes.paseRespones(o.toString());

			point = getPointRes.point;
			isLogin.update(userId, username, userMobile, userEmail, "1",
					userKey, point);
			if (null == barrage) {
				barrage = new GetBarrageDB(BarrageFragment.this);
			}

			barrage.insert(edit_Text, commit_Time);

			GetBarrage();
			adapter.reGetList(mList);
			/**
			 * 发布成功
			 * */
			new AlertDialog(BarrageFragment.this, AlertDialog.POINT_TYPE,
					isSuccess).setTitleText("发布成功")
					.setContentText("当前剩余点数：" + point).setConfirmText("继续发布")
					.setCancelText("下次再说").show();
		}

		@Override
		public void handleErrorData(String info) {
			Logger.e(TAG, info);
			// new AlertDialog(getActivity(), AlertDialog.NOLOGIN_TYPE)
			// .setTitleText("您还未登录，请登录").show();
			if (null != username) {
				if (info.equals("费用不够，请充值后发送")) {
					Toast.makeText(BarrageFragment.this, info,
							Toast.LENGTH_SHORT).show();
					// edit_Input.setText("");
					// edit_Input.setFocusable(true);
					// edit_Input.setFocusableInTouchMode(true);
				} else {
					Toast.makeText(BarrageFragment.this, info,
							Toast.LENGTH_SHORT).show();
				}
			} else {

			}

		}
	};
	int point;

	/**
	 * 
	 * 去获得弹幕信息
	 * */
	public void GetMes(ArrayList<Map<String, String>> list) {

		adapter = new BarrageAdapter(this, list);
		barrageList.setAdapter(adapter);
		// Utility.setListViewHeightBasedOnChildren(barrageList);
		barrageList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview,
					View convertview, int position, long id) {
				// TODO Auto-generated method stub

			}
		});
	}

	GetBarrageDB barrage = null;
	IsSuccessListener isSuccess = new IsSuccessListener() {

		@Override
		public void IsSuccess(Boolean isExit) {

			if (isExit) {
				edit_Input.setText("");
				edit_Input.setFocusable(true);
				edit_Input.setFocusableInTouchMode(true);

			} else {

				edit_Input.setText("");
			}

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent().setClass(this, BaseActivity.class));
			finish();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
