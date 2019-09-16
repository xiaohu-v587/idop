/**
 * 
 */
package com.goodcol.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.goodcol.core.log.Logger;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.server.zxglserver.PccmSendMessageServer;
import com.goodcol.util.AppUtils;
import com.goodcol.util.SendMessageUtil;
import com.goodcol.util.date.BolusDate;
import com.goodcol.util.ext.plugin.quartz.OuartzJob;

/**
 * @author dinggang
 * 
 */
public class PccmSendMessageJob extends OuartzJob {
	public static Logger log = Logger.getLogger(PccmSendMessageJob.class);
	private PccmSendMessageServer messageServer = new PccmSendMessageServer();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Map<String, Object> map = messageServer.findMessIpAndPort();
		System.out.println("触发事件--客户池推送至二级行beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--客户池推送至二级行beign"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			sendMessage(map);
			success();
		} catch (Exception e) {
			System.out.println("-----触发事件--客户池推送至二级行，发送短信发生异常:" + e.toString());
			error();
			e.printStackTrace();
			log.error("触发事件--客户池推送至二级行，发送短信发生异常:" + e.toString());
		}
		log.warn("触发事件--客户池推送至二级行end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("触发事件--客户池推送至二级行end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		// -----------------------------------------------------
		System.out.println("触发事件--重新界定的潜在客户标准,二级分行及以下领导统计值推送begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--重新界定的潜在客户标准,二级分行及以下领导统计值推送begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			// 二级分行及以下领导统计值推送
			sendPotentialMessage(map);
			success();
		} catch (Exception e) {
			System.out
					.println("------触发事件--重新界定的潜在客户标准,二级分行及以下领导统计值推送，发送短信发生异常:"
							+ e.toString());
			error();
			e.printStackTrace();
			log.error("触发事件--重新界定的潜在客户标准,二级分行及以下领导统计值推送，发送短信发生异常:"
					+ e.toString());
		}
		log.warn("触发事件--重新界定的潜在客户标准,二级分行及以下领导统计值推送end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("触发事件--重新界定的潜在客户标准,二级分行及以下领导统计值推送end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		// -------------------------------------------------------
		System.out.println("触发事件--重新界定的潜在客户标准,归属客户经理（待办任务）begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--重新界定的潜在客户标准,归属客户经理（待办任务）begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			// 归属客户经理（待办任务）
			sendPotentialMessageToManager(map);
			success();
		} catch (Exception e) {
			System.out.println("------触发事件--重新界定的潜在客户标准,归属客户经理（待办任务），发送短信发生异常:"
					+ e.toString());
			error();
			e.printStackTrace();
			log.error("触发事件--重新界定的潜在客户标准,归属客户经理（待办任务），发送短信发生异常:" + e.toString());
		}
		log.warn("触发事件--重新界定的潜在客户标准,归属客户经理（待办任务）end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("触发事件--重新界定的潜在客户标准,归属客户经理（待办任务）end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		// -------------------------------------------------------------
		System.out.println("触发事件--上月经营统计结果，推送领导（个人）begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--上月经营统计结果，推送领导（个人）begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			// 领导个人
			sendMessageToLeader(map);
			success();
		} catch (Exception e) {
			System.out.println("------触发事件--上月经营统计结果，推送领导（个人），发送短信发生异常:"
					+ e.toString());
			error();
			e.printStackTrace();
			log.error("触发事件--上月经营统计结果，推送领导（个人），发送短信发生异常:" + e.toString());
		}
		log.warn("触发事件--上月经营统计结果，推送领导（个人）end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("触发事件--上月经营统计结果，推送领导（个人）end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		// -------------------------------------------------------------
		System.out.println("触发事件--上月经营统计结果，推送业务人员begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--上月经营统计结果，推送业务人员begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			// 业务人员
			sendMessageToCustManager(map);
			success();
		} catch (Exception e) {
			System.out.println("------触发事件--上月经营统计结果，推送业务人员，发送短信发生异常:"
					+ e.toString());
			error();
			e.printStackTrace();
			log.error("触发事件--上月经营统计结果，推送业务人员，发送短信发生异常:" + e.toString());
		}
		log.warn("触发事件--上月经营统计结果，推送业务人员end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("触发事件--上月经营统计结果，推送业务人员end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		// -------------------------------------------------------------
		System.out.println("触发事件--上月经营统计结果，推送领导（所处机构）begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		log.warn("触发事件--上月经营统计结果，推送领导（所处机构）begin"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		try {
			// 领导（所处机构）
			sendMessageToLeaderFromOrg(map);
			success();
		} catch (Exception e) {
			System.out.println("------触发事件--上月经营统计结果，推送领导（所处机构），发送短信发生异常:"
					+ e.toString());
			error();
			e.printStackTrace();
			log.error("触发事件--上月经营统计结果，推送领导（所处机构），发送短信发生异常:" + e.toString());
		}
		log.warn("触发事件--上月经营统计结果，推送领导（所处机构）end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
		System.out.println("触发事件--上月经营统计结果，推送领导（所处机构）end"
				+ BolusDate.getDateTime("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 触发事件--上月经营统计结果，推送领导（所处机构）
	 * 性能优化--已被注释
	 * @param map
	 * @throws InterruptedException
	 */
	private void sendMessageToLeaderFromOrg(final Map<String, Object> map) {
		final String role_name = "领导";
		// 上月
		String timePoint = "3";
		// 上月经营结果统计
		List<Record> records = messageServer.findMessageToLeader(timePoint);
		if (records != null && !records.isEmpty()) {
			//------------------可以用这段beign到end的代码，也可以用被注释块代码，注释块代码在测试环境测了没问题------------------------
			//---begin---
			for(Record record:records){
				String org_id = record.getStr("org_id");
				// 全部的领导---责任中心到省行的领导
				List<Record> list = messageServer.findLeadersFromOrg(org_id, role_name,
						"all");
				if (list != null && !list.isEmpty()) {
					String sm_cont = "[公金联盟]战报来啦！" + record.getStr("orgname")
							+ "上月末商机客户转化率" + record.getStr("markreach") + "，排名"
							+ record.getBigDecimal("mark_num").intValue() + "/" + records.size()
							+ "；潜在客户提升率" + record.getStr("latentreach") + "，排名"
							+ record.getBigDecimal("latent_num").intValue() + "/"
							+ records.size() + "。再接再厉！";
					SendMessageUtil.sendMessage(list, sm_cont, map);
				}
			}
			//---end---
//			int total = 0;
//			int updateThreadNum = 20;// 处理数据线程总数
//			final int count = records.size();
//			if (count <= updateThreadNum) {// 小于线程处理总数
//				for (Record record : records) {
//					sendMessage(record, map, role_name, count);
//				}
//			} else {// 大于线程处理总数
//				if (count % updateThreadNum == 0) {
//					total = count / updateThreadNum;
//				} else {
//					total = count / updateThreadNum + 1;
//				}
//				for (int i = 0; i < total; i++) {
//					ExecutorService executorService = Executors
//							.newFixedThreadPool(updateThreadNum);
//					List<Record> rs = null;
//					if (i == 0) {
//						rs = records.subList(0, updateThreadNum);
//					} else {
//						rs = records.subList(i * updateThreadNum, count);
//						if (rs.size() > updateThreadNum) {
//							rs = records.subList(i * updateThreadNum, i
//									* updateThreadNum + updateThreadNum);
//							for (final Record record : rs) {
//								executorService.execute(new Thread(
//										new Runnable() {
//											@Override
//											public void run() {
//												sendMessage(record, map,
//														role_name, count);
//											}
//										}) {
//								});
//							}
//						}
//					}
//					executorService.shutdown();
//					while (!executorService.isTerminated()) {
//					}
//				}
//			}
		} else {
			System.out
					.println("------触发事件--上月经营统计结果，推送领导（所处机构）,查询到的经营统计结果记录为空");
			log.warn("触发事件--上月经营统计结果，推送领导（所处机构）,查询到的经营统计结果记录为空");

		}
	}

	private void sendMessage(Record record, Map<String, Object> map,
			String role_name, int count) {
		String org_id = record.getStr("org_id");
		// 全部的领导---责任中心到省行的领导
		List<Record> list = messageServer.findLeadersFromOrg(org_id, role_name,
				"all");
		if (list != null && !list.isEmpty()) {
			String sm_cont = "[公金联盟]战报来啦！" + record.getStr("orgname")
					+ "上月末商机客户转化率" + record.getStr("markreach") + "，排名"
					+ record.getBigDecimal("mark_num").intValue() + "/" + count
					+ "；潜在客户提升率" + record.getStr("latentreach") + "，排名"
					+ record.getBigDecimal("latent_num").intValue() + "/"
					+ count + "。再接再厉！";
			SendMessageUtil.sendMessage(list, sm_cont, map);
		}
	}

	/**
	 * 触发事件--上月经营统计结果，推送业务人员
	 * 
	 * @param map
	 * @throws InterruptedException
	 */
	private void sendMessageToCustManager(Map<String, Object> map) {
		// 本月
		String timePoint = "1";
		String roleName = "业务人员";
		// 本月业务人员的潜在客户达成率及排名，商机客户转化率及排名
		List<Record> records = messageServer.findAllPersonLatentMark(timePoint,
				roleName);
		if (records != null && !records.isEmpty()) {
			for (Record record : records) {
				String sm_cont = "[公金联盟]战报来啦！本月商机客户转化率"
						+ record.getStr("markreach") + "，排名为"
						+ record.getBigDecimal("mark_rank").intValue() + "/"
						+ records.size() + ";潜在客户转化率"
						+ record.getStr("latentreach") + ",排名"
						+ record.getBigDecimal("latent_rank").intValue() + "/"
						+ records.size() + "。继续加油哦";
				if (AppUtils.StringUtil(record.getStr("phone")) != null) {
					SendMessageUtil.sendMessage(record.getStr("phone"), "01",
							sm_cont, map);
				}
			}
		} else {
			System.out.println("------触发事件--上月经营统计结果，推送业务人员,查询到的经营统计结果记录为空");
			log.warn("触发事件--上月经营统计结果，推送业务人员,查询到的经营统计结果记录为空");
		}
	}

	/**
	 * 触发事件--上月经营统计结果，推送领导（个人）
	 * 
	 * @param map
	 * @throws InterruptedException
	 */
	private void sendMessageToLeader(Map<String, Object> map) {
		// 本月
		String timePoint = "1";
		String roleName = "领导";
		// 本月领导（个人）的潜在客户达成率及排名，商机客户转化率及排名
		List<Record> records = messageServer.findAllPersonLatentMark(timePoint,
				roleName);
		if (records != null && !records.isEmpty()) {
			for (Record record : records) {
				String sm_cont = "[公金联盟]战报来啦！本月商机客户转化率"
						+ record.getStr("markreach") + "，排名为"
						+ record.getBigDecimal("mark_rank").intValue() + "/"
						+ records.size() + ";潜在客户转化率"
						+ record.getStr("latentreach") + ",排名"
						+ record.getBigDecimal("latent_rank").intValue() + "/"
						+ records.size() + "。";
				if (AppUtils.StringUtil(record.getStr("phone")) != null) {
					SendMessageUtil.sendMessage(record.getStr("phone"), "01",
							sm_cont, map);
				}
			}
		} else {
			System.out.println("------触发事件--上月经营统计结果，推送领导（个人）,查询到的经营统计结果记录为空");
			log.warn("触发事件--上月经营统计结果，推送领导（个人）,查询到的经营统计结果记录为空");
		}
	}

	/**
	 * 触发事件--重新界定的潜在客户标准,归属客户经理（待办任务）
	 * 
	 * @param map
	 */
	private void sendPotentialMessageToManager(Map<String, Object> map) {
		// 归属客户经理（待办任务）
		List<Record> records = messageServer.findPotentialCustsToManager();
		List<String> userNos = new ArrayList<String>();
		List<Record> list = new ArrayList<Record>();
		// 一层
		List<Record> level1 = new ArrayList<Record>();
		// 二层
		List<Record> level2 = new ArrayList<Record>();
		// 三层
		List<Record> level3 = new ArrayList<Record>();
		// 四层
		List<Record> level4 = new ArrayList<Record>();
		// 五层
		List<Record> level5 = new ArrayList<Record>();
		if (records != null && !records.isEmpty()) {
			for (int i = 0; i < records.size(); i++) {
				Record record = records.get(i);
				String user_no = record.getStr("user_no");
				String clas_five = record.getStr("clas_five");
				if ("1".equals(clas_five)) {
					level1.add(record);
				} else if ("2".equals(clas_five)) {
					level2.add(record);
				} else if ("3".equals(clas_five)) {
					level3.add(record);
				} else if ("4".equals(clas_five)) {
					level4.add(record);
				} else if ("5".equals(clas_five)) {
					level5.add(record);
				}
				if (userNos != null && !userNos.isEmpty()) {
					if (userNos.indexOf(user_no) != -1) {
						continue;
					}
				}
				userNos.add(user_no);
				list.add(record);
			}
			sendPotentialMessageToManager(userNos, list, level1, level2,
					level3, level4, level5, map);
		} else {
			System.out
					.println("------触发事件--重新界定的潜在客户标准,归属客户经理（待办任务），客户经理名下每层潜在客户数的记录为空");
			log.warn("触发事件--重新界定的潜在客户标准,归属客户经理（待办任务）,客户经理名下每层潜在客户数的记录为空");
		}
	}

	private void sendPotentialMessageToManager(List<String> userNos,
			List<Record> list, List<Record> level1, List<Record> level2,
			List<Record> level3, List<Record> level4, List<Record> level5,
			Map<String, Object> map) {
		if (userNos != null && !userNos.isEmpty()) {
			for (int i = 0; i < userNos.size(); i++) {
				String user_no = userNos.get(i);
				String mobile = list.get(i).getStr("phone");
				int level1_count = getLevelCountByUserNo(level1, user_no);
				int level2_count = getLevelCountByUserNo(level2, user_no);
				int level3_count = getLevelCountByUserNo(level3, user_no);
				int level4_count = getLevelCountByUserNo(level4, user_no);
				int level5_count = getLevelCountByUserNo(level5, user_no);
				int count = level1_count + level2_count + level3_count
						+ level4_count + level5_count;
				if (count == 0) {
					continue;
				}
				String sm_cont = "[公金联盟]本月您名下五层潜在客户" + count + "户，一层至五层分别为"
						+ level1_count + "/" + level2_count + "/"
						+ level3_count + "/" + level4_count + "/"
						+ level5_count + "。请积极推动实现转化，Fighting！";
				SendMessageUtil.sendMessage(mobile, "01", sm_cont, map);
			}
		}
	}

	/**
	 * 根据员工号获取每层潜在客户数
	 * 
	 * @param level
	 * @param user_no
	 * @return
	 */
	private int getLevelCountByUserNo(List<Record> level, String user_no) {
		int level_count = 0;
		if (level != null && !level.isEmpty()) {
			for (int j = 0; j < level.size(); j++) {
				Record record = level.get(j);
				if (user_no.equals(record.getStr("user_no"))) {
					level_count = Integer.parseInt(String.valueOf(record
							.getBigDecimal("count_num")));
					break;
				}
			}
		}
		return level_count;
	}

	/**
	 * 触发事件--重新界定的潜在客户标准,二级分行及以下领导统计值推送
	 * 
	 * @param map
	 */
	private void sendPotentialMessage(Map<String, Object> map) {
		List<Record> records = messageServer.findPotentialCusts();
		List<String> orgnums = new ArrayList<String>();
		List<Record> list = new ArrayList<Record>();
		// 一层
		List<Record> level1 = new ArrayList<Record>();
		// 二层
		List<Record> level2 = new ArrayList<Record>();
		// 三层
		List<Record> level3 = new ArrayList<Record>();
		// 四层
		List<Record> level4 = new ArrayList<Record>();
		// 五层
		List<Record> level5 = new ArrayList<Record>();
		if (records != null && !records.isEmpty()) {
			for (int i = 0; i < records.size(); i++) {
				Record record = records.get(i);
				String orgnum = record.getStr("orgnum");
				String clas_five = record.getStr("clas_five");
				if ("1".equals(clas_five)) {
					level1.add(record);
				} else if ("2".equals(clas_five)) {
					level2.add(record);
				} else if ("3".equals(clas_five)) {
					level3.add(record);
				} else if ("4".equals(clas_five)) {
					level4.add(record);
				} else if ("5".equals(clas_five)) {
					level5.add(record);
				}
				if (orgnums != null && !orgnums.isEmpty()) {
					if (orgnums.indexOf(orgnum) != -1) {
						continue;
					}
				}
				orgnums.add(orgnum);
				list.add(record);
			}
			sendMessage(orgnums, list, level1, level2, level3, level4, level5,
					map);
		} else {
			System.out
					.println("------触发事件--重新界定的潜在客户标准,二级分行及以下领导统计值推送,查找本月推送目标的机构归属，每层潜在客户数的记录为空");
			log.warn("触发事件--重新界定的潜在客户标准,二级分行及以下领导统计值推送,查找本月推送目标的机构归属，每层潜在客户数的记录为空");
		}
	}

	/**
	 * 二级分行及以下领导统计值推送
	 * 
	 * @param orgnums
	 * @param list
	 * @param level1
	 * @param level2
	 * @param level3
	 * @param level4
	 * @param level5
	 */
	private void sendMessage(List<String> orgnums, List<Record> list,
			List<Record> level1, List<Record> level2, List<Record> level3,
			List<Record> level4, List<Record> level5, Map<String, Object> map) {
		String role_name1 = "领导-二级分行";
		String role_name2 = "领导-中心支行";
		String role_name3 = "领导-责任中心";
		if (orgnums != null && !orgnums.isEmpty()) {
			for (int i = 0; i < orgnums.size(); i++) {
				String orgnum = orgnums.get(i);
				String orgname = list.get(i).getStr("orgname");
				int level1_count = getLevelCount(level1, orgnum);
				int level2_count = getLevelCount(level2, orgnum);
				int level3_count = getLevelCount(level3, orgnum);
				int level4_count = getLevelCount(level4, orgnum);
				int level5_count = getLevelCount(level5, orgnum);
				int count = level1_count + level2_count + level3_count
						+ level4_count + level5_count;
				if (count == 0) {
					continue;
				}
				String sm_cont = "[公金联盟]本月" + orgname + "五层潜在客户" + count
						+ "户,一至五层分别为" + level1_count + "/" + level2_count + "/"
						+ level3_count + "/" + level4_count + "/"
						+ level5_count + "。请积极推动实现转化。";
				// ---------查找角色为领导-二级分行的用户----------
				List<Record> userLeaderInfos1 = messageServer
						.findUsersByOrgAndRole(orgnum, role_name1, 1);// 1代表二级分行
				if (userLeaderInfos1 != null && !userLeaderInfos1.isEmpty()) {
					SendMessageUtil.sendMessage(userLeaderInfos1, sm_cont, map);
				}
				// ----------查找角色为领导-中心支行的用户---------
				List<Record> userLeaderInfos2 = messageServer
						.findUsersByOrgAndRole(orgnum, role_name2, 2);// 2代表中心支行
				if (userLeaderInfos2 != null && !userLeaderInfos2.isEmpty()) {
					SendMessageUtil.sendMessage(userLeaderInfos2, sm_cont, map);
				}
				// -----------查找角色为领导-责任中心的用户---------
				List<Record> userLeaderInfos3 = messageServer
						.findUsersByOrgAndRole(orgnum, role_name3, 3);// 3代表责任中心
				if (userLeaderInfos3 != null && !userLeaderInfos3.isEmpty()) {
					SendMessageUtil.sendMessage(userLeaderInfos3, sm_cont, map);
				}
			}
		}
	}

	/**
	 * 根据机构号获取每层潜在客户数
	 * 
	 * @param level
	 * @param orgnum
	 * @return
	 */
	private int getLevelCount(List<Record> level, String orgnum) {
		int level_count = 0;
		if (level != null && !level.isEmpty()) {
			for (int j = 0; j < level.size(); j++) {
				Record record = level.get(j);
				if (orgnum.equals(record.getStr("orgnum"))) {
					level_count = record.getBigDecimal("count_num").intValue();
					break;
				}
			}
		}
		return level_count;
	}

	/**
	 * 触发事件--客户池推送至二级行
	 * 
	 * @param map2
	 */
	private void sendMessage(Map<String, Object> map2) {
		// 查找本月目标的机构归属
		List<Record> orgnums = messageServer.findOrgnums();
		List<Map<String, Object>> list = null;
		if (orgnums != null && !orgnums.isEmpty()) {
			// 根据机构号查找本月 待认领客户中他行客户数量， 待认领客户中我行客户数量，推送入池的待认领客户数量，推送目标的机构归属
			list = messageServer.findSendMeesInfo(orgnums);
		} else {
			System.out.println("-----触发事件--客户池推送至二级行，查找本月目标的机构归属为空");
			log.warn("触发事件--客户池推送至二级行，查找本月目标的机构归属为空");
			return;
		}
		String role_name = "领导-二级分行";
		String role_name1 = "业务人员-二级分行";
		String role_name2 = "领导-中心支行";
		String role_name3 = "领导-责任中心";
		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String orgnum = map.get("orgnum").toString();
				String orgname = map.get("orgname").toString();
				String other_count = map.get("other_count").toString();
				String my_count = map.get("my_count").toString();
				String count = map.get("count").toString();
				if (Integer.parseInt(count) == 0) {
					continue;
				}
				// ----------角色为领导-二级分行的用户-----------
				List<Record> userLeaderInfos = messageServer
						.findUsersByOrgAndRole(orgnum, role_name, 1);// 1代表二级分行
				if (userLeaderInfos != null && !userLeaderInfos.isEmpty()) {
					String sm_cont = "[公金联盟]本月" + count + "户待认领客户已入" + orgname
							+ "分行客户池，" + "其中他行客户" + other_count + "户，存量客户"
							+ my_count + "户。";
					SendMessageUtil.sendMessage(userLeaderInfos, sm_cont, map2);
				}
				// ----------角色为业务人员-二级分行的用户----------
				List<Record> userLeaderInfos1 = messageServer
						.findUsersByOrgAndRole(orgnum, role_name1, 1);// 1代表二级分行
				if (userLeaderInfos1 != null && !userLeaderInfos1.isEmpty()) {
					String sm_cont = "[公金联盟]本月" + count + "户待认领客户已入" + orgname
							+ "分行客户池，" + "其中他行客户" + other_count + "户，存量客户"
							+ my_count + "户。请及时认领哟。";
					SendMessageUtil
							.sendMessage(userLeaderInfos1, sm_cont, map2);
				}
				// -----------角色为领导-中心支行的用户------------
				List<Record> userLeaderInfos2 = messageServer
						.findUsersByOrgAndRole(orgnum, role_name2, 2);// 2代表中心支行
				if (userLeaderInfos2 != null && !userLeaderInfos2.isEmpty()) {
					String sm_cont = "[公金联盟]本月" + count + "户待认领客户已入" + orgname
							+ "分行客户池，" + "其中他行客户" + other_count + "户，存量客户"
							+ my_count + "户。请督促业务人员及时认领。";
					SendMessageUtil
							.sendMessage(userLeaderInfos2, sm_cont, map2);
				}
				// ------------角色为领导-责任中心的用户------------
				List<Record> userLeaderInfos3 = messageServer
						.findUsersByOrgAndRole(orgnum, role_name3, 3);// 3代表责任中心
				if (userLeaderInfos3 != null && !userLeaderInfos3.isEmpty()) {
					String sm_cont = "[公金联盟]本月" + count + "户待认领客户已入" + orgname
							+ "分行客户池，" + "其中他行客户" + other_count + "户，存量客户"
							+ my_count + "户。请督促业务人员及时认领。";
					SendMessageUtil
							.sendMessage(userLeaderInfos3, sm_cont, map2);
				}
			}
		} else {
			System.out.println("------触发事件--客户池推送至二级行,查询的推送数据为空");
			log.warn("触发事件--客户池推送至二级行,查询的推送数据为空");
		}
	}
}
