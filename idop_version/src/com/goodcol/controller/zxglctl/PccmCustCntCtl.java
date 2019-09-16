package com.goodcol.controller.zxglctl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.PccmCustCntServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;
import com.goodcol.util.zxgldbutil.DownDetailUtil;

/**
 * 客户统计控制层
 * 
 * @author start
 * 
 */
@RouteBind(path = "/zxCustCnt")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class PccmCustCntCtl extends BaseCtl {
	
	public static Logger log = Logger.getLogger(PccmCustCntCtl.class);
	PccmCustCntServer pccmCustCntServer = new PccmCustCntServer();
	
	/**
	 * 主页面
	 */
	public void index() {
		
		String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		String orgId = getCurrentUser().getStr("ORG_ID");
		String roleLevel = AppUtils.getLevByRole(roleNames);
		String upOrg = AppUtils.getUpOrg(orgId, roleLevel);		
		
		//获取登陆用户组织代码
		setAttr("upOrg", upOrg);
		setAttr("roleLevel", roleLevel);
		render("index.jsp");
	}
	
	/**
	 * 客户统计列表查询-按组织
	 */
	public void custByOrgList(){
		String upOrg= getPara("upOrg");
		String roleLevel= getPara("roleLevel");
		String dataDate = DateTimeUtil.changeDateString(getPara("data_date"));
		List<Record> lr =pccmCustCntServer.custByOrgList(upOrg,roleLevel,dataDate);
		//List<Record> lr =pccmCustCntServer.custByOrgNewList(upOrg);
		
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", lr);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 客户统计列表查询-按组织-五层分类饼图
	 */
	public void pieData(){
		String upOrg=getPara("upOrg");
		String roleLevel=getPara("roleLevel");
		String dataDate = DateTimeUtil.changeDateString(getPara("dataDate"));
		List<Record> lr =pccmCustCntServer.custByFiveList(upOrg,roleLevel,dataDate);
		JSONArray parray = new JSONArray();
//		String name = null;
//		long value = 0;
//		String data = null;
//		if(null!=lr&&lr.size()>0){
//			for(int i=0;i<lr.size();i++){
//				name = lr.get(i).getStr("clas_five_cn");
////				value = lr.get(i).getLong("cnt");
//				value = lr.get(i).getBigDecimal("cnt").longValue();
//				if(!roleLevel.equals(lr.get(i).getStr("by2"))){
//					data ="{value:"+value+",name:'"+name+"'}";
//					parray.add(data);
//				}
//			}
//		}
		if(null!=lr&&lr.size()>0){
			parray.add("{value:"+lr.get(0).getBigDecimal("month_five_cnt").longValue()+",name:'五层'}");
			parray.add("{value:"+lr.get(0).getBigDecimal("month_four_cnt").longValue()+",name:'四层'}");
			parray.add("{value:"+lr.get(0).getBigDecimal("month_three_cnt").longValue()+",name:'三层'}");
			parray.add("{value:"+lr.get(0).getBigDecimal("month_two_cnt").longValue()+",name:'二层'}");
			parray.add("{value:"+lr.get(0).getBigDecimal("month_one_cnt").longValue()+",name:'一层'}");
		}	
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("pdata", parray);
		
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 客户统计列表查询-按类别
	 */
	public void custByTypeList(){
		String upOrg=getPara("upOrg");
		String roleLevel=getPara("roleLevel");
		List<Record> lr =pccmCustCntServer.custByTypeList(upOrg,roleLevel);
		Record re = new Record();
		long cnt=0;
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				//cnt = cnt+lr.get(i).getLong("cnt");
				cnt = cnt+lr.get(i).getBigDecimal("cnt").longValue();
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
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 客户统计列表查询-按类别-环形图
	 */
	public void ringData(){
		String upOrg=getPara("upOrg");
		String roleLevel=getPara("roleLevel");
		List<Record> lr =pccmCustCntServer.custByTypeList(upOrg,roleLevel);
		
		JSONArray parray = new JSONArray();
		String name = null;
		long value = 0;
		String data = null;
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				name = lr.get(i).getStr("incflg_cn");
				value = lr.get(i).getBigDecimal("cnt").longValue();
				//value = lr.get(i).getLong("cnt");
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
	 * 最新客户入池数据时间
	 */
	public void findNewDate(){
		// 从数据库查询指定条数的用户记录
		//String newDate =pccmCustCntServer.findNewDate();
		String newDate =AppUtils.findGNewDate();
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("newDate", newDate);

		renderJson();
	}
	
	/**
	 * 
	 */
	public void custDetailPage(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		map.put("pageNum", pageNum);
		map.put("pageSize", pageSize);
		
		String dataDate = DateTimeUtil.changeDateString(getPara("data_date"));
		String maxDate = AppUtils.findCustCountMaxDate();
		
		if (StringUtils.isNotBlank(dataDate) && !maxDate.equals(dataDate)) {
			renderNull();
			return;
		}
		// 获取页面输入查询条件
		map.put("cust_no", getPara("cust_no"));
		map.put("name", getPara("name"));
		map.put("clas_five", getPara("clas_five"));
		map.put("cust_type", getPara("cust_type"));
		
		map.put("clas_click", getPara("clas_click"));
		map.put("type_click", getPara("type_click"));
		map.put("flag", getPara("flag"));
		map.put("roleLevel", getPara("roleLevel"));
		map.put("orgNum", getPara("upOrg"));
		map.put("upOrg", AppUtils.getOrgStr(getPara("upOrg"),"2"));
		map.put("areaOrg", AppUtils.getAreaStr(getPara("upOrg")));
		map.put("orgId", getPara("upOrg"));
		
		// 从数据库查询指定条数的用户记录
		Page<Record> r =pccmCustCntServer.custCntPage(map);
		
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
	 * 下载组织汇总数据，保存为excel文件
	 */
	@LogBind(menuname = "下载组织汇总", type = "7", remark = "下载汇总-下载")
	public void downClasSummary() {

		String upOrg=getPara("upOrg");
		String roleLevel=getPara("roleLevel");
		// 从数据库查询指定条数的用户记录
		List<Record> lr =pccmCustCntServer.downloadCustByOrgList(upOrg,roleLevel);
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
		String[] headers = { "orgname", "level_weight", "newcnt"};

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "机构名称", "加权客户数", "新增客户数"};

		String fileName = "";
		String xlsName = "客户组织汇总列表.xls";
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

		String upOrg=getPara("upOrg");
		String roleLevel=getPara("roleLevel");
		List<Record> lr =pccmCustCntServer.custByTypeList(upOrg,roleLevel);
		Record re = new Record();
		long cnt=0;
		if(null!=lr&&lr.size()>0){
			for(int i=0;i<lr.size();i++){
				//cnt = cnt+lr.get(i).getLong("cnt");
				cnt = lr.get(i).getBigDecimal("cnt").longValue();
				lr.get(i).set("rn", i+2);
				lr.get(i).set("flag", 2);
			}
			re.set("incflg_cn", "合计");
			re.set("rn", 1);
			re.set("cnt", cnt);
			re.set("flag", 2);
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
		String[] columns = {"客户来源", "客户数"};

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
		
		// 获取页面输入查询条件
		map.put("cust_no", getPara("cust_no"));
		map.put("name", getPara("name"));
		map.put("clas_five", getPara("clas_five"));
		map.put("cust_type", getPara("cust_type"));
		
		map.put("type_click", getPara("type_click"));
		map.put("flag", getPara("flag"));
		map.put("roleLevel", getPara("roleLevel"));
		map.put("upOrg", getPara("upOrg"));
		
		// 从数据库查询指定条数的用户记录
		List<Record> lr = pccmCustCntServer.custCntList(map);
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
			String xlsName = "客户明细列表.xls";
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
			String xlsName = "客户明细列表.xls";
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
	
	/**
	 * 下载层级汇总
	 * 2018年9月8日
	 */
	public void downloadOrgList(){
		String upOrg=getPara("upOrg");
		String dataDate= DateTimeUtil.changeDateString(getPara("dataDate"));
		if (StringUtils.isBlank(dataDate)) {
			dataDate = AppUtils.findCustCountMaxDate();
		}
		String contentPath = getRequest().getSession().getServletContext().getRealPath("/");
		//获取PA不为零Excel模板文件
		String excelModelPath = contentPath + "/upload/download/cust_count.xls";
		String outPutFilePath = contentPath + "/upload/download/五层加权客户统计信息_"+dataDate+".xls";
		try {
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(excelModelPath)));
			FileOutputStream out = new FileOutputStream(outPutFilePath);
			HSSFSheet sheet = wb.getSheetAt(0);
			//查询指定日期的PA不为零数据
			List<Record> list = pccmCustCntServer.downloadCustByOrgNewList(upOrg,dataDate);
			if(null != list && list.size() > 0){
				for (int i = 0; i < list.size(); i++) {
					HSSFRow row = sheet.getRow(i + 3);
					if(null == row){
						row = sheet.createRow(i + 3);
					}
					Record r = list.get(i);
					row.createCell(0).setCellValue(r.getStr("orgname"));
					row.createCell(1).setCellValue(r.getBigDecimal("last_year_five_cnt").intValue() + "");
					row.createCell(2).setCellValue(r.getBigDecimal("last_year_four_cnt").intValue() + "");
					row.createCell(3).setCellValue(r.getBigDecimal("last_year_three_cnt").intValue() + "");
					row.createCell(4).setCellValue(r.getBigDecimal("last_year_two_cnt").intValue() + "");
					row.createCell(5).setCellValue(r.getBigDecimal("last_year_one_cnt").intValue() + "");
					row.createCell(6).setCellValue(r.getBigDecimal("last_year_add_cnt").doubleValue() + "");
					row.createCell(7).setCellValue(r.getBigDecimal("month_five_cnt").intValue() + "");
					row.createCell(8).setCellValue(r.getBigDecimal("month_four_cnt").intValue() + "");
					row.createCell(9).setCellValue(r.getBigDecimal("month_three_cnt").intValue() + "");
					row.createCell(10).setCellValue(r.getBigDecimal("month_two_cnt").intValue() + "");
					row.createCell(11).setCellValue(r.getBigDecimal("month_one_cnt").intValue() + "");
					row.createCell(12).setCellValue(r.getBigDecimal("month_add_cnt").doubleValue() + "");
					row.createCell(13).setCellValue(r.getBigDecimal("last_year_five_up").intValue() + "");
					row.createCell(14).setCellValue(r.getBigDecimal("last_year_five_amp").multiply(new BigDecimal(100)).doubleValue() + "%");
					row.createCell(15).setCellValue(r.getBigDecimal("last_year_four_up").intValue() + "");
					row.createCell(16).setCellValue(r.getBigDecimal("last_year_four_amp").multiply(new BigDecimal(100)).doubleValue() + "%");
					row.createCell(17).setCellValue(r.getBigDecimal("last_year_three_up").intValue() + "");
					row.createCell(18).setCellValue(r.getBigDecimal("last_year_three_amp").multiply(new BigDecimal(100)).doubleValue() + "%");
					row.createCell(19).setCellValue(r.getBigDecimal("last_year_two_up").intValue() + "");
					row.createCell(20).setCellValue(r.getBigDecimal("last_year_two_amp").multiply(new BigDecimal(100)).doubleValue() + "%");
					row.createCell(21).setCellValue(r.getBigDecimal("last_year_one_up").intValue() + "");
					row.createCell(22).setCellValue(r.getBigDecimal("last_year_one_amp").multiply(new BigDecimal(100)).doubleValue() + "%");
					row.createCell(23).setCellValue(r.getBigDecimal("last_year_add_up").intValue() + "");
					row.createCell(24).setCellValue(r.getBigDecimal("last_year_add_amp").multiply(new BigDecimal(100)).doubleValue() + "%");

				}
			}
			wb.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("下载PA不为零数据出现异常!");
		}
		renderFile(new File(outPutFilePath));
	}
	
	/**
	 * 创建下载任务
	 */
	@LogBind(menuname = "明细下载", type = "7", remark = "明细-下载")
	public void downTask() {
		String org =  getPara("upOrg");
		String flg =  getPara("flg");
		String dataDate = DateTimeUtil.changeDateString(getPara("data_date"));
		String downFlag = "";
		if("1".equals(flg)){
			downFlag =  "SQL_CUST_CNT_MY";
		}else if("2".equals(flg)){
			downFlag =  "SQL_CUST_CNT_OTHER";
		}
		
		if (AppUtils.StringUtil(dataDate) == null) {
			dataDate=AppUtils.findGNewDate();
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
