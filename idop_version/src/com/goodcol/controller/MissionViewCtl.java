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
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.MissionControlUtil;
import com.goodcol.util.MissionUtil;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;

import net.sf.json.JSONObject;
/**
 * 
 * @ClassName: com.goodcol.controller.MissionViewCtl
 * @Description: 任务管理-我发起的任务
 * @author Wangsq
 * @date 2019-07-26
 *
 */
@RouteBind(path = "/missionView")
@Before({ ManagerPowerInterceptor.class })
public class MissionViewCtl extends BaseCtl {

	protected Logger log = Logger.getLogger(MissionViewCtl.class);

	@Override
	public void index() {
		render("index.jsp");
	}

	/**
	 * 
	 * @Title: getList
	 * @Description: 加载发起的任务列表
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void getList() {
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		
		Map<String, Object> map = organSql();
		//拼接sql查询
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
	 * @date 2019-07-26
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
		String missionFlag = getPara("missionFlag"); //任务类型
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer selectSql = new StringBuffer("SELECT YMI.ID,YMI.MISSION_CONFIG_ID,YMC.MISSION_NAME, (select REMARK  from sys_param_info spi where 1=1 and spi.status='1' and spi.key='yygl_mission_status' and spi.val=YMI.MISSION_ISSUE_STATUS) as remark, YMC.MISSION_FLAG, YMI.MISSION_ISSUE_STATUS, YMI.MISSION_STRAT_YMD, YMI.MISSION_END_YMD,");
		selectSql.append("(select REMARK  from sys_param_info spi where 1=1 and spi.status='1' and spi.key='yygl_mission_flag' and spi.val=YMC.MISSION_FLAG) as mission_flag1");
		//selectSql.append(" (CASE WHEN (sysdate - to_date(YMI.MISSION_END_YMD,'yyyy-MM-dd HH24:mi:ss')) > 0 AND YMI.MISSION_ISSUE_STATUS not in ('99') AND YMC.MISSION_ISFEEDBACK != '0' THEN  (CASE WHEN YMI.MISSION_ISSUE_STATUS = '07' THEN '07' ELSE '04' END) ELSE YMI.MISSION_ISSUE_STATUS END )   MY_MISSION_STATX, ");
		//selectSql.append(" (SELECT COUNT(1)  FROM YYGL_MISSION_USER YMU  WHERE YMU.MISSION_ISSUE_ID = YMI.ID AND YMU.USER_MISSION_STATUS = '01') AS COUNT1, ");
		//selectSql.append(" (SELECT COUNT(1)  FROM YYGL_MISSION_USER YMU  WHERE YMU.MISSION_ISSUE_ID = YMI.ID AND YMU.USER_MISSION_STATUS = '02') AS COUNT2 ");
		String extraSql = " FROM YYGL_MISSION_ISSUE YMI ";
		extraSql = extraSql + " LEFT JOIN YYGL_MISSION_CONFIG YMC ON YMI.MISSION_CONFIG_ID = YMC.ID ";
		//extraSql = extraSql + " LEFT JOIN SYS_PARAM_INFO SPI1 ON SPI1.VAL = YMI.MISSION_ISSUE_STATUS ";
		StringBuffer whereSql = new StringBuffer();
		List<String> sqlStr = new ArrayList<String>();
		whereSql.append("  WHERE YMC.DELETE_FLAG = '1' ");
		//判定是否最高权限
		if(MissionUtil.getIntanceof().isSysManagerByUser(user)){//高权限人员
			//不限制筛选范围
		}else{
			//限定筛选自己发起的
			whereSql.append("AND YMC.MISSION_LAUNCH_USERNO = ? AND  YMC.MISSION_LAUNCH_ORG = ? ");
			sqlStr.add(user.getStr("USER_NO")); //登录人
			sqlStr.add(user.getStr("ORG_ID"));  //登录机构
		}
		
		// 添加范围开始
		if (AppUtils.StringUtil(stime) != null) {
			whereSql.append(" AND YMI.MISSION_STRAT_YMD >= ? ");
			sqlStr.add(stime);
		}
		// 添加范围结束
		if (AppUtils.StringUtil(etime) != null) {
			whereSql.append(" AND YMI.MISSION_STRAT_YMD <= ? ");
			sqlStr.add(etime);
		}
		// 添加任务名称（模糊查询）
		if (AppUtils.StringUtil(missionName) != null) {
			whereSql.append(" AND YMC.MISSION_NAME LIKE ? ");
			sqlStr.add("%" + missionName + "%" );
		}
		// 任务状态
		if (AppUtils.StringUtil(missionStat) != null) {
			whereSql.append(" AND YMI.MISSION_ISSUE_STATUS= ? ");
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
		whereSql.append(" ORDER BY YMI.MISSION_STRAT_YMD DESC,YMC.MISSION_NAME");
		extraSql += whereSql.toString();
		map.put("selectSql", selectSql.toString());
		map.put("extraSql", extraSql);
		map.put("sqlStr", sqlStr);
		return map;
	}
	
	/**
	 * @Title: revokeTasks
	 * @Description: 撤销任务
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void revokeTasks() {
		String missionIssueID = getPara("missionIssueID");
		int flag = 0;
		try {
			//根据我发起的任务ID,修改我发起的任务表（YYGL_MISSION_ISSUE）状态为05已撤销，同时修改待处理任务表里面的状态为05已撤销
			flag = Db.update("UPDATE YYGL_MISSION_ISSUE SET MISSION_ISSUE_STATUS = '05' WHERE ID = ?", missionIssueID);
			flag += Db.update("UPDATE YYGL_MISSION_USER SET USER_MISSION_STATUS =  '05' WHERE MISSION_ISSUE_ID = ?", missionIssueID);
		} catch (Exception e) {
			
			flag = -1;
			log.error("任务管理-任务查看-撤销异常", e);
		}
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "撤销任务", "4", "我发起的任务-撤销任务");
		renderText(String.valueOf(flag));
	}
	
	
	/**
	 * @Title: stopTasks
	 * @Description: 终止任务
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void stopTasks() {
		String missionIssueID = getPara("missionIssueID");
		int flag = 0;
		try {
			// 修改我发起的任务表YYGL_MISSION_ISSUE的结束时间为当前时间，状态改为07已结束，终止的任务仍可进行反馈
			flag = Db.update("UPDATE YYGL_MISSION_ISSUE SET MISSION_END_YMD = '"+DateTimeUtil.getTime()+"',MISSION_ISSUE_STATUS = '07' WHERE ID = ?", missionIssueID);
			//flag += Db.update("UPDATE YYGL_MISSION_USER SET USER_MISSION_STATUS = '07' WHERE ID = ?", missionIssueID);
		} catch (Exception e) {
			flag = -1;
			log.error("任务管理-我发起的任务-终止异常", e);
		}
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "终止任务", "4", "我发起的任务-终止任务");
		renderText(String.valueOf(flag));
	}
	
	
	/**
	 * @Title: 
	 * @Description: 跳转至查看汇总页面
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void viewSummaryData() {
		//render("summaryDataList.jsp");
		//根据任务分类信息，跳转到不同的处理页面
		String missionflag = getPara("missionflag");//获取任务分类  0数据类型任务   1文件类型任务
		String taskid = Db.queryStr("SELECT ID FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",getPara("missionIssueId"));
		String forwardAction =  MissionControlUtil.getIntanceof().getViewSummaryPage(taskid,missionflag);
		if(AppUtils.StringUtil(forwardAction) != null){
			forwardAction(forwardAction);
		}else{
			renderText("未找到该任务分类对应配置页！");
		}
		
		
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
		String missionflag = getPara("missionflag");
		setAttr("id",  id);
		setAttr("missionflag",  missionflag);
		render("taskImplementSituation.jsp");
	}
	
	
	/**
	 * @Title: goBack
	 * @Description: 跳转至退回页面
	 * @author Wangsq
	 * @date 2019-07-31
	 *
	 */
	public void goBack() {
		setAttr("id", getPara("id"));
		setAttr("missionName", getPara("missionName"));
		render("goBack.jsp");
	}
	
