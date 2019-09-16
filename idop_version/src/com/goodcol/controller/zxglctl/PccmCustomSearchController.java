package com.goodcol.controller.zxglctl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.upload.UploadFile;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.OperationLogRecordInterceptor;
import com.goodcol.server.zxglserver.PccmCustomSearchServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.zxgldbutil.CommonUtil;
import com.goodcol.util.zxgldbutil.DownDetailUtil;
import com.goodcol.util.zxgldbutil.JsonToMapTool;

/**
 * 定制查询控制层
 * 
 * @author start
 * @2018-10-22
 */
@RouteBind(path = "/zxCustomSearch")
@Before({ ManagerPowerInterceptor.class, OperationLogRecordInterceptor.class })
public class PccmCustomSearchController extends BaseCtl {

	public static Logger log = Logger.getLogger(PccmCustomSearchController.class);

	private PccmCustomSearchServer server = new PccmCustomSearchServer();

	/**
	 * 技术定制页面跳转
	 */
	public void index() {
		render("tec_index.jsp");
	}
	
	/**
	 * 业务定制页面跳转
	 */
	public void busiIndex() {
		render("busi_index.jsp");
	}
	
	/**
	 * 使用人员定制查询页面跳转
	 */
	public void searchIndex() {
		render("sear_index.jsp");
	}
	
	/**
	 * 技术定制新增编辑页面跳转
	 */
	public void tecForm() {
		render("tec_form.jsp");
	}
	
	/**
	 * 业务定制新增编辑页面跳转
	 */
	public void busiForm() {
		render("busi_form.jsp");
	}
	
	/**
	 * 授权详情页面跳转
	 */
	public void authDetail() {
		render("auth_detail.jsp");
	}
	
	/**
	 * 明细下载页面跳转
	 */
	public void downDetail() {
		render("downDetail.jsp");
	}
	
	/**
	 * 汇总下载页面跳转
	 */
	public void downCount() {
		render("downCount.jsp");
	}

	/**
	 * 分页查询技术定制列表
	 */
	public void getTecList() {
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String tableCnName = getPara("table_cn_name");
		String tableEnName = getPara("table_en_name");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tableCnName", tableCnName);
		paramMap.put("tableEnName", tableEnName);
		Page<Record> r = server.getList(paramMap, pageNum, pageSize);
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 返回json数据
		renderJson();
	}

	/**
	 * 添加基础模板
	 */
	public void addBaseModel() {
		
		String result="success";
		
		try {
			String user_no = getCurrentUser().getStr("ID");
			String pageType = getPara("pageType");
			String base_id = getPara("base_id");
			String tableCnName = getPara("table_cn_name");
			String tableEnName = getPara("table_en_name");
			String dateFiledName = getPara("date_filed_name");
			String orgFiledName = getPara("org_filed_name");
			String licenseStatus = "0";
			String str = getPara("gridData");
			String orgIds = getPara("orgIds");
			
			if (StringUtils.isBlank(orgIds)) {
				licenseStatus = "0"; // 默认未授权状态
			}else{
				licenseStatus = "1";
			}
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("table_cn_name", tableCnName);
			paramMap.put("table_en_name", tableEnName);
			paramMap.put("date_filed_name", dateFiledName);
			paramMap.put("org_filed_name", orgFiledName);
			paramMap.put("license_status", licenseStatus);
			paramMap.put("user_no", user_no);
			Map<String, Object> requestMap = JsonToMapTool.convertJsonToMap("{list:" + str + "}");
			
			//保存/编辑模板
			if("add".equals(pageType)){
				base_id = AppUtils.getStringSeq();
				paramMap.put("base_id", base_id);
				paramMap.put("base_seq_name", "pccm_"+DateTimeUtil.getNowDate());
				server.addBaseModel(paramMap);
				//创建对应的序列,用于创建子表英文名称
				server.addModelSeq("pccm_"+DateTimeUtil.getNowDate());
			}else if("edit".equals(pageType)){
				paramMap.put("base_id", base_id);
				server.updateBaseModel(paramMap);
			}
			//保存字段
			server.deleteFILED(base_id);
			server.addBaseFileds(requestMap, base_id);
			//保存权限
			server.deleteAuthOrgs(base_id);
			server.addAuthOrgs(base_id, orgIds, user_no);
		} catch (Exception e) {
			result="fail";
			e.printStackTrace();
		}
		setAttr("result",result);
		// 返回json数据
		renderJson();
	}

