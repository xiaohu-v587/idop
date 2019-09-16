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
public class PccmAppealJob extends OuartzJob {
	public static Logger log = Logger.getLogger(PccmAppealJob.class);
	private PccmSendMessageServer messageServer = new PccmSendMessageServer();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Map<String, Object> map = messageServer.findMessIpAndPort();
		System.out.println("触发事件--申诉未处理beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--申诉未处理beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			// 申诉待处理数量
			int count = messageServer.findAppeal();
			if (count == 0) {
				log.warn("无未处理的申诉任务");
				return;
			}
			String role_name = "分行管理员";
			List<Record> records = messageServer.findUserLeaders(role_name);
			if (records != null && !records.isEmpty()) {
				String sm_cont = "【公金联盟】您当前有" + count + "个申诉待处理，请及时处理。";
				SendMessageUtil.sendMessage(records, sm_cont, map);
			}
			success();
		} catch (Exception e) {
			System.out.println("------触发事件--申诉未处理，发送短信发生异常:" + e.toString());
			error();
			e.printStackTrace();
			log.error("触发事件--申诉未处理，发送短信发生异常:" + e.toString());
		}
		System.out.println("触发事件--申诉未处理end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--申诉未处理end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
	}

}
