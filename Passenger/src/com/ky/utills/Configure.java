package com.ky.utills;

import com.ky.passenger.R;
import com.redbull.log.Logger;

import android.content.Context;
import android.os.Environment;

/**
 * this is a class to do the static variable
 * 
 * 
 * 
 * @author Catherine.Brain
 * */
public class Configure {

	/*
	 * ISDOWNED is to make the file is download or download...and the default
	 * value is zero,,and is about to download
	 */
	public static int ISDOWNED = 0;

	/*
	 * 这里设置一个变量来得到item的长度
	 */
	static public int LENGTH = 3;

	/**
	 * 
	 * 去获得当前的时间
	 * */
	public static String current_Time;
	public static long Cur_Time;

	/**
	 * 去获得当前时间的年 public static int current_Year; /** 去获得当前时间的月
	 * */
	public static int current_Month = 0;
	/**
	 * 去获得当前时间的日
	 * */
	public static int current_Date = 0;
	/**
	 * 去获得当前时间的小时
	 * */
	public static int current_Hour = 0;
	/**
	 * 去获得当前时间的分钟
	 * */
	public static int current_Minute = 0;
	/**
	 * 去获得当前时间的秒
	 * */
	public static int current_Second = 0;
	/**
	 * 去获得当前时间的毫秒数
	 * */
	public static double current_MinSecond = 0.0;
	/**
	 * 
	 http://ky.api.rssrc.com/cleint/primary/1.0/
	 * http://ky.api.rssrc.com/server/primary/1.0/
	 * */
	// public static String IP = "http://192.168.150.14/";
	public static String IP;
	public static String NETHomePath = IP + "client/";
	public static String OUTHomePath = IP + "server/";
	/**
	 * 
	 * 去定义访问的API GetIndex.php GetContentInfo.php GetContentList.php *
	 */
	public static String GETINDEX = "keyun_api/client/primary/1.0/GetIndex.php";// 首页api
	public static String GETCONTENTINFO = "keyun_api/client/primary/1.0/GetContentInfo.php";// 内容页
	public static String GETCONTENTLIST = "keyun_api/client/primary/1.0/GetContentList.php";// 列表页
	/**
	 * 判断当前需要的加密形式
	 * */

	public static String RAW = "RAW";
	public static String B64 = "B64";

	/**
	 * 
	 * 用一个枚举来表示访问的数据api以及模式
	 * */
	public static enum FunctionTagTable {

		GETINDEX(true, "primary/1.0/", "1.1", "GetIndex", RAW), GETCONTENTINFO(
				true, "primary/1.0/", "1.1", "GetContentInfo", RAW), GETCONTENTLIST(
				true, "primary/1.0/", "1.1", "GetContentList", RAW), GETCONTENTNOVEL(
				true, "primary/1.0/", "1.1", "GetContentNovel", RAW), GETCONTENTNOVELINFO(
				true, "primary/1.0/", "1.1", "GetContentNovelInfo", RAW), GETISREGISTER(
				true, "primary/1.0/", "1.1", "RegUser", RAW), GETSUBMITPOINT(
				true, "primary/1.0/", "1.1", "SubmitDm", RAW);
		private boolean isPost = false;// 传递方式

		private String address = "";// 后缀地址
		private String api = "";// 方法名
		private String ver = "";// 版本名
		private String mode = "";// 数据加密方式

		private FunctionTagTable(boolean isPost, String address, String API,
				String VER, String MODE) {
			this.isPost = isPost;
			this.address = address;
			this.api = API;
			this.ver = VER;

			this.mode = MODE;
		}

		public boolean isPost() {
			return isPost;
		}

		public void setPost(boolean isPost) {
			this.isPost = isPost;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getApi() {
			return api;
		}

		public void setApi(String api) {
			this.api = api;
		}

		public String getVer() {
			return ver;
		}

		public void setVer(String ver) {
			this.ver = ver;
		}

		public String getMode() {
			return mode;
		}

		public void setMode(String mode) {
			this.mode = mode;
		}

	}

