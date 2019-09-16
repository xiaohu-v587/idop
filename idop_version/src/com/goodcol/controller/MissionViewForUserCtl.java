package com.goodcol.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.MissionUtil;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;
/**
 * 
 * @ClassName: com.goodcol.controller.MissionForUserCtl
 * @Description: 任务管理-已处理的任务
 * @author Wangsq
 * @date 2019-07-29
 *
 */
@RouteBind(path = "/missionViewForUser")
@Before({ ManagerPowerInterceptor.class })
public class MissionViewForUserCtl extends BaseCtl {

	protected Logger log = Logger.getLogger(MissionViewForUserCtl.class);

	@Override
	public void index() {
		render("index.jsp");
	}

	/**
	 * 
	 * @Title: getList
	 * @Description: 加载已处理的任务列表
	 * @author Wangsq
	 * @date 2019-07-29
	 *
	 */
	public void getList() {
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		Map<String, Object> map = organSql();
		@SuppressWarnings("unchecked")
		Page<Record> pageList = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"),
				(String) map.get("extraSql"), ((List<String>) map.get("sqlStr")).toArray());

		setAttr("datas", pageList.getList());
		setAttr("total", pageList.getTotalRow());
		// 打印日志
		log.info("listGyywInfo--pageList.getList():" + pageList.getList());
		log.info("listGyywInfo--pageList.getTotalRow():" + pageList.getTotalRow());
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"),
				String.valueOf(getSession().getAttribute("currentMenu")), "3", "任务查看-查询");
		renderJson();
	}

	/**
	 * 
	 *  @Title: organSql
	 * @Description: 拼接任务查询用SQL
	 * @author Wangsq
	 * @date 2019-07-29
	 * @return 查询用map对象
	 *
	 */
	public Map<String, Object> organSql() {
		Record user = getCurrentUser();//获取当前登录人的用户信息
		String stime = getPara("stime"); // 任务时间开始
		String etime = getPara("etime"); // 任务时间结束
		String missionName = getPara("missionName"); // 任务名称
		String missionStat = getPara("missionIssueStatus"); //任务状态
		String missionType = getPara("missionType"); //任务类型
		String missionFlag = getPara("missionFlag"); //任务分类
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer selectSql = new StringBuffer("SELECT YMC.MISSION_NAME,YMI.MISSION_CONFIG_ID, YMC.MISSION_FLAG, YMU.ID,YMU.MISSION_ISSUE_ID, YMU.UP_MISSION_ID, YMU.USER_NO, YMU.MISSION_STRAT_YMD, YMU.MISSION_END_YMD, YMU.FEEDBACK_YMD, ");
		selectSql.append(" (select REMARK  from sys_param_info spi where 1=1 and spi.status='1' and spi.key='yygl_mission_status' and spi.val=YMU.USER_MISSION_STATUS) as remark, YMU.USER_MISSION_STATUS,");
		selectSql.append(" YMU.ORGID, YMU.ROLEID, YMU.SEND_MESSAGE_FLAG, YMU.CONFIRM_FLAG, YMU.FORWARD_FLAG,(select REMARK  from sys_param_info spi where 1=1 and spi.status='1' and spi.key='yygl_mission_flag' and spi.val=YMC.MISSION_FLAG) as mission_flag1 ");
		//selectSql.append(" (SELECT COUNT(1)  FROM YYGL_MISSION_USER YMU  WHERE YMU.MISSION_ISSUE_ID = YMI.ID AND YMU.USER_MISSION_STATUS = '01') AS COUNT1, ");
		//selectSql.append(" (SELECT COUNT(1)  FROM YYGL_MISSION_USER YMU  WHERE YMU.MISSION_ISSUE_ID = YMI.ID AND YMU.USER_MISSION_STATUS = '02') AS COUNT2 ");
		String extraSql = " FROM YYGL_MISSION_USER YMU  LEFT JOIN YYGL_MISSION_ISSUE YMI ON YMU.MISSION_ISSUE_ID = YMI.ID  LEFT JOIN YYGL_MISSION_CONFIG YMC ON YMI.MISSION_CONFIG_ID = YMC.ID";
		//extraSql = extraSql + " LEFT JOIN YYGL_MISSION_ISSUE YMI ON YMU.MISSION_ISSUE_ID = YMI.ID ";
		StringBuffer whereSql = new StringBuffer();
		List<String> sqlStr = new ArrayList<String>();
		whereSql.append("  WHERE 1=1  ");
		//判定是否最高权限
		if(MissionUtil.getIntanceof().isSysManagerByUser(user)){//高权限人员
			//不限制筛选范围
		}else{
			//限定筛选自己已处理的
			whereSql.append("AND  YMU.USER_NO =  ? AND  YMU.ORGID = ? ");
			sqlStr.add(user.getStr("USER_NO")); //登录人
			sqlStr.add(user.getStr("ORG_ID"));  //登录机构
		}
		// 添加范围开始
		if (AppUtils.StringUtil(stime) != null) {
			whereSql.append(" AND YMU.MISSION_STRAT_YMD >= ? ");
			sqlStr.add(stime);
		}
		// 添加范围结束
		if (AppUtils.StringUtil(etime) != null) {
			whereSql.append(" AND YMU.MISSION_END_YMD <= ? ");
			sqlStr.add(etime);
		}
		// 添加任务名称
		if (AppUtils.StringUtil(missionName) != null) {
			whereSql.append(" AND YMC.MISSION_NAME LIKE ? ");
			sqlStr.add("%" + missionName + "%" );
		}
		// 任务状态
		if (AppUtils.StringUtil(missionStat) != null) {
			whereSql.append(" AND YMU.USER_MISSION_STATUS = ? ");
			sqlStr.add(missionStat);
		}
		// 任务类型
		if (AppUtils.StringUtil(missionType) != null) {
			whereSql.append(" AND YMC.MISSION_TYPE= ? ");
			sqlStr.add(missionType);
		}
		// 任务分类
		if (AppUtils.StringUtil(missionFlag) != null) {
			whereSql.append(" AND YMC.MISSION_flag= ? ");
			sqlStr.add(missionFlag);
		}
		whereSql.append(" AND YMU.USER_MISSION_STATUS not in ('01','04','99') ORDER BY YMU.MISSION_STRAT_YMD DESC ");
		extraSql += whereSql.toString();
		map.put("selectSql", selectSql.toString());
		map.put("extraSql", extraSql);
		map.put("sqlStr", sqlStr);
		return map;
	}
	
	
	
	/**
	 * @Title: taskImplementSituation
	 * @Description: 跳转至查看任务接收人执行情况
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void taskImplementSituation() {
		String id = getPara("id");
		setAttr("id",  id);
		render("taskImplementSituation.jsp");
	}
	
	
	
	/**
	 * @Title: downloadTaskList
	 * @Description: 下载我已处理的任务列表
	 * @author Wangsq
	 * @date 2019-07-31
	 *
	 */
	public void downloadTaskList() {
		getList();
		List<Record> list = getAttr("datas");
		String[] headers = getPara("headers","").split(regex);
		String[] columns = getPara("columns","").split(regex);
		String fileName = "";
		try {
		    fileName = new String(("已处理的任务列表"+System.currentTimeMillis()+".xls").getBytes("GB2312"),"ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		ExcelRenderx er = new ExcelRenderx(fileName, columns, headers, list,getResponse());
		er.render();
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "已处理的任务".toString(), "7","已处理的任务-下载");
		log.info(getLogPoint().exit("已处理的任务-下载").toString());
		renderNull();
	}
	
	
	
	public void getTaskImplemSituation() {
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String id = getPara("id");
		Map<String, Object> map = TaskImplemSituationSql(id);
		@SuppressWarnings("unchecked")
		Page<Record> pageList = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"),
				(String) map.get("extraSql"), ((List<String>) map.get("sqlStr")).toArray());

		setAttr("datas", pageList.getList());
		setAttr("total", pageList.getTotalRow());
		// 打印日志
		log.info("listGyywInfo--pageList.getList():" + pageList.getList());
		log.info("listGyywInfo--pageList.getTotalRow():" + pageList.getTotalRow());
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"),
				String.valueOf(getSession().getAttribute("currentMenu")), "3", "任务查看-查询");
		renderJson();
	}
	
	public Map<String, Object> TaskImplemSituationSql(String id) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT YMU.*,SUI.NAME ";
		String extraSql = " FROM YYGL_MISSION_USER YMU LEFT JOIN SYS_USER_INFO SUI ON YMU.USER_NO = SUI.USER_NO  ";
		StringBuffer whereSql = new StringBuffer();
		List<String> sqlStr = new ArrayList<String>();
		whereSql.append(" WHERE YMU.MISSION_ISSUE_ID = '"+id+"' ");
		
		if (AppUtils.StringUtil(id) != null) {
			whereSql.append(" and id = ? ");
			sqlStr.add(id.trim());
		}
		extraSql += whereSql.toString();
		map.put("selectSql", sql.toString());
		map.put("extraSql", extraSql);
		map.put("sqlStr", sqlStr);
		return map;
	}
	
	
	/**
	 * @Title: trackTasks
	 * @Description: 跳转至任务跟踪页面
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void trackTasks() {
		setAttr("missionIssueId", getPara("missionIssueId"));
		setAttr("missionname", getPara("missionname"));
		render("trackTasks.jsp");
	}
	
	
	/**
	 * @Title: sendUrgeMessage
	 * @Description: 跳转至催办消息页面
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void sendUrgeMessage() {
		render("sendUrgeMessage.jsp");
	}
	
	/**
	 * @Title: sendUrgeMessage
	 * @Description: 跳转至往来记录页面
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void reciprocalRecords() {
		render("reciprocalRecords.jsp");
	}
	
	
	/**
	 * 
	 * @Title: getReciprocalRecordsList
	 * @Description: 加载往来记录列表
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void getReciprocalRecordsList() {
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		Map<String, Object> map = listRecord();
		@SuppressWarnings("unchecked")
		Page<Record> pageList = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"),
				(String) map.get("extraSql"), ((List<String>) map.get("sqlStr")).toArray());

		setAttr("datas", pageList.getList());
		setAttr("total", pageList.getTotalRow());
		// 打印日志
		log.info("listGyywInfo--pageList.getList():" + pageList.getList());
		log.info("listGyywInfo--pageList.getTotalRow():" + pageList.getTotalRow());
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"),
				String.valueOf(getSession().getAttribute("currentMenu")), "3", "任务查看-查询");
		renderJson();
	}

	/**
	 * 
	 *  @Title: listRecord
	 * @Description: 拼接往来记录查询用SQL
	 * @author Wangsq
	 * @date 2019-07-26
	 * @return 查询用map对象
	 *
	 */
	public Map<String, Object> listRecord() {
		String userid = getCurrentUser().getStr("USER_NO");
		String stime = getPara("stime"); // 任务时间开始
		String etime = getPara("etime"); // 任务时间结束
		String missionName = getPara("missionName"); // 任务名称
		String missionStat = getPara("MISSION_STAT"); //任务状态
		String missionType = getPara("missionType"); //任务类型
		String role_level = getCurrentUser().getStr("ROLE_LEVEL"); //权限
		String orgId = getCurrentUser().getStr("ORG_ID");
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer selectSql = new StringBuffer("");

		String extraSql = " FROM YYGL_MISSION_ISSUE YMI ";
		extraSql = extraSql + " LEFT JOIN SYS_PARAM_INFO SPI1 ON SPI1.VAL = YMI.MISSION_ISSUE_STATUS ";
		extraSql = extraSql + " LEFT JOIN YYGL_MISSION_CONFIG YMC ON YMC.MISSION_CONFIG_ID = YMI.MISSION_CONFIG_ID ";
		extraSql = extraSql + " LEFT JOIN SYS_ORG_INFO SOI ON SOI.ORGNUM = YMC.MISSION_LAUNCH_ORG   ";
		extraSql = extraSql + " LEFT JOIN SYS_USER_INFO SUI ON YMC.MISSION_LAUNCH_USERNO = SUI.USER_NO  ";
		StringBuffer whereSql = new StringBuffer();
		List<String> sqlStr = new ArrayList<String>();
		whereSql.append("  WHERE SPI1.KEY = '15001' AND SOI.STAT = '1'  AND (YMC.SELF IS NULL OR (YMC.SELF = '1' AND MISSION_LAUNCH_USERNO = '"+userid+"'))");
		// 添加范围开始
		if (AppUtils.StringUtil(stime) != null) {
			whereSql.append(" AND MISSION_STRAT_YMD >= ? ");
			sqlStr.add(stime);
		}
		// 添加范围结束
		if (AppUtils.StringUtil(etime) != null) {
			whereSql.append(" AND MISSION_STRAT_YMD <= ? ");
			sqlStr.add(etime);
		}
		// 添加任务名称
		if (AppUtils.StringUtil(missionName) != null) {
			whereSql.append(" AND YMC.MISSION_NAME LIKE CONCAT(CONCAT('%',?),'%')");
			sqlStr.add(missionName);
		}
		// 任务状态
		if (AppUtils.StringUtil(missionStat) != null) {
			whereSql.append(" AND SPI1.VAL= ? ");
			sqlStr.add(missionStat);
		}
		if (AppUtils.StringUtil(missionType) != null) {
			whereSql.append(" AND MISSION_TYPE= ? ");
			sqlStr.add(missionType);
		}
		whereSql.append(" ORDER BY YMI.MISSION_STRAT_YMD DESC,YMC.MISSION_NAME");
		extraSql += whereSql.toString();
		map.put("selectSql", selectSql.toString());
		map.put("extraSql", extraSql);
		map.put("sqlStr", sqlStr);
		return map;
	}
	
	/**
	 * @Title: distributeSituation
	 * @Description: 跳转至分发情况页面
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void distributeSituation() {
		render("distributeSituation.jsp");
	}
	
	/**
	 * 
	 * @Title: getDistributeSituationList
	 * @Description: 加载分发情况列表
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void getDistributeSituationList() {
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		Map<String, Object> map = listDistributeSituation();
		@SuppressWarnings("unchecked")
		Page<Record> pageList = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"),
				(String) map.get("extraSql"), ((List<String>) map.get("sqlStr")).toArray());

		setAttr("datas", pageList.getList());
		setAttr("total", pageList.getTotalRow());
		// 打印日志
		log.info("listGyywInfo--pageList.getList():" + pageList.getList());
		log.info("listGyywInfo--pageList.getTotalRow():" + pageList.getTotalRow());
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"),
				String.valueOf(getSession().getAttribute("currentMenu")), "3", "任务查看-查询");
		renderJson();
	}

	/**
	 * 
	 *  @Title: listRecord
	 * @Description: 拼接分发情况查询用SQL
	 * @author Wangsq
	 * @date 2019-07-26
	 * @return 查询用map对象
	 *
	 */
	public Map<String, Object> listDistributeSituation() {
		String userid = getCurrentUser().getStr("USER_NO");
		String stime = getPara("stime"); // 任务时间开始
		String etime = getPara("etime"); // 任务时间结束
		String missionName = getPara("missionName"); // 任务名称
		String missionStat = getPara("MISSION_STAT"); //任务状态
		String missionType = getPara("missionType"); //任务类型
		String role_level = getCurrentUser().getStr("ROLE_LEVEL"); //权限
		String orgId = getCurrentUser().getStr("ORG_ID");
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer selectSql = new StringBuffer("");

		String extraSql = " FROM YYGL_MISSION_ISSUE YMI ";
		extraSql = extraSql + " LEFT JOIN SYS_PARAM_INFO SPI1 ON SPI1.VAL = YMI.MISSION_ISSUE_STATUS ";
		extraSql = extraSql + " LEFT JOIN YYGL_MISSION_CONFIG YMC ON YMC.MISSION_CONFIG_ID = YMI.MISSION_CONFIG_ID ";
		extraSql = extraSql + " LEFT JOIN SYS_ORG_INFO SOI ON SOI.ORGNUM = YMC.MISSION_LAUNCH_ORG   ";
		extraSql = extraSql + " LEFT JOIN SYS_USER_INFO SUI ON YMC.MISSION_LAUNCH_USERNO = SUI.USER_NO  ";
		StringBuffer whereSql = new StringBuffer();
		List<String> sqlStr = new ArrayList<String>();
		whereSql.append("  WHERE SPI1.KEY = '15001' AND SOI.STAT = '1'  AND (YMC.SELF IS NULL OR (YMC.SELF = '1' AND MISSION_LAUNCH_USERNO = '"+userid+"'))");
		// 添加范围开始
		if (AppUtils.StringUtil(stime) != null) {
			whereSql.append(" AND MISSION_STRAT_YMD >= ? ");
			sqlStr.add(stime);
		}
		// 添加范围结束
		if (AppUtils.StringUtil(etime) != null) {
			whereSql.append(" AND MISSION_STRAT_YMD <= ? ");
			sqlStr.add(etime);
		}
		// 添加任务名称
		if (AppUtils.StringUtil(missionName) != null) {
			whereSql.append(" AND YMC.MISSION_NAME LIKE CONCAT(CONCAT('%',?),'%')");
			sqlStr.add(missionName);
		}
		// 任务状态
		if (AppUtils.StringUtil(missionStat) != null) {
			whereSql.append(" AND SPI1.VAL= ? ");
			sqlStr.add(missionStat);
		}
		if (AppUtils.StringUtil(missionType) != null) {
			whereSql.append(" AND MISSION_TYPE= ? ");
			sqlStr.add(missionType);
		}
		whereSql.append(" ORDER BY YMI.MISSION_STRAT_YMD DESC,YMC.MISSION_NAME");
		extraSql += whereSql.toString();
		map.put("selectSql", selectSql.toString());
		map.put("extraSql", extraSql);
		map.put("sqlStr", sqlStr);
		return map;
	}
	
}
