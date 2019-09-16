//预警认定
package com.goodcol.controller.dop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.goodcol.util.ParamContainer;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;

@RouteBind(path="/warning")
@Before({ ManagerPowerInterceptor.class })
public class WarningCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(WarningCtl.class);

	@Override
	@Before(PermissionInterceptor.class)
	public void index() {
		// TODO Auto-generated method stub
		render("index.jsp");
	}
	
	//首页跳转到待认定列表界面
	@Before(PermissionInterceptor.class)
	public void toindex() {
		String 	qryflag = getPara("qryflag");
//		String  orgid = getCurrentUser().getStr("ORG_ID");
//		setAttr("orgid",orgid);
		String model = getPara("model");
		setAttr("model", model);
		render("index.jsp");
	}
	
	public void singleConfirm(){
		setAttr("flownum", getPara("flownum"));
		render("singleConfirm.jsp");
	}
	public void multiConfirm(){
		render("multiConfirm.jsp");
	}
	public void multiDisConfirm(){
		render("multiDisConfirm.jsp");
	}
	
	public void getMoreWarn(){
		render("getMoreWarn.jsp");
	}
	
	public void getList(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		// 获取查询参数
		Map<String, Object> map = organSql(0);
		@SuppressWarnings("unchecked")
		Page<Record> page = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
		for (Record record : page.getList()) {
			//判断是否超出预设时间
			Date createTime = record.get("create_time");
			String days = Db.findFirst("select * from sys_param_info where key=?","dop_apprvl_day").getStr("val");
			long seconds = new Date().getTime()-createTime.getTime();
			if(seconds > Integer.parseInt(days)*24*60*60*1000){
				record.set("out_date", "1");
			}
			
		}
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		// 打印日志
		log.info("getList--未认定预警:" + page);
		renderJson();
	}
	public void getHaveList(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		// 获取查询参数
		Map<String, Object> map = organSql(1);
		@SuppressWarnings("unchecked")
		Page<Record> page = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		// 打印日志
		log.info("getList--已认定预警:" + page);
		renderJson();
	}
	/**
	 * 获取单笔认定数据详情
	 */
	public void getDetail(){
		String flownum = getPara("flownum");
		String selectSql = "select p.warning_code,p.busi_module,p.warning_name,p.warning_type_code,i.flownum," +
				"p.warning_level,i.warning_status,to_char(to_date(substr(i.create_time,0,8),'yyyy-MM-dd'),'yyyy-MM-dd') as create_time,i.data_date," +
				"i.indentify_status,i.checker_level,i.deptno orgid,i.last_check_stat,i.last_approval_stat," +
				"i.checker_remark,i.approver_remark ,i.indentify_remark,i.is_question  from dop_warning_info i left join dop_warning_param" +
				" p on p.warning_code=i.warning_code where i.flownum=? and  (p.is_use is  null or p.is_use = '1')  ";
		Record data = Db.findFirst(selectSql,flownum);
		String sql = "select id from dop_gangedmenu_info where id in(select id from" +
				" dop_gangedmenu_info start with id=(select id from dop_gangedmenu_info where val='1001') " +
				"connect by prior id=upid) and val=?";
		if(AppUtils.StringUtil(data.getStr("warning_type_code"))!=null){
			String warningTypeId = Db.findFirst(sql.replace("1001", "1003"),
					data.get("warning_type_code")).getStr("id");
			log.info("调用gbase返回单笔数据成功");
			System.out.println("调用gbase返回单笔数据成功");
			data.set("warning_type_code",warningTypeId);
		}
		if(AppUtils.StringUtil(data.getStr("indentify_status"))!=null){
			if(data.getStr("indentify_status").equals("1") || data.getStr("indentify_status").equals("2"))
			data.set("signal_status",data.getStr("indentify_status"));
		}
		if(AppUtils.StringUtil(data.getStr("checker_level"))==null){
			String cheker_level = Db.findFirst("select check_level from dop_warning_param where warning_code=? and  (is_use is  null or is_use = '1') ",data.getStr("warning_code")).getStr("check_level");
			data.set("checker_level",cheker_level);
		}
		setAttr("record",data);
		log.info("获取单笔明细成功");
		System.out.println("获取单笔明细成功");
		renderJson();
	}
	
	/**
	 * 预警查询-预警详情
	 */
	public void getDetail2(){
		String flownum = getPara("flownum");
		String selectSql = 
			"select p.warning_code,                                           "+
			"      (select remark                                             "+
			"         from sys_param_info                                     "+
			"        where key = 'dop_ywtype'                                 "+
			"          and val = p.busi_module) busi_module,                  "+
			"      p.warning_name,                                            "+
			"      (select remark                                             "+
			"         from dop_gangedmenu_info                                "+
			"        where val = p.warning_type_code                          "+
			"          and rownum = 1) warning_type_code,                     "+
			"      i.flownum,                                                 "+
			"      (select remark                                             "+
			"         from sys_param_info                                     "+
			"        where key = 'dop_warning_lvl'                            "+
			"          and val = p.warning_level) warning_level,              "+
			"      (select remark                                             "+
			"         from sys_param_info                                     "+
			"        where key = 'dop_warning_sta'                            "+
			"          and val = i.warning_status) warning_status,            "+
			"      to_char(to_date(substr(i.create_time, 0, 8), 'yyyy-MM-dd'),"+
			"              'yyyy-MM-dd') as create_time,                      "+
			"      i.data_date,                                               "+
			"      i.indentify_status,                                        "+
			"      i.checker_level,                                           "+
			"      i.deptno orgid,                                            "+
			"      (select remark                                             "+
			"         from sys_param_info                                     "+
			"        where key = 'dop_check_stat'                             "+
			"          and val = i.last_check_stat) last_check_stat,          "+
			"      (select remark                                             "+
			"         from sys_param_info                                     "+
			"        where key = 'dop_approv_stat'                            "+
			"          and val = i.last_approval_stat) last_approval_stat,    "+
			"      i.checker_remark,                                          "+
			"      i.approver_remark,                                         "+
			"      i.indentify_remark,                                        "+
			"      (select remark                                             "+
			"         from sys_param_info                                     "+
			"        where key = 'dop_is_question'                            "+
			"          and val = i.is_question) is_question                   "+
			" from dop_warning_info i                                         "+
			" left join dop_warning_param p                                   "+
			"   on p.warning_code = i.warning_code                            "+
			"where i.flownum = ?                                              "+
			"  and (p.is_use is null or p.is_use = '1')                       ";
		Record data = Db.findFirst(selectSql,flownum);
		setAttr("record",data);
		renderJson();
	}
	
	
	/**
	 * 获取业务类型下拉框数据
	 */
	public void getywTypeList(){
		String val = getPara("val");
		String sql = "select id,remark,upid,val from dop_gangedmenu_info start with id=(select id from " +
				"dop_gangedmenu_info where val='"+val+"' and id in(select id from dop_gangedmenu_info start with" +
						" id=(select id from dop_gangedmenu_info where val='1001') connect by prior id=upid)) " +
						"connect by prior id = upid";
		List<Record> list = Db.find(sql);
		setAttr("data", list);
		renderJson();
	}
	
	/**
	 * 获取预警类型下拉列表
	 */
	public void getWarningTypeList(){
		String val = getPara("val","1003");
		String valType = getPara("valType");
		if(AppUtils.StringUtil(valType)!= null){
			val = valType;
		}
		if(val.equals("1001")){
			val="1003";
		}
		String sql = "select id,remark,val,upid from dop_gangedmenu_info start with id=(select id from " +
				"dop_gangedmenu_info where val='"+val+"' and id in(select id from dop_gangedmenu_info start with" +
						" id=(select id from dop_gangedmenu_info where val='1003') connect by prior id=upid)) " +
						"connect by prior id = upid";
		List<Record> list = Db.find(sql);
		setAttr("data",list);
		renderJson();
	}
	/**
	 * 根据查询条件搜索
	 * @return
	 */
	public Map<String, Object> organSql(int flag) {
		// 获取页面输入查询条件
		String ywType = getPara("ywtype"); 
		String startTime = getPara("start_time");
		String endTime = getPara("end_time");
		String startTime1 = getPara("biz_start_time");
		String endTime1 = getPara("biz_end_time");
		String signal_stat = getPara("signal_stat");
		String warningType = getPara("warning_type");
		String warnname = getPara("warn_name");
		String org = getPara("orgid");
		Record user = getCurrentUser();
		//和数据权限有关的  加机构条件 
		//用户角色
//		String roleNames = AppUtils.getRoleNames(user.getStr("ID"));
//		//获取当前机构号
//		String orgId = user.getStr("ORG_ID");
//		//获取当前用户最高等级权限
//		String roleLevel = AppUtils.getLevByRole(roleNames);
//		//最大权限的机构号	
//		String orgNum = AppUtils.getUpOrg(orgId, roleLevel);
		//id代表9位机构号
		//sb.append(" and id in (select id from sys_org_info where id = '" + orgNum + "' or by5 like '%" + orgNum + "%')");
		String selectSql = " select p.warning_code,p.busi_module,p.warning_name,g.remark warning_type_code,p.warning_level,i.warning_status," +
				"to_date(i.create_time,'yyyy-MM-dd HH24:mi:ss') as create_time,to_date(i.data_date,'yyyy-MM-dd HH24:mi:ss') as biz_time,i.indentify_status,i.flownum,case i.deptlevel when '0' then '江苏省分行' when '1' then i.lvl_2_branch_name when '2' then i.lvl_3_branch_name" +
				" when '3' then i.lvl_4_branch_name end orgname";
		//预警类型id
		String warningTypeIds = Db.findFirst("select listagg(id,''',''') within group (order by id) ids from dop_gangedmenu_info start with val='1003' connect by prior id =upid ").getStr("ids");
		String extrasql = " from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code " +
				" left join (select * from dop_gangedmenu_info k where k.id in('"+warningTypeIds+"')) g on g.val=p.warning_type_code ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 and  (p.is_use is  null or p.is_use = '1') and p.warning_dimension !='2' and i.indentify_status is not null ");
		List<String> sqlStr = new ArrayList<String>();
		if(flag==0){
			whereSql.append(" and i.indentify_status=0 ");
		}
		if(flag==1){
			whereSql.append(" and (i.indentify_status = 1 or i.indentify_status=2) ");//1认定通过，2认定不通过
		}
		if (AppUtils.StringUtil(org) != null) {			//页面上选的机构
			//whereSql.append(" and i.deptno in (select id from sys_org_info start with orgnum = '"+org+"' connect by prior orgnum=upid) ");
			whereSql.append(" and i.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
			 sqlStr.add(org.trim());
			 sqlStr.add("%" + org.trim()+ "%");
		}
		else
		{
			////如果查询条件没有指定机构，这里安装用户的数据范围、角色级别 来查， 数据范围优先
			String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");		//数据范围
			whereSql.append(" and i.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
			sqlStr.add(sjfw.trim());
			sqlStr.add("%" + sjfw.trim()+ "%");
		}
		
		
		if (AppUtils.StringUtil(ywType) != null && !"1001".equals(ywType)) {
			whereSql.append(" and p.busi_module = ? ");
			sqlStr.add(ywType.trim());
		}
		if (AppUtils.StringUtil(startTime) != null && AppUtils.StringUtil(endTime) != null) {
			endTime=endTime.substring(0,10)+" 23:59:59";
			whereSql.append(" and to_date(i.create_time,'yyyy-MM-dd HH24:mi:ss') between to_date(?,'yyyy-MM-dd HH24:mi:ss') and " +
					"to_date(?,'yyyy-MM-dd HH24:mi:ss') ");
			sqlStr.add(startTime.trim());
			sqlStr.add(endTime.trim());
		}
		if (AppUtils.StringUtil(startTime1) != null && AppUtils.StringUtil(endTime1) != null) {
			endTime1=endTime1.substring(0,10)+" 23:59:59";
			whereSql.append(" and to_date(i.data_date,'yyyy-MM-dd HH24:mi:ss') between to_date(?,'yyyy-MM-dd HH24:mi:ss') and " +
					"to_date(?,'yyyy-MM-dd HH24:mi:ss') ");
			sqlStr.add(startTime1.trim());
			sqlStr.add(endTime1.trim());
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
		if (AppUtils.StringUtil(signal_stat) != null) {
			String indentify_status = "";
			if(signal_stat.equals("2")){
				indentify_status = "2";
			}
			if(signal_stat.equals("1")){
				indentify_status = "1";
			}
			whereSql.append(" and i.indentify_status = ? ");
			sqlStr.add(indentify_status.trim());
		}
		
		
		extrasql += whereSql.toString()+" order by i.create_time ,i.deptno desc";
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectSql", selectSql);
		map.put("extrasql", extrasql);
		map.put("sqlStr", sqlStr);
		// 打印日志
		log.info("organSql--map:" + map);
		return map;
	}
	@Before(PermissionInterceptor.class)
	public void save(){
		String signal = getPara("signal_status");
		String checkLevel = getPara("checker_level");
		String warningCode = getPara("warning_code");
		String flownum = getPara("flownum");
		String remark = getPara("indentify_remark");
		String isquestion = getPara("is_question");
		String result="success";
		String indentify_status="";
		String warning_status="1";//1正式预警，0非正式预警，认定不通过的为非正式预警
		String last_check_stat = "";
		String nowTime = BolusDate.getDate()+BolusDate.getTime();
		Record user = getCurrentUser();
		String action = "";
		String id = AppUtils.getStringSeq();
		if(AppUtils.StringUtil(checkLevel)==null){
			checkLevel =  Db.findFirst("select check_level from dop_warning_param where warning_code=? and warning_dimension !='2' and  (is_use is  null or is_use = '1')",warningCode).getStr("check_level");
		}
		if("1".equals(signal) && "1".equals(isquestion) ){//生效且是问题，则直接判断为是问题
			String updatesql = "update dop_warning_info set indentify_status='1',indentifier=?,indent_org=?,indentify_time=?,checker_level=?,indentify_remark=?,is_question='1'";
			String whereSql = " where flownum='"+flownum+"' ";
			action = "4";
			try{
				Db.update(updatesql+whereSql,user.getStr("id"),user.get("orgnum"),nowTime,checkLevel,remark);
				Db.update("insert into dop_approver_detail (id,flownum,checker,check_time,check_org,remark,action) values" +
						" (?,?,?,?,?,?,?)",new Object[]{id,flownum,user.get("id"),nowTime,user.get("orgnum"),remark,action});
			}catch(Exception e){
				result="fail";
				e.getMessage();
			}
		}else if("1".equals(signal) && "0".equals(isquestion) ){
			String updatesql = "update dop_warning_info set indentify_status='1',indentifier=?,indent_org=?,indentify_time=?,checker_level=?,indentify_remark=?,is_question='0'";
			String whereSql = " where flownum='"+flownum+"' ";
			action = "4";
			try{
				Db.update(updatesql+whereSql,user.getStr("id"),user.get("orgnum"),nowTime,checkLevel,remark);
				Db.update("insert into dop_approver_detail (id,flownum,checker,check_time,check_org,remark,action) values" +
						" (?,?,?,?,?,?,?)",new Object[]{id,flownum,user.get("id"),nowTime,user.get("orgnum"),remark,action});
			}catch(Exception e){
				result="fail";
				e.getMessage();
			}
		}else {
			if(signal.equals("2")){
				indentify_status = "2";
				warning_status = "0";
				action = "5";
			}else{
				indentify_status = "1";//认定通过则将核查状态设为待核查
				last_check_stat = "0";
				action = "4";
			}
			String updtSql = "update dop_warning_info set indentify_status=?,warning_status=?,indentifier=?,indent_org=?,indentify_time=?,checker_level=?,indentify_remark=?,last_check_stat=?";
			String whereSql = " where flownum='"+flownum+"' ";
			try{
				Db.update(updtSql+whereSql,indentify_status,warning_status,user.getStr("id"),user.get("orgnum"),nowTime,checkLevel,remark,last_check_stat);
				Db.update("insert into dop_approver_detail (id,flownum,checker,check_time,check_org,remark,action) values" +
						" (?,?,?,?,?,?,?)",new Object[]{id,flownum,user.get("id"),nowTime,user.get("orgnum"),remark,action});
			}catch(Exception e){
				result="fail";
				e.getMessage();
			}
		}
		setAttr("result",result);
		renderJson();
	}
	@Before(PermissionInterceptor.class)
	public void multiSave(){
		String warningCodes = getPara("ids");
		String signal = getPara("signal_status","2");
		String checkLevel = getPara("check_level");
		String isquestion = getPara("is_question");
		String result = "success";
		Record user = getCurrentUser();
		String nowTime = BolusDate.getDate()+BolusDate.getTime();
		String indentify_status="";
		String warning_status="1";
		String indentify_remark=getPara("indentify_remark");//认定说明
		String last_check_stat="";
		String action = "";
		if("1".equals(signal) && "1".equals(isquestion) ){//生效且是问题，则直接判断为是问题
			String checkLevelSql = "select p.check_level from dop_warning_param p left join dop_warning_info i on i.warning_code=p.warning_code where flownum=? and  (p.is_use is  null or p.is_use = '1')  and p.warning_dimension !='2'";

			String updatesql = "update dop_warning_info set indentify_status='1',indentifier=?,indent_org=?,indentify_time=?,checker_level=?,indentify_remark=?,is_question='1'"
			                   + " where flownum=?";
			action = "4";
			if(warningCodes.contains(",")){
				String[] warningCodeArr = warningCodes.split(",");
				String[][] params = new String[warningCodeArr.length][6];
				String[][] params2 = new String[warningCodeArr.length][7];
				for (int i = 0; i < warningCodeArr.length; i++) {
					if(AppUtils.StringUtil(checkLevel)==null){
						checkLevel =  Db.findFirst(checkLevelSql,warningCodeArr[i]).getStr("check_level");
					}
					params[i][0]=user.getStr("id");
					params[i][1]=user.getStr("orgnum");
					params[i][2]=nowTime;
					params[i][3]=checkLevel;
					params[i][4]=indentify_remark;
					params[i][5]=warningCodeArr[i];
					params2[i][0] = AppUtils.getStringSeq();
					params2[i][1] = warningCodeArr[i];
					params2[i][2] = user.getStr("id");
					params2[i][3] = nowTime;
					params2[i][4] = user.getStr("orgnum");
					params2[i][5] = indentify_remark;
					params2[i][6] = action;
				}
				try{
					int[] counts = Db.batch(updatesql, params, warningCodeArr.length);
					setAttr("saveFlag",counts.length);
					Db.batch("insert into dop_approver_detail (id,flownum,checker,check_time,check_org,remark,action) values" +
						" (?,?,?,?,?,?,?)", params2, warningCodeArr.length);
				}catch(Exception e){
					result="fail";
					System.out.println(e.getMessage());
				}
			}else{
				checkLevel = Db.findFirst(checkLevelSql,warningCodes).getStr("check_level");
				try{
					Db.update(updatesql,user.get("id"),user.get("orgnum"),nowTime,checkLevel,last_check_stat,warningCodes);
					Db.update("insert into dop_approver_detail (id,flownum,checker,check_time,check_org,remark,action) values" +
							" (?,?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),warningCodes,user.get("id"),nowTime,user.get("orgnum"),indentify_remark,action});
				}catch(Exception e){
					result="fail";
					System.out.println(e.getMessage());
				}
			}
		}else if("1".equals(signal) && "0".equals(isquestion) ){//生效且是问题，则直接判断为是问题
			String checkLevelSql = "select p.check_level from dop_warning_param p left join dop_warning_info i on i.warning_code=p.warning_code where flownum=? and  (p.is_use is  null or p.is_use = '1') and p.warning_dimension !='2' ";

			String updatesql = "update dop_warning_info set indentify_status='1',indentifier=?,indent_org=?,indentify_time=?,checker_level=?,indentify_remark=?,is_question='0'"
			                   + " where flownum=?";
			action = "4";
			if(warningCodes.contains(",")){
				String[] warningCodeArr = warningCodes.split(",");
				String[][] params = new String[warningCodeArr.length][6];
				String[][] params2 = new String[warningCodeArr.length][7];
				for (int i = 0; i < warningCodeArr.length; i++) {
					if(AppUtils.StringUtil(checkLevel)==null){
						checkLevel =  Db.findFirst(checkLevelSql,warningCodeArr[i]).getStr("check_level");
					}
					params[i][0]=user.getStr("id");
					params[i][1]=user.getStr("orgnum");
					params[i][2]=nowTime;
					params[i][3]=checkLevel;
					params[i][4]=indentify_remark;
					params[i][5]=warningCodeArr[i];
					params2[i][0] = AppUtils.getStringSeq();
					params2[i][1] = warningCodeArr[i];
					params2[i][2] = user.getStr("id");
					params2[i][3] = nowTime;
					params2[i][4] = user.getStr("orgnum");
					params2[i][5] = indentify_remark;
					params2[i][6] = action;
				}
				try{
					int[] counts = Db.batch(updatesql, params, warningCodeArr.length);
					setAttr("saveFlag",counts.length);
					Db.batch("insert into dop_approver_detail (id,flownum,checker,check_time,check_org,remark,action) values" +
							" (?,?,?,?,?,?,?)", params2, warningCodeArr.length);
				}catch(Exception e){
					result="fail";
					System.out.println(e.getMessage());
				}
			}else{
				checkLevel = Db.findFirst(checkLevelSql,warningCodes).getStr("check_level");
				try{
					Db.update(updatesql,user.get("id"),user.get("orgnum"),nowTime,checkLevel,last_check_stat,warningCodes);
					Db.update("insert into dop_approver_detail (id,flownum,checker,check_time,check_org,remark,action) values" +
							" (?,?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),warningCodes,user.get("id"),nowTime,user.get("orgnum"),indentify_remark,action});
				}catch(Exception e){
					result="fail";
					System.out.println(e.getMessage());
				}
			}
		}else{
			if(signal.equals("2")){
				indentify_status = "2";
				warning_status = "0";
				action = "5";
			}else{
				indentify_status = "1";
				last_check_stat = "0";
				action = "4";
			}
			String sql = "update dop_warning_info set indentify_status=?,warning_status=?,indentifier=?,indent_org=?,indentify_time=?,checker_level=?,last_check_stat=?,indentify_remark=?" +
					" where flownum=?";
			String checkLevelSql = "select p.check_level from dop_warning_param p left join dop_warning_info i on i.warning_code=p.warning_code where flownum=? and  (p.is_use is  null or p.is_use = '1') and p.warning_dimension !='2' ";
			if(warningCodes.contains(",")){
				String[] warningCodeArr = warningCodes.split(",");
				String[][] params = new String[warningCodeArr.length][9];
				String[][] params2 = new String[warningCodeArr.length][7];
				for (int i = 0; i < warningCodeArr.length; i++) {
					if(AppUtils.StringUtil(checkLevel)==null){
						checkLevel =  Db.findFirst(checkLevelSql,warningCodeArr[i]).getStr("check_level");
					}
					params[i][0]=indentify_status;
					params[i][1]=warning_status;
					params[i][2]=user.getStr("id");
					params[i][3]=user.getStr("orgnum");
					params[i][4]=nowTime;
					params[i][5]=checkLevel;
					params[i][6]=last_check_stat;
					params[i][7]=indentify_remark;
					params[i][8]=warningCodeArr[i];
					params2[i][0] = AppUtils.getStringSeq();
					params2[i][1] = warningCodeArr[i];
					params2[i][2] = user.getStr("id");
					params2[i][3] = nowTime;
					params2[i][4] = user.getStr("orgnum");
					params2[i][5] = indentify_remark;
					params2[i][6] = action;
				}
				try{
					int[] counts = Db.batch(sql, params, warningCodeArr.length);
					setAttr("saveFlag",counts.length);
					Db.batch("insert into dop_approver_detail (id,flownum,checker,check_time,check_org,remark,action) values" +
							" (?,?,?,?,?,?,?)", params2, warningCodeArr.length);
				}catch(Exception e){
					result="fail";
					System.out.println(e.getMessage());
				}
			}else{
				if(AppUtils.StringUtil(checkLevel)==null){
					checkLevel =  Db.findFirst(checkLevelSql,warningCodes).getStr("check_level");
				}
				try{
					Db.update(sql,indentify_status,warning_status,user.get("id"),user.get("org_name"),nowTime,checkLevel,last_check_stat,warningCodes);
					Db.update("insert into dop_approver_detail (id,flownum,checker,check_time,check_org,remark,action) values" +
							" (?,?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),warningCodes,user.get("id"),nowTime,user.get("orgnum"),indentify_remark,action});
				}catch(Exception e){
					result="fail";
					System.out.println(e.getMessage());
				}
			}
		}
		
		setAttr("result",result);
		renderJson();
	}
	public void getCheckLevel(){
		String flownum = getPara("ids");
		List<Map<String,String>> returnList = new ArrayList<>();
			StringBuilder sql = new StringBuilder("SELECT deptno FROM dop_warning_info where flownum in (");
			for (int i = 0; i < flownum.split(",").length; i++) {
				sql.append("?,");
			}
			sql.deleteCharAt(sql.length()-1);
			sql.append(") and deptno in (select id from sys_org_info where id = '001001000' or by5 like '%001001000%')");
			Record r = Db.findFirst(sql.toString(), (Object[])flownum.split(","));
			List<Map<String,String>> checkLevel = ParamContainer.getDictList("dop_apply");
			Iterator<Map<String, String>> iterator = checkLevel.iterator();
			while(iterator.hasNext()){
				Map<String,String> m = iterator.next();
				if(m.get("val").equals("1") && r == null){
					returnList.add(m);
				} else if (m.get("val").equals("0")){
					returnList.add(m);
				}
			}
		setAttr("data",returnList);
		renderJson();
	}
	public void getDetailHeaders(){
		String flownum = getPara("flownum");
		System.out.println("getDetailHeaders 获取参数成功");
		String warningCode = Db.findFirst("select warning_code from dop_warning_info where flownum=?",flownum).getStr("warning_code");
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
		List<Record> data = Db.use("gbase").find("select * from idop_warnings_dtls_lvl2 where flownum=?",flownum);
		setAttr("headers",headers);
		setAttr("data",data);
		log.info("此时成功返回数据的前台header");
		System.out.println("此时成功返回数据的前台header");
		renderJson();
	}
	
	public void getDetailHeader(){
		String check_flownum = getPara("check_flownum");
		String flownum=check_flownum;
		String warningCode = Db.findFirst("select warning_code from dop_warning_info where flownum=?",flownum).getStr("warning_code");
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
		List<Record> data = Db.use("gbase").find("select * from idop_warnings_dtls_lvl2 where flownum=?",flownum);
		setAttr("headers",headers);
		setAttr("data",data);
		renderJson();
	}
	public void getDetailDatas(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String flownum = getPara("flownum");
		Page<Record> page = Db.use("gbase").paginate(pageNum, pageSize,"select * "," from idop_warnings_dtls_lvl2 where flownum=?",flownum);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	public void getDetailData(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String flownum = getPara("flownum");
		Page<Record> page = Db.use("gbase").paginate(pageNum, pageSize,"select * "," from idop_warnings_dtls_lvl2 where flownum=?",flownum);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	//下载
	public void download(){
		String flownum = getPara("flownum");
		String warningCode = Db.findFirst("select warning_code from dop_warning_info where flownum=?",flownum).getStr("warning_code");
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
		List<Record> data = Db.use("gbase").find("select * from idop_warnings_dtls_lvl2 where flownum=?",flownum);
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
			fileName = new String(("预警详情描述"+System.currentTimeMillis()+".xls").getBytes("GB2312"), "ISO_8859_1");
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
	
	/**
	 * 获取提示信息查询页面预警名称下拉列表
	 */
	public void getWarningNameList(){
		String val = getPara("val");
		
		String sql = "select warning_code,warning_name from dop_warning_param where warning_dimension='2' and busi_module= '"+val+"' " ;			
		List<Record> list = Db.find(sql);
		setAttr("data",list);
		renderJson();
	}
	
}
