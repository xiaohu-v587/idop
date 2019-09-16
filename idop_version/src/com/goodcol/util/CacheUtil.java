package com.goodcol.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 缓存工具
 */
public class CacheUtil {
	
	/**
	 * 存储缓存数据
	 * @param key
	 * @param object
	 * @param seconds
	 * @param request
	 */
	public static void setCache(String key, Object object,int seconds,HttpServletRequest request) {

		if(RedisUtil.isRedisOpen()){
			RedisUtil.setObject(key, object, seconds);
		}else{
			request.getSession().setAttribute(key, object);
		}
	}
	
	/**
	 * 读取缓存数据
	 * @param key
	 * @param request
	 * @return
	 */
	public static Object getCache(String key,HttpServletRequest request){
		Object obj = null;
		
		if(RedisUtil.isRedisOpen()){
			obj = RedisUtil.getObject(key);
		}else{
			obj = request.getSession().getAttribute(key);
		}		
		return obj;
	}
	
	/**
	 * 删除缓存数据
	 * @param key
	 * @param request
	 */
	public static void delCache(String key,HttpServletRequest request){
		if(RedisUtil.isRedisOpen()){
			RedisUtil.delObject(key);
		}else{
			request.getSession().removeAttribute(key);
		}
	}
	
	/**
	 * 存储缓存数据
	 * @param key
	 * @param object
	 * @param seconds
	 * @param session
	 */
	public static void setCache(String key, Object object,int seconds,HttpSession session) {

		if(RedisUtil.isRedisOpen()){
			RedisUtil.setObject(key, object, seconds);
		}else{
			session.setAttribute(key, object);
		}
	}
	
	/**
	 * 读取缓存数据
	 * @param key
	 * @param session
	 * @return
	 */
	public static Object getCache(String key,HttpSession session){
		Object obj = null;
		if(RedisUtil.isRedisOpen()){
			obj = RedisUtil.getObject(key);
		}else{
			obj = session.getAttribute(key);
		}		
		return obj;
	}
	
	/**
	 * 删除缓存数据
	 * @param key
	 * @param session
	 */
	public static void delCache(String key,HttpSession session){
		if(RedisUtil.isRedisOpen()){
			RedisUtil.delObject(key);
		}else{
			session.removeAttribute(key);
		}
	}

}
