package com.android.network;

/**
 * 网络访问设置的接�?,即提供给调用者回调方�?,包含请求成功返回数据,请求失败
 * 
 * @author NapoleonBai
 * 
 */
public interface IManagerNetwork {
	/**
	 * 传入请求成功返回的数�?
	 * 
	 * @param str
	 *            返回的数�?
	 */
	public void onSuccessDatas(String str);

	/**
	 * 请求失败返回的错误原�?
	 * 
	 * @param e
	 */
	public void onError(Exception e);

	/**
	 * 请求地址不能为空
	 */
	public void onUrlError();

	/**
	 * 读取数据超时
	 */
	public void onReadDataTimeOut();
}