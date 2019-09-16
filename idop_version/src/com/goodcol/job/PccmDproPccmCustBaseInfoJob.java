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
public class PccmDproPccmCustBaseInfoJob extends OuartzJob {
	public static Logger log = Logger
			.getLogger(PccmDproPccmCustBaseInfoJob.class);
	private static final String GBASE = "gbase";
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("----宽表存储过程开始跑批："
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("宽表存储过程开始跑批：" + BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		//上个月的最后一天
		String v_dataDate = DateTimeUtil.getLastMonthEndDay();
		String sql = " CALL ap_pccm.DPRO_PCCM_CUST_BASE_INFO('" + v_dataDate
				+ "',@a,@b)";
		Db.use(GBASE).update(sql);
		System.out.println("----宽表存储过程结束跑批："
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("宽表存储过程结束跑批：" + BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
	}

}
