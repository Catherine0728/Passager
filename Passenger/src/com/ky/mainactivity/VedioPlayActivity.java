package com.ky.mainactivity;

import java.io.File;

import com.android.widget.CustomProgressDialog;
import com.android.widget.MediaController;
import com.android.widget.StringTools;
import com.android.widget.VideoView;
import com.android.widget.VodCtrTop;
import com.ky.db.RecordVedioPlay;
import com.ky.passenger.R;
import com.ky.utills.Configure;
import com.redbull.log.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue.IdleHandler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 
 * this is video play 视频播放
 * 
 * 这里会考虑用 vitamin：支持多种格式
 * 
 * @author Catherine.Brain
 * */
public class VedioPlayActivity extends Activity {
	String TAG = "VedioPlayActivity";

	MediaController mMediaController;

	String numText;

	// int resourceId;

	VideoView videoView;
	MediaController mediaco;
	private VedioPlayActivity instance;
	RecordVedioPlay recordVideoPlay;
	long getOverTime = 0;
	String getOverTitle;
	SharedPreferences share = null;
	String path = null;
	LinearLayout layout_ToPlay;
	ImageView image_View;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videopaly);
		instance = this;
		share = getPreferences(MODE_PRIVATE);
		getOverTime = share.getInt("overTime", 0);
		getOverTitle = share.getString("overTitle", "");
		layout_ToPlay = (LinearLayout) findViewById(R.id.layout_video_play);
		image_View = (ImageView) findViewById(R.id.image_toplay);
		Configure.PAGER = 6;
		Intent intent = getIntent();

		title = intent.getStringExtra("vedio");

		Logger.d(TAG, "the title is===>" + title);
		mediaco = new MediaController(this);
		path =Configure.VedioFile + title;

		File file = new File(path);
		Logger.d(TAG, "the path  is===>" + path);
		if (file.exists()) {

			videoView = (VideoView) findViewById(R.id.myVideoView);
			videoView.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					finish();
				}
			});
			DB();
			// resourceId = R.drawable.ic_launcher;

		} else {
			System.out.println("文件不存在");
		}

		// if (file.exists()) {
		// // VideoView与MediaController进行关联
		// videoView.setVideoPath(file.getAbsolutePath());
		// videoView.setMediaController(mediaco);
		// mediaco.setMediaPlayer(videoView);
		// // 让VideiView获取焦点
		// videoView.requestFocus();
		// }
		// /* to listener the video is completion */
		// videoView.setOnCompletionListener(new OnCompletionListener() {
		//
		// @Override
		// public void onCompletion(MediaPlayer arg0) {
		// // TODO Auto-generated method stub
		// finish();
		// }
		// });

	}

	private String title;

	private void DB() {
		recordVideoPlay = new RecordVedioPlay(this);
		if (recordVideoPlay.select(title) == 0) {
			layout_ToPlay.setVisibility(View.VISIBLE);
			/* 暂时没有内容，那么就应该先存储 */
			recordVideoPlay.insert(title, getOverTime);
			getOverTime = 0;
			image_View.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					StartToInitData();
					layout_ToPlay.setVisibility(View.GONE);
				}
			});

		} else {
			System.out
					.println("this is a restart=====should to ToastOfTime...");
			getOverTime = recordVideoPlay.select(title);
			// System.out.println("the insert time is===>" + getOverTime);
			ToastOfTime(getOverTime);
		}
	}

	/**
	 * if has found the time is not equal zero,and need to toast the user to
	 * choose the time
	 * 
	 * @author Catherine.Brain
	 * */
	private AlertDialog TimeDialog = null;

	private void ToastOfTime(long getOverTime2) {

		TimeDialog = new AlertDialog.Builder(VedioPlayActivity.this).create();
		TimeDialog.show();
		TimeDialog.getWindow().setContentView(R.layout.time_dialog);
		final Button btn_Restart;
		final Button btn_Goon;

		btn_Restart = (Button) TimeDialog.getWindow().findViewById(
				R.id.btn_restart);
		btn_Goon = (Button) TimeDialog.getWindow().findViewById(R.id.btn_goon);
		TextView mTextView = (TextView) TimeDialog.getWindow().findViewById(
				R.id.text_time);
		mTextView.setText(getOverTime / 3600000 + " : " + getOverTime / 60000
				% 60 + " : " + getOverTime / 1000 % 60 + " : " + getOverTime
				% 1000);
		btn_Restart.requestFocus();
		btn_Restart.setFocusable(true);
		btn_Restart.setFocusableInTouchMode(true);
		btn_Restart.setEnabled(true);
		btn_Restart.setTextColor(0xFFFFFFFF);
		btn_Restart.setTextSize(30);
		btn_Restart.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		btn_Goon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				StartToInitData();
				TimeDialog.dismiss();
			}
		});
		btn_Restart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getOverTime = 0;
				StartToInitData();
				TimeDialog.dismiss();
			}
		});

	}

	public void StartToInitData() {
		playVideo(path);
		initControlView();
		initOperatHintPop();

		setMediaPlayer();

		Looper.myQueue().addIdleHandler(new IdleHandler() {

			@Override
			public boolean queueIdle() {
				if (ctrTop != null && videoView.isPlaying()) {
					showCtrTop();
				}
				return false;
			}
		});

	}

	int btn_judge = 0;
	private long mExitTime;

	private void showExitToast() {
		if ((System.currentTimeMillis() - mExitTime) > 3000) {
			Toast.makeText(this, "再按一次退出......", Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			SharedPreferences.Editor editor = share.edit();

			editor.putInt("overTime", (int) videoView.getCurrentPosition()); // getCurrentPosition

			editor.putString("overTitle", title); // getCurrentPosition
			System.out.println("(int) videoView.getCurrentPosition()"
					+ (int) videoView.getCurrentPosition());
			editor.commit();
			if (title.equals("")) {
			} else {
				recordVideoPlay.update(title, videoView.getCurrentPosition());

				finish();
			}
		}
	}

	public void playVideo(String url) {

		// Message msg = new Message();
		// msg.what = 0;
		// // 发送一个隐藏的消息
		// mHandler.sendMessageDelayed(msg, 5 * 1000);
		videoView.setVideoPath(url);
		// 展示一个进度
		// final CustomProgressDialog dialog = new
		// CustomProgressDialog(instance);
		// if (!dialog.isShowing()) {
		// dialog.show();
		// }
		videoView.seekTo((int) getOverTime);

		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// 取消进度
				// if (null != dialog) {
				// dialog.cancel();
				// }
				videoView.start();
			}
		});

		videoView.requestFocus();
	}

	int addOrbackNum = 30 * 1000;

	@Override
	protected void onDestroy() {
		if (ctrTop != null) {
			ctrTop.dismiss();
		}
		if (hintPop != null) {
			hintPop.dismiss();
		}
		instance.finish();
		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			showExitToast();
			// finish();
			return true;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			// mMediaController.show();
			if (videoView.isPlaying()) {
				// 暂停
				videoView.pause();
				// 展示暂停
				hintPopImg.setImageResource(R.drawable.osd_pause);
				hintPop.showAtLocation(videoView, 17, 0, 0);
			} else {
				hintPop.dismiss();
				videoView.start();
			}

			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			// mMediaController.show();
			showCtrTop();
			// ctrBot.show();
			// if (event.getRepeatCount() == 0 && event.getEventTime() > 2 *
			// 1000) {
			// videoBack();
			// }
			return false;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			mMediaController.show();
			showCtrTop();
			return false;
		case KeyEvent.KEYCODE_MENU:

			return true;

		}
		return false;
	}

	private void showCtrTop() {
		ctrTop.showAtLocation(videoView, Gravity.TOP, 0, 0);
		// ctrTop.setSourceTag(resourceId);

		if (StringTools.isNullOrEmpty(numText)) {
			ctrTop.setVideoName(title);
		} else {
			ctrTop.setVideoName(title + "(" + numText + ")");
		}
	}

	VodCtrTop ctrTop;
	// VodCtrBot ctrBot;

	private PopupWindow hintPop;
	private ImageView hintPopImg;

	private void initControlView() {

		// ctrBot = new VodCtrBot(this, videoView, new Handler() {

		// });

		ctrTop = new VodCtrTop(this, new Handler() {
			@Override
			public void handleMessage(Message msg) {

				super.handleMessage(msg);
			}

		});

	}

	private void initOperatHintPop() {
		hintPop = new PopupWindow();
		hintPopImg = new ImageView(this);
		hintPop.setWindowLayoutMode(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		hintPop.setBackgroundDrawable(new BitmapDrawable());
		WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		this.hintPopImg.setLayoutParams(localLayoutParams);
		this.hintPop.setContentView(this.hintPopImg);
	}

	private void setMediaPlayer() {
		// 全屏之后设置进度条

		mMediaController = new MediaController(this);
		videoView.setMediaController(mMediaController);
		mMediaController.show();
	}

	@Override
	protected void onResume() {
		// setDataSource(url, title);
		super.onResume();

		// getCookies();
	}

	public WindowManager windowManager;
	// private Handler handler;
	// private OverlayThread overlayThread;

	private LinearLayout overlay_ll;
	public TextView overlay;

	// private Handler mHandler = new Handler() {
	//
	// public void handleMessage(Message msg) {
	// if (msg.what == 0) {
	//
	// }
	// };
	// };

	public void initOverlay() {
		// handler = new Handler();
		// overlayThread = new OverlayThread();
		final LayoutInflater inflater = LayoutInflater.from(instance);
		overlay_ll = (LinearLayout) inflater.inflate(R.layout.overlay, null);
		overlay = (TextView) overlay_ll.findViewById(R.id.tv_center);

		final WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		windowManager = (WindowManager) instance
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay_ll, lp);
		// windowManager.addView(overlay_bottom, lp);
	}

	/**
	 * 设置overlay不可见
	 * 
	 * 
	 */
	class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay_ll.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取屏幕的宽和高
	 */
	private void getScreenSize() {
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// VitaVideoView.mVideoWidth = dm.widthPixels;
		// VitaVideoView.mVideoHeight = dm.heightPixels;
	}

	int playNum = 0;
	int titleNum = 0;

	//

	protected void exitApp() {
		UserDialogRunnable userRun = new UserDialogRunnable();
		new Handler().postDelayed(userRun, 300L);
	}

	class UserDialogRunnable implements Runnable {

		@Override
		public void run() {
			finish();
			overridePendingTransition(R.anim.hyperspace_in,
					R.anim.hyperspace_out);
			// android.os.Process.killProcess(android.os.Process.myPid());
		}

	}

	Handler menuHandler = new Handler();

	public void changeScales(int whatMode) {
		videoView.selectScales(whatMode);

	}

	OnErrorListener errorListener = new OnErrorListener() {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {

			return true;
		}
	};

	/**
	 * 简单异或加密解密算法
	 * 
	 * @param str
	 *            要加密的字符串
	 * @return
	 */
	private static String encode2(String str) {
		int code = 112; // 密钥
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			charArray[i] = (char) (charArray[i] ^ code);
		}
		return new String(charArray);
	}

}
