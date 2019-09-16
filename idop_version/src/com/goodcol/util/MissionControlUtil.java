package com.goodcol.util;
import java.lang.annotation.Documented;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;

import com.goodcol.controller.MissionDataReportCtl;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.template.expr.ast.Array;
/**
 * 主要实现目的，根据任务相关的配置读取不同的验证逻辑代码
 * 
 * @author chang
 *
 */
public class MissionControlUtil {
	private static MissionControlUtil intanceof = null;

	private MissionControlUtil() {
 
	}

	public static MissionControlUtil getIntanceof() {
		if (intanceof == null) {
			intanceof = new MissionControlUtil();
		}
		return intanceof;
	} 
	/**
	 * 保存模板逻辑
	 * // result {flag: success  / fail , message: '' ,messagecode : '0' / -1 ... }
	 * @param params
	 * @return
	 */
	public Record saveModelConfig(Record params){
		//指向数据类任务下载模板逻辑
		// result {flag: success  / fail , message: '' ,messagecode : '0' / -1 ... }
		Record result = new MissionDataReportCtl().saveModel(params);
		//指向文件类模板下载逻辑
		return result;
	}
	
	@Deprecated
	public boolean downloadTemplet(Record params,HttpServletResponse response){
		/**
		 * 数据中转逻辑根据不同模板，不同的阶段下载不同的模板信息
		 */
		//if(params.get("").equals("")){//任务配置
			//指向数据类任务下载模板逻辑
			//new MissionDataReportCtl().downloadTemplet(params,response);
			
			//指向文件类模板下载逻辑
			
		//}else if(params.get("").equals("")){//已发布任务
			
			//指向文件类模板下载逻辑
			
			//指向数据类任务下载模板逻辑
		//}else {
			//不下载模板
		//}
		return true;
	}
	
	
	public boolean releaseMission(Record params){
		/**
		 * 数据中转逻辑根据不同模板，不同的阶段下载不同的模板信息
		 */
		String tempContent = "";//消息模板内容
		String tempTitle = "";//消息模板标题
		String messageSql = "";
		/*if(params.get("").equals("")){//任务配置
			//指向数据类任务下载模板逻辑
			new MissionDataReport().releaseMission(params.getStr("id"));
		}else if(params.get("").equals("")){//已发布任务
			//指向文件类模板下载逻辑
		}else {*/
			//默认场景
			
			//查找任务配置获取任务接收人 
			//record{org_id,role_id,user_no}
		//获取任务配置数据
			Record taskparam =  Db.findById("YYGL_MISSION_CONFIG", params.get("id"));
		
			//根据群组标识，取群组配置表中读取群组配置
			String groupid = taskparam.getStr("assign_feedbank_group");
			//是否需要确认
			String mission_sign = taskparam.getStr("mission_sign");
			
			Record group  = Db.findById("pccm_group_info", groupid);
			
			//群组模式 0-机构 2-角色 3-机构和角色 4-机构和人员 5-角色和人员 6-机构角色人员
			String group_mode = group.getStr("group_mode");
			
			List<Record> userList = getUserList(taskparam);
			
			//定义声明已发起任务主表id
			String issueid = AppUtils.getStringSeq();
			
			
			//开始对数据进行下发到个人
			Record userTask = new Record();
			Record userl  = null; 
			if(userList.size() > 0) {
				//插入任务发起记录
				Record issue = new Record();
				issue.set("ID", issueid)
				.set("MISSION_CONFIG_ID", params.get("id"))
				.set("MISSION_STRAT_YMD", DateTimeUtil.getTime())
				.set("MISSION_END_YMD", getMissionEndYmd(taskparam))
				.set("MISSION_ISSUE_STATUS", "03")//03代表任务状态 进行中
				.set("GROUP_MODE", group_mode);//03代表任务状态 进行中
				//向任务表中插入任务下发记录
				Db.save("YYGL_MISSION_ISSUE", issue);
				
				for (int i = 0,len = userList.size(); i < len; i++) {
					userTask.clear(); //清空下数据下面会再次使用
					userl = userList.get(i); 
					//组装人员任务数据
					userTask.set("ID", AppUtils.getStringSeq())
					.set("MISSION_ISSUE_ID", issueid) 		//已发布的任务ID
					.set("USER_NO", userl.get("user_no")) 	//任务接收人
					.set("ORGID", userl.get("orgnum")) 		//任务接收机构
					.set("ROLEID", userl.get("role_id")) 	//任务接收人角色
					.set("MISSION_STRAT_YMD", issue.get("MISSION_STRAT_YMD")) 	//已发布的任务开始时间
					.set("MISSION_END_YMD", issue.get("MISSION_END_YMD")) 		//已发布的任务结束时间
					.set("USER_MISSION_STATUS", "01") 		//默认任务状态 01 待处理未反馈
					.set("CONFIRM_FLAG","1") 				//任务确认标识初始化 1-未确认
					.set("FORWARD_FLAG","1"); 				//转发标识 初始化 1-未转发
					
					//数据保存表中
					Db.save("YYGL_MISSION_USER", userTask);
					
					//根消息通知模板ID，查询出对应的模板  根据模板ID查询对应的消息体，目前只有一种
					String tempSql = "select * from yygl_message_info where id = ?";
					List<Record> tempRecord = Db.find(tempSql,taskparam.getStr("message_id"));//根据消息模板ID查询对应的消息体
					//屈楚萧习题内容和消息体标题
					if(tempRecord.size() > 0) {
						tempContent = tempRecord.get(0).getStr("template_content");
						tempContent = tempContent.replace("taskName", taskparam.getStr("mission_name"));
						tempTitle = tempRecord.get(0).getStr("template_title");
					}
					List<Record> usersList = new ArrayList<Record>();//根据群组查询人员
					//下发时推送消息通知，先根据选择的群组查询出下发的人员
					//如果群组只有机构组成，那么查询机构下的人员
					if("0".equals(group_mode)) {
						usersList = Db.find("select * from sys_user_org_role where orgnum in (select orgnum from PCCM_GROUP_ITEM_INFO where id = ?) ",groupid);
						
					} else if("6".equals(group_mode)) {//机构+角色+人员
						usersList = Db.find("select * from sys_user_org_role where role_id in (select role_id from PCCM_GROUP_ITEM_INFO where id = ?) "
								+ "and orgnum in (select orgnum from PCCM_GROUP_ITEM_INFO where id = ?) and user_no in (select user_no from PCCM_GROUP_ITEM_INFO "
								+ "where id = ?)",groupid,groupid,groupid);
					} else if("2".equals(group_mode)) {//角色
						usersList = Db.find("select * from sys_user_org_role where role_id in (select role_id from PCCM_GROUP_ITEM_INFO where id = ?) ",groupid);
					} else if("3".equals(group_mode)) {//机构加角色
						usersList = Db.find("select u.user_no from sys_user_org_role u where u.orgnum in (select orgnum from PCCM_GROUP_ITEM_INFO where id = ?) and u.role_id in (select role_id from PCCM_GROUP_ITEM_INFO where id = ?)  ",groupid,groupid);
					} else if("4".equals(group_mode)) {//机构加人员
						usersList = Db.find("select u.user_no from sys_user_org_role u where u.orgnum in (select orgnum from PCCM_GROUP_ITEM_INFO where id = ?) and u.user_no in (select user_no from PCCM_GROUP_ITEM_INFO where id = ?)  ",groupid,groupid);
					} else if("5".equals(group_mode)) {//角色加人员
						usersList = Db.find("select u.user_no from sys_user_org_role u where u.role_id in (select role_id from PCCM_GROUP_ITEM_INFO where id = ?) and u.user_no in (select user_no from PCCM_GROUP_ITEM_INFO where id = ?)  ",groupid,groupid);
					} else {
						
					}
					//循环用户的list，依次取出放进消息推送表
					for (int j = 0; j < usersList.size(); j++) {
						messageSql = "INSERT INTO YYGL_MESSAGE_SENDER(ID,MESSAGE_TYPE,MESSAGE_CONTENT,MESSAGE_TITLE,MESSAGE_SENDFLAG,USERNO,SAVE_DATE,SEND_DATE) VALUES"
						+ "('"+AppUtils.getStringSeq()+"','"+taskparam.getStr("message_type")+"','"+tempContent+"','"+tempTitle+"','0','"+usersList.get(j).getStr("user_no")+"','"+DateTimeUtil.getTime()+"','')";
						Db.update(messageSql);
					} 
					
				}
				return true;
			} else {
				return false;
			}
			
	}
	
