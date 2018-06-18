package com.zfm.gleaning.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于处理时间的工具类
 * 
 * @author zm
 *
 */
public class TimeUtils {
	private static int HOUR_LONG = 3600000; // 一小时 3600000毫秒
	private static int DAY_LONG = 3600000 * 24;// 一天 3600000*24毫秒
	
	/**
	 * 根据生日获取年龄
	 * @param birthday
	 * @return
	 */
	public static int transformToAge(long birthday){
		long ms = getNow() - birthday;
		return (int) (ms / DAY_LONG);
	}
	
	/**
	 * 将日期字符串按指定格式转换成日期
	 * @param strDate
	 * @param format
	 * @return
	 */
	public static Date parse(String strDate,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return date;
	}
	/**
	 * 将日期按指定格式转换成日期字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String parse(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String strDate;
		strDate = sdf.format(date);
		return strDate;
	}
	
	// 将时间戳转换为 格式"yyyy-MM-dd HH:mm:ss"
	public static String TimeToString(Long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		Date date = c.getTime();
		return sdf.format(date);
	}

	// 将时间戳转换为 格式"yyyy-MM-dd"
	public static String TimeToStringByMonth(Long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		Date date = c.getTime();
		return sdf.format(date);
	}

	// 将时间格式"yyyy-MM-dd"转化为时间戳 (获取该日开始时间 yyyy-MM-dd 00:00:00的时间戳)
	public static long StringToTime(String timeString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(timeString);
			return date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 时间格式"yyyy-MM" 获取该月开始时间 yyyy-MM-01 00:00:00
	public static long MonthStringToStartTime(String timeString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		try {
			Date date = sdf.parse(timeString);
			return date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 时间格式"yyyy-MM" 获取该月最后一天23时59分59秒999毫秒 yyyy-MM-dd-23:59:59 999
	public static long MonthStringToEndTime(String timeString) {
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		try {
			Date date = sdf.parse(timeString);
			ca.setTime(date);
			ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取该月最后一天
			ca.set(Calendar.HOUR_OF_DAY, 23);
			ca.set(Calendar.MINUTE, 59);
			ca.set(Calendar.SECOND, 59);
			ca.set(Calendar.MILLISECOND, 999);
			return ca.getTime().getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 获取当前时间戳
	public static long getNow() {
		return new Date().getTime();
	}

	// 获取当月开始时间戳 yyyy-MM-01 00:00:00 000
	public static long getStartTimeOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	// 获取当月结束时间戳 yyyy-MM-30/31 23:59:59 999
	public static long getEndTimeOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1); // 下月
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis() - 1;// 下月起始时间-1
	}

	// 获取某月的开始时间yyyy-MM-01 00:00:00 000
	public static long getOneMonthStart(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	// 获取某月的结束时间yyyy-MM-01 00:00:00 000
	public static long getOneMonthEnd(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis() - 1;
	}

	// 获取当天0点的时间戳
	public static long getStartTimeOfDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	// 获取当天结束的时间戳
	public static long getEndTimeOfDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTimeInMillis();
	}

	// 获取当前周七天的开始时间戳 /周day,0点（day从1-7）
	public static long getDayStartTimeOfWeek(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis() + DAY_LONG * (day - 1);
	}

	// 获取当前周七天的结束时间戳 /周day, 23:59:59 999（day从1-7）
	public static long getDayEndTimeOfWeek(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTimeInMillis() + DAY_LONG * (day - 1);
	}

	// 获取那个月的每一周的开始和结束日期
	public static Map<String, Object> getfWeeksOfMonth(String date) throws Exception {
		// date参数如：String date = "2013-09";
		String[] weekStartDay = new String[10];// 保存date月5周的每周的开始日期,格式如"2017-11-04"
		String[] weekEndDay = new String[10];// 保存date月5周的每周的结束日期
		Map<String, Object> resmap = new HashMap<String, Object>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Date date1 = dateFormat.parse(date);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date1);
		int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		System.out.println("days:" + days);
		int count = 0;
		for (int i = 1; i <= days; i++) {
			DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			Date date2 = dateFormat1.parse(date + "-" + i);
			calendar.clear();
			calendar.setTime(date2);
			int k = new Integer(calendar.get(Calendar.DAY_OF_WEEK));
			if (k == 1) {// 若当天是周日
				count++;
				if (i - 6 <= 1) {
					weekStartDay[count] = date + "-01";
				} else {
					if ((i - 6) < 10) {
						weekStartDay[count] = date + "-0" + (i - 6);
					} else {
						weekStartDay[count] = date + "-" + (i - 6);
					}
				}
				if (i < 10) {
					weekEndDay[count] = date + "-0" + i;
				} else {
					weekEndDay[count] = date + "-" + i;
				}
			}
			if (k != 1 && i == days) {// 若是本月最后一天，且不是周日
				count++;
				if ((i - k + 2) < 10) {
					weekStartDay[count] = date + "-0" + (i - k + 2);
				} else {
					weekStartDay[count] = date + "-" + (i - k + 2);
				}
				if (i < 10) {
					weekEndDay[count] = date + "-0" + i;
				} else {
					weekEndDay[count] = date + "-" + i;
				}
			}
		}
		resmap.put("weekStartDay", weekStartDay);// 保存date月5周的每周的开始日期,格式如"2017-11-04"
		resmap.put("weekEndDay", weekEndDay);// 保存date月5周的每周的结束日期
		resmap.put("count", count);// 这个月的周数
		resmap.put("days", days);
		return resmap;
	}

	// 将日期转化为当天的开始的时间戳，"2017-12-28"--> 2017-12-28 00:00:00 000的时间戳
	public static long getDayStart(Date day) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(day);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTimeInMillis();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	// 将日期转化为当天的结束的时间戳，"2017-12-28"--> 2017-12-28 23:59:59 999的时间戳
	public static long getDayEnd(Date day) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(day);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			return calendar.getTimeInMillis();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	// 将日期转化为当天的开始的时间戳，"2017-12-28"--> 2017-12-28 00:00:00 000的时间戳
	public static long getDayStart(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date day = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(day);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}

	// 将日期转化为当天的结束的时间戳，"2017-12-28"--> 2017-12-28 23:59:59 999的时间戳
	public static long getDayEnd(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date day = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(day);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			return calendar.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}

	// 获取当前周的开始时间戳 /周一0点
	public static long getStartTimeOfWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	// 获取当前周的周二开始时间戳 /周二0点
	public static long getStartTimeOfTues() {
		return getStartTimeOfWeek() + DAY_LONG;
	}

	// 获取当前周的周三开始时间戳 /周三0点
	public static long getStartTimeOfWedn() {
		return getStartTimeOfWeek() + DAY_LONG * 2;
	}

	// 获取当前周的周四开始时间戳/周四0点
	public static long getStartTimeOfThur() {
		return getStartTimeOfWeek() + DAY_LONG * 3;
	}

	// 获取当前周的周五开始时间戳/周五0点
	public static long getStartTimeOfFri() {
		return getStartTimeOfWeek() + DAY_LONG * 4;
	}

	// 获取当前周的周六开始时间戳
	public static long getStartTimeOfSatur() {
		return getStartTimeOfWeek() + DAY_LONG * 5;
	}

	// 获取当前周的周日开始时间戳
	public static long getStartTimeOfSun() {
		return getStartTimeOfWeek() + DAY_LONG * 6;
	}

	public static int getHOUR_LONG() {
		return HOUR_LONG;
	}

	public static void setHOUR_LONG(int hOUR_LONG) {
		HOUR_LONG = hOUR_LONG;
	}

	public static int getDAY_LONG() {
		return DAY_LONG;
	}

	public static void setDAY_LONG(int dAY_LONG) {
		DAY_LONG = dAY_LONG;
	}
}
