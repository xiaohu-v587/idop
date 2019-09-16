package com.goodcol.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
/**
 * 配置文件log4j的配置信息
 * @author Administrator
 *
 */
public class Log4jPropertiesContent {
	
	public static Map<String, Object> config = null;
	public static Properties properties = new Properties();
	public static void init()
	{
		config = new HashMap<String, Object>();
		ResourceBundle rb = ResourceBundle.getBundle("log4j");
		Enumeration<String> cfgs = rb.getKeys();
		while (cfgs.hasMoreElements()) {
			String key = cfgs.nextElement();
			String val = rb.getString(key);
			config.put(key, val);
		}
		properties.putAll(config);
	}

	public static String get(String key) {
		if( config == null )
			init();
		return (String) config.get(key);
	}

	public static Object getObj(String key) {
		if( config == null )
			init();
		return config.get(key);
	}

	public static Boolean getToBool(String key, Boolean def) {
		if( config == null )
			init();
		try {
			return Boolean.valueOf((String) config.get(key));
		} catch (Exception e) {
			return def;
		}
	}

	public static Integer getToInteger(String key, Integer def) {
		if( config == null )
			init();
		try {
			return Integer.valueOf((String) config.get(key));
		} catch (Exception e) {
			return def;
		}
	}

	public static Long getToLong(String key, Long def) {
		if( config == null )
			init();
		try {
			return Long.valueOf((String) config.get(key));
		} catch (Exception e) {
			return def;
		}
	}
}
