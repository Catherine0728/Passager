package com.ky.mainactivity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ky.passenger.R;
import com.wwj.net.download.DownloadProgressListener;
import com.wwj.net.download.FileDownloader;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 重复试验的下载
 * */
public class ReDownloadActivity extends Activity {
	private static final int PROCESSING = 1;
	private static final int FAILURE = -1;
	DownAdapter adapter = null;
	private Handler handler = new UIHandler();

	private final class UIHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PROCESSING: // 更新进度
				adapter.holderView.progressBar.setProgress(msg.getData()
						.getInt("size"));
				float num = (float) adapter.holderView.progressBar
						.getProgress()
						/ (float) adapter.holderView.progressBar.getMax();
				int result = (int) (num * 100); // 计算进度
				adapter.holderView.resultView.setText(result + "%");
				if (adapter.holderView.progressBar.getProgress() == adapter.holderView.progressBar
						.getMax()) {
					Toast.makeText(getApplicationContext(), R.string.success,
							Toast.LENGTH_LONG).show();
				}
				adapter.notifyDataSetChanged();
				break;
			case FAILURE: // 下载失败
				Toast.makeText(getApplicationContext(), R.string.error,
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.redownmain);
		initView();
	}

	ListView list;
	ArrayList<Map<String, String>> mList = new ArrayList<Map<String, String>>();

	public void initView() {
		list = (ListView) findViewById(R.id.list_down);
		String path = "http://download.haozip.com/haozip_v3.1.exe";
		String filename = path.substring(path.lastIndexOf('/') + 1);

		try {
			// URL编码（这里是为了将中文进行URL编码）
			filename = URLEncoder.encode(filename, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		path = path.substring(0, path.lastIndexOf("/") + 1) + filename;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// File savDir =
			// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
			// 保存路径
			File savDir = Environment.getExternalStorageDirectory();
			download(path, savDir);
		} else {
			Toast.makeText(getApplicationContext(), R.string.sdcarderror,
					Toast.LENGTH_LONG).show();
		}
		Map<String, String> map = null;
		for (int i = 0; i < 2; i++) {
			map = new HashMap<String, String>();
			map.put("url", path);
			map.put("name", filename);
			mList.add(map);
		}
		adapter = new DownAdapter(this, mList);
		list.setAdapter(adapter);
	}

	/*
	 * 由于用户的输入事件(点击button, 触摸屏幕....)是由主线程负责处理的，如果主线程处于工作状态，
	 * 此时用户产生的输入事件如果没能在5秒内得到处理，系统就会报“应用无响应”错误。
	 * 所以在主线程里不能执行一件比较耗时的工作，否则会因主线程阻塞而无法处理用户的输入事件，
	 * 导致“应用无响应”错误的出现。耗时的工作应该在子线程里执行。
	 */
	private DownloadTask task;

	private void exit() {
		if (task != null)
			task.exit();
	}

	private void download(String path, File savDir) {
		task = new DownloadTask(path, savDir);
		new Thread(task).start();
	}

	/**
	 * 
	 * UI控件画面的重绘(更新)是由主线程负责处理的，如果在子线程中更新UI控件的值，更新后的值不会重绘到屏幕上
	 * 一定要在主线程里更新UI控件的值，这样才能在屏幕上显示出来，不能在子线程中更新UI控件的值
	 * 
	 */
	private final class DownloadTask implements Runnable {
		private String path;
		private File saveDir;
		private FileDownloader loader;

		public DownloadTask(String path, File saveDir) {
			this.path = path;
			this.saveDir = saveDir;
		}

		/**
		 * 退出下载
		 */
		public void exit() {
			if (loader != null)
				loader.exit();
		}

		DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
			@Override
			public void onDownloadSize(int size) {
				Message msg = new Message();
				msg.what = PROCESSING;
				msg.getData().putInt("size", size);
				handler.sendMessage(msg);
			}
		};

		public void run() {
			try {
				// 实例化一个文件下载器
				loader = new FileDownloader(getApplicationContext(), path,
						saveDir, 3);
				// 设置进度条最大值
				adapter.holderView.progressBar.setMax(loader.getFileSize());
				loader.download(downloadProgressListener);
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendMessage(handler.obtainMessage(FAILURE)); // 发送一条空消息对象
			}
		}
	}
}

/**
 * 
 * 定义一个adapter来得到数据
 * */
class DownAdapter extends BaseAdapter implements
		android.view.View.OnClickListener {
	Context mContext;
	ArrayList<Map<String, String>> mList;

	public DownAdapter(Context context, ArrayList<Map<String, String>> list) {
		mContext = context;
		mList = list;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	HolderView holderView = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.re_down, null);
			new HolderView(convertView, position);

		}
		holderView = (HolderView) convertView.getTag();
		holderView.downloadButton.setOnClickListener(this);
		holderView.stopButton.setOnClickListener(this);

		return convertView;
	}

	class HolderView {
		TextView resultView;
		Button downloadButton;
		Button stopButton;
		ProgressBar progressBar;

		public HolderView(View view, int pos) {
			resultView = (TextView) view.findViewById(R.id.resultView);
			downloadButton = (Button) view.findViewById(R.id.downloadbutton);
			stopButton = (Button) view.findViewById(R.id.stopbutton);
			progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
			downloadButton.setTag(pos);
			stopButton.setTag(pos);
			view.setTag(this);
		}
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) v.getTag();
		switch (v.getId()) {

		case R.id.downloadbutton:
			break;
		case R.id.stopbutton:
			break;

		default:
			break;
		}

	}
}
