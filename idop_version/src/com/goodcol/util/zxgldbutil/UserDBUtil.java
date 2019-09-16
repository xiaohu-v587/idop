package com.goodcol.util.zxgldbutil;

import java.util.List;

import com.goodcol.core.plugin.activerecord.Db;
import com.goodcol.core.plugin.activerecord.Record;
import com.goodcol.util.AppUtils;
import com.goodcol.util.date.BolusDate;

public class UserDBUtil {
	private static final String DEFAULT = "default";

	/**
	 * 查询用户角色 2018年5月3日14:38:41
	 * 
	 * @author liutao
	 */
	public List<Record> userRoleList(String id) {
		String sql = "select ri.id as id, ri.name as name from sys_user_role ur "
				+ "left join sys_role_info ri on ur.role_id = ri.id "
				+ "where ur.user_id = ?";
		List<Record> records = Db.find(sql, id);
		return records;
	}

	/**
	 * 查询待分配角色 2018年5月3日14:38:51
	 * 
	 * @author liutao
	 */
	public List<Record> distributeRoleList(String id) {
		String sql = "select ri.id as id, ri.name as name from sys_role_info ri "
				+ "where ri.id not in (select ur.role_id "
				+ "from sys_user_role ur where ur.user_id = ?) and ri.role_dele_flag != '0'";
		List<Record> records = Db.find(sql, id);
		return records;
	}

