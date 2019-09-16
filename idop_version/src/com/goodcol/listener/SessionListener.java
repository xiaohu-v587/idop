package com.goodcol.listener;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.goodcol.util.CacheUtil;
import com.goodcol.util.Dict;

/**
 * sessin监听器，统计在线客户端数及ip数量，以及处理失效后的memcache内容管理
 * 
 * @author lubin
 */
public class SessionListener implements HttpSessionListener, ServletRequestListener, HttpSessionActivationListener {
	/** 客户端sessionid集合,多个浏览器也是可以同一终端上运行 */
	public static final String clientSetkey = "clientSet";
	/** 终端ip集合 */
	public static final String teSetkey = "teSet";
	public static final Long timelong = 43200000L;
	HttpServletRequest request;

	@SuppressWarnings("unchecked")
	@Override
	public void sessionCreated(HttpSessionEvent ev) {
		String sessionid = ev.getSession().getId();
		String ip = request.getRemoteAddr();
		//根据字典项的值，判断是否启用redis缓存
		Set<String> clientSet = (Set<String>) CacheUtil.getCache(clientSetkey, ev.getSession());
		if (clientSet == null) {
			clientSet = new HashSet<String>();
			ev.getSession().setAttribute(clientSetkey, clientSet);
			CacheUtil.setCache(clientSetkey, clientSet, Dict.SECONDS, ev.getSession());
		}
		clientSet.add(sessionid);
		CacheUtil.setCache(clientSetkey, clientSet, Dict.SECONDS, ev.getSession());

		Set<String> teSet = (Set<String>) CacheUtil.getCache(teSetkey, ev.getSession());

		if (teSet == null) {
			teSet = new HashSet<String>();
			CacheUtil.setCache(teSetkey, teSet, Dict.SECONDS, ev.getSession());
		}
		teSet.add(ip);
		CacheUtil.setCache(teSetkey, teSet, Dict.SECONDS, ev.getSession());

	}

	@SuppressWarnings("unchecked")
	@Override
	public void sessionDestroyed(HttpSessionEvent ev) {
		String sessionid = ev.getSession().getId();
		String ip = request.getRemoteAddr();

		Set<String> clientSet = (Set<String>) CacheUtil.getCache(clientSetkey, ev.getSession());
		if (clientSet != null && clientSet.isEmpty() == false) {
			clientSet.remove(sessionid);
			CacheUtil.setCache(clientSetkey, clientSet, Dict.SECONDS, ev.getSession());
		}
		Set<String> teSet = (Set<String>) CacheUtil.getCache(teSetkey, ev.getSession());
		if (teSet != null && teSet.isEmpty() == false) {
			teSet.remove(ip);
			CacheUtil.setCache(teSetkey, teSet, Dict.SECONDS, ev.getSession());
		}
		CacheUtil.delCache(sessionid, ev.getSession());
		
		//MemcacheTool.mcc.delete(sessionid);
		//MemcacheTool.mcc.delete("menu" + sessionid);
		//MemcacheTool.mcc.delete("powersafecodelist" + sessionid);
	}

	@Override
	public void sessionDidActivate(HttpSessionEvent ev) {

	}

	@Override
	public void sessionWillPassivate(HttpSessionEvent ev) {

	}

	@Override
	public void requestDestroyed(ServletRequestEvent ev) {

	}

	@Override
	public void requestInitialized(ServletRequestEvent ev) {
		request = (HttpServletRequest) ev.getServletRequest();
	}

}
