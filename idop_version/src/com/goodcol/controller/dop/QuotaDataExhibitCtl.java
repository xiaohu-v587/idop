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
import com.goodcol.controller.ParamCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ParamContainer;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.ext.render.excel.ExcelRender;

/*
 * 关键指标数据展现
 * @author 陆磊磊
 * @date 2018-11-19
 */
@RouteBind(path = "/quotaDataExhibit")
@Before({ManagerPowerInterceptor.class})
public class QuotaDataExhibitCtl extends BaseCtl{
	
	// 记录日志用
	public static Logger log = Logger.getLogger(QuotaDataExhibitCtl.class); 

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
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		String flag = "0"; // 结束时间
		
		
		
		Record sub=Db.findFirst("select id,type,remark,val from DOP_GANGEDMENU_INFO where id=?",new Object[]{sub_busi_code});//获取业务类型详细信息
		
		
		/******关键指标数据展现柱形图 *******/
		
		
		//log.info("关键指标数据展现柱形图存储过程 ap_idop.DPRO_KEY_INDEX_QRY 调用开始:"+ DateTimeUtil.getTime());
		System.out.println("0关键指标数据展现柱形图存储过程 ap_idop.DPRO_KEY_INDEX_QRY 调用开始:"+ DateTimeUtil.getTime());
		String validSql_bar = " CALL ap_idop.DPRO_KEY_INDEX_QRY('"+ orgid + "','" + startime + "','" + endtime +"','" +busi_module+ "','"+mark_code+"','"+flag+"','"+userNo+"',@a,@b)";
		Db.use("gbase").update(validSql_bar);
		//System.out.println("指标值计算SQL：" + validSql_bar);
		//log.info("关键指标数据展现柱形图存储过程 ap_idop.DPRO_KEY_INDEX_QRY 调用结束:"+ DateTimeUtil.getTime()); 
		System.out.println("1关键指标数据展现柱形图存储过程 ap_idop.DPRO_KEY_INDEX_QRY 调用结束:"+ DateTimeUtil.getTime());
		
		
		/******关键指标数据展现条线图 *******/
		System.out.println("2关键指标数据展现条线图存储过程 ap_idop.DPRO_KEY_INDEX_QRY_12MON 调用开始:"+ DateTimeUtil.getTime());
		//log.info("关键指标数据展现条线图存储过程 ap_idop.DPRO_KEY_INDEX_QRY_12MON 调用开始:"+ DateTimeUtil.getTime());
		
