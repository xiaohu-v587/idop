/**
 * 
 */
package com.goodcol.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.server.zxglserver.PccmSendMessageServer;
import com.goodcol.util.SendMessageUtil;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author dinggang
 * 
 */
public class TestSendMessJob extends OuartzJob {
	public static Logger log = Logger.getLogger(TestSendMessJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			PccmSendMessageServer messageServer = new PccmSendMessageServer();
			Map<String, Object> map = messageServer.findMessIpAndPort();
			List<Record> userInfos = new ArrayList<Record>();
			Record record1 = new Record();
			record1.set("phone", "15156088232");
			Record record2 = new Record();
			record2.set("phone", "13062520599");
			Record record3 = new Record();
			record3.set("phone", "13951880903");
			Record record4 = new Record();
			record4.set("phone", "18626458613");
			userInfos.add(record1);
			userInfos.add(record2);
			userInfos.add(record3);
			userInfos.add(record4);
			String sm_cont = "测试短信发送功能！";
			SendMessageUtil.sendMessage(userInfos, sm_cont, map);
			success();
		} catch (Exception e) {
			error();
			e.printStackTrace();
		}
	}

}
