package com.goodcol.controller;

import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.core.aop.Before;

/**
 * 首页提醒
 * @author sjgl011
 * 
 */
@RouteBind(path = "/home")
@Before({ ManagerPowerInterceptor.class })
public class HomeCtl extends BaseCtl {

	@Override
	public void index() {
		render("../index.html");
	}

	/**
	 * 前台调用的方法 1.判断用户需要显示哪几个项目的信息 2.查询这几个项目的信息，如果没有数据，就不显示
	 */
	public void getData() {
		

	}
}