	/**
	 * @Title: saveGoBack
	 * @Description: 退回反饋
	 * @author Wangsq
	 * @date 2019-08-02
	 *
	 */
	public void saveGoBack() {
		Record user = getCurrentUser();//获取当前登录人的用户信息
		String userno = user.getStr("USER_NO");
		String id =getPara("id");//反馈的任务ID
		JSONObject json = getJsonObj("data");
		String missionRemark = json.getString("mission_remark");//退回说明
		String messageType = json.getString("message_type");//通知方式
		String message_id = json.getString("message_id");//模板ID
		//String missionName = json.getString("missionName");//退回的任务名称
		String sql = "";
		String updateSql = "";
		int flag = 0;
		//更新待处理任务表的状态是已退回的
		updateSql = "UPDATE YYGL_MISSION_USER SET USER_MISSION_STATUS = '04' WHERE ID = ?";
		//向反馈往来记录表中插入一条数据
		sql = "insert into yygl_mission_dealings(id,user_mission_id,action_type,action_user_no,action_date,action_remark) values('"+AppUtils.getStringSeq()+"',"
				+ "'"+id+"','1','"+userno+"','"+DateTimeUtil.getTime()+"','"+missionRemark+"')";
		flag = Db.update(updateSql,id);
		flag += Db.update(sql);
		
		renderText(flag+"");
	}
	
	
	/**
	 * @Title: downloadTaskList
	 * @Description: 下载我发起的任务列表
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void downloadTaskList() {
		getList();//调用查询列表的方法
		List<Record> list = getAttr("datas");//获取列表的list 
		String[] headers = getPara("headers","").split(regex);
		String[] columns = getPara("columns","").split(regex);
		String fileName = "";
		try {
		    fileName = new String(("我发起的任务列表"+System.currentTimeMillis()+".xls").getBytes("GB2312"),"ISO_8859_1");
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		}
		ExcelRenderx er = new ExcelRenderx(fileName, columns, headers, list,getResponse());
		er.render();
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "我发起的任务".toString(), "7","我发起的任务-下载");
		log.info(getLogPoint().exit("我发起的任务-下载").toString());
		renderNull();
	}
	
	
	/**
	 * @Title: getTaskImplemSituation
	 * @Description: 查看任务执行情况列表
	 * @author Wangsq
	 * @date 2019-07-31
	 *
	 */
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
	
