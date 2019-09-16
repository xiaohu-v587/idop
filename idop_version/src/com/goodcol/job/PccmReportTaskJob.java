package com.goodcol.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;
import com.goodcol.util.zxgldbutil.ReportTaskDBUtil;
/**
 * IDOP 定时创建报表
 * @author idop
 *
 */
public class PccmReportTaskJob extends OuartzJob {

	ReportTaskDBUtil dbUtil = new ReportTaskDBUtil();
	private static final String DEFAULE = "default";

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
		String end_time = "";
		//跑批状态
		int run_status = 0;
		//记录数
		int record_size = 0;
		try{
			
			String sql = "select * from pccm_report_task t where t.task_startdate is not null and  t.task_frequency!='0'";
			List<Record> rlist = Db.find(sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date now = new Date();
			String nowTime =sdf.format(now);
			String frequence = "";
			String date = "";
			Date datetime = null;
			String lsdata = "";
			Date lsdate = null;
			Calendar cal = Calendar.getInstance();
			Calendar calstart = Calendar.getInstance();
			Calendar calend = Calendar.getInstance();
			
			for (Record r : rlist){
				record_size = rlist.size();
				frequence = r.getStr("task_frequency");
				date = r.getStr("task_startdate");
				date = date.substring(0,8);
				datetime = sdf.parse(date);
				cal.setTime(datetime);
				if("1".equals(frequence)){//每周
					cal.add(Calendar.DATE, 7);
					calstart.setTime(sdf.parse(r.getStr("task_startdate").substring(0,8)));
					calstart.add(Calendar.DATE, 7);
					if(StringUtils.isNotEmpty(r.getStr("task_enddate"))){
						calend.setTime(sdf.parse(r.getStr("task_enddate").substring(0,8)));
						calend.add(Calendar.DATE, 7);
					}
				}else if("2".equals(frequence)){//每月
					cal.add(Calendar.MONTH, 1);
					calstart.setTime(sdf.parse(r.getStr("task_startdate").substring(0,8)));
					calstart.add(Calendar.MONTH, 1);
					if(StringUtils.isNotEmpty(r.getStr("task_enddate"))){
						calend.setTime(sdf.parse(r.getStr("task_enddate").substring(0,8)));
						calend.add(Calendar.MONTH, 1);
					}
				}else if("3".equals(frequence)){//每季
					cal.add(Calendar.MONTH, 3);
					calstart.setTime(sdf.parse(r.getStr("task_startdate").substring(0,8)));
					calstart.add(Calendar.MONTH, 3);
					if(StringUtils.isNotEmpty(r.getStr("task_enddate"))){
						calend.setTime(sdf.parse(r.getStr("task_enddate").substring(0,8)));
						calend.add(Calendar.MONTH, 3);
					}
				}else if("4".equals(frequence)){//每年
					cal.add(Calendar.YEAR, 1);
					calstart.setTime(sdf.parse(r.getStr("task_startdate").substring(0,8)));
					calstart.add(Calendar.YEAR, 1);
					if(StringUtils.isNotEmpty(r.getStr("task_enddate"))){
						calend.setTime(sdf.parse(r.getStr("task_enddate").substring(0,8)));
						calend.add(Calendar.YEAR, 1);
					}
				}
				lsdate = cal.getTime();
				lsdata = sdf.format(lsdate);
				if(lsdata.equals(nowTime)){
					if(StringUtils.isEmpty(calend.toString())){
						sendtask(r,sdf.format(calstart.getTime()),"");
					}else{
						sendtask(r,sdf.format(calstart.getTime()),sdf.format(calend.getTime()));
					}
				}
			}
			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status=1;
			success();
			
		}catch(Exception e){
			error();
			e.printStackTrace();
		}finally{
			Db.use("default").update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS) values(?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,"定时填报",run_status});
		}
	}
	public void sendtask(Record r,String starttime,String endtime){
		String reportId = AppUtils.getStringSeq();
		String fillway = r.getStr("task_fillway");
		String tableName = r.getStr("task_table_name");
		String filePath = r.getStr("task_attachments");
		String task_name = r.getStr("task_name");
		task_name = task_name.split("_")[0]+"_"+starttime;
		//String reportId = r.getStr("id");
		String groupId = r.getStr("group_id");
		if ("1".equals(fillway)) {
			dbUtil.writeExcel(filePath, tableName);
		}
		String tableNames = "report_"+starttime;
		String sql = "create table "+tableNames+" as select * from "+tableName+" where rownum<1";
		Db.use(DEFAULE).update(sql);
		String itemUserIds = r.getStr("usrs");
		String itemOrgIds = r.getStr("orgs");
		Db.use(DEFAULE).update("insert into pccm_report_task (id, task_name, task_enddate, task_status, task_detail, table_head, task_frequency, task_fillway, org_field, task_issuer_id,"
				+ " task_table_name, task_attachments, fields_location, serial_number, group_id, task_create_date, task_file_name, busi_module,usrs,orgs) "
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { reportId, task_name, endtime, "0", r.getStr("task_detail"), r.getStr("table_head"), r.getStr("task_frequency"), fillway, r.getStr("org_field"),
						r.getStr("task_issuer_id"), tableNames, filePath, r.getStr("fields_location"),r.getStr("serial_number") , groupId, DateTimeUtil.getNowDate(), r.getStr("task_file_name"),r.getStr("busi_module"),itemUserIds,itemOrgIds });
		List<String> usrs = AppUtils.StringUtil(itemUserIds) == null?null:new ArrayList<String>(Arrays.asList(itemUserIds.split(",")));
		List<String> orgs = AppUtils.StringUtil(itemOrgIds) == null?null:new ArrayList<String>(Arrays.asList(itemOrgIds.split(",")));
		String sqls = "select * from pccm_report_file t where t.report_task_id = '"+r.getStr("id")+"'"; 
		Record rs = Db.findFirst(sqls);
		if(rs!=null){
			Db.use(DEFAULE).update(" insert into pccm_report_file (file_id, report_task_id, file_url, file_name) values(?,?,?,?)",
							new Object[] { AppUtils.getStringSeq(), reportId, filePath, r.getStr("task_file_name") });
		}
		//Db.use(DEFAULE).update(" insert into pccm_report_file (file_id, report_task_id, file_url, file_name) values(?,?,?,?)",
		//		new Object[] { AppUtils.getStringSeq(), reportId, filePath, r.getStr("task_file_name") });
		if ("1".equals(fillway)) {
			dbUtil.writeExcel(filePath, tableNames);
		}
		dbUtil.allotReport(reportId, null, groupId, orgs, usrs);// 将报表接收对象插入表中
		dbUtil.updateTaskStatus(reportId, "2");
	}

}
