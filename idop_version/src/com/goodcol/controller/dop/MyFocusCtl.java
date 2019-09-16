package com.goodcol.controller.dop;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ext.anatation.RouteBind;

/*
 * 我的关注
 * @author 刘东源
 * @date 2018-11-21
 */
@RouteBind(path = "/myfocus")
@Before({ManagerPowerInterceptor.class})
public class MyFocusCtl extends BaseCtl{
	
	// 记录日志用
	public static Logger log = Logger.getLogger(ParamCtl.class); 

	/**
	 * 加载主页面
	 */
	@Override
	@Before(PermissionInterceptor.class)
	public void index() {
		render("AddFocus.jsp");
	}
	
	
	
	
	public void getMoreFocus(){
		render("MoreFocus.jsp");
	}
	/**
	 * 查询所有算法
	 */
	public void getList() {
		// 获取当前用户信息
		String userNo = getCurrentUser().getStr("USER_NO");
		//String orgnum = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		//String orgnum = getCurrentUser().getStr("ORG_ID");//后续修改
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		// 查询语句
		//String selectSql = " select id,(SELECT orgname from sys_org_info where  orgnum = t.followed_org and stat = '1' )|| (select d.name  from sys_user_info d where d.user_no = t.follower and d.stat='0' )||'的'|| (select remark from sys_param_info where key='dop_ywtype'  and val = t.busi_module)||'中'||(select remark from DOP_GANGEDMENU_INFO where val=t.sub_busi_code)||(select mark_name from dop_mark_param where mark_code=t.mark_code)|| (select remark from sys_param_info where key='dop_follow_type'  and val =t.follow_type) as describe ";
		String selectSql = " select id,(SELECT orgname from sys_org_info where  orgnum = t.followed_org and stat = '1' )" +
		        "|| (select a.name from sys_user_info a left join sys_org_info p on a.org_id=p.orgnum where a.user_no=t.followed_teller)"+
				"|| (select remark from sys_param_info where key='dop_ywtype'  and val = t.busi_module)" +
				"|| (select warning_name from dop_warning_param where warning_code=t.mark_code )" +
				"|| (select remark from sys_param_info where key='dop_follow_type'  and val =t.follow_type) as describe ";
		String extraSql = " from dop_my_follow  t  ";

		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(userNo) != null) {
			whereSql.append(" AND  t.follower = ? ");
			sqlStr.add(userNo);
		}
	/*	if (AppUtils.StringUtil(orgnum) != null) {
			whereSql.append("  and t.org = ? ");
			sqlStr.add(orgnum);
		}*/
	
		
		// 排序
		whereSql.append(" ORDER BY t.change_time ");
		extraSql += whereSql.toString();
		// 查询
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql,extraSql, sqlStr.toArray());
		// 赋值
		setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());
		// 打印日志
		log.info("getList--r.getList():" + r.getList());
		log.info("getList--r.getTotalRow():" + r.getTotalRow());
		// 记录日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "基本算法管理", "3", "基本算法管理-查询");
		log.info("基本算法管理-查询");
		// 返回json数据
		renderJson();
	}
	
	
	//新增保存算法
	@Before(Tx.class)
	public void save(){
		//获取前台数据
		//主表主键
		String id = AppUtils.getStringSeq();
		//被关注机构
		String followed_org = getPara("followed_org");
		//被关注人
		String followed_teller = getPara("followed_teller");
		//业务模块
		String busi_module = getPara("busi_module");
		//关注类别
		String follow_type = getPara("follow_type");
		//业务子类
		String val = getPara("sub_busi_code");
		String sub_busi_code=null;
		Record  list =Db.findFirst(" select val from dop_gangedmenu_info where id =? ",val);
		if(list !=null ){
			sub_busi_code=list.getStr("val");
		}else{
			sub_busi_code="";
		}
		
		//
		String mark_code=getPara("mark_code");
		//关注者机构号
		String org = getCurrentUser().getStr("ORG_ID");
		//String org = getCurrentUser().getStr("MAX_PERMI_ORGNUM");
		//MAX_PERMI_ORGNUM
		//关注人
		String follower = getCurrentUser().getStr("USER_NO");
		SimpleDateFormat simpleFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		//当前时间
		String change_time = simpleFormat.format(new Date());
		//保存信息
		int flag = Db.update("	INSERT INTO dop_my_follow (id,followed_org, followed_teller,busi_module, follow_type, sub_busi_code,org,follower,change_time,mark_code)  " +
				" VALUES(?,?,?,?,?,?,?,?,?,?)",
				new Object[] {id,followed_org, followed_teller,busi_module, follow_type, sub_busi_code,org,follower,change_time,mark_code});
		// 记录日志
		if(flag > 0){
			setAttr("flag", flag);
		}else{
			setAttr("flag", 0);
			
		}
		log.info("基本算法管理-新增");
		renderJson();
	}

	
	//删除算法
	@Before(Tx.class)
	public void delete(){
		String id = getPara("key");
		//根据id删除算法表数据
		if(AppUtils.StringUtil(id) != null){
			String[] ids = id.split(",");
			for(String everId : ids){
				Db.update(" DELETE FROM dop_my_follow T WHERE T.ID='"+everId+"' ");
				setAttr("record", 1);
			}
		}else{
			setAttr("record", 0);
		}
		renderJson();
	}
	
	//查询用户
	public void getAllUser(){
		String org = getPara("org");
		String key = getPara("key");
		StringBuffer sbf = new StringBuffer(" select p.orgname,t.user_no,t.name from sys_user_info t left join sys_org_info p on t.org_id=p.orgnum  where  1=1 ");
		if(AppUtils.StringUtil(org) != null){
			sbf.append(" and t.org_id in(select orgnum from sys_org_info d start with d.orgnum='"+org+"' connect by prior d.orgnum=d.upid) ");
		}
		if(AppUtils.StringUtil(key) != null){
			sbf.append("  and ( t.user_no like  '%"+key+"%' or t.name like '%"+key+"%' )");
		}
		List<Record> typeList = Db.find(sbf.toString());
		renderJson(typeList);
	}
	
	
	//查询用户
	public void getAllUserList(){
		// 获取查询条件
		int pageNum = getParaToInt("pageIndex")+1;
		int pageSize = getParaToInt("pageSize",5);
		String org = getPara("org");
		String key = getPara("key");
		List<String> sqlList = new ArrayList<>();
		StringBuffer selectSql = new StringBuffer(" select p.orgname,t.user_no,t.name ");
		StringBuffer extraSql = new StringBuffer(" from sys_user_info t left join sys_org_info p on t.org_id=p.orgnum  where  1=1  ");
		if(AppUtils.StringUtil(org) != null){
			extraSql.append(" and t.org_id in(select orgnum from sys_org_info d start with d.orgnum='"+org+"' connect by prior d.orgnum=d.upid) ");
		}
		if(AppUtils.StringUtil(key) != null){
			extraSql.append("  and ( t.user_no like  '%"+key+"%' or t.name like '%"+key+"%' )");
		}
		Page<Record> r = Db.paginate(pageNum, pageSize, selectSql.toString(),extraSql.toString(),sqlList.toArray());
		/*setAttr("data", r.getList());
		setAttr("total", r.getTotalRow());*/
		renderJson(r.getList());
	}
	
}
