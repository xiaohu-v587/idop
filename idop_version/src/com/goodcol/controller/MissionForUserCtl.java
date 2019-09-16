package com.goodcol.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.aop.Before;
import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.core.plugin.activerecord.tx.Tx;
import com.goodcol.interceptor.ManagerPowerInterceptor;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.LoggerUtil;
import com.goodcol.util.MissionControlUtil;
import com.goodcol.util.MissionUtil;
import com.goodcol.util.execl.ExcelRenderx;
import com.goodcol.util.ext.anatation.RouteBind;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @ClassName: com.goodcol.controller.MissionForUserCtl
 * @Description: 任务管理-待处理的任务
 * @author Wangsq
 * @date 2019-07-29
 *
 */
@RouteBind(path = "/missionForUser")
@Before({ ManagerPowerInterceptor.class })
public class MissionForUserCtl extends BaseCtl {

	protected Logger log = Logger.getLogger(MissionForUserCtl.class);

	@Override
	public void index() {
		render("index.jsp");
	}

	/**
	 * 
	 * @Title: getList
	 * @Description: 加载待处理的任务列表
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
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"),String.valueOf(getSession().getAttribute("currentMenu")), "3", "任务查看-查询");
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
		String orgid = user.getStr("ORG_ID"); //当前登录的机构号
		String userno = user.getStr("USER_NO");//当前登录的工号
		String roleid = user.getStr("ROLEID");//当前登录的角色
		String stime = getPara("stime"); // 任务时间开始
		String etime = getPara("etime"); // 任务时间结束
		String missionName = getPara("missionName"); // 任务名称
		String missionStat = getPara("missionIssueStatus"); //任务状态
		String missionType = getPara("missionType"); //任务类型
		String missionFlag = getPara("missionFlag"); //任务分类
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer selectSql = new StringBuffer("SELECT YMC.MISSION_NAME, YMC.MISSION_REQUIRE, YMC.MISSION_FLAG, YMU.ID,YMU.MISSION_ISSUE_ID, YMU.UP_MISSION_ID, YMU.USER_NO, YMU.MISSION_STRAT_YMD, YMU.MISSION_END_YMD, YMU.FEEDBACK_YMD, ");
		selectSql.append(" (select REMARK  from sys_param_info spi where 1=1 and spi.status='1' and spi.key='yygl_mission_status' and spi.val=YMU.USER_MISSION_STATUS) as remark, YMU.USER_MISSION_STATUS,YMC.MISSION_SIGN, YMC.MISSION_ISFEEDBACK, ");
		selectSql.append(" YMU.ORGID, YMU.ROLEID, YMU.SEND_MESSAGE_FLAG, YMU.CONFIRM_FLAG, YMU.FORWARD_FLAG,(select REMARK  from sys_param_info spi where 1=1 and spi.status='1' and spi.key='yygl_mission_flag' and spi.val=YMC.MISSION_FLAG) as mission_flag1 ");
		//selectSql.append(" (SELECT COUNT(1)  FROM YYGL_MISSION_USER YMU  WHERE YMU.MISSION_ISSUE_ID = YMI.ID) AS COUNT1, ");
		//selectSql.append(" (SELECT COUNT(1)  FROM YYGL_MISSION_USER YMU  WHERE YMU.MISSION_ISSUE_ID = YMI.ID AND YMU.USER_MISSION_STATUS = '02') AS COUNT2 ");
		String extraSql = " FROM YYGL_MISSION_USER YMU  LEFT JOIN YYGL_MISSION_ISSUE YMI ON YMU.MISSION_ISSUE_ID = YMI.ID  LEFT JOIN YYGL_MISSION_CONFIG YMC ON YMI.MISSION_CONFIG_ID = YMC.ID";
		//extraSql = extraSql + " LEFT JOIN YYGL_MISSION_ISSUE YMI ON YMU.MISSION_ISSUE_ID = YMI.ID ";
		StringBuffer whereSql = new StringBuffer();
		List<String> sqlStr = new ArrayList<String>();
		whereSql.append("  WHERE 1=1 ");
		//判定是否最高权限
		if(MissionUtil.getIntanceof().isSysManagerByUser(user)){//高权限人员
			//不限制筛选范围
		}else{
			//限定筛选自己待处理的
			whereSql.append(" and (");
			/*//如果选择的群组是只包含机构的话，那么此任务必须为需要确认，该机构下任何人都可以看到这个任务，谁确认次任务，就更新此任务到该人员下
			whereSql.append(" (YMU.ORGID = '"+orgid+"' and YMU.Confirm_Flag = '1') or");
			//或者选择的群组只包含了角色的话，那么该任务必须为需要确认，该角色下任何人都可以看到这个任务，谁确认此任务，就更新此任务到该人员下
			whereSql.append(" (YMU.Roleid = '"+roleid+"' and YMU.Confirm_Flag = '1') or");
			//或者选择的群组只包含了角色和机构的话，那么该任务必须为需要确认，该机构下的角色才可以看到这个任务，谁确认此任务，就更新此任务到该人员下
			whereSql.append(" (YMU.Roleid = '"+roleid+"' and YMU.Orgid = '"+orgid+"' and YMU.Confirm_Flag = '1') or");
			//或者选择的群组只包含了机构和人员的话，那么该任务不需要判断是否需要确认，直接下发到该机构下的该人员
			whereSql.append(" (YMU.Orgid = '"+orgid+"' and YMU.User_No = '"+userno+"') or");
			//或者选择的群组只包含了角色和人员的话，那么该任务不需要判断是否需要确认，直接下发到该角色下的该人员
			whereSql.append(" (YMU.Roleid = '"+roleid+"' and YMU.User_No = '"+userno+"') or");
			//或者选择的群组包含了机构、角色、人员的话，那么该任务不需要判断是否需要确认，直接下发到该机构下的该角色的该人员
			whereSql.append(" (YMU.Orgid = '"+orgid+"' and YMU.Roleid = '"+roleid+"' and YMU.User_No = '"+userno+"')");*/
			
