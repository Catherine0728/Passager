package com.ky.response;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ky.beaninfo.ColumnGameInfo;
import com.ky.beaninfo.ColumnNewsInfo;
import com.ky.beaninfo.ColumnNovelInfo;
import com.ky.beaninfo.ColumnVedioInfo;
import com.ky.beaninfo.CustermInfo;
import com.ky.beaninfo.UserInfo;
import com.redbull.log.Logger;

/*获取当前注册返回的数据*/
public class RegisterResponse extends BaseResponse {
	String TAG = "RegisterResponse";
	public UserInfo useInfo;
	boolean isLogin = false;

	public RegisterResponse(boolean isLogin) {
		this.isLogin = isLogin;
	}

	@Override
	public void paseRespones(String js) {
		try {
			JSONObject json = new JSONObject(js);
			JSONObject item = json.getJSONObject("item");
			useInfo = new UserInfo();
			useInfo.id = item.getString("uid");
		
			if (isLogin) {

//				useInfo.username = item.getString("username");
//				useInfo.email = item.getString("email");
//				useInfo.mobile = item.getString("mobile");
//				useInfo.status = item.getString("status");
				useInfo.point = item.getString("point");
				useInfo.key = item.getString("key");
			} else {
				useInfo.key = item.getString("keys");
			}
			Logger.d(TAG, "the user_id is==>" + useInfo.id
					+ "----the key is=====>" + useInfo.key);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
