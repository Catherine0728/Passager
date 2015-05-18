package com.android.network;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.lhl.exception.CrashInteraction;
import com.redbull.log.Logger;

import android.content.Context;
import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;

/**
 * 本类使用HTTPCLIENT来实现HTTP网络请求,实现了get和post请求方式
 * 
 * @author NapoleonBai
 * 
 */
public final class ManagerNetwork {
	/** 网络连接超时时间 */
	private static final int DEF_CONN_TIMEOUT = 8 * 1000;
	/** 网络读取信息超时时间 */
	private static final int DEF_READ_TIMEOUT = 8 * 1000;
	private static String TAG = "ManagerNetwork";

	/**
	 * 私有化构造方�?
	 */
	public static Context mContext;

	private ManagerNetwork(Context context) {
		mContext = context;
	};

	/**
	 * 
	 * 设置Post请求方式
	 * 
	 * @param url
	 *            基础URL地址
	 * @param map
	 *            请求的参数对�?
	 * @param mIManagerNetwork
	 *            网络请求回调接口
	 */

	// CrashInteraction interaction;

	public static void doPost(final String url, final Map<String, String> map,
			final IManagerNetwork mIManagerNetwork) {
		// final CrashInteraction interaction = CrashInteraction.getInstance();
		// interaction.init(mContext);
		/**
		 * 网络请求是开启一个线程,这是符合Android4.0及以上系统的标准的 因为在Android4.+中，访问网络不能在主程序中进行
		 * 否则运行时报android.os.NetworkOnMainThreadException异常
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				Logger.d(TAG, "我去执行请求咯！");
				// 运行一次后,线程就不运行了,等待下次调用再运行
				do {
					// 判断是否为空
					if (url == null || url.equals("") || map == null) {
						if (mIManagerNetwork != null) {
							// 调用请求URL不能为空方法
							mIManagerNetwork.onUrlError();
							// interaction.initStr("url","url为空");
						}
						// 结束请求
						return;
					}
					HttpPost httpPost = null;
					URI encodedUri = null;
					try {
						encodedUri = new URI(url);
						// 创建post请求对象
						httpPost = new HttpPost(encodedUri);
					} catch (URISyntaxException e) {
						// 清理多余的空格
						String encodedUrl = url.replace(' ', '+');
						httpPost = new HttpPost(encodedUrl);
						e.printStackTrace();
					}
					// 创建访问客户端对象
					HttpClient httpClient = new DefaultHttpClient();
					// 设置网络连接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT,
							DEF_CONN_TIMEOUT);

					// 设置信息读取超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.SO_TIMEOUT, DEF_READ_TIMEOUT);
					try {
						List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
						// 循环得到访问参数
						for (Map.Entry<String, String> entry : map.entrySet()) {
							String key = entry.getKey().toString();
							String value = null;
							if (entry.getValue() == null) {
								value = "";
							} else {
								value = entry.getValue().toString();
							}
							// 得到请求参数，并添加到集合中
							BasicNameValuePair basicNameValuePair = new BasicNameValuePair(
									key, value);
							nameValuePair.add(basicNameValuePair);
							for (int i = 0; i < nameValuePair.size(); i++) {
								Logger.d(TAG, "the request params is-----"
										+ nameValuePair.get(i).getName() + ","
										+ nameValuePair.get(i).getValue()
										+ ";}");
							}
						}
						Logger.d(TAG, "the request url------>" + url);
						// 设置请求格式
						httpPost.setEntity(new UrlEncodedFormEntity(
								nameValuePair, "UTF-8")); // 此处也可以改为GBK
						// 执行请求
						HttpResponse httpResponse = httpClient
								.execute(httpPost);
						if (httpResponse != null) {
							int code = httpResponse.getStatusLine()
									.getStatusCode();
							// 打印状态码
							Log.i("ManagerNetwork",
									"------------- the http request code--------------- "
											+ code);
							// 如果请求成功
							if (code == HttpStatus.SC_OK) {
								// 得到请求回来的数据,使用HttpENtity
								HttpEntity entity = httpResponse.getEntity();
								String result = EntityUtils.toString(entity)
										.trim();
								// 如果返回的数据为空,说明读取数据超时,这里统一处理为请求超时
								if (TextUtils.isEmpty(result)) {
									if (mIManagerNetwork != null) {
										mIManagerNetwork.onReadDataTimeOut();
										return;
									}
								} else {
									// 如果有数据
									if (mIManagerNetwork != null) {
										// 返回数据
										mIManagerNetwork.onSuccessDatas(result);
										return;
									}
								}

							} else if (code == HttpStatus.SC_NOT_FOUND) {

								// 如果有数据
								if (mIManagerNetwork != null) {
									// 返回数据
									mIManagerNetwork.onSuccessDatas("其实服务器没找到");
									return;
								}

							} else {
								// 释放资源
								httpPost.abort();
							}
						} else {
							// 释放资源
							httpPost.abort();
						}
					} catch (Exception e) {
						// 连接错误
						if (mIManagerNetwork != null) {
							// interaction.initStr("error",e + "");
							mIManagerNetwork.onError(e);
						}
						return;
					} catch (OutOfMemoryError e) {
						e.printStackTrace();
						return;
					} finally {
						if (httpClient != null) {
							// 请求接收,使用连接管理器关闭HTTPClient对象
							httpClient.getConnectionManager().shutdown();
							httpClient = null;
						}
					}
					return;
				} while (false);
			}
		}).start();

	}

	/**
	 * 
	 * 设置Get请求方式
	 * 
	 * @param url
	 *            基础URL地址
	 * @param map
	 *            请求的参数对�?
	 * @param mIManagerNetwork
	 *            网络请求回调接口
	 */
	public static void responseFromServiceByGet(final String url,
			final Map<String, String> map,
			final IManagerNetwork mIManagerNetwork) {
		/**
		 * 网络请求是开启一个线�?,这是符合Android4.0及以上系统的标准�? 因为在Android4.+中，访问网络不能在主程序中进�?
		 * 否则运行时报android.os.NetworkOnMainThreadException异常
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 运行�?次后,线程就不运行�?,等待下次调用再运�?
				do {
					// 判断是否为空
					if (url == null || url.equals("")) {
						if (mIManagerNetwork != null) {
							// 调用请求URL不能为空方法
							mIManagerNetwork.onUrlError();
						}
						// 结束请求
						return;
					}
					String newUrl = url;
					// 得到请求参数
					if (map != null) {
						StringBuilder sb = new StringBuilder(url);
						sb.append('?');
						for (Entry<String, String> entry : map.entrySet()) {
							String key = entry.getKey().toString();
							String value = null;
							if (entry.getValue() == null) {
								value = "";
							} else {
								value = entry.getValue().toString();
							}
							sb.append(key);
							sb.append('=');
							try {
								value = URLEncoder.encode(value, HTTP.UTF_8);
								sb.append(value);
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							sb.append('&');
						}
						sb.deleteCharAt(sb.length() - 1);// 删除�?后一�?&
						newUrl = sb.toString();
					}
					HttpGet httpGet = null;
					URI encodedUri = null;
					InputStream is = null;
					try {
						encodedUri = new URI(newUrl);
						httpGet = new HttpGet(encodedUri);
					} catch (URISyntaxException e) {
						// 清理多余的空�?
						String encodedUrl = newUrl.replace(' ', '+');
						httpGet = new HttpGet(encodedUrl);
						e.printStackTrace();
					}
					HttpClient httpClient = new DefaultHttpClient();
					// 设置链接超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.CONNECTION_TIMEOUT,
							DEF_CONN_TIMEOUT);
					// 设置请求超时
					httpClient.getParams().setParameter(
							CoreConnectionPNames.SO_TIMEOUT, DEF_READ_TIMEOUT);
					try {
						HttpResponse httpResponse = httpClient.execute(httpGet);
						if (httpResponse != null) {
							int httpCode = httpResponse.getStatusLine()
									.getStatusCode();
							// 打印状�?�码
							Logger.d("ManagerNetwork",
									"=====>>this is http request status code "
											+ httpCode);
							if (httpCode == HttpStatus.SC_OK) {
								// HttpEntity entity = httpResponse.getEntity();
								// is = entity.getContent();
								// String result = getStrFromInputstream(is);
								//
								// // 如果返回的数据为�?,说明读取数据超时,这里统一处理为请求超�?
								// if (TextUtils.isEmpty(result)) {
								// if (mIManagerNetwork != null) {
								// mIManagerNetwork.onReadDataTimeOut();
								// }
								// } else {
								// // 如果有数�?
								// if (mIManagerNetwork != null) {
								// // 返回数据
								// mIManagerNetwork.onSuccessDatas(result);
								// Logger.log("resulete is===>" + result);
								// }
								// }
							} else {
								// 释放资源
								httpGet.abort();
							}
						} else {
							// 释放资源
							httpGet.abort();
						}
					} catch (Exception e) {
						// 连接错误
						if (mIManagerNetwork != null) {
							mIManagerNetwork.onError(e);
						}
					} catch (OutOfMemoryError e) {
						e.printStackTrace();
						return;
					} finally {
						if (httpClient != null) {
							// 请求接收,使用连接管理器关闭HTTPClient对象
							httpClient.getConnectionManager().shutdown();
							httpClient = null;
						}
					}
				} while (false);
			}
		}).start();
	}

	/**
	 * 转换数据类型：把InputStream转换为字符串
	 * 
	 * @param input
	 *            数据
	 * @return 请求回的数据
	 */
	private static String getStrFromInputstream(InputStream input) {
		String result = null;
		int i = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			while ((i = input.read()) != -1) {
				baos.write(i);
			}
			result = baos.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获得最终的地址（包括301或者302等跳转后的地址）
	 * 
	 * @param from
	 *            原始地址
	 * @return 最终的地址
	 */
	public static String getFinalURL(final String url,
			final IManagerNetwork mIManagerNetwork) {
		final String to = "";
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 运行�?次后,线程就不运行�?,等待下次调用再运�?
				do {
					// 判断是否为空
					if (url == null || url.equals("")) {
						if (mIManagerNetwork != null) {
							// 调用请求URL不能为空方法
							mIManagerNetwork.onUrlError();
						}
						// 结束请求
						return;
					}

					HttpGet httpGet = null;
					URI encodedUri = null;
					InputStream is = null;
					try {
						System.out.println("the encodedUri is===>1" + url);
						encodedUri = new URI(url.trim());
						String Uri = url.replaceAll("？", "%3F");
						Uri = Uri.replaceAll(" ", "%20");
						Uri = Uri.replaceAll("&", "%26");
						System.out.println("the encodedUri is===>2" + Uri);
						httpGet = new HttpGet(Uri);
					} catch (URISyntaxException e) {
						// 清理多余的空�?
						// String encodedUrl = url.replace(' ', '+');
						String encodedUrl = url.replaceAll("？", "%3F");

						encodedUrl = encodedUrl.replaceAll(" ", "%20");

						httpGet = new HttpGet(encodedUrl);
						e.printStackTrace();
					}
					HttpClient httpClient = new DefaultHttpClient();

					HttpParams params = httpClient.getParams();
					params.setParameter(AllClientPNames.HANDLE_REDIRECTS, false);
					try {
						HttpResponse httpResponse = httpClient.execute(httpGet);
						if (httpResponse != null) {
							int httpCode = httpResponse.getStatusLine()
									.getStatusCode();
							// 打印状�?�码
							Logger.d("ManagerNetwork",
									"=====>>this is http request status code "
											+ httpCode);
							if (httpCode == HttpStatus.SC_OK) {
								// HttpEntity entity = httpResponse.getEntity();
								// is = entity.getContent();
								// String result = getStrFromInputstream(is);
								//
								// // 如果返回的数据为�?,说明读取数据超时,这里统一处理为请求超�?
								// if (TextUtils.isEmpty(result)) {
								// if (mIManagerNetwork != null) {
								// mIManagerNetwork.onReadDataTimeOut();
								// }
								// } else {
								// // 如果有数�?
								// if (mIManagerNetwork != null) {
								// // 返回数据
								// mIManagerNetwork.onSuccessDatas(result);
								// Logger.log("resulete is===>" + result);
								// }
								// }
							} else {
								// 释放资源
								httpGet.abort();
							}
						} else {
							// 释放资源
							httpGet.abort();
						}
					} catch (Exception e) {
						// 连接错误
						if (mIManagerNetwork != null) {
							mIManagerNetwork.onError(e);
						}
					} catch (OutOfMemoryError e) {
						e.printStackTrace();
						return;
					} finally {
						if (httpClient != null) {
							// 请求接收,使用连接管理器关闭HTTPClient对象
							httpClient.getConnectionManager().shutdown();
							httpClient = null;
						}
					}
				} while (false);
			}
		}).start();

		return to;
	}
}
