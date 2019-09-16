package com.goodcol.controller.zxglctl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.server.zxglserver.ValidCustServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.anatation.LogBind;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.zxgldbutil.DownDetailUtil;

@RouteBind(path = "/validCust")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class ValidCustController extends BaseCtl {

	private ValidCustServer server = new ValidCustServer();

	@Override
	public void index() {
		// setAttr("month", getMonth());
		//		String sql = "select * from ("
		//				+ "select row_number() over (order by pa_date desc) rn,pa_date "
		//				+ "from pccm_cust_base_info group by pa_date) m where rn= 1 or rn = 2";
		//		List<Record> list = Db.use("gbase").find(sql);
		//		if (null!=list && list.size() > 0) {
		//			if (list.size() == 2) {
		//				setAttr("month", list.get(0).get("pa_date"));
		//				setAttr("lastMonth", list.get(1).get("pa_date"));
		//			}else if(list.size() == 1){
		//				setAttr("month", list.get(0).get("pa_date"));
		//				setAttr("lastMonth", DateTimeUtil.geYesterDay(-5));
		//			}else{
		//				setAttr("month", DateTimeUtil.getPathName());
		//				setAttr("lastMonth", DateTimeUtil.geYesterDay(-5));
		//			}
		//		}
		String sql = " select max(data_month) as data_month from PCCM_VALID_CUST_COUNT";
		Record record = Db.use("default").findFirst(sql);
		String month = record.getStr("data_month");
		setAttr("lastMonth", DateTimeUtil.geYesterDay(-5));
		if (StringUtils.isNotBlank(month)) {
			setAttr("month", month);
		} else {
			setAttr("month", DateTimeUtil.getPathName());
		}
		render("index.jsp");
	}

	/**
	 * 客户信息列表
	 */
	public void getValidCustCountList(){
		String orgNum = getPara("orgNum");
		String month = getPara("month");
		String paDate = DateTimeUtil.changeDateString(getPara("pa_date"));
		if (StringUtils.isNotBlank(paDate)) {
			month = paDate;
		}
		String lastMonth = getPara("lastMonth");
		List<Record> list = server.getValidCustCountList(orgNum, month, lastMonth);
		renderJson(list);
	}

	/**
	 * 有效户详情列表
	 */
	public void getCustList(){
		String name = getPara("name");
		String cust_no = getPara("cust_no");
		String orgNum = getPara("orgNum");
		String month = getPara("month");
		String paDate = DateTimeUtil.changeDateString(getPara("pa_date"));
		if (StringUtils.isNotBlank(paDate) && !month.equals(paDate)) {
			renderNull();
			return;
		}
		String deptLevel = getPara("deptLevel");
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		Page<Record> page = server.getCustList(pageNum, pageSize,month, orgNum,name, cust_no, deptLevel);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	/**
	 * 获取统计月
	 * @return
	 */
	/*private static String getMonth(){
		int days =  DateTimeUtil.getDayOfMonth();
		String month = DateTimeUtil.geYesterDay(DateTimeUtil.getDayOfMonth()); // 上月末 YYYYMMDD
		if (days <= 5) {
			Calendar cal = Calendar.getInstance();
			int month2 = DateTimeUtil.getMonth();
			cal.set(Calendar.YEAR, Integer.parseInt(DateTimeUtil.getYear()));
			cal.set(Calendar.MONTH, month2- 2 -1);
			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DAY_OF_MONTH, lastDay);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			month = sdf.format(cal.getTime());;
		}
		return month;
	}*/
	
	public void indexpa() {
		//		String sql = "select * from ("
		//				+ "select row_number() over (order by pa_date desc) rn,pa_date "
		//				+ "from pccm_cust_base_info group by pa_date) m where rn= 1 or rn = 2";
		//		List<Record> list = Db.use("gbase").find(sql);
		//		if (null!=list && list.size() > 0) {
		//			if (list.size() == 2) {
		//				setAttr("month", list.get(0).get("pa_date"));
		//				setAttr("lastMonth", list.get(1).get("pa_date"));
		//			}else if(list.size() == 1){
		//				setAttr("month", list.get(0).get("pa_date"));
		//				setAttr("lastMonth", DateTimeUtil.geYesterDay(-5));
		//			}else{
		//				setAttr("month", DateTimeUtil.getPathName());
		//				setAttr("lastMonth", DateTimeUtil.geYesterDay(-5));
		//			}
		//		}
		String sql = " select max(data_month) as data_month from pccm_pa_not_zero";
		Record record = Db.use("default").findFirst(sql);
		String month = record.getStr("data_month");
		setAttr("lastMonth", DateTimeUtil.geYesterDay(-5));
		if (StringUtils.isNotBlank(month)) {
			setAttr("month", month);
		} else {
			setAttr("month", DateTimeUtil.getPathName());
		}
		render("indexpa.jsp");
	}
	
	/**
	 * 根据机构id获取PA不为零统计表
	 * 2018年8月3日12:55:53
	 * @author liutao
	 * @return
	 */
	public void findValidCustNumByOrgId(){
		String org_id = getPara("org_id");
		String month = getPara("month");
		String paDate = DateTimeUtil.changeDateString(getPara("pa_date"));
		if (StringUtils.isNotBlank(paDate)) {
			month = paDate;
		}
		String lastMonth = getPara("lastMonth");
		Map<String, String> map = new HashMap<String, String>();
		map.put("month", month);
		map.put("last_month", lastMonth);
		map.put("org_id", org_id);
		List<Record> list = server.findValidCustNumByOrgId(map);
		setAttr("data", list);
		renderJson();
	}
	
	/**
	 * 根据机构号获取当前PA不为零客户数据
	 * 2018年8月9日18:55:12
	 * @author liutao 
	 * @param map
	 * @return
	 */
	public void findValidCustInfoByOrgId(){
		String org_id = getPara("org_id");
		String name = getPara("cust_name");
		String cust_no = getPara("cust_no");
		String month = getPara("month");
		String paDate = DateTimeUtil.changeDateString(getPara("pa_date"));
		if (StringUtils.isNotBlank(paDate) && !month.equals(paDate)) {
			renderNull();
			return;
		}
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		Map<String, String> map = new HashMap<String, String>();
		map.put("month", month);
		map.put("name", name);
		map.put("org_id", org_id);
		map.put("pageNum", pageNum + "");
		map.put("pageSize", pageSize + "");
		map.put("cust_no", cust_no);
		Page<Record> page = server.findValidCustInfoByOrgId(map);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}	
	
	/**
	 * 下载PA不为零客户数据
	 * 2018年8月15日16:24:46
	 * @author liutao
	 */
	@Before(PermissionInterceptor.class)
	public void downloadOrgPaNotZero(){
		String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		String orgId = getCurrentUser().getStr("ORG_ID");
		String roleLevel = AppUtils.getLevByRole(roleNames);
		String orgNum = AppUtils.getUpOrg(orgId, roleLevel);// 最大权限的机构号	
		String month = getPara("month");
		String paDate = DateTimeUtil.changeDateString(getPara("pa_date"));
		if (StringUtils.isNotBlank(paDate)) {
			month = paDate;
		}
		String contentPath = getRequest().getSession().getServletContext().getRealPath("/");
		//获取PA不为零Excel模板文件
		String excelModelPath = contentPath + "/upload/download/pa_not_zero.xls";
		String outPutFilePath = contentPath + "/upload/download/PA不为零客户数据_" + month + ".xls";
		try {
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(excelModelPath)));
			FileOutputStream out = new FileOutputStream(outPutFilePath);
			HSSFSheet sheet = wb.getSheetAt(0);
			//查询指定日期的PA不为零数据
			List<Record> list = server.findDownloadOrgPaNotZero(month, orgNum);
			if(null != list && list.size() > 0){
				for (int i = 0; i < list.size(); i++) {
					HSSFRow row = sheet.getRow(i + 3);
					if(null == row){
						row = sheet.createRow(i + 3);
					}
					Record r = list.get(i);
					row.createCell(0).setCellValue(r.getStr("orgname"));
					row.createCell(1).setCellValue(r.getBigDecimal("lastYearSumNum") + "");
					row.createCell(2).setCellValue(r.getBigDecimal("lastYearNum") + "");
					row.createCell(3).setCellValue(r.getBigDecimal("monthSumNum") + "");
					row.createCell(4).setCellValue(r.getBigDecimal("monthNum") + "");
					row.createCell(5).setCellValue(r.getBigDecimal("incYearSumNum") + "");
					row.createCell(6).setCellValue(r.getBigDecimal("ampliYearSumNum").multiply(new BigDecimal(100)).doubleValue() + "%");
					row.createCell(7).setCellValue(r.getBigDecimal("incYearNum") + "");
					row.createCell(8).setCellValue(r.getBigDecimal("ampliYearNum").multiply(new BigDecimal(100)).doubleValue() + "%");
					row.createCell(9).setCellValue(r.getBigDecimal("incMonthSumNum") + "");
					row.createCell(10).setCellValue(r.getBigDecimal("ampliLastMonthSumNum").multiply(new BigDecimal(100)).doubleValue() + "%");
					row.createCell(11).setCellValue(r.getBigDecimal("incMonthNum") + "");
					row.createCell(12).setCellValue(r.getBigDecimal("ampliLastMonthNum").multiply(new BigDecimal(100)).doubleValue() + "%");
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
	 * 有效户统计下载
	 */
	@Before(PermissionInterceptor.class)//拦截器
	public void downloadValidCustCount(){
		//用户角色
		String roleNames = AppUtils.getRoleNames(getCurrentUser().getStr("ID"));
		//获取当前机构号
		String orgId = getCurrentUser().getStr("ORG_ID");
		//获取当前用户最高等级权限
		String roleLevel = AppUtils.getLevByRole(roleNames);
		// 最大权限的机构号	
		String orgNum = AppUtils.getUpOrg(orgId, roleLevel);
		String month = getPara("month");
		String paDate = DateTimeUtil.changeDateString(getPara("pa_date"));
		if (StringUtils.isNotBlank(paDate)) {
			month = paDate;
		}
		String contentPath = getRequest().getSession().getServletContext().getRealPath("/");
		//获取PA不为零Excel模板文件
		String excelModelPath = contentPath + "/upload/download/valid_cust_count.xls";
		String outPutFilePath = contentPath + "/upload/download/有效户客户数据_" + month + ".xls";
		try {
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(new File(excelModelPath)));
			FileOutputStream out = new FileOutputStream(outPutFilePath);
			HSSFSheet sheet = wb.getSheetAt(0);
			//查询指定日期的PA不为零数据
			List<Record> list = server.downLoadValidCustCountList(month, orgNum);
			if(null != list && list.size() > 0){
				for (int i = 0; i < list.size(); i++) {
					HSSFRow row = sheet.getRow(i + 3);
					if(null == row){
						row = sheet.createRow(i + 3);
					}
					Record r = list.get(i);
					row.createCell(0).setCellValue(r.getStr("orgname"));
					row.createCell(1).setCellValue(r.getBigDecimal("base_count").longValue());
					row.createCell(2).setCellValue(r.getBigDecimal("year_valid_count").longValue());
					row.createCell(3).setCellValue(r.getBigDecimal("year_pa").longValue());
					row.createCell(4).setCellValue(r.getBigDecimal("year_ave_count").longValue());
					row.createCell(5).setCellValue(r.getBigDecimal("month_count").longValue());
					row.createCell(6).setCellValue(r.getBigDecimal("month_valid_count").longValue());
					row.createCell(7).setCellValue(r.getBigDecimal("month_pa").longValue());
					row.createCell(8).setCellValue(r.getBigDecimal("month_ave_count").longValue());
					row.createCell(9).setCellValue(r.getBigDecimal("month_up").longValue() + "");
					row.createCell(10).setCellValue(format(r.getBigDecimal("month_amp")) + "%");
					row.createCell(11).setCellValue(r.getBigDecimal("month_valid_up").longValue());
					row.createCell(12).setCellValue(format(r.getBigDecimal("month_valid_amp")) + "%");
					row.createCell(13).setCellValue(r.getBigDecimal("month_pa_up").longValue());
					row.createCell(14).setCellValue(format(r.getBigDecimal("month_pa_amp")) + "%");
					row.createCell(15).setCellValue(r.getBigDecimal("month_ave_up").longValue());
					row.createCell(16).setCellValue(format(r.getBigDecimal("month_ave_amp")) + "%");
					row.createCell(17).setCellValue(r.getBigDecimal("last_month_up").longValue());
					row.createCell(18).setCellValue(format(r.getBigDecimal("last_month_amp")) + "%");
					row.createCell(19).setCellValue(r.getBigDecimal("last_month_valid_up").longValue());
					row.createCell(20).setCellValue(format(r.getBigDecimal("last_month_valid_amp")) + "%");
					row.createCell(21).setCellValue(r.getBigDecimal("last_month_pa_up").longValue());
					row.createCell(22).setCellValue(format(r.getBigDecimal("last_month_pa_amp")) + "%");
					row.createCell(23).setCellValue(r.getBigDecimal("last_month_ave_up").longValue());
					row.createCell(24).setCellValue(format(r.getBigDecimal("last_month_ave_amp")) + "%");
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
	 * BigDecimal格式化
	 * @param bigDecimal
	 * @return
	 */
	private static String format(BigDecimal bigDecimal){
		DecimalFormat format = new DecimalFormat("0.##");
		return format.format(bigDecimal.doubleValue()*100);
	}

	/**
	 * 有效户数据日期
	 */
	public void validCustCountDate() {
		renderJson(server.validCustCountDate());
	}
	
	/**
	 * PA不为0数据日期
	 */
	public void validCustPaDate(){
		renderJson(server.validCustPaDate());
	}
	
	/**
	 * 创建下载任务
	 */
	@LogBind(menuname = "明细下载", type = "7", remark = "明细-下载")
	public void downTask() {
		String org =  getPara("upOrg");
		String flg =  getPara("flg");
		String paDate = getPara("pa_date");
		String downFlag = "";
		if("1".equals(flg)){
			downFlag =  "SQL_CUST_PA";
		}else if("2".equals(flg)){
			downFlag =  "SQL_CUST_VALID";
		}
		Map<String, Object> taskmap = new HashMap<String, Object>();
		taskmap.put("org", org);
		taskmap.put("downFlag", downFlag);
		
		String rs = DownDetailUtil.DownTask(taskmap);
		setAttr("msg", rs);
		// 返回json数据
		renderJson();
	}
	
}
