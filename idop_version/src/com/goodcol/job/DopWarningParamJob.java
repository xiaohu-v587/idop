package com.goodcol.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class DopWarningParamJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "预警参数表Oracle至gbase";
		//记录数
		int record_size = 0;
		//跑批状态
		int run_status = 0;
		List<Record> warningList = null;
		try{
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			warningList = Db.use("default").find("select * from dop_warning_param");
			if(warningList != null && !warningList.isEmpty()){
				record_size = warningList.size();
				//共插入27个字段
				String columns = "warning_code,warning_name,warning_dimension,busi_module,warning_type_code,warning_level,proc_level,proc_rate,threshold_type,"
						+"initial_x,initial_y,is_manual_indentify,check_level,is_confirm,first_detail,first_detail_code,second_detail,second_detail_code,"
						+"warning_eval,is_key_warning,remark,initial_z,is_use,is_key_dxtz,is_key_jsf,message_org,message_person";
				String insertSql = "insert into dop_warning_param ("+columns+")" +
						"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				String deleteSql = "delete from dop_warning_param where 1=1";
				Db.use("gbase").update(deleteSql);
				Db.use("gbase").batch(insertSql, columns, warningList, warningList.size());
			}
			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status=1;
			success();
		}catch(Exception e){
			error();
			run_status=2;
			e.printStackTrace();
		}finally{
			Db.use("default").update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS) values(?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status});
		}
	}
}
