package com.goodcol.controller.zxglctl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.PccmKPIParaServer;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;
import com.goodcol.util.zxgldbutil.JsonToMapTool;

/**
 * 待办任务相关功能
 * 2018年5月14日14:54:01
 * @author liutao
 *
 */
@RouteBind(path = "/zxtask")
@Before( { ManagerPowerInterceptor.class,OperationLogRecordInterceptor.class})
public class TaskController extends BaseCtl{
	public static Logger log = Logger.getLogger(BaseCtl.class);
	private PccmKPIParaServer server = new PccmKPIParaServer();
	@Override
	public void index() {
		
	}
	
	/**
	 * 待办任务--其他客户列表页
	 * 2018年5月14日14:54:36
	 * @author liutao
	 */
	public void otherClient(){
		render("other.jsp");
	}

	/**
	 * 待办任务--潜在客户列表页
	 * 2018年5月14日15:39:59
	 * @author liutao
	 */
	public void prospectiveCustomer(){
		render("prospectiveCustomer.jsp");
	}
	
	/**
	 * 待办任务--手工报表列表页
	 * 2018年5月14日15:48:43
	 * @author liutao
	 */
	public void manualReport(){
		String level = getCurrentUser().getStr("role_level");
		//String level = Db.findFirst("select by2 from sys_org_info t where t.id= '"+org+"'").getStr("by2");
		setAttr("level",level);
		render("manualReport.jsp");
	}
	
	/**
	 * 待办任务--营销客户列表页
	 * 2018年5月14日15:31:26
	 * @author liutao
	 */
	public void marketCustomer(){
		render("marketCustomer.jsp");
	}
	
	/**
	 * 待办任务--我的KPI列表页
	 * 2018年5月14日15:31:26
	 * @author liutao
	 */
	public void mineKPI(){
		render("mineKPI.jsp");
	}
	
	/**
	 * 待办任务---营销客户
	 * 修改成--商机转化
	 * 2018年6月4日16:04:25
	 * @author liutao
	 * @return 返回所有商机待转化的客户
	 */
	public void findReachCustomer(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String custNo = getPara("customercode");
		String custName = getPara("customername");
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		Page<Record> r = server.findReachCustomer(pageNum, pageSize, custNo, custName, userNo);
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}
	
	/**
	 * 待办任务---潜在客户
	 * 2018年6月4日16:30:53
	 * @author liutao
	 * @return 返回所有潜在客户
	 */
	public void findLurkCustomer(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String custNo = getPara("customercode");
		String custName = getPara("customername");
		
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		Page<Record> r = server.findLurkCustomer(pageNum, pageSize, custNo, custName, userNo);
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}
	
	/**
	 * 进入他行客户详情页面
	 * 2018年6月9日11:10:31
	 * @author liutao
	 */
	public void custDetail(){
		render("custDetail.jsp");
	}
	
	/**
	 * 进入我行客户详情页面
	 * 2018年6月9日11:10:34
	 * @author liutao
	 */
	public void mineCustDetail(){
		render("mineCustDetail.jsp");
	}
	
	/**
	 * 进入我行潜力客户详情页面
	 * 2018年6月9日11:10:37
	 * @author liutao
	 */
	public void qlCustDetail(){
		render("qlCustDetail.jsp");
	}
	
	/**
	 * 查询我行客户详情
	 * 2018年6月9日14:10:11
	 * @author liutao
	 */
	public void findMineCustDetail(){
		String pcpId = getPara("pcpId");
		String period = getPara("period");
		Record r = server.findMineCustDetail(pcpId,period);
		setAttr("custInfo", r);
		renderJson();
	}
	
	/**
	 * 查询我行潜力客户详情
	 * 2018年6月9日14:30:32
	 * @author liutao
	 */
	public void findqlCustDetail(){
		String pcpId = getPara("pcpId");
		String period = getPara("period");
		Record r = server.findMineCustDetail(pcpId,period);
		setAttr("custInfo", r);
		renderJson();
	}
	
	/**
	 * 查询他行客户详情
	 * 2018年6月9日14:55:21
	 * @author liutao
	 */
	public void findCustDetail(){
		String pcpId = getPara("pcpId");
		Record r = server.findCustDetail(pcpId);
		setAttr("custInfo", r);
		renderJson();
	}
	
	/**
	 * 待办任务---潜在客户--数据下载
	 * 2018年7月2日14:44:06
	 * @author liutao
	 * @return 返回所有潜在客户
	 */
	@LogBind(menuname = "待办任务", type = "7", remark = "待办任务潜客提升客户下载")
	public void download(){
		String custNo = getPara("code");
		String custName = getPara("name");
		
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		List<Record> lr = server.download(custNo, custName, userNo);
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = {"orgname", "customercode", "customername", "cust_type", "daynum", "incomday"};

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "责任中心名称", "客户号", "客户名称", "潜在分类", "剩余天数", "当前日均"};

		String fileName = "";
		try {
			fileName = new String("潜客提升客户.xls".getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("OrgCtl-download", e);
		}

		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
		er.render();
		// 打印日志
		log.info("download--lr:" + lr);
		renderNull();
	}
	
	/**
	 * 待办任务---商机转化--数据下载
	 * 修改成--商机转化
	 * 2018年6月4日16:04:25
	 * @author liutao
	 * @return 返回所有商机待转化的客户
	 */
	public void downloadReach(){
		String custNo = getPara("customercode");
		String custName = getPara("customername");
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		List<Record> lr = server.downloadReach(custNo, custName, userNo);
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = {"dummy_cust_no", "customername", "relate_cust_name",
				"cust_manager", "daynum", "marketing_stat"};

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "虚拟客户号", "客户名称", "关联因素", "关联客户经理", "剩余天数", "营销结果"};

		String fileName = "";
		try {
			fileName = new String("商机转化客户.xls".getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("OrgCtl-download", e);
		}

		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
		er.render();
		// 打印日志
		log.info("download--lr:" + lr);
		renderNull();
	}
	
	/***
	 * 保存营销反馈
	 */
	public void saveMarketResult(){
		String str = getPara("marketList");
		Map<String, Object> resultMap = JsonToMapTool.convertJsonToMap("{list:"+str+"}");
		List<Map<String, Object>> list =  (List<Map<String, Object>>) resultMap.get("list");
		List<Record> statList = Db.use("default").find(" select key,name,val from GCMS_PARAM_INFO where key='MARKETING_STAT'");
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		for (Map<String, Object> map : list) {
			String id = map.get("id").toString();
			String marketResult = "0";
			String result = null;
			if (null != map.get("mark_result")) {
				result = String.valueOf(map.get("mark_result"));
				for (Record record : statList) {
					if (record.getStr("name").indexOf(result)>-1) {
						marketResult = record.getStr("val");
						break;
					}
				}
			}
			Db.use("default").update("update pccm_cust_claim set MARKETING_STAT = ? where CUST_POOL_ID = ? and CLAIM_CUST_MGR_ID = ?", marketResult, id, userNo);
		}
		renderNull();
	}
}
