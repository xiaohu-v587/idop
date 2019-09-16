/**
 * 
 */
package com.goodcol.job;

import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.server.zxglserver.PccmSendMessageServer;
import com.goodcol.util.SendMessageUtil;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author dinggang
 * 
 */
public class PccmSendMessThirdJob extends OuartzJob {
	public static Logger log = Logger.getLogger(PccmSendMessThirdJob.class);
	private PccmSendMessageServer messageServer = new PccmSendMessageServer();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Map<String, Object> map = messageServer.findMessIpAndPort();
		System.out.println("触发事件--领导未按时将入池客户分配下面认领的，直接分配领导个人beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--领导未按时将入池客户分配下面认领的，直接分配领导个人beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			sendMessage(map);
			success();
		} catch (Exception e) {
			System.out
					.println("------触发事件--领导未按时将入池客户分配下面认领的，直接分配领导个人,发送短信发生异常:"
							+ e.getMessage());
			error();
			e.printStackTrace();
			log.error("触发事件--领导未按时将入池客户分配下面认领的，直接分配领导个人,发送短信发生异常:"
					+ e.getMessage());
		}
		log.warn("触发事件--领导未按时将入池客户分配下面认领的，直接分配领导个人end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("触发事件--领导未按时将入池客户分配下面认领的，直接分配领导个人end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 触发事件--领导未按时将入池客户分配下面认领的，直接分配领导个人
	 * 
	 * @param map
	 */
	private void sendMessage(Map<String, Object> map) {
		List<Record> records = messageServer.findCustsToLeader();
		if (records != null && !records.isEmpty()) {
			for (int i = 0; i < records.size(); i++) {
				Record record = records.get(i);
				String mobile = record.getStr("phone");
				int count_num = record.getBigDecimal("count_num").intValue();
				if (count_num == 0) {
					continue;
				}
				String sm_cont = "[公金联盟]本月待认领客户仍有" + count_num
						+ "未分配，已归入您个人名下，请关注！";
				SendMessageUtil.sendMessage(mobile, "01", sm_cont, map);
			}
		} else {
			System.out.println("----- 触发事件--领导未按时将入池客户分配下面认领的,查询到的记录为空");
			log.warn("触发事件--领导未按时将入池客户分配下面认领的,查询到的记录为空");
		}
	}

}
