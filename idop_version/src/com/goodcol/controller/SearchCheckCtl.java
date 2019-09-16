package com.goodcol.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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


@RouteBind(path="/searchCheck")
@Before({ ManagerPowerInterceptor.class })
public class SearchCheckCtl extends BaseCtl {
  
	public static Logger log = Logger.getLogger(WarningSearchCtl.class);
	@Override
	public void index() {
		render("index.jsp");
	}
	//发起查询查复
	public void form(){
		//获取当前登陆人id
		String  id = getCurrentUser().getStr("id");
		setAttr("id",id);
		render("form.jsp");
	}
	public void getCheckFlownum(){
		String flownum=getPara("flownum");
		String check_flownum=Db.findFirst("select check_flownum from dop_search_check where check_flownum like '%"+flownum+"%'").getStr("check_flownum");
		setAttr("check_flownum",check_flownum);
		renderJson();
	}
	//获取页面信息
	public void getDetail(){
		String check_flownum=getPara("check_flownum");
		String sql="select * from dop_search_check where check_flownum=? ";
		List<Record> data=Db.find(sql, check_flownum);
		if(data !=null){
			String org= data.get(0).getStr("check_org");
			//String name= data.get(0).getStr("check_name");
			String orgname=Db.findFirst("select t.orgname from sys_org_info t where t.orgnum='"+org+"'").getStr("orgname");
			//String check_name=Db.findFirst("select name from sys_user_info where user_no ='"+name+"'").getStr("name");
			//select * from sys_user_info where user_no=
			for(Record str:data){
				str.set("check_org", orgname);
				//str.set("check_name", check_name);
			}
		}
		setAttr("record",data);
		renderJson();
	}
	//准备发起查询查复保存结果
	public void save(){
		Record user = getCurrentUser();
		String check_flownum=getPara("check_flownum");
		String flownum=check_flownum;
		
		Record data = Db.findFirst("select case t.deptlevel when '0' then '江苏省分行' when '1' then t.lvl_2_branch_name when '2' then t.lvl_3_branch_name when '3' then t.lvl_4_branch_name end orgname ,deptno" +
				" from  dop_warning_info t left join dop_warning_param f  on t.warning_code=f.warning_code where flownum=? ",flownum);
		String orgname=data.getStr("orgname");
		String deptno=data.getStr("deptno");
		String check_org=getPara("check_org");
		if(check_org.equals(orgname)){
			check_org=deptno;
		}
		String search_check_status="0";//待复查
		String id=AppUtils.getStringSeq();
		String result="success";
		//更新dop_warning_info中search_check_status
		String sql="update dop_warning_info t set t.search_check_status= '"+search_check_status+"' where t.flownum= ? ";
		Db.update(sql,flownum);
		
		String check_enddate=getPara("check_enddate");//查复截止日期
		String search_date=BolusDate.getDate();//查询日期
		String createtime=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
		String check_deadline=getPara("check_deadline");//查复期限
		String search_matter=getPara("search_matter");
		String search_name=user.getStr("name");//user.getStr("orgnum")//查复人id
		String search_nameid=user.getStr("id");//查询人
		String search_org=user.getStr("org_id");//查询人机构
		String remarks=null;
		String check_model="";
		String check_warningname="";
		//查询业务模块 预警名称
		String sqlstr="select f.busi_module,f.warning_name from  dop_warning_info t left join dop_warning_param f  on t.warning_code=f.warning_code where flownum='"+flownum+"' and f.warning_dimension !='2'";
		 Record listStr= Db.findFirst(sqlstr);
		if(listStr !=null){
			check_model=listStr.getStr("busi_module");
			check_warningname=listStr.getStr("warning_name");
		}
		//插入操作
	    try {	    	
	    	Db.update(" update dop_search_check t set check_org=?, search_check_status=? ,check_enddate=? , search_date=? ,check_deadline=?,search_name=? ,search_nameid=?,search_matter=?,search_org=?,remarks=? where check_flownum=? "
	    			,new Object[]{check_org,search_check_status,check_enddate,search_date,check_deadline,search_name,search_nameid,search_matter,search_org,remarks,check_flownum});
	    	
	    	Db.update(" insert into dop_searchCheck_detail( id,check_flownum ,search_date,action,search_name,search_nameid, check_org,search_org,remarks,createtime)values(?,?,?,?,?,?,?,?,?,?)"
					,new Object[]{id,check_flownum,search_date,search_check_status,search_name,search_nameid,check_org,search_org,search_matter,createtime});//插入明系
	    	
		} catch (Exception e) {
			result="fail";
			System.out.println(e.getMessage());
		}
		setAttr("result",result);
		renderJson();
	}
	
