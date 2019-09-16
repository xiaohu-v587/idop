/**
 * 
 */
package com.goodcol.server.zxglserver;

import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.SendMessageUtil;
import com.goodcol.util.zxgldbutil.PccmSendMessageDBUtil;

/**
 * @author dinggang
 * 
 */
public class PccmSendMessageServer {
	private PccmSendMessageDBUtil pccmSendMessageDBUtil = new PccmSendMessageDBUtil();
	/**
	 * 查找本月推送目标的机构归属
	 * @return
	 */
	public List<Record> findOrgnums() {
		return pccmSendMessageDBUtil.findOrgnums();
	}
	/**
	 * 根据机构号查找本月 待认领客户中他行客户数量， 待认领客户中我行客户数量，推送入池的待认领客户数量，推送目标的机构归属
	 * @param orgnums
	 * @return
	 */
	public List<Map<String, Object>> findSendMeesInfo(List<Record> orgnums) {
		return pccmSendMessageDBUtil.findSendMeesInfo(orgnums);
	}

	// public List<Record> findUserLeaderInfos(String orgnum, String role_name)
	// {
	// return pccmSendMessageDBUtil.findUserLeaderInfos(orgnum, role_name);
	// }
	/**
	 * 查找短信发送的IP和端口
	 * @return
	 */
	public Map<String, Object> findMessIpAndPort() {
		return pccmSendMessageDBUtil.findMessIpAndPort();
	}

	/**
	 * 查找本月推送目标的机构归属，每层潜在客户数
	 * 
	 * @return
	 */
	public List<Record> findPotentialCusts() {
		return pccmSendMessageDBUtil.findPotentialCusts();
	}

	/**
	 * 待营销天数
	 * 
	 * @return
	 */
	public int findWeekDay() {
		return pccmSendMessageDBUtil.findWeekDay();
	}

	/**
	 * 查找本月推送目标的机构归属
	 * 
	 * @return
	 */
	public List<Record> findUclaimCustsOrgs() {
		return pccmSendMessageDBUtil.findUclaimCustsOrgs();
	}
	/**
	 * 查找本月 待认领客户中他行客户数量，待认领客户中我行客户数量，推送入池的待认领客户数量，推送目标的机构归属
	 * @param third_orgnums
	 * @return
	 */
	public List<Map<String, Object>> findUnclaimMeesInfo(
			List<Record> third_orgnums) {
		return pccmSendMessageDBUtil.findUnclaimMeesInfo(third_orgnums);
	}
	/**
	 * 被分派潜在或商机客户的业务人员
	 * @return
	 */
	public List<Record> findCustsByLeader() {
		return pccmSendMessageDBUtil.findCustsByLeader();
	}
	/**
	 * 查找领导未按时将入池客户分配，直接分配领导个人
	 * @return
	 */
	public List<Record> findCustsToLeader() {
		return pccmSendMessageDBUtil.findCustsToLeader();
	}
	/**
	 * 归属客户经理（待办任务）
	 * @return
	 */
	public List<Record> findPotentialCustsToManager() {
		return pccmSendMessageDBUtil.findPotentialCustsToManager();
	}
	/**
	 * 二级池待认领时间+三级池待认领时间
	 * @return
	 */
	public int findAllWeekDay() {
		return pccmSendMessageDBUtil.findAllWeekDay();
	}
	/**
	 * 截至当前各个机构中心支行客户池未按时分配的客户数
	 * @return
	 */
	public List<Record> findUnclaimCustCount() {
		return pccmSendMessageDBUtil.findUnclaimCustCount();
	}
	/**
	 * 根据角色名称查找用户
	 * @param role_name
	 * @return
	 */
	public List<Record> findUserLeaders(String role_name) {
		return pccmSendMessageDBUtil.findUserLeaders(role_name);
	}

	public void sendCustManagerInfo(Map<String, Object> map) {
		// 待营销天数
		int cust_task_limit = pccmSendMessageDBUtil.findWeekDay();
		// 当前日期
		String now_day = DateTimeUtil.getDateTime();
		// 找出认领后目标人员进行营销，处于待营销状态
		List<Record> records = pccmSendMessageDBUtil.findCustManagers();
		if (records != null && !records.isEmpty()) {
			for (int i = 0; i < records.size(); i++) {
				Record record = records.get(i);
				String phone = record.getStr("phone");
				String customername = record.getStr("customername");
				String claim_time = record.getStr("claim_time");
				claim_time = claim_time.substring(0, 8);
				// 计算待营销截至日期
				String end_day = DateTimeUtil.getDistanceDays1(claim_time,
						cust_task_limit);
				// 计算待营销截至日期减去当前日期，获得天数
				int day = DateTimeUtil.getSpaceDate(now_day, end_day);
				if (day < 0) {
					continue;
				}
				String sm_cont = "【公金联盟】距离" + customername + "商机客户营销结果反馈截止日期还有"
						+ day + "天，" + end_day + "仍未反馈，系统将自动判定营销失败，请及时关注！";
				SendMessageUtil.sendMessage(phone, "01", sm_cont, map);
			}
		} else {
			System.out.println("查出认领后目标人员进行营销，处于待营销状态的记录为空");
		}
	}

	/**
	 * 申诉待处理事件数量
	 * 
	 * @return
	 */
	public int findAppeal() {
		return pccmSendMessageDBUtil.findAppeal();
	}

	/**
	 * 本月全省相同角色人员总数
	 * 
	 * @param role_id
	 * @param timePoint
	 * @return
	 */
	public int findAllUsers(String roleName, String timePoint) {
		return pccmSendMessageDBUtil.findAllUsers(roleName, timePoint);
	}

	/**
	 * 查找机构的上月商机客户转化率，商机排名，潜在客户转化率，潜在排名
	 * 
	 * @param timePoint
	 * @return
	 */
	public List<Record> findMessageToLeader(String timePoint) {
		return pccmSendMessageDBUtil.findMessageToLeader(timePoint);
	}

	/**
	 * 查询领导
	 * 
	 * @param org_id
	 * @param role_name
	 * @param flag
	 * @return
	 */
	public List<Record> findLeadersFromOrg(String org_id, String role_name,
			String flag) {
		return pccmSendMessageDBUtil
				.findLeadersFromOrg(org_id, role_name, flag);
	}

	/**
	 * 查找上月全省机构总数
	 * 
	 * @param timePoint
	 * @return
	 */
	public int findCountOrgs(String timePoint) {
		return pccmSendMessageDBUtil.findCountOrgs(timePoint);
	}
	/**
	 * 根据角色名称查询人员
	 * @param roleName
	 * @return
	 */
	public List<Record> findClaimUsers(String roleName) {
		return pccmSendMessageDBUtil.findClaimUsers(roleName);
	}
	/**
	 * 根据机构号、角色名称、机构级别查询用户
	 * @param orgnum
	 * @param role_name
	 * @param deptlevel
	 * @return
	 */
	public List<Record> findUsersByOrgAndRole(String orgnum, String role_name,
			int deptlevel) {
		return pccmSendMessageDBUtil.findUsersByOrgAndRole(orgnum, role_name,
				deptlevel);
	}

	/**
	 * 判断开户是否营销成功
	 */
	public void updateCustByOpen() {
		pccmSendMessageDBUtil.updateCustByOpen();
	}
	/**
	 * 本月人员的潜在客户达成率及排名，商机客户转化率及排名
	 * @param timePoint
	 * @param roleName
	 * @return
	 */
	public List<Record> findAllPersonLatentMark(String timePoint, String roleName) {
		return pccmSendMessageDBUtil.findAllPersonLatentMark(timePoint,roleName);
	}

}
