package sf.hmg.turntest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.ky.beaninfo.ColumnNewsInfo;
import com.ky.beaninfo.ColumnNovelInfo;
import com.ky.passenger.R;
import com.ky.request.GetContentInfoRequest;
import com.ky.request.GetContentNovelInfoRequest;
import com.ky.request.HttpHomeLoadDataTask;
import com.ky.response.GetContentNovelInfoResponse;
import com.ky.response.GetNewsContentInfoResponse;
import com.ky.response.GetNovelChapterInfoResponse;
import com.ky.utills.Configure;
import com.lhl.callback.IUpdateData;
import com.redbull.log.Logger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

@SuppressLint("WrongCall")
public class turntest extends Activity {
	String TAG = "turntest";
	/** Called when the activity is first created. */
	private PageWidget mPageWidget;
	Bitmap mCurPageBitmap, mNextPageBitmap;
	Canvas mCurPageCanvas, mNextPageCanvas;

	BookPageFactory pagefactory;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		computeDisplayMetrics();

		mPageWidget = new PageWidget(this, Configure.width,
				Configure.height);

		setContentView(mPageWidget);
		Logger.d(TAG, "设备的高为：" + Configure.height + "----项目的宽为===="
				+ Configure.width);
		mCurPageBitmap = Bitmap.createBitmap(Configure.width, Configure.height,
				Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap.createBitmap(Configure.width, Configure.height,
				Bitmap.Config.ARGB_8888);

		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		pagefactory = new BookPageFactory(Configure.width, Configure.height);

		pagefactory.setBgBitmap(BitmapFactory.decodeResource(
				turntest.this.getResources(), R.color.white));
		 intent = getIntent();
		 id = intent.getStringExtra("id");
		 type = intent.getStringExtra("type");
		 url = intent.getStringExtra("url");
		 chapter = intent.getStringExtra("chapter");
		 contents = intent.getStringExtra("contents");
		SetDisplay();
//		 UpdateData();
	}

	String id, type, url, contents, chapter;
	Intent intent;

	public void UpdateData() {
		Logger.d(TAG, "UpdateData");
		GetContentNovelInfoRequest novel_info = new GetContentNovelInfoRequest(
				this, id, type, "1", "2", chapter);
		new HttpHomeLoadDataTask(this, NovelInfoCallBack, false, "", false)
				.execute(novel_info);
	}

	ColumnNovelInfo novelInfo;
	ArrayList<ColumnNovelInfo> novelInfoList = new ArrayList<ColumnNovelInfo>();
	IUpdateData NovelInfoCallBack = new IUpdateData() {

		@Override
		public void updateUi(Object o) {
			GetNovelChapterInfoResponse novelRes = new GetNovelChapterInfoResponse();
			novelRes.paseRespones(o.toString());

			for (int i = 0; i < novelRes.infoList.size(); i++) {
				novelInfo = new ColumnNovelInfo();
				// novelInfo.title = novelRes.novel.title;
				novelInfo.novel_id = novelRes.infoList.get(i).novel_id;
				// novelInfo.name = novelRes.novel.name;
				novelInfo.chapter = novelRes.infoList.get(i).chapter;
				novelInfo.chapter_title = novelRes.infoList.get(i).chapter_title;
				novelInfo.contents = novelRes.infoList.get(i).contents;
				novelInfo.id = novelRes.infoList.get(i).id;
				novelInfoList.add(novelInfo);
			}

			Logger.d(TAG, "the contents is===>" + novelInfo.contents);
			contents = novelInfo.contents;
			SetDisplay();
		}

		@Override
		public void handleErrorData(String info) {
			Logger.e(TAG, info);

		}
	};

	private void computeDisplayMetrics() {
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		Configure.width = dm.widthPixels;
		Configure.height = dm.heightPixels;
	}

	public void SetDisplay() {

		try {

			pagefactory.openbook(contents);
			pagefactory.onDraw(mCurPageCanvas);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Logger.e(TAG, "文件在内存卡里面没有");
		}

		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);

		mPageWidget.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				// TODO Auto-generated method stub

				boolean ret = false;
				if (v == mPageWidget) {
					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						mPageWidget.abortAnimation();
						mPageWidget.calcCornerXY(e.getX(), e.getY());

						pagefactory.onDraw(mCurPageCanvas);
						if (mPageWidget.DragToRight()) {
							try {
								pagefactory.prePage();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (pagefactory.isfirstPage())
								return false;
							pagefactory.onDraw(mNextPageCanvas);
						} else {
							try {
								pagefactory.nextPage();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (pagefactory.islastPage())
								return false;
							pagefactory.onDraw(mNextPageCanvas);
						}
						mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					}

					ret = mPageWidget.doTouchEvent(e);
					return ret;
				}
				return false;
			}

		});

	}

}