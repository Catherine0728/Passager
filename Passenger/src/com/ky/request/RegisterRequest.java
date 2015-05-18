package com.ky.request;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ky.utills.Configure.FunctionTagTable;
import com.lhl.callback.IHomeCallBackRquest;
import com.redbull.log.Logger;

/**
 * 
 * 注册的请求
 * */
public class RegisterRequest extends BaseRequest implements IHomeCallBackRquest {
	public String ReTAG = "GetIndexRequest";
	Context mContext;
	String username, password, phonenumber, email;
	Boolean isLogin = false;

	public RegisterRequest(Context context, String userName,
			String phoneNumber, String pass, String email, boolean isLogin) {
		mContext = context;
		this.username = userName;
		this.password = pass;
		this.email = email;
		this.phonenumber = phoneNumber;
		this.isLogin = isLogin;

	}

	@Override
	public String GetInfo() {
		API = FunctionTagTable.GETISREGISTER.getApi();
		VER = FunctionTagTable.GETISREGISTER.getVer();
		MODE = FunctionTagTable.GETISREGISTER.getMode();
		JSONObject dataJson = new JSONObject();
		try {
			dataJson.put("username", username);
			dataJson.put("password", password);
			if (isLogin) {

			} else {
				dataJson.put("mobile", phonenumber);
				dataJson.put("email", email);

			}

		} catch (JSONException e) {
			Logger.e(ReTAG, e + "");
		}
		return getJsonString(dataJson,true);
	}

	@Override
	public FunctionTagTable GetNetTag() {
		// TODO Auto-generated method stub
		return FunctionTagTable.GETISREGISTER;
	}

}
