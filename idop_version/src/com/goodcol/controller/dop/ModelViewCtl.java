package com.goodcol.controller.dop;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;

/*
 * 模块全景视图
 * @author zy
 */
@RouteBind(path = "/modelview")
@Before({ManagerPowerInterceptor.class})
public class ModelViewCtl extends BaseCtl{
	
	// 记录日志用
	public static Logger log = Logger.getLogger(ModelViewCtl.class); 

	/**
	 * 加载主页面
	 */
	@Override
	@Before(PermissionInterceptor.class)
	public void index() {
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		if(model == null || "".equals(model) ||"null".equals(model)){
			render("nomodel.jsp");
		}else{
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
			setAttr("model", model);
			setAttr("org", orgid);
			render("main.jsp");
		}
	}
	
	/**
	 * 获取评分
	 */
	public void getDataByModel() {
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		String  orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		Record r = Db.findFirst("select b.by2,b.ORGNUM from sys_org_info b where ORGNUM = ? ", orgid);
		BigDecimal code = null;
		if(r!=null){
			
			String by2 = r.get("by2");
			StringBuffer selSql = new StringBuffer() ;
			
			if("1".equals(by2)){
				
				selSql.append("select to_number(to_char(avg(score),'FM9999990.00')) as num from ( " +
						"select t.* from(select row_number()over(partition by lvl_2_branch_no order by data_date desc)rn,t1.* from (  " +
						"select * from DOP_ORG_SCORE_DETAIL where deptlevel = '1' and indexname like '%ZF' and module = '"+model+"' )t1)t " +
						"where t.rn=1)");
				
			}else{
				selSql.append("select to_number(to_char(avg(score),'FM9999990.00')) as num from (" +
						"select t.* from(select row_number()over(partition by case '"+by2+"' when '2' then lvl_2_branch_no when '3' then lvl_3_branch_no when '4' then lvl_4_branch_no end order by data_date desc)rn,t1.* from ( " +
						"select * from DOP_ORG_SCORE_DETAIL where deptlevel = (case '"+by2+"' when '2' then '1' when '3' then '2' when '4' then '3' end) and indexname like '%ZF' and module = '"+model+"'  and (case '"+by2+"' when '2' then lvl_2_branch_no when '3' then lvl_3_branch_no when '3' then lvl_3_branch_no end)='"+orgid+"' )t1)t " +
						"where t.rn=1)");
			}
			
			
			Record selRd = Db.findFirst(selSql.toString());
			
			if(selRd!=null){
				code = selRd.getBigDecimal("num");
			}
		}
		
		setAttr("data", code);
		
		renderJson();
	}
	
