package com.lhl.callback;

/**
 * 更新页面的回调接口
 * 
 * @author Catherine
 * 
 */
public interface IUpdateData {
	// ----返回数据处理后更新页面接口
	public void updateUi(Object o);

	public void handleErrorData(String info);

}
