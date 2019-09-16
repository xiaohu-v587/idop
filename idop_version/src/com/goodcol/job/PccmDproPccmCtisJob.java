/**
 * 
 */
package com.goodcol.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author dinggang
 * 
 */
public class PccmDproPccmCtisJob extends OuartzJob {
	public static Logger log = Logger.getLogger(PccmDproPccmCtisJob.class);
	private static final String GBASE = "gbase";

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("----Ctis存储过程开始跑批："
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("Ctis存储过程开始跑批：" + BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		// 获取上个月份
		String v_dataDate = DateTimeUtil.getLastMonth();
		String sql = " CALL ap_pccm.DPRO_PCCM_CTIS('" + v_dataDate + "',@a,@b)";
		Db.use(GBASE).update(sql);
		System.out.println("----Ctis存储过程结束跑批："
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("Ctis存储过程结束跑批：" + BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));

	}

}
