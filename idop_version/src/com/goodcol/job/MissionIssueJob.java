package com.goodcol.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.MissionControlUtil;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;


public class MissionIssueJob extends OuartzJob {
	@Override
	public void execute(JobExecutionContext arg0) {
		
		List<Record> taskList = new ArrayList<Record>(); ;
		
		String day = DateTimeUtil.getDayOfMonthYmd();
		
		//String quare = DateTimeUtil.getQuarterOfYear();
		
		int dayQuary = DateTimeUtil.getIQuarterOfYear(null);
		
		
		String month =  DateTimeUtil.getMonthOfYear();
		//周
		if ( "星期一".equals(DateTimeUtil.getDayOfWeek()) ) {
			// 如果第二天是星期一，并且该任务是周期任务+每周执行，则当天的任务需要下发
			List<Record>  taskList_t =  Db.find("SELECT * FROM YYGL_MISSION_CONFIG WHERE MISSION_TYPE = '0' AND DELETE_FLAG  = '1'  AND MISSION_ACTIVITI = '0' AND MISSION_LOOP_CYCEL = '1' ");
			taskList.addAll(taskList_t);
		}
		//旬
		if ( "10".equals(day) || "20".equals(day) || "30".equals(day) || ("2".equals(month) &&  ("28".equals(day) || "29".equals(day) )) ) {
			// 如果第二天是10号、20号、30号、2月28，并且该任务是周期任务+每旬，则当天的任务需要下发
			List<Record>  taskList_t =  Db.find("SELECT * FROM YYGL_MISSION_CONFIG WHERE MISSION_TYPE = '0' AND DELETE_FLAG  = '1'  AND MISSION_ACTIVITI = '0' AND MISSION_LOOP_CYCEL = '2' ");
			taskList.addAll(taskList_t);
		}
		//半月
		if("01".equals(day) || "15".equals(day)) {
			List<Record>  taskList_t =  Db.find("SELECT * FROM YYGL_MISSION_CONFIG WHERE MISSION_TYPE = '0' AND DELETE_FLAG  = '1'  AND MISSION_ACTIVITI = '0' AND MISSION_LOOP_CYCEL = '3' ");
			taskList.addAll(taskList_t);
		} 
		//月
		if("01".equals(day)) {
			List<Record>  taskList_t =  Db.find("SELECT * FROM YYGL_MISSION_CONFIG WHERE MISSION_TYPE = '0' AND DELETE_FLAG  = '1'  AND MISSION_ACTIVITI = '0' AND MISSION_LOOP_CYCEL = '4' ");
			taskList.addAll(taskList_t);
		} 
		//季度
		if(dayQuary == 1 ) {
			List<Record>  taskList_t =  Db.find("SELECT * FROM YYGL_MISSION_CONFIG WHERE MISSION_TYPE = '0' AND DELETE_FLAG  = '1'  AND MISSION_ACTIVITI = '0' AND MISSION_LOOP_CYCEL = '5' ");
			taskList.addAll(taskList_t);
		} 
		
		for (Record record : taskList) {
			MissionControlUtil.getIntanceof().releaseMission(record);
		}
		
	}

}
