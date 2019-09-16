
package com.goodcol.ext.kit;

import java.util.Date;
import com.goodcol.kit.StrKit;

/**
 * DateKit.
 */
public class DateKit {
	
	public static String dateFormat = "yyyy-MM-dd";
	public static String timeFormat = "yyyy-MM-dd HH:mm:ss";
	
	public static void setDateFromat(String dateFormat) {
		if (StrKit.isBlank(dateFormat)) {
			throw new IllegalArgumentException("dateFormat can not be blank.");
		}
		DateKit.dateFormat = dateFormat;
	}
	
	public static void setTimeFromat(String timeFormat) {
		if (StrKit.isBlank(timeFormat)) {
			throw new IllegalArgumentException("timeFormat can not be blank.");
		}
		DateKit.timeFormat = timeFormat;
	}
	
	public static Date toDate(String dateStr) {
		throw new RuntimeException("Not finish!!!");
	}
	
	public static String toStr(Date date) {
		return toStr(date, DateKit.dateFormat);
	}
	
	public static String toStr(Date date, String format) {
		throw new RuntimeException("Not finish!!!");
	}
}
