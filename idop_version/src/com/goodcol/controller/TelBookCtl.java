package com.goodcol.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.TelBookServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

/**
 * 通讯录
 * 
 * @author first blush
 */
@RouteBind(path = "/telBook")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class TelBookCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(TelBookCtl.class);
	TelBookServer telBookServer = new TelBookServer();

	/**
	 * 初始化
	 */
	public void index() {
		render("index.jsp");
	}
	
	/**
	 * 增加和修改都使用
	 */
	public void form() {
		render("form.jsp");
	}

	public void listTel(){
	
		// 获取查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		
		//获取登陆用户组织代码用于权限控制
		String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		String orgId = getPara("org_id");
		
		if(AppUtils.StringUtil(orgId)==null&&roleNames.contains("领导")){
			orgId = AppUtils.getUpOrg(getCurrentUser().getStr("ORG_ID"), AppUtils.getLevByRole(roleNames));
		}else if(AppUtils.StringUtil(orgId)==null&&!roleNames.contains("领导")){
			orgId =getCurrentUser().getStr("ORG_ID");
		}
		map.put("orgArr", AppUtils.getOrgStr(orgId,"2"));
		
		// 获取页面输入查询条件
		map.put("id", getPara("id"));
		map.put("name", getPara("name"));
		map.put("mobile", getPara("mobile"));
		// 从数据库查询指定条数的用户记录
		Page<Record> r =telBookServer.pageList(map);

		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());

		// 打印日志
		log.info("listBusi--r.getList():" + r.getList());
		log.info("listBusi--r.getTotalRow():" + r.getTotalRow());
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 下载用户数据，保存为excel文件
	 */
	@LogBind(menuname = "通讯录", type = "7", remark = "通讯录-下载")
	public void download() {

		// 获取查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		
		//获取登陆用户组织代码用于权限控制
		String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		String orgId = getPara("org_id");
		
		if(AppUtils.StringUtil(orgId)==null&&roleNames.contains("领导")){
			orgId = AppUtils.getUpOrg(getCurrentUser().getStr("ORG_ID"), AppUtils.getLevByRole(roleNames));
		}else if(AppUtils.StringUtil(orgId)==null&&!roleNames.contains("领导")){
			orgId =getCurrentUser().getStr("ORG_ID");
		}
		map.put("orgArr", AppUtils.getOrgStr(orgId,"2"));
		
		// 获取页面输入查询条件
		map.put("id", getPara("id"));
		map.put("name", getPara("name"));
		map.put("mobile", getPara("mobile"));
		// 从数据库查询指定条数的用户记录
		List<Record> lr = telBookServer.custMgrList(map);
		int a = 0;
		if(null!=lr){
			a = lr.size();
		}
		System.out.println("当前条数：" + a);
//		if (a == 0) {
//			renderText("<script type='text/javascript'>parent.callback(\"当前没有数据可以下载\"); </script>");
//			return;
//		}
//
//		if (a < 3000) {
//			lr = Db.use("default").find((String) map.get("selectSql") + (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
//
//		} else {
//			renderText("<script type='text/javascript'>parent.callback(\"下载数据不能超过3000条!\"); </script>");
//			return;
//		}

		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = { "orgname", "id", "name", "phone", "remark" };

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "所属机构", "EHR号", "员工姓名", "手机号码", "备注" };

		String fileName = "";
		try {
			fileName = new String("通讯录下载.xls".getBytes("GB2312"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}

		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
		er.render();
		// 打印日志
		log.info("download--lr:" + lr);
		renderNull();
	}

}
