package com.goodcol.controller;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.util.ext.anatation.RouteBind;




/**
 * 手工报表管理
 * 
 * @author sjgl011
 * 
 */
@RouteBind(path = "/reportTask")
@Before( { ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class})
// 路由
public class ReportTaskCtl extends BaseCtl{

	public static Logger log = Logger.getLogger(UserCtl.class);
	
	/**
	 * 控制器默认访问的方法
	 */
	@Override
	public void index() {
		// TODO Auto-generated method stub
		render("index.jsp");
	}
	
	
	
	
	
}
