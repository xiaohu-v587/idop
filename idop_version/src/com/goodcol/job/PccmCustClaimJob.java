package com.goodcol.job;

import org.quartz.JobExecutionContext;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;


public class PccmCustClaimJob extends OuartzJob {
	public static Logger log = Logger.getLogger(PccmCustClaimJob.class);
	@Override
	public void execute(JobExecutionContext arg0) {
		System.out.println("客户被认领查询（明细下载需要）定时任务开始：" + DateTimeUtil.getTime());
		try {
			log.info("存储过程 ap_pccm.DROP_PCCM_CUST_POOL_CLAIM 调用开始:"
					+ DateTimeUtil.getTime());
			String validSql = " CALL ap_pccm.DROP_PCCM_CUST_POOL_CLAIM(@a,@b)";
			Db.use("gbase").update(validSql);
			System.out.println("SQL：" + validSql);
			success();
		} catch (Exception e) {
			error();
			System.out.println("客户认领（明细下载需要）定时任务执行失败：" + e);
		}
		System.out.println("客户认领（明细下载需要）定时任务结束：" + DateTimeUtil.getTime());

	}

}