	//取任务结束时间计算去后台读取配置，再进行计算
	public String getMissionEndYmd(String taskid){
		//获取任务配置数据
		Record taskparam =  Db.findById("YYGL_MISSION_CONFIG",taskid);
		return getMissionEndYmd(taskparam);
	} 
	

	//取任务结束时间计算不去后台读取
	public String getMissionEndYmd(Record param){
		String MISSION_DEADLINE  = param.getStr("MISSION_DEADLINE");
		String MISSION_DEADLINE_HH  = param.getStr("MISSION_DEADLINE_HH");
		String MISSION_DEADLINE_MM  = param.getStr("MISSION_DEADLINE_MM");
		
		//从当前时间点开始计算多少工作日后的时间点
		
		String date = null;
		Calendar cal = Calendar.getInstance();
		if(AppUtils.StringUtil(MISSION_DEADLINE) != null){
			//目前没有工作日表，暂时使用自然日
			cal.add(Calendar.DATE, Integer.parseInt(MISSION_DEADLINE));
			date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			//组装日期
			
			if(AppUtils.StringUtil(MISSION_DEADLINE_HH)!=null && AppUtils.StringUtil(MISSION_DEADLINE_MM)!=null){
				date+=" "+MISSION_DEADLINE_HH+":"+MISSION_DEADLINE_MM+":00";
			}else if(AppUtils.StringUtil(MISSION_DEADLINE_HH)!=null && AppUtils.StringUtil(MISSION_DEADLINE_MM)==null){
				date+=" "+MISSION_DEADLINE_HH+":00:00";
			}else{
				date+=" 23:59:59";
			}
			try {
				Date sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sdf);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			String MISSION_TYPE = param.getStr("MISSION_TYPE");
			String MISSION_LOOP_CYCEL = param.getStr("MISSION_LOOP_CYCEL");
			
			if("0".equals(MISSION_TYPE)){
				
				if("1".equals(MISSION_LOOP_CYCEL)){
					cal.add(Calendar.DATE, 6);
					date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				}else if("2".equals(MISSION_LOOP_CYCEL)){
					cal.add(Calendar.DATE, 9);
					date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				}else if("3".equals(MISSION_LOOP_CYCEL)){
					String day = DateTimeUtil.getDayOfMonthYmd();
					if(Integer.valueOf(day)>14){//下半月
						date = DateTimeUtil.getLastDayOfMonth();
					}else{//上半月
						cal.add(Calendar.DATE, 13);
						date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
					}
					
				}else if("4".equals(MISSION_LOOP_CYCEL)){
					date = DateTimeUtil.getLastDayOfMonth();
				}else if("5".equals(MISSION_LOOP_CYCEL)){
					date = DateTimeUtil.getLastDayOfQuarter();
				}
				
				date+=" 23:59:59";
			}else{
				date = DateTimeUtil.getTime();
			}
		}
		
		return date;
	}
	
	
	
	
	
