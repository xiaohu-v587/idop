package com.goodcol.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.CustKpiServer;
import com.goodcol.server.zxglserver.PccmKPIParaServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.KplUtil;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

/**
 * kpi查询
 * 
 * @author first blush
 */
@RouteBind(path = "/kpi")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class KpiCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(KpiCtl.class);
	CustKpiServer custKpiServer = new CustKpiServer();
	private PccmKPIParaServer server = new PccmKPIParaServer();
	//public final static String excu_month = new SimpleDateFormat("yyyyMM").format(new Date());
	public final static String excu_month = DateTimeUtil.getLastMonth();
	
	/**
	 * 初始化
	 */
	public void index() {
		//render("index.jsp");
		String rolename = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		String role_type = "1";
		if(rolename.contains("领导")||rolename.contains("系统管理员")){
			role_type="2";
		}
		setAttr("mgr_id", getCurrentUser().getStr("ID"));
		setAttr("cust_type", AppUtils.getApplyRole(getCurrentUser().getStr("ID")));
		setAttr("role_type", role_type);
		setAttr("period", excu_month);
		render("myKpi.jsp");
	}
	
	/**
	 * 跳转详情页面//大公司、中小企业、金融机构客户经理
	 */
	public void detail() {
		render("detail.jsp");
	}
	/**
	 * 跳转详情页面//账户经理统计
	 */
	public void detail_acc_cust() {
		render("detail_acc_cust.jsp");
	}
	/**
	 * 跳转详情页面//行政事业客户经理
	 */
	public void detail_admi_cust() {
		render("detail_admi_cust.jsp");
	}

	/**
	 * kpi列表
	 */
	public void listKpi(){
	
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
		map.put("cust_no", getPara("cust_no"));
		map.put("name", getPara("name"));
		map.put("org_id", getPara("org_id"));
		map.put("date", getPara("date"));
		map.put("base_role", getPara("base_role"));
		
		// 从数据库查询指定条数的用户记录
		Page<Record> r =custKpiServer.pageList(map);

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
	 * 详情查询
	 */
	public void getDetail() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		
		String mgr_no = getPara("mgr_id");
		String period = getPara("period");
		String clas_five = getPara("clas_five");
		
		if (AppUtils.StringUtil(mgr_no) == null||AppUtils.StringUtil(period) == null) {
			renderNull();
			return;
		}
		map.put("clas_five", clas_five);
		map.put("mgr_no", mgr_no);
		map.put("period", period);
		
		// 从数据库查询指定条数的用户记录
		Page<Record> r =custKpiServer.pageDetail(map);

		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		renderJson();
	}
	
	/**
	 * 下载用户数据，保存为excel文件
	 */
	@LogBind(menuname = "客户申诉", type = "7", remark = "客户申诉-下载")
	public void download() {

		// 获取查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		//获取登陆用户组织代码用于权限控制
		map.put("orgId", getCurrentUser().getStr("ORG_ID"));
		
		// 获取页面输入查询条件
		map.put("cust_no", getPara("cust_no"));
		map.put("name", getPara("name"));
		map.put("org_id", getPara("org_id"));
		map.put("date", getPara("date"));

		List<Record> lr = custKpiServer.findList(map);
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
		String[] headers = {"period", "second_orgname", "third_orgname", "orgname", "name","cust_mgr_no",
				"cur_post","mgr_level","kpi","promote_stand","is_stand" };

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = {"期次","二级分行名称","中心支行名称","责任中心名称", "客户经理", "EHR", "岗位","岗位等级","KPI值",
				"晋升标准","是否达标"};
		String fileName = "";
		try {
			fileName = new String("KPI列表.xls".getBytes("GB2312"), "ISO-8859-1");
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
	 * kpi测试
	 * @throws Exception 
	 */
	public void kpiJob() throws Exception {
		KplUtil.kplTask(new SimpleDateFormat("yyyyMM").format(new Date()));
		renderNull();
	}

	/********************************************************************************************/
	/**
	 * 五层分类列表
	 */
	public void listFiveKpi(){
	
		Map<String, Object> map = new HashMap<String, Object>();
		
		//获取登陆用户组织代码用于权限控制
		map.put("mgr_id", getPara("mgr_id"));
		map.put("period", getPara("period"));
		
		// 从数据库查询指定条数的记录
		List<Record> lr = custKpiServer.clasFiveList(map);
		Record re = new Record();
		double total=0;
		double kpi=0;
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				kpi = lr.get(i).getBigDecimal("kpi").doubleValue();
				total = BigDecimal.valueOf(total).
						add(BigDecimal.valueOf(kpi)).doubleValue();
			}
			re.set("clas_five_cn", "合计");
			re.set("kpi", total);
			lr.add(re);
		}
		
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", lr);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 饼图
	 */
	public void kpiPieData(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		//获取登陆用户组织代码用于权限控制
		map.put("mgr_id", getPara("mgr_id"));
		map.put("period", getPara("period"));
		
		// 从数据库查询指定条数的记录
		List<Record> lr = custKpiServer.clasFiveList(map);
		JSONArray parray = new JSONArray();
		String name = null;
		double value = 0;
		String data = null;
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				name = lr.get(i).getStr("clas_five_cn");
				value = lr.get(i).getBigDecimal("kpi").doubleValue();
				data ="{value:"+value+",name:'"+name+"'}";
				parray.add(data);
			}
		}	
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("pdata", parray);
		
		// 返回json数据
		renderJson();
	}
	
	
	/**
	 * kpi折线图查询
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void kpiLineData(){
		
		String mgr_id = getPara("mgr_id");
		
		List mList = new ArrayList();
		List dList = new ArrayList();		
		List moveList = new ArrayList();
		List upList = new ArrayList();
		List nextList = new ArrayList();
		
		String moveVal = "0";
		String upVal = "0";
		String nextVal = "0";
		Record re = custKpiServer.getDatas(mgr_id);
		if(null!=re){
			moveVal = AppUtils.StringUtil(re.getStr("trans_stand")) == null?"0":re.getStr("trans_stand");
			upVal = AppUtils.StringUtil(re.getStr("promote_stand")) == null?"0":re.getStr("promote_stand");
			nextVal = AppUtils.StringUtil(re.getStr("next_value")) == null?"0":re.getStr("next_value");
		}
		
		double min=0;
		if(Double.parseDouble(moveVal)>=Double.parseDouble(upVal)){
			min = Double.parseDouble(upVal);
		}else{
			min = Double.parseDouble(moveVal);
		}
		if(min>Double.parseDouble(nextVal)){
			min = Double.parseDouble(nextVal);
		}
		
		// 从数据库查询指定条数的记录
		List<Record> lr = custKpiServer.kpiLineData(mgr_id);
		String m=null;
		double d=0;
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				m = lr.get(i).getStr("period");
				d = lr.get(i).getBigDecimal("kpi").doubleValue();
				mList.add(m);
				dList.add(d);				
				moveList.add(moveVal);
				upList.add(upVal);
				nextList.add(nextVal);
				if(d<min){
					min=d;
				}
			}
		}		
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("min", min>10?min-10:0);
		setAttr("mList", mList);
		setAttr("dList", dList);
		setAttr("moveList", moveList);
		setAttr("upList", upList);
		setAttr("nextList", nextList);	
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 获取期数下拉框的值
	 */
	public void getDateDatas() {
		// 获取期数下拉框到的值
		List<Record> list = custKpiServer.getDateDatas();
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
	 * 获取组织下拉框的值
	 */
	public void getOrgDatas() {
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		String orgId = getCurrentUser().getStr("ORG_ID");
		String level = AppUtils.getOrgLevel(orgId);
		String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		if(roleNames.contains("领导")){
			level = AppUtils.getLevByRole(roleNames);
			orgId = AppUtils.getUpOrg(orgId, level);
		}
		List<Record> list = AppUtils.getOrgList(orgId);
		setAttr("datas", list);
		// 打印日志
		log.info("getListById--datas:" + list);
		renderJson();
	}
	
	/**
	 * 进入商机转化率页面
	 * 2018年6月25日14:37:17
	 * @author liutao
	 */
	public void marketCustomer(){
		renderJsp("marketCustomer.jsp");
	}
	
	/**
	 * 进入潜在达成率页面
	 * 2018年6月25日14:37:17
	 * @author liutao
	 */
	public void prospectiveCustomer(){
		renderJsp("prospectiveCustomer.jsp");
	}
	
	/**
	 * 查询商机转化率数据
	 * 2018年6月26日09:59:44
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回商机转化率数据
	 */
	public void findMarketCustomer(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		String custName = getPara("custName");
		String userNo = getCurrentUser().getStr("USER_NO");
		map.put("custName", custName);
		map.put("userNo", userNo);
		Page<Record> page = custKpiServer.findMarketCustomer(map);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	/**
	 * 查询商机客户数、转化客户数以及中心支行排名
	 * 2018年6月26日11:08:31
	 * @author liutao
	 * @return
	 */
	public void findMarketCustomerRank(){
		/*String userNo = getCurrentUser().getStr("USER_NO");
		//String custName = getPara("custName");
		Record rank1 = custKpiServer.findMarketCustomerRank(userNo, "1");//省行
		Record rank2 = custKpiServer.findMarketCustomerRank(userNo, "2");//分行
		Record rank3 = custKpiServer.findMarketCustomerRank(userNo, "3");//支行
		Record rank = new Record();
		if(null != rank1){
			BigDecimal rankNum1 = rank1.getBigDecimal("ranknum");
			BigDecimal rankNum2 = new BigDecimal(0);
			if(null != rank2){
				rankNum2 = rank2.getBigDecimal("ranknum");
			}
			BigDecimal rankNum3 = new BigDecimal(0);
			if(null != rank3){
				rankNum3 = rank3.getBigDecimal("ranknum");
			}
			rank = rank1;
			rank.set("ranknum", rankNum1 + "/" + rankNum2 + "/" + rankNum3);
			setAttr("data", rank);
			JSONArray parray = new JSONArray();
			String name = "商机客户数";
			String data ="{value:"+rank.getBigDecimal("sumnum")+",name:'"+ name +"'}";
			parray.add(data);
			name = "转化客户数";
			data ="{value:"+rank.getBigDecimal("succnum")+",name:'"+ name +"'}";
			parray.add(data);
			setAttr("pdata", parray);
		}
		renderJson();*/
		
		String userNo = getCurrentUser().getStr("USER_NO");
		Record rank = custKpiServer.findMarketCustomerRank(userNo);
		if(null != rank){
			String rankNum1 = rank.getStr("p_rank");
			String rankNum2 = rank.getStr("b_rank");
			String rankNum3 = rank.getStr("s_rank");
			rank.set("ranknum", rankNum1 + "/" + rankNum2 + "/" + rankNum3);
			setAttr("data", rank);
			JSONArray parray = new JSONArray();
			String name = "商机客户数";
			String data ="{value:"+rank.getStr("sumnum")+",name:'"+ name +"'}";
			parray.add(data);
			name = "转化客户数";
			data ="{value:"+rank.getStr("succnum")+",name:'"+ name +"'}";
			parray.add(data);
			setAttr("pdata", parray);
		}
		renderJson();
	}
	
	/**
	 * 查询个人潜在客户数据
	 * 2018年7月6日15:34:01
	 * @author liutao
	 */
	public void findLatentCustomer(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		String custName = getPara("custName");
		String custNo = getPara("custNo");
		String userNo = getCurrentUser().getStr("USER_NO");
		map.put("custName", custName);
		map.put("userNo", userNo);
		map.put("custNo", custNo);
		Page<Record> page = custKpiServer.findLatentCustomer(map);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	/**
	 * 查询个人潜在客户达成率排名相关数据
	 * 2018年7月6日15:35:02
	 * @author liutao
	 */
	public void findLatentCustomerRank(){
		String userNo = getCurrentUser().getStr("USER_NO");
		//String custName = getPara("custName");
		Record rank = custKpiServer.findLatentCustomerRank(userNo);
		if(null != rank){
			setAttr("data", rank);
			JSONArray parray = new JSONArray();
			String name = "潜在客户数";
			String data ="{value:"+rank.getStr("sumnum")+",name:'"+ name +"'}";
			parray.add(data);
			name = "达成客户数";
			data ="{value:"+rank.getStr("succnum")+",name:'"+ name +"'}";
			parray.add(data);
			setAttr("pdata", parray);
		}
		renderJson();
	}
	
	/**
	 * 查询计算潜在达成客户达成率定时任务
	 * 2018年7月9日09:30:48
	 * @author liutao
	 */
	public void calcUserLatentCustomerJob(){
		//查询出所有需要进行计算的客户经理
		List<Record> list = custKpiServer.findLatentUser();
		for (Record record : list) {
			String user_no = record.getStr("user_no");
			//分别查询每个客户经理的潜在客户相关数(潜在客户达成数和潜在客户总数)
			int succNum = custKpiServer.findLatentNumByUserNo(user_no, "succ");
			int sumNum = custKpiServer.findLatentNumByUserNo(user_no, null);
			String reach = "0";
			if(sumNum > 0){
				float rea = (float)succNum/sumNum*100;
				if(rea != 0){
					reach = String.format("%.2f", rea);
				}
			}
			record.set("succnum", succNum + "");
			record.set("sumnum", sumNum + "");
			record.set("reach", reach);
			record.set("p_rank", "0");
			record.set("b_rank", "0");
			record.set("s_rank", "0");
			custKpiServer.saveUserLatent(record);
			//查询客户经理排名
			int p_rank = custKpiServer.findUserRank(user_no, "1");//省行排名
			int b_rank = custKpiServer.findUserRank(user_no, "2");//分行排名
			int s_rank = custKpiServer.findUserRank(user_no, "3");//支行排名
			record.set("p_rank", p_rank + "");
			record.set("b_rank", b_rank + "");
			record.set("s_rank", s_rank + "");
			//保存信息
			custKpiServer.saveUserLatent(record);
		}
		renderNull();
	}

	/**
	 * 查询计算商机转化客户转化率定时任务
	 * 2018年7月9日09:30:48
	 * @author liutao
	 */
	public void calcUserMarketCustomerJob(){
		//查询出所有需要进行计算的客户经理
		List<Record> list = custKpiServer.findLatentUser();
		for (Record record : list) {
			String user_no = record.getStr("user_no");
			//分别查询每个客户经理的潜在客户相关数(商机客户转化数和商机客户总数)
			int succNum = custKpiServer.findMarketNumByUserNo(user_no, "succ");
			int sumNum = custKpiServer.findMarketNumByUserNo(user_no, null);
			String reach = "0";
			if(sumNum > 0){
				float rea = (float)succNum/sumNum*100;
				if(rea != 0){
					reach = String.format("%.2f", rea);
				}
			}
			record.set("succnum", succNum + "");
			record.set("sumnum", sumNum + "");
			record.set("reach", reach);
			record.set("p_rank", "0");
			record.set("b_rank", "0");
			record.set("s_rank", "0");
			custKpiServer.saveUserMarket(record);
			//查询客户经理排名
			int p_rank = custKpiServer.findUserMarketRank(user_no, "1");//省行排名
			int b_rank = custKpiServer.findUserMarketRank(user_no, "2");//分行排名
			int s_rank = custKpiServer.findUserMarketRank(user_no, "3");//支行排名
			record.set("p_rank", p_rank + "");
			record.set("b_rank", b_rank + "");
			record.set("s_rank", s_rank + "");
			//保存信息
			custKpiServer.saveUserMarket(record);
		}
		renderNull();
	}
	
	/**
	 * 查询机构下的潜在达成率排名数据等(查询机构潜在达成客户数，机构潜在客户总数，以及达成率和排名等)
	 * 2018年7月11日19:36:44
	 * @author liutao
	 * @return
	 */
	public void findOrgLatentRank(){
		//int pageNum = getParaToInt("pageIndex") + 1;
		//int pageSize = getParaToInt("pageSize", 10);
		//String orgnum = getPara("orgnum");
		//Page<Record> page = custKpiServer.findOrgLatentRank(pageNum, pageSize, orgnum);
		//setAttr("data", page.getList());
		//setAttr("total", page.getTotalRow());
		String type = getPara("type");
		String orgnum = getPara("orgnum");
		String orgLevel = getPara("orgLevel");
		Record r = new Record();
		/*if("4".equals(orgLevel)){
			r = custKpiServer.findOrgUserLatentSum(orgnum);
		}else{
			r = custKpiServer.findOrgLatentSum(orgnum);
		}*/
		r = custKpiServer.findOrgLatentSum(orgnum);
		r.set("name", custKpiServer.findOrgNameByOrgId(orgnum));
		if(!"ring".equals(type)){
			List<Record> records = new ArrayList<Record>();
			if("4".equals(orgLevel)){
				//查询支行下个人的数据
				records = custKpiServer.findOrgUserLatentRank(orgnum);
			}else{
				records = custKpiServer.findOrgLatentRank(orgnum);
			}
			List<Record> list = new ArrayList<Record>();
			list.add(r);
			list.addAll(records);
			setAttr("data", list);
		}else{
			//饼图内容
			JSONArray parray = new JSONArray();
			String name = "商机客户数";
			String data ="{value:"+r.getBigDecimal("sumnum")+",name:'"+ name +"'}";
			//切换到GBASE数据库之后返回的数据类型也需要切换  2018年8月28日16:36:13 --liutao
			//String data ="{value:"+r.getDouble("sumnum")+",name:'"+ name +"'}";
			parray.add(data);
			name = "转化客户数";
			data ="{value:"+r.getBigDecimal("succnum")+",name:'"+ name +"'}";
			//切换到GBASE数据库之后返回的数据类型也需要切换  2018年8月28日16:36:13 --liutao
			//data ="{value:"+r.getDouble("succnum")+",name:'"+ name +"'}";
			parray.add(data);
			setAttr("pdata", parray);
		}
		renderJson();
	}
	
	/**
	 * 查询机构下的商机转化率排名数据等(查询机构商机转化客户数，机构商机客户总数，以及转化率和排名等)
	 * 2018年7月11日19:36:44
	 * @author liutao
	 * @return
	 */
	public void findOrgMarketRank(){
		String type = getPara("type");
		String orgnum = getPara("orgnum");
		String orgLevel = getPara("orgLevel");
		//int pageNum = 1;
		//int pageSize = 10;
		//Page<Record> page = null;
		Record r = new Record();
		/*if("4".equals(orgLevel)){
			r = custKpiServer.findOrgUserMarketSum(orgnum);
		}else{
			r = custKpiServer.findOrgMarketSum(orgnum);
		}*/
		//不使用递归查询后使用同一个sql即可查询出正确数据
		r = custKpiServer.findOrgMarketSum(orgnum);
		r.set("name", custKpiServer.findOrgNameByOrgId(orgnum));
		if(!"pie".equals(type)){
			//pageNum = getParaToInt("pageIndex") + 1;
			//pageSize = getParaToInt("pageSize", 10);
			//page = custKpiServer.findOrgMarketRank(pageNum, pageSize, orgnum);
			List<Record> records = new ArrayList<Record>();
			if("4".equals(orgLevel)){
				//查询支行下个人的数据
				records = custKpiServer.findOrgUserMarketRank(orgnum);
			}else{
				records = custKpiServer.findOrgMarketRank(orgnum);
			}
			//page.getList().add(r);
			List<Record> list = new ArrayList<Record>();
			list.add(r);
			/*for(Record r1 : page.getList()){
				list.add(r1);
			}*/
			//list.addAll(page.getList());
			list.addAll(records);
			setAttr("data", list);
			//setAttr("total", page.getTotalRow());
		}else{
			//饼图内容
			JSONArray parray = new JSONArray();
			String name = "商机客户数";
			String data ="{value:"+r.getBigDecimal("sumnum")+",name:'"+ name +"'}";
			//切换到GBASE数据库数据类型  2018年8月30日15:34:29 --liutao
			//String data ="{value:"+r.getDouble("sumnum")+",name:'"+ name +"'}";
			parray.add(data);
			name = "转化客户数";
			data ="{value:"+r.getBigDecimal("succnum")+",name:'"+ name +"'}";
			//切换到GBASE数据库数据类型  2018年8月30日15:34:29 --liutao
			//data ="{value:"+r.getDouble("succnum")+",name:'"+ name +"'}";
			parray.add(data);
			setAttr("pdata", parray);
		}
		renderJson();
	}
	
	/**
	 * 查询当前用户是否包含领导角色
	 * 2018年7月17日09:47:18
	 * @author liutao
	 */
	public void findUserOrg(){
		Record m = getCurrentUser();
		String userNo = m.getStr("USER_NO");
		List<Record> records = server.findWhetherLead(userNo);
		int roleLevel = 9;
		if(null != records && records.size() > 0){
			for (Record record : records) {
				String name = record.getStr("name");
				if(StringUtils.isNotBlank(name)){
					if(name.contains("省行")){
						setAttr("flag", "1");
						if(roleLevel > 1){
							roleLevel = 1;
						}
					}else if(name.contains("二级分行")){
						if(roleLevel > 2){
							setAttr("flag", "2");
							roleLevel = 2;
						}
					}else if(name.contains("中心支行")){
						if(roleLevel > 3){
							setAttr("flag", "3");
							roleLevel = 3;
						}
					}else{
						if(roleLevel > 3){
							setAttr("flag", "0");
							roleLevel = 9;
						}
					}
				}else{
					if(roleLevel > 3){
						setAttr("flag", "0");
						roleLevel = 9;
					}
				}
			}
		}else{
			setAttr("flag", "0");
		}
		String orgId = "";
		if(1 == roleLevel){
			//省行领导，直接返回省行机构号，机构层级：1
			orgId = custKpiServer.findOrgIdByUser(userNo, "1", "orgnum");
		}else if(2 == roleLevel){
			//分行领导，返回当前人所在的分行机构号，机构层级：2
			orgId = custKpiServer.findOrgIdByUser(userNo, "2", "orgnum");
		}else if(3 == roleLevel){
			//省行领导，返回当前人所在的支行机构号，机构层级：3
			orgId = custKpiServer.findOrgIdByUser(userNo, "3", "orgnum");
		}else{
			//非领导角色，需要判断是否存在省行本部或者"综合管理部"
			//1、省行本部的查看的就是省行领导查看的范围
			orgId = custKpiServer.findOrgIdByUser(userNo, "3", "orgnum");
			//001001000：江苏省分行本部机构id
			if("001001000".equals(orgId)){
				orgId = custKpiServer.findOrgIdByUser(userNo, "1", "orgnum");
			}else{
				orgId = "";//需要清空机构信息，否则返回去的是错误的
				//2、"综合管理部"查看的是当前人所在的支行机构号，查看的是支行领导查看的范围
				String orgname = custKpiServer.findOrgIdByUser(userNo, "4", "orgname");
				if(orgname.contains("综合管理部")){
					orgId = custKpiServer.findOrgIdByUser(userNo, "3", "orgnum");
				}
			}
		}
		setAttr("orgId", orgId);
		renderJson();
	}
	
	/**
	 * 查询机构商机转化率数据
	 * 2018年7月13日10:57:01
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回机构商机转化率数据
	 */
	public void findOrgMarketCustomer(){
		Map<String, Object> map = new HashMap<String, Object>();
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String orgnum = getPara("orgnum");
		String custName = getPara("custName");
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		map.put("orgNo", orgnum);
		map.put("custName", custName);
		Page<Record> p = custKpiServer.findOrgMarketCustomer(map);
		setAttr("data", p.getList());
		setAttr("total", p.getTotalRow());
		renderJson();
	}
	
	/**
	 * 查询机构潜在达成率数据
	 * 2018年7月17日19:32:32
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回机构潜在达成率数据
	 */
	public void findOrgLatentCustomer(){
		Map<String, Object> map = new HashMap<String, Object>();
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String orgnum = getPara("orgnum");
		String custName = getPara("custName");
		String custNo = getPara("custNo");
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		map.put("orgNo", orgnum);
		map.put("custName", custName);
		map.put("custNo", custNo);
		Page<Record> p = custKpiServer.findOrgLatentCustomer(map);
		setAttr("data", p.getList());
		setAttr("total", p.getTotalRow());
		renderJson();
	}
	
	/**
	 * 商机客户数据根据条件进行下载
	 * 2018年7月18日20:38:11
	 * @author liutao
	 */
	public void downloadMarket(){
		//需要区分个人和机构数据
		String custName = getPara("custName");
		String dataType = getPara("dataType");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("custName", custName);
		List<Record> lr = new ArrayList<Record>();
		if("2".equals(dataType)){
			//查询机构下载数据
			String orgnum = getPara("orgnum");
			map.put("orgNo", orgnum);
			lr = custKpiServer.downloadOrgMarket(map);
		}else{
			//查询个人下载数据
			String userNo = getCurrentUser().getStr("USER_NO");
			map.put("userNo", userNo);
			lr = custKpiServer.downloadPersonMarket(map);
		}
		
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = {"dummy_cust_no", "customername", "relate_cust_name", "name", "phone", "marketing_stat"};

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "虚拟客户号", "客户名称", "关联因素", "关联客户经理", "关联客户经理联系方式", "转化结果"};

		String fileName = "";
		try {
			fileName = new String("商机转化客户数据.xls".getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("OrgCtl-download", e);
		}

		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
		er.render();
		renderNull();
	}
	
	/**
	 * 潜在客户数据根据条件进行下载
	 * 2018年7月18日20:38:44
	 * @author liutao
	 */
	public void downloadLatent(){
		//需要区分个人和机构数据
		String custName = getPara("custName");
		String custNo = getPara("custNo");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("custName", custName);
		map.put("custNo", custNo);
		String dataType = getPara("dataType");
		List<Record> lr = new ArrayList<Record>();
		if("2".equals(dataType)){
			//查询机构下载数据
			String orgnum = getPara("orgnum");
			map.put("orgNo", orgnum);
			lr = custKpiServer.downloadOrgLatent(map);
		}else{
			//查询个人下载数据
			String userNo = getCurrentUser().getStr("USER_NO");
			map.put("userNo", userNo);
			lr = custKpiServer.downloadPersonLatent(map);
		}
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = {"orgname", "customercode", "customername", "latent_name", "incomday", "flag", "clas_five_name"};

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "责任中心名称", "客户号", "客户名称", "潜在分类", "存款&理财日均", "是否达成", "当前类型"};

		String fileName = "";
		try {
			fileName = new String("潜在达成客户数据.xls".getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("OrgCtl-download", e);
		}

		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
		er.render();
		renderNull();
	}
	
	/**
	 * 根据机构号查询上级机构号
	 * 2018年7月20日14:47:20
	 * @author liutao
	 */
	public void findParentOrgNum(){
		String orgnum = getPara("orgnum");
		orgnum = custKpiServer.findParentOrgNum(orgnum);
		setAttr("orgnum", orgnum);
		renderJson();
	}
}