	//发起查询查复
	public void conservation(){
		Record user = getCurrentUser();
		String check_flownum=getPara("check_flownum");
		//发起保存钱先查询是否为回退
		String is_over_due="1";
	/*	String sql="select search_check_status from dop_warning_info where check_flownum='"+check_flownum+"' ";
		String search_check_status=Db.findFirst(sql).getStr("search_check_status");
		if("2".equals(search_check_status)){
			search_check_status="0";////如果是回退先变更状态 
		}*/
			
		String result="success";
		String remarks=getPara("remarks");
		
		//判断数据是否超时
		Record list=Db.findFirst("select * from dop_search_check where check_flownum=?", check_flownum);
		if(list !=null){
			String check_enddate=list.get("check_enddate");			
			String time=BolusDate.getDate();
			if(check_enddate !=null){
				int s= check_enddate.compareTo(time);
				//判断是否超出预设时间
				if(s< 0){
					is_over_due="0";//0代表超时
				}
			}
		}
		
		try {
			//Db.update(" update dop_search_check t set remarks= ? ,search_check_status=? where t.check_flownum=? " , new Object[]{remarks,search_check_status,check_flownum});
			Db.update(" update dop_search_check t set remarks= ? ,is_over_due=? where t.check_flownum=? " , new Object[]{remarks,is_over_due,check_flownum});
		} catch (Exception e) {
			result="fail";
			System.out.println(e.getMessage());
		}
		setAttr("result",result);
		renderJson();
	}
	//查询完成
	public void finsh(){
		String check_flownum=getPara("check_flownum");
		String remarks=getPara("remarks");
		String result="success";
		String is_over_due="1";
		String flownum=check_flownum;
		String search_check_status="4";
		//更新dop_warning_info中search_check_status
				String sql="update dop_warning_info t set t.search_check_status= '"+search_check_status+"' where t.flownum= ? ";
				Db.update(sql,flownum);
				
//				//判断数据是否超时
//				Record list=Db.findFirst("select * from dop_search_check where check_flownum=?", check_flownum);
//				if(list !=null){
//					String check_enddate=list.get("check_enddate");			
//					String time=BolusDate.getDate();
//					if(check_enddate !=null){
//						int s= check_enddate.compareTo(time);
//						//判断是否超出预设时间
//						if(s< 0){
//							is_over_due="0";//0代表超时
//						}
//					}
//				}
				
		try {
			Db.update(" update dop_search_check t set search_check_status= ? ,remarks= ?  where t.check_flownum=? " , new Object[]{search_check_status,remarks,check_flownum});
		} catch (Exception e) {
			result="fail";
			System.out.println(e.getMessage());
		}
		SeachCheckDetail.getSeachCheckDetail(check_flownum,search_check_status);
		setAttr("result",result);
		renderJson();
	}

	
	//查询收回
	public void takeBack(){
		String check_flownum=getPara("check_flownum");
		String check_name="";
		String check_remark=null;
		String remarks=getPara("remarks");
		String d="";
		String result="success";
		String search_check_status="3";
		String flownum=check_flownum;
		//更新dop_warning_info中search_check_status
				String sql="update dop_warning_info t set t.search_check_status= '"+search_check_status+"' where t.flownum= ? ";
				Db.update(sql,flownum);
				
				
				//判断数据是否超时
				String is_over_due="1";
				Record list=Db.findFirst("select * from dop_search_check where check_flownum=?", check_flownum);
				if(list !=null){
					String check_enddate=list.get("check_enddate");
					String check_date=list.get("check_date");
					String time=BolusDate.getDate();
					
					if(check_enddate !=null){
						if(check_date!=null){
							int s= check_enddate.compareTo(check_date);
							if(s< 0){
								is_over_due="0";//0代表超时
							}
						}else{
							int s= check_enddate.compareTo(time);
							//判断是否超出预设时间
							if(s< 0){
								is_over_due="0";//0代表超时
							}
						}
					}
				}
		try {
			Db.update(" update dop_search_check t set search_check_status= ? ,is_over_due=? ,check_date=?,check_name=?,check_remark=? ,remarks=? where t.check_flownum=? " , new Object[]{search_check_status,is_over_due,d,check_name,check_remark,remarks,check_flownum});
		} catch (Exception e) {
			result="fail";
			System.out.println(e.getMessage());
		}
		SeachCheckDetail.getSeachCheckDetail(check_flownum,search_check_status);
		setAttr("result",result);
		renderJson();
	}
	public void backform(){
		render("backform.jsp");
	}
	public void takebackform(){
		render("takebackform.jsp");
	}
	