	/**
	 * 根据任务读取任务接收人
	 * @param param
	 * @return
	 */
	public List<Record> getUserList(Record param){
		/**
		 * 暂时没考虑多机构情况，没有表啊
		 * 
		 */
		//存储人员列表内容
		List<Record> userList = new ArrayList<Record>();
		
		//根据群组标识，取群组配置表中读取群组配置
		String groupid = param.getStr("assign_feedbank_group");
		
		String mission_sign = param.getStr("mission_sign");
		
		Record group  = Db.findFirst("select * from  pccm_group_info where id=?", groupid);
		
		List<Record> items  = Db.find("SELECT *  FROM  PCCM_GROUP_ITEM_INFO WHERE ID = ?", groupid);
		//群组模式 0-机构 2-角色 3-机构和角色 4-机构和人员 5-角色和人员 6-机构角色人员
		String group_mode = group.getStr("group_mode");
		
		if("0".equals(group_mode)){
			//机构模式下，判断是否需要确认，如果无需确认，此时，发送任务给机构所有人
			if("0".equals(mission_sign)){ //无需确认
				//组合机构数据，查询机构下人员信息，目前需要指定到具体有人的机构
				StringBuffer sql = new StringBuffer();
				sql.append("select user_no,orgnum,role_id,'0' mission_sign from sys_user_org_role where orgnum in ( ");
				List<Object> paramList = new ArrayList<Object>();
				boolean isFir = true;
				for (Record item : items) {
					if(isFir){
						isFir = false;
					}else{
						sql.append(",");
					}
					sql.append("?");
					paramList.add(item.get("orgnum"));
				}
				sql.append(" ) ");
				userList =  Db.find(sql.toString(),paramList.toArray());
			}else{	//需要确认，发送给该机构，不指定具体人员和角色
				for (Record item : items) {
					userList.add(new Record().set("orgnum", item.get("orgnum")).set("user_no","").set("role_id", "").set("mission_sign", "1"));
				}
			}
		}else if("2".equals(group_mode)){ //角色
			//机构模式下，判断是否需要确认，如果无需确认，此时，发送任务给机构所有人
			if("0".equals(mission_sign)){ //无需确认
				//组合机构数据，查询机构下人员信息，目前需要指定到具体有人的机构
				StringBuffer sql = new StringBuffer();
				sql.append("select user_no,orgnum,role_id,'0' mission_sign from sys_user_org_role where role_id in ( ");
				List<Object> paramList = new ArrayList<Object>();
				boolean isFir = true;
				for (Record item : items) {
					if(isFir){
						isFir = false;
					}else{
						sql.append(",");
					}
					sql.append("?");
					paramList.add(item.get("role_id"));
				}
				sql.append(" ) ");
				userList =  Db.find(sql.toString(),paramList.toArray());
			}else{	//需要确认，发送给该机构，不指定具体人员和角色
				
				StringBuffer sql = new StringBuffer();
				sql.append("select '' user_no,orgnum,role_id,'1' mission_sign from sys_user_org_role where role_id in ( ");
				List<Object> paramList = new ArrayList<Object>();
				boolean isFir = true;
				for (Record item : items) {
					if(isFir){
						isFir = false;
					}else{
						sql.append(",");
					}
					sql.append("?");
					paramList.add(item.get("role_id"));
				}
				sql.append(" )  group by orgnum,role_id ");
				userList =  Db.find(sql.toString(),paramList.toArray());
			}
		}else if("3".equals(group_mode)){ //机构和角色
			//机构模式下，判断是否需要确认，如果无需确认，此时，发送任务给机构所有人
			
			List<Record> orgList = new ArrayList<Record>();
			List<Record> roleList = new ArrayList<Record>();
			for (Record item : items) {
				if(AppUtils.StringUtil(item.getStr("orgnum")) != null){
					orgList.add(item);
				}else if(AppUtils.StringUtil(item.getStr("role_id")) != null){
					roleList.add(item);
				}
			}
			
			if("0".equals(mission_sign)){ //无需确认
				//组合机构数据，查询机构下人员信息，目前需要指定到具体有人的机构
				StringBuffer sql = new StringBuffer();
				sql.append("select '' user_no,orgnum,role_id,'0' mission_sign from sys_user_org_role where role_id in ( ");
				List<Object> paramList = new ArrayList<Object>();
				boolean isFir = true;
				for (Record item : roleList) {
					if(isFir){
						isFir = false;
					}else{
						sql.append(",");
					}
					sql.append("?");
					paramList.add(item.get("role_id"));
				}
				sql.append(" ) ");
				
				sql.append(" and orgnum in ( ");
				isFir = true;
				for (Record item : orgList) {
					if(isFir){
						isFir = false;
					}else{
						sql.append(",");
					}
					sql.append("?");
					paramList.add(item.get("orgnum"));
				}
				sql.append(" ) ");
				
				userList =  Db.find(sql.toString(),paramList.toArray());
				
			}else{	//需要确认，发送给该机构，不指定具体人员和角色
				BigDecimal bd;
				for (Record item : orgList) {
					//检查该机构是否存在对应角色的人员？，没有对应角色的处理人员时，不予发送
					for (Record role : roleList) {
						bd = Db.queryBigDecimal("select count(1) from sys_user_org_role where orgnum = ? and role_id = ? ",item.getStr("orgnum"),role.get("role_id"));
						if(bd != null && bd.intValue() > 0){
							userList.add(new Record().set("orgnum",item.getStr("orgnum")).set("user_no","").set("role_id", role.get("role_id")).set("mission_sign", "1"));
						}
					}
				}
			}
		}else if("4".equals(group_mode)){ //机构和人员，直接发送到该机构人员名下，如果出现该人员在该机构存在多角色情况，不予处理，需要对人员信息进行标记，该机构该人员任何角色都可处理该任务
			//机构模式下，判断是否需要确认，如果无需确认，此时，发送任务给机构所有人
			if("0".equals(mission_sign)){ //无需确认
				//组合机构数据，查询机构下人员信息，目前需要指定到具体有人的机构
				for (Record item : items) {
					userList.add(new Record().set("orgnum", item.get("orgnum")).set("user_no",item.get("user_no")).set("role_id", "").set("mission_sign", "0"));
				}
			}else{	//需要确认，发送给该机构，不指定具体人员和角色
				for (Record item : items) {
					userList.add(new Record().set("orgnum", item.get("orgnum")).set("user_no",item.get("user_no")).set("role_id", "").set("mission_sign", "1"));
				}
			}
		}else if("5".equals(group_mode)){ //角色和人员，直接发送到该机构人员名下，如果出现该人员在该机构存在多角色情况，不予处理，需要对人员信息进行标记，该机构该人员任何角色都可处理该任务
			//机构模式下，判断是否需要确认，如果无需确认，此时，发送任务给机构所有人
			
			StringBuffer sql = new StringBuffer();
			sql.append("select user_no,orgnum,role_id,'"+mission_sign+"' mission_sign from sys_user_org_role where role_id in ( ");
			List<Object> paramList = new ArrayList<Object>();
			boolean isFir = true;
			for (Record item : items) {
				if(isFir){
					isFir = false;
				}else{
					sql.append(",");
				}
				sql.append("?");
				paramList.add(item.get("role_id"));
			}
			sql.append(" ) ");
			
			sql.append(" and user_no in ( ");
			isFir = true;
			for (Record item : items) {
				if(isFir){
					isFir = false;
				}else{
					sql.append(",");
				}
				sql.append("?");
				paramList.add(item.get("user_no"));
			}
			sql.append(" ) ");
			
			userList =  Db.find(sql.toString(),paramList.toArray());
		}else if("6".equals(group_mode)){ //机构和角色和人员
			//机构模式下，判断是否需要确认，如果无需确认，此时，发送任务给机构所有人
			if("0".equals(mission_sign)){ //无需确认
				//组合机构数据，查询机构下人员信息，目前需要指定到具体有人的机构
				for (Record item : items) {
					userList.add(new Record().set("orgnum", item.get("orgnum")).set("user_no",item.get("user_no")).set("role_id", item.get("role_id")).set("mission_sign", "0"));
				}
			}else{	//需要确认，发送给该机构，不指定具体人员和角色
				for (Record item : items) {
					userList.add(new Record().set("orgnum", item.get("orgnum")).set("user_no",item.get("user_no")).set("role_id", item.get("role_id")).set("mission_sign", "1"));
				}
			}
		}
		return userList;
	}
	
