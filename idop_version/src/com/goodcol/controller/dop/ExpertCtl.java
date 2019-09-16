package com.goodcol.controller.dop;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang.StringUtils;

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
 * 专家页面参数配置
 */
@RouteBind(path = "/expert")
@Before({ManagerPowerInterceptor.class})
public class ExpertCtl extends BaseCtl{
	
	// 记录日志用
	public static Logger log = Logger.getLogger(ExpertCtl.class); 

	/**
	 * 加载主页面
	 */
	@Override
//	@Before(PermissionInterceptor.class)
	public void index() {
		render("index.jsp");
	}
	
	/**
	 * 跳转至新增页面
	 */
//	@Before(PermissionInterceptor.class)
	public void toAdd() {
		render("form.jsp");
	}
	
	/**
	 * 跳转至编辑页面
	 */
//	@Before(PermissionInterceptor.class)
	public void toEdit() {
		render("form.jsp");
	}
	
	//获取页面名称
	public void getPageName(){
		String sql = "select id,page from dop_expert_config c ";
		List<Record> list = Db.find(sql);
		setAttr("data",list);
		renderJson();
	}
	

	
	/**
	 * 查询所有配置
	 */
	public void getList() {
		// 获取当前用户信息
		//String userNo = getCurrentUser().getStr("USER_NO");
		//String orgnum = getCurrentUser().getStr("ORG_ID");
		// 获取查询条件
	
		String module = getPara("module");
		String dict = getPara("dict");
		String page = getPara("page");
		String resid = getPara("resid");
		
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
				
		// 查询语句
		String selectSql = " select  t.id, t.page,t.resid,(select name from sys_user_info i where i.user_no=t.createuser) createuser," +
				"t.createtime,(select name from sys_user_info i where i.user_no=t.last_check_user) last_check_user,t.last_check_time, " +
				"(select remark from sys_param_info where key = 'bi_module' and val = t.module) module, " +
				"(select remark from sys_param_info where key = 'dict' and val = t.dict) dict" ;
				
		String extraSql = " FROM DOP_EXPERT_CONFIG t ";

		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(module) != null) {
			whereSql.append(" AND  t.module = ? ");
			sqlStr.add(module);
		}
		if (AppUtils.StringUtil(dict) != null) {
			whereSql.append("  and t.dict = ? ");
			sqlStr.add(dict);
		}
		if (AppUtils.StringUtil(page) != null) {
			whereSql.append("  and t.page = ?");
			sqlStr.add(page);
		}
		if (AppUtils.StringUtil(resid) != null) {
			whereSql.append("  and t.resid like ? ");
			sqlStr.add("%" + resid + "%");
		}
		
		// 排序
		whereSql.append(" ORDER BY t.createtime desc ");
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
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "专家报告报表参数配置", "3", "专家报告报表参数配置-查询");
		log.info("专家报告报表参数配置-查询");
		// 返回json数据
		renderJson();
	}
	
	//新增保存配置
	@Before(Tx.class)
	public void save(){
		//获取前台数据
		//主表主键
		String id = AppUtils.getStringSeq();
		String module = getPara("module");
		String dict = getPara("dict");
		String page = getPara("page");
		String resid = getPara("resid");
		String createUser=getCurrentUser().getStr("USER_NO");
		String createTime = AppUtils.getCurrentTimeStr();
		String sql = "";
		sql = "insert into DOP_EXPERT_CONFIG(id, module, dict, page, resid, createuser, createtime) values(?,?,?,?,?,?,?)";
		Db.update(sql, id, module, dict, page, resid, createUser, createTime);
		
		// 记录日志
		log.info("专家分析报表参数配置-新增");
		renderJson();
	}
	
	//根据主键，获取主键对应监测详细数据
	public void getDetailById(){
		String id = getPara("key");
		
		/* 获取id对应监测信息*/
		String sql = "select id, module, page, resid, dict from dop_expert_config where id=?";
		//获取主表数据
		Record re = Db.findFirst(sql, id);		
		setAttr("record", re);
		// 记录日志
		log.info("组合监测配置-获取详情");
		renderJson();
	}
	
	//编辑保存配置
	@Before(Tx.class)
	public void update(){
		String id = getPara("id");
		String module = getPara("module");
		String dict = getPara("dict");
		String page = getPara("page");
		String resid = getPara("resid");
		String user=getCurrentUser().getStr("USER_NO");
		String time = AppUtils.getCurrentTimeStr();
		String sql = "";
		sql = "update  DOP_EXPERT_CONFIG set module=?, dict=?, page=?, resid=?, last_check_user=?, last_check_time=? where id=? ";
		Db.update(sql, module, dict, page, resid, user, time,id);
		
		log.info("专家分析报表参数配置-修改");
		renderJson();
		
	}
	
	//删除配置表和条件表监测信息
	@Before(Tx.class)
	public void delete(){
		String id = getPara("key");
		Db.update(" DELETE FROM dop_expert_config T WHERE T.ID=?",id);
//		Db.update(" DELETE FROM dop_monitor_config_detail T WHERE T.configid=?",id);
		renderJson();
	}
	
//	public void dictValueChanged(){
//		String dict =getPara("dict");
//		String sql = "select id,page from dop_expert_config c where dict=? ";
//		List<Record> list = Db.find(sql,dict);
//		setAttr("data",list);
//		renderJson();
//	}


}
