package com.goodcol.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Model;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.model.SysPosition;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.ext.anatation.RouteBind;
import com.goodcol.util.safe.MD5;

/**
 * 岗位管理
 * @author changxy
 * @version 1.0
 * 
 */
@RouteBind(path = "/position")
@Before({ManagerPowerInterceptor.class})
public class PositionCtl extends BaseCtl {
	public static Logger log=Logger.getLogger(PositionCtl.class);

	@Override
	public void index() {
		render("index.jsp");
	}
	public void list(){
		super.defualtList(organSql());
		// 打印日志
		log.info("listUser--r.getList():" +getAttr("data"));
		log.info("listUser--r.getTotalRow():" + getAttr("total"));
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "岗位管理", "3", "岗位信息-查询");
		// 返回json数据
		renderJson();		
	}
	
	/**
	 * 条件组合函数
	 * @return
	 */
	protected Map<String, Object> organSql() {
		String selectSql = " select * ";
		String extrasql = " from  sys_position ";
		StringBuffer whereSql = new StringBuffer(" where 1 = 1 ");
		List<String> sqlStr = new ArrayList<String>();
		super.setFindPara(whereSql, sqlStr, "name,describe,duty".split(regex), "data_state".split(regex));
		super.betweenPara(whereSql, sqlStr, "term,term_days".split(regex));
		extrasql += whereSql.toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("selectSql", selectSql);
		map.put("extrasql", extrasql);
		map.put("sqlStr", sqlStr);
		log.info("organSql--map:" + map);
		return map;		
	}
	/**
	 * 新增编辑页面
	 */
	public void form(){
		render("form.jsp");
	}

	/**
	 * 保存或修改
	 */
	public void save(){
		Model<?> model = getModel(SysPosition.class);
		String id = model.getStr("id");
		if (AppUtils.StringUtil(id) != null)
		{
			model.update();
		} 
		else 
		{
			model.set("id",  AppUtils.getStringSeq()).set("data_state", "0").save();
		}
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "岗位管理", "4", "岗位信息-"+(AppUtils.StringUtil(id) != null?"修改":"新增"));
		renderJson();
	}
	
	/**
	 * 删除
	 */
	public void del() {
		String uuids = getPara("ids");
		if (uuids.contains(",")) {
			String[] array = uuids.split(",");
			String sql ="update sys_position set data_state='1' where id in "+super.getParasToStringRegex(array);
			Db.update(sql, array);
		} else {
			Db.update("update sys_position set data_state='1' where id = ? ", uuids);
		}
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "岗位管理", "6", "岗位信息-删除");
		renderJson();
	}
	
	
}