	/**
	 * 
	 * 先设置一个vedio path
	 * */
	public static String VEDIOTEST = "http://cv1.jikexueyuan.com/201501191802/1c0df293d31291214f5af4e6854241a5/android/course_490/01/video/c490b_01_h264_sd_960_540.mp4";

	// 是否有SDCARD
	public static final boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取后缀名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot);
			}
		}
		return filename;
	}

	/**
	 * 获取文件名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('/');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				filename = filename.substring(dot + 1);
				Logger.log("filename:" + filename);
				return filename;
			}
		}
		return filename;
	}

	/**
	 * 字符串比较大小
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean compare(String s1, String s2) {

		int i = s2.compareTo(s1);
		boolean update;
		if (i <= 0) {
			update = false;
		} else {
			update = true;
		}
		return update;
	}

	/**
	 * 
	 * 当前设备的宽和高
	 * */

	public static int width, height;
	/**
	 * 
	 * 写一个值来得到当前的key值
	 * */
	public static String key;
	/**
	 * 
	 * 定义一个常量来表示媒体机固定的SSID
	 * */
	public static String SSID = "sap37";
	/**
	 * 
	 * 定义一个bool值来表示当前是否连接到外网
	 * 
	 * 默认的是没有连接，所以为false
	 * */

	public static boolean ISOUTNET = false;
	/**
	 * 
	 * 定义一个固定的图片数组
	 * */
	public static int[] image_video_Icon = { R.drawable.img_one,
			R.drawable.img_two, R.drawable.img_three, R.drawable.img_four,
			R.drawable.img_five, R.drawable.img_six, R.drawable.img_seven,
			R.drawable.img_eight, R.drawable.img_nine, R.drawable.img_tt,
			R.drawable.img_threet };
	public static int[] image_novel_icon = { R.drawable.image_novel_eight,
			R.drawable.image_novel_seven, R.drawable.image_novel_six,
			R.drawable.image_novel_five, R.drawable.image_novel_four,
			R.drawable.image_novel_three, R.drawable.image_novel_two,
			R.drawable.image_novel_one, R.drawable.image_novel_nine };
	/**
	 * 
	 * 
	 * 定义一个新闻的数组
	 * 
	 * */

	public static int[] imageNews = { R.drawable.new_one, R.drawable.new_two,
			R.drawable.new_three, R.drawable.new_four, R.drawable.new_five,
			R.drawable.new_six, R.drawable.new_seven, R.drawable.new_eight,
			R.drawable.new_nine, R.drawable.new_ten };
	/**
	 * 
	 * 
	 * 定义一个文字的数组
	 * */
	public static String[] str_array = { "热点一", "热点二" };
	/**
	 * 
	 * 定义一个小图片的数组
	 * */
	public static int[] game_icon = { R.drawable.icon_game_one,
			R.drawable.icon_game_two, R.drawable.icon_game_three,
			R.drawable.icon_game_four, R.drawable.icon_game_five,
			R.drawable.icon_game_six, R.drawable.icon_game_seven,
			R.drawable.icon_game_nine, R.drawable.icon_game_five };
	/**
	 * 
	 * 定义一个变量来表示此时应用此时所在的页面
	 * 
	 * 0表示首页表示（弹幕） ------2表示提醒 -----3表示下载 -----4表示我的的未登录的页面 ------5表示我的已经登录的页面
	 * ----6表示是其他页面
	 * */
	public static int PAGER = 1;
	/**
	 * 
	 * 定义一个变量来判断当前是否获得数据，如果没有获得数据，我们应该不让点击获得列表信息 默认为false，就是没有获得
	 * */
	public static boolean ISGETDATA = false;

	/**
	 * androidId
	 * 
	 * @param context
	 * @return
	 */
	public static String getAndroidId(Context context) {
		String str = null;
		str = DeviceInfo.getOnlyId(context);
		return str;
	}

	/**
	 * 
	 * 定义几个变量来表示当前MAC地址
	 * */

	public static String WIFIMAC;
	public static String ETHMAC;
	public static String MAC;

	public static String AndroidID;
	/**
	 * 
	 * 去连接到固定的WIFI 无线：MTALL-000F11 密码：12345678
	 * */
	// public static String WIFISSID = "MTALL-000F11";
	public static String SSIDPWd = "12345678";
	public static String WIFISSID = "saesrywer7";
	/**
	 * 
	 * 这是地图定位的key
	 * */
	public static String AK = "DnKxbGCHwGSVbPp6vrZIDnRi";
	/**
	 * 定义一个String类型的变量，然后就可以来的到当期的城市
	 * 
	 * */
	public static String location;
	/**
	 * 
	 * 应该定义一个固定的文件夹，来存储当前我们这个应用的一些数据信息
	 * */
	public static String StroageFileName = "/mnt/sdcard";
	/**
	 * 
	 * 去定义固定的文件装用户下载的电影
	 */
	public static String DOWN_VEDIOPATH = "mnt/sdcard/Passenger";
	/**
	 * 
	 * 下载游戏以及是视频以及小说的都应该分别用不同文件夹封装起来
	 * */
	public static String GameFile = DOWN_VEDIOPATH + "/DownGame/";
	public static String NovelFile = DOWN_VEDIOPATH + "/DownNovel/";

	public static String VedioFile = DOWN_VEDIOPATH + "/DownVedio/";

	public static int XORENCRYPTION = 123456;

	/**
	 * 得到当前的版本信息 如果服务器的版本大于本地应用的版本，我们就应该弹框就辛苦一系列的弹框
	 * */
	public static int localVersion = 0;
	public static int serverVersion = 0;
	/**
	 * 下载包安装路径
	 * 
	 * */
	public static final String savePath = StroageFileName + DOWN_VEDIOPATH
			+ "/update/";
	/**
	 * 下载包包名
	 * 
	 * */
	public static final String saveFileName = savePath + "update.apk";

	/**
	 * 利用xUtils实现的下载需要的常量
	 * */
	// /**
	// * 定义文件下载的固定文件夹
	// * */
	// public static final String DOWNFILE = "mnt/sdcard/Passenger";
	// public static final String GAME = DOWNFILE + "/Game/";
	// public static final String VIDEO = DOWNFILE + "/Video/";
	// public static final String NOVEL = DOWNFILE + "/Novel/";

	/**
	 * 
	 * 定义一些视频以及游戏的下载地址·
	 * */
	public static final String[] VedioPath = {
			"http://hwx.chinafilmad.com/lc.mp4",
			"http://hwx.chinafilmad.com/tbbdh.mp4",
			"http://hwx.chinafilmad.com/hjdl.mp4",
			"http://hwx.chinafilmad.com/ccnn.mp4",
			"http://hwx.chinafilmad.com/letitgo.mp4",
			"http://hwx.chinafilmad.com/rewind.mp4 " };
	public static final String[] GamePath = {
			"http://gdown.baidu.com/data/wisegame/945e6c492987b32b/91zhushou_39870.apk",
			"http://gdown.baidu.com/data/wisegame/fb9ec53a3faf29ca/anzhuoshichang_87.apk",
			"http://gdown.baidu.com/data/wisegame/874981595f6b0b92/zhenyouwang_16.apk",
			"http://gdown.baidu.com/data/wisegame/44678f0ac42a4755/oupengliulanqi_50.apk",
			"http://gdown.baidu.com/data/wisegame/19af598924f0f158/lianlianmianfeiWiFi_254.apk",
			"http://gdown.baidu.com/data/wisegame/f790f5818d8d9f93/laizidoudizhu_15.apk" };
	/**
	 * 定义一个变量来查看是否点击了编辑
	 * 默认是没有编辑
	 * 
	 * */
	public static boolean isEdit = false;

}
