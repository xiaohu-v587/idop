package com.goodcol.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class DopMarkParamJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "指标参数表Oracle至gbase";
		//记录数
		int record_size = 0;
		//跑批状态
		int run_status = 0;
		List<Record> markList = null;
		try{
			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			markList = Db.use("default").find("select * from dop_mark_param");
			if(markList != null && !markList.isEmpty()){
				record_size = markList.size();
				//共插入30个字段
				String columns = "mark_code,mark_name,mark_dimension,busi_module,sub_busi_code,mark_type_code,unit," +
						"cur,value_type,proc_level,proc_rate,summary_level,display_level,first_detail,first_detail_code,second_detail,second_detail_code," +
						"mark_eval,proc_mode,mark_direct,mark_att,mark_group,mark_average_mode,mark_minmax_mode,total_type,dividend,divisor,is_key_mark,is_use,source,zb3";
				String insertSql = "insert into dop_mark_param ("+columns+")" +
						"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				String deleteSql = "delete from dop_mark_param where 1=1";
				Db.use("gbase").update(deleteSql);
				Db.use("gbase").batch(insertSql, columns, markList, markList.size());
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
