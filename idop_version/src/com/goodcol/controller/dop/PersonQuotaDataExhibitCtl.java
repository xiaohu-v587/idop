package com.goodcol.controller.dop;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

/*
 * 关键指标数据展现
 * @author 陆磊磊
 * @date 2018-11-19
 */
@RouteBind(path = "/personQuotaDataExhibit")
@Before({ManagerPowerInterceptor.class})
public class PersonQuotaDataExhibitCtl extends BaseCtl{
	
	// 记录日志用
	public static Logger log = Logger.getLogger(PersonQuotaDataExhibitCtl.class); 

	/**
	 * 加载关键指标数据展现主页面
	 */
	@Override
	@Before(PermissionInterceptor.class)
	public void index() {
		String key = getPara("key");
		String style = getPara("style");
		String  orgid = null;
		if(AppUtils.StringUtil(key)!= null){
			if(AppUtils.StringUtil(style)!= null && "0".equals(style)){
				orgid = key;
			}
		}
		setAttr("org", orgid);
		render("index.jsp");
	}
	
	
	public void view() {
		render("viewData.jsp");
	}
	
	public void checkDetail() {
		//获取前台界面的柜员名称
		String orgid = getPara("orgid"); //机构
		String followed_teller = getPara("followed_teller"); //柜员名称
		setAttr("orgid", orgid);
		setAttr("followed_teller", followed_teller);
		render("checkDetail.jsp");
	}
	
	
	/**
	 * 查询所有算法
	 */
	public void getList() {
		// 获取查询条件
		String orgid = getPara("orgid"); //机构
		String busi_module = getPara("busi_module"); //业务模块
		String sub_busi_code = getPara("sub_busi_code"); //业务类型
		String mark_code = getPara("mark_code"); // 指标名称
		String startime = getPara("startime"); // 开始时间
		String endtime = getPara("endtime"); // 结束时间
		String followed_teller = getPara("followed_teller"); // 结束时间
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		String flag = "0"; // 结束时间
		
		
		
		Record sub=Db.findFirst("select id,type,remark,val from DOP_GANGEDMENU_INFO where id=?",new Object[]{sub_busi_code});//获取业务类型详细信息
		
		
		/******关键指标数据展现柱形图 *******/
		
		
		//log.info("关键指标数据展现柱形图存储过程 ap_idop.DPRO_KEY_INDEX_QRY 调用开始:"+ DateTimeUtil.getTime());
		System.out.println("0关键指标数据展现柱形图存储过程 ap_idop.DPRO_KEY_INDEX_QRY_PERSON 调用开始:"+ DateTimeUtil.getTime());
		String validSql_bar = " CALL ap_idop.DPRO_KEY_INDEX_QRY_PERSON('"+ orgid + "','" + startime + "','" + endtime +"','" +busi_module+ "','"+mark_code+"','"+flag+"','"+userNo+"',@a,@b)";
		Db.use("gbase").update(validSql_bar);
		//System.out.println("指标值计算SQL：" + validSql_bar);
		//log.info("关键指标数据展现柱形图存储过程 ap_idop.DPRO_KEY_INDEX_QRY 调用结束:"+ DateTimeUtil.getTime()); 
		System.out.println("1关键指标数据展现柱形图存储过程 ap_idop.DPRO_KEY_INDEX_QRY_PERSON 调用结束:"+ DateTimeUtil.getTime());
		
		
		/******关键指标数据展现条线图 *******/
		System.out.println("2关键指标数据展现条线图存储过程 ap_idop.DPRO_KEY_INDEX_QRY_12MON_PERSON 调用开始:"+ DateTimeUtil.getTime());
		//log.info("关键指标数据展现条线图存储过程 ap_idop.DPRO_KEY_INDEX_QRY_12MON 调用开始:"+ DateTimeUtil.getTime());
		
		String validSql_line = " CALL ap_idop.DPRO_KEY_INDEX_QRY_12MON_PERSON('"+ orgid + "','"+busi_module+ "','"+mark_code+"','"+ followed_teller +"','"+userNo+"',@a,@b)";
		Db.use("gbase").update(validSql_line);
		System.out.println("3指标值计算SQL：" + validSql_line);
		System.out.println("4关键指标数据展现条线图存储过程 ap_idop.DPRO_KEY_INDEX_QRY_12MON_PERSON 调用结束:"+ DateTimeUtil.getTime());
		//log.info("关键指标数据展现条线图存储过程 ap_idop.DPRO_KEY_INDEX_QRY_12MON 调用结束:"+ DateTimeUtil.getTime()); 
		
		Record r = Db.findFirst("select UNIT,MARK_NAME,MARK_NAME||'(单位：'||UNIT||')' LENGEND  from DOP_MARK_PARAM t where MARK_CODE = ?",mark_code);
		
		String lengend = "--";
		if(r != null && AppUtils.StringUtil(r.getStr("UNIT")) != null){
			lengend = r.getStr("LENGEND");
		}
		
			
		// 柱形图查询语句
		String selectSql_bar = "";
		if("%".equals(r.getStr("UNIT"))){
			selectSql_bar = " select lvl_1_branch_no, lvl_1_branch_name,lvl_2_branch_no, lvl_2_branch_name, " +
					"lvl_3_branch_no, lvl_3_branch_name, lvl_4_branch_no, lvl_4_branch_name, deptlevel, module,brno, " +
					"score,indexname,lv1num,deptno,begindate,enddate, " +
					
					"(case when deptlevel='0' then lvl_1_branch_name when deptlevel='1' then lvl_2_branch_name  when deptlevel='2' then lvl_3_branch_name " +
					" when deptlevel='3' then lvl_4_branch_name else '' end) deptname,(select mark_name||'('||unit||')' from ap_idop.dop_mark_param where mark_code=indexname) mark_name, (value*100) value "+
					"  FROM  ap_idop.DOP_KEY_INDEX_ORG_PERSON dki left join dop_mark_param dmp on dki.indexname=dmp.mark_code where czy ='"+userNo+"'  order by to_number(value) desc" ;
		}else{
			selectSql_bar = " select lvl_1_branch_no, lvl_1_branch_name,lvl_2_branch_no, lvl_2_branch_name, " +
					"lvl_3_branch_no, lvl_3_branch_name, lvl_4_branch_no, lvl_4_branch_name, deptlevel, module,brno, " +
					"score,indexname,lv1num,deptno,begindate,enddate, " +
					
					"(case when deptlevel='0' then lvl_1_branch_name when deptlevel='1' then lvl_2_branch_name  when deptlevel='2' then lvl_3_branch_name " +
					" when deptlevel='3' then lvl_4_branch_name else '' end) deptname,(select mark_name||'('||unit||')' from ap_idop.dop_mark_param where mark_code=indexname) mark_name, value "+
					"  FROM  ap_idop.DOP_KEY_INDEX_ORG_PERSON dki left join dop_mark_param dmp on dki.indexname=dmp.mark_code where czy ='"+userNo+"'  order by to_number(value) desc" ;
		}
		
		
		
		
		// 条线图
		String selectSql_line = "";
				
				if("%".equals(r.getStr("UNIT"))){
					selectSql_line=" select tellno,lvl_1_branch_no, lvl_1_branch_name,lvl_2_branch_no, lvl_2_branch_name, " +
							"lvl_3_branch_no, lvl_3_branch_name, lvl_4_branch_no, lvl_4_branch_name, deptlevel, module, " +
							"indexname,deptno,(mon1_value * 100) mon1_value,(mon2_value * 100) mon2_value,(mon3_value * 100) mon3_value,(mon4_value * 100) mon4_value,(mon5_value * 100) mon5_value,(mon6_value * 100) mon6_value,(mon7_value * 100) mon7_value," +
							"(mon8_value * 100) mon8_value,(mon9_value * 100) mon9_value,(mon10_value * 100) mon10_value,(mon11_value *100) mon11_value ,(mon12_value * 100) mon12_value  FROM  ap_idop.DOP_KEY_INDEX_ORG_12MON_PERSON where czy ='"+userNo+"' order by tellno desc" ;
				}else{
					selectSql_line=" select tellno,lvl_1_branch_no, lvl_1_branch_name,lvl_2_branch_no, lvl_2_branch_name, " +
							"lvl_3_branch_no, lvl_3_branch_name, lvl_4_branch_no, lvl_4_branch_name, deptlevel, module, " +
							"indexname,deptno, mon1_value, mon2_value, mon3_value, mon4_value, mon5_value, mon6_value, mon7_value," +
							" mon8_value, mon9_value, mon10_value, mon11_value , mon12_value  FROM  ap_idop.DOP_KEY_INDEX_ORG_12MON_PERSON where czy ='"+userNo+"' order by tellno desc ";
				}
				

		
		System.out.println(" 5柱形图查询SQL：" + selectSql_bar);
		// 柱形图查询
		List<Record> r_bar=Db.use("gbase").find(selectSql_bar);
		String[]indexname_bar=new String[r_bar.size()];
		StringBuffer s_bar=new StringBuffer();
		List<Record> r_bars=new ArrayList<Record>();
		   //指标值为null时处理
		   for(Record rr:r_bar){
			   if(rr.get("value")!=null){
				   r_bars.add(rr);
			   }
		   }
		   r_bar.clear();
		   r_bar.addAll(r_bars);
		if(r_bar!=null&&r_bar.size()>0){
			for (int i=0;i<r_bar.size();i++){//循环遍历柱形图查询结果
				r_bar.get(i).set("sub_busi_code", sub.get("remark").toString());//在查询结果里插入业务类型值
				String level = "";
				if(!"QXPJ".equals(r_bar.get(i).get("deptno").toString())){
					level = r_bar.get(i).get("deptlevel").toString();
				}
				if(level.equals("0")){
					indexname_bar[i]=r_bar.get(i).get("lvl_1_branch_name").toString();
				}else if(level.equals("1")){
					indexname_bar[i]=r_bar.get(i).get("lvl_2_branch_name").toString();
				}else if(level.equals("2")){
					indexname_bar[i]=r_bar.get(i).get("lvl_3_branch_name").toString();
				}else if(level.equals("3")){
					indexname_bar[i]=r_bar.get(i).get("lvl_4_branch_name").toString();
				}else{
					indexname_bar[i]="辖区平均";
				}
				s_bar.append(r_bar.get(i).get("value")==null?"0":r_bar.get(i).get("value").toString());
				if(i<r_bar.size()-1){
					s_bar.append(",");
				}
			}
		}
		
		System.out.println(" 6条线图查询SQL：" + selectSql_bar);
		// 条线图查询
		List<Record> r_lines=Db.use("gbase").find(selectSql_line);
		Record r_line = null;
		if(r_lines!=null){
			r_line = r_lines.get(0);
		}
		
		Record r_line2 = null;
		if(r_lines.size()>1){
			if(!"".equals(r_lines.get(1).get("tellno")))
			r_line2 = r_lines.get(1);
		}
		
		StringBuffer s_line2=new StringBuffer();
		if(r_line2!=null){
			s_line2.append(r_line2.get("mon7_value")==null?"0":r_line2.get("mon7_value").toString());
			s_line2.append(",");
			
			s_line2.append(r_line2.get("mon8_value")==null?"0":r_line2.get("mon8_value").toString());
			s_line2.append(",");
			
			s_line2.append(r_line2.get("mon9_value")==null?"0":r_line2.get("mon9_value").toString());
			s_line2.append(",");
			
			s_line2.append(r_line2.get("mon10_value")==null?"0":r_line2.get("mon10_value").toString());
			s_line2.append(",");
			
			s_line2.append(r_line2.get("mon11_value")==null?"0":r_line2.get("mon11_value").toString());
			s_line2.append(",");
			
			s_line2.append(r_line2.get("mon12_value")==null?"0":r_line2.get("mon12_value").toString());
		}
		
		String deptname_line="";
		if(r_line!=null){
			String level=r_line.get("deptlevel").toString();
			if(level.equals("0")){
				deptname_line=r_line.get("lvl_1_branch_name").toString();
			}else if(level.equals("1")){
				deptname_line=r_line.get("lvl_2_branch_name").toString();
			}else if(level.equals("2")){
				deptname_line=r_line.get("lvl_3_branch_name").toString();
			}else if(level.equals("3")){
				deptname_line=r_line.get("lvl_4_branch_name").toString();
			}
		}
		
		String[]dateList=getLas6Month();
		
		StringBuffer s_line=new StringBuffer();
		
		s_line.append(r_line.get("mon7_value")==null?"0":r_line.get("mon7_value").toString());
		s_line.append(",");
		
		s_line.append(r_line.get("mon8_value")==null?"0":r_line.get("mon8_value").toString());
		s_line.append(",");
		
		s_line.append(r_line.get("mon9_value")==null?"0":r_line.get("mon9_value").toString());
		s_line.append(",");
		
		s_line.append(r_line.get("mon10_value")==null?"0":r_line.get("mon10_value").toString());
		s_line.append(",");
		
		s_line.append(r_line.get("mon11_value")==null?"0":r_line.get("mon11_value").toString());
		s_line.append(",");
		
		s_line.append(r_line.get("mon12_value")==null?"0":r_line.get("mon12_value").toString());
	   //指标值为null时处理
	   for(Record rr:r_bar){
		   if(rr.get("value")==null){
			   rr.set("value", "--");
		   }
	   }
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", r_bar);
		map.put("val_bar",s_bar.toString());
		map.put("dept_bar", StringUtils.join(indexname_bar,","));
		map.put("date_bar", StringUtils.join(dateList,","));
		map.put("deptname_line", deptname_line);
		map.put("val_line",s_line.toString());
		map.put("val_line2",s_line2.toString());
		map.put("legend",lengend);
		
		
		
		// 打印日志
		System.out.println("getList_bar--r_bar.getList():" + r_bar);
		System.out.println("getList_bar--r_bar.getTotalRow():" + r_bar.size());
		
		//log.info("getList_bar--r_bar.getList():" + r_bar);
		//log.info("getList_bar--r_bar.getTotalRow():" + r_bar.size());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "关键指标数据展现", "3", "关键指标数据展现-查询");
		System.out.println("9关键指标数据展现-查询");
		//log.info("关键指标数据展现-查询");
		// 返回json数据
		renderJson(map);
	}
	
