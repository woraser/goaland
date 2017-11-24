package com.anosi.asset.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {
	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static String getFormateDate(Date date) {
		SimpleDateFormat dateFormat = setFormat(DATE_PATTERN);
		String timeString = dateFormat.format(date);
		return timeString;
	}

	public static String getFormateDate(Date date, String pattern) {
		SimpleDateFormat dateFormat = setFormat(pattern);
		String timeString = dateFormat.format(date);
		return timeString;
	}

	private static SimpleDateFormat setFormat(String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat;
	}

	public static Date getDateByParttern(String date, String pattern) {
		SimpleDateFormat dateFormat = setFormat(pattern);
		try {
			Date resultDate = dateFormat.parse(date);
			return resultDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Date getDateByParttern(String date) {
		SimpleDateFormat dateFormat = setFormat(DATE_PATTERN);
		try {
			Date resultDate = dateFormat.parse(date);
			return resultDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getMonthTimeQuantum() {
		// 获取当前日期前一个月日期到当前日期的时间段，格式:2016/02/12-2016/03/12
		Date date = new Date();// 当前日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 格式化对象
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.MONTH, -1);// 月份减一
		String startTimeString = sdf.format(calendar.getTime());
		String endTimeString = sdf.format(date);
		String timeQuantum = startTimeString + "~" + endTimeString;
		return timeQuantum;
	}

	public static Date getBeforeMonthTime(Date date, int i) {
		return getLaterMonthTime(date, -i);
	}

	public static Date getBeforeWeekTime(Date date, int i) {
		return getLaterWeekTime(date, -i);
	}

	public static Date getBeforeDayTime(Date date, int i) {
		return getLaterDayTime(date, -i);
	}

	public static Date getBeforeYearTime(Date date, int i) {
		return getLaterYearTime(date, -i);
	}

	public static Date getLaterMonthTime(Date date, int i) {
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.MONTH, i);// 月份减一
		Date startTimeString = calendar.getTime();
		return startTimeString;
	}

	public static Date getLaterWeekTime(Date date, int i) {
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.WEDNESDAY, i);//
		Date startTimeString = calendar.getTime();
		return startTimeString;
	}

	public static Date getLaterDayTime(Date date, int i) {
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.DATE, i);//
		Date startTimeString = calendar.getTime();
		return startTimeString;
	}

	public static Date getLaterYearTime(Date date, int i) {
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.YEAR, i);//
		Date startTimeString = calendar.getTime();
		return startTimeString;
	}

}
