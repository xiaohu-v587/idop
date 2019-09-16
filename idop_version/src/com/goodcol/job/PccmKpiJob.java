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
public class PccmKpiJob extends OuartzJob {
	private static Logger log=Logger.getLogger(PccmKpiJob.class);
	
	//跑批时间测试
	private static String excuTime = new SimpleDateFormat("yyyyMM").format(new Date());
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		// -----开始时间-----
//		String start_time = "";
//		// -----结束时间-----
//		String end_time = "";
//		String incflg = null;
//		//记录数
//		int record_size = 0;
//		//跑批状态
//		int run_status = 0;
//		try{
//			start_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
//			KplUtil.kplTask(excuTime);
//			run_status=1;
//		}catch(Exception e){
//			run_status=2;
//			e.printStackTrace();
//			log.error("跑批时在"+BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss")+"发生异常:"+e.getMessage());
//		}finally{
//			end_time=BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss");
//			Db.use("default").update("insert into PCCM_QUARTZ_RUN(id,START_TIME,END_TIME,RECORD_SIZE,INCFLG,RUN_STATUS) values(?,?,?,?,?,?)",new Object[]{AppUtils.getStringSeq(),start_time,end_time,record_size,incflg,run_status});
//			log.info("KPI"+excuTime+"批次跑批结束！！！");
//		}
		System.out.println("KPI时任务开始：" + DateTimeUtil.getTime());
		try {
			String lastMonthFirstDay = DateTimeUtil.getLastMonthFirstDay();
			String lastMonthEndDay = DateTimeUtil.getLastMonthEndDay();
	

			/****** KPI *******/
			log.info("KPI存储过程 ap_pccm.NEW_DROP_PCCM_KPI 调用开始:"
					+ DateTimeUtil.getTime());
			String kpiSql = " CALL ap_pccm.NEW_DROP_PCCM_KPI('"
					+ lastMonthFirstDay + "','" + lastMonthEndDay + "',@a,@b)";
			Db.use("gbase").update(kpiSql);
			System.out.println("KPI SQL：" + kpiSql);
			log.info("KPI存储过程 ap_pccm.NEW_DROP_PCCM_KPI 调用结束:"
					+ DateTimeUtil.getTime());

	
			success();
		} catch (Exception e) {
			error();
			log.error("KPI定时任务执行失败：" + e);
		}
		System.out.println("KPI定时任务结束：" + DateTimeUtil.getTime());
		
	}

}