	/**
	 * 获取业务类型下拉列表
	 */
	public void getSubbusicodeList(){
		String val = getPara("val");
		String sql="";
		if(val.equals("1001")){
			sql = " select id,remark,val,upid from dop_gangedmenu_info start with id=(select id from dop_gangedmenu_info where val='1001')  connect by prior id = upid order by collate asc";
		}else{
			sql = "select id,remark,val,upid from dop_gangedmenu_info start with id=(select id from " +
					"dop_gangedmenu_info where val=? and id in(select id from dop_gangedmenu_info start with" +
					" id=(select id from dop_gangedmenu_info where val='1001') connect by prior id=upid)) " +
					"connect by prior id = upid  order by collate asc";
		}
		List<Record> list = val.equals("1001")?Db.find(sql):Db.find(sql ,new Object[]{val});
		setAttr("data",list);
		renderJson();
	}
	
	/**
	 * 获取指标类型下拉列表
	 */
	public void getMarktypecodeList(){
		String val = getPara("val");
		String sql="";
		if(val.equals("1001")){
			sql = " select id,remark,val,upid from dop_gangedmenu_info start with id=(select id from dop_gangedmenu_info where val='1002')  connect by prior id = upid";
		}else{
			sql = "select id,remark,val,upid from dop_gangedmenu_info start with id=(select id from " +
					"dop_gangedmenu_info where val=? and id in(select id from dop_gangedmenu_info start with" +
							" id=(select id from dop_gangedmenu_info where val='1002') connect by prior id=upid)) " +
							"connect by prior id = upid";
		}
		
		List<Record> list = val.equals("1001")?Db.find(sql):Db.find(sql ,new Object[]{val});
		setAttr("data",list);
		renderJson();
	}
	
