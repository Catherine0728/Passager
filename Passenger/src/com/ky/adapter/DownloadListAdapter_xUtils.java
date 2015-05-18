package com.ky.adapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.listener.CheckStateListener;
import com.ky.db.VideoDowningDB;
import com.ky.mainactivity.VedioPlayActivity;
//import com.ky.mainactivity.DownloadListActivity.DownloadItemViewHolder;
import com.ky.passenger.R;
import com.ky.utills.Configure;
import com.lhl.db.DowningVideoDB;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.sample.download.DownloadInfo;
import com.lidroid.xutils.sample.download.DownloadManager;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 对于更换的xUtils的adapter
 * */
public class DownloadListAdapter_xUtils extends BaseAdapter {
	private int flag = 0;
	private final Context mContext;
	private final LayoutInflater mInflater;
	DownloadInfo VideodownloadInfo, GamedownloadInfo, NoveldownloadInfo;
	List<DownloadInfo> listVedio, listGame, listNovel;
	private DownloadManager downloadManager;
	DowningVideoDB DowningDB = null;
	int RadioFlags = 0;

	// //为全选判断定义弱引用变量
	// WeakReference<Activity> weak;
	CheckStateListener CheckListener = null;

	public DownloadListAdapter_xUtils(Context context, int flag,
			DownloadManager downloadManager, CheckStateListener checkListener) {
		// this.weak = new WeakReference<Activity>((Activity) context);
		this.CheckListener = checkListener;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.flag = flag;
		this.downloadManager = downloadManager;
		isChice = new boolean[downloadManager.getAllDownLoadInfo().size()];
		for (int i = 0; i < downloadManager.getAllDownLoadInfo().size(); i++) {
			isChice[i] = false;
		}
	}

	List<DownloadInfo> AllDownLoadInfo = new ArrayList<DownloadInfo>();

	@Override
	public int getCount() {
		listVedio = new ArrayList<DownloadInfo>();
		listGame = new ArrayList<DownloadInfo>();
		listNovel = new ArrayList<DownloadInfo>();
		if (downloadManager == null)
			return 0;
		AllDownLoadInfo = downloadManager.getAllDownLoadInfo();

		for (int i = 0; i < AllDownLoadInfo.size(); i++) {
			if (AllDownLoadInfo.get(i).getFlag().equals("apk")) {
				listGame.add(AllDownLoadInfo.get(i));
			} else if (AllDownLoadInfo.get(i).getFlag().equals("mp4")) {
				listVedio.add(AllDownLoadInfo.get(i));
			} else {
				listNovel.add(AllDownLoadInfo.get(i));
			}
		}
		if (flag == 0) {
			// SetDefault(listVedio);
			return listVedio.size();

		} else if (flag == 1) {
			// SetDefault(listNovel);
			return listNovel.size();

		} else {
			// SetDefault(listGame);
			return listGame.size();

		}

	}

	// 恢复默认
	public void SetDefault(int radioFlags) {
		System.out.println("SetDefault");
		this.RadioFlags = radioFlags;
		Configure.isEdit = false;

	}

