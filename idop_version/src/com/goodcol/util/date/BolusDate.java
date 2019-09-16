package com.goodcol.util.date;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
 
/**
 * <p>日期处理类。</p>
 *
 * <br>使用说明:
 * <br>本来主要提供各种日期相关操作。提供的方法都是静态static,方便使用。
 * <br>主要提供获取当前日期和当前时间，日期时间格式转换等。
 * <br>
 * <br>修改记录
 * <br>
 * <br>
 *
 */
public class BolusDate {
  /**
   * 日期格式化。
   * <br>10位yyyy/MM/dd 转换为8位yyyyMMdd
   * @param date - 要格式化的日期字符串: 10位 yyyy/MM/dd或yyyy-MM-dd
   * @return String - 返回格式化后的日期
   * <br>若date长度不为10，即格式不为yyyy/MM/dd形式的日期，则直接返回date。
   * <br>若date为null, 则返回""
   */
  public static String dateTo8(String date) {
    if (date == null)
      return "";
    if (date.trim().length() != 10) {
      return date;
    }
    return date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
  }

  /**
   * 日期格式化。
   * <br>8位yyyyMMdd 转换为10位yyyy/MM/dd
   * @param date - 要格式化的日期字符串: 8位 yyyyMMdd
   * @return String - 返回格式化后的日期
   * <br>若date长度不为8，即格式不为yyyyMMdd形式的日期，则直接返回date。
   * <br>若date为null, 则返回""
   */
  public static String dateTo10(String date) {
  	return dateTo10(date,"/");
  }
  /**
   * 日期格式化。
   * <br>8位yyyyMMdd 转换为10位yyyy/MM/dd
   * @param date - 要格式化的日期字符串: 8位 yyyyMMdd
   * @return String - 返回格式化后的日期
   * <br>若date长度不为8，即格式不为yyyyMMdd形式的日期，则直接返回date。
   * <br>若date为null, 则返回""
   */
  public static String dateTo10(String date,String symbol) {
    if (date == null)
      return "";
    if (date.trim().length() != 8)
      return "";
    return date.substring(0, 4) + symbol + date.substring(4, 6) + symbol +
        date.substring(6, 8);
  }
  /**
   * 日期格式化。
   * <br>8位yyyyMMdd 转换为10位yyyy/MM/dd
   * <br>yyyy、MM和dd之间的分隔字符自定义(flag)
   * @param date - 要格式化的日期字符串: 8位 yyyyMMdd
   * @param flag - yyyy、MM和dd之间的分隔字符
   * @return String - 返回格式化后的日期
   * <br>若date长度不为8，即格式不为yyyyMMdd形式的日期，则直接返回date。
   * <br>若date为null, 则返回""
   */
  public static String dateTo10(String date, char flag) {
    if (date == null)
      return "";
    if (date.trim().length() != 8)
      return "";
    return date.substring(0, 4) + flag + date.substring(4, 6) + flag +
        date.substring(6, 8);
  }

  /**
   * 时间格式化。
   * <br>8位(HH:mm:ss)或7位(H:mm:ss)的时间转换为6位(HHmmss)或5位(Hmmss)
   * <br>时间的分隔字符可以是任意字符，一般为冒号(:)
   * @param time - 要格式化的时间字符串:8位(HH:mm:ss)或7位(H:mm:ss)
   * @return String - 返回格式化后的时间
   * <br>若time长度不为8或7，即格式不为8位(HH:mm:ss)或7位(H:mm:ss)形式的时间，则直接返回date。
   * <br>若time为null, 则返回""
   */
  public static String timeTo6(String time) {
  	if(time == null)
  		return "";
    int len = time.length();
    if (len < 7 || len > 8)
      return "";
    return time.substring(0, len - 6) + time.substring(len - 5, len - 3) +
        time.substring(len - 2, len);
  }

  /**
   * 时间格式化。
   * <br>6位(HHmmss)或5位(Hmmss)的时间转换为8位(HH:mm:ss)或7位(H:mm:ss)
   * @param time - 要格式化的时间字符串: 6位(HHmmss)或5位(Hmmss)
   * @return String - 返回格式化后的时间
   * <br>若time长度不为5或6，即格式不为6位(HHmmss)或5位(Hmmss)形式的时间，则直接返回date。
   * <br>若time为null, 则返回""
   */
  public static String timeTo8(String time) {
  	if(time == null)
  		return "";
    int len = time.length();
    if (len < 5 || len > 6)
      return "";
    return time.substring(0, len - 4) + ":" + time.substring(len - 4, len - 2) +
        ":" + time.substring(len - 2, len);
  }

