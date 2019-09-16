package com.goodcol.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

public class DopHrSynJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// -----开始时间-----
		String start_time = "";
		// -----结束时间-----
		String end_time = "";
		String incflg = "人资同步";
		// 记录数
		int record_size = 0;
		// 跑批状态
		int run_status = 0;
		try {
			start_time = BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			String updatesql = "update sys_user_info set hr_syn = '0' where hr_syn = '1' and to_date(syn_date,'yyyy-mm-dd hh24:mi:ss') <= to_date(?, 'yyyy-mm-dd hh24:mi:ss')";
			record_size = Db.update(updatesql,start_time);
			end_time = BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
			run_status = 1;
			success();
		} catch (Exception e) {
			error();
			run_status = 2;
			e.printStackTrace();
		} finally {
			Db.use("default")
					.update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS) values(?,?,?,?,?,?)",
							new Object[] { AppUtils.getStringSeq(), start_time,
									end_time, record_size, incflg, run_status });
		}
	}

}