	/**
	 * 删除模板
	 */
	public void deleteModel() {
		
		String base_ids = getPara("base_ids");
		if (base_ids.contains(",")) {
			String[] array = base_ids.split(",");
			for (int i = 0; i < array.length; i++) {
				//删除模板
				server.deleteModel(array[i]);
				//删除字段
				server.deleteFILED(array[i]);
				//删除权限
				server.deleteAuthOrgs(array[i]);
			}
		} else {
			server.deleteModel(base_ids);
			server.deleteFILED(base_ids);
		}
		renderNull();
	}

	
	/**
	 * 查询明细
	 */
	public void getDetail() {
		String base_id = getPara("base_id");
		//父表明细
		Record record = server.getModelDetail(base_id);
		//字段明细
		List<Record>  reList =  server.getFieldDetail(base_id);
		//是否存在子表
		boolean isExist = server.isExistChildTable(base_id);
		//权限查询
		List<Record> orgList = server.getAuthOrgs(base_id);
		String orgIds = "";
		for(Record re : orgList){
			orgIds +="".equals(orgIds)? re.getStr("id"):","+re.getStr("id");
		}
		setAttr("record", record);
		setAttr("reList", reList);
		setAttr("isExist", isExist);
		setAttr("orgTree", orgIds);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 更改表单状态
	 */
	public void changeStatus(){
		String base_id = getPara("base_id");
		String status = getPara("status");
		server.changeStatus(base_id, status);
		renderNull();
	}
	
	/**
	 * 上传excel
	 */
	public void importData(){
 		HSSFWorkbook hb = null;
 		InputStream is = null;
 		HSSFRow row = null;
 		Set<Map<String, Object>> entitySet = new HashSet<Map<String, Object>>();
 		Map<String, Object> map = null;
// 		StringBuffer msg = new StringBuffer();
// 		String msgStr = null;
 		try {
 			UploadFile uploadFile=this.getFile("upload_file");
  			// 判断有文件才进行上传
  			if(uploadFile!=null){
  				File file=uploadFile.getFile();
  				//文件名
  				String fileName = file.getName();
  				//尾缀名
  				String suffix = fileName.substring(fileName.lastIndexOf("."));
  				if(!".xls".equals(suffix)){
  					renderFailJsonMsg("请上传尾缀是xls的Excel文件！");
  					return;
  				}
  				is = new FileInputStream(file);
  				hb = new HSSFWorkbook(is);
  				HSSFSheet sheet = hb.getSheetAt(0);
  				int totalrows = sheet.getLastRowNum()+1;
  				if(totalrows<2){
  					renderFailJsonMsg("Excel无数据！");
  					return;
  				}
  				for(int i=1;i<sheet.getLastRowNum()+1;i++){
  					map = new HashMap<String, Object>();
  					//创建一个行对象
  					row = sheet.getRow(i);
  					//读取行内容
  					//校验数据
  					if(null!=CommonUtil.findCellValue(row.getCell(0))){
  						map.put("sort_num", CommonUtil.findCellValue(row.getCell(0)));
  					}
  					if(null!=CommonUtil.findCellValue(row.getCell(1))){
  						map.put("filed_en_name", CommonUtil.findCellValue(row.getCell(1)));
  					}
  					if(null!=CommonUtil.findCellValue(row.getCell(2))){
  						map.put("filed_cn_name", CommonUtil.findCellValue(row.getCell(2)));
  					}
  					if(null!=CommonUtil.findCellValue(row.getCell(3))){
  						map.put("filed_length", CommonUtil.findCellValue(row.getCell(3)));
  					}
  					if(null!=CommonUtil.findCellValue(row.getCell(4))){
  						map.put("filed_type", CommonUtil.findCellValue(row.getCell(4)));
  					}
  					if(null!=CommonUtil.findCellValue(row.getCell(5))){
  						map.put("description", CommonUtil.findCellValue(row.getCell(5)));
  					}
  					
  					entitySet.add(map);
  				}
  				//转换json,根据序号排序
  				List<Map<String, Object>> list = new ArrayList<>(entitySet);
  				Collections.sort(list, new Comparator<Map<String, Object>>(){
					public int compare(Map<String, Object> o1, Map<String, Object> o2) {
						String o1Str = String.valueOf(o1.get("sort_num"));
						String o2Str = String.valueOf(o2.get("sort_num"));
						if(!StringUtils.isNumeric(o1Str)){
							o1Str = "0";
	  					}
						if(!StringUtils.isNumeric(o2Str)){
							o2Str = "0";
						}
						return Double.valueOf(o1Str).compareTo(Double.valueOf(o2Str));
					}
  				});
  				JSONArray jsonData = JSONArray.fromObject(list);
  				setAttr("jsonData", jsonData);
  				// 返回json数据
  				renderJson();
  				renderSuccessJsonMsg( "上传成功！");
  			}else{
  				renderFailJsonMsg("请先上传文件！");
  				return;
  			}
  			
  		} catch (Exception e) {
  			renderFailJsonMsg("上传失败！");
  		}
	}
	
	/**
	 * 授权详情
	 */
	public void getAuthOrgs() {
		String base_id = getPara("base_id");
		//
		List<Record> reList = server.getAuthOrgs(base_id);
		setAttr("reList", reList);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 分页查询业务定制父列表
	 */
	public void getBusiList() {
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String tableCnName = getPara("table_cn_name");
		String tableEnName = getPara("table_en_name");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tableCnName", tableCnName);
		paramMap.put("tableEnName", tableEnName);
		paramMap.put("orgId", getCurrentUser().getStr("ORG_ID"));
		Page<Record> r = server.getBusiList(paramMap, pageNum, pageSize);
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 查询字段明细
	 */
	public void getFieldDetail() {
		String base_pid = getPara("base_pid");
		//字段明细
		List<Record>  fieldList =  server.getFieldDetail(base_pid);
		setAttr("fieldList", fieldList);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 保存子模版信息
	 */
	public void addChildModel() {

		String result="success";
		try {
			String userNo = getCurrentUser().getStr("ID");
			String base_id = getPara("base_id");
			String base_pid = getPara("base_pid");
			String tableCnName = getPara("table_cn_name");
			String tableEnName = getPara("table_en_pname");
			String filedIds = getPara("filedIds");
			String pageType = getPara("pageType");
			String orgIds = getPara("orgIds");
			String base_seq_name = getPara("base_seq_name");
			String licenseStatus = "0";
			
			if (StringUtils.isBlank(orgIds)) {
				licenseStatus = "0"; // 默认未授权状态
			}else{
				licenseStatus = "1";
			}
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("table_cn_name", tableCnName);
			paramMap.put("license_status", licenseStatus);
			paramMap.put("base_pid", base_pid);
			paramMap.put("user_no", userNo);
			//保存/编辑模板
			if("add".equals(pageType)){
				base_id = AppUtils.getStringSeq();
				paramMap.put("base_id", base_id);
				paramMap.put("table_en_name", tableEnName+"_"+AppUtils.findSeq(base_seq_name));
				server.addChildModel(paramMap);
			}else if("edit".equals(pageType)){
				paramMap.put("base_id", base_id);
				server.updateChildModel(paramMap);
			}
			//保存子模板字段信息
			server.deleteChildFILED(base_id);
			server.addChildModelFileds(base_id, filedIds);
			//保存权限
			server.deleteAuthOrgs(base_id);
			server.addAuthOrgs(base_id, orgIds, userNo);
		} catch (Exception e) {
			result="fail";
			e.printStackTrace();
		}
		setAttr("result",result);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 分页查询业务定制子列表
	 */
	public void getBusiChildList() {
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String tableCnName = getPara("table_cn_name");
		String tableEnName = getPara("table_en_name");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tableCnName", tableCnName);
		paramMap.put("tableEnName", tableEnName);
		paramMap.put("user_no", getCurrentUser().getStr("ID"));
		Page<Record> r = server.getBusiChildList(paramMap, pageNum, pageSize);
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 查询子表明细
	 */
	public void getChildDetail() {
		String base_id = getPara("base_id");
		String base_pid = getPara("base_pid");
		//子表明细
		Record record = server.getModelDetail(base_id);
		//字段明细
		List<Record>  pList =  server.getFieldDetail(base_pid);
		List<Record>  childList =  server.getFieldDetail(base_id);
		//权限查询
		List<Record> orgList = server.getAuthOrgs(base_id);
		String orgIds = "";
		for(Record re : orgList){
			orgIds +="".equals(orgIds)? re.getStr("id"):","+re.getStr("id");
		}
		setAttr("record", record);
		setAttr("childList", childList);
		setAttr("pList", pList);
		setAttr("orgTree", orgIds);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 删除子模板
	 */
	public void deleteChildModel() {
		
		String base_ids = getPara("base_ids");
		if (base_ids.contains(",")) {
			String[] array = base_ids.split(",");
			for (int i = 0; i < array.length; i++) {
				//删除模板
				server.deleteModel(array[i]);
				//删除字段
				server.deleteChildFILED(array[i]);
				//删除权限
				server.deleteAuthOrgs(array[i]);
			}
		} else {
			server.deleteModel(base_ids);
			server.deleteFILED(base_ids);
		}
		renderNull();
	}
	
	/**
	 * 分页查询业务定制父列表
	 */
	public void getSerList() {
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String tableCnName = getPara("table_cn_name");
		String tableEnName = getPara("table_en_name");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tableCnName", tableCnName);
		paramMap.put("tableEnName", tableEnName);
		paramMap.put("orgId", getCurrentUser().getStr("ORG_ID"));
		Page<Record> r = server.getSerList(paramMap, pageNum, pageSize);
		// 将获取到的用户信息填充到request对象的属性中
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 返回json数据
		renderJson();
	}

	/**
	 * 查询子表字段明细
	 */
	public void getChildFieldDetail() {
		String base_id = getPara("base_id");
		String downFlag = getPara("downFlag");
		//字段明细
		List<Record>  fieldList =  server.getChildFieldDetail(base_id,downFlag);
		setAttr("fieldList", fieldList);
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 创建下载任务
	 */
	public void downTask() {
		String rs="F";
		String user_no = getCurrentUser().getStr("ID");
		String user_name = getCurrentUser().getStr("NAME");
		
		String task_text = getPara("task_text");
		String table_en_pname = getPara("table_en_pname");
		String filedIds = getPara("filedIds");
		String pageType = getPara("pageType");
		String orgIds = getPara("orgIds");
		String heads = getPara("heads");
		String data_date =  DateTimeUtil.changeDateString(getPara("data_date"));
		String date_filed_name = getPara("date_filed_name");
		String org_filed_name = getPara("org_filed_name");
		
		//组装SQL
		String sql = "";
		String selSql="";
		if(StringUtils.isNotBlank(filedIds)){
			String[] filedArr = filedIds.split(",");
			String[] headArr = heads.split(",");
			selSql = "o2.orgname as 二级分行名称,o2.id 二级分行机构号,o1.orgname 中心支行名称,o1.id 中心支行机构号,o.orgname 责任中心名称";
			for(int i=0;i<filedArr.length;i++){
				selSql += ","+filedArr[i]+" as "+headArr[i];
			}
			heads = "二级分行名称,二级分行机构号,中心支行名称,中心支行机构号,责任中心名称,"+heads;
			if("addDetail".equals(pageType)){
				sql = " select "+selSql+" from "+table_en_pname+" p "
					+" left join sys_org_info o on p."+org_filed_name+" = o.id "
					+" left join sys_org_info o1 on o.upid = o1.id "
					+" left join sys_org_info o2 on o1.upid = o2.id "
					+" where p."+org_filed_name+" in (" +orgIds+") and p."
					+date_filed_name+" = '"+data_date+"' order by p."+org_filed_name; 
			}else if("addTotal".equals(pageType)){
				String groups = getPara("groups");
				if(!groups.contains(org_filed_name)){
					groups+="p."+org_filed_name;
				}
				groups+=",o2.orgname ,o2.id,o1.orgname,o1.id,o.orgname";
				heads = heads+",客户数";
				sql = " select "+selSql+",count(1) as 客户数 from "+table_en_pname+" p "
					+" left join sys_org_info o on p."+org_filed_name+" = o.id "
					+" left join sys_org_info o1 on o.upid = o1.id "
					+" left join sys_org_info o2 on o1.upid = o2.id "
					+" where p."+org_filed_name+" in (" +orgIds+") and p."
					+date_filed_name+" = '"+data_date
					+"' group by "+groups
					+" order by p."+org_filed_name;
			}
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("sql", sql);
			paramMap.put("task_text", task_text);
			paramMap.put("user_name", user_name);
			paramMap.put("user_no", user_no);
			paramMap.put("head", heads);
			System.out.println(sql);
			System.out.println(heads);
			//创建下载任务
			rs = DownDetailUtil.DownReportTask(paramMap);
		}
		
		setAttr("msg", rs);
		// 返回json数据
		renderJson();
	}

}
