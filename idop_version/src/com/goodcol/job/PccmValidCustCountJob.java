package com.goodcol.job;

import java.util.List;

import org.quartz.JobExecutionContext;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * 
 * @author start
 * @2018-8-23
 */
public class PccmValidCustCountJob extends OuartzJob {

	public static Logger log = Logger.getLogger(PccmValidCustCountJob.class);

	@Override
	public void execute(JobExecutionContext arg0) {
		System.out.println("有效户及PA部位零和客户统计定时任务开始：" + DateTimeUtil.getTime());
		try {
			String month = "";
			String lastMonth = "";
			String lastYear = "";// 上年
			String lastYearDay = (Integer.parseInt(DateTimeUtil.getYear()) - 1)
					+ "1231";// 上年末
			//String yestoday = DateTimeUtil.getYestoday();// 昨天
			String yestoday = AppUtils.findGNewDate();// 取宽表最新数据日期
			String sql = "select * from ("
					+ "select row_number() over (order by pa_date desc) rn,pa_date "
					+ "from pccm_cust_base_info group by pa_date) m where rn= 1 or rn = 2 ";
			List<Record> list = Db.use("gbase").find(sql);

			sql = "select max(pa_date) as year_date from pccm_cust_base_info where substr(pa_date,1,4) = '"
					+ DateTimeUtil.getYear() + "'";
			Record record = Db.use("gbase").findFirst(sql);
			lastYear = record.getStr("year_date");
			if (null != list && list.size() > 0) {
				if (list.size() == 2) {
					month = list.get(0).get("pa_date");
					lastMonth = list.get(1).get("pa_date");
				} else if (list.size() == 1) {
					month = list.get(0).get("pa_date");
					lastMonth = DateTimeUtil.geYesterDay(-5);
				} else {
					month = "20180731";// DateTimeUtil.getPathName();
					lastMonth = DateTimeUtil.geYesterDay(-5);
				}
			}

			/****** 有效户统计 *******/
			log.info("有效户统计存储过程 ap_pccm.DROP_PCCM_VALID_CUST_COUNT 调用开始:"
					+ DateTimeUtil.getTime());
			String validSql = " CALL ap_pccm.DROP_PCCM_VALID_CUST_COUNT('"
					+ lastYear + "','" + month + "','" + lastMonth + "',@a,@b)";
			Db.use("gbase").update(validSql);
			System.out.println("有效户统计SQL：" + validSql);
			log.info("有效户统计存储过程 ap_pccm.DROP_PCCM_VALID_CUST_COUNT 调用结束:"
					+ DateTimeUtil.getTime());

			/****** PA不为零 *******/
			log.info("PA不为零存储过程 ap_pccm.DPRO_PCCM_PA_NOT_ZERO 调用开始:"
					+ DateTimeUtil.getTime());
			String notZeroSql = " CALL ap_pccm.DPRO_PCCM_PA_NOT_ZERO('"
					+ lastYear + "','" + month + "','" + lastMonth + "',@a,@b)";
			Db.use("gbase").update(notZeroSql);
			System.out.println("PA不为零SQL：" + notZeroSql);
			log.info("PA不为零存储过程 ap_pccm.DPRO_PCCM_PA_NOT_ZERO 调用结束:"
					+ DateTimeUtil.getTime());

			/****** 客户统计 *******/
			log.info("客户统计存储过程 ap_pccm.DROP_PCCM_CUST_NEW_COUNT 调用开始:"
					+ DateTimeUtil.getTime());
			String custCntSql = " CALL ap_pccm.DROP_PCCM_CUST_NEW_COUNT('"
					+ yestoday + "','" + lastYearDay + "',@a,@b)";
			Db.use("gbase").update(custCntSql);
			System.out.println("客户统计SQL：" + custCntSql);
			log.info("客户统计存储过程 ap_pccm.DROP_PCCM_CUST_NEW_COUNT 调用结束:"
					+ DateTimeUtil.getTime());
			success();
		} catch (Exception e) {
			error();
			log.error("有效户及PA部位零和客户统计定时任务执行失败：" + e);
		}
		System.out.println("有效户及PA部位零和客户统计定时任务结束：" + DateTimeUtil.getTime());

	}

}