	@Override
	public Object getItem(int i) {
		if (flag == 0) {
			return listVedio.get(i);
			// return downloadManager.getDownloadInfo(i);
		} else if (flag == 1) {
			return listNovel.get(i);
			// return downloadManager.getDownloadInfo(i);
		} else {
			return listGame.get(i);
			// return downloadManager.getDownloadInfo(i);
		}

	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void CheckUpdate(int id) {
		flag = id;

	}

	public DownloadItemViewHolder holder = null;
	DownloadInfo downloadInfo;

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		System.out.println("getview");

		if (flag == 0) {
			downloadInfo = listVedio.get(i);
		} else if (flag == 1) {
			downloadInfo = listNovel.get(i);
		} else {
			downloadInfo = listGame.get(i);
		}
		// DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
		if (view == null) {
			view = mInflater.inflate(R.layout.download_item, null);
			holder = new DownloadItemViewHolder(downloadInfo);

			ViewUtils.inject(holder, view);
			view.setTag(holder);
			holder.removeBtn.setTag(i);
			// 更新列表
			holder.refresh(0);
		} else {
			holder = (DownloadItemViewHolder) view.getTag();
			// 更新
			holder.update(downloadInfo, 0);
		}

		HttpHandler<File> handler = downloadInfo.getHandler();
		if (handler != null) {
			RequestCallBack callBack = handler.getRequestCallBack();
			if (callBack instanceof DownloadManager.ManagerCallBack) {
				DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
				if (managerCallBack.getBaseCallBack() == null) {
					managerCallBack
							.setBaseCallBack(new DownloadRequestCallBack());
				}
			}
			callBack.setUserTag(new WeakReference<DownloadItemViewHolder>(
					holder));
		}
		if (Configure.isEdit) {
			holder.removeBtn.setVisibility(View.VISIBLE);
			holder.removeBtn.setBackgroundDrawable(getView(i));
			holder.stopBtn.setVisibility(View.GONE);
			System.out.println(" 这是要操作文件的节奏");

		} else {
			holder.removeBtn.setBackgroundDrawable(getView(i));
			holder.removeBtn.setVisibility(View.GONE);
			holder.stopBtn.setVisibility(View.VISIBLE);
			System.out.println("这是不要操作文件的节奏");
		}

		return view;
	}

	// 当界面点击全选或者取消全选的时候进行状态改变
	public void SelectAll(boolean flags) {
		for (int i = 0; i < getCount(); i++) {
			ChooseState(i, true, flags);
		}
		notifyDataSetChanged();
	}

	// 实现删除
	public void Remove(int flags) {
		String name;
		for (int i = 0; i < getCount(); i++) {
			if (isChice[i]) {
				System.out.println("需要删除的item是===》" + i
						+ "===and the flag is===>" + flag);
				if (null == DowningDB) {
					DowningDB = new DowningVideoDB(mContext);
				}

				if (flags == 0) {
					DowningDB.delete(listVedio.get(i).fileName);
					name = listVedio.get(i).fileName;
				} else if (flags == 1) {
					DowningDB.delete(listNovel.get(i).fileName);
					name = listNovel.get(i).fileName;
				} else {
					DowningDB.delete(listGame.get(i).fileName);
					name = listGame.get(i).fileName;
				}

				try {
					if (flags == 0) {
						downloadManager.removeDownload(listVedio.get(i));
					} else if (flags == 1) {
						downloadManager.removeDownload(listNovel.get(i));
					} else {
						downloadManager.removeDownload(listGame.get(i));
					}

				} catch (DbException e) {
					LogUtils.e(e.getMessage(), e);
				}
				holder.DeleteFile(flags, name);
			}
		}

		notifyDataSetChanged();
	}

	private boolean isChice[];
	/**
	 * 
	 * 
	 * @param position用来表示点击的位置
	 * @param flags用来判断是否需要全部下载
	 *            。如果为false就是不用
	 * */
	VideoDowningDB v_DownDB = null;

	public void ChooseState(int position, boolean flags, boolean flag) {
		String name, url;
		if (flags) {
			if (flag) {
				isChice[position] = isChice[position] == true ? true : true;
			} else {
				isChice[position] = isChice[position] == true ? false : false;

			}

		} else {
			// isChice[position] = isChice[position] == true ? false : flag;
			isChice[position] = isChice[position] == true ? false : true;
		}
		if (isChice[position]) {
			if (null == DowningDB) {
				DowningDB = new DowningVideoDB(mContext);
			}
			if (RadioFlags == 0) {
				name = listVedio.get(position).getFileName();
				url = listVedio.get(position).getDownloadUrl();
				System.out.println("thle position is===>" + position
						+ "===and the filename===>"
						+ listVedio.get(position).getFileName()
						+ "====and the downloadurl===>"
						+ listVedio.get(position).getDownloadUrl());
			} else if (RadioFlags == 1) {
				name = listNovel.get(position).getFileName();
				url = listNovel.get(position).getDownloadUrl();
				System.out.println("thle position is===>" + position
						+ "===and the filename===>"
						+ listNovel.get(position).getFileName()
						+ "====and the downloadurl===>"
						+ listNovel.get(position).getDownloadUrl());
			} else {
				name = listGame.get(position).getFileName();
				url = listGame.get(position).getDownloadUrl();
				System.out.println("thle position is===>" + position
						+ "===and the filename===>"
						+ listGame.get(position).getFileName()
						+ "====and the downloadurl===>"
						+ listGame.get(position).getDownloadUrl());

			}

			DowningDB.update("0", name, url, 1);
		} else {

		}

		// 检测是否全选
		if (CheckSelect(1)) {
			// 为真，代表应该全选
			CheckListener.IsChange(true);
		} else {
			// 为假，代表显示取消全选
			CheckListener.IsChange(false);
		}

		this.notifyDataSetChanged();

	}

