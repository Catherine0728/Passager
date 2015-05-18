package com.ky.utills;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.redbull.log.Logger;

/**
 * @author Catherine.Brain
 * 
 *         关于时间的操作
 * 
 * */
public class DateToFormat {

	public DateToFormat() {

	}

	public void toGet() {
		Calendar now = Calendar.getInstance();
		System.out.println("年: " + now.get(Calendar.YEAR));
		System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");
		System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
		System.out.println("时: " + now.get(Calendar.HOUR_OF_DAY));
		System.out.println("分: " + now.get(Calendar.MINUTE));
		System.out.println("秒: " + now.get(Calendar.SECOND));
		System.out.println("当前时间毫秒数：" + now.getTimeInMillis());
		System.out.println(now.getTime());

//		StaticVariable.current_Year = now.get(Calendar.YEAR);
		Configure.current_Month = (now.get(Calendar.MONTH) + 1);
		Configure.current_Date = now.get(Calendar.DAY_OF_MONTH);
		Configure.current_Hour = now.get(Calendar.HOUR_OF_DAY);
		Configure.current_Minute = now.get(Calendar.MINUTE);
		Configure.current_Second = now.get(Calendar.SECOND);
		Configure.current_MinSecond = now.getTimeInMillis();
		Date d = new Date();
		System.out.println(d);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateNowStr = sdf.format(d);
		Configure.current_Time = dateNowStr;
		System.out.println("格式化后的日期：" + dateNowStr);

		String str = "2012-1-13 17:26:33"; // 要跟上面sdf定义的格式一样
		Date today = null;
		try {
			today = sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("字符串转成日期：" + today);
		// float currentTiem = System.currentTimeMillis();
		// System.out.println("the currentTiem is===>" + currentTiem);
	}

	public void getHOrS(Calendar caledar) {
		Calendar now = caledar.getInstance();
		Configure.current_Hour = now.get(Calendar.HOUR_OF_DAY);
		Configure.current_Minute = now.get(Calendar.MINUTE);
		Configure.current_Second = now.get(Calendar.SECOND);
		Configure.Cur_Time = now.getTimeInMillis() / 1000;
	}
}
