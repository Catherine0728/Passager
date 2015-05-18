package com.ky.adapter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import com.ky.mainactivity.DownloadFragment;
import com.ky.mainactivity.DownloadListActivity;
import com.ky.passenger.R;
import com.ky.utills.FileUtils;
import com.ky.utills.LauncherBiz;
import com.ky.utills.Configure;
import com.lhl.db.DowningVideoDB;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.sample.download.DownloadManager;
import com.lidroid.xutils.util.LogUtils;
import com.redbull.log.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * this is the game adapter of the main
 * 
 * @author Catherine.Brain
 * */
public class GameListAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<Map<String, Object>> mList;

	public GameListAdapter(Context context,
			ArrayList<Map<String, Object>> list, DownloadManager downloadManager) {
		mContext = context;
		mList = list;
		this.downloadManager = downloadManager;

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

	HolderView holderView;

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		if (null == convertview) {
			convertview = LayoutInflater.from(mContext).inflate(
					R.layout.main_game_download, null);
			new HolderView(convertview);

		}
		holderView = (HolderView) convertview.getTag();
		// if (position == 0) {
		// new BitmapThread(mList.get(0).get("image").toString()).start();
		// }

		holderView.game_Iamge.setImageResource((Integer) mList.get(position)
				.get("image"));
		// holderView.game_Iamge.setImageBitmap((Bitmap)
		// mList.get(position).get("image"));
		holderView.text_Name.setText("游戏名称:"
				+ mList.get(position).get("name").toString());
		holderView.text_Down
				.setText(mList.get(position).get("down").toString());
		holderView.text_size.setText("游戏大小:"
				+ mList.get(position).get("size").toString());
		holderView.text_Down.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(mContext, "正在下载", Toast.LENGTH_SHORT).show();
				// TOInstatll();
				// Intent intent = new Intent();
				// intent.setClass(mContext, DownloadFragment.class);
				// intent.putExtra("fromwhere", "game");
				// mContext.startActivity(intent);
				// ((Activity) mContext).finish();
				if (Down()) {
					Intent intent = new Intent(mContext,
							DownloadListActivity.class);
					intent.putExtra("fromWhere", 3);
					mContext.startActivity(intent);
				} else {
					Toast.makeText(mContext, "当前应用已经下载过了！", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
		return convertview;
	}

	class HolderView {
		ImageView game_Iamge;
		TextView text_Name, text_Down, text_size;

		public HolderView(View view) {
			game_Iamge = (ImageView) view.findViewById(R.id.game_image);
			text_Name = (TextView) view.findViewById(R.id.game_name);
			text_Down = (TextView) view.findViewById(R.id.game_download);
			text_size = (TextView) view.findViewById(R.id.game_size);
			view.setTag(this);
		}

	}

	// public class BitmapThread extends Thread {
	//
	// private String startBg;
	//
	// public BitmapThread(String start) {
	// startBg = start;
	// }
	//
	// @Override
	// public void run() {
	// try {
	// URL url = new URL(startBg);
	// Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
	// Logger.log("bitmap:" + bitmap
	// + "----------------------------------------------");
	// // cache.put("startBitmap", bitmap);
	// Message msg = new Message();
	// msg.obj = bitmap;
	// imageHandler.sendMessage(msg);
	//
	// } catch (IOException e) {
	// Logger.log("------------" + e.toString() + "<><><><><><>");
	// try {
	// android.os.Debug
	// .dumpHprofData("/mnt/sdcard/hljhomedump.hprof");
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// }
	// }
	// }
	//
	// Handler imageHandler = new Handler() {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// Bitmap bitmap = (Bitmap) msg.obj;
	// // pre.setImageBitmap(bitmap);
	// holderView.game_Iamge.setImageBitmap(bitmap);
	// super.handleMessage(msg);
	// }
	// };
	public void TOInstatll() {

		// TODO Auto-generated method stub
		String shopurl = "http://www.tvapk.com/android/vod/10736.htm/youKXL.apk";
		// String shopForceUpdateCode =
		// response.shopApps.forceUpdateCode;
		String shopVer = "1.1";
		Logger.log("shopVer:" + shopVer);
		String shopMessage = "description";
		extensionName = Configure.getExtensionName(shopurl);
		// 不能用中文
		String shopFileName = "downone" + shopVer + extensionName;
		downloadapk(shopFileName, shopurl);
	}

	/**
	 * 下载
	 * 
	 * @param url
	 */
	String target = "/mnt/sdcard/";
	FinalHttp finalHttp = new FinalHttp();
	HttpHandler httpHandler;
	// CommonToast toast;
	String extensionName;

	public void downloadapk(final String fileName, final String fileUrl) {
		Logger.log("currentFileName:" + fileName + ",fileUrl:" + fileUrl);
		// 判断文件是否存在(如果存在则删除)
		try {
			boolean isFileExist = FileUtils.delectFile(target + fileName);
			Logger.log("isFileExist:" + isFileExist);
		} catch (IOException e) {
			Logger.log("deleteFoder failed:" + e.getLocalizedMessage());
		}

		httpHandler = finalHttp.download(fileUrl, target + fileName, true,
				new AjaxCallBack<File>() {
					@Override
					public void onStart() {
						Logger.log("开始下载");
					}

					@Override
					public void onLoading(long count, long current) {
						Logger.log(current + "/" + count);
					}

					@Override
					public void onSuccess(final File t) {
						Logger.log("下载成功");
						Logger.log(t.getAbsolutePath() + "======<><><><><><>");

						// 如果是apk的话就执行安装
						if (".apk".equals(extensionName)) {
							// 强制安装
							boolean isInstall = LauncherBiz
									.execCommand("system/bin/pm install -r "
											+ "mnt/sdcard/" + fileName);

							Logger.log("isInstall:" + isInstall);
						} else {
							Logger.log("下载文件不是apk文件");
							// toast.setText("下载文件不是apk文件");
							// toast.setIcon(R.drawable.toast_err);
							// toast.show();
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						Logger.log("下载apk失败");
						Logger.log(strMsg + "=====================");
						// toast.setText("下载apk失败");
						// toast.setIcon(R.drawable.toast_err);
						// toast.show();
					}

				});
	}

	// -------------------------------这是xUtils下载-------------------------------------
	// 声明一个db
	DowningVideoDB DowningDB = null;
	public String Flag = "apk";

	// 定义一个变量来得到当前需要下载的路径
	private String path = "";
	// 定义一个变量来得到需要下载文件的名字
	private String down_filename = "";
	private DownloadManager downloadManager;

	public boolean Down() {
		if (null == DowningDB) {
			DowningDB = new DowningVideoDB(mContext);
		}
		int i = (int) (Math.random() * 5 + 1);
		path = Configure.GamePath[i];
		down_filename = path.substring(path.lastIndexOf('/') + 1);
		// String target = "/sdcard/xUtils/" +
		// System.currentTimeMillis()
		// + "lzfile.apk";
		System.out.println("将要下载的文件名为====》" + down_filename);
		String target = "";
		if (down_filename.contains(".apk")) {
			target = Configure.GameFile + down_filename;
			Flag = "apk";
		} else if (down_filename.contains(".mp4")) {

			target = Configure.VedioFile + down_filename;
			Flag = "mp4";
		} else {
			target = Configure.NovelFile + down_filename;
			Flag = "novel";
		}
		if (DowningDB.select(down_filename) == 0) {
			DowningDB.insert("0", path, down_filename,0);

			try {
				downloadManager.addNewDownload(path.toString(), down_filename,
						target, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
						false, Flag, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
						null);
			} catch (DbException e) {
				LogUtils.e(e.getMessage(), e);
			}
			return true;
		}
		return false;

	}

	// ------------------------这是xUtils的下载需要的---------------------------
}
