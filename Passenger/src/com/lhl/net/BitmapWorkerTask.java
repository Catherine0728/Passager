package com.lhl.net;

import com.android.widget.StringTools;
import com.redbull.log.Logger;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 图片处理异步任务
 * 
 * 
 */
@SuppressLint("NewApi")
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
	public static String TAG = "BitmapWorkerTask";

	private boolean isMemoryCache;
	private boolean isDiskCache;
	private Context mContext;

	private View imageView;

	private ImageCallBack callback;

	BitmapUtils bitmapUtil;

	int position;

	NewImageCallBack newCallBack;

	public BitmapWorkerTask(Context context, View imageView,
			boolean isMemoryCache, boolean isDiskCache, ImageCallBack callback) {
		this.mContext = context;
		this.imageView = imageView;
		this.isMemoryCache = isMemoryCache;
		this.isDiskCache = isDiskCache;
		this.callback = callback;
		bitmapUtil = BitmapUtils.getInstance(mContext);
	}

	public BitmapWorkerTask(Context context, View imageView, int i,
			boolean isMemoryCache, boolean isDiskCache,
			NewImageCallBack postImageCallBack) {
		this.mContext = context;
		this.imageView = imageView;
		this.isMemoryCache = isMemoryCache;
		this.isDiskCache = isDiskCache;
		this.newCallBack = postImageCallBack;
		position = i;
		bitmapUtil = BitmapUtils.getInstance(mContext);
	}

	@Override
	protected Bitmap doInBackground(String... arg0) {
		String url = arg0[0];
		Bitmap bitmap = null;
		if (!StringTools.isNullOrEmpty(url)) {
			if (isMemoryCache) {
				// 从内存中取
				bitmap = bitmapUtil.getBitmapFromMemory(url);
				System.out.println("isMemoryCache bitmap:" + bitmap);
			}
			if (bitmap == null && isDiskCache) {
				// 从硬盘上取
				bitmap = bitmapUtil.getBitmapFromDisk(url);
				System.out.println("isDiskCache bitmap:" + bitmap);
			}
			if (bitmap == null) {
				// 从网络获取
				bitmap = bitmapUtil.getBitmapFromNet(url);
				// 添加到缓存中
				bitmapUtil.addToCache(url, bitmap, isMemoryCache, isDiskCache);
			}

		} else {
			Logger.log("bitmap url is null");
		}
		// Logger.log("BitmaoWorkerTask doInBackGroud:"+
		// Thread.currentThread().getName());
		return bitmap;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onPostExecute(Bitmap result) {
//		Logger.d(TAG, "the result is==>" + result
//				+ "-----and the image is===<." + imageView);
		if (imageView != null && result != null) {
			if (imageView instanceof ImageView) {
				((ImageView) imageView).setImageBitmap(result);
			}

			else if (imageView instanceof RelativeLayout) {
				Drawable drawable = new BitmapDrawable(result);
				((RelativeLayout) imageView).setBackground(drawable);
			}

			else if (imageView instanceof FrameLayout) {
				Drawable drawable = new BitmapDrawable(result);
				((FrameLayout) imageView).setBackground(drawable);
			}

			else if (imageView instanceof LinearLayout) {
				Drawable drawable = new BitmapDrawable(result);
				((LinearLayout) imageView).setBackground(drawable);
			}

			if (callback != null) {
				callback.post(result);
			}

			if (newCallBack != null) {
				newCallBack.post(result, position);
			}
		}

		super.onPostExecute(result);
	}

	public static abstract interface NewImageCallBack {

		public abstract void post(Bitmap bitmap, int i);

	}

	public static abstract interface ImageCallBack {

		public abstract void post(Bitmap bitmap);

	}

}
