/**
 * 
 */
package com.goodcol.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author
 * 
 */
public class PccmLatentJob extends OuartzJob {
	private static final String GBASE = "gbase";
	
	/**
	 * 查询计算潜在达成客户达成率定时任务 2018年7月9日09:30:48
	 * 
	 * @author liutao
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//CustKpiServer custKpiServer = new CustKpiServer();
		System.out.println("潜在达成率定时任务开始：" + DateTimeUtil.getTime());
		try {
			// 查询出所有需要进行计算的客户经理
			/*List<Record> list = custKpiServer.findLatentUser();
			for (Record record : list) {
				String user_no = record.getStr("user_no");
				// 分别查询每个客户经理的潜在客户相关数(潜在客户达成数和潜在客户总数)
				int succNum = custKpiServer.findLatentNumByUserNo(user_no,
						"succ");
				int sumNum = custKpiServer.findLatentNumByUserNo(user_no, null);
				String reach = "0";
				if (sumNum > 0) {
					float rea = (float) succNum / sumNum * 100;
					if (rea != 0) {
						reach = String.format("%.2f", rea);
					}
				}
				record.set("succnum", succNum + "");
				record.set("sumnum", sumNum + "");
				record.set("reach", reach);
				record.set("p_rank", "0");
				record.set("b_rank", "0");
				record.set("s_rank", "0");
				custKpiServer.saveUserLatent(record);
				// 查询客户经理排名
				int p_rank = custKpiServer.findUserRank(user_no, "1");// 省行排名
				int b_rank = custKpiServer.findUserRank(user_no, "2");// 分行排名
				int s_rank = custKpiServer.findUserRank(user_no, "3");// 支行排名
				record.set("p_rank", p_rank + "");
				record.set("b_rank", b_rank + "");
				record.set("s_rank", s_rank + "");
				// 保存信息
				custKpiServer.saveUserLatent(record);
			}*/
			
			//修改定时任务为GBASE数据库中的存储过程  2018年9月8日14:31:13  --liutao
			long jobstartTime = System.currentTimeMillis();
			//获取GBASE数据库宽表数据的最新日期
			String data_date = findBaseInfoMaxDate();
			/*********************更新个人潜在达成率数据******************/
			String validSql = " CALL ap_pccm.DPRO_PCCM_USER_LATENT_INFO('"+ data_date +"',@a,@b)";
			Db.use(GBASE).update(validSql);
			
			/*********************更新机构潜在达成率数据******************/
			validSql = " CALL ap_pccm.DPRO_PCCM_ORG_LATENT_INFO(@a,@b)";
			
			Db.use(GBASE).update(validSql);
			long jobEndTime = System.currentTimeMillis();
			System.out.println("潜在达成率定时任务执行结束, 耗时：" + (jobEndTime - jobstartTime) + "毫秒");
			success();
		} catch (Exception e) {
			error();
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询GBASE宽表数据最大的时间是多少
	 * 2018年9月8日15:36:41
	 * @author liutao
	 * @return
	 */
	public String findBaseInfoMaxDate(){
		String sql = "select to_char(max(data_date)) as data_date from ap_pccm.pccm_cust_base_info";
		Record r = Db.use(GBASE).findFirst(sql);
		if (null != r) {
			return r.getStr("data_date");
		} else {
			//如果没有则返回当前时间
			return DateTimeUtil.getPathName();
		}
	}
}
