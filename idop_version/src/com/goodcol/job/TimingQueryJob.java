/**
 * 
 */
package com.goodcol.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author
 * 
 */
public class TimingQueryJob extends OuartzJob {
	private static Logger log=Logger.getLogger(TimingQueryJob.class);
	
	//跑批时间测试
	private static String excuTime = new SimpleDateFormat("yyyyMM").format(new Date());
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		
		try {
			String lastMonthFirstDay = DateTimeUtil.getLastMonthFirstDay();
			String lastMonthEndDay = DateTimeUtil.getLastMonthEndDay();
			String Sql = " select 1 from dual";
			Db.use("default").findFirst(Sql);
		} catch (Exception e) {
			error();
			log.error("定时任务执行失败：" + e);
		}
	}

}
