package com.goodcol.server.zxglserver;

import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Page;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.zxgldbutil.PccmKpiDBUtil;

public class CustKpiServer {

	PccmKpiDBUtil dbUtil = new PccmKpiDBUtil();
	/**
	 * 列表页面
	 */
	public Page<Record> pageList(Map<String, Object> map){
		return dbUtil.pageList(map);
	}
	
	/**
	 * 列表
	 */
	public List<Record> findList(Map<String, Object> map){
		return dbUtil.findList(map);
	}
	
	/**
	 * 客户详情
	 */
	public Page<Record> pageDetail(Map<String, Object> map){
		return dbUtil.pageDetail(map);
	}
	
	/**
	 * 根据客户经理号查询客户经理类型
	 */
	public String getMgrType(String mgr_no){
		return dbUtil.getMgrType(mgr_no);
	}
	
	/**
	 * kpi折线图查询
	 */
	public List<Record> kpiLineData(String mgr_id){
		return dbUtil.kpiLineData(mgr_id);
	}
	
	/**
	 * kpi饼图查询
	 */
	public List<Record> clasFiveList(Map<String, Object> map){
		return dbUtil.clasFiveList(map);
	}
	
	/**
	 * 查询平移值，晋升值，中位置
	 */
	public Record getDatas(String mgr_id) {
		return dbUtil.getDatas(mgr_id);
	}
	
	/**
	 * 获取期数下拉框到的值
	 */
	public List<Record> getDateDatas() {
		return dbUtil.getDateDatas();
	}
	
	
	/**
	 * 查询商机转化率数据
	 * 2018年6月26日09:59:44
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回商机转化率数据
	 */
	public Page<Record> findMarketCustomer(Map<String, Object> map){
		return dbUtil.findMarketCustomer(map);
	}
	
	/**
	 * 查询商机客户数、转化客户数以及中心支行排名
	 * 2018年6月26日11:08:31
	 * @author liutao
	 * @return
	 */
	/*public Record findMarketCustomerRank(String userNo, String orgLevel){
		return dbUtil.findMarketCustomerRank(userNo, orgLevel);
	}*/
	public Record findMarketCustomerRank(String userNo){
		return dbUtil.findMarketCustomerRank(userNo);
	}
	
	/**
	 * 查询所有的潜在客户经理
	 * 2018年7月9日09:40:51
	 * @author liutao
	 * @return
	 */
	public List<Record> findLatentUser(){
		return dbUtil.findLatentUser();
	}
	
	/**
	 * 根据用户登录名查询潜在客户相关数(潜在客户达成数和潜在客户总数)
	 * 2018年7月9日09:46:07
	 * @author liutao
	 * @param user_no
	 * @return
	 */
	public int findLatentNumByUserNo(String user_no, String flag){
		return dbUtil.findLatentNumByUserNo(user_no, flag);
	}
	
	/**
	 * 根据用户名查询用户省行，支行，分行非排名
	 * 2018年7月9日11:25:17
	 * @author liutao
	 * @param user_no
	 * @return
	 */
	public int findUserRank(String user_no, String orgLevel){
		return dbUtil.findUserRank(user_no, orgLevel);
	}
	
	/**
	 * 保存客户经理潜在客户总数、潜在达成数以及潜在客户达成率
	 * 2018年7月9日10:08:33
	 * @author liutao
	 */
	public void saveUserLatent(Record r){
		dbUtil.saveUserLatent(r);
	}
	
	/**
	 * 根据登录用户查询潜在客户相关数以及个人排名
	 * 2018年7月9日15:04:03
	 * @author liutao
	 * @param userNo
	 * @return
	 */
	public Record findLatentCustomerRank(String userNo){
		return dbUtil.findLatentCustomerRank(userNo);
	}
	
	/**
	 * 查询潜在客户数据
	 * 2018年6月26日09:59:44
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回潜在客户数据
	 */
	public Page<Record> findLatentCustomer(Map<String, Object> map){
		return dbUtil.findLatentCustomer(map);
	}
	
	/**
	 * 根据用户登录名查询潜在客户相关数(潜在客户达成数和潜在客户总数)
	 * 2018年7月12日11:21:22
	 * @author liutao
	 * @param user_no
	 * @return
	 */
	public int findMarketNumByUserNo(String user_no, String flag){
		return dbUtil.findMarketNumByUserNo(user_no, flag);
	}
	
	/**
	 * 根据用户名查询用户省行，支行，分行非排名
	 * 2018年7月12日11:21:16
	 * @author liutao
	 * @param user_no
	 * @return
	 */
	public int findUserMarketRank(String user_no, String orgLevel){
		return dbUtil.findUserMarketRank(user_no, orgLevel);
	}
	
	/**
	 * 保存客户经理潜在客户总数、潜在达成数以及潜在客户达成率
	 * 2018年7月12日11:31:16
	 * @author liutao
	 */
	public void saveUserMarket(Record r){
		dbUtil.saveUserMarket(r);
	}
	
	/**
	 * 查询机构下的商机转化率排名数据等(查询机构商机转化客户数，机构商机总数，以及转化率和排名等)
	 * 2018年7月11日19:33:53
	 * @author liutao
	 * @return
	 */
	/*public Page<Record> findOrgMarketRank(int pageNum, int pageSize, String org_id){
		return dbUtil.findOrgMarketRank(pageNum, pageSize, org_id);
	}*/
	
	/**
	 * 查询机构下的商机转化率排名数据等(查询机构商机转化客户数，机构商机总数，以及转化率和排名等)
	 * 2018年7月11日19:33:53
	 * @author liutao
	 * @return
	 */
	public List<Record> findOrgMarketRank(String org_id){
		return dbUtil.findOrgMarketRank(org_id);
	}
	
