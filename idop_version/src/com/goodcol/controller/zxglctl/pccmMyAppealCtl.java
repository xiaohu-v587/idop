package com.goodcol.controller.zxglctl;

import java.util.HashMap;
import java.util.Map;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.PccmMyAppealServer;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 我的申诉
 * 
 * @author start
 * 
 */
@RouteBind(path = "/zxMyAppeal")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class pccmMyAppealCtl extends BaseCtl {
	
	public static Logger log = Logger.getLogger(pccmMyAppealCtl.class);
	PccmMyAppealServer pccmMyAppealServer = new PccmMyAppealServer();
	
	/**
	 * 主页面初始化
	 */
	public void index() {
		render("index.jsp");
	}
	
	/**
	 * 客户申诉列表
	 */
	public void findCustList(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		
		//默认条件	
		map.put("mgr_id", getCurrentUser().getStr("id"));
		// 获取页面输入查询条件
		map.put("cust_no", getPara("cust_no"));
		map.put("name", getPara("name"));
		map.put("appeal_stat", getPara("appeal_stat"));
		map.put("appeal_result", getPara("appeal_result"));
		
		// 从数据库查询指定条数的用户记录
		Page<Record> r =pccmMyAppealServer.pageList(map);

		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());

		// 打印日志
		log.info("listBusi--r.getList():" + r.getList());
		log.info("listBusi--r.getTotalRow():" + r.getTotalRow());
		// 返回json数据
		renderJson();
	}
	
}
