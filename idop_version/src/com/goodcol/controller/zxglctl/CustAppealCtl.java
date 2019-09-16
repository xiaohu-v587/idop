package com.goodcol.controller.zxglctl;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.CustAppealServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

/**
 * 客户申诉
 * 
 * @author start
 * 
 */
@RouteBind(path = "/zxCustAppeal")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class CustAppealCtl extends BaseCtl {
	
	public static Logger log = Logger.getLogger(CustAppealCtl.class);
	CustAppealServer custAppealServer = new CustAppealServer();
	
	/**
	 * 主页面初始化
	 */
	public void index() {
		String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		String orgId = getCurrentUser().getStr("ORG_ID");
		String roleLevel = AppUtils.getLevByRole(roleNames);
		String upOrg = orgId;
		if(AppUtils.StringUtil(roleLevel)!=null){
			//取对应权限级别的orgid
			upOrg = AppUtils.getUpOrg(orgId, roleLevel);	
		}
		
		setAttr("orgId", upOrg);
		
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
		
		//获取登陆用户组织代码用于权限控制
		String orgId = getPara("orgId");
		map.put("orgArr", AppUtils.getOrgStr(orgId,"2"));
		
		//获取页面输入查询条件
		map.put("cust_no", getPara("cust_no"));
//		map.put("cust_mgr", getPara("cust_mgr"));
		map.put("name", getPara("name"));
		map.put("appeal_stat", getPara("appeal_stat"));
		map.put("appeal_reas", getPara("appeal_reas"));
		
		// 从数据库查询指定条数的用户记录
		Page<Record> r =custAppealServer.pageList(map);

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
	 *强制分配保存
	 */
	public void distsave() {
		// 获取查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		//获取登陆用户组织代码用于权限控制
		map.put("user_id", getCurrentUser().getStr("id"));
		map.put("user_name", getCurrentUser().getStr("name"));
		
		map.put("id", getPara("id"));
		map.put("appealid", getPara("appealid"));
		map.put("customercode", getPara("customercode"));
		map.put("mgr_ids", getPara("mgr_ids"));
		map.put("mgr_names", getPara("mgr_names"));
		map.put("claim_props", getPara("claim_props"));
		
		custAppealServer.distrSave(map);
		
		renderNull();
	}
	
	/**
	 * 强制分配页面页面初始化
	 */
	public void compuDistri() {
		render("distribution.jsp");
	}
	
	/**
	 * 驳回保存
	 */
	public void reject() {
		String appealId = getPara("appealid");
		custAppealServer.reject(appealId);
		renderNull();
	}
	
	/**
	 * 申诉原因页面初始化
	 */
	public void detail() {
		render("detail.jsp");
	}
	
	/**
	 * 详情查询
	 */
	public void getDetail() {
		String id = getPara("id");
		if (AppUtils.StringUtil(id) == null) {
			renderNull();
			return;
		}
		Record re = custAppealServer.getDetail(id);
		setAttr("record", re);
		renderJson();
	}
	
	/**
	 * 用户当前组织成员下拉框数据
	 */
	public void getOrgUser() {
		// 获取当前用户信息
		String orgId = getCurrentUser().getStr("ORG_ID");
		List<Record> list = custAppealServer.getOrgUser(AppUtils.getOrgStr(orgId,"2"));
		//List<Record> list = custAppealServer.getOrgUser(null);
		JSONArray array = JSONArray.fromObject(list);
		String jsonStr = "";
		for (int i = 0; i < array.size(); i++) {
			String columns = array.getJSONObject(i).getString("columns");
			if (i == 0) {
				jsonStr = columns;
			} else if (i > 0) {
				jsonStr = columns + "," + jsonStr;
			}
		}
		jsonStr = "[ " + jsonStr + " ]";
		renderJson(jsonStr);
	}
	
	/**
	 * 下载用户数据，保存为excel文件
	 */
	@LogBind(menuname = "客户申诉", type = "7", remark = "客户申诉-下载")
	public void download() {

		// 获取查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		//获取登陆用户组织代码用于权限控制
		String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		String orgId = getPara("orgId");
		map.put("orgArr", roleNames.contains("领导")?null:AppUtils.getOrgStr(orgId,"2"));
		
		//获取页面输入查询条件
		map.put("cust_no", getPara("cust_no"));
//		map.put("cust_mgr", getPara("cust_mgr"));
		map.put("name", getPara("name"));
		map.put("appeal_stat", getPara("appeal_stat"));
		map.put("appeal_reas", getPara("appeal_reas"));

		List<Record> lr = custAppealServer.custList(map);
		int a = 0;
		if(null!=lr){
			a = lr.size();
		}
		System.out.println("当前条数：" + a);

//		if (a == 0) {
//			renderText("<script type='text/javascript'>parent.callback(\"当前没有数据可以下载\"); </script>");
//			return;
//		}else if (a >= 3000) {
//			renderText("<script type='text/javascript'>parent.callback(\"下载数据不能超过3000条!\"); </script>");
//			return;
//		}

		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = { "cust_no", "name","appeal_per_name","mobile", "appeal_reas", "cust_stat_cn","appeal_result"};

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "客户号", "客户名称","申诉人","申诉人联系方式", "申请方案","申诉状态","处理结果" };

		String fileName = "";
		try {
			fileName = new String("客户申诉列表.xls".getBytes("GB2312"), "ISO-8859-1");
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
	
	/**
	 * 查询客户是否处于申诉状态
	 */
	public void getAppNum() {
		String poolId = getPara("id");
		int num = custAppealServer.getAppNum(poolId);
		setAttr("num", num);
		renderJson();
	}
	
	/**
	 * 最新数据时间
	 */
	public void findNewDate(){
		
		
		setAttr("newDate", new SimpleDateFormat("yyyyMMdd").format(new Date()));

		renderJson();
	}
}
