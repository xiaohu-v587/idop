package com.goodcol.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goodcol.controller.MainCtl;
import com.goodcol.core.log.Logger;

/**
 * 工具类 - 日期时间
 * 
 * @author Liuhukang
 * 
 */
public class DateTimeUtil {

	/** 日志工具 */
	private static Logger logger = Logger.getLogger(DateTimeUtil.class);

	public final static SimpleDateFormat df_time = new SimpleDateFormat("yyyy/MM/dd");

	/**
	 * 获取系统日期+时间
	 * 
	 * 格式 :2010-07-20 18:45:44
	 * 
	 * @return
	 */
	public static String getTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/**
	 * 获取精确到毫秒的的系统日期+时间
	 * 
	 * 格式 :100720184544123
	 * 
	 * @return
	 */
	public static String getMSTime() {
		return new SimpleDateFormat("yyMMddHHmmssS").format(new Date());
	}

	/**
	 * 判断两时间是否是同一天
	 * 
	 * @param from
	 *            第一个时间点
	 * @param to
	 *            第二个时间点
	 * @return true:是同一天,false:非同一天
	 */
	public static boolean isSameDay(Date from, Date to) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String firstDate = df.format(from);
		String secondDate = df.format(to);
		return firstDate.equals(secondDate);
	}

	/**
	 * 获取系统日期+时间
	 * 
	 * 格式 :2010-07-20
	 * 
	 * @return
	 */
	public static String getChinaTime() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/**
	 * 获取系统日期+时间
	 * 
	 * 格式 :20100720
	 * 
	 * @return
	 */
	public static String getShortChinaTime() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	/**
	 * 获取星期
	 * 
	 * @return
	 */
	public static String getWeek() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return new SimpleDateFormat("EEEE").format(c.getTime());
	}

	/**
	 * 获取系统日期
	 * 
	 * 格式 :2010-07-20
	 * 
	 * @return
	 */
	public static String getDateTime() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/**
	 * 获取本月第一天日期
	 * 
	 * @return
	 */
	public static String getFirstDayOfMonth() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DATE, 1);
		SimpleDateFormat simpleFormate = new SimpleDateFormat(" yyyy-MM-dd ");
		return simpleFormate.format(calendar.getTime()).trim();
	}

	/**
	 * 获取系统日期
	 * 
	 * 格式 : 20100106
	 * 
	 * @return 8位长度
	 */
	public static String getPathName() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	/**
	 * 获取？天前的日期
	 * 
	 * 格式 : 20100105
	 * 
	 * @param date
	 *            天数
	 * @return
	 */
	public static String geYesterDay(int date) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -date);
		return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	}

	/**
	 * 取得日期型
	 * 
	 * @param date
	 *            长日期型
	 * @return
	 */
	public static String getShortDate(String date) {
		if (date != null) {
			String s = date.replaceAll("-", "");
			return s;
		}
		return null;
	}

	/**
	 * 获取？天前的日期
	 * 
	 * 格式 : 2010-07-20
	 * 
	 * @param date
	 *            天数
	 * @return
	 */
	public static String getYesterDayTime(int date) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -date);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}

	/**
	 * 获取？个月前的日期
	 * 
	 * 格式 : 2010-07-20
	 * 
	 * @param month
	 *            月数
	 * @return @return 8位长度
	 */
	public static String getLastDate(int month) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -month);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}

	/**
	 * String转Date
	 * 
	 * @param time
	 * @return
	 */
	public static Date format(String time) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(time);
		} catch (ParseException e) {
			logger.error("日期格式错误：" + e);
		}
		return null;
	}

	/**
	 * 验证日期的合法性
	 * 
	 * 格式 : yyyy-MM-dd
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static boolean isDate(String date) {
		// 使用正则表达式 测试 字符是否符合 dddd-dd-dd 的格式(d表示数字)
		Pattern p = Pattern.compile("\\d{4}+[-]\\d{2}+[-]\\d{2}+");
		Matcher ma = p.matcher(date);
		if (!ma.matches()) {
			return false;
		}
		// 获取年月日
		int first = date.indexOf('-');
		int second = date.lastIndexOf('-');
		String year = date.substring(0, first);
		String month = date.substring(first + 1, second);
		String day = date.substring(second + 1, date.length());
		int maxDays = 31;
		int y = Integer.parseInt(year);
		int m = Integer.parseInt(month);
		int d = Integer.parseInt(day);
		if (m > 12 || m < 1) {
			return false;
		} else if (m == 4 || m == 6 || m == 9 || m == 11) {
			maxDays = 30;
		} else if (m == 2) {
			if (y % 4 > 0)
				maxDays = 28;
			else if (y % 100 == 0 && y % 400 > 0)
				maxDays = 28;
			else
				maxDays = 29;
		}
		if (d < 1 || d > maxDays) {
			return false;
		}
		return true;
	}

	/**
	 * 获取年份
	 * 
	 * 格式 :2007
	 * 
	 * @return
	 */
	public static String getYear() {
		return new SimpleDateFormat("yyyy").format(new Date());
	}

	/**
	 * 获取月份
	 * 
	 * 格式 :07
	 * 
	 * @return
	 */
	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * 获得今天在本月的第几天
	 * 
	 * 格式 :22
	 * 
	 * @return
	 */
	public static int getDayOfMonth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 按指定日期计算(Date类型)
	 * 
	 * @param date
	 *            指定日期(格式：2011-08-28)
	 * @param day
	 *            天数：为正则是后面一天;负反之
	 * @return
	 */
	public static Date getDistanceDay(String date, int day) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(dateFormat.parse(date));
			c.add(Calendar.DATE, day);
			return c.getTime();
		} catch (ParseException e) {
			logger.error("转换日期格式出错：" + e);
		}
		return null;
	}

	/**
	 * 按指定日期计算(String类型)
	 * 
	 * @param date
	 *            指定日期(格式：2011-08-28)
	 * @param day
	 *            天数：为正则是后面一天;负反之
	 * @return
	 */
	public static String getDistanceDays(String date, int day) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(getDistanceDay(date, day));
	}
	public static String getDistanceDays1(String date, int day) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(getDistanceDay1(date, day));
	}
	
	private static Date getDistanceDay1(String date, int day) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(dateFormat.parse(date));
			c.add(Calendar.DATE, day);
			return c.getTime();
		} catch (ParseException e) {
			logger.error("转换日期格式出错：" + e);
		}
		return null;
	}

	/**
	 * Date转String型
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static String dateToString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	/**
	 * 获取两个日期之间间隔的天数
	 * 
	 * @param minDate
	 * @param maxDate
	 * @return
	 */
	public static int getSpaceDate(String minDate, String maxDate) {
		SimpleDateFormat datetemp = new SimpleDateFormat("yyyy-MM-dd");
		int day = 0;
		try {
			Date maxTemp = datetemp.parse(maxDate);
			Date minTemp = datetemp.parse(minDate);
			day = (int) ((maxTemp.getTime() - minTemp.getTime()) / 1000 / 60 / 60 / 24);
		} catch (ParseException e) {
			logger.error("日期转换失败,详细信息为：" + e.getMessage());
		}
		return day;
	}

	/**
	 * 两个日期型比较大小
	 * 
	 * @param first
	 *            格式：2011-08-28
	 * @param two
	 *            格式：2011-08-28,为NULL代表今天
	 * @return
	 */
	public static boolean compare(String first, String two) {
		int a = 0;
		int b = 0;
		if (two == null || two.equals(""))
			two = DateTimeUtil.getDateTime();
		try {
			a = Integer.parseInt(first.replace("-", ""));
			b = Integer.parseInt(two.replace("-", ""));
		} catch (Exception e) {
			logger.error("日期格式错误：" + e);
		}
		return b <= a;
	}

	public static boolean compare(String date) {
		int a = 0;
		int b = 0;
		String two = DateTimeUtil.getYesterDayTime(1);
		try {
			a = Integer.parseInt(date.replace("-", ""));
			b = Integer.parseInt(two.replace("-", ""));
		} catch (Exception e) {
			logger.error("输入日期格式错误!");
		}
		return b >= a;
	}

	/**
	 * 转换日期格式
	 * 
	 * 将如2012-02-02 转换成 20120202
	 *  
	 * @param date
	 *            日期
	 * @return
	 */
	public static String changeString(String date) {
		return date.replace("-", "");
	}
	
	/**
	 * 转换日期字符串 yyyy-MM-dd hh:mi:ss 转换成 yyyyMMdd
	 */
	public static String changeDateString(String date) {
		if (date != null && date.indexOf("-") != -1) {
			date = date.replace("-", "");
		}
		if (date != null && date.indexOf(":") != -1) {
			date = date.replace(":", "");
		}
		if (date != null && date.indexOf(" ") != -1) {
			date = date.replace(" ", "");
			date = date.trim();
		}
		if (date != null && date.length() > 9) {
			date = date.substring(0, 8);
		}
		return date;
	}
	/**
	 * 转换日期字符串 yyyy-MM-dd hh:mi:ss 转换成 yyyyMMddhhmiss
	 */
	public static String changeDateStringAndH(String date) {
		if (date != null && date.indexOf("-") != -1) {
			date = date.replace("-", "");
		}
		if (date != null && date.indexOf(":") != -1) {
			date = date.replace(":", "");
		}
		if (date != null && date.indexOf(" ") != -1) {
			date = date.replace(" ", "");
			date = date.trim();
		}
		
		return date;
	}
	
	
	/**
	 * 获取？个月前的日期
	 * 
	 * 格式 : 20100720
	 * 
	 * @param month
	 *            月数
	 * @return @return 8位长度
	 */
	public static String getMontherDate(int month) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -month);
		return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	}
	
	/**
	 * 获取？天前的日期
	 * 
	 * 格式 : 20180101235959
	 * 
	 * @param date
	 *            天数
	 * @return
	 */
	public static String getPreviousDate(int date) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -date);
		return new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime());
	}
	
	/**
	 * 获取当前时间
	 * 格式： 20180509181001
	 * @param date
	 * @return
	 */
	public static String getNowDate() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	/**
	 * 获取上个月的日期
	 * 2018年5月28日15:30:05
	 * @author liutao
	 * @return 返回上个月日期(例如：201804)
	 */
	public static String getLastMonth(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		return new SimpleDateFormat("yyyyMM").format(cal.getTime());
	}
	
	/**
	 * 获取上个月第一天
	 */
	public static String getLastMonthFirstDay(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	}
	
	/**
	 * 获取上个月第一天
	 */
	public static String getLastMonthFirstDay(String format){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
	
	/**
	 * 获取上个月的最后一天
	 */
	public static String getLastMonthEndDay(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 0);
		return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	}
	
	/**
	 * 获取上个月的最后一天
	 */
	public static String getLastMonthEndDay(String format){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 0);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
	
	/**
	 * 获取昨天
	 */
	public static String getYestoday(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	}
	
	
	public static void main(String[] args) {
		System.out.println(getLastDayOfMonth());
		//setAttr("startime", DateTimeUtil.getLastMonthFirstDay("yyyy-MM-dd"));
		//setAttr("endtime", DateTimeUtil.getLastMonthEndDay("yyyy-MM-dd"));
		System.out.println(DateTimeUtil.getLastMonthFirstDay("yyyy-MM-dd"));
		System.out.println(DateTimeUtil.getLastMonthEndDay("yyyy-MM-dd"));
	}
	

	/**
	 * 获取本季度第一天日期
	 * 
	 * @return
	 */
	public static String getFirstDayOfQuarter() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		if(month>=Calendar.JANUARY&& month<=Calendar.MARCH){
			cal.set(Calendar.MONTH, Calendar.JANUARY);
		}else if(month>=Calendar.APRIL&& month<=Calendar.JUNE){
			cal.set(Calendar.MONTH, Calendar.APRIL);
		}else if(month>=Calendar.JULY&& month<=Calendar.AUGUST){
			cal.set(Calendar.MONTH, Calendar.JULY);
		}else if(month>=Calendar.OCTOBER&& month<=Calendar.DECEMBER){
			cal.set(Calendar.MONTH, Calendar.OCTOBER);
		}
		cal.set(Calendar.DATE, 1);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	

	/**
	 * 获取带斜杠当前日期
	 * 
	 * @param startTime
	 * @return
	 */

	public static String getDqrqStr() {

		String dqrq = "";

		try {
			Calendar calendar = Calendar.getInstance();

			Date Date = calendar.getTime();

			dqrq = df_time.format(Date);
		} catch (Exception e) {
			logger.error("日期转换出现异常:", e);
		}
		return dqrq;
	}
	/**
	 * 获取本年第一天日期
	 * 
	 * @return
	 */
	public static String getFirstDayOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	
	/**
	 * 获取本年最后一天日期
	 * 
	 * @return
	 */
	public static String getLastDayOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	
	/**
	 * 获取本月第一天日期
	 * 
	 * @return
	 */
	public static String getLastDayOfMonth() {
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
		return simpleFormate.format(calendar.getTime()).trim();
	}
	
	/**
	 * 获取本季度最后一天日期
	 * 
	 * @return
	 */
	public static String getLastDayOfQuarter() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		if(month>=Calendar.JANUARY&& month<=Calendar.MARCH){
			cal.set(Calendar.MONTH, Calendar.MARCH);
		}else if(month>=Calendar.APRIL&& month<=Calendar.JUNE){
			cal.set(Calendar.MONTH, Calendar.JUNE);
		}else if(month>=Calendar.JULY&& month<=Calendar.AUGUST){
			cal.set(Calendar.MONTH, Calendar.AUGUST);
		}else if(month>=Calendar.OCTOBER&& month<=Calendar.DECEMBER){
			cal.set(Calendar.MONTH, Calendar.DECEMBER);
		}
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 0);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	
	/**
	 *日期计算
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static String getAddDay(String start_time,int days) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new SimpleDateFormat("yyyyMMdd").parse(start_time));
		cal.add(Calendar.DATE, days);
		return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	}
	
	/**
	 * 获取当前日期是周第几天
	 * @return
	 */
	public static int getDayOfWeekDay(String default_date) {
		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		if(AppUtils.StringUtil(default_date) == null){
			
		}else{
			try {
				date = sdf1.parse(default_date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				date = new Date();
			}
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int w = calendar.get(Calendar.DAY_OF_WEEK) -1;
		if(w<0) w=0;
		return w;
	}
	
	/**
	 * 获取当前日期是星期几
	 * @return
	 */
	public static String getDayOfWeek() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);
		return sdf.format(date);
	}
	
	
	public static String getDayOfWeek(String default_date){
		Date date = new Date();
		String week = "";
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		
		
		if(AppUtils.StringUtil(default_date) == null){
			week = sdf.format(date);
		}else{
			try {
				date = sdf1.parse(default_date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				date = new Date();
			}
			week = sdf.format(date);
		}
		return week;
	}
	
	
	/**
	 * 获取当前日期是几号
	 */
	public static String getDayOfMonthYmd() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dStr = sdf.format(date);
		String[] ds = dStr.split("-");
		
		return ds[ds.length-1];
	}
	
	public static String getDayOfMonth(String default_date) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		String dStr = sdf.format(date);
		
		if(AppUtils.StringUtil(default_date) == null){
			dStr = sdf.format(date);
		}else{
			try {
				date = sdf1.parse(default_date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				date = new Date();
			}
			dStr = sdf.format(date);
		}
		
		
		
		String[] ds = dStr.split("-");
		
		return ds[ds.length-1];
	}
	
	/**
	 * 获取当前日期是几月份
	 */
	public static String getMonthOfYear() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dStr = sdf.format(date);
		String[] ds = dStr.split("-");
		
		return ds[1];
	}
	
	
	public static String getMonthOfYear(String default_date) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		if(AppUtils.StringUtil(default_date) == null){
			
		}else{
			try {
				date = sdf1.parse(default_date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				date = new Date();
			}
		}
		
		String dStr = sdf.format(date);
		String[] ds = dStr.split("-");
		
		return ds[1];
	}
	
	/**
	 * 获取当前日期是第几季度
	 * @throws ParseException 
	 */
	public static String getQuarterOfYear() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dStr = sdf.format(date);
		String[] ds = dStr.split("-");
		
		if(ds[1].equals("01") || ds[1].equals("02") || ds[1].equals("03")) {
			return "1";
		}
		else if(ds[1].equals("04") || ds[1].equals("05") || ds[1].equals("06")) {
			return "2";
		}
		else if(ds[1].equals("07") || ds[1].equals("08") || ds[1].equals("09")) {
			return "3";
		}
		else if(ds[1].equals("10") || ds[1].equals("11") || ds[1].equals("12")) {
			return "4";
		}
		
		return "";
	}
	
	/**
	 * 获取当前日期是季度的第多少天
	 * @param cURRENT_DAY
	 * @return
	 */
	public static int getIQuarterOfYear(String default_date) {
		Date date = new Date();
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		//String dStr = sdf.format(date);
		
		if(AppUtils.StringUtil(default_date) == null){
			//dStr = sdf.format(date);
		}else{
			try {
				date = sdf1.parse(default_date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				date = new Date();
			}
			//dStr = sdf.format(date);
		}
		Calendar now = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		now.setTime(date);
		start.setTime(date);
		int month = now.get(Calendar.MONTH)+1;
		int pm = (month-1)%3;
		start.add(Calendar.MONTH, 0-pm);
		start.set(Calendar.DATE, 1);
		int days = now.get(Calendar.DAY_OF_YEAR)-start.get(Calendar.DAY_OF_YEAR);
		/*String[] ds = dStr.split("-");
		String month = ds[ds.length-2];
		int m = Integer.valueOf(month);*/
		return days;
	}
	
	/**
	 * 每半月天数
	 * @param default_date
	 * @return
	 */
	public static int getDayOfHalfMonth(String default_date) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		String dStr = sdf.format(date);
		
		if(AppUtils.StringUtil(default_date) == null){
			dStr = sdf.format(date);
		}else{
			try {
				date = sdf1.parse(default_date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				date = new Date();
			}
			dStr = sdf.format(date);
		}
		
		
		
		String[] ds = dStr.split("-");
		String day = ds[ds.length-1];
		int d = Integer.valueOf(day);
		if(d>14){
			d = d - 14;
		}
		return d;
	}
	
}