  /**
   * 时间格式化。
   * <br>6位(HHmmss)或5位(Hmmss)的时间转换为8位(HH:mm:ss)或7位(H:mm:ss)
   * <br>时间的分隔字符是自定义的字符(flag)
   * @param time - 要格式化的时间字符串: 6位(HHmmss)或5位(Hmmss)
   * @param flag - 时间的分隔字符
   * @return String - 返回格式化后的时间
   * <br>若time长度不为5或6，即格式不为6位(HHmmss)或5位(Hmmss)形式的时间，则直接返回date。
   * <br>若time为null, 则返回""
   */
  public static String timeTo8(String time, char flag) {
  	if(time == null)
  		return "";
    int len = time.length();
    if (len < 5 || len > 6)
      return "";
    return time.substring(0, len - 4) + flag + time.substring(len - 4, len - 2) +
        flag + time.substring(len - 2, len);
  }

  /**
   * 获取当前日期或时间。
   * <br>根据不同的格式化字符串获取不同格式的日期或时间
   * <br>具体用法参见类java.text.SimpleDateFormat
   * <br>例如: format="yyyy" 则为获取当前年份
   * <br>      format="MM"   则为获取当前月份
   * <br>      format="dd"   则为获取当前日期号day
   * <br>      ...
   * @param format - 指定获取当前日期或时间格式的字符串
   * @return String - 返回指定格式的日期或时间字符串
   * <br>若格式字符串非法，则返回"";
   */
  public static String getDateTime(String format) {
    SimpleDateFormat dataFormat = null;
    try {
      dataFormat = new SimpleDateFormat(format);
    }
    catch (Exception e) {
      return "";
    }
    Date date = new Date();
    String dateString = dataFormat.format(date);
    return dateString;
  }

  /**
   * 获取当前日期。
   * <br>获取的日期格式为yyyyMMdd
   * @return String - 返回当前日期
   */
  public static String getDate() {
    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
    Date date = new Date();
    String dateString = dataFormat.format(date);
    return dateString;
  }

  /**
   * 获取当前是星期几。
   * <br>0为星期天、1为星期一、以此类推。
   * @return String - 返回当前星期几
   */
  public static int getWeek() {
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int posOfWeek = cal.get(Calendar.DAY_OF_WEEK);

    posOfWeek--; //Calendar格式转化成普通格式  0星期天， 1 星期一...
    return posOfWeek;
  }

  /**
   * 获取当前时间。
   * <br>获取的时间的格式为6位(HHmmss)或5位(Hmmss)
   * @return String - 返回当前时间
   */
  public static String getTime() {
    SimpleDateFormat dataFormat = new SimpleDateFormat("HHmmss");
    Date date = new Date();
    String timeString = dataFormat.format(date);
    return timeString;
  }

  /**
   * 月份相加运算。
   * <br>指定日期(yyyyMM) 加上月份数(月份数正负都可以)，计算出新的日期(yyyyMM)
   * <br>例如  指定日期为200307, 加上月数为-8, 则计算得到日期为 200211
   * @param date - 指定日期,格式为yyyyMM
   * @param months - 指定相加月数
   * @return String - 返回计算后的日期
   */
  public static String addMonth(String date, int months) {
    if (date.length() != 6)
      return "";
    int year = Integer.parseInt(date.substring(0, 4));
    int month = Integer.parseInt(date.substring(4, 6));
    month += months;
    while (month <= 0) {
      year--;
      month += 12;
    }
    while (month > 12) {
      year++;
      month -= 12;
    }
    String ret = "" + year;
    if (month >= 10)
      ret += month;
    else
      ret += "0" + month;
    return ret;
  }

  /**
   * 日期相加运。
   * <br>指定日期(yyyyMMdd) 加上天数(天数正负都可以)，计算出新的日期(yyyyMMdd)
   * <br>例如  指定日期为20030702, 加上月数为-3, 则计算得到日期为 20030629
   * @param date - 指定日期,格式为yyyyMMdd
   * @param days - 指定相加天数
   * @return String - 返回计算后的日期
   */
  public static String addDate(String date, int days) {
    if (Long.parseLong(date) < 19010101)
      return date;
    String str = date;
    int year = Integer.parseInt(str.substring(0, 4));
    int month = Integer.parseInt(str.substring(4, 6));
    int day = Integer.parseInt(str.substring(6, 8));
    GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
    calendar.add(Calendar.DATE, days);
    long newDate = calendar.get(Calendar.YEAR) * 10000 +
        (calendar.get(Calendar.MONTH) + 1) * 100 +
        calendar.get(Calendar.DAY_OF_MONTH);
    return "" + newDate;
  }