	// 查询当前选中的部分（0和1）
	public boolean CheckSelect(int flags) {
		// 这里有一个flags，0用来表示用户只是查询当前是否有选中的。1表示的是查询当前是否是全选，根据返回的结果进行改变底部的状态
		if (flags == 0) {
			for (int i = 0; i < getCount(); i++) {

				System.out.println(i + "的状态是===》" + isChice[i] + "getcount ==>"
						+ getCount());
				if (isChice[i]) {
					return true;

				}
			}
			return false;
		} else {

			for (int i = 0; i < getCount(); i++) {

				// 一旦遇到没有选中的就返回，且底部应该改为全选，若全是选中的，那么就改变底部栏状态为取消全选
				if (!isChice[i]) {
					return false;

				}
			}
			return true;

		}

	}

	// 主要就是下面的代码了
	private LayerDrawable getView(int post) {
		Bitmap bitmap;
		Bitmap bitmap2 = null;
		LayerDrawable la = null;
		if (Configure.isEdit) {
			bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(
					R.drawable.icon_select_default)).getBitmap();

			if (isChice[post] == true) {
				bitmap2 = BitmapFactory.decodeResource(mContext.getResources(),
						R.drawable.icon_select_foucs);
			}

			if (bitmap2 != null) {
				Drawable[] array = new Drawable[1];
				// array[0] = new BitmapDrawable(bitmap);
				array[0] = new BitmapDrawable(bitmap2);
				la = new LayerDrawable(array);
				la.setLayerInset(0, 0, 0, 0, 0); // 第几张图离各边的间距
				// la.setLayerInset(1, 0, 65, 65, 0);
			} else {
				Drawable[] array = new Drawable[1];
				array[0] = new BitmapDrawable(bitmap);
				la = new LayerDrawable(array);
				la.setLayerInset(0, 0, 0, 0, 0);
			}

		} else {
			bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(
					R.drawable.icon_delete)).getBitmap();

