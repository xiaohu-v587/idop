package com.goodcol.server.zxglserver;

import java.util.List;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.zxgldbutil.PccmKPIParaDBUtil;

public class PccmKPIParaServer {
	private PccmKPIParaDBUtil dbUtil = new PccmKPIParaDBUtil();

	/**
	 * 获取KPI明星客户经理 2018年5月28日15:19:00
	 * 
	 * @author liutao
	 * @param period
	 *            期次
	 * @return 返回KPI明星客户
	 */
	public List<Record> findKPIStarCustomer(String roleType) {
		// 计算上月期次
		String period = DateTimeUtil.getLastMonth();

		return dbUtil.findKPIStarCustomer(period, roleType);
	}

	/**
	 * 获取过去的所有期次 2018年5月29日10:32:15
	 * 
	 * @author liutao
	 */
	public List<Record> getPeriod() {

		return dbUtil.getPeriod();
	}

	/**
	 * 根据页面条件查询所有的KPI 2018年5月29日11:31:47
	 * 
	 * @author liutao
	 * @return
	 */
	public Page<Record> getAllKPIList(int pageNum, int pageSize,
			String userName, String userNo, String orgId, String period) {

		return dbUtil.getAllKPIList(pageNum, pageSize, userName, userNo, orgId,
				period);
	}

	public Page<Record> getList(Record record) {

		return dbUtil.getList(record);
	}

	public void del(String ids) {

		dbUtil.del(ids);
	}

	public Record findKpiParam(String id) {

		return dbUtil.findKpiParam(id);
	}

	public List<Record> findKpiParaDetail(String id) {

		return dbUtil.findKpiParaDetail(id);
	}

	public String saveKpiParam(Record record) {

		return dbUtil.saveKpiParam(record);
	}

	public void saveKpiParamDetail(Record r) {

		dbUtil.saveKpiParamDetail(r);
	}

	/**
	 * 根据用户查询其客户的贷款总和以及存款总和 2018年5月31日14:37:16
	 * 
	 * @param custId
	 *            用户id
	 * @param timePoint
	 *            时间点(1：上周；2：当前；3：上月；4：上年)
	 */
	public Record findFundByCustId(String userId, String timePoint) {

		return dbUtil.findFundByCustId(userId, timePoint);
	}

	/**
	 * 根据用户查询其客户的价值客户数或成功营销客户数以及总营销客户数 2018年5月31日15:01:31
	 * 
	 * @author liutao
	 * @param custId
	 *            用户id
	 * @param timePoint
	 *            时间点(1：上周；2：当前；3：上月；4：上年)
	 * @param type
	 *            查询类型 (1：查询价值客户数；2：查询成功营销客户数；3：查询总营销客户数)
	 * @return
	 */
	public Record findCustomerNumByCustId(String custId, String timePoint,
			String type) {

		return dbUtil.findCustomerNumByCustId(custId, timePoint, type);
	}

	/**
	 * 根据用户查询KPI 2018年5月31日15:22:34
	 * 
	 * @author liutao
	 * @param custNo
	 *            用户登录名(EHR号)
	 * @param timePoint
	 *            时间点(1：上周；2：当前；3：上月；4：上年)
	 * @return
	 */
	public Record findKPIByCustNo(String userNo, String timePoint) {

		return dbUtil.findKPIByCustNo(userNo, timePoint);
	}

	/**
	 * 查询营销达成率明星客户经理 2018年6月1日10:43:53
	 * 
	 * @author liutao
	 * @return
	 */
	public List<Record> findReachStarCustomer(String roleType) {

		return dbUtil.findReachStarCustomer(roleType);
	}

	/**
	 * 待办任务---营销客户 2018年6月4日16:04:25
	 * 
	 * @author liutao
	 * @return 返回所有未营销的客户
	 */
	public Page<Record> findReachCustomer(int pageNum, int pageSize,
			String custNo, String custName, String userNo) {

		return dbUtil.findReachCustomer(pageNum, pageSize, custNo, custName,
				userNo);
	}

