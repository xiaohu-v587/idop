 package com.goodcol.util;

/**
 * json格式化
 * 
 * @author Liuhukang
 * 
 */
public class StringFormat {

	/** json关键字 */
	public static String[] keys = new String[] { "[", "]", "{", "}", "'", "\"", ":", "," };

	/** 需要删除的关键字 */
	public static String[] dels = new String[] { "\\", "\b", "\t", "\n", "\f", "\r" };

	public static String format(String str) {
		for (String del : dels) {
			str = str.replace(del, "");
		}
		for (String key : keys) {
			str = str.replace(key, "\\" + key);
		}
		return str;
	}

	public static void main(String[] args) {
		String aa = "[亲爱的]用\\户{0}您好，您有\n一封新邮件{0}，请注意查收！";
		System.out.println(aa);
		System.out.println(StringFormat.format(aa));
	}

}
