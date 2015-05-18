package com.ky.mainactivity;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
/**
 * 
 * 第一种方式获得当前城市的方法
 * */
public class GetCity{
	/**
	 * 借助Google MAP 通过用户当前经纬度 获得用户当前城市
	 */
	static final String GOOGLE_MAPS_API_KEY = "abcdefg";

	private LocationManager locationManager;
	private Location currentLocation;
	private String city = "全国";

	public GetCity(Context context) {
		this.locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	// 只是简单的获取城市 不需要实时更新 所以这里先注释
		 this.locationManager.requestLocationUpdates(
		 LocationManager.GPS_PROVIDER, 1000, 0,
		 new LocationListener() {
		 public void onLocationChanged(Location loc) {
		 //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
		 // Save the latest location
		 currentLocation = loc;
		 // Update the latitude & longitude TextViews
		 System.out
		 .println("getCity()"
		 + (loc.getLatitude() + " " + loc
		 .getLongitude()));
		 }
		
		 public void onProviderDisabled(String arg0) {
		 System.out.println(".onProviderDisabled(关闭)"+arg0);
		 }
		
		 public void onProviderEnabled(String arg0) {
		 System.out.println(".onProviderEnabled(开启)"+arg0);
		 }
		
		 public void onStatusChanged(String arg0, int arg1,
		 Bundle arg2) {
		 System.out.println(".onStatusChanged(Provider的转态在可用、" +
		 "暂时不可用和无服务三个状态直接切换时触发此函数)"+
		 arg0+" "+arg1+" "+arg2);
		 }
		 });
		currentLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (currentLocation == null)
			currentLocation = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	}

	/**
	 * 开始解析
	 */
	public void start() {
		if (currentLocation != null) {
			new Thread() {
				public void run() {
					String temp = reverseGeocode(currentLocation);
					if (temp != null && temp.length() >= 2)
						city = temp;
				}
			}.start();
		} else {
			System.out.println("GetCity.start()未获得location");
		}
	}

	/**
	 * 获得城市
	 * 
	 * @return
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 通过Google map api 解析出城市
	 * 
	 * @param loc
	 * @return
	 */
	public String reverseGeocode(Location loc) {
		// http://maps.google.com/maps/geo?q=40.714224,-73.961452&output=json&oe=utf8&sensor=true_or_false&key=your_api_key
		String localityName = "";
		HttpURLConnection connection = null;
		URL serverAddress = null;

		try {
			// build the URL using the latitude & longitude you want to lookup
			// NOTE: I chose XML return format here but you can choose something
			// else
			serverAddress = new URL("http://maps.google.com/maps/geo?q="
					+ Double.toString(loc.getLatitude()) + ","
					+ Double.toString(loc.getLongitude())
					+ "&output=xml&oe=utf8&sensor=true&key="
					+ GOOGLE_MAPS_API_KEY);
			// set up out communications stuff
			connection = null;

			// Set up the initial connection
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);

			connection.connect();

			try {
				InputStreamReader isr = new InputStreamReader(
						connection.getInputStream());
				InputSource source = new InputSource(isr);
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				XMLReader xr = parser.getXMLReader();
				GoogleReverseGeocodeXmlHandler handler = new GoogleReverseGeocodeXmlHandler();

				xr.setContentHandler(handler);
				xr.parse(source);

				localityName = handler.getLocalityName();
				System.out.println("GetCity.reverseGeocode()" + localityName);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("GetCity.reverseGeocode()" + ex);
		}

		return localityName;
	}

	/**
	 * The final piece of this puzzle is parsing the xml that is returned from
	 * google’s service. For this example I am using the java SAX (simple api
	 * for xml) parser. The final class to show here is
	 * GoogleReverseGeocodeXmlHandler. In my example, I only want the name of
	 * the city the user is in, so my XmlHandler class I’m about to show only
	 * parses that piece of information. If you want to grab more complete
	 * information (I’ll also give an example file that contains the XML
	 * returned by Google), you’ll have to add more to this class
	 * 
	 * @author Administrator
	 * 
	 */
	public class GoogleReverseGeocodeXmlHandler extends DefaultHandler {
		private boolean inLocalityName = false;
		private boolean finished = false;
		private StringBuilder builder;
		private String localityName;

		public String getLocalityName() {
			return this.localityName;
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			if (this.inLocalityName && !this.finished) {
				if ((ch[start] != '\n') && (ch[start] != ' ')) {
					builder.append(ch, start, length);
				}
			}
		}

		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			super.endElement(uri, localName, name);

			if (!this.finished) {
				if (localName.equalsIgnoreCase("LocalityName")) {
					this.localityName = builder.toString();
					this.finished = true;
				}

				if (builder != null) {
					builder.setLength(0);
				}
			}
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			builder = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, name, attributes);

			if (localName.equalsIgnoreCase("LocalityName")) {
				this.inLocalityName = true;
			}
		}
	}
}