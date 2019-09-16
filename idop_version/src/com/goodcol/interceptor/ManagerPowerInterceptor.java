package com.goodcol.interceptor;

import com.goodcol.core.aop.Interceptor;
import com.goodcol.core.core.ActionInvocation;
import com.goodcol.core.core.Controller;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.CacheUtil;

/**
 * 管理用户后台登录状态及权限验证拦截器
 */
public class ManagerPowerInterceptor implements Interceptor {
	
	public void intercept(ActionInvocation ai) {
		Controller ctrl = ai.getController();
		String user_token = ctrl.getCookie("user_token");

		Record po = (Record) CacheUtil.getCache(user_token, ctrl.getRequest());
		String acceptStr = ctrl.getRequest().getHeader("Accept");
		if (po == null) {
			if ( acceptStr.indexOf("html") > 0 || acceptStr.indexOf("xml") > 0 || acceptStr.indexOf("application") > 0 || acceptStr.indexOf("image") > 0) {
				ctrl.render("../sessionTimeout.jsp");
			} else {
				ctrl.setAttr("code", "9999");
				ctrl.setAttr("isnosession", true);
				ctrl.setAttr("desc", "登录超时,请重新登录");
				ctrl.renderJson();
			}
		} else {
			ai.invoke();// 注意 一定要执行此方法
		}
	}
}