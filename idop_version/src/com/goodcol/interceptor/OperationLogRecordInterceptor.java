package com.goodcol.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.goodcol.core.aop.Interceptor;
import com.goodcol.core.core.ActionInvocation;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.CacheUtil;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.RedisUtil;
import com.goodcol.util.ext.anatation.LogBind;

public class OperationLogRecordInterceptor implements Interceptor {
	/***
	 * 记录操作日志
	 */
	@Override
	public void intercept(ActionInvocation ai) {
		// 获取当前请求的 methodName  获取当前用户
		String methodName = ai.getMethodName();
		String currentUser = getCurrentUser(ai);
		// 获取注解的方法
		Method[] methods = ai.getController().getClass().getDeclaredMethods();
		Method meth = null;
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				meth = m;
				break;
			}
		}
		//方法没有注解直接返回
		if (meth.getAnnotations().length == 0) {
			ai.invoke();
			return;
		}
		//获取操作日志的注解
		
		boolean flag = false;
		Annotation[] annotations = meth.getAnnotations();
		for(int i= 0;i<annotations.length;i++){
			String name = annotations[i].annotationType().getCanonicalName();
			if(LogBind.class.getCanonicalName().equals(name)){
				flag = true;
				LogBind logb = (LogBind) meth.getAnnotations()[i];
				//方法开始之前、结束之后操作信息
				StringBuffer MethodBegin = new StringBuffer();
				StringBuffer MethodEnd = new StringBuffer();
				//获取系统的换行符
				String separator = System.getProperty("line.separator");
				String uuid = LoggerUtil.getIntanceof().saveLogger(currentUser, logb.menuname(),logb.type(), logb.remark());
				//log4j中记录的日志信息
				Logger log = Logger.getLogger(meth.getClass());
				//类的全路径和方法头信息
				String classAndMethodInfo = "["+meth.getDeclaringClass().getName()+"."+meth.getName()+"()]:";
				MethodBegin.append(classAndMethodInfo).append(uuid).append(separator);
				MethodBegin.append("----------------------------  "+uuid+"  开始时间： "+ DateTimeUtil.getTime() + "------------------------------");
				log.warn(MethodBegin.toString());
				//执行具体的方法
				ai.getController().getRequest().setAttribute("uniqueKeyForLog", classAndMethodInfo+uuid);
				ai.invoke();
				MethodEnd.append(classAndMethodInfo).append(separator).append(uuid).append("   Action：").append(meth.getDeclaringClass().getName()).append("  |  ");
				MethodEnd.append("   Method：").append(meth.getName()).append("()").append(separator);
				MethodEnd.append(uuid).append("   操作人：").append(currentUser).append("  |  ");
				MethodEnd.append("操作菜单：").append(logb.menuname()).append("  |  ");
				MethodEnd.append("操作类型：").append(getType(logb.type())).append("  |  ");
				MethodEnd.append("操作备注：").append(logb.remark()).append(separator);
				MethodEnd.append("----------------------------  "+uuid+"  结束时间："+ DateTimeUtil.getTime() + "------------------------------");
				log.warn(MethodEnd.toString());
			}
		}
		if(flag == false){
			ai.invoke();
		}
		
	}
	
	private String getType(String type){
		switch (type) {
			case "0":
				return "打开页面";
			case "1":
				return "登陆";
			case "2":
				return "注销";
			case "3":
				return "查询";
			case "4":
				return "新增";
			case "5":
				return "更新";
			case "6":
				return "删除";
			case "7":
				return "下载";
			case "8":
				return "轮岗规则设置";
			case "9":
				return "发送短信";
			}
		return type;
	}
	private String getCurrentUser(ActionInvocation ai) {
		String user_token = ai.getController().getCookie("user_token");
		if (user_token == null || user_token.equals(""))
			return null;
		Object r = CacheUtil.getCache(user_token, ai.getController().getRequest());
		if (r == null)
			return null;
		return ((Record) r).getStr("USER_NO");
	}
}