	protected String getParasToStringRegex(Object... paras){
		StringBuffer sql = new StringBuffer(); 
		sql.append("(");
		boolean isFirst = true;
		for (int i = 0; i < paras.length; i++) {
			if(isFirst)
				isFirst = false;
			else
				sql.append(",");
			sql.append("?");
		}
		sql.append(")");
		return sql.toString();
	}
	
	public String downloadTemplet(String taskid){
		//获取任务配置数据
		Record params =  Db.findById("YYGL_MISSION_CONFIG",taskid);
		/*if(params.get("").equals("")){//任务配置
		//指向数据类任务下载模板逻辑
		}else if(params.get("").equals("")){//已发布任务
			//指向文件类模板下载逻辑
		}else {*/
		
		
		/*}*/
		String mission_flag =  params.getStr("mission_flag"); //任务类型 0-数据类，1-文件类
		
		String action = "";
		
		if("0".equals(mission_flag)){
			action = "/missiondatareport/downloadTemplet";
		}else{
			
		}
		
		return action;
	}
	
	public String downloadTaskTemplet(String taskid){
		//获取任务配置数据
		Record params =  Db.findById("YYGL_MISSION_CONFIG",taskid);
		/*if(params.get("").equals("")){//任务配置
		//指向数据类任务下载模板逻辑
		}else if(params.get("").equals("")){//已发布任务
			//指向文件类模板下载逻辑
		}else {*/
		
		
		/*}*/
		String mission_flag =  params.getStr("mission_flag"); //任务类型 0-数据类，1-文件类
		
		String action = "";
		
		if("0".equals(mission_flag)){
			action = "/missiondatareport/downloadTaskTemplet";
		}else{
			
		}
		
		return action;
		
		
	}
	
	
	
	
	public String getAddModelPage(String taskid,String mission_flag){
		//获取任务配置数据
		//Record params =  Db.findById("YYGL_MISSION_CONFIG",taskid);
		/*if(params.get("").equals("")){//任务配置
		//指向数据类任务下载模板逻辑
		}else if(params.get("").equals("")){//已发布任务
			//指向文件类模板下载逻辑
		}else {*/
		
		
		/*}*/
		//String mission_flag =  params.getStr("mission_flag"); //任务类型 0-数据类，1-文件类
		
		String action = "";
		
		if("0".equals(mission_flag)){
			action = "/missiondatareport/addModel";
		}else{
			
		}
		
		return action;
	}
	