	/**
	 * 待办任务---潜在客户 2018年6月4日16:30:53
	 * 
	 * @author liutao
	 * @return 返回所有潜在客户
	 */
	public Page<Record> findLurkCustomer(int pageNum, int pageSize,
			String custNo, String custName, String userNo) {

		return dbUtil.findLurkCustomer(pageNum, pageSize, custNo, custName,
				userNo);
	}

	/**
	 * 查询工作日历 2018年6月6日15:53:56
	 * 
	 * @author liutao
	 * @return
	 */
	public List<Record> findCalendar(String userId, String userNo) {

		return dbUtil.findCalendar(userId, userNo);
	}

	/**
	 * 查询首页代办任务数（商机客户待处理数、价值客户待处理数、客户池待认领/分配数） 2018年6月7日16:37:06
	 * 
	 * @author liutao
	 * @return
	 */
	public Record findTaskNum(String userNo) {
		return dbUtil.findTaskNum(userNo);
	}

	/**
	 * 查询菜单信息 2018年6月8日14:33:26
	 * 
	 * @author liutao
	 */
	public Record findMenuInfoByUrl(String url) {
		return dbUtil.findMenuInfoByUrl(url);
	}

	/**
	 * 查找工作日历的所有任务 2018年6月9日09:24:21
	 * 
	 * @param pageNum
	 *            当前页面
	 * @param pageSize
	 *            每页数量
	 * @param custNo
	 *            客户号
	 * @param custName
	 *            客户名称
	 * @param userNo
	 *            用户登录名
	 * @author liutao
	 */
	public Page<Record> findAllCalendarTask(int pageNum, int pageSize,
			String custNo, String custName, String userNo) {
		return dbUtil.findAllCalendarTask(pageNum, pageSize, custNo, custName,
				userNo);
	}

	/**
	 * 查询我行客户详情 2018年6月9日14:10:11
	 * 
	 * @author liutao
	 */
	public Record findMineCustDetail(String pcpId,String period) {
		return dbUtil.findMineCustDetail(pcpId,period);
	}

	public List<Record> findKpiParams(Record record) {
		return dbUtil.findKpiParams(record);
	}

	/**
	 * 查询他行客户详情 2018年6月9日14:55:21
	 * 
	 * @author liutao
	 */
	public Record findCustDetail(String pcpId) {
		return dbUtil.findCustDetail(pcpId);
	}

	/**
	 * 待办任务---潜在客户--数据下载 2018年7月2日14:47:05
	 * 
	 * @author liutao
	 * @return 返回所有潜在客户
	 */
	public List<Record> download(String custNo, String custName, String userNo) {
		return dbUtil.download(custNo, custName, userNo);
	}

	/**
	 * 待办任务---潜在客户--数据下载 2018年7月2日14:47:05
	 * 
	 * @author liutao
	 * @return 返回所有潜在客户
	 */
	public List<Record> downloadReach(String custNo, String custName,
			String userNo) {
		return dbUtil.downloadReach(custNo, custName, userNo);
	}

	/**
	 * 查询雷达图个人加权数 2018年7月4日16:03:46
	 * 
	 * @author liutao
	 * @param userNo
	 *            当前登录人EHR号
	 * @return 返回个人加权数
	 */
	public Record findPersonWeighting(String userNo, String timePoint) {
		return dbUtil.findPersonWeighting(userNo, timePoint);
	}

	/**
	 * 查询个人的商机客户转化率 2018年7月4日16:31:21
	 * 
	 * @author liutao
	 * @param userNo
	 *            当前登录人EHR号
	 * @return 返回个人的商机客户转化率
	 */
	public Record findPersonMarkReach(String userNo, String timePoint) {
		return dbUtil.findPersonMarkReach(userNo, timePoint);
	}

	/**
	 * 查询个人的潜在客户达成率数据 2018年7月4日18:01:22
	 * 
	 * @author liutao
	 * @param userNo
	 *            当前登录人EHR号
	 * @return 返回个人的潜在客户达成率数据
	 */
	public Record findPersonLatentReach(String userNo, String timePoint) {
		return dbUtil.findPersonLatentReach(userNo, timePoint);
	}

