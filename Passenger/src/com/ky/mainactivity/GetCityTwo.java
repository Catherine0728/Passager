package com.ky.mainactivity;

import java.io.IOException;
import java.util.List;

import com.ky.passenger.R;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 
 * 第二种获得当前城市的方法
 * */
public class GetCityTwo {

	Context mContext;

	public GetCityTwo(Context context) {
		this.mContext = context;
		openGPSSettings();
		doWork();
	}

	private void openGPSSettings() {
		LocationManager alm = (LocationManager) mContext
				.getSystemService(mContext.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(mContext, "GPS模块正常", Toast.LENGTH_SHORT).show();
			doWork();
			return;
		}

		Toast.makeText(mContext, "请开启GPS！", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		// startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面

	}

	private void doWork() {
		String msg = "";
		// TextView textView = (TextView) findViewById(R.id.tv1);

		LocationManager locationManager = (LocationManager) mContext
				.getSystemService(mContext.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		// 获得最好的定位效果
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		// 使用省电模式
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 获得当前的位置提供者
		String provider = locationManager.getBestProvider(criteria, true);
		// 获得当前的位置
		Location location = locationManager.getLastKnownLocation(provider);

		Geocoder gc = new Geocoder(mContext);
		List<Address> addresses = null;
		try {
			addresses = gc.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (addresses.size() > 0) {
			msg += "AddressLine：" + addresses.get(0).getAddressLine(0) + "\n";
			msg += "CountryName：" + addresses.get(0).getCountryName() + "\n";
			msg += "Locality：" + addresses.get(0).getLocality() + "\n";
			msg += "FeatureName：" + addresses.get(0).getFeatureName();
		}
		// textView.setText(msg);
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

}
