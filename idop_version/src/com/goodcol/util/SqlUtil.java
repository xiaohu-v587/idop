package com.goodcol.util;

import java.lang.reflect.Method;
import com.goodcol.util.ext.anatation.SqlBind;

/***
 * Sqlbind解析工具类
 * 
 * @author Administrator
 * 
 */
public class SqlUtil {
	/**
	 * 获取出方法上的注解
	 * @param cl 当前类，getClass()
	 * @param methodName 当前方法名
	 * @param joinSql 对于注解补充的sql
	 * @return
	 */
	public static String getSql(Class<?> cl, String methodName,
			String... joinSql) {
		// 可以获取出私有方法和共有方法
		Method[] methods = cl.getDeclaredMethods();
		Method meth = null;
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				meth = m;
				break;
			}
		}
		SqlBind sqlb = (SqlBind) meth.getAnnotations()[0];
		return sqlb.value() + joinSql;
	}
}