	/**
	 * 跳转到反馈页面
	 * @param taskid
	 * @return
	 */
	public String getRetoactionPage(String taskid, String missionflag){
		//获取任务配置数据
		Record params =  Db.findById("YYGL_MISSION_CONFIG",taskid);
		/*if(params.get("").equals("")){//任务配置
		//指向数据类任务下载模板逻辑
		}else if(params.get("").equals("")){//已发布任务
			//指向文件类模板下载逻辑
		}else {*/
		
		
		/*}*/
		if("0".equals(missionflag)){//业务数据收集类
			return "/missiondatareport/retoaction";
		} else {
			return "/missionFileReport/retoaction";
		}
		
		
	}
	
	/**
	 * 下载打包数据文件
	 * @param taskid
	 * @return
	 */
	public String downloadPackRetoaction(String taskid, String missionflag){
		//获取任务配置数据
		Record params =  Db.findById("YYGL_MISSION_CONFIG",taskid);
		/*if(params.get("").equals("")){//任务配置
		//指向数据类任务下载模板逻辑
		}else if(params.get("").equals("")){//已发布任务
			//指向文件类模板下载逻辑
		}else {*/
		
		/*}*/
		if("0".equals(missionflag)){//业务数据收集类
			return "/missiondatareport/downloadPackRetoaction";
		} else {
			return "/missionFileReport/downloadFilesToZip";
		}
		
		
	}
	
	
	
	
	/**
	 * 跳转到查看汇总页面
	 * @param taskid
	 * @return
	 */
	public String getViewSummaryPage(String taskid, String missionflag){
		//获取任务配置数据
		//Record params =  Db.findById("YYGL_MISSION_CONFIG",taskid);
		/*if(params.get("").equals("")){//任务配置
		//指向数据类任务下载模板逻辑
		}else if(params.get("").equals("")){//已发布任务
			//指向文件类模板下载逻辑
		}else {*/
		
		
		/*}*/
		//根据任务分类跳转不同页面  0--数据类型汇总  1--文件类型汇总
		if(missionflag.equals("0")) {
			return "/missiondatareport/summaryView";
		} else {
			return "/missionFileReport/summaryView";
		}
		
		
	}
	

	
	/**
	 * 跳转到查看反馈内容页面
	 * @param taskid
	 * @return
	 */
	public String getRetoactionViewPage(String taskid, String missionflag){
		//获取任务配置数据
		//Record params =  Db.findById("YYGL_MISSION_CONFIG",taskid);
		/*if(params.get("").equals("")){//任务配置
		//指向数据类任务下载模板逻辑
		}else if(params.get("").equals("")){//已发布任务
			//指向文件类模板下载逻辑
		}else {*/
		
		
		/*}*/
		if("0".equals(missionflag)) {
			return "/missiondatareport/retoactionView";
		} else {
			return "/missionFileReport/retoactionView";
		}
		
		
	}
	
	

