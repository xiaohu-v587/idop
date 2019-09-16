/**
 * 
 */
package com.goodcol.controller.zxglctl;

import java.util.List;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.server.zxglserver.PccmLabelSendServer;
import com.goodcol.server.zxglserver.PccmStandardServer;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * @author dinggang
 *
 */
@RouteBind(path = "/pccm_label_send")
@Before({ ManagerPowerInterceptor.class })
public class PccmLabelSendCtl extends BaseCtl{

	@Override
	public void index() {
		render("index.jsp");
	}
	public void getList(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String custno=getPara("custno");
		String custname=getPara("custname");
		Record record=new Record();
		record.set("pageNum", pageNum);
		record.set("pageSize", pageSize);
		record.set("custno",custno);
		record.set("custname", custname);
		PccmLabelSendServer pccmLabelSendServer=new PccmLabelSendServer();
		List<Record> records =pccmLabelSendServer.getList(record);
		setAttr("data", records);
		setAttr("total", records.size());
		renderJson();
	}
	public void form(){
		render("form.jsp");
	}
	public void export(){
		renderNull();
	}
	public void send(){
		renderJson();
	}
}