  /**
   * 时间相加运算。
   * <br>指定时间(HHmmss或Hmmss) 加上秒数(秒数正负都可以)，计算出新的时间
   * <br>例如  指定时间为203001, 加上秒数为-60, 则计算得到日期为 202901
   * @param time - 指定时间,格式为HHmmss或Hmmss
   * @param seconds - 指定相加秒数
   * @return String - 返回计算后的时间
   */
  public static String addTime(String time, int seconds) {
    int len = time.length();
    if (len < 5 || len > 6)
      return "";
    int hh = Integer.parseInt(time.substring(0, len - 4));
    int mm = Integer.parseInt(time.substring(len - 4, len - 2));
    int ss = Integer.parseInt(time.substring(len - 2, len));

    int s = seconds % 60;
    int m = seconds / 60;

    mm += m;
    ss += s;

    //处理秒
    if (ss < 0) {
      mm--;
      ss += 60;
    }
    if (ss >= 60) {
      mm++;
      ss -= 60;
    }

    //处理分
    hh += mm / 60;
    mm = mm % 60;
    if (mm >= 60) {
      hh++;
      mm -= 60;
    }
    if (mm < 0) {
      hh--;
      mm += 60;
    }

    //处理小时
    hh = hh % 24;
    if (hh < 0) {
      hh += 24;
    }

    //组返回字符串
    String newTime = "" + hh;
    if (mm < 10)
      newTime += "0" + mm;
    else
      newTime += mm;
    if (ss < 10)
      newTime += "0" + ss;
    else
      newTime += ss;
    return newTime;
  }

  /**
   * 计算两个日期相隔天数。
   * <br>计算结果统一成正数
   * @param date1 - 指定日期(yyyyMMdd)的其中一个
   * @param date2 - 指定日期(yyyyMMdd)的另外一个
   * @return int - 返回计算后的天数  失败返回-1
   */
  public static int diffDate(String date1, String date2) {
    try {

      SimpleDateFormat simpledateformat = new SimpleDateFormat(
          "yyyyMMdd");
      Date d1 = simpledateformat.parse(date1);
      Date d2 = simpledateformat.parse(date2);

     long tmp=d1.getTime()-d2.getTime();
      if(tmp<0)
        tmp=-tmp;

      int diff=(int)(tmp/(24*60*60*1000));

      return diff;
    }catch (Exception e) {
     // e.printStackTrace();
      return -1;
    }

  }
  
  	/**
   	 * 计算两个日期时间相隔秒数。
   	 * <br>第一个减去第二个，允许返回负数
   	 * @param date1 - 指定日期(yyyyMMddhhmmss)的其中一个
   	 * @param date2 - 指定日期(yyyyMMddhhmmss)的另外一个
   	 * @return int - 返回计算后的天数  失败返回-1
   	 */
  	public static long diffDateTime(String date1, String date2) {
    	try {
			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddhhmmss");
      		Date d1 = simpledateformat.parse(date1);
      		Date d2 = simpledateformat.parse(date2);

     		long diff = d1.getTime() - d2.getTime();
      		return diff;
    	} catch(Exception e) {
    		System.out.println( "时间格式错误");
    		return 0;
    	}
  	}
  

  /**
   * 计算两个日期相隔月数。
   * <br>计算结果统一成正数
   * <br>未实现   ---不好描述，看实际情况是否需要
   * @param date1 - 指定日期(yyyyMMdd)的其中一个
   * @param date2 - 指定日期(yyyyMMdd)的另外一个
   * @return int - 返回计算后的月数
   */
/*  public static int diffMonth(String date1, String date2) {

    return 0;
  }*/

  /**
   * 获取一个月的最后一天。
   * <br>例如20030111,即一月份的最后一天是20030131
   * @param date - 指定日期(yyyyMMdd)
   * @return String - 返回计算后的日期
   */
  public static String getLastDayOfMonth(String date) {
    if (date.length() != 8)
      return "";
    int year = Integer.parseInt(date.substring(0, 4));
    int month = Integer.parseInt(date.substring(4, 6));
    int day = 0;
    if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 ||
        month == 10 || month == 12) {
      day = 31;
    }
    else if (month == 2) {
      if ( ( (year % 4) == 0) && ( (year % 100) != 0)) {
        day = 29;
      }
      else {
        day = 28;
      }
    }
    else {
      day = 30;
    }

