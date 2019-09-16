/**
 * 
 */
package com.goodcol.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.server.zxglserver.PccmKpiRunServer;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author dinggang
 *
 */
public class PccmKpiRunJob extends OuartzJob {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String nowDate=BolusDate.getDateTime("yyyy-MM-dd");
		PccmKpiRunServer pccmKpiRunServer=new PccmKpiRunServer();
		pccmKpiRunServer.kpITask(nowDate);
	}

}
