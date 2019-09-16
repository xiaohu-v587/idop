//预警查询
package com.goodcol.controller.dop;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;
@RouteBind(path="/prompting_search")
/*@Before({ ManagerPowerInterceptor.class })
*/public class PromptingSearchCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(WarningSearchCtl.class);
	@Override
/*	@Before(PermissionInterceptor.class)
*/	public void index() {
		String key = getPara("key");
		String style = getPara("style");
		String  orgid = getCurrentUser().getStr("ORG_ID");
		if(AppUtils.StringUtil(key)!= null){
			if(AppUtils.StringUtil(style)!= null && "0".equals(style)){
				orgid = key;
			}
		}
		setAttr("org", orgid);
		render("index.jsp");
	}
	
	
	public void getWarningNameList(){
		String val=getPara("val");
		String sql="";
		sql="select t.warning_name,t.warning_code from dop_warning_param  t where t.busi_module='"+val+"' " +
		 		"and (t.is_use is  null or t.is_use = '1') and t.warning_dimension ='2' order by t.warning_name asc";
		List<Record> list=Db.find(sql);
		setAttr("data",list);
		renderJson();
		
	}
	
	public void detail(){
		setAttr("id", getAttr("id"));
		render("detail.jsp");
	}
	public void getList(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String isDown = getPara("isDown");
		if(isDown!= null ){
			pageNum = 1;
			pageSize = 1;
		}
		
		Map<String, Object> map = organSql("");
		@SuppressWarnings("unchecked")
		Page<Record> page = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	/**
	 * 根据查询条件搜索
	 * @return
	 */
	public Map<String, Object> organSql(String type) {
		// 获取页面输入查询条件
		Record user = getCurrentUser();
		//默认当前用户层级
		String orgid = getPara("orgid");
		
		String busi_module = getPara("busi_module"); 
		String startTime = getPara("start_time");
		String endTime = getPara("end_time");
		String bizStartTime = getPara("biz_start_time");
		String bizEndTime = getPara("biz_end_time");
		String followed_teller = getPara("followed_teller");
		String warning_name = getPara("warning_name");
		String is_key_dxtz = getPara("is_key_dxtz");
		String prompt_status = getPara("prompt_status");
		
		String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");//数据范围,不能取用户自己所在的机构，而要按照用户的数据范围、角色级别 来查， 数据范围优先
		String userOrg = user.getStr("MAX_PERMI_ORGNUM");
		String roleLevel = user.getStr("ROLE_LEVEL");

		
//		String selectSql = " select  i.id,(select remark from sys_param_info where key='dxtz' and val=p.is_key_dxtz) is_key_dxtz, p.is_key_jsf,p.warning_code prompt_no, g.remark busi_module,p.warning_name prompt_name," +
//					" to_char(to_date(i.create_time,'yyyy-MM-dd HH24:mi:ss'),'yyyy-MM-dd') as prompt_time, to_char(to_date(i.data_date,'yyyy-MM-dd HH24:mi:ss'),'yyyy-MM-dd') as data_date," +
//					" case i.deptlevel when '0' then '江苏省分行' when '1' then i.lvl_2_branch_name when '2' then i.lvl_3_branch_name" +
//					" when '3' then i.lvl_4_branch_name end org_name,(select name from sys_user_info where user_no=substr(i.teller_no,14)) teller_name,(select remark from sys_param_info where val=i.prompt_status and key='prompt_status') prompt_status";
		String selectSql="select t.*, (case when t.status is null then '存量数据' else t.status end) prompt_status  ";		
		
		//预警类型id
		String warningTypeIds = Db.findFirst("select listagg(id,''',''') within group (order by id) ids from dop_gangedmenu_info start with val='1003' connect by prior id =upid ").getStr("ids");
		String extrasql ="from ( select  i.id,(select remark from sys_param_info where key='dxtz' and val=p.is_key_dxtz) is_key_dxtz, p.is_key_jsf,p.warning_code prompt_no, g.remark busi_module,p.warning_name prompt_name," +
				" to_char(to_date(i.create_time,'yyyy-MM-dd HH24:mi:ss'),'yyyy-MM-dd') as prompt_time, to_char(to_date(i.data_date,'yyyy-MM-dd HH24:mi:ss'),'yyyy-MM-dd') as data_date," +
				" case i.deptlevel when '0' then '江苏省分行' when '1' then i.lvl_2_branch_name when '2' then i.lvl_3_branch_name" +
				" when '3' then i.lvl_4_branch_name end org_name,(select name from sys_user_info where user_no=substr(i.teller_no,14)) teller_name,(select remark from sys_param_info where val=i.prompt_status and key='prompt_status') status" +  
				" from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code " +
				" left join (select * from dop_gangedmenu_info k where k.id in ('"+warningTypeIds+"')) g on g.val=p.busi_module ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1  and  (p.is_use is  null or p.is_use = '1') and p.warning_dimension='2' ");
		List<String> sqlStr = new ArrayList<String>();
		if (AppUtils.StringUtil(busi_module) != null && !"1001".equals(busi_module)) {
			whereSql.append(" and p.busi_module = ? ");
			sqlStr.add(busi_module.trim());
		}
		if (AppUtils.StringUtil(startTime) != null && AppUtils.StringUtil(endTime) != null) {
			endTime=endTime.substring(0,10)+"23:59:59";
			whereSql.append(" and to_date(i.create_time,'yyyy-MM-dd HH24:mi:ss') between to_date(?,'yyyy-MM-dd HH24:mi:ss') and " +
					"to_date(?,'yyyy-MM-dd HH24:mi:ss') ");
			sqlStr.add(startTime.trim());
			sqlStr.add(endTime.trim());
		}
		if (AppUtils.StringUtil(bizStartTime) != null) {
			bizStartTime=bizStartTime.substring(0,10);
			whereSql.append(" and to_date(i.data_date, 'yyyyMMdd') >= to_date(?, 'yyyy-MM-dd') ");
			sqlStr.add(bizStartTime.trim());
		}
		if (AppUtils.StringUtil(bizEndTime) != null) {
			bizEndTime=bizEndTime.substring(0,10);
			whereSql.append(" and to_date(i.data_date, 'yyyyMMdd') <= to_date(?, 'yyyy-MM-dd') ");
			sqlStr.add(bizEndTime.trim());
		}
		if (AppUtils.StringUtil(is_key_dxtz) != null) {
			whereSql.append(" and p.is_key_dxtz = ? ");
			sqlStr.add(is_key_dxtz.trim());
		}
//		if (AppUtils.StringUtil(orgid) != null) {
//			whereSql.append(" and i.deptno in (select id from sys_org_info start with orgnum = '"+orgid+"' connect by prior orgnum=upid) ");
//		}
		
		if(AppUtils.StringUtil(followed_teller) != null){
			whereSql.append( " and  substr(i.teller_no,14) = ? ");
			sqlStr.add(followed_teller.trim());
		}
		
		if (AppUtils.StringUtil(orgid) != null) {			//页面上选的机构
			//whereSql.append(" and i.deptno in (select id from sys_org_info start with orgnum = '"+org+"' connect by prior orgnum=upid) ");
			whereSql.append(" and i.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
			 sqlStr.add(orgid.trim());
			 sqlStr.add("%" + orgid.trim()+ "%");
		}
		
		if (AppUtils.StringUtil(warning_name) != null) {
			whereSql.append(" and p.warning_code = ? ");
			sqlStr.add(warning_name.trim());
		}
		
		if (AppUtils.StringUtil(prompt_status) != null) {
			String [] status = prompt_status.split(regex);
			StringBuffer inSqlBuffer = new StringBuffer();
			Boolean isFir = true;
			if("9".equals(prompt_status)){
				whereSql.append("and i.prompt_status is null");
			}else if(prompt_status.contains("9")){
				for(String val : status){
					if(!"9".equals(val)){
						if(isFir){
							isFir = false;
						}else{
							inSqlBuffer.append(",");
						}
						inSqlBuffer.append("?");
						sqlStr.add(val);
					}
				}
				whereSql.append(" and (i.prompt_status in ("+inSqlBuffer.toString()+") or  i.prompt_status is null )");
			}else{
				whereSql.append("and i.prompt_status is not null");
				
				for (String val : status) {
					if(isFir){
						isFir = false;
					}else{
						inSqlBuffer.append(",");
					}
					inSqlBuffer.append("?");
					sqlStr.add(val);
				}
				whereSql.append(" and i.prompt_status in ("+inSqlBuffer.toString()+")");
			}
		
		}
		
		extrasql += whereSql.toString() +" order by i.create_time )t";
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectSql", selectSql);
		map.put("extrasql", extrasql);
		map.put("sqlStr", sqlStr);
		// 打印日志
		log.info("organSql--map:" + map);
		return map;
	}
	
	public String getOrgColumn(String by2){
		String OrgColumn;
		switch(by2){
		case "1":OrgColumn = "lvl_2_branch_no";break;//顶级行
		case "2":OrgColumn = "lvl_2_branch_no";break;//分行
		case "3":OrgColumn = "lvl_3_branch_no";break;//支行
		case "4":OrgColumn = "lvl_4_branch_no";break;//网点/辖区
		default:OrgColumn="";break;
		}
		return OrgColumn;
	}
	
	public void getListByExport(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String isDown = getPara("isDown");
		if(isDown!= null ){
			pageNum = 1;
			pageSize = 999999;
		}
		List<String> idList = new ArrayList<>();

		Map<String, Object> map = organSql("");	
		@SuppressWarnings("unchecked")
		Page<Record> page2 = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
		StringBuffer inSqlBuffer = new StringBuffer();
		Boolean isFir = true;
		for(int i=0;i<page2.getList().size();i++){
			String id=page2.getList().get(i).getStr("id");
			idList.add(id);
			if(isFir){
				isFir = false;
			}else{
				inSqlBuffer.append(",");
			}
			inSqlBuffer.append("?");
			Db.update("update dop_warning_info set prompt_status='4' where id=?",id );
		}
		
		String sql = " select  i.id,(select remark from sys_param_info where key='dxtz' and val=p.is_key_dxtz) is_key_dxtz, p.is_key_jsf,p.warning_code prompt_no, g.remark busi_module,p.warning_name prompt_name,to_char(to_date(i.create_time,'yyyy-MM-dd HH24:mi:ss'),'yyyy-MM-dd') as prompt_time, to_char(to_date(i.data_date,'yyyy-MM-dd HH24:mi:ss'),'yyyy-MM-dd') as data_date," +
				"case i.deptlevel when '0' then '江苏省分行' when '1' then i.lvl_2_branch_name when '2' then i.lvl_3_branch_name" +
				" when '3' then i.lvl_4_branch_name end org_name,(select name from sys_user_info where user_no=substr(i.teller_no,14)) teller_name,(select remark from sys_param_info where val=i.prompt_status and key='prompt_status') prompt_status";
				
		//预警类型id
		String warningTypeIds = Db.findFirst("select listagg(id,''',''') within group (order by id) ids from dop_gangedmenu_info start with val='1003' connect by prior id =upid ").getStr("ids");
		String extra = " from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code " +
				" left join (select * from dop_gangedmenu_info k where k.id in ('"+warningTypeIds+"')) g on g.val=p.busi_module ";
		StringBuffer where= new StringBuffer(" where 1 = 1  and  (p.is_use is  null or p.is_use = '1') and p.warning_dimension='2' ");
		
		where.append(" and i.id in ("+inSqlBuffer.toString()+")");	
		
		extra += where.toString() +" order by i.create_time";
		
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("selectSql", sql);
		map1.put("extrasql", extra);
		map1.put("sqlStr", idList);
		
		@SuppressWarnings("unchecked")
		Page<Record> page1 = Db.paginate(pageNum, pageSize, (String) map1.get("selectSql"), (String) map1.get("extrasql"), ((List<String>) map1.get("sqlStr")).toArray());

		setAttr("data", page1.getList());
		setAttr("total", page1.getTotalRow());
		renderJson();
	}
	
	public void getListByDown(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
			
		Map<String, Object> map = organSql("2");
		@SuppressWarnings("unchecked")
		Page<Record> page = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	/**
	 * 导出Excel表格 
	 */
//	@Before(PermissionInterceptor.class)
	public void download(){
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = getPara("execlcolumns").split(",");
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = getPara("execlheaders").split(",");
		
		String result=getPara("result");
		
		String fileName = "";
		try {
			fileName = new String((getPara("execlfilename")+System.currentTimeMillis()+".xls").getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
		if("true".equals(result)){
			getListByExport();
		}else{
			getListByDown();
		}		
		List<Record> list = getAttr("data");
		// 转换成excel
		ExcelRenderx er = new ExcelRenderx(fileName, columns, headers, list, getResponse());
		er.render(); 
		// 打印日志   
		log.info("download--list:" + list);
		renderNull();
	}
	
	public void getRemark(){
		String prompt_no = getPara("prompt_no");
		String remark = Db.findFirst("select remark from dop_warning_param t where t.warning_code='"+prompt_no+"' and t.warning_dimension ='2' ").get("remark");
		setAttr("remark",remark);
		renderJson();
	}
	
	public void getDetail(){
		String id = getPara("id");
		String selectSql = " select t.*,(case when t.status is null then '存量数据' else status end) prompt_status from " +
				" (select p.warning_code prompt_no,(select remark from dop_gangedmenu_info where val=p.busi_module and upid='CAAC03E702B84EB792D4CFBA924CB1247' ) busi_module,p.warning_name prompt_name," +
				" to_char(to_date(substr(i.create_time,0,8),'yyyy-MM-dd'),'yyyy-MM-dd') as prompt_time,to_char(to_date(i.data_date,'yyyy-MM-dd'),'yyyy-MM-dd') data_date," +
				" (select orgname from sys_org_info where id=i.deptno) orgid,(select name from sys_user_info where user_no=substr(i.teller_no,14)) teller_name, " +
				" (select remark  from sys_param_info where key='dxtz' and val= p.is_key_dxtz ) is_key_dxtz,p.is_key_jsf," +
				" (select remark from sys_param_info  where val=i.prompt_status and key='prompt_status') status ," +
				" p.message_org,p.message_person from dop_warning_info i left join dop_warning_param" +
				" p on p.warning_code=i.warning_code where i.id=? and  (p.is_use is  null or p.is_use = '1')) t  ";
		Record data = Db.findFirst(selectSql,id);
		
		setAttr("record",data);
		renderJson();
	}
	
	public void getDetailDatas(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String id = getPara("id");
		Page<Record> page = Db.use("gbase").paginate(pageNum, pageSize,"select * "," from idop_warnings_dtls_lvl2 where id=?",id);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	public void export(){
		String id = getPara("id");
		String warningCode = Db.findFirst("select warning_code from dop_warning_info where id=?",id).getStr("warning_code");
		List<Record> columns = Db.use("gbase").find("select * from idop_param_dtl_lvl2_list where warning_code=?",warningCode);
		List<Record> headers = new ArrayList<>();
		if(columns!=null && !columns.isEmpty()){
			for (Record record : columns) {
				String fieldName = "field";
				if(record.getInt("column_ordinal_pos")>=10){
					fieldName = fieldName+record.get("column_ordinal_pos");
				}else{
					fieldName = fieldName+"0"+record.get("column_ordinal_pos");
				}
				headers.add(new Record().set("header", record.get("column_desc")).set("field",fieldName).set("headerAlign", "center").set("align", "center").set("visible", "true"));
			}
		}
		List<Record> data = Db.use("gbase").find("select * from idop_warnings_dtls_lvl2 where id=?",id);
		// 转换成excel
		 int len=headers.size();
		 String[] header=new String[len];
		 String[] column=new String[len];
		
		if(columns!=null && !columns.isEmpty()){
			for(int i=0;i<len;i++){
				header[i]= headers.get(i).getStr("field").toString();
				column[i] = headers.get(i).getStr("header").toString();
			}
		}
		String fileName = "";
		try {
			fileName = new String(("提示详情描述"+System.currentTimeMillis()+".xls").getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
	
	  ExcelRenderx er = null;
	 // header=headers
	  er = new ExcelRenderx(fileName,column, header, data, getResponse());
		er.render(); 
		// 打印日志
		log.info("download--list:" + data);
		renderNull();
	}
	
	public void getDetailHeaders(){
		String id = getPara("id");
		System.out.println("getDetailHeaders 获取参数成功");
		String warningCode = Db.findFirst("select warning_code from dop_warning_info where id=?",id).getStr("warning_code");
		System.out.println("getDetailHeaders 执行oracle sql 成功");
		List<Record> columns = Db.use("gbase").find("select * from idop_param_dtl_lvl2_list where warning_code=?",warningCode);
		System.out.println("getDetailHeaders 执行gbase sql 成功");
		List<Record> headers = new ArrayList<>();
		if(columns!=null && !columns.isEmpty()){
			for (Record record : columns) {
				String fieldName = "field";
				if(record.getInt("column_ordinal_pos")>=10){
					fieldName = fieldName+record.get("column_ordinal_pos");
				}else{
					fieldName = fieldName+"0"+record.get("column_ordinal_pos");
				}
				headers.add(new Record().set("header", record.get("column_desc")).set("field",fieldName).set("headerAlign", "center").set("align", "center").set("visible", "true"));
			}
		}
		List<Record> data = Db.use("gbase").find("select * from idop_warnings_dtls_lvl2 where id=?",id);
		setAttr("headers",headers);
		setAttr("data",data);
		log.info("此时成功返回数据的前台header");
		System.out.println("此时成功返回数据的前台header");
		renderJson();
	}
	

	public void getPrompt(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String isDown = getPara("isDown");
		if(isDown!= null ){
			pageNum = 1;
			pageSize = 999999;
		}
		
		Map<String, Object> map = organSql("");
		@SuppressWarnings("unchecked")
		Page<Record> page = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
		
		List<String> statusList = new ArrayList<>();
		
		String result="false";
		for(int i=0;i<page.getList().size();i++){
			String prompt_status=page.getList().get(i).getStr("prompt_status");
			statusList.add(prompt_status);
		}
	
		if(statusList.contains("未发送短信")||statusList.contains("已发送短信")||statusList.contains("已人工提示")||statusList.isEmpty()){
//				setAttr("data", page.getList());
//				setAttr("total", page.getTotalRow());
			setAttr("result",result);
		}else{
			result="true";
			setAttr("result",result);
		}
		renderJson();
	}
}
