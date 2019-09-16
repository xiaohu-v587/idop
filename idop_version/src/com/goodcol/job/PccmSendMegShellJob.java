package com.goodcol.job;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * 调用shell发送短信
 * 
 * @author start
 * @2018-9-27
 */
public class PccmSendMegShellJob extends OuartzJob {

	private static final String DEFAULT = "default";

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {

			String ip = Db.use(DEFAULT).queryStr("select remark from gcms_param_info where key= 'MESSAGE_IP'");
			String port = Db.use(DEFAULT).queryStr("select remark from gcms_param_info where key= 'MESSAGE_PORT'");
			String shellPath = Db.use(DEFAULT).queryStr("select remark from gcms_param_info where key= 'MESSAGE_SHELL_PATH'");
			String unicode = Db.use(DEFAULT).queryStr("select val from gcms_param_info where key= 'UNICODE'");
			if (StringUtils.isBlank(unicode)) {
				unicode = "gbk";
			}
			System.out.println("短信发送地址及端口：" + ip + ":" + port);
			System.out.println("shell脚本路径：" + shellPath);
			List<Record> list = Db.use(DEFAULT).find("select * from pccm_msg_info where SEND_STATUS = '0'");
			for (Record record : list) {
				//String[] commond = { shellPath, record.getStr("mobile"), new String((record.getStr("sm_cont")).getBytes(), unicode)};
				String[] commond = { shellPath, record.getStr("mobile"), URLEncoder.encode(record.getStr("sm_cont"), unicode)};
				System.out.println("短信内容: " + new String((record.getStr("sm_cont")).getBytes(), unicode) + "----编码格式：" + unicode);
				Runtime.getRuntime().exec(commond);
				System.out.println("调用shell发送短信手机号码及内容：" + commond);
				Db.use(DEFAULT).update("update pccm_msg_info set SEND_STATUS = '1',SEND_TIME = '" + DateTimeUtil.getNowDate() + "' where id = '" + record.getStr("id") + "'");
			}
			success();
		} catch (Exception e) {
			error();
			e.printStackTrace();
		}
	}
}
