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
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.SendMessageUtil;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author dinggang
 * 
 */
public class PccmSendMessageEndJob extends OuartzJob {

	public static Logger log = Logger.getLogger(PccmSendMessageEndJob.class);
	private PccmSendMessageServer messageServer = new PccmSendMessageServer();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Map<String, Object> map = messageServer.findMessIpAndPort();
		System.out.println("触发事件--截止日期前倒计时（参数化设置）提醒：中心支行客户池未按时分派beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--截止日期前倒计时（参数化设置）提醒：中心支行客户池未按时分派beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			// 获取本月第一天
			String first_day = DateTimeUtil.getFirstDayOfMonth();
			// 获取二级池待认领时间+三级池待认领时间
			int weekday = messageServer.findAllWeekDay();
			// 获取截至日期
			String end_day = DateTimeUtil.getDistanceDays(first_day, weekday);
			// 获取当前日期
			String now_day = DateTimeUtil.getDateTime();
			// 截至日期减去当前日期
			int day = DateTimeUtil.getSpaceDate(now_day, end_day);
			// 超期
			if (day < 0) {
				System.out
						.println("-------触发事件--截止日期前倒计时（参数化设置）提醒：中心支行客户池未按时分派，已经超期，无发送信息");
				log.warn("触发事件--截止日期前倒计时（参数化设置）提醒：中心支行客户池未按时分派，已经超期，无发送信息");
			}
			// 截至当前各个机构中心支行客户池未按时分配的客户数
			List<Record> records = messageServer.findUnclaimCustCount();
			String role_name = "领导-中心支行";
			if (records != null && !records.isEmpty()) {
				for (int i = 0; i < records.size(); i++) {
					Record record = records.get(i);
					String orgnum = record.getStr("orgnum");
					int count = record.getBigDecimal("count_num").intValue();
					// 查找本机构下中心支行领导
					List<Record> userLeaderInfos = messageServer.findUsersByOrgAndRole(orgnum, role_name, 2);
					if (userLeaderInfos == null || userLeaderInfos.isEmpty()) {
						continue;
					}
					String sm_cont = "[公金联盟]距离分派截止日期还有" + day + "天，本机构仍有"
							+ count + "名客户未分派，" + end_day
							+ "系统将自动把未分派客户归入您个人名下！";
					SendMessageUtil.sendMessage(userLeaderInfos, sm_cont, map);
				}
			} else {
				System.out
						.println("----- 触发事件--截止日期前倒计时（参数化设置）提醒：中心支行客户池未按时分派,查询到的记录为空");
				log.warn("触发事件--截止日期前倒计时（参数化设置）提醒：中心支行客户池未按时分派,查询到的记录为空");
			}
			success();
		} catch (Exception e) {
			System.out
					.println("------触发事件--截止日期前倒计时（参数化设置）提醒：中心支行客户池未按时分派：发送短信发生异常:"
							+ e.toString());
			error();
			e.printStackTrace();
			log.error("触发事件--截止日期前倒计时（参数化设置）提醒：中心支行客户池未按时分派：发送短信发生异常:"
					+ e.toString());
		}
		System.out.println("触发事件--截止日期前倒计时（参数化设置）提醒：中心支行客户池未按时分派end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--截止日期前倒计时（参数化设置）提醒：中心支行客户池未按时分派end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		// ------------------------------------------------------------------
		System.out.println("触发事件--截止日期前倒计时（参数化设置）提醒目标人员begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--截止日期前倒计时（参数化设置）提醒目标人员begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			messageServer.sendCustManagerInfo(map);
			success();
		} catch (Exception e) {
			System.out.println("------触发事件--截止日期前倒计时（参数化设置）提醒目标人员，发送短信发生异常:"
					+ e.toString());
			error();
			e.printStackTrace();
			log.error("触发事件--截止日期前倒计时（参数化设置）提醒目标人员，发送短信发生异常:" + e.toString());
		}
		System.out.println("触发事件--截止日期前倒计时（参数化设置）提醒目标人员end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--截止日期前倒计时（参数化设置）提醒目标人员end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
	}
}
