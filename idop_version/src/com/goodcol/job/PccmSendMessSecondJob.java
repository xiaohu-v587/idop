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
public class PccmSendMessSecondJob extends OuartzJob {
	public static Logger log = Logger.getLogger(PccmSendMessSecondJob.class);
	private PccmSendMessageServer messageServer = new PccmSendMessageServer();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Map<String, Object> map = messageServer.findMessIpAndPort();
		System.out.println("触发事件--客户池中未认领客户推送至中心支行beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--客户池推送至二级行beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			sendMessage(map);
			success();
		} catch (Exception e) {
			System.out.println("------触发事件--客户池中未认领客户推送至中心支行，发送短信发生异常:"
					+ e.toString());
			error();
			e.printStackTrace();
			log.error("触发事件--客户池中未认领客户推送至中心支行，发送短信发生异常:" + e.toString());
		}
		log.warn("触发事件--客户池推送至二级行end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("触发事件--客户池中未认领客户推送至中心支行end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		//-------------------------------------------
		System.out.println("触发事件--领导分派beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--领导分派beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			sendMessageToCustManager(map);
			success();
		} catch (Exception e) {
			System.out.println("------触发事件--领导分派，发送短信发生异常:" + e.toString());
			error();
			e.printStackTrace();
			log.error("触发事件--领导分派，发送短信发生异常:" + e.toString());
		}
		log.warn("触发事件--领导分派end" + BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("触发事件--领导分派end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 触发事件--领导分派
	 * 
	 * @param map
	 */
	private void sendMessageToCustManager(Map<String, Object> map) {
		// 被分派潜在或商机客户的业务人员
		List<Record> list = messageServer.findCustsByLeader();
		// 待营销天数
		int weekday = messageServer.findWeekDay();
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Record record = list.get(i);
				String mobile = record.getStr("phone");
				int count_num = record.getBigDecimal("count_num").intValue();
				if(count_num==0){
					continue;
				}
				String sm_cont = "[公金联盟]您的大boss已将" + count_num
						+ "客户分配到您名下，客户具体信息" + "请至系统查看，并在" + weekday
						+ "天内于系统内反馈营销情况。";
				SendMessageUtil.sendMessage(mobile, "01", sm_cont, map);
			}
		} else {
			System.out.println("-----触发事件--领导分派,查询到的记录为空");
			log.warn("触发事件--领导分派,查询到的记录为空");
		}
	}

	/**
	 * 触发事件--客户池中未认领客户推送至中心支行
	 * 
	 * @param map2
	 */
	private void sendMessage(Map<String, Object> map2) {
		// 查找本月目标的机构归属
		List<Record> third_orgnums = messageServer.findUclaimCustsOrgs();
		List<Map<String, Object>> list = null;
		if (third_orgnums != null && !third_orgnums.isEmpty()) {
			list = messageServer.findUnclaimMeesInfo(third_orgnums);
		} else {
			System.out.println("-----触发事件--客户池中未认领客户推送至中心支行，查找本月目标的机构归属为空");
			log.warn("触发事件--客户池中未认领客户推送至中心支行，查找本月目标的机构归属为空");
		}
		String role_name1 = "领导";
		String role_name2 = "业务人员-中心支行";
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String orgnum = map.get("orgnum").toString();
				String orgname = map.get("orgname").toString();
				String other_count = map.get("other_count").toString();
				String my_count = map.get("my_count").toString();
				String count = map.get("count").toString();
				if (Integer.parseInt(count) > 0) {
					// --------查询中心支行及以下领导即责任中心到中心支行的领导---------
					List<Record> userLeaderInfos1 = messageServer
							.findLeadersFromOrg(orgnum, role_name1, "2");
					if (userLeaderInfos1 != null && !userLeaderInfos1.isEmpty()) {
						String sm_cont = "[公金联盟]本月" + count + "户待认领客户已入"
								+ orgname + "客户池，" + "其中他行客户" + other_count
								+ "户，存量客户" + my_count + "户,请尽快进行认领分派工作并跟进营销。";
						SendMessageUtil.sendMessage(userLeaderInfos1, sm_cont,
								map2);
					}
					// --------查询中心支行业务人员---------
					List<Record> userLeaderInfos2 = messageServer
							.findUsersByOrgAndRole(orgnum, role_name2, 2);
					if (userLeaderInfos2 != null && !userLeaderInfos2.isEmpty()) {
						String sm_cont = "[公金联盟]本月" + count + "户待认领客户已入"
								+ orgname + "客户池，" + "其中他行客户" + other_count
								+ "户，存量客户" + my_count + "户,请尽快进行认领工作并跟进营销。";
						SendMessageUtil.sendMessage(userLeaderInfos2, sm_cont,
								map2);
					}
				}
			}
		} else {
			System.out.println("-----触发事件--客户池中未认领客户推送至中心支行,查询到的记录为空");
			log.warn("触发事件--客户池中未认领客户推送至中心支行,查询到的记录为空");
		}
	}

}