		String validSql_line = " CALL ap_idop.DPRO_KEY_INDEX_QRY_12MON('"+ orgid + "','"+busi_module+ "','"+mark_code+"','"+userNo+"',@a,@b)";
		Db.use("gbase").update(validSql_line);
		System.out.println("3指标值计算SQL：" + validSql_line);
		System.out.println("4关键指标数据展现条线图存储过程 ap_idop.DPRO_KEY_INDEX_QRY_12MON 调用结束:"+ DateTimeUtil.getTime());
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
					"  FROM  ap_idop.DOP_KEY_INDEX_ORG dki left join dop_mark_param dmp on dki.indexname=dmp.mark_code where czy ='"+userNo+"'  order by to_number(value) desc" ;
		}else{
			selectSql_bar = " select lvl_1_branch_no, lvl_1_branch_name,lvl_2_branch_no, lvl_2_branch_name, " +
					"lvl_3_branch_no, lvl_3_branch_name, lvl_4_branch_no, lvl_4_branch_name, deptlevel, module,brno, " +
					"score,indexname,lv1num,deptno,begindate,enddate, " +
					
					"(case when deptlevel='0' then lvl_1_branch_name when deptlevel='1' then lvl_2_branch_name  when deptlevel='2' then lvl_3_branch_name " +
					" when deptlevel='3' then lvl_4_branch_name else '' end) deptname,(select mark_name||'('||unit||')' from ap_idop.dop_mark_param where mark_code=indexname) mark_name, value "+
					"  FROM  ap_idop.DOP_KEY_INDEX_ORG dki left join dop_mark_param dmp on dki.indexname=dmp.mark_code where czy ='"+userNo+"'  order by to_number(value) desc" ;
		}
		
		
		
		
		// 条线图
		String selectSql_line = "";
				
				if("%".equals(r.getStr("UNIT"))){
					selectSql_line=" select lvl_1_branch_no, lvl_1_branch_name,lvl_2_branch_no, lvl_2_branch_name, " +
							"lvl_3_branch_no, lvl_3_branch_name, lvl_4_branch_no, lvl_4_branch_name, deptlevel, module, " +
							"indexname,deptno,(mon1_value * 100) mon1_value,(mon2_value * 100) mon2_value,(mon3_value * 100) mon3_value,(mon4_value * 100) mon4_value,(mon5_value * 100) mon5_value,(mon6_value * 100) mon6_value,(mon7_value * 100) mon7_value," +
							"(mon8_value * 100) mon8_value,(mon9_value * 100) mon9_value,(mon10_value * 100) mon10_value,(mon11_value *100) mon11_value ,(mon12_value * 100) mon12_value  FROM  ap_idop.DOP_KEY_INDEX_ORG_12MON where czy ='"+userNo+"'";
				}else{
					selectSql_line=" select lvl_1_branch_no, lvl_1_branch_name,lvl_2_branch_no, lvl_2_branch_name, " +
							"lvl_3_branch_no, lvl_3_branch_name, lvl_4_branch_no, lvl_4_branch_name, deptlevel, module, " +
							"indexname,deptno, mon1_value, mon2_value, mon3_value, mon4_value, mon5_value, mon6_value, mon7_value," +
							" mon8_value, mon9_value, mon10_value, mon11_value , mon12_value  FROM  ap_idop.DOP_KEY_INDEX_ORG_12MON where czy ='"+userNo+"'";
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
				String level=r_bar.get(i).get("deptlevel").toString();
				if(level.equals("0")){
					indexname_bar[i]=r_bar.get(i).get("lvl_1_branch_name").toString();
				}else if(level.equals("1")){
					indexname_bar[i]=r_bar.get(i).get("lvl_2_branch_name").toString();
				}else if(level.equals("2")){
					indexname_bar[i]=r_bar.get(i).get("lvl_3_branch_name").toString();
				}else if(level.equals("3")){
					indexname_bar[i]=r_bar.get(i).get("lvl_4_branch_name").toString();
				}
				s_bar.append(r_bar.get(i).get("value")==null?"0":r_bar.get(i).get("value").toString());
				if(i<r_bar.size()-1){
					s_bar.append(",");
				}
			}
		}
		
		System.out.println(" 6条线图查询SQL：" + selectSql_bar);
		// 条线图查询
		Record r_line=Db.use("gbase").findFirst(selectSql_line);
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
	@Before(PermissionInterceptor.class)
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
		log.info("关键指标数据导出下一级机构数据存储过程 ap_idop.DPRO_KEY_INDEX_QRY 调用开始:"
				+ DateTimeUtil.getTime());
		String userNo = getCurrentUser().getStr("USER_NO");
		
		String validSql_bar = " CALL ap_idop.DPRO_KEY_INDEX_QRY('"
				+ orgid + "','" + startime + "','" + endtime +"','" +busi_module+ "','"+mark_code+"','"+flag+"','"+userNo+"',@a,@b)";
		Db.use("gbase").update(validSql_bar);
		System.out.println("指标值计算SQL：" + validSql_bar);
		log.info("关键指标数据导出下一级机构数据存储过程 ap_idop.DPRO_KEY_INDEX_QRY 调用结束:"
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
				"score,value,indexname,lv1num,deptno,begindate,enddate, " +
				
				"(case when deptlevel='0' then lvl_1_branch_name when deptlevel='1' then lvl_2_branch_name  when deptlevel='2' then lvl_3_branch_name " +
				" when deptlevel='3' then lvl_4_branch_name else '' end) deptname,(select mark_name from ap_idop.dop_mark_param where mark_code=indexname) mark_name "+
				"  FROM  ap_idop.DOP_KEY_INDEX_ORG where czy=? order by deptno ";
		
		// 查询
		List<Record> list = Db.use("gbase").find(selectSql,userNo);
		
		Record sub=Db.findFirst("select id,type,remark,val from DOP_GANGEDMENU_INFO where id=?",new Object[]{sub_busi_code});//获取业务类型详细信息
		
		for (Record record : list) {
			record.set("sub_busi_code", sub.get("remark").toString());//在查询结果里插入业务类型值
			record.set("busi_module",ParamContainer.getDictName("dop_ywtype", record.getStr("module")));
			record.set("create_time", record.getStr("begindate")+"至"+record.getStr("enddate"));
		}
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = { "create_time","brno","deptname", "busi_module","sub_busi_code","mark_name","value"};

		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = { "时间", "机构号","机构", "业务模块","业务类型","指标名称","指标值" };
		
	
		String fileName = "";
		try {
			fileName = new String("关键指标数据展现.xls".getBytes("GB2312"), "ISO_8859_1");
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
		String starttime =  getPara("starttime");
		if(StringUtils.isNotEmpty(starttime)&&starttime.length()>10){
			starttime = starttime.substring(0,10).replace("-", "");
		}		
		String endtime =  getPara("endtime");
		if(StringUtils.isNotEmpty(endtime)&&endtime.length()>10){
			endtime = endtime.substring(0,10).replace("-", "");
		}
		String teller = getPara("teller");
		// 获取查询条件
		int pageNum = getParaToInt("pageIndex")+ 1;
		int pageSize = getParaToInt("pageSize",10);
		if(pageSize==999999){
			pageNum=1;
		}
		String code = getPara("mark_dimension");
		if(StringUtils.isEmpty(code)){
			code = getPara("code");
		}
		
		queryField();
		List<Record> rlist =getAttr("rlist");
		String fieldstr = "";
		for(int i=0;i<rlist.size();i++){
			fieldstr+=rlist.get(i).getStr("column_name")+",";
		}
		String selectSql = "select t.indicator_value,data_date as create_time,"+fieldstr+" t.lvl_4_branch_name,t.indicator_code   " ;
		String sqlExceptSelect = 	" from idop_indicators_dtl_lvl1 t where 1=1  and t.indicator_code = ?" +
				" and ?>=data_date and ?<=data_date";
		Page<Record> page = null;
		if(StringUtils.isNotEmpty(teller)){
			sqlExceptSelect += " and substr(teller_no,14) = ? ";
			if("000000000".equals(deptno)){ 
				page =  Db.use("gbase").paginate(pageNum, pageSize, selectSql,sqlExceptSelect,code,endtime,starttime,teller);
			}else{
				sqlExceptSelect +=" and (id = ? or t.lvl_2_branch_no =? or t.lvl_3_branch_no=? or t.lvl_4_branch_no=?) ";
				page =  Db.use("gbase").paginate(pageNum, pageSize, selectSql,sqlExceptSelect,code,endtime,starttime,teller,deptno,deptno,deptno,deptno);
			}
		}else{
			if("000000000".equals(deptno)){ 
				page =  Db.use("gbase").paginate(pageNum, pageSize, selectSql,sqlExceptSelect,code,endtime,starttime);
			}else{
				sqlExceptSelect +=" and (id = ? or t.lvl_2_branch_no =? or t.lvl_3_branch_no=? or t.lvl_4_branch_no=?) ";
				page =  Db.use("gbase").paginate(pageNum, pageSize, selectSql,sqlExceptSelect,code,endtime,starttime,deptno,deptno,deptno,deptno);
			}
		}
		
		// 查询

		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	public void getZbFirstDetailList(){
		String indexname = getPara("indexname");
		// 获取查询条件
		String qurSql =  "select first_detail_code from dop_mark_param t where t.mark_code = "+"'"+indexname+"'";
		String firstDetailCode = Db.findFirst(qurSql).get("first_detail_code");
		String code = "";
			String[] str = firstDetailCode.split(",");
			for(int i = 0;i<str.length; i++){
				code = code+"'"+str[i]+"',";
			}
			code = code.substring(0, code.length()-1);
		String querySql = "select * from sys_param_info t where t.key = 'first_detail' and val in "+"("+code+")" ;	
		List<Record> rlist = Db.find(querySql);
		//setAttr("data", rlist);
		renderJson(rlist);
	}
	public void getZbDetailList(){
		String indexname = getPara("indexname");
		// 获取查询条件
		String qurSql =  "select first_detail_code from dop_mark_param t where t.mark_code = "+"'"+indexname+"'";
		String firstDetailCode = Db.findFirst(qurSql).getStr("first_detail_code");
		Map<String,String> map = new HashMap<String,String>();
		map.put("firstDetailCode", firstDetailCode);
		setAttr("firstDetailCode", firstDetailCode);
		renderJson(map);
	}
	
	public void queryField(){
		String mark_dimension = getPara("mark_dimension");
		if(StringUtils.isEmpty(mark_dimension)){
			mark_dimension = getPara("code");
		}
		// 获取查询条件
		String qurSql =  "select column_name,column_desc from idop_param_dtl_lvl1_list t where t.indicator_code = "+"'"+mark_dimension+"'";
		List<Record> rlist= Db.use("gbase").find(qurSql);
		setAttr("rlist", rlist);
		renderJson(rlist);
	}
	public void downloadDetails(){
		getZbFirstDetail();
		List<Record> list = getAttr("data");
		
		
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = {};//getPara("execlheaders").split(",");
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = {};//getPara("execlcolumns").split(",");;
		headers = getPara("execlcolumns").split(",");
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		columns = getPara("execlheaders").split(",");;
		String fileName = "";
		try {
			fileName = new String((System.currentTimeMillis()+".xls").getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, list, getResponse());
		er.render();
		renderNull();
		// 打印日志
	}
	
	public void downloadDetailsForTeller(){
		getZbFirstDetail();
		List<Record> list = getAttr("data");
		
		queryField();
		List<Record> rlist =getAttr("rlist");
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = new String[rlist.size()+4];//{"create_time","indicator_code","lvl_4_branch_name","indicator_value"};//getPara("execlheaders").split(",");
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = new String[rlist.size()+4];//{"预警时间","一级明细名称","机构名称","值"};//getPara("execlcolumns").split(",");;
		headers[0]="create_time";
		columns[0]="预警时间";
		headers[1]="indicator_code";
		columns[1]="一级明细名称";
		headers[2]="lvl_4_branch_name";
		columns[2]="机构名称";
		headers[3]="indicator_value";
		columns[3]="值";
		for(int i=0;i<rlist.size();i++){
			columns[i+4]=rlist.get(i).getStr("column_desc");
			headers[i+4]=rlist.get(i).getStr("column_name");

		}	
		String fileName = "";
		try {
			fileName = new String((System.currentTimeMillis()+".xls").getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, list, getResponse());
		er.render();
		renderNull();
		// 打印日志
	}
	
	
	public void downloadFirst(){
		getZbDetailList();
		String codes =getAttr("firstDetailCode");
		String code = codes.split(",")[0];		
		List<Record> rlist = queryFields(code);
		getZbFirstAll(code);
		List<Record> list = getAttr("data");
		
		// 指定用哪些查询出来的字段填充excel文件
		String[] headers = new String[rlist.size()+4];//{"create_time","indicator_code","lvl_4_branch_name","indicator_value"};//getPara("execlheaders").split(",");
		// excel文件的每列的名称,顺序与填充字段的顺序保持一致
		String[] columns = new String[rlist.size()+4];//{"预警时间","一级明细名称","机构名称","值"};//getPara("execlcolumns").split(",");;
		headers[0]="create_time";
		columns[0]="预警时间";
		headers[1]="indicator_code";
		columns[1]="一级明细名称";
		headers[2]="lvl_4_branch_name";
		columns[2]="机构名称";
		headers[3]="indicator_value";
		columns[3]="值";
		for(int i=0;i<rlist.size();i++){
			columns[i+4]=rlist.get(i).getStr("column_desc");
			headers[i+4]=rlist.get(i).getStr("column_name");

		}		
		String fileName = "";
		try {
			fileName = new String((System.currentTimeMillis()+".xls").getBytes("GB2312"), "ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
			log.error("userctl-download", e);
		}
		// 转换成excel
		ExcelRender er = new ExcelRender(fileName, columns, headers, list, getResponse());
		er.render();
		renderNull();
		// 打印日志
	}
	
	public List queryFields(String code){
		// 获取查询条件
		String qurSql =  "select column_name,column_desc from idop_param_dtl_lvl1_list t where t.indicator_code = "+"'"+code+"'";
		List<Record> rlist= Db.use("gbase").find(qurSql);
		return rlist;
	}
	public void getZbFirstAll(String code){
		String deptno = getPara("deptno");
		String indexname = getPara("indexname");
		String starttime =  getPara("starttime");
		//starttime = starttime.substring(0,10).replace("-", "");
		String endtime =  getPara("endtime");
		//endtime = endtime.substring(0,10).replace("-", "");
		//String teller = getPara("teller");
		// 获取查询条件
		int pageNum =  1;
		int pageSize = 999999;
		
		//String code = getPara("mark_dimension");
		
		List<Record> rlist =queryFields(code);
		String fieldstr = "";
		for(int i=0;i<rlist.size();i++){
			fieldstr+=rlist.get(i).getStr("column_name")+",";
		}
		
		String sql = "select first_detail_code from dop_mark_param t where t.mark_code = ?";
		String firstDetailCode = Db.findFirst(sql,indexname).get("first_detail_code");
		String ss = "";
		String[] str = firstDetailCode.split(",");
		for(int i = 0;i<str.length; i++){
			ss = ss+"'"+str[i]+"',";
		}
		ss = ss.substring(0, ss.length()-1);
		String selectSql = "select t.indicator_value,'"+starttime+"至"+endtime+"' as create_time,"+fieldstr+" t.lvl_4_branch_name,t.indicator_code   " ;
		String sqlExceptSelect = 	" from idop_indicators_dtl_lvl1 t where 1=1  and t.indicator_code in (" +ss+
				") and ?>=data_date and ?<=data_date";
		Page<Record> page = null;
		
		if("000000000".equals(deptno)){ 
			page =  Db.use("gbase").paginate(pageNum, pageSize, selectSql,sqlExceptSelect,endtime,starttime);
		}else{
			sqlExceptSelect +=" and (id = ? or t.lvl_2_branch_no =? or t.lvl_3_branch_no=? or t.lvl_4_branch_no=?) ";
			page =  Db.use("gbase").paginate(pageNum, pageSize, selectSql,sqlExceptSelect,endtime,starttime,deptno,deptno,deptno,deptno);
		}
		
		// 查询

		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	
	public void checkDetail() {
		//获取前台界面的柜员名称
		String orgid = getPara("orgid"); //机构
		setAttr("orgid", orgid);
		render("checkDetail.jsp");
	}
}