	/**
	 * @Title: TaskImplemSituationSql
	 * @Description: 任务接收人执行情况sql拼接
	 * @author Wangsq
	 * @date 2019-07-31
	 * 
	 * 我发起的任务列表查询任务执行情况，只查询自己发给的任务执行人，而不查询任务执行人转发的
	 *
	 */
	public Map<String, Object> TaskImplemSituationSql(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		String missionStat = getPara("missionIssueStatus"); //任务状态
		String sql = "SELECT YMU.*,SUI.NAME,  (select REMARK  from sys_param_info spi where 1=1 and spi.status='1' and spi.key='yygl_mission_status' and spi.val=YMU.USER_MISSION_STATUS) as remark";
		String extraSql = " FROM YYGL_MISSION_USER YMU LEFT JOIN SYS_USER_INFO SUI ON YMU.USER_NO = SUI.USER_NO  ";
		StringBuffer whereSql = new StringBuffer();
		List<String> sqlStr = new ArrayList<String>();
		whereSql.append(" WHERE 1=1");
		
		if (AppUtils.StringUtil(id) != null) {
			whereSql.append(" AND YMU.MISSION_ISSUE_ID = ? ");
			sqlStr.add(id.trim());
		}
		//任务状态
		if (AppUtils.StringUtil(missionStat) != null) {
			whereSql.append(" AND YMU.USER_MISSION_STATUS = ? ");
			sqlStr.add(missionStat.trim());
		}
		whereSql.append(" AND UP_MISSION_ID IS NULL");
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
		setAttr("id", getPara("id"));
		setAttr("type", getPara("type"));
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
		setAttr("ids", getPara("id"));
		setAttr("usernos", getPara("usernos"));
		render("sendUrgeMessage.jsp");
	}
	

	/**
	 * @Title: saveUrgMessage
	 * @Description: 保存催办消息
	 * @author Wangsq
	 * @date 2019-08-01
	 *
	 */
	public void saveUrgMessage() {
		String id =getPara("id");//我的待处理任务ID  可多人发送
		String usernos = getPara("usernos");
		JSONObject json = getJsonObj("data");
		String messageType = json.getString("message_type");//通知方式
		String messageContent = json.getString("message_content");//消息内容
		String[] idList = id.split(",");
		String[] usernoList = usernos.split(",");
		String sql = "";
		String updatrSql = "";
		List<String> sqlList = new ArrayList<String>();
		int flag = 0;
		//循环取出任务ID，一条条进行插入
		for (int i = 0; i < usernoList.length; i++) {
			
			sql = "INSERT INTO YYGL_MESSAGE_SENDER(ID,MESSAGE_TYPE,MESSAGE_CONTENT,MESSAGE_TITLE,MESSAGE_SENDFLAG,USERNO,"
					+ "SAVE_DATE,SEND_DATE) values('"+AppUtils.getStringSeq()+"','"+messageType+"','"+messageContent+"',"
					+ "'催办消息','1','"+usernoList[i]+"','"+DateTimeUtil.getTime()+"','')";	
			sqlList.add(sql);
		}
		if(sqlList.size() > 0) {
			Db.batch(sqlList, idList.length);//批量添加sql
			for (int j = 0; j < idList.length; j++) {
				updatrSql = "UPDATE YYGL_MISSION_USER SET SEND_MESSAGE_FLAG = '0' WHERE ID = ?";
				flag = Db.update(updatrSql,idList[j]);
			}
		} else {
			flag = 0;
		}	
		renderText(flag+"");
	}
	
	
	
