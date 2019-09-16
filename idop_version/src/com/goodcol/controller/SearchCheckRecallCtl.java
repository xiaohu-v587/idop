package com.goodcol.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.goodcol.controller.dop.WarningSearchCtl;
import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.interceptor.PermissionInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.SeachCheckDetail;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.anatation.RouteBind;

@RouteBind(path="/searchCheckRecall")
@Before({ ManagerPowerInterceptor.class })
public class SearchCheckRecallCtl extends BaseCtl{
	public static Logger log = Logger.getLogger(WarningSearchCtl.class);
	@Override
	public void index() {
		// TODO Auto-generated method stub
		render("index.jsp");
	}
    
	public void check(){
		render("form.jsp");
	}
	
	//首页跳转到查复界面
//	@Before(PermissionInterceptor.class)
	public void toindex() {
		//flag=2时  核查  flag=3时 审核
//		String flag = getPara("qryflag");
		String model = getPara("model");
		setAttr("model", model);
		render("index.jsp");
	}
	
	public void getList(){
		Record user=getCurrentUser();
		String userid=user.getStr("id");
		String orgid=user.getStr("orgnum");
		String sjfw=getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String check_model=getPara("check_model");
		String check_warningname=getPara("check_warningname");
		String check_flownum=getPara("check_flownum");
		String search_org=getPara("search_org");
		String start_time=getPara("start_time");
		String end_time=getPara("end_time");
	//	StringBuffer whereSql = new StringBuffer(" where 1 = 1 and ( search_check_status='0' or search_check_status='2' ) and check_org='"+sjfw+"' or check_org='"+orgid+"'");
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 and ( search_check_status='0' or search_check_status='2' ) and check_org in ('"+sjfw+"','"+orgid+"')");
		List<String> sqlStr = new ArrayList<String>();
		if(AppUtils.StringUtil(check_model) !=null){
			whereSql.append(" and check_model=? ");
			sqlStr.add(check_model);
		}
		if(AppUtils.StringUtil(check_warningname) !=null){
			whereSql.append(" and check_warningname=? ");
			sqlStr.add(check_warningname);
		}
		if(AppUtils.StringUtil(check_flownum) !=null){
			whereSql.append(" and check_flownum=? ");
			sqlStr.add(check_flownum);
		}
		if(AppUtils.StringUtil(search_org) !=null){
			whereSql.append(" and search_org=? ");
			sqlStr.add(search_org);
		}
		if (AppUtils.StringUtil(start_time) != null) {
			start_time=start_time.substring(0,10);
			whereSql.append(" and to_date(t.check_date, 'yyyyMMdd') >= to_date(?, 'yyyy-MM-dd') ");
			sqlStr.add(start_time);
		}
		
		if (AppUtils.StringUtil(end_time) != null) {
			end_time=end_time.substring(0,10);
			whereSql.append(" and to_date(t.check_date, 'yyyyMMdd') <= to_date(?, 'yyyy-MM-dd') ");
			sqlStr.add(end_time);
		}
		
		whereSql.append(" order by check_date ");
		// busi_module
		Page<Record> page=Db.paginate(pageNum, pageSize, "select t.check_flownum,(select remark from sys_param_info p where p.key = 'dop_ywtype' and p.val=t.check_model) as check_model "
				+" , t.check_warningname ,t.search_name,t.search_nameid,t.search_date,t.check_enddate,t.is_over_due," +
				"( select remark from  SYS_PARAM_INFO f where  f.key='searchCheck_status' and f.val= t.search_check_status ) search_check_status"
				, "from dop_search_check t " +whereSql.toString(), sqlStr.toArray());
		
		System.out.println(new Date());
		for(Record ls:page.getList()){
		String check_enddate=ls.get("check_enddate");
		String check_date=ls.get("check_date");
		String time=BolusDate.getDate();
		if(check_enddate !=null){
			if(check_date==null){
				int s= check_enddate.compareTo(time);
				//判断是否超出预设时间
				if(s < 0){
					ls.set("is_over_due", "0");//0代表超时				
				}else{
					ls.set("is_over_due", "1");//1代表不超时
				}
			}else{
				int s= check_enddate.compareTo(check_date);
				//判断是否超出预设时间
				if(s < 0){
					ls.set("is_over_due", "0");//0代表超时				
				}else{
					ls.set("is_over_due", "1");//1代表不超时
				}
			}
			
		}
		}
		
		setAttr("data",page.getList());
		setAttr("total",page.getTotalRow());
		renderJson();
		
	}
	
