package com.goodcol.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import com.goodcol.util.safe.CipherUtil;


/**
 * 配置文件config.properties所有配置
 *  
 */
public class PropertiesContent {

	public static Map<String, Object> config = null;
	public static Properties properties = new Properties();
	public static void init()
	{
		config = new HashMap<String, Object>();
		ResourceBundle rb = ResourceBundle.getBundle("config");
		Enumeration<String> cfgs = rb.getKeys();
		while (cfgs.hasMoreElements()) {
			String key = cfgs.nextElement();
			String val = rb.getString(key);
			if (key.contains("mail.password") == true) {
				try {
					config.put(key, CipherUtil.decryptData(val));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				config.put(key, val);
			}
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