    String newDate = "" + year;
    if (month < 10) {
      newDate += "0" + month + day;
    }
    else {
      newDate += "" + month + day;
    }
    return newDate;
  }

  /****************************时间日历操作**************************************/

  /**
   *  根据输入长度格式化时间
   *  @param DateString 时间字符串
   *  @return 格式化时间串
   */

  public static String FormatDate(String DateString) {
    String ReturnDate = "";
    if (DateString != null) {
      DateString = DateString.trim();
      int len = DateString.length();
      switch (len) {
        case 4:
          ReturnDate = DateString;
          break;
        case 6:
          ReturnDate = DateString.substring(0, 2) + ":" +
              DateString.substring(2, 4)
              + ":" + DateString.substring(4, 6);
          break;
        case 8:
          ReturnDate = DateString.substring(0, 4) + "/" +
              DateString.substring(4, 6)
              + "/" + DateString.substring(6, 8);
          break;
        case 14:
          ReturnDate = DateString.substring(0, 4) + "/" +
              DateString.substring(4, 6)
              + "/" + DateString.substring(6, 8) + " " +
              DateString.substring(8, 10)
              + ":" + DateString.substring(10, 12) + ":" +
              DateString.substring(12, 14);
          break;
        case 15:
          ReturnDate = DateString.substring(0, 4) + "/" +
              DateString.substring(4, 6)
              + "/" + DateString.substring(6, 8) + " " +
              DateString.substring(9, 11)
              + ":" + DateString.substring(11, 13) + ":" +
              DateString.substring(13, 15);
          break;
        default:
          ReturnDate = "日期格式有误";
          break;
      }
    }
    else {
      ReturnDate = "日期为空!";
    }
    return ReturnDate;
  }

  /**
   *    返回日历的年字符串
   *   @param cal 日历
   *    @return 日历的年字符串
   */
  public static String getYear(Calendar cal) {
    return String.valueOf(cal.get(Calendar.YEAR));
  }

  /**
   *    返回日历的月字符串(两位)
   *   @param cal 日历
   *    @return 返回日历的月字符串(两位)
   */
  public static String getMonth(Calendar cal) {
    return BolusData.fix0BeforeString(String.valueOf(cal.get(Calendar.MONTH) + 1),
                                       2);
  }

  /**
   *    返回日历的日字符串(两位)
   *   @param cal 日历
   *    @return 返回日历的日字符串(两位)
   */
  public static String getDay(Calendar cal) {
    return BolusData.fix0BeforeString(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)),
                                       2);
  }

  /**
   *    返回日历的时字符串(两位)
   *   @param cal 日历
   *    @return 返回日历的时字符串(两位)
   */
  public static String getHour(Calendar cal) {
    return BolusData.fix0BeforeString(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)),
                                       2);
  }

  /**
   *    返回日历的分字符串(两位)
   *   @param cal 日历
   *    @return 返回日历的分字符串(两位)
   */
  public static String getMinute(Calendar cal) {
    return BolusData.fix0BeforeString(String.valueOf(cal.get(Calendar.MINUTE)), 2);
  }

  /**
   *    返回日历的秒字符串(两位)
   *   @param cal 日历
   *    @return 返回日历的秒字符串(两位)
   */
  public static String getSecond(Calendar cal) {
    return BolusData.fix0BeforeString(String.valueOf(cal.get(Calendar.SECOND)), 2);
  }

  /**
   *    返回日历的日期字符串（格式："yyyy-mm-dd"）
   *   @param cal 日历
   *    @return 返回日历的日期字符串（格式："yyyy-mm-dd"）
   */
  public static String getDateStr(Calendar cal) {
    return getYear(cal) + "-" + getMonth(cal) + "-" + getDay(cal);
  }

  /**
   *    返回日历的日期字符串（格式："yyyymmdd"）
   *   @param cal 日历
   *    @return 返回日历的日期字符串（格式："yyyymmdd"）
   */
  public static String getNumDateStr(Calendar cal) {
    return getYear(cal) + getMonth(cal) + getDay(cal);
  }

  /**
   *    返回日历的时间字符串（格式："hh:ss:mm"）
   *   @param cal 日历
   *    @return 返回日历的时间字符串（格式："hh:ss:mm"）
   */
  public static String getTimeStr(Calendar cal) {
    return getHour(cal) + ":" + getMinute(cal) + ":" + getSecond(cal);
  }

  /**
   *    返回日历的时间字符串（格式："hhssmm"）
   *   @param cal 日历
   *    @return 返回日历的时间字符串（格式："hhssmm"）
   */
  public static String getNumTimeStr(Calendar cal) {
    return getHour(cal) + getMinute(cal) + getSecond(cal);
  }

  /**
   *    返回日历的日期时间字符串（格式："yyyy-mm-dd hh:ss:mm"）
   *   @param  cal 日历
   *    @return 返回日历的日期时间字符串（格式："yyyy-mm-dd hh:ss:mm"）
   */
  public static String getDate(Calendar cal) {
    return getDateStr(cal) + " " + getTimeStr(cal);
  }

  /**
   *    返回日历的日期时间字符串（格式："yyyymmdd hhssmm"）
   *   @param cal 日历
   *    @return 返回日历的日期时间字符串（格式："yyyymmdd hhssmm"）
   */
  public static String getNumDate(Calendar cal) {
    return getNumDateStr(cal) + " " + getNumTimeStr(cal);
  }

  /**
   *    取当前日期时间的字符串，格式为"yyyy-mm-dd hh:ss:mm"
   *    @return 返回当前日期时间的字符串，格式为"yyyy-mm-dd hh:ss:mm"
   */
  public static String getNow() {
    Calendar now = Calendar.getInstance();
    return getDateStr(now) + " " + getTimeStr(now);
  }

  /**
   *    取当前日期时间的字符串，格式为"yyyymmdd hhssmm"
   *    @return 返回当前日期时间的字符串，格式为"yyyymmdd hhssmm"
   */
  public static String getNumNow() {
    Calendar now = Calendar.getInstance();
    return getNumDateStr(now) + " " + getNumTimeStr(now);
  }

  /**
   *    取当前日期的字符串，格式为"yyyy-mm-dd"
   *    @return 返回当前日期的字符串，格式为"yyyy-mm-dd"
   */
  public static String getNowDate() {
    Calendar now = Calendar.getInstance();
    return getDateStr(now);
  }

  /**
   *    取当前日期的字符串，格式为"yyyymmdd"
   *    @return 返回当前日期的字符串，格式为"yyyymmdd"
   */
  public static String getNumNowDate() {
    Calendar now = Calendar.getInstance();
    return getNumDateStr(now);
  }

  /**
   *    取当前时间的字符串，格式为"hh:ss:mm"
   *    @return 返回当前时间的字符串，格式为"hh:ss:mm"
   */
  public static String getNowTime() {
    Calendar now = Calendar.getInstance();
    return getTimeStr(now);
  }

  /**
   *    取当前时间的字符串，格式为"hhssmm"
   *    @return 返回当前时间的字符串，格式为"hhssmm"
   */
  public static String getNumNowTime() {
    Calendar now = Calendar.getInstance();
    return getNumTimeStr(now);
  }

  /**
   * 比较两个日期前后
   * @param todaystring String   时间串1
   * @param compareString String  时间串2
   * @return 时间1  早于 时间2  返回true
   */
  public static boolean beforeDate(String todaystring, String compareString) {
    try {
      SimpleDateFormat simpledateformat = new SimpleDateFormat(
          "yyyyMMdd hhmmss");
      Date d1 = simpledateformat.parse(todaystring);
      Date d2 = simpledateformat.parse(compareString);
      return d1.before(d2);
    }
    catch (Exception e) {

      return false;
    }
  }

  /**
   * 比较两个日期前后
   * @param todaystring String   时间串1
   * @param compareString String  时间串2
   * @return 时间1  晚于 时间2  返回true
   */

  public static boolean afterDate(String todaystring, String compareString) {
    try {
      SimpleDateFormat simpledateformat = new SimpleDateFormat(
          "yyyyMMdd hhmmss");
      Date d1 = simpledateformat.parse(todaystring);
      Date d2 = simpledateformat.parse(compareString);
      return d1.after(d2);
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * 判断时间是否在后两个时间之间
   * @param todaystring String 时间串1
   * @param startdate String 时间串2
   * @param enddate String 时间串3
   * @return boolean
   */
  public static boolean betweenDate(String todaystring, String startdate,
                                    String enddate) {
    return (beforeDate(todaystring, enddate) &&
            afterDate(todaystring, startdate));
  }

  
  /**
   * 取得8位日期+6位时间的相对1970年01月01日0点的毫秒数
   * @param date86
   * @return 相对1970年01月01日0点的毫秒数
   */
  public static  long getTime(String date86){
  	try {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(
            "yyyyMMddhhmmss");
        Date d1 = simpledateformat.parse(date86);
       
        return d1.getTime();
      }
      catch (Exception e) {
        return 0;
      }
  
  }
  /**
   * 取得8位日期+6位时间的相对1970年01月01日0点的毫秒数
   * @param date8
   * @param time6
   * @return 相对1970年01月01日0点的毫秒数
   */
  public static  long getTime(String date8,String time6){
  	return getTime(date8+time6);
  
  }
  
}
