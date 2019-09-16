/**
 * 
 */
package com.goodcol.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.log.Logger;
import com.goodcol.server.zxglserver.PccmSendMessageServer;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author dinggang
 * 
 */
public class PccmCustMarketJob extends OuartzJob {
	public static Logger log = Logger.getLogger(PccmCustMarketJob.class);
	private PccmSendMessageServer messageServer = new PccmSendMessageServer();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("判断开户是否营销成功,并把营销成功的数据吐到oracle---beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("判断开户是否营销成功,并把营销成功的数据吐到oracle---" + BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		messageServer.updateCustByOpen();
		System.out.println("判断开户是否营销成功,并把营销成功的数据吐到oracle---end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("判断开户是否营销成功,并把营销成功的数据吐到oracle---end" + BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
	}

}
