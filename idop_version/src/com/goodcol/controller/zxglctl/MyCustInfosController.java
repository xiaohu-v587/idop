package com.goodcol.controller.zxglctl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import com.goodcol.server.zxglserver.PccmMyCustServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;
import com.goodcol.util.zxgldbutil.DownDetailUtil;

/**
 * 我的客户控制层
 * 
 * @author start
 * 
 */
@RouteBind(path = "/zxMyCust")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class MyCustInfosController extends BaseCtl {
	
	public static Logger log = Logger.getLogger(MyCustInfosController.class);
	PccmMyCustServer pccmMyCustServer = new PccmMyCustServer();
	CustAppealServer custAppealServer = new CustAppealServer();

	/**
	 * 主页面
	 */
	public void index() {
		//获取登陆用户组织代码
		setAttr("org_id", getCurrentUser().getStr("ORG_ID"));
		render("index.jsp");
	}

	/**
	 * 客户分类列表
	 */
	public void getCategoryList(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		//获取登陆用户组织代码用于权限控制
		map.put("orgId", getCurrentUser().getStr("ORG_ID"));
		map.put("userId", getCurrentUser().getStr("id"));
		
		// 获取页面输入查询条件
		map.put("cust_no", getPara("cust_no"));
		map.put("name", getPara("name"));
		map.put("clas_five", getPara("clas_five"));
		map.put("mark_prog", getPara("mark_prog"));
		map.put("cust_type", getPara("cust_type"));
		
		// 从数据库查询指定条数的用户记录
		List<Record> r =pccmMyCustServer.mainList(map);

		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r);

		// 打印日志
		log.info("listBusi--r.getList():" + r);
		// 返回json数据
		renderJson();
	}
	

	/**
	 * 客户详细列表
	 */
	public void detailPage() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		
		//获取登陆用户组织代码用于权限控制
		map.put("orgId", getCurrentUser().getStr("ORG_ID"));
		map.put("userId", getCurrentUser().getStr("id"));
		
		// 获取页面输入查询条件
		map.put("cust_no", getPara("cust_no"));
		map.put("name", getPara("name"));
		map.put("clas_five", getPara("clas_five"));
		map.put("mark_prog", getPara("mark_prog"));
		map.put("cust_type", getPara("cust_type"));
		map.put("clas_click", getPara("clas_click"));
		
		// 从数据库查询指定条数的用户记录
		Page<Record> r =pccmMyCustServer.detailPage(map);

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
	 * 客户详情
	 */
	public void getCustDetail() {
		render("custDetail.jsp");
	}
	
	/**
	 * 客户移交
	 */
	public void transfer() {
		render("transfer.jsp");
	}
	
	/**
	 * 客户移交保存
	 */
	public void transave() {
		// 获取查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		//获取登陆用户组织代码用于权限控制
		map.put("user_id", getCurrentUser().getStr("id"));
		map.put("user_name", getCurrentUser().getStr("name"));
		
		map.put("mgrNames", getPara("mgr_names"));
		map.put("mgrIds", getPara("mgr_ids"));
		map.put("claimids", getPara("claimids"));
		map.put("claimProps", getPara("claim_props"));
		map.put("clas", getPara("clas"));
		
		pccmMyCustServer.transave(map);
		
		renderNull();
	}
	/**
	 * 下载层级汇总数据，保存为excel文件
	 */
	@LogBind(menuname = "下载层级汇总", type = "7", remark = "下载汇总-下载")
	public void downClasSummary() {

		String userId=getCurrentUser().getStr("id");
		// 从数据库查询指定条数的用户记录
		List<Record> lr =pccmMyCustServer.clasFiveList(userId);
		Record re = new Record();
		Map<String, Object> map = null;
		double incomday=0;
		double finaday=0;
		double cnt=0.00;//加权合计
		int newCnt=0;
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				incomday = BigDecimal.valueOf(incomday).
						add(lr.get(i).getBigDecimal("incomday")).doubleValue();
				finaday = BigDecimal.valueOf(finaday).
						add(lr.get(i).getBigDecimal("finaday")).doubleValue();
				//cnt = cnt+(lr.get(i).getBigDecimal("cnt").intValue())*(AppUtils.getFiveParam((String)lr.get(i).getStr("clas_five")));
				cnt = cnt+(lr.get(i).getBigDecimal("level_weight").doubleValue());
				newCnt = newCnt+lr.get(i).getBigDecimal("newCnt").intValue();
				lr.get(i).set("rn", i+2);
				lr.get(i).set("flag", 1);
				
//				map = new HashMap<String, Object>();
//				map.put("clas_five", lr.get(i).getStr("clas_five"));
//				map.put("userId", getCurrentUser().getStr("id"));
//				map.put("flag", 1);
//				List<Record> r =pccmMyCustServer.myCustList(map);
//				if(null!=r&&r.size()>0){
//					for(int j=0;j<r.size();j++){
//						incomday = BigDecimal.valueOf(incomday).
//								add(BigDecimal.valueOf(r.get(j).getDouble("incomday"))).doubleValue();
//						finaday = BigDecimal.valueOf(finaday).
//								add(BigDecimal.valueOf(r.get(j).getDouble("finaday"))).doubleValue();
//					}
//				}
			}
			re.set("clas_five_cn", "加权合计");
			re.set("rn", 1);
			re.set("cnt", String.format("%.2f", cnt));
			re.set("newCnt", newCnt);
			re.set("incomday", String.format("%.2f",incomday));
			re.set("finaday", String.format("%.2f",finaday));
			re.set("flag", 1);
			lr.add(0, re);
		}
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
		String[] headers = { "clas_five_cn", "cnt", "incomday","finaday","newcnt"};

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "客户层级", "客户数", "日均存款总额","日均理财总额","新开客户数"};

		String fileName = "";
		String xlsName = "客户层级汇总列表.xls";
		try {
			fileName = new String(xlsName.getBytes("GB2312"), "ISO-8859-1");
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
	 * 下载类型汇总数据，保存为excel文件
	 */
	@LogBind(menuname = "下载类型汇总", type = "7", remark = "下载类型汇总-下载")
	public void downTypeSummary() {

		String userId=getCurrentUser().getStr("id");
		// 从数据库查询指定条数的用户记录
		List<Record> lr =pccmMyCustServer.custClasList(userId);
		Record re = new Record();
		int cnt=0;
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				cnt = cnt+lr.get(i).getBigDecimal("cnt").intValue();
				lr.get(i).set("rn", i+2);
				lr.get(i).set("flag", "存量客户".equals(lr.get(i).get("incflg_cn"))?"1":"2");
			}
			re.set("incflg_cn", "合计");
			re.set("rn", 1);
			re.set("cnt", cnt);
			re.set("flag", 3);
			lr.add(0,re);
		}
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
		String[] headers = {"incflg_cn", "cnt"};

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "客户来源", "客户数"};

		String fileName = "";
		String xlsName = "客户来源汇总列表.xls";
		try {
			fileName = new String(xlsName.getBytes("GB2312"), "ISO-8859-1");
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
	 * 下载用户数据，保存为excel文件
	 */
	@LogBind(menuname = "明细下载", type = "7", remark = "明细-下载")
	public void downdetail() {

		Map<String, Object> map = new HashMap<String, Object>();
		
		//获取登陆用户组织代码用于权限控制
		//map.put("orgId", getCurrentUser().getStr("ORG_ID"));
		map.put("userId", getCurrentUser().getStr("id"));
		
		// 获取页面输入查询条件
		map.put("cust_no", getPara("cust_no"));
		map.put("name", getPara("name"));
		map.put("clas_five", getPara("clas_five"));
		map.put("cust_type", getPara("cust_type"));
		map.put("clas_click", getPara("clas_click"));
		map.put("type_click", getPara("type_click"));
		map.put("flag", getPara("flag"));

		List<Record> lr = pccmMyCustServer.myCustList(map);
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
		
		if("1".equals(getPara("flag"))){
			// 指定用哪些查询出来的字段填充excel文件
			String[] headers = { "resp_center_name", "customername","customercode", "clas_five_cn","incompoint","incomday",
					"ilpoint","ilday","busi_inc","product_prop" };

			// excel文件的每列的名称,顺序与填充字段的顺序保持一致
			String[] columns = { "责任中心名称","客户名称","客户号","五层分类", "存款时点余额", "存款日均余额","存贷比（时点）","存贷比（日均）","营收",
					"产品覆盖率"};
			String fileName = "";
			String xlsName = "我的客户明细列表.xls";
			try {
				fileName = new String(xlsName.getBytes("GB2312"), "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				log.error("userctl-download", e);
			}

			// 转换成excel
			ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
			er.render();
		}else if("2".equals(getPara("flag"))){
			// 指定用哪些查询出来的字段填充excel文件
			String[] headers = { "resp_center_name", "customername","dummy_cust_no","relate_cust_name","relate_mgr_name",
					"relate_mgr_mobile"};

			// excel文件的每列的名称,顺序与填充字段的顺序保持一致
			String[] columns = { "责任中心名称","客户名称","虚拟客户号", "关联客户名称", "关联客户经理","关联客户经理联系方式"};
			String fileName = "";
			String xlsName = "我的客户明细列表.xls";
			try {
				fileName = new String(xlsName.getBytes("GB2312"), "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				log.error("userctl-download", e);
			}

			// 转换成excel
			ExcelRender er = new ExcelRender(fileName, columns, headers, lr, getResponse());
			er.render();
		}
		// 打印日志
		log.info("download--lr:" + lr);
		renderNull();
	}
	
	/***20180623*********************************************************************************************************/
	/**
	 * 五层分类列表查询
	 */
	public void clasFiveList() {
		String userId=getCurrentUser().getStr("id");
		// 从数据库查询指定条数的用户记录
		List<Record> lr =pccmMyCustServer.clasFiveList(userId);
		Record re = new Record();
		Map<String, Object> map = null;
		double incomday=0;
		double finaday=0;
		double cnt=0.00;//加权合计
		int newCnt=0;
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				incomday = BigDecimal.valueOf(incomday).
						add(lr.get(i).getBigDecimal("incomday")).doubleValue();
				finaday = BigDecimal.valueOf(finaday).
						add(lr.get(i).getBigDecimal("finaday")).doubleValue();
				//cnt = cnt+(lr.get(i).getBigDecimal("cnt").intValue())*(AppUtils.getFiveParam((String)lr.get(i).getStr("clas_five")));
				cnt = cnt+(lr.get(i).getBigDecimal("level_weight").doubleValue());
				newCnt = newCnt+lr.get(i).getBigDecimal("newCnt").intValue();
				lr.get(i).set("rn", i+2);
				lr.get(i).set("flag", 1);
				
//				map = new HashMap<String, Object>();
//				map.put("clas_five", lr.get(i).getStr("clas_five"));
//				map.put("userId", getCurrentUser().getStr("id"));
//				map.put("flag", 1);
//				List<Record> r =pccmMyCustServer.myCustList(map);
//				if(null!=r&&r.size()>0){
//					for(int j=0;j<r.size();j++){
//						incomday = BigDecimal.valueOf(incomday).
//								add(BigDecimal.valueOf(r.get(j).getDouble("incomday"))).doubleValue();
//						finaday = BigDecimal.valueOf(finaday).
//								add(BigDecimal.valueOf(r.get(j).getDouble("finaday"))).doubleValue();
//					}
//				}
			}
			re.set("clas_five_cn", "加权合计");
			re.set("rn", 1);
			re.set("cnt", String.format("%.2f", cnt));
			re.set("newCnt", newCnt);
			re.set("incomday", String.format("%.2f",incomday));
			re.set("finaday", String.format("%.2f",finaday));
			re.set("flag", 1);
			lr.add(0, re);
		}
		
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", lr);
		// 打印日志
		log.info("clasFiveList--r.getList():" + lr);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 客户分类列表查询
	 */
	public void custClasList() {
		String userId=getCurrentUser().getStr("id");
		// 从数据库查询指定条数的用户记录
		List<Record> lr =pccmMyCustServer.custClasList(userId);
		Record re = new Record();
		int cnt=0;
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				cnt = cnt+lr.get(i).getBigDecimal("cnt").intValue();
				lr.get(i).set("rn", i+2);
				lr.get(i).set("flag", 2);
			}
			re.set("incflg_cn", "合计");
			re.set("rn", 1);
			re.set("cnt", cnt);
			re.set("flag", 2);
			lr.add(0,re);
		}
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", lr);
		// 打印日志
		log.info("clasFiveList--r.getList():" + lr);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 我的客户详情列表
	 */
	public void myCustPage(){
		 Map<String, Object> map = new HashMap<String, Object>();
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		
		//获取登陆用户组织代码用于权限控制
		//map.put("orgId", getCurrentUser().getStr("ORG_ID"));
		map.put("userId", getCurrentUser().getStr("id"));
		
		// 获取页面输入查询条件
		map.put("cust_no", getPara("cust_no"));
		map.put("name", getPara("name"));
		map.put("clas_five", getPara("clas_five"));
		map.put("cust_type", getPara("cust_type"));
		map.put("clas_click", getPara("clas_click"));
		map.put("type_click", getPara("type_click"));
		map.put("flag", getPara("flag"));
		
		// 从数据库查询指定条数的用户记录
		Page<Record> r =pccmMyCustServer.myCustPage(map);
		
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
	 * 产品覆盖率页面初始化
	 */
	public void detail() {
		render("detail.jsp");
	}
	
	/**
	 * 产品覆盖率查询
	 */
	public void productdetail() {
		 String pool_id=getPara("id");
		 String customercode=getPara("customercode");
		 String orgnum=getPara("orgnum");
		// 从数据库查询指定条数的用户记录
		//Record r =pccmMyCustServer.custdetail(pool_id);
		Record r =pccmMyCustServer.custdetail(customercode,orgnum);
		
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r);
		// 打印日志
		log.info("clasFiveList--r.getList():" + r);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 饼图
	 */
	public void pieData(){
		
		String userId=getCurrentUser().getStr("id");
		
		// 从数据库查询指定条数的记录
		List<Record> lr = pccmMyCustServer.clasFiveList(userId);;
		JSONArray parray = new JSONArray();
		String name = null;
		double value = 0;
		String data = null;
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				name = lr.get(i).getStr("clas_five_cn");
				value = lr.get(i).getBigDecimal("cnt").doubleValue();
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
	 * 环形图
	 */
	public void ringData(){
		
		String userId=getCurrentUser().getStr("id");
		
		// 从数据库查询指定条数的记录
		List<Record> lr = pccmMyCustServer.custClasList(userId);;
		JSONArray parray = new JSONArray();
		String name = null;
		double value = 0;
		String data = null;
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				name = lr.get(i).getStr("incflg_cn");
				value = lr.get(i).getBigDecimal("cnt").doubleValue();
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
	 * 五层分类说明
	 */
	public void clasFiveText() {
		String text = "五层分类说明（单位：万元）：<br/>";
		List<Record> lr = pccmMyCustServer.clasFiveText();
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				text += lr.get(i).getStr("clas_five_name")+"：";
				if(AppUtils.StringUtil(lr.get(i).getStr("symbol1")) != null){
					text += lr.get(i).getBigDecimal("condition_val1")+lr.get(i).getStr("symbol1").replace(">", "<");
				}
				text += "日均存款及表内外理财日均合计";
				if(AppUtils.StringUtil(lr.get(i).getStr("symbol2")) != null){
					text += lr.get(i).getStr("symbol2")+lr.get(i).getBigDecimal("condition_val2");
				}
				text += "<br/>";
			}
		}
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("text", text);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 最新客户入池数据时间
	 */
	public void findNewDate(){
		
		
		// 获取查询参数
		String userId=getCurrentUser().getStr("id");
		// 从数据库查询指定条数的用户记录
		String newDate =pccmMyCustServer.findNewDate(userId);
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("newDate", newDate);

		renderJson();
	}
	
	/**
	 * 用户当前组织成员下拉框数据
	 */
	public void getOrgUser() {
		String user_no=getPara("user_no");
		String name=getPara("name");
		// 获取当前用户信息
		String orgId = getCurrentUser().getStr("ORG_ID");
		String upOrg = null;
		String orgArr = null;
		// 判断是否省本部
		if(AppUtils.isProv(orgId)){
			orgArr = null;
		}else{
			upOrg = AppUtils.getUpOrg(orgId, "2");
			orgArr = AppUtils.getOrgStr(upOrg,"2");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		
		map.put("user_no", user_no);
		map.put("name", name);
		map.put("orgArr", orgArr);
		
		// 从数据库查询指定条数的用户记录
		Page<Record> r =pccmMyCustServer.getOrgUser(map);

		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 创建下载任务
	 */
	@LogBind(menuname = "明细下载", type = "7", remark = "明细-下载")
	public void downTask() {
		String org =  null;
		String flg =  getPara("flg");
		String downFlag = "";
		if("1".equals(flg)){
			downFlag =  "SQL_MY_CUST_MY";
		}else if("2".equals(flg)){
			downFlag =  "SQL_MY_CUST_OTHER";
		}
		String user_no = getCurrentUser().getStr("ID");
		String user_name = getCurrentUser().getStr("NAME");
		//创建下载任务
		Map<String, Object> taskmap = new HashMap<String, Object>();
		taskmap.put("org", org);
		taskmap.put("downFlag", downFlag);
		taskmap.put("user_no", user_no);
		taskmap.put("user_name", user_name);
		String rs = DownDetailUtil.DownTask(taskmap);
		setAttr("msg", rs);
		// 返回json数据
		renderJson();
	}
	
	
}