	/**
	 * 加载全景页面
	 */
	public void indexYq() {
		String  orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		
		String orgnum = getPara("orgnum");
		if(AppUtils.StringUtil(orgnum) != null){
			orgid = orgnum;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar ci = Calendar.getInstance();
		ci.setTime(new Date());//先存入时间
		ci.add(Calendar.MONTH, -1);//再设置计算参数
		String month = sdf.format(ci.getTime()); 
		
		setAttr("startime", DateTimeUtil.getLastMonthFirstDay("yyyy-MM-dd"));
		setAttr("endtime", DateTimeUtil.getLastMonthEndDay("yyyy-MM-dd"));
		
		
		
		setAttr("org", orgid);
		render("index_yq.jsp");
	}
	
	/**
	 * 加载机构维度主页面
	 */
	//@Before(PermissionInterceptor.class)
	public void indexJw() {
		String  orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar ci = Calendar.getInstance();
		ci.setTime(new Date());//先存入时间
		ci.add(Calendar.MONTH, -1);//再设置计算参数
		String month = sdf.format(ci.getTime()); 
		setAttr("month", month);
		setAttr("org", orgid);
		render("index_jw.jsp");
	}
	
	
	/**
	 * 加载人员维度主页面
	 */
	//@Before(PermissionInterceptor.class)
	public void indexRw() {
		String  orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar ci = Calendar.getInstance();
		ci.setTime(new Date());//先存入时间
		ci.add(Calendar.MONTH, -1);//再设置计算参数
		String month = sdf.format(ci.getTime()); 
		setAttr("month", month);
		setAttr("org", orgid);
		render("index_rw.jsp");
	}
	
	
	/**
	 * 加载预警信息页面
	 */
	//@Before(PermissionInterceptor.class)
	public void indexYj() {
		String stat = getPara("stat");
		String  orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		//和数据权限有关的  加最大权限机构条件 
		
		if(AppUtils.StringUtil(stat) != null){
			orgid = getPara("orgnum");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar ci = Calendar.getInstance();
		ci.setTime(new Date());//先存入时间
		ci.add(Calendar.MONTH, -1);//再设置计算参数
		String month = sdf.format(ci.getTime()); 
		
		setAttr("startime", DateTimeUtil.getLastMonthFirstDay("yyyy-MM-dd"));
		setAttr("endtime", DateTimeUtil.getLastMonthEndDay("yyyy-MM-dd"));
		setAttr("month", month);
		setAttr("org", orgid);
		render("index_yj.jsp");
	}
	
	/**
	 * 加载预警信息详情页面
	 */
	//@Before(PermissionInterceptor.class)
	public void toYjDetailTable() {
		render("yjdetail.jsp");
	}
	
	
	/*
	 	1.为保证数据绘制及页面加载协调
	 		1.首先加载页面
	 		2.异步拉取数据
	 		3.数据分类保证某一块数据有问题时不受影响
	 */

	/**
	 * 查询 重点预警信息
	 */
	public void getWarnMap(){
		//计算24小时（1天）前的时间 ，PS：考虑时间可能为时间制,采用小时计算时间
		ParamContainer pc = new ParamContainer();
		@SuppressWarnings("static-access")
		String hourStr = pc.getDictName("dop_warnmaphour", "hour");
		int hour = 24;
		if(AppUtils.StringUtil(hourStr) != null){
			hour = Integer.parseInt(hourStr);
		}
		
		//该数据格式是 3,4,5需要转换
		@SuppressWarnings("static-access")
		String leve = pc.getDictNameNew("dop_panoramaconfig", "leve"); 
		
		//依照那种数据类型进行统计 0-预警数量 1-确认问题数量
		@SuppressWarnings("static-access")
		String datatype = pc.getDictNameNew("dop_panoramaconfig", "datatype"); 
		
		leve = AppUtils.getRegexStr(leve);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar ci = Calendar.getInstance();
		ci.setTime(new Date());//先存入时间
		ci.add(Calendar.HOUR_OF_DAY, -hour);//再设置计算参数
		String createtime = sdf.format(ci.getTime()); 
		
		
		String  orgid = getPara("orgnum");
		if(AppUtils.StringUtil(orgid) == null){
			orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		}
		
		List<Object> paramList = new ArrayList<>();
		String by2 = null;
		
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		// 查询
		List<Record> rList = new ArrayList<Record>();
		if(AppUtils.StringUtil(orgid)!=null){//防止无机构人员获取数据
			Record r = Db.findFirst("select b.by2,b.ORGNUM from sys_org_info b where ORGNUM = ? ", orgid);//不需要判定机构是否删除
			String orgsql = "";
			paramList.add(createtime);		
			if(r!=null){//空值判定
				by2 = r.get("by2");
				if("1".equals(r.get("by2"))){//省级分行机构
					orgsql="select ORGNUM from sys_org_info where by2=2 and ORGNUM !=?  start with ORGNUM=? connect by prior ORGNUM = upid";
					paramList.add(r.getStr("ORGNUM"));
					paramList.add(r.getStr("ORGNUM"));
				}else if("2".equals(r.get("by2"))){//二级分行
					orgsql="?";
					paramList.add(r.getStr("ORGNUM"));
				}else{//管辖行、网点人员 不允许查询
					//查询该网点的/管辖行 的二级分行 机构号
					orgsql="?";
					Record r1 = Db.findFirst("select ORGNUM,by2 from sys_org_info where by2=2  start with ORGNUM=? connect by prior  upid = ORGNUM ", orgid);//不需要判定机构是否删除
					orgid = r1.getStr("ORGNUM");
					by2 = r1.getStr("by2");
					paramList.add(r1.getStr("ORGNUM"));
				}
			} 
			if(orgsql!=""){//控制 管辖行、网点人员 不允许查询
				//拼装sql
				String sql = 
						"select t.*, to_char((t.lv4 / t.cut)*100,'9999999990.99') lv from("+
						"select  LVL_2_BRANCH_NO,count(id) cut,"+
						"        sum(case when b.warning_level in("+leve+") then 1 else 0 end) lv4"+
						"  from dop_warning_info a"+
						"  left join dop_warning_param b"+
						"    on a.warning_code = b.warning_code"+
						" where  (b.is_use is  null or b.is_use = '1') and a.data_date>? "+
						" and b.busi_module = '"+model+"'  and a.lvl_2_branch_no in ("+orgsql+") " +
						(AppUtils.StringUtil(datatype) == null ? "":(datatype.equals("0")?"":" and a.IS_QUESTION='1'" +
								 ""))+
						" group by LVL_2_BRANCH_NO"+
						") t";
				// 查询
				rList = Db.find(sql, paramList.toArray());
			}
		}
		// 赋值
		setAttr("data", rList);
		setAttr("org", orgid);
		setAttr("orgstat", by2);
		// 打印日志
		log.info("getWarnMap--rList:" + rList);
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "重点预警信息地图", "3", "重点预警信息地图-查询");
		log.info("运营全景-查询");
		// 返回json数据
		renderJson();
	}
	
	public void getAreaBasic(){
		//机构 
		String orgid = getPara("orgid");
		
		
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2,orgname,bancsid from sys_org_info  where orgnum ='"+orgid+"'  and  stat ='1'");
		String by2 = orgRe.getStr("by2");
		String bancsid = orgRe.getStr("bancsid");
		
		/*List<Record> orgList = Db.find("select bancsid within group (order by null) as orgnums  from sys_org_info where by2 = 4 and  stat ='1' and bancsid is not null  start with ORGNUM='"+orgid+"' connect by prior ORGNUM = upid");
		boolean isFir = true;
		StringBuffer orgsql = new StringBuffer();
		for (Record r : orgList) {
			if(isFir){
				isFir = false;
			}else{
				orgsql.append(",");
			}
			orgsql.append("'"+r.getStr("bancsid")+"'");
		}*/
	
		
		//从GBASE获取对应数据
		//查询 字典项 业务模块 
		List<Record> paramList = Db.find(" select * from sys_param_info p where p.key='dop_showtype' and STATUS = 1 order by sortnum");
		boolean isFir = true;
		Record mapR = new Record();
		StringBuffer param = new StringBuffer();
		for (Record r : paramList) {
			if(isFir){
				isFir = false;
			}else{
				param.append(",");
			}
			param.append("'"+r.getStr("val")+"'");
		}
		//取基础运营信息都从idop_indicators_val_list_overview 取不要从idop_indicators_val_list表取
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(
				"select b.*, to_char((case  when b.indicator_code = 'JC001004' or b.indicator_code = 'JC004002' then round(sumval / _cut,0)  else   round(sumval,0)  end)) indicator_value " +
				"from (select indicator_code, sum(indicator_value) sumval, count(1) _cut from " +
				"(select t.* from (select indicator_code,lvl_4_branch_no,lvl_2_branch_no,lvl_3_branch_no,indicator_value," +
				"row_number() over(partition by indicator_code,lvl_4_branch_no order by data_date desc) as rownum  " +
				"from idop_indicators_val_list_overview  where indicator_code in ("+param.toString()+")  " );
		if(!"1".equals(by2)){
			sbsql.append(" and "+getOrgColumn(by2)+" in  (select id from a_dap_dept_para_js   where br_no ='"+bancsid+"' )" );
		}
		sbsql.append( ") t where t.rownum = 1) a group by indicator_code) b ");

		
	
		
		List<Record> rList =  Db.use("gbase").find(sbsql.toString());
		
		for (Record r : rList) {
			//r.set("indicator_name", ((Record)mapR.get(r.getStr("indicator_code"))).get("remark"));
			mapR.set(r.getStr("indicator_code").toUpperCase(), r);
		}
		
		for (Record r : paramList) {
			Record or = ((Record)mapR.get(r.getStr("val")));
			//r.set("indicator_name", ((Record)mapR.get(r.getStr("indicator_code"))).get(""))
			r.set("indicator_value", or!=null?or.get("indicator_value"):"");
		}
		
		setAttr("data", paramList);
		renderJson();
	}
	
	
	//根据查询条件查询某地区的基本信息
	public void getGegendData(){
		
		
		//取每月前N天取上上月的数据,N天后取上月数据 N由字典配置
		ParamContainer pc = new ParamContainer();
		@SuppressWarnings("static-access")
		String dayStr = pc.getDictName("dop_indexradard", "day");
		int day = 5;
		if(AppUtils.StringUtil(dayStr) != null){
			day = Integer.parseInt(dayStr);
		}
		//获取当前天是本月第几天
		int thisDay = DateTimeUtil.getDayOfMonth();
		//层次 1-首页,0-其他 只为处理首页雷达图数据
		String querypagetype = getPara("querypagetype","0");
		int oldmonth = 1;  //默认一个月前
		if(thisDay <= day && querypagetype.equals("1")){
			oldmonth = 2;// 两个月前
		}
		
		
		
		//开始时间
		String startime = getPara("startime");
		//结束时间
		String endtime = getPara("endtime");
		
		String orgnum = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		//机构 默认：当前用户层级 可选：当前用户层级、下级管辖机构
		String orgid = getPara("orgid");
		//对比机构
		String dborgid = getPara("dborgid");
		//图表格式 0-雷达图,1-柱形图,2-折线图,3-饼图
		String pattern = getPara("pattern");
		//当选择图标格式不是折线图时不设置为对比机构
		if(!"2".equals(pattern)){
			dborgid = "";
		}
		//是否在 lenged中展示数据
		String lengedType = getPara("lengedType","0");
		
		
		//查询类型 0-评分,1-预警
		String querytype = getPara("querytype");
		if(AppUtils.StringUtil(orgid)!=null){//机构参数如果未传入,默认设置为 当前登录人机构
			orgnum = orgid;
		}
		
		
		//获取最新日期
		Record getnewdate=Db.findFirst(" select  data_month from DOP_ORG_SCORE_DETAIL t inner join sys_org_info p on T.DEPTNO=P.ORGNUM  " +
		        "where 1=1 and t.deptno in  (select orgnum from sys_org_info where upid=? and stat=1 ) "+
				"group by t.data_month   order by t.data_month desc  " , new Object[]{orgnum});
		String datamonth=getnewdate.getStr("data_month");
		/* 获取机构对应地区数据*/
		/*String sql = 
				"select count(b.emp_num) empnum,                     "+     
				"       count(b.operate_num) operatenum,             "+
				"       count(b.pub_busi_vol)||'笔' pubbusivol,      "+
				"       count(b.pri_busi_vol)||'笔' pribusivol,      "+
				"       count(b.pub_cust_num)||'笔' pubcustnum,      "+
				"       count(b.pri_cust_num)||'笔' pricustnum       "+
				"  from sys_org_info a                               "+
				"  inner join dop_org_operate_info b                 "+
				"    on a.orgnum = b.deptno                          "+
				" where a.orgnum in (select orgnum                   "+
				"                      from sys_org_info             "+
				"                     where 1 = 1                    "+
				"                     start with orgnum = ?          "+
				"                    connect by prior orgnum = upid) ";
		*/

		//获取地区基本信息数据
		//Record re = Db.findFirst(sql,new Object[]{orgid});	
		List<Record> chartsList;
		
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		
		//查询 字典项 业务模块 
		List<Record> paramList = Db.find(" select * from sys_param_info p where p.key='dop_ywtype' and p.val =? order by sortnum",model);
		//组装
		//legend
		List<String> legendList = new ArrayList<>();
		//series
		List<Record> seriesList = new ArrayList<>();
		//xAxis 
		List<String> xAxisList = new ArrayList<>();
		// indicator
		List<Record> indicatorList = new ArrayList<>();
		
		//获取机构级别
		Record org = Db.findFirst("select b.by2,b.ORGNUM,b.orgname from sys_org_info b where ORGNUM = ? ", orgnum);
		String by2 = org.getStr("by2");
		String orgname = org.getStr("orgname");
		//组装查询sql 
		StringBuffer sb = new StringBuffer();
		boolean isFir = true;
		List<String> sqlStr = new ArrayList<String>();
		//查询类型选评分时
		if("0".equals(querytype)){//查询类型选评分
			//查询业务模型数据
		
			StringBuffer selectSql = new StringBuffer();
			//处理总行数据 非对比以及当前所选机构为 省分行
			if(AppUtils.StringUtil(dborgid)==null && "1".equals(by2)){
				
				
				selectSql.append("select '"+orgnum+"' deptno,'"+orgname+"' orgname,TO_NUMBER(TO_CHAR(avg(cut),'FM9999990.00')) cut,");
				isFir = true;
				for (Record r : paramList) {
					if(isFir){
						isFir = false;
					}else{
						selectSql.append(",");
					}
					selectSql.append("TO_NUMBER(TO_CHAR(avg(A"+r.getStr("VAL")+"),'FM9999990.00')) A"+r.getStr("VAL"));
				}
				selectSql.append(" from ( ");
				
				selectSql.append(" select  lvl_2_branch_no,avg(nvl(t.score,0)) cut,");
				isFir = true;
				for (Record r : paramList) {
					if(isFir){
						isFir = false;
					}else{
						selectSql.append(",");
					}
					selectSql.append("sum(case t.module when '"+r.getStr("VAL")+"' then nvl(t.score,0) else 0 end) A"+r.getStr("VAL"));
				}
				selectSql.append(" from DOP_ORG_SCORE_DETAIL t  WHERE 1 = 1 and t.indexname like 'ZF%' and t.module = '"+model+"' ");
				
			}else{
				
				selectSql.append("select f.*,rownum as pm from ( select t.deptno,"+
				" p.orgname,   "+
				"(select count(1) from dop_warning_info w where                            "+
				" w.deptno = t.deptno and w.IS_QUESTION = '1') warnnum,                 "+
				"sum(nvl(t.score,0)) cut,                                                  ");
				isFir = true;
				for (Record r : paramList) {
					if(isFir){
						isFir = false;
					}else{
						selectSql.append(",");
					}
					selectSql.append("sum(case t.module when '"+r.getStr("VAL")+"' then nvl(t.score,0) else 0 end) A"+r.getStr("VAL"));
				}
				selectSql.append(" from DOP_ORG_SCORE_DETAIL t  inner join sys_org_info p on t.deptno = p.orgnum  WHERE 1 = 1 and t.indexname like 'ZF%' ");
			}		
					
			//机构号条件匹配
			if(AppUtils.StringUtil(dborgid)!=null){
				selectSql.append(" and t.deptno in (");
				String [] dborgids = dborgid.split(",");
				isFir = true;
				for (String dbo : dborgids) {
					if(isFir){
						isFir = false;
					}else{
						selectSql.append(",");
					}
					selectSql.append("?");
					sqlStr.add(dbo);
				}
				selectSql.append(")");
			}else{
				if(!"1".equals(by2)){
					selectSql.append(" and  t.deptno =? ");
					sqlStr.add(orgnum);
				}else{
					selectSql.append(" and t.lvl_2_branch_no in (select orgnum from sys_org_info where 1=1 and by2='2' start with orgnum=? connect by prior orgnum=upid ) and t.deptlevel = '1'");
					sqlStr.add(orgnum);
				}
			}
			if(AppUtils.StringUtil(startime)==null && AppUtils.StringUtil(endtime)==null){
				Record newdata=Db.findFirst(" select distinct(t.data_month)data_month  from DOP_ORG_SCORE_DETAIL t  group by t.data_month  order by  t.data_month desc ");
				String damonth=newdata.getStr("data_month");
				selectSql.append("  and t.DATA_MONTH = '"+damonth+"' ");
				//selectSql.append("  and t.DATA_MONTH = to_char(add_months(trunc(sysdate),-"+oldmonth+"),'yyyyMM') ");
			}else{
				if(AppUtils.StringUtil(startime)!=null){
					//数据产生时间YYYYMMDDHHMMSS
					selectSql.append("  and t.DATA_MONTH >= to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMM') ");
					sqlStr.add(startime);
				}
				if(AppUtils.StringUtil(endtime)!=null){
					//数据产生时间YYYYMMDDHHMMSS
					selectSql.append("  and t.DATA_MONTH <=  to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMM')  ");
					sqlStr.add(endtime);
				}
			}
			
			//处理总行数据 非对比以及当前所选机构为 省分行
			if(AppUtils.StringUtil(dborgid)==null && "1".equals(by2)){
				selectSql.append("  group by t.lvl_2_branch_no order by cut desc) f");
			}else{
				selectSql.append(" group by t.deptno,p.orgname order by cut desc) f");
			}
			
			chartsList = Db.find( selectSql.toString(), sqlStr.toArray());
			
			if("0".equals(pattern)){//雷达图
				Record serie ;
				List<Double> values = new ArrayList<>();
				isFir = true;
				for (Record r : chartsList) {
					legendList.add(r.getStr("orgname"));
					values = new ArrayList<>();
					serie = new Record();
					serie.set("name", r.getStr("orgname"));
					for (Record r1 : paramList) {
						values.add(r.getBigDecimal("A"+r1.getStr("VAL")) == null ? 0.00 : r.getBigDecimal("A"+r1.getStr("VAL")).doubleValue());
						if(isFir){
							if("0".equals(lengedType)){
								indicatorList.add(new Record().set("text", r1.getStr("REMARK")).set("max",100));
							}else{
								indicatorList.add(new Record().set("text", r1.getStr("REMARK")+"<br/>"+r.getBigDecimal("A"+r1.getStr("VAL")).doubleValue()+"").set("max",100));
							}
							
						}
					}
					if(isFir){
						if("0".equals(lengedType)){
							indicatorList.add(new Record().set("text", "综合账务").set("max",100));
						}else{
							indicatorList.add(new Record().set("text", "综合账务<br/>100").set("max",100));
						}
					}
					values.add(100.00);
					isFir = false;
					serie.set("value", values);
					seriesList.add(serie);
				}
				
				
				
			}else if("1".equals(pattern)){//柱状图
				Record serie ;
				isFir = true;
				List<String> values = new ArrayList<>();
				for (Record r : chartsList) {
					legendList.add(r.getStr("orgname"));
					values = new ArrayList<>();
					serie = new Record();
					serie.set("name", r.getStr("orgname"));
					serie.set("type", "bar");
					serie.set("barGap", 0);
					for (Record r1 : paramList) {
						values.add(r.getBigDecimal("A"+r1.getStr("VAL"))== null ? "0.00" : r.getBigDecimal("A"+r1.getStr("val")).intValue()+"");
						if(isFir){
							xAxisList.add(r1.getStr("REMARK"));
						}
					}
					isFir = false;
					serie.set("data", values);
					seriesList.add(serie);
				}
			}else{
				//后期可添加
			}
			
			
			
			setAttr("legend", legendList);
			setAttr("indicator", indicatorList);
			setAttr("xAxis", xAxisList);
			setAttr("series", seriesList);
			
		}else{//查询类型选预警
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar ci = Calendar.getInstance();
			ci.setTime(new Date());//先存入时间
			StringBuffer selectSql = new StringBuffer();
			List<String> xaixs = new ArrayList<>();
			
			if("1".equals(pattern)|| "3".equals(pattern)){
				
				//处理总行数据 非对比以及当前所选机构为 省分行
				if(AppUtils.StringUtil(dborgid)==null && "1".equals(by2)){
					selectSql.append("select * from (select '"+orgnum+"' deptno,'"+orgname+"' orgname, b.busi_module, b.warning_name, to_char(count(1)) cut  from dop_warning_info t   left join dop_warning_param b  on t.warning_code = b.warning_code   where 1 = 1 and  (b.is_use is  null or b.is_use = '1') and t.IS_QUESTION = '1'  and b.busi_module = '"+model+"' ");
				}else{
					selectSql.append("select * from (select t."+getOrgColumn(by2)+" deptno,p.orgname, b.busi_module, b.warning_name, to_char(count(1)) cut  from dop_warning_info t   left join dop_warning_param b  on t.warning_code = b.warning_code inner join sys_org_info p on t."+getOrgColumn(by2)+" = p.orgnum  where 1 = 1 and  (b.is_use is  null or b.is_use = '1') and t.IS_QUESTION = '1' and b.busi_module = '"+model+"'  ");
				}
				
				
				if(AppUtils.StringUtil(startime)==null && AppUtils.StringUtil(endtime)==null){
					if("1".equals(pattern)){//柱状图
						ci.add(Calendar.DAY_OF_YEAR, -30);//再设置计算参数
						selectSql.append("  and t.data_date >= ? ");
						sqlStr.add(sdf.format(ci.getTime()));
					}else if("3".equals(pattern)){//饼图
						ci.add(Calendar.DAY_OF_YEAR, -30);//再设置计算参数
						selectSql.append("  and t.data_date >= ? ");
						sqlStr.add(sdf.format(ci.getTime()));
					}
					
				}else{
					if(AppUtils.StringUtil(startime)!=null){
						selectSql.append("  and t.data_date >= to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMdd') ");
						sqlStr.add(startime);
					}
					if(AppUtils.StringUtil(endtime)!=null){
						selectSql.append("  and t.data_date <=  to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMdd')  ");
						sqlStr.add(endtime);
					}
				}
				
				

				//机构号条件匹配
				if(AppUtils.StringUtil(dborgid)!=null){
					selectSql.append(" and t."+getOrgColumn(by2)+" in (");
					String [] dborgids = dborgid.split(",");
					isFir = true;
					for (String dbo : dborgids) {
						if(isFir){
							isFir = false;
						}else{
							selectSql.append(",");
						}
						selectSql.append("?");
						sqlStr.add(dbo);
					}
					selectSql.append(")");
				}else{
					if(!"1".equals(by2)){
						selectSql.append(" and t."+getOrgColumn(by2)+" =?");
						sqlStr.add(orgnum);
					}/*else{
						selectSql.append(" and t."+getOrgColumn(by2)+" =?");
						sqlStr.add(orgnum);
					}*/
					
				}
				//处理总行数据
				if(AppUtils.StringUtil(dborgid)==null && "1".equals(by2)){
					selectSql.append("group by b.busi_module,b.warning_name) pivot(max(to_char(cut))  for busi_module in(");
				}else{
					selectSql.append("group by t."+getOrgColumn(by2)+", b.busi_module,b.warning_name,p.orgname) pivot(max(to_char(cut))  for busi_module in(");
				}
				
				
				isFir = true;
				for (Record r : paramList) {
					if(isFir){
						isFir = false;
					}else{
						selectSql.append(",");
					}
					selectSql.append("'"+r.getStr("VAL")+"' A"+r.getStr("VAL"));
				}
				selectSql.append(")) order by "+"A"+model+" desc ");
				
			}
			else if("2".equals(pattern)){//折线图
				//生成最近半年X轴数据
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
				StringBuffer sb1 = new StringBuffer();
				isFir = true;
				for (int i = 6; i >0; i--) {
					ci.setTime(new Date());//先存入时间
					ci.add(Calendar.MONTH, -i);//再设置计算参数
					xaixs.add(sdf1.format(ci.getTime()));
					if(isFir){
						isFir = false;
					}else{
						sb1.append(",");
					}
					sb1.append("'"+sdf1.format(ci.getTime())+"'  A"+sdf1.format(ci.getTime()));
				}
				
				//处理总行数据 非对比以及当前所选机构为 省分行
				if(AppUtils.StringUtil(dborgid)==null && "1".equals(by2)){
					selectSql.append("select * from (select '"+orgnum+"' deptno,'"+orgname+"' orgname, substr(t.data_date,0,6)  month_date, to_char(count(1)) cut  from dop_warning_info t   left join dop_warning_param b  on t.warning_code = b.warning_code   where 1 = 1 and  (b.is_use is  null or b.is_use = '1') and t.IS_QUESTION = '1' and b.busi_module = '"+model+"'    ");
				}else{
					selectSql.append("select * from (select t."+getOrgColumn(by2)+" deptno,p.orgname, substr(t.data_date,0,6)  month_date, to_char(count(1)) cut  from dop_warning_info t   left join dop_warning_param b  on t.warning_code = b.warning_code inner join sys_org_info p on t."+getOrgColumn(by2)+" = p.orgnum  where 1 = 1 and  (b.is_use is  null or b.is_use = '1') and t.IS_QUESTION = '1' and  b.busi_module = '"+model+"'    ");
				}
				
								sdf = new SimpleDateFormat("yyyyMM");
				ci.add(Calendar.MONTH, -12);//再设置计算参数
				selectSql.append("  and substr(t.data_date,0,6) >= ? ");
				sqlStr.add(sdf.format(ci.getTime()));

				//机构号条件匹配
				if(AppUtils.StringUtil(dborgid)!=null){
					selectSql.append(" and t."+getOrgColumn(by2)+" in (");
					String [] dborgids = dborgid.split(",");
					isFir = true;
					for (String dbo : dborgids) {
						if(isFir){
							isFir = false;
						}else{
							selectSql.append(",");
						}
						selectSql.append("?");
						sqlStr.add(dbo);
					}
					selectSql.append(")");
				}else{
					if(!"1".equals(by2)){
						selectSql.append(" and t."+getOrgColumn(by2)+" =?");
						sqlStr.add(orgnum);
					}
					
				}
				
				//处理总行数据 非对比以及当前所选机构为 省分行
				if(AppUtils.StringUtil(dborgid)==null && "1".equals(by2)){
					selectSql.append("group by  substr(t.data_date,0,6) ) pivot(max(to_char(cut))  for month_date in("+sb1.toString()+"))");
				}else{
					selectSql.append("group by t."+getOrgColumn(by2)+", substr(t.data_date,0,6) ,p.orgname) pivot(max(to_char(cut))  for month_date in("+sb1.toString()+"))");
				}
			}
			
			chartsList = Db.find( selectSql.toString(), sqlStr.toArray());
			if("1".equals(pattern)){//柱状图
				Record serie ;
				isFir = true;
				List<String> values = new ArrayList<>();
				if(chartsList.size()>5){
					List<Record> chartslists = new ArrayList<Record>();
					chartslists.add(chartsList.get(0));
					chartslists.add(chartsList.get(1));
					chartslists.add(chartsList.get(2));
					chartslists.add(chartsList.get(3));
					chartslists.add(chartsList.get(4));
					chartsList = chartslists;
				}
				for (Record r : chartsList) {
					legendList.add(r.getStr("orgname"));
					values = new ArrayList<>();
					serie = new Record();
					serie.set("name", r.getStr("warning_name"));
					serie.set("type", "bar");
					serie.set("barGap", 0);
					for (Record r1 : paramList) {
						values.add(r.getStr("A"+r1.getStr("VAL")));
						if(isFir){
							xAxisList.add(r1.getStr("REMARK"));
						}
					}
					isFir = false;
					serie.set("data", values);
					seriesList.add(serie);
				}
				setAttr("xAxis", xAxisList);
			}else if("2".equals(pattern)){//折线图
				Record r1;
				List<String> data;
				for (Record r : chartsList) {
					r1 = new Record();
					data = new ArrayList<>();
					r1.set("name", r.get("orgname"));
					r1.set("type", "line");
					//r1.set("smooth", true);
					for (String key : xaixs) {
						data.add(r.getStr("A"+key));
					}
					r1.set("data",data);
					seriesList.add(r1);
					legendList.add(r.getStr("orgname"));
				}
				
				// 赋值
				setAttr("xAxis", xaixs);
			}else if("3".equals(pattern)){//饼图
				if(chartsList.size()>5){
					List<Record> chartslists = new ArrayList<Record>();
					chartslists.add(chartsList.get(0));
					chartslists.add(chartsList.get(1));
					chartslists.add(chartsList.get(2));
					chartslists.add(chartsList.get(3));
					chartslists.add(chartsList.get(4));
					int num = 0;
					for(int i = 5;i<chartsList.size();i++){
						num += Integer.parseInt(chartsList.get(i).getStr("A"+model));
					}
					Record addrd = new Record();
					addrd.set("warning_name", "其他");
					addrd.set("A"+model, String.valueOf(num));
					chartslists.add(addrd);
					chartsList = chartslists;
				}
				isFir = true;
				for (Record r : chartsList) {
					for (Record r1 : paramList) {
						seriesList.add(new Record().set("name", r.getStr("warning_name")).set("value", AppUtils.StringUtil(r.getStr("A"+r1.getStr("VAL"))) == null?"0":r.getStr("A"+r1.getStr("VAL")) ));
						if(isFir){
							legendList.add(r1.getStr("REMARK"));
						}
					}
					isFir = false;
					
				}
				setAttr("xAxis", xAxisList);
			}else{
				//后期可添加
			}
			setAttr("legend", legendList);
			setAttr("indicator", indicatorList);
			setAttr("series", seriesList);
		}
		
		//List<Record> chartsList = Db.find(sql);
		//setAttr("record", re);
		setAttr("chartsList", chartsList);
		// 记录日志
		log.info("");
		renderJson();
	}
	/**
	 * 获取表格栏位数据
	 */
	public void getTableList(){
		//为保证数据加载分段,表格数据采用单独加载方式
		
		//开始时间
		String startime = getPara("startime");
		//结束时间
		String endtime = getPara("endtime");
		//机构 默认：当前用户层级 可选：当前用户层级、下级管辖机构
		String orgid = getPara("orgid");

		String orgnum = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		
		String isend = "0";//是否结尾不显示排名 0-显示,1-不显示
		
		if(AppUtils.StringUtil(orgid)!=null){
			orgnum = orgid;
			isend = "1"; 
		}
		
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		
		//查询 字典项 业务模块 
		List<Record> paramList = Db.find(" select * from sys_param_info p where p.key='dop_ywtype' and p.val =? order by sortnum",model);
		
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2 from sys_org_info  where orgnum ='"+orgnum+"'  and  stat ='1'");
		String by2 = orgRe.getStr("by2");
		// 获取查询条件
		int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize",5);
		
		//查询业务模型数据
		
		
		List<String> sqlStr = new ArrayList<String>();
		List<Record> headers = new ArrayList<>();
		StringBuffer extraSql = new StringBuffer();
		List<Record> warnTypeList = Db.find("select * from dop_algorithm_info where ywtype = ?",model);
		
		StringBuffer selectSql = new StringBuffer("select g.*,rownum as pm  ");
		extraSql.append(" from ( select f.*, to_char((select count(1) from dop_warning_info w " +
				"left join dop_warning_param p on w.warning_code = p.warning_code");
		 
		extraSql.append(" where w.IS_QUESTION = '1' and p.busi_module = '"+model+"'  and (case when p.by2 = '1' then  '000000000' when p.by2 = '2' then w.lvl_2_branch_no  when p.by2 = '3' then w.lvl_3_branch_no  when p.by2 = '4' then w.lvl_4_branch_no end) = f.deptno ");
		
		if(AppUtils.StringUtil(startime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			extraSql.append("  and w.DATA_DATE >= to_char(to_date('"+startime+"','yyyy-MM-dd HH24:mi:ss'),'yyyyMMdd') ");
		}
		if(AppUtils.StringUtil(endtime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			extraSql.append("  and w.DATA_DATE <=  to_char(to_date('"+endtime+"','yyyy-MM-dd HH24:mi:ss'),'yyyyMMdd')  ");
		}
		extraSql.append(" )) warnnum ");
		extraSql.append(" from ( select t.deptno,                                "+
				"p.orgname,p.by2,   "+
				"sum(nvl(t.score,0)) cut,                                                  ");
				boolean isFir = true;
//				headers.add(new Record().set("text", "总分").set("field", "zf"));
				for (Record r : warnTypeList) {
					headers.add(new Record().set("text", r.getStr("indexname").toLowerCase()).set("field", r.getStr("indexname").toLowerCase()));
					if(isFir){
						isFir = false;
					}else{
						extraSql.append(",");
					}
					extraSql.append("sum(case t.module when '"+r.getStr("VAL")+"' then nvl(t.score,0) else 0 end) a"+r.getStr("VAL"));
				}
				extraSql.append(" from DOP_ORG_SCORE_DETAIL t  inner join sys_org_info p on t.deptno = p.orgnum ");

		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 and t.indexname like 'ZF%' and t.module = '"+model+"' ");
		// 查询条件
		//机构号条件匹配
		String wd = getAttr("wd");
		if("wd".equals(wd)){
			//Record r = Db.findFirst("select ORGNUM from sys_org_info where by2=4   start with ORGNUM=? connect by prior ORGNUM = upid",orgnum);
			whereSql.append("  and  p.orgnum in (select ORGNUM from sys_org_info where by2=4   start with ORGNUM=? connect by prior ORGNUM = upid)");
			sqlStr.add(orgnum);
		}else{
			whereSql.append("  and (p.upid=? or p.orgnum=?)");
			sqlStr.add(orgnum);
			sqlStr.add(orgnum);
		}
		//whereSql.append("  and (p.upid=? or p.orgnum=?)");
		/*}else{
			//机构号条件匹配
			whereSql.append("  and  p.orgnum =? ");
			sqlStr.add(orgnum);
		}*/
		if(AppUtils.StringUtil(startime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			whereSql.append("  and t.DATA_MONTH >= to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMM') ");
			sqlStr.add(startime);
		}
		if(AppUtils.StringUtil(endtime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			whereSql.append("  and t.DATA_MONTH <=  to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMM')  ");
			sqlStr.add(endtime);
		}
		//屏蔽不计明细数据
		whereSql.append(" and  p.orgname not like '%不计明细%' ");
		whereSql.append(" group by t.deptno,p.orgname,p.by2 ) f left join sys_org_info p on f.deptno = p.orgnum order by f.by2 desc,f.cut desc)g");
		
//		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql.toString(),extraSql.toString()+whereSql.toString(), sqlStr.toArray());
		// 赋值
		
		StringBuffer selSql = new StringBuffer();
		
		StringBuffer werSql = new StringBuffer();
		
		
		selSql.append("select rownum as pm,round(tt.总分/(case times when 0 then 1 else times end), 2) as zf,tt.orgname as orgname   ");
		for(int i = 0;i<warnTypeList.size();i++){
			selSql.append( " ,round(tt.\""+warnTypeList.get(i).getStr("indexname").toLowerCase()+"\"/(case times when 0 then 1 else times end), 2) as \""+warnTypeList.get(i).getStr("indexname").toLowerCase()+"\" ");
		}
		
		werSql.append("from ( select jgname as orgname, ");
		
		werSql.append("sum(case when indexname = '总分' then 1.00 else 0.00 end) as times, ");
		werSql.append("sum(case when indexname = '总分' then score else 0.00 end) as 总分  ");
		
		for(int i = 0;i<warnTypeList.size();i++){
			werSql.append(", sum(case when indexname = '"+warnTypeList.get(i).getStr("indexname").toLowerCase()+"' then score else 0.00 end) as \""+warnTypeList.get(i).getStr("indexname").toLowerCase()+"\" ");
			
		}
		werSql.append(" from (" +
				"select case "+by2+" when 1 then lvl_2_branch_name when 2 then lvl_3_branch_name when 3 then lvl_4_branch_name end as jgname,t.score,t.indexname as indexcode,(case when t.indexname like 'ZF%' then '总分' else  f.indexname end) as indexname " +
				"from DOP_ORG_SCORE_DETAIL t " +
				"left join DOP_ALGORITHM_INFO f on t.indexname = f.indexnum");
		
		
//		System.out.println(orgid);
//		if("1".equals(by2)){//省分行查询所有网点
		werSql.append(" where 1=1 " );
		if(AppUtils.StringUtil(startime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			werSql.append("  and t.DATA_MONTH >= to_char(to_date('"+startime+"','yyyy-MM-dd HH24:mi:ss'),'yyyyMM') ");
//			sqlStr.add(startime);
		}
		if(AppUtils.StringUtil(endtime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			werSql.append("  and t.DATA_MONTH <=  to_char(to_date('"+endtime+"','yyyy-MM-dd HH24:mi:ss'),'yyyyMM')  ");
//			sqlStr.add(endtime);
		}
		if("000000000".equals(orgid)){
			werSql.append(" and t.module = '"+model+"'  and  deptlevel = '"+by2+"'  ) ");
		}else{
			
			werSql.append(" and t.module = '"+model+"'  and  deptlevel = '"+by2+"' and ( case "+by2+" when 2 then lvl_2_branch_no when 3 then lvl_3_branch_no when 4 then lvl_4_branch_no end)='"+orgid+"'  ) ");
		}
//		}else{
//			selSql.append(" where 1=1 and t.module = '"+model+"'  and t.lvl_"+by2+"_branch_no in ('"+orgid+"') and  deptlevel = '3') ");
//		}
		
		werSql.append("group by  jgname order by 总分 desc,jgname desc)tt  where tt.orgname is not null");
//		System.out.println(selSql);
		
		Page<Record> r = Db.paginate(pageNum, pageSize, selSql.toString(), werSql.toString());
		
		
		setAttr("headers",headers);
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		setAttr("isend", isend);
		
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "模块全景图", "3", "模块全景图-查询");
		log.info("模块全景图-查询");
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 下载网点数据
	 */
	public void downloadWdData(){
		
		//开始时间
		String startime = getPara("startime");
		//结束时间
		String endtime = getPara("endtime");
		//机构 默认：当前用户层级 可选：当前用户层级、下级管辖机构
		String orgid = getPara("orgid");

		String orgnum = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		
		String isend = "0";//是否结尾不显示排名 0-显示,1-不显示
		
		if(AppUtils.StringUtil(orgid)!=null){
			orgnum = orgid;
			isend = "1"; 
		}
		
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2 from sys_org_info  where orgnum ='"+orgnum+"'  and  stat ='1'");
		String by2 = orgRe.getStr("by2");
		
		StringBuffer selectSql = new StringBuffer();
		
		List<Record> warnTypeList = Db.find("select * from dop_algorithm_info where ywtype = ?",model);
		
		selectSql.append("select t2.*,rownum as 排名  from (select 机构名称,总分/(case times when 0 then 1 else times end) as 总分");
		
		for(int i = 0;i<warnTypeList.size();i++){
//			selectSql.append(", '"+warnTypeList.get(i).getStr("indexname")+"'/times  as \""+warnTypeList.get(i).getStr("indexname")+"\" ");
			selectSql.append( " ,tt.\""+warnTypeList.get(i).getStr("indexname")+"\"/(case times when 0 then 1 else times end) as \""+warnTypeList.get(i).getStr("indexname")+"\" ");
		}
		
		selectSql.append(" from ( select lvl_4_branch_name as 机构名称, sum(case when indexname = '总分' then 1 else 0.00 end) as times,");
		
		selectSql.append("sum(case when indexname = '总分' then score else 0.00 end) as 总分");
		
		for(int i = 0;i<warnTypeList.size();i++){
			selectSql.append(", sum(case when indexname = '"+warnTypeList.get(i).getStr("indexname")+"' then score else 0.00 end) as \""+warnTypeList.get(i).getStr("indexname")+"\" ");
			
		}
		selectSql.append(" from (" +
				"select t.lvl_4_branch_no,lvl_4_branch_name,t.score,t.indexname as indexcode,(case when t.indexname like 'ZF%' then '总分' else  f.indexname end) as indexname " +
				"from DOP_ORG_SCORE_DETAIL t " +
				"left join DOP_ALGORITHM_INFO f on t.indexname = f.indexnum");
		
		if("1".equals(by2)){//省分行查询所有网点
			selectSql.append(" where 1=1 and t.module = '"+model+"'  and  deptlevel = '3' ");
		}else{
			selectSql.append(" where 1=1 and t.module = '"+model+"'  and t.lvl_"+by2+"_branch_no in ('"+orgid+"') and  deptlevel = '3' ");
		}
		
		if(AppUtils.StringUtil(startime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			selectSql.append("  and t.DATA_MONTH >= to_char(to_date('"+startime+"','yyyy-MM-dd HH24:mi:ss'),'yyyyMM') ");
		}
		if(AppUtils.StringUtil(endtime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			selectSql.append("  and t.DATA_MONTH <=  to_char(to_date('"+endtime+"','yyyy-MM-dd HH24:mi:ss'),'yyyyMM')  ");
		}
		
		selectSql.append(") group by  lvl_4_branch_name order by 总分 desc,lvl_4_branch_name desc)tt)t2");
		
		List<Record> list = Db.find(selectSql.toString());
		
		StringBuffer execlheaders = new StringBuffer();
		StringBuffer execlcolumns = new StringBuffer();
		
		execlheaders.append("机构名称");
		execlcolumns.append("机构名称");
		execlheaders.append(",排名");
		execlcolumns.append(",排名");
		execlheaders.append(",总分");
		execlcolumns.append(",总分");
		for(int i = 0;i<warnTypeList.size();i++){
			execlheaders.append(","+warnTypeList.get(i).getStr("indexname"));
			execlcolumns.append(","+warnTypeList.get(i).getStr("indexname"));
		}
		
		
		
		setAttr("data", list);
		setAttr("execlheaders", execlheaders.toString());
		setAttr("execlcolumns", execlcolumns.toString());
		System.out.println("execlheaders.toString():  "+execlheaders.toString());
		
		renderJson();
	}
	
	/**
	 * 获取曲线图数据
	 */
	public void getLineList(){
		//为保证数据加载分段,表格数据采用单独加载方式
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar ci = Calendar.getInstance();
		ci.setTime(new Date());//先存入时间
		ci.add(Calendar.MONTH, -6);
		//开始时间
		String startime = sdf.format(ci.getTime()); 
		//机构 默认：当前用户层级 可选：当前用户层级、下级管辖机构
		String orgid = getPara("orgid");
		//对比机构
		String dborgid = getPara("dborgid");
		//机构类型 0-本级,1-下级，2-本级加下级
		String orgtype = getPara("orgtype");
		//数据类型 cut-评分,其他 例如 A1001_01 为 业务模型
		String cloumntype =  getPara("cloumntype");
		
		String orgnum = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		
		if(AppUtils.StringUtil(orgid)!=null){
			orgnum = orgid;
		}
		
		//获取当前机构级别
		Record orgRe = Db.findFirst("select by2 from sys_org_info  where orgnum = ?  and  stat ='1'",orgnum);
		String by2 = orgRe.getStr("by2");
		
		
		//生成最近一年X轴数据
		List<String> xaixs = new ArrayList<>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
		StringBuffer sb = new StringBuffer();
		boolean isFir = true;
		for (int i = 6; i >0; i--) {
			ci.setTime(new Date());//先存入时间
			ci.add(Calendar.MONTH, -i);//再设置计算参数
			xaixs.add(sdf1.format(ci.getTime()));
			if(isFir){
				isFir = false;
			}else{
				sb.append(",");
			}
			sb.append("'"+sdf1.format(ci.getTime())+"'  A"+sdf1.format(ci.getTime()));
		}
		
		//查询业务模型数据
		StringBuffer selectSql = new StringBuffer(" select * ");
	    
		StringBuffer extraSql = new StringBuffer(" from( select t.deptno,p.orgname as name,data_month,");
		if("cut".equals(cloumntype) || AppUtils.StringUtil(cloumntype) == null){
			extraSql.append("sum(nvl(t.score,0)) cut");
		}else {
			String module = cloumntype.substring(1);
			extraSql.append("sum(case t.module when '"+module+"' then nvl(t.score,0) else 0 end) cut");
		}
		extraSql.append(" from dop_org_score_detail t inner join sys_org_info p on  t.deptno = p.orgnum ");

		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 and t.indexname like 'ZF%' and t.module = '"+model+"' ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if(AppUtils.StringUtil(dborgid)!=null){
			whereSql.append(" and t.deptno in (");
			String [] dborgids = dborgid.split(",");
			isFir = true;
			for (String dbo : dborgids) {
				if(isFir){
					isFir = false;
				}else{
					whereSql.append(",");
				}
				whereSql.append("?");
				sqlStr.add(dbo);
			}
			whereSql.append(") ");
		}else{
			//机构号条件匹配
			whereSql.append("  and t.deptno =? ");
			sqlStr.add(orgnum);
		}
		if(AppUtils.StringUtil(startime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			whereSql.append("  and t.DATA_MONTH >= ? ");
			sqlStr.add(startime);
		}
		whereSql.append("  group by t.data_month,t.deptno,p.orgname order by deptno,t.data_month)  pivot( max(to_char(cut)) for data_month in("+sb.toString()+"))");
		List<Record> rList = Db.find(selectSql.toString()+extraSql.toString()+whereSql.toString(),sqlStr.toArray());
		List<Record> series = new ArrayList<>();
		Record r1;
		List<String> data;
		List<String> legend = new ArrayList<>();
		Map<String,Double> avgMap = new HashMap<String,Double>();
		for (Record r : rList) {
			r1 = new Record();
			data = new ArrayList<>();
			r1.set("name", r.get("name"));
			r1.set("type", "line");
			r1.set("smooth", true);
			for (String key : xaixs) {
				data.add(r.getStr("A"+key));
				if(AppUtils.StringUtil(r.getStr("A"+key))!=null){
					if(avgMap.containsKey(key)){
						avgMap.put(key,avgMap.get(key)+Double.valueOf(r.getStr("A"+key)));
					}else{
						avgMap.put(key,Double.valueOf(r.getStr("A"+key)));
					}
				}
			}
			r1.set("data",data);
			series.add(r1);
			legend.add(r.getStr("name"));
		}
		
		r1 = new Record();
		data = new ArrayList<>();
		r1.set("name", "平均值");
		r1.set("type", "line");
		r1.set("smooth", true);
		for (String key : xaixs) {
			if(avgMap.containsKey(key)){
				data.add(String.valueOf(avgMap.get(key)/rList.size()));
			}else{
				data.add(null);
			}
		}
		r1.set("data",data);
		series.add(r1);
		legend.add("平均值");
		
		// 赋值
		setAttr("xAxis", xaixs);
		setAttr("series", series);
		setAttr("legend",legend);
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "运营全景图", "3", "运营全景图-查询");
		log.info("运营全景图-查询");
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 查询 红黑榜单
	 */
	public void getRedBlackList() {
		// 获取查询条件
		//时间-月度
		String month_date = getPara("monthdate");
		//当前登录人角色
		String roleid = getCurrentUser().getStr("ROLE_ID");
		String orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2 from sys_org_info  where orgnum ='"+orgid+"'  and  stat ='1'");
		String by2 = orgRe.getStr("by2");
		
		// 获取查询条件
		int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize",5);
				
		//查询业务模型数据
		StringBuffer selectSql = new StringBuffer(" select * ");
		StringBuffer extraSql = new StringBuffer(
				" from (select f.*, rownum  pm,(cut-lag(cut,1,0)over(order by date_month))/cut qs   "+
						"    from (select t."+getDowOrgColumn(by2)+" deptno,                                                "+
						"                 p.orgname ,                                     "+
						"                 substr(t.data_date, 0, 6) date_month,                "+
						"                 count(1) cut                                           "+
						"            from dop_warning_info t                                 "+
						"            left join dop_warning_param w on t.warning_code = w.warning_code        "+
						"            inner join sys_org_info p                                    "+
						"              on t."+getDowOrgColumn(by2)+" = p.orgnum                                    "
				);

		StringBuffer whereSql = new StringBuffer(" WHERE 1=1 and w.busi_module = '"+model+"'");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		
		//机构号条件匹配
		if("8710DBD0E9A94238BF4605C871B61BF1".equals(roleid)){
			whereSql.append("  t."+getOrgColumn(by2)+" = ? and t.deptlevel='"+getOrgLeve(by2)+"' ");
			sqlStr.add(orgid);
		}else{
			if("4".equals(by2)){
				whereSql.append("  and t."+getOrgColumn(by2)+" = ? and t.deptlevel='"+getOrgLeve(by2)+"' ");
				sqlStr.add(orgid);
			}else if("2".equals(by2) || "3".equals(by2)){
				whereSql.append("  and t."+getOrgColumn(by2)+" = ? ");
				sqlStr.add(orgid);
			}else{
				/*whereSql.append("  and t."+getOrgColumn(by2)+" = ? ");
				sqlStr.add(orgid);*/
			}
		}
		if(AppUtils.StringUtil(month_date)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			whereSql.append("  and substr(t.data_date, 0, 6) = to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMM') ");
			sqlStr.add(month_date);
		}
		
		whereSql.append("  group by substr(t.data_date, 0, 6), t."+getDowOrgColumn(by2)+", p.orgname order by deptno, cut) f  where 1 = 1 order by pm )");
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql.toString(),extraSql.toString()+whereSql.toString(), sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "运营全景图", "3", "运营全景图-查询");
		log.info("运营全景图-查询");
		// 返回json数据
		renderJson();
	}
	  
	
	/**
	 * 获取机构维度折线图数据
	 */
	public void getJwLineList(){
		//为保证数据加载分段,表格数据采用单独加载方式
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar ci = Calendar.getInstance();
		ci.setTime(new Date());//先存入时间
		ci.add(Calendar.MONTH, -6);
		//开始时间
		String startime = sdf.format(ci.getTime()); 
		//机构 默认：当前用户层级 可选：当前用户层级、下级管辖机构
		String orgid = getPara("orgid");

		String orgnum = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		
		if(AppUtils.StringUtil(orgid)!=null){
			orgnum = orgid;
		}
		
		//获取当前机构级别
		Record orgRe = Db.findFirst(" select by2,orgname,orgnum from sys_org_info  where orgnum ='"+orgnum+"'  and  stat ='1'");
		String by2 = orgRe.getStr("by2");
		String orgname = orgRe.getStr("orgname");
	
		
		//生成最近一年X轴数据
		List<String> xaixs = new ArrayList<>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
		StringBuffer sb = new StringBuffer();
		boolean isFir = true;
		for (int i = 6; i >0; i--) {
			ci.setTime(new Date());//先存入时间
			ci.add(Calendar.MONTH, -i);//再设置计算参数
			xaixs.add(sdf1.format(ci.getTime()));
			if(isFir){
				isFir = false;
			}else{
				sb.append(",");
			}
			sb.append("'"+sdf1.format(ci.getTime())+"'  A"+sdf1.format(ci.getTime()));
		}
		
		//查询业务模型数据
		StringBuffer selectSql = new StringBuffer(" select * ");
	    
		StringBuffer extraSql = new StringBuffer(" from( ");
		
		String[] warntypes = "问题数量,预警数量".split(",");

		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		
		isFir = true;
		List<String> sqlStr = new ArrayList<String>();
		for(String warntype : warntypes){
			
			if(isFir){
				isFir = false;
			}else{
				extraSql.append(" union all ");
			}
			
			//处理总行数据 非对比以及当前所选机构为 省分行
			if("1".equals(by2)){
				extraSql.append("select '"+orgnum+"' deptno,'"+orgname+"' as name,'"+warntype+"' warndatatype,substr(t.DATA_DATE, 0, 6) data_month,");
				extraSql.append(" count(1) cut ");
				extraSql.append(" from dop_warning_info t inner join dop_warning_param w on t.warning_code = w.warning_code    ");
			}else{
				extraSql.append("select t."+getOrgColumn(by2)+" deptno,p.orgname as name,'"+warntype+"' warndatatype,substr(t.DATA_DATE, 0, 6) data_month,");
				extraSql.append(" count(1) cut ");
				extraSql.append(" from dop_warning_info t inner join dop_warning_param w on t.warning_code = w.warning_code    inner join sys_org_info p on  t."+getOrgColumn(by2)+" = p.orgnum ");
			}
			extraSql.append(" WHERE  w.warning_dimension = '0' and  (w.is_use is  null or w.is_use = '1') and w.busi_module = '"+model+"' ");
			
			if("问题数量".equals(warntype)){
				extraSql.append(" and t.IS_QUESTION='1' ");
			}
			
			//if(AppUtils.StringUtil(orgid)!=null){
				//机构号条件匹配
			
			if("1".equals(by2)){
				
			}else{
				extraSql.append("  and t."+getOrgColumn(by2)+" =? ");
				sqlStr.add(orgnum);
			}
			
			/*}else{
				//机构号条件匹配
				whereSql.append("  and  p.orgnum =? ");
				sqlStr.add(orgnum);
			}*/
			if(AppUtils.StringUtil(startime)!=null){
				//数据产生时间YYYYMMDDHHMMSS
				extraSql.append("  and substr(t.DATA_DATE, 0, 6) >= ? ");
				sqlStr.add(startime);
			}
			
			//处理总行数据 非对比以及当前所选机构为 省分行
			if("1".equals(by2)){
				extraSql.append(" group by substr(t.DATA_DATE, 0, 6) ");
			}else{
				extraSql.append(" group by substr(t.DATA_DATE, 0, 6),t."+getOrgColumn(by2)+",p.orgname ");
			}
			
		}
		
		extraSql.append(" )  pivot( max(to_char(cut)) for data_month in("+sb.toString()+"))");
		List<Record> rList = Db.find(selectSql.toString()+extraSql.toString(),sqlStr.toArray());
		List<Record> series = new ArrayList<>();
		Record r1;
		List<String> data;
		List<String> legend = new ArrayList<>();
		//只会出现同一机构数据
		
		for (Record r : rList) {
			r1 = new Record();
			data = new ArrayList<>();
			r1.set("name", r.get("warndatatype"));
			r1.set("type", "line");
			//r1.set("smooth", true);
			for (String key : xaixs) {
				data.add(r.getStr("A"+key));
			}
			r1.set("data",data);
			series.add(r1);
			legend.add(r.getStr("warndatatype"));
		}
	
		
		// 赋值
		setAttr("xAxis", xaixs);
		setAttr("series", series);
		setAttr("legend",legend);
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "模块全景图", "3", "模块全景图-查询");
		log.info("模块全景图-查询");
		// 返回json数据
		renderJson();
	}
	
	
	
	
	/**
	 * 获取人员/机构维度表格栏位数据
	 */
	public void getRwTableList(){
		//为保证数据加载分段,表格数据采用单独加载方式
		
		//时间
		String monthdate = getPara("monthdate");
		
		//机构 默认：当前用户层级 可选：当前用户层级、下级管辖机构
		String orgid = getPara("orgid");

		String orgnum = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		// 查询条件
		if(AppUtils.StringUtil(orgid)!=null){
			orgnum = orgid;
		}
		
		//判断是人员还是机构 0-人员,1-机构
		String rwtype = getPara("rwtype");
		
		//查询 字典项 业务模块 
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		
		List<Record> paramList = Db.find(" select * from sys_param_info p where p.key='dop_ywtype' and p.val =? order by sortnum",model);
		
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2 from sys_org_info  where orgnum ='"+orgnum+"'  and  stat ='1'");
		String by2 = orgRe.getStr("by2");
		// 获取查询条件
		int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize",5);
		
		//查询业务模型数据
		StringBuffer selectSql = new StringBuffer(" select f.*,rownum as pm ");
	    
		//组装headers
		List<Record> headers = new ArrayList<>();
		StringBuffer extraSql = new StringBuffer(" from ( select t."+getDowOrgColumn(by2)+" deptno,                                "+
				"p.orgname,   "+
				"count(1) cut,                                                  ");
				boolean isFir = true;
				
				for (Record r : paramList) {
					headers.add(new Record().set("text", r.getStr("REMARK")).set("field", "a"+r.getStr("VAL")));
					if(isFir){
						isFir = false;
					}else{
						extraSql.append(",");
					}
					extraSql.append("sum(case w.busi_module when '"+r.getStr("VAL")+"' then 1 else 0 end) a"+r.getStr("VAL"));
				}
				extraSql.append(" from dop_warning_info t inner join dop_warning_param w  on t.WARNING_CODE = w.WARNING_CODE inner join sys_org_info p on t."+getDowOrgColumn(by2)+" = p.orgnum ");

		StringBuffer whereSql = new StringBuffer(" WHERE  t.IS_QUESTION = '1' and  (w.is_use is  null or w.is_use = '1') ");
		List<String> sqlStr = new ArrayList<String>();
		if("0".equals(rwtype)){
			whereSql.append("  and w.WARNING_DIMENSION = '0'");
		}
		if("1".equals(rwtype)){
			whereSql.append("  and w.WARNING_DIMENSION = '1'");
		}
	
		//机构号条件匹配
		if(!by2.equals("1")){
			whereSql.append("  and t."+getOrgColumn(by2)+" =? ");
			sqlStr.add(orgnum);
		}
		
		//sqlStr.add(orgnum);
		/*}else{
			//机构号条件匹配
			whereSql.append("  and  p.orgnum =? ");
			sqlStr.add(orgnum);
		}*/
		if(AppUtils.StringUtil(monthdate)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			whereSql.append("  and substr(t.DATA_DATE,0,6) = to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMM') ");
			sqlStr.add(monthdate);
		}
		whereSql.append(" group by t."+getDowOrgColumn(by2)+",p.orgname order by cut desc) f");
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql.toString(),extraSql.toString()+whereSql.toString(), sqlStr.toArray());
		// 赋值
		setAttr("headers",headers);
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "运营全景图", "3", "运营全景图-查询");
		log.info("运营全景图-查询");
		// 返回json数据
		renderJson();
	}
	
	
	
	/**
	 * 查询 人员维度 重点关注名单
	 */
	public void getKeynoteTaList() {
		//获取查询条件
		//时间-月度
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar ci = Calendar.getInstance();
		ci.setTime(new Date());//先存入时间
		ci.add(Calendar.MONTH, -1);//再设置计算参数
		String month_date = sdf.format(ci.getTime()); 
		
		String month_date1 = getPara("monthdate");
		if(AppUtils.StringUtil(month_date1) != null){
			month_date = month_date1;
		}
		//当前登录人角色
		String roleid = getCurrentUser().getStr("ROLE_ID");
		//String orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		
		
		//机构 默认：当前用户层级 可选：当前用户层级、下级管辖机构
		String orgid = getPara("orgid");
		if(AppUtils.StringUtil(orgid) == null){
			orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		}
		
		//人员
		String followed_teller = getPara("followed_teller");
		
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2 from sys_org_info  where orgnum ='"+orgid+"'  and  stat ='1'");
		String by2 = orgRe.getStr("by2");
		
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		
		// 获取查询条件
		int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize",5);
//		sys_param_info
		//查询业务模型数据
		StringBuffer selectSql = new StringBuffer(" select * ");
		StringBuffer extraSql = new StringBuffer(
				" from (select *"+
				"  from (select row_number() over(Partition by deptno, data_month, teller_no order by deptno, data_month, cut, teller_no desc) rn,"+
				"               temp.*                                                                                                            "+
				"          from (select t."+getOrgColumn(by2)+" deptno,                                                                                                   "+
				"                       p.orgname ,                                                                                        "+
				"                       substr(t.data_date, 0, 6) data_month,                                                                   "+
				"                       t.teller_no,                                                                                              "+
				"                       u.name username,                                                                                          "+
				"                       count(1) cut                                                                                              "+
				"                  from dop_warning_info t" +
				"				   inner join dop_warning_param w"+
                "           		 on t.warning_code = w.warning_code                                                                           "+
				"                  inner join sys_org_info p                                                                                       "+
				"                    on t."+getOrgColumn(by2)+" = p.orgnum                                                                                       "+
				"                  left join sys_user_info u                                                                                      "+
				"                    on t.teller_no =  lpad(u.user_no,20,'0')                                                                                   "
				);

		StringBuffer whereSql = new StringBuffer(
				"                 WHERE  w.warning_dimension = '1' and  (w.is_use is  null or w.is_use = '1') " +
				"					and t.IS_QUESTION = '1'                                                                                   "+
				"                   and t.teller_no != '"+leftFormat("0", 20)+"'                                                                                  "+
				"                   and t.teller_no not like '%ATM%' " +
				"					and w.busi_module = '"+model+"'                                                                              "
				);
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		
		//机构号条件匹配
		if("8710DBD0E9A94238BF4605C871B61BF1".equals(roleid)){
			whereSql.append("  and t."+getOrgColumn(by2)+" = ? and t.deptleve = '"+getOrgLeve(by2)+"'  ");
			sqlStr.add(orgid);
		}else{
			if("4".equals(by2)){
				whereSql.append("  and t."+getOrgColumn(by2)+" = ? and t.deptleve = '"+getOrgLeve(by2)+"' ");
				sqlStr.add(orgid);
			}else if(!"1".equals(by2)){
				whereSql.append("  and t."+getOrgColumn(by2)+" = ?");
				sqlStr.add(orgid);
			}
		}
		if(AppUtils.StringUtil(month_date)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			whereSql.append("  and substr(t.data_date, 0, 6) = to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMM') ");
			sqlStr.add(month_date);
		}
		
		if(AppUtils.StringUtil(followed_teller)!=null){
			whereSql.append("  and t.teller_no = lpad(?,20,'0') ");
			sqlStr.add(followed_teller);
		}
		
		whereSql.append(
				"                 group by substr(t.data_date, 0, 6),                                                                           "+
				"                          t."+getOrgColumn(by2)+",                                                                                                "+
				"                          p.orgname,                                                                                         "+
				"                          t.teller_no,                                                                                           "+
				"                          u.name) temp)                                                                                          "+
				" where rn < 10)                                                                                                                  "
				);
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql.toString(),extraSql.toString()+whereSql.toString(), sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "运营全景图", "3", "运营全景图-查询");
		log.info("运营全景图-查询");
		// 返回json数据
		renderJson();
	}
	
	
	/**
	 * 查询 人员维度 柱状图
	 */
	public void getRwBarList() {
		//获取查询条件
		//时间-月度
		String month_date = getPara("monthdate");
		//人员
		String followed_teller = getPara("followed_teller");
		//当前登录人角色
		String roleid = getCurrentUser().getStr("ROLE_ID");
		String orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		
		String orgnum = getPara("orgid");
		if(AppUtils.StringUtil(orgnum)!=null){
			orgid = orgnum;
		}
		
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2,orgname from sys_org_info  where orgnum ='"+orgid+"'  and  stat ='1'");
		String by2 = orgRe.getStr("by2");
		String orgname = orgRe.getStr("orgname");
		
		//查询 字典项 业务模块 
		List<Record> paramList = Db.find(" select * from sys_param_info p where p.key='dop_ywtype' and p.val =? order by sortnum",model);
		//组装
		//legend
		List<String> legendList = new ArrayList<>();
		//series
		List<Record> seriesList = new ArrayList<>();
		//xAxis 
		List<String> xAxisList = new ArrayList<>();
		
				
		//查询业务模型数据
		StringBuffer selectSql = new StringBuffer("select * from (select ");
		
		boolean isFir = true;
		for (Record r : paramList) {
			if(isFir){
				isFir = false;
			}else{
				selectSql.append(",");
			}
			selectSql.append("sum(case w.busi_module when '"+r.getStr("VAL")+"' then 1 else 0 end) A"+r.getStr("VAL"));
		}
		
		selectSql.append(",count(*) as warning_num,w.warning_name from dop_warning_info t             "+
				" inner join dop_warning_param w          "+
                "  on t.warning_code = w.warning_code    "+
				" inner join sys_org_info p                "+
				"  on t.deptno = p.orgnum                "+
				" WHERE  w.warning_dimension = '1'  and  (w.is_use is  null or w.is_use = '1')       "+
				"  and t.IS_QUESTION = '1'             "+
				"  and t.teller_no != '"+leftFormat("0", 20)+"'           "+
				"  and w.busi_module = '"+model+"'"+
				"  and t.teller_no not like 'ATM%'        "
				);
		
		
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		
		//机构号条件匹配
		if("8710DBD0E9A94238BF4605C871B61BF1".equals(roleid)){
			selectSql.append("  and t."+getOrgColumn(by2)+" = ? and t.deptlevel='"+getOrgLeve(by2)+"' ");
			sqlStr.add(orgid);
		}else{
			if("4".equals(by2)){
				selectSql.append("  and t."+getOrgColumn(by2)+" = ? and t.deptlevel='"+getOrgLeve(by2)+"' ");
				sqlStr.add(orgid);
			}else if(!"1".equals(by2)){
				selectSql.append("  and t."+getOrgColumn(by2)+" = ?");
				sqlStr.add(orgid);
				//sqlStr.add(orgid);
			}
		}
		if(AppUtils.StringUtil(month_date)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			selectSql.append("  and substr(t.data_date, 0, 6) = to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMM') ");
			sqlStr.add(month_date);
		}
		if(AppUtils.StringUtil(followed_teller)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			selectSql.append("  and t.teller_no = ? ");
			sqlStr.add(leftFormat(followed_teller, 20));
		}
		selectSql.append("  group by w.warning_name) where rownum<=5 order by warning_num desc");
		/*selectSql.append(
				"                 group by substr(t.data_date, 0, 6),"+
				"                          t."+getOrgColumn(by2)+",                     "+
				"                          p.orgname                   "
				);*/
		
		List<Record> chartsList = Db.find(selectSql.toString(),sqlStr.toArray());
		
		Record serie ;
		 isFir = true;
		List<String> values = new ArrayList<>();
		for (Record r : chartsList) {
			//legendList.add(orgname);
			values = new ArrayList<>();
			serie = new Record();
			serie.set("name", r.getStr("warning_name"));
			serie.set("type", "bar");
			serie.set("barGap", 0);
			for (Record r1 : paramList) {
				
				values.add(r.get("A"+r1.getStr("VAL")) == null? "0" :r.getBigDecimal("A"+r1.getStr("VAL")).intValue()+"");
				if(isFir){
					xAxisList.add(r1.getStr("REMARK"));
				}
			}
			isFir = false;
			serie.set("data", values);
			seriesList.add(serie);
		}
		// 赋值
		setAttr("legend", legendList);
		setAttr("series", seriesList);
		setAttr("xAxis", xAxisList);
		
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "模块全景图", "3", "模块全景图-查询");
		log.info("模块全景图-查询");
		// 返回json数据
		renderJson();
	}
	
	
	/**
	 * 查询 预警信息 堆叠式柱状图
	 */
	public void getYjBarList() {
		//获取查询条件
		// 触警时间-开始
		String startime = getPara("startime");
		// 触警时间-结束
		String endtime = getPara("endtime");
		//数据类型 0-预警数量,1-确认问题数量
		String yjdatatype = getPara("yjdatatype");
		//统计口径 0-当前用户层级,1-下级管辖机构
		String yjtjtype = getPara("yjtjtype");
		//业务类型
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String ywtype = rd.getStr("MODEL");
		//预警等级
		String warninglvl = getPara("warninglvl");
		//当前登录人角色
		String roleid = getCurrentUser().getStr("ROLE_ID");
		String orgid = getPara("orgid");
		if(AppUtils.StringUtil(orgid)==null){
			orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		}
		
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2,orgname from sys_org_info  where orgnum ='"+orgid+"'  and  stat ='1'");
		String by2 = orgRe.getStr("by2");
		
		//查询 字典项 业务模块 
		
		StringBuffer paramSql = new StringBuffer("select * from sys_param_info p where p.key='dop_ywtype'  ");
		if(AppUtils.StringUtil(ywtype)!=null && !"1001".equals(ywtype)){
			paramSql.append(" and p.val ='"+ywtype+"' ");
		}else{
			paramSql.append(" and p.val !='1001' ");
		}
		paramSql.append(" order by sortnum");
		
		List<Record> paramList = Db.find(paramSql.toString());
		//组装
		//legend
		List<String> legendList = new ArrayList<>();
		//series
		List<Record> seriesList = new ArrayList<>();
		//xAxis 
		List<String> xAxisList = new ArrayList<>();
		
		

		//机构号条件匹配
		String orgColumntype = "",orgname = "p.orgname";
		if("8710DBD0E9A94238BF4605C871B61BF1".equals(roleid)){
			orgColumntype = getOrgColumn(by2);
		}else{
			//统计口径-必输
			if(AppUtils.StringUtil(yjtjtype)!=null){
				if("0".equals(yjtjtype)){
					if("1".equals(by2)){
						orgColumntype = "'"+orgid+"'";
						orgname = "'"+orgRe.getStr("orgname")+"' orgname";
					}else{
						orgColumntype = "t."+getOrgColumn(by2);
					}
				}else if("1".equals(yjtjtype)){
					orgColumntype = getDowOrgColumn(by2);
				}
			}
		}
		
		//查询业务模型数据
		StringBuffer selectSql = new StringBuffer("select * from( select "+orgColumntype+" deptno,"+orgname+",");
		selectSql.append("count(1) cut,");
		boolean isFir = true;
		for (Record r : paramList) {
			if(isFir){
				isFir = false;
			}else{
				selectSql.append(",");
			}
			selectSql.append("sum(case w.busi_module when '"+r.getStr("VAL")+"' then 1 else 0 end) A"+r.getStr("VAL"));
		}
		
		selectSql.append(" from dop_warning_info t inner join dop_warning_param w on t.warning_code = w.WARNING_CODE  ");
		
		if("0".equals(yjtjtype) && "1".equals(by2)){
			
		}else {
			selectSql.append( " inner join sys_org_info p  on "+orgColumntype+" = p.orgnum  ");
		}
		
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		
		//机构号条件匹配
		if("8710DBD0E9A94238BF4605C871B61BF1".equals(roleid)){
			selectSql.append("  and t."+orgColumntype+" = ? and t.deptlevel = '"+getOrgLeve(by2)+"' ");
			sqlStr.add(orgid);
		}else{
			//统计口径-必输
			if(AppUtils.StringUtil(yjtjtype)!=null){
				if("0".equals(yjtjtype)){
					if("1".equals(by2)){
						//selectSql.append("  and t."+getOrgColumn(by2)+" = ?");
						//sqlStr.add(orgid);
					}else{
						selectSql.append("  and t."+getOrgColumn(by2)+" = ?  ");
						sqlStr.add(orgid);
					}
				}else if("1".equals(yjtjtype)){
					if("1".equals(by2)){
						selectSql.append(" and  t."+getOrgColumn(by2)+" in (select ORGNUM from sys_org_info where by2=2 and ORGNUM !=?  start with ORGNUM=? connect by prior ORGNUM = upid)");
						sqlStr.add(orgid);
						sqlStr.add(orgid);
					}else{
						selectSql.append(" and  t."+getOrgColumn(by2)+" =? ");
						sqlStr.add(orgid);
					}
				}
			}
		}
		//数据类型
		if(AppUtils.StringUtil(yjdatatype)!=null){
			if("1".equals(yjdatatype)){
				selectSql.append(" and t.IS_QUESTION = '1' ");
			}
		}
		//预警等级
		if(AppUtils.StringUtil(warninglvl)!=null){
			selectSql.append(" and w.warning_level = ? ");
			sqlStr.add(warninglvl);
		}
		//开始时间
		if(AppUtils.StringUtil(startime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			selectSql.append(" and t.DATA_DATE >= to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMdd') ");
			sqlStr.add(startime);
		}
		//结束时间
		if(AppUtils.StringUtil(endtime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			selectSql.append(" and t.DATA_DATE <=  to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMdd')  ");
			sqlStr.add(endtime);
		}
		
		if( AppUtils.StringUtil(startime)==null && AppUtils.StringUtil(endtime)==null){
			//whereSql.append(" and substr(t.DATA_DATE,0,4) =  to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMddHH24miss')  ");
			selectSql.append("  and substr(t.DATA_DATE,0,4) = to_char(add_months(trunc(sysdate),-1),'yyyyMM') ");
		}
		String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		if (AppUtils.StringUtil(orgid) != null) {			//页面上选的机构
			//whereSql.append(" and i.deptno in (select id from sys_org_info start with orgnum = '"+org+"' connect by prior orgnum=upid) ");
			selectSql.append(" and t.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
			sqlStr.add(orgid.trim());
			sqlStr.add("%" + orgid.trim()+ "%");
		}else{
			selectSql.append(" and t.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
			sqlStr.add(sjfw.trim());
			sqlStr.add("%" + sjfw.trim()+ "%");
		}
		if("0".equals(yjtjtype) && "1".equals(by2)){
			selectSql.append( " and  (w.is_use is  null or w.is_use = '1') ");
		}else {
			selectSql.append( " and  (w.is_use is  null or w.is_use = '1') group by "+orgColumntype+", p.orgname ");
		}
		selectSql.append( ") order by  A" + paramList.get(0).getStr("VAL") + " desc");
		
	
		
		List<Record> chartsList = Db.find(selectSql.toString(),sqlStr.toArray());
		
		List<Record> rr = new ArrayList<Record>();
		   //过滤是否是问题（为0过滤）
			if(chartsList != null && chartsList.size()>0 ){
				String WA1001_01="";
				BigDecimal bd=null;
				for(Record wa : chartsList){
					bd=wa.get("a"+ywtype); 
					WA1001_01=bd.toString();
					if(!("0".equals(WA1001_01))){
						rr.add(wa);
					}
				}
			}
			chartsList.clear();
			chartsList.addAll(rr);
		
		Record serie ;
		isFir = true;
		double count = 0.00;
		//进行数据分组
		Map<String,List<String>> groupMap = new HashMap<String, List<String>>();
		
		
		for (Record r1 : paramList) {
			legendList.add(r1.getStr("REMARK"));
			groupMap.put("A"+r1.getStr("VAL"), new ArrayList<String>());
		}
		
		int cut = 0;
		for (Record r : chartsList) {
			xAxisList.add(r.getStr("orgname"));
			for (Record r1 : paramList) {
				cut=r.getBigDecimal("A"+r1.getStr("VAL")).intValue();
				count+=cut;
				groupMap.get("A"+r1.getStr("VAL")).add(cut+"");
			}
		}
		for (Record r1 : paramList) {
			serie = new Record();
			serie.set("name", r1.getStr("REMARK"));
			serie.set("type", "bar");
			serie.set("data", groupMap.get("A"+r1.getStr("VAL")));
			seriesList.add(serie);
		}
		
		//计算平均值
		double avgnum = count/chartsList.size();
		DecimalFormat df = new DecimalFormat("#.00");
		List<String> values = new ArrayList<>();
		
		for (Record r : chartsList) {
			values.add(String.format("%.2f",avgnum));
		}
		legendList.add("平均值");
		serie = new Record();
		serie.set("name", "平均值");
		serie.set("type", "line");
		serie.set("data", values);
		seriesList.add(serie);
		
		// 赋值
		setAttr("legend", legendList);
		setAttr("series", seriesList);
		setAttr("xAxis", xAxisList);
		
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "运营全景图", "3", "运营全景图-查询");
		log.info("运营全景图-查询");
		// 返回json数据
		renderJson();
	}
	
	
	/**
	 * 查询 预警信息 表格
	 */
	public void getYjTableList() {
		//获取查询条件
		// 触警时间-开始
		String startime = getPara("startime");
		// 触警时间-结束
		String endtime = getPara("endtime");
		//数据类型 0-预警数量,1-确认问题数量
		String yjdatatype = getPara("yjdatatype");
		//统计口径 0-当前用户层级,1-下级管辖机构
		String yjtjtype = getPara("yjtjtype");
		//业务类型
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String ywtype = rd.getStr("MODEL");
//		String ywtype = getPara("ywtype");
		//预警等级
		String warninglvl = getPara("warninglvl");
		//预警名称
		String warningname = getPara("warningname");
		//当前登录人角色
		String roleid = getCurrentUser().getStr("ROLE_ID");
		String orgid = getPara("orgid");
		if(AppUtils.StringUtil(orgid) == null){
			orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		}
		System.out.println("orgid:  "+orgid);
		// 获取查询条件
		int pageNum = getParaToInt("pageIndex");
		int pageSize = getParaToInt("pageSize",5);
		//组装headers
		List<Record> headers = new ArrayList<>();
		
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2,orgname from sys_org_info  where orgnum ='"+orgid+"'  and  stat ='1'");
		String by2 = orgRe.getStr("by2");
		
		//查询 字典项 业务模块 
		
		StringBuffer paramSql = new StringBuffer("select * from sys_param_info p where p.key='dop_ywtype'  ");
		if(AppUtils.StringUtil(ywtype)!=null && !"1001".equals(ywtype)){
			paramSql.append(" and p.val ='"+ywtype+"' ");
		}else{
			paramSql.append(" and p.val !='1001' ");
		}
		paramSql.append(" order by sortnum");
		
		List<Record> paramList = Db.find(paramSql.toString());
				
		//查询业务模型数据
		
		//机构号条件匹配
		String orgColumntype = "" ,orgname = "p.orgname";
		if("8710DBD0E9A94238BF4605C871B61BF1".equals(roleid)){
			orgColumntype = getOrgColumn(by2);
		}else{
			//统计口径-必输
			if(AppUtils.StringUtil(yjtjtype)!=null){
				if("0".equals(yjtjtype)){
					if("1".equals(by2)){
						orgColumntype = "'"+orgid+"'";
						orgname = "'"+orgRe.getStr("orgname")+"' orgname";
					}else{
						orgColumntype = "t."+getOrgColumn(by2);
					}
				}else if("1".equals(yjtjtype)){
					orgColumntype = "t."+getDowOrgColumn(by2);
				}
			}
		}
				
		
		
		StringBuffer selectSql = new StringBuffer(" select * ");
		StringBuffer extraSql = new StringBuffer(" from(select "+orgColumntype+" deptno,"+orgname+",count(1) yacut,sum(case t.IS_QUESTION when '1' then 1 else 0 end) wacut");
		
		if("0".equals(yjtjtype)){
			extraSql.append(",w.warning_name");
		}
		
		StringBuffer whereSql = new StringBuffer(" WHERE 1=1 ");
		List<String> sqlStr = new ArrayList<String>();
		
		if(paramList.size()>1){
			headers.add(new Record().set("text", "合计").set("field", "yacut"));
		}
		
		boolean isFir = true;
		//如果不是1-确认是问题不添加预警
		if("0".equals(yjdatatype)){
			for (Record r : paramList) {
				headers.add(new Record().set("text", r.getStr("REMARK")).set("field", "ya"+r.getStr("VAL")));
				if(isFir){
					isFir = false;
				}else{     
					extraSql.append(",");
				}
				extraSql.append(",sum(case  when  w.busi_module = '"+r.getStr("VAL")+"'   then 1 else 0 end) YA"+r.getStr("VAL"));
			}
		}
	
		if(paramList.size()>1){
			headers.add(new Record().set("text", "合计").set("field", "wacut"));
		}
		isFir = true;
		for (Record r : paramList) {
			headers.add(new Record().set("text", r.getStr("REMARK")).set("field", "wa"+r.getStr("VAL")));
			//if(isFir){
				//isFir = false;
			//}else{
				extraSql.append(",");
			//}
			extraSql.append("sum(case  when  w.busi_module = '"+r.getStr("VAL")+"'  and t.IS_QUESTION = '1' then 1 else 0 end) WA"+r.getStr("VAL"));
		}
		
		extraSql.append(" from dop_warning_info t inner join dop_warning_param w on t.warning_code = w.WARNING_CODE ");
		if("0".equals(yjtjtype) && "1".equals(by2)){
			
		}else {
			extraSql.append( " inner join sys_org_info p  on "+orgColumntype+" = p.orgnum  ");
		}
		// 查询条件
		
		//机构号条件匹配
		if("8710DBD0E9A94238BF4605C871B61BF1".equals(roleid)){
			whereSql.append("  and t."+orgColumntype+" = ? and t.deptlevel = '"+getOrgLeve(by2)+"' ");
			sqlStr.add(orgid);
		}else{
			//统计口径-必输
			if(AppUtils.StringUtil(yjtjtype)!=null){
				if("0".equals(yjtjtype)){
					if("1".equals(by2)){
						//whereSql.append("  and t."+getOrgColumn(by2)+" = ?");
						//sqlStr.add(orgid);
					}else{
						whereSql.append("  and t."+getOrgColumn(by2)+" = ? ");
						sqlStr.add(orgid);
					}
				}else if("1".equals(yjtjtype)){
					if("1".equals(by2)){
						whereSql.append(" and  t."+getOrgColumn(by2)+" in (select ORGNUM from sys_org_info where by2=2 and ORGNUM !=?  start with ORGNUM=? connect by prior ORGNUM = upid)");
						sqlStr.add(orgid);
						sqlStr.add(orgid);
					}else{
						whereSql.append(" and  t."+getOrgColumn(by2)+" =? ");
						sqlStr.add(orgid);
					}
				}
			}
		}
		//数据类型
		if(AppUtils.StringUtil(yjdatatype)!=null){
			if("1".equals(yjdatatype)){
				whereSql.append(" and t.IS_QUESTION = '1' ");
			}
		}
		//预警等级
		if(AppUtils.StringUtil(warninglvl)!=null){
			whereSql.append(" and w.warning_level = ? ");
			sqlStr.add(warninglvl);
		}
		//预警名称
		if(AppUtils.StringUtil(warningname)!=null){
			whereSql.append(" and w.warning_name like ? ");
			sqlStr.add("%"+warningname.trim()+"%");
		}
		
		//开始时间
		if(AppUtils.StringUtil(startime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			whereSql.append(" and t.DATA_DATE >= to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMdd') ");
			sqlStr.add(startime);
		}
		//结束时间
		if(AppUtils.StringUtil(endtime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			whereSql.append(" and t.DATA_DATE <=  to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMdd')  ");
			sqlStr.add(endtime);
		}
		if( AppUtils.StringUtil(startime)==null && AppUtils.StringUtil(endtime)==null){
			//whereSql.append(" and substr(t.DATA_DATE,0,4) =  to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMddHH24miss')  ");
			whereSql.append("  and substr(t.DATA_DATE,0,6) = to_char(add_months(trunc(sysdate),-1),'yyyyMM') ");
		}
		String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");//数据范围,不能取用户自己所在的机构，而要按照用户的数据范围、角色级别 来查， 数据范围优先
		
		if (AppUtils.StringUtil(orgid) != null) {			//页面上选的机构
			//whereSql.append(" and i.deptno in (select id from sys_org_info start with orgnum = '"+org+"' connect by prior orgnum=upid) ");
			whereSql.append(" and t.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
			sqlStr.add(orgid.trim());
			sqlStr.add("%" + orgid.trim()+ "%");
		}else{
			whereSql.append(" and t.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
			sqlStr.add(sjfw.trim());
			sqlStr.add("%" + sjfw.trim()+ "%");
		}
		
		if("0".equals(yjtjtype) && "1".equals(by2)){
			if("0".equals(yjdatatype)){
				whereSql.append( " and  (w.is_use is  null or w.is_use = '1') and w.busi_module = '"+ywtype+"' group by w.warning_name) order by YA"+ywtype+" desc,warning_name desc");
			}else{
				whereSql.append( " and  (w.is_use is  null or w.is_use = '1') and w.busi_module = '"+ywtype+"' group by w.warning_name) order by warning_name desc");
			}
			
		} else if("0".equals(yjtjtype) &&! "1".equals(by2)){
	       if("0".equals(yjdatatype)){
	    	   whereSql.append( " and  (w.is_use is  null or w.is_use = '1') and w.busi_module = '"+ywtype+"' group by w.warning_name,"+orgColumntype+", p.orgname) order by YA"+ywtype+" desc,warning_name desc");
			}else{
				whereSql.append( " and  (w.is_use is  null or w.is_use = '1') and w.busi_module = '"+ywtype+"' group by w.warning_name,"+orgColumntype+", p.orgname) order by warning_name desc");
			}
			
		}else {
	       if("0".equals(yjdatatype)){
	    	   whereSql.append( " and  (w.is_use is  null or w.is_use = '1') group by "+orgColumntype+", p.orgname ) order by YA"+ywtype+" desc");	
			}else{
				whereSql.append( " and  (w.is_use is  null or w.is_use = '1') group by "+orgColumntype+", p.orgname ) order by WA"+ywtype+" desc ");
			}
			
		}
		
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql.toString(),extraSql.toString()+whereSql.toString(), sqlStr.toArray());
		
		if("1".equals(yjdatatype)){
			List<Record> rr = new ArrayList<Record>();
			
			   //过滤是否是问题（为0过滤）
				
				if(r != null && r.getList().size()>0 ){
					String WA1001_01="";
					BigDecimal bd=null;
					for(Record wa : r.getList()){
						bd=wa.get("WA"+ywtype); 
						WA1001_01=bd.toString();
						if(!("0".equals(WA1001_01)) && null !=wa){
							rr.add(wa);
						}
					}
				}
				r.getList().clear();
				r.getList().addAll(rr);
				setAttr("total", r.getList().size());
		}else{
			setAttr("total", r.getTotalRow());
		}
	
	    
		// 赋值
		setAttr("data", r.getList());
		setAttr("headers",headers);
		setAttr("yjtjtype",yjtjtype);
		setAttr("yjdatatype",yjdatatype);
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "运营全景图", "3", "运营全景图-查询");
		log.info("运营全景图-查询");
		// 返回json数据
		renderJson();
	}
	
	
	/**
	 * 查询 详情预警信息 表格
	 */
	public void getYjDetailTableList() {
		//获取查询条件
		// 触警时间-开始
		String startime = getPara("startime");
		// 触警时间-结束
		String endtime = getPara("endtime");
		//数据类型 0-预警数量,1-确认问题数量
		String yjdatatype = getPara("yjdatatype");
		//统计口径 0-当前用户层级,1-下级管辖机构
		String yjtjtype = getPara("yjtjtype");
		//业务类型
//		String ywtype = getPara("ywtype"); //! null
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String ywtype = rd.getStr("MODEL");
		
		//预警等级
		String warninglvl = getPara("warninglvl");
		//预警名称
		String warningname = getPara("warningname");
		//当前登录人角色
		String roleid = getCurrentUser().getStr("ROLE_ID");
		String orgid = getPara("orgid"); 
		if(AppUtils.StringUtil(orgid)==null){
			orgid = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		}
		
		// 获取查询条件
		int pageNum = getParaToInt("pageIndex")+ 1;
		int pageSize = getParaToInt("pageSize",10);

		
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2 from sys_org_info  where orgnum ='"+orgid+"'  and  stat ='1'");
		String by2 = orgRe.getStr("by2");
		
		
		String orgColumntype = "";
		if("8710DBD0E9A94238BF4605C871B61BF1".equals(roleid)){
			orgColumntype = getOrgColumn(by2);
		}else{
			//统计口径-必输
			if(AppUtils.StringUtil(yjtjtype)!=null){
					orgColumntype = getDowOrgColumn(by2);
				
			}
		}
		
		//查询业务模型数据
		
		StringBuffer selectSql = new StringBuffer("select t.deptno,to_char(to_date(t.DATA_DATE,'yyyyMMddHH24miss'),'yyyy/MM/dd') create_time," +
				"t.warning_code,w.warning_name,p.orgname,w.busi_module,(select remark from sys_param_info i where i.key='dop_ywtype' and val = w.busi_module) busi_modulename," +
				"(select remark from DOP_GANGEDMENU_INFO t where val=w.warning_type_code start with t.upid = (select id from DOP_GANGEDMENU_INFO t where val='1003' ) connect by prior id = upid)warning_type_code," +
				"t.IS_QUESTION,w.warning_level,t.last_check_stat, t.warning_status");
		StringBuffer extraSql = new StringBuffer(" from dop_warning_info t inner join dop_warning_param w on t.warning_code = w.WARNING_CODE inner join sys_org_info p  on t.deptno = p.orgnum");
				

		StringBuffer whereSql = new StringBuffer(" WHERE 1=1 and  (w.is_use is  null or w.is_use = '1') ");
		List<String> sqlStr = new ArrayList<String>();	
		
		// 查询条件
		
		//机构号条件匹配
		if("8710DBD0E9A94238BF4605C871B61BF1".equals(roleid)){
			if(AppUtils.StringUtil(warningname)!=null){
				whereSql.append("  and t."+getOrgColumn(by2)+" in (select id from sys_org_info where id = ? or by5 like '%"+orgid+"%') and t.deptlevel = '"+getOrgLeve(by2)+"'");
			}else{
				whereSql.append("  and t."+getOrgColumn(by2)+" =? and t.deptlevel = '"+getOrgLeve(by2)+"' ");
			}
			
			sqlStr.add(orgid);
		}else{
			//统计口径-必输
			if(AppUtils.StringUtil(yjtjtype)!=null){
				if("0".equals(yjtjtype)){
					if(AppUtils.StringUtil(warningname)!=null){
						whereSql.append("  and t."+getOrgColumn(by2)+" in (select id from sys_org_info where id = ? or by5 like '%"+orgid+"%') ");
					}else{
						whereSql.append("  and t."+getOrgColumn(by2)+" =?  ");
					}
					
					sqlStr.add(orgid);
				}else if("1".equals(yjtjtype)){
					if("1".equals(by2)){
						whereSql.append(" and  t."+getOrgColumn(by2)+" in (select ORGNUM from sys_org_info where by2=2 and ORGNUM !=?  start with ORGNUM=? connect by prior ORGNUM = upid)");
						sqlStr.add(orgid);
						sqlStr.add(orgid);
					}else{
						if(AppUtils.StringUtil(warningname)!=null){
							whereSql.append("  and t."+getOrgColumn(by2)+" in (select id from sys_org_info where id = ? or by5 like '%"+orgid+"%') ");
						}else{
							whereSql.append("  and t."+getOrgColumn(by2)+" =?  ");
						}
						sqlStr.add(orgid);
					}
				}
			}
		}
		//数据类型
		if(AppUtils.StringUtil(yjdatatype)!=null){
			if("1".equals(yjdatatype)){
				whereSql.append(" and t.IS_QUESTION = '1' ");
			}
		}
		//业务类型
		if(AppUtils.StringUtil(ywtype)!=null && !"1001".equals(ywtype)){
			whereSql.append(" and w.busi_module = ? ");
			sqlStr.add(ywtype);
		}
		//预警等级
		if(AppUtils.StringUtil(warninglvl)!=null){
			whereSql.append(" and w.warning_level = ? ");
			sqlStr.add(warninglvl);
		}
		//预警名称
		if(AppUtils.StringUtil(warningname)!=null){
			whereSql.append(" and w.warning_name = ? ");
			sqlStr.add(warningname.trim());
		}
		
		
		//开始时间
		if(AppUtils.StringUtil(startime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			whereSql.append(" and t.DATA_DATE >= to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMdd') ");
			sqlStr.add(startime);
		}
		//结束时间
		if(AppUtils.StringUtil(endtime)!=null){
			//数据产生时间YYYYMMDDHHMMSS
			whereSql.append(" and t.DATA_DATE <=  to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMdd')  ");
			sqlStr.add(endtime);
		}
		if( AppUtils.StringUtil(startime)==null && AppUtils.StringUtil(endtime)==null){
			//whereSql.append(" and substr(t.DATA_DATE,0,4) =  to_char(to_date(?,'yyyy-MM-dd HH24:mi:ss'),'yyyyMMddHH24miss')  ");
			whereSql.append("  and substr(t.DATA_DATE,0,6) = to_char(add_months(trunc(sysdate),-1),'yyyyMM') ");
		}
		
		whereSql.append( " order by t.DATA_DATE,t.id");
		
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql.toString(),extraSql.toString()+whereSql.toString(), sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "运营全景图", "3", "运营全景图-查询");
		log.info("运营全景图-查询");
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 查询当前机构下辖机构的数据
	 */
	public void getListByOrg() {
		// 获取当前用户所属机构
		//String userNo = getCurrentUser().getStr("USER_NO");
		String orgnum = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		
		String userno = getCurrentUser().getStr("USER_NO");
		Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", userno);
		String model = rd.getStr("MODEL");
		//取每月前N天取上上月的数据,N天后取上月数据 N由字典配置
		ParamContainer pc = new ParamContainer();
		@SuppressWarnings("static-access")
		String dayStr = pc.getDictName("dop_indexradard", "day");
		int day = 5;
		if(AppUtils.StringUtil(dayStr) != null){
			day = Integer.parseInt(dayStr);
		}
		//获取当前天是本月第几天
		int thisDay = DateTimeUtil.getDayOfMonth();
		//层次 1-首页,0-其他 只为处理首页雷达图数据
		String querypagetype = getPara("querypagetype","0");
		int oldmonth = -1;  //默认一个月前
		if(thisDay <= day && querypagetype.equals("1")){
			oldmonth = -2;// 两个月前
		}
		//获取最新data_month
		Record newdata=Db.findFirst(" select distinct(t.data_month)data_month  from DOP_ORG_SCORE_DETAIL t  group by t.data_month  order by  t.data_month desc ");
		String datamonth=newdata.getStr("data_month");
		
		//获取当前机构级别
		Record orgRe = Db.findFirst("  select by2 from sys_org_info  where orgnum ='"+orgnum+"'  and  stat ='1'");
		String orgLevel = orgRe.getStr("by2");
		// 获取查询条件
		int pageNum = getParaToInt("pageIndex");
		int pageSize = 5;
		//获取上个月份
		//获得系统当前时间
		SimpleDateFormat sbf = new SimpleDateFormat("YYYYMM");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, oldmonth);
		String beforeMonth = sbf.format(cal.getTime());
		// 查询语句
		StringBuffer selectSql = new StringBuffer(" select sum(t.score) as total,p.orgname,T.DEPTNO " );
		
		StringBuffer extraSql = new StringBuffer(" from DOP_ORG_SCORE_DETAIL t inner join sys_org_info p on T.DEPTNO=P.ORGNUM ");

		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 and  t.indexname ='ZF' and t.module = '"+model+"' ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if(AppUtils.StringUtil(datamonth) != null){
			whereSql.append(" and t.data_month = ?   ");
			sqlStr.add(datamonth);
		}
		if (AppUtils.StringUtil(orgLevel) != null) {
			//除网点外的其他层级机构
			if(!"4".equals(orgLevel)){
				whereSql.append(" AND  t.deptno in  (select orgnum from sys_org_info where upid=? and stat=1 ) ");
				sqlStr.add(orgnum);
			}else{
				whereSql.append(" AND  1 = 2 ");
			}
		}
		
		// 排序
		whereSql.append("  group by t.deptno,p.orgname  order by total desc ");
		//extraSql = whereSql.toString();
		// 查询
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql.toString(),extraSql.toString()+whereSql.toString(), sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "运营全景图", "3", "运营全景图-查询");
		log.info("运营全景图-查询");
		// 返回json数据
		renderJson();
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
	public String getDowOrgColumn(String by2){
		String OrgColumn;
		switch(by2){
		case "1":OrgColumn = "lvl_2_branch_no";break;//顶级行
		case "2":OrgColumn = "lvl_3_branch_no";break;//分行
		case "3":OrgColumn = "lvl_4_branch_no";break;//支行
		case "4":OrgColumn = "lvl_4_branch_no";break;//网点/辖区
		default:OrgColumn="";break;
		}
		return OrgColumn;
	}
	
	public String getOrgLeve(String by2){
		String OrgColumn;
		switch(by2){
		case "1":OrgColumn = "0";break;//顶级行
		case "2":OrgColumn = "1";break;//分行
		case "3":OrgColumn = "2";break;//支行
		case "4":OrgColumn = "3";break;//网点/辖区
		default:OrgColumn="";break;
		}
		return OrgColumn;
	}
	
	public String getUpOrgLeve(String by2){
		String OrgColumn;
		switch(by2){
		case "1":OrgColumn = "0";break;//顶级行
		case "2":OrgColumn = "0";break;//分行
		case "3":OrgColumn = "1";break;//支行
		case "4":OrgColumn = "2";break;//网点/辖区
		default:OrgColumn="";break;
		}
		return OrgColumn;
	}
	
	public String getDowOrgLeve(String by2){
		String OrgColumn;
		switch(by2){
		case "1":OrgColumn = "1";break;//顶级行
		case "2":OrgColumn = "2";break;//分行
		case "3":OrgColumn = "3";break;//支行
		case "4":OrgColumn = "3";break;//网点/辖区
		default:OrgColumn="";break;
		}
		return OrgColumn;
	}
	
	
	public void  download(){
		String method = getPara("method");
		if("getTableListForWd".equals(method)){
			setAttr("wd", "wd");
		}else{
			setAttr("wd", "");
		}
		switch(method){
			case "getTableList" : getTableList();break;
			case "getTableListForWd" : downloadWdData();break;
			case "getRwTableList" : getRwTableList();break;
			case "getKeynoteTaList" : getKeynoteTaList();break;
			case "getYjTableList" : getYjTableList();break;
			default:break;
		}
		List<Record> list = getAttr("data");
		String yjtjtype=getAttr("yjtjtype");
		String yjdatatype=getAttr("yjdatatype");
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = {};//getPara("execlheaders").split(",");
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = {};//getPara("execlcolumns").split(",");;
	   
		String fileName = "";
		try {
			fileName = new String((getPara("execlfilename")+System.currentTimeMillis()+".xls").getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
		// 转换成excel
		ExcelRenderx er = null;
		if(!"getYjTableList".equals(method)&&!"getTableListForWd".equals(method)){
			// 指定用哪些查询出来的字段填充excel文件
			headers = getPara("execlheaders").split(",");
			// excel文件的每列的名称,顺序与填充字段的顺序保持一致
			columns = getPara("execlcolumns").split(",");
			er = new ExcelRenderx(fileName, columns, headers, list, getResponse());
			er.render(); 
		}else if("getYjTableList".equals(method)){
			
			List<Record> headList = getAttr("headers");
			String [] headers1 = new String[headList.size()+1];
			String [] columns1 = new String[headList.size()+1];
			if("0".equals(yjtjtype)){
				columns1[0] = "warning_name";
				headers1[0] = "问题名称";
			}else{
				columns1[0] = "orgname";
				headers1[0] = "机构名称";	
			}
			
			for (int i = 0; i < headList.size(); i++) {
				headers1[i+1] = headList.get(i).getStr("text");
				columns1[i+1] = headList.get(i).getStr("field");
			}
			
			int len = headList.size()/2;
			if("1".equals(yjdatatype)){
				len=headList.size();
			}
			//头部两行需要合并
			String [] oneRowArr = new String[headList.size()+1];
			if("0".equals(yjtjtype)){
				oneRowArr[0] = "问题名称";
			}else{
				oneRowArr[0] = "机构名称";
			}
			//选择是问题yjdatatype-1
			if("1".equals(yjdatatype)){
				oneRowArr[1]="问题数量";
			}else{
				for(int i=1;i<headList.size()+1;i++){
					if(i<len+1){
						oneRowArr[i] = "预警数量";
					}else{
						oneRowArr[i] = "问题数量";
					}
					
				}
			}
		
			//二维数组2行头部
			String [][] headers2 = new String[2][];
			headers2[0] = oneRowArr;
			headers2[1] = headers1;
			
			
			//设置 单元格 合并位置起始行，结束行，起始列，结束列
			String [][] columnsMulNum  = new String[1][];
		
			if("1".equals(yjdatatype)){
				String [] oneMulNum  = {"0,1,0,0","0,0,1,"+len};
				columnsMulNum[0] = oneMulNum;
			}else{
				String [] oneMulNum   = {"0,1,0,0","0,0,1,"+len,"0,0,"+(len+1)+","+headList.size()};
				columnsMulNum[0] = oneMulNum;
			}
			
		
		
			er = new ExcelRenderx(fileName, columns1, headers2,columnsMulNum, list, getResponse());
			er.renderMulRow(); 
		}else{
			// 指定用哪些查询出来的字段填充excel文件
			headers = getAttr("execlheaders").toString().split(",");
			// excel文件的每列的名称,顺序与填充字段的顺序保持一致
			columns = getAttr("execlcolumns").toString().split(",");
			er = new ExcelRenderx(fileName, columns, headers, list, getResponse());
			er.render(); 
		}
		// 打印日志
		log.info("download--list:" + list);
		renderNull();
	}
	
	private String leftFormat(String userno,int len){
		long i = Integer.parseInt(userno);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumIntegerDigits(len);
		nf.setMinimumIntegerDigits(len);
		return nf.format(i);
	}
	
	
	//根据柜员号或者机构号来查询
		public void markSearch(){
			//获取前台传过来的参数
			String key = getPara("key");
			StringBuffer select = new StringBuffer();
			select.append(" select * ");
			StringBuffer sqlExceptSelect = new StringBuffer();
			sqlExceptSelect.append(" from DOP_MARK_PARAM t where 1=1 ");
			if(AppUtils.StringUtil(key)!=null)
				sqlExceptSelect.append(" and t.MARK_NAME like '%"+key+"%' ");
			
			sqlExceptSelect.append(" order by MARK_NAME ");

			Page<Record> page = Db.paginate(1, pageSize, select.toString(), sqlExceptSelect.toString());
			setAttr("data", page.getList());
			renderJson(page.getList());
		}
		
		
		
		
		//获取我的关注的数据
		public void getMyFocus(){
			String userno = getCurrentUser().getStr("id");
//			String roleLvl = getCurrentUser().getStr("role_level");
			String  maxPermiOrgnum =  getCurrentUser().getStr("MAX_PERMI_ORGNUM");
			String remarks=Db.findFirst("select remark from SYS_PARAM_INFO t where t.key='NearDate' and t.status='1' ").get("remark");
			int date=Integer.parseInt(remarks);
			
			String useno = getCurrentUser().getStr("USER_NO");
			Record rd = Db.findFirst("select b.model from sys_user_info b where b.USER_NO = ?", useno);
			String model = rd.getStr("MODEL");
			
			//先查出关注的预警
			String selectSql = " select followed_org, max(follower) follower, busi_module, sub_busi_code, mark_code, follow_type, followed_teller from (select case when t.followed_org is null then '"+ maxPermiOrgnum +"' else t.followed_org end followed_org, t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where t.busi_module = ? and ((follower = ?  and t.follow_type = '0') " + "or (t.assigned_type = '2' and t.follow_type = '0')))" +
					"  group by followed_org,busi_module,sub_busi_code,mark_code,follow_type,followed_teller order by followed_org,follow_type desc ";
			List<Record> lr = Db.find(selectSql,model,userno);//0代表预警
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
					String followed_org = re.getStr("followed_org");
					String busi_module = re.getStr("busi_module");
					
					String extraSql = " from dop_warning_info i left join dop_warning_param p on" +
							" p.warning_code=i.warning_code where i.deptno in (select id from sys_org_info where id = ? or by5 like ?) and p.busi_module=?  " +
							"and  (p.is_use is  null or p.is_use = '1')  and p.warning_type_code= ?  and p.warning_code= ? and i.create_time >= to_char(" +
							"sysdate-'"+date+"','yyyyMMddHH24:MI:SS') ";
					String wheresql = " ";
					if(followed_teller != null){
						wheresql = " and  i.teller_no = '"+followed_teller+"' ";
					}
					List<Record> warning = Db.find("select distinct (select orgname from sys_org_info where orgnum = ? and stat=1) orgname, " +
							" 	(select remark from sys_param_info where key = 'dop_ywtype' and val=p.busi_module) name, " +
							//" 	(select remark from DOP_GANGEDMENU_INFO where  val= p.warning_type_code) ywzlname, " +
							" 	(select warning_name from dop_warning_param where warning_code=p.warning_code ) warncodename, " +//预警名称
							"   (select count(i.is_question)" + extraSql + wheresql +"  and i.warning_status='1' and is_question = '1'  ) isquestioncount ,"+
							" 	(select d.name from sys_user_info d where d.user_no = i.teller_no ) username,(select count(1) " + extraSql + wheresql +"  and " +
							"  i.warning_status='1'  ) warningcount " +extraSql, followed_org,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code,followed_org,"%"+followed_org+"%",busi_module,sub_busi_code,mark_code);
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
/*			if(org != null){
				String pftjSql = " select t.followed_org,t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where follower = ?  and t.busi_module = ? and t.follow_type = '1' and  t.followed_org = ? " +
						" group by followed_org,t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller order by followed_org,t.follow_type desc ";
				List<Record> pflr = Db.find(pftjSql,userno,model,org);//1代表评分
				StringBuffer sbf = new StringBuffer(" ");
				String busimodule=" ";//业务模块
				if(pflr != null && pflr.size() > 0){
					//拼接完结束
					for (Record record : pflr) {
						String pfSql = " from dop_org_score_detail d left join sys_org_info o on o.id=d.deptno where deptno=? " +
								" and data_month >= to_char(add_months(sysdate,-2),'yyyyMM') and indexname='ZF' " +
								" and module = ? order by d.data_month desc,grow_rate desc ";
						List<Record> scoreTempList = Db.find("select  d.data_month,d.deptno,o.orgname,d.score,case lag(score,1)over(order by data_month) when 0 then 0 else score/lag(score,1)over(order by data_month)-1 end grow_rate "
								+pfSql,org,record.get("busi_module"));
						busimodule=record.get("busi_module");
						if(scoreTempList != null && scoreTempList.size() > 0){
							Record lastMonthRecord = scoreTempList.get(0);//上个月得分
							sbf.append(appendScoreStr(lastMonthRecord,busimodule));//拼接该机构评分
						}
					}
				}
				describe.append(sbf+"+");
			}*/
			String pftjSql = " select followed_org, max(follower) follower, busi_module, sub_busi_code, mark_code, follow_type, followed_teller from(select case when t.followed_org is null then '"+ maxPermiOrgnum +"' else t.followed_org end followed_org, t.follower,t.busi_module,t.sub_busi_code,t.mark_code,t.follow_type,t.followed_teller from dop_my_follow t where t.busi_module = ? and ((follower = ?  and t.follow_type = '1') " + "or (t.assigned_type = '2' and t.follow_type = '1')))" +
					"  group by followed_org,busi_module,sub_busi_code,mark_code,follow_type,followed_teller order by followed_org,follow_type desc ";
			List<Record> pflr = Db.find(pftjSql,userno,model);//1代表评分
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
			}
			return record;
			
		}
		
}