	/**
	 * 查询关键指标名称下拉列表
	 */
	public void getMarkParamList(){
		
		String sub_busi_code=getPara("sub_busi_code");
		String mark_dimension=getPara("mark_dimension");
		
		if(AppUtils.StringUtil(sub_busi_code)!=null ){
			Record sub=Db.findFirst("select id,type,remark,val from DOP_GANGEDMENU_INFO where id=?",new Object[]{sub_busi_code});//获取业务类型
			sub_busi_code=sub.get("val").toString();
		}
		
		String sql="select mark_code , mark_name from dop_mark_param  ";
		
		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1  ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(sub_busi_code) != null) {
			whereSql.append(" AND  sub_busi_code = ? ");
			sqlStr.add(sub_busi_code);
		}
		if(AppUtils.StringUtil(mark_dimension) != null){
			whereSql.append(" AND  mark_dimension = ? ");
			sqlStr.add(mark_dimension);
		}
		
		whereSql.append("  and is_key_mark = ? ");
		sqlStr.add("1");
		
		whereSql.append("  and total_type is not null  ");
		whereSql.append("  order by mark_code ");
		sql+=whereSql.toString();

		List<Record>list=Db.find(sql,sqlStr.toArray());
		setAttr("data",list);
		renderJson();
	}
	
	
	//获取最近12个月的年月
	public String[]getLas6Month(){
		String[]str=new String[6];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		for(int i=6;i>0;i--){
			Date date = new Date();
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, i*-1);
			str[6-i]=sdf.format(cal.getTime());
			
		}
		return str;
	}
	

	/**
	 * 导出Excel表格
	 */
	@SuppressWarnings("unchecked")
