package com.ky.request;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.widget.CustomProgressDialog;
import com.android.widget.StringTools;
import com.ky.passenger.R;
import com.ky.utills.JsonTools;
import com.ky.utills.PrefrenceHandler;
import com.ky.utills.Configure;
import com.lhl.callback.IHomeCallBackRquest;
import com.lhl.callback.IUpdateData;
import com.redbull.log.Logger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.View;

/**
 * Home所有的http请求
 * 
 * 
 */
public class HttpHomeLoadDataTask extends
		AsyncTask<IHomeCallBackRquest, String, String> {

	private Context mContext;
	private IUpdateData callBack;
	private boolean isShownDialog;

	public static CustomProgressDialog mDialog;
	private SoftReference<CustomProgressDialog> softReference;// ---将
																// dialog储存在缓存里面

	private boolean isComplete;// ---程序是否完成
	private String requestUrl;
	private boolean isDefault;

	private boolean isMoreDialog = false;
	public static int code;

	public void setShownDialog(boolean isShownDialog) {
		this.isShownDialog = isShownDialog;
	}

	public boolean isMoreDialog() {
		return isMoreDialog;
	}

	public void setMoreDialog(boolean isMoreDialog) {
		this.isMoreDialog = isMoreDialog;
	}

	public static HttpHomeLoadDataTask instance;

	public static HttpHomeLoadDataTask getInstance() {
		if (instance == null) {
			instance = new HttpHomeLoadDataTask();
		}
		return instance;
	}

	public HttpHomeLoadDataTask() {

	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	public void setIUpdateData(IUpdateData callBack) {
		this.callBack = callBack;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public HttpHomeLoadDataTask(Context c, IUpdateData callBack,
			boolean isShownDialog, String requestUrl) {
		this.mContext = c;
		this.callBack = callBack;
		this.isShownDialog = isShownDialog;
		this.requestUrl = requestUrl;// 支持动态url

		instance = this;

		if (isShownDialog) {
			mDialog = new CustomProgressDialog(mContext);
			softReference = new SoftReference<CustomProgressDialog>(mDialog);
		}

	}

	public HttpHomeLoadDataTask(Context c, IUpdateData callBack,
			boolean isShownDialog, String requestUrl, boolean isDefault) {
		this.mContext = c;
		this.callBack = callBack;
		this.isShownDialog = isShownDialog;
		this.requestUrl = requestUrl;// 支持动态url
		this.isDefault = isDefault;// 是否地址不变

		instance = this;

		if (isShownDialog) {
			mDialog = new CustomProgressDialog(mContext);
			softReference = new SoftReference<CustomProgressDialog>(mDialog);
		}

	}

	@Override
	protected void onPreExecute() {

		isComplete = false;
		if (isShownDialog) {
			if ((!isComplete) && !this.isCancelled()) {
				mDialog = softReference.get();
				// mDialog = new CustomProgressDialog(mContext);
				if (!mDialog.isShowing()) {
					mDialog.show();
				}
				// // mDialog.setMessage("稍等片刻，精彩内容即将呈现......");
				// mDialog.setCancelable(true);
				// // mDialog.setIcon(R.drawable.icon_loading);
				// View dialogView = CommonTools.getView(mContext,
				// R.layout.hlj_loading_dialog, null);
				// // mDialog.setView(dialogView);
				// mDialog.show();
				// mDialog.getWindow().setContentView(R.layout.hlj_loading_dialog);
				// mDialog.setOnCancelListener(new DialogCancleLisen());
			} else {

			}
		}
		super.onPreExecute();
	}

	public class DialogCancleLisen implements DialogInterface.OnCancelListener {

		@Override
		public void onCancel(DialogInterface dialog) {
			if ((!HttpHomeLoadDataTask.this.isCancelled() && !isComplete)) {
				if (dialog != null) {
					dialog.cancel();
					dialog.dismiss();
					dialog = null;
				}
				HttpHomeLoadDataTask.this.cancel(true);
			} else {
				if (dialog != null) {
					dialog.cancel();
					dialog.dismiss();
					dialog = null;
				}
			}
		}

	}

	private IHomeCallBackRquest request;

	@Override
	protected void onPostExecute(String resultStr) {
		String postTAG = "onPostExecute";
		super.onPostExecute(resultStr);

		try {
			isComplete = true;
			if (isShownDialog) {
				Logger.log("isMoreDialog:" + isMoreDialog()
						+ "<><><><><><><><><><>");
				if (isMoreDialog()) {
					postExecuteHandler.sendEmptyMessageDelayed(0, 5 * 1000);
				} else {
					if (mDialog != null) {
						mDialog.dismiss();
						mDialog.cancel();
						// mDialog = null;
					}
				}
				// else {
				// if (mDialog != null) {
				// mDialog.dismiss();
				// mDialog.cancel();
				// }
				// }
			}
			if (HttpRequest.httpPostState) {
				if (callBack != null) {
					 Logger.d(postTAG, "resultStr:" + resultStr);
					JSONObject jo = new JSONObject(resultStr);
					String mode = JsonTools.getString(jo, "MODE");
					Logger.d(postTAG, "----JSONObject--------------" + jo);
					code = JsonTools.getInt(jo, "CODE");
					Logger.d(postTAG, "----code--------------" + code);
					if (code == 0) {
						if (Configure.B64.equals(mode)) {
							String result = (String) jo.get("RESULT");
							byte[] b = Base64.decode(result, 1);
							String s = new String(b, "utf-8");
							Logger.d(postTAG, s);
							callBack.updateUi(s);

						} else if (Configure.RAW.equals(mode)) {
							if (!StringTools.isNullOrEmpty(String.valueOf(jo
									.get("RESULT")))) {
								JSONObject result = jo.getJSONObject("RESULT");
								Logger.d(postTAG, result.toString());
								callBack.updateUi(result);

							} else {
								callBack.updateUi("");
							}
						}
					} else {
						JSONObject obj = jo.getJSONObject("MSG");

						String cnStr = obj.getString("cn");
						String enStr = obj.getString("en");
						if (!StringTools.isNullOrEmpty(cnStr)) {
							callBack.handleErrorData(cnStr);
						} else if (!StringTools.isNullOrEmpty(enStr)) {
							callBack.handleErrorData(enStr);
						}

					}

				} else {
					Logger.log("HttpRequstTask's callback is null");
				}
			} else {
				if (callBack != null) {
					callBack.handleErrorData(resultStr);
				} else {
					Logger.log("HttpRequstTask's callback is null");
				}
			}
			mContext = null;
		} catch (Exception e) {
			Logger.log("onPostExecute error=" + e.getMessage());
			if (mDialog != null) {
				mDialog.dismiss();
				mDialog.cancel();
				mDialog = null;
			}
			if (callBack != null) {
				callBack.handleErrorData("未知错误");
			}
			mContext = null;
		}

	}

	Handler postExecuteHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			Logger.log("-----------------postExecuteHandler--------------");
			if (mDialog != null) {
				mDialog.dismiss();
				mDialog.cancel();
				// mDialog = null;
			}
			super.handleMessage(msg);
		};

	};

	// ---拼接url的字符串
	private String getParams(String params) throws JSONException {
		JSONObject paramsJson = new JSONObject(params);
		StringBuilder sb = new StringBuilder();

		Iterator<String> iterator = paramsJson.keys();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			sb.append("&");
			sb.append(key);
			sb.append("=");

			String value = String.valueOf(paramsJson.get(key));

			try {
				sb.append(URLEncoder.encode(StringTools.defaultToUtf(value),
						"UTF-8"));
			} catch (Exception e) {
				Logger.log("Encoding error=" + e.getMessage());
			}

		}
		String url = sb.toString();
		String newUrl = replaceFirstChar(url);
		return newUrl;
	}

	/**
	 * 首个"&"替换成"?"
	 * 
	 * @param url
	 * @return
	 */
	private String replaceFirstChar(String url) {
		StringBuffer sb = new StringBuffer();
		String s = url.substring(1);
		sb.append("?");
		sb.append(s);
		return sb.toString();
	}

	private HttpPost getHttpPost(IHomeCallBackRquest request) {

		List<NameValuePair> qparams = new ArrayList<NameValuePair>();

		try {
			// 把请求的 json对象拆开以post传递
			// JSONObject json = new JSONObject(request.getInfo());
			// for (Iterator it = json.keys(); it.hasNext();) {
			// String key = (String) it.next();
			// String value = (String) json.get(key);
			// qparams.add(new BasicNameValuePair(key, value));
			// }

			JSONObject json = new JSONObject(request.GetInfo());
			qparams.add(new BasicNameValuePair("data", json.toString()));

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String url = "";

		if (StringTools.isNullOrEmpty(requestUrl)) {
			if (Configure.ISOUTNET) {
				/**
				 * 
				 * 没有在内网状态下的
				 * */
				if (StringTools.isNullOrEmpty(Configure.OUTHomePath)) {
					Configure.OUTHomePath = PrefrenceHandler
							.getServerAddress(mContext);
				}

				if (isDefault) {
					url = Configure.OUTHomePath
							+ request.GetNetTag().getAddress()
							+ request.GetNetTag().getVer() + ".php";
				} else {

					url = Configure.OUTHomePath
							+ request.GetNetTag().getAddress()
							+ request.GetNetTag().getVer() + ".php";
				}
			} else {
				/**
				 * 
				 * 在内网状态下
				 * */

				if (StringTools.isNullOrEmpty(Configure.NETHomePath)) {
					Configure.NETHomePath = PrefrenceHandler
							.getServerAddress(mContext);
				}

				if (isDefault) {
					url = Configure.NETHomePath
							+ request.GetNetTag().getAddress()
							+ request.GetNetTag().getVer() + ".php";
				} else {

					url = Configure.NETHomePath
							+ request.GetNetTag().getAddress()
							+ request.GetNetTag().getVer() + ".php";
				}
			}

		} else {
			url = requestUrl;
		}

		Logger.log("url:--------->" + url);

		HttpPost httpPost = new HttpPost(url);
		// HttpPost httpPost = new HttpPost(HomeConstants.homePath);

		UrlEncodedFormEntity paramEntity = null;
		org.apache.http.Header header = new BasicHeader("User-Agent",
				"Chrome/32.0.1700.107");
		/**
		 * add cookie
		 */
		Cookie cookie = new BasicClientCookie("fastweb_title",
				"fastwebtongzhuo100");

		// Cookie cookie1 = new BasicClientCookie("PHPSESSID","");

		CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.add(cookie);
		List<Header> headerList = cookieSpecBase.formatCookies(cookies);
		try {
			paramEntity = new UrlEncodedFormEntity(qparams, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.log("UnsupportedEncodingException=="
					+ e.getLocalizedMessage());
		}
		Logger.log(headerList.get(0).getName() + ":"
				+ headerList.get(0).getValue());
		httpPost.addHeader(header);
		httpPost.addHeader(headerList.get(0));
		httpPost.setEntity(paramEntity);
		return httpPost;
	}

	private String httpGet(String url) throws Exception {
		HttpGet get = new HttpGet(url);
		String strResult = "";

		get.addHeader("Content-Type", "application/json;charset=utf-8");
		strResult = HttpRequest.httpRequestGet(get);
		return strResult;
	}

	@Override
	protected String doInBackground(IHomeCallBackRquest... params) {
		// ---- 进行网络传输数据
		String result = "";
		request = params[0];
		try {
			if (request.GetNetTag().isPost()) {
				// POST请求
				HttpPost post = getHttpPost(request);
				result = HttpRequest.httpRequestPost(post);
			} else {
				String url;
				// GET
				if (Configure.ISOUTNET) {
					/**
					 * 
					 * 没有在内网的状态下
					 * */
					url = Configure.OUTHomePath
							+ request.GetNetTag().getAddress();
				} else {
					/**
					 * 
					 * 在内网的状态下
					 * */
					url = Configure.NETHomePath
							+ request.GetNetTag().getAddress();
				}

				if (null != request.GetInfo()) {
					url += getParams(request.GetInfo());
				}
				result = httpGet(url);
			}
		} catch (Exception e) {
			Logger.log("NETERROR--" + e.getMessage());
		}
		return result; // TODO Auto-generated method stub
	}

}