			//如果选择的群组是只包含机构的话，那么此任务必须为需要确认，该机构下任何人都可以看到这个任务，谁确认次任务，就更新此任务到该人员下
			whereSql.append(" ( YMU.ORGID = '"+orgid+"' AND YMU.CONFIRM_FLAG = '1' AND YMC.MISSION_SIGN = '1' AND  YMI.GROUP_MODE = '0' ) OR");
			//或者选择的群组只包含了角色的话，那么该任务必须为需要确认，该角色下任何人都可以看到这个任务，谁确认此任务，就更新此任务到该人员下
			whereSql.append(" ( YMU.ORGID = '"+orgid+"' AND YMU.ROLEID = '"+roleid+"' AND YMU.CONFIRM_FLAG = '1' AND YMC.MISSION_SIGN = '1' AND  YMI.GROUP_MODE in ( '2','3' ) ) OR");
			//或者选择的群组只包含了角色和机构的话，那么该任务必须为需要确认，该机构下的角色才可以看到这个任务，谁确认此任务，就更新此任务到该人员下
			//whereSql.append(" ( YMU.ORGID = '"+orgid+"' AND YMU.ROLEID = '"+roleid+"' AND YMU.CONFIRM_FLAG = '1' AND YMC.MISSION_SIGN = '1' AND  YMI.GROUP_MODE = '3' ) OR");
			//或者选择的群组只包含了机构和人员的话，那么该任务不需要判断是否需要确认，直接下发到该机构下的该人员，不分配角色？
			whereSql.append(" ( YMU.ORGID = '"+orgid+"' AND YMU.USER_NO = '"+userno+"' AND  YMI.GROUP_MODE = '4' ) OR");
			//或者选择的群组只包含了角色和人员的话，那么该任务不需要判断是否需要确认，直接下发到该机构角色下的该人员
			//whereSql.append(" ( YMU.ORGID = '"+orgid+"' AND YMU.ROLEID = '"+roleid+"' AND YMU.USER_NO = '"+userno+"' AND  YMI.GROUP_MODE = '5' ) OR");
			//或者选择的群组包含了机构、角色、人员的话，那么该任务不需要判断是否需要确认，直接下发到该机构下的该角色的该人员
			whereSql.append(" ( YMU.ORGID = '"+orgid+"' AND YMU.ROLEID = '"+roleid+"' AND YMU.USER_NO = '"+userno+"'  )   ");
			
			
			whereSql.append(" )");
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
			whereSql.append(" AND YMC.MISSION_FLAG= ? ");
			sqlStr.add(missionFlag);
		}
		whereSql.append(" AND YMU.USER_MISSION_STATUS in ('01','04') ORDER BY YMU.MISSION_STRAT_YMD DESC ");
		extraSql += whereSql.toString();
		map.put("selectSql", selectSql.toString());
		map.put("extraSql", extraSql);
		map.put("sqlStr", sqlStr);
		return map;
	}
	
	
	
	/**
	 * @Title: forwardTask
	 * @Description: 跳转至转发页面
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void forwardTask() {
		setAttr("id", getPara("id"));
		setAttr("missionIssueId", getPara("missionIssueId"));
		setAttr("missionName", getPara("missionName"));
		render("forwardTask.jsp");
	}
	
	
	/**
	 * 
	 * @Title: save
	 * @Description: 保存转发的任务
	 * @author Wangsq
	 * @date 2019-07-29
	 *
	 */
	@Before(Tx.class)
	public void save() {
		String id = getPara("id");//我的任务ID
		String missionIssueId =getPara("missionIssueId");//我的任务下发ID
		String missionName = getPara("missionName");//转发的任务名称
		String mission_deadline = getPara("mission_deadline");//完成天数
		String mission_deadline_hh = getPara("mission_deadline_hh");//完成小时
		String mission_deadline_mm = getPara("mission_deadline_mm");//完成分钟
		String message_type = getPara("message_type");   //通知方式
		String mission_remark = getPara("mission_remark");//转发说明 
		String messageId = getPara("message_id");//消息通知模板Id，后期扩展使用，目前只有一种模板
		String items = getPara("items");
		JSONArray itemArr =  JSONArray.fromObject(items);
		String endDate = getMissionEndYmd1(mission_deadline,mission_deadline_hh,mission_deadline_mm);//任务结束日期
		String sql = "";//声明插入待处理表的sql
		String messageSql = "";//声明插入消息调度表的Sql
		String tempContent = "";//消息模板内容
		String tempTitle = "";//消息模板标题
		int flag = 0;
		
		//根消息通知模板ID，查询出对应的模板  根据模板ID查询对应的消息体，目前只有一种，所以直接写死
		//String tempSql = "select * from 模板主题表（尚未创建） where id = ?";
		String tempSql = "select * from yygl_message_info where id = ?";
		List<Record> tempRecord = Db.find(tempSql,messageId);
		if(tempRecord.size() > 0) {
			tempContent = tempRecord.get(0).getStr("template_content");
			tempContent = tempContent.replace("taskName", missionName);
			tempTitle = tempRecord.get(0).getStr("template_title");
		}
		
		JSONObject user = new JSONObject();
		//循环接收人list，转发任务，批量存入待处理表
		for (int i = 0,len = itemArr.size(); i < len; i++) {
			user = itemArr.getJSONObject(i);
			sql = "INSERT INTO YYGL_MISSION_USER(ID,MISSION_ISSUE_ID,UP_MISSION_ID,USER_NO,MISSION_STRAT_YMD,MISSION_END_YMD,FEEDBACK_YMD,"
					+ "USER_MISSION_STATUS,ORGID,ROLEID,SEND_MESSAGE_FLAG,CONFIRM_FLAG,FORWARD_FLAG,USER_MISSION_REMARK) values('"+AppUtils.getStringSeq()+"','"+missionIssueId+"',"
							+ "'"+id+"','"+user.getString("user_no")+"','"+DateTimeUtil.getTime()+"','"+endDate+"','','01','"+user.getString("orgnum")+"','"+user.getString("role_id")+"','1','1','1','"+mission_remark+"')";
			
			messageSql = "INSERT INTO YYGL_MESSAGE_SENDER(ID,MESSAGE_TYPE,MESSAGE_CONTENT,MESSAGE_TITLE,MESSAGE_SENDFLAG,USERNO,SAVE_DATE,SEND_DATE) VALUES"
					+ "('"+AppUtils.getStringSeq()+"','"+message_type+"','"+tempContent+"','"+tempTitle+"','0','"+user.getString("user_no")+"','"+DateTimeUtil.getTime()+"','')";
			
			flag = Db.update(sql);
			
			flag += Db.update(messageSql);
			
		}
		flag += Db.update("update YYGL_MISSION_USER set FORWARD_FLAG='0' where id=?",id);
		renderText(flag+"");
	}
	
	
	/**
	 * @Title: revokeTasks
	 * @Description: 收回转发任务
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void recoverTasks() {
		String upMissionId = getPara("id");
		int flag = 0;
		//检查该任务是否还有任务未转发
		BigDecimal bd = Db.queryBigDecimal("SELECT COUNT(1) FROM YYGL_MISSION_USER WHERE UP_MISSION_ID IN ( SELECT id FROM YYGL_MISSION_USER WHERE UP_MISSION_ID = ? )",upMissionId);
		if(bd != null && bd.intValue() >0 ){
			flag = -2; //该任务已被下级转发，无法收回
		}else{
			//该任务接收人没有任务接收人，此时可以进行收回动作，无论该任务是否已经有接收人处理了，不保留已处理结果数据，处理留痕？
			//此处不验证任务是否可以收回,前端控制？
			bd = Db.queryBigDecimal("SELECT COUNT(1) FROM YYGL_MISSION_USER WHERE UP_MISSION_ID  = ? ",upMissionId);
			if(bd != null && bd.intValue() > 0){
				try {
					flag = Db.update("DELETE FROM YYGL_MISSION_USER WHERE UP_MISSION_ID = ?", upMissionId);
					flag += Db.update("UPDATE YYGL_MISSION_USER SET FORWARD_FLAG =  '1' WHERE ID = ?", upMissionId);
				} catch (Exception e) {
					flag = -1;
					log.error("待处理任务-收回转发任务-收回异常", e);
				}
			}else{
				flag = -3;//该任务未转发给他人或者已收回任务
			}
		}
		
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "收回任务", "4", "收回转发任务");
		renderText(String.valueOf(flag));
	}
	
	
	/**
	 * @Title: signTasks
	 * @Description: 签收任务
	 * @author Wangsq
	 * @date 2019-08-02
	 *
	 */
	public void signTasks() {
		Record user = getCurrentUser();
		String userno = user.getStr("USER_NO");
		String orgid = user.getStr("ORG_ID");
		String roleid = user.getStr("ROLEID");
		String id = getPara("id");//待处理任务Id
		int flag = 0;
		try {
			//根据任务ID修改该条任务的确认标识为0（已确认）
			flag = Db.update("UPDATE YYGL_MISSION_USER SET CONFIRM_FLAG =  '0',CONFIRM_DATE = ?,USER_NO = ?,ORGID = ?,ROLEID = ? WHERE ID = ?",DateTimeUtil.getTime(),userno,orgid,roleid, id);
		} catch (Exception e) {
			flag = -1;
			log.error("待处理任务-签收任务-签收异常", e);
		}
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "签收任务", "4", "签收任务");
		renderText(String.valueOf(flag));
	}
	
	
	/**
	 * @Title: signTasks
	 * @Description: 确认签收任务
	 * @author Wangsq
	 * @date 2019-08-11
	 *
	 */
	public void confirmTasks() {
		String id = getPara("id");//待处理任务Id
		int flag = 0;
		try {
			//根据任务ID修改该条任务的确认标识为0（已确认）
			flag = Db.update("UPDATE YYGL_MISSION_USER SET CONFIRM_FLAG =  '0',CONFIRM_DATE = ? WHERE ID = ?",DateTimeUtil.getTime(), id);
		} catch (Exception e) {
			flag = -1;
			log.error("待处理任务-签收任务-签收异常", e);
		}
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "签收任务", "4", "签收任务");
		renderText(String.valueOf(flag));
	}
	
	
	/**
	 * @Title: notFeedBackTasks
	 * @Description: 无需反馈任务
	 * @author Wangsq
	 * @date 2019-08-03
	 *
	 */
	public void notFeedBackTasks() {
		String id = getPara("id");//待处理任务Id
		int flag = 0;
		try {
			//根据任务ID修改该条任务的无需反馈，并且修改该任务状态为已提交
			//无需反馈：无需反馈的任务就是不需要反馈任务数据，只需要点击无需反馈，确认任务即可
			flag = Db.update("UPDATE YYGL_MISSION_USER SET feedback_FLAG =  '0' WHERE ID = ?", id);
			flag += Db.update("UPDATE YYGL_MISSION_USER SET USER_MISSION_STATUS =  '02' WHERE ID = ?", id);
		} catch (Exception e) {
			flag = -1;
			log.error("待处理任务-无需反馈-确认异常", e);
		}
		// 记录操作日志
		LoggerUtil.getIntanceof().saveLogger(getCurrentUser().getStr("USER_NO"), "无需反馈", "4", "无需反馈");
		renderText(String.valueOf(flag));
	}
	
	
	/**
	 * @Title: noticeContent
	 * @Description: 跳转至发送消息内容页面
	 * @author Wangsq
	 * @date 2019-07-30
	 *
	 */
	public void noticeContent() {
		setAttr("id", getPara("id"));
		setAttr("missionIssueId", getPara("missionIssueId"));
		render("noticeContent.jsp");
	}
	
	
	/**
	 * @Title: retoaction
	 * @Description: 跳转至任务填报页面
	 * @author Wangsq
	 * @date 2019-07-30
	 *
	 */
	public void retoaction() {
		//根据任务分类信息，跳转到不同的处理页面
		//可根据任务类型打开任务模板页
		String missionflag = getPara("missionflag");//获取到任务分类，0数据类型  1文件类型
		String taskid = Db.queryStr("SELECT ID FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",getPara("missionIssueId"));
		//if(missionflag.equals("0")) {
		String forwardAction =  MissionControlUtil.getIntanceof().getRetoactionPage(taskid,missionflag);
		if(AppUtils.StringUtil(forwardAction) != null){
			forwardAction(forwardAction);
		}else{
			renderText("未找到该任务分类对应配置页！");
		}
	}
	
	/**
	 * @Title: retoactionTsak
	 * @Description: 保存反馈数据
	 * @author Wangsq
	 * @date 2019-07-30
	 *
	 */
	public void retoactionTsak() {
		int flag = 0;
		String type = getPara("type");//任务分类
		String userMissionId = getPara("userMissionId");//任务Id
		/**
		 * 如果任务分类是0，代表是数据反馈类型
		 * 1代表文件反馈类型
		 * 其他类型根据不同的业务自己去扩展，这里只展示数据反馈类型和文件反馈类型
		 */
		if(type.equals("0")) {
			//flag=Db.find("插入语句");
			flag = 1;
		} else if(type.equals("1")) {
			
		} else {
			
		}
		renderText(flag+"");
	}
	
	
	
	/**
	 * @Title: viewSummaryData
	 * @Description: 跳转至查看汇总页面
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void viewSummaryData() {
		String missionflag = getPara("missionflag");
		//根据任务分类信息，跳转到不同的处理页面
		//可根据任务类型打开任务模板页
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
		setAttr("id",  getPara("id"));
		setAttr("upMissionId",getPara("upMissionId"));
		setAttr("missionflag",getPara("missionflag"));
		setAttr("type",getPara("type"));
		render("taskImplementSituation.jsp");
	}
	
	
	/**
	 * @Title: downloadTaskList
	 * @Description: 下载我发起的任务列表
	 * @author Wangsq
	 * @date 2019-07-26
	 *
	 */
	public void downloadTaskList() {
		getList();
		List<Record> list = getAttr("datas");
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
	 * @Description: 任务接收人执行情况列表
	 * @author Wangsq
	 * @date 2019-08-02
	 *
	 */
	public void getTaskImplemSituation() {
		// 获取查询参数
		int pageNum = getParaToInt("pageIndex") + 1;
		int pageSize = getParaToInt("pageSize", 10);
		String id = getPara("id");
		String upMissionId = getPara("upMissionId");
		Map<String, Object> map = TaskImplemSituationSql(id,upMissionId);
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
	 * @date 2019-08-02
	 *
	 */
	public Map<String, Object> TaskImplemSituationSql(String id,String upMissionId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT YMU.*,SUI.NAME,  (select REMARK  from sys_param_info spi where 1=1 and spi.status='1' and spi.key='yygl_mission_status' and spi.val=YMU.USER_MISSION_STATUS) as remark";
		String extraSql = " FROM YYGL_MISSION_USER YMU LEFT JOIN SYS_USER_INFO SUI ON YMU.USER_NO = SUI.USER_NO  ";
		StringBuffer whereSql = new StringBuffer();
		List<String> sqlStr = new ArrayList<String>();
		whereSql.append(" WHERE 1=1");
		
		if (AppUtils.StringUtil(id) != null) {
			whereSql.append(" AND YMU.MISSION_ISSUE_ID = ? ");
			sqlStr.add(id.trim());
		}
		
		if (AppUtils.StringUtil(upMissionId) != null) {
			whereSql.append(" AND YMU.UP_MISSION_ID = ? ");
			sqlStr.add(upMissionId.trim());
		}
		extraSql += whereSql.toString();
		map.put("selectSql", sql.toString());
		map.put("extraSql", extraSql);
		map.put("sqlStr", sqlStr);
		return map;
	}
	
	
	/**
	 * @Title: getTrackRate
	 * @Description: 任务跟踪查询完成比率和预期比率
	 * @author Wangsq
	 * @date 2019-08-05
	 *
	 */
	public void getTrackRate() {
		String missionIssueId = getPara("missionIssueId");//任务Id
		StringBuffer originalSql = new StringBuffer();
		//查询主任务下发的人数
		originalSql.append("select distinct ");
		originalSql.append(" (select count(1) from yygl_mission_user where mission_issue_id = ? and ");
	    originalSql.append( "(to_date(feedback_ymd,'yyyy-MM-dd HH24:mi:ss') - to_date(mission_end_ymd,'yyyy-MM-dd HH24:mi:ss')) > 0 ");
	    originalSql.append(" and UP_MISSION_ID is not null) overTimeCount, "); //逾期反馈的人数
	    originalSql.append(" (select count(1) from yygl_mission_user where mission_issue_id = ? and ");
	    originalSql.append( "(to_date(feedback_ymd,'yyyy-MM-dd HH24:mi:ss') - to_date(mission_end_ymd,'yyyy-MM-dd HH24:mi:ss')) < 0 ");
	    originalSql.append(" and UP_MISSION_ID is not null) inTimeCount, "); //正常反馈的人数
	    originalSql.append(" (select count(1) from yygl_mission_user where mission_issue_id = ? and ");
	    originalSql.append(" and UP_MISSION_ID is not null) taskCount "); //主任务任务总人数
	    originalSql.append(" from yygl_mission_user WHERE mission_issue_id = ?");
		List<Record> originalList = Db.find(originalSql.toString(),missionIssueId,missionIssueId,missionIssueId,missionIssueId);
		int overTimeCount = originalList.get(0).getBigDecimal("overTimeCount").intValue();//获取逾期反馈的人数
		int inTimeCount = originalList.get(0).getBigDecimal("inTimeCount").intValue();//获取正常反馈的人数
		int taskCount = originalList.get(0).getBigDecimal("taskCount").intValue();//获取任务总人数
		String finshRate = String.valueOf(((overTimeCount + inTimeCount)/taskCount)); //完成比率
		String overRate = String.valueOf(overTimeCount/taskCount);//逾期比率
		setAttr("finshRate", finshRate);
		setAttr("overRate", overRate);
		renderJson();
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
	
	
	//取任务结束时间计算不去后台读取
	public String getMissionEndYmd1(String MISSION_DEADLINE,String MISSION_DEADLINE_HH,String MISSION_DEADLINE_MM){
		
		//从当前时间点开始计算多少工作日后的时间点
		String date = DateTimeUtil.getYesterDayTime(Integer.parseInt("-"+MISSION_DEADLINE));
		if(AppUtils.StringUtil(MISSION_DEADLINE_HH)!=null && AppUtils.StringUtil(MISSION_DEADLINE_MM)!=null){
			date+=" "+MISSION_DEADLINE_HH+":"+MISSION_DEADLINE_MM+":00";
		}else if(AppUtils.StringUtil(MISSION_DEADLINE_HH)!=null && AppUtils.StringUtil(MISSION_DEADLINE_MM)==null){
			date+=" "+MISSION_DEADLINE_HH+":00:00";
		}else{
			date+=" 23:59:59";
		}
		return date;
	}
	
	
	//打开查看反馈页面
	public void retoactionView(){
		String missionflag = getPara("missionflag");
		String taskid = Db.queryStr("SELECT ID FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",getPara("missionIssueId"));
		String forwardAction =  MissionControlUtil.getIntanceof().getRetoactionViewPage(taskid, missionflag);
		if(AppUtils.StringUtil(forwardAction) != null){
			forwardAction(forwardAction);
		}else{
			renderText("未找到该任务分类对应配置页！");
		}
	}
	
	//检查转发的任务是否都已反馈
	public void isOpenRetoactionView(){
		//检查任务是否已转发，转发后任务是否都已收回
		String id = getPara("id");
		String forward_flag = getPara("forward_flag");
		String flag = "0";
		if("0".equals(forward_flag)){//已转发
			//检查是否还存在未完成情况
			BigDecimal bd =  Db.queryBigDecimal("select count(1) from YYGL_MISSION_USER  where UP_MISSION_ID = ? and USER_MISSION_STATUS != '02' ",id);
			if(bd != null && bd.intValue() > 0){
				flag = "-1";
			}
		}else{//未转发
			
		}
		renderText(flag);
	}
	
	
	public void downloadTaskTemplet(){
		String missionflag = getPara("missionflag","");
		String taskid = Db.queryStr("SELECT ID FROM YYGL_MISSION_CONFIG C WHERE C.ID  =(SELECT MISSION_CONFIG_ID FROM YYGL_MISSION_ISSUE I WHERE I.ID = ? )",getPara("missionIssueId"));
		String forwardAction =  MissionControlUtil.getIntanceof().downloadTaskTemplet(taskid);
		if(AppUtils.StringUtil(forwardAction) != null){
			forwardAction(forwardAction);
		}else{
			renderText("未找到该任务分类对应配置页！");
		}
	}
}