	/**
	 * 更新用户角色
	 * 
	 * @param uid
	 *            员工id
	 * @param roleIds
	 *            多个角色id
	 */
	public boolean updateUserRole(String uid, String roleIds) throws Exception {
		// 删除之前的员工角色
		String delSql = "delete from sys_user_role where user_id = ?";
		// 重新添加选择的角色
		String updSql = "insert all ";
		String[] roleIdArr = roleIds.split(",");
		for (String roleId : roleIdArr) {
			updSql += "into sys_user_role(user_id, role_id) values ('" + uid
					+ "', '" + roleId + "') ";
		}
		updSql += " select 1 from dual ";
		Db.update(delSql, uid);
		if (roleIdArr.length > 0) {
			int insert = Db.update(updSql);
			if (insert > 0) {
				// 查询选择的角色中是否有KPI身份角色，如果有则设置其中一个到身份表中保存
				String kpiRoleSql = "select sri.kpi_flag from sys_role_info sri where sri.id = ? "
						+ "and sri.kpi_flag is not null";
				for (String roleId : roleIdArr) {
					roleId = roleId.replaceAll("'", "");
					Record r = Db.use(DEFAULT).findFirst(kpiRoleSql, roleId);
					if (null != r) {
						// String kpiFlag = r.getStr("kpi_flag");
						// 设置身份，不考虑是否选择了其他的KPI身份，检测到之后直接添加，其他的不管，然后break循环
						// 查询KPI身份角色对应字典表中的身份
						/*
						 * kpiRoleSql =
						 * "select id from gcms_param_info gpi where gpi.key = 'role' and gpi.val = ?"
						 * ; r = Db.use(DEFAULT).findFirst(kpiRoleSql, kpiFlag);
						 */
						// 身份角色现在不使用gcms_param_info这个表进行查询，直接拿着角色表id进行保存即可

						// 查询出身份之后进行新增，首先删除以前的身份然后再添加
						// String kpiRoleDelSql =
						// "delete gcms_role_apply where user_id = ?";
						// Db.use(DEFAULT).update(kpiRoleDelSql, uid);
						String findSql = "select count(1) as num from gcms_role_apply where user_id = ? ";
						r = Db.findFirst(findSql, uid);
						if (r != null && r.getBigDecimal("num").intValue() > 0) {
							// 执行修改
							updSql = "update gcms_role_apply set role_id = ? where user_id = ? ";
							Db.update(updSql, roleId, uid);
						} else {
							// 重新新增
							String id = AppUtils.getStringSeq();
							// String kpiRoleId = r.getStr("id");
							String kpiRoleInsertSql = "insert into gcms_role_apply(id, user_id, role_id) values(?, ?, ?)";
							Db.use(DEFAULT).update(kpiRoleInsertSql, id, uid,
									roleId);
							break;
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 保存当前用户的默认角色 2018年7月25日14:40:35
	 * 
	 * @author liutao
	 * @param userId
	 *            用户id
	 * @param roleId
	 *            角色id
	 * @return
	 */
	public boolean saveDefaultRole(String userId, String roleId) {
		String sql = "update sys_user_info set role_id = ? where id = ? ";
		int update = Db.use(DEFAULT).update(sql, roleId, userId);
		if (update > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询用户角色信息 2018年7月25日15:10:22
	 * 
	 * @author liutao
	 * @param uId
	 * @return
	 */
	public Record getUserRoleInfo(String uId) {
		String sql = "select sui.id as user_id, sri.name as role_name from sys_user_info sui "
				+ "left join sys_role_info sri on sri.id = sui.role_id where sui.id = ?";
		return Db.use(DEFAULT).findFirst(sql, uId);
	}

	/**
	 * 查找中心支行领导
	 * 
	 * @return
	 */
	public List<Record> findSubLeaders() {
		String sql = "select u.* from sys_user_info u,sys_role_info r "
				+ " where u.role_id=r.id and r.name='领导-中心支行'";
		return Db.use(DEFAULT).find(sql);
	}

	/**
	 * 在三级池超期未认领的客户
	 * 
	 * @return
	 */
	public List<Record> findCustByOver() {
		// 查找待认领客户中心支行客户池停留时间参数标准
		int third_week_day = Integer.parseInt(Db.use(DEFAULT).queryStr(
				"select val from gcms_param_info where key=?",
				new Object[] { "THIRD_WEEK_DAY" }));
		List<Record> records = Db
				.use(DEFAULT)
				.find("select * from pccm_cust_pool where third_indate is not null "
						+ " and third_indate!='null' and third_indate!='NULL' and cussts='1' and (incflg='1' or incflg='2' or incflg='3')"
						+ " and ceil((to_date(to_char(sysdate,'yyyymmdd'),'yyyymmdd')-to_date(third_indate,'yyyymmdd')))>?",
						new Object[] { third_week_day });
		return records;
	}

	/**
	 * 取中心支行领导,该客户推送给中心支行领导
	 * 
	 * @param record
	 * @param leader
	 */
	public void saveCustClaim(Record record, Record leader) {
		String dummy_cust_no = record.getStr("dummy_cust_no");
		List<Record> records = Db.use(DEFAULT).find(
				"select * from pccm_cust_claim where cust_no=? "
						+ " and claim_cust_mgr_id=? and incflg!='4'",
				new Object[] { dummy_cust_no, leader.getStr("user_no") });
		if (records == null || records.isEmpty()) {
			Db.use(DEFAULT)
					.update("insert into pccm_cust_claim values(id,cust_no,claim_prop,claim_cust_mgr_id,claim_cust_mgr_name,"
							+ "claim_time,cust_pool_id,incflg) values(?,?,?,?,?,?,?,?)",
							new Object[] { dummy_cust_no, dummy_cust_no, "100",
									leader.getStr("user_no"),
									leader.getStr("name"),
									BolusDate.getDateTime("yyyyMMddHHmmss"),
									record.getStr("id"),
									record.getStr("incflg") });
			Db.use(DEFAULT).update(
					"update pccm_cust_pool set cussts='3' where id=?",
					new Object[] { record.getStr("id") });
		}
	}

	/**
	 * 分行超期的入支行池
	 */
	public void updateThirdPool() {
		// 查找待认领客户分行客户池停留时间参数标准
		int second_week_day = Integer.parseInt(Db.use(DEFAULT).queryStr(
				"select val from gcms_param_info where key=?",
				new Object[] { "SECOND_WEEK_DAY" }));
		Db.use(DEFAULT)
				.update("update pccm_cust_pool set third_indate=? where cussts='1' and id in "
						+ " (select id from pccm_cust_pool where (third_indate is null or third_indate='null' or third_indate='NULL') "
						+ " and (incflg='1' or incflg='2' or incflg='3') and ceil((to_date(to_char(sysdate,'yyyymmdd'),'yyyymmdd')-to_date(indate,'yyyymmdd')))>?)",
						new Object[] { BolusDate.getDateTime("yyyyMMdd"),
								second_week_day });

	}
}