	/**
	 * 查询四级机构下的商机转化率数据
	 * 2018年7月17日16:46:16
	 * @author liutao
	 * @param org_id
	 * @return
	 * 返回结果例如：
	 * 张三    3   4   75.00
	 * 李四    1   5   20.00
	 */
	public List<Record> findOrgUserMarketRank(String org_id){
		return dbUtil.findOrgUserMarketRank(org_id);
	}
	
	/**
	 * 查询某个机构下所有的商机客户总数和所有已经转化的商机客户数
	 * 2018年7月17日11:11:19
	 * @author liutao
	 * @param org_id 机构id
	 * @return
	 */
	public Record findOrgMarketSum(String org_id){
		return dbUtil.findOrgMarketSum(org_id);
	}
	
	/**
	 * 查询某个四级机构下所有的商机客户总数和所有已经转化的商机客户数
	 * 2018年7月17日16:59:28
	 * @author liutao
	 * @param org_id 机构id
	 * @return
	 */
	/*public Record findOrgUserMarketSum(String org_id){
		return dbUtil.findOrgUserMarketSum(org_id);
	}*/
	
	/**
	 * 查询机构下的潜在达成率排名数据等(查询机构潜在达成客户数，机构潜在总数，以及达成率和排名等)
	 * 2018年7月11日19:36:44
	 * @author liutao
	 * @return
	 */
	/*public Page<Record> findOrgLatentRank(int pageNum, int pageSize, String org_id){
		return dbUtil.findOrgLatentRank(pageNum, pageSize, org_id);
	}*/
	/**
	 * 查询机构下的潜在达成率排名数据等(查询机构潜在达成客户数，机构潜在总数，以及达成率和排名等)
	 * 2018年7月11日19:36:44
	 * @author liutao
	 * @return
	 */
	public List<Record> findOrgLatentRank(String org_id){
		return dbUtil.findOrgLatentRank(org_id);
	}
	
	/**
	 * 查询四级机构下的潜在达成率数据
	 * 2018年7月17日16:46:16
	 * @author liutao
	 * @param org_id
	 * @return
	 * 返回结果例如：
	 * 张三    3   4   75.00
	 * 李四    1   5   20.00
	 */
	public List<Record> findOrgUserLatentRank(String org_id){
		return dbUtil.findOrgUserLatentRank(org_id);
	}
	
	/**
	 * 查询某个机构下所有的潜在客户总数和所有已经达成的潜在客户数
	 * 2018年7月17日11:11:19
	 * @author liutao
	 * @param org_id 机构id
	 * @return
	 */
	public Record findOrgLatentSum(String org_id){
		return dbUtil.findOrgLatentSum(org_id);
	}
	
	/**
	 * 查询某个四级机构下所有的潜在客户总数和所有已经达成的潜在客户数
	 * 2018年7月17日16:59:28
	 * @author liutao
	 * @param org_id 机构id
	 * @return
	 */
	public Record findOrgUserLatentSum(String org_id){
		return dbUtil.findOrgUserLatentSum(org_id);
	}
	
	/**
	 * 根据EHR号和机构等级查询所在机构号
	 * 2018年7月17日10:00:23
	 * @author liutao
	 * @return 返回机构号id
	 */
	public String findOrgIdByUser(String userNo, String OrgLevel, String type){
		return dbUtil.findOrgIdByUser(userNo, OrgLevel, type);
	}
	
	/**
	 * 根据机构号查询机构名称
	 * 2018年7月17日14:10:30
	 * @author liutao
	 * @param orgId 机构号id
	 * @return
	 */
	public String findOrgNameByOrgId(String orgId){
		return dbUtil.findOrgNameByOrgId(orgId);
	}
	
	/**
	 * 查询机构商机转化率数据
	 * 2018年7月13日10:57:01
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回机构商机转化率数据
	 */
	public Page<Record> findOrgMarketCustomer(Map<String, Object> map){
		return dbUtil.findOrgMarketCustomer(map);
	}
	
	/**
	 * 查询机构潜在客户数据
	 * 2018年7月12日10:51:25
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回机构潜在客户数据
	 */
	public Page<Record> findOrgLatentCustomer(Map<String, Object> map){
		return dbUtil.findOrgLatentCustomer(map);
	}
	
	/**
	 * 下载个人的商机客户数据
	 * 2018年7月18日20:46:18
	 * @author liutao
	 * @param map
	 * @return
	 */
	public List<Record> downloadPersonMarket(Map<String, Object> map){
		return dbUtil.downloadPersonMarket(map);
	}
	
	/**
	 * 查询机构商机转化率数据
	 * 2018年7月13日10:57:01
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回机构商机转化率数据
	 */
	public List<Record> downloadOrgMarket(Map<String, Object> map){
		return dbUtil.downloadOrgMarket(map);
	}
	
	/**
	 * 下载个人潜在客户数据
	 * 2018年7月19日09:57:48
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回潜在客户数据
	 */
	public List<Record> downloadPersonLatent(Map<String, Object> map){
		return dbUtil.downloadPersonLatent(map);
	}
	
	/**
	 * 下载机构潜在客户数据
	 * 2018年7月19日10:00:51
	 * @author liutao
	 * @param map 查询条件参数
	 * @return 返回机构潜在客户数据
	 */
	public List<Record> downloadOrgLatent(Map<String, Object> map){
		return dbUtil.downloadOrgLatent(map);
	}
	
	/**
	 * 根据机构号id查询上级机构号
	 * 2018年7月20日14:51:16
	 * @author liutao
	 * @param orgnum 机构号id
	 * @return
	 */
	public String findParentOrgNum(String orgnum){
		return dbUtil.findParentOrgNum(orgnum);
	}
}
