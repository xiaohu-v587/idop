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
 * 组合参数配置
 */
@RouteBind(path = "/cpc")
@Before({ManagerPowerInterceptor.class})
public class CpcCtl extends BaseCtl{
	
	// 记录日志用
	public static Logger log = Logger.getLogger(CpcCtl.class); 

	/**
	 * 加载主页面
	 */
	@Override
//	@Before(PermissionInterceptor.class)
	public void index() {
		render("index.jsp");
	}
	
	
	/**
	 * 查询所有配置
	 */
	public void getList() {
		// 获取当前用户信息
		//String userNo = getCurrentUser().getStr("USER_NO");
		//String orgnum = getCurrentUser().getStr("ORG_ID");
		// 获取查询条件
		String monitorType = getPara("monitor_type");
		String monitorName = getPara("monitor_name");
		String monitorRate = getPara("monitor_rate");
		String monitorLevel = getPara("monitor_level");
		String monitorMethod = getPara("monitor_method");
		String monitorState = getPara("monitor_state");
		
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		// 查询语句
		String selectSql = " select  t.id, t.monitor_name, " +
				"(select remark from sys_param_info where key = 'monitor_type' and val = t.monitor_type) monitor_type, " +
				"(select remark from sys_param_info where key = 'monitor_rate' and val = t.monitor_rate) monitor_rate, " +
				"(select remark from sys_param_info where key = 'monitor_level' and val = t.monitor_level) monitor_level, " +
				"(select remark from sys_param_info where key = 'monitor_method' and val = t.monitor_method) monitor_method, " +
				"(select remark from sys_param_info where key = 'monitor_state' and val = t.monitor_state) monitor_state";
		String extraSql = " FROM DOP_MONITOR_CONFIG t ";

		StringBuffer whereSql = new StringBuffer(" WHERE 1 = 1 and monitor_name is not null");
		List<String> sqlStr = new ArrayList<String>();
		// 查询条件
		if (AppUtils.StringUtil(monitorType) != null) {
			whereSql.append(" AND  t.monitor_type = ? ");
			sqlStr.add(monitorType);
		}
		if (AppUtils.StringUtil(monitorName) != null) {
			whereSql.append("  and t.monitor_name like ? ");
			sqlStr.add("%" + monitorName + "%");
		}
		if (AppUtils.StringUtil(monitorRate) != null) {
			whereSql.append("  and t.monitor_rate = ? ");
			sqlStr.add(monitorRate);
		}
		if (AppUtils.StringUtil(monitorLevel) != null) {
			whereSql.append("  and t.monitor_level = ?");
			sqlStr.add(monitorLevel);
		}
		if (AppUtils.StringUtil(monitorMethod) != null) {
			whereSql.append("  and t.monitor_method = ?");
			sqlStr.add(monitorMethod);
		}
		if (AppUtils.StringUtil(monitorState) != null) {
			whereSql.append("  and t.monitor_state = ? ");
			sqlStr.add(monitorState);
		}
		// 排序
		whereSql.append(" ORDER BY t.monitor_name ");
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
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "监测参数配置", "3", "监测参数配置-查询");
		log.info("监测参数配置-查询");
		// 返回json数据
		renderJson();
	}
	
	/**
	 * 跳转至监测参数编辑页面
	 */
//	@Before(PermissionInterceptor.class)
	public void toEdit() {
		render("form.jsp");
	}
	
	/**
	 * 跳转至监测参数新增页面
	 */
//	@Before(PermissionInterceptor.class)
	public void toAdd() {
		render("form.jsp");
	}
	
	/**
	 * 跳转至监测参数新增页面
	 */