	/**
	 * 查询用户是否有领导角色 2018年7月5日11:09:08
	 * 
	 * @author liutao
	 * @return 返回查询到的领导角色，如果没有返回空
	 */
	public List<Record> findWhetherLead(String userNo) {
		return dbUtil.findWhetherLead(userNo);
	}

	/**
	 * 查询首页机构雷达图累计存款日均以及累计贷款日均 2018年7月5日15:10:41
	 * 
	 * @author liutao
	 * @param timePoint
	 *            查询时间点
	 * @return 返回机构累计存眷日均以及累计贷款日均
	 */
	public Record findOrgMoneyday(String userNo, String leadRole,
			String timePoint) {
		return dbUtil.findOrgMoneyday(userNo, leadRole, timePoint);
	}

	/**
	 * 查询首页机构雷达图加权数据 2018年7月5日16:14:01
	 * 
	 * @author liutao
	 * @param timePoint
	 *            查询时间点
	 * @param userNo
	 *            当前登录人EHR号
	 * @param leadRole
	 *            领导角色级别
	 * @return 返回机构加权
	 */
	public Record findOrgWeighting(String userNo, String leadRole,
			String timePoint) {
		return dbUtil.findOrgWeighting(userNo, leadRole, timePoint);
	}

	/**
	 * 查询首页机构雷达图商机转化率数据 2018年7月6日09:39:42
	 * 
	 * @author liutao
	 * @param userNo
	 *            当前登录人EHR号
	 * @param leadRole
	 *            领导角色级别
	 * @param timePoint
	 *            查询时间点
	 * @return 返回机构雷达图商机转化率数据
	 */
	public Record findOrgMarkReach(String userNo, String leadRole,
			String timePoint) {
		return dbUtil.findOrgMarkReach(userNo, leadRole, timePoint);
	}

	/**
	 * 查询首页机构雷达图潜在客户达成率数据 2018年7月6日10:18:47
	 * 
	 * @author liutao
	 * @param userNo
	 *            当前登录人EHR号
	 * @param leadRole
	 *            领导角色级别
	 * @param timePoint
	 *            查询时间点
	 * @return 返回潜在客户达成率数据
	 */
	public Record findOrgLatentReach(String userNo, String leadRole,
			String timePoint) {
		return dbUtil.findOrgLatentReach(userNo, leadRole, timePoint);
	}

	/**
	 * 查询首页雷达图机构领导视图数据 2018年8月8日18:11:40
	 * 
	 * @author liutao
	 * @param userNo
	 * @param leadRole
	 *            机构等级
	 * @param timePoint
	 * @return
	 */
	public Record findOrgRadarMap(String userNo, String leadRole,
			String timePoint) {
		return dbUtil.findOrgRadarMap(userNo, leadRole, timePoint);
	}

	public int findAllPersonLatentReach(String user_no, String role_id,
			String timePoint) {
		return dbUtil.findAllPersonLatentReach(user_no, role_id, timePoint);
	}

	public int findAllPersonMarkReach(String user_no, String role_id,
			String timePoint) {
		return dbUtil.findAllPersonMarkReach(user_no, role_id, timePoint);
	}

	/**
	 * 查询个人雷达图相关数据 2018年9月1日10:20:39
	 * 
	 * @author liutao
	 * @param userNo
	 * @param timePoint
	 * @return
	 */
	public Record findPersonRadarMap(String userNo, String timePoint) {
		return dbUtil.findPersonRadarMap(userNo, timePoint);
	}

	/**
	 * 查询潜在达成率明星客户经理 2018年9月7日11:16:25
	 * 
	 * @author liutao
	 * @return
	 */
	public List<Record> findReachStarLatent(String roleType) {
		return dbUtil.findReachStarLatent(roleType);
	}

	/**
	 * //该业务人员的潜在客户达成率及排名，商机客户转化率及排名
	 * 
	 * @param user_no
	 * @param timePoint
	 * @param role_id 
	 * @return
	 */
	public Record findPersonLatentMark(String user_no, String timePoint, String roleName) {
		return dbUtil.findPersonLatentMark(user_no, timePoint,roleName);
	}
}
