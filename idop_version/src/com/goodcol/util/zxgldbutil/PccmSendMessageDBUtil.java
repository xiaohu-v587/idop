/**
 * 
 */
package com.goodcol.util.zxgldbutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.DateTimeUtil;
import com.goodcol.util.date.BolusDate;

/**
 * @author dinggang
 * 
 */
public class PccmSendMessageDBUtil {
	private static final String DEFAULT = "default";
	private static final String GBASE = "gbase";

	/**
	 * 查找本月推送目标的机构归属
	 * 
	 * @return
	 */
	public List<Record> findOrgnums() {
		String indate = BolusDate.getDateTime("yyyyMM");
		// orgnum不为空，取出所有九位机构号;orgnum为空，根据统一社会信用代码与机构表的关系取出所有九位机构号
		List<Record> orgnums = Db
				.use(DEFAULT)
				.find("select distinct p.orgnum,s.orgname,s.by2 from pccm_cust_pool p,sys_org_info s where p.orgnum is not null and p.orgnum!='null' "
						+ " and p.orgnum!='NULL' and p.cussts='1' and p.deptlevel='3' and p.orgnum=s.orgnum and p.indate like ? "
						+ " union all "
						+ " select distinct s.orgnum,s.orgname,s.by2 from sys_org_info s,"
						+ "(select o.org_id as orgnum from pccm_cust_pool p,pccm_cust_org_are o "
						+ " where (p.orgnum is null or p.orgnum='null' or p.orgnum='NULL') "
						+ " and p.area_code=o.area_id and p.cussts='1' and p.deptlevel='3' and p.indate like ?) s1 where s.orgnum=s1.orgnum",
						new Object[] { "%" + indate + "%", "%" + indate + "%" });
		return orgnums;
	}

