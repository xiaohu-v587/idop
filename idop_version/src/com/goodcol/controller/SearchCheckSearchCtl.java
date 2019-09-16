package com.goodcol.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;

@RouteBind(path="/searchCheckSearch")
@Before({ManagerPowerInterceptor.class })
public class SearchCheckSearchCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(SearchCheckSearchCtl.class);
	@Override
	public void index() {
		render("scs_index.jsp");
	}

	public void getList() {
		Record user = getCurrentUser();
		String roleLevel = user.getStr("ROLE_LEVEL");
		String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		String userOrg=user.getStr("org_id");
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String bizModule = getPara("biz_module");
		String warningName = getPara("warning_name");
		String searchNo = getPara("search_no");
		String searchStartDate = getPara("search_start_date");
		String searchEndDate = getPara("search_end_date");
		String searchOrg = getPara("search_org");
		String checkOrg = getPara("check_org");
		String checkStartDate = getPara("check_start_date");
		String checkEndDate = getPara("check_end_date");
		String searchCheckStatus = getPara("sc_status");
		String isOverdue = getPara("is_overdue");
			
		//判断数据是否超时
		String is_over_due="";
		String time=BolusDate.getDate();
		List<Record> list=Db.find("select * from dop_search_check ");
		for (Record r : list) {
			String check_flownum=r.getStr("check_flownum");
			String check_enddate=r.getStr("check_enddate");	
			String check_date=r.getStr("check_date");
			String search_check_status=r.getStr("search_check_status");
			if("3".equals(search_check_status)||"4".equals(search_check_status)){
				
			}else{
				if(check_enddate!=null){
					if(check_date==null){
						int s=check_enddate.compareTo(time);
						if(s<0){
							is_over_due="0";//0 代表超时
						}else{
							is_over_due="1";//1 代表未超时
						}
						Db.update("update dop_search_check set is_over_due=? where check_flownum=?",is_over_due,check_flownum);
					}else{
						int s=check_enddate.compareTo(check_date);
						if(s<0){
							is_over_due="0";//0 代表超时
						}else{
							is_over_due="1";//1 代表未超时
						}
						Db.update("update dop_search_check set is_over_due=? where check_flownum=?",is_over_due,check_flownum);
					}
				}
			}
			
			
				
			
		}
		
		
		
		String selectSql = "select (select remark from sys_param_info p where p.key = 'dop_ywtype' and p.val = t.check_model) as biz_module, check_warningname warning_name, " +
				"check_flownum search_no, search_date, check_enddate search_enddate, " +
				"(select orgname from sys_org_info org where org.orgnum = t.search_org) search_org, search_name searcher, check_date, (select orgname from sys_org_info org where org.orgnum = t.check_org) check_org, " +
				"check_name checker, ( select remark from  SYS_PARAM_INFO f where  f.key='searchCheck_status' and f.val= t.search_check_status ) sc_status,( select remark from  SYS_PARAM_INFO f where  f.key='isOverdue' and f.val= t.is_over_due) is_over_due";
		StringBuffer extraSql = new StringBuffer(" from DOP_SEARCH_CHECK t  where 1 = 1 and t.search_check_status is not null ");
		List<String> sqlStr = new ArrayList<String>();
		if (AppUtils.StringUtil(bizModule) != null) {
			extraSql.append(" and t.check_model = ? " );
			sqlStr.add(bizModule);
		}
		if (AppUtils.StringUtil(warningName) != null) {
			extraSql.append(" and t.check_warningname = ?");
			sqlStr.add(warningName);
		}
		if (AppUtils.StringUtil(searchNo) != null) {
			extraSql.append(" and t.check_flownum = ?");
			sqlStr.add(searchNo);
		}
		if (AppUtils.StringUtil(searchStartDate) != null) {
			searchStartDate = searchStartDate.substring(0, 10);
			extraSql.append(" and to_date(t.search_date, 'yyyyMMdd') >= to_date(?, 'yyyy-MM-dd') ");
			sqlStr.add(searchStartDate);
		}
		if (AppUtils.StringUtil(searchEndDate) != null) {
			searchEndDate = searchEndDate.substring(0, 10);
			extraSql.append(" and to_date(t.search_date, 'yyyyMMdd') <= to_date(?, 'yyyy-MM-dd') ");
			sqlStr.add(searchEndDate);
		}
		if (AppUtils.StringUtil(searchOrg) != null) {
			extraSql.append(" and t.search_org in (select id from sys_org_info where id = ? or by5 like ?)");
			sqlStr.add(searchOrg);
			sqlStr.add("%" + searchOrg+ "%");
		}		
		if (AppUtils.StringUtil(checkOrg) != null) {
			extraSql.append(" and t.check_org in  (select id from sys_org_info where id = ? or by5 like ?)");
			sqlStr.add(checkOrg);
			sqlStr.add("%" + checkOrg+ "%");
		}
		else
		{
			////如果查询条件没有指定机构，这里按照用户的数据范围、角色级别 来查， 数据范围优先
			extraSql.append(" and t.check_org in (select id from sys_org_info where id = ? or by5 like ?)");
			sqlStr.add(sjfw.trim());
			sqlStr.add("%" + sjfw.trim()+ "%");
		}
		
		if (AppUtils.StringUtil(checkStartDate) != null) {
			checkStartDate = checkStartDate.substring(0, 10);
			extraSql.append(" and to_date(t.check_date, 'yyyyMMdd') >= to_date(?, 'yyyy-MM-dd') ");
			sqlStr.add(checkStartDate);
		}
		if (AppUtils.StringUtil(checkEndDate) != null) {
			checkEndDate = checkEndDate.substring(0, 10);
			extraSql.append(" and to_date(t.check_date, 'yyyyMMdd') <= to_date(?, 'yyyy-MM-dd') ");
			sqlStr.add(checkEndDate);
		}
		if (AppUtils.StringUtil(searchCheckStatus) != null) {
			extraSql.append(" and t.search_check_status = ? ");
			sqlStr.add(searchCheckStatus);
		}
		if (AppUtils.StringUtil(isOverdue) != null) {
			extraSql.append(" and t.is_over_due = ? ");
			sqlStr.add(isOverdue);
		}
		
		
		Page<Record> page = Db.paginate(pageNum, pageSize, selectSql, extraSql.toString(), sqlStr.toArray());
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	public void detail() {
		render("scs_detail.jsp");
	}
	
	public void getDetail() {
		String flownum = getPara("flownum");
			String sql =  "select spi1.remark       biz_module,       "  +
						  "      check_warningname warning_name,      "  +
						  "      check_flownum     search_no,         "  +
						  "      search_date,                         "  +
						  "      check_enddate     search_enddate,    "  +
						  "      soi1.orgname      search_org,        "  +
						  "      search_name       searcher,          "  +
						  "      check_date,                          "  +
						  "      soi2.orgname      check_org,         "  +
						  "      check_name        checker,           "  +
						  "      spi2.remark       sc_status,         "  +
						  "      trade_information trade_info,        "  +
						  "      search_matter,                       "  +
						  "      check_remark                         "  +
						  " from dop_search_check dsc                 "  +
						  " left join sys_param_info spi1             "  +
						  "   on spi1.val = dsc.check_model           "  +
						  " left join sys_org_info soi1               "  +
						  "   on soi1.orgnum = dsc.search_org         "  +
						  " left join sys_org_info soi2               "  +
						  "   on soi2.orgnum = dsc.check_org          "  +
						  " left join sys_param_info spi2             "  +
						  "   on spi2.val = dsc.search_check_status   "  +
						  "where spi1.key = 'dop_ywtype'              "  +
						  "  and spi2.key = 'searchCheck_status' and check_flownum = ?";
		Record record = Db.findFirst(sql, flownum);
		setAttr("record", record);
		renderJson();
	}
	//导出

	public void download() {
		getList();
		List<Record> list = getAttr("data");
		
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
				
		// 转换成excel
	    ExcelRenderx er = new ExcelRenderx(fileName, columns, headers, list, getResponse());
		er.render(); 
		// 打印日志   
		log.info("download--list:" + list);
		renderNull();
	}
	
	
}