			Drawable[] array = new Drawable[1];
			// array[0] = new BitmapDrawable(bitmap);
			array[0] = new BitmapDrawable(bitmap);
			la = new LayerDrawable(array);
			la.setLayerInset(0, 0, 0, 0, 0); // 第几张图离各边的间距

		}
		return la; // 返回叠加后的图
	}

	public class DownloadItemViewHolder {
		@ViewInject(R.id.download_label)
		TextView label;
		// @ViewInject(R.id.download_state)
		// TextView state;
		@ViewInject(R.id.download_pb)
		ProgressBar progressBar;
		@ViewInject(R.id.download_stop_btn)
		Button stopBtn;
		@ViewInject(R.id.download_remove_btn)
		public Button removeBtn;
		@ViewInject(R.id.download_progress)
		TextView download_progress;
		private DownloadInfo downloadInfo;

		public DownloadItemViewHolder(DownloadInfo downloadinfo) {
			this.downloadInfo = downloadinfo;

		}

		@OnClick(R.id.download_stop_btn)
		public void stop(View view) {
			HttpHandler.State state = downloadInfo.getState();

			switch (state) {
			case WAITING:
			case STARTED:
			case LOADING:
				try {
					downloadManager.stopDownload(downloadInfo);
				} catch (DbException e) {
					LogUtils.e(e.getMessage(), e);
				}
				break;
			case CANCELLED:
			case FAILURE:
				try {
					downloadManager.resumeDownload(downloadInfo,
							new DownloadRequestCallBack());
				} catch (DbException e) {
					LogUtils.e(e.getMessage(), e);
				}
				notifyDataSetChanged();
				if (download_progress.toString().equals("100%")) {
					StartPlay(view);
				} else {

				}

				break;
			case SUCCESS:
				StartPlay(view);
				break;
			default:
				break;
			}
		}

		public void StartPlay(View view) {

			if (label.getText().toString().contains(".apk")) {
				// 游戏
				File file = new File(Configure.GameFile
						+ label.getText().toString());
				openFile(file);
			} else if (label.getText().toString().contains(".mp4")) {
				// 视频
				Intent intent = new Intent();
				intent.putExtra("vedio", label.getText().toString());
				intent.setClass(mContext, VedioPlayActivity.class);
				mContext.startActivity(intent);
			} else {
				// 小说

			}
		}

		@OnClick(R.id.download_remove_btn)
		public void RemoveBtn(View view) {
			if (Configure.isEdit) {
				System.out.println("点击进行选中====");
				ChooseState((Integer) view.getTag(), false, false);
			} else {
				System.out.println("点击进行删除=====");
				if (null == DowningDB) {
					DowningDB = new DowningVideoDB(mContext);
				}
				DowningDB.delete(downloadInfo.fileName);

				try {
					downloadManager.removeDownload(downloadInfo);

				} catch (DbException e) {
					LogUtils.e(e.getMessage(), e);
				}

				DeleteFile(RadioFlags, downloadInfo.fileName);
			}
			notifyDataSetChanged();

		}

		public void DeleteFile(int RadioFlag, String name) {
			System.out.println("当前的是===》" + RadioFlags + "===and the name===>"
					+ name);
			File file;
			if (RadioFlag == 0) {
				file = new File(Configure.VedioFile);
			} else if (RadioFlag == 1) {
				file = new File(Configure.NovelFile);
			} else {
				file = new File(Configure.GameFile);
			}
			File[] fl = file.listFiles();

			for (int i = 0; i < fl.length; i++) {
				System.out.println("the file is==>" + fl[i]);
				if (fl[i].toString().contains(name)) {
					if (fl[i].delete()) {
						System.out.println("终于成功的删掉了！");
					} else {
						System.out.println("怎么会删不掉呢！");
					}
				}

			}
		}

		/**
		 * 
		 * 如果在这里，我们安装成功，就可以直接弹出安装界面了 在手机上打开文件
		 */
		public void openFile(File f) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);

			/* 调用getMIMEType()来取得MimeType */
			String type = "application/vnd.android.package-archive";
			/* 设置intent的file与MimeType */
			intent.setDataAndType(Uri.fromFile(f), type);
			mContext.startActivity(intent);
		}

		public void update(DownloadInfo downloadInfo, int flag) {
			this.downloadInfo = downloadInfo;
			// 更新列表
			refresh(flag);
		}

		// 检测当前文件是否存在
		public boolean CheckFile(DownloadInfo downloadInfo) {
			File file;
			if (RadioFlags == 0) {
				file = new File(Configure.VedioFile);
			} else if (RadioFlags == 1) {
				file = new File(Configure.NovelFile);
			} else {
				file = new File(Configure.GameFile);
			}
			File[] fl = file.listFiles();

			for (int i = 0; i < fl.length; i++) {
				if (fl[i].getName().toString()
						.contains(downloadInfo.getFileName())) {
					System.out.println("数据库和文件下面都有==》" + fl[i]);
					return true;
				} else
					System.out.println("这是文件下面没有的节奏===》" + fl[i]
							+ "====and the 数据库的名字==="
							+ downloadInfo.getFileName());
				{

				}
			}
			return false;
		}

		/**
		 * 进行数据库的查询，对不同的文件进行状态的显示
		 * */
		int isSelect = 0;

		public void refresh(int id) {
			System.out.println("refresh===>" + RadioFlags);
			if (CheckFile(downloadInfo)) {
				System.out
						.println("数据库和文件下面都有==》" + downloadInfo.getFileName());
				label.setText(downloadInfo.getFileName());

				if (downloadInfo.getFileLength() > 0) {
					progressBar
							.setProgress((int) (downloadInfo.getProgress() * 100 / downloadInfo
									.getFileLength()));
					download_progress
							.setText((downloadInfo.getProgress() * 100 / downloadInfo
									.getFileLength()) + "%");
				} else {
					progressBar.setProgress(0);
				}
				if (download_progress.getText().equals("100%")) {
					progressBar.setProgress(100);
				}

				stopBtn.setVisibility(View.VISIBLE);
				// stopBtn.setText(mAppContext.getString(R.string.stop));
				stopBtn.setBackgroundResource(R.drawable.icon_stop);
				HttpHandler.State state = downloadInfo.getState();
				switch (state) {
				case WAITING:
					// stopBtn.setText(mAppContext.getString(R.string.stop));
					stopBtn.setBackgroundResource(R.drawable.icon_downing);
					break;
				case STARTED:
					// stopBtn.setText(mAppContext.getString(R.string.stop));
					stopBtn.setBackgroundResource(R.drawable.icon_downing);
					break;
				case LOADING:
					// stopBtn.setText(mAppContext.getString(R.string.stop));
					stopBtn.setBackgroundResource(R.drawable.icon_downing);
					break;
				case CANCELLED:
					// stopBtn.setText(mAppContext.getString(R.string.resume));
					stopBtn.setBackgroundResource(R.drawable.icon_retry);
					break;
				case SUCCESS:
					// stopBtn.setVisibility(View.INVISIBLE);
					// stopBtn.setText(mAppContext.getString(R.string.success));
					// 判断是视频还是小说还是游戏，然后进行安装还是进行播放
					if (downloadInfo.getFileName().contains(".apk")) {
						stopBtn.setBackgroundResource(R.drawable.icon_install);
					} else if (downloadInfo.getFileName().contains(".mp4")) {
						stopBtn.setBackgroundResource(R.drawable.icon_start);

					} else {
						stopBtn.setBackgroundResource(R.drawable.icon_read);
					}
					// stopBtn.setBackgroundResource(R.drawable.icon_start);
					// progressBar.setVisibility(View.GONE);
					break;
				case FAILURE:
					// stopBtn.setText(mAppContext.getString(R.string.retry));
					if (download_progress.toString().equals("100%")) {
						if (downloadInfo.getFileName().contains(".apk")) {
							stopBtn.setBackgroundResource(R.drawable.icon_install);
						} else if (downloadInfo.getFileName().contains(".mp4")) {
							stopBtn.setBackgroundResource(R.drawable.icon_start);

						} else {
							stopBtn.setBackgroundResource(R.drawable.icon_read);
						}
					} else {
						stopBtn.setBackgroundResource(R.drawable.icon_retry);

					}

					break;
				default:
					break;
				}
			} else {
				System.out.println("====and the 数据库的名字==="
						+ downloadInfo.getFileName());
				if (null == DowningDB) {
					DowningDB = new DowningVideoDB(mContext);
				}
			}

		}
	}

	private class DownloadRequestCallBack extends RequestCallBack<File> {

		@SuppressWarnings("unchecked")
		private void refreshListItem() {
			if (userTag == null)
				return;
			WeakReference<DownloadItemViewHolder> tag = (WeakReference<DownloadItemViewHolder>) userTag;
			DownloadItemViewHolder holder = tag.get();
			if (holder != null) {
				// 更新列表
				holder.refresh(0);
			}
		}

		@Override
		public void onStart() {
			refreshListItem();
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			refreshListItem();
		}

		@Override
		public void onSuccess(ResponseInfo<File> responseInfo) {
			refreshListItem();
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			refreshListItem();
		}

		@Override
		public void onCancelled() {
			refreshListItem();
		}
	}

}
