package com.goodcol.controller.dop;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;

import com.goodcol.controller.BaseCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;

/**
 * 预警信息统计
 * @author 张强强
 * @date 2018-11-19
 *
 */
@RouteBind( path = "/warningcount")
@Before({ManagerPowerInterceptor.class})
public class WarningCountCtl extends BaseCtl{
	public static Logger log = Logger.getLogger(WarningSearchCtl.class);
	@Override
	@Before(PermissionInterceptor.class)
	public void index() {
		String key = getPara("key");
		String style = getPara("style");
		//和数据权限有关的  加最大权限机构条件 
				String  orgid =  getCurrentUser().getStr("MAX_PERMI_ORGNUM");
				
				if(AppUtils.StringUtil(key)!= null){
					if(AppUtils.StringUtil(style)!= null && "0".equals(style)){
						orgid = key;
					}
				}
				String orgnum = getPara("orgnum");
				if(AppUtils.StringUtil(orgnum) != null){
					orgid = orgnum;
				}
				setAttr("org", orgid);
		setAttr("startime", DateTimeUtil.getLastMonthFirstDay("yyyy-MM-dd"));
		setAttr("endtime", DateTimeUtil.getLastMonthEndDay("yyyy-MM-dd"));
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
		Page<Record> page = Db.paginate(pageNum, pageSize, map.get("selectSql").toString(), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	public void getListByExport(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
			
		Map<String, Object> map = organSql("2");
		@SuppressWarnings("unchecked")
		Page<Record> page = Db.paginate(pageNum, pageSize, map.get("selectSql").toString(), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
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
					" and i.indentify_status ='0' and  (p.is_use is  null or p.is_use = '1') and p.warning_dimension !='2' ";
			
			indentWarning = Db.queryBigDecimal(sql);
		}
		
		
		String extrasql = " from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code " +
				" left join dop_gangedmenu_info g on g.val=p.warning_type_code  ";
		
		String whereSql2 = " where  (i.last_check_stat='0'  ) and p.warning_dimension !='2' and  (p.is_use is  null or p.is_use = '1') and  (p.is_use is  null or p.is_use = '1') and (select case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no "+
					" when '3' then lvl_4_branch_no end checker_level from dop_warning_info i2 where i2.id=i.id)='"+sjfw+"' and p.warning_dimension !='2'";
		//待核查
		checkWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql2);
		
		String whereSql3 = "where p.is_confirm='1' and  (p.is_use is  null or p.is_use = '1') and i.last_approval_stat='0'  and " +
			"i.last_approver_org ='"+sjfw+"' and p.warning_dimension !='2'";
				//		" i.LAST_CHECKER_ORG in (select id from sys_org_info start with orgnum = '"+user.getStr("orgnum")+"' connect by prior orgnum=upid)" ;
		//待审批
		approvalWarning = Db.queryBigDecimal(selectSql+extrasql+whereSql3);
		//待认定(界面：warning)
		setAttr("indentWarning",indentWarning.intValue());
		//核查(界面：manager)
		setAttr("checkWarning",checkWarning.intValue());
		//审批(界面：manager) 
		setAttr("approvalWarning",approvalWarning.intValue());
		renderJson();
	}
	/**
	 * 导出Excel表格
	 */
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
	public Map<String, Object> organSql(String type) {// 获取页面输入查询条件
		Record user = getCurrentUser();
		//默认当前用户层级
		String orgid = getPara("orgid");
		String userorg=getCurrentUser().getStr("ORG_ID");
		String ywType = getPara("ywtype"); 
		String startTime = getPara("start_time");
		String endTime = getPara("end_time");
		String startTime1 = getPara("start_time1");
		String endTime1 = getPara("end_time1");
		String followed_teller = getPara("followed_teller");
		String warningStatus = getPara("warning_status");
		String warningType = getPara("warning_type");
		String warnname = getPara("warn_name");
		String warningLevel = getPara("warning_level");
		String roleLevel = user.getStr("ROLE_LEVEL");
		String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");//数据范围,不能取用户自己所在的机构，而要按照用户的数据范围、角色级别 来查， 数据范围优先
		if("1".equals(type)){
			ywType = getAttr("ywtype"); 
			startTime = getAttr("start_time");
			endTime = getAttr("end_time");
			warningStatus = getAttr("warning_status");
			warningType = getAttr("warning_type");
			warningLevel = getAttr("warning_level");
		}
		StringBuffer selectSql = new StringBuffer();
		selectSql.append( " select min(p.warning_name) warning_name," +
				"i.warning_code," );
		if(StringUtils.isNotEmpty(orgid)){
			selectSql.append("(select orgname from sys_org_info t where t.orgnum='" +orgid+
			"') orgname,");
		}
		selectSql.append(" min(p.warning_level) warning_level, p.busi_module,min(g.remark) warning_type_code,min(p.is_key_warning) is_key_warning," +
				" min(case  when p.is_confirm = '1' then '是' else  '否' end) is_confirm," +
				" min(i.create_time) create_time,min(case  when p.is_manual_indentify = '1' then '是' else '否' end) is_manual_indentify," +
				" count(1) as count, sum(case when (indentify_status = '1' or indentify_status = '2' or indentify_status = '0') then" +
				" 1 else 0 end) count_indentify, sum(case  when indentify_status = '0' and last_check_stat is null and last_approval_stat is null and is_question is null then" +
				" 1 else 0 end) on_indentify,sum(case  when indentify_status = '1'  then 1 else  0 end) indentify," +
				" sum(case when indentify_status = '2' then 1 else 0  end) not_indentify," +
				" sum(case when indentify_status = '9' then  1 else 0 end) no_indentify,");
			if("1".equals(roleLevel)){
				selectSql.append(" sum(case when last_check_stat = '0' and i.deptno in (select id from sys_org_info where id = '000000000' or by5 like '%000000000%') then 1 else 0 end) on_check," +
						" sum(case  when last_check_stat = '1'  and i.deptno in (select id from sys_org_info where id = '000000000' or by5 like '%000000000%')  then 1 else  0 end) last_check," +
						" sum(case  when last_check_stat = '2'  and i.deptno in (select id from sys_org_info where id = '000000000' or by5 like '%000000000%') then   1 else  0 end) last_not_check," +
						" sum(case  when (last_check_stat = '2' or last_check_stat = '1' or last_check_stat = '0')  and i.deptno in (select id from sys_org_info where id = '000000000' or by5 like '%000000000%') then 1 else 0 end) count_check,");
			}else{
				selectSql.append(" sum(case when last_check_stat = '0' and " +
						"(case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no when '2' then lvl_3_branch_no when '3' then " +
						"lvl_4_branch_no end = '"+sjfw+"') then 1 else 0 end) on_check," +
						" sum(case  when last_check_stat = '1'  and " +
						"(case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no when '2' then lvl_3_branch_no when '3' then " +
						"lvl_4_branch_no end = '"+sjfw+"') then 1 else  0 end) last_check," +
						" sum(case  when last_check_stat = '2'  and " +
						"(case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no when '2' then lvl_3_branch_no when '3' then " +
						"lvl_4_branch_no end = '"+sjfw+"') then   1 else  0 end) last_not_check," +
						" sum(case  when (last_check_stat = '2' or last_check_stat = '1' or last_check_stat = '0')  and " +
						"(case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no when '2' then lvl_3_branch_no when '3' then " +
						"lvl_4_branch_no end = '"+sjfw+"') then 1 else 0 end) count_check,");
			}
			if("1".equals(roleLevel)){
				selectSql.append(" sum(case when last_approval_stat = '0' and i.deptno in (select id from sys_org_info where id = '000000000' or by5 like '%000000000%') then 1 else 0 end) on_last_approval," +
						" sum(case  when last_approval_stat = '1'  and i.deptno in (select id from sys_org_info where id = '000000000' or by5 like '%000000000%')  then 1 else  0 end) last_approval," +
						" sum(case  when last_approval_stat = '2'  and i.deptno in (select id from sys_org_info where id = '000000000' or by5 like '%000000000%') then   1 else  0 end) last_not_approval," +
						" sum(case  when (last_approval_stat = '2' or last_approval_stat = '1' or last_approval_stat = '0')  and i.deptno in (select id from sys_org_info where id = '000000000' or by5 like '%000000000%') then 1 else 0 end) count_approval,");

				/*
				selectSql.append(" sum(case  when last_approval_stat = '0' and (last_approver_org='"+userorg+"' or last_approver_org='"+sjfw+"') then   1 else 0 end) on_last_approval," +
						" sum(case  when last_approval_stat = '1' and (last_approver_org='"+userorg+"' or last_approver_org='"+sjfw+"')  then 1 else 0 end) last_approval," +
						" sum(case   when last_approval_stat = '2' and (last_approver_org='"+userorg+"' or last_approver_org='"+sjfw+"') then  1 else  0 end) last_not_approval," +
						" sum(case  when (last_approval_stat = '2' or last_approval_stat = '1' or last_approval_stat='0') and (last_approver_org='"+userorg+"' or last_approver_org='"+sjfw+"') then 1 else 0 end) count_approval," 
						);
						*/
			}
			else
			{
				selectSql.append(" sum(case when last_approval_stat = '0' and " +
						"(case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no when '2' then lvl_3_branch_no when '3' then " +
						"lvl_4_branch_no end = '"+sjfw+"') then 1 else 0 end) on_last_approval," +
						" sum(case  when last_approval_stat = '1'  and " +
						"(case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no when '2' then lvl_3_branch_no when '3' then " +
						"lvl_4_branch_no end = '"+sjfw+"') then 1 else  0 end) last_approval," +
						" sum(case  when last_approval_stat = '2'  and " +
						"(case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no when '2' then lvl_3_branch_no when '3' then " +
						"lvl_4_branch_no end = '"+sjfw+"') then   1 else  0 end) last_not_approval," +
						" sum(case  when (last_approval_stat = '2' or last_approval_stat = '1' or last_approval_stat = '0')  and " +
						"(case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no when '2' then lvl_3_branch_no when '3' then " +
						"lvl_4_branch_no end = '"+sjfw+"') then 1 else 0 end) count_approval,");
//				selectSql.append(" sum(case  when last_approval_stat = '0' and (last_approver_org='"+userorg+"' or last_approver_org='"+sjfw+"') then   1 else 0 end) on_last_approval," +
//						" sum(case  when last_approval_stat = '1' and (last_approver_org='"+userorg+"' or last_approver_org='"+sjfw+"')  then 1 else 0 end) last_approval," +
//						" sum(case   when last_approval_stat = '2' and (last_approver_org='"+userorg+"' or last_approver_org='"+sjfw+"') then  1 else  0 end) last_not_approval," +
//						" sum(case  when (last_approval_stat = '2' or last_approval_stat = '1' or last_approval_stat='0') and (last_approver_org='"+userorg+"' or last_approver_org='"+sjfw+"') then 1 else 0 end) count_approval," 
//						);
			}
			selectSql.append("sum(case when is_question = '1' then 1 else 0 end) is_question," +
					" sum(case when is_question = '0' then   1 else  0 end) is_not_question," +
					" sum(case when indentify_status is not null and last_check_stat is null  and is_question = '1' then 1 else 0  end) check_question," +
					" sum(case when indentify_status is not null and last_check_stat is null  and is_question = '0' then 1 else 0  end) check_not_question," +
					" sum(case when indentify_status is not null and last_check_stat is not null  and is_question = '1' then 1 else  0 end) approve_question," +
					" sum(case  when indentify_status is not null and last_check_stat is not null   and is_question = '0' then 1 else 0 end) approve_not_question"
					);
		//预警类型id
		String warningTypeIds = Db.findFirst("select listagg(id,''',''') within group (order by id) ids from dop_gangedmenu_info start with val='1003' connect by prior id =upid ").getStr("ids");
		String extrasql = " from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code " +
				" left join (select * from dop_gangedmenu_info k where k.id in ('"+warningTypeIds+"')) g on g.val=p.warning_type_code ";
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
		//开始时间
		if(AppUtils.StringUtil(startTime1)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			whereSql.append(" and i.DATA_DATE >= to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMdd') ");
			sqlStr.add(startTime1);
		}
		//结束时间
		if(AppUtils.StringUtil(endTime1)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			whereSql.append(" and i.DATA_DATE <=  to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMdd')  ");
			sqlStr.add(endTime1);
		}
		if (AppUtils.StringUtil(warningStatus) != null) {
			whereSql.append(" and i.warning_status = ? ");
			sqlStr.add(warningStatus.trim());
		}
//		if (AppUtils.StringUtil(orgid) != null) {
//			whereSql.append(" and i.deptno in (select id from sys_org_info start with orgnum = '"+orgid+"' connect by prior orgnum=upid) ");
//		}
		
		if(AppUtils.StringUtil(followed_teller) != null){
			whereSql.append( " and  substr(i.teller_no,14,7) = ? ");
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
				whereSql.append(" and i.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
				sqlStr.add(sjfw.trim());
				sqlStr.add("%" + sjfw.trim()+ "%");
		}
		if (AppUtils.StringUtil(warningType) != null) {
			warningType=Db.findFirst("select val from dop_gangedmenu_info where id=?",warningType).getStr("val");
			whereSql.append(" and p.warning_type_code = ? ");
			sqlStr.add(warningType.trim());
		}
		if (AppUtils.StringUtil(warnname) != null) {
			whereSql.append(" and p.warning_code = ? ");
			sqlStr.add(warnname.trim());
		}
		if (AppUtils.StringUtil(warningLevel) != null) {
			whereSql.append(" and p.warning_level = ? ");
			sqlStr.add(warningLevel.trim());
		}
		/*if (AppUtils.StringUtil(sjfw) != null) {
			whereSql.append("  and case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no "+
					" when '3' then lvl_4_branch_no end =?");
			sqlStr.add(sjfw.trim());
		}*/
		extrasql += whereSql.toString() +" group by i.warning_code,p.busi_module  order by p.busi_module desc,count desc";
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
		List<Record> list = null;
		int sum = 0;
		//网点的只查当前网点的
		if("4".equals(orgLevel)){
			list = Db.find(" select t.deptno,(select orgname from sys_org_info where orgnum = t.deptno) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where t.deptlevel = '"+orgLevel+"' "+
					" and t.warning_status = '1'  and  (d.is_use is  null or d.is_use = '1') and t.indentify_status='1' and t.last_check_stat = '1'  and t.last_approval_stat = '1' and  "+
					" d.is_key_warning='1'  and t.deptno = '"+orgnum+"'   and t.create_time >=to_char(sysdate-1,'yyyyMMddHH24:MI:SS') and p.warning_dimension !='2' group by  deptno  ");
		}else{
			list = Db.find(" select t.deptno,(select orgname from sys_org_info where orgnum = t.deptno) as orgname,count(1) as total from dop_warning_info t left join dop_warning_param d on t.warning_code = d.warning_code   where t.deptlevel = '"+orgLevel+"' "+
					" and t.warning_status = '1' and  (d.is_use is  null or d.is_use = '1')  and t.indentify_status='1' and t.last_check_stat = '1'  and t.last_approval_stat = '1' and  "+
					" d.is_key_warning='1'  and t.deptno in (select orgnum from sys_org_info where upid = '"+orgnum+"' and stat = '1')  and t.create_time >=to_char(sysdate-1,'yyyyMMddHH24:MI:SS') and p.warning_dimension !='2' group by deptno");
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
		//先查出关注的预警
		String selectSql = " select t.followed_org,t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where follower = ?  and t.follow_type = '0' " +
				"  group by followed_org,t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller order by followed_org,t.follow_type desc ";
		List<Record> lr = Db.find(selectSql,userno);//0代表预警
		String org = null;
		StringBuffer describe = new StringBuffer(" ");
		Map<String,String> orgMap = new HashMap<>();
		if(lr != null && lr.size() > 0){
			for(Record re : lr){
				//机构中模块的数据
				StringBuffer sbf = new StringBuffer(" ");
				if(org == null){
					org = re.getStr("followed_org");
					orgMap.put(org, "org_id");
				}
				String followed_teller = re.getStr("followed_teller");
				String sub_busi_code = re.getStr("sub_busi_code");
				String mark_code=re.getStr("mark_code");
				String followed_org = re.getStr("followed_org");
				String busi_module = re.getStr("busi_module");
				//当相邻的两个机构号不相同时，则查询评分的数据，如果没有评分数据，则结束当前字段
				if(!org.equals(followed_org)){
					orgMap.put(followed_org, "org_id");
					String pftjSql = " select t.followed_org,t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where follower = ?  and t.follow_type = '1' and  t.followed_org = ? " +
							" and busi_module=? group by followed_org,t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller order by followed_org,t.follow_type desc ";
					List<Record> pflr = Db.find(pftjSql,userno,org,busi_module);//1代表评分
					if(pflr != null && pflr.size() > 0){
						//拼接完结束
						String pfSql = " from dop_org_score_detail d left join sys_org_info o on o.id=d.deptno where " +
								"deptno=?  and data_month >= to_char(add_months(sysdate,-2),'yyyyMM') and indexname='ZF'" +
								" and module=? order by d.data_month desc,grow_rate desc ";
						List<Record> scoreTempList = Db.find("select  d.data_month,d.deptno,o.orgname,d.score,case lag(score,1)over(order by data_month) when 0 then 0 else score/lag(score,1)over(order by data_month)-1 end grow_rate "
								+pfSql,org,busi_module);
						if(scoreTempList != null && scoreTempList.size() > 0){
							Record lastMonthRecord = scoreTempList.get(0);//上个月得分
							sbf.append(appendScoreStr(lastMonthRecord));//拼接该机构评分
						}
					}
				}
				String extraSql = " from dop_warning_info i left join dop_warning_param p on" +
						" p.warning_code=i.warning_code where i.deptno=? and p.busi_module=?  " +
						"and  (p.is_use is  null or p.is_use = '1') and p.warning_dimension !='2'  and p.warning_type_code= ?  and p.warning_code= ? and i.create_time >= to_char(" +
						"sysdate-7,'yyyyMMddHH24:MI:SS') ";
				String wheresql = " ";
				if(followed_teller != null){
					wheresql = " and  i.teller_no = '"+followed_teller+"' ";
				}
				List<Record> warning = Db.find("select distinct (select orgname from sys_org_info where orgnum = i.deptno and stat=1) orgname, " +
						//" 	(select remark from sys_param_info where key = 'dop_ywtype' and val=p.busi_module) name, " +
						//" 	(select remark from DOP_GANGEDMENU_INFO where  val= p.warning_type_code) ywzlname, " +
						" 	(select warning_name from dop_warning_param where warning_code=p.warning_code and warning_dimension !='2' ) warncodename, " +//预警名称
						"   (select count(i.is_question)" + extraSql + wheresql +"  and i.warning_status='1' ) isquestioncount ,"+
						" 	(select d.name from sys_user_info d where d.user_no = i.teller_no ) username,(select count(1) " + extraSql + wheresql +"  and " +
						"  i.warning_status='1' ) warningcount " +extraSql ,followed_org,busi_module,sub_busi_code,mark_code,followed_org,busi_module,sub_busi_code,mark_code,followed_org,busi_module,sub_busi_code,mark_code);
				if(warning != null && warning.size() > 0){
					if(followed_teller != null){
						if("0".equals(warning.get(0).getBigDecimal("warningcount").toString())){
							//sbf.append(warning.get(0).getStr("orgname")+warning.get(0).getStr("username")+warning.get(0).getStr("name")+"中"+warning.get(0).getStr("ywzlname")+"预警。-");
							sbf.append(warning.get(0).getStr("orgname")+warning.get(0).getStr("username")+warning.get(0).getStr("warncodename")
							  +"预警:"+warning.get(0).getBigDecimal("warningcount").toString()+"笔。 确认存在问题:"+warning.get(0).getBigDecimal("isquestioncount").toString()+"笔。-");
						}else{
							//sbf.append(warning.get(0).getStr("orgname")+warning.get(0).getStr("username")+warning.get(0).getStr("name")+"中"+warning.get(0).getStr("ywzlname")+"预警,确认存在问题。-");
							sbf.append(warning.get(0).getStr("orgname")+warning.get(0).getStr("username")+warning.get(0).getStr("warncodename")
									  +"预警:"+warning.get(0).getBigDecimal("warningcount").toString()+"笔。 确认存在问题:"+warning.get(0).getBigDecimal("isquestioncount").toString()+"笔。-");
						}
					}else{
						if("0".equals(warning.get(0).getBigDecimal("warningcount").toString())){
							//sbf.append(warning.get(0).getStr("orgname")+warning.get(0).getStr("name")+"中"+warning.get(0).getStr("ywzlname")+"预警。-");
							sbf.append(warning.get(0).getStr("orgname")+warning.get(0).getStr("warncodename")
						        +"预警:"+warning.get(0).getBigDecimal("warningcount").toString()+"笔。 确认存在问题:"+warning.get(0).getBigDecimal("isquestioncount").toString()+"笔。-");
						}else{
							sbf.append(warning.get(0).getStr("orgname")+warning.get(0).getStr("warncodename")
						    +"预警:"+warning.get(0).getBigDecimal("warningcount").toString()+"笔。 确认存在问题:"+warning.get(0).getBigDecimal("isquestioncount").toString()+"笔。-");
						}
					}
				}
				org=followed_org;
				describe.append(sbf+"+");
			}
		}
		if(org != null){
			String pftjSql = " select t.followed_org,t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where follower = ?  and t.follow_type = '1' and  t.followed_org = ? " +
					" group by followed_org,t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller order by followed_org,t.follow_type desc ";
			List<Record> pflr = Db.find(pftjSql,userno,org);//1代表评分
			StringBuffer sbf = new StringBuffer(" ");
			if(pflr != null && pflr.size() > 0){
				//拼接完结束
				for (Record record : pflr) {
					String pfSql = " from dop_org_score_detail d left join sys_org_info o on o.id=d.deptno where deptno=? " +
							" and data_month >= to_char(add_months(sysdate,-2),'yyyyMM') and indexname='ZF' " +
							" and module = ? order by d.data_month desc,grow_rate desc ";
					List<Record> scoreTempList = Db.find("select  d.data_month,d.deptno,o.orgname,d.score,case lag(score,1)over(order by data_month) when 0 then 0 else score/lag(score,1)over(order by data_month)-1 end grow_rate "
							+pfSql,org,record.get("busi_module"));
					if(scoreTempList != null && scoreTempList.size() > 0){
						Record lastMonthRecord = scoreTempList.get(0);//上个月得分
						sbf.append(appendScoreStr(lastMonthRecord));//拼接该机构评分
					}
				}
			}
			describe.append(sbf+"+");
		}
		String pftjSql = " select t.followed_org,t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where follower = ?  and t.follow_type = '1' " +
				"  group by followed_org,t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller order by followed_org,t.follow_type desc ";
		List<Record> pflr = Db.find(pftjSql,userno);//1代表评分
		StringBuffer sbf = new StringBuffer(" ");
		if(pflr != null && pflr.size() > 0){
			//拼接完结束
			for (Record record : pflr) {
				String pforg = record.getStr("followed_org");
				if(!orgMap.containsKey(pforg)){
					String pfSql = " from dop_org_score_detail d left join sys_org_info o on o.id=d.deptno where deptno=?  and data_month >= to_char(add_months(sysdate,-2),'yyyyMM') and indexname='ZF' and module=? order by d.data_month desc,grow_rate desc ";
					List<Record> scoreTempList = Db.find("select  d.data_month,d.deptno,o.orgname,d.score,case lag(score,1)over(order by data_month) when 0 then 0 else score/lag(score,1)over(order by data_month)-1 end grow_rate "
							+pfSql,pforg,record.get("busi_module"));
					if(scoreTempList != null && scoreTempList.size() > 0){
						Record lastMonthRecord = scoreTempList.get(0);//上个月得分
						sbf.append(appendScoreStr(lastMonthRecord));//拼接该机构评分
					}
				}
			}
		}
		describe.append(sbf+"+");
		String key = "dop_morenum";
		String val = "01";
		String remark = Db.queryStr("select remark from SYS_PARAM_INFO where key = ? and val = ? ",
				new Object[] { key, val });
		setAttr("disnum",remark);
		setAttr("results",describe.toString().replace("-", "</br>"));
		renderJson();
	}
	private String appendScoreStr(Record lastMonthRecord){
		String lastMonth = lastMonthRecord.getStr("data_month").substring(4);
		//将个位数月份0去掉，01月>>1月
		if(lastMonth.substring(0, 1).equals("0")){
			lastMonth = lastMonth.substring(1);
		}
		BigDecimal growRate = lastMonthRecord.getBigDecimal("grow_rate");
		String growFlag = "";
		String rtnStr = "";
		try{
			if(growRate.compareTo(new BigDecimal(0))==-1){
				growRate = growRate.multiply(new BigDecimal(-1));
				growFlag = "下降";
			}else{
				growFlag = "增长";
			}
			growRate = growRate.multiply(new BigDecimal(100));
			growRate = growRate.setScale(2,BigDecimal.ROUND_HALF_UP);//保留两位小数，四舍五入
			if("下降".equals(growFlag)){
				rtnStr = "<span>"+lastMonthRecord.getStr("orgname")+lastMonth+"月运营评分"+lastMonthRecord.getBigDecimal("score").toString()+"分,较上个月"+growFlag+growRate+"%。</span><span class='decline_box'></span>+";
			}else{
				rtnStr = "<span>"+lastMonthRecord.getStr("orgname")+lastMonth+"月运营评分"+lastMonthRecord.getBigDecimal("score").toString()+"分,较上个月"+growFlag+growRate+"%。</span><span class='rise_box'></span>+";
			}
		}catch(Exception e){
			System.out.println("WarningSearchCtl.getMyFocus()>>>>>>>>>>> growRate空指针异常，原因：未获得上月数据");
			e.printStackTrace();
		}
		return rtnStr;
		
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
}