	public void backSave(){		
		renderJson();
	}
	
	public void takebackSave(){		
		renderJson();
	}
	
	//退回
	public void back(){
		String check_flownum=getPara("check_flownum");
		String check_enddate=getPara("check_enddate");
		String check_deadline=getPara("check_deadline");
		String remarks=getPara("remarks");
		String result="success";
		String search_check_status="2";
		String flownum=check_flownum;
		String time=BolusDate.getDate();
		String is_over_due="1";
		String check_name="";
		String check_date="";
		String check_remark=null;
		
		//更新dop_warning_info中search_check_status
		String sql="update dop_warning_info t set t.search_check_status= '"+search_check_status+"' where t.flownum= ? ";
		Db.update(sql,flownum);
		
		try {
			Db.update(" update dop_search_check t set search_check_status= ? ,check_enddate=?,check_deadline=?,is_over_due=?,check_name=?,check_date=?, check_remark=? ,remarks=? where t.check_flownum=? " , new Object[]{search_check_status,check_enddate,check_deadline,is_over_due,check_name,check_date, check_remark,remarks,check_flownum});
		} catch (Exception e) {
			result="fail";
			System.out.println(e.getMessage());
		}
		SeachCheckDetail.getSeachCheckDetail(check_flownum,search_check_status);
//		Db.update(" update dop_search_check t set remarks=null  where t.check_flownum=? " , new Object[]{check_flownum});

		setAttr("result",result);
		renderJson();
	}
	
	
	//获取时间
	public void getCheckDate(){
		String flownum=getPara("flownum");
		String check_flownum=Db.findFirst(" select check_flownum from dop_search_check where check_flownum like '%"+flownum+"%' ").getStr("check_flownum");
		setAttr("check_flownum",check_flownum);
		renderJson();
	}
	//获取机构信息
	public void getOrgInformation(){
		String flownum=getPara("flownum");
		
		Record data = Db.findFirst("select case t.deptlevel when '0' then '江苏省分行' when '1' then t.lvl_2_branch_name when '2' then t.lvl_3_branch_name when '3' then t.lvl_4_branch_name end orgname " +
				" from  dop_warning_info t left join dop_warning_param f  on t.warning_code=f.warning_code where flownum=? ",flownum);
		setAttr("data",data);
		renderJson();
	}
	public void getCheckEndDate(){
		String val=getPara("val");
		int day=Integer.parseInt(val);
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		//calendar.clear();
		calendar.add(Calendar.DAY_OF_MONTH, day);
		
		Date date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String str = sdf.format(date);
		System.out.println(str);
		setAttr("data",str);
		renderJson();
	}
	//查询流水
	public void  getCheckList(){
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String check_flownum = getPara("check_flownum");
		Page<Record> page = Db.paginate(pageNum, pageSize," select ( select remark from  SYS_PARAM_INFO where  key='searchCheck_status' and val = ad.action) action,ad.remarks remark,to_date(ad.check_date,'yyyy-MM-dd') check_date,to_date(ad.search_date,'yyyy-MM-dd') search_date," +
				"  check_name , search_name ",
				" from dop_searchCheck_detail ad  where check_flownum=? order by ad.createtime ",
				check_flownum);
		//(select name  from sys_user_info f where f.id=ad.check_name ),(select name  from sys_user_info f where f.id=ad.search_name )		
		setAttr("data", page.getList());
		setAttr("total", page.getTotalRow());
		renderJson();
	}
}
