//预警核查
package com.goodcol.controller.dop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.goodcol.util.ext.anatation.RouteBind;
@RouteBind(path="/warning_manage")
@Before({ ManagerPowerInterceptor.class })
public class WarningManageCtl extends BaseCtl {
	public static Logger log = Logger.getLogger(WarningManageCtl.class);
	@Override
	@Before(PermissionInterceptor.class)
	public void index() {
		// TODO Auto-generated method stub
		render("index.jsp");
	}
	
	//首页跳转到待审核界面
	@Before(PermissionInterceptor.class)
	public void toindex() {
		//flag=2时  核查  flag=3时 审核
		String flag = getPara("qryflag");
		String model = getPara("model");
		setAttr("model", model);
		setAttr("flag",flag);
		String checkStat = getPara("check_stat");
		String approvStat = getPara("approv_stat");
		render("index.jsp");
	}
	
	public void check(){
		render("form.jsp");
	}
	public void recheck(){
		render("form_recheck.jsp");
	}
	
	@SuppressWarnings("unchecked")
	public void getList(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		List<Record> lr1 = new ArrayList<Record>();
		String flag = getPara("flag");
		String checkStat = getPara("check_stat");
		String search_check_status=getPara("search_check_status");//查询查复状态
		String days = Db.findFirst("select * from sys_param_info where key=?","dop_apprvl_day").getStr("val");//多少天未处置超时
		//待核查
		if("2".equals(flag)){
			Map<String, Object> map = organSql(2);
			lr1 = Db.find((String) map.get("selectSql")+(String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
			for (Record record : lr1) {
				record.set("dataflag", "1");//待核查
				//判断是否超出预设时间
				addOutDateFlag(record, 1,days);
			}
//		}else if("3".equals(flag)){
//		//待审批
//			Map<String, Object> map = organSql(3);
//			List<Record> lr2 = Db.find((String) map.get("selectSql")+(String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
//			for (Record record : lr2) {
//				record.set("dataflag", "2");//待审批
//				//判断是否超出预设时间
//				addOutDateFlag(record, 2,days);
//			}
//			lr1.addAll(lr2);
		}else{
			
			//Page<Record> page = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"), (String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
			//待核查数据,如果checkStat有值
				Map<String, Object> map = organSql(0);
				lr1 = Db.find((String) map.get("selectSql")+(String) map.get("extrasql"), ((List<String>) map.get("sqlStr")).toArray());
				for (Record record : lr1) {
					record.set("dataflag", "1");//待核查
					//判断是否超出预设时间
					addOutDateFlag(record, 1,days);
				}
			
		}
		int start = (pageNum-1)*pageSize;
		int end = pageSize*pageNum;
		int total = lr1.size();
		if(total < end && total >= start){
			lr1 = lr1.subList(start, total);
		}else{
			lr1 = lr1.subList(start, end);
		}
		setAttr("data", lr1);
		setAttr("total", total);
		renderJson();
	}
	/**
	 * 获取核查流水
	 */
	public void getCheckList(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String flownum = getPara("flownum");
		Page<Record> page = Db.paginate(pageNum, pageSize,"select ad.action,ad.remark,to_date(ad.check_time,'yyyy-MM-dd HH24:mi:ss') check_time,u.name checker_name "," from dop_approver_detail ad left join " +
				" sys_user_info u on u.id=ad.checker where flownum=? order by ad.check_time desc",
				flownum);
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
	

	/**
	 * 保存核查结果
	 */
	public void save(){
		String flownum = getPara("flownum");
		String remark = getPara("checker_remark");
		Record user = getCurrentUser();
		String roleLevel = user.getStr("ROLE_LEVEL");
		String nowTime = BolusDate.getDate()+BolusDate.getTime();
		String result="success";
		//查询该条预警是否需要上级行审批
		String isConfirm = Db.findFirst("select p.is_confirm from dop_warning_param p left join dop_warning_info i on i.warning_code=p.warning_code where i.flownum=? and p.warning_dimension !='2' and  (p.is_use is  null or p.is_use = '1')",flownum).getStr("is_confirm");
		String updtSql = "update dop_warning_info set last_checker=?,last_checker_org=?,last_check_time=?,last_check_stat=?,is_question=?,last_approval_stat=? ,last_approver_org=? ,checker_remark=?";
		String updtSql1 = "update dop_warning_info set last_approver=?,last_approver_org=?,last_approval_time=?,last_approval_stat=?,is_question=? ";
		String whereSql = " where flownum='"+flownum+"'";
		List<String> sqlStr = new ArrayList<String>();
		sqlStr.add(user.getStr("id"));
		sqlStr.add(user.getStr("orgnum"));
		sqlStr.add(nowTime);
		String check = getPara("is_question");
		String approval = getPara("approval");
		String up_Approval_org ="";
		String lastCheckStat = "";//0待核查 1 核查通过，2 核查不通过
		String lastApprovalStat = "";//0待审批 1 核查通过，2 核查不通过
		String action = "";//0 核查通过 ；1 核查不通过；2 审批通过；3 审批不通过--核查通过且不需要审批或核查通过并且审批通过确认为问题
		String isQuestion = "";
		if(AppUtils.StringUtil(check)!=null){
			if("1".equals(check)){
				lastCheckStat="1";
				action="0";		//核查通过
				if("0".equals(isConfirm) ||"1".equals(roleLevel)){//不需要上级行确认则根据核查结果确定是否为问题
					isQuestion = "1";
					
				}
				else
				{
					lastApprovalStat="0";
					//核查人可能是分行下级的子部门，所以要取他数据范围定义的最大机构,找出审批机构
					String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");//数据范围,不能取用户自己所在的机构，而要按照用户的数据范围、角色级别 来查， 数据范围优先
					up_Approval_org = Db.findFirst("select upid from sys_org_info where orgnum =?",sjfw).getStr("orgnum");
					if(AppUtils.StringUtil(up_Approval_org)==null) up_Approval_org="000000000";
				}
			}
			if("0".equals(check)){
				lastCheckStat="2";
				action="1";	//核查不通过
				if("0".equals(isConfirm) ||"1".equals(roleLevel)){//不需要上级行确认则根据核查结果确定是否为问题
					isQuestion = "0";
				}
				else
				{
					lastApprovalStat="0";
					//核查人可能是分行下级的子部门，所以要取他数据范围定义的最大机构,找出审批机构
					String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");//数据范围,不能取用户自己所在的机构，而要按照用户的数据范围、角色级别 来查， 数据范围优先
					up_Approval_org = Db.findFirst("select upid from sys_org_info where orgnum =?",sjfw).getStr("orgnum");
					if(AppUtils.StringUtil(up_Approval_org)==null) up_Approval_org="000000000";
				}
			}
		}

		try{
			sqlStr.add(lastCheckStat);
			sqlStr.add(isQuestion);
			sqlStr.add(lastApprovalStat);
			sqlStr.add(up_Approval_org);
			sqlStr.add(remark);
			//保存核查信息
			int count = Db.update(updtSql+whereSql,sqlStr.toArray());
			//插入核查流水表
			if(count > 0){
				String id = AppUtils.getStringSeq();
				Db.update("insert into dop_approver_detail (id,flownum,checker,check_time,check_org,remark,action) values" +
						" (?,?,?,?,?,?,?)",new Object[]{id,flownum,user.get("id"),nowTime,user.get("orgnum"),remark,action});
			}
		}catch(Exception e){
			result="fail";
			System.out.println(e.getMessage());
		}
		setAttr("result",result);
		renderJson();
	}
	/**
	 * 根据查询条件搜索
	 * @return
	 */
	public Map<String, Object> organSql(int flag) {
		Record user = getCurrentUser();
		String userOrg = user.getStr("MAX_PERMI_ORGNUM");
		String roleLevel = user.getStr("ROLE_LEVEL");
		// 获取页面输入查询条件
		String orgid = getPara("orgid");
		String ywType = getPara("ywtype"); 
		String startTime = getPara("start_time");
		String warnname = getPara("warn_name");
		String endTime = getPara("end_time");
		String startTime1 = getPara("biz_start_time");
		String endTime1 = getPara("biz_end_time");
		String checkStat = getPara("check_stat");
		String search_check_status=getPara("search_check_status");//查询查复状态
		String warnType = getPara("warning_type");

		//String approvStat = getPara("approv_stat");
		String selectSql = " select p.warning_code,p.busi_module,p.warning_name,g.remark warning_type_code,p.warning_level,i.warning_status,to_date(i.create_time,'yyyy-MM-dd HH24:mi:ss') as create_time,to_date(i.data_date,'yyyy-MM-dd HH24:mi:ss') as biz_time," +
				"case i.deptlevel when '0' then '江苏省分行' when '1' then i.lvl_2_branch_name when '2' then i.lvl_3_branch_name" +
				" when '3' then i.lvl_4_branch_name end orgname,i.last_check_stat,i.indentify_status,to_date(i.last_check_time,'yyyy-MM-dd HH24:mi:ss') as" +
				" last_check_time,(select name from sys_user_info where id=i.last_checker) last_checker,i.storage_time,i.flownum,to_date(i.last_approval_time,'yyyy-MM-dd HH24:mi:ss') as last_approval_time," +
				" ( select remark from  SYS_PARAM_INFO f where  f.key='searchCheck_status' and f.val= i.search_check_status ) search_check_status, " +
				" (select name from sys_user_info where id=i.last_approver) last_approver,i.last_approval_stat";
		//预警类型id
		String warningTypeIds = Db.findFirst("select listagg(id,''',''') within group (order by id) ids from dop_gangedmenu_info start with val='1003' connect by prior id =upid ").getStr("ids");
		String extrasql = " from dop_warning_info i left join dop_warning_param p on p.warning_code=i.warning_code " +
//				" left join dop_gangedmenu_info g on g.val=p.warning_type_code and g.id in('"+warningTypeIds+"')  " ;
				" left join dop_gangedmenu_info g on g.val=p.warning_type_code   " ;
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 and (i.indentify_status=1 or i.indentify_status=9) and  (p.is_use is  null or p.is_use = '1') and p.warning_dimension !='2' ");
		List<String> sqlStr = new ArrayList<String>();
		
		
		
		if (AppUtils.StringUtil(ywType) != null && !"1001".equals( ywType)) {
				whereSql.append(" and p.busi_module = ? ");
				sqlStr.add(ywType.trim());
		}
		if (AppUtils.StringUtil(warnname) != null) {
			whereSql.append(" and p.warning_code = ? ");
			sqlStr.add(warnname.trim());
		}
		
		if (AppUtils.StringUtil(warnType) != null) {
			whereSql.append(" and g.id =? ");
			sqlStr.add(warnType.trim());
			
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
		
		if (flag==2 && AppUtils.StringUtil(checkStat) == null)		//首页点过来   并且页面上没有选核查状态时
		{
			whereSql.append(" and (i.last_check_stat='0' ) ");
		}
		
		//查询查复状态
		if(AppUtils.StringUtil(search_check_status)!=null){
			whereSql.append(" and i.search_check_status = ? ");
			sqlStr.add(search_check_status.trim());
		}
		
		
		if (AppUtils.StringUtil(checkStat) != null) {
			whereSql.append(" and i.last_check_stat = ? ");
			sqlStr.add(checkStat.trim());
		}
		
		
		
		if (AppUtils.StringUtil(orgid) != null) {			//页面上选的机构
			//whereSql.append(" and i.deptno in (select id from sys_org_info start with orgnum = '"+org+"' connect by prior orgnum=upid) ");
			whereSql.append(" and i.deptno in (select id from sys_org_info where id = ? or by5 like ?)");
			 sqlStr.add(orgid.trim());
			 sqlStr.add("%" + orgid.trim()+ "%");
		}

		
		
		//查询需要该用户核查的数据
		if((flag==0 || flag ==2) && !"1".equals(roleLevel)){
			whereSql.append("  and case checker_level when '0' then '000000000' when '1' then lvl_2_branch_no  when '2' then lvl_3_branch_no "+
					" when '3' then lvl_4_branch_no end =? ");
			String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");//数据范围,不能取用户自己所在的机构，而要按照用户的数据范围、角色级别 来查， 数据范围优先
			sqlStr.add(sjfw);
		}
		if((flag==0 || flag ==2) && "1".equals(roleLevel)){
			whereSql.append(" and (i.deptno in (select id from sys_org_info where id = ? or by5 like ?) or checker_level='0')");
			 sqlStr.add("001001000");
			 sqlStr.add("%" + "001001000"+ "%");
		}
		
		
		
		
		extrasql += whereSql.toString() +" order by i.create_time";
		// selectSql,extrasql,sqlStr 装入map，返回
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectSql", selectSql);
		map.put("extrasql", extrasql);
		map.put("sqlStr", sqlStr);
		// 打印日志
		log.info("organSql--map:" + map);
		return map;
	}
	//添加待处理超时标志
		private void addOutDateFlag(Record record,int flag,String days){
			Date cmpTime = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			if(flag==1){//待核查		核查在认定时间或者同步到oracle的时间基础上 算超期
				String indentifyTime = record.get("indentify_time");
				String storageTime = record.get("storage_time");
				String cmpStr = indentifyTime==null?storageTime:indentifyTime;
				if(cmpStr!=null){
					try {
						cmpTime = sdf.parse(cmpStr);
					} catch (Exception e) {
						System.out.println("日期转换错误");
						e.printStackTrace();
					}
				}
			}
			if(flag==2){			//审批在核查时间的基础上判断超期
				cmpTime = record.get("last_check_time");
			}
			if(cmpTime!=null){
				long seconds = new Date().getTime()-cmpTime.getTime();
				//判断是否超出预设时间
				if(seconds > Integer.parseInt(days)*24*60*60*1000){
					record.set("out_date", "1");
				}
			}
		}
}