//	@Before(PermissionInterceptor.class)
	public void download(){
		
		// 获取查询条件
		String orgid = getPara("orgid"); //机构
		String busi_module = getPara("busi_module"); //业务模块
		String sub_busi_code = getPara("sub_busi_code"); //业务类型
		String mark_code = getPara("mark_code"); // 指标名称
		String startime = getPara("startime"); // 开始时间
		String endtime = getPara("endtime"); // 结束时间
		
		String flag="0";
		/******关键指标数据导出下一级机构数据 *******/
		log.info("关键指标数据导出下一级机构数据存储过程 ap_idop.DPRO_KEY_INDEX_ORG_PERSON_EXP 调用开始:"
				+ DateTimeUtil.getTime());
		String userNo = getCurrentUser().getStr("USER_NO");
		
		String validSql_bar = " CALL ap_idop.DPRO_KEY_INDEX_QRY_PERSON_EXP('"
				+ orgid + "','" + startime + "','" + endtime +"','" +busi_module+ "','"+mark_code+"','"+userNo+"',@a,@b)";
		Db.use("gbase").update(validSql_bar);
		System.out.println("" + validSql_bar);
		log.info("关键指标数据导出下一级机构数据存储过程 ap_idop.DPRO_KEY_INDEX_QRY_PERSON_EXP 调用结束:"
				+ DateTimeUtil.getTime()); 
		exportExcel(sub_busi_code);
		renderNull();
	}
	
	
	/**
	 * 导出网点Excel表格
	 */
	@SuppressWarnings("unchecked")
	@Before(PermissionInterceptor.class)
	public void downloadWd(){
		
		// 获取查询条件
		String orgid = getPara("orgid"); //机构
		String busi_module = getPara("busi_module"); //业务模块
		String sub_busi_code = getPara("sub_busi_code"); //业务类型
		String mark_code = getPara("mark_code"); // 指标名称
		String startime = getPara("startime"); // 开始时间
		String endtime = getPara("endtime"); // 结束时间
		
		String flag="1";
		/******关键指标数据导出网点机构数据 *******/
		log.info("关键指标数据导出网点机构数据存储过程 ap_idop.DPRO_KEY_INDEX_QRY 调用开始:"
				+ DateTimeUtil.getTime());
		String userNo = getCurrentUser().getStr("USER_NO");
		String validSql_bar = " CALL ap_idop.DPRO_KEY_INDEX_QRY('"
				+ orgid + "','" + startime + "','" + endtime +"','" +busi_module+ "','"+mark_code+"','"+flag+"','"+userNo+"',@a,@b)";
		Db.use("gbase").update(validSql_bar);
		System.out.println("指标值计算SQL：" + validSql_bar);
		log.info("关键指标数据导出网点机构数据存储过程 ap_idop.DPRO_KEY_INDEX_QRY 调用结束:"
				+ DateTimeUtil.getTime()); 
		exportExcel(sub_busi_code);
		renderNull();
	}
	
	
	public void exportExcel(String sub_busi_code){
		String userNo = getCurrentUser().getStr("USER_NO");
		String selectSql = " select lvl_1_branch_no, lvl_1_branch_name,lvl_2_branch_no, lvl_2_branch_name, " +
				"lvl_3_branch_no, lvl_3_branch_name, lvl_4_branch_no, lvl_4_branch_name, deptlevel, module,brno, " +
//				"score,value,indexname,lv1num,deptno,begindate,enddate, " +
				"score,value,indexname,lv1num,deptno,begindate,enddate, substr(teller_no,14) teller_no, " +
//				"(case when deptlevel='0' then lvl_1_branch_name when deptlevel='1' then lvl_2_branch_name  when deptlevel='2' then lvl_3_branch_name " +
//				" when deptlevel='3' then lvl_4_branch_name else '' end) deptname,(select mark_name from ap_idop.dop_mark_param where mark_code=indexname) mark_name "+
				" (select orgname from ap_idop.sys_org_info where deptno=orgnum) deptname,(select mark_name from ap_idop.dop_mark_param where mark_code=indexname) mark_name "+
				"  FROM  ap_idop.DOP_KEY_INDEX_ORG_PERSON where czy=? order by deptno ";
		
		// 查询
		List<Record> list = Db.use("gbase").find(selectSql,userNo);
		
		Record sub=Db.findFirst("select id,type,remark,val from DOP_GANGEDMENU_INFO where id=?",new Object[]{sub_busi_code});//获取业务类型详细信息
		String queryStr = "select name from sys_user_info t where t.id = ?";
		String queryStr1 = "select bancsid from sys_org_info t where t.id = ?";

		for (Record record : list) {
			record.set("sub_busi_code", sub.get("remark").toString());//在查询结果里插入业务类型值
			record.set("busi_module",ParamContainer.getDictName("dop_ywtype", record.getStr("module")));
			record.set("create_time", record.getStr("begindate")+"至"+record.getStr("enddate"));
			Record temp =null;
			record.set("teller_name", ((temp = Db.findFirst(queryStr,  record.getStr("teller_no"))) != null) ? temp.getStr("name") : record.get("teller_no"));
			record.set("deptno", ((temp = Db.findFirst(queryStr1,  record.getStr("deptno"))) != null) ? temp.getStr("bancsid") : record.get("deptno"));

		}
		// 指定用哪些查询出来的字段填充excel文件
//		String[] headers = { "create_time","brno","deptname","teller_no", "busi_module","sub_busi_code","mark_name","value"};
		String[] headers = { "create_time","deptno","deptname","teller_no","teller_name", "busi_module","sub_busi_code","mark_name","value"};
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "时间", "机构号","机构","EHR号","人员", "业务模块","业务类型","指标名称","指标值" };
		
	
		String fileName = "";
		try {
			fileName = new String("人员明细.xls".getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, list, getResponse());
		er.render();
		// 打印日志
		log.info("download--list:" + list);
	}
	
	public void getZbFirstDetail(){
		String deptno = getPara("deptno");
		String indexname = getPara("indexname");
		// 获取查询条件
				int pageNum = getParaToInt("pageIndex")+ 1;
				int pageSize = getParaToInt("pageSize",10);
		String selectSql = "select t.indicator_value,substr(t.create_time,0,8) create_time,t.lvl_4_branch_name,t.indicator_code   " ;
		String sqlExceptSelect = 	" from idop_indicators_dtl_lvl1 t where 1=1  and (id = ? or t.lvl_2_branch_no =? or t.lvl_3_branch_no=? or t.lvl_4_branch_no=?) and t.indicator_code in " +
				"(select first_detail_code from dop_mark_param t where t.mark_code = ?)";
		// 查询
		Page<Record> page =  Db.use("gbase").paginate(pageNum, pageSize, selectSql,sqlExceptSelect,deptno,deptno,deptno,deptno,indexname);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	

}
