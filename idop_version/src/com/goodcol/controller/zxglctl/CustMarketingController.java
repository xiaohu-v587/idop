package com.goodcol.controller.zxglctl;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.CustMarketingServer;
import com.goodcol.util.ext.anatation.RouteBind;

/***
 * 客户营销管理
 * 
 * @author start
 * @2018-6-4
 */
@RouteBind(path = "/zxMarket")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class CustMarketingController extends BaseCtl {
	private CustMarketingServer server = new CustMarketingServer();

	/**
	 * 新增营销记录
	 */
	public void addMarketing() {
		String custNo = getPara("customercode");
		String status = getPara("status");
		String date = getPara("marketDate");
		String remark = getPara("remark");
		String id = getPara("id");
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		boolean flag = server.save(custNo, userNo, date, status, remark, id);
		setAttr("flag", flag);
		renderJson();
	}

	/**
	 * 删除营销记录
	 */
	public void delMarketing() {
		String id = getPara("id");
		server.del(id);
		renderNull();
	}

	/**
	 * 查询列表
	 */
	public void findList() {
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String custNo = getPara("customercode");
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		Page<Record> r = server.findList(pageNum, pageSize, custNo, userNo);
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}

	@Override
	public void index() {
		renderJsp("/pages/zxuser/feedback.jsp");
	}

}