	public Record copyModelConfig(Record param) {
		//获取业务分类标识
		String mission_flag =  param.get("mission_flag");
		Record result = new Record();
		if("0".equals(mission_flag)){//业务数据收集类
			result = new MissionDataReportCtl().copyModelConfig(param);
		}
		return result;
	} 

	/**
	 *  根据当前选中的任务中存放 接收人工号，机构号，角色组合条件
	 * @param sql
	 * @param listParam
	 * @param userTask
	 * @param column
	 * @return
	 */
	public Record getDataReportSplitSqlByUserTask(StringBuffer sql,List<Object> listParam,Record userTask,Record column){
		
		//在这里写的目的呢，是考虑后期可以把该配置放到数据库中去
		
		if("0".equals(column.get("col_break_type"))){//0-按照机构号
			
			sql.append(" AND ( "+column.getStr("col_name")+" = ? OR DATA_TYPE = '0' ) ");
			listParam.add(userTask.getStr("ORGID")); 
			
		}else if("1".equals(column.get("col_break_type"))){//1-按照机构名称
			
			sql.append(" AND ("+column.getStr("col_name")+" = (SELECT ORGNAME FROM  SYS_ORG_INFO WHERE ORGNUM = ?) OR DATA_TYPE = '0' )");
			listParam.add(userTask.getStr("ORGID"));
			
		}else if("2".equals(column.get("col_break_type"))){//2-按照角色
			
			sql.append(" AND ("+column.getStr("col_name")+" = (SELECT NAME FROM  SYS_ROLE_INFO WHERE ID = ?)  OR DATA_TYPE = '0' )");
			listParam.add(userTask.getStr("ROLEID"));
			
		}else if("3".equals(column.get("col_break_type"))){//3-按照员工号
			
			sql.append(" AND ("+column.getStr("col_name")+" = ? OR DATA_TYPE = '0' )");
			listParam.add(userTask.get("USER_NO"));
			
		}
		
		
		
		
		return null;
	}
	
}
