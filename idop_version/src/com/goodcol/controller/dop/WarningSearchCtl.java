//预警查询
package com.goodcol.controller.dop;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;
@RouteBind(path="/warning_search")
@Before({ ManagerPowerInterceptor.class })
public class WarningSearchCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(WarningSearchCtl.class);
	@Override
	@Before(PermissionInterceptor.class)
	public void index() {
		String key = getPara("key");
		String style = getPara("style");
		//String  orgid = getCurrentUser().getStr("ORG_ID");
		String  orgid =  getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		if(AppUtils.StringUtil(key)!= null){
			if(AppUtils.StringUtil(style)!= null && "0".equals(style)){
				orgid = key;
			}
		}
		setAttr("org", orgid);
		render("index.jsp");
	}
	
	public void toindex() {
		String orgid = getPara("orgnum");
		String warnkey = "1";
		String warning_status = "1";
		String indentify_status = "1";
		String check_stat = "1";
		Record re = new Record();
		String key = getPara("key");
		String style = getPara("style");
		String userno = null;
		if(AppUtils.StringUtil(key)!= null){
			if(AppUtils.StringUtil(style)!= null && "0".equals(style)){
				orgid = key;
			}else if(AppUtils.StringUtil(style)!= null && "1".equals(style)){
				userno = key;
			}
		}
		re.set("orgid", orgid);
		re.set("teller_no", userno);
		re.set("is_key_warning", warnkey);
		re.set("warning_status", warning_status);
		re.set("indentify_status", indentify_status);
		re.set("check_stat", check_stat);
		setAttr("datas", re.toJson());
		setAttr("flag","1");
		setAttr("org", orgid);
		render("index.jsp");
	}
	
	public void toindexs() {
		String orgid = getPara("orgnum");
		String warnkey = "1";//重点预警
		String warning_status = "1";
		Record re = new Record();
		String key = getPara("key");
		String style = getPara("style");
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time =format.format(date);
		String startTime = time.substring(0,10)+"00:00:00";
		String endTime = time;
		String userno = null;
		if(AppUtils.StringUtil(key)!= null){
			if(AppUtils.StringUtil(style)!= null && "0".equals(style)){
				orgid = key;
			}else if(AppUtils.StringUtil(style)!= null && "1".equals(style)){
				userno = key;
			}
		}
		re.set("orgid", orgid);
		re.set("teller_no", userno);
		re.set("is_key_warning", warnkey);
		re.set("warning_status", warning_status);
		re.set("start_time", startTime);
		re.set("end_time", endTime);
		setAttr("datas", re.toJson());
		setAttr("flag","1");
		setAttr("org", orgid);
		render("index.jsp");
	}
	
	public void toIndexFromBizMonitor() {
		String start_time = getPara("date");
		String end_time = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			switch(getPara("fre")) {
			case "日": break;
			case "周":
				cal.setTime(sdf.parse(start_time));
				int interval = cal.get(Calendar.DAY_OF_WEEK);
				if (interval == 1) {
					cal.add(Calendar.DATE, -6);
				} else {
					cal.add(Calendar.DATE, -interval+2);
				}
				start_time = sdf.format(cal.getTime());
				cal.add(Calendar.DATE, 6);
				end_time = sdf.format(cal.getTime());
				break;
			case "月":
				cal.setTime(sdf.parse(start_time));
				cal.set(Calendar.DAY_OF_MONTH, 1);
				start_time = sdf.format(cal.getTime());
				cal.add(Calendar.MONTH, 1);
				cal.add(Calendar.DATE, -1);
				end_time = sdf.format(cal.getTime());
				break;
			case "季":
				cal.setTime(sdf.parse(start_time));
				int mo = cal.get(Calendar.MONTH);
				if ((mo+1)%3 == 2) {
					cal.add(Calendar.MONTH, -1);
				} else if ((mo+1)%3 == 0) {
					cal.add(Calendar.MONTH, -2);
				}
				cal.set(Calendar.DAY_OF_MONTH, 1);
				start_time = sdf.format(cal.getTime());
				cal.add(Calendar.MONTH, 3);
				cal.add(Calendar.DATE, -1);
				end_time = sdf.format(cal.getTime());
				break;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Record re = new Record();
		re.set("biz_start_time",start_time);
		re.set("biz_end_time", end_time);
		String warncode = getPara("c");
		Record r = Db.findFirst("select busi_module, DOP_GANGEDMENU_INFO.id type from dop_warning_param left join DOP_GANGEDMENU_INFO on warning_type_code = val where warning_code = ?", warncode);
		re.set("ywtype", r.get("busi_module"));
		re.set("warning_type", r.get("type"));
		re.set("warn_name", warncode);
		setAttr("flag", "bizMonitor").setAttr("datas", re.toJson()).setAttr("org", "000000000");;
		render("index.jsp");
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
	
	public void getListByExport(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
			
		Map<String, Object> map = organSql("2");
		@SuppressWarnings("unchecked")
		Page<Record> page = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	//查询我的流程的数据
	@SuppressWarnings("unchecked")
	public void getWarningInfo(){
		Record user = getCurrentUser();
		BigDecimal indentWarning = new BigDecimal(0);
		BigDecimal checkWarning = new BigDecimal(0);
		BigDecimal approvalWarning = new BigDecimal(0);
		BigDecimal searchCheckWarning = new BigDecimal(0);
		
		String selectSql = " select count(1) ";
		/*
		//待认定
		setAttr("indentify_status", "0");
		Map<String, Object> map = organSql("1");
		String extrasql = (String) map.get("extrasql");
		Object[] objs = ((List<String>) map.get("sqlStr")).toArray();
		indentWarning = Db.queryBigDecimal(selectSql+extrasql,objs);
		removeAttr("indentify_status");
		
		//待核查
		setAttr("check_stat", "0");
		map = organSql("1");
		extrasql = (String) map.get("extrasql");
		objs = ((List<String>) map.get("sqlStr")).toArray();
		checkWarning = Db.queryBigDecimal(selectSql+extrasql,objs);
		removeAttr("check_stat");
		
		//待审批
		setAttr("approval_stat", "0");
		map = organSql("1");
		extrasql = (String) map.get("extrasql");
		objs = ((List<String>) map.get("sqlStr")).toArray();
		approvalWarning = Db.queryBigDecimal(selectSql+extrasql,objs);
		removeAttr("approval_stat");
		
		String selectSql = " select count(1) ";
		*/
//		whereSql.append(" and i.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
		
		String sjfw= user.getStr("MAX_PERMI_ORGNUM");
		String orgid=user.getStr("orgnum");
		String roleLevel = user.getStr("ROLE_LEVEL");
		
//		StringBuffer whereSql1 = new StringBuffer(" where 1 = 1 and g.id in(select id from dop_gangedmenu_info " +
//				" start with id=(select id from dop_gangedmenu_info where val='1003') connect by prior id=upid) ");
//		
		//判断权限机构级别是否为省行 
		String org_by2 = AppUtils.getOrgLevel(sjfw);
		if("1".equals(org_by2)){
			//待认定  都是省行认定
			String sql = "select count(*)  from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code left join (" +
					"select * from dop_gangedmenu_info k where k.id in ('351627F908C7461A9B8F82EB1BDEE750','35915F0EA9DC4B3DBFFD0C267579EA4C','4349615CE0A5433F9E23BADB7100D990'," +
					"'54AA6F8373B94F3AB115548D3867C6F7','679BE1CBC3C94B0689ECA17D7480A44D','8BDA5EE49374486CB87E7832EC8536F4','9DD1BF61F7F3419CAF5EB76AAD8BFDAA','9ECC18D14F7F4CB9812582C342F70936'," +
					"'CAAC03E702B84EB792D4CFBA924CB1247','CE1A1A8E9026486BB11124CAE2457D5A','DF39639549A449E8B1974D3B497165BB')) g on g.val=p.warning_type_code " +
					"where 1 = 1 " +
					" and i.indentify_status ='0' and  (p.is_use is  null or p.is_use = '1') and p.warning_dimension !='2'  and i.deptno in (select id from sys_org_info where id = '000000000' or by5 like '%000000000%')";
			
			indentWarning = Db.queryBigDecimal(sql);
		}
		String extrasql = " from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code " +
				" left join dop_gangedmenu_info g on g.val=p.warning_type_code  ";
		
		if("1".equals(roleLevel)){
			String whereSql2 = " where  (i.last_check_stat='0'  ) and  (p.is_use is  null or p.is_use = '1') and " +
					"( lvl_3_branch_no ='001001000') and p.warning_dimension !='2' ";
			//待核查
			checkWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql2);
		}else if("4".equals(roleLevel)){
			String whereSql2 = " where  (i.last_check_stat='0'  ) and  (p.is_use is  null or p.is_use = '1') and (select case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no "+
					" when '3' then lvl_4_branch_no end checker_level from dop_warning_info i2 where i2.id=i.id)='"+sjfw+"' ";
			//待核查
			checkWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql2);
		
			String whereSql4 = " where  (i.search_check_status='0' or i.search_check_status='2' )  and  (p.is_use is  null or p.is_use = '1') and  "+
				" (select check_org from dop_search_check c where c.check_flownum=i.flownum ) ='"+sjfw+"' ";
			//待查复
			searchCheckWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql4);
		
		}
		else if("2".equals(roleLevel)){
			String whereSql2 = " where  (i.last_check_stat='0'  ) and  (p.is_use is  null or p.is_use = '1') and (select case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no "+
					" when '3' then lvl_4_branch_no end checker_level from dop_warning_info i2 where i2.id=i.id)='"+sjfw+"' ";
			//待核查
			checkWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql2);
			
			String whereSql4 = " where  (i.search_check_status='0' or i.search_check_status='2' )  and  (p.is_use is  null or p.is_use = '1') and  "+
				" (select check_org from dop_search_check c where c.check_flownum=i.flownum ) in ('"+sjfw+"','"+orgid+"')";
			//待查复
			searchCheckWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql4);
		
		}
		else
		{
			
			String whereSql2 = " where  (i.last_check_stat='0'  ) and  (p.is_use is  null or p.is_use = '1') and (select case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no "+
						" when '3' then lvl_4_branch_no end checker_level from dop_warning_info i2 where i2.id=i.id)='"+sjfw+"' ";
			//待核查
			checkWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql2);
			
			String whereSql4 = " where  (i.search_check_status='0' or i.search_check_status='2' )  and  (p.is_use is  null or p.is_use = '1') and   "+
					" (select check_org from dop_search_check c where c.check_flownum=i.flownum ) in ('"+sjfw+"','"+orgid+"')";
				//待查复
				searchCheckWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql4);
			
		}
		String whereSql3 = "where p.is_confirm='1' and  (p.is_use is  null or p.is_use = '1') and i.last_approval_stat='0'  and " +
			"i.last_approver_org ='"+sjfw+"' ";
				//		" i.LAST_CHECKER_ORG in (select id from sys_org_info start with orgnum = '"+user.getStr("orgnum")+"' connect by prior orgnum=upid)" ;
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		setAttr("model",model);
		//待审批
		approvalWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql3);
		//待认定(界面：warning)
		setAttr("indentWarning",indentWarning.intValue());
		//核查(界面：manager)
		setAttr("checkWarning",checkWarning.intValue());
		//审批(界面：manager) 
		setAttr("approvalWarning",approvalWarning.intValue());
		//待查复(界面：warning)
		setAttr("searchCheckWarning",searchCheckWarning.intValue());
		renderJson();
	}
	
	//查询我的流程的数据(分模块的)
	@SuppressWarnings("unchecked")
	public void getWarningInfoByModel(){
		Record user = getCurrentUser();
		BigDecimal indentWarning = new BigDecimal(0);
		BigDecimal checkWarning = new BigDecimal(0);
		BigDecimal approvalWarning = new BigDecimal(0);
		BigDecimal searchCheckWarning = new BigDecimal(0);
		
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		
		String selectSql = " select count(1) ";
		/*
		//待认定
		setAttr("indentify_status", "0");
		Map<String, Object> map = organSql("1");
		String extrasql = (String) map.get("extrasql");
		Object[] objs = ((List<String>) map.get("sqlStr")).toArray();
		indentWarning = Db.queryBigDecimal(selectSql+extrasql,objs);
		removeAttr("indentify_status");
		
		//待核查
		setAttr("check_stat", "0");
		map = organSql("1");
		extrasql = (String) map.get("extrasql");
		objs = ((List<String>) map.get("sqlStr")).toArray();
		checkWarning = Db.queryBigDecimal(selectSql+extrasql,objs);
		removeAttr("check_stat");
		
		//待审批
		setAttr("approval_stat", "0");
		map = organSql("1");
		extrasql = (String) map.get("extrasql");
		objs = ((List<String>) map.get("sqlStr")).toArray();
		approvalWarning = Db.queryBigDecimal(selectSql+extrasql,objs);
		removeAttr("approval_stat");
		
		String selectSql = " select count(1) ";
		*/
//		whereSql.append(" and i.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
		
		String sjfw= user.getStr("MAX_PERMI_ORGNUM");
		String orgid=user.getStr("orgnum");
		String roleLevel = user.getStr("ROLE_LEVEL");
		
//		StringBuffer whereSql1 = new StringBuffer(" where 1 = 1 and g.id in(select id from dop_gangedmenu_info " +
//				" start with id=(select id from dop_gangedmenu_info where val='1003') connect by prior id=upid) ");
//		
		//判断权限机构级别是否为省行 
		String org_by2 = AppUtils.getOrgLevel(sjfw);
		if("1".equals(org_by2)){
			//待认定  都是省行认定
			String sql = "select count(*)  from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code left join (" +
					"select * from dop_gangedmenu_info k where k.id in ('351627F908C7461A9B8F82EB1BDEE750','35915F0EA9DC4B3DBFFD0C267579EA4C','4349615CE0A5433F9E23BADB7100D990'," +
					"'54AA6F8373B94F3AB115548D3867C6F7','679BE1CBC3C94B0689ECA17D7480A44D','8BDA5EE49374486CB87E7832EC8536F4','9DD1BF61F7F3419CAF5EB76AAD8BFDAA','9ECC18D14F7F4CB9812582C342F70936'," +
					"'CAAC03E702B84EB792D4CFBA924CB1247','CE1A1A8E9026486BB11124CAE2457D5A','DF39639549A449E8B1974D3B497165BB')) g on g.val=p.warning_type_code " +
					"where 1 = 1 " +
					" and i.indentify_status ='0' and p.busi_module='"+model+"' and  (p.is_use is  null or p.is_use = '1') and p.warning_dimension !='2' ";
			
			indentWarning = Db.queryBigDecimal(sql);
		}
		String extrasql = " from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code " +
				" left join dop_gangedmenu_info g on g.val=p.warning_type_code ";
		
		if("1".equals(roleLevel)){
			String whereSql2 = " where  (i.last_check_stat='0'  ) and  (p.is_use is  null or p.is_use = '1') and  (p.is_use is  null or p.is_use = '1') and " +
					"( lvl_3_branch_no ='001001000')   and p.busi_module='"+model+"'  and p.warning_dimension !='2' ";
			//待核查
			checkWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql2);
		}else if("4".equals(roleLevel)){
			String whereSql2 = " where  (i.last_check_stat='0'  ) and  (p.is_use is  null or p.is_use = '1') and    (p.is_use is  null or p.is_use = '1') and (select case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no "+
					" when '3' then lvl_4_branch_no end checker_level from dop_warning_info i2 where i2.id=i.id)='"+sjfw+"'   and p.busi_module='"+model+"' ";
			//待核查
			checkWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql2);
		
			String whereSql4 = " where  (i.search_check_status='0' or i.search_check_status='2' )  and  (p.is_use is  null or p.is_use = '1') and  "+
				" (select check_org from dop_search_check c where c.check_flownum=i.flownum )='"+sjfw+"' ";
			//待查复
			searchCheckWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql4);
		
		}
		else if("2".equals(roleLevel)){
			String whereSql2 = " where  (i.last_check_stat='0'  ) and  (p.is_use is  null or p.is_use = '1') and    (p.is_use is  null or p.is_use = '1') and (select case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no "+
					" when '3' then lvl_4_branch_no end checker_level from dop_warning_info i2 where i2.id=i.id)='"+sjfw+"'   and p.busi_module='"+model+"' ";
			//待核查
			checkWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql2);
			
			String whereSql4 = " where  (i.search_check_status='0' or i.search_check_status='2' )  and  (p.is_use is  null or p.is_use = '1') and"+
				" (select check_org from dop_search_check c where c.check_flownum=i.flownum )in ('"+sjfw+"','"+orgid+"')";
			//待查复
			searchCheckWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql4);
		
		}
		else
		{
			
			String whereSql2 = " where  (i.last_check_stat='0'  ) and  (p.is_use is  null or p.is_use = '1') and    (p.is_use is  null or p.is_use = '1') and (select case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no "+
						" when '3' then lvl_4_branch_no end checker_level from dop_warning_info i2 where i2.id=i.id)='"+sjfw+"'   and p.busi_module='"+model+"' ";
			//待核查
			checkWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql2);
			
			String whereSql4 = " where  (i.search_check_status='0' or i.search_check_status='2' )  and  (p.is_use is  null or p.is_use = '1') and   "+
					" (select check_org from dop_search_check c where c.check_flownum=i.flownum )in ('"+sjfw+"','"+orgid+"')";
				//待查复
				searchCheckWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql4);
		}
		String whereSql3 = "where p.is_confirm='1' and p.busi_module='"+model+"' and  (p.is_use is  null or p.is_use = '1') and i.last_approval_stat='0'  and " +
			"i.last_approver_org ='"+sjfw+"' ";
				//		" i.LAST_CHECKER_ORG in (select id from sys_org_info start with orgnum = '"+user.getStr("orgnum")+"' connect by prior orgnum=upid)" ;
		setAttr("model",model);
		//待审批
		approvalWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql3);
		//待认定(界面：warning)
		setAttr("indentWarning",indentWarning.intValue());
		//核查(界面：manager)
		setAttr("checkWarning",checkWarning.intValue());
		//审批(界面：manager) 
		setAttr("approvalWarning",approvalWarning.intValue());
		//待查复(界面：warning)
		setAttr("searchCheckWarning",searchCheckWarning.intValue());
		renderJson();
	}
	
	
	/**
	 * 导出Excel表格 
	 */
	@Before(PermissionInterceptor.class)
	public void download(){
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = getPara("execlcolumns").split(",");
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = getPara("execlheaders").split(",");
	   
		String fileName = "";
		try {
			fileName = new String((getPara("execlfilename")+System.currentTimeMillis()+".xls").getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
				
		getListByExport();
		List<Record> list = getAttr("data");
		// 转换成excel
		ExcelRenderx er = new ExcelRenderx(fileName, columns, headers, list, getResponse());
		er.render(); 
		// 打印日志   
		log.info("download--list:" + list);
		renderNull();
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
		
		String ywType = getPara("ywtype"); 
		String startTime = getPara("start_time");
		String endTime = getPara("end_time");
		String bizStartTime = getPara("biz_start_time");
		String bizEndTime = getPara("biz_end_time");
		String followed_teller = getPara("followed_teller");
		String warningStatus = getPara("warning_status");
		String warningType = getPara("warning_type");
		String warnname = getPara("warn_name");
		String warningLevel = getPara("warning_level");
		String indentifyStatus = getPara("indentify_status");
		String checkStat = getPara("check_stat");
		String isKeyWarning = getPara("is_key_warning");
		String approvalStat = getPara("approval_stat");
		String isQuestion = getPara("is_question");
		String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");//数据范围,不能取用户自己所在的机构，而要按照用户的数据范围、角色级别 来查， 数据范围优先
		String userOrg = user.getStr("MAX_PERMI_ORGNUM");
		String roleLevel = user.getStr("ROLE_LEVEL");
		if("1".equals(type)){
			ywType = getAttr("ywtype"); 
			startTime = getAttr("start_time");
			endTime = getAttr("end_time");
			warningStatus = getAttr("warning_status");
			warningType = getAttr("warning_type");
			warningLevel = getAttr("warning_level");
			indentifyStatus = getAttr("indentify_status");
			checkStat = getAttr("check_stat");
			isKeyWarning = getAttr("is_key_warning");
			approvalStat = getAttr("approval_stat");
			isQuestion = getAttr("is_question");
		}
		String selectSql = "";
		if("2".equals(type)){
			selectSql = " select p.warning_code,(select remark from sys_param_info t where t.key = 'dop_ywtype' and t.val=p.busi_module) as busi_module,p.warning_name,g.remark warning_type_code,p.warning_level,i.warning_status,to_date(i.create_time,'yyyy-MM-dd HH24:mi:ss') as create_time,i.data_date," +
					"case i.deptlevel when '0' then '江苏省分行' when '1' then i.lvl_2_branch_name when '2' then i.lvl_3_branch_name" +
					" when '3' then i.lvl_4_branch_name end orgname,i.last_check_stat,i.indentify_status,i.last_approval_stat,p.is_key_warning,i.flownum,case i.is_question when '0' then '否' when '1' then '是' end is_question, i.last_checker, i.checker_remark," +
					"(select bancsid from sys_org_info t where t.id = i.deptno) bancsid ";
		}else{
			selectSql = " select p.warning_code,p.busi_module,p.warning_name,g.remark warning_type_code,p.warning_level,i.warning_status,to_date(i.create_time,'yyyy-MM-dd HH24:mi:ss') as create_time, to_date(i.data_date,'yyyy-MM-dd HH24:mi:ss') as data_date," +
					"case i.deptlevel when '0' then '江苏省分行' when '1' then i.lvl_2_branch_name when '2' then i.lvl_3_branch_name" +
					" when '3' then i.lvl_4_branch_name end orgname,(select bancsid from sys_org_info where orgnum=(case i.deptlevel when '0' then '000000000' when '1' then i.lvl_2_branch_no when '2' then i.lvl_3_branch_no when '3' then i.lvl_4_branch_no end )) orgno,i.last_check_stat,i.indentify_status,i.last_approval_stat,p.is_key_warning,i.flownum" +
					",case i.is_question when '0' then '否' when '1' then '是' end is_question, " +
					"i.last_checker, i.checker_remark,(select bancsid from sys_org_info t where t.id = i.deptno) bancsid ";
		}
		//预警类型id
		String warningTypeIds = Db.findFirst("select listagg(id,''',''') within group (order by id) ids from dop_gangedmenu_info start with val='1003' connect by prior id =upid ").getStr("ids");
		String extrasql = " from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code " +
				" left join (select * from dop_gangedmenu_info k where k.id in ('"+warningTypeIds+"')) g on g.val=p.warning_type_code  ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1  and  (p.is_use is  null or p.is_use = '1') and p.warning_dimension !='2' and i.indentify_status is not null ");
		List<String> sqlStr = new ArrayList<String>();
		if (AppUtils.StringUtil(ywType) != null && !"1001".equals(ywType)) {
			whereSql.append(" and p.busi_module = ? ");
			sqlStr.add(ywType.trim());
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
			sqlStr.add(bizStartTime);
		}
		if (AppUtils.StringUtil(bizEndTime) != null) {
			bizEndTime=bizEndTime.substring(0,10);
			whereSql.append(" and to_date(i.data_date, 'yyyyMMdd') <= to_date(?, 'yyyy-MM-dd') ");
			sqlStr.add(bizEndTime);
		}
		if (AppUtils.StringUtil(warningStatus) != null) {
			whereSql.append(" and i.warning_status = ? ");
			sqlStr.add(warningStatus.trim());
		}
//		if (AppUtils.StringUtil(orgid) != null) {
//			whereSql.append(" and i.deptno in (select id from sys_org_info start with orgnum = '"+orgid+"' connect by prior orgnum=upid) ");
//		}
		
		if(AppUtils.StringUtil(followed_teller) != null){
//			whereSql.append( " and  substr(i.teller_no,14,7) = ? ");//20位还是7位
			whereSql.append( " and  i.teller_no = ? ");//20位还是7位
			sqlStr.add(followed_teller.trim());
		}
		
		if (AppUtils.StringUtil(orgid) != null) {			//页面上选的机构
			//whereSql.append(" and i.deptno in (select id from sys_org_info start with orgnum = '"+org+"' connect by prior orgnum=upid) ");
			whereSql.append(" and i.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
			 sqlStr.add(orgid.trim());
			 sqlStr.add("%" + orgid.trim()+ "%");
		}
		else
		{
			if (AppUtils.StringUtil(checkStat) == null && AppUtils.StringUtil(approvalStat) == null) 
			////如果查询条件没有指定机构，这里按照用户的数据范围、角色级别 来查， 数据范围优先
			{
				whereSql.append(" and i.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
				sqlStr.add(sjfw.trim());
				sqlStr.add("%" + sjfw.trim()+ "%");
			}
		}
		if (AppUtils.StringUtil(warningType) != null) {
			warningType=Db.findFirst("select val from dop_gangedmenu_info where id=?",warningType).getStr("val");
			whereSql.append(" and p.warning_type_code = ? ");
			sqlStr.add(warningType.trim());
		}
		if (AppUtils.StringUtil(warnname) != null) {
			if(warnname.contains(",")){//此时为多预警
				StringBuffer inSqlBuffer = new StringBuffer();
				Boolean isFir = true;
				String [] warnnames = warnname.split(regex);
				for (String val : warnnames) {
					if(isFir){
						isFir = false;
					}else{
						inSqlBuffer.append(",");
					}
					inSqlBuffer.append("?");
					sqlStr.add(val);
				}
				whereSql.append(" and p.warning_code  in ("+inSqlBuffer.toString()+")");
			}else{
				whereSql.append(" and p.warning_code = ? ");
				sqlStr.add(warnname.trim());
			}
		}
		if (AppUtils.StringUtil(warningLevel) != null) {
			whereSql.append(" and p.warning_level = ? ");
			sqlStr.add(warningLevel.trim());
		}
		if (AppUtils.StringUtil(indentifyStatus) != null) {
			whereSql.append(" and i.indentify_status = ? ");
			sqlStr.add(indentifyStatus.trim());
		}
		if (AppUtils.StringUtil(checkStat) != null) {
			if("1".equals(roleLevel)){
				whereSql.append(" and i.last_check_stat = ? and i.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
				sqlStr.add(checkStat.trim());
				sqlStr.add("000000000");
				 sqlStr.add("%" + "000000000"+ "%");
			}else{
				whereSql.append(" and i.last_check_stat = ? and case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no "+
						" when '3' then lvl_4_branch_no end =?");
				sqlStr.add(checkStat.trim());
				sqlStr.add(sjfw.trim());
			}
			
		}		
		if (AppUtils.StringUtil(isKeyWarning) != null) {
			whereSql.append(" and p.is_key_warning = ? ");
			sqlStr.add(isKeyWarning.trim());
		}
		if (AppUtils.StringUtil(approvalStat) != null) {
			if("1".equals(roleLevel)){
				whereSql.append(" and i.last_approval_stat= ?  and  i.last_approver_org in (select id from sys_org_info where id = ? or by5 like ?) ");
				sqlStr.add(approvalStat.trim());
				sqlStr.add("000000000");
				sqlStr.add("%" + "000000000"+ "%");
			}
			else
			{
				whereSql.append(" and i.last_approval_stat = ? and case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no "+
						" when '3' then lvl_4_branch_no end =?");
				sqlStr.add(approvalStat.trim());
				sqlStr.add(sjfw.trim());
			}
//			String userorg=getCurrentUser().getStr("ORG_ID");		//本机构审批的
//			sqlStr.add(sjfw.trim());
//			sqlStr.add("%" +userorg.trim()+ "%");
		}
		if (AppUtils.StringUtil(isQuestion) != null) {
			whereSql.append(" and i.is_question  = ? ");
			sqlStr.add(isQuestion.trim());
		}
		extrasql += whereSql.toString() +" order by p.warning_name,i.create_time,i.flownum";
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectSql", selectSql);
		map.put("extrasql", extrasql);
		map.put("sqlStr", sqlStr);
		// 打印日志
		log.info("organSql--map:" + map);
		return map;
	}
	
	//获取已确定的重点预警信号
	public void getImportantData(){
		// 获取当前用户所属机构
		//String userNo = getCurrentUser().getStr("USER_NO");
		String orgnum = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2 from sys_org_info  where orgnum ='"+orgnum+"'  and  stat ='1'");
		String orgLevel = orgRe.getStr("by2");
		
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		//判断当前人所选的首页0管理员首页，1运营专家首页，网点首页
		String roleid=getCurrentUser().getStr("ROLEID");
		String model ="";
		String pageInfo="";
		if(roleid !=null){
		       pageInfo=Db.findFirst("select t.main_page from sys_role_info t where t.id='"+roleid+"' ").getStr("main_page");
			if(pageInfo !=null){
				if("1".equals(pageInfo)){
					model=rd.getStr("MODEL");
				}
			}
		}
		
		List<Record> list = null;
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String time =format.format(date);
		String startTime = time.substring(0,8)+"000000";
		int sum = 0;
		if("1".equals(pageInfo)){
			//网点的只查当前网点的
			if("4".equals(orgLevel)){
				list = Db.find(" select t.deptno,(select orgname from sys_org_info where orgnum = t.deptno) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where t.deptlevel = '"+orgLevel+"' "+
						"   and  (d.is_use is  null or d.is_use = '1') and d.warning_dimension !='2' and d.busi_module = '"+model+"' and  "+
						//" d.is_key_warning='1'  and t.deptno = '"+orgnum+"'   and t.create_time >=to_char(sysdate-1,'yyyyMMddHH24:MI:SS') group by  deptno  ");
						" d.is_key_warning='1'  and t.deptno = '"+orgnum+"'   and t.create_time >='"+startTime+"'  group by  deptno   order by total desc ");
			}else if("3".equals(orgLevel)){
				list = Db.find(" select t.lvl_4_branch_no deptno ,(select orgname from sys_org_info where orgnum = t.lvl_4_branch_no) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where "+
						"  (d.is_use is  null or d.is_use = '1') and d.warning_dimension !='2' and d.busi_module = '"+model+"'  and  "+
						" d.is_key_warning='1'  and t.lvl_4_branch_no in (select orgnum from sys_org_info where (id = '"+orgnum+"' or by5 like '%"+orgnum+"%') and stat = '1')  and t.create_time >='"+startTime+"'  group by lvl_4_branch_no  order by total desc ");
			}
			else if("2".equals(orgLevel)){
				list = Db.find(" select t.lvl_3_branch_no deptno,(select orgname from sys_org_info where orgnum = t.lvl_3_branch_no) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where "+
						"   (d.is_use is  null or d.is_use = '1') and d.warning_dimension !='2' and d.busi_module = '"+model+"'  and  "+
						" d.is_key_warning='1'  and t.lvl_3_branch_no in (select orgnum from sys_org_info where (id = '"+orgnum+"' or by5 like '%"+orgnum+"%') and stat = '1')  and t.create_time >='"+startTime+"' group by lvl_3_branch_no  order by total desc ");
			}
			else if("1".equals(orgLevel)){
				list = Db.find(" select t.lvl_2_branch_no deptno,(select orgname from sys_org_info where orgnum = t.lvl_2_branch_no) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where "+
						"  (d.is_use is  null or d.is_use = '1') and d.warning_dimension !='2' and d.busi_module = '"+model+"'  and  "+
						" d.is_key_warning='1'  and t.lvl_2_branch_no in (select orgnum from sys_org_info where (id = '"+orgnum+"' or by5 like '%"+orgnum+"%') and stat = '1')  and t.create_time >='"+startTime+"' group by lvl_2_branch_no  order by total desc ");
			}
		}else{
			//网点的只查当前网点的
			if("4".equals(orgLevel)){
				list = Db.find(" select t.deptno,(select orgname from sys_org_info where orgnum = t.deptno) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where t.deptlevel = '"+orgLevel+"' "+
						"   and  (d.is_use is  null or d.is_use = '1') and d.warning_dimension !='2' and  "+
						//" d.is_key_warning='1'  and t.deptno = '"+orgnum+"'   and t.create_time >=to_char(sysdate-1,'yyyyMMddHH24:MI:SS') group by  deptno  ");
						" d.is_key_warning='1'  and t.deptno = '"+orgnum+"'   and t.create_time >='"+startTime+"'  group by  deptno   order by total desc ");
			}else if("3".equals(orgLevel)){
				list = Db.find(" select t.lvl_4_branch_no deptno ,(select orgname from sys_org_info where orgnum = t.lvl_4_branch_no) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where "+
						"  (d.is_use is  null or d.is_use = '1')  and d.warning_dimension !='2' and  "+
						" d.is_key_warning='1'  and t.lvl_4_branch_no in (select orgnum from sys_org_info where (id = '"+orgnum+"' or by5 like '%"+orgnum+"%') and stat = '1')  and t.create_time >='"+startTime+"'  group by lvl_4_branch_no  order by total desc ");
			}
			else if("2".equals(orgLevel)){
				list = Db.find(" select t.lvl_3_branch_no deptno,(select orgname from sys_org_info where orgnum = t.lvl_3_branch_no) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where "+
						"   (d.is_use is  null or d.is_use = '1') and d.warning_dimension !='2'  and  "+
						" d.is_key_warning='1'  and t.lvl_3_branch_no in (select orgnum from sys_org_info where (id = '"+orgnum+"' or by5 like '%"+orgnum+"%') and stat = '1')  and t.create_time >='"+startTime+"' group by lvl_3_branch_no  order by total desc ");
			}
			else if("1".equals(orgLevel)){
				list = Db.find(" select t.lvl_2_branch_no deptno,(select orgname from sys_org_info where orgnum = t.lvl_2_branch_no) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where "+
						"  (d.is_use is  null or d.is_use = '1')  and d.warning_dimension !='2' and  "+
						" d.is_key_warning='1'  and t.lvl_2_branch_no in (select orgnum from sys_org_info where (id = '"+orgnum+"' or by5 like '%"+orgnum+"%') and stat = '1')  and t.create_time >='"+startTime+"' group by lvl_2_branch_no  order by total desc ");
			}
		}
	
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				sum +=Integer.valueOf(list.get(i).get("total").toString()); 
			}
		}
		String key = "dop_morenum";
		String val = "01";
		String remark = Db.queryStr("select remark from SYS_PARAM_INFO where key = ? and val = ? ",
				new Object[] { key, val });
		setAttr("disnum",remark);
		setAttr("data",list);
		setAttr("sum",sum);
		System.out.println("共有"+sum+"条数据");
		
		renderJson();
	}

	//获取已确定的重点预警信号(分模块)
	public void getImportantDataByModel(){
		// 获取当前用户所属机构
		//String userNo = getCurrentUser().getStr("USER_NO");
		String orgnum = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2 from sys_org_info  where orgnum ='"+orgnum+"'  and  stat ='1'");
		String orgLevel = orgRe.getStr("by2");
		List<Record> list = null;
		int sum = 0;
		//网点的只查当前网点的
		if("4".equals(orgLevel)){
			list = Db.find(" select t.deptno,(select orgname from sys_org_info where orgnum = t.deptno) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where t.deptlevel = '"+orgLevel+"' "+
					" and t.warning_status = '1'  and  (d.is_use is  null or d.is_use = '1') and t.indentify_status='1' and d.warning_dimension !='2' and t.last_check_stat = '1'  and t.last_approval_stat = '1' and  "+
					" d.is_key_warning='1' and d.busi_module='"+model+"'  and t.deptno = '"+orgnum+"'   and t.create_time >=to_char(sysdate-1,'yyyyMMddHH24:MI:SS') group by  deptno  ");
		}else{
			list = Db.find(" select t.deptno,(select orgname from sys_org_info where orgnum = t.deptno) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where t.deptlevel = '"+orgLevel+"' "+
					" and t.warning_status = '1' and  (d.is_use is  null or d.is_use = '1')  and t.indentify_status='1' and t.last_check_stat = '1' and d.warning_dimension !='2' and t.last_approval_stat = '1' and  "+
					" d.is_key_warning='1' and d.busi_module='"+model+"'  and t.deptno in (select orgnum from sys_org_info where upid = '"+orgnum+"' and stat = '1')  and t.create_time >=to_char(sysdate-1,'yyyyMMddHH24:MI:SS') group by deptno");
		}
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				sum +=Integer.valueOf(list.get(i).get("total").toString()); 
			}
		}
		String key = "dop_morenum";
		String val = "01";
		String remark = Db.queryStr("select remark from SYS_PARAM_INFO where key = ? and val = ? ",
				new Object[] { key, val });
		setAttr("disnum",remark);
		setAttr("data",list);
		setAttr("sum",sum);
		
		
		renderJson();
	}
	
	//获取我的关注的数据
	public void getMyFocus(){
		String userno = getCurrentUser().getStr("id");
//		String roleLvl = getCurrentUser().getStr("role_level");
		String  maxPermiOrgnum =  getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		String remarks=Db.findFirst("select remark from SYS_PARAM_INFO t where t.key='NearDate' and t.status='1' ").get("remark");
		int date=Integer.parseInt(remarks);
		//先查出关注的预警
		String selectSql = "select followed_org, max(follower) follower, busi_module, sub_busi_code, mark_code, follow_type, followed_teller from (select case when t.followed_org is null then '"+ maxPermiOrgnum +"' else t.followed_org end followed_org, t.follower, t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where (follower = ?  and t.follow_type = '0') " + "or (t.assigned_type = '1' and t.follow_type = '0'))" +
				"  group by followed_org,busi_module,sub_busi_code,mark_code,follow_type,followed_teller order by followed_org,follow_type desc ";
		List<Record> lr = Db.find(selectSql,userno);//0代表预警
		String org = null;
		Map<String,String> orgMap = new HashMap<>();
		List<Record> dataList = new ArrayList<>();
		if(lr != null && lr.size() > 0){
			for(Record re : lr){
				//机构中模块的数据
					org = re.getStr("followed_org");
					 
					orgMap.put(org, "org_id");
				String followed_teller = re.getStr("followed_teller");
				
				String sub_busi_code = re.getStr("sub_busi_code");
				String mark_code=re.getStr("mark_code");
				if (mark_code.equals("ZN901003")) {
					System.out.println(re);
				}
				String followed_org = re.getStr("followed_org");
				String busi_module = re.getStr("busi_module");
				
				
				String extraSql = " from dop_warning_info i left join dop_warning_param p on" +
						" p.warning_code=i.warning_code where i.deptno in (select id from sys_org_info where id = ? or by5 like ?)  and p.busi_module=?  " +
						"and  (p.is_use is  null or p.is_use = '1')  and p.warning_type_code= ?  and p.warning_code= ? and i.create_time >= to_char(" +
						"sysdate-'"+date+"','yyyyMMddHH24:MI:SS') and p.warning_dimension !='2'";
				String wheresql = " ";
				if(followed_teller != null){
					wheresql = " and  i.teller_no = '"+followed_teller+"' ";
				}
				List<Record> warning = Db.find("select distinct (select orgname from sys_org_info where orgnum = ? and stat=1) orgname, " +
						" 	(select remark from sys_param_info where key = 'dop_ywtype' and val=p.busi_module) name, " +
						//" 	(select remark from DOP_GANGEDMENU_INFO where  val= p.warning_type_code) ywzlname, " +
						" 	(select warning_name from dop_warning_param where  warning_code=p.warning_code and warning_dimension !='2' ) warncodename, " +//预警名称
						"   (select count(i.is_question)" + extraSql + wheresql +"  and i.warning_status='1' and is_question = '1' ) isquestioncount ,"+
						" 	(select d.name from sys_user_info d where d.user_no = i.teller_no ) username,(select count(1) " + extraSql + wheresql +"  and " +
						"  i.warning_status='1' ) warningcount " +extraSql ,followed_org,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code);
				if(warning != null && warning.size() > 0){
					if(followed_teller != null){
						warning.get(0).set("orgid", re.getStr("followed_org"));
						warning.get(0).set("date", date);
						warning.get(0).set("busi_module", busi_module);
						warning.get(0).set("mark_code", mark_code);
						warning.get(0).set("userno", followed_teller);
					}else{
						warning.get(0).set("orgid", re.getStr("followed_org"));
						warning.get(0).set("date", date);
						warning.get(0).set("busi_module", busi_module);
						warning.get(0).set("mark_code", mark_code);
					}
					dataList.add(warning.get(0));
				}
				org=followed_org;
				
			}
		}
		String pftjSql = "select followed_org, max(follower) follower, busi_module, sub_busi_code, mark_code, follow_type, followed_teller from (select case when t.followed_org is null then '"+ maxPermiOrgnum +"' else t.followed_org end followed_org, t.follower, t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where (follower = ?  and t.follow_type = '1') " +"or (t.assigned_type = '1' and t.follow_type = '1'))" +
				"  group by followed_org,busi_module,sub_busi_code,mark_code,follow_type,followed_teller order by followed_org,follow_type desc ";
		List<Record> pflr = Db.find(pftjSql,userno);//1代表评分
		if(pflr != null && pflr.size() > 0){
			//拼接完结束
			String busimodule=" ";
			for (Record record : pflr) {
				String pforg = record.getStr("followed_org");
				//if(!orgMap.containsKey(pforg)){
					String pfSql = " from dop_org_score_detail d left join sys_org_info o on o.id=d.deptno where deptno=?  and data_month >= to_char(add_months(sysdate,-2),'yyyyMM') and indexname='ZF' and module=? order by d.data_month desc,grow_rate desc ";
					List<Record> scoreTempList = Db.find("select  d.data_month,d.deptno,o.orgname,d.score,case lag(score,1)over(order by data_month) when 0 then 0 else score/lag(score,1)over(order by data_month)-1 end grow_rate "
							+pfSql,pforg,record.get("busi_module"));
					busimodule=record.get("busi_module");
					if(scoreTempList != null && scoreTempList.size() > 0){
						Record lastMonthRecord = scoreTempList.get(0);//上个月得分
						Record r = appendScoreStr(lastMonthRecord,busimodule);
						if (r != null) {
							r.set("orgid", pforg);
							dataList.add(r);
						}
					}
				//}
			}
		}
		String key = "dop_morenum";
		String val = "01";
		String remark = Db.queryStr("select remark from SYS_PARAM_INFO where key = ? and val = ? ",
				new Object[] { key, val });
		setAttr("disnum",remark);
		setAttr("data", dataList);
		renderJson();
	}
	private Record appendScoreStr(Record lastMonthRecord,String busi_module){
		
		//获取业务模块名称
		String ywmkname=Db.findFirst("  select remark from sys_param_info where key='dop_ywtype'  and val = ? ", busi_module).getStr("remark");
		Record record = new Record();
		String lastMonth = lastMonthRecord.getStr("data_month").substring(4);
		//将个位数月份0去掉，01月>>1月
		if(lastMonth.substring(0, 1).equals("0")){
			lastMonth = lastMonth.substring(1);
		}
		BigDecimal growRate = lastMonthRecord.getBigDecimal("grow_rate");
		String growFlag = "";
		if(growRate !=null){
		try{
			if(growRate.compareTo(new BigDecimal(0))==-1){
				growRate = growRate.multiply(new BigDecimal(-1));
				growFlag = "下降";
			}else{
				growFlag = "增长";
			}
			growRate = growRate.multiply(new BigDecimal(100));
			growRate = growRate.setScale(2,BigDecimal.ROUND_HALF_UP);//保留两位小数，四舍五入
			record.set("orgname", lastMonthRecord.getStr("orgname")).set("ywmkname", ywmkname).set("lastMonth", lastMonth).set("score", lastMonthRecord.getBigDecimal("score").toString()).set("growFlag", growFlag).set("growRate", growRate);
		}catch(Exception e){
			System.out.println("WarningSearchCtl.getMyFocus()>>>>>>>>>>> growRate空指针异常，原因：未获得上月数据");
			e.printStackTrace();
		}
		} else {
			return null;
		}
		return record;
		
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
	
	public void getRemark(){
		String warning_code = getPara("warning_code");
		String remark = Db.findFirst("select remark from dop_warning_param t where t.warning_code='"+warning_code+"' and t.warning_dimension !='2' ").get("remark");
		setAttr("remark",remark);
		renderJson();
	}
	//点击查询查复按钮时判定查复是否发起
	public void searchCf(){
		
		String flownum=getPara("flownum");
		String check_flownum=flownum;
		String is_over_due="";//0-超时,1-未超时
		String id=AppUtils.getStringSeq();
		String cxcfStatus="";
		String check_nameid="";
		
		String check_model="";
		String check_warningname="";
		//查询业务模块 预警名称
		String sqlstr="select f.busi_module,f.warning_name from  dop_warning_info t left join dop_warning_param f  on t.warning_code=f.warning_code where flownum='"+flownum+"' and f.warning_dimension !='2'";
		 Record listStr= Db.findFirst(sqlstr);
		if(listStr !=null){
			check_model=listStr.getStr("busi_module");
			check_warningname=listStr.getStr("warning_name");
		}
		
		//判断查询查复是否发起
		String sql="select t.search_check_status ,t.check_nameid from  dop_search_check t where check_flownum=? ";
		Record list=Db.findFirst(sql,check_flownum);
		if(list !=null){
			cxcfStatus=list.get("search_check_status");
			if(cxcfStatus==null){
				cxcfStatus="";
			}
			check_nameid=list.get("check_nameid");
			if(check_nameid==null){
				check_nameid="";
			}
		}else{
			is_over_due="1";
			Db.update(" insert into dop_search_check( id,check_flownum ,search_check_status,check_nameid,check_model,check_warningname,is_over_due)values(?,?,?,?,?,?,?)"
					,new Object[]{id,check_flownum,cxcfStatus,check_nameid,check_model,check_warningname,is_over_due});
		}
		
		setAttr("cxcfStatus",cxcfStatus);
		setAttr("check_name",check_nameid);
		
		renderJson();
	}
	
	public void tosearch() {
		String orgname = getPara("orgname");
		String warning_type_code = getPara("warning_type_code");
		try {
			orgname = URLDecoder.decode(getPara("orgname"),"utf-8");
			warning_type_code = URLDecoder.decode(getPara("warning_type_code"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String busi_module = getPara("busi_module");
		String warning_code = getPara("warning_code");
		String start_time = getPara("start_time");
		String end_time = getPara("end_time");
		String start_time1 = getPara("start_time1");
		String end_time1 = getPara("end_time1");
		Record re = new Record();
		
		if (AppUtils.StringUtil(start_time) != null && AppUtils.StringUtil(end_time) != null ) {
			start_time=start_time.substring(0,10);
			end_time=end_time.substring(0,10);
			setAttr("start_time", start_time);
			setAttr("end_time", end_time);
			re.set("start_time", start_time);
			re.set("end_time", end_time);
		}
		if (AppUtils.StringUtil(start_time1) != null && AppUtils.StringUtil(end_time1) != null ) {
			start_time1=start_time1.substring(0,10);
			end_time1=end_time1.substring(0,10);
			setAttr("biz_start_time", start_time1);
			setAttr("biz_end_time", end_time1);
			re.set("biz_start_time", start_time1);
			re.set("biz_end_time", end_time1);
		}
		
		Record orgrd=Db.findFirst("select id from sys_org_info where orgname=?",orgname);
		String orgid=orgrd.getStr("id");
		
		Record warnrd=Db.findFirst("select id from dop_gangedmenu_info where upid=" +
				"(select id from dop_gangedmenu_info where upid=(select id from dop_gangedmenu_info where val='1003') and val=?) and remark=?",busi_module,warning_type_code);
		
		String warning_type=warnrd.getStr("id");
		
		
		
		re.set("orgid",orgid);
		re.set("ywtype",busi_module);
		re.set("warning_type", warning_type);
		re.set("warn_name", warning_code);
		setAttr("datas", re.toJson());
		setAttr("flag", "1");
		setAttr("org", orgid);
		setAttr("ywtype", busi_module);
		setAttr("warning_type", warning_type);
		setAttr("warn_name", warning_code);
		render("index.jsp");
	}
	
	public void toIndexFromMyFollowOfIndexPage() {
		String orgid = getPara("orgid");
		String ywtype = getPara("ywtype");
		String mark_code = getPara("warn_name");
		String date = getPara("date");
		String userno = getPara("userno");
		String username = getPara("username");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -Integer.parseInt(date));
		String start_time = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		Record r = Db.findFirst("select busi_module, DOP_GANGEDMENU_INFO.id type from dop_warning_param left join DOP_GANGEDMENU_INFO on warning_type_code = val where warning_code = ?", mark_code);
		String warning_type=r.get("type");
		setAttr("org", orgid);
		if (AppUtils.StringUtil(userno) != null) {
			setAttr("userno", userno);
			setAttr("username", username);
		}
		setAttr("ywtype", ywtype);
		setAttr("warning_type", warning_type);
		setAttr("warn_name", mark_code);
		setAttr("start_time", start_time);
		setAttr("flag", "2");
		render("index.jsp");
	}
}