	public void save(){
		String check_flownum=getPara("check_flownum");
		String check_remark=getPara("check_remark");
		String search_check_status="1";//已查复
		String flownum=check_flownum;
		
		Record user = getCurrentUser();
		String check_nameid=user.getStr("id");
		String check_name=user.getStr("name");
		String check_org=user.getStr("orgnum");
		String check_date=BolusDate.getDate();//查复日期
		String remarks=null;
		String is_over_due="";
		String sql="update dop_warning_info t set t.search_check_status= '"+search_check_status+"' where t.flownum= ? ";
		Db.update(sql,flownum);		
		String result="success";
		
		//设置是否超时
		Record r=Db.findFirst("select check_enddate from dop_search_check where check_flownum =?",check_flownum);
		String check_enddate=r.getStr("check_enddate");
		if(check_enddate !=null){
			int s= check_enddate.compareTo(check_date);
			//判断是否超出预设时间
			if(s< 0){
				is_over_due="0";//0代表超时
			}else{
				is_over_due="1";//1代表不超时
			}
		}
		try {
			Db.update("update dop_search_check t set t.search_check_status = ? , t.check_remark = ? ,check_name=? ,check_nameid=?,check_org=?,check_date=?,t.is_over_due=? where t.check_flownum=?",
					new Object[]{search_check_status,check_remark,check_name,check_nameid,check_org,check_date,is_over_due,check_flownum});
		} catch (Exception e) {
			result="fail";
			System.out.println(e.getMessage());
		}
		
		//插入明细
		SeachCheckDetail.getSeachCheckDetail(check_flownum,search_check_status);
		String sql1="update dop_search_check t set t.remarks= "+remarks+" where t.check_flownum= ? ";
		Db.update(sql1,check_flownum);
		setAttr("result",result);
		renderJson();
	}
	//获取预警编码
	public void getWarningCode(){
		String check_flownum=getPara("check_flownum");
		String flownum=check_flownum;
		String warning_code=Db.findFirst(" select warning_code from dop_warning_info where flownum=? ", flownum).getStr("warning_code");
		setAttr("warning_code",warning_code);
		renderJson();
	}
	//获取预警名称
	public void getWarningTypeList(){
		String val=getPara("val");
		String sql="";
//		sql="select t.warning_name,t.warning_code from dop_warning_param  t where t.busi_module='"+val+"' " +
//			 		"and (t.is_use is  null or t.is_use = '1') and t.warning_code like '%902%'  order by t.warning_name asc";
		sql="select t.warning_name,t.warning_code from dop_warning_param  t where t.busi_module='"+val+"' " +
		 		"and (t.is_use is  null or t.is_use = '1') and t.warning_dimension !='2' order by t.warning_name asc";
		List<Record> list=Db.find(sql);
		setAttr("data",list);
		renderJson();
	}
	
	public void getDetail(){
		String  check_flownum=getPara("check_flownum");
		Record user = getCurrentUser();
		String check_name=user.getStr("name");
		String check_nameid=user.getStr("id");
		String check_org=user.getStr("orgnum");
		String date=BolusDate.getDate();//查询日期
	
		
//		String sqlStr=" update dop_search_check t set t.check_name=? ,check_nameid=?, t.check_org=? ,t.check_date=? where t.check_flownum=? ";
//		Db.update(sqlStr,new Object[]{check_name,check_nameid,check_org,check_date,check_flownum});
		
		String sql="select t.check_flownum,(select remark from sys_param_info p where p.key = 'dop_ywtype' and p.val=t.check_model) as check_model " +
				", t.check_warningname ,t.check_name,t.check_nameid,'"+date+"' check_date,t.search_date ,t.search_name ,t.search_nameid,t.remarks,t.search_matter,t.check_remark,t.trade_information ," +
				" (select f.orgname from sys_org_info f where f.orgnum= t.search_org) search_org , " +
				" (select k.orgname from sys_org_info k where k.orgnum= t.check_org) check_org ," +
				"( select remark from  SYS_PARAM_INFO f where  f.key='searchCheck_status' and f.val= t.search_check_status ) search_check_status " +
				" from dop_search_check t where t.check_flownum= '"+check_flownum+"' ";
		Record record=Db.findFirst(sql);
		

		
		setAttr("record",record);
		renderJson();
	}
}