	/**
	 * 根据机构号查找本月 待认领客户中他行客户数量， 待认领客户中我行客户数量，推送入池的待认领客户数量，推送目标的机构归属
	 * 
	 * @param orgnums
	 * @return
	 */
	public List<Map<String, Object>> findSendMeesInfo(List<Record> orgnums) {
		String indate = BolusDate.getDateTime("yyyyMM");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < orgnums.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			Record record = orgnums.get(i);
			// 待认领客户中他行客户数量
			int other_count = Integer
					.parseInt(String
							.valueOf(Db
									.use(DEFAULT)
									.queryBigDecimal(
											"select count(1) from sys_org_info s,"
													+ "(select o.org_id as orgnum from pccm_cust_pool p,pccm_cust_org_are o "
													+ " where (p.orgnum is null or p.orgnum='null' or p.orgnum='NULL') "
													+ " and p.area_code=o.area_id and p.cussts='1' and p.deptlevel='3' and o.org_id=? and p.indate like ?) s1 where s.orgnum=s1.orgnum",
											new Object[] {
													record.getStr("orgnum"),
													"%" + indate + "%" })));
			// 待认领客户中我行客户数量
			int my_count = Integer
					.parseInt(String
							.valueOf(Db
									.use(DEFAULT)
									.queryBigDecimal(
											"select count(1) from pccm_cust_pool p,sys_org_info s "
													+ " where p.orgnum is not null and p.orgnum!='null' and p.orgnum!='NULL' and p.cussts='1' "
													+ " and p.deptlevel='3' and p.orgnum=s.orgnum and p.orgnum=? and p.indate like ?",
											new Object[] {
													record.getStr("orgnum"),
													"%" + indate + "%" })));
			// 推送入池的待认领客户数量
			int count = other_count + my_count;
			map.put("orgnum", record.getStr("orgnum"));
			map.put("orgname", record.getStr("orgname"));
			map.put("by2", record.getStr("by2"));
			map.put("other_count", other_count);
			map.put("my_count", my_count);
			map.put("count", count);
			list.add(map);
		}
		return list;
	}

	/**
	 * 根据机构号、角色名称查找用户
	 * 
	 * @param orgnum
	 * @param role_name
	 * @return
	 */
	// public List<Record> findUserLeaderInfos(String orgnum, String role_name)
	// {
	// List<Record> records = Db
	// .use(DEFAULT)
	// .find("select u.user_no,u.phone from sys_user_role ur,sys_user_info u,sys_role_info r "
	// +
	// " where u.user_no=ur.user_id and r.id=ur.role_id and u.phone is not null and u.phone!='null' and u.phone!='NULL' "
	// + " and r.name=? and u.org_id=?",
	// new Object[] { role_name, orgnum });
	// return records;
	// }

	/**
	 * 查找短信发送的IP和端口
	 * 
	 * @return
	 */
	public Map<String, Object> findMessIpAndPort() {
		Map<String, Object> map = new HashMap<String, Object>();
		String ip = Db.use(DEFAULT).queryStr(
				"select remark from gcms_param_info where key=?",
				new Object[] { "MESSAGE_IP" });
		String port = Db.use(DEFAULT).queryStr(
				"select remark from gcms_param_info where key=?",
				new Object[] { "MESSAGE_PORT" });
		map.put("ip", ip);
		map.put("port", port);
		return map;
	}

	/**
	 * 查找本月推送目标的机构归属，每层潜在客户数
	 * 
	 * @return
	 */
	public List<Record> findPotentialCusts() {
		String indate = BolusDate.getDateTime("yyyyMM");
		List<Record> records = Db
				.use(DEFAULT)
				.find("select s.orgname,p.orgnum,p.count_num,p.clas_five from (select count(clas_potential) as count_num,orgnum,clas_five"
						+ " from pccm_cust_pool where deptlevel='3' and incflg='4' and indate like ? group by orgnum,clas_five)p,"
						+ "sys_org_info s where p.orgnum=s.orgnum",
						new Object[] { "%" + indate + "%" });
		return records;
	}

	/**
	 * 待营销天数
	 * 
	 * @return
	 */
	public int findWeekDay() {
		int cust_task_limit = Integer.parseInt(Db.use(DEFAULT).queryStr(
				"select val from gcms_param_info where key=?",
				new Object[] { "CUST_TASK_LIMIT" }));
		return cust_task_limit;
	}

	/**
	 * 查找本月推送目标的机构归属
	 * 
	 * @return
	 */
	public List<Record> findUclaimCustsOrgs() {
		String indate = BolusDate.getDateTime("yyyyMM");
		// orgnum不为空，取出所有九位机构号;orgnum为空，根据统一社会信用代码与机构表的关系取出所有九位机构号
		List<Record> orgnums = Db
				.use(DEFAULT)
				.find("select distinct p.orgnum,s.orgname,s.by2 from pccm_cust_pool p,sys_org_info s where p.orgnum is not null and p.orgnum!='null' "
						+ " and p.orgnum!='NULL' and p.cussts='1' and p.deptlevel='3' and p.third_indate is not null and p.third_indate!='NULL' and p.third_indate!='null' "
						+ " and p.orgnum=s.orgnum and p.indate like ? union all "
						+ " select distinct s.orgnum,s.orgname,s.by2 from sys_org_info s,"
						+ "(select o.org_id as orgnum from pccm_cust_pool p,pccm_cust_org_are o "
						+ " where (p.orgnum is null or p.orgnum='null' or p.orgnum='NULL') "
						+ " and p.area_code=o.area_id and p.cussts='1' and p.deptlevel='3' and p.third_indate is not null and p.third_indate!='NULL' and p.third_indate!='null' "
						+ " and p.indate like ?) s1 where s.orgnum=s1.orgnum",
						new Object[] { "%" + indate + "%", "%" + indate + "%" });
		return orgnums;
	}

	/**
	 * 查找本月 待认领客户中他行客户数量，待认领客户中我行客户数量，推送入池的待认领客户数量，推送目标的机构归属
	 * 
	 * @param third_orgnums
	 * @return
	 */
	public List<Map<String, Object>> findUnclaimMeesInfo(
			List<Record> third_orgnums) {
		String indate = BolusDate.getDateTime("yyyyMM");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < third_orgnums.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			Record record = third_orgnums.get(i);
			// 待认领客户中他行客户数量
			int other_count = Integer
					.parseInt(String
							.valueOf(Db
									.use(DEFAULT)
									.queryBigDecimal(
											"select count(1) from sys_org_info s,"
													+ "(select o.org_id as orgnum from pccm_cust_pool p,pccm_cust_org_are o "
													+ " where (p.orgnum is null or p.orgnum='null' or p.orgnum='NULL') "
													+ " and p.area_code=o.area_id and p.cussts='1' and p.deptlevel='3' and p.third_indate is not null and "
													+ " p.third_indate!='NULL' and p.third_indate!='null' and o.org_id=? and p.indate like ?) s1 where s.orgnum=s1.orgnum",
											new Object[] {
													record.getStr("orgnum"),
													"%" + indate + "%" })));
			// 待认领客户中我行客户数量
			int my_count = Integer
					.parseInt(String
							.valueOf(Db
									.use(DEFAULT)
									.queryBigDecimal(
											"select count(1) from pccm_cust_pool p,sys_org_info s "
													+ " where p.orgnum is not null and p.orgnum!='null' and p.orgnum!='NULL' and p.cussts='1' "
													+ " and p.deptlevel='3' and p.third_indate is not null and p.third_indate!='NULL' "
													+ " and p.third_indate!='null' "
													+ " and p.orgnum=s.orgnum and p.orgnum=? and p.indate like ?",
											new Object[] {
													record.getStr("orgnum"),
													"%" + indate + "%" })));
			// 推送入池的待认领客户数量
			int count = other_count + my_count;
			map.put("orgnum", record.getStr("orgnum"));
			map.put("orgname", record.getStr("orgname"));
			map.put("by2", record.getStr("by2"));
			map.put("other_count", other_count);
			map.put("my_count", my_count);
			map.put("count", count);
			list.add(map);
		}
		return list;

	}

	/**
	 * 被分派潜在或商机客户的业务人员
	 * 
	 * @return
	 */
	public List<Record> findCustsByLeader() {
		List<Record> records = Db
				.use(DEFAULT)
				.find("select s1.phone,s.count_num from "
						+ "(select c.claim_cust_mgr_id,count(c.claim_cust_mgr_id) as count_num from pccm_cust_pool p,pccm_cust_claim c "
						+ " where p.id=c.cust_pool_id and p.deptlevel='3' and c.marketing_stat='0' and c.del_stat='0' "
						+ " and c.creat_id is not null and c.creat_name is not null "
						+ " group by c.claim_cust_mgr_id) s,"
						+ "(select u.phone,u.user_no from sys_user_info u,sys_role_info r "
						+ " where u.role_id=r.id and u.phone is not null and u.phone!='null' and u.phone!='NULL'"
						+ " and r.name like '%业务人员%') s1"
						+ " where s1.user_no=s.claim_cust_mgr_id");
		return records;
	}

	/**
	 * 查找领导未按时将入池客户分配，直接分配领导个人
	 * 
	 * @return
	 */
	public List<Record> findCustsToLeader() {
		// 查找待认领客户中心支行客户池停留时间参数标准
		int third_week_day = Integer.parseInt(Db.use(DEFAULT).queryStr(
				"select val from gcms_param_info where key=?",
				new Object[] { "THIRD_WEEK_DAY" }));
		// 本月
		String indate = BolusDate.getDateTime("yyyyMM");
		List<Record> records = Db
				.use(DEFAULT)
				.find("select s1.phone,s.count_num from"
						+ "(select c.claim_cust_mgr_id,count(c.claim_cust_mgr_id) as count_num "
						+ " from (select * from pccm_cust_pool where (incflg='1' or incflg='2' or incflg='3')"
						+ " and indate like ? and third_indate is not null "
						+ " and third_indate!='null' and third_indate!='NULL' and cussts='3' and deptlevel='3' and "
						+ " ceil((to_date(to_char(sysdate,'yyyymmdd'),'yyyymmdd')-to_date(third_indate,'yyyymmdd')))>?)p,"
						+ "pccm_cust_claim c where p.id=c.cust_pool_id and c.del_stat='0' group by c.claim_cust_mgr_id)s,"
						+ "(select u.user_no,u.phone from sys_user_info u,sys_role_info r "
						+ " where u.role_id=r.id and r.name like '%领导%'"
						+ " and u.phone is not null and u.phone!='null' and u.phone!='NULL')s1"
						+ " where s.claim_cust_mgr_id=s1.user_no",
						new Object[] { "%" + indate + "%", third_week_day });
		return records;
	}

	/**
	 * 归属客户经理（待办任务）
	 * 
	 * @return
	 */
	public List<Record> findPotentialCustsToManager() {
		String indate = BolusDate.getDateTime("yyyyMM");
		List<Record> records = Db
				.use(DEFAULT)
				.find("select s.phone,s.name,s.user_no,p.count_num,p.clas_five from "
						+ " (select c.claim_cust_mgr_id,p.clas_five,count(c.claim_cust_mgr_id) as count_num "
						+ " from pccm_cust_pool p,pccm_cust_claim c where p.id=c.cust_pool_id and p.incflg='4' and c.del_stat='0'"
						+ " and p.deptlevel='3' and p.indate like ? group by c.claim_cust_mgr_id,p.clas_five)p,"
						+ " (select u.phone,u.name,u.user_no from sys_user_info u,sys_role_info r "
						+ " where u.role_id=r.id and u.phone is not null and u.phone!='null' and u.phone!='NULL'"
						+ " and r.name like '%客户经理%')s"
						+ " where p.claim_cust_mgr_id=s.user_no",
						new Object[] { "%" + indate + "%" });
		return records;
	}

	/**
	 * 二级池待认领时间+三级池待认领时间
	 * 
	 * @return
	 */
	public int findAllWeekDay() {
		int third_week_day = Integer.parseInt(Db.use(DEFAULT).queryStr(
				"select val from gcms_param_info where key=?",
				new Object[] { "THIRD_WEEK_DAY" }));
		int second_week_day = Integer.parseInt(Db.use(DEFAULT).queryStr(
				"select val from gcms_param_info where key=?",
				new Object[] { "SECOND_WEEK_DAY" }));
		return third_week_day + second_week_day;
	}

	/**
	 * 截至当前各个机构中心支行客户池未按时分配的客户数
	 * 
	 * @return
	 */
	public List<Record> findUnclaimCustCount() {
		List<Record> records = Db
				.use(DEFAULT)
				.find("select s.orgnum,s.count_num,s1.by2 from (select orgnum,count(1) as count_num from pccm_cust_pool p where p.cussts='1'"
						+ " and p.deptlevel='3' and p.third_indate is not null and p.third_indate!='NULL' and p.third_indate!='null' "
						+ " and p.orgnum is not null and p.orgnum!='null' and p.orgnum!='NULL' group by p.orgnum "
						+ " union all "
						+ " select o.org_id as orgnum,p.count_num from (select area_code,count(1) as count_num "
						+ " from pccm_cust_pool p where p.cussts='1' and p.deptlevel='3'"
						+ " and p.third_indate is not null and p.third_indate!='NULL' and p.third_indate!='null' "
						+ " and (p.orgnum is null or p.orgnum='null' or p.orgnum='NULL') group by p.area_code)p,pccm_cust_org_are o "
						+ " where p.area_code=o.area_id)s,sys_org_info s1 where s.orgnum=s1.orgnum");
		return records;
	}

	/**
	 * 根据角色名称查找用户
	 * 
	 * @param role_name
	 * @return
	 */
	public List<Record> findUserLeaders(String role_name) {
		List<Record> records = Db
				.use(DEFAULT)
				.find("select u.user_no,u.phone from sys_user_info u,sys_role_info r "
						+ " where u.role_id=r.id and u.phone is not null and u.phone!='null' and u.phone!='NULL' "
						+ " and r.name=?", new Object[] { role_name });
		return records;
	}

	/**
	 * 找出认领后处于待营销状态的目标人员
	 * 
	 * @return
	 */
	public List<Record> findCustManagers() {
		List<Record> records = Db
				.use(DEFAULT)
				.find("select p.customername,p.claim_time,u.phone,p.id from (select p.customername,c.claim_time,c.claim_cust_mgr_id,c.id "
						+ " from pccm_cust_pool p,pccm_cust_claim c where p.id=c.cust_pool_id and c.del_stat='0' and p.deptlevel='3' "
						+ " and c.marketing_stat='0' and p.incflg<>'4' and c.claim_time is not null and c.claim_time!='null' and c.claim_time!='NULL')p,"
						+ "sys_user_info u where p.claim_cust_mgr_id=u.user_no"
						+ " and u.phone is not null and u.phone!='null' and u.phone!='NULL'");
		return records;
	}

	// 当获得天数小于0，则系统自动判定营销失败，原因为客户经理未处理
	// public void updateCustClaim(Record record) {
	// String run_date = BolusDate.getDateTime("yyyyMMdd");
	// Db.use(DEFAULT)
	// .update("update pccm_cust_claim c set (c.marketing_stat,run_date)="
	// + "(select '6','"
	// + run_date
	// +
	// "' from pccm_cust_pool p where c.cust_pool_id=p.id and p.incflg<>'4' and c.id=? and del_stat='0' and marketing_stat!='6') ",
	// new Object[] { record.getStr("id") });
	//
	// }

	/**
	 * 申诉待处理事件数量
	 * 
	 * @return
	 */
	public int findAppeal() {
		int count = Db
				.use(DEFAULT)
				.queryBigDecimal(
						"select count(1) from pccm_cust_appeal where appeal_stat='1'")
				.intValue();
		return count;
	}

	/**
	 * 计算全省相同角色人员总数
	 * 
	 * @param role_id
	 * @param timePoint
	 * @return
	 */
	public int findAllUsers(String roleName, String timePoint) {
		int count = Db
				.use(DEFAULT)
				.queryBigDecimal(
						"select count(1) from pccm_person_ladermap_info ppli,sys_user_info sui,sys_role_info sri "
								+ " where ppli.user_id=sui.user_no and sui.role_id=sri.id and sri.name like ? and ppli.time_point=?",
						new Object[] { "%" + roleName + "%", timePoint })
				.intValue();
		return count;
	}

	/**
	 * 查找机构的上月商机客户转化率，商机排名，潜在客户转化率，潜在排名
	 * 
	 * @param timePoint
	 * @return
	 */
	public List<Record> findMessageToLeader(String timePoint) {
		List<Record> records = Db
				.use(DEFAULT)
				.find("select a.latentreach,a.orgname,a.org_id,a.latent_num,b.markreach,b.mark_num from "
						+ " (select p.latentreach,p.org_id,o.orgname,row_number() over (order by p.latentreach desc) as latent_num "
						+ " from pccm_org_ladermap_info p,sys_org_info o where p.org_id=o.orgnum "
						+ " and p.time_point=? order by p.latentreach desc)a,"
						+ " (select p.markreach,p.org_id,o.orgname,row_number() over (order by p.markreach desc) as mark_num "
						+ " from pccm_org_ladermap_info p,sys_org_info o where p.org_id=o.orgnum "
						+ " and p.time_point=? order by p.markreach desc)b where a.org_id=b.org_id",
						new Object[] { timePoint, timePoint });
		return records;
	}

	/**
	 * 查找机构下该角色的客户
	 * 
	 * @param org_id
	 * @param role_name
	 * @param flag
	 * @return
	 */
	public List<Record> findLeadersFromOrg(String org_id, String role_name,
			String flag) {
		List<Record> records = new ArrayList<Record>();
		if (flag.equals("all")) {// 查询责任中心到省行的领导
			records = Db
					.use(DEFAULT)
					.find("select u.phone from sys_user_info u,sys_role_info r"
							+ " where u.role_id=r.id and u.phone is not null and u.phone!='null' "
							+ " and u.phone!='NULL' and u.org_id=? and r.name like ?"
							+ " union all"
							+ " select b.phone from (select upid from sys_org_info where orgnum=?)a,"
							+ " (select u.phone,u.org_id from sys_user_info u,sys_role_info r"
							+ " where u.role_id=r.id and u.phone is not null and u.phone!='null' "
							+ "	and u.phone!='NULL' and r.name like ?) b where a.upid=b.org_id"
							+ " union all"
							+ " select b.phone from (select upid from sys_org_info "
							+ "	where orgnum=(select upid from sys_org_info where orgnum=?))a,"
							+ " (select u.user_no,u.phone,u.org_id from sys_user_info u,sys_role_info r where"
							+ " u.role_id=r.id and u.phone is not null and u.phone!='null' "
							+ "	and u.phone!='NULL' and r.name like ?)b where a.upid=b.org_id"
							+ " union all"
							+ " select b.phone from (select upid from sys_org_info where orgnum="
							+ "(select upid from sys_org_info "
							+ "	where orgnum=(select upid from sys_org_info where orgnum=?)))a,"
							+ " (select u.user_no,u.phone,u.org_id from sys_user_info u,sys_role_info r where"
							+ " u.role_id=r.id and u.phone is not null and u.phone!='null' "
							+ "	and u.phone!='NULL' and r.name like ?)b where a.upid=b.org_id",
							new Object[] { org_id, "%" + role_name + "%",
									org_id, "%" + role_name + "%", org_id,
									"%" + role_name + "%", org_id,
									"%" + role_name + "%" });
		} else if (flag.equals("2")) {// 查询责任中心到中心支行的领导
			records = Db
					.use(DEFAULT)
					.find("select u.phone from sys_user_info u,sys_role_info r"
							+ " where u.role_id=r.id and u.phone is not null and u.phone!='null' "
							+ " and u.phone!='NULL' and u.org_id=? and r.name like ?"
							+ " union all"
							+ " select b.phone from (select upid from sys_org_info where orgnum=?)a,"
							+ " (select u.phone,u.org_id from sys_user_info u,sys_role_info r"
							+ " where u.role_id=r.id and u.phone is not null and u.phone!='null' "
							+ "	and u.phone!='NULL' and r.name like ?) b where a.upid=b.org_id",
							new Object[] { org_id, "%" + role_name + "%",
									org_id, "%" + role_name + "%" });
		}
		return records;
	}

	/**
	 * 查找上月全省机构总数
	 * 
	 * @param timePoint
	 * @return
	 */
	public int findCountOrgs(String timePoint) {
		int count = Db
				.use(DEFAULT)
				.queryBigDecimal(
						"select count(1) from sys_org_info o,pccm_org_ladermap_info p where o.orgnum=p.org_id and p.time_point=?",
						new Object[] { timePoint }).intValue();
		return count;
	}

	/**
	 * 根据角色名称查询人员
	 * 
	 * @param roleName
	 * @return
	 */
	public List<Record> findClaimUsers(String roleName) {
		String indate = BolusDate.getDateTime("yyyyMM");
		List<Record> records = Db
				.use(DEFAULT)
				.find("select u.user_no,u.phone,r.name,r.id from (select u.user_no,u.phone,u.role_id from "
						+ " (select claim_cust_mgr_id as user_no from pccm_cust_claim where del_stat='0' and claim_time like ?)s,"
						+ "sys_user_info u where s.user_no=u.user_no"
						+ " and u.phone is not null and u.phone!='null' and u.phone!='NULL')u,"
						+ "sys_role_info r where u.role_id=r.id and r.name like ?",
						new Object[] { "%" + indate + "%", "%" + roleName + "%" });
		return records;
	}

	/**
	 * 根据机构号、角色名称、机构级别查询用户
	 * 
	 * @param orgnum
	 * @param role_name
	 * @param deptlevel
	 * @return
	 */
	public List<Record> findUsersByOrgAndRole(String orgnum, String role_name,
			int deptlevel) {
		List<Record> records = new ArrayList<Record>();
		if (deptlevel == 1) {
			records = Db
					.use(DEFAULT)
					.find("select b.phone from "
							+ "(select upid from sys_org_info where orgnum=(select upid from sys_org_info s where s.orgnum=?))a,"
							+ "(select u.user_no,u.phone,u.org_id from sys_user_info u,sys_role_info r "
							+ " where u.role_id=r.id and u.phone is not null and u.phone!='null' "
							+ " and u.phone!='NULL' and r.name=?)b where a.upid=b.org_id",
							new Object[] { orgnum, role_name });
		} else if (deptlevel == 2) {
			records = Db
					.use(DEFAULT)
					.find("select b.phone from "
							+ "(select upid from sys_org_info s where s.orgnum=?)a,"
							+ "(select u.user_no,u.phone,u.org_id from sys_user_info u,sys_role_info r "
							+ " where u.role_id=r.id and u.phone is not null and u.phone!='null' "
							+ " and u.phone!='NULL' and r.name=?)b where a.upid=b.org_id",
							new Object[] { orgnum, role_name });
		} else if (deptlevel == 3) {
			records = Db
					.use(DEFAULT)
					.find("select u.phone from sys_user_info u,sys_role_info r "
							+ " where u.role_id=r.id and u.phone is not null and u.phone!='null' "
							+ " and u.phone!='NULL' and u.org_id=? and r.name=?",
							new Object[] { orgnum, role_name });
		}
		return records;
	}

	/**
	 * 判断开户是否营销成功
	 */
	public void updateCustByOpen() {
		String run_date = BolusDate.getDateTime("yyyyMMdd");// 跑批日期
		String data_date = findBaseInfoMaxDate();// 查询宽表最新数据日期
		// 营销过程中开户，一旦开户信息被认领，系统校验，认领人与商机认领人一致，自动判定营销成功
		Db.use(GBASE)
				.update("update ap_pccm.pccm_cust_claim c,(select b.id from "
						+ "(select name from ap_pccm.pccm_cust_base_info where data_date=?)a,"
						+ "(select p.customername,c.id from ap_pccm.pccm_cust_claim c,ap_pccm.pccm_cust_pool p "
						+ " where p.id=c.cust_pool_id and p.deptlevel='3' and p.cust_manager=c.claim_cust_mgr_id "
						+ " and (c.marketing_stat='0' or c.marketing_stat='2')"
						+ " and c.del_stat='0' and p.incflg<>'4')b where a.name=b.customername)c1 "
						+ " set c.marketing_stat='1',c.run_date=? where c.id=c1.id",
						new Object[] { data_date, run_date });
		// 营销过程中开户，一旦开户信息被认领，系统校验，认领人与商机认领人不一致，自动判定营销终止
		Db.use(GBASE)
				.update("update ap_pccm.pccm_cust_claim c,(select b.id from "
						+ "(select name from ap_pccm.pccm_cust_base_info where data_date=?)a,"
						+ "(select p.customername,c.id from ap_pccm.pccm_cust_claim c,ap_pccm.pccm_cust_pool p "
						+ " where p.id=c.cust_pool_id and p.deptlevel='3' and p.cust_manager<>c.claim_cust_mgr_id "
						+ " and (c.marketing_stat='0' or c.marketing_stat='2')"
						+ " and c.del_stat='0' and p.incflg<>'4')b where a.name=b.customername)c1 "
						+ " set c.marketing_stat='3',c.run_date=? where c.id=c1.id",
						new Object[] { data_date, run_date });
		// 吐到oracle数据（自动判定营销成功、自动判定营销终止的）
		List<Record> records = Db
				.use(GBASE)
				.find("select c.* from ap_pccm.pccm_cust_pool p,ap_pccm.pccm_cust_claim c where p.id=c.cust_pool_id "
						+ " and (c.marketing_stat='1' or c.marketing_stat='3') and c.del_stat='0' "
						+ " and c.run_date=? and p.incflg<>'4'",
						new Object[] { run_date });
		// 遍历gbase数据
		if (records != null && !records.isEmpty()) {
			for (Record record : records) {
				Record r = Db
						.use(DEFAULT)
						.findFirst(
								"select c.* from pccm_cust_pool p,pccm_cust_claim c where p.id=c.cust_pool_id "
										+ " and p.deptlevel='3' and c.id=? and p.incflg<>'4'",
								new Object[] { record.getStr("id") });
				if (r != null) {
					String marketing_stat = r.getStr("marketing_stat");
					// 6代表客户超过待营销天数，系统自动判定客户经理未处理，所以不能更新
					if (!"6".equals(marketing_stat)) {
						Db.use(DEFAULT)
								.update("update pccm_cust_claim set marketing_stat=? where id=? and incflg<>'4'",
										new Object[] {
												record.getStr("marketing_stat"),
												record.getStr("id") });
					}
				} else {
					Db.use(DEFAULT)
							.update("insert into pccm_cust_claim(id,cust_no,claim_prop,claim_cust_mgr_id,claim_cust_mgr_name,"
									+ "claim_time,del_stat,del_time,cust_pool_id,marketing_stat,creat_id,creat_name,run_date,incflg) "
									+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
									new Object[] {
											record.getStr("id"),
											record.getStr("cust_no"),
											record.getStr("claim_prop"),
											record.getStr("claim_cust_mgr_id"),
											record.getStr("claim_cust_mgr_name"),
											record.getStr("claim_time"),
											record.getStr("del_stat"),
											record.getStr("del_time"),
											record.getStr("cust_pool_id"),
											record.getStr("marketing_stat"),
											record.getStr("creat_id"),
											record.getStr("creat_name"),
											record.getStr("run_date"),
											record.getStr("incflg") });
				}
			}
		}
		// 超过待营销天数，则判定客户经理未处理
		int cust_task_limit = Integer.parseInt(Db.use(DEFAULT).queryStr(
				"select val from gcms_param_info where key=?",
				new Object[] { "CUST_TASK_LIMIT" }));
		Db.use(DEFAULT)
				.update("update pccm_cust_claim c set (c.marketing_stat,run_date)=(select '6','"
						+ run_date
						+ "' "
						+ " from pccm_cust_pool p where c.cust_pool_id=p.id and p.deptlevel='3' and c.del_stat='0' "
						+ " and (c.marketing_stat<>'0' or c.marketing_stat<>'2') and p.incflg<>'4'"
						+ " and ceil((to_date(to_char(sysdate,'yyyymmdd'),'yyyymmdd')-to_date(substr(c.claim_time,1,8),'yyyymmdd')))>?)",
						new Object[] { cust_task_limit });
	}

	/**
	 * 查找宽表最新日期
	 * 
	 * @return
	 */
	private String findBaseInfoMaxDate() {
		String sql = "select to_char(max(data_date)) as data_date from ap_pccm.pccm_cust_base_info";
		Record r = Db.use(GBASE).findFirst(sql);
		if (null != r) {
			return r.getStr("data_date");
		} else {
			// 如果没有则返回当前时间
			return DateTimeUtil.getPathName();
		}
	}

	/**
	 * 本月人员的潜在客户达成率及排名，商机客户转化率及排名
	 * 
	 * @param timePoint
	 * @param roleName
	 * @return
	 */
	public List<Record> findAllPersonLatentMark(String timePoint,
			String roleName) {
		String sql = "select a.latentreach,a.phone,a.num as latent_rank, b.markreach, b.num as mark_rank"
				+ " from (select p.user_id,p.phone, p.latentreach, p.num"
				+ " from (select ppli.user_id,sui.phone,"
				+ "ppli.latentreach,"
				+ "row_number() over(order by ppli.latentreach desc) as num"
				+ " from pccm_person_ladermap_info ppli, sys_user_info sui,sys_role_info sri"
				+ " where ppli.user_id = sui.user_no and sui.role_id=sri.id and sri.name like ? and ppli.time_point=?) p) a,"
				+ "(select p.user_id,p.phone,p.markreach, p.num"
				+ " from (select ppli.user_id,sui.phone,"
				+ "ppli.markreach,"
				+ "row_number() over(order by ppli.markreach desc) as num"
				+ " from pccm_person_ladermap_info ppli, sys_user_info sui,sys_role_info sri"
				+ " where ppli.user_id = sui.user_no and sui.role_id=sri.id and sri.name like ? and ppli.time_point=?) p) b"
				+ " where a.user_id = b.user_id";
		List<Record> records = Db.use(DEFAULT).find(sql, "%" + roleName + "%",
				timePoint, "%" + roleName + "%", timePoint);
		return records;
	}
}