	/**
	 * @Title: sendUrgeMessage
	 * @Description: 跳转至往来记录页面
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void reciprocalRecords() {
		setAttr("id", getPara("id"));
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
		String id = getPara("id");
		Map<String, Object> map = listRecord(id);
		@SuppressWarnings("unchecked")
		Page<Record> pageList = Db.paginate(pageNum, pageSize, (String) map.get("selectSql"),
				(String) map.get("extraSql"), ((List<String>) map.get("sqlStr")).toArray());

		setAttr("datas", pageList.getList());
		setAttr("total", pageList.getTotalRow());
		// 打印日志
		log.info("listRecord--pageList.getList():" + pageList.getList());
		log.info("listRecord--pageList.getTotalRow():" + pageList.getTotalRow());
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"),
				String.valueOf(getSession().getAttribute("currentMenu")), "3", "查看往来记录-查询");
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
	public Map<String, Object> listRecord(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer selectSql = new StringBuffer("select ymd.id, ymd.USER_MISSION_ID, ymd.ACTION_TYPE,(select spi.remark from sys_param_info spi where spi.val = ymd.action_type and "
				+ "spi.status = '1' and spi.key='yygl_action_type') as actiontype,ymd.ACTION_USER_NO,ymd.ACTION_DATE, (case when ymd.ACTION_REMARK = 'null' then '' else ymd.ACTION_REMARK end) as action_remark, (select sui.name from sys_user_info sui where "
				+ "sui.user_no = ymd.ACTION_USER_NO) as username ");
		String extraSql = " FROM YYGL_MISSION_DEALINGS YMD ";
		
		StringBuffer whereSql = new StringBuffer();
		List<String> sqlStr = new ArrayList<String>();
		whereSql.append("  WHERE 1=1");
		whereSql.append(" and ymd.USER_MISSION_ID = ? ");
		sqlStr.add(id.trim());
		whereSql.append(" ORDER BY YMD.ACTION_DATE DESC");
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
		setAttr("id", getPara("id"));
		setAttr("missionIssueId", getPara("missionIssueId"));
		setAttr("missionflag", getPara("missionflag"));
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
		String id = getPara("id");
		String missionIssueId = getPara("missionIssueId");
		String missionIssueStatus = getPara("missionIssueStatus");
		Map<String, Object> map = listDistributeSituation(id,missionIssueId,missionIssueStatus);
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
	 * 查看分发情况，根据任务ID以及上级Id查询任务表YYGL_MISSION_USER
	 *
	 */
	public Map<String, Object> listDistributeSituation(String id, String missionIssueId, String missionIssueStatus) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT YMU.*,SUI.NAME,  (select REMARK  from sys_param_info spi where 1=1 and spi.status='1' and spi.key='yygl_mission_status' and spi.val=YMU.USER_MISSION_STATUS) as remark";
		String extraSql = " FROM YYGL_MISSION_USER YMU LEFT JOIN SYS_USER_INFO SUI ON YMU.USER_NO = SUI.USER_NO  ";
		StringBuffer whereSql = new StringBuffer();
		List<String> sqlStr = new ArrayList<String>();
		whereSql.append(" WHERE 1=1");
		//拼接查询条件
		//根据上级Id查询该任务分发的记录
		if (AppUtils.StringUtil(missionIssueId) != null) {
			whereSql.append(" AND YMU.UP_MISSION_ID = ? ");
			sqlStr.add(id.trim());
		}
		if (AppUtils.StringUtil(missionIssueStatus) != null) {
			whereSql.append(" AND YMU.USER_MISSION_STATUS = ? ");
			sqlStr.add(missionIssueStatus.trim());
		}
		extraSql += whereSql.toString();
		map.put("selectSql", sql.toString());
		map.put("extraSql", extraSql);
		map.put("sqlStr", sqlStr);
		return map;
	}
	
	
	/**
	 * @Title: getTrackTasksList
	 * @Description: 任务跟踪查询
	 * @author Wangsq
	 * @date 2019-07-31
	 *
	 */
	public void getTrackTasksList() {
		Record user = getCurrentUser();//获取当前登录人的用户信息
		String orgId = user.getStr("ORG_ID");
		//已发起的任务数据id
		//String missionIssueId = getPara("missionIssueId");
		String searchType = getPara("searchType");
		
		String id = getPara("id");//任务Id
		String type = getPara("type"); // user_table -  用户任务查看 ，issue_table - 发起任务查看
		Record issueR = null;
		List<Record> userList = new ArrayList<Record>();
		
		if("issue_table".equals(type)){

			
			//当前节点为已发起节点，需要组装数据，建立最基础的关联关系
			//当前需要的数据情况
			StringBuffer issuesql = new StringBuffer();
			issuesql.append("select ");
			issuesql.append("t.id, ");
			issuesql.append("s.name, ");
			issuesql.append("s.feedbackcount, ");//已反馈人数
			issuesql.append("s.notfeedback, ");//未反馈人数
			issuesql.append("(case when s.notfeedback = 0 then 0 else ROUND(s.notfeedback/(taskcount)*100,2) end ) taskrate, ");//任务进度
			issuesql.append("taskcount, ");//任务执行总数
			issuesql.append("t.mission_issue_status, ");//任务状态
			issuesql.append(" '' as upid, ");
			issuesql.append(" '' as overdueflag ");
			issuesql.append("from yygl_mission_issue t left join ");
			issuesql.append("( ");
			issuesql.append("select  ");
			issuesql.append("id, ");
			if(AppUtils.StringUtil(searchType) != null && "2".equals(searchType)) {
				issuesql.append("(select orgname from yygl_mission_config ymc  LEFT JOIN sys_org_info sui ON sui.orgnum = ymc.mission_launch_ORG where ymc.id = t1.mission_config_id) name, ");
			} else if(AppUtils.StringUtil(searchType) != null && "1".equals(searchType)) {
				issuesql.append("(select name from yygl_mission_config ymc left join sys_user_info sui on sui.user_no = ymc.mission_launch_userno where ymc.id = t1.mission_config_id) name, ");
			}
			
			issuesql.append("(select count(1) from yygl_mission_user where up_mission_id is null and user_mission_status in ('02') and mission_issue_id = t1.id) feedbackcount, ");//已反馈人数
			issuesql.append("(select count(1) from yygl_mission_user where up_mission_id is null and user_mission_status in ('01','04') and mission_issue_id = t1.id) notfeedback, ");//未反馈人数
			issuesql.append("(select count(1) from yygl_mission_user where up_mission_id is null and mission_issue_id = t1.id) taskcount ");//总任务数据
			issuesql.append("from yygl_mission_issue t1  where t1.id = ?");
			issuesql.append(") s on t.id = s.id where t.id = ? ");
			
			issueR = Db.findFirst(issuesql.toString(),id,id);
			//组装待处理任务节点数据
			
			StringBuffer usersql = new StringBuffer();
			usersql.append("select  ");
			usersql.append("t.id, ");
			usersql.append("s.name, ");
			usersql.append("s.feedbackcount, ");//已反馈人数 
			usersql.append("s.notfeedback, ");//未反馈人数
			usersql.append("(case when s.notfeedback = 0 then 0 else ROUND(s.feedbackcount/(taskcount)*100,2) end ) taskrate, ");//任务进度
			usersql.append("taskcount, ");//任务执行总数
			usersql.append("t.user_mission_status, ");//任务状态
			usersql.append("s.upid, ");
			usersql.append("s.overdueflag ");
			usersql.append("from yygl_mission_user t left join ");
			usersql.append("( ");
			usersql.append("select  ");
			usersql.append(" id, user_no,");
			if(AppUtils.StringUtil(searchType) != null && "2".equals(searchType)) {
				usersql.append(" (select orgname from sys_org_info sui where sui.orgnum = t1.orgid) name, ");
			} else if(AppUtils.StringUtil(searchType) != null && "1".equals(searchType)) {
				usersql.append(" (select name from sys_user_info sui where sui.user_no = t1.user_no) name, ");
			}
			usersql.append("(select count(1) from yygl_mission_user where  user_mission_status in ('02') and up_mission_id = t1.id) feedbackcount, ");//已反馈人数
			usersql.append("(select count(1) from yygl_mission_user where  user_mission_status in ('01','04') and up_mission_id = t1.id) notfeedback, ");//未反馈人数
			usersql.append("(select count(1) from yygl_mission_user where  up_mission_id = t1.id) taskcount, ");//总任务数据
			usersql.append("(case when up_mission_id is null then mission_issue_id else up_mission_id end ) upid, ");	
			usersql.append("(case when to_char(SYSDATE, 'yyyy-mm-dd HH24:MI:SS') > MISSION_END_YMD then '1' else '0' end ) overdueflag ");	
			//是否逾期
			usersql.append("from yygl_mission_user t1 where t1.mission_issue_id = ?");
			usersql.append(") s on t.id = s.id where t.mission_issue_id = ? ");
			
			userList = Db.find(usersql.toString(),id,id);
			
		}else if("user_table".equals(type)){
			
			StringBuffer usersql1 = new StringBuffer();
			usersql1.append("select  ");
			usersql1.append("t.id, ");
			usersql1.append("s.name, ");
			usersql1.append("s.feedbackcount, ");//已反馈人数 
			usersql1.append("s.notfeedback, ");//未反馈人数
			usersql1.append("(case when s.notfeedback = 0 then 0 else ROUND(s.feedbackcount/(taskcount)*100,2) end ) taskrate, ");//任务进度
			usersql1.append("taskcount, ");//任务执行总数
			usersql1.append("t.user_mission_status, ");//任务状态
			usersql1.append("s.upid, ");
			usersql1.append("s.overdueflag ");
			usersql1.append("from yygl_mission_user t left join ");
			usersql1.append("( ");
			usersql1.append("select  ");
			usersql1.append(" id, user_no,");
			if(AppUtils.StringUtil(searchType) != null && "2".equals(searchType)) {
				usersql1.append(" (select orgname from sys_org_info sui where sui.orgnum = t1.orgid) name, ");
			} else if(AppUtils.StringUtil(searchType) != null && "1".equals(searchType)) {
				usersql1.append(" (select name from sys_user_info sui where sui.user_no = t1.user_no) name, ");
			}
			usersql1.append("(select count(1) from yygl_mission_user where  user_mission_status in ('02') and up_mission_id = t1.id) feedbackcount, ");//已反馈人数
			usersql1.append("(select count(1) from yygl_mission_user where  user_mission_status in ('01','04') and up_mission_id = t1.id) notfeedback, ");//未反馈人数
			usersql1.append("(select count(1) from yygl_mission_user where  up_mission_id = t1.id) taskcount, ");//总任务数据
			usersql1.append("(case when up_mission_id is null then mission_issue_id else up_mission_id end ) upid, ");	
			usersql1.append("(case when to_char(SYSDATE, 'yyyy-mm-dd HH24:MI:SS') > MISSION_END_YMD then '1' else '0' end ) overdueflag ");	
			//是否逾期
			usersql1.append("from yygl_mission_user t1 where t1.id = ?");
			usersql1.append(") s on t.id = s.id where t.id = ? ");
			
			
			issueR = Db.findFirst(usersql1.toString(),id,id);
			
			StringBuffer usersql = new StringBuffer();
			usersql.append("select  ");
			usersql.append("t.id, ");
			usersql.append("s.name, ");
			usersql.append("s.feedbackcount, ");//已反馈人数 
			usersql.append("s.notfeedback, ");//未反馈人数
			usersql.append("(case when s.notfeedback = 0 then 0 else ROUND(s.feedbackcount/(taskcount)*100,2) end ) taskrate, ");//任务进度
			usersql.append("taskcount, ");//任务执行总数
			usersql.append("t.user_mission_status, ");//任务状态
			usersql.append("s.upid, ");
			usersql.append("s.overdueflag ");
			usersql.append("from yygl_mission_user t left join ");
			usersql.append("( ");
			usersql.append("select  ");
			usersql.append(" id, user_no,");
			if(AppUtils.StringUtil(searchType) != null && "2".equals(searchType)) {
				usersql.append(" (select orgname from sys_org_info sui where sui.orgnum = t1.orgid) name, ");
			} else if(AppUtils.StringUtil(searchType) != null && "1".equals(searchType)) {
				usersql.append(" (select name from sys_user_info sui where sui.user_no = t1.user_no) name, ");
			}
			usersql.append("(select count(1) from yygl_mission_user where  user_mission_status in ('02') and up_mission_id = t1.id) feedbackcount, ");//已反馈人数
			usersql.append("(select count(1) from yygl_mission_user where  user_mission_status in ('01','04') and up_mission_id = t1.id) notfeedback, ");//未反馈人数
			usersql.append("(select count(1) from yygl_mission_user where  up_mission_id = t1.id) taskcount, ");//总任务数据
			usersql.append("(case when up_mission_id is null then mission_issue_id else up_mission_id end ) upid, ");	
			usersql.append("(case when to_char(SYSDATE, 'yyyy-mm-dd HH24:MI:SS') > MISSION_END_YMD then '1' else '0' end ) overdueflag ");	
			//是否逾期
			usersql.append("from yygl_mission_user t1 where t1.up_mission_id = ?");
			usersql.append(") s on t.id = s.id where t.up_mission_id = ? ");
			
			userList = Db.find(usersql.toString(),id,id);
			
			
		}
		
		
		
		
		Map<String,Object> r = issueR.getColumns();
		formatReportData(r, userList);
		JSONObject json = JSONObject.fromObject(r);
		renderJson(json.toString());
	}
	
	@SuppressWarnings({ "unchecked" })
	private Map<String,Object> formatReportData(Map<String,Object> r,List<Record> list){
		//检查该id下是否还有子节点 name,value,children
		List<Map<String, Object>> childList =  getChildList(r.get("id")+"", list);
		if(childList.size() > 0){
			r.put("children", new ArrayList<Map<String, Object>>());
			for (Map<String, Object> record : childList) {
				formatReportData(record, list);
				((ArrayList<Map<String, Object>>)r.get("children")).add(record);
			}
		}
		return r;
	}
	
	
	private List<Map<String,Object>> getChildList(String id,List<Record> list){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for (Record record : list) {
			if(id.equals(record.get("upid"))){
				result.add(record.getColumns());
				//list.remove(record);
			}
		}
		return result;
	}
	
	
	/**
	 * @Title: getTrackRate
	 * @Description: 任务跟踪查询完成比率和预期比率
	 * @author Wangsq
	 * @date 2019-08-05
	 *
	 */
	public void getTrackRate() {
		String id = getPara("id");//任务Id
		String type = getPara("type"); // user_table -  用户任务查看 ，issue_table - 发起任务查看
		String searchType =  getPara("searchType"); // 0-人员 1-机构
		StringBuffer originalSql = new StringBuffer();
		Record original = null;
		if("issue_table".equals(type)){
			
			//if("0".equals(searchType)){
				
				//查询主任务下发的人数
				originalSql.append("select distinct ");
				originalSql.append(" (select count(1) from yygl_mission_user where mission_issue_id = t.mission_issue_id and ");
			   // originalSql.append( "(to_date(feedback_ymd,'yyyy-MM-dd HH24:mi:ss') - to_date(mission_end_ymd,'yyyy-MM-dd HH24:mi:ss')) > 0 ");
			    originalSql.append( " (( ");
    		    originalSql.append( " 	user_mission_status = '02' ");
			    originalSql.append( " 	and ");
			    originalSql.append( " 	(to_date(feedback_ymd, 'yyyy-MM-dd HH24:mi:ss') - to_date(mission_end_ymd, 'yyyy-MM-dd HH24:mi:ss')) > 0 ");
			    originalSql.append( " ) or  ");
			    originalSql.append( " ( ");
			    originalSql.append( " 	user_mission_status != '02' ");
			    originalSql.append( "	and ");
			    originalSql.append( " 	(sysdate - to_date(mission_end_ymd, 'yyyy-MM-dd HH24:mi:ss')) > 0 ");
			    originalSql.append( " ) )");
			    originalSql.append(" and UP_MISSION_ID is null ) overTimeCount, "); //逾期反馈的人数
			    originalSql.append(" (select count(1) from yygl_mission_user where mission_issue_id = t.mission_issue_id  ");
			    //originalSql.append( " and (to_date(feedback_ymd,'yyyy-MM-dd HH24:mi:ss') - to_date(mission_end_ymd,'yyyy-MM-dd HH24:mi:ss')) < 0 ");
			    originalSql.append(" and user_mission_status  in ('02') "); //反馈的人数
			    originalSql.append(" and UP_MISSION_ID is null) inTimeCount, "); //反馈的人数
			    originalSql.append(" (select count(1) from yygl_mission_user where mission_issue_id = t.mission_issue_id ");
			    originalSql.append(" and UP_MISSION_ID is null) taskCount,user_mission_status "); //主任务任务总人数
			    originalSql.append(" from yygl_mission_user t WHERE mission_issue_id = ?");
			    original = Db.findFirst(originalSql.toString(),id);
				
			//}else if("1".equals(searchType)){
				
			//}
			
			
			
			
		}else if("user_table".equals(type)){
		
			//if("0".equals(searchType)){
				
				//查询主任务下发的人数
				originalSql.append("select distinct ");
				originalSql.append(" (select count(1) from yygl_mission_user where UP_MISSION_ID = t.id and ");
			   // originalSql.append( "(to_date(feedback_ymd,'yyyy-MM-dd HH24:mi:ss') - to_date(mission_end_ymd,'yyyy-MM-dd HH24:mi:ss')) > 0 ");
			    originalSql.append( " (( ");
    		    originalSql.append( " 	user_mission_status = '02' ");
			    originalSql.append( " 	and ");
			    originalSql.append( " 	(to_date(feedback_ymd, 'yyyy-MM-dd HH24:mi:ss') - to_date(mission_end_ymd, 'yyyy-MM-dd HH24:mi:ss')) > 0 ");
			    originalSql.append( " ) or  ");
			    originalSql.append( " ( ");
			    originalSql.append( " 	user_mission_status != '02' ");
			    originalSql.append( "	and ");
			    originalSql.append( " 	(sysdate - to_date(mission_end_ymd, 'yyyy-MM-dd HH24:mi:ss')) > 0 ");
			    originalSql.append( " ) )");
			    originalSql.append(" ) overTimeCount, "); //逾期反馈的人数
			    originalSql.append(" (select count(1) from yygl_mission_user where UP_MISSION_ID = t.id  ");
			    //originalSql.append( " and (to_date(feedback_ymd,'yyyy-MM-dd HH24:mi:ss') - to_date(mission_end_ymd,'yyyy-MM-dd HH24:mi:ss')) < 0 ");
			    originalSql.append(" and user_mission_status  in ('02') "); //反馈的人数
			    originalSql.append(" ) inTimeCount, "); //反馈的人数
			    originalSql.append(" (select count(1) from yygl_mission_user where UP_MISSION_ID = t.id ");
			    originalSql.append(" ) taskCount, user_mission_status "); //主任务任务总人数
			    originalSql.append(" from yygl_mission_user t WHERE id = ?");
			    original = Db.findFirst(originalSql.toString(),id);
				
			//}else if("1".equals(searchType)){
				
			//}
			
		}
		
		double overTimeCount = original.getBigDecimal("overTimeCount").doubleValue();//获取逾期反馈的人数
		double inTimeCount = original.getBigDecimal("inTimeCount").doubleValue();//获取正常反馈的人数
		double taskCount = original.getBigDecimal("taskCount").doubleValue();//获取任务总人数
		double finshRate = 0.00; //完成比率
		double overRate = 0.00;//逾期比率
		if(taskCount > 0){
			finshRate = (inTimeCount/taskCount)*100; //完成比率
			overRate = (overTimeCount/taskCount)*100;//逾期比率
		}else{
			if("02".equals(original.get("user_mission_status"))){
				finshRate = 100.00; //完成比率
				overRate = 0.00;//逾期比率
			}else{
				finshRate = 0.00; //完成比率
				overRate = 0.00;//逾期比率
			}
		}
		
		setAttr("finshRate", String.format("%.2f", finshRate));
		setAttr("overRate", String.format("%.2f", overRate));
		
		renderJson();
	}
	/**
	 * 下载打包文件
	 */
	public void downloadPackRetoaction() {
		//根据任务分类信息，跳转到不同的处理页面
		String missionflag = getPara("missionflag");//获取任务分类  0数据类型任务   1文件类型任务
		String taskid = Db.queryStr("SELECT ID FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",getPara("missionIssueId"));
		String forwardAction =  MissionControlUtil.getIntanceof().downloadPackRetoaction(taskid,missionflag);
		if(AppUtils.StringUtil(forwardAction) != null){
			forwardAction(forwardAction);
		}else{
			renderHtml("<script type='text/javascript'>parent.callback(\"未找到该任务分类对应配置！\"); </script>");
		}
	}

	

}