//	@Before(PermissionInterceptor.class)
	public void toView() {
		render("form.jsp");
	}
	
	public void saveAsTpl() {
		String id = AppUtils.getStringSeq();
		String monitorType = getPara("monitor_type");
		String monitorRate = getPara("monitor_rate");
		String monitorLevel = getPara("monitor_level");
		Db.update("insert into dop_monitor_config(id, monitor_type, monitor_rate, monitor_level) values(?, ?, ?, ?)", id, monitorType, monitorRate, monitorLevel);
		saveConditons(id);
		String id1 = AppUtils.getStringSeq();
		Db.update("insert into dop_config_detail(id, config_name, configid) values(?, ?, ?)", id1, getPara("tplName"), id);
		renderJson();
	}
	
	public void getTplList() {
		List<Record> tplList = Db.find("select config_name name, configid val from dop_config_detail p inner join dop_monitor_config t on p.configid = t.id");
		setAttr("data", tplList);
		renderJson();
	}
	
	/**
	 *跳转至新增模板页面 
	 */
	public void toAddTpl() {
		render("form.jsp");
	}
	

	
	/**
	 * 查询指标名称下拉列表
	 */
	public void getMarkParamList(){
		
		String sub_busi_code=getPara("sub_busi_code");
		String monitor_type=getPara("monitor_type");
		
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
		if(AppUtils.StringUtil(monitor_type) != null){
			if (monitor_type.equals("1")) {//1表示监测类型为人员监测，仅查询人员维度指标
				whereSql.append(" AND  mark_dimension = '1' ");
			} else {//其他情况（机构监测、业务监测）时，人员和机构维度都查
				whereSql.append(" AND  mark_dimension in ('0','1') ");
			}
		}
		
//		whereSql.append("  and is_key_mark = ? ");
//		sqlStr.add("0");
		
//		whereSql.append("  and total_type is not null  ");
		whereSql.append("  order by mark_code ");
		sql+=whereSql.toString();

		List<Record>list=Db.find(sql,sqlStr.toArray());
		setAttr("data",list);
		renderJson();
	}
	
	/**
	 * 预警名称下拉列表
	 */
	public void getWarnNameList(){
		String warnType = getPara("val");
		String monitorType = getPara("monitor_type");
		String sql="";
		if (AppUtils.StringUtil(monitorType) != null) {
			if ("1".equals(monitorType)) {
				sql="select t.warning_name,t.warning_code from dop_warning_param  t where t.warning_type_code=(select val from dop_gangedmenu_info t where t.id = "+"'"+warnType+"') and (t.is_use is  null or t.is_use = '1') and t.WARNING_DIMENSION='1' order by t.warning_name asc";
			} else {
				sql="select t.warning_name,t.warning_code from dop_warning_param  t where t.warning_type_code=(select val from dop_gangedmenu_info t where t.id = "+"'"+warnType+"') and (t.is_use is  null or t.is_use = '1') order by t.warning_name asc";
			}
		}
		List<Record> list=Db.find(sql);
		setAttr("data",list);
		renderJson();
	}
	
	//新增保存监测配置
	@Before(Tx.class)
	public void save(){
		//获取前台数据
		//主表主键
		String id = AppUtils.getStringSeq();
		String monitorName = getPara("monitor_name");
		String monitorRate = getPara("monitor_rate");
		String monitorType = getPara("monitor_type");
		String monitorLevel = getPara("monitor_level");
		String monitorMethod = getPara("monitor_method");
		String monitorState = getPara("monitor_state");
		String createTime = AppUtils.getCurrentTimeStr();
		String sql = "";
		if (AppUtils.StringUtil(monitorLevel) != null) {
			sql = "insert into DOP_MONITOR_CONFIG(id, monitor_name, monitor_rate, monitor_type, monitor_level, monitor_method, monitor_state, createtime) values(?,?,?,?,?,?,?,?)";
			Db.update(sql, id, monitorName, monitorRate, monitorType, monitorLevel, monitorMethod, monitorState, createTime);
		} else {
			sql = "insert into DOP_MONITOR_CONFIG(id, monitor_name, monitor_rate, monitor_type, monitor_method, monitor_state, createtime) values(?,?,?,?,?,?,?)";
			Db.update(sql, id, monitorName, monitorRate, monitorType, monitorMethod, monitorState, createTime);
		}
		saveConditons(id);
		// 记录日志
		log.info("组合参数配置-新增");
		renderJson();
	}

	private void saveConditons(String id) {
		int counter = getParaToInt("counter");
		String sql = "";
		int tNum = getParaToInt("tNum");
		int wNum = getParaToInt("wNum");
		Object[][] targetParams = new Object[tNum][8];
		Object[][] warningParams = new Object[wNum][9];
		int tCounter = 0, wCounter = 0;
		for (int i = 1; i <= counter; i++) {
			String flag = getPara("flag_" + i);
			String conditionId = AppUtils.getStringSeq();
			if ("t".equals(flag)) {//指标
				targetParams[tCounter][0] = conditionId;
				targetParams[tCounter][1] = getPara("biz_module_target_" + i);
				targetParams[tCounter][2] = 1;//指标(1) or 预警(0);
				targetParams[tCounter][3] = getPara("num_target_" + i);
				targetParams[tCounter][4] = getPara("operator_target_" + i);
				targetParams[tCounter][5] = id;
				targetParams[tCounter][6] = getPara("biz_type_" + i);
				targetParams[tCounter][7] = getPara("target_"+ i);
				tCounter++;
			} else {//预警
				warningParams[wCounter][0] = conditionId;
				warningParams[wCounter][1] = getPara("biz_module_warning_" + i);
				warningParams[wCounter][2] = 0; 
				warningParams[wCounter][3] = getPara("num_warning_" + i);
				warningParams[wCounter][4] = getPara("operator_warning_" + i);
				warningParams[wCounter][5] = id;
				warningParams[wCounter][6] = getPara("warning_type_" +i);
				warningParams[wCounter][7] = getPara("warning_name_" + i);
				warningParams[wCounter][8] = getPara("question_"+i);
				wCounter++;
			}
		}
		if (tNum > 0) {
			sql = "insert into DOP_MONITOR_CONFIG_DETAIL(id, busi_module, business_type, value, compare_relate, configid, sub_busi_code, mark_type_code) values(?,?,?,?,?,?,?,?)";
			Db.batch(sql, targetParams, tNum);
		}
		if (wNum > 0) {
			sql = "insert into DOP_MONITOR_CONFIG_DETAIL(id, busi_module, business_type, value, compare_relate, configid, sub_busi_code, mark_type_code, question) values(?,?,?,?,?,?,?,?,?)";
			Db.batch(sql, warningParams, wNum);
		}
	}
	
	//编辑保存监测配置
	@Before(Tx.class)
	public void update(){
		String id = getPara("id");
		Db.update("update dop_monitor_config set monitor_name = ?, monitor_rate = ?, monitor_type = ?, monitor_level = ?, monitor_method = ?, monitor_state = ? where id = ?", getPara("monitor_name"), getPara("monitor_rate"), getPara("monitor_type"), getPara("monitor_level"), getPara("monitor_method"), getPara("monitor_state"), id);
		Db.update("delete from dop_monitor_config_detail where configid = ?", id);
		int tNum = getParaToInt("tNum");
		int wNum = getParaToInt("wNum");
		Object[][] targetParams = new Object[tNum][8];
		Object[][] warningParams = new Object[wNum][9];
		int tCounter = 0, wCounter = 0;
		String sql = "";
		int counter = getParaToInt("counter");
		for (int i = 1; i <= counter; i++) {
			String flag = getPara("flag_" + i);
			String conditionId = AppUtils.getStringSeq();
			if ("t".equals(flag)) {//指标
				targetParams[tCounter][0] = conditionId;
				targetParams[tCounter][1] = getPara("biz_module_target_" + i);
				targetParams[tCounter][2] = 1;//指标(1) or 预警(0);
				targetParams[tCounter][3] = getPara("num_target_" + i);
				targetParams[tCounter][4] = getPara("operator_target_" + i);
				targetParams[tCounter][5] = id;
				targetParams[tCounter][6] = getPara("biz_type_" + i);
				targetParams[tCounter][7] = getPara("target_"+ i);
				tCounter++;
			} else {//预警
				warningParams[wCounter][0] = conditionId;
				warningParams[wCounter][1] = getPara("biz_module_warning_" + i);
				warningParams[wCounter][2] = 0; 
				warningParams[wCounter][3] = getPara("num_warning_" + i);
				warningParams[wCounter][4] = getPara("operator_warning_" + i);
				warningParams[wCounter][5] = id;
				warningParams[wCounter][6] = getPara("warning_type_" +i);
				warningParams[wCounter][7] = getPara("warning_name_" + i);
				warningParams[wCounter][8] = getPara("question_"+i);
				wCounter++;
			}
		}
		if (tNum > 0) {
			sql = "insert into DOP_MONITOR_CONFIG_DETAIL(id, busi_module, business_type, value, compare_relate, configid, sub_busi_code, mark_type_code) values(?,?,?,?,?,?,?,?)";
			Db.batch(sql, targetParams, tNum);
		}
		if (wNum > 0) {
			sql = "insert into DOP_MONITOR_CONFIG_DETAIL(id, busi_module, business_type, value, compare_relate, configid, sub_busi_code, mark_type_code, question) values(?,?,?,?,?,?,?,?,?)";
			Db.batch(sql, warningParams, wNum);
		}
		log.info("组合参数配置-修改");
		renderJson();
		
	}
	
	//根据主键，获取主键对应监测详细数据
	public void getDetailById(){
		String id = getPara("key");
		
		/* 获取id对应监测信息*/
		String sql = "select id, monitor_name, monitor_rate, monitor_type, monitor_level, monitor_method, monitor_state from dop_monitor_config where id=?";
		//获取主表数据
		Record re = Db.findFirst(sql, id);	
		
		//获取条件数据
		String sql1 = "select busi_module biz_module_target, value num_target, compare_relate operator_target, sub_busi_code biz_type, mark_type_code target from dop_monitor_config_detail where configid = ? and business_type = '1'";
		String sql2 = "select busi_module biz_module_warning, value num_warning, compare_relate operator_warning, sub_busi_code warning_type, mark_type_code warning_name, question from dop_monitor_config_detail where configid = ? and business_type = '0'";
		List<Record> tList = Db.find(sql1, re.get("id"));	
		List<Record> wList = Db.find(sql2, re.get("id"));
		setAttr("record", re);
		setAttr("tList", tList);
		setAttr("wList", wList);
		setAttr("tSize", tList.size());
		setAttr("wSize", wList.size());
		// 记录日志
		log.info("组合监测配置-获取详情");
		renderJson();
	}
	
	//删除配置表和条件表监测信息
	@Before(Tx.class)
	public void delete(){
		String id = getPara("key");
		Db.update(" DELETE FROM dop_monitor_config T WHERE T.ID=?",id);
		Db.update(" DELETE FROM dop_monitor_config_detail T WHERE T.configid=?",id);
		renderJson();
	}
	
	
	/**
	 *删除模板
	 */
	@Before(Tx.class)
	public void delTpl() {
		String id = getPara("key");
		Db.update(" DELETE FROM dop_monitor_config T WHERE T.ID=?",id);
		Db.update(" DELETE FROM dop_monitor_config_detail T WHERE T.configid=?",id);
		Db.update("delete from dop_config_detail where configid = ?", id);
		setAttr("msg", "success");
		renderJson();
	}

}
